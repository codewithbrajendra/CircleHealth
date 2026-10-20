package com.circles.health.doctor.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtil {

    public static String convertObjectToString(Object object) throws Exception {
        ObjectMapper Obj = new ObjectMapper();
        return Obj.writeValueAsString(object);
    }
}
