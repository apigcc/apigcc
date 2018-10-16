package com.github.ayz6uem.restdoc;

import com.github.ayz6uem.restdoc.handler.RestDocHandler;
import com.github.ayz6uem.restdoc.schema.Tree;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.utils.SourceRoot;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;

/**
 * 工具入口类、上下文
 */
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
        this.tree.setId(env.getProject());
        this.tree.setName(env.getTitle());
        this.tree.setDescription(env.getDescription());
    }

    public RestDoc(String root) {
        this(new Enviroment().source(root));
    }

    public RestDoc parse() {
        //是否使用责任链？
        ParserConfiguration configuration = env.buildParserConfiguration();
        Iterator<String> iterator = env.sources.iterator();
        while (iterator.hasNext()){
            String source = iterator.next();
            SourceRoot root = new SourceRoot(Paths.get(source),configuration);
            root.tryToParseParallelized().forEach(result -> result.ifSuccessful(cu -> cu.accept(env.visitor(), this.getTree())));
        }
        return this;
    }

    public void build() {
        env.pipeline().forEach(this::build);
    }

    public void build(RestDocHandler... handlers){
        Arrays.stream(handlers).forEach(this::build);
    }

    public void build(RestDocHandler handler){
        handler.handle(this);
    }


    public Tree getTree() {
        return tree;
    }

    public Enviroment getEnv() {
        return env;
    }
}
