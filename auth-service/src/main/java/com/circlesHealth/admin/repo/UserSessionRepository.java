package com.circlesHealth.admin.repo;

import com.circlesHealth.admin.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByTokenAndSessionUniqueId(String token, String sessionUniqueId);

    Optional<UserSession> findBySessionUniqueIdAndActive(String sessionUniqueId, boolean active);

    Optional<UserSession> findBySessionUniqueIdAndRefreshToken(String sessionUniqueId, String refreshToken);

    @Modifying
    @Transactional
    void deleteByActiveOrExpiresAtLessThan(boolean active, LocalDateTime expiresAt);
    
    List<UserSession> findByUserId(Long userId);
    
    @Transactional
    @Modifying
    @Query("delete from UserSession userSession where userSession.id = :id")
    void deleteById(@Param("id") Long id);
}
