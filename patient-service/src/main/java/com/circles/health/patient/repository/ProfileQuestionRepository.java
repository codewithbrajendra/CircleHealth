package com.circles.health.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.circles.health.patient.entity.ProfileQuestion;

public interface ProfileQuestionRepository extends JpaRepository<ProfileQuestion, Long> {

}
