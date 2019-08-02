package com.apigcc.core.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;

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

    public static ObjectNode merge(ObjectNode result, ObjectNode... nodes) {
        if (result != null) {
            for (ObjectNode node : nodes) {
                Iterator<String> iterator = node.fieldNames();
                while (iterator.hasNext()){
                    String key = iterator.next();
                    result.set(key,node.get(key));
                }
            }
        }
        return result;
    }
}
