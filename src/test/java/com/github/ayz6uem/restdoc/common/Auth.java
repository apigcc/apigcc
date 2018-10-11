package com.github.ayz6uem.restdoc.common;

import lombok.Data;

@Data
public class Auth {

    String token;

    public static class Login{

    }

    public static class Logout{

    }

    class Foo{

        public void foo(){

            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            });

        }

    }

}
