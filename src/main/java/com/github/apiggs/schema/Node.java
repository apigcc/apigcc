package com.github.apiggs.schema;

import com.github.apiggs.Environment;
import com.github.apiggs.http.HttpMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Tree Group HttpMessage继承Node，已方便在visit中传播
 * @see Tree
 * @see Group
 * @see HttpMessage
 */
@Getter
@Setter
public class Node {

    public static Comparator<Node> COMPARATOR = Comparator.comparingInt(o -> o.index);

    String id;
    String name;
    String description;
    int index = Environment.DEFAULT_NODE_INDEX;
    /**
     * 扩展属性
     * 如：Spring在Controller的RequestMapping，可以存在扩展属性中
     */
    Map<String, Object> ext = new HashMap<>();

}
