package com.circles.health.patient.response;

import java.time.LocalDateTime;

public class ProfileInfo {
	private Integer profileQuestionId;
	private String profileAnswer;
	private LocalDateTime profileFillingdate;
	public Integer getProfileQuestionId() {
		return profileQuestionId;
	}
	public void setProfileQuestionId(Integer profileQuestionId) {
		this.profileQuestionId = profileQuestionId;
	}
	public String getProfileAnswer() {
		return profileAnswer;
	}
	public void setProfileAnswer(String profileAnswer) {
		this.profileAnswer = profileAnswer;
	}
	public LocalDateTime getProfileFillingdate() {
		return profileFillingdate;
	}
	public void setProfileFillingdate(LocalDateTime profileFillingdate) {
		this.profileFillingdate = profileFillingdate;
	}

	
	

}
