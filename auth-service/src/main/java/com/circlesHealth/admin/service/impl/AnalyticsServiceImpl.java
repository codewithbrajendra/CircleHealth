package com.circlesHealth.admin.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.circlesHealth.admin.response.AnalyticsExitAndBlogPageDetails;
import com.circlesHealth.admin.response.AnalyticsPerPageDetail;
import com.circlesHealth.admin.response.AnalyticsReportingQueryData;
import com.circlesHealth.admin.response.PatientAnalyticsData;
import com.circlesHealth.admin.response.PatientAnalyticsSingleUserCSV;
import com.circlesHealth.admin.service.AnalyticsService;
import com.circlesHealth.admin.utils.JsonUtil;
import com.circlesHealth.admin.utils.RestTemplateUtil;

@Service
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

	@Autowired
    RestTemplate restTemplate;
	
	@Value("${patient.service.base.api.url}")
    String patientServiceUrl;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

	@Override
	public Object getAnalyticsDataForAllUsers(AnalyticsReportingQueryData analyticsReportingQueryData, String token, String userUniqueId) {
		LOGGER.info("inside service impl with analytics daterange  :: " + analyticsReportingQueryData.getStartDate() 
			+ " enddate :: " + analyticsReportingQueryData.getEndDate());
		Object obj = null;
		try {
			obj = RestTemplateUtil.postMethod(patientServiceUrl + "/analytics/getanalyticsdataforallusers", token, userUniqueId,
					JsonUtil.convertObjectToString(analyticsReportingQueryData), restTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}

	@Override
	public Object getAnalyticsDataForUser(AnalyticsReportingQueryData analyticsReportingQueryData, String token,
			String userUniqueId) {
		LOGGER.info("inside service impl with analytics for a user from daterange  :: " + analyticsReportingQueryData.getStartDate() 
		+ " enddate :: " + analyticsReportingQueryData.getEndDate());
		Object obj = null;
		try {
			obj = RestTemplateUtil.postMethod(patientServiceUrl + "/analytics/getanalyticsdataforuser", token, userUniqueId,
					JsonUtil.convertObjectToString(analyticsReportingQueryData), restTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}

	@Override
	public PatientAnalyticsData getAnalyticsCSVForAllUsers(AnalyticsReportingQueryData analyticsReportingQueryData) {
		LOGGER.info("inside service impl with analytics for a csv from daterange  :: " + analyticsReportingQueryData.getStartDate() 
		+ " enddate :: " + analyticsReportingQueryData.getEndDate());
		try {
			PatientAnalyticsData resp = RestTemplateUtil.postMethodForDownloadCSVForAllUsers(patientServiceUrl 
					+ "/analytics/getanalyticscsvdataforallusers", JsonUtil.convertObjectToString(analyticsReportingQueryData), restTemplate);
			if (resp != null) {
				return resp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	
	@Override
	public ByteArrayInputStream generateAnalyticsCSVForAllUsers3(PatientAnalyticsData analyticsData) {
		ByteArrayOutputStream out = null;
		CSVPrinter csvPrinter =null;
		
		try {
			if (analyticsData != null) {
				out = new ByteArrayOutputStream();
				String[] headers = {"TOTAL VISIT","UNIQUE VISITORS COUNT", "TOTAL WELLNESS CHECK", "UNIQUE WELLNESS CHECK", "WELLNESS CHECK ATTEMPTED(In Percentage)"
						,"TOTAL PROFILE QUESTION", "UNIQUE PROFILE QUESTION", "TOTAL BOOKING", "BOOKING PERCENTAGE", "NUMBER OF TIMES VIDEO WATCHED"};
				List<String> headersList = new ArrayList<>(Arrays.asList(headers));
				List<String> csvData = new ArrayList<>();
				csvData.add(analyticsData.getTotalVisitCount());
				csvData.add(analyticsData.getUniqueUserCount());
				csvData.add(analyticsData.getTotalWellnessCheck());
				csvData.add(analyticsData.getUniqueVisitorWellnessCheck());
				csvData.add(analyticsData.getUsersPercentageWhoAttemptedWellnessCheck() + "%");
				csvData.add(analyticsData.getTotalProfileQuestion());
				csvData.add(analyticsData.getUniqueVisitorsProfileQuestion());
				csvData.add(analyticsData.getBooking());
				csvData.add(analyticsData.getBookingPercentage() + "%");
				csvData.add(analyticsData.getTotalTimeVideoWatched());
				
				if(analyticsData.getWellnessPageDetails() != null && !analyticsData.getWellnessPageDetails().isEmpty()) {
					for (AnalyticsPerPageDetail detail : analyticsData.getWellnessPageDetails()) {
						csvData.add(detail.getPageViews());
						csvData.add(detail.getTotalTimeOnPage() + " minutes");
						csvData.add(detail.getAvgTimeOnPage() + " seconds");
						headersList.add(detail.getPagePath());
						headersList.add(detail.getPagePath() + " Total Time");
						headersList.add(detail.getPagePath() + " Average Time");
					}
				}
				
				if (analyticsData.getProfilePageDetails() != null && !analyticsData.getProfilePageDetails().isEmpty()) {
					for (AnalyticsPerPageDetail detail : analyticsData.getProfilePageDetails()) {
						csvData.add(detail.getPageViews());
						csvData.add(detail.getTotalTimeOnPage() + " minutes");
						csvData.add(detail.getAvgTimeOnPage() + " seconds");
						headersList.add(detail.getPagePath());
						headersList.add(detail.getPagePath() + " Total Time");
						headersList.add(detail.getPagePath() + " Average Time");
					}
				}
				
				if (analyticsData.getPerPageDetails() != null && !analyticsData.getPerPageDetails().isEmpty()) {
					for (AnalyticsPerPageDetail detail : analyticsData.getPerPageDetails()) {
						csvData.add(detail.getPageViews());
						csvData.add(detail.getTotalTimeOnPage() + " minutes");
						csvData.add(detail.getAvgTimeOnPage() + " seconds");
						headersList.add(detail.getPagePath());
						headersList.add(detail.getPagePath() + " Total Time");
						headersList.add(detail.getPagePath() + " Average Time");
					}
				}
				
				CSVFormat format = CSVFormat.DEFAULT.withHeader(headersList.toArray(String[]::new));
				csvPrinter = new CSVPrinter(new PrintWriter(out), format);
				csvPrinter.printRecord(csvData);
				csvPrinter.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	@Override
	public ByteArrayInputStream generateAnalyticsCSVForAllUsers(PatientAnalyticsData analyticsData) {
		ByteArrayOutputStream out = null;
		CSVPrinter csvPrinter = null;

		try {
			if (analyticsData != null) {
				out = new ByteArrayOutputStream();
				String[] headers = { "Total Video Watched", "Total Spent Times", "page name", "Avg time in seconds",
						"Total time in minuts", "Time spent on each  wellness questions" };
				List<String[]> data = new ArrayList<String[]>();
				LOGGER.info("videodata" + analyticsData.getTotalVisitCount());
				data.add(new String[] { "Total Visit Count", analyticsData.getTotalVisitCount() });
				data.add(new String[] { "UNIQUE VISITORS COUNT", analyticsData.getUniqueUserCount() });

				data.add(new String[] { "TOTAL WELLNESS CHECK", analyticsData.getTotalWellnessCheck() });
				data.add(new String[] { "UNIQUE USER HAD WELLNESS CHECK", analyticsData.getUniqueVisitorWellnessCheck()});
				data.add(new String[] { "WELLNESS CHECK ATTEMPTED BY UNIQUES USERS(In Percentage)", analyticsData.getUsersPercentageWhoAttemptedWellnessCheck() + "%"});
				data.add(new String[] { "USERS HAD WELLNESS AND PROFILING", analyticsData.getTotalProfileQuestion() });
				data.add(new String[] { "TOTAL BOOKING", analyticsData.getBooking() });
				data.add(new String[] { "BOOKING PERCENTAGE", analyticsData.getBookingPercentage() + "%" });
				data.add(new String[] { "NUMBER OF TIMES VIDEO WATCHED", analyticsData.getTotalTimeVideoWatched() });
				if (analyticsData.getWellnessPageDetails() != null
						&& !analyticsData.getWellnessPageDetails().isEmpty()) {
					data.add(new String[] {});
					for (AnalyticsPerPageDetail detail : analyticsData.getWellnessPageDetails()) {
						data.add(new String[] { detail.getPagePath(), detail.getPageViews() });
						data.add(new String[] { detail.getPagePath() + " Total Time", detail.getTotalTimeOnPage() + " minutes" });
						data.add(new String[] { detail.getPagePath() + " Average Time", detail.getAvgTimeOnPage() + " seconds"});
					}
				}

				if (analyticsData.getProfilePageDetails() != null && !analyticsData.getProfilePageDetails().isEmpty()) {
					data.add(new String[] {});
					for (AnalyticsPerPageDetail detail : analyticsData.getProfilePageDetails()) {
						data.add(new String[] { detail.getPagePath(), detail.getPageViews() });
						data.add(new String[] { detail.getPagePath() + " Total Time", detail.getTotalTimeOnPage() + " minutes" });
						data.add(new String[] { detail.getPagePath() + " Average Time", detail.getAvgTimeOnPage() + " seconds" });
					}
				}

				if (analyticsData.getPerPageDetails() != null && !analyticsData.getPerPageDetails().isEmpty()) {
					data.add(new String[] {});
					for (AnalyticsPerPageDetail detail : analyticsData.getPerPageDetails()) {
						data.add(new String[] { detail.getPagePath(), detail.getPageViews() });
						data.add(new String[] { detail.getPagePath() + " Total Time", detail.getTotalTimeOnPage() + " minutes" });
						data.add(new String[] { detail.getPagePath() + " Average Time", detail.getAvgTimeOnPage() + " seconds" });
					}
				}

				data.add(new String[]{});
				data.add(new String[] {" Blog Platform Details"});
			
				if (analyticsData.getBlogPageDetails() != null && !analyticsData.getBlogPageDetails().isEmpty()) {
					for (AnalyticsExitAndBlogPageDetails detail : analyticsData.getBlogPageDetails()) {
						LOGGER.info("detail.getPagePath() Blog page detail new ::: "+detail.getPagePath());
					   // LOGGER.info("detail.getPageViews() Blog page detail ::: "+detail.getPageViews());
						data.add(new String[] { detail.getPagePath(), detail.getPageCount() });
						//data.add(new String[] { detail.getPagePath() + " Total Time", detail.getPageVisitTime() + " minutes" });
						//LOGGER.info("Total visit time Blog page detail ::: "+detail.getPageVisitTime());
						//data.add(new String[] { detail.getPagePath() + " Average Time", detail.getAvgTimeOnPage() + " seconds" });
					}
				}
								data.add(new String[]{});
				data.add(new String[] {"Exit Platform Details"});
				if (analyticsData.getExitPageDetails() != null && !analyticsData.getExitPageDetails().isEmpty()) {
					for (AnalyticsExitAndBlogPageDetails detail : analyticsData.getExitPageDetails()) {
						//LOGGER.info("detail.getPageViews() exit page detail ::: "+detail.getPageViews());
						LOGGER.info("detail.getPagePath( exit page detail ::: "+detail.getPagePath());
						data.add(new String[] { detail.getPagePath(), detail.getPageCount() });
						//data.add(new String[] { detail.getPagePath() + " Total Time", detail.getTotalTimeOnPage() + " minutes" });
						//data.add(new String[] { detail.getPagePath() + " Average Time", detail.getAvgTimeOnPage() + " seconds" });
					}
				}
				
				CSVFormat format = CSVFormat.DEFAULT.withHeader();
				csvPrinter = new CSVPrinter(new PrintWriter(out), format);
				csvPrinter.printRecords(data);
				csvPrinter.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ByteArrayInputStream(out.toByteArray());
	}

		@Override
	public PatientAnalyticsSingleUserCSV getAnalyticsDataForSingleUserCSV(
			AnalyticsReportingQueryData analyticsReportingQueryData) {
			LOGGER.info("inside service impl with analytics for a csv from daterange  :: " + analyticsReportingQueryData.getStartDate() 
			+ " enddate :: " + analyticsReportingQueryData.getEndDate() + "userId :: "+analyticsReportingQueryData.getUserId());
			try {
				PatientAnalyticsSingleUserCSV resp = RestTemplateUtil.getMethodForDownloadCSVForSingleUser(patientServiceUrl 
						+ "/analytics/getanalyticscsvforsingleuser", JsonUtil.convertObjectToString(analyticsReportingQueryData), restTemplate);
				LOGGER.info("Response from Patient Service for single user csv ::: "+resp);
				if (resp != null) {
					return resp;
				}
			} catch (Exception e) {
				LOGGER.info("Some exception occur from Patient Service for single user csv ::: "+e.getMessage());
				e.printStackTrace();
			}
			return null;
	}

	@Override
	public ByteArrayInputStream getAnalyticsDataForSingleUserCSV(PatientAnalyticsSingleUserCSV analyticsCSVData) {
		
		
		ByteArrayOutputStream out = null;
		CSVPrinter csvPrinter = null;

		try {
				out = new ByteArrayOutputStream();
				List<String[]> data = new ArrayList<String[]>();
				LOGGER.info("Total visit count ::: "+analyticsCSVData.getTotalVisit());
				data.add(new String[] {"Total Visit Count",analyticsCSVData.getTotalVisit()});
				LOGGER.info("Total  Time Spent ::: "+analyticsCSVData.getTimeSpentonEachPage());
				data.add(new String[] {"Total  Time Spent",analyticsCSVData.getTimeSpentonEachPage()});
				LOGGER.info("Total Wellness ::: "+analyticsCSVData.getTotalWellness());
				data.add(new String[] {"Total Wellness",analyticsCSVData.getTotalWellness()});
				LOGGER.info("TotalWellness Profilling Complted ::: "+analyticsCSVData.getTotalwellnessProfillingComplted());
				data.add(new String[] {"TotalWellness Profilling Complted",analyticsCSVData.getTotalwellnessProfillingComplted()});
				LOGGER.info("Total Booking ::: "+analyticsCSVData.getTotalBooking());
				data.add(new String [] {"Total Booking",analyticsCSVData.getTotalBooking()});
				LOGGER.info("Total video Watched ::: "+analyticsCSVData.getTotalVideoWatched());
				data.add(new String[]{"Total video Watched",analyticsCSVData.getTotalVideoWatched()});
				data.add(new String[] {});
				data.add(new String[] {" Time spent on each Wellness question"});
				if (analyticsCSVData.getWellnessPageDetails() != null
						&& !analyticsCSVData.getWellnessPageDetails().isEmpty())

				{
					LOGGER.info("  Wellness size   "+ analyticsCSVData.getWellnessPageDetails().size());

					for (AnalyticsPerPageDetail detail : analyticsCSVData.getWellnessPageDetails()) {
						String date = "", time = "";
						if (detail.getPageVisitTime() != null && !detail.getPageVisitTime().isEmpty()) {
							String[] arry = (detail.getPageVisitTime().replace("Z", "")).split("T");
							date = arry[0];
							time = arry[1];
						}
						data.add(new String[] { detail.getPagePath() + " Date & Time ", date, time });
					}
				}

				data.add(new String[] {});
				data.add(new String[] {"Time spent on each Profiling question"});
				if (analyticsCSVData.getProfilePageDetails() != null && !analyticsCSVData.getProfilePageDetails().isEmpty()) {
					for (AnalyticsPerPageDetail detail : analyticsCSVData.getProfilePageDetails()) {
						String date = "", time = "";
						if (detail.getPageVisitTime() != null && !detail.getPageVisitTime().isEmpty()) {
							String[] arry = (detail.getPageVisitTime().replace("Z", "")).split("T");
							date = arry[0];
							time = arry[1];
						}
						data.add(new String[] { detail.getPagePath() + " Date & Time ", date, time });					}
				}
								data.add(new String[]{});

				data.add(new String[] {"Time spent on each Page"});
				if (analyticsCSVData.getPerPageDetails() != null && !analyticsCSVData.getPerPageDetails().isEmpty()) {
					for (AnalyticsPerPageDetail detail : analyticsCSVData.getPerPageDetails()) {
						String date = "", time = "";
						if (detail.getPageVisitTime() != null && !detail.getPageVisitTime().isEmpty()) {
							String[] arry = (detail.getPageVisitTime().replace("Z", "")).split("T");
							date = arry[0];
							time = arry[1];
						}
						data.add(new String[] { detail.getPagePath() + " Date & Time ", date, time });					}
				}
			data.add(new String[]{});
				data.add(new String[] {" Blog Platform Details"});
			
				if (analyticsCSVData.getBlogPageDetails() != null && !analyticsCSVData.getBlogPageDetails().isEmpty()) {
					for (AnalyticsExitAndBlogPageDetails detail : analyticsCSVData.getBlogPageDetails()) {
						LOGGER.info("detail.getPagePath() Blog page detail new ::: "+detail.getPagePath());
					   // LOGGER.info("detail.getPageViews() Blog page detail ::: "+detail.getPageViews());
						data.add(new String[] { detail.getPagePath(), detail.getPageCount() });
						//data.add(new String[] { detail.getPagePath() + " Total Time", detail.getPageVisitTime() + " minutes" });
						//LOGGER.info("Total visit time Blog page detail ::: "+detail.getPageVisitTime());
						//data.add(new String[] { detail.getPagePath() + " Average Time", detail.getAvgTimeOnPage() + " seconds" });
					}
				}
								data.add(new String[]{});
				data.add(new String[] {"Exit Platform Details"});
				if (analyticsCSVData.getExitPageDetails() != null && !analyticsCSVData.getExitPageDetails().isEmpty()) {
					for (AnalyticsExitAndBlogPageDetails detail : analyticsCSVData.getExitPageDetails()) {
						//LOGGER.info("detail.getPageViews() exit page detail ::: "+detail.getPageViews());
						LOGGER.info("detail.getPagePath( exit page detail ::: "+detail.getPagePath());
						data.add(new String[] { detail.getPagePath(), detail.getPageCount() });
						//data.add(new String[] { detail.getPagePath() + " Total Time", detail.getTotalTimeOnPage() + " minutes" });
						//data.add(new String[] { detail.getPagePath() + " Average Time", detail.getAvgTimeOnPage() + " seconds" });
					}
				}
				
				
				CSVFormat format = CSVFormat.DEFAULT.withHeader();
				csvPrinter = new CSVPrinter(new PrintWriter(out), format);
				csvPrinter.printRecords(data);
				csvPrinter.flush();
			
		} catch (Exception e) {
			LOGGER.info("Some exception occured inside getAnalyticsDataForSingleUserCSV of AnalyticsServiceImpl ::: "+e.getMessage());
			e.printStackTrace();
		}

		return new ByteArrayInputStream(out.toByteArray());
	}
	
}
