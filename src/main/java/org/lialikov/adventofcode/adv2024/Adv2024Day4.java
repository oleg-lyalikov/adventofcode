package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.Date;

public class Adv2024Day4 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 18
        System.out.println(getPart1("2024/2024-12-04-sample.txt"));
        // 2530
        System.out.println(getPart1("2024/2024-12-04.txt"));
        // 9
        System.out.println(getPart2("2024/2024-12-04-sample.txt"));
        // 1921
        System.out.println(getPart2("2024/2024-12-04.txt"));

        System.out.println(new Date());
    }

    private static final int I_DIFF = 3;

    private static boolean xMatchForward(char[][] chars, int i, int j) {
        if (j + I_DIFF >= chars[i].length) {
            return false;
        }
        return chars[i][j] == 'X' && chars[i][j+1] == 'M' && chars[i][j+2] == 'A' && chars[i][j+3] == 'S';
    }
    private static boolean xMatchBackward(char[][] chars, int i, int j) {
        if (j - I_DIFF < 0) {
            return false;
        }
        return chars[i][j] == 'X' && chars[i][j-1] == 'M' && chars[i][j-2] == 'A' && chars[i][j-3] == 'S';
    }
    private static boolean yMatchForward(char[][] chars, int i, int j) {
        if (i + I_DIFF >= chars.length) {
            return false;
        }
        return chars[i][j] == 'X' && chars[i+1][j] == 'M' && chars[i+2][j] == 'A' && chars[i+3][j] == 'S';
    }
    private static boolean yMatchBackward(char[][] chars, int i, int j) {
        if (i - I_DIFF < 0) {
            return false;
        }
        return chars[i][j] == 'X' && chars[i-1][j] == 'M' && chars[i-2][j] == 'A' && chars[i-3][j] == 'S';
    }
    private static boolean diagUpMatchForward(char[][] chars, int i, int j) {
        if (j + I_DIFF >= chars[i].length || i - I_DIFF < 0) {
            return false;
        }
        return chars[i][j] == 'X' && chars[i-1][j+1] == 'M' && chars[i-2][j+2] == 'A' && chars[i-3][j+3] == 'S';
    }
    private static boolean diagUpMatchBackward(char[][] chars, int i, int j) {
        if (j - I_DIFF < 0 || i + I_DIFF >= chars.length) {
            return false;
        }
        return chars[i][j] == 'X' && chars[i+1][j-1] == 'M' && chars[i+2][j-2] == 'A' && chars[i+3][j-3] == 'S';
    }
    private static boolean diagDownMatchForward(char[][] chars, int i, int j) {
        if (j + I_DIFF >= chars[i].length || i + I_DIFF >= chars.length) {
            return false;
        }
        return chars[i][j] == 'X' && chars[i+1][j+1] == 'M' && chars[i+2][j+2] == 'A' && chars[i+3][j+3] == 'S';
    }
    private static boolean diagDownMatchBackward(char[][] chars, int i, int j) {
        if (j - I_DIFF < 0 || i - I_DIFF < 0) {
            return false;
        }
        return chars[i][j] == 'X' && chars[i-1][j-1] == 'M' && chars[i-2][j-2] == 'A' && chars[i-3][j-3] == 'S';
    }

    private static long getPart1(String inputFile) {
        char[][] chars = FileUtil.toCharArrays(inputFile);

        long xMatchForward = 0;
        long xMatchBackward = 0;
        long yMatchForward = 0;
        long yMatchBackward = 0;
        long diagUpMatchForward = 0;
        long diagUpMatchBackward = 0;
        long diagDownMatchForward = 0;
        long diagDownMatchBackward = 0;
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < chars[0].length; j++) {
                if (xMatchForward(chars, i, j)) {
                    xMatchForward++;
                }
                if (xMatchBackward(chars, i, j)) {
                    xMatchBackward++;
                }
                if (yMatchForward(chars, i, j)) {
                    yMatchForward++;
                }
                if (yMatchBackward(chars, i, j)) {
                    yMatchBackward++;
                }
                if (diagUpMatchForward(chars, i, j)) {
                    diagUpMatchForward++;
                }
                if (diagUpMatchBackward(chars, i, j)) {
                    diagUpMatchBackward++;
                }
                if (diagDownMatchForward(chars, i, j)) {
                    diagDownMatchForward++;
                }
                if (diagDownMatchBackward(chars, i, j)) {
                    diagDownMatchBackward++;
                }
            }
        }

        return xMatchForward + xMatchBackward + yMatchForward + yMatchBackward + diagUpMatchForward + diagUpMatchBackward + diagDownMatchForward + diagDownMatchBackward;
    }

    private static boolean isXmas(char[][] chars, int i, int j) {
        if (chars[i][j] != 'A') {
            return false;
        }
        if (i == 0 || i >= chars.length - 1) {
            return false;
        }
        if (j == 0 || j >= chars[i].length - 1) {
            return false;
        }
        if (!(chars[i - 1][j - 1] == 'M' && chars[i + 1][j + 1] == 'S') &&
                !(chars[i - 1][j - 1] == 'S' && chars[i + 1][j + 1] == 'M')) {
            return false;
        }
        return (chars[i - 1][j + 1] == 'M' && chars[i + 1][j - 1] == 'S') ||
                (chars[i - 1][j + 1] == 'S' && chars[i + 1][j - 1] == 'M');
    }

    private static long getPart2(String inputFile) {
        char[][] chars = FileUtil.toCharArrays(inputFile);

        long res = 0;
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < chars[0].length; j++) {
                if (isXmas(chars, i, j)) {
                    res++;
                }
            }
        }

        return res;
    }
}
