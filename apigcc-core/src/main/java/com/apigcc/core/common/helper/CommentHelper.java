package com.apigcc.core.common.helper;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.javadoc.description.JavadocDescriptionElement;
import com.github.javaparser.javadoc.description.JavadocInlineTag;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserClassDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserFieldDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;

import java.util.Optional;
import java.util.stream.Collectors;

public class CommentHelper {

    /**
     * 获取完整注释字符串
     * @param description
     * @return
     */
    public static String getDescription(JavadocDescription description){
        return description.getElements()
                .stream()
                .filter(e -> !(e instanceof JavadocInlineTag))
                .map(JavadocDescriptionElement::toText).collect(Collectors.joining());
    }

    public static String getContent(Comment comment){
        if(!comment.isJavadocComment()){
            return comment.getContent();
        }
        return getDescription(comment.asJavadocComment().parse().getDescription());
    }


    public static String getComment(MethodUsage it){
        if (it.getDeclaration() instanceof JavaParserMethodDeclaration) {
            MethodDeclaration wrappedNode = ((JavaParserMethodDeclaration) it.getDeclaration()).getWrappedNode();
            Optional<Comment> optional = wrappedNode.getComment();
            if(optional.isPresent()){
                return CommentHelper.getContent(optional.get());
            }
        }
        return null;
    }
    public static String getComment(ResolvedFieldDeclaration it){
        if(it instanceof JavaParserFieldDeclaration){
            FieldDeclaration wrappedNode = ((JavaParserFieldDeclaration) it).getWrappedNode();
            Optional<Comment> optional = wrappedNode.getComment();
            if(optional.isPresent()){
                return CommentHelper.getContent(optional.get());
            }
        }else if(it instanceof JavaParserClassDeclaration){
            JavaParserClassDeclaration classDeclaration = (JavaParserClassDeclaration) it;

        }
        return null;
    }
}
