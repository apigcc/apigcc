package com.github.apigcc.springmvc;

import com.github.apigcc.core.Apigcc;
import com.github.apigcc.core.Context;
import com.github.apigcc.core.common.diff.FileMatcher;
import com.github.apigcc.core.common.helper.FileHelper;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import org.junit.Test;
import org.springframework.data.domain.Page;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SpringTest {

    @Test
    public void t() throws IOException {
//
//        Path path = Paths.get("org/springframework/data/domain/Page.java");
//        FileHelper.write(path, code);
//
//        JavaParserTypeSolver javaParserTypeSolver = new JavaParserTypeSolver(path);
//
//        CompilationUnit cu = StaticJavaParser.parse(path);
//
//        System.out.println(cu);

    }



    @Test
    public void test() throws IOException {

        Context context = new Context();
        context.setId("ava");
        context.setName("AVA");
        context.addSource(Paths.get("E:/avalokitesvara/application/src/main/java/com/dingdingzn/avalokitesvara/api/dms/equipment/"));
        context.addDependency(Paths.get("E:/avalokitesvara"));
//        context.addDependency(Paths.get("E:/denpendency/spring-data-commons-2.1.10.RELEASE-sources/"));
//        context.setCss("https://darshandsoni.com/asciidoctor-skins/css/monospace.css");
//        context.addJar(Paths.get("E:\\apigcc-hub\\build\\dependency\\spring-data-commons-2.2.3.RELEASE-sources.jar"));

        Apigcc apigcc = new Apigcc(context);
        apigcc.parse();
        apigcc.render();
    }


    @Test
    public void testTestToolls() throws IOException {

        Context context = new Context();
        context.setId("test-tools");
        context.setName("测试工具");
        context.addSource(Paths.get("D:/workspaces/ubisor-test-tools/backend/"));
//        context.setCss("https://darshandsoni.com/asciidoctor-skins/css/monospace.css");

        Apigcc apigcc = new Apigcc(context);
        apigcc.parse();
        apigcc.render();

        Path buildAdoc = Paths.get("build/test-tools/index.adoc");
        Path template = Paths.get("src/test/resources/test-tools.adoc");
        Path templateHtml = Paths.get("src/test/resources/template.html");
        Path resultHtml = Paths.get("build/test-tools/diff.html");

        FileMatcher fileMatcher = new FileMatcher();
        int changed = fileMatcher.compare(template, buildAdoc);
        if(changed>0){
            fileMatcher.rederHtml(templateHtml, resultHtml);
        }

        System.out.println("BUILD SUCCESS");
    }


    @Test
    public void testUbcloud() throws IOException {

        Context context = new Context();
        context.setId("ubcloud");
        context.setName("优碧云1");
        context.addSource(Paths.get("D:/workspaces/ubisor-backend/ubcloud-front-web/"));
        context.addDependency(Paths.get("D:/workspaces/ubisor-backend/"));
        context.setCss("https://darshandsoni.com/asciidoctor-skins/css/monospace.css");

        Apigcc apigcc = new Apigcc(context);
        apigcc.parse();
        apigcc.render();

        Path buildAdoc = Paths.get("build/ubcloud/index.adoc");
        Path template = Paths.get("src/test/resources/ubcloud-front-web.adoc");
        Path templateHtml = Paths.get("src/test/resources/template.html");
        Path resultHtml = Paths.get("build/ubcloud/diff.html");

        FileMatcher fileMatcher = new FileMatcher();
        int changed = fileMatcher.compare(template, buildAdoc);
        if(changed>0){
            fileMatcher.rederHtml(templateHtml, resultHtml);
        }

        System.out.println("BUILD SUCCESS");
    }

}