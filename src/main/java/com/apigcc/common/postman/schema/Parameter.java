package com.apigcc.common.postman.schema;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Parameter{

    String key;
    String type;
    Object value;
    String description;
    boolean disabled = false;

}