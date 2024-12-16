package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Adv2024Day14 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 12
        System.out.println(getPart1("2024/2024-12-14-sample.txt", 11, 7));
        // 211773366
        System.out.println(getPart1("2024/2024-12-14.txt"));

        // 7344
        System.out.println(getPart2("2024/2024-12-14.txt"));

        System.out.println(new Date());
    }

    private static final Pattern P = Pattern.compile("p=([-\\d]+),([-\\d]+)\\s+v=([-\\d]+),([-\\d]+)");

    private static List<Robot> read(String inputFile) {
        List<Robot> res = new ArrayList<>();
        List<String> lines = FileUtil.readLines(inputFile);
        lines.forEach(l -> {
            Matcher m = P.matcher(l);
            if (!m.matches()) {
                throw new IllegalStateException("Does not match: " + l);
            }
            res.add(new Robot(
                    Integer.parseInt(m.group(1)),
                    Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4))
            ));
        });
        return res;
    }

    private static final int spaceX = 101;
    private static final int spaceY = 103;

    private static int teleport(int coord, boolean x, int spaceX, int spaceY) {
        if (coord < 0) {
            return x ? spaceX + coord : spaceY + coord;
        } else if (x && coord >= spaceX) {
            return coord - spaceX;
        } else if (!x && coord >= spaceY) {
            return coord - spaceY;
        }
        return coord;
    }

    private static void move(List<Robot> robots, int spaceX, int spaceY) {
        for (Robot r : robots) {
            r.x = teleport(r.x + r.vx, true, spaceX, spaceY);
            r.y = teleport(r.y + r.vy, false, spaceX, spaceY);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static long getPart1(String inputFile) {
        return getPart1(inputFile, spaceX, spaceY);
    }

    private static long getPart1(String inputFile, int spaceX, int spaceY) {
        List<Robot> robots = read(inputFile);
        print(robots, spaceX, spaceY);
        for (int i = 0; i < 100; i++) {
            move(robots, spaceX, spaceY);
        }
        print(robots, spaceX, spaceY);
        return getSafetyFactor(robots, spaceX, spaceY);
    }

    private static void print(List<Robot> robots, int spaceX, int spaceY) {
        Map<Position, Integer> map = new HashMap<>();
        robots.forEach(r -> {
            Position p = new Position(r.x, r.y);
            Integer current = map.computeIfAbsent(p, pp -> 0);
            map.put(p, current + 1);
        });
        for (int i = 0; i < spaceY; i++) {
            for (int j = 0; j < spaceX; j++) {
                Integer robotsCount = map.get(new Position(j, i));
                if (robotsCount == null) {
                    System.out.print('.');
                } else {
                    System.out.print(robotsCount);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static long getSafetyFactor(List<Robot> robots, int spaceX, int spaceY) {
        int[][] quadrants = new int[2][2];
        int xHalf = spaceX / 2;
        int yHalf = spaceY / 2;
        for (Robot r : robots) {
            if (r.x == xHalf || r.y == yHalf) {
                continue;
            }
            int x = r.x < xHalf ? 0 : 1;
            int y = r.y < yHalf ? 0 : 1;
            quadrants[y][x]++;
        }
        long res = 1;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                res *= quadrants[i][j];
            }
        }
        return res;
    }

    @SuppressWarnings("SameParameterValue")
    private static long getPart2(String inputFile) {
        List<Robot> robots = read(inputFile);
        print(robots, spaceX, spaceY);
        AtomicInteger i = new AtomicInteger(0);
        AtomicInteger xMax = new AtomicInteger();
        Map<Integer, Integer> firstStep = new HashMap<>();
        Map<Integer, Integer> rowCount = new HashMap<>();
        while (true) {
            i.incrementAndGet();
            move(robots, spaceX, spaceY);

            Map<Integer, Set<Integer>> byY = new HashMap<>();
            robots.forEach(r -> byY.computeIfAbsent(r.y, yy -> new HashSet<>()).add(r.x));

            AtomicBoolean stop = new AtomicBoolean(false);
            byY.forEach((k, v) -> {
                if (v.size() > 20 && v.size() >= xMax.get()) {
                    boolean first = v.size() > xMax.get();
                    xMax.set(v.size());
                    System.out.println("New x max count: " + xMax.get());
                    System.out.println("X coords: " + v.stream().sorted().toList());
                    firstStep.computeIfAbsent(v.size(), ss -> i.get());
                    int c = rowCount.computeIfAbsent(v.size(), ss -> 0);
                    rowCount.put(v.size(), c + 1);
                    if (c > 10) {
                        stop.set(true);
                    }
                    if (first) {
                        print(robots, spaceX, spaceY);
                    }
                }
            });
            if (stop.get()) {
                print(robots, spaceX, spaceY);
                break;
            }
        }
        return firstStep.get(xMax.get());
    }

    public static class Robot {
        int x;
        int y;
        int vx;
        int vy;

        public Robot(int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        @Override
        public String toString() {
            return "Robot{" +
                    "x=" + x +
                    ", y=" + y +
                    ", vx=" + vx +
                    ", vy=" + vy +
                    '}';
        }
    }
}
