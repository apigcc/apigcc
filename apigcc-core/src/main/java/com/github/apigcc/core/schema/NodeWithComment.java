package com.github.apigcc.core.schema;

import com.github.apigcc.core.common.helper.CommentHelper;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 可被注释的对象
 * 可解析为tag的对象
 */
@Slf4j
public abstract class NodeWithComment {

    /**
     * javadoc 中的tag
     */
    protected Map<String, Tag> tags = new HashMap<>();

    /**
     * 解析注释
     * @param comment
     */
    public void accept(Comment comment) {
        if (!comment.isJavadocComment()) {
            this.setComment(comment.getContent());
            return;
        }
        Javadoc javadoc = comment.asJavadocComment().parse();
        this.setComment(CommentHelper.getDescription(javadoc.getDescription()));

        javadoc.getBlockTags().forEach(this::parse);
    }

    /**
     * 解析并保存blockTag
     * @param blockTag
     */
    private void parse(JavadocBlockTag blockTag) {
        Tag tag = new Tag(blockTag);
        tags.put(tag.getCompositeId(), tag);
    }

    /**
     * 查找注释标签
     * @param id
     * @return
     */
    public Optional<Tag> tag(String id) {
        return Optional.ofNullable(tags.get(id));
    }

    /**
     * 是否被@ignore 标记
     * @return
     */
    public boolean isIgnore() {
        return tag("ignore").isPresent();
    }

    /**
     * 获取index标签的值
     * @return
     */
    public Optional<Integer> index(){
        Optional<Tag> optional = tag("index");
        if(optional.isPresent()){
            Tag tag = optional.get();
            String content = tag.getContent();
            try{
                return Optional.of(Integer.parseInt(content));
            }catch (Exception e){
                log.warn(content+" parse error");
            }
        }
        return Optional.empty();
    }

    /**
     * 设置注释内容
     * @param content
     */
    public abstract void setComment(String content);

}
