package com.circles.health.patient.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.circles.health.patient.entity.HomeContent;
import com.circles.health.patient.entity.VideoContent;
import com.circles.health.patient.entity.VideoPlayData;
import com.circles.health.patient.response.VideoContentResponse;
import com.circles.health.patient.service.PatientContentService;
import com.circles.health.patient.utils.DateUtil;
import com.circles.health.patient.utils.constants.EndPoints;
import com.circles.health.patient.utils.constants.ResponseMessage;
import com.circles.health.patient.utils.enums.ResponseStatus;
import com.circles.health.patient.wrapper.BaseResponse;

@RestController
@RequestMapping(EndPoints.BASE_PATH + "/home")
public class PatientContentController {

	@Autowired
	private PatientContentService patientContentService;
	
	@GetMapping("/homeContent/{userId}")
	public ResponseEntity<BaseResponse> getHomeContent(@PathVariable("userId") Long userId) {
		
		VideoContent videoContent = this.patientContentService.getVideoContent(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
					ResponseMessage.HOME_CONTENT_DATA,DateUtil.getCurrentDateTime(),
				Collections.emptyList(), videoContent));
	}
	
	
	@PostMapping("/setVideoContent")
	public ResponseEntity<BaseResponse>  setVideoContent(@RequestBody  VideoContentResponse videoContentResponse) {
		
		VideoContentResponse savedVideoContentResponse = this.patientContentService.setVideoContentResponse(videoContentResponse.getUserId(), videoContentResponse.getVideoViewed(), 
				videoContentResponse.getVideoDurationInSeccond(), videoContentResponse.getVideoCompleted());
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
					ResponseMessage.VIDEO_CONTENT_DATA,DateUtil.getCurrentDateTime(),
				Collections.emptyList(), savedVideoContentResponse));
	}
	
	@GetMapping("/setVideoPlayData/{userId}")
	public ResponseEntity<BaseResponse> setVideoPlayData(@PathVariable("userId") Long userId) {
		
		VideoPlayData videoPlayData = this.patientContentService.setVideoPlayData(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
					ResponseMessage.VIDEO_PLAY_DATA,DateUtil.getCurrentDateTime(),
				Collections.emptyList(), videoPlayData));
	}
	
}
