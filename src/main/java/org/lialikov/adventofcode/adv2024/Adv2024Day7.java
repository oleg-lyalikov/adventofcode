package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Adv2024Day7 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 3749
        System.out.println(getPart1("2024/2024-12-07-sample.txt"));
        // 4998764814652
        System.out.println(getPart1("2024/2024-12-07.txt"));
        // 11387
        System.out.println(getPart2("2024/2024-12-07-sample.txt"));
        // 37598910447546
        System.out.println(getPart2("2024/2024-12-07.txt"));

        System.out.println(new Date());
    }

    private static final Pattern P = Pattern.compile("(\\d+):(.*)");

    private static long getPart1(String inputFile) {
        return solve(inputFile, false);
    }

    private static long solve(String inputFile, boolean allowConcat) {
        List<String> lines = FileUtil.readLines(inputFile);
        long res = 0;
        for (String line : lines) {
            Matcher m = P.matcher(line);
            if (!m.matches()) {
                throw new IllegalStateException("Cannot be matched: " + line);
            }
            long number = Long.parseLong(m.group(1));
            List<Long> parts = Arrays.stream(m.group(2).split("\\s+"))
                    .filter(s -> !s.isBlank())
                    .map(Long::parseLong)
                    .toList();
            if (isSolvable(number, parts, allowConcat)) {
                res += number;
            }
        }
        return res;
    }

    private static boolean isSolvable(long initialNumber, List<Long> parts, boolean allowConcat) {
        Queue<Eq> toCheck = new LinkedList<>();
        toCheck.add(new Eq(initialNumber, parts, parts.size() - 1));
        while (!toCheck.isEmpty()) {
            Eq eq = toCheck.poll();
            long next = eq.parts.get(eq.index);
            if (eq.index == 0) {
                if (eq.number - next == 0) {
                    return true;
                }
                continue;
            }
            toCheck.add(new Eq(eq.number - next, parts, eq.index - 1));
            if (eq.number % next == 0) {
                toCheck.add(new Eq(eq.number / next, parts, eq.index - 1));
            }
            if (allowConcat) {
                int length = (int) (Math.log10(next) + 1);
                long tenPowered = (long) Math.pow(10, length);
                if ((eq.number - next) % tenPowered == 0) {
                    toCheck.add(new Eq((eq.number - next) / tenPowered, parts, eq.index - 1));
                }
            }
        }
        return false;
    }

    private static long getPart2(String inputFile) {
        return solve(inputFile, true);
    }

    public record Eq(
            long number,
            List<Long> parts,
            int index
    ) { }
}
