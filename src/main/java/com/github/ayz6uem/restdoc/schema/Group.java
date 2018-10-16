package com.github.ayz6uem.restdoc.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.ayz6uem.restdoc.http.HttpMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求组，如一个folder，一个Controller
 */
public class Group extends Node {

    @JsonIgnore
    Tree parent;

    List<HttpMessage> nodes = new ArrayList<>();
    /**
     * 扩展属性
     * 如：Spring在Controller的RequestMapping，可以存在扩展属性中
     */
    Map<String, Object> ext = new HashMap<>();

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public List<HttpMessage> getNodes() {
        return nodes;
    }

    public void setNodes(List<HttpMessage> nodes) {
        this.nodes = nodes;
    }

    public Map<String, Object> getExt() {
        return ext;
    }

    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }

    public Tree getParent() {
        return parent;
    }

    public void setParent(Tree parent) {
        this.parent = parent;
    }
}
