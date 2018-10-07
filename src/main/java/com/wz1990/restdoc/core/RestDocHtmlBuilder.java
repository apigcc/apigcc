package com.wz1990.restdoc.core;

import org.asciidoctor.*;

import java.io.File;

public class RestDocHtmlBuilder {

    Enviroment enviroment;

    public RestDocHtmlBuilder(Enviroment enviroment) {
        this.enviroment = enviroment;
    }

    public void build(){
        Options options = OptionsBuilder.options()
                .mkDirs(true)
                .toDir(new File(enviroment.getRestdocPath()))
                .safe(SafeMode.UNSAFE)
                .get();
        AsciiDocDirectoryWalker directoryWalker = new AsciiDocDirectoryWalker(enviroment.getAdocPath());
        Asciidoctor.Factory.create().convertDirectory(directoryWalker,options);
    }

}
