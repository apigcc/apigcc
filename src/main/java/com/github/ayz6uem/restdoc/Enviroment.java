package com.github.ayz6uem.restdoc;

import com.github.ayz6uem.restdoc.springmvc.SpringNodeVisitor;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.ProjectRoot;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Getter
@Setter
public class Enviroment {

    /**
     * 默认的文档树访问器
     */
    public static Iterable<RestDocVisitor> DEFAULT_PIPELINE = Lists.newArrayList(new AsciidocBuilder(), new HtmlBuilder());

    public static final String DEFAULT_OUT_PATH = "build/restdoc/";
    public static final String DEFAULT_PROJECT_PATH = System.getProperty("user.dir");
    public static final String DEFAULT_SOURCE_PATH = DEFAULT_PROJECT_PATH + "/src/main/java/";

    private enum Framework {

        SPRINGMVC(new SpringNodeVisitor());

        private NodeVisitor visitor;

        Framework(NodeVisitor visitor) {
            this.visitor = visitor;
        }

        @SneakyThrows
        public NodeVisitor visitor(){
            return visitor;
        }

    }

    /**
     * source code folder wait for parse
     * or just some code
     * default: parse user.dir 's code
     */
    Set<String> sources = Sets.newHashSet(DEFAULT_SOURCE_PATH);

    /**
     * dependency source code folders
     */
    Set<String> dependencies = Sets.newHashSet(DEFAULT_PROJECT_PATH, DEFAULT_SOURCE_PATH);

    /**
     * dependency third jars
     */
    Set<String> jars = Sets.newHashSet();

    /**
     * 输出目录
     */
    String out = DEFAULT_OUT_PATH;

    /**
     * 项目名称 生成 projectName.json projectName.adoc projectName.html
     */
    String projectName;

    /**
     * 文档标题
     */
    String title;

    /**
     * 文档描述
     */
    String description;
    /**
     * 忽略哪些类型的参数、类解析
     */
    Set<String> ignoreTypes = Sets.newHashSet();

    /**
     * 当前项目使用了什么框架
     */
    Framework currentFramework = Framework.SPRINGMVC;


    public Enviroment source(String ... values){
        this.sources.addAll(Sets.newHashSet(values));
        return this;
    }

    public Enviroment dependency(String ... values){
        this.dependencies.addAll(Sets.newHashSet(values));
        return this;
    }

    public Enviroment jar(String ... values){
        this.jars.addAll(Sets.newHashSet(values));
        return this;
    }

    public Enviroment projectName(String value){
        this.projectName = value;
        return this;
    }

    public Enviroment out(String value){
        this.out = value;
        return this;
    }

    public Enviroment title(String value){
        this.title = value;
        return this;
    }

    public Enviroment description(String value){
        this.description = value;
        return this;
    }

    public Enviroment ignore(String ... values){
        this.ignoreTypes.addAll(Sets.newHashSet(values));
        return this;
    }

    public Iterable<RestDocVisitor> pipeline(){
        return DEFAULT_PIPELINE;
    }

    /**
     * 通过某种方法判断当前项目采用了什么框架
     * @return
     */
    public Framework currentFramework(){
        return currentFramework;
    }

    public ParserConfiguration buildParserConfiguration(){
        Objects.requireNonNull(sources,"source can not be null");
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());

        dependencies.forEach(value-> combinedTypeSolver.add(new JavaParserTypeSolver(value)));
        jars.forEach(value->{
            try {
                combinedTypeSolver.add(new JarTypeSolver(value));
            } catch (IOException e) {
                log.warn(value+" got an error:"+e.getMessage());
            }
        });

        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setSymbolResolver(new JavaSymbolSolver(combinedTypeSolver));
        return parserConfiguration;
    }

    public NodeVisitor nodeVisitor(){
        return currentFramework().visitor();
    }

}
