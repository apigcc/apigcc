package com.apigcc.handler;

import com.apigcc.common.loging.Logger;
import com.apigcc.common.loging.LoggerFactory;
import com.apigcc.schema.Tree;
import com.apigcc.Options;
import org.asciidoctor.*;

import java.util.Objects;

/**
 * Asciidoctorj文档转换工具
 */
public class HtmlTreeHandler implements TreeHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(Tree tree, Options options) {
        AttributesBuilder attributes = AttributesBuilder.attributes();
        attributes.sectionNumbers(true);
        attributes.noFooter(true);
        if (Objects.nonNull(options.getCss())) {
            attributes.linkCss(true);
            attributes.styleSheetName(options.getCss());
        }
        //asciidoctorj 的 options
        OptionsBuilder builder = OptionsBuilder.options()
                .mkDirs(true)
                .toDir(options.getOutPath().toFile())
                .safe(SafeMode.UNSAFE)
                .attributes(attributes);
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        asciidoctor.convertDirectory(new AsciiDocDirectoryWalker(options.getOutPath().toString()), builder.get());
        log.info("Render {}", options.getOutPath());
    }

}
