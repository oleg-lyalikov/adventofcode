package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.model.Direction;
import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Adv2024Day6 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 41
        System.out.println(getPart1("2024/2024-12-06-sample.txt"));
        // 4826
        System.out.println(getPart1("2024/2024-12-06.txt"));
        // 6
        System.out.println(getPart2("2024/2024-12-06-sample.txt"));
        // 1721
        System.out.println(getPart2("2024/2024-12-06.txt"));

        System.out.println(new Date());
    }

    private static Pair<Integer, Integer> getStart(char[][] chars) {
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < chars[i].length; j++) {
                if (chars[i][j] == '^') {
                    return Pair.of(i, j);
                }
            }
        }
        throw new IllegalStateException("Start is not found");
    }

    private static long getPart1(String inputFile) {
        char[][] chars = FileUtil.toCharArrays(inputFile);
        Pair<Integer, Integer> start = getStart(chars);

        int i = start.getFirst();
        int j = start.getSecond();
        Direction d = Direction.N;
        Set<Pair<Integer, Integer>> visited = new HashSet<>();
        visited.add(start);
        while (true) {
            int nextI = d.nextI(i);
            int nextJ = d.nextJ(j);
            if (chars[nextI][nextJ] == '#') {
                d = nextD(d);
            } else {
                i = nextI;
                j = nextJ;
                visited.add(Pair.of(i, j));
                if (i == chars.length - 1 || j == chars[0].length - 1 || i == 0 || j == 0) {
                    break;
                }
            }
        }

        return visited.size();
    }

    private static Direction nextD(Direction d) {
        return switch (d) {
            case N -> Direction.E;
            case E -> Direction.S;
            case S -> Direction.W;
            case W -> Direction.N;
        };
    }

    private static boolean isCycle(char[][] chars, Pair<Integer, Integer> start, int boxI, int boxJ) {
        int i = start.getFirst();
        int j = start.getSecond();
        Direction d = Direction.N;
        Set<CoordDirection> visitedBoxes = new HashSet<>();
        while (true) {
            int nextI = d.nextI(i);
            int nextJ = d.nextJ(j);
            if (chars[nextI][nextJ] == '#' || (nextI == boxI && nextJ == boxJ)) {
                CoordDirection cd = new CoordDirection(nextI, nextJ, d);
                if (visitedBoxes.contains(cd)) {
                    return true;
                }
                visitedBoxes.add(cd);
                d = nextD(d);
            } else {
                i = nextI;
                j = nextJ;
                if (i == chars.length - 1 || j == chars[0].length - 1 || i == 0 || j == 0) {
                    return false;
                }
            }
        }
    }

    private static long getPart2(String inputFile) {
        char[][] chars = FileUtil.toCharArrays(inputFile);
        Pair<Integer, Integer> start = getStart(chars);

        long res = 0;
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < chars[0].length; j++) {
                if (chars[i][j] == '.' && isCycle(chars, start, i, j)) {
                    res++;
                }
            }
        }
        return res;
    }

    public record CoordDirection (int i, int j, Direction d) { }
}
