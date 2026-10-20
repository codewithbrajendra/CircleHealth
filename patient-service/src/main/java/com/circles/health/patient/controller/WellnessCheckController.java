package com.circles.health.patient.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.circles.health.patient.entity.ProfileAnswer;
import com.circles.health.patient.entity.User;
import com.circles.health.patient.entity.WellnessCheckAnswer;
import com.circles.health.patient.response.ProfileQuestionData;
import com.circles.health.patient.response.ProfileQuestionResponse;
import com.circles.health.patient.response.SaveWellnessCheckData;
import com.circles.health.patient.response.WellnessCheck;
import com.circles.health.patient.response.WellnessQuestion;
import com.circles.health.patient.service.ProfileQuestionService;
import com.circles.health.patient.service.UserService;
import com.circles.health.patient.service.WellnessCheckService;
import com.circles.health.patient.utils.DateUtil;
import com.circles.health.patient.utils.constants.EndPoints;
import com.circles.health.patient.utils.constants.ResponseMessage;
import com.circles.health.patient.utils.enums.ResponseStatus;
import com.circles.health.patient.wrapper.BaseResponse;

@RestController
@RequestMapping(EndPoints.BASE_PATH)
public class WellnessCheckController {
	
	@Autowired
	private WellnessCheckService wellnessCheckService;
	
	@Autowired 
	private ProfileQuestionService profileQuestionService;
	
    @Autowired
	private UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(WellnessCheckController.class);

	@GetMapping("/wellnesscheck")
	public ResponseEntity<BaseResponse> getWellnessCheck() {

		List<WellnessCheck> wellnesscheck = null;
		try {

			wellnesscheck = this.wellnessCheckService.getWellnessCheck();

		} catch (Exception exception) {
			LOGGER.error(ResponseMessage.DATA_NOT_FOUND + exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.DATA_NOT_FOUND + exception.getMessage(), DateUtil.getCurrentDateTime(),
							Collections.emptyList(), null));
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(), "WellnessQuestion",
						DateUtil.getCurrentDateTime(), Collections.emptyList(), wellnesscheck));

	}
	
	
	@PostMapping("/savewellnesscheck")
	public ResponseEntity<BaseResponse> saveWellnessCheck(@RequestBody WellnessQuestion wellnessQuestion) {
		LOGGER.info("wellnessQuestion for user==  "+wellnessQuestion.getUserId());
		WellnessCheckAnswer wellnessCheckAnswer = null;
		try {
		
			wellnessCheckAnswer = this.wellnessCheckService.saveWellnessCheck(wellnessQuestion);
			if(wellnessCheckAnswer != null) {
				LOGGER.info("Inside wellnessCheckAnswer not null condition ::: "+wellnessCheckAnswer);
				User userTobeUpdated = this.userService.getUserById(wellnessCheckAnswer.getUserId());
				userTobeUpdated.setWellnessCheck("Yes");
				User updatedUser = this.userService.updateUser(userTobeUpdated);
				if(updatedUser != null) {
				LOGGER.info("User wellness check updated successfully.");
				}
			}
		}catch (Exception exception) {
			LOGGER.error(ResponseMessage.WELLNESSCHECK_SAVE_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.WELLNESSCHECK_SAVE_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
					ResponseMessage.WELLNESSCHECK_CREATED_SUCCESS,DateUtil.getCurrentDateTime(),
				Collections.emptyList(), wellnessCheckAnswer));
	}
	
	@PostMapping("/savewellnesscheck1")
	public ResponseEntity<BaseResponse> saveWellnessCheck1(@RequestBody WellnessQuestion wellnessQuestion) {
		LOGGER.info("wellnessQuestion for user==  "+wellnessQuestion.getUserId());
		SaveWellnessCheckData saveWellnessCheckData = null;
		try {
		
			saveWellnessCheckData = this.wellnessCheckService.saveWellnessCheck1(wellnessQuestion);
			if(saveWellnessCheckData != null) {
				LOGGER.info("Inside wellnessCheckAnswer not null condition ::: "+saveWellnessCheckData);
				User userTobeUpdated = this.userService.getUserById(saveWellnessCheckData.getUserId());
				userTobeUpdated.setWellnessCheck("Yes");
				User updatedUser = this.userService.updateUser(userTobeUpdated);
				if(updatedUser != null) {
				LOGGER.info("User wellness check updated successfully.");
				}
			}
		}catch (Exception exception) {
			LOGGER.error(ResponseMessage.WELLNESSCHECK_SAVE_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.WELLNESSCHECK_SAVE_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
					ResponseMessage.WELLNESSCHECK_CREATED_SUCCESS,DateUtil.getCurrentDateTime(),
				Collections.emptyList(), saveWellnessCheckData));
	}
		
	@GetMapping("/profilequestion")
	public ResponseEntity<BaseResponse> getProfileQuestion() {

		List<ProfileQuestionResponse> profileQuestion = null;
		try {

			profileQuestion = this.profileQuestionService.getProfileQuestion();

		} catch (Exception exception) {
			LOGGER.error(ResponseMessage.DATA_NOT_FOUND + exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseStatus.FAILURE.value(),
							ResponseMessage.DATA_NOT_FOUND + exception.getMessage(), DateUtil.getCurrentDateTime(),
							Collections.emptyList(), null));
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new BaseResponse(HttpStatus.OK.value(), ResponseStatus.SUCCESS.value(), "ProfileQuestion",
						DateUtil.getCurrentDateTime(), Collections.emptyList(), profileQuestion));

	}

	@PostMapping("/saveprofilequestion")
	public ResponseEntity<BaseResponse> saveProfileQuestion(@RequestBody ProfileQuestionData profileData) {
		LOGGER.info("profileAnswer for user==  "+profileData.getUserId());
		try {
		
			profileData = this.profileQuestionService.saveProfileQuestion(profileData);
		}catch (Exception exception) {
			LOGGER.error(ResponseMessage.PROFILE_SAVE_ERROR+exception.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResponseStatus.FAILURE.value(),
						ResponseMessage.PROFILE_SAVE_ERROR+exception.getMessage(),DateUtil.getCurrentDateTime(),
					Collections.emptyList(), null));
		}
		return ResponseEntity.status(HttpStatus.OK).body(
				new BaseResponse(HttpStatus.OK.value(),ResponseStatus.SUCCESS.value(),
					ResponseMessage.PROFILE_CREATED_SUCCESS,DateUtil.getCurrentDateTime(),
				Collections.emptyList(), profileData));
	}
	

}
