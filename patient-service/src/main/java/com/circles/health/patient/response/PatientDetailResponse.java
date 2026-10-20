package com.circles.health.patient.response;

import java.util.List;

public class PatientDetailResponse {
	
	private Long userId;
	private PatientWellnessCheckDetails wellnessCheckDetails;
	private List<PatientProfileDetails> profileDetails;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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