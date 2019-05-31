package com.apigcc.core.handler.postman.schema;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Rest api schema from postman schema
 * https://schema.getpostman.com/json/collection/v2.1.0/collection.json
 */
@Setter
@Getter
public class Postman {

    Info info = new Info();
    List<Folder> item = new ArrayList<>();

}
