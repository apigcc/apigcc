package com.github.apiggs.markup.asciidoc;

/**
 * 文档属性
 */
public enum DocAttribute implements CharSequence {

    TOC_LEFT(":toc:left");

    private String text;

    DocAttribute(String text) {
        this.text = text;
    }

    @Override
    public int length() {
        return text.length();
    }

    @Override
    public char charAt(int index) {
        return text.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return text.subSequence(start, end);
    }

    @Override
    public String toString() {
        return text;
    }
}
