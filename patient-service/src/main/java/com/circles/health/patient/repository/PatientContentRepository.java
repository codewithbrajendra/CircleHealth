package com.circles.health.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.circles.health.patient.entity.HomeContent;
import com.circles.health.patient.entity.VideoContent;

public interface PatientContentRepository extends JpaRepository<VideoContent, Long>{

	VideoContent findByUserId(Long userId);
}
