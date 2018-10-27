package com.github.apiggs.handler;

import com.github.apiggs.Environment;
import com.github.apiggs.schema.Tree;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

/**
 * 文档结构树访问器
 */
public interface TreeHandler {

    void handle(Tree tree, Environment env);

    default void write(Path file, String content, Charset charset, OpenOption... openOptions) {
        if (file.getParent() != null) {
            try {
                Files.createDirectories(file.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed create directory", e);
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file, charset, openOptions)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file", e);
        }
    }

}
