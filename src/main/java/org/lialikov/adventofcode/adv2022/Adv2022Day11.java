package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Adv2022Day11 {

    public static void main(String[] args) {
        // 10605
        System.out.println(getMonkeyBusiness("2022/2022-12-11-sample.txt", 20, true));
        // 67830
        System.out.println(getMonkeyBusiness("2022/2022-12-11.txt", 20, true));
        // 2713310158
        System.out.println(getMonkeyBusiness("2022/2022-12-11-sample.txt", 10000, false));
        // 15305381442
        System.out.println(getMonkeyBusiness("2022/2022-12-11.txt", 10000, false));
    }

    private static final Pattern MONKEY = Pattern.compile("Monkey\\s+(\\d+):");
    private static final Pattern STARTING_ITEMS = Pattern.compile("\\s+Starting items:\\s+([\\d,\\s]+)");
    private static final Pattern OPERATION = Pattern.compile("\\s+Operation:\\s+new\\s*=\\s*old\\s*([+*])\\s*((\\d+)|(old))");
    private static final Pattern TEST = Pattern.compile("\\s+Test:\\s+divisible by\\s+(\\d+)");
    private static final Pattern IF_TRUE = Pattern.compile("\\s+If true: throw to monkey\\s+(\\d+)");
    private static final Pattern IF_FALSE = Pattern.compile("\\s+If false: throw to monkey\\s+(\\d+)");

    private static List<Monkey> readMonkeys(String file) {
        List<Monkey> monkeys = new ArrayList<>();
        AtomicReference<Monkey> current = new AtomicReference<>();
        FileUtil.read(file, l -> {
            if (!StringUtils.hasText(l)) {
                return;
            }
            Matcher m = MONKEY.matcher(l);
            if (m.matches()) {
                Monkey monkey = new Monkey();
                monkey.n = Integer.parseInt(m.group(1));
                current.set(monkey);
                monkeys.add(monkey);
                return;
            }
            m = STARTING_ITEMS.matcher(l);
            if (m.matches()) {
                current.get().items = Arrays.stream(m.group(1)
                        .split("\\s*,\\s*"))
                        .map(s -> new BigInteger(s.trim()))
                        .collect(Collectors.toList());
                return;
            }
            m = OPERATION.matcher(l);
            if (m.matches()) {
                String opTypeStr = m.group(1);
                final OpType opType;
                if ("+".equals(opTypeStr)) {
                    opType = OpType.ADD;
                } else if ("*".equals(opTypeStr)) {
                    opType = OpType.MULTIPLY;
                } else {
                    throw new IllegalStateException("Unknown op type: " + opTypeStr);
                }
                String valueStr = m.group(2);
                boolean old = valueStr.equals("old");
                int value = old ? -1 : Integer.parseInt(valueStr);
                current.get().operation = new Operation(opType, value, old);
                return;
            }
            m = TEST.matcher(l);
            if (m.matches()) {
                current.get().testDivisibleBy = Integer.parseInt(m.group(1));
                return;
            }
            m = IF_TRUE.matcher(l);
            if (m.matches()) {
                current.get().testTrueMonkey = Integer.parseInt(m.group(1));
                return;
            }
            m = IF_FALSE.matcher(l);
            if (m.matches()) {
                current.get().testFalseMonkey = Integer.parseInt(m.group(1));
                return;
            }
            throw new IllegalStateException("Unparseable line: " + l);
        });
        for (int i = 0; i < monkeys.size(); i++) {
            if (monkeys.get(i).n != i) {
                throw new IllegalStateException("Unexpected monkey number: " + monkeys.get(i).n);
            }
        }
        return monkeys;
    }

    private static long getMonkeyBusiness(String file, int rounds, boolean divideByThree) {
        List<Monkey> monkeys = readMonkeys(file);
        long divisor = monkeys.stream()
                .mapToLong(m -> m.testDivisibleBy)
                .reduce(1, (left, right) -> left * right);
        for (int r = 0; r < rounds; r++) {
            for (Monkey m : monkeys) {
                Iterator<BigInteger> itemsIterator = m.items.iterator();
                while (itemsIterator.hasNext()) {
                    BigInteger item = itemsIterator.next();
                    BigInteger opValue = m.operation.old ? item : BigInteger.valueOf(m.operation.value);
                    if (m.operation.opType == OpType.ADD) {
                        item = item.add(opValue);
                    } else {
                        item = item.multiply(opValue);
                    }
                    if (divideByThree) {
                        item = item.divide(BigInteger.valueOf(3));
                        BigInteger[] divideRes = item.divideAndRemainder(BigInteger.valueOf(m.testDivisibleBy));
                        if (divideRes[1].equals(BigInteger.ZERO)) {
                            monkeys.get(m.testTrueMonkey).items.add(item);
                        } else {
                            monkeys.get(m.testFalseMonkey).items.add(item);
                        }
                    } else {
                        BigInteger[] divRes = item.divideAndRemainder(BigInteger.valueOf(divisor));
                        BigInteger newItem = divRes[1];
                        BigInteger[] divideRes = newItem.divideAndRemainder(BigInteger.valueOf(m.testDivisibleBy));
                        if (divideRes[1].equals(BigInteger.ZERO)) {
                            monkeys.get(m.testTrueMonkey).items.add(newItem);
                        } else {
                            monkeys.get(m.testFalseMonkey).items.add(newItem);
                        }
                    }
                    m.inspected++;
                    itemsIterator.remove();
                }
            }
        }
        monkeys.sort(Comparator.comparingLong(o -> o.inspected));
        return monkeys.get(monkeys.size() - 1).inspected * monkeys.get(monkeys.size() - 2).inspected;
    }

    private static class Monkey {
        int n;
        List<BigInteger> items = new ArrayList<>();
        Operation operation;
        int testDivisibleBy;
        int testTrueMonkey;
        int testFalseMonkey;
        long inspected = 0;
    }

    private enum OpType {
        MULTIPLY,
        ADD
    }
    private static class Operation {
        final OpType opType;
        final int value;
        final boolean old;

        Operation(OpType opType, int value, boolean old) {
            this.opType = opType;
            this.value = value;
            this.old = old;
        }
    }
}
