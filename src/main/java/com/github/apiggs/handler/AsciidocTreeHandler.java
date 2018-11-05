package com.github.apiggs.handler;

import com.github.apiggs.Environment;
import com.github.apiggs.http.HttpMessage;
import com.github.apiggs.http.HttpRequest;
import com.github.apiggs.http.HttpResponse;
import com.github.apiggs.markup.MarkupBuilder;
import com.github.apiggs.markup.asciidoc.AsciiDoc;
import com.github.apiggs.schema.Appendix;
import com.github.apiggs.util.Cell;
import com.github.apiggs.schema.Group;
import com.github.apiggs.schema.Tree;
import com.github.apiggs.util.loging.Logger;
import com.github.apiggs.util.loging.LoggerFactory;
import com.google.common.base.Strings;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * adoc文件构建器
 */
public class AsciidocTreeHandler implements TreeHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());
    MarkupBuilder builder = MarkupBuilder.getInstance();

    @Override
    public void handle(Tree tree, Environment env) {
        builder.header(tree.getName(), AsciiDoc.DOCTYPE_BOOK, AsciiDoc.TOC_LEFT);
        if (Objects.nonNull(tree.getVersion())) {
            builder.paragraph("version:" + tree.getVersion());
        }
        if (Objects.nonNull(tree.getDescription())) {
            builder.paragraph(tree.getDescription());
        }

        int section = 1;

        if (!Strings.isNullOrEmpty(tree.getReadme())) {
            builder.title(1, section + " 文档说明");
            builder.paragraph(tree.getReadme());
            section++;
        }

        for (Group group : tree.getBucket().getGroups()) {
            buildGroup(group, section++);
        }

        if (!tree.getAppendices().isEmpty()) {
            builder.title(1, section + " 附录");

            for (int i = 0; i < tree.getAppendices().size(); i++) {
                Appendix appendix = tree.getAppendices().get(i);
                if (!appendix.getCells().isEmpty()) {
                    builder.title(2, section + "." + (i + 1) + " " + appendix.getName());

                    table(appendix.getCells());
                }
            }

            section++;
        }

        try {
            Path adoc = env.getOutPath().resolve(env.getId() + AsciiDoc.EXTENSION);
            write(adoc, builder.getContent(), StandardCharsets.UTF_8);
            log.info("Build {}", adoc);
        } finally {
            builder.clean();
        }
    }


    private void buildGroup(Group group, int num) {
        builder.title(1, num + " " + group.getName());
        if (Objects.nonNull(group.getDescription())) {
            builder.paragraph(group.getDescription());
        }

        int index = 0;
        for (HttpMessage httpMessage : group.getNodes()) {
            buildHttpMessage(httpMessage, num + ".", ++index);
        }

    }

    private void buildHttpMessage(HttpMessage message, String prefix, int num) {
        builder.title(2, prefix + num + " " + message.getName());
        if (Objects.nonNull(message.getDescription())) {
            builder.paragraph(message.getDescription());
        }

        HttpRequest request = message.getRequest();
        builder.listing(builder -> {
            for (String uri : request.getUris()) {
                builder.textLine(request.getMethod()
                        + " "
                        + uri
                        + request.queryString()
                        + " "
                        + message.getVersion());
            }
            request.getHeaders().forEach((k, v) -> builder.textLine(k + ": " + v));
            if (request.hasBody()) {
                builder.br();
                builder.text(request.bodyString());
            }
        }, "source,REQUEST");

        ntcdd(request.getCells());

        HttpResponse response = message.getResponse();
        if (!response.isEmpty()) {
            builder.listing(builder -> {
                builder.textLine(message.getVersion() + " " + response.getStatus());
                response.getHeaders().forEach((k, v) -> builder.textLine(k + ": " + v));
                if (response.hasBody()) {
                    builder.br();
                    builder.text(response.bodyString());
                }
            }, "source,RESPONSE");
            ntcdd(response.getCells());
        }

    }

    private void ntcdd(List<Cell<String>> cells) {
        if (cells.size() > 0) {
            List<List<String>> responseTable = new ArrayList<>();
            responseTable.add(Arrays.asList("NAME", "TYPE", "CONDITION", "DEFAULT", "DESCRIPTION"));
            cells.forEach(parameter -> responseTable.add(parameter.toList()));
            builder.table(responseTable);
        }
    }

    private void table(List<Cell<String>> cells) {
        if (cells.size() > 0) {
            List<List<String>> responseTable = new ArrayList<>();
            cells.forEach(parameter -> responseTable.add(parameter.toList()));
            builder.table(responseTable,false,false);
        }
    }

}
