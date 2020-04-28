package com.github.apigcc.core.common.postman;

import com.github.apigcc.core.schema.Header;
import com.github.apigcc.core.schema.Method;
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
    List<Header> headers = new ArrayList<>();
    Body body = new Body();

}
