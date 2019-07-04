package com.apigcc.render;

import com.apigcc.Context;
import com.apigcc.schema.Project;
import org.asciidoctor.*;
import org.asciidoctor.jruby.AsciiDocDirectoryWalker;

import java.nio.file.Path;

public class AsciidocHtmlRender {

    public void render(Project project){

        Path buildPath = Context.getInstance().getBuildPath();
        Path projectBuildPath = buildPath.resolve(project.getId());


        AttributesBuilder attributes = AttributesBuilder.attributes();
        attributes.sectionNumbers(true);
        attributes.noFooter(true);
//        if (Objects.nonNull(options.getCss())) {
//            attributes.linkCss(true);
//            attributes.styleSheetName(options.getCss());
//        }
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
