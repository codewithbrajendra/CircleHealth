package com.circlesHealth.admin.controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.circlesHealth.admin.response.AnalyticsReportingQueryData;
import com.circlesHealth.admin.response.PatientAnalyticsData;
import com.circlesHealth.admin.response.PatientAnalyticsSingleUserCSV;
import com.circlesHealth.admin.service.AnalyticsService;
import com.circlesHealth.admin.utils.DateUtil;
import com.circlesHealth.admin.utils.constants.EndPoints;
import com.circlesHealth.admin.utils.constants.ResponseMessage;
import com.circlesHealth.admin.utils.enums.ResponseStatus;
import com.circlesHealth.admin.wrapper.BaseResponse;

@RestController
@RequestMapping(EndPoints.BASE_PATH + "/analytics")
public class AnalyticsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsController.class);
	
	@Autowired
	private AnalyticsService analyticsService;
	
	@PostMapping("/getanalyticsdataforallusers")
	public ResponseEntity<BaseResponse> getAnalyticsDataForAllUsers(@RequestBody AnalyticsReportingQueryData analyticsReportingQueryData,
			@RequestHeader(name = "Authorization") String token,
			@RequestHeader(name = "userUniqueId") String userUniqueId) {

		LOGGER.info("fetch analytics data for startdate :: " + analyticsReportingQueryData.getStartDate() + " and enddate :: " 
				+ analyticsReportingQueryData.getEndDate() + " token == " + token);
		
		try {
			Object responseObject = this.analyticsService.getAnalyticsDataForAllUsers(analyticsReportingQueryData, token, userUniqueId);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(),
							ResponseMessage.GET_ANALYTICS_DATA, DateUtil.getCurrentDateTime(),
							Collections.emptyList(), responseObject));

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.ERROR_ANALYTICS_DATA + exception.getMessage(),
							DateUtil.getCurrentDateTime(), Collections.emptyList(), null));

		}
	}

	@GetMapping("/getanalyticscsvforallusers/{stringStartDate}/{stringEndDate}/{ageStart}/{ageEnd}/{gender}/{ethnicity}")
	public ResponseEntity<InputStreamResource> getAnalyticsCSVForAllUsers(@PathVariable("stringStartDate") String stringStartDate, @PathVariable("stringEndDate") String stringEndDate,
			@PathVariable("ageStart") String ageStart,@PathVariable("ageEnd") String ageEnd,@PathVariable("gender") String gender,@PathVariable("ethnicity") String ethnicity) {

		LOGGER.info("fetch analytics csv for startdate :: " + stringStartDate + " and enddate :: " + stringEndDate);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		  LocalDate startDate = DateUtil.convertStringIntoLocalDate(stringStartDate, formatter);
		  LocalDate endDate = DateUtil.convertStringIntoLocalDate(stringEndDate, formatter);
		  LocalDate userAgeStart = null;
		  LocalDate userAgeEnd = null;
		  if(!ageStart.equals("null")) {
			   userAgeStart = DateUtil.convertStringIntoLocalDate(ageStart, formatter);
		  } 
		  if(!ageEnd.equals("null")) {
			  userAgeEnd = DateUtil.convertStringIntoLocalDate(ageEnd, formatter);
		  }
		  
		HttpHeaders respHeaders = null;
		InputStreamResource isr = null;
		AnalyticsReportingQueryData analyticsReportingQueryData = new AnalyticsReportingQueryData();
		analyticsReportingQueryData.setStartDate(startDate);
		analyticsReportingQueryData.setEndDate(endDate);
		analyticsReportingQueryData.setAgeStart(userAgeStart);
		analyticsReportingQueryData.setAgeEnd(userAgeEnd);
		analyticsReportingQueryData.setGender(gender.equals("null") ? null : gender);
		analyticsReportingQueryData.setEthnicity(ethnicity.equals("null") ? null : ethnicity);
		try {
			PatientAnalyticsData analyticsData = this.analyticsService.getAnalyticsCSVForAllUsers(analyticsReportingQueryData);
			if (analyticsData != null) {
				ByteArrayInputStream csvData = this.analyticsService.generateAnalyticsCSVForAllUsers(analyticsData);
				
				respHeaders = new HttpHeaders();
				MediaType mediaType = MediaType.parseMediaType("application/csv");
				respHeaders.setContentType(mediaType);
				respHeaders.setContentDispositionFormData("attachment", "analyticscsv_allusers.csv");
				isr = new InputStreamResource(csvData);
			}

		} catch (Exception exception) {
			LOGGER.info("Some exception occur while get Patient Detail PDF ::: "+exception.getMessage());
			exception.printStackTrace();
		}

		return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
	}

	@GetMapping("/getanalyticscsvforallusers3/{stringStartDate}/{stringEndDate}")
	public ResponseEntity<InputStreamResource> getAnalyticsCSVForAllUsers3(@PathVariable("stringStartDate") String stringStartDate, @PathVariable("stringEndDate") String stringEndDate) {

		LOGGER.info("fetch analytics csv for startdate :: " + stringStartDate + " and enddate :: " + stringEndDate);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		  LocalDate startDate = DateUtil.convertStringIntoLocalDate(stringStartDate, formatter);
		  LocalDate endDate = DateUtil.convertStringIntoLocalDate(stringEndDate, formatter);
		HttpHeaders respHeaders = null;
		InputStreamResource isr = null;
		AnalyticsReportingQueryData analyticsReportingQueryData = new AnalyticsReportingQueryData();
		analyticsReportingQueryData.setStartDate(startDate);
		analyticsReportingQueryData.setEndDate(endDate);
		try {
			PatientAnalyticsData analyticsData = this.analyticsService.getAnalyticsCSVForAllUsers(analyticsReportingQueryData);
			if (analyticsData != null) {
				ByteArrayInputStream csvData = this.analyticsService.generateAnalyticsCSVForAllUsers3(analyticsData);
				
				respHeaders = new HttpHeaders();
				MediaType mediaType = MediaType.parseMediaType("application/csv");
				respHeaders.setContentType(mediaType);
				respHeaders.setContentDispositionFormData("attachment", "analyticscsv_allusers.csv");
				isr = new InputStreamResource(csvData);
			}

		} catch (Exception exception) {
			LOGGER.info("Some exception occur while get Patient Detail PDF ::: "+exception.getMessage());
			exception.printStackTrace();
		}

		return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
	}
	
	@PostMapping("/getanalyticsdataforuser")
	public ResponseEntity<BaseResponse> getAnalyticsDataForUser(@RequestBody AnalyticsReportingQueryData analyticsReportingQueryData,
			@RequestHeader(name = "Authorization") String token,
			@RequestHeader(name = "userUniqueId") String userUniqueId) {

		LOGGER.info("fetch analytics data for a user from startdate :: " + analyticsReportingQueryData.getStartDate() + " and enddate :: " 
				+ analyticsReportingQueryData.getEndDate() + " token == " + token);
		
		try {
			Object responseObject = this.analyticsService.getAnalyticsDataForUser(analyticsReportingQueryData, token, userUniqueId);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(),
							ResponseMessage.GET_ANALYTICS_DATA, DateUtil.getCurrentDateTime(),
							Collections.emptyList(), responseObject));

		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.ERROR_ANALYTICS_DATA + exception.getMessage(),
							DateUtil.getCurrentDateTime(), Collections.emptyList(), null));

		}
	}
	
	
	@GetMapping("/getanalyticscsvforsingleuser/{userId}/{stringStartDate}/{stringEndDate}")
	public ResponseEntity<InputStreamResource> getAnalyticsCSVDataForParticularUser(@PathVariable("userId") String userId, @PathVariable("stringStartDate") String stringStartDate, @PathVariable("stringEndDate") String stringEndDate) {

		LOGGER.info("fetch analytics csv for startdate :: " + stringStartDate + " and enddate :: " + stringEndDate);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		  LocalDate startDate = DateUtil.convertStringIntoLocalDate(stringStartDate, formatter);
		  LocalDate endDate = DateUtil.convertStringIntoLocalDate(stringEndDate, formatter);
		HttpHeaders respHeaders = null;
		InputStreamResource isr = null;
		AnalyticsReportingQueryData analyticsReportingQueryData = new AnalyticsReportingQueryData();
		analyticsReportingQueryData.setStartDate(startDate);
		analyticsReportingQueryData.setEndDate(endDate);
		analyticsReportingQueryData.setUserId(Long.parseLong(userId));
		try {
			PatientAnalyticsSingleUserCSV analyticsCSVData = this.analyticsService.getAnalyticsDataForSingleUserCSV(analyticsReportingQueryData);
			LOGGER.info("PatientAnalyticsSingleUserCSV data from patient service ::: "+analyticsCSVData);
			if (analyticsCSVData != null) {
				ByteArrayInputStream csvData = this.analyticsService.getAnalyticsDataForSingleUserCSV(analyticsCSVData);
				
				respHeaders = new HttpHeaders();
				MediaType mediaType = MediaType.parseMediaType("application/csv");
				respHeaders.setContentType(mediaType);
				respHeaders.setContentDispositionFormData("attachment", "analyticscsv_singleuser.csv");
				isr = new InputStreamResource(csvData);
			}

		} catch (Exception exception) {
			LOGGER.info("Some exception occur while get Patient Detail PDF ::: "+exception.getMessage());
			exception.printStackTrace();
		}

		return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
	}

}
