package com.circles.health.patient.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.circles.health.patient.entity.VideoPlayData;


public interface VideoPlayRepository extends JpaRepository<VideoPlayData, Long>{

	Long countByVideoPlayTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

	@Query(value = "select count(*) from video_play_data where video_play_time between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender)",nativeQuery = true)
	Long countByVideoPlayTimeBetweenByGender(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender);
	
	@Query(value = "select count(*) from video_play_data where video_play_time between :startdate and :enddate and user_id in (select u.user_id from user u where u.ethnicity = :ethnicity)",nativeQuery = true)
	Long countByVideoPlayTimeBetweenByEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ethnicity") String ethnicity);
	
	@Query(value = "select count(*) from video_play_data where video_play_time between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.ethnicity = :ethnicity)",nativeQuery = true)
	Long countByVideoPlayTimeBetweenByGenderAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ethnicity") String ethnicity);
	
	@Query(value = "select count(*) from video_play_data where video_play_time between :startdate and :enddate and user_id in (select u.user_id from user u where u.dob between :ageStart and :ageEnd)",nativeQuery = true)
	Long countByVideoPlayTimeBetweenByAge(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(*) from video_play_data where video_play_time between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.dob between :ageStart and :ageEnd)",nativeQuery = true)
	Long countByVideoPlayTimeBetweenByAgeAndGender(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(*) from video_play_data where video_play_time between :startdate and :enddate and user_id in (select u.user_id from user u where u.ethnicity = :ethnicity and u.dob between :ageStart and :ageEnd)",nativeQuery = true)
	Long countByVideoPlayTimeBetweenByAgeAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("ethnicity") String ethnicity,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	@Query(value = "select count(*) from video_play_data where video_play_time between :startdate and :enddate and user_id in (select u.user_id from user u where u.gender = :gender and u.ethnicity = :ethnicity and u.dob between :ageStart and :ageEnd)",nativeQuery = true)
	Long countByVideoPlayTimeBetweenByAgeAndGenderAndEthnicity(@Param("startdate")LocalDateTime startDate, @Param("enddate")LocalDateTime endDate,@Param("gender") String gender,@Param("ethnicity") String ethnicity,@Param("ageStart") LocalDate ageStart,@Param("ageEnd") LocalDate ageEnd);
	
	Long countByUserIdAndVideoPlayTimeBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);

}
