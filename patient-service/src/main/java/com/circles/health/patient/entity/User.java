package com.circles.health.patient.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = -715163039926305286L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_unique_id", nullable = false, unique = true)
    private String userUniqueId;

    @Column(name = "access_code", nullable = false, unique = true)
    private String accessCode;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "sur_name", nullable = false)
    private String surName;

    @Column(name = "dob", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "ethnicity", nullable = false)
    private String ethnicity;

    @Column(name = "therapist_preference", nullable = false)
    private String therapistPreference;

    @Column(name = "therapist_id", nullable = false)
    private Long therapistId;
    
    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "wellness_check")
    private String wellnessCheck;
    
    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

	public User() {
		super();
	}

	public User(Long userId, String userUniqueId, String accessCode, String email, String firstName, String surName,
			LocalDate dob, String gender, String userType, String ethnicity, String therapistPreference,Long therapistId,
			Boolean isEmailVerified, Boolean isActive, String wellnessCheck, String createdBy, LocalDateTime createdDate, String modifiedBy,
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
		this.therapistId = therapistId;
		this.isEmailVerified = isEmailVerified;
		this.isActive = isActive;
		this.wellnessCheck = wellnessCheck;
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

	public Long getTherapistId() {
		return therapistId;
	}

	public void setTherapistId(Long therapistId) {
		this.therapistId = therapistId;
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

	public String getWellnessCheck() {
		return wellnessCheck;
	}

	public void setWellnessCheck(String wellnessCheck) {
		this.wellnessCheck = wellnessCheck;
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
