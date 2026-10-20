package com.circlesHealth.admin.utils.fileconstant;

import java.util.HashMap;
import java.util.Map;

public enum ImageType {

    PNG("image/png"), JPEG("image/jpeg"), JPG("image/jpg"), HEIC("image/heic");

    private static final Map<String, ImageType> lookup = new HashMap<>();

    static {
        for (ImageType imageType : ImageType.values()) {
            lookup.put(imageType.value(), imageType);
        }
    }

    private String value;

    ImageType(String value) {
        this.value = value;
    }

    public static ImageType get(String value) {
        return lookup.get(value);
    }

    public String value() {
        return this.value;
    }
}
