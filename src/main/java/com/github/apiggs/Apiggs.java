package com.github.apiggs;

import com.github.apiggs.handler.TreeHandler;
import com.github.apiggs.schema.Bucket;
import com.github.apiggs.schema.Group;
import com.github.apiggs.schema.Tree;
import com.github.apiggs.util.loging.Logger;
import com.github.apiggs.util.loging.LoggerFactory;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.utils.SourceRoot;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 🐷 工具入口类、上下文
 */
public class Apiggs {

    public static final ThreadLocal<Apiggs> context = new ThreadLocal<>();

    public static Apiggs getContext(){
        return context.get();
    }

    Logger log = LoggerFactory.getLogger(this.getClass());

    Environment env;
    Tree tree;

    public Apiggs() {
        this(new Environment());
    }

    public Apiggs(Environment env) {
        this.env = env;
        this.tree = new Tree();
        this.tree.setId(env.getId());
        this.tree.setName(env.getTitle());
        this.tree.setDescription(env.getDescription());
        this.tree.setVersion(env.getVersion());
        this.tree.setBucket(new Bucket(env.getId()));

        context.set(this);
    }

    public Apiggs(String root) {
        this(new Environment().source(Paths.get(root)));
    }

    /**
     * 搜寻给定代码及依赖环境
     * 找到Endpoints，构建Tree
     *
     * @return
     */
    public Apiggs lookup() {

        ParserConfiguration configuration = env.buildParserConfiguration();
        for (Path source : env.getSources()) {
            log.info("Parsing source : {}", source);
            SourceRoot root = new SourceRoot(source, configuration);
            root.tryToParseParallelized().forEach(result -> result.ifSuccessful(cu -> cu.accept(env.visitor(), this.getTree())));
        }

        Integer totalNodes = tree.getBucket().getGroups().stream()
                .map(g -> g.getNodes().size())
                .reduce(0, (sum, i) -> sum += i);
        log.info("\r\nFound {} Controllers, {} Endpoints", tree.getBucket().getGroups().size(), totalNodes);

        return this;
    }

    /**
     * 执行默认的构建任务
     */
    public void build() {
        env.pipeline().forEach(this::build);
    }

    public void build(TreeHandler... handlers) {
        Arrays.stream(handlers).forEach(this::build);
    }

    public void build(TreeHandler handler) {
        handler.handle(tree, env);
    }

    public Tree getTree() {
        return tree;
    }

    public Environment getEnv() {
        return env;
    }

}
