package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.model.Direction;
import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;

import static org.lialikov.adventofcode.util.DataUtil.isValid;

public class Adv2023Day17 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 102
        System.out.println(getPart1("2023/2023-12-17-sample.txt"));
        // 907
        //System.out.println(getPart1("2023/2023-12-17.txt"));

        // 94
        //System.out.println(getPart2("2023/2023-12-17-sample.txt"));
        // 71
        //System.out.println(getPart2("2023/2023-12-17-sample2.txt"));
        // 1057 1:40
        //System.out.println(getPart2("2023/2023-12-17.txt"));

        System.out.println(new Date());
    }

    public static long getPart2(String inputFile) {
        int[][] input = readFile(inputFile);
        Map<Vertex, PathInfo> data = bfs2(input);
        return getMinAtEnd(input, data);
    }

    private static Map<Vertex, PathInfo> bfs2(int[][] input) {
        Vertex start1 = new Vertex(0, 0, Direction.N, 0);
        Vertex start2 = new Vertex(0, 0, Direction.W, 0);
        Set<Vertex> toVisit = new HashSet<>();
        Set<Vertex> visited = new HashSet<>();
        Map<Vertex, PathInfo> data = new HashMap<>();
        data.put(start1, new PathInfo(0));
        data.put(start2, new PathInfo(0));

        toVisit.add(start1);
        toVisit.add(start2);
        int minSteps = 4;
        long start = System.currentTimeMillis();
        long processed = 0;
        while (!toVisit.isEmpty()) {
            if (System.currentTimeMillis() - start > 10_000) {
                start = System.currentTimeMillis();
                System.out.println("To visit: " + toVisit.size() + ", processed: " + processed + ", date: " + new Date());
            }
            Iterator<Vertex> iterator = toVisit.iterator();
            Vertex v = iterator.next();
            iterator.remove();
            processed++;
            if (visited.contains(v)) {
                continue;
            }

            PathInfo info = data.get(v);
            Set<Direction> next = getNextDirections(v, 10);

            for (Direction d : next) {
                int steps = d == v.source ? v.steps + 1 : minSteps;
                int coordSteps = d == v.source ? 1 : minSteps;
                int y = d.nextI(v.y, coordSteps);
                int x = d.nextJ(v.x, coordSteps);

                if (isValid(x, y, input)) {
                    Vertex vNext = new Vertex(x, y, d, steps);
                    final long weigh;
                    if (d == v.source) {
                        weigh = info.weigh + input[y][x];
                    } else {
                        long tmp = info.weigh;
                        for (int k = 1; k <= minSteps; k++) {
                            tmp += input[d.nextI(v.y, k)][d.nextJ(v.x, k)];
                        }
                        weigh = tmp;
                    }
                    PathInfo nextInfo = data.get(vNext);
                    if (nextInfo == null || nextInfo.weigh > weigh) {
                        data.put(vNext, new PathInfo(weigh));
                        toVisit.add(vNext);
                        visited.remove(vNext);
                    }
                }
            }

            visited.add(v);
        }

        return data;
    }

    public static long getPart1(String inputFile) {
        int[][] input = readFile(inputFile);
        Map<Vertex, PathInfo> data = bfs(input);
        return getMinAtEnd(input, data);
    }

    private static long getMinAtEnd(int[][] input, Map<Vertex, PathInfo> data) {
        int y = input.length - 1;
        int x = input[0].length - 1;
        long res = Long.MAX_VALUE;
        for (int steps = 1; steps <= 10; steps++) {
            PathInfo d1 = data.get(new Vertex(x, y, Direction.S, steps));
            if (d1 != null && d1.weigh < res) {
                res = d1.weigh;
            }
            PathInfo d2 = data.get(new Vertex(x, y, Direction.E, steps));
            if (d2 != null && d2.weigh < res) {
                res = d2.weigh;
            }
        }
        return res;
    }

    private static Map<Vertex, PathInfo> bfs(int[][] input) {
        Vertex start1 = new Vertex(0, 0, Direction.S, 0);
        Vertex start2 = new Vertex(0, 0, Direction.E, 0);
        Set<Vertex> toVisit = new HashSet<>();
        Set<Vertex> visited = new HashSet<>();
        Map<Vertex, PathInfo> data = new HashMap<>();
        data.put(start1, new PathInfo(0));
        data.put(start2, new PathInfo(0));

        toVisit.add(start1);
        toVisit.add(start2);
        long start = System.currentTimeMillis();
        while (!toVisit.isEmpty()) {
            if (System.currentTimeMillis() - start > 10_000) {
                start = System.currentTimeMillis();
                System.out.println("To visit: " + toVisit.size() + ", date: " + new Date());
            }
            Iterator<Vertex> iterator = toVisit.iterator();
            Vertex v = iterator.next();
            iterator.remove();
            if (visited.contains(v)) {
                continue;
            }

            PathInfo info = data.get(v);
            Set<Direction> next = getNextDirections(v, 3);
            for (Direction d : next) {
                int y = d.nextI(v.y);
                int x = d.nextJ(v.x);
                int steps = d == v.source ? v.steps + 1 : 1;
                if (isValid(x, y, input)) {
                    Vertex vNext = new Vertex(x, y, d, steps);
                    long weigh = info.weigh + input[y][x];
                    PathInfo nextInfo = data.get(vNext);
                    if (nextInfo == null || nextInfo.weigh > weigh) {
                        data.put(vNext, new PathInfo(weigh));
                        toVisit.add(vNext);
                        visited.remove(vNext);
                    }
                }
            }

            visited.add(v);
        }

        return data;
    }

    private static Set<Direction> getNextDirections(Vertex v, int maxSteps) {
        Set<Direction> res = EnumSet.allOf(Direction.class);
        res.remove(v.source.opposite());
        if (v.steps == maxSteps) {
            res.remove(v.source);
        }
        return res;
    }

    public static int[][] readFile(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        int[][] res = new int[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                res[i][j] = Integer.parseInt("" + lines.get(i).charAt(j));
            }
        }
        return res;
    }

    private record Vertex(
            int x,
            int y,
            Direction source,
            int steps
    ) {
    }

    private record PathInfo(
            long weigh
            //, int steps
    ) {
    }
}
