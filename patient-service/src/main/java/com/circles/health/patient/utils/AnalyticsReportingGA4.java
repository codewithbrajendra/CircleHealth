package com.circles.health.patient.utils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.circles.health.patient.response.AnalyticsPerPageDetail;
import com.circles.health.patient.response.PatientAnalyticsData;
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

public class AnalyticsReportingGA4 {

  public static void main(String... args) throws Exception {
//    /**
//     * TODO(developer): Replace this variable with your Google Analytics 4 property ID before
//     * running the sample.
//     */
    String propertyId = "305360491"; //UAT - 298701590 PROD - 305360491
    sampleRunReport(propertyId);
  }
//
//  // This is an example snippet that calls the Google Analytics Data API and runs a simple report
//  // on the provided GA4 property id.
  static void sampleRunReport(String propertyId) throws Exception {
//    /**
//     * TODO(developer): Uncomment this variable and replace with your Google Analytics 4 property ID
//     * before running the sample.
//     */
//    // propertyId = "YOUR-GA4-PROPERTY-ID";
//
//    // Using a default constructor instructs the client to use the credentials
//    // specified in GOOGLE_APPLICATION_CREDENTIALS environment variable.
    try (BetaAnalyticsDataClient analyticsData = BetaAnalyticsDataClient.create()) {
    	PatientAnalyticsData patientAnalyticsData = new PatientAnalyticsData();

	
//    	RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + propertyId)
//			    .addDimensions(Dimension.newBuilder().setName("customUser:dob"))
//				.addMetrics(Metric.newBuilder().setName("sessions"))
//				.addMetrics(Metric.newBuilder().setName("activeusers"))
//				.addDateRanges(DateRange.newBuilder().setStartDate("2022-03-01").setEndDate("2022-03-06"))
//				 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
//				 ().setFieldName("customUser:ethnicity")
//				.setStringFilter(StringFilter.newBuilder().setValue("Other Ethnic Group")) ))
//				.build();
//
//    	LocalDate startDate = LocalDate.parse("1987-03-07");
//    	LocalDate endDate = LocalDate.parse("1996-03-07");
//    	
//	RunReportResponse response = analyticsData.runReport(request);
//	System.out.println("response ::: "+response);
//	if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
//		List<Row> rows = response.getRowsList().stream().filter(x -> {
//			if (!x.getDimensionValues(0).getValue().equals("undefined")
//					&& !x.getDimensionValues(0).getValue().equals("0")
//					&& !x.getDimensionValues(0).getValue().equals("(not set)")
//					&& startDate
//							.isBefore(LocalDate.parse(x.getDimensionValues(0).getValue()))
//					&& endDate
//							.isAfter(LocalDate.parse(x.getDimensionValues(0).getValue()))) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//		).collect(Collectors.toList());
//		System.out.println("rows ::: "+rows);
//		for (Row row : rows) {
//			patientAnalyticsData.setTotalVisitCount(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
//		}
//	
//	
//	
//    	System.out.println("Total visit count ::: "+patientAnalyticsData.getTotalVisitCount());
    	
    	
    	
//    	
//    	Map<String, String> map = new HashMap<String, String>();
//		map.put("/", "Login Page");
//		map.put("/forgottencode", "Forget Code");
//		map.put("/home", "Home");
//		map.put("/aboutus", "About Us");
//		map.put("/wellnesscheck", "Wellness Check");
//		map.put("/profilingquestion", "Profiling Question");
//		map.put("/blogs", "Blogs");
//		map.put("/faq", "FAQ");
//		map.put("/bookingschedule", "Book Schedule");
//		map.put("/cookie", "Cookie");
//		map.put("/privacy", "Privacy");
//		map.put("/terms", "Terms");
//		map.put("/aup", "AUP");
//		map.put("/contactus", "Contact Us");
//		map.put("/myprofile", "My Profile");
//		map.put("/support", "Support");
//		map.put("/myprofile/mywellnesscheck", "My Wellness Check");
//		map.put("/myprofile/myschedule", "My Schedule");
//		map.put("/myprofile/myprofiling", "Profiling Question");
//		map.put("/myprofile/myprofilesetting", "Profile Details");
//
//		RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + propertyId)
//				.addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
//				.addDimensions(Dimension.newBuilder().setName("pagePath"))
//				.addDimensions(Dimension.newBuilder().setName("customUser:date_time"))
//				.addMetrics(Metric.newBuilder().setName("screenPageViews"))
//				.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
//				.addMetrics(Metric.newBuilder().setName("averageDuration")
//						.setExpression("userEngagementDuration/engagedSessions"))
//				.addDateRanges(
//						DateRange.newBuilder().setStartDate("2022-03-01")
//								.setEndDate("2022-03-07"))
//				.setDimensionFilter(FilterExpression.newBuilder()
//						.setFilter(Filter.newBuilder().setFieldName("customUser:crm_id")
//								.setStringFilter(StringFilter.newBuilder()
//										.setValue(String.valueOf(3)))))
//				.build();
//		RunReportResponse response = analyticsData.runReport(request);
//		System.out.println("  Per page PAGESingle User RUN REPORT RESPONSE rows count ::: " + response);
//
//		if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
//			
//			List<AnalyticsPerPageDetail> analyticsPerPageDetails = new ArrayList<>();
//			List<AnalyticsPerPageDetail> wellnessPageDetails = new ArrayList<>();
//			List<AnalyticsPerPageDetail> profilePageDetails = new ArrayList<>();
//			int profileQuestionCount = 1;
//			for (Row row : response.getRowsList()) {
//				AnalyticsPerPageDetail detail = new AnalyticsPerPageDetail();
//				boolean otherPageThanMapValue = false;
//				if (row.getDimensionValues(1) != null && row.getDimensionValues(1).getValue() != null
//						&& !row.getDimensionValues(1).getValue().isBlank()) {
//					String pagePath = row.getDimensionValues(1).getValue();
//					String pageVisitTime = row.getDimensionValues(2).getValue().isEmpty()
//							|| row.getDimensionValues(2).getValue().equalsIgnoreCase("(not set)") ? ""
//									: row.getDimensionValues(2).getValue();
//
//					detail.setPageVisitTime(pageVisitTime);
//					// detail.setPageVisitTime(pageVisitTime!= null && !pageVisitTime.isBlank() ?
//					// pageVisitTime : pageVisitTime);
//					if (map.get(pagePath) != null) {
//						detail.setPagePath(map.get(pagePath));
//						otherPageThanMapValue = false;
//					} else if (pagePath.contains("takewellnesscheck/")) {
//						if (pagePath.split("/").length > 2) {
//							detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank()
//									? pagePath.split("/")[2]
//									: pagePath);
//							otherPageThanMapValue = false;
//						} else {
//							otherPageThanMapValue = true;
//						}
//					} else if (pagePath.contains("profiling/")) {
//
//						// profileQuestionCount++;
//						 if (profileQuestionCount <= 14 && pagePath.split("/").length > 2) {
//
//						if (pagePath.split("/").length > 2) {
//							detail.setPagePath(pagePath.split("/")[2] != null && !pagePath.split("/")[2].isBlank()
//									? ("Q-" + pagePath.split("/")[2])
//									: pagePath);
//							otherPageThanMapValue = false;
//						} else {
//							otherPageThanMapValue = true;
//						}
//
//					}
//					}
//
//					//
//					else {
//						otherPageThanMapValue = true;
//					}
//
//					if (row.getMetricValues(0) != null && row.getMetricValues(0).getValue() != null
//							&& !row.getMetricValues(0).getValue().isBlank()) {
//
//						detail.setPageViews(row.getMetricValues(0).getValue());
//					}
//
//					if (row.getMetricValues(1) != null && row.getMetricValues(1).getValue() != null
//							&& !row.getMetricValues(1).getValue().isBlank()) {
//						double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
//						detail.setTotalTimeOnPage(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
//					} else {
//						detail.setTotalTimeOnPage("0");
//					}
//
//					if (row.getMetricValues(2) != null && row.getMetricValues(2).getValue() != null
//							&& !row.getMetricValues(2).getValue().isBlank()) {
//						if (row.getMetricValues(2).getValue().equals("0")) {
//							detail.setAvgTimeOnPage("0");
//						} else {
//							double timeInSeconds = Double.parseDouble(row.getMetricValues(2).getValue());
//							detail.setAvgTimeOnPage(
//									String.valueOf(new DecimalFormat("0.00").format(timeInSeconds)));
//						}
//					} else {
//						detail.setAvgTimeOnPage("0");
//					}
//
//					if (!otherPageThanMapValue) {
//						if (pagePath.contains("takewellnesscheck/PHQ")
//								|| pagePath.contains("takewellnesscheck/GAD")) {
//							wellnessPageDetails.add(detail);
//
//						} else if (pagePath.contains("profiling/")) {
//							profilePageDetails.add(detail);
//
//						}
//
//						else {
//							analyticsPerPageDetails.add(detail);
//						}
//
//					}
//				}
//			}
//			if (wellnessPageDetails != null && !wellnessPageDetails.isEmpty()) {
//
//				wellnessPageDetails.sort((AnalyticsPerPageDetail a1,
//						AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])
//								- (Integer.parseInt(a2.getPagePath().split("-")[1]))));
//				System.out.println("wellnessPageDetails ::: "+wellnessPageDetails);
//
//				
//			}
//			if (profilePageDetails != null && !profilePageDetails.isEmpty()) {
//
//				profilePageDetails.sort((AnalyticsPerPageDetail a1,
//						AnalyticsPerPageDetail a2) -> (Integer.parseInt(a1.getPagePath().split("-")[1])
//								- (Integer.parseInt(a2.getPagePath().split("-")[1]))));
//				System.out.println("profilePageDetails ::: "+
//						profilePageDetails != null && !profilePageDetails.isEmpty() ? profilePageDetails : null);
//			}
//
//			System.out.println("analyticsPerPageDetails ::: "+
//					analyticsPerPageDetails != null && !analyticsPerPageDetails.isEmpty() ? analyticsPerPageDetails
//							: null);
//
//		}
    	
    	
    	
    	
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
		
		RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + propertyId)
				.addDimensions(Dimension.newBuilder().setName("pagePath"))
				.addDimensions(Dimension.newBuilder().setName("customUser:dob"))
				.addDimensions(Dimension.newBuilder().setName("customUser:gender"))
				.addMetrics(Metric.newBuilder().setName("screenPageViews"))
				.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
				.addMetrics(Metric.newBuilder().setName("averageDuration").setExpression("userEngagementDuration/sessions"))
				.addMetrics(Metric.newBuilder().setName("averageDurationPeruser").setExpression("userEngagementDuration/activeUsers"))
				.addDateRanges(DateRange.newBuilder().setStartDate("2022-03-02").setEndDate("2022-03-08"))
				 .setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder
						  ().setFieldName("customUser:gender")
						  .setStringFilter(StringFilter.newBuilder().setValue("male")) ))
						 
				
				.build();

		LocalDate ageStart = LocalDate.parse("1987-03-08");
    	LocalDate ageEnd = LocalDate.parse("1996-03-08");
		
		
		RunReportResponse response = analyticsData.runReport(request);
		System.out.println("response ::: "+response);
        int profileCount = 0;
		if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
			List<AnalyticsPerPageDetail> analyticsPerPageDetails = new ArrayList<> ();
			List<AnalyticsPerPageDetail> wellnessPageDetails = new ArrayList<> ();
			List<AnalyticsPerPageDetail> profilePageDetails = new ArrayList<> ();

			List<Row> rows = response.getRowsList().stream().filter(x -> {
				if (!x.getDimensionValues(1).getValue().equals("undefined")
						&& !x.getDimensionValues(1).getValue().equals("0")
						&& !x.getDimensionValues(1).getValue().equals("(not set)")
						&& ageStart
								.isBefore(LocalDate.parse(x.getDimensionValues(1).getValue()))
						&& ageEnd
								.isAfter(LocalDate.parse(x.getDimensionValues(1).getValue()))) {
					return true;
				} else {
					return false;
				}
			}
			).collect(Collectors.toList());
			System.out.println("rows ::: "+rows);
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
    	
