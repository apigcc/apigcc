package com.github.apiggs.handler;

import com.github.apiggs.Environment;
import com.github.apiggs.schema.Tree;
import org.asciidoctor.*;

import java.io.File;

/**
 * Asciidoctorj文档转换工具
 */
public class HtmlTreeHandler implements TreeHandler {

    @Override
    public void handle(Tree tree, Environment env) {
        Options options = OptionsBuilder.options()
                .mkDirs(true)
                .toDir(new File(env.getOut()))
                .safe(SafeMode.UNSAFE)
                .get();
        AsciiDocDirectoryWalker directoryWalker = new AsciiDocDirectoryWalker(env.getOut());
        Asciidoctor.Factory.create().convertDirectory(directoryWalker,options);

    }
}
