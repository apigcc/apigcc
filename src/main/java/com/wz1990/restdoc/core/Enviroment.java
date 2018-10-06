package com.wz1990.restdoc.core;

import com.github.javaparser.utils.SourceRoot;
import lombok.Getter;

import java.nio.file.Paths;
import java.util.Objects;

public class Enviroment {

    @Getter
    SourceRoot sourceRoot;

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{

        Enviroment enviroment = new Enviroment();

        public Enviroment build(){
            Objects.requireNonNull(enviroment.sourceRoot,"sourceRoot需要设置");
            return enviroment;
        }

        public Builder path(String path){
            enviroment.sourceRoot = new SourceRoot(Paths.get(path));
            return this;
        }

    }

}
