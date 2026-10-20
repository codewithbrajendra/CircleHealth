package com.circles.health.patient.response;

import java.util.List;


public class PatientAnalyticsSingleUserCSV {
	
	 private String totalVisit;
	 private String totalSpentTime; 
	 private String totalWellness;
	 private String totalwellnessProfillingComplted;
	 private String totalBooking;
	 private String totalVideoWatched;
	 private String timeSpentonEachPage;
	 private String timeSpentonEachWellnessQuestion;
	 private String timeSpentonEachProfilingQuestion;
	 private List<AnalyticsPerPageDetail> userPage;
		private List<AnalyticsExitAndBlogPageDetails> blogPageDetails;
		private List<AnalyticsExitAndBlogPageDetails> exitPageDetails;

	 private List<UserAnalyticsBookingDetails> bookingDetail;
	 private List<AnalyticsPerPageDetail> perPageDetails;
		private List<AnalyticsPerPageDetail> wellnessPageDetails;
		private List<AnalyticsPerPageDetail> profilePageDetails;
		
	 
	 public String getTotalVisit() {
		return totalVisit;
	}
	public void setTotalVisit(String totalVisit) {
		this.totalVisit = totalVisit;
	}
	public String getTotalSpentTime() {
		return totalSpentTime;
	}
	public void setTotalSpentTime(String totalSpentTime) {
		this.totalSpentTime = totalSpentTime;
	}
	public String getTotalWellness() {
		return totalWellness;
	}
	public void setTotalWellness(String totalWellness) {
		this.totalWellness = totalWellness;
	}
	public String getTotalwellnessProfillingComplted() {
		return totalwellnessProfillingComplted;
	}
	public void setTotalwellnessProfillingComplted(String totalwellnessProfillingComplted) {
		this.totalwellnessProfillingComplted = totalwellnessProfillingComplted;
	}
	public String getTotalBooking() {
		return totalBooking;
	}
	public void setTotalBooking(String totalBooking) {
		this.totalBooking = totalBooking;
	}
	public String getTotalVideoWatched() {
		return totalVideoWatched;
	}
	public void setTotalVideoWatched(String totalVideoWatched) {
		this.totalVideoWatched = totalVideoWatched;
	}
	public String getTimeSpentonEachPage() {
		return timeSpentonEachPage;
	}
	public void setTimeSpentonEachPage(String timeSpentonEachPage) {
		this.timeSpentonEachPage = timeSpentonEachPage;
	}
	public String getTimeSpentonEachWellnessQuestion() {
		return timeSpentonEachWellnessQuestion;
	}
	public void setTimeSpentonEachWellnessQuestion(String timeSpentonEachWellnessQuestion) {
		this.timeSpentonEachWellnessQuestion = timeSpentonEachWellnessQuestion;
	}
	public String getTimeSpentonEachProfilingQuestion() {
		return timeSpentonEachProfilingQuestion;
	}
	public void setTimeSpentonEachProfilingQuestion(String timeSpentonEachProfilingQuestion) {
		this.timeSpentonEachProfilingQuestion = timeSpentonEachProfilingQuestion;
	}
	
	public List<AnalyticsPerPageDetail> getUserPage() {
		return userPage;
	}
	public void setUserPage(List<AnalyticsPerPageDetail> userPage) {
		this.userPage = userPage;
	}
	
	public List<UserAnalyticsBookingDetails> getBookingDetail() {
		return bookingDetail;
	}
	public void setBookingDetail(List<UserAnalyticsBookingDetails> bookingDetail) {
		this.bookingDetail = bookingDetail;
	}
	public List<AnalyticsPerPageDetail> getPerPageDetails() {
		return perPageDetails;
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
	public void setPerPageDetails(List<AnalyticsPerPageDetail> perPageDetails) {
		this.perPageDetails = perPageDetails;
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
			

}
