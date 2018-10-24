package com.github.apiggs.example;

import com.github.apiggs.Apiggs;
import com.github.apiggs.Environment;
import org.junit.Test;

import java.nio.file.Paths;

public class ApiggsTest {

    @Test
    public void testApiggs() {
        Environment env = new Environment()
                .source(Environment.DEFAULT_PROJECT_PATH.resolve(Paths.get("src", "test", "java")))
                .id("example")
                .title("示例接口文档")
                .description("示例接口文档，使用默认模板");
        Apiggs piggs = new Apiggs(env);
        piggs.lookup();
        piggs.build();
    }

}
