package org.lialikov.adventofcode.util;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParseUtil {
    public static List<Long> parse(String string) {
        return parse(string, "\\s+");
    }

    public static List<Long> parse(String string, String regex) {
        return Arrays.stream(string.trim().split(regex))
                .filter(StringUtils::hasText)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public static List<Character> asCharList(String string) {
        return string.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
    }
}
