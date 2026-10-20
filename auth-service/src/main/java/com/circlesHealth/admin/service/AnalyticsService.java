package com.circlesHealth.admin.service;

import java.io.ByteArrayInputStream;

import com.circlesHealth.admin.response.AnalyticsReportingQueryData;
import com.circlesHealth.admin.response.PatientAnalyticsData;
import com.circlesHealth.admin.response.PatientAnalyticsSingleUserCSV;
import com.circlesHealth.admin.response.UserAnalyticsBookingDetails;

public interface AnalyticsService {

	Object getAnalyticsDataForAllUsers(AnalyticsReportingQueryData analyticsReportingQueryData, String token, String userUniqueId);

	Object getAnalyticsDataForUser(AnalyticsReportingQueryData analyticsReportingQueryData, String token, String userUniqueId);

	PatientAnalyticsData getAnalyticsCSVForAllUsers(AnalyticsReportingQueryData analyticsReportingQueryData);

	ByteArrayInputStream generateAnalyticsCSVForAllUsers(PatientAnalyticsData analyticsData);

	ByteArrayInputStream generateAnalyticsCSVForAllUsers3(PatientAnalyticsData analyticsData);
	
	
	PatientAnalyticsSingleUserCSV getAnalyticsDataForSingleUserCSV(
			AnalyticsReportingQueryData analyticsReportingQueryData);

	ByteArrayInputStream getAnalyticsDataForSingleUserCSV(PatientAnalyticsSingleUserCSV analyticsCSVData);

	//ByteArrayInputStream generateAnalyticsCSVForSingleUser(UserAnalyticsBookingDetails userAnalyticsBookingDetails);


}
