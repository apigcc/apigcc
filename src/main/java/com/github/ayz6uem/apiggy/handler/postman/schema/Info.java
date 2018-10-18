package com.github.ayz6uem.apiggy.handler.postman.schema;

public class Info {

    String name;
    String _postman_id;
    String description;
    String version;
    String schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_postman_id() {
        return _postman_id;
    }

    public void set_postman_id(String _postman_id) {
        this._postman_id = _postman_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
