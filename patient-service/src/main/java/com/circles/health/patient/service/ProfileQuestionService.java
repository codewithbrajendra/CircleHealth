package com.circles.health.patient.service;

import java.util.List;

import com.circles.health.patient.entity.ProfileAnswer;
import com.circles.health.patient.response.ProfileQuestionData;
import com.circles.health.patient.response.ProfileQuestionResponse;

public interface ProfileQuestionService {
	public List<ProfileQuestionResponse> getProfileQuestion();

	public ProfileQuestionData saveProfileQuestion(ProfileQuestionData profileQuestiondata);


}
