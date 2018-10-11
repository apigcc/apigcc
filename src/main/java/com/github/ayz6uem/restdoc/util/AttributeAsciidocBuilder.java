package com.github.ayz6uem.restdoc.util;

import io.github.swagger2markup.markup.builder.MarkupBlockStyle;
import io.github.swagger2markup.markup.builder.MarkupDocBuilder;
import io.github.swagger2markup.markup.builder.internal.asciidoc.AsciiDoc;
import io.github.swagger2markup.markup.builder.internal.asciidoc.AsciiDocBuilder;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 扩展 markup的文档构建器
 * 增加文档属性输出
 */
public class AttributeAsciidocBuilder extends AsciiDocBuilder {

    public static final Map<MarkupBlockStyle, String> BLOCK_STYLE = new HashMap<MarkupBlockStyle, String>() {{
        put(MarkupBlockStyle.EXAMPLE, "====");
        put(MarkupBlockStyle.LISTING, "----");
        put(MarkupBlockStyle.LITERAL, "....");
        put(MarkupBlockStyle.PASSTHROUGH, "++++");
        put(MarkupBlockStyle.SIDEBAR, "****");
    }};

    public static AttributeAsciidocBuilder newInstance(){
        return new AttributeAsciidocBuilder();
    }

    public MarkupDocBuilder documentTitle(String title) {
        return documentTitle(title, ":doctype: book", ":toc: left");
    }

    public MarkupDocBuilder documentTitle(String title, String ... attrs) {
        Validate.notBlank(title, "title must not be blank");
        documentBuilder.append(AsciiDoc.DOCUMENT_TITLE)
                .append(replaceNewLinesWithWhiteSpace(title)).append(newLine);
        for (String attr : attrs) {
            documentBuilder.append(attr).append(newLine);
        }
        documentBuilder.append(newLine);
        return this;
    }

    public MarkupDocBuilder block(Consumer<AttributeAsciidocBuilder> consumer) {
        return block(consumer, null);
    }

    public MarkupDocBuilder block(Consumer<AttributeAsciidocBuilder> consumer, String language) {
        if (language != null){
            documentBuilder.append(String.format("[source,%s]", language)).append(newLine);
        }
        Validate.notNull(consumer,"listingBlock consumer can not be null");
        documentBuilder.append(BLOCK_STYLE.get(MarkupBlockStyle.LITERAL)).append(newLine);
        consumer.accept(this);
        documentBuilder.append(BLOCK_STYLE.get(MarkupBlockStyle.LITERAL)).append(newLine);
        documentBuilder.append(newLine);
        return this;
    }

}
