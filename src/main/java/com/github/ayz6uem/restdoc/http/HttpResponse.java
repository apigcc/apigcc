package com.github.ayz6uem.restdoc.http;

import com.github.ayz6uem.restdoc.schema.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HttpResponse {

    HttpResponseStatus status = HttpResponseStatus.DEFAULT;
    HttpHeaders headers = new HttpHeaders();
    Object body;

    List<Cell> cells = new ArrayList<>();

    public boolean isEmpty(){
        return cells.isEmpty() && Objects.isNull(body)  && headers.isEmpty();
    }


    public HttpResponseStatus getStatus() {
        return status;
    }

    public void setStatus(HttpResponseStatus status) {
        this.status = status;
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
