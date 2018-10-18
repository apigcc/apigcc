package com.github.apiggs.handler.postman.schema;

import java.util.ArrayList;
import java.util.List;

public class Url{

    String raw;
    String protocol = "http";
    String host = "{{host}}";
    String path;
    String port;
    List<Parameter> query = new ArrayList<>();

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<Parameter> getQuery() {
        return query;
    }

    public void setQuery(List<Parameter> query) {
        this.query = query;
    }
}
