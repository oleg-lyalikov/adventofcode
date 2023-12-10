package org.lialikov.adventofcode.util;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParseUtil {
    public static List<Long> parse(String string) {
        return Arrays.stream(string.split("\\s+"))
                .filter(StringUtils::hasText)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
