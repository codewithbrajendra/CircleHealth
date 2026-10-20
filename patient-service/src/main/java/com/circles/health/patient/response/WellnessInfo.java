package com.circles.health.patient.response;

public class WellnessInfo {
	
	private Integer wellnessquestionId;
	private Integer wellnessoptionId;
	private String wellnesscategory;
	private String wellnesstimeInSec;
	private Integer wellnessresponseScore;
	public Integer getWellnessquestionId() {
		return wellnessquestionId;
	}
	public void setWellnessquestionId(Integer wellnessquestionId) {
		this.wellnessquestionId = wellnessquestionId;
	}
	public Integer getWellnessoptionId() {
		return wellnessoptionId;
	}
	public void setWellnessoptionId(Integer wellnessoptionId) {
		this.wellnessoptionId = wellnessoptionId;
	}
	public String getWellnesscategory() {
		return wellnesscategory;
	}
	public void setWellnesscategory(String wellnesscategory) {
		this.wellnesscategory = wellnesscategory;
	}
	public String getWellnesstimeInSec() {
		return wellnesstimeInSec;
	}
	public void setWellnesstimeInSec(String wellnesstimeInSec) {
		this.wellnesstimeInSec = wellnesstimeInSec;
	}
	public Integer getWellnessresponseScore() {
		return wellnessresponseScore;
	}
	public void setWellnessresponseScore(Integer wellnessresponseScore) {
		this.wellnessresponseScore = wellnessresponseScore;
	}
	

}
