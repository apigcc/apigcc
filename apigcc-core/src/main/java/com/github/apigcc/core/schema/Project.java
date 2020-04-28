package com.github.apigcc.core.schema;

import com.github.apigcc.core.Context;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * 项目
 */
@Setter
@Getter
public class Project extends Node {

    String version;

    Map<String, Book> books = new TreeMap<>();

    public void addChapter(Chapter chapter) {
        if(Objects.isNull(chapter.getBookName())){
            chapter.setBookName(Book.DEFAULT);
        }
        if (!books.containsKey(chapter.getBookName())) {
            books.put(chapter.getBookName(), new Book(chapter.getBookName()));
        }
        books.get(chapter.getBookName()).getChapters().add(chapter);
    }

    /**
     * 根据环境配置初始化
     * @param context
     */
    public void init(Context context) {
        setId(context.getId());
        setName(context.getName());
        setDescription(context.getDescription());
        setVersion(context.getVersion());
    }
}
