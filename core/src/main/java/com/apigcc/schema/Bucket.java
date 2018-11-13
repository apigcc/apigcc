package com.apigcc.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Bucket extends Node {

    public Bucket(String name) {
        this.id = name;
        this.name = name;
    }

    List<Group> groups = Lists.newLinkedList();

    @JsonIgnore
    Tree parent;

    public boolean isEmpty() {
        return groups.isEmpty();
    }

    public List<Group> getGroups(){
        groups.sort(COMPARATOR);
        return groups;
    }

}
