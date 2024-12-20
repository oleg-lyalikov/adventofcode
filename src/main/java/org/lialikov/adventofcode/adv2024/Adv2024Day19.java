package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.util.*;

public class Adv2024Day19 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 6
        System.out.println(getPart1("2024/2024-12-19-sample.txt"));
        // 367
        System.out.println(getPart1("2024/2024-12-19.txt"));

        // 16
        System.out.println(getPart2("2024/2024-12-19-sample.txt"));
        // 724388733465031
        System.out.println(getPart2("2024/2024-12-19.txt"));

        System.out.println(new Date());
    }

    private static Input read(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        List<String> patterns = Arrays.stream(lines.get(0).split("\\s*,\\s*")).toList();
        List<String> designs = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            if (StringUtils.hasText(lines.get(i))) {
                designs.add(lines.get(i));
            }
        }
        return new Input(patterns, designs);
    }

    private static boolean isPossible(String design, List<String> patterns) {
        Set<Integer> toVisit = new HashSet<>(List.of(0));
        Set<Integer> visited = new HashSet<>();
        while (!toVisit.isEmpty()) {
            Integer current = toVisit.iterator().next();
            toVisit.remove(current);
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            for (String pattern : patterns) {
                int length = pattern.length();
                if (current + length > design.length()) {
                    continue;
                }
                String toCompare = design.substring(current, current + length);
                if (toCompare.equals(pattern)) {
                    toVisit.add(current + length);
                }
            }
        }
        return visited.contains(design.length());
    }

    private static long getPart1(String inputFile) {
        Input in = read(inputFile);
        long res = 0;
        for (String design : in.designs) {
            if (isPossible(design, in.patterns)) {
                res++;
            }
        }
        return res;
    }

    private static long getVariants(String design, List<String> patterns) {
        Set<Integer> toVisit = new HashSet<>(List.of(0));
        Map<Integer, Long> res = new HashMap<>(Map.of(0, 1L));
        Set<Integer> visited = new HashSet<>();
        while (!toVisit.isEmpty()) {
            Integer current = Integer.MAX_VALUE;
            for (Integer toCheck : toVisit) {
                if (toCheck < current) {
                    current = toCheck;
                }
            }
            toVisit.remove(current);
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            long currentRes = res.get(current);
            for (String pattern : patterns) {
                int length = pattern.length();
                int next = current + length;
                if (next > design.length()) {
                    continue;
                }
                String toCompare = design.substring(current, next);
                if (toCompare.equals(pattern)) {
                    toVisit.add(next);
                    long value = res.computeIfAbsent(next, vv -> 0L);
                    res.put(next, value + currentRes);
                }
            }
        }
        Long result = res.get(design.length());
        return result == null ? 0 : result;
    }

    private static long getPart2(String inputFile) {
        Input in = read(inputFile);
        long res = 0;
        for (String design : in.designs) {
            res += getVariants(design, in.patterns);
        }
        return res;
    }

    private record Input(List<String> patterns, List<String> designs) { }
}
