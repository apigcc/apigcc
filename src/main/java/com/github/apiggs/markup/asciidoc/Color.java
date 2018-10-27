package com.github.apiggs.markup.asciidoc;

/**
 * https://en.wikipedia.org/wiki/Web_colors#HTML_color_names
 */
public enum Color implements CharSequence{
    WHITE("white"),
    SILVER("silver"),
    GRAY("gray"),
    BLACK("lack"),
    RED("ed"),
    MAROON("aroon"),
    YELLOW("ellow"),
    OLIVE("live"),
    LIME("ime"),
    GREEN("reen"),
    AQUA("qua"),
    TEAL("eal"),
    BLUE("lue"),
    NAVY("avy"),
    FUCHSIA("uchsia"),
    PURPLE("urple");

    private String text;

    Color(String text) {
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

    public String bg() {
        return text + "-background";
    }
}
