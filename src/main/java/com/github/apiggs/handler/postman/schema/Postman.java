package com.github.apiggs.handler.postman.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * Rest api schema from postman schema
 * https://schema.getpostman.com/json/collection/v2.1.0/collection.json
 */
public class Postman {

    Info info = new Info();
    List<Folder> item = new ArrayList<>();

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<Folder> getItem() {
        return item;
    }

    public void setItem(List<Folder> item) {
        this.item = item;
    }
}
