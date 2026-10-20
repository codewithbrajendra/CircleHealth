package com.circles.health.doctor.serviceImpl;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.circles.health.doctor.entity.PatientBookingDetail;
import com.circles.health.doctor.entity.TherapistDetail;
import com.circles.health.doctor.repo.PatientBookingRepository;
import com.circles.health.doctor.repo.TherapistRepository;
import com.circles.health.doctor.request.EditTherapistNameRequest;
import com.circles.health.doctor.request.UserAnalyticsReportingQueryData;
import com.circles.health.doctor.request.WebHookRequest;
import com.circles.health.doctor.response.AddEditTherapistResponse;
import com.circles.health.doctor.response.AnalyticsReportingQueryData;
import com.circles.health.doctor.response.TherapistNamesResponse;
import com.circles.health.doctor.response.UserAnalyticsBookingDetails;
import com.circles.health.doctor.service.TherapistService;
import com.circles.health.doctor.util.JsonUtil;
import com.circles.health.doctor.util.RestTemplateUtil;

@Service
@Transactional
public class TherapistServiceImpl implements TherapistService{

	private static final Logger LOGGER = LoggerFactory.getLogger(TherapistServiceImpl.class);
	
	@Autowired
	private TherapistRepository therapistRepository;
	
	@Autowired
	private PatientBookingRepository patientBookingRepository;
	
	@Value("${patient.service.base.api.url}")
    String patientServiceUrl;
	
	@Value("${api.calendly.user.me}")
    String apiUserMeCalendlyUrl;
	
	@Value("${api.calendly.webhook.subcription.url}")
    String apiWebHookSubcriptionUrl;
	
	@Autowired
    RestTemplate restTemplate;
	
	@Override
	public TherapistDetail addTherapist(TherapistDetail therapistDetail) {
		therapistDetail.setTherapistName(therapistDetail.getTherapistFirstName()+" "+therapistDetail.getTherapistLastName());
		TherapistDetail savedTherapistDetail = this.therapistRepository.save(therapistDetail);
		return savedTherapistDetail;
	}

	@Override
	public AddEditTherapistResponse therapsitToTherapistResponse(TherapistDetail therapistDetail) {
		AddEditTherapistResponse addEditTherapistResponse = new AddEditTherapistResponse();
			addEditTherapistResponse.setTherapistId(therapistDetail.getTherapistId());
			addEditTherapistResponse.setTherapistFirstName(therapistDetail.getTherapistFirstName());
			addEditTherapistResponse.setTherapistLastName(therapistDetail.getTherapistLastName());
			addEditTherapistResponse.setSchedulingUrl(therapistDetail.getSchedulingUrl());
			addEditTherapistResponse.setEmail(therapistDetail.getEmail());
			addEditTherapistResponse.setBearerToken(therapistDetail.getBearerToken());
			addEditTherapistResponse.setApiKey(therapistDetail.getApiKey());
	    return addEditTherapistResponse;
	}


	@Override
	public List<TherapistDetail> getTherapists() {
		LOGGER.info("Inside getTherapists service method.");
		return this.therapistRepository.findAll();
	}


	@Override
	public AddEditTherapistResponse editTherapist(TherapistDetail therapistDetail) {
		TherapistDetail therapistDetailToBeEdited = this.therapistRepository.findByTherapistId(therapistDetail.getTherapistId());
		therapistDetailToBeEdited.setTherapistFirstName(therapistDetail.getTherapistFirstName());
		therapistDetailToBeEdited.setTherapistLastName(therapistDetail.getTherapistLastName());
		therapistDetailToBeEdited.setEmail(therapistDetail.getEmail());
		therapistDetailToBeEdited.setSchedulingUrl(therapistDetail.getSchedulingUrl());
		therapistDetailToBeEdited.setBearerToken(therapistDetail.getBearerToken());
		therapistDetailToBeEdited.setApiKey(therapistDetail.getApiKey());
		therapistDetailToBeEdited.setTherapistName(therapistDetail.getTherapistFirstName()+" "+therapistDetail.getTherapistLastName());
		TherapistDetail savedtherapistDetail = (TherapistDetail) this.therapistRepository.save(therapistDetailToBeEdited);
		
		EditTherapistNameRequest editTherapistNameRequest = new EditTherapistNameRequest();
		editTherapistNameRequest.setTherapistId(therapistDetail.getTherapistId());
		editTherapistNameRequest.setTherapistName(therapistDetailToBeEdited.getTherapistName());
		editTherapistNameRequest.setUserId(therapistDetail.getUserId());
		
		try {
			String result = this.sendEditNameRequestToPatientData(editTherapistNameRequest);
			LOGGER.info("Therpaist name edited result from Patient Service ::: "+result);
		} catch (Exception e) {
			LOGGER.info("Some exception occur from Patient Service while editing therapist name ::: "+e.getMessage());
			e.printStackTrace();
		}
		
		AddEditTherapistResponse addEditTherapistResponse = this.therapsitToTherapistResponse(savedtherapistDetail);
		
		return addEditTherapistResponse;
	}


