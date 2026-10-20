package com.circles.health.patient.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="wellness_check_option")

public class WellnessCheckOption implements Serializable {
	
	
	
	private static final long serialVersionUID = 6529864164417433739L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private Long wellnessOptionId;
	private String optionCaption;
	private Integer optionScore;
	private Integer optionSequence;
	private String category;

	public Long getWellnessOptionId() {
		return wellnessOptionId;
	}
	public void setWellnessOptionId(Long wellnessOptionId) {
		this.wellnessOptionId = wellnessOptionId;
	}
	public String getOptionCaption() {
		return optionCaption;
	}
	public void setOptionCaption(String optionCaption) {
		this.optionCaption = optionCaption;
	}
	public Integer getOptionScore() {
		return optionScore;
	}
	public void setOptionScore(Integer optionScore) {
		this.optionScore = optionScore;
	}
	public Integer getOptionSequence() {
		return optionSequence;
	}
	public void setOptionSequence(Integer optionSequence) {
		this.optionSequence = optionSequence;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	

}
