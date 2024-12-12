package org.lialikov.adventofcode.util;

public class DataUtil {

    public static boolean isValid(int x, int y, int[][] lines) {
        if (y < 0 || y >= lines.length) {
            return false;
        }
        return x >= 0 && x < lines[0].length;
    }

}
