package com.github.apiggs.markup.asciidoc;

public class TextStyle {

    public static final String BIG = "big";
    public static final String SMALL = "small";
    public static final String UNDERLINE = "underline";
    public static final String OVERLINE = "overline";
    public static final String LINETHROUGH = "line-through";

    /**
     * https://en.wikipedia.org/wiki/Web_colors#HTML_color_names
     */
    public enum Color{
        White,
        Silver,
        Gray,
        Black,
        Red,
        Maroon,
        Yellow,
        Olive,
        Lime,
        Green,
        Aqua,
        Teal,
        Blue,
        Navy,
        Fuchsia,
        Purple
    }

    public static String BG(Color color){
        return color+"-background";
    }



}
