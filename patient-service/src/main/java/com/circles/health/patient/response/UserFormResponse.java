package com.circles.health.patient.response;

import com.circles.health.patient.entity.User;

public class UserFormResponse {

	private UserFormData data;
	private String status;
	private String message;
	
	public UserFormResponse() {
		super();
	}

	public UserFormResponse(UserFormData data, String status, String message) {
		super();
		this.data = data;
		this.status = status;
		this.message = message;
	}

	public UserFormData getData() {
		return data;
	}

	public void setData(UserFormData data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
