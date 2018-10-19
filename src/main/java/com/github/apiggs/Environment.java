package com.github.apiggs;

import com.github.apiggs.handler.AsciidocTreeHandler;
import com.github.apiggs.handler.HtmlTreeHandler;
import com.github.apiggs.handler.TreeHandler;
import com.github.apiggs.handler.postman.PostmanBuilder;
import com.github.apiggs.visitor.NodeVisitor;
import com.github.apiggs.visitor.springmvc.SpringVisitor;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class Environment {

    public static final Path DEFAULT_PRODUCTION = Paths.get("apiggs");
    public static final Path DEFAULT_SOURCE_STRUCTURE = Paths.get("src","main","java");
  
    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 默认的文档树访问器
     */
    public static Iterable<TreeHandler> DEFAULT_PIPELINE = Lists.newArrayList(new PostmanBuilder(), new AsciidocTreeHandler(), new HtmlTreeHandler());

    public static final Path DEFAULT_OUT_PATH = Paths.get("build").resolve(DEFAULT_PRODUCTION);
    public static final Path DEFAULT_PROJECT_PATH = Paths.get(System.getProperty("user.dir"));
    public static final Path DEFAULT_SOURCE_PATH = DEFAULT_PROJECT_PATH.resolve(DEFAULT_SOURCE_STRUCTURE);

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

    private Set<Path> sources = Sets.newHashSet();

    /**
     * dependency source code folders
     */
    private Set<Path> dependencies = Sets.newHashSet();

    /**
     * dependency third jars
     */
    private Set<Path> jars = Sets.newHashSet();

    /**
     * 输出目录
     */
    private Path out = DEFAULT_OUT_PATH;

    /**
     * 项目名称 生成 index.json index.adoc index.html
     */
    private String project = "index";

    /**
     * 文档标题
     */
    private String title;

    /**
     * 文档描述
     */
    private String description;
    /**
     * 忽略哪些类型的参数、类解析
     */
    public static Set<String> IGNORE_TYPES = Sets.newHashSet();

    /**
     * 当前项目使用了什么框架
     */
    private Framework currentFramework = Framework.SPRINGMVC;

    public Environment source(Path ... values){
        for (Path value : values) {
            this.sources.add(value);
        }
        dependency(values);
        return this;
    }

    public Environment dependency(Path ... values){
        for (Path value : values) {
            this.dependencies.add(value);
        }
        return this;
    }

    public Environment jar(Path ... values){
        this.jars.addAll(Sets.newHashSet(values));
        return this;
    }

    public Environment project(String value){
        this.project = value;
        return this;
    }

    public Environment out(Path value){
        this.out = value.resolve(DEFAULT_PRODUCTION);
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
        IGNORE_TYPES.addAll(Sets.newHashSet(values));
        return this;
    }

    public Iterable<TreeHandler> pipeline(){
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
        if(sources.isEmpty()){
            source(DEFAULT_SOURCE_PATH);
        }
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

    public Set<Path> getSources() {
        return sources;
    }

    public Set<Path> getDependencies() {
        return dependencies;
    }

    public Set<Path> getJars() {
        return jars;
    }

    public Path getOut() {
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
        return IGNORE_TYPES;
    }
}
