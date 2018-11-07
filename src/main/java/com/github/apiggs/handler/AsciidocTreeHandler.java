package com.github.apiggs.handler;

import com.github.apiggs.Environment;
import com.github.apiggs.http.HttpMessage;
import com.github.apiggs.http.HttpRequest;
import com.github.apiggs.http.HttpResponse;
import com.github.apiggs.markup.MarkupBuilder;
import com.github.apiggs.schema.Appendix;
import com.github.apiggs.schema.Bucket;
import com.github.apiggs.schema.Group;
import com.github.apiggs.schema.Tree;
import com.github.apiggs.util.Cell;
import com.github.apiggs.util.loging.Logger;
import com.github.apiggs.util.loging.LoggerFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.github.apiggs.markup.asciidoc.AsciiDoc.*;

/**
 * adoc文件构建器
 */
public class AsciidocTreeHandler implements TreeHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());
    MarkupBuilder builder = MarkupBuilder.getInstance();


    @Override
    public void handle(Tree tree, Environment env) {

        List<CharSequence> attrs = Lists.newArrayList(
                attr(DOCTYPE, BOOK),
                attr(TOC, LEFT), attr(TOC_LEVEL, 3),
                attr(SOURCE_HIGHLIGHTER, PRETTIFY));

        builder.header(tree.getName(), attrs.toArray(new CharSequence[0]));
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

        if (tree.getBuckets().isEmpty()) {
            for (Group group : tree.getBucket().getGroups()) {
                buildGroup(group, 1, "", section);
                section++;
            }
        } else {
            if (buildBucket(tree.getBucket(), 1, section)) {
                section++;
            }
            for (Bucket bucket : tree.getBuckets().values()) {
                if (buildBucket(bucket, 1, section)) {
                    section++;
                }
            }
        }

        if (!tree.getAppendices().isEmpty()) {
            builder.title(1, section + " 附录");
            int index = 1;
            for (Appendix appendix : tree.getAppendices()) {

                if (!appendix.getCells().isEmpty()) {
                    builder.title(2, section + "." + (index++) + " " + appendix.getName());
                    table(appendix.getCells());
                }
            }
        }

        try {
            Path adoc = env.getOutPath().resolve(env.getId() + EXTENSION);
            write(adoc, builder.getContent(), StandardCharsets.UTF_8);
            log.info("Build {}", adoc);
        } finally {
            builder.clean();
        }
    }

    private boolean buildBucket(Bucket bucket, int level, int section) {
        if (!bucket.isEmpty()) {
            builder.title(level, section + " " + bucket.getName());
            int index = 1;
            for (Group group : bucket.getGroups()) {
                buildGroup(group, level + 1, section + ".", index++);
            }
            return true;
        }
        return false;
    }


    private void buildGroup(Group group, int level, String prefix, int num) {
        builder.title(level, prefix + num + " " + group.getName());
        if (Objects.nonNull(group.getDescription())) {
            builder.paragraph(group.getDescription());
        }

        int index = 1;
        for (HttpMessage httpMessage : group.getNodes()) {
            buildHttpMessage(httpMessage, level + 1, prefix + num + ".", index++);
        }

    }

    private void buildHttpMessage(HttpMessage message, int level, String prefix, int num) {
        builder.title(level, prefix + num + " " + message.getName());
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
            builder.table(responseTable, false, false);
        }
    }

}
