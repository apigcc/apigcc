package com.apigcc;

import com.apigcc.common.helper.FileHelper;
import com.apigcc.render.AsciidocHtmlRender;
import com.apigcc.render.AsciidocRender;
import com.apigcc.render.ProjectRender;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Getter
public class Context {

    public static final Integer DEFAULT_NODE_INDEX = 99;
    public static final String DEFAULT_PROJECT_ID = "api";
    public static final String DEFAULT_BUILD_PATH = "build";
    public static final String DEFAULT_CODE_STRUCTURE = "src/main/java";

    @Setter
    public List<ProjectRender> renders = Lists.newArrayList(new AsciidocRender(), new AsciidocHtmlRender());

    @Setter
    private Path buildPath = Paths.get(DEFAULT_BUILD_PATH);

    /**
     * 源码目录
     */
    private List<Path> sources = Lists.newArrayList();

    /**
     * 依赖源码
     */
    private List<Path> dependencies = Lists.newArrayList();

    /**
     * 依赖jar包
     */
    private List<Path> jars = Lists.newArrayList();

    @Setter
    private String id = DEFAULT_PROJECT_ID;
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private String version;

    /**
     * 渲染html时的css
     */
    @Setter
    private String css;

    public void addSource(Path path){
        sources.add(path);
        sources.addAll(FileHelper.find(path, DEFAULT_CODE_STRUCTURE));
        addDependency(path);
    }

    public void addDependency(Path path){
        dependencies.add(path);
        dependencies.addAll(FileHelper.find(path, DEFAULT_CODE_STRUCTURE));
    }

    public void addJar(Path path){
        jars.add(path);
    }

}
