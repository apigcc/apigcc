package com.apigcc.core.handler.postman.schema;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Folder {

    String name;
    String description;
    List<Item> item = new ArrayList<>();

}
