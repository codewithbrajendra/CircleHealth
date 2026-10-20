package com.circlesHealth.admin.response;

import java.time.LocalDate;

public class CreateUserResponse {

	private Long userId;
	private String firstName;
	private String surName;
	private String accessCode;
	private LocalDate dob;
	private String gender;
	private String email;
	private String ethnicity;
	private String therapistPreference;
	
	public CreateUserResponse() {
		super();
	}

	public CreateUserResponse(Long userId, String firstName, String surName, String accessCode, LocalDate dob, String gender,
			String email, String ethnicity, String therapistPreference) {
		this.userId = userId;
		this.firstName = firstName;
		this.surName = surName;
		this.accessCode = accessCode;
		this.dob = dob;
		this.gender = gender;
		this.email = email;
		this.ethnicity = ethnicity;
		this.therapistPreference = therapistPreference;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
}
