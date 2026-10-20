package com.circlesHealth.admin.dto;

public class RequestParamDto {

	private String email;
	private Long userId;
	private String accessCode;
	private String oldAccessCode;
	private String newAccessCode;
	
	public RequestParamDto() {
		super();
	}

	public RequestParamDto(String email, Long userId, String accessCode, String oldAccessCode, String newAccessCode) {
		super();
		this.email = email;
		this.userId = userId;
		this.accessCode = accessCode;
		this.oldAccessCode = oldAccessCode;
		this.newAccessCode = newAccessCode;
	}

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getOldAccessCode() {
		return oldAccessCode;
	}

	public void setOldAccessCode(String oldAccessCode) {
		this.oldAccessCode = oldAccessCode;
	}

	public String getNewAccessCode() {
		return newAccessCode;
	}

	public void setNewAccessCode(String newAccessCode) {
		this.newAccessCode = newAccessCode;
	}

}
