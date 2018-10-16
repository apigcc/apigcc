package com.github.ayz6uem.restdoc.handler;

import com.github.ayz6uem.restdoc.RestDoc;
import org.asciidoctor.*;

import java.io.File;

/**
 * Asciidoctorj文档转换工具
 */
public class HtmlHandler implements RestDocHandler {

    @Override
    public void handle(RestDoc restDoc) {
        Options options = OptionsBuilder.options()
                .mkDirs(true)
                .toDir(new File(restDoc.getEnv().getOut()))
                .safe(SafeMode.UNSAFE)
                .get();
        AsciiDocDirectoryWalker directoryWalker = new AsciiDocDirectoryWalker(restDoc.getEnv().getOut());
        Asciidoctor.Factory.create().convertDirectory(directoryWalker,options);

    }
}
