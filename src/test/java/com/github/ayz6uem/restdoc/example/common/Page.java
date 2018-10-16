package com.github.ayz6uem.restdoc.example.common;

public class Page extends Query {

    /**
     * 第几页
     */
    int page = 1;
    /* 每页条数 */
    int sizs = 20;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSizs() {
        return sizs;
    }

    public void setSizs(int sizs) {
        this.sizs = sizs;
    }
}
