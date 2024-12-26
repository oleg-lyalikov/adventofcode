package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Adv2024Day25 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 3
        System.out.println(getPart1("2024/2024-12-25-sample.txt"));
        // 2803. That's not the right answer; your answer is too low.
        // 3077
        System.out.println(getPart1("2024/2024-12-25.txt"));

        System.out.println(new Date());
    }

    private static Input read(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);

        List<String> current = new ArrayList<>();
        List<List<String>> all = new ArrayList<>();
        for (String line : lines) {
            if (line.isBlank()) {
                all.add(current);
                current = new ArrayList<>();
                continue;
            }
            current.add(line);
        }
        if (!current.isEmpty()) {
            all.add(current);
        }

        List<Item> locks = new ArrayList<>();
        List<Item> keys = new ArrayList<>();
        for (List<String> item : all) {
            boolean isLock = item.get(0).chars().allMatch(ch -> ch == '#');
            int space = 0;
            for (String line : item) {
                if (line.chars().allMatch(ch -> ch == '.')) {
                    space++;
                }
            }
            List<Integer> curr = new ArrayList<>(item.get(0).length());
            for (int j = 0; j < item.get(0).length(); j++) {
                int count = 0;
                for (int i = 1; i < item.size() - 1; i++) {
                    if (item.get(i).charAt(j) == '#') {
                        count++;
                    }
                }
                curr.add(count);
            }
            Item i = new Item(curr, space, item.size() - 1);
            if (isLock) {
                locks.add(i);
            } else {
                keys.add(i);
            }
        }
        return new Input(locks, keys);
    }

    private static long getPart1(String inputFile) {
        Input in = read(inputFile);

        long res = 0;
        for (Item lock : in.locks) {
            for (Item key : in.keys) {
                if (lock.value.size() != key.value.size()) {
                    throw new IllegalStateException("Should not happen");
                }
                int max = Math.max(lock.height, key.height);

                boolean match = true;
                for (int i = 0; i < lock.value.size(); i++) {
                    if ((lock.value.get(i) + key.value.get(i)) >= max) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    res++;
                }
            }
        }

        return res;
    }

    private record Input(List<Item> locks, List<Item> keys) { }
    private record Item(List<Integer> value, int space, int height) { }
}
