package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;

public class Adv2024Day18 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 22
        System.out.println(getPart1("2024/2024-12-18-sample.txt", 7, 12));
        // 436
        System.out.println(getPart1("2024/2024-12-18.txt", 71, 1024));

        // 6,1
        System.out.println(getPart2("2024/2024-12-18-sample.txt", 7, 12));
        // 61,50
        System.out.println(getPart2("2024/2024-12-18.txt", 71, 1024));

        System.out.println(new Date());
    }

    private static List<Position> read(String inputFile) {
        return FileUtil.readLines(inputFile).stream()
                .map(s -> s.split(","))
                .map(arr -> new Position(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])))
                .toList();
    }

    private static Map<Position, Integer> getStepsMap(boolean[][] corrupted, int size) {
        Position start = new Position(0, 0);
        Map<Position, Integer> steps = new HashMap<>();
        steps.put(start, 0);
        Set<Position> toVisit = new HashSet<>(List.of(start));
        Set<Position> visited = new HashSet<>();
        while (!toVisit.isEmpty()) {
            Position current = null;
            int currentSteps = Integer.MAX_VALUE;
            for (Position toCheck : toVisit) {
                Integer st = steps.get(toCheck);
                if (st != null && st < currentSteps) {
                    currentSteps = st;
                    current = toCheck;
                }
            }

            if (current == null) {
                break;
            }
            toVisit.remove(current);
            visited.add(current);

            List<Position> toCheck = List.of(
                    new Position(current.x() - 1, current.y()),
                    new Position(current.x() + 1, current.y()),
                    new Position(current.x() , current.y() - 1),
                    new Position(current.x(), current.y() + 1)
            );
            for (Position p : toCheck) {
                if (p.x() < 0 || p.x() >= size || p.y() < 0 || p.y() >= size) {
                    continue;
                }
                if (corrupted[p.y()][p.x()]) {
                    continue;
                }
                Integer pSteps = steps.get(p);
                if (pSteps == null || (currentSteps + 1) < pSteps) {
                    steps.put(p, currentSteps + 1);
                }
                if (!visited.contains(p)) {
                    toVisit.add(p);
                }
            }
        }
        return steps;
    }

    private static int getSteps(boolean[][] corrupted, int size) {
        return getStepsMap(corrupted, size).get(new Position(size - 1, size - 1));
    }

    private static long getPart1(String inputFile, int size, int limit) {
        List<Position> bytes = read(inputFile);
        boolean[][] corrupted = new boolean[size][size];
        for (int i = 0; i < limit; i++) {
            corrupted[bytes.get(i).y()][bytes.get(i).x()] = true;
        }
        return getSteps(corrupted, size);
    }

    private static String getPart2(String inputFile, int size, int limit) {
        List<Position> bytes = read(inputFile);
        boolean[][] corrupted = new boolean[size][size];
        for (int i = 0; i < limit; i++) {
            corrupted[bytes.get(i).y()][bytes.get(i).x()] = true;
        }
        Position end = new Position(size - 1, size - 1);
        for (int i = limit; i < bytes.size(); i++) {
            corrupted[bytes.get(i).y()][bytes.get(i).x()] = true;
            Map<Position, Integer> steps = getStepsMap(corrupted, size);
            Integer res = steps.get(end);
            if (res == null) {
                return bytes.get(i).x() + "," + bytes.get(i).y();
            }
        }
        return null;
    }
}
