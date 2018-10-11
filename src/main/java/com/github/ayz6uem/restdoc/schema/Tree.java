package com.github.ayz6uem.restdoc.schema;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Rest api Tree
 * example:
 * {
 *     id:'restdoc',
 *     group:'com.wz1990.doc',
 *     version:'1.0',
 *     name:'接口文档',
 *     description:'接口描述'
 * }
 */
@Getter
@Setter
public class Tree extends Node{

    String group;
    String version;
    List<Node> nodes = new ArrayList<>();

}
