package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;

public class Adv2023Day15 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 1320
        System.out.println(getPart1("2023/2023-12-15-sample.txt"));
        // 519041
        System.out.println(getPart1("2023/2023-12-15.txt"));
        // 145
        System.out.println(getPart2("2023/2023-12-15-sample.txt"));
        System.out.println(getPart2("2023/2023-12-15.txt"));

        System.out.println(new Date());
    }

    public static long getPart2(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        if (lines.size() != 1) {
            throw new IllegalStateException("!!!");
        }
        String[] parts = lines.get(0).split(",");
        long res = 0;
        Map<Long, List<Lens>> map = new HashMap<>();
        for (String part: parts) {
            Lens lens = new Lens();
            int minusIndex = part.indexOf("-");
            if (minusIndex > 0) {
                lens.label = part.substring(0, minusIndex);
            }
            int equalIndex = part.indexOf("=");
            if (equalIndex >= 0) {
                lens.label = part.substring(0, equalIndex);
                lens.value = Integer.parseInt(part.substring(equalIndex + 1));
            }

            long hash = getHash(lens.label);
            map.computeIfAbsent(hash, h -> new ArrayList<>());
            List<Lens> list = map.get(hash);
            if (minusIndex > 0) {
                list.removeIf(next -> next.label.equals(lens.label));
            } else if (equalIndex > 0) {
                boolean found = false;
                for (Lens next : list) {
                    if (next.label.equals(lens.label)) {
                        next.value = lens.value;
                        found = true;
                    }
                }
                if (!found) {
                    list.add(lens);
                }
            }
        }

        for (long i = 0; i < 256; i++) {
            List<Lens> list = map.get(i);
            if (list == null) {
                continue;
            }
            long box = i + 1;
            for (int j = 0; j < list.size(); j++) {
                int lensI = j + 1;
                long value = box * lensI * list.get(j).value;
                res += value;
            }
        }
        return res;
    }

    public static long getPart1(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        if (lines.size() != 1) {
            throw new IllegalStateException("!!!");
        }
        String[] parts = lines.get(0).split(",");
        long res = 0;
        for (String part: parts) {
            long hash = getHash(part);
            res += hash;
        }
        return res;
    }

    private static long getHash(String part) {
        long res = 0;
        for (char ch : part.toCharArray()) {
            res += ch;
            res *= 17;
            res = res % 256;
        }
        return res;
    }

    private static class Lens {
        String label;
        int value;
    }
}
