package com.github.ayz6uem.restdoc.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class ObjectMappers {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ObjectMapper instance(){
        return objectMapper;
    }

    public static ObjectWriter getPrettyObjectWriter(){
        return objectMapper.writerWithDefaultPrettyPrinter();
    }

    public static String toPretty(Object node) {
        try {
            return getPrettyObjectWriter().writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
