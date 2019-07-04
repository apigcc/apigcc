package com.apigcc;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.nio.file.Paths;

@Setter
@Getter
public class Context {

    private static ThreadLocal<Context> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static final Integer DEFAULT_NODE_INDEX = 99;

    private Path buildPath = Paths.get("build");

    public static Context getInstance(){
        Context context = CONTEXT_THREAD_LOCAL.get();
        if(context==null){
            context = newInstance();
        }
        return context;
    }

    private synchronized static Context newInstance(){
        Context context = CONTEXT_THREAD_LOCAL.get();
        if(context==null){
            context = new Context();
            CONTEXT_THREAD_LOCAL.set(context);
        }
        return context;
    }

}
