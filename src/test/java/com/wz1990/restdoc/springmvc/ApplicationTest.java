package com.wz1990.restdoc.springmvc;

import com.wz1990.restdoc.core.RestDoc;
import com.wz1990.restdoc.helper.JsonHelper;
import org.junit.Before;
import org.junit.Test;

public class ApplicationTest {

    String sourcePath;

    @Before
    public void init(){
        sourcePath = System.getProperty("user.dir");
    }

    @Test
    public void testRestdoc(){
        RestDoc restDoc = new RestDoc(sourcePath).parse();
        System.out.println(JsonHelper.toPretty(restDoc.getTree()));
    }

}
