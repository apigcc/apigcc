package com.wz1990.restdoc.core;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.wz1990.restdoc.helper.EntityHolder;
import com.wz1990.restdoc.helper.EntityVisitor;
import com.wz1990.restdoc.schema.Tree;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.List;

/**
 * 工具入口类
 */
public class RestDoc {

    @Getter
    Enviroment enviroment;
    @Getter
    Tree tree;
    @Getter
    EntityHolder entityHolder = new EntityHolder();

    private List<ParseResult<CompilationUnit>> parseResults;

    private RestDoc() {
        tree = new Tree();
    }

    public RestDoc(Enviroment enviroment) {
        this();
        this.enviroment = enviroment;
    }

    public RestDoc(String path) {
        this(Enviroment.builder().path(path).build());
    }

    @SneakyThrows
    public RestDoc parse(){
        parseResults = enviroment.getSourceRoot().tryToParse();
        parseEntity();
        parseFramework();
        return this;
    }

    /**
     * 解析实体类结构
     */
    private void parseEntity(){
        parseResults.forEach(result-> result.ifSuccessful(cu-> cu.accept(new EntityVisitor(),entityHolder)));
        entityHolder.linkParent();
    }

    @SneakyThrows
    private void parseFramework(){
        RestArrayVisitor visitor = Framework.currentFramework().visitor.newInstance();
        parseResults.forEach(result-> result.ifSuccessful(cu-> cu.accept(visitor,this)));
    }

}
