package com.circles.health.patient.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "wellness_check_question")

public class WellnessCheckQuestion implements Serializable {
	
	private static final long serialVersionUID = 1045429340457200560L;
	@Id
	@GeneratedValue
	private Integer wellnessCheckQuestionId;
	private String wellnessCheckQuestion;
	private String wellnessCheckQuestionCategory;
	private Integer wellnessCheckQuestionSequence;
	public Integer getWellnessCheckQuestionId() {
		return wellnessCheckQuestionId;
	}
	public void setWellnessCheckQuestionId(Integer wellnessCheckQuestionId) {
		this.wellnessCheckQuestionId = wellnessCheckQuestionId;
	}
	public String getWellnessCheckQuestion() {
		return wellnessCheckQuestion;
	}
	public void setWellnessCheckQuestion(String wellnessCheckQuestion) {
		this.wellnessCheckQuestion = wellnessCheckQuestion;
	}
	public String getWellnessCheckQuestionCategory() {
		return wellnessCheckQuestionCategory;
	}
	public void setWellnessCheckQuestionCategory(String wellnessCheckQuestionCategory) {
		this.wellnessCheckQuestionCategory = wellnessCheckQuestionCategory;
	}
	public Integer getWellnessCheckQuestionSequence() {
		return wellnessCheckQuestionSequence;
	}
	public void setWellnessCheckQuestionSequence(Integer wellnessCheckQuestionSequence) {
		this.wellnessCheckQuestionSequence = wellnessCheckQuestionSequence;
	}
	 

}
