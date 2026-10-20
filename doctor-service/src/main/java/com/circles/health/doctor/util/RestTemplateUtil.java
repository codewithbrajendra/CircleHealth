package com.circles.health.doctor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.circles.health.doctor.request.EditTherapistNameRequest;
import com.circles.health.doctor.serviceImpl.TherapistServiceImpl;
import com.circles.health.doctor.wrapper.BaseResponse;

public class RestTemplateUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateUtil.class);
	
    private static HttpHeaders getEntity(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        //headers.set("userUniqueId", userUniqueId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    private static HttpHeaders getEntityForDelete(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
    
    public static Object postMethod(String url, String token,String body, RestTemplate restTemplate) {
    	LOGGER.info("Inside postmethod, url ::: "+url);
    	LOGGER.info("Inside postmethod, token ::: "+token);
    	LOGGER.info("Inside postmethod, body ::: "+body);
    	HttpHeaders httpHeaders = getEntity(token);

    	LOGGER.info("HttpHeaders value is ::: "+httpHeaders);
    	LOGGER.info("HttpHeaders Authorization value is ::: "+httpHeaders.get("Authorization"));
        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);
        LOGGER.info("HttpEntity value is ::: "+request);
//        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(
//                url, request, Object.class);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
        
        Object baseResponse = responseEntity.getBody();

        return baseResponse;
    }
    
    public static Object getMethod(String url, String token, RestTemplate restTemplate) {
        LOGGER.info("Inside getMethod of RestTemplateUtil. Url : "+url+" token : "+token);
		Object responseObject = null;
		
		HttpHeaders httpHeaders = getEntity(token);
		
		
		HttpEntity<String> request = new HttpEntity<>(httpHeaders);
		
//		ResponseEntity<BaseResponse> responseEntity = restTemplate.getForEntity(
//		url, request, BaseResponse.class);
		
		//ResponseEntity<BaseResponse> responseEntity = restTemplate.exchange(url, request);
		
		ResponseEntity<Object> responseEntity = restTemplate.exchange(url,HttpMethod.GET, request, Object.class);
		LOGGER.info("ResponseEntity from rest template : "+responseEntity);
		Object baseResponse = responseEntity.getBody();
		LOGGER.info("baseResponse from getBody method : "+baseResponse);
		
		return baseResponse;
	}
    
    public static String postMethodForTherapistName(String url, EditTherapistNameRequest body, RestTemplate restTemplate) {

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, body, String.class);
		
		String baseResponse = responseEntity.getBody();
		
		return baseResponse;
	}
    
    public static Long getMethodForTherapistId(String url, RestTemplate restTemplate) {
    	LOGGER.info("getMethodForTherapistId url ::: "+url);
    	ResponseEntity<Long> therapistIdResponse = restTemplate.getForEntity(url, Long.class);
    	
    	Long therapistId = therapistIdResponse.getBody();
    	LOGGER.info("therapistId from response ::: "+therapistId);
    	return therapistId;
    	
    }
    
    public static void deleteMethodForCalendlyWebHookSubscription(String url, String token, RestTemplate restTemplate) {
    	LOGGER.info("deleteMethodForCalendlyWebHookSubscription url ::: "+url);
        HttpHeaders httpHeaders = getEntityForDelete(token);
		HttpEntity<String> request = new HttpEntity<>(httpHeaders);
		ResponseEntity<Object> responseEntity = restTemplate.exchange(url,HttpMethod.DELETE, request, Object.class);
		LOGGER.info("Response from CalendlyWebHookSubscription ::: "+responseEntity);
    }
}