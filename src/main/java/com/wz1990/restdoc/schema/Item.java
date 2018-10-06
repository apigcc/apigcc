package com.wz1990.restdoc.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wz1990.restdoc.helper.Entity;
import com.wz1990.restdoc.helper.HttpSchema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Body{

        BodyMode mode;
        String raw;
        Entity entity;
        List<Parameter> urlencoded = new ArrayList<>();
        List<Parameter> formdata = new ArrayList<>();
        FileParameter file;

    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Parameter{

        String key;
        String type;
        String value;
        String src;
        String description;
        boolean disabled = false;

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

    }


}
