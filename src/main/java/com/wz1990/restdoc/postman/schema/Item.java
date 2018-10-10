package com.wz1990.restdoc.postman.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wz1990.restdoc.ast.AstType;
import com.wz1990.restdoc.util.HttpSchema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Item extends Node {

    Request request = new Request();
    Response response = new Response();

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Request{

        Url url = new Url();
        Method method;
        String description;
        List<Header> header = new ArrayList<>();
        Body body = new Body();
        List<Parameter> pathVariables = new ArrayList<>();

        public void addDescription(String description) {
            if(Objects.isNull(description)){
                return;
            }
            description +=  "\r\n\r\n";
            if (this.description == null) {
                this.description = description;
            } else {
                this.description += description;
            }
        }

    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Url{

        String raw;
        String protocol = "http";
        String host = "{{host}}";
        String path;
        String port;
        List<Parameter> query = new ArrayList<>();
        List<Parameter> pathVariable = new ArrayList<>();

        public String getQueryString(){
            StringBuilder stringBuilder = new StringBuilder();
            Parameter.join(query,stringBuilder);
            if(stringBuilder.length()>0){
                stringBuilder.insert(0,"?");
            }
            return stringBuilder.toString();
        }

    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @NoArgsConstructor
    public static class Header{

        public static final Header APPLICATION_JSON = new Header(HttpSchema.CONTENT_TYPE,HttpSchema.APPLICATION_JSON);
        public static final Header APPLICATION_URLENCODED = new Header(HttpSchema.CONTENT_TYPE,HttpSchema.APPLICATION_URLENCODED);
        public static final Header APPLICATION_FORM_DATA = new Header(HttpSchema.CONTENT_TYPE,HttpSchema.FORM_DATA);

        String key;
        String value;
        String description;

        public Header(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString(){
            return key + ": " + value;
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Body{

        BodyMode mode;
        String raw;
        List<Parameter> rawParameter = new ArrayList<>();
        AstType entity;
        List<Parameter> urlencoded = new ArrayList<>();
        List<Parameter> formdata = new ArrayList<>();
        FileParameter file;

        @JsonIgnore
        public boolean isEmpty(){
            return Objects.isNull(raw) && urlencoded.isEmpty() && formdata.isEmpty();
        }

        public String toString(){
            if(raw!=null){
                return raw;
            }
            StringBuilder stringBuilder = new StringBuilder();
            Parameter.join(urlencoded,stringBuilder);
            Parameter.join(formdata,stringBuilder);
            return stringBuilder.toString();
        }

    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Parameter{

        String key;
        String type;
        Object value;
        String src;
        String description;
        boolean disabled = false;

        public static void join(List<Parameter> parameters, StringBuilder stringBuilder){
            parameters.forEach(parameter -> {
                if(stringBuilder.length()!=0){
                    stringBuilder.append("&");
                }
                stringBuilder.append(parameter.getKey()).append("=").append(parameter.getValue());
            });
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class FileParameter{

        String src;
        String content;

    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Response{

        String name = "example";
        List<Header> header = new ArrayList<>();
        String body;
        List<Parameter> bodyParameter = new ArrayList<>();

        @JsonIgnore
        public boolean isEmpty(){
            return header.isEmpty() && StringUtils.isEmpty(body);
        }

    }


}
