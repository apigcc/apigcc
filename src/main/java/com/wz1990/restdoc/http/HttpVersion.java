package com.wz1990.restdoc.http;

public enum HttpVersion {

    V_1_0("HTTP/1.0"),V_1_1("HTTP/1.1");

    private String text;

    HttpVersion(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
