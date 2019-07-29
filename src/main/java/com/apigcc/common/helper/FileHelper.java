package com.apigcc.common.helper;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.apigcc.Context.DEFAULT_CODE_STRUCTURE;

@Slf4j
public class FileHelper {

    public static void write(Path file, String content) {

        if (file.getParent() != null) {
            try {
                Files.createDirectories(file.getParent());
            } catch (IOException e) {
                throw new RuntimeException("Failed create directory", e);
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file", e);
        }
    }

    public static List<Path> find(Path start, String structure){
        try {
            return Files.walk(start)
                    .filter(p->p.endsWith(structure)).collect(Collectors.toList());
        } catch (IOException e) {
            log.warn("find path error:{} {}", start, e.getMessage());
        }
        return Lists.newArrayList();
    }

}
