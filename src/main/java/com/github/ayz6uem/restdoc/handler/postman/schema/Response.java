package com.github.ayz6uem.restdoc.handler.postman.schema;

import java.util.ArrayList;
import java.util.List;

public class Response{

    String name;
    Request originalRequest ;
    List<Header> header = new ArrayList<>();
    String body;
    String status;
    Integer code;
    String _postman_previewlanguage = "json";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Request getOriginalRequest() {
        return originalRequest;
    }

    public void setOriginalRequest(Request originalRequest) {
        this.originalRequest = originalRequest;
    }

    public List<Header> getHeader() {
        return header;
    }

    public void setHeader(List<Header> header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String get_postman_previewlanguage() {
        return _postman_previewlanguage;
    }

    public void set_postman_previewlanguage(String _postman_previewlanguage) {
        this._postman_previewlanguage = _postman_previewlanguage;
    }
}
