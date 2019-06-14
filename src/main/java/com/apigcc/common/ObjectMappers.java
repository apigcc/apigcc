package com.apigcc.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ObjectMappers {

    public static final ObjectMapper instance;

    static {
        instance = new ObjectMapper();
        instance.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String pretty(Object node) {
        try {
            return instance.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
