package com.github.apiggs.handler;

import com.github.apiggs.Environment;
import com.github.apiggs.markup.asciidoc.AsciiDoc;
import com.github.apiggs.schema.Tree;
import com.github.apiggs.util.loging.Logger;
import com.github.apiggs.util.loging.LoggerFactory;
import org.asciidoctor.*;

import java.util.Objects;

/**
 * Asciidoctorj文档转换工具
 */
public class HtmlTreeHandler implements TreeHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(Tree tree, Environment env) {
        AttributesBuilder attributes = AttributesBuilder.attributes();
        if(Objects.nonNull(env.getCss())){
            attributes.linkCss(true);
            attributes.styleSheetName(env.getCss());
        }
        Options options = OptionsBuilder.options()
                .mkDirs(true)
                .toDir(env.getOutPath().toFile())
                .safe(SafeMode.UNSAFE)
                .attributes(attributes)
                .get();
        AsciiDocDirectoryWalker directoryWalker = new AsciiDocDirectoryWalker(env.getOutPath().toString());
        Asciidoctor.Factory.create().convertDirectory(directoryWalker,options);
        log.info("Render {}",env.getOutPath());
    }
}