	@Override
	public String deleteTherapist(Long therapistId) {
		this.therapistRepository.deleteById(therapistId);
		return "Therapist deleted successfully.";
	}


	@Override
	public List<TherapistNamesResponse> getTherapistsName() {
		List<TherapistDetail> therapists = this.therapistRepository.findAll();
		List<TherapistNamesResponse> therapistsResponse = new ArrayList<>();
		
		for(TherapistDetail therapistDetail : therapists) {
			TherapistNamesResponse therapistNamesResponse = new TherapistNamesResponse();
			therapistNamesResponse.setTherapistId(therapistDetail.getTherapistId());
			therapistNamesResponse.setTherapistName(therapistDetail.getTherapistFirstName()+" "+therapistDetail.getTherapistLastName());
			therapistsResponse.add(therapistNamesResponse);
		}
		
		
		return therapistsResponse;
	}


	@Override
	public TherapistDetail getTherapistDetailById(Long therapistId) {
		return this.therapistRepository.findByTherapistId(therapistId);
	}


	@Override
	public PatientBookingDetail getPatientBookingDetailByEmail(String email) {
		return this.patientBookingRepository.findByEmail(email);
	}
	
	private String sendEditNameRequestToPatientData(EditTherapistNameRequest editTherapistNameRequest)
			throws Exception {

		String o = RestTemplateUtil.postMethodForTherapistName(patientServiceUrl + "/editTherapistName",editTherapistNameRequest, restTemplate);

		return o;
	}


	@Override
	public PatientBookingDetail savePatientBookingDetail(PatientBookingDetail patientBookingDetail) {
		LOGGER.info("patientBookingDetail to be save ::: "+patientBookingDetail);
		return this.patientBookingRepository.save(patientBookingDetail);
	}


	@Override
	public Object getScheduledEventData(String eventUrl, String token) {
		LOGGER.info("Inside getScheduledEventData method.");
		//String token = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2F1dGguY2FsZW5kbHkuY29tIiwiaWF0IjoxNjM5NDg5NjkzLCJqdGkiOiIzZDRjNmVmMi05MDliLTRmODctOGU4NC1kYzlhODY5ZjEzOGYiLCJ1c2VyX3V1aWQiOiJCREJCUkxZNVFJUVVaVFA1In0.LNZ8a2eidlGzPZvBACQFPCt8x06ClrNdDUVHQ94UpDE";
		Object obj = RestTemplateUtil.getMethod(eventUrl, token, restTemplate);
		return obj;
	}


	@Override
	public Long getTherapistIdByEmail(String email) {
		String url = patientServiceUrl + "/getTherapistIdByEmail/"+email;
		return RestTemplateUtil.getMethodForTherapistId(url, restTemplate);
	}


	@Override
	public String getTokenByTherapistId(Long therapistId) {
		LOGGER.info("getTokenByTherapistId method therapistId ::: "+therapistId);
		return this.therapistRepository.getTokenByTherapistId(therapistId);
	}


