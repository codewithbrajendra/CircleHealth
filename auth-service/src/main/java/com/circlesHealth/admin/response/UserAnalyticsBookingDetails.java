package com.circlesHealth.admin.response;

import java.time.LocalDate;

public class UserAnalyticsBookingDetails {
	
	 private Long userId;
     private String bookingTimeDuration;
     private String bookingStatus;
     private LocalDate bookingDate;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getBookingTimeDuration() {
		return bookingTimeDuration;
	}
	public void setBookingTimeDuration(String bookingTimeDuration) {
		this.bookingTimeDuration = bookingTimeDuration;
	}
	public String getBookingStatus() {
		return bookingStatus;
	}
	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}
	public LocalDate getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}
    
}
