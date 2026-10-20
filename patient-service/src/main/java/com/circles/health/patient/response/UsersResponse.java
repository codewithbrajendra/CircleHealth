package com.circles.health.patient.response;

import java.util.List;

import com.circles.health.patient.entity.User;

public class UsersResponse {

	private List<User> data;
	private String status;
	private String message;
	
	public UsersResponse() {
		super();
	}

	public UsersResponse(List<User> data, String status, String message) {
		this.data = data;
		this.status = status;
		this.message = message;
	}

    public List<User> getData() {
		return data;
	}

	public void setData(List<User> data) {
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
