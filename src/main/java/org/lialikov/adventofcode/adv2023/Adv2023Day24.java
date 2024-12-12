package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.lialikov.adventofcode.util.MathUtil;
import org.lialikov.adventofcode.util.ParseUtil;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Adv2023Day24 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 2
        //System.out.println(getPart1("2023/2023-12-24-sample.txt", 7d, 27d));
        // 15889
        //System.out.println(getPart1("2023/2023-12-24.txt", 200000000000000d, 400000000000000d));
        // 47
        //System.out.println(getPart2("2023/2023-12-24-sample.txt"));
        // 801386475216902
        System.out.println(getPart2("2023/2023-12-24.txt"));

        System.out.println(new Date());
    }

    private static long getPart2(String inputFile) {
        List<Line> lines = parse(inputFile);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Future<Pair<Long, Long>> x = executorService.submit(() -> {
            Pair<Long, Long> x1 = findVCoord0(lines, Coord.X);
            if (x1 == null) {
                throw new IllegalStateException("X not found");
            }
            System.out.println(x1);
            return x1;
        });
        Future<Pair<Long, Long>> y = executorService.submit(() -> {
            Pair<Long, Long> y1 = findVCoord0(lines, Coord.Y);
            if (y1 == null) {
                throw new IllegalStateException("Y not found");
            }
            System.out.println(y1);
            return y1;
        });

        Future<Pair<Long, Long>> z = executorService.submit(() -> {
            Pair<Long, Long> z1 = findVCoord0(lines, Coord.Z);
            if (z1 == null) {
                throw new IllegalStateException("Z not found");
            }
            System.out.println(z1);
            return z1;
        });

        try {
            return x.get().getSecond() + y.get().getSecond() + z.get().getSecond();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    private static Pair<Long, Long> findVCoord0(List<Line> lines, Coord coord) {
        final long coordMin = coord.getStart(getWithMinCoord(lines, coord));
        final long coordMax = coord.getStart(getWithMaxCoord(lines, coord));
        for (long vv = 1; vv < 1_000_000_000; vv++) {
            if (vv % 1000 == 0) {
                System.out.println("Coord: " + coord + ", velocity: " + vv + ", date: " + new Date());
            }
            List<Long> vWithNeg = List.of(vv, -vv);
            for (long v : vWithNeg) {
                //Long coord0 = findCoord0(v, lines, coord);
                //Long coord0 = findCoord0(v, lines, coord, getCoordMin(v, lines, coord), getCoordMax(v, lines, coord));
                Long coord0 = findCoord0(v, lines, coord, coordMin, coordMax);
                if (coord0 != null) {
                    System.out.println("coord0: " + coord0 + ", v: " + v);
                    return Pair.of(v, coord0);
                }
            }
        }
        return null;
    }

    private static long getCoordMax(long v, List<Line> lines, Coord coord) {
        // TODO: hz
        long resDefault = coord.getStart(getWithMaxCoord(lines, coord)) * 2;
        if (v > 0) {
            return lines.stream()
                    .filter(l -> coord.getVel(l) <= v)
                    .mapToLong(coord::getStart)
                    .min()
                    .orElse(resDefault);
        } else {
            long res1 = lines.stream()
                    .filter(l -> coord.getVel(l) <= v)
                    .mapToLong(coord::getStart)
                    .max()
                    .orElse(resDefault);
            long timeMax = lines.stream()
                    .filter(l -> coord.getVel(l) < 0)
                    .mapToLong(l -> Math.abs(coord.getStart(l) / coord.getVel(l)) + 1)
                    .max()
                    .orElse(resDefault / v);
            long res2 = Math.abs(timeMax * v);
            return Math.min(res1, res2);
        }
    }

    private static long getCoordMin(long v, List<Line> lines, Coord coord) {
        if (v > 0) {
            Optional<Line> line = getWithVelGreaterThan(lines, coord, v)
                    .max(Comparator.comparingLong(coord::getStart));
            return line.map(coord::getStart).orElse(0L);
        } else {
            // TODO: default is not optimal
            long resDefault = v * (lines.size() - 1);
            return lines.stream()
                    .filter(l -> coord.getVel(l) >= v)
                    .mapToLong(coord::getStart)
                    .max()
                    .orElse(resDefault);
        }
    }

    private static Line getWithMaxVel(List<Line> lines, Coord coord) {
        return Collections.max(lines, Comparator.comparingLong(coord::getVel));
    }

    private static Stream<Line> getWithVelGreaterThan(List<Line> lines, Coord coord, long value) {
        return lines.stream().filter(l -> coord.getVel(l) >= value);
    }

    private static Line getWithMaxCoord(List<Line> lines, Coord coord) {
        return Collections.max(lines, Comparator.comparingLong(coord::getStart));
    }

    private static Line getWithMinVel(List<Line> lines, Coord coord) {
        return Collections.min(lines, Comparator.comparingLong(coord::getVel));
    }

    private static Line getWithMinCoord(List<Line> lines, Coord coord) {
        return Collections.min(lines, Comparator.comparingLong(coord::getStart));
    }

    private static Map<Long, List<Line>> byVel(List<Line> lines, Coord coord) {
        Map<Long, List<Line>> res = new HashMap<>();
        for (Line line : lines) {
            long vel = coord.getVel(line);
            res.computeIfAbsent(vel, v -> new ArrayList<>()).add(line);
        }

        res.forEach((vel, list) -> {
            if (list.size() == 1) {
                return;
            }
            long start1 = coord.getStart(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                long start2 = coord.getStart(list.get(i));
                if ((start2 - start1) % vel != 0) {
                    //throw new IllegalStateException("!!!");
                }
            }
        });

        return res;
    }

    private static Long findCoord0(long v, List<Line> lines, Coord coord) {
        long coordStartMin = getCoordMin(v, lines, coord);
        long coordStartMax = getCoordMax(v, lines, coord);

        List<Line> sortedByVel = new ArrayList<>(lines);
        sortedByVel.sort(Comparator.comparingLong(coord::getVel));

        List<Line> sortedByStart = new ArrayList<>(lines);
        sortedByStart.sort(Comparator.comparingLong(coord::getStart));

        if (coordStartMax < coordStartMin) {
            return null;
        }

        Map<Long, List<Line>> byVel = byVel(lines, coord);

        List<Line> withCurrentVel = byVel.get(v);
        if (withCurrentVel != null && withCurrentVel.size() > 1) {
            return null;
        } else if (withCurrentVel != null && withCurrentVel.size() == 1) {
            coordStartMin = coord.getStart(withCurrentVel.get(0));
            coordStartMax = coordStartMin;
        }

        Set<Long> velDiffs = new HashSet<>();
        Map<Long, List<Line>> byVelDiff = new HashMap<>();
        long lcm = 1;
        for (Line l : lines) {
            long velDiff = coord.getVel(l) - v;
            byVelDiff.computeIfAbsent(velDiff, vd -> new ArrayList<>()).add(l);
            lcm = MathUtil.lcm(lcm, velDiff);
            velDiffs.add(velDiff);
        }
        for (Map.Entry<Long, List<Line>> e : byVelDiff.entrySet()) {
            long velDiff = e.getKey();
            List<Line> list = e.getValue();
            if (list.size() == 1) {
                continue;
            }
            long s1 = coord.getStart(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                long s2 = coord.getStart(list.get(i));
            }
        }

        return findCoord0(v, lines, coord, coordStartMin, coordStartMax);
    }

    private static Long findCoord0(long v, List<Line> lines, Coord coord, long coordStartMin, long coordStartMax) {
        long finalLcm = 1;
        for (Line line : lines) {
            finalLcm = MathUtil.lcm(finalLcm, v - coord.getVel(line));
        }
        long lcm;
        long coordDiff = 0;
        long vDiff = 0;
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            long vNextDiff = v - coord.getVel(line);
            long coordNextDiff = coord.getStart(line);
            if (vNextDiff == 0) {
                if (isValid(v, lines, coord, coordNextDiff)) {
                    return coordNextDiff;
                } else {
                    return null;
                }
            }

            Long resCoord = null;
            if (i == 0) {
                lcm = MathUtil.lcm(1, vNextDiff);
                resCoord = coordNextDiff;
            } else {
                lcm = MathUtil.lcm(vDiff, vNextDiff);
                boolean last = lcm > coordStartMax;
                for (long j = lcm + coordDiff; j >= coordDiff; j -= vDiff) {
                    if ((coordNextDiff - j) % vNextDiff == 0) {
                        resCoord = j;
                        break;
                    }
                }
                if (last) {
                    break;
                }
            }

            if (resCoord == null) {
                return null;
            }
            vDiff = lcm;
            coordDiff = resCoord;
        }

        System.out.println("CoordDiff: " + coordDiff + ", coord: " + coord + ", v: " + v);
        long minCoord = coord.getStart(getWithMinCoord(lines, coord));
        long start = coordDiff - ((((coordDiff - minCoord) / vDiff) + 1) * vDiff);
        for (long i = start; i <= (coordStartMax + vDiff * 10); i += vDiff) {
            if (isValid(v, lines, coord, i)) {
                return i;
            }
        }

        return null;
    }

    private static boolean isValid(long v, List<Line> lines, Coord coord, long c) {
        boolean found = true;
        Set<Long> times = new HashSet<>();
        for (Line l : lines) {
            long coordDiff = c - coord.getStart(l);
            long velDiff = coord.getVel(l) - v;
            if (velDiff == 0) {
                if (coordDiff != 0) {
                    found = false;
                    break;
                }
            } else if (coordDiff % velDiff == 0) {
                long divide = coordDiff / velDiff;
                if (divide < 0 || times.contains(divide)) {
                    found = false;
                    break;
                }
                times.add(divide);
            } else {
                found = false;
                break;
            }
        }
        return found;
    }

    private static final int SCALE = 100;

    private static double getYCross(Line l1, Line l2) {
        double part1 = (l1.A * l2.C / l2.A) - l1.C;
        double part2 = l1.B - (l2.B * (double) l1.A / l2.A);
        return part1 / part2;
    }

    private static BigDecimal getBigYCross(Line l1, Line l2, RoundingMode roundingMode) {
        BigDecimal C1 = l1.getBigC(roundingMode);
        BigDecimal C2 = l2.getBigC(roundingMode);
        BigDecimal part1 = BigDecimal.valueOf(l1.A).multiply(C2).divide(BigDecimal.valueOf(l2.A), SCALE, roundingMode).subtract(C1);
        BigDecimal part2 = BigDecimal.valueOf(l1.B).subtract(
                BigDecimal.valueOf(l2.B).multiply(BigDecimal.valueOf(l1.A)).divide(BigDecimal.valueOf(l2.A), SCALE, roundingMode)
        );
        if (part2.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return part1.divide(part2, SCALE, roundingMode);
    }

    private static double getXByY(Line l, double y) {
        return (-l.C - l.B * y) / l.A;
    }

    private static double getTimeByY(Line l, double y) {
        return (y - (double) l.yStart) / l.yVel;
    }

    private static BigDecimal getBigTimeByY(Line l, BigDecimal y, RoundingMode roundingMode) {
        return y.subtract(BigDecimal.valueOf(l.yStart)).divide(BigDecimal.valueOf(l.yVel), SCALE, roundingMode);
    }

    private static BigDecimal getBigXByY(Line l, BigDecimal y, RoundingMode roundingMode) {
        return l.getBigC(roundingMode).negate().subtract(
                BigDecimal.valueOf(l.B).multiply(y)
        ).divide(BigDecimal.valueOf(l.A), SCALE, roundingMode);
    }

    private static long getPart1(String inputFile, double areaStart, double areaEnd) {
        List<Line> lines = parse(inputFile);
        long res = getPart1Simple(lines, areaStart, areaEnd);
        long res1 = getPart1(lines, areaStart, areaEnd, RoundingMode.HALF_UP);
        long res2 = getPart1(lines, areaStart, areaEnd, RoundingMode.CEILING);
        long res3 = 0;// getPart1(lines, areaStart, areaEnd, null);
        System.out.println("Res: " + res + ", res1: " + res1 + ", res2: " + res2 + ", res3: " + res3);
        return res;
    }

    private static long getPart1(List<Line> lines, double areaStart, double areaEnd, RoundingMode roundingMode) {
        long res = 0;
        BigDecimal areaStartBig = BigDecimal.valueOf(areaStart);
        BigDecimal areaEndBig = BigDecimal.valueOf(areaEnd);
        for (int i = 0; i < lines.size(); i++) {
            Line l1 = lines.get(i);
            for (int j = i + 1; j < lines.size(); j++) {
                Line l2 = lines.get(j);
                BigDecimal yCross = getBigYCross(l1, l2, roundingMode);
                if (yCross == null) {
                    continue;
                }
//                if (yCross == Double.NEGATIVE_INFINITY || yCross == Double.POSITIVE_INFINITY) {
//                    continue;
//                }
                BigDecimal time1 = getBigTimeByY(l1, yCross, roundingMode);
                BigDecimal time2 = getBigTimeByY(l2, yCross, roundingMode);
                if (time1.compareTo(BigDecimal.ZERO) < 0 || time2.compareTo(BigDecimal.ZERO) < 0) {
                    continue;
                }
                BigDecimal xCross = getBigXByY(l1, yCross, roundingMode);
                if (yCross.compareTo(areaStartBig) < 0 || yCross.compareTo(areaEndBig) > 0 ||
                        xCross.compareTo(areaStartBig) < 0 || xCross.compareTo(areaEndBig) > 0) {
                    continue;
                }
                res++;
                //System.out.println("i: " + i + ", j: " + j + ", x: " + xCross + ", y: " + yCross + ", time1: " + time1 + ", time2: " + time2);
            }
        }
        return res;
    }

    private static long getPart1Simple(List<Line> lines, double areaStart, double areaEnd) {
        long res = 0;
        for (int i = 0; i < lines.size(); i++) {
            Line l1 = lines.get(i);
            for (int j = i + 1; j < lines.size(); j++) {
                Line l2 = lines.get(j);
                double yCross = getYCross(l1, l2);
                if (yCross == Double.NEGATIVE_INFINITY || yCross == Double.POSITIVE_INFINITY) {
                    continue;
                }
                double time1 = getTimeByY(l1, yCross);
                double time2 = getTimeByY(l2, yCross);
                if (time1 < 0 || time2 < 0) {
                    continue;
                }
                double xCross = getXByY(l1, yCross);
                if (yCross < areaStart || yCross > areaEnd || xCross < areaStart || xCross > areaEnd) {
                    continue;
                }
                res++;
                //System.out.println("i: " + i + ", j: " + j + ", x: " + xCross + ", y: " + yCross + ", time1: " + time1 + ", time2: " + time2);
            }
        }
        return res;
    }

    private static List<Line> parse(String inputFile) {
        List<String> linesStr = FileUtil.readLines(inputFile);
        return linesStr.stream().map(l -> {
            String[] parts = l.split("@");
            if (parts.length != 2) {
                throw new IllegalStateException("!!!");
            }
            List<Long> coords = ParseUtil.parse(parts[0], "\\s*,\\s*");
            if (coords.size() != 3) {
                throw new IllegalStateException("!!!");
            }
            List<Long> velos = ParseUtil.parse(parts[1], "\\s*,\\s*");
            if (velos.size() != 3) {
                throw new IllegalStateException("!!!");
            }
            long A = Math.abs(velos.get(1));
            if (velos.get(0) > 0) {
                A = -A;
            }
            long B = Math.abs(velos.get(0));
            if (velos.get(1) < 0) {
                B = -B;
            }
            double C = A * ((velos.get(0) * (double) coords.get(1) / velos.get(1)) - coords.get(0));
            BigDecimal C1 = getC(velos.get(0), coords.get(1), velos.get(1), coords.get(0), A, RoundingMode.HALF_UP);
            BigDecimal C2 = getC(velos.get(0), coords.get(1), velos.get(1), coords.get(0), A, RoundingMode.CEILING);
            BigDecimal C3 = BigDecimal.valueOf(0);//getC(velos.get(0), coords.get(1), velos.get(1), coords.get(0), A, null);
            return new Line(coords.get(0), coords.get(1), coords.get(2), velos.get(0), velos.get(1), velos.get(2), A, B, C, C1, C2, C3);
        }).collect(Collectors.toList());
    }

    private static BigDecimal getC(long velos0, long coords1, long velos1, long coords0, long A, RoundingMode roundingMode) {
        BigDecimal v0 = BigDecimal.valueOf(velos0);
        BigDecimal coord1 = BigDecimal.valueOf(coords1);
        BigDecimal v1 = BigDecimal.valueOf(velos1);
        BigDecimal coord0 = BigDecimal.valueOf(coords0);
        BigDecimal res = v0.multiply(coord1);
        if (roundingMode == null) {
            res = res.divide(v1);
        } else {
            res = res.divide(v1, SCALE, roundingMode);
        }
        return res.subtract(coord0).multiply(BigDecimal.valueOf(A));
    }

    private record Line(
            long xStart,
            long yStart,
            long zStart,
            long xVel,
            long yVel,
            long zVel,
            long A,
            long B,
            double C,
            BigDecimal C1,
            BigDecimal C2,
            BigDecimal C3
    ) {
        public BigDecimal getBigC(RoundingMode roundingMode) {
            return roundingMode == RoundingMode.HALF_UP ? C1 : C2;
        }
    }

    private enum Coord {
        X() {
            @Override
            public long getStart(Line line) {
                return line.xStart;
            }

            @Override
            public long getVel(Line line) {
                return line.xVel;
            }
        },
        Y() {
            @Override
            public long getStart(Line line) {
                return line.yStart;
            }

            @Override
            public long getVel(Line line) {
                return line.yVel;
            }
        },
        Z() {
            @Override
            public long getStart(Line line) {
                return line.zStart;
            }

            @Override
            public long getVel(Line line) {
                return line.zVel;
            }
        };

        public abstract long getStart(Line line);
        public abstract long getVel(Line line);
    }
}
