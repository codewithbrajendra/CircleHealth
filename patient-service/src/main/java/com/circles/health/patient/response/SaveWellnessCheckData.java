package com.circles.health.patient.response;

import java.time.LocalDateTime;
import java.util.List;

public class SaveWellnessCheckData {

	private Long wellnessAnswerId;
	private Long userId;
	private Integer phq9Score;
	private Integer gad7Score;
	private Integer responseScore;
	private LocalDateTime wellnessCheckDate;
	private List<WellnessCheckDetails> wellnessCheckDetails;
	
	public SaveWellnessCheckData() {
		super();
	}

	public SaveWellnessCheckData(Long wellnessAnswerId, Long userId, Integer phq9Score, Integer gad7Score,Integer responseScore,LocalDateTime wellnessCheckDate,
			List<WellnessCheckDetails> wellnessCheckDetails) {
		this.wellnessAnswerId = wellnessAnswerId;
		this.userId = userId;
		this.phq9Score = phq9Score;
		this.gad7Score = gad7Score;
		this.responseScore = responseScore;
		this.wellnessCheckDate = wellnessCheckDate;
		this.wellnessCheckDetails = wellnessCheckDetails;
	}

	public Long getWellnessAnswerId() {
		return wellnessAnswerId;
	}

	public void setWellnessAnswerId(Long wellnessAnswerId) {
		this.wellnessAnswerId = wellnessAnswerId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Integer getResponseScore() {
		return responseScore;
	}

	public void setResponseScore(Integer responseScore) {
		this.responseScore = responseScore;
	}

	public LocalDateTime getWellnessCheckDate() {
		return wellnessCheckDate;
	}

	public void setWellnessCheckDate(LocalDateTime wellnessCheckDate) {
		this.wellnessCheckDate = wellnessCheckDate;
	}

	public List<WellnessCheckDetails> getWellnessCheckDetails() {
		return wellnessCheckDetails;
	}

	public void setWellnessCheckDetails(List<WellnessCheckDetails> wellnessCheckDetails) {
		this.wellnessCheckDetails = wellnessCheckDetails;
	}
	
}
