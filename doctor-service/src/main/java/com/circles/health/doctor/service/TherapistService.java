package com.circles.health.doctor.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.circles.health.doctor.entity.PatientBookingDetail;
import com.circles.health.doctor.entity.TherapistDetail;
import com.circles.health.doctor.request.UserAnalyticsReportingQueryData;
import com.circles.health.doctor.request.WebHookRequest;
import com.circles.health.doctor.response.AddEditTherapistResponse;
import com.circles.health.doctor.response.AnalyticsReportingQueryData;
import com.circles.health.doctor.response.TherapistNamesResponse;
import com.circles.health.doctor.response.UserAnalyticsBookingDetails;

public interface TherapistService {

	TherapistDetail addTherapist(TherapistDetail therapistDetail);
	
	AddEditTherapistResponse editTherapist(TherapistDetail therapistDetail);
	
	List<TherapistDetail> getTherapists();
	
	String deleteTherapist(Long therapistId);
	
	List<TherapistNamesResponse> getTherapistsName();
	
	TherapistDetail getTherapistDetailById(Long therapistId);
	
	PatientBookingDetail getPatientBookingDetailByEmail(String email);
	
	PatientBookingDetail savePatientBookingDetail(PatientBookingDetail patientBookingDetail);
	
	Object getScheduledEventData(String eventUrl, String token);
	
	Long getTherapistIdByEmail(String email);
	
	String getTokenByTherapistId(Long therapistId);

	AnalyticsReportingQueryData fetchUniqueUserCountAndTotalBooking(AnalyticsReportingQueryData analyticsReportingQueryData);
	
	PatientBookingDetail getPatientBookingDetailByUserIdAndStatus(Long userId);
	
	List<PatientBookingDetail> findByUserId(Long userId);
	
	Object getUserAndOrganizationUrl(String token);
	
	Object setCalendlyWebHook(WebHookRequest webHookRequest,String token);
	
	AddEditTherapistResponse therapsitToTherapistResponse(TherapistDetail therapistDetail);
	
	TherapistDetail saveTherapistDetail(TherapistDetail therapistDetail);
	
	void deleteMethodForCalendlyWebHookSubscription(String url, String token);

	AnalyticsReportingQueryData fetchUserTotalBookingBetweenDateRange(AnalyticsReportingQueryData analyticsReportingQueryData);
	
	List<UserAnalyticsBookingDetails> fetchSingleUserTotalBookingBetweenDateRange(UserAnalyticsReportingQueryData userAnalyticsReportingQueryData);
	
}
