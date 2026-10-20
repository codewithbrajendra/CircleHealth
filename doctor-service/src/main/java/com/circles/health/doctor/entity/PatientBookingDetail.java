package com.circles.health.doctor.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "patient_booking_detail")
public class PatientBookingDetail {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_booking_id")
    private Long patientBookingId;

	private Long userId;
	
	private String email;
	
	private String event;
	
	private String eventUrl;
	
	private String cancelUrl;
	
	private String name;
	
	private String status;
	
	private String uri;
	
	private String cancelReason;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	private LocalDate bookingDate;
	
	private String validateStartTime;
	
	private String bookingStartTime;
	
	private String bookingEndTime;
	
	private Boolean isCalendlyBooking;
	
	private String note;

	public PatientBookingDetail() {
		super();
	}

	public PatientBookingDetail(Long patientBookingId, Long userId, String email, String event, String eventUrl, String cancelUrl, String name,
			String status, String uri, String cancelReason, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDate bookingDate, String validateStartTime,  String bookingStartTime,
			String bookingEndTime, Boolean isCalendlyBooking, String note) {
		this.patientBookingId = patientBookingId;
		this.userId = userId;
		this.email = email;
		this.event = event;
		this.eventUrl = eventUrl;
		this.cancelUrl = cancelUrl;
		this.name = name;
		this.status = status;
		this.uri = uri;
		this.cancelReason = cancelReason;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.bookingDate = bookingDate;
		this.validateStartTime = validateStartTime;
		this.bookingStartTime = bookingStartTime;
		this.bookingEndTime = bookingEndTime;
		this.isCalendlyBooking = isCalendlyBooking;
		this.note = note;
	}

	public Long getPatientBookingId() {
		return patientBookingId;
	}

	public void setPatientBookingId(Long patientBookingId) {
		this.patientBookingId = patientBookingId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventUrl() {
		return eventUrl;
	}

	public void setEventUrl(String eventUrl) {
		this.eventUrl = eventUrl;
	}

	public String getCancelUrl() {
		return cancelUrl;
	}

	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDate getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
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

	public Boolean getIsCalendlyBooking() {
		return isCalendlyBooking;
	}

	public void setIsCalendlyBooking(Boolean isCalendlyBooking) {
		this.isCalendlyBooking = isCalendlyBooking;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}
