package com.github.ayz6uem.apiggy.schema;

/**
 * Tree Group HttpMessage继承Node，已方便在visit中传播
 * @see Tree
 * @see Group
 * @see com.github.ayz6uem.apiggy.http.HttpMessage
 */
public class Node {

    String id;
    String name;
    String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
