package com.circlesHealth.admin.utils;

import java.util.UUID;

public final class StringUtils {
    private StringUtils() {
    }

    public static boolean isNotEmpty(String s) {
        return s != null && !s.isEmpty();
    }

    public static String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static String trimValue(String s) {
        return s != null ? s.trim() : null;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
    
    public static String getExtension(String fileName) {
    	return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
