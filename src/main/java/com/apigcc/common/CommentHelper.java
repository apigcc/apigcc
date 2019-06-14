package com.apigcc.common;

import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.javadoc.description.JavadocDescriptionElement;
import com.github.javaparser.javadoc.description.JavadocInlineTag;

import java.util.stream.Collectors;

public class CommentHelper {

    public static String getDescription(JavadocDescription description){
        return description.getElements()
                .stream()
                .filter(e -> !(e instanceof JavadocInlineTag))
                .map(JavadocDescriptionElement::toText).collect(Collectors.joining());
    }

}
