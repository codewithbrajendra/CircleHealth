package com.circles.health.patient.service;

public interface PatientBookingService {

	public Object getPatientBookingDetail(Long therapistId, Long userId) throws Exception;
	
	public Object getAllPatientBookingDetail(Long userId) throws Exception;
}
