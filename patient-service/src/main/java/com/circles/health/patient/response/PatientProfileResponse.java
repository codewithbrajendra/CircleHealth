package com.circles.health.patient.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientProfileResponse {

	private Long userId;
	private String accessCode;
	private String email;
	private String firstName;
	private String surName;
	private LocalDate dob;
	private String gender;
	private String ethnicity;
	private String therapistPreference;
	private Long therapistId;
	private Boolean wellnessCheck;
	private LocalDateTime lastWellnessCheckDate;
	private Boolean isCalendlyBooking;
	private String validateStartTime;
	private String lastCalendlyBookingDate;
	private String calendlyBookingTimeStart;
	private String calendlyBookingTimeEnd;
	private String CalendlyBookingTherapistName;
	private String calendlyBookingNote;
	private Boolean isPatientProfileCompleted;
	private String status;
	
	public PatientProfileResponse() {
		super();
	}

	public PatientProfileResponse(Long userId, String accessCode, String email, String firstName, String surName,
			LocalDate dob, String gender, String ethnicity, String therapistPreference, Long therapistId,
			Boolean wellnessCheck, LocalDateTime lastWellnessCheckDate, Boolean isCalendlyBooking,String validateStartTime,
			String lastCalendlyBookingDate, String calendlyBookingTimeStart, String calendlyBookingTimeEnd,
			String calendlyBookingTherapistName,String calendlyBookingNote, Boolean isPatientProfileCompleted, String status) {
		this.userId = userId;
		this.accessCode = accessCode;
		this.email = email;
		this.firstName = firstName;
		this.surName = surName;
		this.dob = dob;
		this.gender = gender;
		this.ethnicity = ethnicity;
		this.therapistPreference = therapistPreference;
		this.therapistId = therapistId;
		this.wellnessCheck = wellnessCheck;
		this.lastWellnessCheckDate = lastWellnessCheckDate;
		this.isCalendlyBooking = isCalendlyBooking;
		this.validateStartTime = validateStartTime;
		this.lastCalendlyBookingDate = lastCalendlyBookingDate;
		this.calendlyBookingTimeStart = calendlyBookingTimeStart;
		this.calendlyBookingTimeEnd = calendlyBookingTimeEnd;
		this.CalendlyBookingTherapistName = calendlyBookingTherapistName;
		this.calendlyBookingNote = calendlyBookingNote;
		this.isPatientProfileCompleted = isPatientProfileCompleted;
		this.status = status;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
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

	public String getTherapistPreference() {
		return therapistPreference;
	}

	public void setTherapistPreference(String therapistPreference) {
		this.therapistPreference = therapistPreference;
	}

	public Long getTherapistId() {
		return therapistId;
	}

	public void setTherapistId(Long therapistId) {
		this.therapistId = therapistId;
	}

	public Boolean getWellnessCheck() {
		return wellnessCheck;
	}

	public void setWellnessCheck(Boolean wellnessCheck) {
		this.wellnessCheck = wellnessCheck;
	}

	public LocalDateTime getLastWellnessCheckDate() {
		return lastWellnessCheckDate;
	}

	public void setLastWellnessCheckDate(LocalDateTime lastWellnessCheckDate) {
		this.lastWellnessCheckDate = lastWellnessCheckDate;
	}

	public Boolean getIsCalendlyBooking() {
		return isCalendlyBooking;
	}

	public void setIsCalendlyBooking(Boolean isCalendlyBooking) {
		this.isCalendlyBooking = isCalendlyBooking;
	}

	public String getValidateStartTime() {
		return validateStartTime;
	}

	public void setValidateStartTime(String validateStartTime) {
		this.validateStartTime = validateStartTime;
	}

	public String getLastCalendlyBookingDate() {
		return lastCalendlyBookingDate;
	}

	public void setLastCalendlyBookingDate(String lastCalendlyBookingDate) {
		this.lastCalendlyBookingDate = lastCalendlyBookingDate;
	}

	public String getCalendlyBookingTimeStart() {
		return calendlyBookingTimeStart;
	}

	public void setCalendlyBookingTimeStart(String calendlyBookingTimeStart) {
		this.calendlyBookingTimeStart = calendlyBookingTimeStart;
	}

	public String getCalendlyBookingTimeEnd() {
		return calendlyBookingTimeEnd;
	}

	public void setCalendlyBookingTimeEnd(String calendlyBookingTimeEnd) {
		this.calendlyBookingTimeEnd = calendlyBookingTimeEnd;
	}

	public String getCalendlyBookingTherapistName() {
		return CalendlyBookingTherapistName;
	}

	public void setCalendlyBookingTherapistName(String calendlyBookingTherapistName) {
		CalendlyBookingTherapistName = calendlyBookingTherapistName;
	}

	public String getCalendlyBookingNote() {
		return calendlyBookingNote;
	}

	public void setCalendlyBookingNote(String calendlyBookingNote) {
		this.calendlyBookingNote = calendlyBookingNote;
	}

	public Boolean getIsPatientProfileCompleted() {
		return isPatientProfileCompleted;
	}

	public void setIsPatientProfileCompleted(Boolean isPatientProfileCompleted) {
		this.isPatientProfileCompleted = isPatientProfileCompleted;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
