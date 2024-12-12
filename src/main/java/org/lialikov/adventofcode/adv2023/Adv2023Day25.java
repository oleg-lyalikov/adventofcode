package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;

import java.util.*;

public class Adv2023Day25 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 54
        System.out.println(getPart1("2023/2023-12-25-sample.txt"));
        // 552695
        System.out.println(getPart1("2023/2023-12-25.txt"));

        System.out.println(new Date());
    }

    private static boolean containsAny(Set<Vertex> s, Set<Vertex> toCheck) {
        for (Vertex v : toCheck) {
            if (s.contains(v)) {
                return true;
            }
        }
        return false;
    }

    private static long getPart1(String inputFile) {
        Graph graph = readGraph(inputFile);
        for (int i = 0; i < 1000; i++) {
            MinCut minCut = getKargerMinCut(graph);
            if (minCut.minCut == 3) {
                return (long) minCut.s1.size() * minCut.s2.size();
            }
        }
        return -1;
    }

    private static List<Edge> getEdgesByVertex(List<Edge> edges, Vertex v) {
        return edges.stream().filter(e -> e.v1.equals(v) || e.v2.equals(v)).toList();
    }

    private static MinCut getKargerMinCut(Graph graph) {
        Random r = new Random();
        List<Edge> edges = new ArrayList<>(graph.edges);

        List<Pair<Vertex, Vertex>> merged = new ArrayList<>();
        for (int i = 0; i < graph.vertices.size() - 2; i++) {
            int edgeIndex = r.nextInt(edges.size());
            Edge edge = edges.get(edgeIndex);
            Vertex toLeave = edge.v1;
            Vertex toRemove = edge.v2;
            merged.add(Pair.of(toLeave, toRemove));

            List<Edge> originalEdges = getEdgesByVertex(edges, toRemove);
            List<Edge> replacedEdges = originalEdges.stream()
                    .map(e -> {
                        final Vertex v;
                        if (e.v1.equals(toRemove)) {
                            v = e.v2;
                        } else if (e.v2.equals(toRemove)) {
                            v = e.v1;
                        } else {
                            throw new IllegalStateException("!!!");
                        }
                        return new Edge(toLeave, v);
                    }).filter(e -> !e.v1.equals(e.v2))
                    .toList();

            edges.removeAll(originalEdges);
            edges.addAll(replacedEdges);
        }

        Set<Vertex> s1 = new HashSet<>();
        s1.add(merged.get(0).getFirst());
        for (int i = 0; i < graph.vertices.size(); i++) {
            for (Pair<Vertex, Vertex> p : merged) {
                if (s1.contains(p.getFirst())) {
                    s1.add(p.getSecond());
                } else if (s1.contains(p.getSecond())) {
                    s1.add(p.getFirst());
                }
            }
        }

        Set<Vertex> s2 = new HashSet<>();
        for (Vertex v : graph.vertices) {
            if (!s1.contains(v)) {
                s2.add(v);
            }
        }
        return new MinCut(s1, s2, edges.size());
    }

    private static Graph readGraph(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        Set<Vertex> vertexSet = new HashSet<>();
        List<Edge> edges = new ArrayList<>();
        Map<Vertex, List<Edge>> edgesByVertex = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length != 2) {
                throw new IllegalStateException("!!!");
            }
            Vertex v1 = new Vertex(parts[0]);
            vertexSet.add(v1);
            List<Vertex> vertices = Arrays.stream(parts[1].trim().split("\\s+"))
                    .map(Vertex::new)
                    .toList();
            List<Edge> v1Edges = edgesByVertex.computeIfAbsent(v1, vv1 -> new ArrayList<>());
            for (Vertex v2 : vertices) {
                vertexSet.add(v2);
                Edge e = new Edge(v1, v2);
                v1Edges.add(e);
                edges.add(e);
                edgesByVertex.computeIfAbsent(v2, vv2 -> new ArrayList<>())
                        .add(e);
            }
        }
        return new Graph(new ArrayList<>(vertexSet), edges, edgesByVertex);
    }

    private record Graph(
            List<Vertex> vertices,
            List<Edge> edges,
            Map<Vertex, List<Edge>> edgesByVertex
    ) {
    }

    private record Vertex(
            String name
    ) {
    }

    private record Edge(
            Vertex v1,
            Vertex v2
    ) {
        public Vertex getOther(Vertex v) {
            if (v1.equals(v)) {
                return v2;
            } else if (v2.equals(v)) {
                return v1;
            }
            throw new IllegalStateException("Vertex " + v + " does not belong to edge " + this);
        }
    }

    private record MinCut(
        Set<Vertex> s1,
        Set<Vertex> s2,
        int minCut
    ) {
    }
}
