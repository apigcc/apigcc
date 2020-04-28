package com.github.apigcc.core.schema;

import com.github.apigcc.core.common.helper.CommentHelper;
import com.github.apigcc.core.common.helper.StringHelper;
import com.github.javaparser.javadoc.JavadocBlockTag;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * //@param username 用户名
 *    id    key      content
 */
@Slf4j
@Setter
@Getter
public class Tag {

    String id;
    String key;
    String content;

    public Tag(JavadocBlockTag blockTag){
        setId(blockTag.getTagName());
        setKey(blockTag.getName().isPresent() ? blockTag.getName().get() : "");
        setContent(CommentHelper.getDescription(blockTag.getContent()));
    }

    public String getCompositeId(){
        String id = getId();
        if (StringHelper.nonBlank(getKey())) {
            id += ":" + getKey();
        }
        return id;
    }

}
