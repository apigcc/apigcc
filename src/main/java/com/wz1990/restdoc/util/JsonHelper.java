package com.wz1990.restdoc.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wz1990.restdoc.ast.AstType;
import lombok.SneakyThrows;

import java.util.Objects;

public class JsonHelper {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectWriter getPrettyObjectWriter(){
        return objectMapper.writerWithDefaultPrettyPrinter();
    }

    public static ArrayNode toArray(Object value){
        ArrayNode arrayNode = objectMapper.createArrayNode();
        add(arrayNode,value);
        return arrayNode;
    }

    @SneakyThrows
    public static String toArrayPretty(Object value){
        return getPrettyObjectWriter().writeValueAsString(toArray(value));
    }

    public static void add(ArrayNode arrayNode, Object value){
        if(value instanceof AstType){
            AstType v = (AstType)value;
            arrayNode.add(v.getJsonNode());
        }
        if(value instanceof String){
            String v = (String)value;
            arrayNode.add(v);
        }
        if(value instanceof Integer){
            Integer v = (Integer)value;
            arrayNode.add(v);
        }
    }

    public static void set(ObjectNode objectNode, String key, Object value){
        if(Objects.isNull(value)){
            return;
        }
        if(value instanceof AstType){
            AstType v = (AstType)value;
            objectNode.set(key,v.getJsonNode());
        }
        if(value instanceof String){
            String v = (String)value;
            objectNode.put(key,v);
        }
        if(value instanceof Integer){
            Integer v = (Integer)value;
            objectNode.put(key,v);
        }
    }

    @SneakyThrows
    public static String toPretty(Object node) {
        return getPrettyObjectWriter().writeValueAsString(node);
    }
}
