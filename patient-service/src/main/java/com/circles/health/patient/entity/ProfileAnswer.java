package com.circles.health.patient.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="profile_question_answer")

public class ProfileAnswer implements Serializable {
	private static final long serialVersionUID = 7418092691793248720L;

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long profileAnswerId;
		private Integer profileQuestionId;
		
		@Column(name="profile_answer",columnDefinition = "varchar(500)")
		private String profileAnswer;
		private Long userId;
       private Long wellnessCheckId;
		

		@Column(name = "profile_filling_date", nullable = false, updatable = false)
		private LocalDateTime profileFillingDate;
		
		
		public Long getProfileAnswerId() {
			return profileAnswerId;
		}

		public void setProfileAnswerId(Long profileAnswerId) {
			this.profileAnswerId = profileAnswerId;
		}

		public Integer getProfileQuestionId() {
			return profileQuestionId;
		}

		public void setProfileQuestionId(Integer profileQuestionId) {
			this.profileQuestionId = profileQuestionId;
		}

		public String getProfileAnswer() {
			return profileAnswer;
		}

		public void setProfileAnswer(String profileAnswer) {
			this.profileAnswer = profileAnswer;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public LocalDateTime getProfileFillingDate() {
			return profileFillingDate;
		}

		public void setProfileFillingDate(LocalDateTime profileFillingDate) {
			this.profileFillingDate = profileFillingDate;
		}

		public Long getWellnessCheckId() {
			return wellnessCheckId;
		}

		public void setWellnessCheckId(Long wellnessCheckId) {
			this.wellnessCheckId = wellnessCheckId;
		}

		

}
