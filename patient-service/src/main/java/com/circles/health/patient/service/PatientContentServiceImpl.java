package com.circles.health.patient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.circles.health.patient.entity.HomeContent;
import com.circles.health.patient.entity.VideoContent;
import com.circles.health.patient.entity.VideoPlayData;
import com.circles.health.patient.repository.PatientContentRepository;
import com.circles.health.patient.repository.VideoPlayRepository;
import com.circles.health.patient.response.VideoContentResponse;
import com.circles.health.patient.utils.DateUtil;

@Service
@Transactional
public class PatientContentServiceImpl implements PatientContentService {

	@Autowired
	private PatientContentRepository patientContentRepository;
	
	@Autowired
	private VideoPlayRepository videoPlayRepository;
	
	@Override
	public VideoContent getVideoContent(Long userId) {
		
		VideoContent videoContent = this.patientContentRepository.findByUserId(userId);
		
		return videoContent;
	}

	@Override
	public VideoContentResponse setVideoContentResponse(Long userId, Boolean videoViewed, Long videoDurationInSeccond,
			Boolean videoCompleted) {
		VideoContent savedVideoContent = null;
		VideoContent videoContent = this.patientContentRepository.findByUserId(userId);
		VideoContentResponse videoContentResponse = new VideoContentResponse();
		
		videoContent.setVideoCompleted(videoCompleted);
		videoContent.setVideoDurationInSeccond(videoDurationInSeccond);
		videoContent.setVideoViewed(videoViewed);
			
		savedVideoContent = this.patientContentRepository.save(videoContent);
			videoContentResponse.setVideoCompleted(savedVideoContent.getVideoCompleted());
			videoContentResponse.setVideoDurationInSeccond(savedVideoContent.getVideoDurationInSeccond());
			videoContentResponse.setVideoViewed(savedVideoContent.getVideoViewed());
			videoContentResponse.setUserId(savedVideoContent.getUserId());
		
			this.setVideoPlayData(userId);
			
		return videoContentResponse;
	}

	@Override
	public VideoPlayData setVideoPlayData(Long userId) {
		VideoPlayData videoPlayData = new VideoPlayData();
		videoPlayData.setUserId(userId);
		videoPlayData.setVideoPlayTime(DateUtil.getCurrentDateTime());
		return this.videoPlayRepository.save(videoPlayData);
	}

}
