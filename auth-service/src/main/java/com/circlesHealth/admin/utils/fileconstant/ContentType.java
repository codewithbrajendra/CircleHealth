package com.circlesHealth.admin.utils.fileconstant;

import java.util.HashMap;
import java.util.Map;

public enum ContentType {

    IMAGE("IMAGE"), VIDEO("VIDEO");

    private static final Map<String, ContentType> lookup = new HashMap<>();

    static {
        for (ContentType contentType : ContentType.values()) {
            lookup.put(contentType.value(), contentType);
        }
    }

    private String value;

    ContentType(String value) {
        this.value = value;
    }

    public static ContentType get(String value) {
        return lookup.get(value);
    }

    public String value() {
        return this.value;
    }
}
