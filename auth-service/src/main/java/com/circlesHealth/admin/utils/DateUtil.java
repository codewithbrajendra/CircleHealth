package com.circlesHealth.admin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    private DateUtil() {
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    public static LocalDate convertStringIntoLocalDate(String stringDate, DateTimeFormatter formatter) {
    	
    	 LocalDate localDate = LocalDate.parse(stringDate, formatter);
    	 
    	 return localDate;
    }
}
