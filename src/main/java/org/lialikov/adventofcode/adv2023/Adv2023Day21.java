package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.FileUtil;
import org.lialikov.adventofcode.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Adv2023Day21 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 16
        //System.out.println(getPart1("2023/2023-12-21-sample.txt", 6));
        // 42
        //System.out.println(getPart1("2023/2023-12-21-sample.txt", 64));
        // 3853
        //System.out.println(getPart1("2023/2023-12-21.txt", 64));

        //Steps: 1, : 2
        //Steps: 2, : 4
        //Steps: 3, : 6
        //Steps: 4, : 9
        //Steps: 5, : 13
        //Steps: 6, : 16
        //Steps: 7, : 21
        //Steps: 8, : 25
        //Steps: 9, : 29
        //Steps: 10, : 33
        //Steps: 11, : 35
        //Steps: 12, : 40
        //Steps: 13, : 39
        //Steps: 14, : 42
        //Steps: 15, : 39
        //Steps: 16, : 42
//        for (int i = 1; i <= 16; i++) {
//            System.out.println("Steps: " + i + ", : " + getPart1("2023/2023-12-21-sample.txt", i));
//        }

//        for (int i = 1; i <= 50; i++) {
//            System.out.println("Steps: " + i + ", : " + getPart2("2023/2023-12-21-sample.txt", i));
//        }

