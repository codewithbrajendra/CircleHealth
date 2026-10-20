package com.circlesHealth.admin.form;

//import lombok.Data;

import javax.validation.constraints.NotEmpty;

//@Data
public class PatientLoginForm {

    @NotEmpty(message = "Access code must not be blank.")
    private String accessCode;

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
}