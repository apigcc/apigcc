package com.github.ayz6uem.apiggy;

import com.github.ayz6uem.apiggy.handler.TreeHandler;
import com.github.ayz6uem.apiggy.schema.Group;
import com.github.ayz6uem.apiggy.schema.Tree;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.utils.SourceRoot;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * 🐷 工具入口类、上下文
 */
public class Apiggy {

    Environment env;
    Tree tree;

    public Apiggy() {
        this(new Environment());
    }

    public Apiggy(Environment env) {
        this.env = env;
        this.tree = new Tree();
        this.tree.setContext(this);
        this.tree.setId(env.getProject());
        this.tree.setName(env.getTitle());
        this.tree.setDescription(env.getDescription());
    }

    public Apiggy(String root) {
        this(new Environment().source(root));
    }

    /**
     * 搜寻给定代码及依赖环境
     * 找到Endpoints，构建Tree
     * @return
     */
    public Apiggy lookup() {
        //是否使用责任链？
        ParserConfiguration configuration = env.buildParserConfiguration();
        for (String source : env.sources) {
            SourceRoot root = new SourceRoot(Paths.get(source), configuration);
            root.tryToParseParallelized().forEach(result -> result.ifSuccessful(cu -> cu.accept(env.visitor(), this.getTree())));
        }

        //对Group进行排序
        tree.getGroups().sort(Group.COMPARATOR);

        return this;
    }

    /**
     * 执行默认的构建任务
     */
    public void build() {
        env.pipeline().forEach(this::build);
    }

    public void build(TreeHandler... handlers){
        Arrays.stream(handlers).forEach(this::build);
    }

    public void build(TreeHandler handler){
        handler.handle(tree, env);
    }

    public Tree getTree() {
        return tree;
    }

    public Environment getEnv() {
        return env;
    }
}
