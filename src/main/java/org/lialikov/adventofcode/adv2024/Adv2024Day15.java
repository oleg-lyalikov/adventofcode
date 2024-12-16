package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.model.Direction;
import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;

public class Adv2024Day15 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 2028
        //System.out.println(getPart1("2024/2024-12-15-sample1.txt"));
        // 10092
        //System.out.println(getPart1("2024/2024-12-15-sample2.txt"));
        // 1465523
        //System.out.println(getPart1("2024/2024-12-15.txt"));

        // 9021
        System.out.println(getPart2("2024/2024-12-15-sample2.txt"));
        // 1471049
        System.out.println(getPart2("2024/2024-12-15.txt"));

        System.out.println(new Date());
    }

    private static Input read(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        int mapISize = 0;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isBlank()) {
                mapISize = i;
                break;
            }
        }
        int maxJSize = lines.get(0).length();
        char[][] map = new char[mapISize][maxJSize];
        Position robot = null;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = lines.get(i).charAt(j);
                if (map[i][j] == '@') {
                    robot = new Position(j, i);
                }
            }
        }
        if (robot == null) {
            throw new IllegalStateException("Robot not found");
        }

        List<Direction> moves = getMoves(mapISize, lines);
        return new Input(map, moves, robot);
    }

    private static List<Direction> getMoves(int mapISize, List<String> lines) {
        List<Direction> moves = new ArrayList<>();
        for (int i = mapISize + 1; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                char c = lines.get(i).charAt(j);
                Direction d = switch (c) {
                    case '>' -> Direction.E;
                    case '<' -> Direction.W;
                    case '^' -> Direction.N;
                    case 'v' -> Direction.S;
                    default -> throw new IllegalStateException("Unexpected value: " + c);
                };
                moves.add(d);
            }
        }
        return moves;
    }

    private static void move(Input input, int n) {
        Direction d = input.moves.get(n);
        Position robot = input.robot;
        int nextI = d.nextI(robot.y());
        int nextJ = d.nextJ(robot.x());
        if (input.map[nextI][nextJ] == '#') {
            return;
        }
        if (input.map[nextI][nextJ] == '.') {
            input.map[nextI][nextJ] = '@';
            input.map[robot.y()][robot.x()] = '.';
            input.robot = new Position(nextJ, nextI);
            return;
        }
        if (input.map[nextI][nextJ] == 'O') {
            int i = nextI;
            int j = nextJ;
            while (true) {
                i = d.nextI(i);
                j = d.nextJ(j);
                if (input.map[i][j] == '.') {
                    input.map[i][j] = 'O';
                    input.map[nextI][nextJ] = '@';
                    input.map[robot.y()][robot.x()] = '.';
                    input.robot = new Position(nextJ, nextI);
                    return;
                } else if (input.map[i][j] == '#') {
                    return;
                }
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static long getPart1(String inputFile) {
        Input input = read(inputFile);
        for (int i = 0; i < input.moves.size(); i++) {
            move(input, i);
        }
        print(input);
        return getGpsCoords(input);
    }

    private static long getGpsCoords(Input input) {
        long res = 0;
        for (int i = 0; i < input.map.length; i++) {
            for (int j = 0; j < input.map[i].length; j++) {
                if (input.map[i][j] == 'O' || input.map[i][j] == '[') {
                    res += (i * 100L) + j;
                }
            }
        }
        return res;
    }

    private static void print(Input input) {
        for (int i = 0; i < input.map.length; i++) {
            for (int j = 0; j < input.map[i].length; j++) {
                System.out.print(input.map[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static Input scale(Input in) {
        char[][] map = new char[in.map.length][in.map[0].length * 2];
        Position robot = null;
        for (int i = 0; i < in.map.length; i++) {
            for (int j = 0; j < in.map[i].length; j++) {
                char ch = in.map[i][j];
                if (ch == '.' || ch == '#') {
                    map[i][j * 2] = ch;
                    map[i][j * 2 + 1] = ch;
                } else if (ch == '@') {
                    map[i][j * 2] = ch;
                    map[i][j * 2 + 1] = '.';
                    robot = new Position(j * 2, i);
                } else if (ch == 'O') {
                    map[i][j * 2] = '[';
                    map[i][j * 2 + 1] = ']';
                }
            }
        }
        return new Input(map, in.moves, robot);
    }

    @SuppressWarnings("SameParameterValue")
    private static long getPart2(String inputFile) {
        Input input = read(inputFile);
        print(input);
        input = scale(input);
        print(input);
        for (int i = 0; i < input.moves.size(); i++) {
            move2(input, i);
        }
        print(input);
        return getGpsCoords(input);
    }

    private static void move2(Input input, int n) {
        Direction d = input.moves.get(n);
        Position robot = input.robot;
        int nextI = d.nextI(robot.y());
        int nextJ = d.nextJ(robot.x());
        char chNext = input.map[nextI][nextJ];
        if (chNext == '#') {
            return;
        }
        if (chNext == '.') {
            input.map[nextI][nextJ] = '@';
            input.map[robot.y()][robot.x()] = '.';
            input.robot = new Position(nextJ, nextI);
            return;
        }
        if (chNext == '[' || chNext == ']') {
            if (d == Direction.E || d == Direction.W) {
                int j = nextJ;
                while (true) {
                    j = d.nextJ(j, 2);
                    if (input.map[nextI][j] == '.') {
                        input.map[nextI][nextJ] = '@';
                        input.map[robot.y()][robot.x()] = '.';
                        input.robot = new Position(nextJ, nextI);
                        int jj = d.nextJ(nextJ);
                        while (true) {
                            int jj2 = d.nextJ(jj);
                            input.map[nextI][jj] = d == Direction.E ? '[' : ']';
                            input.map[nextI][jj2] = d == Direction.E ? ']' : '[';
                            jj = d.nextJ(jj, 2);
                            if (jj2 == j) {
                                return;
                            }
                        }
                    } else if (input.map[nextI][j] == '#') {
                        return;
                    }
                }
            } else {
                LinkedList<Position> toCheck = new LinkedList<>();
                LinkedList<Position> toMove = new LinkedList<>();
                Position start = chNext == ']' ? new Position(nextJ - 1, nextI) : new Position(nextJ, nextI);
                toCheck.add(start);
                toMove.add(start);
                while (!toCheck.isEmpty()) {
                    Position p = toCheck.removeFirst();
                    int i = d.nextI(p.y());
                    int j = p.x();
                    char chLeft = input.map[i][j];
                    char chRight = input.map[i][j + 1];
                    if (chLeft == '#' || chRight == '#') {
                        return;
                    }
                    if (chLeft != '.' || chRight != '.') {
                        if (chLeft == '[') {
                            Position pp = new Position(j, i);
                            toCheck.add(pp);
                            toMove.add(pp);
                        } else if (chLeft == ']') {
                            Position pp = new Position(j - 1, i);
                            toCheck.add(pp);
                            toMove.add(pp);
                        }
                        if (chRight == '[') {
                            Position pp = new Position(j + 1, i);
                            toCheck.add(pp);
                            toMove.add(pp);
                        }
                    }
                }

                while (!toMove.isEmpty()) {
                    Position p = toMove.removeLast();
                    int i = d.nextI(p.y());
                    input.map[i][p.x()] = '[';
                    input.map[i][p.x() + 1] = ']';
                    input.map[p.y()][p.x()] = '.';
                    input.map[p.y()][p.x() + 1] = '.';
                }

                input.map[nextI][nextJ] = '@';
                input.map[robot.y()][robot.x()] = '.';
                input.robot = new Position(nextJ, nextI);
            }
        }
    }

    public static class Input {
        char[][] map;
        List<Direction> moves;
        Position robot;

        public Input(char[][] map, List<Direction> moves, Position robot) {
            this.map = map;
            this.moves = moves;
            this.robot = robot;
        }

        @Override
        public String toString() {
            return "Input{" +
                    "map=" + Arrays.toString(map) +
                    ", moves=" + moves +
                    ", robot=" + robot +
                    '}';
        }
    }
}
