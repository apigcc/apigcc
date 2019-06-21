package com.apigcc.parser;

import com.apigcc.schema.Chapter;
import com.apigcc.schema.Section;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public interface ParserStrategy {

    /**
     * 判断是否为需要解析的类
     *
     * @param n
     * @return
     */
    boolean accept(ClassOrInterfaceDeclaration n);

    /**
     * 判断是否为需要解析的方法
     *
     * @param n
     * @return
     */
    boolean accept(MethodDeclaration n);

    void visit(MethodDeclaration n, Chapter chapter, Section section);

    void visit(ClassOrInterfaceDeclaration n, Chapter chapter);
}
