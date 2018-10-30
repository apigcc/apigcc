package com.github.apiggs.example.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Page extends Query {

    /**
     * 第几页
     */
    @JsonProperty
    int page = 1;
    /* 每页条数 */
    @JSONField(name = "limit")
    int sizs = 20;
    @SerializedName("totalPage")
    int total = 0;
    @JsonProperty("max")
    int maxPage = 0;

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
