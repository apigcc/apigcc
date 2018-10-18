package com.github.apiggs.example.hello;

public class Greeting {

    /**
     * 编号
     */
    private final long id;
    /**
     * 内容
     */
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
