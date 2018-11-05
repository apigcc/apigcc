package com.github.apiggs.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.apiggs.http.HttpMessage;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * 请求组，如一个folder，一个Controller
 */
@Setter
@Getter
public class Group extends Node {

    boolean rest;
    @JsonIgnore
    Bucket parent;

    List<HttpMessage> nodes = Lists.newLinkedList();

    public List<HttpMessage> getNodes(){
        nodes.sort(Node.COMPARATOR);
        return nodes;
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

}
