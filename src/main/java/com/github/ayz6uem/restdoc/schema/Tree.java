package com.github.ayz6uem.restdoc.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.ayz6uem.restdoc.RestDoc;

import java.util.*;

/**
 * Rest api Tree
 * example:
 * {
 * id:'restdoc',
 * group:'com.github.ayz6uem',
 * version:'1.0',
 * name:'接口文档',
 * description:'接口描述'
 * }
 */
public class Tree extends Node {

    String realm;
    String version;
    List<Group> groups = new ArrayList<>();

    @JsonIgnore
    RestDoc context;

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public RestDoc getContext() {
        return context;
    }

    public void setContext(RestDoc context) {
        this.context = context;
    }

}
