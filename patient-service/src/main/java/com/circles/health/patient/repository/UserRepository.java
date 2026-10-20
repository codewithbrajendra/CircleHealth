package com.circles.health.patient.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.circles.health.patient.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	@Query("select u from User u where u.email = :email and u.dob = :dob")
	public User getByEmailAndDOB(@Param("email") String email, @Param("dob") LocalDate dob);
	
	@Query("select u from User u where u.accessCode = :accessCode")
	public User getByAccessCode(@Param("accessCode") String accessCode);
	
	public User getByEmail(@Param("email") String email);
	
	public void deleteByEmail(@Param("email") String email);

	Optional<User> findByUserUniqueId(String userUniqueId);
	
	User getByUserId(@Param("userId") Long userId);
	
	User getByUserIdAndAccessCode(Long userId, String accessCode);
	
	User getByUserIdAndEmail(Long userId, String email);
	
	List<User> getByTherapistId(Long therapistId);
	
	List<User> findByFirstNameStartsWith(String firstName);
	
	List<User> findBySurNameStartsWith(String surName);
	
	List<User> findByEmailStartsWith(String email);
	
	List<User> findByFirstNameStartsWithAndSurNameStartsWith(@Param("firstName") String firstName, @Param("surName") String surName);
	
	List<User> findByFirstNameStartsWithAndEmailStartsWith(@Param("firstName") String firstName, @Param("email") String email);
	
	List<User> findBySurNameStartsWithAndEmailStartsWith(@Param("surName") String surName, @Param("email") String email);
	
	List<User> findByFirstNameStartsWithAndSurNameStartsWithAndEmailStartsWith(@Param("firstName") String firstName, @Param("surName") String surName, @Param("email") String email);

    @Query("select u.therapistId from User u where u.email = :email")
	Long getTherapistIdByEmail(@Param("email") String email);

    @Query(value="select count(distinct email) from user", nativeQuery = true)
	Long countByDistinctEmail();
    
    @Query(value="select count(*) from user", nativeQuery = true)
    Long getCountUser();
	
}
