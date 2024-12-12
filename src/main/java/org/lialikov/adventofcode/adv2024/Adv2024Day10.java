package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;

public class Adv2024Day10 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 36
        System.out.println(getPart1("2024/2024-12-10-sample.txt"));
        // 754
        System.out.println(getPart1("2024/2024-12-10.txt"));
        // 81
        System.out.println(getPart2("2024/2024-12-10-sample.txt"));
        // 1609
        System.out.println(getPart2("2024/2024-12-10.txt"));

        System.out.println(new Date());
    }

    private static int[][] readInput(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        int[][] data = new int[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                data[i][j] = lines.get(i).charAt(j) - '0';
            }
        }
        return data;
    }

    @SuppressWarnings("SameParameterValue")
    private static Set<Position> getPositions(int[][] data, int n) {
        Set<Position> res = new HashSet<>();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (data[i][j] == n) {
                    res.add(new Position(j, i));
                }
            }
        }
        return res;
    }

    private static void addConditionally(int i, int j, int[][] data, int n, Set<Position> toVisit) {
        if (i >= 0 && i < data.length && j >= 0 && j < data[i].length &&
                data[i][j] == n) {
            toVisit.add(new Position(j, i));
        }
    }

    private static void addConditionally2(int i, int j, int[][] data, int n, List<Position> current, Set<List<Position>> toVisit) {
        if (i >= 0 && i < data.length && j >= 0 && j < data[i].length &&
                data[i][j] == n) {
            List<Position> copy = new ArrayList<>(current);
            copy.add(new Position(j, i));
            toVisit.add(copy);
        }
    }

    private static long getTrailheads(int[][] data, Position start) {
        Set<Position> toVisit = new HashSet<>();
        toVisit.add(start);
        Set<Position> res = new HashSet<>();
        while (!toVisit.isEmpty()) {
            Iterator<Position> iterator = toVisit.iterator();
            Position next = iterator.next();
            iterator.remove();

            int number = data[next.y()][next.x()];
            if (number == 9) {
                res.add(next);
                continue;
            }

            int n = data[next.y()][next.x()] + 1;

            addConditionally(next.y() - 1, next.x(), data, n, toVisit);
            addConditionally(next.y() + 1, next.x(), data, n, toVisit);
            addConditionally(next.y(), next.x() - 1, data, n, toVisit);
            addConditionally(next.y(), next.x() + 1, data, n, toVisit);
        }
        return res.size();
    }

    private static long getRating(int[][] data, Position start) {
        Set<List<Position>> toVisit = new HashSet<>();
        toVisit.add(new ArrayList<>(List.of(start)));
        Set<List<Position>> res = new HashSet<>();
        while (!toVisit.isEmpty()) {
            Iterator<List<Position>> iterator = toVisit.iterator();
            List<Position> nextList = iterator.next();
            iterator.remove();

            Position next = nextList.get(nextList.size() - 1);
            int number = data[next.y()][next.x()];
            if (number == 9) {
                res.add(nextList);
                continue;
            }

            int n = data[next.y()][next.x()] + 1;

            addConditionally2(next.y() - 1, next.x(), data, n, nextList, toVisit);
            addConditionally2(next.y() + 1, next.x(), data, n, nextList, toVisit);
            addConditionally2(next.y(), next.x() - 1, data, n, nextList, toVisit);
            addConditionally2(next.y(), next.x() + 1, data, n, nextList, toVisit);
        }
        return res.size();
    }

    private static long getPart1(String inputFile) {
        int[][] data = readInput(inputFile);
        Set<Position> start = getPositions(data, 0);
        long res = 0;
        for (Position s : start) {
            res += getTrailheads(data, s);
        }
        return res;
    }

    private static long getPart2(String inputFile) {
        int[][] data = readInput(inputFile);
        Set<Position> start = getPositions(data, 0);
        long res = 0;
        for (Position s : start) {
            res += getRating(data, s);
        }
        return res;
    }
}
