package com.circles.health.patient.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.circles.health.patient.repository.ProfileAnswerRepository;
import com.circles.health.patient.repository.UserRepository;
import com.circles.health.patient.repository.VideoPlayRepository;
import com.circles.health.patient.repository.WellnessCheckAnswerRepository;
import com.circles.health.patient.response.AnalyticsExitAndBlogPageDetails;
import com.circles.health.patient.response.AnalyticsPerPageDetail;
import com.circles.health.patient.response.AnalyticsReportingQueryData;
import com.circles.health.patient.response.PatientAnalyticsData;
import com.circles.health.patient.response.PatientAnalyticsSingleUserCSV;
import com.circles.health.patient.response.UserAnalyticsBookingDetailsList;
import com.circles.health.patient.utils.RestTemplateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.analytics.data.v1beta.BetaAnalyticsDataClient;
import com.google.analytics.data.v1beta.DateRange;
import com.google.analytics.data.v1beta.Dimension;
import com.google.analytics.data.v1beta.Filter;
import com.google.analytics.data.v1beta.Filter.StringFilter;
import com.google.analytics.data.v1beta.FilterExpression;
import com.google.analytics.data.v1beta.Metric;
import com.google.analytics.data.v1beta.Row;
import com.google.analytics.data.v1beta.RunReportRequest;
import com.google.analytics.data.v1beta.RunReportResponse;

