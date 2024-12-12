package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.model.Direction;
import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Adv2023Day18 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 62
        //S######
        //#.....#
        //###...#
        //..#...#
        //..#...#
        //###.###
        //#...#..
        //##..###
        //.#....#
        //.######
        //System.out.println(getPart1("2023/2023-12-18-sample.txt"));
        // 47139
        //System.out.println(getPart1("2023/2023-12-18.txt"));
        // 62
        //System.out.println(getPart1V2("2023/2023-12-18-sample.txt"));
        // 47139
        //System.out.println(getPart1V2("2023/2023-12-18.txt"));

        // 952408144115
        System.out.println(getPart2("2023/2023-12-18-sample.txt"));
        // 173152345887206
        System.out.println(getPart2("2023/2023-12-18.txt"));

        System.out.println(new Date());
    }

    public static long getPart2(String inputFile) {
        return getPart1V2(readMapV2(inputFile, true));
    }

    public static long getPart1V2(InputMap map) {
        long res = 0;
        boolean enclosed = false;

        int xSize = map.data[0].length;
        long resInRow = 0;
        //        S###### 7
        //        #.....# 14
        //        ###...# 21
        //        ..#...# 26
        //        ..#...# 31
        //        ###.### 38
        //        #...#.. 43
        //        ##..### 50
        //        .#....# 56
        //        .###### 62
        List<Integer> xGoesDown = new ArrayList<>();
        for (int y = 0; y < map.data.length; y++) {
            if (y > 0) {
                int diff = map.distinctY[y] - map.distinctY[y - 1] - 1;
                long enclosed1 = 0;
                for (int i = 0; i < xGoesDown.size(); i += 2) {
                    enclosed1 += (map.data[y - 1][xGoesDown.get(i + 1)].x - map.data[y - 1][xGoesDown.get(i)].x) + 1;
                }
                res += (enclosed1 * diff);
            }

            SmallPoint prev = null;
            resInRow = 0;
            xGoesDown = new ArrayList<>();
            for (int x = 0; x < xSize; x++) {
                SmallPoint p = map.data[y][x];
                if (p == null) {
                    continue;
                }

                resInRow++;
                if (prev != null && (enclosed || (prev.pipe.d1 == Direction.E || p.pipe.d1 == Direction.W))) {
                    resInRow += (p.x - prev.x - 1);
                }

                if (p.changes) {
                    enclosed = !enclosed;
                }

                if (p.pipe.d1 == Direction.S || p.pipe.d2 == Direction.S) {
                    xGoesDown.add(x);
                }

                prev = p;
            }
            res += resInRow;
        }

        return res;
    }

    public static long getPart1V2(String inputFile) {
        return getPart1V2(readMapV2(inputFile, false));
    }

    private static List<InputLine> readInputLines(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        return lines.stream().map(l -> {
            Direction d = switch (l.charAt(0)) {
                case 'R' -> Direction.E;
                case 'D' -> Direction.S;
                case 'L' -> Direction.W;
                case 'U' -> Direction.N;
                default -> throw new IllegalStateException("Illegal direction: " + l.charAt(0));
            };
            String[] parts = l.split("\\s+");
            return new InputLine(d, Integer.parseInt(parts[1]), parts[2]);
        }).toList();
    }

    private static List<InputLine> readInputLines2(String inputFile) {
        List<InputLine> input = readInputLines(inputFile);
        return input.stream()
                .map(i -> {
                    int direction = Integer.parseInt(i.color.substring(i.color.length() - 2, i.color.length() - 1));
                    Direction d = switch (direction) {
                        case 0 -> Direction.E;
                        case 1 -> Direction.S;
                        case 2 -> Direction.W;
                        case 3 -> Direction.N;
                        default -> throw new IllegalStateException("" + direction);
                    };
                    String base64 = i.color.substring(2, i.color.length() - 2);
                    int count = Integer.parseInt(base64, 16);
                    return new InputLine(d, count, i.color);
                })
                .collect(Collectors.toList());
    }

    private static boolean changesEnclosing(Pipe p) {
        return hasDirections(p, Direction.N, Direction.S) ||
                hasDirections(p, Direction.N, Direction.E) ||
                hasDirections(p, Direction.N, Direction.W);
    }
    public static InputMap readMapV2(String inputFile, boolean part2) {
        List<InputLine> input = part2 ? readInputLines2(inputFile) : readInputLines(inputFile);

        int x = 0;
        int y = 0;
        List<List<Point>> res = new ArrayList<>();
        res.add(new ArrayList<>());
        Point start = new Point(Type.START, null, x, y, Status.LOOP);
        res.get(y).add(start);
        Point lastPoint = null;
        SortedMap<Integer, List<SmallPoint>> byY = new TreeMap<>();
        SortedMap<Integer, List<SmallPoint>> byX = new TreeMap<>();
        for (InputLine line : input) {
            if (lastPoint != null) {
                Pipe p = new Pipe(lastPoint.pipe.d1, line.d);
                boolean changesEnclosing = changesEnclosing(p);
                SmallPoint smallPoint = new SmallPoint(lastPoint.x, lastPoint.y, changesEnclosing, p);
                byY.computeIfAbsent(y, ddd -> new ArrayList<>()).add(smallPoint);
                byX.computeIfAbsent(x, ddd -> new ArrayList<>()).add(smallPoint);
            }
            if (line.d == Direction.E || line.d == Direction.W) {
                x = line.d == Direction.E ? (x + line.count) : (x - line.count);
            } else {
                y = line.d == Direction.S ? (y + line.count) : (y - line.count);
            }
            lastPoint = new Point(Type.PIPE, new Pipe(line.d.opposite(), null), x, y, Status.LOOP);
        }
        //noinspection DataFlowIssue
        if (lastPoint.x != 0 || lastPoint.y != 0) {
            throw new IllegalStateException("!!!");
        }
        Pipe p = new Pipe(lastPoint.pipe.d1, input.get(0).d);
        boolean changesEnclosing = changesEnclosing(p);
        SmallPoint smallPoint = new SmallPoint(lastPoint.x, lastPoint.y, changesEnclosing, p);
        byY.get(y).add(smallPoint);
        byX.get(x).add(smallPoint);

        byY.forEach((k, v) -> v.sort(Comparator.comparingInt(value -> value.x)));
        byX.forEach((k, v) -> v.sort(Comparator.comparingInt(value -> value.y)));

        int[] distinctY = new int[byY.keySet().size()];
        Map<Integer, Integer> yToIndex = new HashMap<>();

        int i = 0;
        for (int v : byY.keySet()) {
            distinctY[i] = v;
            yToIndex.put(v, i);
            i++;
        }
        int[] distinctX = new int[byX.keySet().size()];
        Map<Integer, Integer> xToIndex = new HashMap<>();
        i = 0;
        for (int v : byX.keySet()) {
            distinctX[i] = v;
            xToIndex.put(v, i);
            i++;
        }


        SmallPoint[][] res1 = new SmallPoint[byY.size()][byX.size()];
        for (i = 0; i < distinctY.length; i++) {
            List<SmallPoint> line = byY.get(distinctY[i]);
            SmallPoint prev = null;
            for (SmallPoint sp : line) {
                res1[i][xToIndex.get(sp.x)] = sp;
                if ((prev != null && prev.pipe.d2 == Direction.E) ||
                        (prev != null && sp.pipe.d2 == Direction.W)) {
                    Pipe pipe = prev.pipe.d2 == Direction.E ? new Pipe(Direction.W, Direction.E) : new Pipe(Direction.E, Direction.W);
                    for (int j = xToIndex.get(prev.x) + 1; j < xToIndex.get(sp.x); j++) {
                        if (res1[i][j] != null) {
                            throw new IllegalStateException("!!!");
                        } else {
                            res1[i][j] = new SmallPoint(distinctX[j], distinctY[i], false, pipe);
                        }
                    }
                }
                prev = sp;
            }
        }
        //print("Init0", res1);
        for (int j = 0; j < distinctX.length; j++) {
            SmallPoint prev = null;
            for (i = 0; i < distinctY.length; i++) {
                SmallPoint current = res1[i][j];
                if (current == null) {
                    continue;
                }
                if (prev != null) {
                    if ((prev.pipe != null && prev.pipe.d2 == Direction.S && current.pipe != null) ||
                            (prev.pipe != null && current.pipe != null && current.pipe.d2 == Direction.N)) {
                        Pipe pipe = prev.pipe.d2 == Direction.S ? new Pipe(Direction.N, Direction.S) : new Pipe(Direction.S, Direction.N);
                        for (int ii = yToIndex.get(prev.y) + 1; ii < yToIndex.get(current.y); ii++) {
                            if (res1[ii][j] != null) {
                                throw new IllegalStateException("!!!");
                            } else {
                                res1[ii][j] = new SmallPoint(distinctX[j], distinctY[ii], true, pipe);
                            }
                        }
                    }
                }

                prev = current;
            }
        }

        print("Init", res1);
        return new InputMap(res1, distinctY);
    }

    public static long getPart1(String inputFile) {
        List<List<Point>> map = readMap(inputFile);
        print("Init", map);

        //setLoopStatus(map);
        //print("After loop status", map);
        setOutStatus(map);
        //print("After set out status", map);

        int xSize = map.get(0).size();
        int count = 0;
        //noinspection ForLoopReplaceableByForEach
        for (int y = 0; y < map.size(); y++) {
            List<Point> points = map.get(y);
            for (int x = 0; x < xSize; x++) {
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
        //print("final", map);

        return getStatusCount(map, Status.ENCLOSED) + getStatusCount(map, Status.LOOP);
    }

    private static boolean hasDirections(Pipe p, Direction d1, Direction d2) {
        return (p.d1 == d1 && p.d2 == d2) || (p.d1 == d2 && p.d2 == d1);
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

    private static void print(String text, SmallPoint[][] map) {
        System.out.println(text);
        for (SmallPoint[] points : map) {
            for (SmallPoint p : points) {
                if (p == null) {
                    System.out.print(' ');
                } else {
                    System.out.print('#');
                }
            }
            System.out.println();
        }
    }

    private static void print(String text, List<List<Point>> map) {
        System.out.println(text);
        for (List<Point> points : map) {
            for (Point p : points) {
//                if (p.status == Status.LOOP) {
//                    System.out.print('L');
//                } else {
//                    System.out.print(' ');
//                }
                if (p.type == Type.START) {
                    System.out.print('S');
                } else if (p.status == Status.OUT) {
                    System.out.print(' ');
                } else if (p.status == Status.ENCLOSED) {
                    System.out.print('I');
                } else if (p.status == Status.LOOP || p.pipe != null) {
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
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

    private static void setLoopStatus(List<List<Point>> map) {
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
    private static List<List<Status>> initStatus(List<List<Point>> map) {
        List<List<Status>> res = new ArrayList<>(map.size());
        for (List<Point> points : map) {
            List<Status> statusLine = new ArrayList<>(points.size());
            points.forEach(p -> statusLine.add(null));
            res.add(statusLine);

        }
        return res;
    }

    public static List<List<Point>> readMap(String inputFile) {
        List<InputLine> input = readInputLines(inputFile);

        int x = 0;
        int y = 0;
        List<List<Point>> res = new ArrayList<>();
        res.add(new ArrayList<>());
        //Point start = new Point(Type.START, new Pipe(Direction.E, Direction.E), x, y, Status.LOOP);
        Point start = new Point(Type.START, null, x, y, Status.LOOP);
        res.get(y).add(start);
        Point lastPoint = null;
        for (InputLine line : input) {
            if (lastPoint != null) {
                lastPoint.pipe = new Pipe(lastPoint.pipe.d1, line.d);
            }
            if (line.d == Direction.E || line.d == Direction.W) {
                for (int i = 0; i < line.count; i++) {
                    Direction d1 = line.d.opposite();
                    Direction d2 = i == line.count - 1 ? null : line.d;
                    int index = line.d == Direction.E ? (x + i + 1) : (x - i - 1);
                    extend(res, index, y);
                    if (index < 0) {
                        index = 0;
                    }
                    Point p = new Point(Type.PIPE, new Pipe(d1, d2), index, y, Status.LOOP);
                    res.get(y).set(index, p);
                    lastPoint = p;
                }
            } else {
                for (int i = 0; i < line.count; i++) {
                    Direction d1 = line.d.opposite();
                    Direction d2 = i == line.count - 1 ? null : line.d;
                    int index = line.d == Direction.S ? (y + i + 1) : (y - i - 1);
                    extend(res, x, index);
                    if (index < 0) {
                        index = 0;
                    }
                    Point p = new Point(Type.PIPE, new Pipe(d1, d2), x, index, Status.LOOP);
                    res.get(index).set(x, p);
                    lastPoint = p;
                }
            }
            //noinspection DataFlowIssue
            x = lastPoint.x;
            y = lastPoint.y;
        }
        //noinspection DataFlowIssue
        if (lastPoint.x != 0 || lastPoint.y != 0) {
            //throw new IllegalStateException("!!!");
        }
        lastPoint.pipe = new Pipe(lastPoint.pipe.d1, input.get(0).d);
        lastPoint.type = Type.START;

        int max = 0;
        for (List<Point> re : res) {
            if (re.size() > max) {
                max = re.size();
            }
        }
        for (int i = 0; i < res.size(); i++) {
            extend(res, max - 1, i);
        }

        return res;
    }

    private static void updateIndexes(List<List<Point>> res) {
        for (int i = 0; i < res.size(); i++) {
            for (int j = 0; j < res.get(i).size(); j++) {
                Point p = res.get(i).get(j);
                p.x = j;
                p.y = i;
            }
        }
    }

    private static void extend(List<List<Point>> res, int x, int y) {
        if (res.size() == y) {
            res.add(new ArrayList<>());
        } else if (y < 0) {
            res.add(0, new ArrayList<>());
            y = 0;
            updateIndexes(res);
        }
        List<Point> line = res.get(y);
        if (line.size() <= x) {
            int max = x - line.size() + 1;
            for (int i = 0; i < max; i++) {
                line.add(new Point(Type.GROUND, null, line.size(), y, null));
            }
        } else if (x < 0) {
            for (int i = 0; i < res.size(); i++) {
                res.get(i).add(0, new Point(Type.GROUND, null, 0, i, null));
            }
            updateIndexes(res);
        }
    }

//    public static List<List<Point>> readMap(String inputFile) {
//        List<List<Point>> map = new ArrayList<>();
//        FileUtil.read(inputFile, s -> map.add(s.chars().mapToObj(value -> {
//            if ('.' == value) {
//                return new Point(Type.GROUND, null, (char) value);
//            } else if ('S' == value) {
//                return new Point(Type.START, null, (char) value);
//            } else {
//                return new Point(Type.PIPE, getPipeByChar(value), (char) value);
//            }
//        }).collect(Collectors.toList())));
//        return map;
//    }

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
        public Point(Type type, Pipe pipe, int x, int y, Status status) {
            this.type = type;
            this.pipe = pipe;
            this.x = x;
            this.y = y;
            this.status = status;
        }

        Type type;
        Pipe pipe;
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

    public record InputLine(
            Direction d,
            int count,
            String color
    ) {
    }

    public record SmallPoint (
        int x,
        int y,
        boolean changes,
        Pipe pipe
    ) {}

    public record InputMap(
            SmallPoint[][] data,
            int[] distinctY
    ) {
    }
}
