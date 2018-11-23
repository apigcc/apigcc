package com.apigcc.example;

import com.apigcc.core.Apigcc;
import com.apigcc.core.Options;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by lenovo on 2018/11/19.
 * 描述：
 */

public class JfinalTest {

    static void testJfinal(){
        Path path=Paths.get("jfinal","src","main","java");
        Options options = new Options()
                .source(path)
                .ignore("ResponseEntity")
                .jar(Paths.get("src/test/resources/lib/apigcc-model-1.0-SNAPSHOT.jar"))
                .id("apigcc")
                .title("jfinal示例接口文档")
                .description("示例接口文档，使用默认模板");
        Apigcc apigcc = new Apigcc(options);
        apigcc.lookup().build();
    }

    public static void main(String[] args) {
        testJfinal();
    }
}
