package com.circles.health.patient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.circles.health.patient.utils.RestTemplateUtil;

@Service
public class PatientBookingServiceImpl implements PatientBookingService {

	@Value("${doctor.service.base.api.url}")
    String doctorServiceUrl;
	
	@Autowired
    RestTemplate restTemplate;
	
	@Override
	public Object getPatientBookingDetail(Long therapistId, Long userId) throws Exception {
		
		return this.sendGetRequestToTherapistData(therapistId, userId);
	}

	private Object sendGetRequestToTherapistData(Long therapistId, Long userId)
			throws Exception {

		Object o = RestTemplateUtil.getMethod(doctorServiceUrl + "/doctor/getPatientBookingDetailByUserId/"+therapistId+"/"+userId, restTemplate);

		return o;
	}
	
	private Object sendGetRequestToAllTherapistData(Long userId) throws Exception {

		Object o = RestTemplateUtil.getMethod(doctorServiceUrl + "/doctor/getPatientBookingDetailsByUserId/"+userId, restTemplate);

		return o;
	}

	@Override
	public Object getAllPatientBookingDetail(Long userId) throws Exception {
		
		return this.sendGetRequestToAllTherapistData(userId);
	}
	
}
