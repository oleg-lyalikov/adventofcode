package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lialikov.adventofcode.util.StringUtil.findWord;

public class Adv2024Day3 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 161
        System.out.println(getPart1("2024/2024-12-03-sample.txt"));
        // 178538786
        System.out.println(getPart1("2024/2024-12-03.txt"));
        // 48
        System.out.println(getPart2("2024/2024-12-03-sample2.txt"));
        // 102467299
        System.out.println(getPart2("2024/2024-12-03.txt"));

        System.out.println(new Date());
    }

    private static final Pattern P1 = Pattern.compile("mul\\((\\d+),(\\d+)\\)");

    private static long getPart1(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        long res = 0;
        for (String line : lines) {
            Matcher m = P1.matcher(line);

            while (m.find()) {
                int n1 = Integer.parseInt(m.group(1));
                int n2 = Integer.parseInt(m.group(2));
                res += (long) n1 * n2;
            }
        }
        return res;
    }

    private static int findMax(List<Integer> numbers, int n) {
        int res = -1;
        for (Integer number : numbers) {
            if (number < n && number > res) {
                res = number;
            }
        }
        return res;
    }

    private static long getPart2(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        long res = 0;
        boolean lastDont = false;
        for (String line : lines) {
            Matcher m = P1.matcher(line);

            List<Integer> dont = findWord(line, "don't()");
            List<Integer> does = findWord(line, "do()");

            while (m.find()) {
                if (!m.group().isBlank()) {
                    int dontIndex = findMax(dont, m.start());
                    int doIndex = findMax(does, m.start());
                    if (dontIndex == -1 && doIndex == -1) {
                        if (lastDont) {
                            continue;
                        }
                    }
                    if (dontIndex > doIndex) {
                        lastDont = true;
                        continue;
                    } else {
                        lastDont = false;
                    }
                    int n1 = Integer.parseInt(m.group(1));
                    int n2 = Integer.parseInt(m.group(2));
                    res += (long) n1 * n2;
                }
            }
        }
        return res;
    }
}
