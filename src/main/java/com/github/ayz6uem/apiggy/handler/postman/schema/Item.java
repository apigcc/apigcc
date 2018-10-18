package com.github.ayz6uem.apiggy.handler.postman.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Item {

    String id = UUID.randomUUID().toString();
    String name;
    String description;
    Request request = new Request();
    List<Response> response = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

}
