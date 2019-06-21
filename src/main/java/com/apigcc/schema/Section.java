package com.apigcc.schema;

import com.apigcc.common.ObjectMappers;
import com.apigcc.common.QueryStringBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * 小节，一个请求，封装为一个小节
 */
@Setter
@Getter
public class Section extends Node {

    String method;
    String uri;
    List<Header> inHeaders = new ArrayList<>();
    ObjectNode pathVariable = ObjectMappers.instance.createObjectNode();
    JsonNode parameter;
    boolean queryParameter = true;
    Map<String,Row> requestRows = new LinkedHashMap<>();

    List<Header> outHeaders = new ArrayList<>();
    JsonNode response;
    Map<String,Row> responseRows = new LinkedHashMap<>();

    public String getParameterString(){
        if(queryParameter && parameter instanceof ObjectNode){
            return new QueryStringBuilder().append((ObjectNode)parameter).toString();
        }
        return ObjectMappers.pretty(parameter);
    }

    public String getResponseString(){
        if(response!=null){
            return ObjectMappers.pretty(response);
        }
        return "";
    }

    public void addRequestRows(Collection<Row> rows){
        for (Row row : rows) {
            if(row.getKey()!=null){
                requestRows.put(row.getKey(),row);
            }
        }
    }

    public void addResponseRows(Collection<Row> rows){
        for (Row row : rows) {
            if(row.getKey()!=null) {
                responseRows.put(row.getKey(), row);
            }
        }
    }

}
