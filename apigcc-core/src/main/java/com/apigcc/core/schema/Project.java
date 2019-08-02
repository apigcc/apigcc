package com.apigcc.core.schema;

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
}
