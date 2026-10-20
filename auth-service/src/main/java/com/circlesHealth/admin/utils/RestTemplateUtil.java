package com.circlesHealth.admin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.circlesHealth.admin.response.AnalyticsUserRequestData;
import com.circlesHealth.admin.response.PatientAnalyticsData;
import com.circlesHealth.admin.response.PatientAnalyticsSingleUserCSV;
import com.circlesHealth.admin.response.PatientDetailResponseForFile;
import com.circlesHealth.admin.wrapper.BaseResponse;

public class RestTemplateUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateUtil.class);
	
    private static HttpHeaders getEntity(String token, String userUniqueId) {
    	LOGGER.info("getEntity token ::: "+token);
    	LOGGER.info("getEntity userUniqueId ::: "+userUniqueId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("userUniqueId", userUniqueId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    public static Object postMethod(String url, String token, String userUniqueId,
                                    String body, RestTemplate restTemplate) {

        Object responseObject = null;

        HttpHeaders httpHeaders = getEntity(token, userUniqueId);


        HttpEntity<String> request = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<BaseResponse> responseEntity = restTemplate.postForEntity(
                url, request, BaseResponse.class);

        BaseResponse baseResponse = responseEntity.getBody();

        if (baseResponse != null) {
            responseObject = baseResponse.getData();
        }

        
        return responseObject;
    }
    
    public static Object getMethod(String url, String token, String userUniqueId, RestTemplate restTemplate) {

		Object responseObject = null;
		
		HttpHeaders httpHeaders = getEntity(token, userUniqueId);
		
		
		HttpEntity<String> request = new HttpEntity<>(httpHeaders);
		
//		ResponseEntity<BaseResponse> responseEntity = restTemplate.getForEntity(
//		url, request, BaseResponse.class);
		
		//ResponseEntity<BaseResponse> responseEntity = restTemplate.exchange(url, request);
		
		ResponseEntity<BaseResponse> responseEntity = restTemplate.exchange(url,HttpMethod.GET, request, BaseResponse.class);
		
		BaseResponse baseResponse = responseEntity.getBody();
		
		if (baseResponse != null) {
		responseObject = baseResponse.getData();
		}
		
		
		return responseObject;
	}
    
    public static PatientDetailResponseForFile getMethodForDownloadPdf(String url, RestTemplate restTemplate) {
    	
        ResponseEntity<PatientDetailResponseForFile> responseEntity = restTemplate.getForEntity(url, PatientDetailResponseForFile.class);
		
		return responseEntity.getBody();
	}
    
    public static PatientAnalyticsData postMethodForDownloadCSVForAllUsers(String url, String body, RestTemplate restTemplate) {
    	LOGGER.info("inside rest template with analytics for a user from daterange  :: ");
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<PatientAnalyticsData> responseEntity = restTemplate.postForEntity(url, request, PatientAnalyticsData.class);

        return responseEntity.getBody();
	}
    
    public static PatientAnalyticsSingleUserCSV getMethodForDownloadCSVForSingleUser(String url, String body, RestTemplate restTemplate) {
    	LOGGER.info("inside rest template with analytics for a single user csv from daterange  :: ");
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<PatientAnalyticsSingleUserCSV> responseEntity = restTemplate.postForEntity(url, request, PatientAnalyticsSingleUserCSV.class);

        return responseEntity.getBody();
	}
    
   public static AnalyticsUserRequestData getAnalyticsUserRequestData(String url, RestTemplate restTemplate) {
    	
        ResponseEntity<AnalyticsUserRequestData> responseEntity = restTemplate.getForEntity(url, AnalyticsUserRequestData.class);
		
		return responseEntity.getBody();
	}
}