package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Adv2022Day3 {

    public static void main(String[] args) {
        System.out.println(getPrioritiesSum("2022/2022-12-03-sample.txt"));
        System.out.println(getPrioritiesSum("2022/2022-12-03.txt"));
        System.out.println(getPrioritiesGroupsSum("2022/2022-12-03-sample.txt"));
        System.out.println(getPrioritiesGroupsSum("2022/2022-12-03.txt"));
    }

    private static int getPriority(Character c) {
        final int res;
        if (Character.isUpperCase(c)) {
            res = (c - 'A') + 27;
        } else {
            res = (c - 'a') + 1;
        }
        if (res < 1 || res > 52) {
            throw new IllegalStateException("Unexpected char: " + c);
        }
        return res;
    }

    private static Set<Character> toSet(String s) {
        return s.chars().mapToObj(c -> (char) c).collect(Collectors.toSet());
    }

    public static BigInteger getPrioritiesSum(String file) {
        AtomicReference<BigInteger> res = new AtomicReference<>(BigInteger.ZERO);
        FileUtil.read(file, s -> {
            if (!StringUtils.hasText(s)) {
                return;
            }

            String part1 = s.substring(0, s.length() / 2);
            String part2 = s.substring(s.length() / 2);
            if (part1.length() != part2.length()) {
                throw new IllegalStateException("Unexpected string: " + s);
            }

            Set<Character> chars1 = toSet(part1);
            Set<Character> chars2 = toSet(part2);

            boolean found = false;
            for (Character c : chars1) {
                if (chars2.contains(c)) {
                    res.set(res.get().add(BigInteger.valueOf(getPriority(c))));
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalStateException("Char not found: " + s);
            }
        });

        return res.get();
    }

    public static BigInteger getPrioritiesGroupsSum(String file) {
        AtomicReference<BigInteger> res = new AtomicReference<>(BigInteger.ZERO);
        final int groupSize = 3;
        final String[] group = new String[groupSize];
        final AtomicInteger index = new AtomicInteger(0);
        FileUtil.read(file, s -> {
            if (!StringUtils.hasText(s)) {
                return;
            }

            group[index.getAndIncrement()] = s;

            if (index.get() > 2) {
                index.set(0);

                Set<Character> chars1 = toSet(group[0]);
                Set<Character> chars2 = toSet(group[1]);
                Set<Character> chars3 = toSet(group[2]);

                boolean found = false;
                for (Character c : chars1) {
                    if (chars2.contains(c) && chars3.contains(c)) {
                        if (found) {
                            throw new IllegalStateException("Already found another char");
                        }
                        res.set(res.get().add(BigInteger.valueOf(getPriority(c))));
                        found = true;
                    }
                }

                if (!found) {
                    throw new IllegalStateException("Char not found: " + s);
                }
            }
        });

        if (index.get() != 0) {
            throw new IllegalStateException("Missing last computation");
        }
        return res.get();
    }
}
