package com.wz1990.restdoc.util;

import org.junit.Test;

public class URLTest {

    @Test
    public void test1(){
        String p = "/";
        String s = "/";
        System.out.println(URL.normalize(p,s));
    }

}
