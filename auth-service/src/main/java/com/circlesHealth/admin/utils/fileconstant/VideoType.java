package com.circlesHealth.admin.utils.fileconstant;

import java.util.HashMap;
import java.util.Map;

public enum VideoType {

    MOV("video/quicktime"), MP4("video/mp4");

    private static final Map<String, VideoType> lookup = new HashMap<>();

    static {
        for (VideoType videoType : VideoType.values()) {
            lookup.put(videoType.value(), videoType);
        }
    }

    private String value;

    VideoType(String value) {
        this.value = value;
    }

    public static VideoType get(String value) {
        return lookup.get(value);
    }

    public String value() {
        return this.value;
    }
}
