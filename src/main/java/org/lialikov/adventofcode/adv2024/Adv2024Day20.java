package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.DataUtil;
import org.lialikov.adventofcode.util.FileUtil;
import org.lialikov.adventofcode.util.MapUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

public class Adv2024Day20 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 8
        System.out.println(getPart1("2024/2024-12-20-sample.txt", 12));
        // 1263
        System.out.println(getPart1("2024/2024-12-20.txt", 100));

        // 8
        System.out.println(getPart2("2024/2024-12-20-sample.txt", 12, 2));
        // 1263
        System.out.println(getPart2("2024/2024-12-20.txt", 100, 2));
        // 285
        System.out.println(getPart2("2024/2024-12-20-sample.txt", 50, 20));
        // 957831
        System.out.println(getPart2("2024/2024-12-20.txt", 100, 20));

        System.out.println(new Date());
    }

    private static Position getEnd(char[][] map) {
        return MapUtil.find(map, 'E');
    }

    private static Position getStart(char[][] map) {
        return MapUtil.find(map, 'S');
    }

    private static void visit(char[][] map, Position start, BiConsumer<Position, Position> consumer) {
        Position current = start;
        Set<Position> visited = new HashSet<>();
        while (current != null) {
            visited.add(current);

            List<Position> next = MapUtil.streamNext(current)
                    .filter(p -> DataUtil.isValid(p, map))
                    .filter(p -> map[p.y()][p.x()] != '#')
                    .filter(p -> !visited.contains(p))
                    .toList();
            if (next.size() > 2) {
                throw new IllegalStateException("Unexpected next positions: " + next);
            } else if (next.size() == 1) {
                Position n = next.get(0);
                consumer.accept(current, n);
                current = n;
            } else {
                consumer.accept(current, null);
                current = null;
            }
        }
    }

    private static Map<Position, Integer> getTimes(char[][] map) {
        Position start = getStart(map);
        Map<Position, Integer> times = new HashMap<>(Map.of(start, 0));
        visit(map, start, (from, to) -> {
            final int fromTime = times.get(from);
            Integer time = times.get(to);
            if (time == null || time > (fromTime + 1)) {
                times.put(to, fromTime + 1);
            }
        });
        return times;
    }

    private static Map<Position, Integer> getFromPositionToEnd(char[][] map, Map<Position, Integer> fromStartToPosition) {
        final int fromStartToEnd = fromStartToPosition.get(getEnd(map));
        Map<Position, Integer> fromPositionToEnd = new HashMap<>();
        fromStartToPosition.forEach((p, t) -> fromPositionToEnd.put(p, fromStartToEnd - t));
        return fromPositionToEnd;
    }

    private static long getPart1(String inputFile, long atLeastSaveTime) {
        char[][] map = FileUtil.toCharArrays(inputFile);

        Map<Position, Integer> fromStartToPosition = getTimes(map);
        Map<Position, Integer> fromPositionToEnd = getFromPositionToEnd(map, fromStartToPosition);
        final long baseTime = fromStartToPosition.get(getEnd(map));

        long res = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '#') {
                    List<Position> toCheck = MapUtil.streamNext(new Position(j, i))
                            .filter(p -> DataUtil.isValid(p, map))
                            .filter(p -> map[p.y()][p.x()] != '#')
                            .toList();
                    if (toCheck.size() < 2) {
                        continue;
                    }

                    int toEnd = Integer.MAX_VALUE;
                    int fromStart = Integer.MAX_VALUE;
                    for (Position p : toCheck) {
                        int timeToEnd = fromPositionToEnd.get(p);
                        if (timeToEnd < toEnd) {
                            toEnd = timeToEnd;
                        }
                        int timeFromStart = fromStartToPosition.get(p);
                        if (timeFromStart < fromStart) {
                            fromStart = timeFromStart;
                        }
                    }

                    long time = fromStart + toEnd + 2;
                    if ((baseTime - time) >= atLeastSaveTime) {
                        res++;
                    }
                }
            }
        }

        return res;
    }

    private static long getPart2(String inputFile, long atLeastSaveTime, int limit) {
        char[][] map = FileUtil.toCharArrays(inputFile);
        Map<Position, Integer> fromStartToPosition = getTimes(map);
        Map<Position, Integer> fromPositionToEnd = getFromPositionToEnd(map, fromStartToPosition);

        final long baseTime = fromStartToPosition.get(getEnd(map));
        AtomicLong res = new AtomicLong(0);
        SortedMap<Long, Integer> counts = new TreeMap<>();
        visit(map, getStart(map), (from, to) -> {
            Map<Position, Integer> toCheck = getToCheck(limit, from, map);
            final int timeFromStart = fromStartToPosition.get(from);

            for (Map.Entry<Position, Integer> p: toCheck.entrySet()) {
                int timeToEnd = fromPositionToEnd.get(p.getKey());
                long time = timeFromStart + timeToEnd + p.getValue();
                long saveTime = baseTime - time;
                if (saveTime >= atLeastSaveTime) {
                    res.incrementAndGet();
                    int v = counts.computeIfAbsent(saveTime, ss -> 0);
                    counts.put(saveTime, v + 1);
                }
            }
        });

        return res.get();
    }

    private static Map<Position, Integer> getToCheck(int limit, Position from, char[][] map) {
        Map<Position, Integer> toCheck = new HashMap<>();

        Set<Position> starts = new HashSet<>(MapUtil.streamNext(from)
                .filter(p -> DataUtil.isValid(p, map))
                .toList());
        Set<Position> visited = new HashSet<>();
        Deque<Position> toVisit = new LinkedList<>(starts);
        Set<Position> toVisitPlan = new HashSet<>(toVisit);
        Map<Position, Integer> length = new HashMap<>();
        toVisit.forEach(p -> length.put(p, 1));
        while (!toVisit.isEmpty()) {
            Position current = toVisit.poll();
            int currentLength = length.get(current);
            visited.add(current);

            MapUtil.streamNext(current)
                    .filter(p -> DataUtil.isValid(p, map))
                    .filter(p -> map[p.y()][p.x()] != '#')
                    .forEach(p -> {
                        Integer pLength = toCheck.get(p);
                        if (pLength == null || pLength > (currentLength + 1)) {
                            toCheck.put(p, currentLength + 1);
                        }
                    });

            if ((currentLength + 1) < limit) {
                MapUtil.streamNext(current)
                        .filter(p -> DataUtil.isValid(p, map))
                        .filter(p -> !visited.contains(p))
                        .filter(p -> !toVisitPlan.contains(p))
                        .forEach(p -> {
                            length.put(p, currentLength + 1);
                            toVisit.add(p);
                            toVisitPlan.add(p);
                        });
            }
        }

        toCheck.remove(from);
        return toCheck;
    }
}
