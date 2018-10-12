package com.github.ayz6uem.restdoc.util;

import com.github.ayz6uem.restdoc.common.Auth;
import com.github.ayz6uem.restdoc.common.User;
import org.junit.Test;

public class URLTest {

    @Test
    public void test1(){
        String p = "/users";
        String s = "/";
        System.out.println(URL.normalize(p,s));
    }

    @Test
    public void test2(){
        Auth user = new Auth();
        String json = ObjectMappers.toPretty(user);
        System.out.println(json);
    }

}
