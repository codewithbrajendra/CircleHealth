package com.circlesHealth.admin.response;

public class WellnessCheckDetails {
	private Integer questionId;
	private String question;
	private Integer questionSequence;
	private Long optionId;
	private String option;
	private Integer optionSequence;
	private Integer score;
	 private String  category;
	
	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public Integer getQuestionSequence() {
		return questionSequence;
	}
	public void setQuestionSequence(Integer questionSequence) {
		this.questionSequence = questionSequence;
	}
	public Long getOptionId() {
		return optionId;
	}
	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public Integer getOptionSequence() {
		return optionSequence;
	}
	public void setOptionSequence(Integer optionSequence) {
		this.optionSequence = optionSequence;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
}
