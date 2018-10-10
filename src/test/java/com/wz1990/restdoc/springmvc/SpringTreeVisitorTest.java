package com.wz1990.restdoc.springmvc;

import com.github.javaparser.utils.SourceRoot;
import com.wz1990.restdoc.Enviroment;
import com.wz1990.restdoc.RestDoc;
import com.wz1990.restdoc.util.JsonHelper;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class SpringTreeVisitorTest {

    @Test
    public void test1() throws IOException {

        RestDoc restDoc = new RestDoc(Enviroment.builder().build());

        SourceRoot root = new SourceRoot(Paths.get(System.getProperty("user.dir")));
        root.tryToParse().forEach(result->{
            result.ifSuccessful(compilationUnit -> compilationUnit.accept(
                    new SpringTreeVisitor(restDoc),null));
        });

        System.out.println(JsonHelper.toPretty(restDoc.getTree()));
    }

}
