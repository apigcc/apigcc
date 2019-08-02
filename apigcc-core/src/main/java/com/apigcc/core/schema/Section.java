package com.apigcc.core.schema;

import com.apigcc.core.common.helper.StringHelper;
import com.apigcc.core.common.ObjectMappers;
import com.apigcc.core.common.QueryStringBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * 小节，一个请求，封装为一个小节
 */
@Setter
@Getter
public class Section extends Node {

    Method method;
    String uri;
    Map<String,Header> inHeaders = new LinkedHashMap<>();
    ObjectNode pathVariable = ObjectMappers.instance.createObjectNode();
    JsonNode parameter;
    boolean queryParameter = true;
    Map<String,Row> requestRows = new LinkedHashMap<>();

    Map<String,Header> outHeaders = new LinkedHashMap<>();
    JsonNode response;
    Map<String,Row> responseRows = new LinkedHashMap<>();
    Object rawResponse;

    public void addRequestRow(Row row){
        requestRows.put(row.getKey(), row);
    }

    public void addRequestRows(Collection<Row> rows){
        for (Row row : rows) {
            if(row.getKey()!=null && !requestRows.containsKey(row.getKey())){
                requestRows.put(row.getKey(),row);
            }
        }
    }

    public String getRequestLine(){
        StringBuilder builder = new StringBuilder(this.method.toString());
        builder.append(" ").append(this.uri);
        if(this.queryParameter && Objects.equals("GET", this.method)){
            String parameterString = getParameterString();
            if(StringHelper.nonBlank(parameterString)){
                builder.append("?").append(parameterString);
            }
        }
        builder.append(" HTTP/1.1");
        return builder.toString();
    }

    public String getParameterString(){
        if(queryParameter && parameter instanceof ObjectNode){
            return new QueryStringBuilder().append((ObjectNode)parameter).toString();
        }
        return ObjectMappers.pretty(parameter);
    }

    public boolean hasRequestBody(){
        if(Objects.equals("GET",this.method)){
            return false;
        }
        return parameter!=null && parameter.size()>0;
    }

    public void addResponseRow(Row row){
        responseRows.put(row.getKey(), row);
    }

    public void addResponseRows(Collection<Row> rows){
        for (Row row : rows) {
            if(row.getKey()!=null && !responseRows.containsKey(row.getKey())) {
                responseRows.put(row.getKey(), row);
            }
        }
    }

    public boolean hasResponseBody(){
        return response!=null || rawResponse!=null;
    }

    public String getResponseString(){
        if(response!=null){
            return ObjectMappers.pretty(response);
        }
        return String.valueOf(rawResponse);
    }

    public void addInHeader(Header header){
        if (!inHeaders.containsKey(header.getKey())) {
            inHeaders.put(header.getKey(),header);
        }
    }

    public void addOutHeader(Header header){
        if (!outHeaders.containsKey(header.getKey())) {
            outHeaders.put(header.getKey(),header);
        }
    }

}
