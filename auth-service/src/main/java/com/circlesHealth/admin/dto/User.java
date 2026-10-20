package com.circlesHealth.admin.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;


public class User implements Serializable {

    private static final long serialVersionUID = -715163039926305286L;

	private Long userId;

    private String userUniqueId;

    private String accessCode;

    private String email;

    private String firstName;

    private String surName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob;

    private String gender;

    private String userType;

    private String ethnicity;

    private String therapistPreference;

    private Boolean isEmailVerified;

    private Boolean isActive;

    private String createdBy;

    private LocalDateTime createdDate;

    private String modifiedBy;

    private LocalDateTime modifiedDate;

	public User() {
		super();
	}

	public User(Long userId, String userUniqueId, String accessCode, String email, String firstName, String surName,
			LocalDate dob, String gender, String userType, String ethnicity, String therapistPreference,
			Boolean isEmailVerified, Boolean isActive, String createdBy, LocalDateTime createdDate, String modifiedBy,
			LocalDateTime modifiedDate) {
		this.userId = userId;
		this.userUniqueId = userUniqueId;
		this.accessCode = accessCode;
		this.email = email;
		this.firstName = firstName;
		this.surName = surName;
		this.dob = dob;
		this.gender = gender;
		this.userType = userType;
		this.ethnicity = ethnicity;
		this.therapistPreference = therapistPreference;
		this.isEmailVerified = isEmailVerified;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserUniqueId() {
		return userUniqueId;
	}

	public void setUserUniqueId(String userUniqueId) {
		this.userUniqueId = userUniqueId;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
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

	public Boolean getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(Boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
