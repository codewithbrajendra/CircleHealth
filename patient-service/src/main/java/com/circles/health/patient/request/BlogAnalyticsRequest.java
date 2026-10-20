package com.circles.health.patient.request;

public class BlogAnalyticsRequest {

	private Long userId;
	private Long blogNo;
	
	public BlogAnalyticsRequest() {
		super();
	}
	
	public BlogAnalyticsRequest(Long userId, Long blogNo) {
		this.userId = userId;
		this.blogNo = blogNo;
	}

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getBlogNo() {
		return blogNo;
	}
	public void setBlogNo(Long blogNo) {
		this.blogNo = blogNo;
	}
	
	
}
