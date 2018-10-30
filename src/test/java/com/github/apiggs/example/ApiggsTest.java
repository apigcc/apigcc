package com.github.apiggs.example;

import com.github.apiggs.Apiggs;
import com.github.apiggs.Environment;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.List;

/**
 * @title Apiggs示例文档
 * @description 通过javadoc设置文档描述信息
 * 优先级大于通过Environment.description()设置的值
 * @readme 所有接口均使用Https调用
 * /app路径下的接口为app专用
 * /mini路径下的接口为小程序专用
 */
public class ApiggsTest {

    @Test
    public void testApiggs() {
        Environment env = new Environment()
                .source(Paths.get("src","test","java"))
                .id("example")
                .title("示例接口文档")
                .description("示例接口文档，使用默认模板");
        Apiggs apiggs = new Apiggs(env);
        apiggs.lookup().build();



    }

}