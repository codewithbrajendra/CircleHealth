package com.circles.health.doctor.response;

public class TherapistNamesResponse {

	private Long therapistId;
	private String therapistName;
	
	public TherapistNamesResponse() {
		super();
	}

	public TherapistNamesResponse(Long therapistId, String therapistName) {
		this.therapistId = therapistId;
		this.therapistName = therapistName;
	}

	public Long getTherapistId() {
		return therapistId;
	}

	public void setTherapistId(Long therapistId) {
		this.therapistId = therapistId;
	}

	public String getTherapistName() {
		return therapistName;
	}

	public void setTherapistName(String therapistName) {
		this.therapistName = therapistName;
	}
}
