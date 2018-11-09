package com.github.apiggs.common.markup.asciidoc;

import com.github.apiggs.common.markup.MarkupBuilder;
import com.github.apiggs.common.Assert;
import com.google.common.base.Strings;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;

import static com.github.apiggs.common.markup.asciidoc.AsciiDoc.*;

public class AsciiDocBuilder implements MarkupBuilder {

    public static final int MAX_TITLE = 6;

    private StringBuilder content = new StringBuilder();

    @Override
    public MarkupBuilder header(String text, CharSequence... attrs) {
        Assert.notBlank(text, "header must not be blank");
        content.append(HEADER);
        content.append(nobr(text.trim()));
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
        Assert.notBlank(text, "header must not be blank");
        Assert.between(level, 1, MAX_TITLE, "title level can not be " + level);
        br();
        content.append(Strings.repeat(TITLE.toString(), level + 1)).append(WHITESPACE)
                .append(nobr(text.trim()));
        br();
        return this;
    }

    @Override
    public MarkupBuilder text(String text) {
        if (Assert.isBlank(text)) {
            return this;
        }
        content.append(text.trim());
        return this;
    }

    @Override
    public MarkupBuilder textLine(String text) {
        if (Assert.isBlank(text)) {
            return this;
        }
        text(nobr(text));
        br();
        return this;
    }

    @Override
    public MarkupBuilder paragraph(String text, CharSequence... attrs) {
        if (Assert.isBlank(text)) {
            return this;
        }
        content.append(HARDBREAKS);
        br();
        if (attrs.length > 0) {
            content.append("[");
            for (CharSequence attr : attrs) {
                content.append(attr).append(WHITESPACE);
            }
            content.append("]");
            br();
        }
        text(text);
        newLine();
        return this;
    }

    @Override
    public MarkupBuilder note(String text) {
        paragraph(text, NOTE);
        return this;
    }

    @Override
    public MarkupBuilder tip(String text) {
        paragraph(text, TIP);
        return this;
    }

    @Override
    public MarkupBuilder important(String text) {
        paragraph(text, IMPORTANT);
        return this;
    }

    @Override
    public MarkupBuilder warning(String text) {
        paragraph(text, WARNING);
        return this;
    }

    @Override
    public MarkupBuilder caution(String text) {
        paragraph(text, CAUTION);
        return this;
    }

    @Override
    public MarkupBuilder block(Consumer<MarkupBuilder> consumer, CharSequence flag, CharSequence... attrs) {
        if (attrs.length > 0) {
            content.append("[");
            for (CharSequence attr : attrs) {
                content.append(attr).append(" ");
            }
            content.append("]");
            br();
        }
        content.append(flag);
        br();
        consumer.accept(this);
        br();
        content.append(flag);
        newLine();
        return this;

    }

    @Override
    public MarkupBuilder listing(Consumer<MarkupBuilder> consumer, CharSequence... attrs) {
        return block(consumer, LISTING, attrs);
    }

    @Override
    public MarkupBuilder literal(Consumer<MarkupBuilder> consumer, CharSequence... attrs) {
        return block(consumer, LITERAL, attrs);
    }

    @Override
    public MarkupBuilder sidebar(Consumer<MarkupBuilder> consumer, CharSequence... attrs) {
        return block(consumer, SIDEBAR, attrs);
    }

    @Override
    public MarkupBuilder comment(Consumer<MarkupBuilder> consumer, CharSequence... attrs) {
        return block(consumer, COMMENT, attrs);
    }

    @Override
    public MarkupBuilder passthrough(Consumer<MarkupBuilder> consumer, CharSequence... attrs) {
        return block(consumer, PASSTHROUGH, attrs);
    }

    @Override
    public MarkupBuilder quote(Consumer<MarkupBuilder> consumer, CharSequence... attrs) {
        return block(consumer, QUOTE, attrs);
    }

    @Override
    public MarkupBuilder example(Consumer<MarkupBuilder> consumer, CharSequence... attrs) {
        return block(consumer, EXAMPLE, attrs);
    }

    @Override
    public MarkupBuilder list(String text) {
        return list(text, LIST_FLAG);
    }

    @Override
    public MarkupBuilder list(String text, CharSequence flag) {
        if (!Assert.isBlank(text)) {
            content.append(flag).append(nobr(text));
        }
        return this;
    }

    @Override
    public MarkupBuilder url(String text, String url) {
        if (!Assert.isBlank(text) && !!Assert.isBlank(url)) {
            content.append(url).append("[").append(nobr(text)).append("]");
            br();
        }
        return this;
    }

    @Override
    public MarkupBuilder image(String text, String url) {
        if (!Assert.isBlank(text) && !Assert.isBlank(url)) {
            text("image:");
            url(text, url);
        }
        return this;
    }

    @Override
    public MarkupBuilder table(List<List<String>> data) {
        return table(data, true, false);
    }

    @Override
    public MarkupBuilder table(List<List<String>> data, boolean header, boolean footer) {
        int min = 1;
        if (header) {
            min++;
        }
        if (footer) {
            min++;
        }
        if (data.size() < min) {
            return this;
        }
        content.append("[options=\"");
        if (header) {
            content.append("header");
        }
        if (header && footer) {
            content.append(",");
        }
        if (footer) {
            content.append("footer");
        }
        content.append("\"]");
        br();
        content.append(TABLE);
        br();
        for (List<String> rows : data) {
            for (String cell : rows) {
                content.append(TABLE_CELL);
                monospaced(cell.replace(TABLE_CELL, "\\" + TABLE_CELL));
            }
            br();
        }
        content.append(TABLE);
        newLine();
        return this;
    }


    public MarkupBuilder style(CharSequence flag, String text, CharSequence... textStyle) {
        if (Assert.isBlank(text)) {
            return this;
        }
        if (Objects.nonNull(textStyle) && textStyle.length > 0) {
            content.append("[");
            for (CharSequence style : textStyle) {
                content.append(style).append(" ");
            }
            content.append("]");
        }
        content.append(flag);
        text(text);
        content.append(flag);
        return this;

    }

    @Override
    public MarkupBuilder emphasized(String text, CharSequence... textStyle) {
        return style(EMPHASIZED, text, textStyle);
    }

    @Override
    public MarkupBuilder strong(String text, CharSequence... textStyle) {
        return style(STRONG, text, textStyle);
    }

    @Override
    public MarkupBuilder monospaced(String text, CharSequence... textStyle) {
        return style(MONOSPACED, text, textStyle);
    }

    @Override
    public MarkupBuilder quoted(String text, CharSequence... textStyle) {
        return style(QUOTE, text, textStyle);
    }

    @Override
    public MarkupBuilder doubleQuoted(String text, CharSequence... textStyle) {
        return style(DOUBLE_QUOTED, text, textStyle);
    }

    @Override
    public MarkupBuilder unquoted(String text, CharSequence... textStyle) {
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
        br();
        return this;
    }

    @Override
    public String getContent() {
        return content.toString();
    }

    @Override
    public void clean() {
        content = new StringBuilder();
    }

    String nobr(String content) {
        if (Assert.isBlank(content)) {
            return content;
        }
        return content.replaceAll(BR.toString(),
                Matcher.quoteReplacement(WHITESPACE.toString()));
    }

}
