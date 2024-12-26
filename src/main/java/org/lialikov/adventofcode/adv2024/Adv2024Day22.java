package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Adv2024Day22 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 37327623
        System.out.println(getPart1("2024/2024-12-22-sample1.txt"));
        // 12759339434
        System.out.println(getPart1("2024/2024-12-22.txt"));

        // 23
        System.out.println(getPart2("2024/2024-12-22-sample2.txt"));
        // 1405
        System.out.println(getPart2("2024/2024-12-22.txt"));

        System.out.println(new Date());
    }

    private static long getPart1(String inputFile) {
        List<Integer> in = FileUtil.readLines(inputFile).stream()
                .map(Integer::parseInt)
                .toList();

        long res = 0;
        for (int v : in) {
            res += getNextSecret(v, 2000);
        }
        return res;
    }

    private static final long prune = 16777216;

    private static long mixPrune(long v, long mix) {
        v = v ^ mix;
        v = v % prune;
        return v;
    }

    @SuppressWarnings("SameParameterValue")
    private static long getNextSecret(int v, int nTimes) {
        long value = v;
        for (int i = 0; i < nTimes; i++) {
            value = getNextSecret(value);
        }
        return value;
    }

    private static long getNextSecret(long v) {
        long value = v;
        long v1 = value * 64;
        value = mixPrune(value, v1);

        long v2 = value / 32;
        value = mixPrune(value, v2);

        long v3 = value * 2048;
        value = mixPrune(value, v3);

        return value;
    }

    private static long getPart2(String inputFile) {
        List<Integer> in = FileUtil.readLines(inputFile).stream()
                .map(Integer::parseInt)
                .toList();

        Map<Integer, Map<Key, Integer>> prices = new HashMap<>();
        for (int j = 0; j < in.size(); j++) {
            Map<Key, Integer> currentPrices = new HashMap<>();
            prices.put(j, currentPrices);
            long current = in.get(j);
            int currentDigit = (int) (current % 10);
            Deque<Integer> changes = new LinkedList<>();
            for (int i = 0; i < 2000; i++) {
                long next = getNextSecret(current);
                int nextDigit = (int) (next % 10);
                int diff = nextDigit - currentDigit;

                if (changes.size() < 4) {
                    changes.add(diff);
                } else {
                    changes.poll();
                    changes.add(diff);
                }
                if (changes.size() == 4) {
                    Key key = new Key(new ArrayList<>(changes));
                    currentPrices.putIfAbsent(key, nextDigit);
                }

                current = next;
                currentDigit = nextDigit;
            }
        }

        Set<Key> keys = prices.values().stream()
                .flatMap(pp -> pp.keySet().stream())
                .collect(Collectors.toSet());

        long max = 0;
        for (Key key : keys) {
            long keyValue = 0;
            for (int i = 0; i < in.size(); i++) {
                Integer price = prices.get(i).get(key);
                if (price != null) {
                    keyValue += price;
                }
            }
            if (keyValue > max) {
                max = keyValue;
            }
        }

        return max;
    }

    private record Key(List<Integer> changes) { }
}
