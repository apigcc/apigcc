package com.github.apiggs.example;

import com.github.apiggs.util.loging.Logger;
import com.github.apiggs.util.loging.LoggerFactory;
import io.github.robwin.diff.DiffAssertions;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiffUtil {

    static Logger LOGGER = LoggerFactory.getLogger(DiffUtil.class);

    public static void assertThatAllFilesAreEqual(Path expectedDirectory, Path actualDirectory, String reportName) {
        Path reportPath = Paths.get("build/diff-report/", reportName);
        try {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(expectedDirectory)) {
                for (Path expectedFile : directoryStream) {
                    Path actualFile = actualDirectory.resolve(expectedFile.getFileName());
                    LOGGER.info("Diffing file {} with {}", actualFile, expectedFile);
                    DiffAssertions.assertThat(actualFile).isEqualTo(expectedFile, reportPath);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to assert that all files are equal", e);
        }
    }

    public static void assertThatFileIsEqual(Path expectedFile, Path actualFile, String reportName) {
        Path reportPath = Paths.get("build/diff-report/", reportName);
        LOGGER.info("Diffing file {} with {}", actualFile, expectedFile);
        DiffAssertions.assertThat(actualFile).isEqualTo(expectedFile, reportPath);
    }
}
