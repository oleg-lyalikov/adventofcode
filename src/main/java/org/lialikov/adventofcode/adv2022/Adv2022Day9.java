package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;
import org.lialikov.adventofcode.util.Position;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Adv2022Day9 {

    public static void main(String[] args) {
        System.out.println(getDistinctTailPositions("2022/2022-12-09-sample-1.txt", 1));
        System.out.println(getDistinctTailPositions("2022/2022-12-09.txt", 1));
        System.out.println(getDistinctTailPositions("2022/2022-12-09-sample-1.txt", 9));
        System.out.println(getDistinctTailPositions("2022/2022-12-09-sample-2.txt", 9));
        System.out.println(getDistinctTailPositions("2022/2022-12-09.txt", 9));
    }

    private static final Pattern PATTERN = Pattern.compile("(\\w)\\s+(\\d+)");

    private static long getDistinctTailPositions(String file, int tailsNumber) {
        AtomicReference<Position> head = new AtomicReference<>(new Position(0, 0));
        AtomicReference<List<Position>> tails = new AtomicReference<>(new ArrayList<>(tailsNumber));
        for (int i = 0; i < tailsNumber; i++) {
            tails.get().add(head.get());
        }
        Set<Position> tailVisited = new HashSet<>();
        tailVisited.add(tails.get().get(tailsNumber - 1));

        FileUtil.read(file, l -> {
            Matcher m = PATTERN.matcher(l);
            if (!m.matches()) {
                throw new IllegalStateException("Unexpected format: " + l);
            }

            Direction direction = Direction.valueOf(m.group(1));
            int steps = Integer.parseInt(m.group(2));

            final NewTailResult newTails = getNewTails(head.get(), direction, steps, tails.get());

            if (tails.get().size() != newTails.newTails.size()) {
                throw new IllegalStateException("Unexpected new tails size: " + newTails.newTails.size());
            }
            tails.set(newTails.newTails);
            head.set(newTails.newHead);
            tailVisited.addAll(newTails.lastTailVisits);
        });

        return tailVisited.size();
    }

    private static NewTailResult getNewTails(Position head, Direction direction, int steps, List<Position> currentTails) {
        List<Position> res = new ArrayList<>();
        List<Position> tails = new ArrayList<>(currentTails);

        Position newHead = head;
        for (int i = 0; i < steps; i++) {
            switch (direction) {
                case D -> newHead = new Position(newHead.x, newHead.y + 1);
                case U -> newHead = new Position(newHead.x, newHead.y - 1);
                case L -> newHead = new Position(newHead.x - 1, newHead.y);
                case R -> newHead = new Position(newHead.x + 1, newHead.y);
                default -> throw new IllegalStateException("Unexpected direction: " + direction);
            }

            tails.set(0, getNewTail(newHead, tails.get(0)));
            for (int j = 1; j < tails.size(); j++) {
                tails.set(j, getNewTail(tails.get(j - 1), tails.get(j)));
            }

            //System.out.println(direction + ":" + steps + " -> head=" + newHead + ", tails=" + tails);
            //printGraph(newHead, tails);

            res.add(tails.get(tails.size() - 1));
        }

        Assert.notNull(newHead, "");
        return new NewTailResult(newHead, tails, res);
    }

    private static void printGraph(Position newHead, List<Position> tails) {
        int minX = newHead.x;
        int maxX = newHead.x;
        int minY = newHead.y;
        int maxY = newHead.y;
        for (Position t : tails) {
            minX = Math.min(minX, t.x);
            maxX = Math.max(maxX, t.x);
            minY = Math.min(minY, t.y);
            maxY = Math.max(maxY, t.y);
        }
        for (int j = minY; j <= maxY; j++) {
            for (int i = minX; i <= maxX; i++) {
                Position temp = new Position(i, j);
                if (temp.equals(newHead)) {
                    System.out.print('H');
                    continue;
                }
                boolean found = false;
                for (int z = 0; z < tails.size(); z++) {
                    if (temp.equals(tails.get(z))) {
                        System.out.print(z + 1);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
    }

    private static Position getNewTail(Position newHead, Position currentTail) {
        int xDiff = newHead.x - currentTail.x;
        int yDiff = newHead.y - currentTail.y;
        int xDiffAbs = Math.abs(xDiff);
        int yDiffAbs = Math.abs(yDiff);
        if (xDiffAbs <= 1 && yDiffAbs <= 1) {
            return currentTail;
        }

        int stepX = xDiff > 0 ? 1 : -1;
        int stepY = yDiff > 0 ? 1 : -1;
        if (xDiffAbs == 2) {
            if (yDiffAbs == 0) {
                return new Position(currentTail.x + stepX, currentTail.y);
            } else {
                return new Position(currentTail.x + stepX, currentTail.y + stepY);
            }
        }
        if (yDiffAbs == 2) {
            if (xDiffAbs == 0) {
                return new Position(currentTail.x, currentTail.y + stepY);
            } else {
                return new Position(currentTail.x + stepX, currentTail.y + stepY);
            }
        }

        throw new IllegalStateException("Unexpected state");
    }

    private enum Direction {
        R,
        L,
        U,
        D
    }

    private static class NewTailResult {
        final Position newHead;
        final List<Position> newTails;
        final List<Position> lastTailVisits;

        public NewTailResult(Position newHead, List<Position> newTails, List<Position> lastTailVisits) {
            this.newHead = newHead;
            this.newTails = newTails;
            this.lastTailVisits = lastTailVisits;
        }
    }
}
