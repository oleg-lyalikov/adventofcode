package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Adv2024Day17 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 4,6,3,5,6,3,5,2,1,0
        System.out.println(getPart1("2024/2024-12-17-sample1.txt"));
        // 3,1,4,3,1,7,1,6,3
        System.out.println(getPart1("2024/2024-12-17.txt"));

        // 117440
        System.out.println(getPart2IterateAll("2024/2024-12-17-sample2.txt"));
        // 37221270076916
        System.out.println(getPart2("2024/2024-12-17.txt"));

        System.out.println(new Date());
    }

    private static long A;
    private static long B;
    private static long C;

    private static Input read(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        String line = lines.get(0);
        long a = Long.parseLong(line.substring(12));
        line = lines.get(1);
        long b = Long.parseLong(line.substring(12));
        line = lines.get(2);
        long c = Long.parseLong(line.substring(12));
        List<Integer> program = Arrays.stream(lines.get(4).substring(9)
                .split(","))
                .map(Integer::parseInt)
                .toList();
        A = a;
        B = b;
        C = c;
        return new Input(a, b, c, program);
    }

    private static List<Long> run(Input in) {
        List<Long> res = new ArrayList<>();
        for (int i = 0; i < in.program.size();) {
            int code = in.program.get(i);
            int operand = in.program.get(i + 1);
            Instr instr = Instr.byCode[code];
            if (instr == Instr.jnz) {
                if (A != 0) {
                    i = operand;
                    continue;
                }
            }
            long executed = instr.exec(operand);
            if (instr == Instr.out) {
                res.add(executed);
            }
            i += 2;
        }
        return res;
    }

    private static String getPart1(String inputFile) {
        Input in = read(inputFile);
        List<Long> res = run(in);
        return res.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    @SuppressWarnings("SameParameterValue")
    private static long getPart2IterateAll(String inputFile) {
        Input in = read(inputFile);
        long max = (long) Math.pow(8, in.program.size());
        for (long i = 0; i < max; i++) {
            A = i;
            List<Long> res = run(in);
            if (res.stream().map(Long::intValue).toList().equals(in.program)) {
                return i;
            }
        }
        throw new IllegalStateException("Not found");
    }

    @SuppressWarnings("SameParameterValue")
    private static long getPart2(String inputFile) {
        Input in = read(inputFile);
        int pow = in.program.size() - 1;
        Set<Long> results = new HashSet<>(List.of((long) Math.pow(8, pow)));
        long lastA;
        for (int i = in.program.size() - 1; i >= 0; i--) {
            long add = (long) Math.pow(8, i - 1);
            Set<Long> nextResults = new HashSet<>();
            for (Long result : results) {
                A = result;
                lastA = A;
                for (long j = 0; j < 8; j++) {
                    List<Long> res = run(in);
                    boolean matches = true;
                    for (int ii = in.program.size() - 1; ii >= i; ii--) {
                        if (res.get(i) != (long) in.program.get(i)) {
                            matches = false;
                            break;
                        }
                    }
                    if (matches) {
                        nextResults.add(lastA);
                    }
                    A = result + add * (j + 1);
                    lastA = A;
                }
            }
            results = nextResults;
        }
        if (results.size() != 1) {
            throw new IllegalStateException("Unexpected results amount: " + results);
        }
        return results.iterator().next();
    }

    private static long combo(int operand) {
        if (operand < 0) {
            throw new IllegalStateException("Cannot be negative: " + operand);
        }
        return switch (operand) {
            case 4 -> A;
            case 5 -> B;
            case 6 -> C;
            case 7 -> Long.MAX_VALUE;
            default -> operand;
        };
    }

    private enum Instr {
        adv(0) {
            @Override
            public long exec(int operand) {
                long denominator = (long) Math.pow(2, combo(operand));
                A = A / denominator;
                return A;
            }
        },
        bxl(1) {
            @Override
            public long exec(int operand) {
                B = B ^ operand;
                return B;
            }
        },
        bst(2) {
            @Override
            public long exec(int operand) {
                B = combo(operand) % 8;
                return B;
            }
        },
        jnz(3) {
            @Override
            public long exec(int operand) {
                return 0;
            }
        },
        bxc(4) {
            @Override
            public long exec(int operand) {
                B = B ^ C;
                return B;
            }
        },
        out(5) {
            @Override
            public long exec(int operand) {
                return combo(operand) % 8;
            }
        },
        bdv(6) {
            @Override
            public long exec(int operand) {
                long denominator = (long) Math.pow(2, combo(operand));
                B = A / denominator;
                return B;
            }
        },
        cdv(7) {
            @Override
            public long exec(int operand) {
                long denominator = (long) Math.pow(2, combo(operand));
                C = A / denominator;
                return C;
            }
        };

        private final int code;

        Instr(int code) {
            this.code = code;
        }

        public abstract long exec(int operand);

        public int getCode() {
            return code;
        }

        public static final Instr[] byCode;
        static {
            Instr[] res = new Instr[8];
            for (Instr v : Instr.values()) {
                res[v.getCode()] = v;
            }
            byCode = res;
        }
    }

    private record Input(long A, long B, long C, List<Integer> program) { }
}
