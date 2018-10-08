package com.wz1990.restdoc.core;

import com.github.javaparser.utils.SourceRoot;
import lombok.Getter;

import java.nio.file.Paths;
import java.util.Objects;

public class Enviroment {

    public static final String DEFAULT_BUILD_PATH = "build";
    public static final String DEFAULT_ADOC_PATH = DEFAULT_BUILD_PATH + "/adoc";
    public static final String DEFAULT_RESTDOC_PATH = DEFAULT_BUILD_PATH + "/restdoc";
    public static final String DEFAULT_JSON_FILE = DEFAULT_BUILD_PATH + "/json";

    @Getter
    SourceRoot sourceRoot;
    @Getter
    String jsonFile = DEFAULT_JSON_FILE;
    @Getter
    String adocPath = DEFAULT_ADOC_PATH;
    @Getter
    String restdocPath = DEFAULT_RESTDOC_PATH;

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        Enviroment enviroment = new Enviroment();

        public Enviroment build(){
            Objects.requireNonNull(enviroment.sourceRoot,"sourceRoot需要设置");
            return enviroment;
        }

        public Builder root(String root){
            enviroment.sourceRoot = new SourceRoot(Paths.get(root));
            return this;
        }

        public Builder jsonFile(String jsonFile){
            enviroment.jsonFile = jsonFile;
            return this;
        }

        public Builder adoc(String adocPath){
            enviroment.adocPath = adocPath;
            return this;
        }

        public Builder restdoc(String restdocPath){
            enviroment.restdocPath = restdocPath;
            return this;
        }

    }

}