	@Override
	public AnalyticsReportingQueryData fetchUniqueUserCountAndTotalBooking(AnalyticsReportingQueryData analyticsReportingQueryData) {
		try {
			if (analyticsReportingQueryData.getStartDate() != null && analyticsReportingQueryData.getEndDate() != null) {
				Long totalBooking =  patientBookingRepository.bookingCountByDateRange(analyticsReportingQueryData.getStartDate(), 
						analyticsReportingQueryData.getEndDate()),
					 distinctUserCount = patientBookingRepository.distinctUserCountForBooking(analyticsReportingQueryData.getStartDate(), 
								analyticsReportingQueryData.getEndDate());
				analyticsReportingQueryData.setTotalBooking(totalBooking);
				analyticsReportingQueryData.setDistinctUser(distinctUserCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return analyticsReportingQueryData;
	}


	@Override
	public PatientBookingDetail getPatientBookingDetailByUserIdAndStatus(Long userId) {
		LOGGER.info("Inside getPatientBookingDetailByUserIdAndStatus method. User Id ::: "+userId);
		PatientBookingDetail patientBookingDetail = this.patientBookingRepository.findByUserIdAndStatusOrderByIdDesc(userId);
		
		return patientBookingDetail;
	}


	@Override
	public List<PatientBookingDetail> findByUserId(Long userId) {
		LOGGER.info("Inside getPatientBookingDetailByUserIdAndStatus method. User Id ::: "+userId);
		
		return this.patientBookingRepository.findByUserId(userId);
	}


	@Override
	public Object getUserAndOrganizationUrl(String token) {
		LOGGER.info("Inside getUserAndOrganizationUrl method.");
		Object obj = RestTemplateUtil.getMethod(apiUserMeCalendlyUrl, token, restTemplate);
		return obj;
	}


	@Override
	public Object setCalendlyWebHook(WebHookRequest webHookRequest,String token) {
		LOGGER.info("Inside setCalendlyWebHook method in TherapistServiceImpl.");
		Object object = null;
		try {
			String jsonObject = JsonUtil.convertObjectToString(webHookRequest);
			 object = RestTemplateUtil.postMethod(apiWebHookSubcriptionUrl, token, jsonObject, restTemplate);
			 LOGGER.info("Response from calendly webhook RestTemplate ::: "+object);
		} catch (Exception e) {
			LOGGER.info("Error occured while creating calendly webhook ::: "+e.getMessage());
			e.printStackTrace();
		}
		return object;
	}

	@Override
	public TherapistDetail saveTherapistDetail(TherapistDetail therapistDetail) {
		TherapistDetail savedTherapistDetail = this.therapistRepository.save(therapistDetail);
		return savedTherapistDetail;
	}

	@Override
	public void deleteMethodForCalendlyWebHookSubscription(String url, String token) {
		LOGGER.info("Inside deleteMethodForCalendlyWebHookSubscription in TherapistServiceImpl. url ::: "+url);
		RestTemplateUtil.deleteMethodForCalendlyWebHookSubscription(url, token, restTemplate);
	}

	@Override
	public AnalyticsReportingQueryData fetchUserTotalBookingBetweenDateRange(AnalyticsReportingQueryData analyticsReportingQueryData) {
		try {
			if (analyticsReportingQueryData.getStartDate() != null && analyticsReportingQueryData.getEndDate() != null) {
				Long totalBooking =  patientBookingRepository.bookingCountByUserIdAndDateRange(analyticsReportingQueryData.getUserId(), 
						analyticsReportingQueryData.getStartDate(), analyticsReportingQueryData.getEndDate());
				analyticsReportingQueryData.setTotalBooking(totalBooking);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return analyticsReportingQueryData;
	}

	@Override
	public List<UserAnalyticsBookingDetails> fetchSingleUserTotalBookingBetweenDateRange(
			UserAnalyticsReportingQueryData userAnalyticsReportingQueryData) {
		List<PatientBookingDetail> userPatientBookingDetails=null;
		List<UserAnalyticsBookingDetails>  userAnalyticsBookingDetails=null;
		try {
			if (userAnalyticsReportingQueryData.getStartDate() != null && userAnalyticsReportingQueryData.getEndDate() != null) {
				userPatientBookingDetails=patientBookingRepository.findByUserIdAndBookingDateRange(userAnalyticsReportingQueryData.getUserId(),
						userAnalyticsReportingQueryData.getStartDate(), userAnalyticsReportingQueryData.getEndDate());
				LOGGER.info("userPatientBookingDetails response from repository isEmpty check ::: "+userPatientBookingDetails.isEmpty());
				if(!userPatientBookingDetails.isEmpty()) {
					
					userAnalyticsBookingDetails=new ArrayList<>();
					for(PatientBookingDetail detail: userPatientBookingDetails){
						UserAnalyticsBookingDetails userAnalyticsBookingDetail=new UserAnalyticsBookingDetails();
						userAnalyticsBookingDetail.setUserId(detail.getUserId());
						userAnalyticsBookingDetail.setBookingTimeDuration(detail.getBookingStartTime()+" - "+detail.getBookingEndTime());
						userAnalyticsBookingDetail.setBookingStatus(detail.getStatus());
						userAnalyticsBookingDetail.setBookingDate(detail.getBookingDate());
						userAnalyticsBookingDetails.add(userAnalyticsBookingDetail);
					}
					
				}
			
			}
			

		} catch (Exception e) {
			LOGGER.info("Some Exception occur inside fetchSingleUserTotalBookingBetweenDateRange method of TherapistServiceImpl ::: "+e.getMessage());
		}
		return userAnalyticsBookingDetails;
	}
}
