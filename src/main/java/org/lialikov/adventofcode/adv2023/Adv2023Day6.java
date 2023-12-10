package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Adv2023Day6 {

    public static void main(String[] args) {
        // 288
        System.out.println(getNWaysMultiplied("2023/2023-12-06-sample.txt"));
        // 503424
        System.out.println(getNWaysMultiplied("2023/2023-12-06.txt"));
        // 71503
        System.out.println(getNWays2("2023/2023-12-06-sample.txt"));
        // 32607562
        System.out.println(getNWays2("2023/2023-12-06.txt"));
    }

    private static List<Long> parse(String string) {
        return Arrays.stream(string.split("\\s+"))
                .filter(StringUtils::hasText)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
    private static long removeSpacesAndParse(String string) {
        return Long.parseLong(string.replace(" ", ""));
    }

    public static long getNWaysMultiplied(String inputFile) {
        AtomicReference<List<Long>> times = new AtomicReference<>();
        AtomicReference<List<Long>> distances = new AtomicReference<>();
        FileUtil.read(inputFile, s -> {
            if (s.startsWith("Time")) {
                times.set(parse(s.substring(s.indexOf(":") + 1)));
            } else if (s.startsWith("Distance")) {
                distances.set(parse(s.substring(s.indexOf(":") + 1)));
            }
        });

        long res = 0;
        for (int i = 0; i < times.get().size(); i++) {
            long time = times.get().get(i);
            long distance = distances.get().get(i);
            long matches = getMatches(time, distance);
            if (res == 0) {
                res = 1;
            }
            res *= matches;
        }
        return res;
    }

    public static long getNWays2(String inputFile) {
        AtomicLong time = new AtomicLong();
        AtomicLong distance = new AtomicLong();
        FileUtil.read(inputFile, s -> {
            if (s.startsWith("Time")) {
                time.set(removeSpacesAndParse(s.substring(s.indexOf(":") + 1)));
            } else if (s.startsWith("Distance")) {
                distance.set(removeSpacesAndParse(s.substring(s.indexOf(":") + 1)));
            }
        });
        return getMatches(time.get(), distance.get());
    }

    public static long getMatches(long time, long distance) {
        boolean isOdd = time % 2 == 0;
        long bestI = time / 2;
        long d = bestI * (time - bestI);
        long matches = 0;
        while (d > distance && bestI >= 0) {
            matches++;
            bestI--;
            d = bestI * (time - bestI);
        }
        matches *= 2;
        if (isOdd) {
            matches--;
        }
        return matches;
    }
}
