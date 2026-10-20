package com.circles.health.patient.entity;

import java.time.LocalDateTime;

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
@Table(name = "blog_analytics_data")
public class BlogAnalyticsData {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_analytics_data_id")
	private Long blogAnalyticsDataId;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "blog_no")
	private Long blogNo;
	
	@Column(name = "blog_clicking_time")
	private LocalDateTime blogClickingTime;

	public BlogAnalyticsData() {
		super();
	}

	public BlogAnalyticsData(Long blogAnalyticsDataId, Long userId, Long blogNo, LocalDateTime blogClickingTime) {
		this.blogAnalyticsDataId = blogAnalyticsDataId;
		this.userId = userId;
		this.blogNo = blogNo;
		this.blogClickingTime = blogClickingTime;
	}

	public Long getBlogAnalyticsDataId() {
		return blogAnalyticsDataId;
	}

	public void setBlogAnalyticsDataId(Long blogAnalyticsDataId) {
		this.blogAnalyticsDataId = blogAnalyticsDataId;
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

	public LocalDateTime getBlogClickingTime() {
		return blogClickingTime;
	}

	public void setBlogClickingTime(LocalDateTime blogClickingTime) {
		this.blogClickingTime = blogClickingTime;
	}
	
}
