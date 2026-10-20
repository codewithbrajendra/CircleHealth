package com.circles.health.patient.response;

import java.util.List;
import java.util.Map;

public class UserFormData {

	List<Gender> gender;
	
	List<Ethnicity> eithnicity;
	
	List<TherapistPreference> therapistPreference;
	
	public UserFormData() {
		super();
	}

	public UserFormData(List<Gender> gender, List<Ethnicity> eithnicity,
			List<TherapistPreference> therapistPreference) {
		this.gender = gender;
		this.eithnicity = eithnicity;
		this.therapistPreference = therapistPreference;
	}

	public List<Gender> getGender() {
		return gender;
	}

	public void setGender(List<Gender> gender) {
		this.gender = gender;
	}

	public List<Ethnicity> getEithnicity() {
		return eithnicity;
	}

	public void setEithnicity(List<Ethnicity> eithnicity) {
		this.eithnicity = eithnicity;
	}

	public List<TherapistPreference> getTherapistPreference() {
		return therapistPreference;
	}

	public void setTherapistPreference(List<TherapistPreference> therapistPreference) {
		this.therapistPreference = therapistPreference;
	}
	
}
