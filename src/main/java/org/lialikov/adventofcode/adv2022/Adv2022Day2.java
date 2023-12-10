package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Adv2022Day2 {

    public static void main(String[] args) {
        System.out.println(calcTotalScore("2022/2022-12-02-sample.txt"));
        System.out.println(calcTotalScoreV2("2022/2022-12-02-sample.txt"));
        System.out.println(calcTotalScore("2022/2022-12-02.txt"));
        System.out.println(calcTotalScoreV2("2022/2022-12-02.txt"));
    }

    private static final Map<Pair<String, String>, Integer> scores = Map.of(
            Pair.of("A", "X"), 3,
            Pair.of("A", "Y"), 6,
            Pair.of("A", "Z"), 0,
            Pair.of("B", "X"), 0,
            Pair.of("B", "Y"), 3,
            Pair.of("B", "Z"), 6,
            Pair.of("C", "X"), 6,
            Pair.of("C", "Y"), 0,
            Pair.of("C", "Z"), 3
    );

    private static final Map<String, Integer> shapeScore = Map.of(
            "X", 1,
            "Y", 2,
            "Z", 3
    );

    private static BigInteger calcTotalScoreV2(String file) {
        AtomicReference<BigInteger> res = new AtomicReference<>(BigInteger.ZERO);

        Map<Pair<String, String>, String> mapping = Map.of(
                Pair.of("A", "X"), "Z",
                Pair.of("A", "Y"), "X",
                Pair.of("A", "Z"), "Y",
                Pair.of("B", "X"), "X",
                Pair.of("B", "Y"), "Y",
                Pair.of("B", "Z"), "Z",
                Pair.of("C", "X"), "Y",
                Pair.of("C", "Y"), "Z",
                Pair.of("C", "Z"), "X"
        );

        FileUtil.read(file, s -> {
            if (!StringUtils.hasText(s)) {
                return;
            }
            String[] split = s.split("\\s+");
            String first = split[0].trim().toUpperCase(Locale.ROOT);
            String second = split[1].trim().toUpperCase(Locale.ROOT);

            final String secondMapped = mapping.get(Pair.of(first, second));
            if (secondMapped == null) {
                throw new IllegalStateException("Unexpected line: " + s);
            }

            res.set(res.get().add(BigInteger.valueOf(getRoundScore(first, secondMapped))));
        });
        return res.get();
    }

    private static BigInteger calcTotalScore(String file) {
        AtomicReference<BigInteger> res = new AtomicReference<>(BigInteger.ZERO);

        FileUtil.read(file, s -> {
            if (!StringUtils.hasText(s)) {
                return;
            }
            String[] split = s.split("\\s+");
            String first = split[0].trim().toUpperCase(Locale.ROOT);
            String second = split[1].trim().toUpperCase(Locale.ROOT);

            res.set(res.get().add(BigInteger.valueOf(getRoundScore(first, second))));
        });
        return res.get();
    }

    private static int getRoundScore(String first, String second) {
        final Integer match = scores.get(Pair.of(first, second));
        if (match == null) {
            throw new IllegalStateException("Unexpected line");
        }

        final Integer shape = shapeScore.get(second);
        if (shape == null) {
            throw new IllegalStateException("Cannot happen");
        }

        return match + shape;
    }
}
