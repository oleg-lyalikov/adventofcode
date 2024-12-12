package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Adv2024Day2 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 2
        System.out.println(getPart1("2024/2024-12-02-sample.txt"));
        // 606
        System.out.println(getPart1("2024/2024-12-02.txt"));
        // 4
        System.out.println(getPart2("2024/2024-12-02-sample.txt"));
        // 644
        System.out.println(getPart2("2024/2024-12-02.txt"));

        System.out.println(new Date());
    }

    private static boolean isSafe(List<Integer> report) {
        int last = report.get(0);
        Boolean increasing = null;
        boolean safe = true;
        for (int i = 1; i < report.size(); i++) {
            int next = report.get(i);
            if (next == last) {
                safe = false;
            } else if (i == 1 && next > last) {
                increasing = true;
            } else if (i == 1) {
                increasing = false;
            }

            if (next > last && !increasing) {
                safe = false;
            } else if (next < last && increasing) {
                safe = false;
            } else if (Math.abs(next - last) > 3) {
                safe = false;
            }
            if (!safe) {
                break;
            }
            last = next;
        }
        return safe;
    }

    private static long getPart1(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        AtomicLong res = new AtomicLong();
        lines.forEach(l -> {
            List<Integer> report = Arrays.stream(l.split("\\s+")).map(Integer::parseInt).toList();
            if (isSafe(report)) {
                res.incrementAndGet();
            }
        });
        return res.get();
    }

    private static long getPart2(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        AtomicLong res = new AtomicLong();
        lines.forEach(l -> {
            List<Integer> report = Arrays.stream(l.split("\\s+")).map(Integer::parseInt).toList();
            if (isSafe(report)) {
                res.incrementAndGet();
            } else {
                for (int i = 0; i < report.size(); i++) {
                    List<Integer> copy = new ArrayList<>(report);
                    //noinspection SuspiciousListRemoveInLoop
                    copy.remove(i);
                    if (isSafe(copy)) {
                        res.incrementAndGet();
                        break;
                    }
                }
            }
        });
        return res.get();
    }
}
