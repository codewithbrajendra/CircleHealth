package com.circlesHealth.admin.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PatientWellnessCheckDetails {
	
	private Long wellnessCheckId;
	private Integer totalWellnessScore;
	private Integer phq9Score;
	private Integer gad7Score;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime wellnessCheckDate;
	private Boolean activeDate;
	private List<WellnessCheckDetails> wellnessCheckDetials;
	
	public Long getWellnessCheckId() {
		return wellnessCheckId;
	}
	public void setWellnessCheckId(Long wellnessCheckId) {
		this.wellnessCheckId = wellnessCheckId;
	}
	public Integer getTotalWellnessScore() {
		return totalWellnessScore;
	}
	public void setTotalWellnessScore(Integer totalWellnessScore) {
		this.totalWellnessScore = totalWellnessScore;
	}
	public Integer getPhq9Score() {
		return phq9Score;
	}
	public void setPhq9Score(Integer phq9Score) {
		this.phq9Score = phq9Score;
	}
	public Integer getGad7Score() {
		return gad7Score;
	}
	public void setGad7Score(Integer gad7Score) {
		this.gad7Score = gad7Score;
	}
	public LocalDateTime getWellnessCheckDate() {
		return wellnessCheckDate;
	}
	public void setWellnessCheckDate(LocalDateTime wellnessCheckDate) {
		this.wellnessCheckDate = wellnessCheckDate;
	}
	public List<WellnessCheckDetails> getWellnessCheckDetials() {
		return wellnessCheckDetials;
	}
	public void setWellnessCheckDetials(List<WellnessCheckDetails> wellnessCheckDetials) {
		this.wellnessCheckDetials = wellnessCheckDetials;
	}
	public Boolean getActiveDate() {
		return activeDate;
	}
	public void setActiveDate(Boolean activeDate) {
		this.activeDate = activeDate;
	}
}
