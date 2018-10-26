package com.github.apiggs.markup.asciidoc;

public enum AsciiDoc implements CharSequence {
    HEADER("= "),
    TABLE("|==="),
    TABLE_CELL("|"),
    TITLE("="),
    EMPHASIZED("_"),
    STRONG("*"),
    MONOSPACED("+"),
    QUOTED("`"),
    DOUBLE_QUOTED("``"),
    UNQUOTED("#"),
    LIST_FLAG("1. "),
    LIST_FLAG_LETTER("a. "),
    LIST_FLAG_LETTER_UPPER("A. "),
    LISTING("----"),
    LITERAL("...."),
    SIDEBAR("****"),
    COMMENT("////"),
    PASSTHROUGH("++++"),
    QUOTE("____"),
    EXAMPLE("===="),
    NOTE("[NOTE]"),
    TIP("[TIP]"),
    IMPORTANT("[IMPORTANT]"),
    WARNING("[WARNING]"),
    CAUTION("[CAUTION]"),
    PAGEBREAKS("<<<"),
    HARDBREAKS("[%hardbreaks]"),
    WHITESPACE(" "),
    BR("\r\n"),
    NEW_LINE("\r\n\r\n"),
    HBR(" +");

    private final String markup;

    AsciiDoc(final String markup) {
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
}
