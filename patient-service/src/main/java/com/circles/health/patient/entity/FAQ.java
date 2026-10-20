package com.circles.health.patient.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "FAQ")
public class FAQ {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
	private Long faqId;
	
	
	@Column(name = "faq_question", length=1000)
	private String faqQuestion;
	
	@Column(name = "faq_answer", length=5000)
	private String faqAnswer;

	public FAQ() {
		super();
	}

	public FAQ(Long faqId, String faqQuestion, String faqAnswer) {
		this.faqId = faqId;
		this.faqQuestion = faqQuestion;
		this.faqAnswer = faqAnswer;
	}

	public Long getFaqId() {
		return faqId;
	}

	public void setFaqId(Long faqId) {
		this.faqId = faqId;
	}

	public String getFaqQuestion() {
		return faqQuestion;
	}

	public void setFaqQuestion(String faqQuestion) {
		this.faqQuestion = faqQuestion;
	}

	public String getFaqAnswer() {
		return faqAnswer;
	}

	public void setFaqAnswer(String faqAnswer) {
		this.faqAnswer = faqAnswer;
	}
	
}
