package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Adv2022Day4 {

    public static void main(String[] args) {
        System.out.println(getFullyContainedCount("2022/2022-12-04-sample.txt"));
        System.out.println(getFullyContainedCount("2022/2022-12-04.txt"));
        System.out.println(getOverlappingCount("2022/2022-12-04-sample.txt"));
        System.out.println(getOverlappingCount("2022/2022-12-04.txt"));
    }

    private static final Pattern PATTERN = Pattern.compile("(\\d+)-(\\d+),(\\d+)-(\\d+)");

    private static long getFullyContainedCount(String file) {
        AtomicLong res = new AtomicLong(0);
        FileUtil.read(file, l -> {
            if (!StringUtils.hasText(l)) {
                return;
            }

            Matcher m = PATTERN.matcher(l);
            if (!m.matches()) {
                throw new IllegalStateException("Pattern not matched: " + l);
            }

            long r11 = Long.parseLong(m.group(1));
            long r12 = Long.parseLong(m.group(2));
            long r21 = Long.parseLong(m.group(3));
            long r22 = Long.parseLong(m.group(4));

            if (r11 > r12) {
                throw new IllegalStateException("Unexpected range: " + r11 + "-" + r12);
            }
            if (r21 > r22) {
                throw new IllegalStateException("Unexpected range: " + r21 + "-" + r22);
            }

            if (r11 >= r21 && r12 <= r22) {
                res.incrementAndGet();
            } else if (r21 >= r11 && r22 <= r12) {
                res.incrementAndGet();
            }
        });
        return res.get();
    }

    private static boolean withinRange(long n, long start, long end) {
        return n >= start && n <= end;
    }

    private static long getOverlappingCount(String file) {
        AtomicLong res = new AtomicLong(0);
        FileUtil.read(file, l -> {
            if (!StringUtils.hasText(l)) {
                return;
            }

            Matcher m = PATTERN.matcher(l);
            if (!m.matches()) {
                throw new IllegalStateException("Pattern not matched: " + l);
            }

            long r11 = Long.parseLong(m.group(1));
            long r12 = Long.parseLong(m.group(2));
            long r21 = Long.parseLong(m.group(3));
            long r22 = Long.parseLong(m.group(4));

            if (r11 > r12) {
                throw new IllegalStateException("Unexpected range: " + r11 + "-" + r12);
            }
            if (r21 > r22) {
                throw new IllegalStateException("Unexpected range: " + r21 + "-" + r22);
            }

            if (withinRange(r11, r21, r22) || withinRange(r12, r21, r22)) {
                res.incrementAndGet();
            } else if (withinRange(r21, r11, r12) || withinRange(r22, r11, r12)) {
                res.incrementAndGet();
            }
        });
        return res.get();
    }
}
