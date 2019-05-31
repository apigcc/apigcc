package com.apigcc.core;

import com.apigcc.core.handler.TreeHandler;
import com.apigcc.core.schema.Bucket;
import com.apigcc.core.visitor.Framework;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * 工具入口类、上下文
 */
@Slf4j
public class Apigcc extends Context {

    public Apigcc() {
        this(new Options());
    }

    public Apigcc(String root) {
        this(new Options().source(Paths.get(root)));
    }

    public Apigcc(Options options) {
        super();
        this.options = options;
        this.tree.setId(options.getId());
        this.tree.setName(options.getTitle());
        this.tree.setDescription(options.getDescription());
        this.tree.setVersion(options.getVersion());
        this.tree.setBucket(new Bucket(options.getId()));
        this.getIgnoreTypes().addAll(options.getIgnores());
    }

    /**
     * 解析源代码
     * @return
     */
    private List<CompilationUnit> parseSource(){

        List<CompilationUnit> cus = Lists.newLinkedList();

        ParserConfiguration parserConfiguration = buildParserConfiguration();
        for (Path path : options.getSources()) {
            SourceRoot root = new SourceRoot(path, parserConfiguration);
            try {
                for (ParseResult<CompilationUnit> result : root.tryToParse()) {
                    if(result.isSuccessful() && result.getResult().isPresent()){
                        cus.add(result.getResult().get());
                    }
                }
            } catch (IOException e) {
                log.warn("parse source error : {}", root.getRoot());
            }
        }

        return cus;
    }

    /**
     * 搜寻给定代码及依赖环境
     * 找到Endpoints，构建Tree
     *
     * @return
     */
    public Apigcc lookup() {

        List<CompilationUnit> cus = parseSource();

        Framework framework = Framework.getCurrent(cus);

        for (CompilationUnit cu : cus) {
            cu.accept(framework.getVisitor(),this.tree);
        }

        Integer totalNodes = tree.getBucket().getGroups().stream()
                .map(g -> g.getNodes().size())
                .reduce(0, (sum, i) -> sum += i);
        log.info("Found {} Controllers, {} Endpoints", tree.getBucket().getGroups().size(), totalNodes);

        return this;
    }

    /**
     * 执行默认的构建任务
     */
    public void build() {
        getPipeline().forEach(this::build);
    }

    public void build(TreeHandler... handlers) {
        Arrays.stream(handlers).forEach(this::build);
    }

    public void build(TreeHandler handler) {
        handler.handle(tree, options);
    }


    /**
     * 构建代码解析所需的环境
     * @return
     */
    private ParserConfiguration buildParserConfiguration() {
        if (options.getSources().isEmpty()) {
            options.source(options.getProject().resolve(Options.DEFAULT_SOURCE_STRUCTURE));
        }

        getTypeSolver().add(new ReflectionTypeSolver());

        options.getDependencies().forEach(value -> getTypeSolver().add(new JavaParserTypeSolver(value)));
        options.getJars().forEach(value -> {
            try {
                getTypeSolver().add(new JarTypeSolver(value));
            } catch (IOException e) {
                log.debug("read jar fail:{}",value);
            }
        });

        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setSymbolResolver(new JavaSymbolSolver(getTypeSolver()));
        return parserConfiguration;
    }

}
