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
@Table(name = "home_content")
public class HomeContent {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "content_id")
    private Long contentId;
	
	@Column(name = "video_link1")
	private String videoLink1;
	
	@Column(name = "video_link2")
	private String videoLink2;
	
	@Column(name = "heading1")
	private String heading1;
	
	@Column(name = "heading2")
	private String heading2;
	
	@Column(name = "content1")
	private String content1;
	
	@Column(name = "content2")
	private String content2;
	
	@Column(name = "image1")
	private String image1;
	
	@Column(name = "image2")
	private String image2;
	
	@Column(name = "video_viewed")
	private Boolean videoViewed;
	
	@Column(name = "video_duration_in_seccond")
	private Long videoDurationInSeccond;
	
	@Column(name = "video_completed")
	private Boolean videoCompleted;

	public HomeContent() {
		super();
	}

	public HomeContent(Long contentId, String videoLink1, String videoLink2, String heading1, String heading2,
			String content1, String content2, String image1, String image2, Boolean videoViewed,
			Long videoDurationInSeccond, Boolean videoCompleted) {
		super();
		this.contentId = contentId;
		this.videoLink1 = videoLink1;
		this.videoLink2 = videoLink2;
		this.heading1 = heading1;
		this.heading2 = heading2;
		this.content1 = content1;
		this.content2 = content2;
		this.image1 = image1;
		this.image2 = image2;
		this.videoViewed = videoViewed;
		this.videoDurationInSeccond = videoDurationInSeccond;
		this.videoCompleted = videoCompleted;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public String getVideoLink1() {
		return videoLink1;
	}

	public void setVideoLink1(String videoLink1) {
		this.videoLink1 = videoLink1;
	}

	public String getVideoLink2() {
		return videoLink2;
	}

	public void setVideoLink2(String videoLink2) {
		this.videoLink2 = videoLink2;
	}

	public String getHeading1() {
		return heading1;
	}

	public void setHeading1(String heading1) {
		this.heading1 = heading1;
	}

	public String getHeading2() {
		return heading2;
	}

	public void setHeading2(String heading2) {
		this.heading2 = heading2;
	}

	public String getContent1() {
		return content1;
	}

	public void setContent1(String content1) {
		this.content1 = content1;
	}

	public String getContent2() {
		return content2;
	}

	public void setContent2(String content2) {
		this.content2 = content2;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public Boolean getVideoViewed() {
		return videoViewed;
	}

	public void setVideoViewed(Boolean videoViewed) {
		this.videoViewed = videoViewed;
	}

	public Long getVideoDurationInSeccond() {
		return videoDurationInSeccond;
	}

	public void setVideoDurationInSeccond(Long videoDurationInSeccond) {
		this.videoDurationInSeccond = videoDurationInSeccond;
	}

	public Boolean getVideoCompleted() {
		return videoCompleted;
	}

	public void setVideoCompleted(Boolean videoCompleted) {
		this.videoCompleted = videoCompleted;
	}
	
}
