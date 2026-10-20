package com.circlesHealth.admin.response;

import java.time.LocalDate;

public class AnalyticsUserRequestData {

	private LocalDate dob;
	private String gender;
	private String ethnicity;
	
	public AnalyticsUserRequestData() {
		super();
	}

    public AnalyticsUserRequestData(LocalDate dob, String gender, String ethnicity) {
		this.dob = dob;
		this.gender = gender;
		this.ethnicity = ethnicity;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}
	
	
}
