package com.wz1990.restdoc;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;
import com.wz1990.restdoc.ast.AstTypeHolder;
import com.wz1990.restdoc.postman.RestDocJsonBuilder;
import com.wz1990.restdoc.schema.Tree;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * 工具入口类、上下文
 */
@Getter
@Setter
public class RestDoc {

    Enviroment enviroment;
    Tree tree;
    AstTypeHolder entityHolder = new AstTypeHolder();

    private List<ParseResult<CompilationUnit>> parseResults;

    private RestDoc() {
        tree = new Tree();
    }

    public RestDoc(Enviroment enviroment) {
        this();
        this.enviroment = enviroment;
    }

    public RestDoc(String root) {
        this(Enviroment.builder().source(root).build());
    }

    @SneakyThrows
    public RestDoc parse() {
        //是否使用责任链？
        Objects.requireNonNull(enviroment.getSource(),"source can not be null");
        SourceRoot sourceRoot = new SourceRoot(Paths.get(enviroment.getSource()));
        parseResults = sourceRoot.tryToParse();

        parseEntity();

        parseFramework();
        return this;
    }

    /**
     * 解析实体类结构
     */
    private void parseEntity() {
//        parseResults.forEach(result -> result.ifSuccessful(cu -> cu.accept(new EntityVisitor(), entityHolder)));
//        entityHolder.linkParent();
    }

    @SneakyThrows
    private void parseFramework() {
        RestArrayVisitor visitor = Framework.currentFramework().visitor.newInstance();
        parseResults.forEach(result -> result.ifSuccessful(cu -> cu.accept(visitor, this)));
    }

    public RestDoc buildJson() {
        new RestDocJsonBuilder(tree, enviroment.getJsonFile()).build();
        return this;
    }

    public RestDoc buildAdoc() {
        new RestDocMarkupBuilder(tree, enviroment.getAdocPath()).build();
        return this;
    }

    public RestDoc buildRestdoc() {
        new RestDocHtmlBuilder(enviroment).build();
        return this;
    }

}
