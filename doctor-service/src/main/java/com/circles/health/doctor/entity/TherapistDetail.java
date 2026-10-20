package com.circles.health.doctor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "therapist_detail")
public class TherapistDetail {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "therapist_id")
    private Long therapistId;
	
	@Column(name = "user_id")
    private Long userId;
	
	@Column(name = "therapist_uuid")
	private String therapistUUID;
	
	@Column(name = "therapist_name")
	private String therapistName;
	
	@Column(name = "therapist_first_name")
	private String therapistFirstName;
	
	@Column(name = "therapist_last_name")
	private String therapistLastName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "scheduling_url")
	private String schedulingUrl;
	
	@Column(name = "bearer_token")
	private String bearerToken;

	@Column(name = "api_key")
	private String apiKey;
	
	@Column(name = "calendly_web_hook_subscription_url")
	private String calendlyWebHookSubscriptionUrl;
	
	public TherapistDetail() {
		super();
	}

	public TherapistDetail(Long therapistId, Long userId, String therapistUUID, String therapistName,
			String therapistFirstName, String therapistLastName, String email, String schedulingUrl,
			String bearerToken, String apiKey, String calendlyWebHookSubscriptionUrl) {
		this.therapistId = therapistId;
		this.userId = userId;
		this.therapistUUID = therapistUUID;
		this.therapistName = therapistName;
		this.therapistFirstName = therapistFirstName;
		this.therapistLastName = therapistLastName;
		this.email = email;
		this.schedulingUrl = schedulingUrl;
		this.bearerToken = bearerToken;
		this.apiKey = apiKey;
		this.calendlyWebHookSubscriptionUrl = calendlyWebHookSubscriptionUrl;
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

	public String getTherapistUUID() {
		return therapistUUID;
	}

	public void setTherapistUUID(String therapistUUID) {
		this.therapistUUID = therapistUUID;
	}

	public String getTherapistName() {
		return therapistName;
	}

	public void setTherapistName(String therapistName) {
		this.therapistName = therapistName;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSchedulingUrl() {
		return schedulingUrl;
	}

	public void setSchedulingUrl(String schedulingUrl) {
		this.schedulingUrl = schedulingUrl;
	}

	public String getBearerToken() {
		return bearerToken;
	}

	public void setBearerToken(String bearerToken) {
		this.bearerToken = bearerToken;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getCalendlyWebHookSubscriptionUrl() {
		return calendlyWebHookSubscriptionUrl;
	}

	public void setCalendlyWebHookSubscriptionUrl(String calendlyWebHookSubscriptionUrl) {
		this.calendlyWebHookSubscriptionUrl = calendlyWebHookSubscriptionUrl;
	}
	
}
