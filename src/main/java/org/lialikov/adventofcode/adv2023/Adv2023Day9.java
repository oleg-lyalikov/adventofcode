package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.lialikov.adventofcode.util.ParseUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Adv2023Day9 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 114
        System.out.println(getExtrapolatedSum("2023/2023-12-09-sample.txt"));
        // 1584748274
        System.out.println(getExtrapolatedSum("2023/2023-12-09.txt"));

        // 2
        System.out.println(getExtrapolatedBackwardSum("2023/2023-12-09-sample.txt"));
        // 1026
        System.out.println(getExtrapolatedBackwardSum("2023/2023-12-09.txt"));

        System.out.println(new Date());
    }

    public static long getExtrapolatedSum(String inputFile) {
        List<List<Long>> ranges = new ArrayList<>();
        FileUtil.read(inputFile, s -> ranges.add(ParseUtil.parse(s)));

        long res = 0;
        for (List<Long> range : ranges) {
            res += getExtrapolated(range);
        }
        return res;
    }

    public static long getExtrapolatedBackwardSum(String inputFile) {
        List<List<Long>> ranges = new ArrayList<>();
        FileUtil.read(inputFile, s -> ranges.add(ParseUtil.parse(s)));

        long res = 0;
        for (List<Long> range : ranges) {
            res += getExtrapolatedBackward(range);
        }
        return res;
    }

    public static long getExtrapolated(List<Long> range) {
        List<List<Long>> ranges = new ArrayList<>();
        ranges.add(range);
        List<Long> current = range;
        while (current.size() > 1 && !current.stream().allMatch(l -> l == 0)) {
            List<Long> newRange = new ArrayList<>(current.size() - 1);
            for (int i = 1; i < current.size(); i++) {
                newRange.add(current.get(i) - current.get(i - 1));
            }
            ranges.add(newRange);
            current = newRange;
        }
        long res = 0;
        for (List<Long> r : ranges) {
            res += r.get(r.size() - 1);
        }
        return res;
    }

    public static long getExtrapolatedBackward(List<Long> range) {
        List<List<Long>> ranges = new ArrayList<>();
        ranges.add(range);
        List<Long> current = range;
        while (current.size() > 1 && !current.stream().allMatch(l -> l == 0)) {
            List<Long> newRange = new ArrayList<>(current.size() - 1);
            for (int i = 1; i < current.size(); i++) {
                newRange.add(current.get(i) - current.get(i - 1));
            }
            ranges.add(newRange);
            current = newRange;
        }
        Long currentV = null;
        for (int i = ranges.size() - 1; i >=0 ;i--) {
            current = ranges.get(i);
            if (currentV == null) {
                currentV = current.get(0);
                continue;
            }
            currentV = current.get(0) - currentV;
        }
        return currentV;
    }
}