@Service
@Transactional
public class PatientAnalyticsServiceImpl implements PatientAnalyticsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PatientAnalyticsServiceImpl.class);

	@Autowired
	private WellnessCheckAnswerRepository wellnessCheckAnswerRepository;
	
	@Autowired
	private ProfileAnswerRepository profileAnswerRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VideoPlayRepository videoPlayRepository;
	
	@Value("${doctor.service.base.api.url}")
    String doctorServiceUrl;
	
	@Value("${google.analytics.propertyId}")
    String gaPropertyId;

	@Autowired
    RestTemplate restTemplate;
	
	@Override
	public PatientAnalyticsData getAnalyticsDataForParticularUser(AnalyticsReportingQueryData analyticsReportingQueryData) {
		PatientAnalyticsData patientAnalyticsData = null;
		
		try {
			if (analyticsReportingQueryData != null && analyticsReportingQueryData.getStartDate() != null 
					&& analyticsReportingQueryData.getEndDate() != null) {
				patientAnalyticsData = new PatientAnalyticsData();
				//code to set total user visit and total time spent on website
				setTotalVisitAndTotalTimeOnWebsiteUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
				
				//code to set date of every visit
				setusersDateOfEveryVisitUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
				//setPageDetailDataForAllPatientusingGA4
				//code to set all page details
				setPageDetailDataForUserUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
				//code for exit page for single user
				setExitPageDetailDataForSingleUserusingGA4(analyticsReportingQueryData, patientAnalyticsData);
				//code for blog view  page details
				setBlogViewForSingleUserusingGA4( analyticsReportingQueryData, patientAnalyticsData);
		
				
				LocalDateTime startDateTime = analyticsReportingQueryData.getStartDate().atTime(0, 0, 0),
						  	  endDateTime = analyticsReportingQueryData.getEndDate().atTime(23, 59, 59);
				
				patientAnalyticsData.setTotalWellnessCheck(String.valueOf(wellnessCheckAnswerRepository.countByUserIdAndWellnessCheckDateBetween
						(analyticsReportingQueryData.getUserId(), startDateTime, endDateTime)));
				
				patientAnalyticsData.setTotalProfileQuestion(String.valueOf(profileAnswerRepository.totalProfileQuestionCountByUser
						(analyticsReportingQueryData.getUserId(), startDateTime, endDateTime)));
				
				patientAnalyticsData.setTotalTimeVideoWatched(String.valueOf(videoPlayRepository.countByUserIdAndVideoPlayTimeBetween
						(analyticsReportingQueryData.getUserId(), startDateTime, endDateTime)));
				
				//code to set users therapist booking related data
				AnalyticsReportingQueryData obj = fetchUserTotalBookings(analyticsReportingQueryData);
				if (obj != null) {
					patientAnalyticsData.setBooking(String.valueOf(obj.getTotalBooking()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return patientAnalyticsData;
	}

	
	//***************SingleUserCSV Methods****************//

	@Override
	public PatientAnalyticsSingleUserCSV getAnalyticsDataForSingleUserCSV(
			AnalyticsReportingQueryData analyticsReportingQueryData) {
		
		PatientAnalyticsSingleUserCSV  patientAnalyticsSingleUserCSV=null;

		try {
			if (analyticsReportingQueryData != null && analyticsReportingQueryData.getStartDate() != null 
					&& analyticsReportingQueryData.getEndDate() != null) {
			
			patientAnalyticsSingleUserCSV = new PatientAnalyticsSingleUserCSV();

			setTotalVisitAndTotalTimeForSingleUser(analyticsReportingQueryData,patientAnalyticsSingleUserCSV);
		//setPageDetailDataForForSingleUserCSV
			setPageDetailDataForSingleUserCSV(analyticsReportingQueryData,patientAnalyticsSingleUserCSV);
			//setExitPage For CSv
	       setExitPageDetailDataForSingleUserCSV( analyticsReportingQueryData, patientAnalyticsSingleUserCSV);
	       //setBlogView For CSV
	   	setBlogViewForSingleUserCSV( analyticsReportingQueryData, patientAnalyticsSingleUserCSV); 


			LocalDateTime startDateTime = analyticsReportingQueryData.getStartDate().atTime(0, 0, 0),
				  	  endDateTime = analyticsReportingQueryData.getEndDate().atTime(23, 59, 59);
		
			patientAnalyticsSingleUserCSV.setTotalWellness(String.valueOf(wellnessCheckAnswerRepository.countByUserIdAndWellnessCheckDateBetween
				(analyticsReportingQueryData.getUserId(), startDateTime, endDateTime)));
		
			patientAnalyticsSingleUserCSV.setTotalwellnessProfillingComplted(String.valueOf(profileAnswerRepository.totalProfileQuestionCountByUser
				(analyticsReportingQueryData.getUserId(), startDateTime, endDateTime)));
		
			patientAnalyticsSingleUserCSV.setTotalVideoWatched(String.valueOf(videoPlayRepository.countByUserIdAndVideoPlayTimeBetween
				(analyticsReportingQueryData.getUserId(), startDateTime, endDateTime)));
			
			
					//code to set users therapist booking related data
			UserAnalyticsBookingDetailsList obj = getAnalyticsDataForParticularCSVUserForBooking(analyticsReportingQueryData);
			LOGGER.info(" UserAnalyticsBookingDetailsList PatientAnalyticsServiceImpl ::: "+obj);
			if (obj != null && obj.getUserAnalyticsBookingDetails() != null) {
			patientAnalyticsSingleUserCSV.setBookingDetail(obj.getUserAnalyticsBookingDetails());
			//patientAnalyticsData.setBooking(String.valueOf(obj.getTotalBooking()));

			LOGGER.info(" Some error occured while getting patient booking detail from doctor service PatientAnalyticsServiceImpl ::: "+obj.getUserAnalyticsBookingDetails());
		}
			//add for total booking//
			AnalyticsReportingQueryData totalBooking = fetchUserTotalBookings(analyticsReportingQueryData);
			if (totalBooking != null) {
				patientAnalyticsSingleUserCSV.setTotalBooking(String.valueOf(totalBooking.getTotalBooking()));
			}

		} 
		}catch (Exception e) {
			LOGGER.info("Some exception occur inside getAnalyticsDataForSingleUserCSV of PatientAnalyticsServiceImpl ::: "+e.getMessage());
		}
		return patientAnalyticsSingleUserCSV;
	}
	
	
	//***************ALLPatientsAnalticsData Methods****************//

	@Override
	public PatientAnalyticsData getAnalyticsDataForAllPatient(AnalyticsReportingQueryData analyticsReportingQueryData) {
		PatientAnalyticsData patientAnalyticsData = null;
		
		try {
			if(analyticsReportingQueryData != null && analyticsReportingQueryData.getStartDate() != null 
					&& analyticsReportingQueryData.getEndDate() != null && analyticsReportingQueryData.getGender() == null && analyticsReportingQueryData.getEthnicity() == null 
					&& analyticsReportingQueryData.getAgeStart() == null && analyticsReportingQueryData.getAgeEnd() == null) 
			{
				LOGGER.info("Inside startdate and enddate not null condition.");
				patientAnalyticsData = new PatientAnalyticsData();
				
				//code to get total visits and total unique users.
				setTotalVisitsForAllPatientUsingGA4(analyticsReportingQueryData.getStartDate(),
						analyticsReportingQueryData.getEndDate(), patientAnalyticsData);
				
				setUniqueVisitorsForAllPatientUsingGA4(analyticsReportingQueryData.getStartDate(),
						analyticsReportingQueryData.getEndDate(), patientAnalyticsData);
				
				//code to get page data
				setPageDetailDataForAllPatientusingGA4(analyticsReportingQueryData.getStartDate()
						, analyticsReportingQueryData.getEndDate(), patientAnalyticsData);
				// code for get Exit page
				
				setExitPageDetailDataForAllPatientusingGA4(analyticsReportingQueryData.getStartDate()
						, analyticsReportingQueryData.getEndDate(), patientAnalyticsData);
				
				//code for getBlowViewCount

				setBlogPageDetailDataForAllPatientusingGA4(analyticsReportingQueryData.getStartDate()
						, analyticsReportingQueryData.getEndDate(), patientAnalyticsData);
				
				LocalDateTime startDateTime = analyticsReportingQueryData.getStartDate().atTime(0, 0, 0),
							  endDateTime = analyticsReportingQueryData.getEndDate().atTime(23, 59, 59);
			
				Long uniquwWellnessCheckByDateRange = wellnessCheckAnswerRepository.distinctUsersWellnessCheckByDateRange(startDateTime, endDateTime),
					 userCount = userRepository.count();
				
				Double userPercentageWhoAttemptedWellnessCheck = ((double)(uniquwWellnessCheckByDateRange * 100) / (double)userCount);
				LOGGER.info("");
				patientAnalyticsData.setTotalWellnessCheck(String.valueOf(wellnessCheckAnswerRepository.countByWellnessCheckDateBetween(startDateTime, endDateTime)));
				patientAnalyticsData.setUniqueVisitorWellnessCheck(String.valueOf(uniquwWellnessCheckByDateRange));
				patientAnalyticsData.setUsersPercentageWhoAttemptedWellnessCheck(String.valueOf(new DecimalFormat("0.00").format(userPercentageWhoAttemptedWellnessCheck)));
				
				patientAnalyticsData.setTotalProfileQuestion(String.valueOf(profileAnswerRepository.totalProfileQuestionCount(startDateTime, endDateTime)));
				patientAnalyticsData.setUniqueVisitorsProfileQuestion(String.valueOf(profileAnswerRepository.distinctUserProfileQuestion(startDateTime, endDateTime)));
				
				//code to set users therapist booking related data
				AnalyticsReportingQueryData obj = fetchUniqueUserCountAndTotalBookings(analyticsReportingQueryData);
				if (obj != null) {
					patientAnalyticsData.setBooking(String.valueOf(obj.getTotalBooking()));
					Long userCountByEmail = userRepository.countByDistinctEmail();
					Double percentageUser = ((double)(obj.getDistinctUser() * 100) / (double)userCountByEmail);	
					patientAnalyticsData.setBookingPercentage(String.valueOf(new DecimalFormat("0.00").format(percentageUser)));
				}
				
				patientAnalyticsData.setTotalTimeVideoWatched(String.valueOf(videoPlayRepository.countByVideoPlayTimeBetween(startDateTime, endDateTime)));
				
			} 
			
		else if (analyticsReportingQueryData != null && analyticsReportingQueryData.getStartDate() != null 
					&& analyticsReportingQueryData.getEndDate() != null && analyticsReportingQueryData.getGender() != null && analyticsReportingQueryData.getEthnicity() == null
					&& analyticsReportingQueryData.getAgeStart() == null && analyticsReportingQueryData.getAgeEnd() == null) {
				patientAnalyticsData = new PatientAnalyticsData();
				LOGGER.info("Inside startdate, enddate and gender not null condition.");
				//code to get total visits and total unique users.
				setTotalVisitsForAllPatientGenderUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
				
				setUniqueVisitorsForAllPatientGenderUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
				
				//code to get page data
				setPageDetailDataForAllPatientGenderusingGA4(analyticsReportingQueryData, patientAnalyticsData);
				// code for get Exit page
				
				setExitPageDetailDataForAllPatientGenderusingGA4(analyticsReportingQueryData, patientAnalyticsData);
				
				//code for getBlowViewCount

				setBlogPageDetailDataForAllPatientGenderusingGA4(analyticsReportingQueryData, patientAnalyticsData);
				
				LocalDateTime startDateTime = analyticsReportingQueryData.getStartDate().atTime(0, 0, 0),
							  endDateTime = analyticsReportingQueryData.getEndDate().atTime(23, 59, 59);
			
				Long uniquwWellnessCheckByDateRange = wellnessCheckAnswerRepository.distinctUsersWellnessCheckByDateRangeAndGender(startDateTime, endDateTime,analyticsReportingQueryData.getGender()),
					 userCount = userRepository.count();
				
				Double userPercentageWhoAttemptedWellnessCheck = ((double)(uniquwWellnessCheckByDateRange * 100) / (double)userCount);
				patientAnalyticsData.setTotalWellnessCheck(String.valueOf(wellnessCheckAnswerRepository.countByWellnessCheckDateBetweenAndGender(startDateTime, endDateTime,analyticsReportingQueryData.getGender())));
				patientAnalyticsData.setUniqueVisitorWellnessCheck(String.valueOf(uniquwWellnessCheckByDateRange));
				patientAnalyticsData.setUsersPercentageWhoAttemptedWellnessCheck(String.valueOf(new DecimalFormat("0.00").format(userPercentageWhoAttemptedWellnessCheck)));
				
				patientAnalyticsData.setTotalProfileQuestion(String.valueOf(profileAnswerRepository.totalProfileQuestionCountByGender(startDateTime, endDateTime,analyticsReportingQueryData.getGender())));
				patientAnalyticsData.setUniqueVisitorsProfileQuestion(String.valueOf(profileAnswerRepository.distinctUserProfileQuestionByGender(startDateTime, endDateTime,analyticsReportingQueryData.getGender())));
				
				//code to set users therapist booking related data
				AnalyticsReportingQueryData obj = fetchUniqueUserCountAndTotalBookings(analyticsReportingQueryData);
				if (obj != null) {
					patientAnalyticsData.setBooking(String.valueOf(obj.getTotalBooking()));
					Long userCountByEmail = userRepository.countByDistinctEmail();
					Double percentageUser = ((double)(obj.getDistinctUser() * 100) / (double)userCountByEmail);	
					patientAnalyticsData.setBookingPercentage(String.valueOf(new DecimalFormat("0.00").format(percentageUser)));
				}
				
				patientAnalyticsData.setTotalTimeVideoWatched(String.valueOf(videoPlayRepository.countByVideoPlayTimeBetweenByGender(startDateTime, endDateTime,analyticsReportingQueryData.getGender())));
				
			} 
			
		else if (analyticsReportingQueryData != null && analyticsReportingQueryData.getStartDate() != null 
				&& analyticsReportingQueryData.getEndDate() != null && analyticsReportingQueryData.getGender() == null && analyticsReportingQueryData.getEthnicity() != null
				&& analyticsReportingQueryData.getAgeStart() == null && analyticsReportingQueryData.getAgeEnd() == null) {
			patientAnalyticsData = new PatientAnalyticsData();
			LOGGER.info("Inside startdate, enddate and ethnicity not null condition.");
			//code to get total visits and total unique users.
			setTotalVisitsForAllPatientEthinicityUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			setUniqueVisitorsForAllPatientEthinicityUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			//code to get page data
			setPageDetailDataForAllPatientEthinicityusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			// code for get Exit page
			
			setExitPageDetailDataForAllPatientEthinicityusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			//code for getBlowViewCount

			setBlogPageDetailDataForAllPatientEthinicityusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			LocalDateTime startDateTime = analyticsReportingQueryData.getStartDate().atTime(0, 0, 0),
						  endDateTime = analyticsReportingQueryData.getEndDate().atTime(23, 59, 59);
		
			Long uniquwWellnessCheckByDateRange = wellnessCheckAnswerRepository.distinctUsersWellnessCheckByDateRangeAndEthnicity(startDateTime, endDateTime,analyticsReportingQueryData.getEthnicity()),
				 userCount = userRepository.count();
			
			Double userPercentageWhoAttemptedWellnessCheck = ((double)(uniquwWellnessCheckByDateRange * 100) / (double)userCount);
			patientAnalyticsData.setTotalWellnessCheck(String.valueOf(wellnessCheckAnswerRepository.countByWellnessCheckDateBetweenAndEthnicity(startDateTime, endDateTime,analyticsReportingQueryData.getEthnicity())));
			patientAnalyticsData.setUniqueVisitorWellnessCheck(String.valueOf(uniquwWellnessCheckByDateRange));
			patientAnalyticsData.setUsersPercentageWhoAttemptedWellnessCheck(String.valueOf(new DecimalFormat("0.00").format(userPercentageWhoAttemptedWellnessCheck)));
			
			patientAnalyticsData.setTotalProfileQuestion(String.valueOf(profileAnswerRepository.totalProfileQuestionCountByEthnicity(startDateTime, endDateTime,analyticsReportingQueryData.getEthnicity())));
			patientAnalyticsData.setUniqueVisitorsProfileQuestion(String.valueOf(profileAnswerRepository.distinctUserProfileQuestionByEthnicity(startDateTime, endDateTime,analyticsReportingQueryData.getEthnicity())));
			
			//code to set users therapist booking related data
			AnalyticsReportingQueryData obj = fetchUniqueUserCountAndTotalBookings(analyticsReportingQueryData);
			if (obj != null) {
				patientAnalyticsData.setBooking(String.valueOf(obj.getTotalBooking()));
				Long userCountByEmail = userRepository.countByDistinctEmail();
				Double percentageUser = ((double)(obj.getDistinctUser() * 100) / (double)userCountByEmail);	
				patientAnalyticsData.setBookingPercentage(String.valueOf(new DecimalFormat("0.00").format(percentageUser)));
			}
			
			patientAnalyticsData.setTotalTimeVideoWatched(String.valueOf(videoPlayRepository.countByVideoPlayTimeBetweenByEthnicity(startDateTime, endDateTime,analyticsReportingQueryData.getEthnicity())));
			
		} 

		else if(analyticsReportingQueryData != null && analyticsReportingQueryData.getStartDate() != null 
				&& analyticsReportingQueryData.getEndDate() != null && analyticsReportingQueryData.getGender() == null && analyticsReportingQueryData.getEthnicity() == null 
				&& analyticsReportingQueryData.getAgeStart() != null && analyticsReportingQueryData.getAgeEnd() != null) 
		{
			patientAnalyticsData = new PatientAnalyticsData();
			LOGGER.info("Inside startdate, enddate and age not null condition.");
			//code to get total visits and total unique users.
			setTotalVisitsForAllPatientDOBUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			setUniqueVisitorsForAllPatientDOBUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			//code to get page data
			setPageDetailDataForAllPatientDOBusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			// code for get Exit page
			
			setExitPageDetailDataForAllPatientDOBusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			//code for getBlowViewCount

			setBlogPageDetailDataForAllPatientDOBusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			LocalDateTime startDateTime = analyticsReportingQueryData.getStartDate().atTime(0, 0, 0),
						  endDateTime = analyticsReportingQueryData.getEndDate().atTime(23, 59, 59);
		
			Long uniquwWellnessCheckByDateRange = wellnessCheckAnswerRepository.distinctUsersWellnessCheckByDateRangeAndAge(startDateTime, endDateTime,
					analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd()),
				 userCount = userRepository.count();
			
			Double userPercentageWhoAttemptedWellnessCheck = ((double)(uniquwWellnessCheckByDateRange * 100) / (double)userCount);
			LOGGER.info("");
			patientAnalyticsData.setTotalWellnessCheck(String.valueOf(wellnessCheckAnswerRepository.countByWellnessCheckDateBetweenAndAge(startDateTime, endDateTime, 
					analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			patientAnalyticsData.setUniqueVisitorWellnessCheck(String.valueOf(uniquwWellnessCheckByDateRange));
			patientAnalyticsData.setUsersPercentageWhoAttemptedWellnessCheck(String.valueOf(new DecimalFormat("0.00").format(userPercentageWhoAttemptedWellnessCheck)));
			
			patientAnalyticsData.setTotalProfileQuestion(String.valueOf(profileAnswerRepository.totalProfileQuestionCountByAge(startDateTime, endDateTime,analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			patientAnalyticsData.setUniqueVisitorsProfileQuestion(String.valueOf(profileAnswerRepository.distinctUserProfileQuestionByAge(startDateTime, endDateTime,analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			
			//code to set users therapist booking related data
			AnalyticsReportingQueryData obj = fetchUniqueUserCountAndTotalBookings(analyticsReportingQueryData);
			if (obj != null) {
				patientAnalyticsData.setBooking(String.valueOf(obj.getTotalBooking()));
				Long userCountByEmail = userRepository.countByDistinctEmail();
				Double percentageUser = ((double)(obj.getDistinctUser() * 100) / (double)userCountByEmail);	
				patientAnalyticsData.setBookingPercentage(String.valueOf(new DecimalFormat("0.00").format(percentageUser)));
			}
			
			patientAnalyticsData.setTotalTimeVideoWatched(String.valueOf(videoPlayRepository.countByVideoPlayTimeBetweenByAge(startDateTime, endDateTime,analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			
		} 
			
		else if(analyticsReportingQueryData != null && analyticsReportingQueryData.getStartDate() != null 
				&& analyticsReportingQueryData.getEndDate() != null && analyticsReportingQueryData.getGender() != null && analyticsReportingQueryData.getEthnicity() == null 
				&& analyticsReportingQueryData.getAgeStart() != null && analyticsReportingQueryData.getAgeEnd() != null) 
		{
			patientAnalyticsData = new PatientAnalyticsData();
			LOGGER.info("Inside startdate, enddate, gender and age not null condition.");
			//code to get total visits and total unique users.
			setTotalVisitsForAllPatientDOBAndGenderUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			setUniqueVisitorsForAllPatientDOBAndGenderUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			//code to get page data
			setPageDetailDataForAllPatientDOBAndGenderusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			// code for get Exit page
			
			setExitPageDetailDataForAllPatientDOBAndGenderusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			//code for getBlowViewCount

			setBlogPageDetailDataForAllPatientDOBAndGenderusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			LocalDateTime startDateTime = analyticsReportingQueryData.getStartDate().atTime(0, 0, 0),
						  endDateTime = analyticsReportingQueryData.getEndDate().atTime(23, 59, 59);
		
			Long uniquwWellnessCheckByDateRange = wellnessCheckAnswerRepository.distinctUsersWellnessCheckByDateRangeAgeAndGender(startDateTime, endDateTime,analyticsReportingQueryData.getGender(),
					analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd()),
				 userCount = userRepository.count();
			
			Double userPercentageWhoAttemptedWellnessCheck = ((double)(uniquwWellnessCheckByDateRange * 100) / (double)userCount);
			LOGGER.info("");
			patientAnalyticsData.setTotalWellnessCheck(String.valueOf(wellnessCheckAnswerRepository.countByWellnessCheckDateBetweenAgeAndGender(startDateTime, endDateTime, analyticsReportingQueryData.getGender(),
					analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			patientAnalyticsData.setUniqueVisitorWellnessCheck(String.valueOf(uniquwWellnessCheckByDateRange));
			patientAnalyticsData.setUsersPercentageWhoAttemptedWellnessCheck(String.valueOf(new DecimalFormat("0.00").format(userPercentageWhoAttemptedWellnessCheck)));
			
			patientAnalyticsData.setTotalProfileQuestion(String.valueOf(profileAnswerRepository.totalProfileQuestionCountByAgeAndGender(startDateTime, endDateTime,analyticsReportingQueryData.getGender(),analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			patientAnalyticsData.setUniqueVisitorsProfileQuestion(String.valueOf(profileAnswerRepository.distinctUserProfileQuestionByAgeAndGender(startDateTime, endDateTime,analyticsReportingQueryData.getGender(),analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			
			//code to set users therapist booking related data
			AnalyticsReportingQueryData obj = fetchUniqueUserCountAndTotalBookings(analyticsReportingQueryData);
			if (obj != null) {
				patientAnalyticsData.setBooking(String.valueOf(obj.getTotalBooking()));
				Long userCountByEmail = userRepository.countByDistinctEmail();
				Double percentageUser = ((double)(obj.getDistinctUser() * 100) / (double)userCountByEmail);	
				patientAnalyticsData.setBookingPercentage(String.valueOf(new DecimalFormat("0.00").format(percentageUser)));
			}
			
			patientAnalyticsData.setTotalTimeVideoWatched(String.valueOf(videoPlayRepository.countByVideoPlayTimeBetweenByAgeAndGender(startDateTime, endDateTime,analyticsReportingQueryData.getGender(),analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			
		}
		
		else if(analyticsReportingQueryData != null && analyticsReportingQueryData.getStartDate() != null 
				&& analyticsReportingQueryData.getEndDate() != null && analyticsReportingQueryData.getGender() == null && analyticsReportingQueryData.getEthnicity() != null 
				&& analyticsReportingQueryData.getAgeStart() != null && analyticsReportingQueryData.getAgeEnd() != null) 
		{
			patientAnalyticsData = new PatientAnalyticsData();
			LOGGER.info("Inside startdate, enddate, ethnicity and age not null condition.");
			//code to get total visits and total unique users.
			setTotalVisitsForAllPatientDOBAndEthinicityUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			setUniqueVisitorsForAllPatientDOBAndEthinicityUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			//code to get page data
			setPageDetailDataForAllPatientDOBAndEthinicityusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			// code for get Exit page
			
			setExitPageDetailDataForAllPatientDOBAndEthinicityusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			//code for getBlowViewCount

			setBlogPageDetailDataForAllPatientDOBAndEthinicityusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			LocalDateTime startDateTime = analyticsReportingQueryData.getStartDate().atTime(0, 0, 0),
						  endDateTime = analyticsReportingQueryData.getEndDate().atTime(23, 59, 59);
		
			Long uniquwWellnessCheckByDateRange = wellnessCheckAnswerRepository.distinctUsersWellnessCheckByDateRangeAgeAndEthnicity(startDateTime, endDateTime,analyticsReportingQueryData.getEthnicity(),
					analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd()),
				 userCount = userRepository.count();
			
			Double userPercentageWhoAttemptedWellnessCheck = ((double)(uniquwWellnessCheckByDateRange * 100) / (double)userCount);
			LOGGER.info("");
			patientAnalyticsData.setTotalWellnessCheck(String.valueOf(wellnessCheckAnswerRepository.countByWellnessCheckDateBetweenAgeAndEthnicity(startDateTime, endDateTime, analyticsReportingQueryData.getEthnicity(),
					analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			patientAnalyticsData.setUniqueVisitorWellnessCheck(String.valueOf(uniquwWellnessCheckByDateRange));
			patientAnalyticsData.setUsersPercentageWhoAttemptedWellnessCheck(String.valueOf(new DecimalFormat("0.00").format(userPercentageWhoAttemptedWellnessCheck)));
			
			patientAnalyticsData.setTotalProfileQuestion(String.valueOf(profileAnswerRepository.totalProfileQuestionCountByAgeAndEthnicity(startDateTime, endDateTime,analyticsReportingQueryData.getEthnicity(),analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			patientAnalyticsData.setUniqueVisitorsProfileQuestion(String.valueOf(profileAnswerRepository.distinctUserProfileQuestionByAgeAndEthnicity(startDateTime, endDateTime,analyticsReportingQueryData.getEthnicity(),analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			
			//code to set users therapist booking related data
			AnalyticsReportingQueryData obj = fetchUniqueUserCountAndTotalBookings(analyticsReportingQueryData);
			if (obj != null) {
				patientAnalyticsData.setBooking(String.valueOf(obj.getTotalBooking()));
				Long userCountByEmail = userRepository.countByDistinctEmail();
				Double percentageUser = ((double)(obj.getDistinctUser() * 100) / (double)userCountByEmail);	
				patientAnalyticsData.setBookingPercentage(String.valueOf(new DecimalFormat("0.00").format(percentageUser)));
			}
			
			patientAnalyticsData.setTotalTimeVideoWatched(String.valueOf(videoPlayRepository.countByVideoPlayTimeBetweenByAgeAndEthnicity(startDateTime, endDateTime,analyticsReportingQueryData.getEthnicity(),analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			
		}
			
		else if (analyticsReportingQueryData != null && analyticsReportingQueryData.getStartDate() != null 
                && analyticsReportingQueryData.getEndDate() != null && analyticsReportingQueryData.getGender() != null && analyticsReportingQueryData.getEthnicity() != null
                && analyticsReportingQueryData.getAgeStart() == null && analyticsReportingQueryData.getAgeEnd() == null) {
        patientAnalyticsData = new PatientAnalyticsData();
        LOGGER.info("Inside startdate, enddate, gender and ethnicity not null condition.");
        //code to get total visits and total unique users.
        setTotalVisitsForAllPatientGenderAndEthnicityUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
     
        setUniqueVisitorsForAllPatientGenderAndEthnicityUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
        
        //code to get page data
        setPageDetailDataForAllPatientGenderAndEthnicityusingGA4(analyticsReportingQueryData, patientAnalyticsData);
        // code for get Exit page
       
        setExitPageDetailDataForAllPatientGenderAndEthnicityusingGA4(analyticsReportingQueryData, patientAnalyticsData);
       
        //code for getBlowViewCount

       setBlogPageDetailDataForAllPatientGenderAndEthnicityusingGA4(analyticsReportingQueryData, patientAnalyticsData);
        //code to get total visits and total unique users GenderFilter.
        
       LocalDateTime startDateTime = analyticsReportingQueryData.getStartDate().atTime(0, 0, 0),
                                  endDateTime = analyticsReportingQueryData.getEndDate().atTime(23, 59, 59);

         Long uniquwWellnessCheckByDateRange = wellnessCheckAnswerRepository.distinctUsersWellnessCheckByDateRangeAndGenderAndEthnicity(startDateTime, endDateTime, analyticsReportingQueryData.getGender(), analyticsReportingQueryData.getEthnicity()),
                  userCount = userRepository.count();
         
        Double userPercentageWhoAttemptedWellnessCheck = ((double)(uniquwWellnessCheckByDateRange * 100) / (double)userCount);
        patientAnalyticsData.setTotalWellnessCheck(String.valueOf(wellnessCheckAnswerRepository.countByWellnessCheckDateBetweenAndGenderAndEthnicity(startDateTime, endDateTime, analyticsReportingQueryData.getGender(), analyticsReportingQueryData.getEthnicity())));
       patientAnalyticsData.setUniqueVisitorWellnessCheck(String.valueOf(uniquwWellnessCheckByDateRange));
         patientAnalyticsData.setUsersPercentageWhoAttemptedWellnessCheck(String.valueOf(new DecimalFormat("0.00").format(userPercentageWhoAttemptedWellnessCheck)));
        
        patientAnalyticsData.setTotalProfileQuestion(String.valueOf(profileAnswerRepository.totalProfileQuestionCountByGenderAndEhtnicity(startDateTime, endDateTime, analyticsReportingQueryData.getGender(), analyticsReportingQueryData.getEthnicity())));
         patientAnalyticsData.setUniqueVisitorsProfileQuestion(String.valueOf(profileAnswerRepository.distinctUserProfileQuestionByGenderAndEthnicity(startDateTime, endDateTime, analyticsReportingQueryData.getGender(), analyticsReportingQueryData.getEthnicity())));
        
          //code to set users therapist booking related data
         AnalyticsReportingQueryData obj = fetchUniqueUserCountAndTotalBookings(analyticsReportingQueryData);
         if (obj != null) {
                 patientAnalyticsData.setBooking(String.valueOf(obj.getTotalBooking()));
                 Long userCountByEmail = userRepository.countByDistinctEmail();
                 Double percentageUser = ((double)(obj.getDistinctUser() * 100) / (double)userCountByEmail); 
                patientAnalyticsData.setBookingPercentage(String.valueOf(new DecimalFormat("0.00").format(percentageUser)));
         }
         
         patientAnalyticsData.setTotalTimeVideoWatched(String.valueOf(videoPlayRepository.countByVideoPlayTimeBetweenByGenderAndEthnicity(startDateTime, endDateTime,analyticsReportingQueryData.getGender(),analyticsReportingQueryData.getEthnicity())));
        
} 
			
			
		else if (analyticsReportingQueryData != null && analyticsReportingQueryData.getStartDate() != null 
				&& analyticsReportingQueryData.getEndDate() != null && analyticsReportingQueryData.getGender() != null && analyticsReportingQueryData.getEthnicity() != null
				&& analyticsReportingQueryData.getAgeStart() != null && analyticsReportingQueryData.getAgeEnd() != null) {
			patientAnalyticsData = new PatientAnalyticsData();
			LOGGER.info("Inside all field not null condition.");
			//code to get total visits and total unique users.
			setTotalVisitsForAllPatientDOBAndGenderAndEthnicityUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			setUniqueVisitorsForAllPatientDOBAndGenderAndEthnicityUsingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			//code to get page data
			setPageDetailDataForAllPatientDOBAndGenderAndEthnicityusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			// code for get Exit page
			
			setExitPageDetailDataForAllPatientDOBAndGenderAndEthnicityusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			
			//code for getBlowViewCount

			setBlogPageDetailDataForAllPatientDOBAndGenderAndEthnicityusingGA4(analyticsReportingQueryData, patientAnalyticsData);
			//code to get total visits and total unique users GenderFilter.
			
			LocalDateTime startDateTime = analyticsReportingQueryData.getStartDate().atTime(0, 0, 0),
						  endDateTime = analyticsReportingQueryData.getEndDate().atTime(23, 59, 59);
		
			Long uniquwWellnessCheckByDateRange = wellnessCheckAnswerRepository.distinctUsersWellnessCheckByDateRangeAgeAndGenderAndEthnicity(startDateTime, endDateTime, analyticsReportingQueryData.getGender(), analyticsReportingQueryData.getEthnicity(),
					analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd()),
				 userCount = userRepository.count();
			
			Double userPercentageWhoAttemptedWellnessCheck = ((double)(uniquwWellnessCheckByDateRange * 100) / (double)userCount);
			patientAnalyticsData.setTotalWellnessCheck(String.valueOf(wellnessCheckAnswerRepository.countByWellnessCheckDateBetweenAgeAndGenderAndEthnicity(startDateTime, endDateTime, analyticsReportingQueryData.getGender(), analyticsReportingQueryData.getEthnicity(),
					analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			patientAnalyticsData.setUniqueVisitorWellnessCheck(String.valueOf(uniquwWellnessCheckByDateRange));
			patientAnalyticsData.setUsersPercentageWhoAttemptedWellnessCheck(String.valueOf(new DecimalFormat("0.00").format(userPercentageWhoAttemptedWellnessCheck)));
			
			patientAnalyticsData.setTotalProfileQuestion(String.valueOf(profileAnswerRepository.totalProfileQuestionCountByAgeAndGenderAndEhtnicity(startDateTime, endDateTime, analyticsReportingQueryData.getGender(), analyticsReportingQueryData.getEthnicity(),
					analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			patientAnalyticsData.setUniqueVisitorsProfileQuestion(String.valueOf(profileAnswerRepository.distinctUserProfileQuestionByAgeAndGenderAndEthnicity(startDateTime, endDateTime, analyticsReportingQueryData.getGender(), analyticsReportingQueryData.getEthnicity(),
					analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			
			//code to set users therapist booking related data
			AnalyticsReportingQueryData obj = fetchUniqueUserCountAndTotalBookings(analyticsReportingQueryData);
			if (obj != null) {
				patientAnalyticsData.setBooking(String.valueOf(obj.getTotalBooking()));
				Long userCountByEmail = userRepository.countByDistinctEmail();
				Double percentageUser = ((double)(obj.getDistinctUser() * 100) / (double)userCountByEmail);	
				patientAnalyticsData.setBookingPercentage(String.valueOf(new DecimalFormat("0.00").format(percentageUser)));
			}
			
			patientAnalyticsData.setTotalTimeVideoWatched(String.valueOf(videoPlayRepository.countByVideoPlayTimeBetweenByAgeAndGenderAndEthnicity(startDateTime, endDateTime,analyticsReportingQueryData.getGender(),analyticsReportingQueryData.getEthnicity(),
					analyticsReportingQueryData.getAgeStart(), analyticsReportingQueryData.getAgeEnd())));
			
		} 

	
		} catch (Exception e) {
			LOGGER.info("Some exception occur inside getAnalyticsDataForAllPatient method of PatientAnalyticsServiceImpl class ::: "+e.getMessage());
			e.printStackTrace();
		}
		
		return patientAnalyticsData;

	}
	
	private void setTotalVisitsForAllPatientUsingGA4(LocalDate startDate, LocalDate endDate,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addMetrics(Metric.newBuilder().setName("sessions"))
						.addMetrics(Metric.newBuilder().setName("activeusers"))
						.addDateRanges(DateRange.newBuilder().setStartDate(startDate.toString()).setEndDate(endDate.toString()))
						.build();
			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				for (Row row : response.getRowsList()) {
					patientAnalyticsData.setTotalVisitCount(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setUniqueVisitorsForAllPatientUsingGA4(LocalDate startDate, LocalDate endDate,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
						.addDateRanges(DateRange.newBuilder().setStartDate(startDate.toString()).setEndDate(endDate.toString()))
						.build();
			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				for (Row row : response.getRowsList()) {
					if(!row.getDimensionValues(0).getValue().equals("0") || !row.getDimensionValues(0).getValue().equals("(not set)")) {
						uniqueUserCount++;
					}
					patientAnalyticsData.setUniqueUserCount(String.valueOf(uniqueUserCount));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setPageDetailDataForAllPatientusingGA4(LocalDate startDate, LocalDate endDate, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
					.addMetrics(Metric.newBuilder().setName("averageDuration").setExpression("userEngagementDuration/sessions"))
					.addMetrics(Metric.newBuilder().setName("averageDurationPeruser").setExpression("userEngagementDuration/activeUsers"))
					.addDateRanges(DateRange.newBuilder().setStartDate(startDate.toString()).setEndDate(endDate.toString()))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
            int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsPerPageDetail> analyticsPerPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> wellnessPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> profilePageDetails = new ArrayList<> ();

				for (Row row : response.getRowsList()) {
					AnalyticsPerPageDetail detail = new AnalyticsPerPageDetail();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (profileCount <= 14 && pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageViews(row.getMetricValues(0).getValue());
						}
						
						if (row.getMetricValues(1) != null && row.getMetricValues(1).getValue() != null && !row.getMetricValues(1).getValue().isBlank()) {
							double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
							detail.setTotalTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
						} else {
							detail.setTotalTimeOnPage("0");
						}
						
						if (row.getMetricValues(2) != null && row.getMetricValues(2).getValue() != null && !row.getMetricValues(2).getValue().isBlank()) {
							if (row.getMetricValues(2).getValue().equals("0")) {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(3).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							} else {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(2).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							}
						} else {
							detail.setAvgTimeOnPage("0");
						}
			
						if (!otherPageThanMapValue) {
							if (pagePath.contains("takewellnesscheck/PHQ") || pagePath.contains("takewellnesscheck/GAD")) {
								wellnessPageDetails.add(detail);
							} else if (profileCount <= 14 && pagePath.contains("profiling/")) {
								profilePageDetails.add(detail);
							} else {
								analyticsPerPageDetails.add(detail);
							}
						}
					}
				}
				
				if (wellnessPageDetails != null && !wellnessPageDetails.isEmpty()) {
					wellnessPageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setWellnessPageDetails(wellnessPageDetails != null && !wellnessPageDetails.isEmpty() ? wellnessPageDetails : null);
				}
				if (profilePageDetails != null && !profilePageDetails.isEmpty()) {
					profilePageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setProfilePageDetails(profilePageDetails != null && !profilePageDetails.isEmpty() ? profilePageDetails : null);
				}
				
				patientAnalyticsData.setPerPageDetails(analyticsPerPageDetails != null && !analyticsPerPageDetails.isEmpty() ? analyticsPerPageDetails : null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void setExitPageDetailDataForAllPatientusingGA4(LocalDate startDate, LocalDate endDate, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("customUser:page_exit"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(startDate.toString()).setEndDate(endDate.toString()))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info("RUN REPORT RESPONSE  : : : "+response);
			int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsExitAndBlogPageDetails> exitPageDetails =new ArrayList<>();
				LOGGER.info("responserowlist  : : : "+response.getRowsList());

				for (Row row : response.getRowsList()) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
							detail.setCategory("Pages");
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("WellnessCheck");
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("Profiling");
								LOGGER.info("PageExitProfileCount All user value ::: "+Integer.parseInt(pagePath.split("/")[2]));								
								detail.setPageExitProfileCount(Integer.parseInt(pagePath.split("/")[2]));
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info("RowMatrixValue  : : : "+row.getMetricValues(0).getValue());

						}
						
						
			
						if (!otherPageThanMapValue) {
							exitPageDetails.add(detail);
						}
					}
				}
				if (exitPageDetails != null && !exitPageDetails.isEmpty()) {
		LOGGER.info("Before Sorting ExitPageDetails  : "+exitPageDetails);
		/*
		 * Collections.sort(exitPageDetails, Collections.reverseOrder());
		 * 
		 * LOGGER.info("After Sorting ExitPageDetails  : "+exitPageDetails);
		 */
						patientAnalyticsData.setExitPageDetails(exitPageDetails != null && !exitPageDetails.isEmpty() ? exitPageDetails : null);
						

				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void setBlogPageDetailDataForAllPatientusingGA4(LocalDate startDate, LocalDate endDate, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(startDate.toString()).setEndDate(endDate.toString()))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			//LOGGER.info(" blogPageDetails RUN REPORT RESPONSE  : : : "+response);

			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				//LOGGER.info("  blogPageDetails RESPONSE RowList  : : : "+response.getRowsList());

				List<AnalyticsExitAndBlogPageDetails> blogPageDetails =new ArrayList<>();
				for (Row row : response.getRowsList()) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							LOGGER.info("pagePath for blog ::: "+map.get(pagePath)+" === "+"RowMatrix Value ::: "+row.getMetricValues(0).getValue());
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						}  else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info(" blogPageDetails BLowView RowMatrix Value  : : : "+row.getMetricValues(0).getValue());

						}
						else {
							detail.setPageCount("0");

						}
						
			
						if (!otherPageThanMapValue) {
							blogPageDetails.add(detail);
						}
					}
				}
				if (blogPageDetails != null && !blogPageDetails.isEmpty()) {
					LOGGER.info("Before Sorting blogPageDetails  : "+blogPageDetails);

					//blogPageDetails.reversesort(a1.getPageCount())-(a2.getPageCount());
			        //Collections.sort(al, Collections.reverseOrder());

						patientAnalyticsData.setBlogPageDetails(blogPageDetails != null && !blogPageDetails.isEmpty() ? blogPageDetails : null);


				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void setusersDateOfEveryVisitUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					  .addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
				      .addDimensions(Dimension.newBuilder().setName("date"))

				      .addMetrics(Metric.newBuilder().setName("sessions"))
				      .addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
				      .addMetrics(Metric.newBuilder().setName("averageDuration").setExpression("userEngagementDuration/engagedSessions"))
				      .addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString())
					  .setEndDate(analyticsReportingQueryData.getEndDate().toString()))
				      .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:crm_id")
					  .setStringFilter(StringFilter.newBuilder().setValue(String.valueOf(analyticsReportingQueryData.getUserId())))))
				      .build();

			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsPerPageDetail> detailList = new ArrayList<> ();
				DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
				for (Row row : response.getRowsList()) {
					AnalyticsPerPageDetail detail = new AnalyticsPerPageDetail();
					detail.setVisitDate(row.getDimensionValues(1) != null ? LocalDate.parse(row.getDimensionValues(1).getValue().trim(), formatter) : null);
					detail.setPageViews(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
					detail.setTotalTimeOnPage(row.getMetricValues(1) != null  ? row.getMetricValues(1).getValue() : "0");
					detail.setAvgTimeOnPage(row.getMetricValues(2) != null  ? row.getMetricValues(2).getValue() : "0");
					detailList.add(detail);
				}
				
				patientAnalyticsData.setPatientVisitDetailsByDate(detailList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setPageDetailDataForUserUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int profileQuestionCount=1;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
	                .addDimensions(Dimension.newBuilder().setName("pagePath"))
	                .addMetrics(Metric.newBuilder().setName("screenPageViews"))
	                .addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
                    .addMetrics(Metric.newBuilder().setName("averageDuration").setExpression("userEngagementDuration/engagedSessions"))
                    .addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString())
					.setEndDate(analyticsReportingQueryData.getEndDate().toString()))
	                .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:crm_id")
	    		    .setStringFilter(StringFilter.newBuilder().setValue(String.valueOf(analyticsReportingQueryData.getUserId())))))
	                .build();
			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsPerPageDetail> analyticsPerPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> wellnessPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> profilePageDetails = new ArrayList<> ();
               
				for (Row row : response.getRowsList()) {
					AnalyticsPerPageDetail detail = new AnalyticsPerPageDetail();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(1) != null && row.getDimensionValues(1).getValue() != null && !row.getDimensionValues(1).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(1).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						}     
						//else if (pagePath.contains("takewellnesscheck/")) {
						 else if (pagePath.contains("takewellnesscheck/PHQ") || pagePath.contains("takewellnesscheck/GAD")) {

							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							//if(profileQuestionCount<=14) {
								//profileQuestionCount++;
							if (profileQuestionCount <= 14 && pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						
						} 
						//}
						
						//
						else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							
							detail.setPageViews(row.getMetricValues(0).getValue());
						}
						
						if (row.getMetricValues(1) != null && row.getMetricValues(1).getValue() != null && !row.getMetricValues(1).getValue().isBlank()) {
							double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
							detail.setTotalTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
						} else {
							detail.setTotalTimeOnPage("0");
						}
						
						if (row.getMetricValues(2) != null && row.getMetricValues(2).getValue() != null && !row.getMetricValues(2).getValue().isBlank()) {
							if (row.getMetricValues(2).getValue().equals("0")) {
								detail.setAvgTimeOnPage("0");
							} else {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(2).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							}
						} else {
							detail.setAvgTimeOnPage("0");
						}
			
						if (!otherPageThanMapValue) {
							if (pagePath.contains("takewellnesscheck/PHQ") || pagePath.contains("takewellnesscheck/GAD")) {
								wellnessPageDetails.add(detail);
							} else if (pagePath.contains("profiling/")) {
								profilePageDetails.add(detail);
							} 
							
							else {
								analyticsPerPageDetails.add(detail);
							}
							
						
						}
					}
				}
				
				if (wellnessPageDetails != null && !wellnessPageDetails.isEmpty()) {
					wellnessPageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setWellnessPageDetails(wellnessPageDetails != null && !wellnessPageDetails.isEmpty() ? wellnessPageDetails : null);
				}
				if (profilePageDetails != null && !profilePageDetails.isEmpty()) {
					profilePageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setProfilePageDetails(profilePageDetails != null && !profilePageDetails.isEmpty() ? profilePageDetails : null);
				}
				
				patientAnalyticsData.setPerPageDetails(analyticsPerPageDetails != null && !analyticsPerPageDetails.isEmpty() ? analyticsPerPageDetails : null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	private AnalyticsReportingQueryData fetchUniqueUserCountAndTotalBookings(AnalyticsReportingQueryData analyticsReportingQueryData){
		AnalyticsReportingQueryData obj = null;
		try {
			obj = RestTemplateUtil.postMethodToFetchAnalyticsData(doctorServiceUrl + "/doctor/fetchuniqueusercountandtotalbooking", 
					convertObjectToString(analyticsReportingQueryData), restTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	private AnalyticsReportingQueryData fetchUserTotalBookings(AnalyticsReportingQueryData analyticsReportingQueryData){
		AnalyticsReportingQueryData obj = null;
		try {
			obj = RestTemplateUtil.postMethodToFetchAnalyticsData(doctorServiceUrl + "/doctor/fetchusertotalbookingdetail", 
					convertObjectToString(analyticsReportingQueryData), restTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}

	private String convertObjectToString(Object object) {
		try {
			ObjectMapper Obj = new ObjectMapper();
			Obj.registerModule(new JavaTimeModule());
			Obj.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			return Obj.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	

		//*************Code For Gender Filter***************************//
	private void setTotalVisitsForAllPatientGenderUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addDimensions(Dimension.newBuilder().setName("customUser:gender"))
						.addMetrics(Metric.newBuilder().setName("sessions"))
						.addMetrics(Metric.newBuilder().setName("activeusers"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:gender")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info(" setTotalVisitsAndUniqueVisitorsForAllPatientGenderUsingGA4  "+response);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				for (Row row : response.getRowsList()) {
					patientAnalyticsData.setTotalVisitCount(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setUniqueVisitorsForAllPatientGenderUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
					    .addDimensions(Dimension.newBuilder().setName("customUser:gender"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:gender")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info(" setTotalVisitsAndUniqueVisitorsForAllPatientGenderUsingGA4  "+response);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				for (Row row : response.getRowsList()) {
					if(!row.getDimensionValues(0).getValue().equals("0") || !row.getDimensionValues(0).getValue().equals("(not set)")) {
						uniqueUserCount++;
					}
					patientAnalyticsData.setUniqueUserCount(String.valueOf(uniqueUserCount));
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		private void setPageDetailDataForAllPatientGenderusingGA4( AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
					.addMetrics(Metric.newBuilder().setName("averageDuration").setExpression("userEngagementDuration/sessions"))
					.addMetrics(Metric.newBuilder().setName("averageDurationPeruser").setExpression("userEngagementDuration/activeUsers"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
							  ().setFieldName("customUser:gender")
							  .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
            int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsPerPageDetail> analyticsPerPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> wellnessPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> profilePageDetails = new ArrayList<> ();

				for (Row row : response.getRowsList()) {
					AnalyticsPerPageDetail detail = new AnalyticsPerPageDetail();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (profileCount <= 14 && pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageViews(row.getMetricValues(0).getValue());
						}
						
						if (row.getMetricValues(1) != null && row.getMetricValues(1).getValue() != null && !row.getMetricValues(1).getValue().isBlank()) {
							double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
							detail.setTotalTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
						} else {
							detail.setTotalTimeOnPage("0");
						}
						
						if (row.getMetricValues(2) != null && row.getMetricValues(2).getValue() != null && !row.getMetricValues(2).getValue().isBlank()) {
							if (row.getMetricValues(2).getValue().equals("0")) {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(3).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							} else {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(2).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							}
						} else {
							detail.setAvgTimeOnPage("0");
						}
			
						if (!otherPageThanMapValue) {
							if (pagePath.contains("takewellnesscheck/PHQ") || pagePath.contains("takewellnesscheck/GAD")) {
								wellnessPageDetails.add(detail);
							} else if (profileCount <= 14 && pagePath.contains("profiling/")) {
								profilePageDetails.add(detail);
							} else {
								analyticsPerPageDetails.add(detail);
							}
						}
					}
				}
				
				if (wellnessPageDetails != null && !wellnessPageDetails.isEmpty()) {
					wellnessPageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setWellnessPageDetails(wellnessPageDetails != null && !wellnessPageDetails.isEmpty() ? wellnessPageDetails : null);
				}
				if (profilePageDetails != null && !profilePageDetails.isEmpty()) {
					profilePageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setProfilePageDetails(profilePageDetails != null && !profilePageDetails.isEmpty() ? profilePageDetails : null);
				}
				
				patientAnalyticsData.setPerPageDetails(analyticsPerPageDetails != null && !analyticsPerPageDetails.isEmpty() ? analyticsPerPageDetails : null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void setExitPageDetailDataForAllPatientGenderusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("customUser:page_exit"))
					.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
					 ().setFieldName("customUser:gender")
					 .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info("RUN REPORT RESPONSE  : : : "+response);
			int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsExitAndBlogPageDetails> exitPageDetails =new ArrayList<>();
				LOGGER.info("responserowlist  : : : "+response.getRowsList());

				for (Row row : response.getRowsList()) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
							detail.setCategory("Pages");
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("WellnessCheck");
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("Profiling");
								LOGGER.info("PageExitProfileCount All user value ::: "+Integer.parseInt(pagePath.split("/")[2]));								
								detail.setPageExitProfileCount(Integer.parseInt(pagePath.split("/")[2]));
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info("RowMatrixValue  : : : "+row.getMetricValues(0).getValue());

						}
						
						
			
						if (!otherPageThanMapValue) {
							exitPageDetails.add(detail);
						}
					}
				}
				if (exitPageDetails != null && !exitPageDetails.isEmpty()) {
		LOGGER.info("Before Sorting ExitPageDetails  : "+exitPageDetails);
		/*
		 * Collections.sort(exitPageDetails, Collections.reverseOrder());
		 * 
		 * LOGGER.info("After Sorting ExitPageDetails  : "+exitPageDetails);
		 */
						patientAnalyticsData.setExitPageDetails(exitPageDetails != null && !exitPageDetails.isEmpty() ? exitPageDetails : null);
						

				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void setBlogPageDetailDataForAllPatientGenderusingGA4(  AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
							  ().setFieldName("customUser:gender")
							  .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
							 
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			//LOGGER.info(" blogPageDetails RUN REPORT RESPONSE  : : : "+response);

			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				//LOGGER.info("  blogPageDetails RESPONSE RowList  : : : "+response.getRowsList());

				List<AnalyticsExitAndBlogPageDetails> blogPageDetails =new ArrayList<>();
				for (Row row : response.getRowsList()) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							LOGGER.info("pagePath for blog ::: "+map.get(pagePath)+" === "+"RowMatrix Value ::: "+row.getMetricValues(0).getValue());
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						}  else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info(" blogPageDetails BLowView RowMatrix Value  : : : "+row.getMetricValues(0).getValue());

						}
						else {
							detail.setPageCount("0");

						}
						
			
						if (!otherPageThanMapValue) {
							blogPageDetails.add(detail);
						}
					}
				}
				if (blogPageDetails != null && !blogPageDetails.isEmpty()) {
					LOGGER.info("Before Sorting blogPageDetails  : "+blogPageDetails);

					//blogPageDetails.reversesort(a1.getPageCount())-(a2.getPageCount());
			        //Collections.sort(al, Collections.reverseOrder());

						patientAnalyticsData.setBlogPageDetails(blogPageDetails != null && !blogPageDetails.isEmpty() ? blogPageDetails : null);


				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//**********************End Code for Gender**********//
//*************Code For Ethinicity Filter***************************//
	private void setTotalVisitsForAllPatientEthinicityUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addMetrics(Metric.newBuilder().setName("sessions"))
						.addMetrics(Metric.newBuilder().setName("activeusers"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:ethnicity")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				for (Row row : response.getRowsList()) {
					patientAnalyticsData.setTotalVisitCount(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setUniqueVisitorsForAllPatientEthinicityUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:ethnicity")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				for (Row row : response.getRowsList()) {
					if(!row.getDimensionValues(0).getValue().equals("0") || !row.getDimensionValues(0).getValue().equals("(not set)")) {
						uniqueUserCount++;
					}
					patientAnalyticsData.setUniqueUserCount(String.valueOf(uniqueUserCount));
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		private void setPageDetailDataForAllPatientEthinicityusingGA4( AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
					.addMetrics(Metric.newBuilder().setName("averageDuration").setExpression("userEngagementDuration/sessions"))
					.addMetrics(Metric.newBuilder().setName("averageDurationPeruser").setExpression("userEngagementDuration/activeUsers"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
							  ().setFieldName("customUser:ethnicity")
							  .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
							 
					
					.build();

			RunReportResponse response = analyticsData.runReport(request);
            int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsPerPageDetail> analyticsPerPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> wellnessPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> profilePageDetails = new ArrayList<> ();

				for (Row row : response.getRowsList()) {
					AnalyticsPerPageDetail detail = new AnalyticsPerPageDetail();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (profileCount <= 14 && pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageViews(row.getMetricValues(0).getValue());
						}
						
						if (row.getMetricValues(1) != null && row.getMetricValues(1).getValue() != null && !row.getMetricValues(1).getValue().isBlank()) {
							double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
							detail.setTotalTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
						} else {
							detail.setTotalTimeOnPage("0");
						}
						
						if (row.getMetricValues(2) != null && row.getMetricValues(2).getValue() != null && !row.getMetricValues(2).getValue().isBlank()) {
							if (row.getMetricValues(2).getValue().equals("0")) {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(3).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							} else {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(2).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							}
						} else {
							detail.setAvgTimeOnPage("0");
						}
			
						if (!otherPageThanMapValue) {
							if (pagePath.contains("takewellnesscheck/PHQ") || pagePath.contains("takewellnesscheck/GAD")) {
								wellnessPageDetails.add(detail);
							} else if (profileCount <= 14 && pagePath.contains("profiling/")) {
								profilePageDetails.add(detail);
							} else {
								analyticsPerPageDetails.add(detail);
							}
						}
					}
				}
				
				if (wellnessPageDetails != null && !wellnessPageDetails.isEmpty()) {
					wellnessPageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setWellnessPageDetails(wellnessPageDetails != null && !wellnessPageDetails.isEmpty() ? wellnessPageDetails : null);
				}
				if (profilePageDetails != null && !profilePageDetails.isEmpty()) {
					profilePageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setProfilePageDetails(profilePageDetails != null && !profilePageDetails.isEmpty() ? profilePageDetails : null);
				}
				
				patientAnalyticsData.setPerPageDetails(analyticsPerPageDetails != null && !analyticsPerPageDetails.isEmpty() ? analyticsPerPageDetails : null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	private void setExitPageDetailDataForAllPatientEthinicityusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("customUser:page_exit"))
					.addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
					 ().setFieldName("customUser:ethnicity")
					 .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info("RUN REPORT RESPONSE  : : : "+response);
			int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsExitAndBlogPageDetails> exitPageDetails =new ArrayList<>();
				LOGGER.info("responserowlist  : : : "+response.getRowsList());

				for (Row row : response.getRowsList()) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
							detail.setCategory("Pages");
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("WellnessCheck");
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("Profiling");
								LOGGER.info("PageExitProfileCount All user value ::: "+Integer.parseInt(pagePath.split("/")[2]));								
								detail.setPageExitProfileCount(Integer.parseInt(pagePath.split("/")[2]));
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info("RowMatrixValue  : : : "+row.getMetricValues(0).getValue());

						}
						
						
			
						if (!otherPageThanMapValue) {
							exitPageDetails.add(detail);
						}
					}
				}
				if (exitPageDetails != null && !exitPageDetails.isEmpty()) {
		LOGGER.info("Before Sorting ExitPageDetails  : "+exitPageDetails);
		/*
		 * Collections.sort(exitPageDetails, Collections.reverseOrder());
		 * 
		 * LOGGER.info("After Sorting ExitPageDetails  : "+exitPageDetails);
		 */
						patientAnalyticsData.setExitPageDetails(exitPageDetails != null && !exitPageDetails.isEmpty() ? exitPageDetails : null);
						

				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void setBlogPageDetailDataForAllPatientEthinicityusingGA4(  AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
							  ().setFieldName("customUser:ethnicity")
							  .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
							 
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info(" blogPageDetails RUN REPORT RESPONSE  : : : "+response);

			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				LOGGER.info("  blogPageDetails RESPONSE RowList  : : : "+response.getRowsList());

				List<AnalyticsExitAndBlogPageDetails> blogPageDetails =new ArrayList<>();
				for (Row row : response.getRowsList()) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							LOGGER.info("pagePath for blog ::: "+map.get(pagePath)+" === "+"RowMatrix Value ::: "+row.getMetricValues(0).getValue());
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						}  else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info(" blogPageDetails BLowView RowMatrix Value  : : : "+row.getMetricValues(0).getValue());

						}
						else {
							detail.setPageCount("0");

						}
						
			
						if (!otherPageThanMapValue) {
							blogPageDetails.add(detail);
						}
					}
				}
				if (blogPageDetails != null && !blogPageDetails.isEmpty()) {
					LOGGER.info("Before Sorting blogPageDetails  : "+blogPageDetails);

					//blogPageDetails.reversesort(a1.getPageCount())-(a2.getPageCount());
			        //Collections.sort(al, Collections.reverseOrder());

						patientAnalyticsData.setBlogPageDetails(blogPageDetails != null && !blogPageDetails.isEmpty() ? blogPageDetails : null);


				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	//**************END Ethinicity Code **************//

	//*************Code For Gender And Ethnicity Filter***************************//
		private void setTotalVisitsForAllPatientGenderAndEthnicityUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
				PatientAnalyticsData patientAnalyticsData) {
			int uniqueUserCount = 0;
			try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
				RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
						    .addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
							.addMetrics(Metric.newBuilder().setName("sessions"))
							.addMetrics(Metric.newBuilder().setName("activeusers"))
							.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
							 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
							 ().setFieldName("customUser:gender")
							.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender()))))
							 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
							 ().setFieldName("customUser:ethnicity")
									.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity()))))
							.build();

				RunReportResponse response = analyticsData.runReport(request);
				if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
					for (Row row : response.getRowsList()) {
						patientAnalyticsData.setTotalVisitCount(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
					}
				}
				
				
				
			} catch (Exception e) {
				LOGGER.info("Some exception occur inside setTotalVisitsForAllPatientGenderAndEthnicityUsingGA4 ::: "+e.getMessage());
			}
		}
		
		private void setUniqueVisitorsForAllPatientGenderAndEthnicityUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
				PatientAnalyticsData patientAnalyticsData) {
			int uniqueUserCount = 0;
			try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
				RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
						    .addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
						    .addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
							.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
							 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
							 ().setFieldName("customUser:gender")
							.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender()))))
							 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
							 ().setFieldName("customUser:ethnicity")
									.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity()))))
							.build();

				RunReportResponse response = analyticsData.runReport(request);
				if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
					for (Row row : response.getRowsList()) {
						if(!row.getDimensionValues(0).getValue().equals("0") || !row.getDimensionValues(0).getValue().equals("(not set)")) {
							uniqueUserCount++;
						}
						patientAnalyticsData.setUniqueUserCount(String.valueOf(uniqueUserCount));
					}
				}
				
				
				
			} catch (Exception e) {
				LOGGER.info("Some exception occur inside setUniqueVisitorsForAllPatientGenderAndEthnicityUsingGA4 ::: "+e.getMessage());
			}
		}
		
			private void setPageDetailDataForAllPatientGenderAndEthnicityusingGA4( AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
			try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("/", "Login Page");
				map.put("/forgottencode", "Forget Code");
				map.put("/home", "Home");
				map.put("/aboutus", "About Us");
				map.put("/wellnesscheck", "Wellness Check");
				map.put("/profilingquestion", "Profiling Question");
				map.put("/blogs", "Blogs");
				map.put("/faq", "FAQ");
				map.put("/bookingschedule", "Book Schedule");
				map.put("/cookie", "Cookie");
				map.put("/privacy", "Privacy");
				map.put("/terms", "Terms");
				map.put("/aup", "AUP");
				map.put("/contactus", "Contact Us");
				map.put("/myprofile", "My Profile");
				map.put("/support", "Support");
				map.put("/myprofile/mywellnesscheck", "My Wellness Check");
				map.put("/myprofile/myschedule", "My Schedule");
				map.put("/myprofile/myprofiling", "Profiling Question");
				map.put("/myprofile/myprofilesetting", "Profile Details");
				
				RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
						.addDimensions(Dimension.newBuilder().setName("pagePath"))
						.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
						.addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
						.addMetrics(Metric.newBuilder().setName("screenPageViews"))
						.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
						.addMetrics(Metric.newBuilder().setName("averageDuration").setExpression("userEngagementDuration/sessions"))
						.addMetrics(Metric.newBuilder().setName("averageDurationPeruser").setExpression("userEngagementDuration/activeUsers"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						().setFieldName("customUser:gender")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:ethnicity")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
						
						.build();

				RunReportResponse response = analyticsData.runReport(request);
	            int profileCount = 0;
				if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
					List<AnalyticsPerPageDetail> analyticsPerPageDetails = new ArrayList<> ();
					List<AnalyticsPerPageDetail> wellnessPageDetails = new ArrayList<> ();
					List<AnalyticsPerPageDetail> profilePageDetails = new ArrayList<> ();

					
					for (Row row : response.getRowsList()) {
						AnalyticsPerPageDetail detail = new AnalyticsPerPageDetail();
						boolean otherPageThanMapValue = false;
						if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
							String pagePath = row.getDimensionValues(0).getValue();
							if (map.get(pagePath) != null) {
								detail.setPagePath(map.get(pagePath));
								otherPageThanMapValue = false;
							} else if (pagePath.contains("takewellnesscheck/")) {
								if (pagePath.split("/").length > 2) {
									detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
									otherPageThanMapValue = false;
								} else {
									otherPageThanMapValue = true;
								}
							} else if (pagePath.contains("profiling/")) {
								profileCount++;
								if (profileCount <= 14 && pagePath.split("/").length > 2) {
									//if(pagePath.split("/")[2]>14)
									detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
									otherPageThanMapValue = false;
								} else {
									otherPageThanMapValue = true;
								}
							} else {
								otherPageThanMapValue = true;
							}
							
							if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
								detail.setPageViews(row.getMetricValues(0).getValue());
							}
							
							if (row.getMetricValues(1) != null && row.getMetricValues(1).getValue() != null && !row.getMetricValues(1).getValue().isBlank()) {
								double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
								detail.setTotalTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
							} else {
								detail.setTotalTimeOnPage("0");
							}
							
							if (row.getMetricValues(2) != null && row.getMetricValues(2).getValue() != null && !row.getMetricValues(2).getValue().isBlank()) {
								if (row.getMetricValues(2).getValue().equals("0")) {
									double timeInSeconds = Double.parseDouble(row.getMetricValues(3).getValue());
									detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
								} else {
									double timeInSeconds = Double.parseDouble(row.getMetricValues(2).getValue());
									detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
								}
							} else {
								detail.setAvgTimeOnPage("0");
							}
				
							if (!otherPageThanMapValue) {
								if (pagePath.contains("takewellnesscheck/PHQ") || pagePath.contains("takewellnesscheck/GAD")) {
									wellnessPageDetails.add(detail);
								} else if (profileCount <= 14 && pagePath.contains("profiling/")) {
									profilePageDetails.add(detail);
								} else {
									analyticsPerPageDetails.add(detail);
								}
							}
						}
					}
					
					if (wellnessPageDetails != null && !wellnessPageDetails.isEmpty()) {
						wellnessPageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
								(Integer.parseInt(a2.getPagePath().split("-")[1]))));
						patientAnalyticsData.setWellnessPageDetails(wellnessPageDetails != null && !wellnessPageDetails.isEmpty() ? wellnessPageDetails : null);
					}
					if (profilePageDetails != null && !profilePageDetails.isEmpty()) {
						profilePageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
								(Integer.parseInt(a2.getPagePath().split("-")[1]))));
						patientAnalyticsData.setProfilePageDetails(profilePageDetails != null && !profilePageDetails.isEmpty() ? profilePageDetails : null);
					}
					
					patientAnalyticsData.setPerPageDetails(analyticsPerPageDetails != null && !analyticsPerPageDetails.isEmpty() ? analyticsPerPageDetails : null);
				}

			} catch (Exception e) {
				LOGGER.info("Some exception occur inside setPageDetailDataForAllPatientGenderAndEthnicityusingGA4 ::: "+e.getMessage());
			}
		}


		private void setExitPageDetailDataForAllPatientGenderAndEthnicityusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
			try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("/", "Login Page");
				map.put("/forgottencode", "Forget Code");
				map.put("/home", "Home");
				map.put("/aboutus", "About Us");
				map.put("/wellnesscheck", "Wellness Check");
				map.put("/profilingquestion", "Profiling Question");
				map.put("/blogs", "Blogs");
				map.put("/faq", "FAQ");
				map.put("/bookingschedule", "Book Schedule");
				map.put("/cookie", "Cookie");
				map.put("/privacy", "Privacy");
				map.put("/terms", "Terms");
				map.put("/aup", "AUP");
				map.put("/contactus", "Contact Us");
				map.put("/myprofile", "My Profile");
				map.put("/support", "Support");
				map.put("/myprofile/mywellnesscheck", "My Wellness Check");
				map.put("/myprofile/myschedule", "My Schedule");
				map.put("/myprofile/myprofiling", "Profiling Question");
				map.put("/myprofile/myprofilesetting", "Profile Details");
				map.put( "/blogs/blog1", "Blog1");
				map.put("/blogs/blog2", "Blog2");
				map.put("/blogs/blog3", "Blog3");
				map.put("/blogs/blog4", "Blog4");

				RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
						.addDimensions(Dimension.newBuilder().setName("customUser:page_exit"))
						.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
						.addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
						.addMetrics(Metric.newBuilder().setName("screenPageViews"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:gender")
						 .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender()))))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:ethnicity")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity()))))
						.build();

				RunReportResponse response = analyticsData.runReport(request);
				LOGGER.info("RUN REPORT RESPONSE  : : : "+response);
				int profileCount = 0;
				if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
					List<AnalyticsExitAndBlogPageDetails> exitPageDetails =new ArrayList<>();
					LOGGER.info("responserowlist  : : : "+response.getRowsList());

					
					for (Row row : response.getRowsList()) {
						AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
						boolean otherPageThanMapValue = false;
						if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
							String pagePath = row.getDimensionValues(0).getValue();
							if (map.get(pagePath) != null) {
								detail.setPagePath(map.get(pagePath));
								otherPageThanMapValue = false;
								detail.setCategory("Pages");
							} else if (pagePath.contains("takewellnesscheck/")) {
								if (pagePath.split("/").length > 2) {
									detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
									otherPageThanMapValue = false;
									detail.setCategory("WellnessCheck");
								} else {
									otherPageThanMapValue = true;
								}
							} else if (pagePath.contains("profiling/")) {
								profileCount++;
								if (pagePath.split("/").length > 2) {
									//if(pagePath.split("/")[2]>14)
									detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
									otherPageThanMapValue = false;
									detail.setCategory("Profiling");
									LOGGER.info("PageExitProfileCount All user value ::: "+Integer.parseInt(pagePath.split("/")[2]));								
									detail.setPageExitProfileCount(Integer.parseInt(pagePath.split("/")[2]));
								} else {
									otherPageThanMapValue = true;
								}
							} else {
								otherPageThanMapValue = true;
							}
							
							if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
								detail.setPageCount(row.getMetricValues(0).getValue());
								LOGGER.info("RowMatrixValue  : : : "+row.getMetricValues(0).getValue());

							}
							
							
				
							if (!otherPageThanMapValue) {
								exitPageDetails.add(detail);
							}
						}
					}
					if (exitPageDetails != null && !exitPageDetails.isEmpty()) {
			LOGGER.info("Before Sorting ExitPageDetails  : "+exitPageDetails);
			/*
			 * Collections.sort(exitPageDetails, Collections.reverseOrder());
			 * 
			 * LOGGER.info("After Sorting ExitPageDetails  : "+exitPageDetails);
			 */
							patientAnalyticsData.setExitPageDetails(exitPageDetails != null && !exitPageDetails.isEmpty() ? exitPageDetails : null);
							

					}
					
				}

			} catch (Exception e) {
				LOGGER.info("Some exception occur inside setExitPageDetailDataForAllPatientGenderAndEthnicityusingGA4 ::: "+e.getMessage());
			}
		}
		private void setBlogPageDetailDataForAllPatientGenderAndEthnicityusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
			try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put( "/blogs/blog1", "Blog1");
				map.put("/blogs/blog2", "Blog2");
				map.put("/blogs/blog3", "Blog3");
				map.put("/blogs/blog4", "Blog4");

				RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
						.addDimensions(Dimension.newBuilder().setName("pagePath"))
						.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
						.addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
						.addMetrics(Metric.newBuilder().setName("screenPageViews"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:gender")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
						.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:ethnicity")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
						.build();

				RunReportResponse response = analyticsData.runReport(request);
				LOGGER.info(" blogPageDetails RUN REPORT RESPONSE  : : : "+response);

				if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
					LOGGER.info("  blogPageDetails RESPONSE RowList  : : : "+response.getRowsList());

					List<AnalyticsExitAndBlogPageDetails> blogPageDetails =new ArrayList<>();
					for (Row row : response.getRowsList()) {
						AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
						boolean otherPageThanMapValue = false;
						if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
							String pagePath = row.getDimensionValues(0).getValue();
							if (map.get(pagePath) != null) {
								LOGGER.info("pagePath for blog ::: "+map.get(pagePath)+" === "+"RowMatrix Value ::: "+row.getMetricValues(0).getValue());
								detail.setPagePath(map.get(pagePath));
								otherPageThanMapValue = false;
							}  else {
								otherPageThanMapValue = true;
							}
							
							if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
								detail.setPageCount(row.getMetricValues(0).getValue());
								LOGGER.info(" blogPageDetails BLowView RowMatrix Value  : : : "+row.getMetricValues(0).getValue());

							}
							else {
								detail.setPageCount("0");

							}
							if (!otherPageThanMapValue) {
								blogPageDetails.add(detail);
							}
						}
					}
					if (blogPageDetails != null && !blogPageDetails.isEmpty()) {
						LOGGER.info("Before Sorting blogPageDetails  : "+blogPageDetails);

						//blogPageDetails.reversesort(a1.getPageCount())-(a2.getPageCount());
				        //Collections.sort(al, Collections.reverseOrder());

							patientAnalyticsData.setBlogPageDetails(blogPageDetails != null && !blogPageDetails.isEmpty() ? blogPageDetails : null);
					}
					
				}

			} catch (Exception e) {
				LOGGER.info("Some excpetionn occur inside setBlogPageDetailDataForAllPatientGenderAndEthnicityusingGA4 ::: "+e.getMessage());
			}
		}

		//**********************End Code for Gender And Ethnicity Filter**********//
	
	//*************Code For DOB And Gender And Ethnicity Filter***************************//
	private void setTotalVisitsForAllPatientDOBAndGenderAndEthnicityUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					    .addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
						.addMetrics(Metric.newBuilder().setName("sessions"))
						.addMetrics(Metric.newBuilder().setName("activeusers"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:gender")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender()))))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:ethnicity")
								.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity()))))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(0).getValue().equals("undefined")
							&& !x.getDimensionValues(0).getValue().equals("0")
							&& !x.getDimensionValues(0).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(0).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(0).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				for (Row row : rows) {
					patientAnalyticsData.setTotalVisitCount(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setUniqueVisitorsForAllPatientDOBAndGenderAndEthnicityUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
					    .addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					    .addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:gender")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender()))))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:ethnicity")
								.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity()))))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				for (Row row : rows) {
					if(!row.getDimensionValues(0).getValue().equals("0") || !row.getDimensionValues(0).getValue().equals("(not set)")) {
						uniqueUserCount++;
					}
					patientAnalyticsData.setUniqueUserCount(String.valueOf(uniqueUserCount));
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		private void setPageDetailDataForAllPatientDOBAndGenderAndEthnicityusingGA4( AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
					.addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
					.addMetrics(Metric.newBuilder().setName("averageDuration").setExpression("userEngagementDuration/sessions"))
					.addMetrics(Metric.newBuilder().setName("averageDurationPeruser").setExpression("userEngagementDuration/activeUsers"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
					().setFieldName("customUser:gender")
					.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:ethnicity")
					.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
					
					.build();

			RunReportResponse response = analyticsData.runReport(request);
            int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsPerPageDetail> analyticsPerPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> wellnessPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> profilePageDetails = new ArrayList<> ();

				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				
				for (Row row : rows) {
					AnalyticsPerPageDetail detail = new AnalyticsPerPageDetail();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (profileCount <= 14 && pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageViews(row.getMetricValues(0).getValue());
						}
						
						if (row.getMetricValues(1) != null && row.getMetricValues(1).getValue() != null && !row.getMetricValues(1).getValue().isBlank()) {
							double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
							detail.setTotalTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
						} else {
							detail.setTotalTimeOnPage("0");
						}
						
						if (row.getMetricValues(2) != null && row.getMetricValues(2).getValue() != null && !row.getMetricValues(2).getValue().isBlank()) {
							if (row.getMetricValues(2).getValue().equals("0")) {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(3).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							} else {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(2).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							}
						} else {
							detail.setAvgTimeOnPage("0");
						}
			
						if (!otherPageThanMapValue) {
							if (pagePath.contains("takewellnesscheck/PHQ") || pagePath.contains("takewellnesscheck/GAD")) {
								wellnessPageDetails.add(detail);
							} else if (profileCount <= 14 && pagePath.contains("profiling/")) {
								profilePageDetails.add(detail);
							} else {
								analyticsPerPageDetails.add(detail);
							}
						}
					}
				}
				
				if (wellnessPageDetails != null && !wellnessPageDetails.isEmpty()) {
					wellnessPageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setWellnessPageDetails(wellnessPageDetails != null && !wellnessPageDetails.isEmpty() ? wellnessPageDetails : null);
				}
				if (profilePageDetails != null && !profilePageDetails.isEmpty()) {
					profilePageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setProfilePageDetails(profilePageDetails != null && !profilePageDetails.isEmpty() ? profilePageDetails : null);
				}
				
				patientAnalyticsData.setPerPageDetails(analyticsPerPageDetails != null && !analyticsPerPageDetails.isEmpty() ? analyticsPerPageDetails : null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void setExitPageDetailDataForAllPatientDOBAndGenderAndEthnicityusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("customUser:page_exit"))
					.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
					.addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
					 ().setFieldName("customUser:gender")
					 .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender()))))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:ethnicity")
					.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity()))))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info("RUN REPORT RESPONSE  : : : "+response);
			int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsExitAndBlogPageDetails> exitPageDetails =new ArrayList<>();
				LOGGER.info("responserowlist  : : : "+response.getRowsList());

				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				
				for (Row row : rows) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
							detail.setCategory("Pages");
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("WellnessCheck");
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("Profiling");
								LOGGER.info("PageExitProfileCount All user value ::: "+Integer.parseInt(pagePath.split("/")[2]));								
								detail.setPageExitProfileCount(Integer.parseInt(pagePath.split("/")[2]));
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info("RowMatrixValue  : : : "+row.getMetricValues(0).getValue());

						}
						
						
			
						if (!otherPageThanMapValue) {
							exitPageDetails.add(detail);
						}
					}
				}
				if (exitPageDetails != null && !exitPageDetails.isEmpty()) {
		LOGGER.info("Before Sorting ExitPageDetails  : "+exitPageDetails);
		/*
		 * Collections.sort(exitPageDetails, Collections.reverseOrder());
		 * 
		 * LOGGER.info("After Sorting ExitPageDetails  : "+exitPageDetails);
		 */
						patientAnalyticsData.setExitPageDetails(exitPageDetails != null && !exitPageDetails.isEmpty() ? exitPageDetails : null);
						

				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void setBlogPageDetailDataForAllPatientDOBAndGenderAndEthnicityusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
					.addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:gender")
					.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
					.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:ethnicity")
					.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info(" blogPageDetails RUN REPORT RESPONSE  : : : "+response);

			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				LOGGER.info("  blogPageDetails RESPONSE RowList  : : : "+response.getRowsList());

				List<AnalyticsExitAndBlogPageDetails> blogPageDetails =new ArrayList<>();
				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				for (Row row : rows) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							LOGGER.info("pagePath for blog ::: "+map.get(pagePath)+" === "+"RowMatrix Value ::: "+row.getMetricValues(0).getValue());
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						}  else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info(" blogPageDetails BLowView RowMatrix Value  : : : "+row.getMetricValues(0).getValue());

						}
						else {
							detail.setPageCount("0");

						}
						if (!otherPageThanMapValue) {
							blogPageDetails.add(detail);
						}
					}
				}
				if (blogPageDetails != null && !blogPageDetails.isEmpty()) {
					LOGGER.info("Before Sorting blogPageDetails  : "+blogPageDetails);

					//blogPageDetails.reversesort(a1.getPageCount())-(a2.getPageCount());
			        //Collections.sort(al, Collections.reverseOrder());

						patientAnalyticsData.setBlogPageDetails(blogPageDetails != null && !blogPageDetails.isEmpty() ? blogPageDetails : null);
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//**********************End Code for DOB And Gender And Ethnicity Filter**********//	
private void setTotalVisitAndTotalTimeOnWebsiteUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
	try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
		RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
					.addMetrics(Metric.newBuilder().setName("sessions"))
					.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString())
					.setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:crm_id")
				    .setStringFilter(StringFilter.newBuilder().setValue(String.valueOf(analyticsReportingQueryData.getUserId())))))
					.build();

		RunReportResponse response = analyticsData.runReport(request);
		if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
			for (Row row : response.getRowsList()) {
				patientAnalyticsData.setTotalVisitCount(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
				if (row.getMetricValues(1) != null) {
					double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
					patientAnalyticsData.setTotalTimeUserSpentOnWebsite(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
				}
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}

private void setExitPageDetailDataForSingleUserusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
	try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("/", "Login Page");
		map.put("/forgottencode", "Forget Code");
		map.put("/home", "Home");
		map.put("/aboutus", "About Us");
		map.put("/wellnesscheck", "Wellness Check");
		map.put("/profilingquestion", "Profiling Question");
		map.put("/blogs", "Blogs");
		map.put("/faq", "FAQ");
		map.put("/bookingschedule", "Book Schedule");
		map.put("/cookie", "Cookie");
		map.put("/privacy", "Privacy");
		map.put("/terms", "Terms");
		map.put("/aup", "AUP");
		map.put("/contactus", "Contact Us");
		map.put("/myprofile", "My Profile");
		map.put("/support", "Support");
		map.put("/myprofile/mywellnesscheck", "My Wellness Check");
		map.put("/myprofile/myschedule", "My Schedule");
		map.put("/myprofile/myprofiling", "Profiling Question");
		map.put("/myprofile/myprofilesetting", "Profile Details");
		map.put( "/blogs/blog1", "Blog1");
		map.put("/blogs/blog2", "Blog2");
		map.put("/blogs/blog3", "Blog3");
		map.put("/blogs/blog4", "Blog4");

		RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
				.addDimensions(Dimension.newBuilder().setName("customUser:page_exit"))
				.addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))

				.addMetrics(Metric.newBuilder().setName("screenPageViews"))
				.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
				 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:crm_id")
						  .setStringFilter(StringFilter.newBuilder().setValue(String.valueOf(analyticsReportingQueryData.getUserId())))))
	     
				.build();

		RunReportResponse response = analyticsData.runReport(request);
		//LOGGER.info(" Single User RUN REPORT RESPONSE  : : : "+response);

		if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
			List<AnalyticsExitAndBlogPageDetails> exitPageDetails =new ArrayList<>();
			//LOGGER.info(" Single User responserowlist  : : : "+response.getRowsList());

			for (Row row : response.getRowsList()) {
				AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
				boolean otherPageThanMapValue = false;
				if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
					String pagePath = row.getDimensionValues(0).getValue();
					if (map.get(pagePath) != null) {
						detail.setPagePath(map.get(pagePath));
						otherPageThanMapValue = false;
						detail.setCategory("Pages");
					} else if (pagePath.contains("takewellnesscheck/")) {
						if (pagePath.split("/").length > 2) {
							detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
							otherPageThanMapValue = false;
							detail.setCategory("WellnessCheck");
						} else {
							otherPageThanMapValue = true;
						}
					} else if (pagePath.contains("profiling/")) {
						if (pagePath.split("/").length > 2) {
							detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
							otherPageThanMapValue = false;
							detail.setCategory("Profiling");
							LOGGER.info("PageExitProfileCount Single user value ::: "+Integer.parseInt(pagePath.split("/")[2]));
							detail.setPageExitProfileCount(Integer.parseInt(pagePath.split("/")[2]));
						} else {
							otherPageThanMapValue = true;
						}
					} else {
						otherPageThanMapValue = true;
					}
					
					if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
						detail.setPageCount(row.getMetricValues(0).getValue());
						//LOGGER.info(" Single user RowMatrixValue  : : : "+row.getMetricValues(0).getValue());

					}
											if (!otherPageThanMapValue) {
						exitPageDetails.add(detail);
					}
				}
			}
			if (exitPageDetails != null && !exitPageDetails.isEmpty()) {
	       // LOGGER.info("Single user Sorting ExitPageDetails  : "+exitPageDetails);
			patientAnalyticsData.setExitPageDetails(exitPageDetails != null && !exitPageDetails.isEmpty() ? exitPageDetails : null);
					

			}
			
		}

	} catch (Exception e) {
		e.printStackTrace();
	}
}


