package com.circles.health.patient.response;

public class Ethnicity {

	private String name;
	private String value;
	
	public Ethnicity() {
		super();
	}

	public Ethnicity(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
