package com.circles.health.patient.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ForgottenAccessCodeRequest {

	private String email;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate dob;
	
	public ForgottenAccessCodeRequest() {
		super();
	}

	public ForgottenAccessCodeRequest(String email, LocalDate dob) {
		super();
		this.email = email;
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

}
