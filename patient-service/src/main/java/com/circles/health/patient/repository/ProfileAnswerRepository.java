
package com.circles.health.patient.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.circles.health.patient.entity.ProfileAnswer;

public interface ProfileAnswerRepository extends JpaRepository<ProfileAnswer, Long> {

	List<ProfileAnswer> findByWellnessCheckId(Long wellnessAnswerId);

	ProfileAnswer findTopByUserIdOrderByProfileAnswerIdDesc(Long userId);

	@Query(value = "select count(distinct wellness_check_id) from profile_question_answer where profile_filling_date between :startdate and :enddate", nativeQuery = true)
	Long totalProfileQuestionCount(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate);

	@Query(value = "select count(distinct wellness_check_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender)", nativeQuery = true)
	Long totalProfileQuestionCountByGender(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender);
	
	@Query(value = "select count(distinct wellness_check_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.ethnicity = :ethnicity)", nativeQuery = true)
	Long totalProfileQuestionCountByEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ethnicity") String ethnicity);
	
	@Query(value = "select count(distinct wellness_check_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.ethnicity = :ethnicity)", nativeQuery = true)
	Long totalProfileQuestionCountByGenderAndEhtnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ethnicity") String ethnicity);
	
	@Query(value = "select count(distinct user_id) from profile_question_answer where profile_filling_date between :startdate and :enddate", nativeQuery = true)
	Long distinctUserProfileQuestion(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate);

	@Query(value = "select count(distinct user_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender)", nativeQuery = true)
	Long distinctUserProfileQuestionByGender(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender);
	
	@Query(value = "select count(distinct user_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.ethnicity = :ethnicity)", nativeQuery = true)
	Long distinctUserProfileQuestionByEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ethnicity") String ethnicity);
	
	@Query(value = "select count(distinct user_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.ethnicity = :ethnicity)", nativeQuery = true)
	Long distinctUserProfileQuestionByGenderAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ethnicity") String ethnicity);
	
	@Query(value = "select count(distinct wellness_check_id) from profile_question_answer where user_id=:userid and profile_filling_date between :startdate and :enddate",
			nativeQuery = true)
	Long totalProfileQuestionCountByUser(@Param("userid")Long userId, @Param("startdate")LocalDateTime startDateTime, @Param("enddate")LocalDateTime endDateTime);

	@Query(value = "select count(distinct wellness_check_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long totalProfileQuestionCountByAge(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);

	@Query(value = "select count(distinct wellness_check_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long totalProfileQuestionCountByAgeAndGender(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(distinct wellness_check_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.ethnicity = :ethnicity and u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long totalProfileQuestionCountByAgeAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ethnicity") String ethnicity,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(distinct wellness_check_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.ethnicity = :ethnicity and u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long totalProfileQuestionCountByAgeAndGenderAndEhtnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ethnicity") String ethnicity,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(distinct user_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long distinctUserProfileQuestionByAge(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(distinct user_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long distinctUserProfileQuestionByAgeAndGender(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(distinct user_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.ethnicity = :ethnicity and u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long distinctUserProfileQuestionByAgeAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ethnicity") String ethnicity,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(distinct user_id) from profile_question_answer where profile_filling_date between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.ethnicity = :ethnicity and u.dob between :ageStart and :ageEnd)", nativeQuery = true)
	Long distinctUserProfileQuestionByAgeAndGenderAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ethnicity") String ethnicity,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	

}
