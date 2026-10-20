package com.circles.health.patient.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.circles.health.patient.entity.ProfileAnswer;
import com.circles.health.patient.entity.ProfileQuestion;
import com.circles.health.patient.repository.ProfileAnswerRepository;
import com.circles.health.patient.repository.ProfileQuestionRepository;
import com.circles.health.patient.response.ProfileInfo;
import com.circles.health.patient.response.ProfileQuestionData;
import com.circles.health.patient.response.ProfileQuestionResponse;
import com.circles.health.patient.utils.DateUtil;

@Transactional
@Service
public class ProfileQuestionSeriviceImpl implements ProfileQuestionService {
	@Autowired
	private ProfileAnswerRepository profileAnswerRepository;
	@Autowired
	private ProfileQuestionRepository profileQuestionRepository;

	@Override
	public List<ProfileQuestionResponse> getProfileQuestion() {
		List<ProfileQuestionResponse> profileQuestionresponselist = new ArrayList<>();
		try {
			List<ProfileQuestion> profileQuestion = profileQuestionRepository.findAll();
			if (profileQuestion != null && profileQuestion.size() > 0) {
				for (ProfileQuestion question : profileQuestion) {
					ProfileQuestionResponse profileQuestionResponse = new ProfileQuestionResponse();
					profileQuestionResponse.setProfileId(question.getProfileId());
					profileQuestionResponse.setProfileQuestion(question.getProfileQuestion());
					profileQuestionResponse.setProfileQuestionSequence(question.getProfileQuestionSequence());

					if (question.getProfileQuestionSequence() == 1) {

						profileQuestionResponse.setProfileQuestionAttempt(true);

					} else {
						profileQuestionResponse.setProfileQuestionAttempt(false);
						;
					}
					profileQuestionresponselist.add(profileQuestionResponse);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return profileQuestionresponselist;
	}

	@Override
	public ProfileQuestionData saveProfileQuestion(ProfileQuestionData profileQuestiondata) {
		
		
		try {
			if(profileQuestiondata!=null && profileQuestiondata.getProfileInfo()!=null 
					&& profileQuestiondata.getProfileInfo().size()>0) {
				
				LocalDateTime dateTime=DateUtil.getCurrentDateTime();
				for( ProfileInfo questionInfo:profileQuestiondata.getProfileInfo()) {
					ProfileAnswer profileAnswer=new ProfileAnswer();
					profileAnswer.setUserId(profileQuestiondata.getUserId());
					profileAnswer.setWellnessCheckId(profileQuestiondata.getWellnessCheckId());
					profileAnswer.setProfileQuestionId(questionInfo.getProfileQuestionId());
					profileAnswer.setProfileAnswer(questionInfo.getProfileAnswer());
					profileAnswer.setProfileFillingDate(dateTime);

					profileAnswerRepository.save(profileAnswer);
				}
				
				
				
				
			}
			
		} catch (Exception e) {
e.printStackTrace();
		}
		return profileQuestiondata;
	}

	

}
