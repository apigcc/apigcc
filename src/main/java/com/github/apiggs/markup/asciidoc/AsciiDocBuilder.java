package com.github.apiggs.markup.asciidoc;

import com.github.apiggs.markup.MarkupBuilder;
import com.github.apiggs.util.Validate;
import com.google.common.base.Strings;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;

import static com.github.apiggs.markup.asciidoc.AsciiDoc.*;

public class AsciiDocBuilder implements MarkupBuilder {

    public static final int MAX_TITLE = 6;

    StringBuilder content = new StringBuilder();

    @Override
    public MarkupBuilder header(String text, CharSequence ... attrs) {
        Validate.notBlank(text, "header must not be blank");
        content.append(HEADER).append(nobr(text));
        br();
        for (CharSequence attr : attrs) {
            content.append(attr);
            br();
        }
        br();
        return this;
    }

    @Override
    public MarkupBuilder title(int level, String text) {
        Validate.notBlank(text, "header must not be blank");
        Validate.between(level, 1, MAX_TITLE, "title level can not be " + level);
        br();
        content.append(Strings.repeat(TITLE.toString(), level)).append(WHITESPACE)
                .append(nobr(text));
        br();
        return this;
    }

    @Override
    public MarkupBuilder text(String text) {
        content.append(text);
        return this;
    }

    @Override
    public MarkupBuilder textLine(String text) {
        text(nobr(text));
        newLine();
        return this;
    }

    @Override
    public MarkupBuilder paragraph(String text) {
        content.append(HARDBREAKS);
        br();
        text(text);
        newLine();
        return this;
    }

    @Override
    public MarkupBuilder note(String text) {
        content.append(NOTE);
        br();
        paragraph(text);
        return this;
    }

    @Override
    public MarkupBuilder tip(String text) {
        content.append(TIP);
        br();
        paragraph(text);
        return this;
    }

    @Override
    public MarkupBuilder important(String text) {
        content.append(IMPORTANT);
        br();
        paragraph(text);
        return this;
    }

    @Override
    public MarkupBuilder warning(String text) {
        content.append(WARNING);
        br();
        paragraph(text);
        return this;
    }

    @Override
    public MarkupBuilder caution(String text) {
        content.append(CAUTION);
        br();
        paragraph(text);
        return this;
    }

    @Override
    public MarkupBuilder block(Consumer<MarkupBuilder> consumer, CharSequence flag, String ... attrs) {
        if(attrs.length>0){
            content.append("[");
            for (String attr : attrs) {
                content.append(attr).append(" ");
            }
            content.append("]");
        }
        br();
        content.append(flag);
        br();
        consumer.accept(this);
        br();
        content.append(flag);
        newLine();
        return this;

    }

    @Override
    public MarkupBuilder listing(Consumer<MarkupBuilder> consumer, String ... attrs) {
        return block(consumer, LISTING, attrs);
    }

    @Override
    public MarkupBuilder literal(Consumer<MarkupBuilder> consumer, String ... attrs) {
        return block(consumer, LITERAL, attrs);
    }

    @Override
    public MarkupBuilder sidebar(Consumer<MarkupBuilder> consumer, String ... attrs) {
        return block(consumer, SIDEBAR, attrs);
    }

    @Override
    public MarkupBuilder comment(Consumer<MarkupBuilder> consumer, String ... attrs) {
        return block(consumer, COMMENT, attrs);
    }

    @Override
    public MarkupBuilder passthrough(Consumer<MarkupBuilder> consumer, String ... attrs) {
        return block(consumer, PASSTHROUGH, attrs);
    }

    @Override
    public MarkupBuilder quote(Consumer<MarkupBuilder> consumer, String ... attrs) {
        return block(consumer, QUOTE, attrs);
    }

    @Override
    public MarkupBuilder example(Consumer<MarkupBuilder> consumer, String ... attrs) {
        return block(consumer, EXAMPLE, attrs);
    }

    @Override
    public MarkupBuilder list(String text) {
        return list(text, LIST_FLAG);
    }

    @Override
    public MarkupBuilder list(String text, CharSequence flag) {
        content.append(flag).append(nobr(text));
        return this;
    }

    @Override
    public MarkupBuilder url(String text, String url) {
        content.append(url).append("[").append(nobr(text)).append("]");
        br();
        return this;
    }

    @Override
    public MarkupBuilder image(String text, String url) {
        text("image:");
        url(text, url);
        return this;
    }

    @Override
    public MarkupBuilder table(List<List<String>> data) {
        return table(data, true, false);
    }

    @Override
    public MarkupBuilder table(List<List<String>> data, boolean header, boolean footer) {
        content.append("[options=\"");
        if(header){
            content.append("header");
        }
        if(header && footer){
            content.append(",");
        }
        if(footer){
            content.append("footer");
        }
        content.append("\"]");
        br();
        content.append(TABLE);
        br();
        for (List<String> rows : data) {
            for (String cell : rows) {
                content.append(TABLE_CELL);
                content.append(cell);
            }
            br();
        }
        content.append(TABLE);
        newLine();
        return this;
    }



    public MarkupBuilder style(CharSequence flag, String text, String ... textStyle) {
        if(Objects.nonNull(textStyle) && textStyle.length>0){
            content.append("[");
            for (String style : textStyle) {
                content.append(style).append(" ");
            }
            content.append("]");
        }
        content.append(flag).append(text).append(flag);
        return this;

    }
    @Override
    public MarkupBuilder emphasized(String text, String ... textStyle) {
        return style(EMPHASIZED, text, textStyle);
    }

    @Override
    public MarkupBuilder strong(String text, String ... textStyle) {
        return style(STRONG, text, textStyle);
    }

    @Override
    public MarkupBuilder monospaced(String text, String ... textStyle) {
        return style(MONOSPACED, text, textStyle);
    }

    @Override
    public MarkupBuilder quoted(String text, String ... textStyle) {
        return style(QUOTE, text, textStyle);
    }

    @Override
    public MarkupBuilder doubleQuoted(String text, String ... textStyle) {
        return style(DOUBLE_QUOTED, text, textStyle);
    }

    @Override
    public MarkupBuilder unquoted(String text, String ... textStyle) {
        return style(UNQUOTED, text, textStyle);
    }

    @Override
    public MarkupBuilder br() {
        content.append(BR);
        return this;
    }

    @Override
    public MarkupBuilder hbr() {
        content.append(HBR);
        return this;
    }

    @Override
    public MarkupBuilder newLine() {
        content.append(NEW_LINE);
        return this;
    }

    @Override
    public MarkupBuilder pageBreak() {
        content.append(PAGEBREAKS);
        return this;
    }

    @Override
    public String getContent() {
        return content.toString();
    }

    String nobr(String content) {
        return content.replaceAll(BR.toString(),
                Matcher.quoteReplacement(WHITESPACE.toString()));
    }

}
