package com.circles.health.patient.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PatientDetailResponseForFile {

	private Long userId;
	private String name;
	private String email;
	private String gender;
	private String ethnicity;
	private String accessCode;
	private String therapistPreference;
	private Long therapistId;
	@JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob;
	private PatientWellnessCheckDetails wellnessCheckDetails;
	private List<PatientProfileDetails> profileDetails;
	
	public PatientDetailResponseForFile() {
		super();
	}
	
	public PatientDetailResponseForFile(Long userId, String name, String email, String gender, String ethnicity,
			String accessCode, String therapistPreference, Long therapistId, LocalDate dob,
			PatientWellnessCheckDetails wellnessCheckDetails, List<PatientProfileDetails> profileDetails) {
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.ethnicity = ethnicity;
		this.accessCode = accessCode;
		this.therapistPreference = therapistPreference;
		this.therapistId = therapistId;
		this.dob = dob;
		this.wellnessCheckDetails = wellnessCheckDetails;
		this.profileDetails = profileDetails;
	}


	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
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

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public PatientWellnessCheckDetails getWellnessCheckDetails() {
		return wellnessCheckDetails;
	}
	public void setWellnessCheckDetails(PatientWellnessCheckDetails wellnessCheckDetails) {
		this.wellnessCheckDetails = wellnessCheckDetails;
	}
	public List<PatientProfileDetails> getProfileDetails() {
		return profileDetails;
	}
	public void setProfileDetails(List<PatientProfileDetails> profileDetails) {
		this.profileDetails = profileDetails;
	}
}
