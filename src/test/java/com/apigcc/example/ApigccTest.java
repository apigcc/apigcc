package com.apigcc.example;

import com.apigcc.Apigcc;
import com.apigcc.Options;
import com.apigcc.example.diff.MatchUtil;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @title Apigcc示例文档
 * @description 通过javadoc设置文档描述信息
 * 优先级大于通过Environment.description()设置的值
 * @readme 所有接口均使用Https调用
 * /app路径下的接口为app专用
 * /mini路径下的接口为小程序专用
 */
public class ApigccTest {

    @Test
    public void testApigcc() {
        Options options = new Options()
                .source(Paths.get("src", "test", "java"))
                .ignore("ResponseEntity")
                .jar(Paths.get("src/test/resources/lib/apigcc-model-1.0-SNAPSHOT.jar"))
                .id("apigcc")
                .title("示例接口文档")
                .description("示例接口文档，使用默认模板");
        Apigcc apigcc = new Apigcc(options);
        apigcc.lookup().build();

        Path buildAdoc = options.getOutPath().resolve(options.getId() + ".adoc");
        Path template = options.getOutPath().resolve("../../src/test/resources/template.adoc");
        Path templateHtml = options.getOutPath().resolve("../../src/test/resources/template.html");
        Path resultHtml = options.getOutPath().resolve("result.html");

        new MatchUtil(templateHtml, resultHtml).compare(template, buildAdoc);
    }

}