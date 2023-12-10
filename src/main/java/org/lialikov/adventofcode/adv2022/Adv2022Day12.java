package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;
import org.lialikov.adventofcode.util.Position;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static org.lialikov.adventofcode.util.FileUtil.toCharList;

public class Adv2022Day12 {

    public static void main(String[] args) {
        // 31
        System.out.println(getMinSteps("2022/2022-12-12-sample.txt"));
        // 449
        System.out.println(getMinSteps("2022/2022-12-12.txt"));
        // 29
        System.out.println(getMinASteps("2022/2022-12-12-sample.txt"));
        // 443
        System.out.println(getMinASteps("2022/2022-12-12.txt"));
    }

    private static long getMinSteps(String file) {
        Input input = Input.readFile(file);
        return calcSteps(input)[input.start.x][input.start.y];
    }

    private static long getMinASteps(String file) {
        Input input = Input.readFile(file);
        long[][] res = calcSteps(input);
        long min = res[input.start.x][input.start.y];
        for (int i = 0; i < input.map.size(); i++) {
            for (int j = 0; j < input.map.get(0).size(); j++) {
                if (input.map.get(i).get(j) == 'a' && (res[i][j] > 0 && res[i][j] < min)) {
                    min = res[i][j];
                }
            }
        }
        return min;
    }

    private static long[][] calcSteps(Input input) {
        int xSize = input.map.size();
        int ySize = input.map.get(0).size();

        long[][] res = new long[xSize][ySize];
        res[input.end.x][input.end.y] = 0;
        Deque<Position> positions = new LinkedList<>();
        positions.add(input.end);
        while (!positions.isEmpty()) {
            positions.addAll(calcAround(input.map, res, input.end, positions.removeFirst()));
        }

        return res;
    }

    private static long weight(List<List<Character>> map, Position p) {
        Character c = map.get(p.x).get(p.y);
        if (c == 'E') {
            c = 'z';
        } else if (c == 'S') {
            c = 'a';
        }
        return c - 'a';
    }

    private static List<Position> calcAround(List<List<Character>> map, long[][] res, Position end, Position position) {
        List<Position> r = new ArrayList<>();
        long positionWeight = weight(map, position);
        long positionSteps = res[position.x][position.y];

        Function<Position, Void> process = p -> {
            if (p.equals(end)) {
                return null;
            }
            long diff = positionWeight - weight(map, p);
            if (diff <= 1) {
                res[p.x][p.y] = positionSteps + 1;
                r.add(p);
            }
            return null;
        };

        int x = position.x - 1;
        int y = position.y;
        if (x >= 0 && res[x][y] == 0) {
            process.apply(new Position(x, y));
        }

        x = position.x + 1;
        if (x < map.size() && res[x][y] == 0) {
            process.apply(new Position(x, y));
        }

        x = position.x;
        y = position.y - 1;
        if (y >= 0 && res[x][y] == 0) {
            process.apply(new Position(x, y));
        }

        y = position.y + 1;
        if (y < map.get(0).size() && res[x][y] == 0) {
            process.apply(new Position(x, y));
        }

        return r;
    }

    private static class Input {
        Position start = null;
        Position end = null;
        List<List<Character>> map = new ArrayList<>();

        private static Input readFile(String file) {
            Input input = new Input();
            FileUtil.read(file, l -> input.map.add(toCharList(l)));

            for (int i = 0; i < input.map.size(); i++) {
                for (int j = 0; j < input.map.get(i).size(); j++) {
                    if (input.map.get(i).get(j) == 'S') {
                        input.start = new Position(i, j);
                    } else if (input.map.get(i).get(j) == 'E') {
                        input.end = new Position(i, j);
                    }
                }
            }

            Assert.notNull(input.end, "End cannot be null");
            Assert.notNull(input.start, "Start cannot be null");
            return input;
        }
    }
}
