package com.terragene.baxen.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectToJsonConverter {


    public static String convert(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;

        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // Manejar excepciones según corresponda
            e.printStackTrace();
        }

        return jsonString;
    }
}
