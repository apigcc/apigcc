package com.apigcc.schema;

import com.apigcc.common.ObjectMappers;
import com.apigcc.common.QueryStringBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 小节，一个请求，封装为一个小节
 */
@Setter
@Getter
public class Section extends Node {

    String method;
    List<String> uris = new ArrayList<>();
    List<Header> inHeaders = new ArrayList<>();
    ObjectNode pathVariable = ObjectMappers.instance.createObjectNode();
    JsonNode parameter;
    boolean queryParameter;

    int code;
    String text;
    List<Header> outHeaders = new ArrayList<>();
    JsonNode response;

    public String getParameterString(){
        if(queryParameter && parameter instanceof ObjectNode){
            return new QueryStringBuilder().append((ObjectNode)parameter).toString();
        }
        return ObjectMappers.pretty(parameter);
    }

    public String getResponseString(){
        return ObjectMappers.pretty(response);
    }


}
