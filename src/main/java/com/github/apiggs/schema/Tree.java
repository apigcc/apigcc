package com.github.apiggs.schema;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Rest api Tree
 * example:
 * {
 * id:'index',
 * group:'com.github.apiggs',
 * version:'1.0',
 * name:'接口文档',
 * description:'接口描述'
 * }
 */
@Getter
@Setter
public class Tree extends Node {

    String version;
    String readme;
    /**
     * 默认的桶
     */
    Bucket bucket = new Bucket(Bucket.DEFAULT_BUCKET);
    /**
     * 其他的桶
     */
    Map<String,Bucket> buckets = Maps.newTreeMap(String::compareTo);
    List<Appendix> appendices = new LinkedList<>();

    public List<Appendix> getAppendices(){
        appendices.sort(Node.COMPARATOR);
        return appendices;
    }

}
