package com.apigcc.render;

import com.apigcc.Apigcc;
import com.apigcc.Context;
import com.apigcc.common.helper.FileHelper;
import com.apigcc.common.markup.MarkupBuilder;
import com.apigcc.common.markup.asciidoc.AsciiDoc;
import com.apigcc.schema.*;
import com.google.common.collect.Lists;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 渲染adoc文件
 */
public class AsciidocRender implements ProjectRender {

    public static final CharSequence[] attrs = Lists.newArrayList(
            AsciiDoc.attr(AsciiDoc.DOCTYPE, AsciiDoc.BOOK),
            AsciiDoc.attr(AsciiDoc.TOC, AsciiDoc.LEFT), AsciiDoc.attr(AsciiDoc.TOC_LEVEL, 2), AsciiDoc.attr(AsciiDoc.TOC_TITLE, "TOC"),
            AsciiDoc.attr(AsciiDoc.SOURCE_HIGHLIGHTER, AsciiDoc.HIGHLIGHTJS)).toArray(new CharSequence[0]);

    @Override
    public void render(Project project) {

        project.getBooks().forEach((name, book) -> {
            MarkupBuilder builder = MarkupBuilder.getInstance();
            String displayName = project.getName();
            if(!Objects.equals(Book.DEFAULT, name)){
                displayName += " - " + name;
            }
            builder.header(displayName, attrs);
            if (Objects.nonNull(project.getVersion())) {
                builder.paragraph("version:" + project.getVersion());
            }
            builder.paragraph(project.getDescription());
            for (Chapter chapter : book.getChapters()) {
                if (chapter.isIgnore() || chapter.getSections().isEmpty()) {
                    continue;
                }
                builder.title(1, chapter.getName());
                builder.paragraph(chapter.getDescription());
                for (Section section : chapter.getSections()) {
                    builder.title(2, section.getName());
                    builder.paragraph(section.getDescription());


                    builder.title(4, "request");
                    builder.listing(b -> {
                        b.textLine(section.getRequestLine());
                        section.getInHeaders().forEach(b::textLine);
                        if (section.hasRequestBody()) {
                            b.br();
                            b.text(section.getParameterString());
                        }
                    }, "source,HTTP");

                    table(builder, section.getRequestRows().values());

                    builder.title(4, "response");

                    builder.listing(b -> {
                        section.getOutHeaders().forEach(builder::textLine);
                        if (section.hasResponseBody()) {
                            b.br();
                            b.text(section.getResponseString());
                        }else{
                            b.text("N/A");
                        }
                    }, "source,JSON");

                    table(builder, section.getResponseRows().values());

                }
            }

            Path buildPath = Apigcc.getInstance().getContext().getBuildPath();
            Path projectBuildPath = buildPath.resolve(project.getId());
            Path adocFile = projectBuildPath.resolve(name + AsciiDoc.EXTENSION);
            FileHelper.write(adocFile, builder.getContent());
        });

    }

    private void table(MarkupBuilder builder, Collection<Row> rows) {
        if (rows.size() > 0) {
            List<List<String>> responseTable = new ArrayList<>();
            responseTable.add(Lists.newArrayList("Key", "Type", "Condition", "Def", "Remark"));
            rows.forEach(row -> responseTable.add(Lists.newArrayList(row.getKey(), row.getType(), row.getCondition(), row.getDef(), row.getRemark())));
            builder.table(responseTable, true, false);
        }
    }

}
