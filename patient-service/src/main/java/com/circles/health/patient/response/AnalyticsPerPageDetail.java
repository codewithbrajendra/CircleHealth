package com.circles.health.patient.response;

import java.time.LocalDate;

public class AnalyticsPerPageDetail {
	
	private String pagePath;
	private String pageViews;
	private String totalTimeOnPage;
	private String avgTimeOnPage;
	private String  pageVisitTime;
	private LocalDate visitDate;
	
	public String getPagePath() {
		return pagePath;
	}
	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}
	public String getPageViews() {
		return pageViews;
	}
	public void setPageViews(String pageViews) {
		this.pageViews = pageViews;
	}
	public String getTotalTimeOnPage() {
		return totalTimeOnPage;
	}
	public void setTotalTimeOnPage(String totalTimeOnPage) {
		this.totalTimeOnPage = totalTimeOnPage;
	}
	public String getAvgTimeOnPage() {
		return avgTimeOnPage;
	}
	public void setAvgTimeOnPage(String avgTimeOnPage) {
		this.avgTimeOnPage = avgTimeOnPage;
	}
	public LocalDate getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(LocalDate visitDate) {
		this.visitDate = visitDate;
	}
	public String getPageVisitTime() {
		return pageVisitTime;
	}
	public void setPageVisitTime(String pageVisitTime) {
		this.pageVisitTime = pageVisitTime;
	}

}