//        for (int i = 1; i <= 4; i++) {
//            System.out.println(getPart1("2023/2023-12-21-sample.txt", i));
//            System.out.println(getPart2("2023/2023-12-21-sample.txt", i));
//        }

        //System.out.println(getPart1("2023/2023-12-21.txt", 26501365));

        for (int i = 1; i <= 7; i++) {
            System.out.println("Steps: " + i + ": " + getPart1("2023/2023-12-21-sample2.txt", i));
        }

        System.out.println(new Date());
    }

    private static int getGardenCountAtDistance(char[][] map, Position start, int distance) {
        int xPlus = (start.x() + distance) % map[0].length;
        int xMinus = start.x() - distance;
        if (xMinus < 0) {
            xMinus = map[0].length - (Math.abs(xMinus) % map[0].length);
        }
        int yPlus = (start.y() + distance) % map.length;
        int yMinus = start.y() - distance;
        if (yMinus < 0) {
            yMinus = map.length - (Math.abs(yMinus) % map.length);
        }
        int res = 0;
        if (isValidGarden(map[yMinus][start.x()])) {
            res++;
        }
        if (isValidGarden(map[yPlus][start.x()])) {
            res++;
        }
        if (isValidGarden(map[start.y()][xPlus])) {
            res++;
        }
        if (isValidGarden(map[start.y()][xMinus])) {
            res++;
        }
        return res;
    }

    private static Map<Integer, Integer> byDistance(char[][] map, Position start, Integer printStep) {
        Map<Integer, Integer> resMap = new HashMap<>();
        int oddRes = 0;
        int evenRes = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (isValidGarden(map[i][j])) {
                    int currentStep = Math.abs(start.x() - j) + Math.abs((start.y() - i));
                    resMap.putIfAbsent(currentStep, 0);
                    resMap.put(currentStep, resMap.get(currentStep) + 1);
                    if (currentStep % 2 == 0) {
                        evenRes++;
                    } else {
                        oddRes++;
                    }
                    if (printStep != null) {
                        if (currentStep == printStep) {
                            System.out.print('X');
                        } else {
                            if (i == start.y() && j == start.x()) {
                                System.out.print('S');
                            } else {
                                System.out.print('.');
                            }
                        }
                    }
                } else {
                    if (printStep != null) {
                        System.out.print('#');
                    }
                }
            }
            if (printStep != null) {
                System.out.println();
            }
        }
        return resMap;
    }

    private static Map<Integer, Integer> byDistance2(char[][] map, Position start) {
        Map<Integer, Integer> res = new HashMap<>();
        for (int i = 0; i < map.length; i++) {
            res.put(i, getGardenCountAtDistance(map, start, i));
        }
        return res;
    }

    private static long getPart2(String inputFile, int steps) {
        char[][] map = StringUtil.toCharArrays(FileUtil.readLines(inputFile));
        Position start = getStart(map);
        char[][] map2 = doubleMap(map);
        Position start2 = getStart(map2);
        long[] extra = new long[map.length * 2 + 1];
        extra[0] = 1;
        for (int i = 1; i <= map.length * 2; i++) {
            extra[i] = getPart1(map2, start2, i);
            //System.out.println("Steps: " + i + ", : " + getPart1(map2, start2, i));
        }
        for (int i = 0; i < extra.length; i++) {
            if (i % 2 == 0) {
                System.out.print(extra[i] + ", ");
            }
        }
        System.out.println();
        for (int i = 0; i < extra.length; i++) {
            if (i % 2 == 1) {
                System.out.print(extra[i] + ", ");
            }
        }

        Map<Integer, Integer> byDistanceMap = byDistance(map2, start2, 6);
        Map<Integer, Integer> byDistance2Map = byDistance2(map2, start2);
        long res = 0;
        boolean isOdd = steps % 2 == 1;
        long prev = 0;
        for (int i = 1; i <= steps; i++) {
            boolean isIterationOdd = i % 2 == 1;
            if ((isOdd && isIterationOdd) || (!isOdd && !isIterationOdd)) {
                if (i < extra.length) {
                    //System.out.println("Diff: " + (extra[i - 1] - res));
                    res = extra[i];
                } else {
                    res += prev;
                }
                prev = res;
            }
        }
        return res;

//        long res = 0;
//        boolean isOdd = steps % 2 == 1;
//        for (int i = 1; i <= steps; i++) {
//            if ((isOdd && (i % 2 == 0)) || (!isOdd && (i % 2 == 1))) {
//                continue;
//            }
//            res += getGardenCountAtDistance(map, start, i);
//        }
//        if (!isOdd) {
//            res++;
//        }
//        return res;
    }

    private static long getPart1(String inputFile, int steps) {
        char[][] map = StringUtil.toCharArrays(FileUtil.readLines(inputFile));
        Position start = getStart(map);
        return getPart1(map, start, steps);
    }

    private static long getPart1(char[][] map, Position start, int steps) {
        //System.out.println("Garden points count: " + getGardenCount(map));
        Set<Position> next = Set.of(start);
        for (int i = 0; i < steps; i++) {
            next = next.stream()
                    .flatMap(p -> getNext(map, p).stream())
                    .collect(Collectors.toSet());
            //print(map, next);
            //System.out.println(next.size());
//            if (i % 10000 == 0) {
//                System.out.println("Step: " + i + ", date: " + new Date());
//            }
        }
        print(map, next);
        return next.size();
    }
    private static char[][] doubleMap(char[][] map) {
        char[][] res = new char[map.length * 3][map[0].length * 3];
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res.length; j++) {
                char current = map[i % map.length][j % map.length];
                if (current == 'S' && i > map.length && i < map.length * 2 && j > map[0].length && j < map[0].length * 2) {
                    res[i][j] = 'S';
                } else if (current == '#') {
                    res[i][j] = '#';
                } else {
                    res[i][j] = '.';
                }
            }
        }
        //print(res, Collections.emptySet());
        return res;
    }

    private static int getGardenCount(char[][] map) {
        int res = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'S' || map[i][j] == '.') {
                    res++;
                }
            }
        }
        return res;
    }

    private static void print(char[][] map, Set<Position> positions) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (positions.contains(new Position(j, i))) {
                    System.out.print('O');
                } else {
                    System.out.print(map[i][j]);
                }
            }
            System.out.println();
        }
    }

    private static List<Position> getNext(char[][] map, Position p) {
        return Stream.of(
                new Position(p.x() - 1, p.y()),
                new Position(p.x() + 1, p.y()),
                new Position(p.x(), p.y() - 1),
                new Position(p.x(), p.y() + 1)
        ).filter(pp -> isValidGarden(map, pp))
                .collect(Collectors.toList());
    }

    private static boolean isValidGarden(char ch) {
        return ch == '.' || ch == 'S';
    }

    private static boolean isValidGarden(char[][] map, Position p) {
        if (p.x() < 0 || p.y() < 0) {
            return false;
        }
        if (p.y() >= map.length) {
            return false;
        }
        if (p.x() >= map[0].length) {
            return false;
        }
        char element = map[p.y()][p.x()];
        return isValidGarden(element);
    }

    private static Position getStart(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'S') {
                    return new Position(j, i);
                }
            }
        }
        throw new IllegalStateException("Cannot find start");
    }
}
