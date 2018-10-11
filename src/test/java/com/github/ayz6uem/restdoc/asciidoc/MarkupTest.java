package com.github.ayz6uem.restdoc.asciidoc;

import com.github.ayz6uem.restdoc.util.AttributeAsciidocBuilder;
import io.github.swagger2markup.markup.builder.MarkupAdmonition;
import io.github.swagger2markup.markup.builder.MarkupBlockStyle;
import io.github.swagger2markup.markup.builder.MarkupTableColumn;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarkupTest {

    private final String newLine = System.getProperty("line.separator");

    private List<MarkupTableColumn> tableColumns;
    private List<List<String>> tableCells;

    @Before
    public void setUp() {
        tableColumns = Arrays.asList(
                new MarkupTableColumn().withHeader("Header1"),
                new MarkupTableColumn().withWidthRatio(2),
                new MarkupTableColumn().withHeader("Header3").withWidthRatio(1).withHeaderColumn(true));
        tableCells = new ArrayList<>();
        tableCells.add(Arrays.asList("Row 1,Column 1", "Row 1,Column 2", "Row 1,Column 3"));
        tableCells.add(Arrays.asList("Row 2,Column 1", "Row 2,Column 2", "Row 2,Column 3"));
    }


    @Test
    public void testAsciiDoc() throws IOException, URISyntaxException {
        AttributeAsciidocBuilder builder = AttributeAsciidocBuilder.newInstance();

        builder.documentTitle("Test title",":doctype: book")
                .sectionTitleLevel(1, "Section Level 1a")
                .sectionTitleWithAnchorLevel(1, "Section with anchor Level 1a", "level-1a")
                .sectionTitleWithAnchorLevel(1, "Section with anchor Level 1a")
                .sectionTitleLevel(2, "Section Level 2a")
                .sectionTitleWithAnchorLevel(2, "Section with anchor Level 2a", "level-2a")
                .sectionTitleWithAnchorLevel(2, "Section with anchor Level 2a")
                .sectionTitleLevel(3, "Section Level 3a")
                .sectionTitleWithAnchorLevel(3, "Section with anchor Level 3a", "level-3a")
                .sectionTitleWithAnchorLevel(3, "Section with anchor Level 3a")
                .sectionTitleLevel(4, "Section Level 4a")
                .sectionTitleWithAnchorLevel(4, "Section with anchor Level 4a", "level-4a")
                .sectionTitleWithAnchorLevel(4, "Section with anchor Level 4a")
                .sectionTitleLevel(5, "Section Level 5a")
                .sectionTitleWithAnchorLevel(5, "Section with anchor Level 5a", "level-5a")
                .sectionTitleWithAnchorLevel(5, "Section with anchor Level 5a")
                .paragraph("Paragraph with long text bla bla bla bla bla")
                .paragraph("\rLine1\nLine2\r\n", false)
                .paragraph("\rLine1\nLine2\r\n", true)
                .listingBlock("Source code listing")
                .listingBlock("MarkupDocBuilder builder = MarkupDocBuilders.documentBuilder(MarkupLanguage.MARKDOWN)", "java")
                .block("Example", MarkupBlockStyle.EXAMPLE)
                .block("Example", MarkupBlockStyle.EXAMPLE, "Example", null)
                .block("Example", MarkupBlockStyle.EXAMPLE, null, MarkupAdmonition.IMPORTANT)
                .block("Listing", MarkupBlockStyle.LISTING, null, MarkupAdmonition.CAUTION)
                .block("Literal", MarkupBlockStyle.LITERAL, null, MarkupAdmonition.NOTE)
                .block("Sidebar", MarkupBlockStyle.SIDEBAR, null, MarkupAdmonition.TIP)
                .block("Passthrough", MarkupBlockStyle.PASSTHROUGH, null, MarkupAdmonition.WARNING)
                .pageBreak()
                .table(tableCells)
                .tableWithColumnSpecs(tableColumns, tableCells)
                .sectionTitleLevel1("Section Level 1b")
                .sectionTitleLevel2("Section Level 2b")
                .textLine("text line", true)
                .literalTextLine("Literal text line", true)
                .boldTextLine("Bold text line", true)
                .italicTextLine("Italic text line", true)
                .boldText("bold").italicText("italic").text("regular").newLine(true)
                .unorderedList(Arrays.asList("Entry1", "Entry2", "Entry 2"))
                .anchor("anchor", "text").newLine()
                .anchor(" Simple    anchor").newLine()
                .anchor("  \u0240 µ&|ù This .:/-_#  ").newLine()
                .crossReferenceRaw("./document.adoc", "anchor", "text").newLine(true)
                .crossReferenceRaw("  \u0240 µ&|ù This .:/-_  ").newLine(true)
                .crossReference("./document.adoc", "anchor", "text").newLine(true)
                .crossReference("  \u0240 µ&|ù This .:/-_  ").newLine(true)
                .listingBlock("GET http://{host}}/users/{id}");

        Path outputFile = Paths.get("out/adoc/test");

        builder.writeToFileWithoutExtension(builder.addFileExtension(outputFile), StandardCharsets.UTF_8);
        builder.writeToFile(outputFile, StandardCharsets.UTF_8);

//        Path expectedFile = Paths.get(MarkupTest.class.getResource("/expected/asciidoc/test.adoc").toURI());
//        builder.addFileExtension(outputFile);
//        DiffUtils.assertThatFileIsEqual(expectedFile, builder.addFileExtension(outputFile), "testAsciiDoc.html");
    }


}
