package com.github.apiggs.example.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
