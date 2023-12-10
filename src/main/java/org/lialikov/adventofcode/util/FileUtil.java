package org.lialikov.adventofcode.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {

    public static void read(String filename, Consumer<String> consumer) {
        //noinspection ConstantConditions
        try (Stream<String> stream = Files.lines(Paths.get(FileUtil.class.getResource("/" + filename).toURI()), StandardCharsets.UTF_8)) {
            stream.forEach(consumer);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Character> toCharList(String str) {
        return str.chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toList());
    }
}
