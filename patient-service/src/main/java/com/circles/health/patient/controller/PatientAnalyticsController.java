package com.circles.health.patient.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.circles.health.patient.entity.WellnessCheckQuestion;
import com.circles.health.patient.response.AnalyticsPerPageDetail;
import com.circles.health.patient.response.AnalyticsReportingQueryData;
import com.circles.health.patient.response.PatientAnalyticsData;
import com.circles.health.patient.response.PatientAnalyticsSingleUserCSV;
import com.circles.health.patient.response.WellnessCheck;
import com.circles.health.patient.service.PatientAnalyticsService;
import com.circles.health.patient.service.PatientBookingService;
import com.circles.health.patient.utils.DateUtil;
import com.circles.health.patient.utils.constants.EndPoints;
import com.circles.health.patient.utils.constants.ResponseMessage;
import com.circles.health.patient.utils.enums.ResponseStatus;
import com.circles.health.patient.wrapper.BaseResponse;

@RestController
@RequestMapping(EndPoints.BASE_PATH + "/analytics")
public class PatientAnalyticsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PatientAnalyticsController.class);
	
	@Autowired
	private PatientAnalyticsService patientAnalyticsService;
	@Autowired
	private PatientBookingService patientBookingService;
	
	
	@PostMapping("/getanalyticsdataforallusers")
	public ResponseEntity<BaseResponse> getAnalyticsDataForAllPatient(@RequestBody AnalyticsReportingQueryData analyticsReportingQueryData) {
		LOGGER.info("fetch patient analytics data for startdate :: " + analyticsReportingQueryData.getStartDate() 
			+ " and enddate :: " + analyticsReportingQueryData.getEndDate());
		LOGGER.info("abha111");
		PatientAnalyticsData patientData = null;
		try {
			patientData = this.patientAnalyticsService.getAnalyticsDataForAllPatient(analyticsReportingQueryData);
		} catch (Exception exception) {
			LOGGER.error(ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage(),
							DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(),
						"Data fetched successfully", DateUtil.getCurrentDateTime(), Collections.emptyList(),
						patientData));
	}

	@PostMapping("/getanalyticscsvdataforallusers")
	public ResponseEntity<PatientAnalyticsData> getAnalyticsCSVDataForAllusers(@RequestBody AnalyticsReportingQueryData analyticsReportingQueryData) {
		LOGGER.info("fetch patient analytics csv data for startdate :: " + analyticsReportingQueryData.getStartDate() 
			+ " and enddate :: " + analyticsReportingQueryData.getEndDate());
		
		PatientAnalyticsData patientData = null;
		try {
			patientData = this.patientAnalyticsService.getAnalyticsDataForAllPatient(analyticsReportingQueryData);
		} catch (Exception exception) {
			LOGGER.error(ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(patientData);
	}

	
	@PostMapping("/getanalyticsdataforuser")
	public ResponseEntity<BaseResponse> getAnalyticsDataForUser(@RequestBody AnalyticsReportingQueryData analyticsReportingQueryData) {
		LOGGER.info("fetch patient analytics data for startdate :: " + analyticsReportingQueryData.getStartDate() 
			+ " and enddate :: " + analyticsReportingQueryData.getEndDate() + " and userId :: " + analyticsReportingQueryData.getUserId());
		
		PatientAnalyticsData patientData = null;
		try {
			patientData = this.patientAnalyticsService.getAnalyticsDataForParticularUser(analyticsReportingQueryData);
		} catch (Exception exception) {
			LOGGER.error(ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage(),
							DateUtil.getCurrentDateTime(), Collections.emptyList(), null));
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(),
						"Data fetched successfully", DateUtil.getCurrentDateTime(), Collections.emptyList(),
						patientData));
	}

	
		
	@PostMapping("/getanalyticscsvforsingleuser")
	public ResponseEntity<PatientAnalyticsSingleUserCSV> getAnalyticsCSVDataForParticularUser(@RequestBody AnalyticsReportingQueryData analyticsReportingQueryData) {
		LOGGER.info("fetch patient analytics csv data for startdate  for particular:: " + analyticsReportingQueryData.getStartDate() 
			+ " and enddate :: " + analyticsReportingQueryData.getEndDate() + " and userId :: "+analyticsReportingQueryData.getUserId());
		
		PatientAnalyticsSingleUserCSV patientAnalyticsSingleUserCSV = null;
		try {
			patientAnalyticsSingleUserCSV = this.patientAnalyticsService.getAnalyticsDataForSingleUserCSV(analyticsReportingQueryData);
			LOGGER.info("PatientAnalyticsSingleUserCSV data inside PatientAnalyticsController ::: "+patientAnalyticsSingleUserCSV.getProfilePageDetails());
		} catch (Exception exception) {
			LOGGER.error(ResponseMessage.SOMETHING_WENT_WRONG + exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(patientAnalyticsSingleUserCSV);
	}

	
	
}
