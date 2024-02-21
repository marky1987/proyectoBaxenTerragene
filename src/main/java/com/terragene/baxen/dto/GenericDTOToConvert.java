package com.terragene.baxen.dto;

import java.util.HashMap;
import java.util.Map;

public class GenericDTOToConvert {
    private Map<String, Object> fields;

    public GenericDTOToConvert() {
        fields = new HashMap<>();
    }

    public Object getField(String fieldName) {
        return fields.get(fieldName);
    }

    public void setField(String fieldName, Object value) {
        fields.put(fieldName, value);
    }
}
