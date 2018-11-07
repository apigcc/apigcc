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
 *
 * @see Tree
 * @see Group
 * @see HttpMessage
 */
@Getter
@Setter
public class Node {

    public static Comparator<Node> COMPARATOR = (o1, o2) -> {
        if (o1.index != o2.index) {
            return o1.index - o2.index;
        }
        if (o1.id != null && o2.id != null) {
            return o1.id.compareTo(o2.id);
        }
        if (o1.name != null && o2.name != null) {
            return o1.name.compareTo(o2.name);
        }
        return 0;
    };

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
