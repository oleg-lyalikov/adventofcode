package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.model.Algo;
import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Adv2023Day23 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 94
        //System.out.println(getPart1("2023/2023-12-23-sample.txt", Algo.PART1));
        // 2030
        //System.out.println(getPart1("2023/2023-12-23.txt", Algo.PART1));

        // 154
        //System.out.println(getPart1("2023/2023-12-23-sample.txt", Algo.PART2));
        // 154
        //System.out.println(getPart2("2023/2023-12-23-sample.txt"));
        // 5918 That's not the right answer.
        //System.out.println(getPart2ShortestNegative("2023/2023-12-23-sample.txt"));

        // 154
        //System.out.println(getPart2ReducedGraph("2023/2023-12-23-sample.txt", 154));
        // 5710 That's not the right answer.
        // 6334 That's not the right answer. (then it should be greater than 6334)
        // 6390 !
        System.out.println(getPart2ReducedGraph("2023/2023-12-23.txt", 6300));

        // 154
        //System.out.println(getPart2ReducedGraphFromEnd("2023/2023-12-23-sample.txt", 154));
        // 6670. That's not the right answer. Curiously, it's the right answer for someone else;
        // 7042 That's not the right answer.
        // 6814 That's not the right answer.
        // 5670 That's not the right answer.
        //System.out.println(getPart2ReducedGraphFromEnd("2023/2023-12-23.txt", 6300));

        // 154
        //System.out.println(getPart2ReducedGraphShortestNegative("2023/2023-12-23-sample.txt"));
        // 4790 That's not the right answer.
        //System.out.println(getPart2ReducedGraphShortestNegative("2023/2023-12-23.txt"));

        // 154
        //System.out.println(getPart2ReducedGraphShortestNegativeFromEnd("2023/2023-12-23-sample.txt"));
        // 6526 That's not the right answer. Curiously, it's the right answer for someone else;
        // 6358 That's not the right answer.
        // 5550 That's not the right answer.
        //System.out.println(getPart2ReducedGraphShortestNegativeFromEnd("2023/2023-12-23.txt"));

        //System.out.println(getPart2ReducedGraph("2023/2023-12-23.txt"));
        //System.out.println(getPart2V2("2023/2023-12-23.txt"));
        // 4306 That's not the right answer; your answer is too low.
        // 4307 That's not the right answer; your answer is too low.
        // 5150 That's not the right answer; your answer is too low.
        // 5890 That's not the right answer.
        // 6670. That's not the right answer. Curiously, it's the right answer for someone else;
        // 7042 That's not the right answer.
        //System.out.println(getPart1("2023/2023-12-23.txt", Algo.PART2));

        System.out.println(new Date());
    }

    private static final Position NO_POSITION = new Position(-10, -10);

    private static int getSingleDotX(char[][] map, int i) {
        int res = - 1;
        for (int j = 0; j < map[i].length; j++) {
            if (map[i][j] == '.') {
                if (res != -1) {
                    throw new IllegalStateException("!!!");
                }
                res = j;
            }
        }
        return res;
    }

    private static int getVerticesCount(char[][] map) {
        int res = 0;
        for (char[] chars : map) {
            for (int j = 0; j < map[0].length; j++) {
                if (chars[j] != '#') {
                    res++;
                }
            }
        }
        return res;
    }

    private static Set<Set<Position>> getEdges(char[][] map, Position start) {
        Set<Set<Position>> edges = new HashSet<>();
        Set<Position> toVisit = new HashSet<>();
        toVisit.add(start);
        Set<Position> visited = new HashSet<>();
        while (!toVisit.isEmpty()) {
            Iterator<Position> i = toVisit.iterator();
            Position next = i.next();
            i.remove();
            if (visited.contains(next)) {
                continue;
            }
            visited.add(next);

            Set<Set<Position>> nextEdges = getNext(map, next, null, Algo.PART2).stream()
                    .map(p -> Set.of(next, p))
                    .collect(Collectors.toSet());
            edges.addAll(nextEdges);
            nextEdges.forEach(toVisit::addAll);
        }
        return edges;
    }

    private static long getPart2ShortestNegative(String inputFile) {
        char[][] map = FileUtil.toCharArrays(inputFile);
        Position start = new Position(getSingleDotX(map, 0), 0);
        Position end = new Position(getSingleDotX(map, map.length - 1), map.length - 1);
        int vertices = getVerticesCount(map);
        Set<Set<Position>> edges = getEdges(map, start);

        Map<Position, Set<Position>> visited = new HashMap<>();
        Map<Position, Integer> iteration = new HashMap<>();
        Map<Position, Integer> distance = new HashMap<>();
        Map<Position, Position> pred = new HashMap<>();
        distance.put(start, 0);
        visited.put(start, Set.of(start));
        iteration.put(start, -1);
        for (int i = 0; i < vertices - 1; i++) {
            for (Set<Position> edge : edges) {
                Iterator<Position> it = edge.iterator();
                Position p1 = it.next();
                Position p2 = it.next();
                Integer d1 = distance.get(p1);
                Integer d2 = distance.get(p2);
                if (d1 == null && d2 == null) {
                    continue;
                }
                if (d1 != null) {
                    if (visited.get(p1).contains(p2)) {
                        //continue;
                    }
                    if (iteration.get(p1) == i) {
                        continue;
                    }
                    if (d2 == null || (d2 > (d1 - 1) && Math.abs(d1 - d2) != 1)) {
                        distance.put(p2, d1 - 1);
                        pred.put(p2, p1);
                        Set<Position> vvv = new HashSet<>(visited.get(p1));
                        vvv.add(p2);
                        visited.put(p2, vvv);
                        iteration.put(p2, i);
                    }
                }
                if (d2 != null) {
                    if (visited.get(p2).contains(p1)) {
                        //continue;
                    }
                    if (iteration.get(p2) == i) {
                        continue;
                    }
                    if (d1 == null || (d1 > (d2 - 1) && Math.abs(d1 - d2) != 1)) {
                        distance.put(p1, d2 - 1);
                        pred.put(p1, p2);
                        Set<Position> vvv = new HashSet<>(visited.get(p2));
                        vvv.add(p1);
                        visited.put(p1, vvv);
                        iteration.put(p1, i);
                    }
                }
            }
        }

        return distance.get(end);
    }

    private static long getPath(Graph graph, Set<Vertex2> vertexes) {
        if (vertexes == null) {
            return 0;
        }
        Set<Vertex2> resSet = new HashSet<>();
        for (Vertex2 v : vertexes) {
            if (!resSet.contains(graph.toOpposite.get(v))) {
                resSet.add(v);
            }
        }
        long vertexSum = resSet.stream().mapToLong(v -> v.count).sum();
        return vertexSum + resSet.size() - 1;
    }

    private static long getPart2ReducedGraphShortestNegativeFromEnd(String inputFile) {
        Graph graph = readGraph(inputFile);
        return getPart2ReducedGraphShortestNegative(graph, graph.toOpposite.get(graph.end), graph.toOpposite.get(graph.start));
    }

    private static long getPart2ReducedGraphShortestNegative(Graph graph, Vertex2 start, Vertex2 end) {
        int vertices = graph.vertexes.size() / 2;

        System.out.println("Vertexes count: " + vertices);

        Map<Vertex2, Integer> iteration = new HashMap<>();
        Map<Vertex2, Integer> distance = new HashMap<>();
        Map<Vertex2, Pair<Set<Vertex2>, Set<Position>>> visited = new HashMap<>();
        visited.put(start, Pair.of(Set.of(start), new HashSet<>()));
        distance.put(start, -start.count + 1);
        iteration.put(start, -1);
        for (int i = 0; i < vertices - 1; i++) {
            for (Edge edge : graph.edges) {
                Integer d1 = distance.get(edge.v1);
                Integer d2 = distance.get(edge.v2);
                if (d1 == null) {
                    continue;
                }
                if (iteration.get(edge.v1) == i) {
                    continue;
                }
                int v2Weight = d1 - edge.v2.count - 1;
                if (d2 == null || d2 > v2Weight) {
                    Pair<Set<Vertex2>, Set<Position>> currentVisited = visited.get(edge.v1);
                    Set<Vertex2> vvv = new HashSet<>(currentVisited.getFirst());
//                    if (vvv.contains(edge.v2) || vvv.contains(graph.toOpposite.get(edge.v2))) {
//                        continue;
//                    }
                    Set<Position> currentVisitedJoints = new HashSet<>(currentVisited.getSecond());
                    if (currentVisitedJoints.contains(edge.joint)) {
                        continue;
                    }

                    distance.put(edge.v2, v2Weight);
                    iteration.put(edge.v2, i);
                    if (edge.v2.equals(end)) {
                        System.out.println("New end: " + distance.get(edge.v2) + ", iteration: " + i);
                    }
                    vvv.add(edge.v2);
                    currentVisitedJoints.add(edge.joint);
                    visited.put(edge.v2, Pair.of(vvv, currentVisitedJoints));
                }
            }
        }

        print(graph, visited.get(end).getFirst());
        return -distance.get(end);
    }

    private static void print(Graph graph, Set<Vertex2> visited) {
        int maxI = graph.originalMap.length;
        int maxJ = graph.originalMap[0].length;
        Map<Position, Vertex2> vertexByPosition = graph.getVertexByPositionMap();
        Set<Position> joints = graph.edges.stream().map(e -> e.joint).collect(Collectors.toSet());
        for (int i = 0; i < maxI; i++) {
            for (int j = 0; j < maxJ; j++) {
                if (graph.originalMap[i][j] == '#') {
                    System.out.print('#');
                } else {
                    Position p = new Position(j, i);
                    if (joints.contains(p)) {
                        System.out.print('+');
                    } else {
                        Vertex2 v = vertexByPosition.get(p);
                        if (v == null) {
                            throw new IllegalStateException("!!!");
                        }
                        if (visited.contains(v) || visited.contains(graph.toOpposite.get(v))) {
                            System.out.print('O');
                        } else {
                            System.out.print('.');
                        }
                    }
                }
            }
            System.out.println();
        }
    }

    private static long getPart2ReducedGraphShortestNegative(String inputFile) {
        Graph graph = readGraph(inputFile);
        return getPart2ReducedGraphShortestNegative(graph, graph.start, graph.end);
    }

    private static long getPart2ReducedGraphFromEnd(String inputFile, int printIfGreaterThan) {
        Graph graph = readGraph(inputFile);
        long res = 0;
        for (int i = 0; i < 10000; i++) {
            long current = getPart2ReducedGraph(graph, graph.toOpposite.get(graph.end), graph.toOpposite.get(graph.start), printIfGreaterThan);
            if (current > res) {
                System.out.println("New res found: " + current + ", i = " + i);
                res = current;
            }
        }
        return res;
    }

    private static long getPart2ReducedGraph(String inputFile, int printIfGreaterThan) {
        Graph graph = readGraph(inputFile);
        long res = 0;
        for (int i = 0; i < 10000; i++) {
            long current = getPart2ReducedGraph(graph, graph.start, graph.end, printIfGreaterThan);
            if (current > res) {
                System.out.println("New res found: " + current + ", i = " + i);
                res = current;
            }
        }
        // 6334
        return res;
    }

    private static long getPart2ReducedGraph(Graph graph, Vertex2 start, Vertex2 end, int printIfGreaterThan) {
        List<VisitItem> toVisit = new LinkedList<>();
        toVisit.add(new VisitItem(start, new HashSet<>(), new HashSet<>()));

        Map<Vertex2, List<Edge>> nextEdges = graph.vertexes.stream().collect(Collectors.toMap(
                v -> v,
                v -> graph.edges.stream().filter(e -> e.v1.equals(v)).collect(Collectors.toList())
        ));
        Map<Vertex2, List<Vertex2>> nextVertexes = graph.vertexes.stream().collect(Collectors.toMap(
                v -> v,
                v -> graph.edges.stream().filter(e -> e.v1.equals(v)).map(e -> e.v2).collect(Collectors.toList())
        ));
        Map<Edge, Integer> byEdge = new HashMap<>();

        long iterations = 0;
        long res = 0;
        Set<Vertex2> resSet = new HashSet<>();
        while (!toVisit.isEmpty()) {
            Iterator<VisitItem> i = toVisit.iterator();
            VisitItem nextVisitItem = i.next();
            i.remove();
            Vertex2 next = nextVisitItem.v;
            Vertex2 nextOpposite = graph.toOpposite.get(next);
            Set<Vertex2> nextVisited = nextVisitItem.visited;
            if (nextVisited.contains(next) || nextVisited.contains(nextOpposite)) {
                continue;
            }
            nextVisited.add(next);
            nextVisited.add(nextOpposite);

            if (next.equals(end)) {
                Set<Vertex2> resSetTest = new HashSet<>();
                for (Vertex2 v : nextVisited) {
                    if (!resSetTest.contains(graph.toOpposite.get(v))) {
                        resSetTest.add(v);
                    }
                }
                long path = getPath(resSetTest);
                if (path > res) {
                    res = path;
                    resSet = resSetTest;
                    //System.out.println("New res found: " + res);
                }
                continue;
            }

            iterations++;
            if (iterations % 10000 == 0) {
                System.out.println("Iteration: " + iterations + ", to visit: " + toVisit.size() + ", date: " + new Date());
            }

            List<Edge> edges = nextEdges.get(next);
            for (Edge edge : edges) {
                Vertex2 v2 = edge.v2;
                if (nextVisited.contains(v2) || nextVisited.contains(graph.toOpposite.get(v2))) {
                    continue;
                }

                int path = getPath(nextVisited, v2);
                Integer currentPath = byEdge.get(edge);
                if (currentPath != null && path <= currentPath) {
                    continue;
                }

                if (nextVisitItem.visitedJoints.contains(edge.joint)) {
                    continue;
                }

                Set<Position> nextVisitedJoints = new HashSet<>(nextVisitItem.visitedJoints);
                nextVisitedJoints.add(edge.joint);

                byEdge.put(edge, path);

                toVisit.add(new VisitItem(v2, new HashSet<>(nextVisited), nextVisitedJoints));
                Collections.shuffle(toVisit);
            }
        }

        if (res - 1 > printIfGreaterThan) {
            print(graph, resSet);
        }
        // start vertex should have 1 less step
        return res - 1;
    }

    private static int getPath(Set<Vertex2> vertices) {
        if (vertices.isEmpty()) {
            return 0;
        }
        return vertices.stream().mapToInt(v -> v.count).sum() + vertices.size() - 1;
    }

    private static int getPath(Set<Vertex2> vertices, Vertex2 add) {
        return getPath(vertices) + add.count + 1;
    }

    private static Graph readGraph(String inputFile) {
        char[][] map = FileUtil.toCharArrays(inputFile);
        System.out.println("Total steps: " + getVerticesCount(map));
        Position start = new Position(getSingleDotX(map, 0), 0);
        Position end = new Position(getSingleDotX(map, map.length - 1), map.length - 1);

        Deque<Position> toVisit = new LinkedList<>();
        toVisit.add(start);
        Set<Position> visited = new HashSet<>();
        List<Vertex2> vertexes = new ArrayList<>();
        List<Pair<Set<Position>, Position>> edges = new ArrayList<>();
        Map<Position, Vertex2> vertexMapByStart = new HashMap<>();
        Map<Position, Vertex2> vertexMapByEnd = new HashMap<>();
        Map<Vertex2, Set<Position>> vertexPositions = new HashMap<>();
        while (!toVisit.isEmpty()) {
            Position p = toVisit.poll();
            if (visited.contains(p)) {
                continue;
            }
            visited.add(p);
            if (vertexMapByStart.get(p) != null) {
                continue;
            }

            int count = 0;
            Set<Position> next = Set.of(p);
            Position prev = p;
            Position prevPrev = null;
            Set<Position> content = new HashSet<>(Set.of(p));
            while (true) {
                Position currentPosition = next.iterator().next();
                Set<Position> nextOriginal = new HashSet<>(getNext(map, currentPosition, null, Algo.PART2));
                next = new HashSet<>(nextOriginal);
                next.removeAll(visited);
                if (next.isEmpty()) {
                    content.add(currentPosition);
                    Vertex2 toAdd1 = new Vertex2(p, prev, count + 1, start.equals(p), end.equals(prev));
                    Vertex2 toAdd2 = new Vertex2(prev, p, count + 1, start.equals(p), end.equals(prev));
                    vertexes.add(toAdd1);
                    vertexes.add(toAdd2);
                    vertexMapByStart.put(toAdd1.start, toAdd1);
                    vertexMapByStart.put(toAdd2.start, toAdd2);
                    vertexMapByEnd.put(toAdd1.end, toAdd1);
                    vertexMapByEnd.put(toAdd2.end, toAdd2);
                    vertexPositions.put(toAdd1, content);
                    vertexPositions.put(toAdd2, content);

                    nextOriginal.remove(prevPrev);
                    if (nextOriginal.isEmpty() && end.equals(prev)) {
                        break;
                    } else if (nextOriginal.size() != 1) {
                        throw new IllegalStateException("!!!");
                    }
                    Position joint = nextOriginal.iterator().next();
                    next = new HashSet<>(getNext(map, joint, null, Algo.PART2));
                    //next.remove(prev);
                    //next.forEach(pos -> edges.add(Set.of(p, pos)));
                    List<Position> nextList = new ArrayList<>(next);
                    for (int i = 0; i < nextList.size(); i++) {
                        for (int j = i + 1; j < nextList.size(); j++) {
                            edges.add(Pair.of(
                                    Set.of(nextList.get(i), nextList.get(j)),
                                    joint
                            ));
                        }
                    }
                    break;
                }
                Position nextPosition = next.iterator().next();
                if (nextOriginal.size() <= 2) {
                    count++;
                    prevPrev = prev;
                    prev = nextPosition;
                    visited.add(prev);
                    content.add(currentPosition);
                } else {
                    Vertex2 toAdd1 = new Vertex2(p, prevPrev, count, start.equals(p), end.equals(prevPrev));
                    Vertex2 toAdd2 = new Vertex2(prevPrev, p, count, start.equals(p), end.equals(prevPrev));
                    vertexes.add(toAdd1);
                    vertexes.add(toAdd2);
                    vertexMapByStart.put(toAdd1.start, toAdd1);
                    vertexMapByStart.put(toAdd2.start, toAdd2);
                    vertexMapByEnd.put(toAdd1.end, toAdd1);
                    vertexMapByEnd.put(toAdd2.end, toAdd2);
                    vertexPositions.put(toAdd1, content);
                    vertexPositions.put(toAdd2, content);

                    List<Position> nextList = new ArrayList<>(nextOriginal);
                    for (int i = 0; i < nextList.size(); i++) {
                        for (int j = i + 1; j < nextList.size(); j++) {
                            edges.add(Pair.of(
                                    Set.of(nextList.get(i), nextList.get(j)),
                                    currentPosition
                            ));
                        }
                    }

                    nextOriginal.remove(prevPrev);
                    toVisit.addAll(nextOriginal);
                    break;
                }
            }
        }

        List<Edge> edges2 = edges.stream().distinct().flatMap(e -> {
            Iterator<Position> i = e.getFirst().iterator();
            Position p1 = i.next();
            Position p2 = i.next();
            Vertex2 v1 = vertexMapByEnd.get(p1);
            Vertex2 v2 = vertexMapByStart.get(p2);
            return Stream.of(
                    new Edge(vertexMapByEnd.get(p1), vertexMapByStart.get(p2), e.getSecond(), 1 + v2.count),
                    new Edge(vertexMapByEnd.get(p2), vertexMapByStart.get(p1), e.getSecond(), 1 + v1.count)
            );
        }).distinct().toList();

        Map<Vertex2, Vertex2> toOpposite = new HashMap<>();
        vertexes.forEach(v -> toOpposite.put(v, vertexMapByStart.get(v.end)));

        System.out.println("Vertexes count: " + vertexes.size() / 2);
        return new Graph(edges2, vertexes, toOpposite, vertexMapByStart.get(start), vertexMapByEnd.get(end), vertexPositions, map);
    }

    private static long getPart2(String inputFile) {
        char[][] map = FileUtil.toCharArrays(inputFile);
        Position start = new Position(getSingleDotX(map, 0), 0);
        Position end = new Position(getSingleDotX(map, map.length - 1), map.length - 1);

        List<Pair<Position, Set<Position>>> toVisit = new LinkedList<>();
        toVisit.add(Pair.of(start, new HashSet<>()));

        long iterations = 0;
        long res = 0;
        while (!toVisit.isEmpty()) {
            Iterator<Pair<Position, Set<Position>>> i = toVisit.iterator();
            Pair<Position, Set<Position>> nextPair = i.next();
            i.remove();
            Position next = nextPair.getFirst();
            Set<Position> nextVisited = nextPair.getSecond();
            if (nextVisited.contains(next)) {
                continue;
            }
            nextVisited.add(next);
            if (next.equals(end)) {
                if ((nextVisited.size() - 1) > res) {
                    res = nextVisited.size() - 1;
                    System.out.println("New res found: " + res);
                }
                continue;
            }

            iterations++;
            if (iterations % 10000 == 0) {
                System.out.println("Iteration: " + iterations + ", to visit: " + toVisit.size() + ", date: " + new Date());
            }

            List<Position> positions = getNext(map, next, null, Algo.PART2);
            for (Position position : positions) {
                if (nextVisited.contains(position)) {
                    continue;
                }
                toVisit.add(Pair.of(position, new HashSet<>(nextVisited)));
            }
        }

        return res;
    }

    private static long getPart1(String inputFile, Algo algo) {
        char[][] map = FileUtil.toCharArrays(inputFile);
        Position start = new Position(getSingleDotX(map, 0), 0);
        Position end = new Position(getSingleDotX(map, map.length - 1), map.length - 1);

        Map<Pair<Position, Position>, Integer> paths = new HashMap<>();
        Set<Vertex> visited = new HashSet<>();
        List<Pair<Vertex, Set<Position>>> toVisit = new LinkedList<>();
        Vertex endVertex = new Vertex(end, NO_POSITION, 1);
        Pair<Position, Position> endPair = Pair.of(endVertex.p, endVertex.from);
        paths.put(endPair, endVertex.path);

        Set<Position> set = new HashSet<>();
        set.add(endVertex.p);
        set.add(endVertex.from);
        toVisit.add(Pair.of(endVertex, set));

        long iterations = 0;
        while (!toVisit.isEmpty()) {
            Iterator<Pair<Vertex, Set<Position>>> i = toVisit.iterator();
            Pair<Vertex, Set<Position>> nextPair = i.next();
            i.remove();
            Vertex next = nextPair.getFirst();
            if (visited.contains(next)) {
                continue;
            }
            visited.add(next);
            iterations++;
            if (iterations % 10000 == 0) {
                System.out.println("Iteration: " + iterations + ", to visit: " + toVisit.size());
            }

            List<Pair<Position, Position>> nextPositions = getNext(map, next.p, next.from, algo)
                    .stream().map(p -> Pair.of(p, next.p))
                    .toList();
            //int path = next.path + 1;
            Set<Position> currentSet = nextPair.getSecond();
            int newPath = currentSet.size();
            nextPositions.forEach(pair -> {
                Integer existing = paths.get(pair);
                Vertex v = new Vertex(pair.getFirst(), pair.getSecond(), newPath);
                if (existing == null || (existing < newPath && !currentSet.contains(v.p))) {
                    paths.put(pair, newPath);
                    Set<Position> nextSet = new HashSet<>(currentSet);
                    nextSet.add(v.p);
                    toVisit.add(Pair.of(v, nextSet));
                }
            });
        }

        return getMax(paths, start);
    }

    private static long getMax(Map<Pair<Position, Position>, Integer> paths, Position p) {
        long res = 0;
        for (Map.Entry<Pair<Position, Position>, Integer> e : paths.entrySet()) {
            if (e.getKey().getFirst().equals(p)) {
                if (e.getValue() > res) {
                    res = e.getValue();
                }
            }
        }
        return res - 1;
    }

    private static List<Position> getNext(char[][] map, Position p, Position pFrom, Algo algo) {
        return Stream.of(
                new Position(p.x(), p.y() + 1),
                new Position(p.x(), p.y() - 1),
                new Position(p.x() + 1, p.y()),
                new Position(p.x() - 1, p.y())
        ).filter(pos -> {
            if (pos.x() < 0 || pos.x() >= map[0].length || pos.y() < 0 || pos.y() >= map.length) {
                return false;
            }
            char ch = map[pos.y()][pos.x()];
            if (ch == '#') {
                return false;
            }
            if (algo == Algo.PART1) {
                if (ch == '>' && pos.x() > p.x()) {
                    return false;
                }
                if (ch == '<' && pos.x() < p.x()) {
                    return false;
                }
                if (ch == 'v' && pos.y() > p.y()) {
                    return false;
                }
                if (ch == '^' && pos.y() < p.y()) {
                    return false;
                }
            }
            return !pos.equals(pFrom);
        }).collect(Collectors.toList());
    }

    private record Vertex(
            Position p,
            Position from,
            Integer path
    ) {
    }

    private record Vertex2(
            Position start,
            Position end,
            int count,
            boolean isStart,
            boolean isEnd
    ) {
    }

    private record Edge(
        Vertex2 v1,
        Vertex2 v2,
        Position joint,
        int weight
    ) {
    }

    private record Graph(
            List<Edge> edges,
            List<Vertex2> vertexes,
            Map<Vertex2, Vertex2> toOpposite,
            Vertex2 start,
            Vertex2 end,
            Map<Vertex2, Set<Position>> vertexPositions,
            char[][] originalMap
    ) {
        public Map<Position, Vertex2> getVertexByPositionMap() {
            Map<Position, Vertex2> res = new HashMap<>();
            vertexPositions.forEach((key, value) -> value.forEach(p -> res.put(p, key)));
            return res;
        }
    }

    private record VisitItem(
        Vertex2 v,
        Set<Vertex2> visited,
        Set<Position> visitedJoints
    ) {
    }
}
