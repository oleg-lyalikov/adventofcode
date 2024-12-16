package org.lialikov.adventofcode.util;

import org.lialikov.adventofcode.model.Position;

public class MapUtil {

    public static Position find(char[][] map, char ch) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == ch) {
                    return new Position(j, i);
                }
            }
        }
        throw new IllegalStateException(ch + " cannot be found");
    }

}
