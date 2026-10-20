package com.circlesHealth.admin.utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum MySqlConstant {
    TRUE(1), FALSE(0);

    private static final Map<Long, MySqlConstant> lookup = new HashMap<>();

    static {
        for (MySqlConstant mySqlConstant : MySqlConstant.values()) {
            lookup.put(mySqlConstant.value(), mySqlConstant);
        }
    }

    private long value;

    MySqlConstant(long value) {
        this.value = value;
    }

    public static MySqlConstant get(long value) {
        return lookup.get(value);
    }

    public long value() {
        return this.value;
    }
}
