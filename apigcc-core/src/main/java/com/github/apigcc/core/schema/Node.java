package com.github.apigcc.core.schema;

import com.github.apigcc.core.Context;
import com.github.apigcc.core.common.helper.StringHelper;
import com.github.javaparser.ast.comments.Comment;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 节点
 */
@Setter
@Getter
public class Node extends NodeWithComment implements Comparable<Node> {

    String id;
    String name;
    String description;
    int index = Context.DEFAULT_NODE_INDEX;

    /**
     * 扩展属性
     * 如：Spring在Controller的RequestMapping，可以存在扩展属性中
     */
    Map<String, Object> ext = new HashMap<>();

    /**
     * 获取参数标签
     * @param id
     * @return
     */
    public Optional<Tag> param(String id) {
        return tag("param:" + id);
    }

    /**
     * 按照index, id, name排序
     * @param other
     * @return
     */
    @Override
    public int compareTo(@Nonnull Node other) {
        if (this.index != other.index) {
            return this.index - other.index;
        }
        if (this.id != null && other.id != null) {
            return this.id.compareTo(other.id);
        }
        if (this.name != null && other.name != null) {
            return this.name.compareTo(other.name);
        }
        return 0;
    }

    @Override
    public void accept(Comment comment) {
        super.accept(comment);
        super.index().ifPresent(this::setIndex);
    }

    /**
     * 解析描述区域
     * 第一行解析为name
     * 其余解析为description
     * @param content
     */
    @Override
    public void setComment(String content) {
        String[] arr = content.split("(\\r\\n)|(\\r)|(\\n)+", 2);
        if (arr.length >= 1 && StringHelper.nonBlank(arr[0])) {
            name = arr[0];
        }
        if (arr.length >= 2 && StringHelper.nonBlank(arr[1])) {
            description = arr[1];
        }
    }
}
