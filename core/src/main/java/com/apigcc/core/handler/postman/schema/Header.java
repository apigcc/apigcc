package com.apigcc.core.handler.postman.schema;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Header{

    String key;
    String value;
    String description;

    public Header(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
