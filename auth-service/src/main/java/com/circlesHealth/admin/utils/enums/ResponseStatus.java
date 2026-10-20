package com.circlesHealth.admin.utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum ResponseStatus {
    SUCCESS("Success"), FAILURE("Failure");

    private String value;

    ResponseStatus(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    private static final Map<String, ResponseStatus> lookup = new HashMap<>();
    static {
        for (ResponseStatus responseStatus : ResponseStatus.values()) {
            lookup.put(responseStatus.value(), responseStatus);
        }
    }

    public static ResponseStatus get(String value) {
        return lookup.get(value);
    }
}
