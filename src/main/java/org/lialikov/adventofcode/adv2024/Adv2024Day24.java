package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Adv2024Day24 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 2024
        System.out.println(getPart1("2024/2024-12-24-sample.txt"));
        // 51715173446832
        System.out.println(getPart1("2024/2024-12-24.txt"));

        // dpg,kmb,mmf,tvp,vdk,z10,z15,z25
        System.out.println(getPart2("2024/2024-12-24.txt"));

        System.out.println(new Date());
    }

    private static final Pattern p1 = Pattern.compile("([\\da-zA-Z]+):\\s*(\\d+)");
    private static final Pattern p2 = Pattern.compile("([\\da-zA-Z]+) ([a-zA-Z]+) ([\\da-zA-Z]+) -> ([\\da-zA-Z]+)");

    private static Wire fromString(String wire) {
        char first = wire.charAt(0);
        if (first == 'x' || first == 'y' || first == 'z') {
            return new Wire(wire, first, Integer.parseInt(wire.substring(1)));
        }
        return new Wire(wire, first, null);
    }

    private static Input read(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        Map<Wire, Integer> init = new HashMap<>();
        List<Gate> gates = new ArrayList<>();
        boolean parseInit = true;
        for (String line : lines) {
            if (line.isBlank()) {
                parseInit = false;
                continue;
            }
            if (parseInit) {
                Matcher m = p1.matcher(line);
                if (!m.matches()) {
                    throw new IllegalStateException("Cannot happen");
                }
                String wire = m.group(1);
                Integer value = Integer.parseInt(m.group(2));
                init.put(fromString(wire), value);
            } else {
                Matcher m = p2.matcher(line);
                if (!m.matches()) {
                    throw new IllegalStateException("Cannot happen");
                }
                String wireIn1 = m.group(1);
                Op op = Op.valueOf(m.group(2));
                String wireIn2 = m.group(3);
                String wireOut = m.group(4);
                gates.add(new Gate(fromString(wireIn1), fromString(wireIn2), fromString(wireOut), op));
            }
        }
        return new Input(init, gates);
    }

    private static long getPart1(String inputFile) {
        Input in = read(inputFile);

        Map<Wire, Integer> values = new HashMap<>(in.init);
        while (true) {
            boolean changed = false;
            for (Gate g : in.gates) {
                Integer in1 = values.get(g.wireIn1);
                Integer in2 = values.get(g.wireIn2);
                if (in1 == null || in2 == null) {
                    continue;
                }
                Integer value = values.get(g.wireOut);
                if (value != null) {
                    continue;
                }
                final int res = switch (g.op) {
                    case OR -> (in1 == 1 || in2 == 1) ? 1 : 0;
                    case AND -> (in1 == 1 && in2 == 1) ? 1 : 0;
                    case XOR -> (!in1.equals(in2)) ? 1 : 0;
                };
                values.put(g.wireOut, res);
                changed = true;
            }
            if (!changed) {
                break;
            }
        }

        return getPart1Res(values);
    }

    private static long getPart1Res(Map<Wire, Integer> values) {
        Map<Integer, Integer> zValues = new HashMap<>();
        int maxIndex = 0;
        for (Map.Entry<Wire, Integer> e : values.entrySet()) {
            if (e.getKey().first == 'z') {
                zValues.put(e.getKey().index, e.getValue());
                if (e.getKey().index > maxIndex) {
                    maxIndex = e.getKey().index;
                }
            }
        }

        long res = 0;
        for (int i = 0; i <= maxIndex; i++) {
            int value = zValues.get(i);
            if (value == 1) {
                res += (long) Math.pow(2, i);
            }
        }
        return res;
    }

    private static boolean isXY(Gate gate, Integer index) {
        return index.equals(gate.wireIn1.index) && index.equals(gate.wireIn2.index) &&
                ((gate.wireIn1.first == 'x' && gate.wireIn2.first == 'y') || (gate.wireIn1.first == 'y' && gate.wireIn2.first == 'x'));
    }

    private static int getSize(Input in) {
        int size = 0;
        for (Gate g : in.gates) {
            if (g.wireIn1.index != null && g.wireIn1.index > size) {
                size = g.wireIn1.index;
            }
            if (g.wireIn2.index != null && g.wireIn2.index > size) {
                size = g.wireIn2.index;
            }
            if (g.wireOut.index != null && g.wireOut.index > size) {
                size = g.wireOut.index;
            }
        }
        return size;
    }

    @SuppressWarnings("SameParameterValue")
    private static String getPart2(String inputFile) {
        Input in = read(inputFile);

        final int size = getSize(in);

        Map<Integer, Gate> xors = new HashMap<>();
        for (int i = 0; i < size; i++) {
            Integer index = i;
            for (Gate g : in.gates) {
                if (isXY(g, index) && g.op == Op.XOR) {
                    xors.put(index, g);
                }
            }
        }

        Gate firstGate = xors.get(0);
        if (!firstGate.wireOut.name.equals("z00")) {
            throw new IllegalStateException("Invalid out wire for x00 xor y00 gate: " + firstGate);
        }

        List<Pair<Gate, Gate>> swaps = new ArrayList<>();

        for (int i = 1; i < size; i++) {
            Gate xor = xors.get(i);
            String zWireName = "z" + (i > 9 ? i : ("0" + i));
            Wire zWire = new Wire(zWireName, 'z', i);
            Gate zGate = findOut(in.gates, zWire);
            if (zGate.op != Op.XOR) {
                Gate toReplace = null;
                for (Gate gate : in.gates) {
                    if (gate.op == Op.XOR && (gate.wireIn1.equals(xor.wireOut) || gate.wireIn2.equals(xor.wireOut))) {
                        toReplace = gate;
                        break;
                    }
                }
                if (toReplace == null) {
                    throw new IllegalStateException("Not found");
                }
                swaps.add(Pair.of(zGate, toReplace));
                continue;
            }

            boolean xorMatches = zGate.wireIn1.equals(xor.wireOut) || zGate.wireIn2.equals(xor.wireOut);
            if (!xorMatches) {
                Gate outGate1 = findOut(in.gates, zGate.wireIn1);
                Gate outGate2 = findOut(in.gates, zGate.wireIn2);
                Gate toReplace = outGate1.op == Op.OR ? outGate2 : outGate1;
                swaps.add(Pair.of(xor, toReplace));
            }
        }

        if (swaps.size() != 4) {
            throw new IllegalStateException("Unexpected gate swap count (4 expected): " + swaps.size());
        }
        return swaps.stream()
                .flatMap(s -> Stream.of(s.getFirst().wireOut, s.getSecond().wireOut()))
                .map(w -> w.name)
                .sorted()
                .collect(Collectors.joining(","));
    }

    private static Gate findOut(List<Gate> gates, Wire out) {
        List<Gate> res = new ArrayList<>();
        for (Gate g : gates) {
            if (g.wireOut.name.equals(out.name)) {
                res.add(g);
            }
        }
        if (res.size() != 1) {
            throw new IllegalStateException("Unexpected out wire count (1 expected): " + res.size());
        }
        return res.get(0);
    }

    private enum Op {
        AND,
        OR,
        XOR
    }

    private record Wire(String name, char first, Integer index) { }
    private record Gate(Wire wireIn1, Wire wireIn2, Wire wireOut, Op op) { }

    private record Input(Map<Wire, Integer> init, List<Gate> gates) { }
}
