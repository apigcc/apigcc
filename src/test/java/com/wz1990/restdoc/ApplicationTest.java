package com.wz1990.restdoc;

import com.wz1990.restdoc.helper.JsonHelper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ApplicationTest {

    String root;

    @Before
    public void init(){
        root = System.getProperty("user.dir");
    }

    @Test
    public void testParse(){
        RestDoc restDoc = new RestDoc(root).parse();
        System.out.println(JsonHelper.toPretty(restDoc.getTree()));
    }

    @Test
    public void testAdoc(){
        RestDoc restDoc = new RestDoc(root).parse();
        restDoc.buildAdoc();
    }

    @Test
    public void testRestdoc(){
        RestDoc restDoc = new RestDoc(root).parse();
        restDoc.getTree().getInfo().setName("Restdoc接口文档");
        restDoc.getTree().getInfo().setDescription("Restdoc接口文档");
        restDoc.buildJson().buildAdoc().buildRestdoc();
    }

    @Test
    public void testRestdocOnly(){
        RestDoc restDoc = new RestDoc(root);
        restDoc.buildRestdoc();
    }

}
