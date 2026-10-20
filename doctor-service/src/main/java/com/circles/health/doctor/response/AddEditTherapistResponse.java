package com.circles.health.doctor.response;


public class AddEditTherapistResponse {

	private Long therapistId;
	private String therapistFirstName;
	private String therapistLastName;
	private String bearerToken;
	private String schedulingUrl;
	private String email;
	private String apiKey;
	
	public AddEditTherapistResponse() {
		super();
	}

	public AddEditTherapistResponse(Long therapistId, String therapistFirstName, String therapistLastName,
			String bearerToken, String schedulingUrl, String email, String apiKey) {
		this.therapistId = therapistId;
		this.therapistFirstName = therapistFirstName;
		this.therapistLastName = therapistLastName;
		this.bearerToken = bearerToken;
		this.schedulingUrl = schedulingUrl;
		this.email = email;
		this.apiKey = apiKey;
	}

	public Long getTherapistId() {
		return therapistId;
	}

	public void setTherapistId(Long therapistId) {
		this.therapistId = therapistId;
	}

	public String getTherapistFirstName() {
		return therapistFirstName;
	}

	public void setTherapistFirstName(String therapistFirstName) {
		this.therapistFirstName = therapistFirstName;
	}

	public String getTherapistLastName() {
		return therapistLastName;
	}

	public void setTherapistLastName(String therapistLastName) {
		this.therapistLastName = therapistLastName;
	}

	public String getBearerToken() {
		return bearerToken;
	}

	public void setBearerToken(String bearerToken) {
		this.bearerToken = bearerToken;
	}

	public String getSchedulingUrl() {
		return schedulingUrl;
	}

	public void setSchedulingUrl(String schedulingUrl) {
		this.schedulingUrl = schedulingUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
}
