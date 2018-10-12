package com.github.ayz6uem.restdoc.ast;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserParameterDeclaration;
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
        String content = getContent(n);
        comments.setContent(content);
        n.ifJavadocComment(javadocComment->{
            Javadoc javadoc = javadocComment.parse();
            comments.parseBlockTags(javadoc.getBlockTags());
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

    public static String getContent(Comment comment){
        if(comment instanceof JavadocComment){
            return getContent(((JavadocComment)comment).parse().getDescription());
        }else{
            return comment.getContent();
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

    public static String getCommentAsString(ResolvedFieldDeclaration declaration){
        Optional<Comment> optional = ((JavaParserFieldDeclaration) declaration).getWrappedNode().getComment();
        return optional.isPresent()?getContent(optional.get()):null;
    }

    public static String getCommentAsString(ResolvedParameterDeclaration declaration) {
        Optional<Comment> optional = ((JavaParserParameterDeclaration)declaration).getWrappedNode().getComment();
        return optional.isPresent()?getContent(optional.get()):null;
    }

    public static String getCommentFromMethod(Parameter expr) {
        if(expr.getParentNode().isPresent()){
            MethodDeclaration method = (MethodDeclaration)expr.getParentNode().get();
            if(method.getComment().isPresent()){
                Comments comments = Comments.of(method.getComment().get());
                return comments.getParams().get(expr.getNameAsString());
            }
        }
        return null;
    }
}
