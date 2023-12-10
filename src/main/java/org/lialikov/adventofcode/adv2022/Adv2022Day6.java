package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Adv2022Day6 {

    public static void main(String[] args) {
        System.out.println(getStartOfPacketIndex("2022/2022-12-06-sample-1.txt"));
        System.out.println(getStartOfPacketIndex("2022/2022-12-06-sample-2.txt"));
        System.out.println(getStartOfPacketIndex("2022/2022-12-06-sample-3.txt"));
        System.out.println(getStartOfPacketIndex("2022/2022-12-06-sample-4.txt"));
        System.out.println(getStartOfPacketIndex("2022/2022-12-06-sample-5.txt"));
        System.out.println(getStartOfPacketIndex("2022/2022-12-06.txt"));

        System.out.println(getStartOfMessageIndex("2022/2022-12-06-sample-1.txt"));
        System.out.println(getStartOfMessageIndex("2022/2022-12-06-sample-2.txt"));
        System.out.println(getStartOfMessageIndex("2022/2022-12-06-sample-3.txt"));
        System.out.println(getStartOfMessageIndex("2022/2022-12-06-sample-4.txt"));
        System.out.println(getStartOfMessageIndex("2022/2022-12-06-sample-5.txt"));
        System.out.println(getStartOfMessageIndex("2022/2022-12-06.txt"));
    }

    private static int getStartOfPacketIndex(String file) {
        return getDistinctSetStart(file, 4);
    }

    private static int getStartOfMessageIndex(String file) {
        return getDistinctSetStart(file, 14);
    }

    private static int getDistinctSetStart(String file, int nDistinct) {
        AtomicInteger res = new AtomicInteger(0);
        FileUtil.read(file, l -> {
            char[] chars = l.toCharArray();
            for (int i = 0; i < chars.length - nDistinct + 1; i++) {
                Set<Character> set = new HashSet<>();
                for (int j = 0; j < nDistinct; j++) {
                    set.add(chars[i + j]);
                }
                if (set.size() == nDistinct) {
                    res.set(i + nDistinct);
                    return;
                }
            }
        });
        return res.get();
    }
}
