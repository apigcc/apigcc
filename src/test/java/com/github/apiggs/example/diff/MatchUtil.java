package com.github.apiggs.example.diff;

import org.junit.Assert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchUtil {

    public static void compare(Path template, Path build){
        compare(readFile(template),readFile(build));
    }
    public static void compare(String templateText, String buildText){
        MatchPatcher matchPatcher = new MatchPatcher();
        matchPatcher.Patch_Margin = 20;
        LinkedList<MatchPatcher.Diff> diffs = matchPatcher.diff_main(templateText,buildText, true);
        boolean changed = hasChange(diffs);
        if(changed){
            StringBuilder msg = new StringBuilder();
            matchPatcher.diff_cleanupSemantic(diffs);
            LinkedList<MatchPatcher.Patch> patches = matchPatcher.patch_make(templateText, diffs);
            patches.forEach(patch->{
                msg.append("----------------------").append("\r\n");
                patch.diffs.forEach(diff -> {
                    switch (diff.operation) {
                        case DELETE:
                            msg.append("-("+br(diff.text)+")");
                            break;
                        case INSERT:
                            msg.append("+("+br(diff.text)+")");
                            break;
                        case EQUAL:
                            msg.append(br(diff.text));
                            break;
                    }
                });
                msg.append("\r\n");

            });
            System.err.print(msg.toString());
        }

        Assert.assertFalse(changed);
        System.out.println("BUILD SUCCESS");
    }

    private static boolean hasChange(LinkedList<MatchPatcher.Diff> diffs){
        for (MatchPatcher.Diff diff : diffs) {
            if(!diff.operation.equals(MatchPatcher.Operation.EQUAL)){
                return true;
            }
        }
        return false;
    }

    private static String br(String text){
        return text.replaceAll("¶","\r\n");
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
            BufferedReader bufRead = new BufferedReader(input)){
            String line = bufRead.readLine();
            while (line != null) {
                sb.append(line).append('\n');
                line = bufRead.readLine();
            }
        }catch (Exception e){
            throw new IllegalArgumentException(e.getMessage(),e);
        }
        return sb.toString();
    }


    public static int lineNumber(String text) {
        Matcher m = Pattern.compile("(\r\n)|(\r)|(\n)").matcher(text);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }

    public static int lineNumber(String text, int index) {
        return lineNumber(text.substring(0, index));
    }

    public static int previousLine(String text, int index, int margin) {
        if (index < text.length() && margin > 0) {
            for (int i = index; i >= 0; i--) {
                if (i == 0) {
                    return i;
                }
                char c = text.charAt(i);
                if (('\r' == c && '\n' != text.charAt(i + 1)) || '\n' == c) {
                    margin--;
                }
                if (margin == 0) {
                    if(i == index){
                        return i;
                    }
                    return i+1;
                }
            }
        }
        return index;
    }

    public static int nextLine(String text, int index, int margin) {
        if (index < text.length() && margin > 0) {
            for (int i = index; i < text.length(); i++) {
                if (i == text.length() - 1) {
                    return i;
                }
                char c = text.charAt(i);
                if (('\r' == c && '\n' != text.charAt(i + 1)) || '\n' == c) {
                    margin--;
                }
                if (margin == 0) {
                    return i;
                }
            }
        }
        return index;
    }

}
