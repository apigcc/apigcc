package com.github.ayz6uem.restdoc.ast;

import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class Comments {

    String name;
    String description;
    Map<String,String> params = new HashMap<>();

    public static Comments of(Comment n){
        Comments comments = new Comments();
        n.ifJavadocComment(javadocComment->{
            Javadoc javadoc = javadocComment.parse();
            String content = getContent(javadoc.getDescription());
            comments.setContent(content);
            comments.parseBlockTags(javadoc.getBlockTags());
        });
        n.ifBlockComment(blockComment -> {
            comments.setContent(blockComment.getContent());
        });
        n.ifLineComment(lineComment -> {
            comments.setContent(lineComment.getContent());
        });

        return comments;
    }

    private void setContent(String content){
        if(Objects.isNull(content)){
            return;
        }
        String[] arr = content.split("(\\r\\n)+",2);
        if(arr.length>=1){
            setName(arr[0]);
        }
        if(arr.length>=2){
            setDescription(arr[1]);
        }
    }

    private void parseBlockTags(List<JavadocBlockTag> blockTags) {
        for (int i = 0; i < blockTags.size(); i++) {
            JavadocBlockTag blockTag = blockTags.get(i);
            if("param".equals(blockTag.getTagName())
                    && blockTag.getName().isPresent()
                    && !blockTag.getContent().isEmpty()){
                String name = blockTag.getName().get();
                params.put(name,getContent(blockTag.getContent()));
            }
        }
    }

    private static String getContent(JavadocDescription javadocDescription){
        StringBuilder sb = new StringBuilder();
        javadocDescription.getElements().forEach(element->{
            String elementString = element.toText();
            if(Objects.nonNull(elementString)){
                sb.append(elementString);
            }
        });
        return sb.toString();
    }

}
