package com.apigcc.schema;

import com.apigcc.common.CommentHelper;
import com.apigcc.core.Context;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.javadoc.Javadoc;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Setter
@Getter
public class Node implements Comparable<Node> {

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
     * javadoc 中的tag
     */
    Map<String, Tag> tags = new HashMap<>();

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

    public void accept(Comment comment) {
        if (!comment.isJavadocComment()) {
            setNameAndDescription(comment.getContent());
            return;
        }
        Javadoc javadoc = comment.asJavadocComment().parse();
        setNameAndDescription(CommentHelper.getDescription(javadoc.getDescription()));

        javadoc.getBlockTags().forEach(blockTag -> {
            Tag tag = new Tag();
            tag.id = blockTag.getTagName();
            tag.key = blockTag.getName().isPresent() ? blockTag.getName().get() : null;
            tag.content = CommentHelper.getDescription(blockTag.getContent());
            putTag(tag.id, tag);
        });

    }

    public void setNameAndDescription(String content) {
        String[] arr = content.split("(\\r\\n)|(\\r)|(\\n)+", 2);
        if (arr.length >= 1) {
            name = arr[0];
        }
        if (arr.length >= 2) {
            description = arr[1];
        }
    }

    public Optional<Tag> getTag(String id) {
        return Optional.ofNullable(tags.get(id));
    }

    public void putTag(String id, Tag tag) {
        tags.put(id, tag);
    }
}
