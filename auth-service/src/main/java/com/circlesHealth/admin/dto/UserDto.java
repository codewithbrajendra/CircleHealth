package com.circlesHealth.admin.dto;

import com.circlesHealth.admin.entity.UserRole;
import com.circlesHealth.admin.entity.UserSession;
import com.fasterxml.jackson.annotation.JsonIgnore;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class UserDto {

    private Long userId;
    private String email;
    private String userUniqueId;
    private String userType;
    private String createdBy;
    private LocalDate dob;
    private String ethnicity;
    private String gender;
    private LocalDateTime createdDate;
    private Boolean active = true;
    private Boolean isEmailVerified = true;
    private LocalDateTime modifiedDate;
    private String modifiedBy;
    private Set<UserRole> userRoles;
    private Set<UserSession> userSessions;
	
    public UserDto() {
		super();
	}

	public UserDto(Long userId, String email, String userUniqueId, String userType, String createdBy,LocalDate dob,String ethnicity,String gender,
			LocalDateTime createdDate, Boolean active, Boolean isEmailVerified, LocalDateTime modifiedDate,
			String modifiedBy, Set<UserRole> userRoles, Set<UserSession> userSessions) {
		super();
		this.userId = userId;
		this.email = email;
		this.userUniqueId = userUniqueId;
		this.userType = userType;
		this.createdBy = createdBy;
		this.dob = dob;
		this.ethnicity = ethnicity;
		this.gender = gender;
		this.createdDate = createdDate;
		this.active = active;
		this.isEmailVerified = isEmailVerified;
		this.modifiedDate = modifiedDate;
		this.modifiedBy = modifiedBy;
		this.userRoles = userRoles;
		this.userSessions = userSessions;
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

	public String getUserUniqueId() {
		return userUniqueId;
	}

	public void setUserUniqueId(String userUniqueId) {
		this.userUniqueId = userUniqueId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(Boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public Set<UserSession> getUserSessions() {
		return userSessions;
	}

	public void setUserSessions(Set<UserSession> userSessions) {
		this.userSessions = userSessions;
	}
    
}