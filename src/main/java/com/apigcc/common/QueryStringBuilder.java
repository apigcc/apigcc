package com.apigcc.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;

public class QueryStringBuilder {

    private StringBuilder builder = new StringBuilder();

    public QueryStringBuilder append(Object key, Object value){
        if(builder.length()>0){
            builder.append("&");
        }
        builder.append(key);
        builder.append("=");
        builder.append(value);
        return this;
    }

    public QueryStringBuilder append(ObjectNode objectNode){
        Iterator<String> iterator = objectNode.fieldNames();
        while (iterator.hasNext()){
            String key = iterator.next();
            JsonNode valueNode = objectNode.get(key);
            String value = valueNode.isTextual()?valueNode.asText():valueNode.toString();
            append(key,value);
        }
        return this;
    }

    public String toString(){
        return builder.toString();
    }



}
