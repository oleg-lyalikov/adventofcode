package org.lialikov.adventofcode.util;

import org.lialikov.adventofcode.model.Position;

public class DataUtil {

    public static boolean isValid(int x, int y, int[][] lines) {
        return isValid(x, y, lines[0].length, lines.length);
    }

    private static boolean isValid(int x, int y, int sizeX, int sizeY) {
        if (y < 0 || y >= sizeY) {
            return false;
        }
        return x >= 0 && x < sizeX;
    }

    public static boolean isValid(Position p, char[][] map) {
        return isValid(p.x(), p.y(), map[0].length, map.length);
    }
}
