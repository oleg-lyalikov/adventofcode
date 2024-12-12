package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;

import static org.lialikov.adventofcode.util.ParseUtil.asCharList;

public class Adv2023Day11 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 374
        System.out.println(getPathsSum("2023/2023-12-11-sample.txt"));
        // 9805264
        System.out.println(getPathsSum("2023/2023-12-11.txt"));
        // 1030
        System.out.println(getPathsSum("2023/2023-12-11-sample.txt", 10));
        // 8410
        System.out.println(getPathsSum("2023/2023-12-11-sample.txt", 100));
        // 779032247216
        System.out.println(getPathsSum("2023/2023-12-11.txt", 1000000));

        System.out.println(new Date());
    }

    public static Space readSpace(String inputFile) {
        return readSpace(readMap(inputFile));
    }

    public static List<List<Character>> readMap(String inputFile) {
        List<List<Character>> map = new ArrayList<>();
        FileUtil.read(inputFile, s -> map.add(asCharList(s)));
        return map;
    }

    public static Space readSpace(List<List<Character>> map) {
        List<Integer> emptyColumns = new ArrayList<>();
        List<Integer> emptyRows = new ArrayList<>();
        List<Point> galaxies = new ArrayList<>();
        for (int j = 0; j < map.get(0).size(); j++) {
            boolean empty = true;
            for (List<Character> characters : map) {
                if (characters.get(j) != '.') {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                emptyColumns.add(j);
            }
        }
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i).stream().allMatch(c -> c == '.')) {
                emptyRows.add(i);
            }
            for (int j = 0; j < map.get(0).size(); j++) {
                if (map.get(i).get(j) == '#') {
                    galaxies.add(new Point(j, i));
                }
            }
        }
        return new Space(galaxies, emptyColumns, emptyRows);
    }

    public static long getPathsSum(String inputFile) {
        Space space = readSpace(inputFile);
        long res = 0;
        for (int i = 0; i < space.galaxies.size(); i++) {
            Point g1 = space.galaxies.get(i);
            for (int j = i + 1; j < space.galaxies.size(); j++) {
                Point g2 = space.galaxies.get(j);
                long path = Math.abs(g1.x - g2.x) + Math.abs(g1.y - g2.y);
                path += expanded(g1.y, g2.y, space.emptyRows);
                path += expanded(g1.x, g2.x, space.emptyColumns);
                res += path;
            }
        }
        return res;
    }

    private static long expanded(int i1, int i2, List<Integer> emptyLines) {
        long res = 0;
        for (int line: emptyLines) {
            if (i1 < line && i2 > line) {
                res++;
            } else if (i1 > line && i2 < line) {
                res++;
            }
        }
        return res;
    }

    private static long expanded(int i1, int i2, List<Integer> emptyLines, long multiplier) {
        long res = 0;
        for (int line: emptyLines) {
            if (i1 < line && i2 > line) {
                res += multiplier - 1;
            } else if (i1 > line && i2 < line) {
                res += multiplier - 1;
            }
        }
        return res;
    }

    public static long getPathsSum(String inputFile, long multiplier) {
        Space space = readSpace(inputFile);
        long res = 0;
        for (int i = 0; i < space.galaxies.size(); i++) {
            Point g1 = space.galaxies.get(i);
            for (int j = i + 1; j < space.galaxies.size(); j++) {
                Point g2 = space.galaxies.get(j);
                long path = Math.abs(g1.x - g2.x) + Math.abs(g1.y - g2.y);
                path += expanded(g1.y, g2.y, space.emptyRows, multiplier);
                path += expanded(g1.x, g2.x, space.emptyColumns, multiplier);
                res += path;
            }
        }
        return res;
    }

    public record Space(
        List<Point> galaxies,
        List<Integer> emptyColumns,
        List<Integer> emptyRows
    ) {
    }

    public record Point(
            int x,
            int y
    ) {
    }
}
