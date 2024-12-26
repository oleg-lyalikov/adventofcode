package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;

public class Adv2024Day21 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 126384
        System.out.println(getPart1("2024/2024-12-21-sample.txt"));
        // 212830 That's not the right answer; your answer is too high.
        // 202902 That's not the right answer; your answer is too low.
        // 206798
        System.out.println(getPart1("2024/2024-12-21.txt"));

        // 126384
        System.out.println(getPart2("2024/2024-12-21-sample.txt", 2));
        // 154115708116294 ?
        System.out.println(getPart2("2024/2024-12-21-sample.txt", 25));
        // 206798
        System.out.println(getPart2("2024/2024-12-21.txt", 2));
        // 752640519372310 That's not the right answer; your answer is too high.
        // 300829231931924 That's not the right answer; your answer is too high.
        // 299308553583764 That's not the right answer; your answer is too high.
        // 287046661958274 That's not the right answer.
        // 208477261143644 That's not the right answer.
        // 251508572750680
        System.out.println(getPart2("2024/2024-12-21.txt", 25));

        System.out.println(new Date());
    }

    private static Map<Character, Position> getPositions(char[][] data) {
        Map<Character, Position> positions = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                positions.put(data[i][j], new Position(j, i));
            }
        }
        return positions;
    }

    private static final char[][] numeric = new char[][] {
            { '7', '8', '9' },
            { '4', '5', '6' },
            { '1', '2', '3' },
            { 'X', '0', 'A' }
    };
    private static final Map<Character, Position> numericPositions = getPositions(numeric);
    private static final char[][] directional = new char[][] {
            { 'X', '^', 'A' },
            { '<', 'v', '>' }
    };

    // -------------- 27
    // v<<A>>^A  vA^A    v<<A >>^A A  v<A <A >>^A A vAA ^<A >A v<A >^A A <A >A v<A <A >>^A AA vA ^<A >A
    // <A          >A    <       A A    v  <    A A  >>   ^  A   v   A A  ^  A   v  <    A AA  >   ^  A
    //  ^           A            ^ ^            < <          A       > >     A           v vv         A
    //              3                                        7               9                        A
    // -------------- 23
    // <v<A>>^A vA^A    <vA <A  A  >>^A     A    vA <^A >A A vA ^A <vA>^AA<A>A<v<A>A>^AAAvA<^A>A
    // <A         >A      v  <  <     A     A    >  ^    A A  >  A
    //  ^          A                  <     <            ^ ^     A
    //             3                                             7
    private static final Map<Character, Position> directionalPositions = getPositions(directional);

    private static void addPath(List<Character> path, int n, char d) {
        for (int i = 0; i < n; i++) {
            path.add(d);
        }
    }

    private static List<Character> getDirectionalPath(List<Character> path) {
        Position current = directionalPositions.get('A');
        List<Character> res = new ArrayList<>();
        for (char ch : path) {
            Position next = directionalPositions.get(ch);

            int nY = Math.abs(current.y() - next.y());
            char dY = current.y() < next.y() ? 'v' : '^';
            int nX = Math.abs(current.x() - next.x());
            char dX = current.x() < next.x() ? '>' : '<';

            addPath(res, nY, dY);
            addPath(res, nX, dX);

            res.add('A');

            current = next;
        }
        return res;
    }

    private static List<List<Character>> getNumericPaths(String line) {
        int kMax = (int) Math.pow(2, line.length());
        Set<List<Character>> res = new HashSet<>();
        for (int k = 0; k < kMax; k++) {
            List<Character> path = getNumericPath2(line, k);
            res.add(path);
        }
        return new ArrayList<>(res);
    }

    private static List<Character> getNumericPath2(String line, int k) {
        Position current = numericPositions.get('A');
        List<Character> path = new ArrayList<>();
        String s = Integer.toBinaryString(k);
        int index = 0;
        for (char ch : line.toCharArray()) {
            Position next = numericPositions.get(ch);

            int nY = Math.abs(current.y() - next.y());
            char dY = current.y() < next.y() ? 'v' : '^';
            int nX = Math.abs(current.x() - next.x());
            char dX = current.x() < next.x() ? '>' : '<';
            if (current.y() == numeric.length - 1 && next.x() == 0) {
                addPath(path, nY, dY);
                addPath(path, nX, dX);
            } else if (current.x() == 0 && next.y() == numeric.length - 1) {
                addPath(path, nX, dX);
                addPath(path, nY, dY);
            } else {
                boolean xFirst = index < s.length() && s.charAt(index) == '1';
                if (xFirst) {
                    addPath(path, nX, dX);
                    addPath(path, nY, dY);
                } else {
                    addPath(path, nY, dY);
                    addPath(path, nX, dX);
                }
            }
            path.add('A');

            current = next;
            index++;
        }
        return path;
    }

    private static List<Character> getNumericPath(String line) {
        Position current = numericPositions.get('A');
        List<Character> path = new ArrayList<>();
        for (char ch : line.toCharArray()) {
            Position next = numericPositions.get(ch);

            int nY = Math.abs(current.y() - next.y());
            char dY = current.y() < next.y() ? 'v' : '^';
            int nX = Math.abs(current.x() - next.x());
            char dX = current.x() < next.x() ? '>' : '<';
            if (current.y() == numeric.length - 1 && next.x() == 0) {
                addPath(path, nY, dY);
                addPath(path, nX, dX);
            } else if (current.x() == 0 && next.y() == numeric.length - 1) {
                addPath(path, nX, dX);
                addPath(path, nY, dY);
            } else {
                if (dY == '^') {
                    addPath(path, nX, dX);
                    addPath(path, nY, dY);
                } else {
                    addPath(path, nY, dY);
                    addPath(path, nX, dX);
                }
            }
            path.add('A');

            current = next;
        }
        return path;
    }

    private static List<Character> getShortestPath(String line, int dimensional) {
        List<Character> nextPath = getNumericPath(line);
        for (int i = 0; i < dimensional; i++) {
            nextPath = getDirectionalPath(nextPath);
        }
        return nextPath;
    }

    private static long getPart1(String inputFile) {
        return getPart1(inputFile, 2);
    }

    @SuppressWarnings("SameParameterValue")
    private static long getPart1(String inputFile, int dimensional) {
        List<String> lines = FileUtil.readLines(inputFile);
        long res = 0;
        for (String line : lines) {
            List<Character> path = getShortestPath(line, dimensional);

            int length = path.size();
            int code = Integer.parseInt(line.replaceAll("A", ""));

            res += (long) length * code;
        }
        return res;
    }

    private static void addCounts(List<Key> keyList, Map<Key, Long> counts, long multi) {
        for (Key key : keyList) {
            Long count = counts.computeIfAbsent(key, kk -> 0L);
            counts.put(key, count + multi);
        }
    }

    private static List<Character> withStartA(List<Character> path) {
        List<Character> res = new ArrayList<>(path.size() + 1);
        res.add('A');
        res.addAll(path);
        return res;
    }

    private static long getPart2(String inputFile, int dimensional) {
        List<String> lines = FileUtil.readLines(inputFile);
        long res = 0;
        for (String line : lines) {
            long lineRes = Long.MAX_VALUE;
            List<List<Character>> numericPaths = getNumericPaths(line);
            for (List<Character> pathWithoutA: numericPaths) {
                List<Character> path = withStartA(pathWithoutA);

                Map<Key, Long> counts = new LinkedHashMap<>();

                List<Key> keyList = toKeyList(path);
                addCounts(keyList, counts, 1);

                for (int j = 0; j < dimensional; j++) {
                    Map<Key, Long> nextCounts = new LinkedHashMap<>();
                    for (Map.Entry<Key, Long> e : counts.entrySet()) {
                        List<List<Character>> paths = getDirectionalPaths(e.getKey().start(), e.getKey().end());
                        List<Character> p = paths.get(0);
                        p = withStartA(p);
                        List<Key> pk = toKeyList(p);
                        addCounts(pk, nextCounts, e.getValue());
                    }
                    counts = nextCounts;
                }

                int code = Integer.parseInt(line.replaceAll("A", ""));
                long length = counts.values().stream().mapToLong(l -> l).sum();

                long pathRes = length * code;
                if (pathRes < lineRes) {
                    lineRes = pathRes;
                }
            }
            res += lineRes;
        }
        return res;
    }

    private static List<Key> toKeyList(List<Character> path) {
        Character current = path.get(0);
        List<Key> res = new ArrayList<>(path.size() - 1);
        for (int i = 1; i < path.size(); i++) {
            res.add(new Key(current, path.get(i)));
            current = path.get(i);
        }
        return res;
    }

    private static List<List<Character>> getDirectionalPaths(Character start, Character ch) {
        return List.of(getDirectionalPath2(start, ch));
    }

    private static List<Character> getDirectionalPath2(Character start, Character ch) {
        Position current = directionalPositions.get(start);
        Position next = directionalPositions.get(ch);

        int nY = Math.abs(current.y() - next.y());
        char dY = current.y() < next.y() ? 'v' : '^';
        int nX = Math.abs(current.x() - next.x());
        char dX = current.x() < next.x() ? '>' : '<';

        List<Character> res = new ArrayList<>();

        if (current.x() == 0 && next.y() == 0) {
            addPath(res, nX, dX);
            addPath(res, nY, dY);
        } else if (current.y() == 0 && next.x() == 0) {
            addPath(res, nY, dY);
            addPath(res, nX, dX);
        } else {
            if (nX == 0 || nY == 0) {
                // order does not matter
                addPath(res, nX, dX);
                addPath(res, nY, dY);
            } else {
                if (dX == '<') {
                    addPath(res, nX, dX);
                    addPath(res, nY, dY);
                } else {
                    addPath(res, nY, dY);
                    addPath(res, nX, dX);
                }
            }
        }

        res.add('A');

        return res;
    }

    private record Key(Character start, Character end) { }
}
