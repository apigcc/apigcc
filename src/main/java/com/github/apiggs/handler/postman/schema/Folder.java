package com.github.apiggs.handler.postman.schema;

import java.util.ArrayList;
import java.util.List;

public class Folder {

    String name;
    String description;
    List<Item> item = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }
}
