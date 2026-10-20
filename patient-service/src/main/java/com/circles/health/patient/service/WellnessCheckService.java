package com.circles.health.patient.service;

import java.time.LocalDateTime;
import java.util.List;

import com.circles.health.patient.entity.WellnessCheckAnswer;
import com.circles.health.patient.response.SaveWellnessCheckData;
import com.circles.health.patient.response.WellnessCheck;
import com.circles.health.patient.response.WellnessQuestion;

public interface WellnessCheckService {
	public List<WellnessCheck> getWellnessCheck();

	public WellnessCheckAnswer saveWellnessCheck(WellnessQuestion wellnessQuestion);

    public LocalDateTime getWellnessCheckDate(Long userId);
	
    public SaveWellnessCheckData saveWellnessCheck1(WellnessQuestion wellnessQuestion);
}
