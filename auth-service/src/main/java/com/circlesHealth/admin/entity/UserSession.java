package com.circlesHealth.admin.entity;

//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(name = "user_session")
@EntityListeners(AuditingEntityListener.class)
public class UserSession implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_session_id")
    private Long id;

    @Column(name = "session_unique_id", nullable = false, unique = true)
    private String sessionUniqueId;

    @Column(name = "token", nullable = false, length = 2000)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "refresh_token", length = 2000)
    private String refreshToken;

    @Column(name = "created_date", nullable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

	public UserSession() {
		super();
	}

	public UserSession(Long id, String sessionUniqueId, String token, LocalDateTime expiresAt, Boolean active,
			Long userId, String refreshToken, LocalDateTime createdDate, LocalDateTime modifiedDate) {
		super();
		this.id = id;
		this.sessionUniqueId = sessionUniqueId;
		this.token = token;
		this.expiresAt = expiresAt;
		this.active = active;
		this.userId = userId;
		this.refreshToken = refreshToken;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSessionUniqueId() {
		return sessionUniqueId;
	}

	public void setSessionUniqueId(String sessionUniqueId) {
		this.sessionUniqueId = sessionUniqueId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}