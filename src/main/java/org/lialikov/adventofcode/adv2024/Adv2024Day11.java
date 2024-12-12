package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;
import org.lialikov.adventofcode.util.ParseUtil;

import java.util.*;

import static org.lialikov.adventofcode.util.MathUtil.numOfDigits;

public class Adv2024Day11 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 55312
        System.out.println(getPart1("2024/2024-12-11-sample.txt"));
        // 203953
        System.out.println(getPart1("2024/2024-12-11.txt"));
        // 65601038650482 ?
        System.out.println(getPart2("2024/2024-12-11-sample.txt"));
        // 242090118578155
        System.out.println(getPart2("2024/2024-12-11.txt"));

        System.out.println(new Date());
    }

    private static List<Long> readInput(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        if (lines.size() != 1) {
            throw new IllegalStateException("Unexpected input: " + lines);
        }
        return ParseUtil.parse(lines.get(0));
    }

    private static long getPart1(String inputFile) {
        List<Long> stones = readInput(inputFile);
        return numOfStones(stones, 25);
    }

    @SuppressWarnings("SameParameterValue")
    private static long numOfStones(List<Long> stones, int iterations) {
        Map<Double, Long> tenPows = new HashMap<>();
        for (int i = 0; i < iterations; i++) {
            List<Long> next = new ArrayList<>();
            for (long stone : stones) {
                if (stone == 0) {
                    next.add(1L);
                    continue;
                }
                int length = numOfDigits(stone);
                if (length % 2 == 0) {
                    long tenPow = tenPows.computeIfAbsent((double) length / 2, d -> (long) Math.pow(10, d));
                    next.add(stone / tenPow);
                    next.add(stone % tenPow);
                    continue;
                }
                next.add(stone * 2024);
            }
            stones = next;
        }
        return stones.size();
    }

    private static final Map<Double, Long> tenPows = new HashMap<>();

    private static long numOfStones(long stone, int iterations, Map<StoneIteration, Long> map) {
        Long res = map.get(new StoneIteration(stone, iterations));
        if (res != null) {
            return res;
        }
        if (iterations == 0) {
            return 1;
        }
        if (stone == 0) {
            return numOfStones(1L, iterations - 1, map);
        }
        int length = numOfDigits(stone);
        if (length % 2 == 0) {
            long tenPow = tenPows.computeIfAbsent((double) length / 2, d -> (long) Math.pow(10, d));
            return numOfStones(stone / tenPow, iterations - 1, map) +
                    numOfStones(stone % tenPow, iterations - 1, map);
        }
        return numOfStones(stone * 2024, iterations - 1, map);
    }

    @SuppressWarnings("SameParameterValue")
    private static Map<StoneIteration, Long> init(int iterations, int nMax) {
        Map<StoneIteration, Long> res = new HashMap<>();
        for (int i = 1; i <= iterations; i++) {
            for (int j = 0; j < nMax; j++) {
                res.put(new StoneIteration(j, i), numOfStones(j, i, res));
            }
        }
        return res;
    }

    private static long getPart2(String inputFile) {
        List<Long> stones = readInput(inputFile);
        final int iterations = 75;
        Map<StoneIteration, Long> map = init(iterations, 100);
        long res = 0;
        for (long stone : stones) {
            res += numOfStones(stone, iterations, map);
        }
        return res;
    }

    public record StoneIteration(long stone, int i) {
    }
}
