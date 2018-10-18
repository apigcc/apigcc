package com.github.ayz6uem.apiggy.example;

import com.github.ayz6uem.apiggy.Environment;
import com.github.ayz6uem.apiggy.Apiggy;
import org.junit.Before;
import org.junit.Test;

public class ApiggyTest {

    String source;

    @Before
    public void setUp(){
        source = System.getProperty("user.dir")+"/src/test/java";
    }

    @Test
    public void testApiggy(){
        Environment env = new Environment()
                .source(source)
                .project("example")
                .title("示例接口文档")
                .description("示例接口文档，使用默认模板");
        Apiggy piggy = new Apiggy(env);
        piggy.lookup();
        piggy.build();
    }

}
