package com.github.apiggs.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.apiggs.util.ObjectMappers;
import com.github.apiggs.util.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HttpResponse {

    HttpResponseStatus status = HttpResponseStatus.DEFAULT;
    HttpHeaders headers = new HttpHeaders();
    Object body;

    List<Cell<String>> cells = new ArrayList<>();

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

    public List<Cell<String>> getCells() {
        return cells;
    }

    public boolean hasBody(){
        return body != null;
    }

    public String bodyString(){
        if(getBody()!=null && getBody() instanceof JsonNode){
            return ObjectMappers.toPretty(getBody());
        }
        return "";
    }

}
