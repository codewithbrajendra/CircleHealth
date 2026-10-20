package com.circlesHealth.admin.repo;

import com.circlesHealth.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserId(Long userId);

	Optional<User> findByEmail(String email);

	Optional<User> findByAccessCodeAndUserType(String accessCode,String userType);

	@Query(value = "select * from user where email = :email and access_code = :accessCode and user_type in :userType", nativeQuery=true )
	Optional<User> findByEmailAndAccessCodeAndUserType(String email,String accessCode,List<String> userType);

	Optional<User> findByUserUniqueId(String userUniqueId);

	boolean existsByUserUniqueId(String userUniqueId);

	boolean existsByEmail(String email);

	@Query(value = "select * from user where user_unique_id = ?1", nativeQuery = true)
	User findUserNames(String userUniqueId);
	
	User findByAccessCode(String accessCode);
	
	User findByPatientId(Long patientId);
	
	@Transactional
    @Modifying
	@Query("delete from User u where u.patientId = :patientId")
	void deleteByPatientId(Long patientId);

	//User  findByAdminId(Long userId);
	User getByUserId(@Param("userId") Long userId);
	
	User getByUserIdAndEmail(Long userId, String email);
	
	User getByEmail(@Param("email") String email);
	
}
