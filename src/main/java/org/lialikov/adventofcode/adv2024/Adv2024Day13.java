package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.model.Position;
import org.lialikov.adventofcode.model.PositionLong;
import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Adv2024Day13 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 480
        System.out.println(getPart1("2024/2024-12-13-sample.txt"));
        // 35082
        System.out.println(getPart1("2024/2024-12-13.txt"));

        // 480
        System.out.println(getPart2("2024/2024-12-13-sample.txt", null));
        // 875318608908 ?
        System.out.println(getPart2("2024/2024-12-13-sample.txt", 10000000000000L));
        // 82570698600470
        System.out.println(getPart2("2024/2024-12-13.txt", 10000000000000L));

        System.out.println(new Date());
    }

    private static final Pattern A = Pattern.compile("Button A: X\\+(\\d+),\\s*Y\\+(\\d+)");
    private static final Pattern B = Pattern.compile("Button B: X\\+(\\d+),\\s*Y\\+(\\d+)");
    private static final Pattern PRIZE = Pattern.compile("Prize: X=(\\d+),\\s*Y=(\\d+)");

    private static Position toPosition(Matcher m) {
        if (!m.matches()) {
            throw new IllegalStateException("Does not match " + m);
        }
        return new Position(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
    }

    private static List<Input> read(String inputFile, Long addToPrize) {
        List<Input> res = new ArrayList<>();
        List<String> lines = FileUtil.readLines(inputFile);
        Position a = null;
        Position b = null;
        PositionLong prize;
        for (String line : lines) {
            if (line.isBlank()) {
                a = null;
                b = null;
                continue;
            }
            if (a == null) {
                Matcher m = A.matcher(line);
                a = toPosition(m);
            } else if (b == null) {
                Matcher m = B.matcher(line);
                b = toPosition(m);
            } else {
                Matcher m = PRIZE.matcher(line);
                Position p = toPosition(m);
                prize = addToPrize != null ? new PositionLong(p.x() + addToPrize, p.y() + addToPrize) : new PositionLong(p.x(), p.y());
                res.add(new Input(a, b, prize));
            }
        }
        return res;
    }

    private static final int maxI = 100;

    private static long getTokens(Pair<Long, Long> step) {
        return getTokens(step.getFirst(), step.getSecond());
    }

    private static long getTokens(long aSteps, long bSteps) {
        return aSteps * 3 + bSteps;
    }

    private static long getPart1(String inputFile) {
        long res = 0;
        List<Input> input = read(inputFile, null);
        for (Input in : input) {
            int ax = in.a.x();
            long bxSteps;
            List<Pair<Long, Long>> xSteps = new ArrayList<>();
            for (long i = 0; i <= maxI; i++) {
                long resAx = ax * i;
                if (resAx > in.prize.x()) {
                    break;
                }
                long remaining = in.prize.x() - resAx;
                if (remaining % in.b.x() == 0) {
                    bxSteps = remaining / in.b.x();
                    if (bxSteps <= maxI) {
                        xSteps.add(Pair.of(i, bxSteps));
                    }
                }
            }
            Long min = null;
            for (Pair<Long, Long> step : xSteps) {
                long aSteps = step.getFirst();
                long bSteps = step.getSecond();
                long ay = aSteps * in.a.y();
                long by = bSteps * in.b.y();
                if (ay <= in.prize.y() && by <= in.prize.y() && (ay + by == in.prize.y())) {
                    long tokens = getTokens(step);
                    if (min == null || min > tokens) {
                        min = tokens;
                    }
                }
            }
            if (min != null) {
                res += min;
            }
        }
        return res;
    }

    private static long getPart2(String inputFile, Long addToPrize) {
        long res = 0;
        List<Input> input = read(inputFile, addToPrize);
        for (Input in : input) {
            BigDecimal[] divided = getBStepsDivided(in);
            if (divided[1].equals(new BigDecimal(0))) {
                BigDecimal bBig = divided[0];
                long b = bBig.longValue();

                BigDecimal toSubtract = bBig.multiply(new BigDecimal(in.b.x()));
                BigDecimal v = new BigDecimal(in.prize.x());
                v = v.subtract(toSubtract);
                BigDecimal[] dividedA = v.divideAndRemainder(new BigDecimal(in.a.x()));
                if (dividedA[1].equals(new BigDecimal(0))) {
                    long a = dividedA[0].longValue();
                    long tokens = getTokens(a, b);
                    res += tokens;
                }
            }
        }
        return res;
    }

    /**
     * Returns b steps
     * ax[a] + bx[b] = X
     * ay[a] + by[b] = Y
     */
    private static BigDecimal[] getBStepsDivided(Input in) {
        BigDecimal val1 = new BigDecimal(in.a.y());
        val1 = val1.multiply(new BigDecimal(in.prize.x()));
        BigDecimal val2 = new BigDecimal(in.a.x());
        val2 = val2.multiply(new BigDecimal(in.prize.y()));
        BigDecimal val = val1.subtract(val2);

        BigDecimal div1 = new BigDecimal(in.a.y());
        div1 = div1.multiply(new BigDecimal(in.b.x()));
        BigDecimal div2 = new BigDecimal(in.b.y());
        div2 = div2.multiply(new BigDecimal(in.a.x()));
        BigDecimal div = div1.subtract(div2);

        return val.divideAndRemainder(div);
    }

    public record Input(Position a, Position b, PositionLong prize) { }
}
