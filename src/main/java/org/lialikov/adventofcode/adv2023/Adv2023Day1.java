package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Adv2023Day1 {

    public static void main(String[] args) {
        System.out.println(getCalibrationSum("2023/2023-12-01-sample.txt"));
        System.out.println(getCalibrationSum("2023/2023-12-01.txt"));
        System.out.println(getCalibrationSum2("2023/2023-12-01-sample-2.txt"));
        System.out.println(getCalibrationSum2("2023/2023-12-01.txt"));
    }

    public static long getCalibrationSum(String inputFile) {
        AtomicLong res = new AtomicLong(0);
        FileUtil.read(inputFile, s -> {
            if (StringUtils.hasText(s)) {
                char first = 0;
                char last = 0;
                for (char ch : s.toCharArray()) {
                    if (Character.isDigit(ch)) {
                        if (first == 0) {
                            first = ch;
                        }
                        last = ch;
                    }
                }
                if (first != 0) {
                    res.addAndGet(Long.parseLong("" + first + last));
                }
            }
        });
        return res.get();
    }

    private static final Map<String, Integer> MAP = Map.of(
         "one", 1,
         "two", 2,
         "three", 3,
         "four", 4,
         "five", 5,
         "six", 6,
         "seven", 7,
         "eight", 8,
         "nine", 9
    );

    public static long getCalibrationSum2(String inputFile) {
        AtomicLong res = new AtomicLong(0);
        FileUtil.read(inputFile, s -> {
            if (StringUtils.hasText(s)) {
                AtomicInteger first = new AtomicInteger(-1);
                AtomicInteger firstIndex = new AtomicInteger(-1);
                AtomicInteger last = new AtomicInteger(-1);
                AtomicInteger lastIndex = new AtomicInteger(-1);
                for (int i = 0; i < s.length(); i++) {
                    char ch = s.charAt(i);
                    if (Character.isDigit(ch)) {
                        int parsed = Integer.parseInt("" + ch);
                        if (first.get() == -1) {
                            first.set(parsed);
                            firstIndex.set(i);
                        }
                        last.set(parsed);
                        lastIndex.set(i);
                    }
                }
                MAP.forEach((key, value) -> {
                    int i = s.indexOf(key);
                    if (i < 0) {
                        return;
                    }
                    if (i < firstIndex.get() || firstIndex.get() == -1) {
                        first.set(value);
                        firstIndex.set(i);
                    }
                    i = s.lastIndexOf(key);
                    if (i > lastIndex.get() || lastIndex.get() == -1) {
                        last.set(value);
                        lastIndex.set(i);
                    }
                });
                if (first.get() == -1 || last.get() == -1) {
                    throw new IllegalStateException("First: " + first.get() + ", last: " + last.get());
                }

                long number = first.get() * 10L + last.get();
                res.addAndGet(number);
            }
        });
        return res.get();
    }
}
