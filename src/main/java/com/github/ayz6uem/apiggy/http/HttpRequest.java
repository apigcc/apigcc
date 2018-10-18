package com.github.ayz6uem.apiggy.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.ayz6uem.apiggy.schema.Cell;
import com.github.ayz6uem.apiggy.util.ObjectMappers;

import java.util.ArrayList;
import java.util.List;

/**
 * a http request
 */
public class HttpRequest {

    HttpRequestMethod method;
    List<String> uris = new ArrayList<>();
    HttpHeaders headers = new HttpHeaders();

    Object body;

    List<Cell> cells = new ArrayList<>();

    public HttpRequestMethod getMethod() {
        return method;
    }

    public void setMethod(HttpRequestMethod method) {
        this.method = method;
    }

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
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

    public Object queryString() {
        if (HttpRequestMethod.GET.equals(method)) {
            return cells.size() > 0 ? "?" + Cell.join(cells) : "";
        }
        return "";
    }

    public boolean hasBody() {
        return !HttpRequestMethod.GET.equals(method) && (body != null || hasParameter());
    }

    public String bodyString() {
        if (getBody() != null && getBody() instanceof JsonNode) {
            return ObjectMappers.toPretty(getBody());
        } else {
            return Cell.join(cells);
        }
    }

    public boolean hasParameter() {
        for (Cell cell : cells) {
            if (!cell.isDisabled()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据请求方法，参数等
     * 设置合理的content-Type
     * 最低是 form_urlencoded，如果已经是json，并不会覆盖
     * @return
     */
    public void checkContentType() {
        if (hasBody()) {
            headers.setContentType(HttpHeaders.ContentType.APPLICATION_X_WWW_FORM_URLENCODED);
        }
    }
}
