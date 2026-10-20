package com.circles.health.patient.request;

public class EditTherapistNameRequest {

	private Long therapistId;
	private Long userId;
	private String therapistName;
	
	public EditTherapistNameRequest() {
		super();
	}

	public EditTherapistNameRequest(Long therapistId, Long userId, String therapistName) {
		this.therapistId = therapistId;
		this.userId = userId;
		this.therapistName = therapistName;
	}

	public Long getTherapistId() {
		return therapistId;
	}

	public void setTherapistId(Long therapistId) {
		this.therapistId = therapistId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTherapistName() {
		return therapistName;
	}

	public void setTherapistName(String therapistName) {
		this.therapistName = therapistName;
	}
	
}
