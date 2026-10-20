package com.circles.health.patient.response;

public class VideoContentResponse {

	private Long userId;
	private Boolean videoViewed;
	private Long videoDurationInSeccond;
	private Boolean videoCompleted;
	
	public VideoContentResponse() {
		super();
	}

	public VideoContentResponse(Long userId, Boolean videoViewed, Long videoDurationInSeccond, Boolean videoCompleted) {
		this.userId = userId;
		this.videoViewed = videoViewed;
		this.videoDurationInSeccond = videoDurationInSeccond;
		this.videoCompleted = videoCompleted;
	}

	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
