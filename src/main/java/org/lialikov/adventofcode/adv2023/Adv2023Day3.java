package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Adv2023Day3 {

    public static void main(String[] args) {
        //System.out.println(getEnginePartsSum("2023/2023-12-03-sample.txt"));
        //System.out.println(getEnginePartsSum("2023/2023-12-03.txt"));
        //System.out.println(getGearRationSum("2023/2023-12-03-sample.txt"));
        System.out.println(getGearRationSum("2023/2023-12-03.txt"));
    }

    final static AtomicBoolean isNumber = new AtomicBoolean(false);
    final static AtomicInteger number = new AtomicInteger(-1);
    final static AtomicInteger numberXStart = new AtomicInteger(-1);
    final static AtomicInteger numberXEnd = new AtomicInteger(-1);
    final static List<Pair<Integer, Integer>> symbolsIndexes = new ArrayList<>();
    final static List<Pair<Integer, Pair<Pair<Integer, Integer>, Integer>>> numbersIndexes = new ArrayList<>();
    final static AtomicInteger y = new AtomicInteger(-1);
    final static List<Pair<Integer, Integer>> gearIndexes = new ArrayList<>();

    public static long getEnginePartsSum(String inputFile) {
        FileUtil.read(inputFile, s -> {
            clear();
            y.incrementAndGet();
            for (int x = 0; x < s.length(); x++) {
                char ch = s.charAt(x);
                if (ch == '.') {
                    clear();
                } else if (Character.isDigit(ch)) {
                    int digit = ch - '0';
                    if (!isNumber.get()) {
                        isNumber.set(true);
                        number.set(digit);
                        numberXStart.set(x);
                        numberXEnd.set(x);
                    } else {
                        number.set(number.get() * 10 + digit);
                        numberXEnd.set(x);
                    }
                } else {
                    clear();
                    symbolsIndexes.add(Pair.of(x, y.get()));
                }
            }
        });
        clear();

        AtomicLong res = new AtomicLong(0);
        for(Pair<Integer, Pair<Pair<Integer, Integer>, Integer>> numberIndex: numbersIndexes) {
            Pair<Pair<Integer, Integer>, Integer> index = numberIndex.getSecond();
            Pair<Integer, Integer> xRange = index.getFirst();
            int y = index.getSecond();
            for (Pair<Integer, Integer> symbolIndex: symbolsIndexes) {
                int symbolX = symbolIndex.getFirst();
                int symbolY = symbolIndex.getSecond();
                if (Math.abs(y - symbolY) > 1) {
                    continue;
                }
                if ((symbolX >= xRange.getFirst() - 1) && (symbolX <= xRange.getSecond() + 1)) {
                    res.addAndGet(numberIndex.getFirst());
                }
            }
        }

        return res.get();
    }

    private static void clear() {
        if (number.get() != -1) {
            addNumber(number.get(), numberXStart.get(), numberXEnd.get(), y.get());
        }
        isNumber.set(false);
        number.set(-1);
        numberXStart.set(-1);
        numberXEnd.set(-1);
    }

    private static void addNumber(int number, int xStart, int xEnd, int y) {
        numbersIndexes.add(Pair.of(number, Pair.of(Pair.of(xStart, xEnd), y)));
    }

    public static long getGearRationSum(String inputFile) {
        FileUtil.read(inputFile, s -> {
            clear();
            y.incrementAndGet();
            for (int x = 0; x < s.length(); x++) {
                char ch = s.charAt(x);
                if (ch == '.') {
                    clear();
                } else if (Character.isDigit(ch)) {
                    int digit = ch - '0';
                    if (!isNumber.get()) {
                        isNumber.set(true);
                        number.set(digit);
                        numberXStart.set(x);
                        numberXEnd.set(x);
                    } else {
                        number.set(number.get() * 10 + digit);
                        numberXEnd.set(x);
                    }
                } else if (ch == '*') {
                    clear();
                    gearIndexes.add(Pair.of(x, y.get()));
                } else {
                    clear();
                }
            }
        });
        clear();

        AtomicLong res = new AtomicLong(0);
        for(Pair<Integer, Integer> gearIndex: gearIndexes) {
            int gearX = gearIndex.getFirst();
            int gearY = gearIndex.getSecond();
            List<Integer> numbers = new ArrayList<>();
            for (Pair<Integer, Pair<Pair<Integer, Integer>, Integer>> numberIndex: numbersIndexes) {
                Pair<Pair<Integer, Integer>, Integer> index = numberIndex.getSecond();
                Pair<Integer, Integer> xRange = index.getFirst();
                int y = index.getSecond();

                if (Math.abs(y - gearY) > 1) {
                    continue;
                }
                if ((gearX >= xRange.getFirst() - 1) && (gearX <= xRange.getSecond() + 1)) {
                    numbers.add(numberIndex.getFirst());
                }
            }
            if (numbers.size() == 2) {
                res.addAndGet((long) numbers.get(0) * numbers.get(1));
            }
        }

        return res.get();
    }
}
