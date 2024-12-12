package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.Assert;

import java.util.*;

public class Adv2024Day1 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 11
        System.out.println(getPart1("2024/2024-12-01-sample.txt"));
        // 1834060
        System.out.println(getPart1("2024/2024-12-01.txt"));
        // 31
        System.out.println(getPart2("2024/2024-12-01-sample.txt"));
        // 21607792
        System.out.println(getPart2("2024/2024-12-01.txt"));

        System.out.println(new Date());
    }

    private static long getPart1(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        lines.forEach(l -> {
            String[] line = l.split("\\s+");
            Assert.isTrue(line.length == 2, "");
            first.add(Integer.parseInt(line[0]));
            second.add(Integer.parseInt(line[1]));
        });
        Collections.sort(first);
        Collections.sort(second);
        long res = 0;
        for (int i = 0; i < first.size(); i++) {
            int distance = Math.abs(first.get(i) - second.get(i));
            res += distance;
        }
        return res;
    }

    private static long getPart2(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        lines.forEach(l -> {
            String[] line = l.split("\\s+");
            Assert.isTrue(line.length == 2, "");
            first.add(Integer.parseInt(line[0]));
            second.add(Integer.parseInt(line[1]));
        });

        Map<Integer, Integer> times = new HashMap<>();
        second.forEach(n -> {
            int old = times.computeIfAbsent(n, nn -> 0);
            times.put(n, old + 1);
        });
        long res = 0;
        for (int n : first) {
            Integer nTimes = times.get(n);
            if (nTimes != null) {
                res += ((long) n * nTimes);
            }
        }
        return res;
    }
}
