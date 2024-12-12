package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;

import java.util.*;

public class Adv2024Day8 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 14
        System.out.println(getPart1("2024/2024-12-08-sample.txt"));
        // 240
        System.out.println(getPart1("2024/2024-12-08.txt"));
        // 34
        System.out.println(getPart2("2024/2024-12-08-sample.txt"));
        // 955
        System.out.println(getPart2("2024/2024-12-08.txt"));

        System.out.println(new Date());
    }

    private static Position antiPosition(char[][] chars, Position p1, Position p2) {
        int yDiff = Math.abs(p1.y() - p2.y());
        int xDiff = Math.abs(p1.x() - p2.x());
        int i = p2.y() < p1.y() ? p1.y() - 2 * yDiff : p1.y() + 2 * yDiff;
        int j = p2.x() < p1.x() ? p1.x() - 2 * xDiff : p1.x() + 2 * xDiff;
        if (i < chars.length && i >= 0 && j < chars[0].length && j >= 0) {
            return new Position(j, i);
        }
        return null;
    }

    private static Set<Position> getAntinodes(char[][] chars, Map<Character, List<Position>> antennas) {
        Set<Position> res = new HashSet<>();
        antennas.forEach((ch, positions) -> {
            for (int i = 0; i < positions.size(); i++) {
                Position pi = positions.get(i);
                for (int j = i + 1; j < positions.size(); j++) {
                    Position pj = positions.get(j);
                    Position p1 = antiPosition(chars, pi, pj);
                    if (p1 != null) {
                        res.add(p1);
                    }
                    Position p2 = antiPosition(chars, pj, pi);
                    if (p2 != null) {
                        res.add(p2);
                    }
                }
            }
        });
        return res;
    }

    private static Pair<char[][], Map<Character, List<Position>>> readInput(String inputFile) {
        char[][] chars = FileUtil.toCharArrays(inputFile);
        Map<Character, List<Position>> antennas = new HashMap<>();
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < chars[i].length; j++) {
                if (chars[i][j] != '.') {
                    antennas.computeIfAbsent(chars[i][j], ch -> new ArrayList<>()).add(new Position(i, j));
                }
            }
        }
        return Pair.of(chars, antennas);
    }

    private static long getPart1(String inputFile) {
        Pair<char[][], Map<Character, List<Position>>> input = readInput(inputFile);
        return getAntinodes(input.getFirst(), input.getSecond()).size();
    }

    private static void addAntinodesLine(Set<Position> res, Position prev, Position next, char[][] chars) {
        Position position = antiPosition(chars, prev, next);
        while (position != null) {
            res.add(position);
            prev = next;
            next = position;
            position = antiPosition(chars, prev, next);
        }
    }

    private static Set<Position> getAntinodes2(char[][] chars, Map<Character, List<Position>> antennas) {
        Set<Position> res = new HashSet<>();
        antennas.forEach((ch, positions) -> {
            if (positions.size() > 1) {
                for (int i = 0; i < positions.size(); i++) {
                    Position pi = positions.get(i);
                    res.add(pi);
                    for (int j = i + 1; j < positions.size(); j++) {
                        Position pj = positions.get(j);
                        addAntinodesLine(res, pi, pj, chars);
                        addAntinodesLine(res, pj, pi, chars);
                        res.add(pj);
                    }
                }
            }
        });
        return res;
    }

    private static long getPart2(String inputFile) {
        Pair<char[][], Map<Character, List<Position>>> input = readInput(inputFile);
        return getAntinodes2(input.getFirst(), input.getSecond()).size();
    }
}
