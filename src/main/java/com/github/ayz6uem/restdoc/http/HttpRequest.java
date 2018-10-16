package com.github.ayz6uem.restdoc.http;

import com.github.ayz6uem.restdoc.schema.Cell;

import java.util.ArrayList;
import java.util.List;

/**
 * a http request
 */
public class HttpRequest {

    HttpRequestMethod method;
    String uri;
    HttpHeaders headers = new HttpHeaders();

    Object body;

    List<Cell> cells = new ArrayList<>();

    public HttpRequestMethod getMethod() {
        return method;
    }

    public void setMethod(HttpRequestMethod method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }
}
