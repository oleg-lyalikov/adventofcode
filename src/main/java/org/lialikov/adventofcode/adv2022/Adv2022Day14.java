package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.model.Direction;
import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.lialikov.adventofcode.util.MapUtil.print;

public class Adv2022Day14 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 24
        System.out.println(getPart1("2022/2022-12-14-sample.txt"));
        // 672
        System.out.println(getPart1("2022/2022-12-14.txt"));

        // 93
        System.out.println(getPart2("2022/2022-12-14-sample.txt"));
        // 26831
        System.out.println(getPart2("2022/2022-12-14.txt"));

        System.out.println(new Date());
    }

    private static Input read(String inputFile, boolean part2) {
        List<String> lines = FileUtil.readLines(inputFile);
        List<List<Position>> paths = new ArrayList<>();
        int xMax = 0;
        int yMax = 0;
        for (String line : lines) {
            List<Position> points = Arrays.stream(line.split("\\s*->\\s*"))
                    .filter(s -> !s.isBlank())
                    .map(s -> s.split(","))
                    .map(p -> new Position(Integer.parseInt(p[0]), Integer.parseInt(p[1])))
                    .toList();
            xMax = Math.max(xMax, points.stream().mapToInt(Position::x).max().orElseThrow());
            yMax = Math.max(yMax, points.stream().mapToInt(Position::y).max().orElseThrow());
            paths.add(points);
        }

        if (part2) {
            yMax += 2;
            xMax = (xMax + 1) * 2;
        }

        char[][] map = new char[yMax + 1][xMax + 1];
        for (List<Position> points : paths) {
            Position current = points.get(0);
            map[current.y()][current.x()] = '#';
            for (int pi = 1; pi < points.size(); pi++) {
                Position next = points.get(pi);
                final Direction d;
                if (next.y() > current.y()) {
                    d = Direction.S;
                } else if (next.y() < current.y()) {
                    d = Direction.N;
                } else if (next.x() < current.x()) {
                    d = Direction.W;
                } else if (next.x() > current.x()) {
                    d = Direction.E;
                } else {
                    throw new IllegalStateException("Unexpected");
                }

                do {
                    current = new Position(d.nextJ(current.x()), d.nextI(current.y()));
                    map[current.y()][current.x()] = '#';
                } while (!current.equals(next));
            }
        }

        if (part2) {
            for (int j = 0; j < map[0].length; j++) {
                map[map.length - 1][j] = '#';
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != '#') {
                    map[i][j] = '.';
                }
            }
        }

        return new Input(paths, map);
    }

    private static int getStartX(char[][] map, boolean part2) {
        for (int j = 0; j < map[0].length; j++) {
            int iMax = part2 ? map.length - 1 : map.length;
            for (int i = 0; i < iMax; i++) {
                if (map[i][j] != '.') {
                    return j;
                }
            }
        }
        return 0;
    }

    private static int getEndX(char[][] map, boolean part2) {
        for (int j = map[0].length - 1; j >= 0; j--) {
            int iMax = part2 ? map.length - 1 : map.length;
            for (int i = 0; i < iMax; i++) {
                if (map[i][j] != '.') {
                    return j;
                }
            }
        }
        return map[0].length - 1;
    }

    private static long getPart1(String inputFile) {
        Input in = read(inputFile, false);
        return solve(in, false);
    }

    private static long solve(Input in, boolean part2) {
        long count = 0;
        boolean changed = true;
        while (changed) {
            int j = 500;
            changed = false;
            for (int i = 0; i < in.map.length - 1; i++) {
                int nextI = i + 1;
                if (in.map[nextI][j] == '.') {
                    continue;
                }
                if (j == 0) {
                    break;
                } else if (in.map[nextI][j - 1] == '.') {
                    j--;
                } else if (j == in.map[0].length - 1) {
                    break;
                } else if (in.map[nextI][j + 1] == '.') {
                    j++;
                } else {
                    if (in.map[i][j] == 'o') {
                        break;
                    }
                    in.map[i][j] = 'o';
                    changed = true;
                    break;
                }
            }
            if (changed) {
                count++;
            }
        }
        print(in.map, getStartX(in.map, part2), getEndX(in.map, part2));
        return count;
    }

    private static long getPart2(String inputFile) {
        Input in = read(inputFile, true);
        return solve(in, true);
    }

    private record Input(List<List<Position>> paths, char[][] map) { }
}