private void setBlogViewForSingleUserusingGA4( AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
	try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
		Map<String, String> map = new HashMap<String, String>();
		map.put( "/blogs/blog1", "Blog1");
		map.put("/blogs/blog2", "Blog2");
		map.put("/blogs/blog3", "Blog3");
		map.put("/blogs/blog4", "Blog4");

				RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
				.addDimensions(Dimension.newBuilder().setName("pagePath"))
				.addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
				.addMetrics(Metric.newBuilder().setName("screenPageViews"))
				.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
				.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:crm_id")
				 .setStringFilter(StringFilter.newBuilder().setValue(String.valueOf(analyticsReportingQueryData.getUserId())))))
			     
				.build();

		RunReportResponse response = analyticsData.runReport(request);
		LOGGER.info(" blogPageDetailsfor single user RUN REPORT RESPONSE  : : : "+response);

		if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
			LOGGER.info("  blogPageDetails RESPONSE RowList  : : : "+response.getRowsList());

			List<AnalyticsExitAndBlogPageDetails> blogPageDetails =new ArrayList<>();
			for (Row row : response.getRowsList()) {
				AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
				boolean otherPageThanMapValue = false;
				if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
					String pagePath = row.getDimensionValues(0).getValue();
					if (map.get(pagePath) != null) {
						detail.setPagePath(map.get(pagePath));
						otherPageThanMapValue = false;
					}  else {
						otherPageThanMapValue = true;
					}
				if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
						detail.setPageCount(row.getMetricValues(0).getValue());
						LOGGER.info(" blogPageDetails single user RowMatrix Value  : : : "+row.getMetricValues(0).getValue());

					}
					else if((row.getMetricValues(0).getValue() == null ))
					{
						detail.setPageCount("0");
					}
					if (!otherPageThanMapValue) {
						blogPageDetails.add(detail);
					}
				}
			}
			if (blogPageDetails != null && !blogPageDetails.isEmpty()) {
				LOGGER.info("single user Sorting blogPageDetails  : "+blogPageDetails);
				patientAnalyticsData.setBlogPageDetails(blogPageDetails != null && !blogPageDetails.isEmpty() ? blogPageDetails : null);
			}
			
		}

	} catch (Exception e) {
		e.printStackTrace();
	}
}

