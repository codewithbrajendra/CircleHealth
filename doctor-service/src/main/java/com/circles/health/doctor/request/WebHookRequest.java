package com.circles.health.doctor.request;

import java.util.List;

public class WebHookRequest {

	private String url;
	private List<String> events;
	private String organization;
	private String user;
	private String scope;
	private String signing_key;
	
	public WebHookRequest() {
		super();
	}

	public WebHookRequest(String url, List<String> events, String organization, String user, String scope,
			String signing_key) {
		this.url = url;
		this.events = events;
		this.organization = organization;
		this.user = user;
		this.scope = scope;
		this.signing_key = signing_key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getEvents() {
		return events;
	}

	public void setEvents(List<String> events) {
		this.events = events;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getSigning_key() {
		return signing_key;
	}

	public void setSigning_key(String signing_key) {
		this.signing_key = signing_key;
	}
	
}
