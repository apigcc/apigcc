package com.github.apiggs.example;

import com.github.apiggs.Apiggs;
import com.github.apiggs.Environment;
import org.junit.Before;
import org.junit.Test;

public class ApiggsTest {

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
        Apiggs piggy = new Apiggs(env);
        piggy.lookup();
        piggy.build();
    }

}
