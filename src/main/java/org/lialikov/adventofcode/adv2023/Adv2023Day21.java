package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.DataUtil;
import org.lialikov.adventofcode.util.FileUtil;
import org.lialikov.adventofcode.util.MapUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.lialikov.adventofcode.util.MapUtil.find;

public class Adv2023Day21 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 16
        //System.out.println(getPart1("2023/2023-12-21-sample.txt", 6));
        // 42
        //System.out.println(getPart1("2023/2023-12-21-sample.txt", 64));
        // 3853
        //System.out.println(getPart1("2023/2023-12-21.txt", 64));

//        for (int steps = 1; steps < 17; steps++) {
//            System.out.println("Steps " + steps + ": " + getPart1("2023/2023-12-21-sample.txt", steps));
//        }
//        for (int steps = 1; steps < 17; steps++) {
//            System.out.println("Steps " + steps + ": " + getPart2("2023/2023-12-21-sample.txt", steps));
//        }

        // 16
        //System.out.println(getPart2("2023/2023-12-21-sample.txt", 6));
        // 50
        //System.out.println(getPart2("2023/2023-12-21-sample.txt", 10));
        // 1594
        //System.out.println(getPart2("2023/2023-12-21-sample.txt", 50));
        // 6536
        //System.out.println(getPart2("2023/2023-12-21-sample.txt", 100));
        // 167004
        //System.out.println(getPart2("2023/2023-12-21-sample.txt", 500));
        // 668697
        //System.out.println(getPart2("2023/2023-12-21-sample.txt", 1000));
        // 16733044
        System.out.println(getPart2("2023/2023-12-21-sample.txt", 5000));

        // 42
        //System.out.println(getPart2("2023/2023-12-21-sample.txt", 64));
        // 3853
        //System.out.println(getPart2("2023/2023-12-21.txt", 64));

        //System.out.println(getPart2("2023/2023-12-21.txt", 26501365));

        System.out.println(new Date());
    }

    private static long getPart1(String inputFile, int steps) {
        char[][] map = FileUtil.toCharArrays(inputFile);
        Position start = find(map, 'S');
        return getPart1(map, start, steps);
    }

    private static long getPart1(char[][] map, Position start, int steps) {
        Set<Position> next = Set.of(start);
        for (int i = 0; i < steps; i++) {
            next = next.stream()
                    .flatMap(p -> getNext(map, p).stream())
                    .collect(Collectors.toSet());
        }
        //if (steps % 2 == 1) {
            //print(map, next);
        //}
        return next.size();
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
        return MapUtil.streamNext(p)
                .filter(pp -> DataUtil.isValid(pp, map))
                .filter(pp -> isValidGarden(map[pp.y()][pp.x()]))
                .collect(Collectors.toList());
    }

    private static boolean isValidGarden(char ch) {
        return ch == '.' || ch == 'S';
    }

    private static long getPart2(String inputFile, int steps) {
        char[][] map = FileUtil.toCharArrays(inputFile);
        Position start = find(map, 'S');
        return getPart2(map, start, steps);
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
        return res;
    }

    private static long getPart2(char[][] map, Position start, int steps) {
        Set<Position> next = Set.of(start);
        Set<Position> prevEven = new HashSet<>(next);
        Set<Position> prevOdd = new HashSet<>();
        Map<Position, Long> prevEvenCounts = new HashMap<>(Map.of(start, 1L));
        Map<Position, Long> prevOddCount = new HashMap<>();
        long evenSum = 1;
        long oddSum = 0;
        int mult = 500;
        Map<Position, Integer> firstSteps = new HashMap<>(Map.of(start, 1));
        Map<Position, Integer> secondSteps = new HashMap<>();
        Map<Position, Long> allCounts = new HashMap<>(Map.of(start, 1L));
        for (int i = 1; i <= steps; i++) {
            boolean even = i % 2 == 0;
            Set<Position> prev = even ? prevEven : prevOdd;
            Map<Position, Long> prevCounts = even ? prevEvenCounts : prevOddCount;
            next = next.stream()
                    .flatMap(p -> getNextFiltered(map, p, prev))
                    .collect(Collectors.toSet());
//            Iterator<Position> it = next.iterator();
//            while (it.hasNext()) {
//                Position ip = it.next();
//                Position casted = cast(map, ip, mult);
//                Position key = ip.equals(casted) ? ip : casted;
//                if (key != ip) {
//                    it.remove();
//                }
//                long v = prevCounts.computeIfAbsent(key, pp -> 0L);
//                prevCounts.put(key, v + 1);
//            }
            int finalI = i;
            next.forEach(n -> {
                Position casted = cast(map, n, 1);
                if (!firstSteps.containsKey(casted)) {
                    firstSteps.computeIfAbsent(casted, nn -> finalI);
                } else {
                    secondSteps.computeIfAbsent(casted, nn -> finalI);
                }
                long v = allCounts.computeIfAbsent(casted, nn -> 0L);
                allCounts.put(casted, v + 1);
            });

            if (even) {
                evenSum += next.size();
            } else {
                oddSum += next.size();
            }

            if (even) {
                prevEven = next;
            } else {
                prevOdd = next;
            }

            //print(map, next);
            System.out.println("Steps " + i + ": " + ((i % 2 == 0) ? evenSum : oddSum));
        }
        //if (steps % 2 == 1) {
        //print(map, next);
        //}
        Map<Position, Long> counts = (steps % 2 == 0) ? prevEvenCounts : prevOddCount;
        //return counts.values().stream().mapToLong(aLong -> aLong).sum();
        return (steps % 2 == 0) ? evenSum : oddSum;
    }

    private static Stream<Position> getNextFiltered(char[][] map, Position p, Set<Position> filter) {
        return MapUtil.streamNext(p)
                .filter(pp -> !filter.contains(pp))
                .filter(pp -> isValidGarden2(map, pp));
    }

    private static Position cast(char[][] map, Position p, int mult) {
        int xMax = map[0].length * mult;
        int x = p.x() % xMax;
        if (x < 0) {
            x += xMax;
        }
        int yMax = map.length * mult;
        int y = p.y() % yMax;
        if (y < 0) {
            y += yMax;
        }
        return new Position(x, y);
    }

    private static boolean isValidGarden2(char[][] map, Position p) {
        Position casted = cast(map, p, 1);
        return isValidGarden(map[casted.y()][casted.x()]);
    }
}
