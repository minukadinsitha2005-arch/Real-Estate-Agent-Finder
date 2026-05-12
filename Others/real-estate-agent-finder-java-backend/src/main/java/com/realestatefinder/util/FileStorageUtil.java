package com.realestatefinder.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public final class FileStorageUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static Path baseDirectory;

    private FileStorageUtil() {
    }

    public static synchronized void initialize(String directoryPath) {
        if (directoryPath == null || directoryPath.isBlank()) {
            directoryPath = System.getProperty("java.io.tmpdir") + "/real-estate-agent-finder-data";
        }
        baseDirectory = Path.of(directoryPath);
        try {
            Files.createDirectories(baseDirectory);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize file storage directory.", e);
        }
    }

    public static synchronized void appendLine(String fileName, String message) {
        ensureInitialized();
        Path filePath = baseDirectory.resolve(fileName);
        String timestampedMessage = "[" + LocalDateTime.now().format(FORMATTER) + "] " + message + System.lineSeparator();
        try {
            Files.writeString(filePath, timestampedMessage, StandardCharsets.UTF_8,
                    Files.exists(filePath)
                            ? java.nio.file.StandardOpenOption.APPEND
                            : java.nio.file.StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write file log: " + fileName, e);
        }
    }

    public static synchronized List<String> readLastLines(String fileName, int limit) {
        ensureInitialized();
        Path filePath = baseDirectory.resolve(fileName);
        if (!Files.exists(filePath)) {
            return Collections.emptyList();
        }
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            if (limit <= 0 || lines.size() <= limit) {
                return lines;
            }
            return lines.subList(lines.size() - limit, lines.size());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private static void ensureInitialized() {
        if (baseDirectory == null) {
            initialize(null);
        }
    }
}
