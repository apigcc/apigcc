package com.github.apiggs;

import com.github.apiggs.common.loging.Logger;
import com.github.apiggs.common.loging.LoggerFactory;
import com.google.common.collect.Sets;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Getter
public class Options {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public static final Path DEFAULT_PRODUCTION = Paths.get(Context.NAME);
    public static final Path DEFAULT_SOURCE_STRUCTURE = Paths.get("src", "main", "java");
    public static final Path DEFAULT_PROJECT_PATH = Paths.get(System.getProperty("user.dir"));
    public static final Path DEFAULT_OUT = Paths.get("build");

    /**
     * project根目录
     */
    private Path project = DEFAULT_PROJECT_PATH;

    /**
     * 项目名称 生成 index.json index.adoc index.html
     */
    private String id = "index";

    /**
     * 文档标题
     */
    private String title;

    /**
     * 文档描述
     */
    private String description;
    /**
     * 文档版本
     */
    private String version;

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
     * 输出文件包裹的文件夹
     */
    private Path production = DEFAULT_PRODUCTION;

    /**
     * 输出目录
     */
    private Path out = DEFAULT_OUT;

    private String css;

    private Set<String> ignores;



    public Options project(Path value) {
        this.project = value;
        return this;
    }

    public Options id(String value) {
        this.id = value;
        return this;
    }

    public Options title(String value) {
        this.title = value;
        return this;
    }

    public Options description(String value) {
        this.description = value;
        return this;
    }

    public Options version(String value) {
        this.version = value;
        return this;
    }

    public Options source(Path... values) {
        for (Path value : values) {
            if(!value.isAbsolute()){
                value = project.resolve(value);
            }
            if (Files.exists(value)) {
                this.sources.add(value);
            }
        }
        dependency(values);
        return this;
    }

    public Options dependency(Path... values) {
        for (Path value : values) {
            if(!value.isAbsolute()){
                value = project.resolve(value);
            }
            if (Files.exists(value)) {
                this.dependencies.add(value);
            }
        }
        return this;
    }

    public Options jar(Path... values) {
        for (Path value : values) {
            if(!value.isAbsolute()){
                value = project.resolve(value);
            }
            if (!Files.exists(value)) {
                continue;
            }
            if (!Files.isDirectory(value) && value.toString().endsWith("jar")){
                this.jars.add(value);
            }
            try {
                Files.list(value).forEach(this::jar);
            } catch (IOException e) {
                log.debug("read list of {} error", value);
            }
        }
        return this;
    }

    public Options production(Path value) {
        this.production = value;
        return this;
    }

    public Options out(Path value) {
        this.out = value;
        return this;
    }

    public Options css(String css) {
        this.css = css;
        return this;
    }

    public Options ignore(String... values) {
        ignores = Sets.newHashSet(values);
        return this;
    }

    public Path getOutPath() {
        if(out.isAbsolute()){
            return out.resolve(production);
        }
        return project.resolve(out).resolve(production);
    }

}
