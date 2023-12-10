package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Adv2022Day10 {

    public static void main(String[] args) {
        System.out.println(getSignalStrengthSum("2022/2022-12-10-sample.txt"));
        System.out.println(getSignalStrengthSum("2022/2022-12-10.txt"));
        drawCrt("2022/2022-12-10-sample.txt");
        drawCrt("2022/2022-12-10.txt");
    }

    private static final Pattern ADDX = Pattern.compile("addx\\s+(-?\\d+)");

    private static long getSignalStrengthSum(String file) {
        AtomicInteger cycle = new AtomicInteger(1);
        AtomicLong x = new AtomicLong(1);
        AtomicLong res = new AtomicLong(0);
        FileUtil.read(file, l -> {
            boolean noop = l.trim().equals("noop");
            if (noop) {
                cycle.incrementAndGet();
                res.addAndGet(getStrength(cycle.get(), x.get()));
            } else {
                Matcher m = ADDX.matcher(l);
                if (!m.matches()) {
                    throw new IllegalStateException("Unexpected line: " + l);
                }
                int addX = Integer.parseInt(m.group(1));

                res.addAndGet(getStrength(cycle.get() + 1, x.get()));

                x.addAndGet(addX);
                cycle.addAndGet(2);
                res.addAndGet(getStrength(cycle.get(), x.get()));
            }
        });
        return res.get();
    }

    private static long getStrength(int cycle, long x) {
        if ((cycle - 20) % 40 == 0) {
            return cycle * x;
        }
        return 0;
    }

    private static void drawCrt(String file) {
        AtomicInteger cycle = new AtomicInteger(1);
        AtomicLong x = new AtomicLong(1);
        drawScreen(cycle.get(), x.get());
        FileUtil.read(file, l -> {
            boolean noop = l.trim().equals("noop");
            if (noop) {
                cycle.incrementAndGet();
                drawScreen(cycle.get(), x.get());
            } else {
                Matcher m = ADDX.matcher(l);
                if (!m.matches()) {
                    throw new IllegalStateException("Unexpected line: " + l);
                }
                int addX = Integer.parseInt(m.group(1));

                drawScreen(cycle.get() + 1, x.get());

                x.addAndGet(addX);
                cycle.addAndGet(2);
                drawScreen(cycle.get(), x.get());
            }
        });
    }

    private static void drawScreen(int cycle, long x) {
        int crt = (cycle % 40) - 1;
        if (crt == -1) {
            crt = 39;
        }
        if (crt == 0) {
            System.out.println();
        }
        if (x > (crt - 2) && x < (crt + 2)) {
            System.out.print("#");
        } else {
            System.out.print('.');
        }
    }
}
