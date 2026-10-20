package com.circles.health.patient.response;

import java.util.List;

public class ProfileQuestionData {
	private Long userId;
	private Long wellnessCheckId;

	private List<ProfileInfo> profileInfo;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getWellnessCheckId() {
		return wellnessCheckId;
	}

	public void setWellnessCheckId(Long wellnessCheckId) {
		this.wellnessCheckId = wellnessCheckId;
	}

	public List<ProfileInfo> getProfileInfo() {
		return profileInfo;
	}

	public void setProfileInfo(List<ProfileInfo> profileInfo) {
		this.profileInfo = profileInfo;
	}

		
	

}
