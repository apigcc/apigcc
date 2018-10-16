package com.github.ayz6uem.restdoc;

import com.github.ayz6uem.restdoc.handler.postman.PostmanBuilder;
import com.github.ayz6uem.restdoc.visitor.NodeVisitor;
import com.github.ayz6uem.restdoc.visitor.springmvc.SpringVisitor;
import com.github.ayz6uem.restdoc.handler.AsciidocHandler;
import com.github.ayz6uem.restdoc.handler.HtmlHandler;
import com.github.ayz6uem.restdoc.handler.RestDocHandler;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class Environment {

    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 默认的文档树访问器
     */
    public static Iterable<RestDocHandler> DEFAULT_PIPELINE = Lists.newArrayList(new PostmanBuilder(), new AsciidocHandler(), new HtmlHandler());

    public static final String DEFAULT_OUT_PATH = "build/restdoc/";
    public static final String DEFAULT_PROJECT_PATH = System.getProperty("user.dir");
    public static final String DEFAULT_SOURCE_PATH = DEFAULT_PROJECT_PATH + "/src/main/java/";

    private enum Framework {

        SPRINGMVC(new SpringVisitor());

        private NodeVisitor visitor;

        Framework(NodeVisitor visitor) {
            this.visitor = visitor;
        }

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
     * 项目名称 生成 project.json project.adoc project.html
     */
    String project;

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
    public static Set<String> ignoreTypes = Sets.newHashSet();

    /**
     * 当前项目使用了什么框架
     */
    Framework currentFramework = Framework.SPRINGMVC;

    public Environment source(String ... values){
        this.sources.addAll(Sets.newHashSet(values));
        dependency(values);
        return this;
    }

    public Environment dependency(String ... values){
        this.dependencies.addAll(Sets.newHashSet(values));
        return this;
    }

    public Environment jar(String ... values){
        this.jars.addAll(Sets.newHashSet(values));
        return this;
    }

    public Environment project(String value){
        this.project = value;
        return this;
    }

    public Environment out(String value){
        this.out = value;
        return this;
    }

    public Environment title(String value){
        this.title = value;
        return this;
    }

    public Environment description(String value){
        this.description = value;
        return this;
    }

    public Environment ignore(String ... values){
        this.ignoreTypes.addAll(Sets.newHashSet(values));
        return this;
    }

    public Iterable<RestDocHandler> pipeline(){
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

    public NodeVisitor visitor(){
        return currentFramework().visitor();
    }

    public Set<String> getSources() {
        return sources;
    }

    public Set<String> getDependencies() {
        return dependencies;
    }

    public Set<String> getJars() {
        return jars;
    }

    public String getOut() {
        return out;
    }

    public String getProject() {
        return project;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static Set<String> getIgnoreTypes() {
        return ignoreTypes;
    }
}
