package com.github.apigcc.core.common.helper;

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

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 注释解析
 */
public class CommentHelper {

    /**
     * 解析属性的注释
     * @param it
     * @return
     */
    public static Optional<Comment> getComment(ResolvedFieldDeclaration it) {
        if (it instanceof JavaParserFieldDeclaration) {
            FieldDeclaration wrappedNode = ((JavaParserFieldDeclaration) it).getWrappedNode();
            return wrappedNode.getComment();
        }
        if (it instanceof JavaParserClassDeclaration) {
            JavaParserClassDeclaration classDeclaration = (JavaParserClassDeclaration) it;
            return classDeclaration.getWrappedNode().getComment();
        }
        return Optional.empty();
    }

    /**
     * 获取注释完整字符串
     *
     * @param description
     * @return
     */
    public static String getDescription(JavadocDescription description) {
        return description.getElements()
                .stream()
                .filter(e -> !(e instanceof JavadocInlineTag))
                .map(JavadocDescriptionElement::toText).collect(Collectors.joining());
    }

}
