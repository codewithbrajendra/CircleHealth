package com.circles.health.patient.response;

public class ValidMessageResponse {

	private String status;
	private String message;
	private Boolean isValid;
	
	public ValidMessageResponse() {
		super();
	}

	public ValidMessageResponse(String status, String message, Boolean isValid) {
		super();
		this.status = status;
		this.message = message;
		this.isValid = isValid;
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

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
	
}
