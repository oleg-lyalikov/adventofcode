package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;

import java.util.*;

public class Adv2024Day5 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 143
        System.out.println(getPart1("2024/2024-12-05-sample.txt"));
        // 6949
        System.out.println(getPart1("2024/2024-12-05.txt"));
        // 123
        System.out.println(getPart2("2024/2024-12-05-sample.txt"));
        // 4145
        System.out.println(getPart2("2024/2024-12-05.txt"));

        System.out.println(new Date());
    }

    private static Pair<Map<Integer, Set<Integer>>, List<List<Integer>>> readInput(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        Map<Integer, Set<Integer>> order = new HashMap<>();
        List<List<Integer>> updates = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            if (line.contains("|")) {
                String[] ordered = line.split("\\|");
                if (ordered.length != 2) {
                    throw new IllegalStateException("Unexpected line: " + line);
                }
                int key = Integer.parseInt(ordered[0]);
                order.computeIfAbsent(key, k -> new HashSet<>()).add(Integer.parseInt(ordered[1]));
            } else {
                List<Integer> update = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
                if (update.size() % 2 != 1) {
                    throw new IllegalStateException("Even length: " + update);
                }
                updates.add(update);
            }
        }
        return Pair.of(order, updates);
    }

    private static boolean isCorrectlyOrdered(List<Integer> update, Map<Integer, Set<Integer>> order) {
        boolean valid = true;
        for (int i = 0; i < update.size() - 1; i++) {
            int n = update.get(i);
            if (order.get(n) == null || !order.get(n).contains(update.get(i + 1))) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    private static long getPart1(String inputFile) {
        Pair<Map<Integer, Set<Integer>>, List<List<Integer>>> input = readInput(inputFile);
        Map<Integer, Set<Integer>> order = input.getFirst();
        List<List<Integer>> updates = input.getSecond();

        long res = 0;
        for (List<Integer> update : updates) {
            if (isCorrectlyOrdered(update, order)) {
                res += update.get(update.size() / 2);
            }
        }

        return res;
    }

    private static void set(List<Integer> update, int index, int number) {
        if (update.size() < index + 1) {
            update.add(number);
        } else {
            update.set(index, number);
        }
    }

    private static List<Integer> fix(List<Integer> update, Map<Integer, Set<Integer>> order) {
        List<Integer> fixed = new ArrayList<>(update.size());
        for (int i = 0; i < update.size() - 1; i++) {
            int n = fixed.size() == (i + 1) ? fixed.get(i) : update.get(i);
            int next = update.get(i + 1);
            if (order.get(n) != null && order.get(n).contains(next)) {
                set(fixed, i, n);
                set(fixed, i + 1, next);
            } else {
                set(fixed, i, next);
                set(fixed, i + 1, n);
            }
        }
        return fixed;
    }

    private static long getPart2(String inputFile) {
        Pair<Map<Integer, Set<Integer>>, List<List<Integer>>> input = readInput(inputFile);
        Map<Integer, Set<Integer>> order = input.getFirst();
        List<List<Integer>> updates = input.getSecond();

        long res = 0;
        for (List<Integer> update : updates) {
            if (!isCorrectlyOrdered(update, order)) {
                List<Integer> fixed = fix(update, order);
                while (!isCorrectlyOrdered(fixed, order)) {
                    fixed = fix(fixed, order);
                }
                res += fixed.get(fixed.size() / 2);
            }
        }

        return res;
    }
}
