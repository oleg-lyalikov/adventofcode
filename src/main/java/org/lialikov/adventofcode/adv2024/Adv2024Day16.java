package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.model.Direction;
import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;

import static org.lialikov.adventofcode.util.MapUtil.find;

public class Adv2024Day16 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 7036
        System.out.println(getPart1("2024/2024-12-16-sample1.txt"));
        // 11048
        System.out.println(getPart1("2024/2024-12-16-sample2.txt"));
        // 143580
        System.out.println(getPart1("2024/2024-12-16.txt"));

        // 45
        System.out.println(getPart2("2024/2024-12-16-sample1.txt"));
        // 64
        System.out.println(getPart2("2024/2024-12-16-sample2.txt"));
        // 645
        System.out.println(getPart2("2024/2024-12-16.txt"));

        System.out.println(new Date());
    }

    private static final long rotateCost = 1000;

    private static long getMoveCost(Direction currentD, Direction nextD) {
        if (currentD == nextD) {
            return 1;
        } else if ((currentD == Direction.E && nextD == Direction.W) || (currentD == Direction.W && nextD == Direction.E) ||
                    (currentD == Direction.N && nextD == Direction.S) || (currentD == Direction.S && nextD == Direction.N)) {
            return rotateCost * 2 + 1;
        } else {
            return rotateCost + 1;
        }
    }

    private static long findShortestPath(char[][] map, Position start, Position end) {
        return findShortestPath(map, start).get(end);
    }

    private static Map<Position, Long> findShortestPath(char[][] map, Position start) {
        Map<Position, Long> res = new HashMap<>();
        res.put(start, 0L);
        Set<PositionDirection> toVisit = new HashSet<>();
        toVisit.add(new PositionDirection(start, Direction.E));
        Set<Position> visited = new HashSet<>();
        while (!toVisit.isEmpty()) {
            long min = Long.MAX_VALUE;
            PositionDirection current = null;
            for (PositionDirection pd : toVisit) {
                Long distance = res.get(pd.p);
                if (distance != null && distance < min) {
                    current = pd;
                    min = distance;
                }
            }
            if (current == null) {
                break;
            }
            toVisit.remove(current);
            if (visited.contains(current.p)) {
                continue;
            }
            visited.add(current.p);

            long distance = res.get(current.p);
            for (Direction nextD : Direction.values()) {
                Position nextP = nextD.nextP(current.p);
                if (!visited.contains(nextP) && map[nextP.y()][nextP.x()] != '#') {
                    long cost = getMoveCost(current.d, nextD);
                    cost += distance;
                    Long nextCost = res.get(nextP);
                    if (nextCost == null || nextCost > cost) {
                        res.put(nextP, cost);
                    }
                    toVisit.add(new PositionDirection(nextP, nextD));
                }
            }
        }
        return res;
    }

    private static long getPart1(String inputFile) {
        char[][] map = FileUtil.toCharArrays(inputFile);
        Position start = find(map, 'S');
        Position end = find(map, 'E');
        return findShortestPath(map, start, end);
    }

    private static long getPart2(String inputFile) {
        char[][] map = FileUtil.toCharArrays(inputFile);
        Position start = find(map, 'S');
        Position end = find(map, 'E');
        ShortestPaths res = findShortestPath2(map, start);

        List<Direction> minD = new ArrayList<>();
        Long minValue = null;
        for (Direction d : Direction.values()) {
            Long value = res.shortestPaths.get(new PositionDirection(end, d));
            if (value != null && (minValue == null || minValue > value)) {
                minValue = value;
            }
        }
        for (Direction d : Direction.values()) {
            Long value = res.shortestPaths.get(new PositionDirection(end, d));
            if (value != null && value.equals(minValue)) {
                minD.add(d);
            }
        }

        Set<Position> result = new HashSet<>();
        for (Direction d : minD) {
            Set<Position> paths = res.paths.get(new PositionDirection(end, d));
            result.addAll(paths);
        }
        return result.size();
    }

    private static ShortestPaths findShortestPath2(char[][] map, Position start) {
        PositionDirection startPositionDirection = new PositionDirection(start, Direction.E);

        Map<PositionDirection, Long> res = new HashMap<>(Map.of(startPositionDirection, 0L));
        Set<PositionDirection> toVisit = new HashSet<>(List.of(startPositionDirection));
        Map<PositionDirection, Set<Position>> paths = new HashMap<>(Map.of(startPositionDirection, new HashSet<>(List.of(start))));

        Set<PositionDirection> visited = new HashSet<>();
        while (!toVisit.isEmpty()) {
            long min = Long.MAX_VALUE;
            PositionDirection current = null;
            for (PositionDirection pd : toVisit) {
                Long distance = res.get(pd);
                if (distance != null && distance < min) {
                    current = pd;
                    min = distance;
                }
            }
            if (current == null) {
                break;
            }
            toVisit.remove(current);
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            long distance = res.get(current);
            for (Direction nextD : Direction.values()) {
                Position nextP = nextD.nextP(current.p);
                if (map[nextP.y()][nextP.x()] != '#') {
                    long cost = getMoveCost(current.d, nextD);
                    cost += distance;
                    PositionDirection nextPositionDirection = new PositionDirection(nextP, nextD);
                    Long nextCost = res.get(nextPositionDirection);
                    if (nextCost == null || nextCost > cost) {
                        res.put(nextPositionDirection, cost);

                        Set<Position> nextPath = new HashSet<>();
                        addPaths(nextPath, paths.get(current), nextP);
                        paths.put(nextPositionDirection, nextPath);
                    } else if (nextCost == cost) {
                        Set<Position> nextPath = paths.get(nextPositionDirection);
                        addPaths(nextPath, paths.get(current), nextP);
                    }
                    toVisit.add(new PositionDirection(nextP, nextD));
                }
            }
        }
        return new ShortestPaths(res, paths);
    }

    private static void addPaths(Set<Position> nextPaths, Set<Position> toAdd, Position last) {
        nextPaths.addAll(toAdd);
        nextPaths.add(last);
    }

    private record PositionDirection(Position p, Direction d) { }
    private record ShortestPaths(
            Map<PositionDirection, Long> shortestPaths,
            Map<PositionDirection, Set<Position>> paths
    ) {}
}
