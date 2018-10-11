package com.github.ayz6uem.restdoc.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.util.Objects;

public class ObjectMappers {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper instance(){
        return objectMapper;
    }

    public static ObjectWriter getPrettyObjectWriter(){
        return objectMapper.writerWithDefaultPrettyPrinter();
    }

    @SneakyThrows
    public static String toPretty(Object node) {
        return getPrettyObjectWriter().writeValueAsString(node);
    }
}
