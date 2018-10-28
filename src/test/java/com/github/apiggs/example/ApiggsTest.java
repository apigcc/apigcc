package com.github.apiggs.example;

import com.github.apiggs.Apiggs;
import com.github.apiggs.Environment;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 以下是文档说明
 * @readme 所有接口均使用Https调用
 * /app路径下的接口为app专用
 * /mini路径下的接口为小程序专用
 */
public class ApiggsTest {

    @Test
    public void testApiggs() {
        Path project = Paths.get("/","Users","wz","Desktop","Apiggs","apiggs");
        Environment env = new Environment()
                .project(project)
                .source(Paths.get("src","test","java"))
                .id("example")
                .title("示例接口文档")
                .description("示例接口文档，使用默认模板");
        Apiggs apiggs = new Apiggs(env);
        apiggs.lookup().build();
    }

}