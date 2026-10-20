package com.circles.health.patient.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.circles.health.patient.entity.WellnessCheckAnswer;
import com.circles.health.patient.entity.WellnessCheckOption;
import com.circles.health.patient.entity.WellnessCheckQuestion;
import com.circles.health.patient.repository.WellnessCheckAnswerRepository;
import com.circles.health.patient.repository.WellnessCheckOptionRepository;
import com.circles.health.patient.repository.WellnessCheckQuestionRepository;
import com.circles.health.patient.response.SaveWellnessCheckData;
import com.circles.health.patient.response.WellnessCheck;
import com.circles.health.patient.response.WellnessCheckDetails;
import com.circles.health.patient.response.WellnessInfo;
import com.circles.health.patient.response.WellnessQuestion;
import com.circles.health.patient.utils.DateUtil;

@Service
@Transactional
public class WellnessCheckServiceImpl implements WellnessCheckService {
	@Autowired
	private WellnessCheckQuestionRepository wellnessCheckQuestionRepository;
	@Autowired
	private WellnessCheckOptionRepository wellnessCheckOptionRepository;
	@Autowired
	private WellnessCheckAnswerRepository wellnessCheckAnswerRepository;

	public List<WellnessCheck> getWellnessCheck() {

		List<WellnessCheck> wellneschecklist = new ArrayList<>();
		try {

			List<WellnessCheckQuestion> wellnessCheckQuestion = wellnessCheckQuestionRepository.findAll();
			if (wellnessCheckQuestion != null && wellnessCheckQuestion.size() > 0) {

				for (WellnessCheckQuestion question : wellnessCheckQuestion) {
					WellnessCheck wellcheck = new WellnessCheck();
					wellcheck.setWellnessQuestionId(question.getWellnessCheckQuestionId());
					wellcheck.setWellnessQuestionCategory(question.getWellnessCheckQuestionCategory());
					wellcheck.setWellnessQuestion(question.getWellnessCheckQuestion());
					wellcheck.setWellnessQuestionSequence(question.getWellnessCheckQuestionSequence());
					if (question.getWellnessCheckQuestionSequence() == 10
							|| question.getWellnessCheckQuestionSequence() == 18) {

						wellcheck.setWellnessOption(wellnessCheckOptionRepository.findByCategory("Optional"));

					} else {
						wellcheck.setWellnessOption(wellnessCheckOptionRepository.findByCategory("Required"));
					}
					
					if (question.getWellnessCheckQuestionSequence() == 1) {

						wellcheck.setWellnessQuestionAttempt(true);

					} else {
						wellcheck.setWellnessQuestionAttempt(false);
					}

					wellcheck.setTakentime("0");
					
					wellneschecklist.add(wellcheck);
					
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return wellneschecklist;
	}

	@Override
	public WellnessCheckAnswer saveWellnessCheck(WellnessQuestion wellnessQuestion) {
		WellnessCheckAnswer wellnessCheckAnswer = new WellnessCheckAnswer();
		wellnessCheckAnswer.setUserId(wellnessQuestion.getUserId());

		if (wellnessQuestion.getWellnessInfo() != null && wellnessQuestion.getWellnessInfo().size() > 0) {
			String wellness = "";
			int score = 0, phqscore = 0, gadscore = 0;

			for (WellnessInfo info : wellnessQuestion.getWellnessInfo()) {
				if (info.getWellnessresponseScore() != null && info.getWellnessoptionId() != null) {

					wellness += (info.getWellnessquestionId() + ":" + info.getWellnessoptionId() + ":"
							+ (info.getWellnesstimeInSec() != null ? info.getWellnesstimeInSec() : 0) + ":"
							+ info.getWellnessresponseScore() + ",");

					score += info.getWellnessresponseScore();

					if (info.getWellnesscategory().equalsIgnoreCase("PHQ9")) {
						phqscore += info.getWellnessresponseScore();

					} else if (info.getWellnesscategory().equalsIgnoreCase("GAD7")) {
						gadscore += info.getWellnessresponseScore();

					}
				}
			}

			wellnessCheckAnswer.setWellnessResponse(wellness.substring(0, wellness.length() - 1));
			wellnessCheckAnswer.setResponseScore(score);
			wellnessCheckAnswer.setWellnessCheckDate(DateUtil.getCurrentDateTime());
			wellnessCheckAnswer.setPhq9Score(phqscore);
			wellnessCheckAnswer.setGad7Score(gadscore);

		}

		return wellnessCheckAnswerRepository.save(wellnessCheckAnswer);

	} 

	@Override
	public SaveWellnessCheckData saveWellnessCheck1(WellnessQuestion wellnessQuestion) {
		WellnessCheckAnswer wellnessCheckAnswer = new WellnessCheckAnswer();
		wellnessCheckAnswer.setUserId(wellnessQuestion.getUserId());
        
		List<WellnessCheckDetails> wellnessCheckDetails = new ArrayList<>();
		SaveWellnessCheckData saveWellnessCheckData = new SaveWellnessCheckData();
		if (wellnessQuestion.getWellnessInfo() != null && wellnessQuestion.getWellnessInfo().size() > 0) {
			String wellness = "";
			int score = 0, phqscore = 0, gadscore = 0;

			for (WellnessInfo info : wellnessQuestion.getWellnessInfo()) {
				if (info.getWellnessresponseScore() != null && info.getWellnessoptionId() != null) {
					WellnessCheckDetails WellnessCheckDetail = new WellnessCheckDetails();
					wellness += (info.getWellnessquestionId() + ":" + info.getWellnessoptionId() + ":"
							+ (info.getWellnesstimeInSec() != null ? info.getWellnesstimeInSec() : 0) + ":"
							+ info.getWellnessresponseScore() + ",");

					score += info.getWellnessresponseScore();

					if (info.getWellnesscategory().equalsIgnoreCase("PHQ9")) {
						phqscore += info.getWellnessresponseScore();
						WellnessCheckDetail.setScore(info.getWellnessresponseScore());

					} else if (info.getWellnesscategory().equalsIgnoreCase("GAD7")) {
						gadscore += info.getWellnessresponseScore();
						WellnessCheckDetail.setScore(info.getWellnessresponseScore());

					}
					WellnessCheckQuestion wellnessCheckQuestion = this.wellnessCheckQuestionRepository.getById(info.getWellnessquestionId());
					WellnessCheckDetail.setQuestionId(wellnessCheckQuestion.getWellnessCheckQuestionId());
					WellnessCheckDetail.setQuestionSequence(wellnessCheckQuestion.getWellnessCheckQuestionSequence());
					WellnessCheckDetail.setQuestion(wellnessCheckQuestion.getWellnessCheckQuestion());
					WellnessCheckDetail.setCategory(wellnessCheckQuestion.getWellnessCheckQuestionCategory());
					
					
					WellnessCheckOption wellnessCheckOption = new WellnessCheckOption();
					WellnessCheckDetail.setOptionId(wellnessCheckOption.getWellnessOptionId());
					WellnessCheckDetail.setOptionSequence(wellnessCheckOption.getOptionSequence());
					WellnessCheckDetail.setOption(wellnessCheckOption.getOptionCaption());
					
					wellnessCheckDetails.add(WellnessCheckDetail);
				}
			}

			
			
			
			
			wellnessCheckAnswer.setWellnessResponse(wellness.substring(0, wellness.length() - 1));
			wellnessCheckAnswer.setResponseScore(score);
			wellnessCheckAnswer.setWellnessCheckDate(DateUtil.getCurrentDateTime());
			wellnessCheckAnswer.setPhq9Score(phqscore);
			wellnessCheckAnswer.setGad7Score(gadscore);
			saveWellnessCheckData.setGad7Score(gadscore);
			saveWellnessCheckData.setPhq9Score(phqscore);
			saveWellnessCheckData.setResponseScore(score);
			saveWellnessCheckData.setWellnessCheckDetails(wellnessCheckDetails);
		}

		WellnessCheckAnswer savedWellnessCheckAnswer = this.wellnessCheckAnswerRepository.save(wellnessCheckAnswer);
		saveWellnessCheckData.setWellnessAnswerId(savedWellnessCheckAnswer.getWellnessAnswerId());
		saveWellnessCheckData.setUserId(wellnessQuestion.getUserId());
		saveWellnessCheckData.setWellnessCheckDate(DateUtil.getCurrentDateTime());
		
		return saveWellnessCheckData;

	} 
	
	@Override
	public LocalDateTime getWellnessCheckDate(Long userId) {
		
		WellnessCheckAnswer wellnessCheckAnswer = this.wellnessCheckAnswerRepository.findTopByUserIdOrderByWellnessAnswerIdDesc(userId);
		
		if(wellnessCheckAnswer != null) {
			return wellnessCheckAnswer.getWellnessCheckDate();
		}
		
		return null;
	}

	
	
	
	
}
