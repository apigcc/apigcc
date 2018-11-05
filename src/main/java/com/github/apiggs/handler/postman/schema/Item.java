package com.github.apiggs.handler.postman.schema;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class Item {

    String id = UUID.randomUUID().toString();
    String name;
    String description;
    Request request = new Request();
    List<Response> response = new ArrayList<>();


}
