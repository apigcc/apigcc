package com.apigcc.core.common.postman;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class Item extends Folder {

    String id = UUID.randomUUID().toString();
    Request request = new Request();
    List<Response> response = new ArrayList<>();

    public Item() {
        setItem(null);
    }
}
