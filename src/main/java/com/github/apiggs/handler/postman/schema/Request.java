package com.github.apiggs.handler.postman.schema;

import java.util.ArrayList;
import java.util.List;

public class Request{

    Url url = new Url();
    Method method;
    String description;
    List<Header> header = new ArrayList<>();
    Body body = new Body();

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
