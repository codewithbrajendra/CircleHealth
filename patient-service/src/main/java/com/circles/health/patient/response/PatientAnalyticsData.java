package com.circles.health.patient.response;

import java.util.List;

public class PatientAnalyticsData {
	
	private Long userId;
	private String totalVisitCount;
	private String uniqueUserCount;
	private String totalWellnessCheck;
	private String uniqueVisitorWellnessCheck;
	private String usersPercentageWhoAttemptedWellnessCheck;
	private String totalProfileQuestion;
	private String uniqueVisitorsProfileQuestion;
	private String booking;
	private String bookingPercentage;
	private String totalTimeVideoWatched;
	private String totalTimeUserSpentOnWebsite;
	private List<AnalyticsPerPageDetail> perPageDetails;
	private List<AnalyticsPerPageDetail> wellnessPageDetails;
	private List<AnalyticsPerPageDetail> profilePageDetails;
	private List<AnalyticsExitAndBlogPageDetails> blogPageDetails;
	private List<AnalyticsExitAndBlogPageDetails> exitPageDetails;

	private List<AnalyticsPerPageDetail>allwebsitegenderlist;


	private List<AnalyticsPerPageDetail> patientVisitDetailsByDate;
	
	public String getTotalVisitCount() {
		return totalVisitCount;
	}
	public void setTotalVisitCount(String totalVisitCount) {
		this.totalVisitCount = totalVisitCount;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUniqueUserCount() {
		return uniqueUserCount;
	}
	public void setUniqueUserCount(String uniqueUserCount) {
		this.uniqueUserCount = uniqueUserCount;
	}
	public String getTotalWellnessCheck() {
		return totalWellnessCheck;
	}
	public void setTotalWellnessCheck(String totalWellnessCheck) {
		this.totalWellnessCheck = totalWellnessCheck;
	}
	public String getTotalProfileQuestion() {
		return totalProfileQuestion;
	}
	public void setTotalProfileQuestion(String totalProfileQuestion) {
		this.totalProfileQuestion = totalProfileQuestion;
	}
	public String getUniqueVisitorWellnessCheck() {
		return uniqueVisitorWellnessCheck;
	}
	public void setUniqueVisitorWellnessCheck(String uniqueVisitorWellnessCheck) {
		this.uniqueVisitorWellnessCheck = uniqueVisitorWellnessCheck;
	}
	public String getUsersPercentageWhoAttemptedWellnessCheck() {
		return usersPercentageWhoAttemptedWellnessCheck;
	}
	public void setUsersPercentageWhoAttemptedWellnessCheck(String usersPercentageWhoAttemptedWellnessCheck) {
		this.usersPercentageWhoAttemptedWellnessCheck = usersPercentageWhoAttemptedWellnessCheck;
	}
	public List<AnalyticsPerPageDetail> getPatientVisitDetailsByDate() {
		return patientVisitDetailsByDate;
	}
	public void setPatientVisitDetailsByDate(List<AnalyticsPerPageDetail> patientVisitDetailsByDate) {
		this.patientVisitDetailsByDate = patientVisitDetailsByDate;
	}
	public String getUniqueVisitorsProfileQuestion() {
		return uniqueVisitorsProfileQuestion;
	}
	public void setUniqueVisitorsProfileQuestion(String uniqueVisitorsProfileQuestion) {
		this.uniqueVisitorsProfileQuestion = uniqueVisitorsProfileQuestion;
	}
	public List<AnalyticsPerPageDetail> getPerPageDetails() {
		return perPageDetails;
	}
	public String getBooking() {
		return booking;
	}
	public void setBooking(String booking) {
		this.booking = booking;
	}
	public String getBookingPercentage() {
		return bookingPercentage;
	}
	public void setBookingPercentage(String bookingPercentage) {
		this.bookingPercentage = bookingPercentage;
	}
	public String getTotalTimeVideoWatched() {
		return totalTimeVideoWatched;
	}
	public void setTotalTimeVideoWatched(String totalTimeVideoWatched) {
		this.totalTimeVideoWatched = totalTimeVideoWatched;
	}
	public String getTotalTimeUserSpentOnWebsite() {
		return totalTimeUserSpentOnWebsite;
	}
	public void setTotalTimeUserSpentOnWebsite(String totalTimeUserSpentOnWebsite) {
		this.totalTimeUserSpentOnWebsite = totalTimeUserSpentOnWebsite;
	}
	public List<AnalyticsPerPageDetail> getWellnessPageDetails() {
		return wellnessPageDetails;
	}
	
	
	public void setWellnessPageDetails(List<AnalyticsPerPageDetail> wellnessPageDetails) {
		this.wellnessPageDetails = wellnessPageDetails;
	}
	public List<AnalyticsPerPageDetail> getProfilePageDetails() {
		return profilePageDetails;
	}
	public void setProfilePageDetails(List<AnalyticsPerPageDetail> profilePageDetails) {
		this.profilePageDetails = profilePageDetails;
	}
	public void setPerPageDetails(List<AnalyticsPerPageDetail> perPageDetails) {
		this.perPageDetails = perPageDetails;
	}
	public List<AnalyticsExitAndBlogPageDetails> getBlogPageDetails() {
		return blogPageDetails;
	}
	public void setBlogPageDetails(List<AnalyticsExitAndBlogPageDetails> blogPageDetails) {
		this.blogPageDetails = blogPageDetails;
	}
	public List<AnalyticsExitAndBlogPageDetails> getExitPageDetails() {
		return exitPageDetails;
	}
	public void setExitPageDetails(List<AnalyticsExitAndBlogPageDetails> exitPageDetails) {
		this.exitPageDetails = exitPageDetails;
	}
	public List<AnalyticsPerPageDetail> getAllwebsitegenderlist() {
		return allwebsitegenderlist;
	}
	public void setAllwebsitegenderlist(List<AnalyticsPerPageDetail> allwebsitegenderlist) {
		this.allwebsitegenderlist = allwebsitegenderlist;
	}
	
}
