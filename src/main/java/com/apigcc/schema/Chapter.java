package com.apigcc.schema;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.TreeSet;

/**
 * 章，一个类解析为一章
 */
@Setter
@Getter
public class Chapter extends Node {

    String bookName;

    Set<Section> sections = new TreeSet<>();

}
