package com.github.apiggs.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.apiggs.Apiggs;

import java.util.*;

/**
 * Rest api Tree
 * example:
 * {
 * id:'index',
 * group:'com.github.apiggs',
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
    Apiggs context;

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

    public Apiggs getContext() {
        return context;
    }

    public void setContext(Apiggs context) {
        this.context = context;
    }

}
