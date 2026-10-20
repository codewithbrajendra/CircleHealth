package com.circles.health.patient.response;

public class TherapistPreference {

	private String name;
	private Long value;
	
	public TherapistPreference() {
		super();
	}

	public TherapistPreference(String name, Long value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}
	
}
