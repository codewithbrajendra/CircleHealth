package com.circles.health.patient.request;

import java.time.LocalDate;

public class UserAnalyticsReportingQueryData {

	private Long userId;
	private LocalDate startDate;
	private LocalDate endDate;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	
}
