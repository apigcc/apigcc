package com.github.apigcc.core.schema;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.TreeSet;

@Setter
@Getter
public class Book extends Node {

    public static final String DEFAULT = "index";

    Set<Chapter> chapters = new TreeSet<>();

    public Book(String id) {
        this.id = id;
    }
}
