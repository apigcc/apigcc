package com.github.apiggs.ast;

import com.github.apiggs.ast.extend.DocTag;
import com.github.apiggs.util.loging.Logger;
import com.github.apiggs.util.loging.LoggerFactory;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescriptionElement;
import com.github.javaparser.javadoc.description.JavadocInlineTag;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;

import java.util.*;

/**
 * java注释解析工具
 */
public class Comments {

    static Logger log = LoggerFactory.getLogger(Comments.class);

    /**
     * 注释第一行
     */
    public String name;
    /**
     * 注释后几行
     */
    public String description;
    /**
     * 全部注释
     */
    public String content;
    /**
     * 标签集合
     */
    public List<Tag> tags = new ArrayList<>();
    /**
     * return标签
     */
    public Tag returnTag;

    public static class Tag{
        public String name;
        public String key;
        public String content;
        public String link;
        public Map<String, String> inline = new HashMap<>();
    }

    public static Optional<Comments> of(Optional<Comment> optional) {
        return optional.map(Comments::of);
    }

    public static Comments of(Comment n) {
        Comments comments = new Comments();
        comments.parse(n);
        return comments;
    }

    private void parse(Comment n){
        if(n.isJavadocComment()){
            parse(n.asJavadocComment());
        }else{
            this.setContent(n.getContent());
        }
    }

    private void parse(JavadocComment n){
        //解析文字描述部分
        StringBuilder builder = new StringBuilder();
        Javadoc javadoc = n.parse();
        for (JavadocDescriptionElement element : javadoc.getDescription().getElements()) {
            builder.append(element.toText());
        }
        this.setContent(builder.toString());

        //解析标签部分
        for (JavadocBlockTag blockTag : javadoc.getBlockTags()) {

            Tag tag = new Tag();
            tag.name = blockTag.getTagName();
            tag.key = blockTag.getName().isPresent()?blockTag.getName().get():null;
            builder = new StringBuilder();
            for (JavadocDescriptionElement element : blockTag.getContent().getElements()) {
                if(element instanceof  JavadocInlineTag){
                    JavadocInlineTag inlineTag = (JavadocInlineTag) element;
                    if(inlineTag.getType().equals(JavadocInlineTag.Type.LINK)){
                        tag.link = inlineTag.getContent();
                    }else{
                        tag.inline.put(inlineTag.getName(), inlineTag.getContent());
                    }
                }else{
                    builder.append(element.toText());
                }
            }
            tag.content = builder.toString();
            tags.add(tag);

            if("return".equals(tag.name)){
                returnTag = tag;
            }
        }
    }

    public static String getTagContent(Optional<Comment> optional, String name) {
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

    public static Optional<Tag> getParamTag(Optional<Comment> optional, String name) {
        Optional<Comments> optionalComments = of(optional);
        if(optionalComments.isPresent()){
            Comments comments = optionalComments.get();
            for (Tag tag : comments.tags) {
                if (Objects.equals(tag.name, "param")) {
                    return Optional.of(tag);
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<Integer> getIndexTag(Optional<Comment> optional) {
        String indexString = getTagContent(optional, DocTag.index.toString());
        if(indexString!=null){
            try{
                return Optional.of(Integer.parseInt(indexString));
            }catch (Exception e){
                log.debug("read index fail:{}",indexString);
            }
        }
        return Optional.empty();
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

    public static String getCommentAsString(ResolvedFieldDeclaration declaration) {
        if (!(declaration instanceof JavaParserFieldDeclaration)) {
            return null;
        }
        Optional<Comment> optional = ((JavaParserFieldDeclaration) declaration).getWrappedNode().getComment();
        return of(optional).map(comments -> comments.content).orElse(null);
    }

    public static String getCommentFromMethod(Parameter expr) {
        if (expr.getParentNode().isPresent()) {
            MethodDeclaration method = (MethodDeclaration) expr.getParentNode().get();
            Optional<Tag> tagOptional = Comments.getParamTag(method.getComment(), expr.getNameAsString());
            if(tagOptional.isPresent()){
                return tagOptional.get().content;
            }
        }
        return null;
    }

}
