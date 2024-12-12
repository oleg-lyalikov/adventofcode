package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.model.Direction;
import org.lialikov.adventofcode.util.FileUtil;
import org.lialikov.adventofcode.model.Position;

import java.util.*;

import static org.lialikov.adventofcode.util.StringUtil.toCharArrays;

public class Adv2023Day16 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 46
        System.out.println(getPart1("2023/2023-12-16-sample.txt"));
        // 6795
        System.out.println(getPart1("2023/2023-12-16.txt"));

        // 51
        System.out.println(getPart2("2023/2023-12-16-sample.txt"));
        // 7154
        System.out.println(getPart2("2023/2023-12-16.txt"));

        System.out.println(new Date());
    }

    public static long getPart2(String inputFile) {
        char[][] lines = toCharArrays(FileUtil.readLines(inputFile));

        long res = 0;
        for (int i = 0; i < lines.length; i++) {
            long res1 = getEnergized(new BeamTile(0, i, Direction.E), lines);
            if (res1 > res) {
                res = res1;
            }
            long res2 = getEnergized(new BeamTile(lines[i].length - 1, i, Direction.W), lines);
            if (res2 > res) {
                res = res2;
            }
        }
        for (int j = 0; j < lines[0].length; j++) {
            long res1 = getEnergized(new BeamTile(j, 0, Direction.S), lines);
            if (res1 > res) {
                res = res1;
            }
            long res2 = getEnergized(new BeamTile(j, lines.length - 1, Direction.N), lines);
            if (res2 > res) {
                res = res2;
            }
        }
        return res;
    }

    public static long getPart1(String inputFile) {
        char[][] lines = toCharArrays(FileUtil.readLines(inputFile));
        return getEnergized(new BeamTile(0, 0, Direction.E), lines);
    }

    private static long getEnergized(BeamTile start, char[][] lines) {
        Set<BeamTile> toProcess = new HashSet<>();
        Set<BeamTile> visited = new HashSet<>();
        toProcess.add(start);
        while (!toProcess.isEmpty()) {
            Iterator<BeamTile> iterator = toProcess.iterator();
            BeamTile bt = iterator.next();
            iterator.remove();
            if (visited.contains(bt)) {
                continue;
            }
            while (true) {
                if (visited.contains(bt)) {
                    break;
                }
                visited.add(bt);
                int i = bt.y;
                int j = bt.x;
                char current = lines[i][j];
                if (current == '.') {
                    bt = next(bt, lines);
                    if (bt == null) {
                        break;
                    }
                } else if (current == '/' || current == '\\') {
                    i = nextI(i, bt.direction, current);
                    j = nextJ(j, bt.direction, current);
                    Direction d = nextD(current, bt.direction);
                    BeamTile next = new BeamTile(j, i, d);
                    if (isValid(next, lines)) {
                        bt = next;
                    } else {
                        break;
                    }
                } else if (current == '|') {
                    if (bt.direction == Direction.N || bt.direction == Direction.S) {
                        bt = next(bt, lines);
                        if (bt == null) {
                            break;
                        }
                    } else {
                        BeamTile bt1 = new BeamTile(bt.x, bt.y - 1, Direction.N);
                        BeamTile bt2 = new BeamTile(bt.x, bt.y + 1, Direction.S);
                        if (isValid(bt1, lines)) {
                            bt = bt1;
                        }
                        if (isValid(bt2, lines)) {
                            if (bt == bt1) {
                                toProcess.add(bt2);
                            } else {
                                bt = bt2;
                            }
                        }
                    }
                } else if (current == '-') {
                    if (bt.direction == Direction.E || bt.direction == Direction.W) {
                        bt = next(bt, lines);
                        if (bt == null) {
                            break;
                        }
                    } else {
                        BeamTile bt1 = new BeamTile(bt.x - 1, bt.y, Direction.W);
                        BeamTile bt2 = new BeamTile(bt.x + 1, bt.y, Direction.E);
                        if (isValid(bt1, lines)) {
                            bt = bt1;
                        }
                        if (isValid(bt2, lines)) {
                            if (bt == bt1) {
                                toProcess.add(bt2);
                            } else {
                                bt = bt2;
                            }
                        }
                    }
                }
            }
        }
        //print(visited, lines);
        return energized(visited);
    }
    private static long energized(Set<BeamTile> beamTiles) {
        Set<Position> visited = new HashSet<>();
        beamTiles.forEach(bt -> visited.add(new Position(bt.x, bt.y)));
        return visited.size();
    }

    private static void print(Set<BeamTile> visited, char[][] lines) {
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length; j++) {
                boolean vis = false;
                for (BeamTile bt : visited) {
                    if (bt.x == j && bt.y == i) {
                        vis = true;
                        break;
                    }
                }
                System.out.print(vis ? '#' : '.');
            }
            System.out.println();
        }
    }

    private static Direction nextD(char current, Direction direction) {
        return switch (direction) {
            case N -> current == '/' ? Direction.E : Direction.W;
            case S -> current == '/' ? Direction.W : Direction.E;
            case E -> current == '/' ? Direction.N : Direction.S;
            case W -> current == '/' ? Direction.S : Direction.N;
        };
    }

    private static BeamTile next(BeamTile bt, char[][] lines) {
        int i = bt.direction.nextI(bt.y);
        int j = bt.direction.nextJ(bt.x);
        BeamTile next = new BeamTile(j, i, bt.direction);
        if (!isValid(next, lines)) {
            return null;
        }
        return next;
    }
    private static boolean isValid(BeamTile bt, char[][] lines) {
        if (bt.y < 0 || bt.y >= lines.length) {
            return false;
        }
        if (bt.x < 0 || bt.x >= lines[0].length) {
            return false;
        }
        return true;
    }
    private static int nextI(int i, Direction direction, char mirror) {
        return switch (direction) {
            case E -> mirror == '/' ? i - 1 : i + 1;
            case S -> i;
            case W -> mirror == '/' ? i + 1 : i - 1;
            case N -> i;
        };
    }
    private static int nextJ(int j, Direction direction, char mirror) {
        return switch (direction) {
            case E -> j;
            case S -> mirror == '/' ? j - 1 : j + 1;
            case W -> j;
            case N -> mirror == '/' ? j + 1 : j - 1;
        };
    }
    private record BeamTile(
            int x,
            int y,
            Direction direction
    ) {
    }
}