//** End Code of Single User Analytics***************************//

								//******************* Code For SingleUSerCsv *****************//
	private void setTotalVisitAndTotalTimeForSingleUser(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsSingleUserCSV patientAnalyticsSingleUserCSV) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
						.addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
						.addMetrics(Metric.newBuilder().setName("sessions"))
						.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString())
						.setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:crm_id")
					    .setStringFilter(StringFilter.newBuilder().setValue(String.valueOf(analyticsReportingQueryData.getUserId())))))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info("setTotalVisitAndTotalTimeForSingleUser rows count :::  "+response.getRowsCount());
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				for (Row row : response.getRowsList()) {
					patientAnalyticsSingleUserCSV.setTotalVisit(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
					LOGGER.info("row.getMetricValues(1) value ::: "+row.getMetricValues(1));
					if (row.getMetricValues(1) != null) {
						double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
						LOGGER.info("timeInMinutes value ::: "+timeInMinutes);
						patientAnalyticsSingleUserCSV.setTimeSpentonEachPage(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
					
						LOGGER.info("setTotalVisitAndTotalTimeForSingleUserVISIT   "+patientAnalyticsSingleUserCSV.getTimeSpentonEachPage());

					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("Some exception occur inside setTotalVisitAndTotalTimeForSingleUser of PatientAnalyticsServiceImpl class ::: "+e.getMessage());
			e.printStackTrace();
		}
	}

	
	
	private void setPageDetailDataForSingleUserCSV(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsSingleUserCSV patientAnalyticsSingleUserCSV) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:date_time"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
					.addMetrics(Metric.newBuilder().setName("averageDuration")
							.setExpression("userEngagementDuration/engagedSessions"))
					.addDateRanges(
							DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString())
									.setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					.setDimensionFilter(FilterExpression.newBuilder()
							.setFilter(Filter.newBuilder().setFieldName("customUser:crm_id")
									.setStringFilter(StringFilter.newBuilder()
											.setValue(String.valueOf(analyticsReportingQueryData.getUserId())))))
					.build();
			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info("  Per page PAGESingle User RUN REPORT RESPONSE rows count ::: " + response.getRowsCount());

			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				LOGGER.info("  Wellness RowList  : : : " + response.getRowsList());

				List<AnalyticsPerPageDetail> analyticsPerPageDetails = new ArrayList<>();
				List<AnalyticsPerPageDetail> wellnessPageDetails = new ArrayList<>();
				List<AnalyticsPerPageDetail> profilePageDetails = new ArrayList<>();
				int profileQuestionCount = 1;
				for (Row row : response.getRowsList()) {
					AnalyticsPerPageDetail detail = new AnalyticsPerPageDetail();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(1) != null && row.getDimensionValues(1).getValue() != null
							&& !row.getDimensionValues(1).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(1).getValue();
						String pageVisitTime = row.getDimensionValues(2).getValue().isEmpty()
								|| row.getDimensionValues(2).getValue().equalsIgnoreCase("(not set)") ? ""
										: row.getDimensionValues(2).getValue();

						detail.setPageVisitTime(pageVisitTime);
						// detail.setPageVisitTime(pageVisitTime!= null && !pageVisitTime.isBlank() ?
						// pageVisitTime : pageVisitTime);
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank()
										? pagePath.split("/")[2]
										: pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {

							// profileQuestionCount++;
							 if (profileQuestionCount <= 14 && pagePath.split("/").length > 2) {

							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank()
										? ("Q-" + pagePath.split("/")[2])
										: pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}

						}
						}

						//
						else {
							otherPageThanMapValue = true;
						}

						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null
								&& !row.getMetricValues(0).getValue().isBlank()) {

							detail.setPageViews(row.getMetricValues(0).getValue());
						}

						if (row.getMetricValues(1) != null && row.getMetricValues(1).getValue() != null
								&& !row.getMetricValues(1).getValue().isBlank()) {
							double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
							detail.setTotalTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
						} else {
							detail.setTotalTimeOnPage("0");
						}

						if (row.getMetricValues(2) != null && row.getMetricValues(2).getValue() != null
								&& !row.getMetricValues(2).getValue().isBlank()) {
							if (row.getMetricValues(2).getValue().equals("0")) {
								detail.setAvgTimeOnPage("0");
							} else {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(2).getValue());
								detail.setAvgTimeOnPage(
										String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							}
						} else {
							detail.setAvgTimeOnPage("0");
						}

						if (!otherPageThanMapValue) {
							if (pagePath.contains("takewellnesscheck/PHQ")
									|| pagePath.contains("takewellnesscheck/GAD")) {
								wellnessPageDetails.add(detail);

							} else if (pagePath.contains("profiling/")) {
								profilePageDetails.add(detail);

							}

							else {
								analyticsPerPageDetails.add(detail);
							}

						}
					}
				}
				if (wellnessPageDetails != null && !wellnessPageDetails.isEmpty()) {

					wellnessPageDetails.sort((AnalyticsPerPageDetail a1,
							AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])
									- (Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsSingleUserCSV.setWellnessPageDetails(wellnessPageDetails);

					LOGGER.info("wELLNESS PAGE DETAILS time spent on each page  " + wellnessPageDetails.size());

				}
				if (profilePageDetails != null && !profilePageDetails.isEmpty()) {

					profilePageDetails.sort((AnalyticsPerPageDetail a1,
							AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])
									- (Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsSingleUserCSV.setProfilePageDetails(
							profilePageDetails != null && !profilePageDetails.isEmpty() ? profilePageDetails : null);
				}

				patientAnalyticsSingleUserCSV.setPerPageDetails(
						analyticsPerPageDetails != null && !analyticsPerPageDetails.isEmpty() ? analyticsPerPageDetails
								: null);

			}
		} catch (Exception e) {
			LOGGER.info(
					"Some exception occur inside setPageDetailDataForSingleUserCSV method of PatientAnalyticsServiceImpl ::: "
							+ e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public UserAnalyticsBookingDetailsList getAnalyticsDataForParticularCSVUserForBooking(
			AnalyticsReportingQueryData analyticsReportingQueryData) {
		
		UserAnalyticsBookingDetailsList obj = null;
		

		try {
			LOGGER.info("USER BOOKING ANAYTICS DETAIL userId ::: "+analyticsReportingQueryData.getUserId());
			obj = RestTemplateUtil.getMethodForDownloadCSVForSingleUser(doctorServiceUrl + "/doctor/fetchSingleUserTotalBookingBetweenDateRange", 
					convertObjectToString(analyticsReportingQueryData), restTemplate);
			LOGGER.info("USER BOOKING ANAYTICS DETAIL ::: "+obj);
	} 
catch (Exception e) {
	LOGGER.info("Some exception occur while getting booking details for single user csv ::: "+e.getMessage());
		e.printStackTrace();
	} 
		return obj;
	}
	private void setExitPageDetailDataForSingleUserCSV(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsSingleUserCSV patientAnalyticsSingleUserCSV) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");
		//https://chqafront.techment.com/blogs/blog2


			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("customUser:page_exit"))
					.addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))

					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:crm_id")
							  .setStringFilter(StringFilter.newBuilder().setValue(String.valueOf(analyticsReportingQueryData.getUserId())))))
		     
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			//LOGGER.info("  EXIT PAGESingle User RUN REPORT RESPONSE rows count ::: "+response.getRowsCount());

			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsExitAndBlogPageDetails> exitPageDetails =new ArrayList<>();
				//LOGGER.info("  EXIT PAGE Single User responserowlist  ::: "+response.getRowsList());

				for (Row row : response.getRowsList()) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
							detail.setCategory("Pages");
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("WellnessCheck");
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("Profiling");
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							//LOGGER.info(" Single user RowMatrixValue  : : : "+row.getMetricValues(0).getValue());

						}
												if (!otherPageThanMapValue) {
							exitPageDetails.add(detail);
						}
					}
				}
				if (exitPageDetails != null && !exitPageDetails.isEmpty()) {
		       // LOGGER.info("Single user Sorting ExitPageDetails  : "+exitPageDetails);
					patientAnalyticsSingleUserCSV.setExitPageDetails(exitPageDetails != null && !exitPageDetails.isEmpty() ? exitPageDetails : null);
						

				}
				
			}

		} catch (Exception e) {
			LOGGER.info("Some error occur inside setExitPageDetailDataForSingleUserCSV method of PatientAnalyticsServiceImpl class ::: "+e.getMessage());
			e.printStackTrace();
		}
	}
	

	private void setBlogViewForSingleUserCSV( AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsSingleUserCSV  patientAnalyticsSingleUserCSV) {
	try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
		Map<String, String> map = new HashMap<String, String>();
		map.put( "/blogs/blog1", "Blog1");
		map.put("/blogs/blog2", "Blog2");
		map.put("/blogs/blog3", "Blog3");
		map.put("/blogs/blog4", "Blog4");
		///blogs/blog1
				RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
				.addDimensions(Dimension.newBuilder().setName("pagePath"))
				.addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
				.addMetrics(Metric.newBuilder().setName("screenPageViews"))
				.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
				.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:crm_id")
				 .setStringFilter(StringFilter.newBuilder().setValue(String.valueOf(analyticsReportingQueryData.getUserId())))))
				 .build();

		RunReportResponse response = analyticsData.runReport(request);
		//LOGGER.info(" blogPageDetailsfor single user RUN REPORT RESPONSE rows count ::: "+response.getRowsCount());

		if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
		//	LOGGER.info("  blogPageDetails RESPONSE RowList  : : : "+response.getRowsList());

			List<AnalyticsExitAndBlogPageDetails> blogPageDetails =new ArrayList<>();
			for (Row row : response.getRowsList()) {
				AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
				boolean otherPageThanMapValue = false;
				if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
					String pagePath = row.getDimensionValues(0).getValue();
                    LOGGER.info("PagePath value for blog page detail ::: "+map.get(pagePath));
					if (map.get(pagePath) != null) {
						detail.setPagePath(map.get(pagePath));
						otherPageThanMapValue = false;
					}  else {
						otherPageThanMapValue = true;
					}
					if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
						detail.setPageCount(row.getMetricValues(0).getValue());
						LOGGER.info(" blogPageDetails single user RowMatrix Value  ::: "+row.getMetricValues(0).getValue());

					}
					else if((row.getMetricValues(0).getValue() == null ))
					{
						detail.setPageCount("0");
					}
					
					LOGGER.info("  BLOG PAGE DETAIL otherPageThanMapValue  "+otherPageThanMapValue);
				if (!otherPageThanMapValue) {
						blogPageDetails.add(detail);
					}
				}
			}
			
			if (blogPageDetails != null && !blogPageDetails.isEmpty()) {
				LOGGER.info("single user Sorting blogPageDetails  : "+blogPageDetails);
				patientAnalyticsSingleUserCSV.setBlogPageDetails(blogPageDetails != null && !blogPageDetails.isEmpty() ? blogPageDetails : null);
			}
			
		}

	} catch (Exception e) {
		LOGGER.info("Some exception occur inside setBlogViewForSingleUserCSV method of PatientAnalyticsServiceImpl ::: "+e.getMessage());
		e.printStackTrace();
	}
}
//*******************End Single USerCSv Code********************//

	//******************DOB analytics changes start*******************//
	private void setTotalVisitsForAllPatientDOBUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addDimensions(Dimension.newBuilder().setName("customUser:dob"))
						.addMetrics(Metric.newBuilder().setName("sessions"))
						.addMetrics(Metric.newBuilder().setName("activeusers"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(0).getValue().equals("undefined")
							&& !x.getDimensionValues(0).getValue().equals("0")
							&& !x.getDimensionValues(0).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(0).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(0).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				for (Row row : rows) {
					patientAnalyticsData.setTotalVisitCount(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setUniqueVisitorsForAllPatientDOBUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
					    .addDimensions(Dimension.newBuilder().setName("customUser:dob"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				for (Row row : rows) {
					if(!row.getDimensionValues(0).getValue().equals("0") || !row.getDimensionValues(0).getValue().equals("(not set)")) {
						uniqueUserCount++;
					}
					patientAnalyticsData.setUniqueUserCount(String.valueOf(uniqueUserCount));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setPageDetailDataForAllPatientDOBusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
					.addMetrics(Metric.newBuilder().setName("averageDuration").setExpression("userEngagementDuration/sessions"))
					.addMetrics(Metric.newBuilder().setName("averageDurationPeruser").setExpression("userEngagementDuration/activeUsers"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
            int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsPerPageDetail> analyticsPerPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> wellnessPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> profilePageDetails = new ArrayList<> ();

				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				
				for (Row row : rows) {
					AnalyticsPerPageDetail detail = new AnalyticsPerPageDetail();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (profileCount <= 14 && pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageViews(row.getMetricValues(0).getValue());
						}
						
						if (row.getMetricValues(1) != null && row.getMetricValues(1).getValue() != null && !row.getMetricValues(1).getValue().isBlank()) {
							double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
							detail.setTotalTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
						} else {
							detail.setTotalTimeOnPage("0");
						}
						
						if (row.getMetricValues(2) != null && row.getMetricValues(2).getValue() != null && !row.getMetricValues(2).getValue().isBlank()) {
							if (row.getMetricValues(2).getValue().equals("0")) {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(3).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							} else {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(2).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							}
						} else {
							detail.setAvgTimeOnPage("0");
						}
			
						if (!otherPageThanMapValue) {
							if (pagePath.contains("takewellnesscheck/PHQ") || pagePath.contains("takewellnesscheck/GAD")) {
								wellnessPageDetails.add(detail);
							} else if (profileCount <= 14 && pagePath.contains("profiling/")) {
								profilePageDetails.add(detail);
							} else {
								analyticsPerPageDetails.add(detail);
							}
						}
					}
				}
				
				if (wellnessPageDetails != null && !wellnessPageDetails.isEmpty()) {
					wellnessPageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setWellnessPageDetails(wellnessPageDetails != null && !wellnessPageDetails.isEmpty() ? wellnessPageDetails : null);
				}
				if (profilePageDetails != null && !profilePageDetails.isEmpty()) {
					profilePageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setProfilePageDetails(profilePageDetails != null && !profilePageDetails.isEmpty() ? profilePageDetails : null);
				}
				
				patientAnalyticsData.setPerPageDetails(analyticsPerPageDetails != null && !analyticsPerPageDetails.isEmpty() ? analyticsPerPageDetails : null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setExitPageDetailDataForAllPatientDOBusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("customUser:page_exit"))
					.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info("RUN REPORT RESPONSE  : : : "+response);
			int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsExitAndBlogPageDetails> exitPageDetails =new ArrayList<>();
				LOGGER.info("responserowlist  : : : "+response.getRowsList());

				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				
				for (Row row : rows) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
							detail.setCategory("Pages");
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("WellnessCheck");
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("Profiling");
								LOGGER.info("PageExitProfileCount All user value ::: "+Integer.parseInt(pagePath.split("/")[2]));								
								detail.setPageExitProfileCount(Integer.parseInt(pagePath.split("/")[2]));
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info("RowMatrixValue  : : : "+row.getMetricValues(0).getValue());

						}
						
						
			
						if (!otherPageThanMapValue) {
							exitPageDetails.add(detail);
						}
					}
				}
				if (exitPageDetails != null && !exitPageDetails.isEmpty()) {
		LOGGER.info("Before Sorting ExitPageDetails  : "+exitPageDetails);
		/*
		 * Collections.sort(exitPageDetails, Collections.reverseOrder());
		 * 
		 * LOGGER.info("After Sorting ExitPageDetails  : "+exitPageDetails);
		 */
						patientAnalyticsData.setExitPageDetails(exitPageDetails != null && !exitPageDetails.isEmpty() ? exitPageDetails : null);
						

				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setBlogPageDetailDataForAllPatientDOBusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			//LOGGER.info(" blogPageDetails RUN REPORT RESPONSE  : : : "+response);

			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				//LOGGER.info("  blogPageDetails RESPONSE RowList  : : : "+response.getRowsList());

				List<AnalyticsExitAndBlogPageDetails> blogPageDetails =new ArrayList<>();
				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				
				for (Row row : rows) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							LOGGER.info("pagePath for blog ::: "+map.get(pagePath)+" === "+"RowMatrix Value ::: "+row.getMetricValues(0).getValue());
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						}  else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info(" blogPageDetails BLowView RowMatrix Value  : : : "+row.getMetricValues(0).getValue());

						}
						else {
							detail.setPageCount("0");

						}
						
			
						if (!otherPageThanMapValue) {
							blogPageDetails.add(detail);
						}
					}
				}
				if (blogPageDetails != null && !blogPageDetails.isEmpty()) {
					LOGGER.info("Before Sorting blogPageDetails  : "+blogPageDetails);

					//blogPageDetails.reversesort(a1.getPageCount())-(a2.getPageCount());
			        //Collections.sort(al, Collections.reverseOrder());

						patientAnalyticsData.setBlogPageDetails(blogPageDetails != null && !blogPageDetails.isEmpty() ? blogPageDetails : null);


				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setTotalVisitsForAllPatientDOBAndGenderUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addDimensions(Dimension.newBuilder().setName("customUser:dob"))
						.addMetrics(Metric.newBuilder().setName("sessions"))
						.addMetrics(Metric.newBuilder().setName("activeusers"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:gender")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(0).getValue().equals("undefined")
							&& !x.getDimensionValues(0).getValue().equals("0")
							&& !x.getDimensionValues(0).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(0).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(0).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				for (Row row : rows) {
					patientAnalyticsData.setTotalVisitCount(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setUniqueVisitorsForAllPatientDOBAndGenderUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
					    .addDimensions(Dimension.newBuilder().setName("customUser:dob"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:gender")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				for (Row row : rows) {
					if(!row.getDimensionValues(0).getValue().equals("0") || !row.getDimensionValues(0).getValue().equals("(not set)")) {
						uniqueUserCount++;
					}
					patientAnalyticsData.setUniqueUserCount(String.valueOf(uniqueUserCount));
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setPageDetailDataForAllPatientDOBAndGenderusingGA4( AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
					.addMetrics(Metric.newBuilder().setName("averageDuration").setExpression("userEngagementDuration/sessions"))
					.addMetrics(Metric.newBuilder().setName("averageDurationPeruser").setExpression("userEngagementDuration/activeUsers"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
							  ().setFieldName("customUser:gender")
							  .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
							 
					
					.build();

			RunReportResponse response = analyticsData.runReport(request);
            int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsPerPageDetail> analyticsPerPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> wellnessPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> profilePageDetails = new ArrayList<> ();

				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				
				for (Row row : rows) {
					AnalyticsPerPageDetail detail = new AnalyticsPerPageDetail();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (profileCount <= 14 && pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageViews(row.getMetricValues(0).getValue());
						}
						
						if (row.getMetricValues(1) != null && row.getMetricValues(1).getValue() != null && !row.getMetricValues(1).getValue().isBlank()) {
							double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
							detail.setTotalTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
						} else {
							detail.setTotalTimeOnPage("0");
						}
						
						if (row.getMetricValues(2) != null && row.getMetricValues(2).getValue() != null && !row.getMetricValues(2).getValue().isBlank()) {
							if (row.getMetricValues(2).getValue().equals("0")) {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(3).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							} else {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(2).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							}
						} else {
							detail.setAvgTimeOnPage("0");
						}
			
						if (!otherPageThanMapValue) {
							if (pagePath.contains("takewellnesscheck/PHQ") || pagePath.contains("takewellnesscheck/GAD")) {
								wellnessPageDetails.add(detail);
							} else if (profileCount <= 14 && pagePath.contains("profiling/")) {
								profilePageDetails.add(detail);
							} else {
								analyticsPerPageDetails.add(detail);
							}
						}
					}
				}
				
				if (wellnessPageDetails != null && !wellnessPageDetails.isEmpty()) {
					wellnessPageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setWellnessPageDetails(wellnessPageDetails != null && !wellnessPageDetails.isEmpty() ? wellnessPageDetails : null);
				}
				if (profilePageDetails != null && !profilePageDetails.isEmpty()) {
					profilePageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setProfilePageDetails(profilePageDetails != null && !profilePageDetails.isEmpty() ? profilePageDetails : null);
				}
				
				patientAnalyticsData.setPerPageDetails(analyticsPerPageDetails != null && !analyticsPerPageDetails.isEmpty() ? analyticsPerPageDetails : null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setBlogPageDetailDataForAllPatientDOBAndGenderusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
							  ().setFieldName("customUser:gender")
							  .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
							 
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			//LOGGER.info(" blogPageDetails RUN REPORT RESPONSE  : : : "+response);

			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				//LOGGER.info("  blogPageDetails RESPONSE RowList  : : : "+response.getRowsList());

				List<AnalyticsExitAndBlogPageDetails> blogPageDetails =new ArrayList<>();
				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				for (Row row : rows) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							LOGGER.info("pagePath for blog ::: "+map.get(pagePath)+" === "+"RowMatrix Value ::: "+row.getMetricValues(0).getValue());
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						}  else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info(" blogPageDetails BLowView RowMatrix Value  : : : "+row.getMetricValues(0).getValue());

						}
						else {
							detail.setPageCount("0");

						}
						
			
						if (!otherPageThanMapValue) {
							blogPageDetails.add(detail);
						}
					}
				}
				if (blogPageDetails != null && !blogPageDetails.isEmpty()) {
					LOGGER.info("Before Sorting blogPageDetails  : "+blogPageDetails);

					//blogPageDetails.reversesort(a1.getPageCount())-(a2.getPageCount());
			        //Collections.sort(al, Collections.reverseOrder());

						patientAnalyticsData.setBlogPageDetails(blogPageDetails != null && !blogPageDetails.isEmpty() ? blogPageDetails : null);


				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setExitPageDetailDataForAllPatientDOBAndGenderusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("customUser:page_exit"))
					.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
					 ().setFieldName("customUser:gender")
					 .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getGender())) ))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info("RUN REPORT RESPONSE  : : : "+response);
			int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsExitAndBlogPageDetails> exitPageDetails =new ArrayList<>();
				LOGGER.info("responserowlist  : : : "+response.getRowsList());

				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				
				for (Row row : rows) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
							detail.setCategory("Pages");
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("WellnessCheck");
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("Profiling");
								LOGGER.info("PageExitProfileCount All user value ::: "+Integer.parseInt(pagePath.split("/")[2]));								
								detail.setPageExitProfileCount(Integer.parseInt(pagePath.split("/")[2]));
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info("RowMatrixValue  : : : "+row.getMetricValues(0).getValue());

						}
						
						
			
						if (!otherPageThanMapValue) {
							exitPageDetails.add(detail);
						}
					}
				}
				if (exitPageDetails != null && !exitPageDetails.isEmpty()) {
		LOGGER.info("Before Sorting ExitPageDetails  : "+exitPageDetails);
		/*
		 * Collections.sort(exitPageDetails, Collections.reverseOrder());
		 * 
		 * LOGGER.info("After Sorting ExitPageDetails  : "+exitPageDetails);
		 */
						patientAnalyticsData.setExitPageDetails(exitPageDetails != null && !exitPageDetails.isEmpty() ? exitPageDetails : null);
						

				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setTotalVisitsForAllPatientDOBAndEthinicityUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addDimensions(Dimension.newBuilder().setName("customUser:dob"))
						.addMetrics(Metric.newBuilder().setName("sessions"))
						.addMetrics(Metric.newBuilder().setName("activeusers"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:ethnicity")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(0).getValue().equals("undefined")
							&& !x.getDimensionValues(0).getValue().equals("0")
							&& !x.getDimensionValues(0).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(0).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(0).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				for (Row row : rows) {
					patientAnalyticsData.setTotalVisitCount(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
				}
			}
			
			
			
		} catch (Exception e) {
			LOGGER.info("Some exception occur inside setTotalVisitsForAllPatientDOBAndEthinicityUsingGA4 ::: "+e.getMessage());
		}
	}
	
	private void setUniqueVisitorsForAllPatientDOBAndEthinicityUsingGA4(AnalyticsReportingQueryData analyticsReportingQueryData,
			PatientAnalyticsData patientAnalyticsData) {
		int uniqueUserCount = 0;
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					    .addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
					    .addDimensions(Dimension.newBuilder().setName("customUser:dob"))
						.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
						 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						 ().setFieldName("customUser:ethnicity")
						.setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
						.build();

			RunReportResponse response = analyticsData.runReport(request);
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				for (Row row : rows) {
					if(!row.getDimensionValues(0).getValue().equals("0") || !row.getDimensionValues(0).getValue().equals("(not set)")) {
						uniqueUserCount++;
					}
					patientAnalyticsData.setUniqueUserCount(String.valueOf(uniqueUserCount));
				}
			}
			
			
			
		} catch (Exception e) {
			LOGGER.info("Some excpetion occur inside setUniqueVisitorsForAllPatientDOBAndEthinicityUsingGA4 ::: "+e.getMessage());
		}
	}
	
	private void setPageDetailDataForAllPatientDOBAndEthinicityusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			
			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					.addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
					.addMetrics(Metric.newBuilder().setName("averageDuration").setExpression("userEngagementDuration/sessions"))
					.addMetrics(Metric.newBuilder().setName("averageDurationPeruser").setExpression("userEngagementDuration/activeUsers"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
							  ().setFieldName("customUser:ethnicity")
							  .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
							 
					
					.build();

			RunReportResponse response = analyticsData.runReport(request);
            int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsPerPageDetail> analyticsPerPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> wellnessPageDetails = new ArrayList<> ();
				List<AnalyticsPerPageDetail> profilePageDetails = new ArrayList<> ();

				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				
				for (Row row : rows) {
					AnalyticsPerPageDetail detail = new AnalyticsPerPageDetail();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (profileCount <= 14 && pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageViews(row.getMetricValues(0).getValue());
						}
						
						if (row.getMetricValues(1) != null && row.getMetricValues(1).getValue() != null && !row.getMetricValues(1).getValue().isBlank()) {
							double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
							detail.setTotalTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
						} else {
							detail.setTotalTimeOnPage("0");
						}
						
						if (row.getMetricValues(2) != null && row.getMetricValues(2).getValue() != null && !row.getMetricValues(2).getValue().isBlank()) {
							if (row.getMetricValues(2).getValue().equals("0")) {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(3).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							} else {
								double timeInSeconds = Double.parseDouble(row.getMetricValues(2).getValue());
								detail.setAvgTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
							}
						} else {
							detail.setAvgTimeOnPage("0");
						}
			
						if (!otherPageThanMapValue) {
							if (pagePath.contains("takewellnesscheck/PHQ") || pagePath.contains("takewellnesscheck/GAD")) {
								wellnessPageDetails.add(detail);
							} else if (profileCount <= 14 && pagePath.contains("profiling/")) {
								profilePageDetails.add(detail);
							} else {
								analyticsPerPageDetails.add(detail);
							}
						}
					}
				}
				
				if (wellnessPageDetails != null && !wellnessPageDetails.isEmpty()) {
					wellnessPageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setWellnessPageDetails(wellnessPageDetails != null && !wellnessPageDetails.isEmpty() ? wellnessPageDetails : null);
				}
				if (profilePageDetails != null && !profilePageDetails.isEmpty()) {
					profilePageDetails.sort((AnalyticsPerPageDetail a1, AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])-
							(Integer.parseInt(a2.getPagePath().split("-")[1]))));
					patientAnalyticsData.setProfilePageDetails(profilePageDetails != null && !profilePageDetails.isEmpty() ? profilePageDetails : null);
				}
				
				patientAnalyticsData.setPerPageDetails(analyticsPerPageDetails != null && !analyticsPerPageDetails.isEmpty() ? analyticsPerPageDetails : null);
			}

		} catch (Exception e) {
			LOGGER.info("Some exception occur inside setPageDetailDataForAllPatientDOBAndEthinicityusingGA4 ::: "+e.getMessage());
		}
	}
	
	private void setExitPageDetailDataForAllPatientDOBAndEthinicityusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("/", "Login Page");
			map.put("/forgottencode", "Forget Code");
			map.put("/home", "Home");
			map.put("/aboutus", "About Us");
			map.put("/wellnesscheck", "Wellness Check");
			map.put("/profilingquestion", "Profiling Question");
			map.put("/blogs", "Blogs");
			map.put("/faq", "FAQ");
			map.put("/bookingschedule", "Book Schedule");
			map.put("/cookie", "Cookie");
			map.put("/privacy", "Privacy");
			map.put("/terms", "Terms");
			map.put("/aup", "AUP");
			map.put("/contactus", "Contact Us");
			map.put("/myprofile", "My Profile");
			map.put("/support", "Support");
			map.put("/myprofile/mywellnesscheck", "My Wellness Check");
			map.put("/myprofile/myschedule", "My Schedule");
			map.put("/myprofile/myprofiling", "Profiling Question");
			map.put("/myprofile/myprofilesetting", "Profile Details");
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("customUser:page_exit"))
					.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					.addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
					 ().setFieldName("customUser:ethnicity")
					 .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info("RUN REPORT RESPONSE  : : : "+response);
			int profileCount = 0;
			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				List<AnalyticsExitAndBlogPageDetails> exitPageDetails =new ArrayList<>();
				LOGGER.info("responserowlist  : : : "+response.getRowsList());

				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				
				for (Row row : rows) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
							detail.setCategory("Pages");
						} else if (pagePath.contains("takewellnesscheck/")) {
							if (pagePath.split("/").length > 2) {
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? pagePath.split("/")[2] : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("WellnessCheck");
							} else {
								otherPageThanMapValue = true;
							}
						} else if (pagePath.contains("profiling/")) {
							profileCount++;
							if (pagePath.split("/").length > 2) {
								//if(pagePath.split("/")[2]>14)
								detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank() ? ("Q-" + pagePath.split("/")[2]) : pagePath);
								otherPageThanMapValue = false;
								detail.setCategory("Profiling");
								LOGGER.info("PageExitProfileCount All user value ::: "+Integer.parseInt(pagePath.split("/")[2]));								
								detail.setPageExitProfileCount(Integer.parseInt(pagePath.split("/")[2]));
							} else {
								otherPageThanMapValue = true;
							}
						} else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info("RowMatrixValue  : : : "+row.getMetricValues(0).getValue());

						}
						
						
			
						if (!otherPageThanMapValue) {
							exitPageDetails.add(detail);
						}
					}
				}
				if (exitPageDetails != null && !exitPageDetails.isEmpty()) {
		LOGGER.info("Before Sorting ExitPageDetails  : "+exitPageDetails);
		/*
		 * Collections.sort(exitPageDetails, Collections.reverseOrder());
		 * 
		 * LOGGER.info("After Sorting ExitPageDetails  : "+exitPageDetails);
		 */
						patientAnalyticsData.setExitPageDetails(exitPageDetails != null && !exitPageDetails.isEmpty() ? exitPageDetails : null);
						

				}
				
			}

		} catch (Exception e) {
			LOGGER.info("Some exception occur inside setExitPageDetailDataForAllPatientDOBAndEthinicityusingGA4 ::: "+e.getMessage());
		}
	}
	
	private void setBlogPageDetailDataForAllPatientDOBAndEthinicityusingGA4(AnalyticsReportingQueryData analyticsReportingQueryData, PatientAnalyticsData patientAnalyticsData) {
		try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put( "/blogs/blog1", "Blog1");
			map.put("/blogs/blog2", "Blog2");
			map.put("/blogs/blog3", "Blog3");
			map.put("/blogs/blog4", "Blog4");

			RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + gaPropertyId)
					.addDimensions(Dimension.newBuilder().setName("pagePath"))
					.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
					.addDimensions(Dimension.newBuilder().setName("customUser:ethnicity"))
					.addMetrics(Metric.newBuilder().setName("screenPageViews"))
					.addDateRanges(DateRange.newBuilder().setStartDate(analyticsReportingQueryData.getStartDate().toString()).setEndDate(analyticsReportingQueryData.getEndDate().toString()))
					.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
							  ().setFieldName("customUser:ethnicity")
							  .setStringFilter(StringFilter.newBuilder().setValue(analyticsReportingQueryData.getEthnicity())) ))
							 
					.build();

			RunReportResponse response = analyticsData.runReport(request);
			LOGGER.info(" blogPageDetails RUN REPORT RESPONSE  : : : "+response);

			if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
				LOGGER.info("  blogPageDetails RESPONSE RowList  : : : "+response.getRowsList());

				List<AnalyticsExitAndBlogPageDetails> blogPageDetails =new ArrayList<>();
				List<Row> rows = response.getRowsList().stream().filter(x -> {
					if (!x.getDimensionValues(1).getValue().equals("undefined")
							&& !x.getDimensionValues(1).getValue().equals("0")
							&& !x.getDimensionValues(1).getValue().equals("(not set)")
							&& analyticsReportingQueryData.getAgeStart()
									.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
							&& analyticsReportingQueryData.getAgeEnd()
									.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
						return true;
					} else {
						return false;
					}
				}
				).collect(Collectors.toList());
				for (Row row : rows) {
					AnalyticsExitAndBlogPageDetails detail = new AnalyticsExitAndBlogPageDetails();
					boolean otherPageThanMapValue = false;
					if (row.getDimensionValues(0) != null && row.getDimensionValues(0).getValue() != null && !row.getDimensionValues(0).getValue().isBlank()) {
						String pagePath = row.getDimensionValues(0).getValue();
						if (map.get(pagePath) != null) {
							LOGGER.info("pagePath for blog ::: "+map.get(pagePath)+" === "+"RowMatrix Value ::: "+row.getMetricValues(0).getValue());
							detail.setPagePath(map.get(pagePath));
							otherPageThanMapValue = false;
						}  else {
							otherPageThanMapValue = true;
						}
						
						if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null && !row.getMetricValues(0).getValue().isBlank()) {
							detail.setPageCount(row.getMetricValues(0).getValue());
							LOGGER.info(" blogPageDetails BLowView RowMatrix Value  : : : "+row.getMetricValues(0).getValue());

						}
						else {
							detail.setPageCount("0");

						}
						
			
						if (!otherPageThanMapValue) {
							blogPageDetails.add(detail);
						}
					}
				}
				if (blogPageDetails != null && !blogPageDetails.isEmpty()) {
					LOGGER.info("Before Sorting blogPageDetails  : "+blogPageDetails);

					//blogPageDetails.reversesort(a1.getPageCount())-(a2.getPageCount());
			        //Collections.sort(al, Collections.reverseOrder());

						patientAnalyticsData.setBlogPageDetails(blogPageDetails != null && !blogPageDetails.isEmpty() ? blogPageDetails : null);


				}
				
			}

		} catch (Exception e) {
			LOGGER.info("Some exception occur inside setBlogPageDetailDataForAllPatientDOBAndEthinicityusingGA4 ::: "+e.getMessage());
		}
	}
	//******************DOB analytics changes end*******************//
}