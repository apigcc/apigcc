package com.github.ayz6uem.apiggy.http;

import com.github.ayz6uem.apiggy.schema.Group;
import com.github.ayz6uem.apiggy.schema.Node;

/**
 * An class that defines a HTTP message,
 * providing common properties and method
 */
public class HttpMessage extends Node {

    HttpVersion version = HttpVersion.DEFAULT;
    HttpRequest request = new HttpRequest();
    HttpResponse response = new HttpResponse();

    Group parent;

    public HttpVersion getVersion() {
        return version;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }
}
