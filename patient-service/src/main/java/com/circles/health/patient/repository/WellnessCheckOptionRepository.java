package com.circles.health.patient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.circles.health.patient.entity.WellnessCheckOption;

public interface WellnessCheckOptionRepository extends JpaRepository<WellnessCheckOption, Long> {

	List<WellnessCheckOption> findByCategory(String string);

}
