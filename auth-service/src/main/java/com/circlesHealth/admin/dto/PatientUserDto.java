package com.circlesHealth.admin.dto;

import java.time.LocalDate;


import com.fasterxml.jackson.annotation.JsonFormat;

public class PatientUserDto {

	private Long userId;
	private String accessCode;
	private String email;
	private String firstName;
	private String surName;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dob;
	private String gender;
	private String ethnicity;
	private String therapistPreference;
	private Long therapistId;
	
	public PatientUserDto() {
		super();
	}

	public PatientUserDto(Long userId,String accessCode, String email, String firstName, String surName, LocalDate dob, String gender,
			String ethnicity, String therapistPreference,Long therapistId) {
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

}
