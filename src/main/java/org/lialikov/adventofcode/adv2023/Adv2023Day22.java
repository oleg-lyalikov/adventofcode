package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Adv2023Day22 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 5
        //System.out.println(getPart1("2023/2023-12-22-sample.txt"));
        // 501
        //System.out.println(getPart1("2023/2023-12-22.txt"));
        // 7
        System.out.println(getPart2("2023/2023-12-22-sample.txt"));
        // 80948
        System.out.println(getPart2("2023/2023-12-22.txt"));

        System.out.println(new Date());
    }

    private static long getPart2(String inputFile) {
        InputData input = getInputData(inputFile);

        Map<Brick, Integer> res = new HashMap<>();
        for (Brick b : input.bricksList) {
            Integer value = res.get(b);
            if (value == null) {
                res.put(b, getAllSupportingCount(input, Set.of(b), res));
            }
        }
        return res.values().stream()
                .mapToLong(v -> v)
                .sum();
    }

    private static int getAllSupportingCount(InputData input, Set<Brick> bricks, Map<Brick, Integer> resMap) {
        int res = 0;
        Set<Brick> next = new HashSet<>(bricks);
        boolean changed = true;
        while (changed) {
            Set<Brick> supports = next.stream()
                    .flatMap(b -> input.supports.get(b).stream())
                    .collect(Collectors.toSet());
            if (supports.isEmpty()) {
                break;
            }

            Set<Brick> nextTmp = new HashSet<>();
            changed = false;
            for (Brick bSupp : supports) {
                if (next.contains(bSupp)) {
                    continue;
                }
                Set<Brick> laysOn = new HashSet<>(input.laysOn.get(bSupp));
                laysOn.removeAll(next);
                if (laysOn.isEmpty()) {
                    res++;
                    nextTmp.add(bSupp);
                    changed = true;
                }
            }
            next.addAll(nextTmp);
        }
        return res;
    }

    private static InputData getInputData(String inputFile) {
        List<Brick> bricksList = read(inputFile);
        Map<Integer, List<Brick>> byZ = new TreeMap<>();
        for (Brick brick : bricksList) {
            byZ.putIfAbsent(brick.z1, new ArrayList<>());
            byZ.get(brick.z1).add(brick);
        }

        Map<Position, Integer> zByPosition = new HashMap<>();
        Map<Position, Brick> topBricks = new HashMap<>();
        Map<Brick, Set<Brick>> laysOn = new HashMap<>();
        Map<Brick, Set<Brick>> supports = new HashMap<>();
        for (Map.Entry<Integer, List<Brick>> e : byZ.entrySet()) {
            List<Brick> bricks = e.getValue();
            for (Brick b1 : bricks) {
                for (Brick b2 : bricks) {
                    if (!b1.equals(b2) && intersectsHeavy(b1, b2)) {
                        throw new IllegalStateException("!!!");
                    }
                }
            }

            for (Brick b : bricks) {
                int maxZ = 0;
                laysOn.put(b, new HashSet<>());
                supports.put(b, new HashSet<>());
                for (int i = b.y1; i <= b.y2; i++) {
                    for (int j = b.x1; j <= b.x2; j++) {
                        Position p = new Position(j, i);
                        Integer z = zByPosition.get(p);
                        if (z == null) {
                            z = 0;
                        }
                        if ((z + 1) > maxZ) {
                            maxZ = z + 1;
                        }
                    }
                }
                int topZ = maxZ + b.z2 - b.z1;
                for (int i = b.y1; i <= b.y2; i++) {
                    for (int j = b.x1; j <= b.x2; j++) {
                        Position p = new Position(j, i);
                        Integer z = zByPosition.get(p);
                        if (z == null) {
                            z = 0;
                        }
                        if (z == maxZ - 1) {
                            Brick topBrick = topBricks.get(p);
                            if (topBrick != null) {
                                supports.get(topBrick).add(b);
                                laysOn.get(b).add(topBrick);
                            }
                        }

                        zByPosition.put(p, topZ);
                        topBricks.put(p, b);
                    }
                }
            }
        }
        return new InputData(bricksList, zByPosition, topBricks, laysOn, supports);
    }

    private static long getPart1(String inputFile) {
        InputData input = getInputData(inputFile);

        long res = 0;
        for (Map.Entry<Brick, Set<Brick>> e : input.supports.entrySet()) {
            Set<Brick> supp = e.getValue();
            if (supp.isEmpty()) {
                res++;
                continue;
            }
            boolean canDisintegrate = true;
            for (Brick bSupp : supp) {
                if (input.laysOn.get(bSupp).size() <= 1) {
                    canDisintegrate = false;
                    break;
                }
            }
            if (canDisintegrate) {
                res++;
            }
        }

        return res;
    }

    private static Set<Position> toXYPositions(Brick b) {
        Set<Position> res = new HashSet<>();
        for (int i = b.y1; i <= b.y2; i++) {
            for (int j = b.x1; j <= b.x2; j++) {
                res.add(new Position(j, i));
            }
        }
        return res;
    }

    private static boolean intersectsHeavy(Brick b1, Brick b2) {
        Set<Position> pos1 = toXYPositions(b1);
        Set<Position> pos2 = toXYPositions(b2);
        for (Position p : pos1) {
            if (pos2.contains(p)) {
                return true;
            }
        }
        return false;
    }

    private static List<Brick> read(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        AtomicInteger counter = new AtomicInteger(0);
        return new ArrayList<>(lines.stream().map(l -> {
            List<Integer> numbers = Arrays.stream(l.split("[~,]"))
                    .map(Integer::parseInt)
                    .toList();
            return new Brick(counter.incrementAndGet(), numbers.get(0), numbers.get(1), numbers.get(2), numbers.get(3), numbers.get(4), numbers.get(5));
        }).toList());
    }

    private record InputData(
            List<Brick> bricksList,
            Map<Position, Integer> zByPosition,
            Map<Position, Brick> topBricks,
            Map<Brick, Set<Brick>> laysOn,
            Map<Brick, Set<Brick>> supports
    ) {
    }
    private record Brick(
            int number,
            int x1,
            int y1,
            int z1,
            int x2,
            int y2,
            int z2
    ) {
    }
}
