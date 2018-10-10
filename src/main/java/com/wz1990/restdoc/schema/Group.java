package com.wz1990.restdoc.schema;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求组，如一个folder，一个Controller
 */
@Getter
@Setter
public class Group extends Node {

    List<Node> nodes = new ArrayList<>();
    /**
     * 扩展属性
     * 如：Spring在Controller的RequestMapping，可以存在扩展属性中
     */
    Map<String,Object> ext = new HashMap<>();

}
