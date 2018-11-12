package com.apigcc.example.diff;

import com.google.common.base.Charsets;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.LinkedList;

public class MatchUtil {

    public Path templateHtml;
    public Path resultHtml;

    public MatchUtil(Path templateHtml, Path resultHtml) {
        this.templateHtml = templateHtml;
        this.resultHtml = resultHtml;
    }

    public void compare(Path template, Path build) {
        compare(readFile(template), readFile(build));
    }

    public void compare(String templateText, String buildText) {
        MatchPatcher matchPatcher = new MatchPatcher();
        matchPatcher.Patch_Margin = 20;
        LinkedList<MatchPatcher.Diff> diffs = matchPatcher.diff_main(templateText, buildText, true);
        int changed = hasChange(diffs);
        if (changed > 0) {
            rederHtml(matchPatcher.diff_prettyHtml(diffs));
        }
        Assert.assertEquals(0, changed);
        System.out.println("BUILD SUCCESS");
    }

    private void rederHtml(String results) {
        String[] lines = br(results).replaceAll("<span>|</span>", "").split("\n");
        String html = readFile(templateHtml);
        html = html.replace("${content}", lines(lines));
        writeFile(resultHtml, html, Charsets.UTF_8);
        FileSystem.open(resultHtml);
    }

    private String lines(String[] lines) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            stringBuilder.append("<tr><td class=\"line-numbers\">").append(i)
                    .append("</td><td>")
                    .append(lines[i]).append("</td></tr>");
        }
        return stringBuilder.toString();
    }

    private static int hasChange(LinkedList<MatchPatcher.Diff> diffs) {
        int changed = 0;
        for (MatchPatcher.Diff diff : diffs) {
            if (!diff.operation.equals(MatchPatcher.Operation.EQUAL)) {
                changed++;
            }
        }
        return changed;
    }

    private static String br(String text) {
        return text.replaceAll("&para;", "");
    }


    /**
     * 读取文件内容
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String readFile(Path path) {
        // Read a file from disk and return the text contents.
        StringBuilder sb = new StringBuilder();
        try (FileReader input = new FileReader(path.toFile());
             BufferedReader bufRead = new BufferedReader(input)) {
            String line = bufRead.readLine();
            while (line != null) {
                sb.append(line).append('\n');
                line = bufRead.readLine();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        return sb.toString();
    }

    public void writeFile(Path file, String content, Charset charset, OpenOption... openOptions) {
        if (file.getParent() != null) {
            try {
                Files.createDirectories(file.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed create directory", e);
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file, charset, openOptions)) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file", e);
        }
    }

}
