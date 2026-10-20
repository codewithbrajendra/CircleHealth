package com.circles.health.doctor.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.circles.health.doctor.entity.TherapistDetail;

@Repository
public interface TherapistRepository extends JpaRepository<TherapistDetail,Long>{

	TherapistDetail findByTherapistId(Long therapistId);
	
	@Query("select td.bearerToken from TherapistDetail td where td.therapistId = :therapistId")
	String getTokenByTherapistId(@Param("therapistId") Long therapistId);
}
