package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;

public class Adv2024Day23 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 7
        System.out.println(getPart1("2024/2024-12-23-sample.txt"));
        // 1154
        System.out.println(getPart1("2024/2024-12-23.txt"));

        // co,de,ka,ta
        System.out.println(getPart2("2024/2024-12-23-sample.txt"));
        // aj,ds,gg,id,im,jx,kq,nj,ql,qr,ua,yh,zn
        System.out.println(getPart2("2024/2024-12-23.txt"));

        System.out.println(new Date());
    }

    private static List<List<String>> read(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        List<List<String>> pairs = new ArrayList<>();
        for (String line : lines) {
            String first = line.substring(0, 2);
            String second = line.substring(3, 5);
            pairs.add(List.of(first, second));
        }
        return pairs;
    }

    private static long getPart1(String inputFile) {
        List<List<String>> pairs = read(inputFile);
        List<List<String>> tPairs = new ArrayList<>();
        for (List<String> pair : pairs) {
            if (pair.get(0).charAt(0) == 't' || pair.get(1).charAt(0) == 't') {
                tPairs.add(pair);
            }
        }
        Set<Set<String>> res = new HashSet<>();
        for (List<String> pair : tPairs) {
            res.addAll(find(pair, pairs));
        }
        return res.size();
    }

    private static Set<Set<String>> find(List<String> exclude, List<List<String>> allPairs) {
        Set<Set<String>> res = new HashSet<>();
        Set<String> excludeSet = new HashSet<>(exclude);
        String connection1 = exclude.get(0);
        String connection2 = exclude.get(1);
        for (List<String> p: allPairs) {
            String p0 = p.get(0);
            String p1 = p.get(1);
            boolean p0Fits = p0.equals(connection1);
            boolean p1Fits = p1.equals(connection1);
            Set<String> set = new HashSet<>(p);
            if (!set.equals(excludeSet) && (p0Fits || p1Fits)) {
                String connection3 = p0Fits ? p1 : p0;
                Set<String> toCheck = Set.of(connection2, connection3);
                for (List<String> p2: allPairs) {
                    if (toCheck.equals(new HashSet<>(p2))) {
                        res.add(Set.of(connection1, connection2, connection3));
                    }
                }
            }
        }
        return res;
    }

    private static String getPart2(String inputFile) {
        List<List<String>> pairs = read(inputFile);

        Set<String> vertices = new HashSet<>();
        Map<String, Set<String>> edges = new HashMap<>();
        for (List<String> pair : pairs) {
            String first = pair.get(0);
            String second = pair.get(1);
            vertices.add(first);
            vertices.add(second);
            edges.computeIfAbsent(first, v -> new HashSet<>()).add(second);
            edges.computeIfAbsent(second, v -> new HashSet<>()).add(first);
        }

        long max = 0;
        Set<String> maxSet = new HashSet<>();
        int i = 0;
        for (String vertex: vertices) {
            Set<Set<String>> sets = getMaxConnected(vertex, edges);
            for (Set<String> set : sets) {
                List<List<String>> res = new ArrayList<>();
                for (List<String> pair : pairs) {
                    if (set.contains(pair.get(0)) && set.contains(pair.get(1))) {
                        res.add(pair);
                    }
                }
                if (res.size() > max) {
                    max = res.size();
                    maxSet = set;
                }
            }
            System.out.println("Processed " + ++i + " from " + vertices.size());
        }

        List<String> res = new ArrayList<>(maxSet);
        Collections.sort(res);
        return String.join(",", res);
    }

    private static Set<Set<String>> getMaxConnected(String vertex, Map<String, Set<String>> edges) {
        Set<Set<String>> res = new HashSet<>();

        Set<Set<String>> toVisit = new HashSet<>();
        toVisit.add(Set.of(vertex));
        Set<Set<String>> visited = new HashSet<>();

        long max = 1;
        while (!toVisit.isEmpty()) {
            Set<String> current = toVisit.iterator().next();
            toVisit.remove(current);
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            for (String c : current) {
                for (String v : edges.get(c)) {
                    if (current.contains(v)) {
                        continue;
                    }
                    Set<String> vEdges = edges.get(v);
                    if (vEdges.containsAll(current)) {
                        Set<String> add = new HashSet<>(current);
                        add.add(v);
                        toVisit.add(add);
                        if (add.size() > max) {
                            res = new HashSet<>();
                            max = add.size();
                        }
                        if (add.size() == max) {
                            res.add(add);
                        }
                    }
                }
            }
        }

        return res;
    }
}
