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
@Table(name = "video_content")
public class VideoContent {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "content_id")
    private Long videoContentId;
	
	@Column(name = "user_id")
    private Long userId;
	
	@Column(name = "video_link1")
	private String videoLink1;
	
	@Column(name = "video_link2")
	private String videoLink2;
	
	@Column(name = "video_viewed")
	private Boolean videoViewed;
	
	@Column(name = "video_duration_in_seccond")
	private Long videoDurationInSeccond;
	
	@Column(name = "video_completed")
	private Boolean videoCompleted;

	public VideoContent() {
		super();
	}

	public VideoContent(Long videoContentId, Long userId, String videoLink1, String videoLink2, Boolean videoViewed,
			Long videoDurationInSeccond, Boolean videoCompleted) {
		this.videoContentId = videoContentId;
		this.userId = userId;
		this.videoLink1 = videoLink1;
		this.videoLink2 = videoLink2;
		this.videoViewed = videoViewed;
		this.videoDurationInSeccond = videoDurationInSeccond;
		this.videoCompleted = videoCompleted;
	}

	public Long getVideoContentId() {
		return videoContentId;
	}

	public void setVideoContentId(Long videoContentId) {
		this.videoContentId = videoContentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
