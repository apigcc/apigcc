package com.github.ayz6uem.restdoc.asciidoc;

import org.asciidoctor.*;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class AsciidoctorTest {

    @Test
    public void test1(){
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        String html = asciidoctor.convert(
                "Writing AsciiDoc is _easy_!",
                new HashMap<String, Object>());
        System.out.println(html);
    }

    @Test
    public void test2(){
        String path = "out/doc";
        File file = new File(path);
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        String[] results = asciidoctor.convertDirectory(
                new AsciiDocDirectoryWalker("out/adoc"),
                OptionsBuilder.options()
                        .mkDirs(true)
                        .toDir(file)

        );
        System.out.println(Arrays.toString(results));
    }


}
