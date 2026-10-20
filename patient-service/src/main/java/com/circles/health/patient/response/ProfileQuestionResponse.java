package com.circles.health.patient.response;


public class ProfileQuestionResponse {

	private Long profileId;
	private String profileQuestion;
	private Integer profileQuestionSequence;
	private Boolean   profileQuestionAttempt;
	private String profileQuestionAnswer;
	public Long getProfileId() {
		return profileId;
	}
	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}
	public String getProfileQuestion() {
		return profileQuestion;
	}
	public void setProfileQuestion(String profileQuestion) {
		this.profileQuestion = profileQuestion;
	}
	
	
	public String getProfileQuestionAnswer() {
		return profileQuestionAnswer;
	}
	public void setProfileQuestionAnswer(String profileQuestionAnswer) {
		this.profileQuestionAnswer = profileQuestionAnswer;
	}
	public Integer getProfileQuestionSequence() {
		return profileQuestionSequence;
	}
	public void setProfileQuestionSequence(Integer profileQuestionSequence) {
		this.profileQuestionSequence = profileQuestionSequence;
	}
	public Boolean getProfileQuestionAttempt() {
		return profileQuestionAttempt;
	}
	public void setProfileQuestionAttempt(Boolean profileQuestionAttempt) {
		this.profileQuestionAttempt = profileQuestionAttempt;
	}



}
