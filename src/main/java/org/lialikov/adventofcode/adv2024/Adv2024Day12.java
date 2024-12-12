package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.model.Direction;
import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;

import static org.lialikov.adventofcode.model.Direction.E;
import static org.lialikov.adventofcode.model.Direction.S;

public class Adv2024Day12 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 140
        System.out.println(getPart1("2024/2024-12-12-sample1.txt"));
        // 772
        System.out.println(getPart1("2024/2024-12-12-sample2.txt"));
        // 1930
        System.out.println(getPart1("2024/2024-12-12-sample3.txt"));
        // 1488414
        System.out.println(getPart1("2024/2024-12-12.txt"));

        // 80
        System.out.println(getPart2("2024/2024-12-12-sample1.txt"));
        // 436
        System.out.println(getPart2("2024/2024-12-12-sample2.txt"));
        // 236
        System.out.println(getPart2("2024/2024-12-12-sample4.txt"));
        // 368
        System.out.println(getPart2("2024/2024-12-12-sample5.txt"));
        // 1206
        System.out.println(getPart2("2024/2024-12-12-sample3.txt"));
        // 911750
        System.out.println(getPart2("2024/2024-12-12.txt"));

        System.out.println(new Date());
    }

    private static boolean matches(char[][] chars, Position p, char plant) {
        if (p.x() < 0 || p.x() >= chars[0].length) {
            return false;
        }
        if (p.y() < 0 || p.y() >= chars.length) {
            return false;
        }
        return chars[p.y()][p.x()] == plant;
    }

    private static void process(char[][] chars, Region r, Collection<Position> toVisit, Position current, Position p) {
        if (matches(chars, p, r.plant)) {
            toVisit.add(p);
        } else {
            r.perimeter++;
            Direction d = current.x() == p.x() ? E : S;
            int coordFrom = d == E ? current.y() : current.x();
            r.addSide(d, p, coordFrom);
        }
    }

    private static Region parseRegion(char[][] chars, Position p) {
        Region r = new Region();
        LinkedList<Position> toVisit = new LinkedList<>();
        toVisit.add(p);
        r.plant = chars[p.y()][p.x()];
        Set<Position> visited = new HashSet<>();
        while (!toVisit.isEmpty()) {
            Position next = toVisit.poll();
            if (visited.contains(next)) {
                continue;
            }
            visited.add(next);

            r.positions.add(next);
            r.area++;

            process(chars, r, toVisit, next, new Position(next.x() - 1, next.y()));
            process(chars, r, toVisit, next, new Position(next.x() + 1, next.y()));
            process(chars, r, toVisit, next, new Position(next.x(), next.y() - 1));
            process(chars, r, toVisit, next, new Position(next.x(), next.y() + 1));
        }
        return r;
    }

    private static List<Region> parseRegions(char[][] chars) {
        List<Region> regions = new ArrayList<>();
        Set<Position> visited = new HashSet<>();
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < chars[i].length; j++) {
                Position p = new Position(j, i);
                if (visited.contains(p)) {
                    continue;
                }
                Region r = parseRegion(chars, p);
                regions.add(r);
                visited.addAll(r.positions);
            }
        }
        return regions;
    }

    private static long getPart1(String inputFile) {
        char[][] chars = FileUtil.toCharArrays(inputFile);
        List<Region> regions = parseRegions(chars);
        long res = 0;
        for (Region r : regions) {
            res += r.area * r.perimeter;
        }
        return res;
    }

    private static long getPart2(String inputFile) {
        char[][] chars = FileUtil.toCharArrays(inputFile);
        List<Region> regions = parseRegions(chars);
        long res = 0;
        for (Region r : regions) {
            long sides = new HashSet<>(r.sides.values()).size();
            res += r.area * sides;
        }
        return res;
    }

    public static class Region {
        char plant;
        long area;
        long perimeter;
        Set<Position> positions = new HashSet<>();
        Map<SideKey, Side> sides = new HashMap<>();

        void addSide(Direction d, Position p, int coordFrom) {
            SideKey key = new SideKey(d, coordFrom, p);
            if (sides.containsKey(key)) {
                return;
            }
            List<Position> toTry = d == E ?
                    List.of(new Position(p.x() - 1, p.y()), new Position(p.x() + 1, p.y())) :
                    List.of(new Position(p.x(), p.y() - 1), new Position(p.x(), p.y() + 1));

            Side side = sides.get(new SideKey(d, coordFrom, toTry.get(0)));
            if (side == null) {
                side = sides.get(new SideKey(d, coordFrom, toTry.get(1)));
            }

            if (side == null) {
                side = sides.computeIfAbsent(key, k -> new Side(d, coordFrom));
            }
            side.positions.add(p);
            sides.put(key, side);
        }
    }

    public record SideKey(Direction d, int coordFrom, Position p) { }

    public static class Side {
        Direction d;
        int coordFrom;
        Set<Position> positions = new HashSet<>();

        public Side(Direction d, int coordFrom) {
            this.d = d;
            this.coordFrom = coordFrom;
        }
    }
}
