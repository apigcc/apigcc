package com.apigcc.spring;

import com.apigcc.common.AnnotationHelper;
import com.apigcc.parser.ParserStrategy;
import com.apigcc.schema.Chapter;
import com.apigcc.schema.Section;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class SpringParserStrategy implements ParserStrategy {

    public static final String ANNOTATION_CONTROLLER = "Controller";
    public static final String ANNOTATION_REST_CONTROLLER = "RestController";
    @Override
    public boolean accept(ClassOrInterfaceDeclaration n) {
        return n.isAnnotationPresent(ANNOTATION_REST_CONTROLLER);
    }

    @Override
    public boolean accept(MethodDeclaration n) {
        return AnnotationHelper.isAnnotationPresent(n,RequestMappingHelper.ANNOTATION_REQUEST_MAPPINGS);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Chapter chapter) {
        chapter.getExt().put("uri",RequestMappingHelper.pickUris(n.getAnnotations()));
    }

    @Override
    public void visit(MethodDeclaration n, Section section) {
        section.setMethod(RequestMappingHelper.pickMethod(n));
        section.setUris(RequestMappingHelper.pickUris(n.getAnnotations()));

    }


}
