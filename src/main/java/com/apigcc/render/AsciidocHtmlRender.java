package com.apigcc.render;

import com.apigcc.Apigcc;
import com.apigcc.Context;
import com.apigcc.common.helper.StringHelper;
import com.apigcc.schema.Project;
import org.asciidoctor.*;
import org.asciidoctor.jruby.AsciiDocDirectoryWalker;

import java.nio.file.Path;
import java.util.Objects;

/**
 * 转换adoc文件为html文件
 */
public class AsciidocHtmlRender implements ProjectRender {

    @Override
    public void render(Project project){

        Path buildPath = Apigcc.getInstance().getContext().getBuildPath();
        Path projectBuildPath = buildPath.resolve(project.getId());

        AttributesBuilder attributes = AttributesBuilder.attributes();
        attributes.sectionNumbers(true);
        attributes.noFooter(true);
        String css = Apigcc.getInstance().getContext().getCss();
        if (StringHelper.nonBlank(css)) {
            attributes.linkCss(true);
            attributes.styleSheetName(css);
        }
        //asciidoctorj 的 options
        OptionsBuilder builder = OptionsBuilder.options()
                .mkDirs(true)
                .inPlace(true)
                .toDir(projectBuildPath.toFile())
                .safe(SafeMode.SAFE)
                .attributes(attributes);
        Asciidoctor.Factory.create()
                .convertDirectory(new AsciiDocDirectoryWalker(projectBuildPath.toString()), builder.get());

    }

}
