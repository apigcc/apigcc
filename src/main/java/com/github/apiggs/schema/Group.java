package com.github.apiggs.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.apiggs.ast.Comments;
import com.github.apiggs.http.HttpMessage;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 请求组，如一个folder，一个Controller
 */
@Setter
@Getter
public class Group extends Node {

    boolean rest;
    String bucketName;

    List<HttpMessage> nodes = Lists.newLinkedList();

    @JsonIgnore
    Bucket parent;

    public List<HttpMessage> getNodes(){
        nodes.sort(COMPARATOR);
        return nodes;
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public void accept(Comments comments) {
        super.accept(comments);
        bucketName = Comments.getBucketName(comments);
    }
}
