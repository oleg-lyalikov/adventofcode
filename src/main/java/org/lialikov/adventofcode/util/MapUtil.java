package org.lialikov.adventofcode.util;

import org.lialikov.adventofcode.model.Position;

import java.util.stream.Stream;

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

    public static Stream<Position> streamNext(Position p) {
        return Stream.of(
            new Position(p.x() + 1, p.y()),
            new Position(p.x() - 1, p.y()),
            new Position(p.x(), p.y() + 1),
            new Position(p.x(), p.y() - 1)
        );
    }
}
