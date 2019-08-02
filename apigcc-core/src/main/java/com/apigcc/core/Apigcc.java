package com.apigcc.core;

import com.apigcc.core.common.helper.StringHelper;
import com.apigcc.core.parser.ParserStrategy;
import com.apigcc.core.parser.VisitorParser;
import com.apigcc.core.render.ProjectRender;
import com.apigcc.core.resolver.TypeResolvers;
import com.apigcc.core.schema.Project;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

@Slf4j
public class Apigcc {

    private static Apigcc INSTANCE;

    public static Apigcc getInstance(){
        return INSTANCE;
    }

    @Getter
    private Context context;
    @Getter
    private Project project = new Project();

    private VisitorParser visitorParser = new VisitorParser();
    private ParserConfiguration parserConfiguration;

    @Getter
    private TypeResolvers typeResolvers = new TypeResolvers();

    private Apigcc(){
        init(new Context());
    }

    public Apigcc(Context context){
        init(context);
    }

    /**
     * 初始化环境配置
     * @param context
     */
    private void init(Context context){
        INSTANCE = this;
        this.context = context;
        project.setId(context.getId());
        project.setName(context.getName());
        project.setDescription(context.getDescription());
        project.setVersion(context.getVersion());

        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        for (Path dependency : context.getDependencies()) {
            typeSolver.add(new JavaParserTypeSolver(dependency));
        }
        for (Path jar : context.getJars()) {
            try {
                typeSolver.add(new JarTypeSolver(jar));
            } catch (IOException e) {
                log.warn("exception on {} {}", jar, e.getMessage());
            }
        }
        typeSolver.add(new ReflectionTypeSolver());

        parserConfiguration = new ParserConfiguration();
        parserConfiguration.setSymbolResolver(new JavaSymbolSolver(typeSolver));

        ParserStrategy strategy = loadParserStrategy();
        strategy.onLoad();
        visitorParser.setParserStrategy(strategy);

    }

    /**
     * 加载并设置解析框架
     * null时，使用读取到的第一个框架解析器
     * 找不到时，报错
     */
    private ParserStrategy loadParserStrategy(){
        ServiceLoader<ParserStrategy> serviceLoader = ServiceLoader.load(ParserStrategy.class);
        List<ParserStrategy> strategies = Lists.newArrayList(serviceLoader);
        if(strategies.isEmpty()){
            throw new IllegalArgumentException("no com.apigcc.core.parser.ParserStrategy implements found");
        }
        if(StringHelper.isBlank(context.framework)){
            return strategies.get(0);
        }
        for (ParserStrategy strategy : strategies) {
            if(Objects.equals(context.framework,strategy.name())){
                return strategy;
            }
        }
        throw new IllegalArgumentException("no com.apigcc.core.parser.ParserStrategy implements found for "+context.framework);
    }

    /**
     * 解析源代码
     * @return
     */
    public Project parse(){
        for (Path source : this.context.getSources()) {
            SourceRoot root = new SourceRoot(source, parserConfiguration);
            try {
                for (ParseResult<CompilationUnit> result : root.tryToParse()) {
                    if(result.isSuccessful() && result.getResult().isPresent()){
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
    public void render(){
        for (ProjectRender render : this.context.getRenders()) {
            render.render(project);
        }
    }

}
