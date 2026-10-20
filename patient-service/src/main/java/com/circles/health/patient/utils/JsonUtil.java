package com.circles.health.patient.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtil {
	
    public static String convertObjectToString(Object object) throws Exception {
        ObjectMapper Obj = new ObjectMapper();
        Obj.registerModule(new JavaTimeModule());
        Obj.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return Obj.writeValueAsString(object);
    }


}
