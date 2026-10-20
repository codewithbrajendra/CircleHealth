package com.circles.health.patient.service;

import com.circles.health.patient.entity.HomeContent;
import com.circles.health.patient.entity.VideoContent;
import com.circles.health.patient.entity.VideoPlayData;
import com.circles.health.patient.response.VideoContentResponse;

public interface PatientContentService {

	public VideoContent getVideoContent(Long userId);
	
	public VideoContentResponse setVideoContentResponse(Long userId, Boolean videoViewed, Long videoDurationInSeccond, Boolean videoCompleted);

	public VideoPlayData setVideoPlayData(Long userId);
}
