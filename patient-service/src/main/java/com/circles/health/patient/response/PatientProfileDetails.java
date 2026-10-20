package com.circles.health.patient.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PatientProfileDetails {
	private Long profileId;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime profileFillingDate;
	private Integer questionId;
	private Integer questionSequence;
	private String Question;
	private String response;

	
	public Long getProfileId() {
		return profileId;
	}
	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}
	public LocalDateTime getProfileFillingDate() {
		return profileFillingDate;
	}
	public void setProfileFillingDate(LocalDateTime profileFillingDate) {
		this.profileFillingDate = profileFillingDate;
	}
	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	public Integer getQuestionSequence() {
		return questionSequence;
	}
	public void setQuestionSequence(Integer questionSequence) {
		this.questionSequence = questionSequence;
	}
	public String getQuestion() {
		return Question;
	}
	public void setQuestion(String question) {
		Question = question;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	
}