//    	
//    	RunReportRequest request = RunReportRequest.newBuilder().setProperty("properties/" + propertyId)
//				.addDimensions(Dimension.newBuilder().setName("customUser:crm_id"))
//				.addMetrics(Metric.newBuilder().setName("sessions"))
//				.addMetrics(Metric.newBuilder().setName("userEngagementDuration"))
//				.addDateRanges(DateRange.newBuilder().setStartDate("2022-03-01")
//				.setEndDate("2022-03-07"))
//				.setDimensionFilter(FilterExpression.newBuilder().setFilter(Filter.newBuilder().setFieldName("customUser:crm_id")
//			    .setStringFilter(StringFilter.newBuilder().setValue(String.valueOf(435)))))
//				.build();
//
//	RunReportResponse response = analyticsData.runReport(request);
//	System.out.println("response ::: "+response);
//	if (response != null && response.getRowsList() != null && !response.getRowsList().isEmpty()) {
//		for (Row row : response.getRowsList()) {
//			patientAnalyticsData.setTotalVisitCount(row.getMetricValues(0) != null  ? row.getMetricValues(0).getValue() : "0");
//			if (row.getMetricValues(1) != null) {
//				double timeInMinutes = Double.parseDouble(row.getMetricValues(1).getValue()) / 60;
//				patientAnalyticsData.setTotalTimeUserSpentOnWebsite(String.valueOf(new DecimalFormat("0.00").format(timeInMinutes)));
//			}
//		}
//	}
    	
    	
    }
  
     }
  //  }
  }


