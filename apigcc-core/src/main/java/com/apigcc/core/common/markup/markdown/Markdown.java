package com.apigcc.core.common.markup.markdown;

public enum Markdown implements CharSequence {
    EXTENSION(".md"),
    /**
     * 各种关键字
     */
    HEADER("# "),
    TABLE_CELL("|"),
    TABLE_ROW("-"),
    TABLE_Header("---|"),
    TITLE("#"),
    EMPHASIZED("**"),
    STRONG("**"),
    MONOSPACED("`"),
    QUOTED("**"),
    DOUBLE_QUOTED("**"),
    UNQUOTED("**"),
    LIST_FLAG("* "),
    LISTING("```"),
    LITERAL("```"),
    SIDEBAR("```"),
    COMMENT("```"),
    PASSTHROUGH("```"),
    QUOTE("> "),
    EXAMPLE("===="),
    NOTE("NOTE"),
    TIP("TIP"),
    IMPORTANT("IMPORTANT"),
    WARNING("WARNING"),
    CAUTION("CAUTION"),
    PAGEBREAKS("   "),
    WHITESPACE(" "),
    BR("\r\n"),
    NEW_LINE("\r\n\r\n"),
    HBR(" +"),
    ;

    private final String markup;

    Markdown(final String markup) {
        this.markup = markup;
    }

    @Override
    public int length() {
        return markup.length();
    }

    @Override
    public char charAt(int index) {
        return markup.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return markup.subSequence(start, end);
    }

    @Override
    public String toString() {
        return markup;
    }

    public static CharSequence attr(Markdown key, Object value){
        return key.toString() + " " + String.valueOf(value);
    }

}
