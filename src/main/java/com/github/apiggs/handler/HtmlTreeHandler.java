package com.github.apiggs.handler;

import com.github.apiggs.Environment;
import com.github.apiggs.schema.Tree;
import org.asciidoctor.*;

/**
 * Asciidoctorj文档转换工具
 */
public class HtmlTreeHandler implements TreeHandler {

    @Override
    public void handle(Tree tree, Environment env) {
        Options options = OptionsBuilder.options()
                .mkDirs(true)
                .toDir(env.getOutPath().toFile())
                .safe(SafeMode.UNSAFE)
                .get();
        AsciiDocDirectoryWalker directoryWalker = new AsciiDocDirectoryWalker(env.getOutPath().toString());
        Asciidoctor.Factory.create().convertDirectory(directoryWalker,options);

    }
}
