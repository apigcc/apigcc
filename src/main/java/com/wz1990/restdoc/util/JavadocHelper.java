package com.wz1990.restdoc.util;

import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JavadocHelper {

    /**
     * 解析javadoc注解
     * @param optional
     * @return
     */
    public static ParsedJavadoc parse(Optional<Javadoc> optional){
        ParsedJavadoc parsedJavadoc = new ParsedJavadoc();
        if(optional.isPresent()){
            Javadoc javadoc = optional.get();
            parseDescription(parsedJavadoc,javadoc.getDescription());
            parseBlockTags(parsedJavadoc,javadoc.getBlockTags());
        }
        return parsedJavadoc;
    }

    private static void parseBlockTags(ParsedJavadoc parsedJavadoc, List<JavadocBlockTag> blockTags) {
        for (int i = 0; i < blockTags.size(); i++) {
            JavadocBlockTag blockTag = blockTags.get(i);
            if("param".equals(blockTag.getTagName()) && blockTag.getName().isPresent()){
                String name = blockTag.getName().get();
                parsedJavadoc.getParams().put(name,blockTag.getContent().toText());
            }
        }
    }

    /**
     * 解析注释描述
     * 第一行解析为名称 name
     * 其他解析为内容 description
     * @param parsedJavadoc
     * @param javadocDescription
     * @return
     */
    public static ParsedJavadoc parseDescription(ParsedJavadoc parsedJavadoc, JavadocDescription javadocDescription){
        String content = getElementsContent(javadocDescription);
        String[] arr = content.split("(\\r\\n)+",2);
        if(arr.length>=1){
            parsedJavadoc.setName(arr[0]);
        }
        if(arr.length>=2){
            parsedJavadoc.setDescription(arr[1]);
        }
        return parsedJavadoc;
    }

    public static String getElementsContent(JavadocDescription javadocDescription){
        StringBuffer stringBuffer = new StringBuffer();
        javadocDescription.getElements().forEach(element->{
            String elementString = element.toText();
            if(Objects.nonNull(elementString)){
                stringBuffer.append(elementString);
            }
        });
        return stringBuffer.toString();
    }

    /**
     * 获取到注解的内容
     * @param optional
     * @return
     */
    public static String getContent(Optional<Comment> optional){
        if(!optional.isPresent()){
            return null;
        }
        Comment comment = optional.get();
        if(comment instanceof JavadocComment){
            JavadocComment javadocComment = (JavadocComment)comment;
            return getElementsContent(javadocComment.parse().getDescription());
        }
        return comment.getContent().trim();
    }

}
