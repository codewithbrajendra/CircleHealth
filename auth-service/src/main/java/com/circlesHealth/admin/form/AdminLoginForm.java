package com.circlesHealth.admin.form;

//import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

//@Data
public class AdminLoginForm {

	@NotEmpty(message = "Email must not be blank.")
	@Email
	private String email;

	@NotBlank
	private String accessCode;

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