package com.github.ayz6uem.restdoc;

import com.github.ayz6uem.restdoc.util.ObjectMappers;
import com.github.ayz6uem.restdoc.handler.AsciidocHandler;
import org.junit.Before;
import org.junit.Test;

public class ApplicationTest {

    String source;

    @Before
    public void setUp(){
        source = System.getProperty("user.dir")+"/src/test/java";
    }

    @Test
    public void testParse(){
        RestDoc restDoc = new RestDoc(source).parse();
        System.out.println(ObjectMappers.toPretty(restDoc.getTree()));
    }

    @Test
    public void testAdoc(){
        RestDoc restDoc = new RestDoc(source);
        restDoc.parse();
        restDoc.build(new AsciidocHandler());
    }

    @Test
    public void testRestdoc(){
        Enviroment env = new Enviroment()
                .source(source)
                .project("user")
                .title("User服务接口文档")
                .description("用户服务文档，使用默认模板");
        RestDoc restDoc = new RestDoc(env);
        restDoc.parse();
        restDoc.build();
    }

}
