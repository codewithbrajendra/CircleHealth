package com.circles.health.patient.dto;

public class RequestParamDto {

	private Long userId;
	private String email;
	private String accessCode;
	
	public RequestParamDto() {
		super();
	}

	public RequestParamDto(Long userId, String email, String accessCode) {
		this.userId = userId;
		this.email = email;
		this.accessCode = accessCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	
	
}
