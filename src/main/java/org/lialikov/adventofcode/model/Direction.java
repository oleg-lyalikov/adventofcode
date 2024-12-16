package org.lialikov.adventofcode.model;

public enum Direction {
    E,
    S,
    W,
    N;

    public Direction opposite() {
        return switch (this) {
            case E -> W;
            case S -> N;
            case W -> E;
            case N -> S;
        };
    }

    public int nextI(int i) {
        return nextI(i, 1);
    }

    public int nextI(int i, int steps) {
        return switch (this) {
            case E, W -> i;
            case S -> i + steps;
            case N -> i - steps;
        };
    }

    public int nextJ(int j) {
        return nextJ(j, 1);
    }

    public int nextJ(int j, int steps) {
        return switch (this) {
            case E -> j + steps;
            case S, N -> j;
            case W -> j - steps;
        };
    }

    public Position nextP(Position p) {
        return new Position(nextJ(p.x()), nextI(p.y()));
    }
}
