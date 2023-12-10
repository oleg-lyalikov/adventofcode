package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Adv2023Day10 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 4
        System.out.println(getFarthestSteps("2023/2023-12-10-sample1.txt"));
        // 8
        System.out.println(getFarthestSteps("2023/2023-12-10-sample2.txt"));
        // 6754
        System.out.println(getFarthestSteps("2023/2023-12-10.txt"));

        // 4
        System.out.println(getEnclosed2("2023/2023-12-10-sample3.txt"));
        // 8
        System.out.println(getEnclosed2("2023/2023-12-10-sample4.txt"));
        // 8
        System.out.println(getEnclosed2("2023/2023-12-10-sample5.txt"));
        // 10
        System.out.println(getEnclosed2("2023/2023-12-10-sample6.txt"));
        // 567
        System.out.println(getEnclosed2("2023/2023-12-10.txt"));

//        // 4
//        System.out.println(getEnclosed("2023/2023-12-10-sample3.txt"));
//        // 8
//        System.out.println(getEnclosed("2023/2023-12-10-sample4.txt"));
//        // 8
//        System.out.println(getEnclosed("2023/2023-12-10-sample5.txt"));
//        // 10
//        System.out.println(getEnclosed("2023/2023-12-10-sample6.txt"));
//        // 567
//        System.out.println(getEnclosed("2023/2023-12-10.txt"));

        System.out.println(new Date());
    }

    public static List<List<Point>> readMap(String inputFile) {
        List<List<Point>> map = new ArrayList<>();
        FileUtil.read(inputFile, s -> map.add(s.chars().mapToObj(value -> {
            if ('.' == value) {
                return new Point(Type.GROUND, null, (char) value);
            } else if ('S' == value) {
                return new Point(Type.START, null, (char) value);
            } else {
                return new Point(Type.PIPE, getPipeByChar(value), (char) value);
            }
        }).collect(Collectors.toList())));
        return map;
    }

    public static long getFarthestSteps(String inputFile) {
        List<List<Point>> map = readMap(inputFile);
        return getFarthestSteps(map);
    }

    public static long getFarthestSteps(List<List<Point>> map) {
        setLoopStatus(map);
        long res = 0;
        for (List<Point> points : map) {
            for (Point point : points) {
                if (point.status == Status.LOOP) {
                    res++;
                }
            }
        }
        long farthest = res / 2;
        if (res % 2 != 0) {
            farthest++;
        }
        return farthest;
    }

    private static List<List<Status>> initStatus(List<List<Point>> map) {
        List<List<Status>> res = new ArrayList<>(map.size());
        for (List<Point> points : map) {
            List<Status> statusLine = new ArrayList<>(points.size());
            points.forEach(p -> statusLine.add(null));
            res.add(statusLine);

        }
        return res;
    }

    public static void setLoopStatus(List<List<Point>> map) {
        Pair<Integer, Integer> start = getStart(map);
        List<Direction> directions = List.of(Direction.N, Direction.W, Direction.E, Direction.S);
        Direction startDirection = null;
        Direction endDirection = null;
        for (Direction d : directions) {
            startDirection = d;
            Direction next = d;
            int x = start.getFirst();
            int y = start.getSecond();
            boolean failed = false;
            List<List<Status>> statuses = initStatus(map);
            statuses.get(y).set(x, Status.LOOP);
            while (true) {
                if (next == Direction.N) {
                    y--;
                } else if (next == Direction.W) {
                    x--;
                } else if (next == Direction.E) {
                    x++;
                } else if (next == Direction.S) {
                    y++;
                }
                if (x < 0 || x >= map.get(0).size()) {
                    failed = true;
                    break;
                }
                if (y < 0 || y >= map.size()) {
                    failed = true;
                    break;
                }

                if (x == start.getFirst() && y == start.getSecond()) {
                    endDirection = next.opposite();
                    break;
                }

                Point north = map.get(y).get(x);
                if (north.type != Type.PIPE) {
                    failed = true;
                    break;
                }

                Direction used = next.opposite();
                if (north.pipe.d1 == used) {
                    next = north.pipe.d2;
                } else if (north.pipe.d2 == used) {
                    next = north.pipe.d1;
                } else {
                    failed = true;
                    break;
                }
                statuses.get(y).set(x, Status.LOOP);
            }
            if (!failed) {
                for (int yy = 0; yy < map.size(); yy++) {
                    for (int xx = 0; xx < map.get(yy).size(); xx++) {
                        map.get(yy).get(xx).status = statuses.get(yy).get(xx);
                    }
                }
                map.get(start.getSecond()).get(start.getFirst()).pipe = new Pipe(startDirection, endDirection);
                return;
            }
        }
    }

    public static Pair<Integer, Integer> getStart(List<List<Point>> map) {
        for (int y = 0; y < map.size(); y++) {
            List<Point> line = map.get(y);
            for (int x = 0; x < line.size(); x++) {
                if (line.get(x).type == Type.START) {
                    return Pair.of(x, y);
                }
            }
        }
        throw new IllegalStateException("Cannot happen");
    }

    public static long getEnclosed(String inputFile) {
        List<List<Point>> map = readMap(inputFile);
        setLoopStatus(map);
        setIndexes(map);
        setOutStatus(map);
        //print(map);
        setEnclosedStatus(map);
        dfsSetEnclosedStatus(map);
        setEnclosedStatus(map);
        dfsSetEnclosedStatus(map);
        setEnclosedStatus(map);
        dfsSetEnclosedStatus(map);
        setEnclosedStatus(map);
        dfsSetEnclosedStatus(map);
        setEnclosedStatus(map);
        dfsSetEnclosedStatus(map);
        print(map);

        return getStatusCount(map, Status.ENCLOSED);
    }

    private static long getStatusCount(List<List<Point>> map, Status status) {
        long res = 0;
        int xSize = map.get(0).size();
        for (List<Point> points : map) {
            for (int x = 0; x < xSize; x++) {
                Point p = points.get(x);
                if (p.status == status) {
                    res++;
                }
            }
        }
        return res;
    }

    public static long getEnclosed2(String inputFile) {
        List<List<Point>> map = readMap(inputFile);
        setLoopStatus(map);
        setIndexes(map);
        setOutStatus(map);

        int xSise = map.get(0).size();
        int count = 0;
        for (List<Point> points : map) {
            for (int x = 0; x < xSise; x++) {
                Point p = points.get(x);
                if (p.status == Status.LOOP) {
                    if (hasDirections(p.pipe, Direction.N, Direction.S) ||
                            hasDirections(p.pipe, Direction.N, Direction.E) ||
                            hasDirections(p.pipe, Direction.N, Direction.W)) {
                        count++;
                    }
                } else if (p.status == null) {
                    p.status = (count % 2 == 1) ? Status.ENCLOSED : Status.OUT;
                }
            }
        }
        print(map);

        return getStatusCount(map, Status.ENCLOSED);
    }

    private static boolean hasDirections(Pipe p, Direction d1, Direction d2) {
        return (p.d1 == d1 && p.d2 == d2) || (p.d1 == d2 && p.d2 == d1);
    }

    private static void print(List<List<Point>> map) {
        for (List<Point> points : map) {
            for (Point p : points) {
                if (p.status == Status.OUT) {
                    System.out.print(' ');
                } else if (p.status == Status.ENCLOSED) {
                    System.out.print(' ');
                } else if (p.status == Status.LOOP || p.pipe != null) {
                    if (p.original == '7') {
                        System.out.print('┐');
                    } else if (p.original == 'L') {
                        System.out.print('┕');
                    } else if (p.original == 'J') {
                        System.out.print('┙');
                    } else if (p.original == 'F') {
                        System.out.print('┌');
                    } else if (p.original == '|') {
                        System.out.print('│');
                    } else if (p.original == '-') {
                        System.out.print('─');
                    } else {
                        System.out.print(p.original);
                    }
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
    }

    private static void setIndexes(List<List<Point>> map) {
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(y).size(); x++) {
                Point p = map.get(y).get(x);
                p.x = x;
                p.y = y;
            }
        }
    }

    private static void dfsSetEnclosedStatus(List<List<Point>> map) {
        Set<Point> visited = new HashSet<>();
        Deque<Point> toVisit = new LinkedList<>();
        for (List<Point> points : map) {
            points.forEach(p -> {
                if (p.status == Status.ENCLOSED) {
                    toVisit.add(p);
                }
            });
        }
        int xSize = map.get(0).size();
        while (!toVisit.isEmpty()) {
            Point next = toVisit.poll();
            visited.add(next);
            next.status = Status.ENCLOSED;

            List<Point> toTry = new ArrayList<>();
            if (next.x > 0) {
                toTry.add(map.get(next.y).get(next.x - 1));
            }
            if (next.x < xSize - 1) {
                toTry.add(map.get(next.y).get(next.x + 1));
            }
            if (next.y > 0) {
                toTry.add(map.get(next.y - 1).get(next.x));
            }
            if (next.y < map.size() - 1) {
                toTry.add(map.get(next.y + 1).get(next.x));
            }
            toTry.stream()
                    .filter(tt -> tt.status == null && !visited.contains(tt))
                    .forEach(toVisit::add);
        }
    }

    private static void setOutStatus(List<List<Point>> map) {
        Set<Point> visited = new HashSet<>();
        Deque<Point> toVisit = new LinkedList<>();
        for (List<Point> points : map) {
            Point p = points.get(0);
            if (p.status == null) {
                toVisit.add(p);
            }
            p = points.get(points.size() - 1);
            if (p.status == null) {
                toVisit.add(p);
            }
        }
        int xSize = map.get(0).size();
        for (int x = 0; x < xSize; x++) {
            Point p = map.get(0).get(x);
            if (p.status == null) {
                toVisit.add(p);
            }
            p = map.get(map.size() - 1).get(x);
            if (p.status == null) {
                toVisit.add(p);
            }
        }
        while (!toVisit.isEmpty()) {
            Point next = toVisit.poll();
            visited.add(next);
            if (next.status == null) {
                next.status = Status.OUT;
                List<Point> toTry = new ArrayList<>();
                if (next.x > 0) {
                    toTry.add(map.get(next.y).get(next.x - 1));
                }
                if (next.x < xSize - 1) {
                    toTry.add(map.get(next.y).get(next.x + 1));
                }
                if (next.y > 0) {
                    toTry.add(map.get(next.y - 1).get(next.x));
                }
                if (next.y < map.size() - 1) {
                    toTry.add(map.get(next.y + 1).get(next.x));
                }
                toTry.stream()
                        .filter(tt -> !visited.contains(tt))
                        .forEach(toVisit::add);
            }
        }
    }

    private static void setEnclosed(Point p, int count) {
        if (p.status == null) {
            if (count % 2 == 1) {
                p.status = Status.ENCLOSED;
            }
        }
    }

    private static int getNextCountByVertical(Point p, int count) {
        if (p.pipe == null || p.status != Status.LOOP || !hasVertical(p.pipe)) {
            return count;
        }
        return count + 1;
    }

    private static int getNextCountByHorizontal(Point p, int count) {
        if (p.pipe == null || !hasHorizontal(p.pipe)) {
            return count;
        }
        return count + 1;
    }

    private static void setEnclosedStatus(List<List<Point>> map) {
        int xSize = map.get(0).size();
        for (int y = 0; y < map.size(); y++) {
            int count = 0;
            for (int x = 0; x < xSize; x++) {
                Point p = map.get(y).get(x);
                setEnclosed(p, count);
                count = getNextCountByVertical(p, count);
            }
            count = 0;
            for (int x = xSize - 1; x >= 0; x--) {
                Point p = map.get(y).get(x);
                setEnclosed(p, count);
                count = getNextCountByVertical(p, count);
            }
        }
//        for (int x = 0; x < xSize; x++) {
//            int count = 0;
//            for (int y = 0; y < map.size(); y++) {
//                Point p = map.get(y).get(x);
//                setEnclosed(p, count);
//                count = getNextCountByHorizontal(p, count);
//            }
//            count = 0;
//            for (int y = map.size() - 1; y >= 0; y--) {
//                Point p = map.get(y).get(x);
//                setEnclosed(p, count);
//                count = getNextCountByHorizontal(p, count);
//            }
//        }
    }

    private static boolean hasVertical(Pipe pipe) {
        return hasDirections(pipe, Direction.N, Direction.S) || hasDirections(pipe, Direction.N, Direction.W) || hasDirections(pipe, Direction.N, Direction.E);
        //return pipe.d1 == Direction.N || pipe.d1 == Direction.S || pipe.d2 == Direction.N || pipe.d2 == Direction.S;
    }
    private static boolean hasHorizontal(Pipe pipe) {
        return false;
        //return pipe.d1 == Direction.E || pipe.d1 == Direction.W || pipe.d2 == Direction.E || pipe.d2 == Direction.W;
    }

    private static Pipe getPipeByChar(int value) {
        return switch (value) {
            case '|' -> new Pipe(Direction.N, Direction.S);
            case '-' -> new Pipe(Direction.W, Direction.E);
            case 'L' -> new Pipe(Direction.N, Direction.E);
            case 'J' -> new Pipe(Direction.N, Direction.W);
            case '7' -> new Pipe(Direction.S, Direction.W);
            case 'F' -> new Pipe(Direction.S, Direction.E);
            default -> throw new RuntimeException("Not found: " + value);
        };
    }

    public enum Direction {
        N,
        W,
        S,
        E;

        public Direction opposite() {
            if (this == N) {
                return S;
            } else if (this == W) {
                return E;
            } else if (this == S) {
                return N;
            } else if (this == E) {
                return W;
            }
            throw new IllegalStateException("Not possible");
        }
    }

    public record Pipe(
            Direction d1,
            Direction d2
    ) {
    }

    public enum Type {
        START,
        GROUND,
        PIPE
    }

    public static class Point {
        public Point(Type type, Pipe pipe, char original) {
            this.type = type;
            this.pipe = pipe;
            this.original = original;
        }

        Type type;
        Pipe pipe;
        char original;
        Status status;
        int x;
        int y;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            if (y != point.y) return false;
            if (type != point.type) return false;
            return Objects.equals(pipe, point.pipe);
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + (pipe != null ? pipe.hashCode() : 0);
            result = 31 * result + x;
            result = 31 * result + y;
            return result;
        }
    }

    public enum Status {
        ENCLOSED,
        OUT,
        LOOP
    }
}
