package com.github.ayz6uem.restdoc;

import com.github.ayz6uem.restdoc.schema.Tree;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.utils.SourceRoot;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;

/**
 * 工具入口类、上下文
 */
@Getter
@Setter
public class RestDoc {

    Enviroment env;
    Tree tree;

    public RestDoc() {
        this(new Enviroment());
    }

    public RestDoc(Enviroment env) {
        this.env = env;
        this.tree = new Tree();
        this.tree.setContext(this);
    }

    public RestDoc(String root) {
        this(new Enviroment().source(root).dependency(root));
    }

    @SneakyThrows
    public RestDoc parse() {
        //是否使用责任链？
        ParserConfiguration configuration = env.buildParserConfiguration();
        NodeVisitor visitor = env.nodeVisitor();
        Iterator<String> iterator = env.sources.iterator();
        while (iterator.hasNext()){
            String source = iterator.next();
            SourceRoot root = new SourceRoot(Paths.get(source),configuration);
            root.tryToParse().forEach(result -> result.ifSuccessful(cu -> cu.accept(visitor, this.getTree())));
        }
        return this;
    }

    public void build() {
        env.pipeline().forEach(this::build);
    }

    public void build(RestDocVisitor ... visitors){
        Arrays.stream(visitors).forEach(this::build);
    }

    public void build(RestDocVisitor visitor){
        visitor.visit(this);
    }


}
