package com.circlesHealth.admin.utils;

import java.util.Collection;

public final class CollectionUtils {
    private CollectionUtils() {
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
