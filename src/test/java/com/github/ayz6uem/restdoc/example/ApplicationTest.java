package com.github.ayz6uem.restdoc.example;

import com.github.ayz6uem.restdoc.Environment;
import com.github.ayz6uem.restdoc.RestDoc;
import org.junit.Before;
import org.junit.Test;

public class ApplicationTest {

    String source;

    @Before
    public void setUp(){
        source = System.getProperty("user.dir")+"/src/test/java";
    }

    @Test
    public void testRestdoc(){
        Environment env = new Environment()
                .source(source)
                .project("example")
                .title("示例接口文档")
                .description("示例接口文档，使用默认模板");
        RestDoc restDoc = new RestDoc(env);
        restDoc.parse();
        restDoc.build();
    }

}
