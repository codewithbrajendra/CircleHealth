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
@Table(name = "blog_article")
public class BlogArticle {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_article_id")
	private Long blogArticleId;
	
	@Column(name = "blog_no")
	private Long blogNo;
	
	@Column(name = "topic")
	private String topic;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "content", length=5000)
	private String content;
	
	@Column(name = "article_date")
	private String articledate;
	
	@Column(name = "blog_url")
	private String blogUrl;

	public BlogArticle() {
		super();
	}

	public BlogArticle(Long blogArticleId, Long blogNo, String topic, String title, String content, String articledate,
			String blogUrl) {
		this.blogArticleId = blogArticleId;
		this.blogNo = blogNo;
		this.topic = topic;
		this.title = title;
		this.content = content;
		this.articledate = articledate;
		this.blogUrl = blogUrl;
	}

	public Long getBlogArticleId() {
		return blogArticleId;
	}

	public void setBlogArticleId(Long blogArticleId) {
		this.blogArticleId = blogArticleId;
	}

	public Long getBlogNo() {
		return blogNo;
	}

	public void setBlogNo(Long blogNo) {
		this.blogNo = blogNo;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getArticledate() {
		return articledate;
	}

	public void setArticledate(String articledate) {
		this.articledate = articledate;
	}

	public String getBlogUrl() {
		return blogUrl;
	}

	public void setBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl;
	}
	
}
