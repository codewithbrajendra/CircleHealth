package com.circlesHealth.admin.utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserRoles {
	ROLE_USER("ROLE_USER"), ROLE_ADMIN("ROLE_ADMIN"), ROLE_RCE("ROLE_RCE"), ROLE_THERAPIST("ROLE_THERAPIST"),
	ROLE_INTERPRETER("ROLE_INTERPRETER"), ROLE_FAMILY("ROLE_FAMILY");

	private final String value;

	private static final Map<String, UserRoles> lookup = new HashMap<>();

	public String value() {
		return this.value;
	}

	static {
		for (UserRoles role : UserRoles.values()) {
			lookup.put(role.value(), role);
		}
	}

	UserRoles(String value) {
		this.value = value;
	}

	public static UserRoles get(String value) {
		return lookup.get(value);
	}
}
