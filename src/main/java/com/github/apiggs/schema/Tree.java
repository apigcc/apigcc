package com.github.apiggs.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    String readme;
    List<Appendix> appendices = new ArrayList<>();

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

    public List<Appendix> getAppendices() {
        return appendices;
    }

    public String getReadme() {
        return readme;
    }

    public void setReadme(String readme) {
        this.readme = readme;
    }
}
