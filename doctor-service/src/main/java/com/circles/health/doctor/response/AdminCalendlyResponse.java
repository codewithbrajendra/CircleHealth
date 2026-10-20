package com.circles.health.doctor.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AdminCalendlyResponse {

	private Boolean isCalendlyBooking;
	
	private LocalDate lastCalendlyBookingDate;
	
    private String bookingStartTime;
	
	private String bookingEndTime;
	
	private String validateStartTime;
	
	private String calendlyBookingTherapistName;
	
	private String calendlyBookingNote;
	
	private String cancelUrl;
	
	private Boolean isBookingCancel;
	
	private String cancelBookingReason;

	private String status;
	
	public AdminCalendlyResponse() {
		super();
	}

	public AdminCalendlyResponse(Boolean isCalendlyBooking, LocalDate lastCalendlyBookingDate, String validateStartTime,
			String bookingStartTime, String bookingEndTime, String calendlyBookingTherapistName,
			String calendlyBookingNote, String cancelUrl, Boolean isBookingCancel, String cancelBookingReason, String status) {
		this.isCalendlyBooking = isCalendlyBooking;
		this.lastCalendlyBookingDate = lastCalendlyBookingDate;
		this.validateStartTime  = validateStartTime;
		this.bookingStartTime = bookingStartTime;
		this.bookingEndTime = bookingEndTime;
		this.calendlyBookingTherapistName = calendlyBookingTherapistName;
		this.calendlyBookingNote = calendlyBookingNote;
		this.cancelUrl = cancelUrl;
		this.isBookingCancel = isBookingCancel;
		this.cancelBookingReason = cancelBookingReason;
		this.status = status;
	}

	public Boolean getIsCalendlyBooking() {
		return isCalendlyBooking;
	}

	public void setIsCalendlyBooking(Boolean isCalendlyBooking) {
		this.isCalendlyBooking = isCalendlyBooking;
	}

	public LocalDate getLastCalendlyBookingDate() {
		return lastCalendlyBookingDate;
	}

	public void setLastCalendlyBookingDate(LocalDate lastCalendlyBookingDate) {
		this.lastCalendlyBookingDate = lastCalendlyBookingDate;
	}

	public String getValidateStartTime() {
		return validateStartTime;
	}

	public void setValidateStartTime(String validateStartTime) {
		this.validateStartTime = validateStartTime;
	}

	public String getBookingStartTime() {
		return bookingStartTime;
	}

	public void setBookingStartTime(String bookingStartTime) {
		this.bookingStartTime = bookingStartTime;
	}

	public String getBookingEndTime() {
		return bookingEndTime;
	}

	public void setBookingEndTime(String bookingEndTime) {
		this.bookingEndTime = bookingEndTime;
	}

	public String getCalendlyBookingTherapistName() {
		return calendlyBookingTherapistName;
	}

	public void setCalendlyBookingTherapistName(String calendlyBookingTherapistName) {
		this.calendlyBookingTherapistName = calendlyBookingTherapistName;
	}

	public String getCalendlyBookingNote() {
		return calendlyBookingNote;
	}

	public void setCalendlyBookingNote(String calendlyBookingNote) {
		this.calendlyBookingNote = calendlyBookingNote;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public Boolean getIsBookingCancel() {
		return isBookingCancel;
	}

	public void setIsBookingCancel(Boolean isBookingCancel) {
		this.isBookingCancel = isBookingCancel;
	}

	public String getCancelBookingReason() {
		return cancelBookingReason;
	}

	public void setCancelBookingReason(String cancelBookingReason) {
		this.cancelBookingReason = cancelBookingReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
