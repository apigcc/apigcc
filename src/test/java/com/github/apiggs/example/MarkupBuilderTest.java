package com.github.apiggs.example;

import com.github.apiggs.markup.MarkupBuilder;
import com.github.apiggs.markup.asciidoc.DocAttribute;
import org.junit.Test;

public class MarkupBuilderTest {

    @Test
    public void test1(){
        MarkupBuilder builder = MarkupBuilder.getInstance();
        builder.header("hello", DocAttribute.TOC_LEFT);
        builder.textLine("hello world");

        System.out.println(builder.getContent());
    }

}
