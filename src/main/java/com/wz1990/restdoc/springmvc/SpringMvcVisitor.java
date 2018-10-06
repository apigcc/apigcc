package com.wz1990.restdoc.springmvc;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.wz1990.restdoc.core.RestArrayVisitor;
import com.wz1990.restdoc.helper.ParsedJavadoc;
import com.wz1990.restdoc.schema.Item;

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
    public void visit(MethodDeclaration n, Item item, ParsedJavadoc parsedJavadoc) {
        //解析注解
        n.getAnnotations().forEach(annotationExpr -> annotationVisitor.visit(annotationExpr, item));
        //解析参数
        n.getParameters().forEach(p -> parameterVisitor.visit(p, item, parsedJavadoc, restDoc));
        //解析返回值
        resultTypeVisitor.visit(n,item,parsedJavadoc,restDoc);
    }
}
