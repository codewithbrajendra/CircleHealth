package com.circles.health.patient.request;

public class FilterUserRequest {

	private String email;
	private String firstName;
	private String surName;
	
	public FilterUserRequest() {
		super();
	}

	public FilterUserRequest(String email, String firstName, String surName) {
		this.email = email;
		this.firstName = firstName;
		this.surName = surName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}
	
}
