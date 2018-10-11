package com.github.ayz6uem.restdoc.util;

import org.junit.Test;

public class URLTest {

    @Test
    public void test1(){
        String p = "/users";
        String s = "/";
        System.out.println(URL.normalize(p,s));
    }

}
