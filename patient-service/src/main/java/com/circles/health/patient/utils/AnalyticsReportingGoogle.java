package com.circles.health.patient.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import com.google.api.services.analyticsreporting.v4.model.SearchUserActivityRequest;
import com.google.api.services.analyticsreporting.v4.model.SearchUserActivityResponse;
import com.google.api.services.analyticsreporting.v4.model.User;

public class AnalyticsReportingGoogle {
	private static final String APPLICATION_NAME = "Hello Analytics Reporting";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String KEY_FILE_LOCATION = "/home/techment/Downloads/circle-health-wellbeing-2021-b0b9df938fd7.json";
	private static final String VIEW_ID = "257371406";//296316086
//	private static final String VIEW_ID = "256944760";//257371406

	public static void main(String[] args) {
		try {
			AnalyticsReporting service = initializeAnalyticsReporting();

			GetReportsResponse response = getReport(service);
			System.out.println(response);
//			SearchUserActivityResponse activityResponse = getUserResponse(service);
//			printResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static SearchUserActivityResponse getUserResponse(AnalyticsReporting service) {
		SearchUserActivityResponse response = null;

		DateRange dateRange = new DateRange();
		dateRange.setStartDate("2021-12-15");
		dateRange.setEndDate("2021-12-22");
		
		User user = new User();
		user.setType("USER_ID");
		user.setUserId("1567");
		
		List<String> activityType = new ArrayList<>();
		activityType.add("PAGEVIEW");
	    
	    SearchUserActivityRequest request = new SearchUserActivityRequest().setViewId(VIEW_ID).
	    		setDateRange(dateRange).setUser(user).setActivityTypes(activityType);
	    
	    try {
			response = service.userActivity().search(request).execute();
			if (response != null) {
				System.out.println(response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * Initializes an Analytics Reporting API V4 service object.
	 *
	 * @return An authorized Analytics Reporting API V4 service object.
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private static AnalyticsReporting initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(KEY_FILE_LOCATION))
				.createScoped(AnalyticsReportingScopes.all());

		// Construct the Analytics Reporting service object.
		return new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}

	/**
	 * Queries the Analytics Reporting API V4.
	 *
	 * @param service An authorized Analytics Reporting API V4 service object.
	 * @return GetReportResponse The Analytics Reporting API V4 response.
	 * @throws IOException
	 */
	private static GetReportsResponse getReport(AnalyticsReporting service) throws IOException {
		// Create the DateRange object.
		DateRange dateRange = new DateRange();
		dateRange.setStartDate("2021-12-01");
		dateRange.setEndDate("today");

		ArrayList<Metric> metrics = new ArrayList<Metric>();
		String matricArray[] = {"ga:pageViews", "ga:pageValue", "ga:timeOnPage", "ga:avgTimeOnPage"};
	    for(String item : matricArray) {
	        metrics.add(new Metric().setExpression(item).setAlias(item.replace("ga:", "")));
	    }
	    
	    ArrayList<Dimension> dimensions = new ArrayList<Dimension>();
	    String[]  dimensionsArr = {"ga:pagePath"};
	    for(String item : dimensionsArr) {
	    	dimensions.add(new Dimension().setName(item));
	    }

	
		// Create the ReportRequest object.
		ReportRequest request = new ReportRequest().setViewId(VIEW_ID)
				.setDateRanges(Arrays.asList(dateRange))
				.setMetrics(metrics)
				.setDimensions(dimensions);

		ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
		requests.add(request);

		// Create the GetReportsRequest object.
		GetReportsRequest getReport = new GetReportsRequest().setReportRequests(requests);

		// Call the batchGet method.
		GetReportsResponse response = service.reports().batchGet(getReport).execute();

		// Return the response.
		return response;
	}

	/**
	 * Parses and prints the Analytics Reporting API V4 response.
	 *
	 * @param response An Analytics Reporting API V4 response.
	 */
	private static void printResponse(GetReportsResponse response) {

		for (Report report : response.getReports()) {
			ColumnHeader header = report.getColumnHeader();
			List<String> dimensionHeaders = header.getDimensions();
			List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
			List<ReportRow> rows = report.getData().getRows();
			

			if (rows == null) {
				System.out.println("No data found for " + VIEW_ID);
				return;
			}

			for (ReportRow row : rows) {
				List<String> dimensions = row.getDimensions();
				List<DateRangeValues> metrics = row.getMetrics();

				for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
					System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
				}

				for (int j = 0; j < metrics.size(); j++) {
					DateRangeValues values = metrics.get(j);
					for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
						System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
					}
				}
			}
		}
	}
}
