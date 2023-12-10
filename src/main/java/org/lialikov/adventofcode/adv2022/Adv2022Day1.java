package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.math.BigInteger.ZERO;

public class Adv2022Day1 {

    public static void main(String[] args) {
        System.out.println(getCalories("2022/2022-12-01.txt"));
        System.out.println(getTop3Calories("2022/2022-12-01.txt"));
    }

    public static BigInteger getCalories(String inputFile) {
        AtomicReference<BigInteger> current = new AtomicReference<>(ZERO);
        AtomicReference<BigInteger> max = new AtomicReference<>(ZERO);
        FileUtil.read(inputFile, s -> {
            if (!StringUtils.hasText(s)) {
                if (current.get().compareTo(max.get()) > 0) {
                    max.set(current.get());
                }
                current.set(ZERO);
                return;
            }

            BigInteger currentLine = new BigInteger(s);
            current.set(current.get().add(currentLine));
        });
        return max.get();
    }

    public static BigInteger getTop3Calories(String inputFile) {
        int size = 3;
        BigInteger[] top = new BigInteger[size];
        for (int i = 0 ; i < size; i++) {
            top[i] = ZERO;
        }
        AtomicReference<BigInteger> current = new AtomicReference<>(ZERO);
        FileUtil.read(inputFile, s -> {
            if (!StringUtils.hasText(s)) {
                for (int i = 0; i < size; i++) {
                    if (current.get().compareTo(top[i]) > 0) {
                        BigInteger temp = top[i];
                        top[i] = current.get();
                        current.set(temp);
                    }
                }

                current.set(ZERO);
                return;
            }

            BigInteger currentLine = new BigInteger(s);
            current.set(current.get().add(currentLine));
        });

        BigInteger res = ZERO;
        for (int i = 0; i < size; i++) {
            res = res.add(top[i]);
        }
        return res;
    }
}
