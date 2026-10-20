package com.circles.health.patient.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.circles.health.patient.entity.WellnessCheckAnswer;

public interface WellnessCheckAnswerRepository extends JpaRepository<WellnessCheckAnswer, Long> {

	List<WellnessCheckAnswer> findByUserId(Long userId);

	WellnessCheckAnswer findTopByUserIdOrderByWellnessAnswerIdDesc(Long userId);

	WellnessCheckAnswer findByUserIdAndWellnessAnswerId(Long userId, Long wellnessId);

	List<WellnessCheckAnswer> findByUserIdOrderByWellnessAnswerIdDesc(Long userId);

	@Query(value = "select count(distinct user_id) from wellness_check_answer", nativeQuery = true)
	Long distinctUsersWellnessCheck();
	
	@Query(value = "select count(distinct user_id) from wellness_check_answer where wellness_check_date between :startdate and :enddate", nativeQuery = true)
	Long distinctUsersWellnessCheckByDateRange(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate);

	@Query(value = "select count(distinct user_id) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender)", nativeQuery = true)
	Long distinctUsersWellnessCheckByDateRangeAndGender(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender);
	
	@Query(value = "select count(distinct user_id) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.ethnicity = :ethnicity)", nativeQuery = true)
	Long distinctUsersWellnessCheckByDateRangeAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ethnicity") String ethnicity);
	
	@Query(value = "select count(distinct user_id) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.ethnicity = :ethnicity)", nativeQuery = true)
	Long distinctUsersWellnessCheckByDateRangeAndGenderAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ethnicity") String ethnicity);
	
	Long countByWellnessCheckDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

	@Query(value = "select count(*) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender)", nativeQuery = true)
	Long countByWellnessCheckDateBetweenAndGender(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender);

	@Query(value = "select count(*) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.ethnicity = :ethnicity)", nativeQuery = true)
	Long countByWellnessCheckDateBetweenAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ethnicity") String ethnicity);
	
	@Query(value = "select count(*) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.ethnicity = :ethnicity)", nativeQuery = true)
	Long countByWellnessCheckDateBetweenAndGenderAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ethnicity") String ethnicity);
	
	Long countByUserIdAndWellnessCheckDateBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
	
	@Query(value = "select count(distinct user_id) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long distinctUsersWellnessCheckByDateRangeAndAge(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);

	@Query(value = "select count(distinct user_id) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long distinctUsersWellnessCheckByDateRangeAgeAndGender(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(distinct user_id) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.ethnicity = :ethnicity and u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long distinctUsersWellnessCheckByDateRangeAgeAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ethnicity") String ethnicity,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(distinct user_id) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.ethnicity = :ethnicity and u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long distinctUsersWellnessCheckByDateRangeAgeAndGenderAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ethnicity") String ethnicity,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(*) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long countByWellnessCheckDateBetweenAndAge(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(*) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long countByWellnessCheckDateBetweenAgeAndGender(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);

	@Query(value = "select count(*) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.ethnicity = :ethnicity and u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long countByWellnessCheckDateBetweenAgeAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ethnicity") String ethnicity,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(*) from wellness_check_answer where wellness_check_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.ethnicity = :ethnicity and u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long countByWellnessCheckDateBetweenAgeAndGenderAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ethnicity") String ethnicity,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	
}
