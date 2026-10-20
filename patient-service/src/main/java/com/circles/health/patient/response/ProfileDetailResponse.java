package com.circles.health.patient.response;

import java.util.List;

public class ProfileDetailResponse {

	private Long userId;
	private String profileQuestionNumber;
	private List<PatientProfileDetails> profileDetails;
	
	public ProfileDetailResponse() {
		super();
	}

	public ProfileDetailResponse(Long userId, List<PatientProfileDetails> profileDetails) {
		this.userId = userId;
		this.profileDetails = profileDetails;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<PatientProfileDetails> getProfileDetails() {
		return profileDetails;
	}

	public void setProfileDetails(List<PatientProfileDetails> profileDetails) {
		this.profileDetails = profileDetails;
	}

	public String getProfileQuestionNumber() {
		return profileQuestionNumber;
	}

	public void setProfileQuestionNumber(String profileQuestionNumber) {
		this.profileQuestionNumber = profileQuestionNumber;
	}
	
	
}
