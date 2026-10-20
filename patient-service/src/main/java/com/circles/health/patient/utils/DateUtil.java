package com.circles.health.patient.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public final class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    private DateUtil() {
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

}
