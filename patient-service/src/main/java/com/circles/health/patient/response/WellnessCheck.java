package com.circles.health.patient.response;

import java.util.List;

import com.circles.health.patient.entity.WellnessCheckOption;

public class WellnessCheck {
	
	
	private Integer wellnessQuestionId;
	private String wellnessQuestion;
	private String wellnessQuestionCategory;
	private Integer wellnessQuestionSequence;
	private Boolean   wellnessQuestionAttempt;
	private String  takentime;
	
    private List<WellnessCheckOption> wellnessOption;

	public Integer getWellnessQuestionId() {
		return wellnessQuestionId;
	}

	public void setWellnessQuestionId(Integer wellnessQuestionId) {
		this.wellnessQuestionId = wellnessQuestionId;
	}

	public String getWellnessQuestion() {
		return wellnessQuestion;
	}

	public void setWellnessQuestion(String wellnessQuestion) {
		this.wellnessQuestion = wellnessQuestion;
	}

	public String getWellnessQuestionCategory() {
		return wellnessQuestionCategory;
	}

	public void setWellnessQuestionCategory(String wellnessQuestionCategory) {
		this.wellnessQuestionCategory = wellnessQuestionCategory;
	}

	public Integer getWellnessQuestionSequence() {
		return wellnessQuestionSequence;
	}

	public void setWellnessQuestionSequence(Integer wellnessQuestionSequence) {
		this.wellnessQuestionSequence = wellnessQuestionSequence;
	}

	public Boolean getWellnessQuestionAttempt() {
		return wellnessQuestionAttempt;
	}

	public void setWellnessQuestionAttempt(Boolean wellnessQuestionAttempt) {
		this.wellnessQuestionAttempt = wellnessQuestionAttempt;
	}

	

	public String getTakentime() {
		return takentime;
	}

	public void setTakentime(String takentime) {
		this.takentime = takentime;
	}

	public List<WellnessCheckOption> getWellnessOption() {
		return wellnessOption;
	}

	public void setWellnessOption(List<WellnessCheckOption> wellnessOption) {
		this.wellnessOption = wellnessOption;
	}
	

}
