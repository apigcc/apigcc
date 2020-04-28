package com.github.apigcc.core;

import com.github.apigcc.core.common.helper.StringHelper;
import com.github.apigcc.core.parser.ParserStrategy;
import com.github.apigcc.core.parser.VisitorParser;
import com.github.apigcc.core.render.ProjectRender;
import com.github.apigcc.core.resolver.TypeResolvers;
import com.github.apigcc.core.schema.Project;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.utils.SourceRoot;
import com.google.common.collect.Iterables;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.ServiceLoader;

@Slf4j
public class Apigcc {

    private static Apigcc INSTANCE;

    public static Apigcc getInstance() {
        return INSTANCE;
    }

    @Getter
    private Context context;
    @Getter
    private Project project = new Project();

    private VisitorParser visitorParser = new VisitorParser();
    private ParserConfiguration parserConfiguration = new ParserConfiguration();

    @Getter
    private TypeResolvers typeResolvers = new TypeResolvers();

    private Apigcc() {
        init(new Context());
    }

    public Apigcc(Context context) {
        init(context);
    }

    /**
     * 初始化环境配置
     *
     * @param context
     */
    private void init(Context context) {
        INSTANCE = this;
        this.context = context;
        this.project.init(this.context);

        ParserStrategy strategy = this.loadParserStrategy();
        strategy.onLoad();
        this.visitorParser.setParserStrategy(strategy);

        this.parserConfiguration.setSymbolResolver(new JavaSymbolSolver(this.context.buildTypeSolver()));
    }

    /**
     * 加载并设置解析框架
     * null时，使用读取到的第一个框架解析器
     * 找不到时，报错
     */
    private ParserStrategy loadParserStrategy() {
        ServiceLoader<ParserStrategy> serviceLoader = ServiceLoader.load(ParserStrategy.class);
        if (Iterables.isEmpty(serviceLoader)) {
            throw new IllegalArgumentException("no com.apigcc.core.parser.ParserStrategy implements found");
        }
        if (StringHelper.isBlank(context.framework)) {
            return Iterables.get(serviceLoader, 0);
        }
        for (ParserStrategy strategy : serviceLoader) {
            if (Objects.equals(context.framework, strategy.name())) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("no com.github.apigcc.core.parser.ParserStrategy implements found for " + context.framework);
    }

    /**
     * 解析源代码
     *
     * @return
     */
    public Project parse() {
        for (Path source : this.context.getSources()) {
            SourceRoot root = new SourceRoot(source, parserConfiguration);
            try {
                for (ParseResult<CompilationUnit> result : root.tryToParse()) {
                    if (result.isSuccessful() && result.getResult().isPresent()) {
                        result.getResult().get().accept(visitorParser, project);
                    }
                }
            } catch (IOException e) {
                log.warn("parse root {} error {}", source, e.getMessage());
            }
        }
        return project;
    }

    /**
     * 渲染解析结果
     */
    public void render() {
        for (ProjectRender render : this.context.getRenders()) {
            render.render(project);
        }
    }

}
