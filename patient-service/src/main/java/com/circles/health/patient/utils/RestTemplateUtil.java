package com.circles.health.patient.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.circles.health.patient.response.AnalyticsReportingQueryData;
import com.circles.health.patient.response.UserAnalyticsBookingDetails;
import com.circles.health.patient.response.UserAnalyticsBookingDetailsList;
import com.circles.health.patient.wrapper.BaseResponse;

public class RestTemplateUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateUtil.class);

    private static HttpHeaders getEntity() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    public static Object getMethod(String url, RestTemplate restTemplate) {

        Object responseObject = null;

        HttpHeaders httpHeaders = getEntity();

        HttpEntity<String> request = new HttpEntity<>("parameters", httpHeaders);

        ResponseEntity<BaseResponse> response = restTemplate.getForEntity(
                url, BaseResponse.class, request);

        BaseResponse baseResponse = response.getBody();

        if (baseResponse != null) {
            responseObject = baseResponse.getData();
        }

        return responseObject;
    }

    public static AnalyticsReportingQueryData postMethodToFetchAnalyticsData(String url, String body, RestTemplate restTemplate) {

        HttpHeaders httpHeaders = getEntity();

        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<AnalyticsReportingQueryData> responseEntity = restTemplate.postForEntity(url, request, AnalyticsReportingQueryData.class);

        return responseEntity.getBody();
    }
    
    public static UserAnalyticsBookingDetailsList getMethodForDownloadCSVForSingleUser(String url, String body, RestTemplate restTemplate) {
    	LOGGER.info("inside rest template with analytics for a user from daterange  :: ");
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<UserAnalyticsBookingDetailsList> responseEntity = restTemplate.postForEntity(url, request,UserAnalyticsBookingDetailsList.class );

        return responseEntity.getBody();
	}
   
}