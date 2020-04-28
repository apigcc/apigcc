package com.github.apigcc.core.parser;

import com.github.apigcc.core.schema.Chapter;
import com.github.apigcc.core.schema.Section;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public interface ParserStrategy {

    /**
     * 解析策略名称
     * @return
     */
    String name();

    /**
     * 加载策略所需的解析组件
     */
    void onLoad();

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
