package com.circles.health.doctor.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.circles.health.doctor.entity.PatientBookingDetail;

@Repository
public interface PatientBookingRepository extends JpaRepository<PatientBookingDetail, Long>{

	PatientBookingDetail findByEmail(String email);

	@Query(value="select count(*) from patient_booking_detail where created_at between :startdate and :enddate", nativeQuery = true)
	Long bookingCountByDateRange(@Param("startdate")LocalDate startDate, @Param("enddate")LocalDate endDate);

	@Query(value="select count(distinct email) from patient_booking_detail where created_at between :startdate and :enddate", nativeQuery = true)
	Long distinctUserCountForBooking(@Param("startdate")LocalDate startDate, @Param("enddate")LocalDate endDate);
	
	@Query(value = "select * from patient_booking_detail where user_id = :userId order by patient_booking_id desc LIMIT 1", nativeQuery = true)
	PatientBookingDetail findByUserIdAndStatusOrderByIdDesc(@Param("userId")Long userId);
	
	List<PatientBookingDetail> findByUserId(Long userId);

	@Query(value="select count(*) from patient_booking_detail where user_id=:userid and created_at between :startdate and :enddate", nativeQuery = true)
	Long bookingCountByUserIdAndDateRange(@Param("userid")Long userId, @Param("startdate")LocalDate startDate, @Param("enddate")LocalDate endDate);

	@Query(value="select * from patient_booking_detail where user_id=:userid and booking_date between :startdate and :enddate",nativeQuery = true)
	List<PatientBookingDetail> findByUserIdAndBookingDateRange(@Param("userid")Long userId, @Param("startdate")LocalDate startDate, @Param("enddate")LocalDate endDate);

}
