package com.github.ayz6uem.restdoc;

import com.github.ayz6uem.restdoc.util.ObjectMappers;
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
        restDoc.build(new AsciidocBuilder());
    }

    @Test
    public void testRestdoc(){
        Enviroment env = new Enviroment()
                .projectName("restdoc")
                .title("Restful文档")
                .description("Restdoc接口文档");
        RestDoc restDoc = new RestDoc(env).parse();
        restDoc.parse();
        restDoc.build();
    }

}
