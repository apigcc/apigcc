package com.apigcc.common.postman.schema;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Request{

    Url url = new Url();
    Method method;
    String description;
    List<Header> header = new ArrayList<>();
    Body body = new Body();

}
