package com.github.ayz6uem.restdoc.handler.postman.schema;

import java.util.ArrayList;
import java.util.List;

public class Body{

    BodyMode mode;
    String raw;
    List<Parameter> urlencoded = new ArrayList<>();
    List<Parameter> formdata = new ArrayList<>();

    public BodyMode getMode() {
        return mode;
    }

    public void setMode(BodyMode mode) {
        this.mode = mode;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public List<Parameter> getUrlencoded() {
        return urlencoded;
    }

    public void setUrlencoded(List<Parameter> urlencoded) {
        this.urlencoded = urlencoded;
    }

    public List<Parameter> getFormdata() {
        return formdata;
    }

    public void setFormdata(List<Parameter> formdata) {
        this.formdata = formdata;
    }
}
