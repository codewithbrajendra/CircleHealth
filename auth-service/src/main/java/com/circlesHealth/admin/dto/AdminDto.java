package com.circlesHealth.admin.dto;

import java.time.LocalDate;

public class AdminDto {
	     private long userId;
	     private String firstName;
	    private String lastName;
	    private String userType;
	    private String createdBy;
	    private String email;
	    private LocalDate dateOfBirth;
	    private String ethnicity;
	    private String gender;
	    private String accessCode;

		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
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
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
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
		public long getUserId() {
			return userId;
		}
		public void setUserId(long userId) {
			this.userId = userId;
		}
		public LocalDate getDateOfBirth() {
			return dateOfBirth;
		}
		public void setDateOfBirth(LocalDate dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
		}
		
	

}
