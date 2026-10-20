package com.circles.health.patient.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="wellness_check_answer")

public class WellnessCheckAnswer  implements Serializable {

	private static final long serialVersionUID = 4821692876618879885L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long wellnessAnswerId;
	private Long userId;
	private Integer phq9Score;
	private Integer gad7Score;

	
	@Column(name = "wellness_check_date", nullable = false, updatable = false)
	private LocalDateTime wellnessCheckDate;
	
	@Column(name="wellness_response",columnDefinition = "varchar(500)")
	private String wellnessResponse;
	private Integer responseScore;
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
	
	public String getWellnessResponse() {
		return wellnessResponse;
	}
	public void setWellnessResponse(String wellnessResponse) {
		this.wellnessResponse = wellnessResponse;
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


}
