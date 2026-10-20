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
@Table(name = "video_play_data")
public class VideoPlayData {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_play_id")
	private Long videoPlayId;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "video_play_time")
	private LocalDateTime videoPlayTime;

	public VideoPlayData() {
		super();
	}

	public VideoPlayData(Long videoPlayId, Long userId, LocalDateTime videoPlayTime) {
		super();
		this.videoPlayId = videoPlayId;
		this.userId = userId;
		this.videoPlayTime = videoPlayTime;
	}

	public Long getVideoPlayId() {
		return videoPlayId;
	}

	public void setVideoPlayId(Long videoPlayId) {
		this.videoPlayId = videoPlayId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDateTime getVideoPlayTime() {
		return videoPlayTime;
	}

	public void setVideoPlayTime(LocalDateTime videoPlayTime) {
		this.videoPlayTime = videoPlayTime;
	}
	
	
	
}
