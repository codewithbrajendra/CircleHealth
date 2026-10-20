package com.circles.health.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.circles.health.patient.entity.WellnessCheckQuestion;

public interface WellnessCheckQuestionRepository extends JpaRepository<WellnessCheckQuestion, Integer> {

}
