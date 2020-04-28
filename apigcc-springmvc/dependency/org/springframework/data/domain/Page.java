package org.springframework.data.domain;

import java.util.List;

public class Page<T> {

    /**
     * 当前页码
     */
    int number;

    /**
     * 页内数据个数
     */
    int size;

    /**
     * 总数据量
     */
    int totalElements;

    /**
     * 总页数
     */
    int totalPages;

    /**
     * 是否第一页
     */
    boolean first;

    /**
     * 是否最后一页
     */
    boolean last;

    /**
     * 是否空页
     */
    boolean empty;

    /**
     * 数据
     */
    List<T> content;

}
