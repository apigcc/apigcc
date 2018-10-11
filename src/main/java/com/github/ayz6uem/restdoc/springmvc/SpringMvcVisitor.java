package com.github.ayz6uem.restdoc.springmvc;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.ayz6uem.restdoc.RestArrayVisitor;
import com.github.ayz6uem.restdoc.schema.Group;

/**
 * Spring Mvc Endpoint解析
 * 解析类方法，方法参数，方法返回值
 * 解析的几种种类
 * 1、PathVariable
 * 2、QueryString
 * 3、包装成类的QueryString
 * 4、RequestBody
 * 5、集合类参数、数组类参数
 */
public class SpringMvcVisitor extends RestArrayVisitor {

    private SpringAnnotationVisitor annotationVisitor = new SpringAnnotationVisitor();

    private SpringParameterVisitor parameterVisitor = new SpringParameterVisitor();

    private SpringResultTypeVisitor resultTypeVisitor = new SpringResultTypeVisitor();

    @Override
    public boolean accept(ClassOrInterfaceDeclaration n) {
        return annotationVisitor.accept(n);
    }

    @Override
    public boolean accept(MethodDeclaration n) {
        return annotationVisitor.accept(n);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Group group) {
//        n.getAnnotations().forEach(annotationExpr -> annotationVisitor.visit(annotationExpr, group));
    }

//    @Override
//    public void visit(MethodDeclaration n, Item item, ParsedJavadoc parsedJavadoc) {
//        n.getAnnotations().forEach(annotationExpr -> annotationVisitor.visit(annotationExpr, item));
//        n.getParameters().forEach(p -> parameterVisitor.visit(p, item, parsedJavadoc, restDoc));
//        resultTypeVisitor.visit(n,item,parsedJavadoc,restDoc);
//    }
}
