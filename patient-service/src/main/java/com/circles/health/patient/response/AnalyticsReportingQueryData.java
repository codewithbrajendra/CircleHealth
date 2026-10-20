package com.circles.health.patient.response;

import java.time.LocalDate;

public class AnalyticsReportingQueryData {
	
	private Long userId;
	private String gender;
	private String ethnicity;
	private Integer age;
	private LocalDate startDate;
	private LocalDate endDate;
	private Long totalBooking;
	private Long distinctUser;
	private LocalDate ageStart;
	private LocalDate ageEnd;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEthnicity() {
		return ethnicity;
	}
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
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
	public Long getTotalBooking() {
		return totalBooking;
	}
	public void setTotalBooking(Long totalBooking) {
		this.totalBooking = totalBooking;
	}
	public Long getDistinctUser() {
		return distinctUser;
	}
	public void setDistinctUser(Long distinctUser) {
		this.distinctUser = distinctUser;
	}
	public LocalDate getAgeStart() {
		return ageStart;
	}
	public void setAgeStart(LocalDate ageStart) {
		this.ageStart = ageStart;
	}
	public LocalDate getAgeEnd() {
		return ageEnd;
	}
	public void setAgeEnd(LocalDate ageEnd) {
		this.ageEnd = ageEnd;
	}

}
