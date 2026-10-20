package com.circles.health.doctor.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.circles.health.doctor.entity.PatientBookingDetail;
import com.circles.health.doctor.entity.TherapistDetail;
import com.circles.health.doctor.request.UserAnalyticsReportingQueryData;
import com.circles.health.doctor.request.WebHookRequest;
import com.circles.health.doctor.response.AddEditTherapistResponse;
import com.circles.health.doctor.response.AdminCalendlyResponse;
import com.circles.health.doctor.response.AnalyticsReportingQueryData;
import com.circles.health.doctor.response.TherapistNamesResponse;
import com.circles.health.doctor.response.UserAnalyticsBookingDetails;
import com.circles.health.doctor.response.UserAnalyticsBookingDetailsList;
import com.circles.health.doctor.service.TherapistService;
import com.circles.health.doctor.util.DateUtil;
import com.circles.health.doctor.utils.constants.EndPoints;
import com.circles.health.doctor.utils.constants.ResponseMessage;
import com.circles.health.doctor.utils.enums.ResponseStatus;
import com.circles.health.doctor.wrapper.BaseResponse;

@RestController
@RequestMapping(EndPoints.BASE_PATH + "/doctor")
public class DoctorController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DoctorController.class);

	@Autowired
	private TherapistService therapistService;
	
	@Value("${api.calendly.webhook.callback.url}")
    String webHookCallBackUrl;
	
	@GetMapping("/callCalendlyWebHook")
	public Object calendlyWebHook() {
		
		return null;
	}
	
	@PostMapping("/getCalendlyEventWebHookData")
	public Object getCalendlyEventWebHookData(@RequestBody Object object) {
		LOGGER.info("getCalendlyEventWebHookData get from request ::: "+object.toString());
		
		PatientBookingDetail patientBookingDetail = new PatientBookingDetail();
		String[] array = object.toString().split(",");
		String eventCreatedOrCanceled = array[1].substring(array[1].indexOf("event=invitee.")+14); 
		System.out.println("eventCreatedOrCanceled ::: "+eventCreatedOrCanceled);
		String payload = object.toString().substring(object.toString().indexOf("cancel_url"));
		String removeBraces = payload.replace("}", "");
		
		String[] payloadValues = removeBraces.split(",");
		String email = null;
		String event = null;
		String cancelUrl = null;
		String name = null;
		String answer = null;
		String status = null;
		String uri = null;
		String reason = null;
		Long userId = null;
		LocalDateTime createdAt = null;
		LocalDateTime updatedAt = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		for(String payloadValue : payloadValues) {
			System.out.println("value ::: "+payloadValue);
			if(payloadValue.contains("cancel_url=")) {
				cancelUrl = payloadValue.substring(payloadValue.lastIndexOf("=")+1);
			}
			if(payloadValue.contains("email=")) {
				email = payloadValue.substring(payloadValue.lastIndexOf("=")+1);
			}
			if(payloadValue.contains("event=")) {
				event = payloadValue.substring(payloadValue.lastIndexOf("=")+1);
			}
			if(payloadValue.contains("name=")) {
				name = payloadValue.substring(payloadValue.lastIndexOf("=")+1);
			}
			if(payloadValue.contains("questions_and_answers=")) {
			 answer = payloadValue.substring(payloadValue.lastIndexOf("r=")+2);
			 if(answer.equals("questions_and_answers=[]")) {
				 answer = "";
			 }
			}
			if(payloadValue.contains("status=")) {
				status = payloadValue.substring(payloadValue.lastIndexOf("=")+1);
			}
			if(payloadValue.contains("uri=")) {
				uri = payloadValue.substring(payloadValue.lastIndexOf("=")+1);
			}
			if(payloadValue.contains("reason=")) {
				reason = payloadValue.substring(payloadValue.lastIndexOf("=")+1);
			}
			if(payloadValue.contains("tracking={utm_campaign=")) {
				if(!payloadValue.substring(payloadValue.lastIndexOf("=")+1).equals("null")) {
				userId = Long.parseLong(payloadValue.substring(payloadValue.lastIndexOf("=")+1));
				}
			}
			if(payloadValue.contains("created_at=")) {
				String removeT = payloadValue.substring(payloadValue.lastIndexOf("=")+1).replace("T", " ");
				String finalString = removeT.replace("Z", "");
				createdAt = LocalDateTime.parse(finalString.substring(0,finalString.lastIndexOf(".")),formatter); 
			}
			if(payloadValue.contains("updated_at=")) {
				String removeT = payloadValue.substring(payloadValue.lastIndexOf("=")+1).replace("T", " ");
				String finalString = removeT.replace("Z", "");
				updatedAt = LocalDateTime.parse(finalString.substring(0,finalString.lastIndexOf(".")),formatter); 
			}
		}
		LOGGER.info("eventCreatedOrCanceled ::: "+eventCreatedOrCanceled+", "+"email ::: "+email+", "+"event ::: "+event
				+"cancelUrl ::: "+cancelUrl+", "+"name ::: "+name+", "+"answer ::: "+answer+", "+"status ::: "+status+", "+"uri ::: "+uri+
				", "+"createdAt ::: "+createdAt+", "+"updatedAt ::: "+updatedAt+", "+"reason ::: "+reason+", "+"userId ::: "+userId);
		
		patientBookingDetail.setEvent(eventCreatedOrCanceled);
		patientBookingDetail.setEventUrl(event);
		patientBookingDetail.setCancelUrl(cancelUrl);
		patientBookingDetail.setEmail(email);
		patientBookingDetail.setName(name);
		patientBookingDetail.setNote(answer);
		patientBookingDetail.setStatus(status);
		patientBookingDetail.setUri(uri);
		patientBookingDetail.setCancelReason(reason);
		patientBookingDetail.setUserId(userId);
		patientBookingDetail.setUpdatedAt(updatedAt);
		patientBookingDetail.setCreatedAt(createdAt);
		if("created".equals(eventCreatedOrCanceled)) {
			patientBookingDetail.setIsCalendlyBooking(Boolean.TRUE);
		} else {
			patientBookingDetail.setIsCalendlyBooking(Boolean.FALSE);
		}
		
		if(status.equals("canceled")) {
			PatientBookingDetail savedPatientBookingDetail = this.therapistService.getPatientBookingDetailByUserIdAndStatus(userId);
			savedPatientBookingDetail.setStatus(status);
			savedPatientBookingDetail.setCreatedAt(createdAt);
			savedPatientBookingDetail.setUpdatedAt(updatedAt);
			savedPatientBookingDetail.setEvent(eventCreatedOrCanceled);
			savedPatientBookingDetail.setCancelReason(reason);
			
			PatientBookingDetail updatedPatientBookingDetail = this.therapistService.savePatientBookingDetail(savedPatientBookingDetail);
			LOGGER.info("updatedPatientBookingDetail Id ::: "+updatedPatientBookingDetail.getPatientBookingId());
		} else {
		
		Long therapistId = this.therapistService.getTherapistIdByEmail(email);
		LOGGER.info("getCalendlyEventWebHookData response from therapistId ::: "+therapistId);
		
		String token = this.therapistService.getTokenByTherapistId(therapistId);
		LOGGER.info("getCalendlyEventWebHookData response from token ::: "+token);
		
		Object eventObject = this.therapistService.getScheduledEventData(event,token);
		LOGGER.info("eventObject from event scheduled ::: "+eventObject);
		
		patientBookingDetail = this.setStartAndEndTime(eventObject, patientBookingDetail);
		
		PatientBookingDetail savedPatientBookingDetail = this.therapistService.savePatientBookingDetail(patientBookingDetail);
		LOGGER.info("savedPatientBookingDetail Id ::: "+savedPatientBookingDetail.getPatientBookingId());
		
		}
		
		return object;
	}
	
	@PostMapping("/addTherapist")
	public ResponseEntity<BaseResponse> addTherapist(@RequestBody TherapistDetail therapistDetail) {
		LOGGER.info("Therapist "+therapistDetail.getTherapistFirstName()+" is to be create.");
		AddEditTherapistResponse addEditTherapistResponse = null;
		TherapistDetail savedtherapistDetail = null;
		try {
		
			savedtherapistDetail = this.therapistService.addTherapist(therapistDetail);
		
			addEditTherapistResponse = this.therapistService.therapsitToTherapistResponse(savedtherapistDetail);
			try {
			Object calendlyWebhookSubcription = this.setCalendlyWebHook(therapistDetail.getApiKey(), therapistDetail.getBearerToken());
			LOGGER.info("calendlyWebhookSubcription response after creating webhook ::: "+calendlyWebhookSubcription);
		
			this.therapistService.saveTherapistDetail(this.setCalendlyWebHookSubscriptionUrl(savedtherapistDetail, calendlyWebhookSubcription.toString()));
			}catch(Exception exception) {
				LOGGER.error("Some error occured while creating webhook subscription ::: "+exception.getMessage());
			}
		}catch (Exception exception) {
			LOGGER.error(ResponseMessage.THERAPIST_ADD_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.THERAPIST_ADD_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
					ResponseMessage.THERAPIST_ADD_SUCCESS,DateUtil.getCurrentDateTime(),
				Collections.emptyList(), addEditTherapistResponse));
	}
	
	@PostMapping("/editTherapist")
	public ResponseEntity<BaseResponse> editTherapist(@RequestBody TherapistDetail therapistDetail) {
		LOGGER.info("Therapist "+therapistDetail.getTherapistFirstName()+" is to be edit.");
		AddEditTherapistResponse addEditTherapistResponse = null;
		try {
		
			addEditTherapistResponse = this.therapistService.editTherapist(therapistDetail);
		
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.THERAPIST_EDITED_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
						new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
							ResponseMessage.THERAPIST_EDITED_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
						ResponseMessage.THERAPIST_EDITED_SUCCESS,DateUtil.getCurrentDateTime(),
					Collections.emptyList(), addEditTherapistResponse));
	}
	
	@GetMapping("/deleteTherapist/{id}")
	public ResponseEntity<BaseResponse> deleteTherapist(@PathVariable("id") Long id) {
		LOGGER.info("Therapist id "+id+" is to be delete.");
		try {
		    String calendlyWebhookSubscriptionUrl = null;
			TherapistDetail therapistDetail = this.therapistService.getTherapistDetailById(id);
			calendlyWebhookSubscriptionUrl = therapistDetail.getCalendlyWebHookSubscriptionUrl();
			String deletedUserMsg = this.therapistService.deleteTherapist(id);
		    this.therapistService.deleteMethodForCalendlyWebHookSubscription(calendlyWebhookSubscriptionUrl,therapistDetail.getBearerToken());
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.THERAPIST_DELETED_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
				ResponseMessage.THERAPIST_DELETED_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
						ResponseMessage.THERAPIST_DELETED_SUCCESS,DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
	}
	
	@GetMapping("/getTherapists")
	public ResponseEntity<BaseResponse> getTherapists() {
		LOGGER.info("Inside getTherapists method for get therapist list.");
		try {
			List<TherapistDetail> therapists = this.therapistService.getTherapists();
			
			if(therapists != null && !therapists.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.GET_ALL_THERAPISTS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), therapists));
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.THERAPISTS_NOT_FOUND,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
		}
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.EXCEPTION_ALL_THERAPISTS+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.EXCEPTION_ALL_THERAPISTS+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		
	}
	
	@GetMapping("/getTherapist/{therapistId}")
	public ResponseEntity<BaseResponse> getTherapist(@PathVariable("therapistId") Long therapistId) {
		LOGGER.info("therapistId from request ::: "+therapistId);
		try {
			TherapistDetail therapistDetail = this.therapistService.getTherapistDetailById(therapistId);
		if(therapistDetail == null) { 
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.THERAPIST_NOT_EXIST,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), null));
	    } else { 
	    	return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.THERAPIST_GET_SUCCESSFULL,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), therapistDetail));
	    }
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.GET_THERAPIST_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.GET_THERAPIST_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null)); 
		}
	}
	
	@GetMapping("/getTherapistsName")
	public ResponseEntity<BaseResponse> getTherapistsName() {
		
		List<TherapistNamesResponse> therapistsResponse = null;
		
		try {
		
		therapistsResponse = this.therapistService.getTherapistsName();
		
	}catch(Exception exception) {
		LOGGER.error(ResponseMessage.GET_THERAPISTS_NAME_ERROR+exception.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
			ResponseMessage.GET_THERAPISTS_NAME_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
				Collections.emptyList(), null));
	}
	
	return ResponseEntity.status(HttpStatus.OK).body(
			new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
					ResponseMessage.GET_THERAPISTS_NAME_SUCCESS,DateUtil.getCurrentDateTime(),
				Collections.emptyList(), therapistsResponse));
	}
	
	@GetMapping("/getCalendlyUrl/{therapistId}")
	public ResponseEntity<BaseResponse> getCalendlyUrl(@PathVariable("therapistId") Long therapistId) {
		LOGGER.info("therapistId from request ::: "+therapistId);
		try {
			TherapistDetail therapistDetail = this.therapistService.getTherapistDetailById(therapistId);
			String calendlyUrl = null;
			if(therapistDetail != null) {
				 calendlyUrl = therapistDetail.getSchedulingUrl();
			}
			
	    	return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.CALENDLY_URL_GET_SUCCESS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), calendlyUrl));
	    
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.CALENDLY_URL_GET_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.CALENDLY_URL_GET_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null)); 
		}
	}
	
	@GetMapping("/getTherapistNameById/{therapistId}")
	public ResponseEntity<BaseResponse> getTherapistNameById(@PathVariable("therapistId") Long therapistId) {
		LOGGER.info("getTherapistNameById therapistId from request ::: "+therapistId);
		try {
			TherapistDetail therapistDetail = this.therapistService.getTherapistDetailById(therapistId);
			String therapistName = therapistDetail.getTherapistFirstName() +  " " + therapistDetail.getTherapistLastName();
		return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.THERAPIST_GET_SUCCESSFULL,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), therapistName));
	    
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.GET_THERAPIST_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.GET_THERAPIST_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null)); 
		}
	}
	
	@GetMapping("/getPatientBookingDetail/{therapistId}/{email}")
	public ResponseEntity<BaseResponse> getPatientBookingDetail(@PathVariable("therapistId") Long therapistId,@PathVariable("email") String email) {
		LOGGER.info("getPatientBookingDetail therapistId from request ::: "+therapistId);
		try {
			AdminCalendlyResponse adminCalendlyResponse = new AdminCalendlyResponse();
			TherapistDetail therapistDetail = this.therapistService.getTherapistDetailById(therapistId);
			PatientBookingDetail patientBookingDetail = this.therapistService.getPatientBookingDetailByEmail(email);
			
			String therapistName = therapistDetail.getTherapistFirstName() +  " " + therapistDetail.getTherapistLastName();
			adminCalendlyResponse.setBookingEndTime(patientBookingDetail.getBookingEndTime());
			adminCalendlyResponse.setBookingStartTime(patientBookingDetail.getBookingStartTime());
			adminCalendlyResponse.setCalendlyBookingTherapistName(therapistName);
			adminCalendlyResponse.setIsCalendlyBooking(patientBookingDetail.getIsCalendlyBooking());
			adminCalendlyResponse.setLastCalendlyBookingDate(patientBookingDetail.getBookingDate());
			adminCalendlyResponse.setCalendlyBookingNote(patientBookingDetail.getNote());
			adminCalendlyResponse.setCancelUrl(patientBookingDetail.getCancelUrl());
			adminCalendlyResponse.setCancelBookingReason(patientBookingDetail.getCancelReason());
			if("canceled".equals(patientBookingDetail.getStatus())) {
				adminCalendlyResponse.setIsBookingCancel(Boolean.TRUE);
			} else {
				adminCalendlyResponse.setIsBookingCancel(Boolean.FALSE);
			}
			LOGGER.info("getPatientBookingDetail : lastCalendlyBookingDate ::: "+adminCalendlyResponse.getLastCalendlyBookingDate());
		return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.PATIENT_BOOKING_DETAIL_SUCCESS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), adminCalendlyResponse));
	    
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.PATIENT_BOOKING_DETAIL_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.PATIENT_BOOKING_DETAIL_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null)); 
		}
	}
	
	private PatientBookingDetail setStartAndEndTime(Object object, PatientBookingDetail patientBookingDetail) {
		
		String[] eventArray = object.toString().split(",");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		for(String eventValue : eventArray) {
			if(eventValue.contains("end_time=")) {
				String removeT = eventValue.substring(eventValue.lastIndexOf("=")+1).replace("T", " ");
				String finalString = removeT.replace("Z", "");
				LocalTime time = LocalDateTime.parse(finalString.substring(0,finalString.lastIndexOf(".")),formatter).toLocalTime();
				String eventEndTime = LocalTime.parse(time.toString(), DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
				patientBookingDetail.setBookingEndTime(eventEndTime);
				LOGGER.info("eventEndTime ::: "+eventEndTime);
			}
			if(eventValue.contains("start_time=")) {
				patientBookingDetail.setValidateStartTime(eventValue.substring(eventValue.lastIndexOf("=")+1));
				String removeT = eventValue.substring(eventValue.lastIndexOf("=")+1).replace("T", " ");
				String finalString = removeT.replace("Z", "");
				LocalTime time = LocalDateTime.parse(finalString.substring(0,finalString.lastIndexOf(".")),formatter).toLocalTime();
				String eventStartTime = LocalTime.parse(time.toString(), DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
				patientBookingDetail.setBookingStartTime(eventStartTime);
				LocalDate localDate = LocalDateTime.parse(finalString.substring(0,finalString.lastIndexOf(".")),formatter).toLocalDate();
				patientBookingDetail.setBookingDate(localDate);
				LOGGER.info("eventStartTime ::: "+eventStartTime);
			}
			
		}
		return patientBookingDetail;
	}
	
	@PostMapping("/fetchuniqueusercountandtotalbooking")
	public ResponseEntity<AnalyticsReportingQueryData> fetchUniqueUserCountAndTotalBooking(@RequestBody AnalyticsReportingQueryData analyticsReportingQueryData) {
		LOGGER.info("inside doctor service controller to fetch booking's data for analytics." + analyticsReportingQueryData.getStartDate());
		try {
			AnalyticsReportingQueryData responseVal = this.therapistService.fetchUniqueUserCountAndTotalBooking(analyticsReportingQueryData);
	    	return ResponseEntity.status(HttpStatus.OK).body(responseVal);
		} catch(Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
		}
	}
	
	@GetMapping("/getPatientBookingDetailByUserId/{therapistId}/{userId}")
	public ResponseEntity<BaseResponse> getPatientBookingDetailByUserId(@PathVariable("therapistId") Long therapistId,@PathVariable("userId") Long userId) {
		LOGGER.info("getPatientBookingDetail therapistId from request ::: "+therapistId);
		try {
			AdminCalendlyResponse adminCalendlyResponse = new AdminCalendlyResponse();
			TherapistDetail therapistDetail = this.therapistService.getTherapistDetailById(therapistId);
			PatientBookingDetail patientBookingDetail = this.therapistService.getPatientBookingDetailByUserIdAndStatus(userId);
			PatientBookingDetail checkBookingStatus = this.checkBookingDateAndSetStatus(patientBookingDetail);
			String therapistName = therapistDetail.getTherapistFirstName() +  " " + therapistDetail.getTherapistLastName();
			adminCalendlyResponse.setBookingEndTime(patientBookingDetail.getBookingEndTime());
			adminCalendlyResponse.setBookingStartTime(patientBookingDetail.getBookingStartTime());
			adminCalendlyResponse.setCalendlyBookingTherapistName(therapistName);
			adminCalendlyResponse.setIsCalendlyBooking(patientBookingDetail.getIsCalendlyBooking());
			adminCalendlyResponse.setLastCalendlyBookingDate(patientBookingDetail.getBookingDate());
			adminCalendlyResponse.setCalendlyBookingNote(patientBookingDetail.getNote());
			adminCalendlyResponse.setCancelUrl(patientBookingDetail.getCancelUrl());
			adminCalendlyResponse.setCancelBookingReason(patientBookingDetail.getCancelReason());
			adminCalendlyResponse.setValidateStartTime(patientBookingDetail.getValidateStartTime());
			adminCalendlyResponse.setStatus(checkBookingStatus.getStatus());
			if("canceled".equals(patientBookingDetail.getStatus())) {
				adminCalendlyResponse.setIsBookingCancel(Boolean.TRUE);
			} else {
				adminCalendlyResponse.setIsBookingCancel(Boolean.FALSE);
			}
			LOGGER.info("getPatientBookingDetail : lastCalendlyBookingDate ::: "+adminCalendlyResponse.getLastCalendlyBookingDate());
		return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.PATIENT_BOOKING_DETAIL_SUCCESS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), adminCalendlyResponse));
	    
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.PATIENT_BOOKING_DETAIL_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.PATIENT_BOOKING_DETAIL_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null)); 
		}
	}
	
	@GetMapping("/getPatientBookingDetailsByUserId/{userId}")
	public ResponseEntity<BaseResponse> getPatientBookingDetailsByUserId(@PathVariable("userId") Long userId) {
		LOGGER.info("getPatientBookingDetailsByUserId userId from request ::: "+userId);
		List<PatientBookingDetail> patientBookingDetails = null;
		try {
			patientBookingDetails = this.therapistService.findByUserId(userId);
			
			return ResponseEntity.status(HttpStatus.OK).body(
					new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
							ResponseMessage.PATIENT_BOOKING_DETAILS_SUCCESS,DateUtil.getCurrentDateTime(),
						Collections.emptyList(), patientBookingDetails));
			
		}catch(Exception exception) {
			LOGGER.error(ResponseMessage.PATIENT_BOOKING_DETAILS_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.PATIENT_BOOKING_DETAILS_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
			
		}
	}
	
	public Object setCalendlyWebHook(String signingKey,String token) {
		LOGGER.info("Inside setCalendlyWebHook in DoctorController class.");
		WebHookRequest webHookRequest = new WebHookRequest();
		List<String> events = new ArrayList<>();
		events.add("invitee.created");
		events.add("invitee.canceled");
		String organization = null;
		String user = null;
		String object = null;
		Object webHookResponse = null;
		try {
			object = this.therapistService.getUserAndOrganizationUrl(token).toString();
			String removeBraces = object.replace("}", "");
			String[] array = removeBraces.split(",");	
	        for(String value : array) {
	        	if(value.contains("current_organization=")) {
	        		organization = value.substring(value.indexOf("=")+1);
	        	}
	        	if(value.contains("uri=")) {
	        		user = value.substring(value.indexOf("=")+1);
	        	}
	        	
	        }
	        webHookRequest.setUrl(webHookCallBackUrl);
			webHookRequest.setEvents(events);
	        webHookRequest.setOrganization(organization);
	        webHookRequest.setUser(user);
	        webHookRequest.setScope("user");
	        webHookRequest.setSigning_key(signingKey);
			
	        webHookResponse = this.therapistService.setCalendlyWebHook(webHookRequest, token);
	        LOGGER.info("webHookResponse from TherapistServiceImpl class ::: "+webHookResponse);
	        
	        return webHookResponse;
	        
		}catch(Exception exception) {
			LOGGER.info("Some exception occur while creating calendly web hook ::: "+exception.getMessage());
			return null;
		}
		
	}
	
	private TherapistDetail setCalendlyWebHookSubscriptionUrl(TherapistDetail therapistDetail, String calendlySubscriptionUrl) {
		String[] array = calendlySubscriptionUrl.split(",");
		String webhookSubcriptionUrl = null;
		for(String value : array) {
			if(value.contains("uri=")) {
				webhookSubcriptionUrl = value.substring(value.indexOf("=")+1);
			}
		  }
		therapistDetail.setCalendlyWebHookSubscriptionUrl(webhookSubcriptionUrl);
		return therapistDetail;
	}
	
	@PostMapping("/fetchusertotalbookingdetail")
	public ResponseEntity<AnalyticsReportingQueryData> fetchUserTotalBookingBetweenDateRange(@RequestBody AnalyticsReportingQueryData analyticsReportingQueryData) {
		try {
			AnalyticsReportingQueryData responseVal = this.therapistService.fetchUserTotalBookingBetweenDateRange(analyticsReportingQueryData);
	    	return ResponseEntity.status(HttpStatus.OK).body(responseVal);
		} catch(Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
		}
	}
	
	public PatientBookingDetail checkBookingDateAndSetStatus(PatientBookingDetail patientBookingDetail) {
		LOGGER.info("Inside checkBookingDateAndSetStatus method, Booking date to be compare ::: "+patientBookingDetail.getValidateStartTime());
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime bookingdDateTime = LocalDateTime.parse(patientBookingDetail.getValidateStartTime().replace("Z", ""));
		PatientBookingDetail updatePatientBookingDetail = null;
		if(bookingdDateTime.isBefore(currentDateTime)) {
			patientBookingDetail.setStatus("completed");
			 updatePatientBookingDetail =	this.therapistService.savePatientBookingDetail(patientBookingDetail);
		}
		if(updatePatientBookingDetail != null) {
			return updatePatientBookingDetail;
		}
		return patientBookingDetail;
		
		
		
		
	}
	
	@PostMapping("/fetchSingleUserTotalBookingBetweenDateRange")
	public ResponseEntity<UserAnalyticsBookingDetailsList> fetchSingleUserTotalBookingBetweenDateRange(@RequestBody UserAnalyticsReportingQueryData userAnalyticsReportingQueryData) {
		LOGGER.info("Inside fetchSingleUserTotalBookingBetweenDateRange method of DoctorController class, userId ::: "+userAnalyticsReportingQueryData.getUserId()
		+" StartDate ::: "+userAnalyticsReportingQueryData.getStartDate()+" EndDate ::: "+userAnalyticsReportingQueryData.getEndDate());
		try {
			UserAnalyticsBookingDetailsList userAnalyticsBookingDetailsList=new UserAnalyticsBookingDetailsList();
			List<UserAnalyticsBookingDetails> userAnalyticsBookingDetails = this.therapistService.fetchSingleUserTotalBookingBetweenDateRange(userAnalyticsReportingQueryData);
			userAnalyticsBookingDetailsList.setUserAnalyticsBookingDetails(userAnalyticsBookingDetails);
			return ResponseEntity.status(HttpStatus.OK).body(userAnalyticsBookingDetailsList);
		} catch(Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
		}
	}
	
	
}
