package com.apigcc.handler.postman.schema;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Response{

    String name;
    Request originalRequest ;
    List<Header> header = new ArrayList<>();
    String body;
    String status;
    Integer code;
    String _postman_previewlanguage = "json";

}
