package com.circles.health.patient.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="profile_question")

public class ProfileQuestion implements Serializable{
	
	
	private static final long serialVersionUID = -5275457068882911200L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long profileId;
	
	@Column(name="profile_question",columnDefinition = "varchar(500)")

	private String profileQuestion;
	
	@Column(name="profileQuestionSequence")
	 private Integer profileQuestionSequence;

	
	public String getProfileQuestion() {
		return profileQuestion;
	}

	public void setProfileQuestion(String profileQuestion) {
		this.profileQuestion = profileQuestion;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}

	
	public Long getProfileId() {
		return profileId;

}

	public Integer getProfileQuestionSequence() {
		return profileQuestionSequence;
	}

	public void setProfileQuestionSequence(Integer profileQuestionSequence) {
		this.profileQuestionSequence = profileQuestionSequence;
	}
}