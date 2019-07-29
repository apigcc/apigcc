package com.apigcc;

import com.apigcc.parser.VisitorParser;
import com.apigcc.render.ProjectRender;
import com.apigcc.schema.Project;
import com.apigcc.spring.SpringParserStrategy;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
public class Apigcc {

    private static Apigcc INSTANCE;

    public static Apigcc getInstance(){
        return INSTANCE;
    }

    private Context context;
    private Project project = new Project();

    private VisitorParser visitorParser = new VisitorParser();
    private ParserConfiguration parserConfiguration;

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
        visitorParser.setParserStrategy(new SpringParserStrategy());

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
        typeSolver.add(new ReflectionTypeSolver(false));

        parserConfiguration = new ParserConfiguration();
        parserConfiguration.setSymbolResolver(new JavaSymbolSolver(typeSolver));
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

    public Context getContext() {
        return context;
    }
}
