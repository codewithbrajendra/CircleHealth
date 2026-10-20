package com.circles.health.patient.response;

import java.util.List;

public class WellnessCheckDetailResponse {

	private Long userId;
	private List<PatientWellnessCheckDetails> wellnessCheckDetails;
	
	public WellnessCheckDetailResponse() {
		super();
	}

	public WellnessCheckDetailResponse(Long userId, List<PatientWellnessCheckDetails> wellnessCheckDetails) {
		this.userId = userId;
		this.wellnessCheckDetails = wellnessCheckDetails;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<PatientWellnessCheckDetails> getWellnessCheckDetails() {
		return wellnessCheckDetails;
	}

	public void setWellnessCheckDetails(List<PatientWellnessCheckDetails> wellnessCheckDetails) {
		this.wellnessCheckDetails = wellnessCheckDetails;
	}
	
}
