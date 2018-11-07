package com.github.apiggs.ast;

import com.github.apiggs.util.loging.Logger;
import com.github.apiggs.util.loging.LoggerFactory;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.javadoc.description.JavadocDescriptionElement;
import com.github.javaparser.javadoc.description.JavadocInlineTag;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * java注释解析工具
 */
@Setter
@Getter
public class Comments {

    static Logger log = LoggerFactory.getLogger(Comments.class);

    /**
     * 注释第一行
     */
    String name;
    /**
     * 注释后几行
     */
    String description;
    /**
     * 全部注释
     */
    String content;
    /**
     * 标签集合
     */
    List<Tag> tags = new ArrayList<>();

    public static Optional<Comments> of(Optional<Comment> optional) {
        return optional.map(Comments::of);
    }

    public static Comments of(Comment n) {
        Comments comments = new Comments();
        if(n.isJavadocComment()){
            comments.parse(n.asJavadocComment());
        }else{
            comments.setContent(n.getContent());
        }
        return comments;
    }

    public static String getBucketName(Comments comments) {
        for (Tag tag : comments.getTags()) {
            if(Tags.bucket.equals(tag)){
                return tag.getContent();
            }
        }
        return null;
    }

    private void parse(JavadocComment n){
        Javadoc javadoc = n.parse();

        //解析文字描述部分
        this.setContent(parseDescriptions(javadoc.getDescription()));

        //解析标签部分
        for (JavadocBlockTag blockTag : javadoc.getBlockTags()) {
            Tag tag = new Tag();
            tag.name = blockTag.getTagName();
            tag.key = blockTag.getName().isPresent()?blockTag.getName().get():null;
            tag.content = parseDescriptions(blockTag.getContent());
            tags.add(tag);
        }
    }

    private void setContent(String content) {
        if (Objects.isNull(content)) {
            return;
        }
        this.content = content;
        String[] arr = content.split("(\\r\\n)|(\\r)|(\\n)+", 2);
        if (arr.length >= 1) {
            name = arr[0];
        }
        if (arr.length >= 2) {
            description = arr[1];
        }
    }

    /**
     * 判断该节点是否应该忽略
     * 是否在注释中有 @ignore 标签
     * @param node
     * @return
     */
    public static boolean isIgnore(Node node){
        Optional<Comments> optional = of(node.getComment());
        if(optional.isPresent()){
            for (Tag tag : optional.get().tags) {
                if(Objects.equals(Tags.ignore.name(),tag.name)){
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean notIgnore(Node node){
        return !isIgnore(node);
    }

    public static Integer getIndex(Comments comments, int def) {
        try{
            for (Tag tag : comments.getTags()) {
                if(Tags.index.equals(tag)){
                    String indexString = tag.getContent();
                    if(!Strings.isNullOrEmpty(indexString)){
                        return Integer.parseInt(indexString);
                    }
                }
            }
        }catch (Exception ignored){
        }
        return def;
    }

    public static String getCommentFromMethod(Parameter expr) {
        if (expr.getParentNode().isPresent() && expr.getParentNode().get() instanceof MethodDeclaration) {
            MethodDeclaration method = (MethodDeclaration) expr.getParentNode().get();
            Optional<Tag> tagOptional = Comments.getParamTag(method.getComment(), expr.getNameAsString());
            if(tagOptional.isPresent()){
                return tagOptional.get().content;
            }
        }
        return "";
    }

    private static Optional<Tag> getParamTag(Optional<Comment> optional, String name) {
        Optional<Comments> optionalComments = of(optional);
        if(optionalComments.isPresent()){
            Comments comments = optionalComments.get();
            for (Tag tag : comments.tags) {
                if (Objects.equals(tag.name, "param") && Objects.equals(tag.key, name)) {
                    return Optional.of(tag);
                }
            }
        }
        return Optional.empty();
    }

    private static String getTagContent(Optional<Comment> optional, String name) {
        Optional<Comments> optionalComments = of(optional);
        if(optionalComments.isPresent()){
            Comments comments = optionalComments.get();
            for (Tag tag : comments.tags) {
                if (Objects.equals(tag.name, name)) {
                    return tag.content;
                }
            }
        }
        return null;
    }

    private static String parseDescriptions(JavadocDescription description){
        StringBuilder builder = new StringBuilder();
        for (JavadocDescriptionElement element : description.getElements()) {
            if(!(element instanceof  JavadocInlineTag)){
                builder.append(element.toText());
            }
        }
        return builder.toString();
    }

}
