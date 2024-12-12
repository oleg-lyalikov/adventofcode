package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Adv2022Day13 {

    public static void main(String[] args) {
        // 13
        System.out.println(getIndicesSum("2022/2022-12-13-sample.txt"));
        // 5252
        System.out.println(getIndicesSum("2022/2022-12-13.txt"));
        // 140
        System.out.println(getDecoderKey("2022/2022-12-13-sample.txt"));
        // 20592
        System.out.println(getDecoderKey("2022/2022-12-13.txt"));
    }

    private static long getDecoderKey(String inputFile) {
        List<Packet> packets = readPackets(inputFile);
        Packet div1 = new Packet(new ArrayList<>(), null);
        div1.data.add(new Packet(List.of(2), div1));
        Packet div2 = new Packet(new ArrayList<>(), null);
        div2.data.add(new Packet(List.of(6), div1));
        packets.add(div1);
        packets.add(div2);
        packets.sort((p1, p2) -> {
            Boolean isRight = isRightOrder(p1, p2);
            //noinspection ComparatorMethodParameterNotUsed
            return (isRight == null || isRight) ? -1 : 1;
        });
        long res = 1;
        for (int i = 0; i < packets.size(); i++) {
            if (packets.get(i) == div1 || packets.get(i) == div2) {
                res *= (i + 1);
            }
        }
        return res;
    }

    private static long getIndicesSum(String inputFile) {
        List<Packet> packets = readPackets(inputFile);
        long res = 0;
        for (int i = 0; i < packets.size(); i += 2) {
            Packet p1 = packets.get(i);
            Packet p2 = packets.get(i + 1);
            Boolean isRight = isRightOrder(p1, p2);
            if (isRight == null || isRight) {
                res += i / 2 + 1;
            }
        }
        return res;
    }

    private static Boolean isRightOrder(Packet p1, Packet p2) {
        for (int i = 0; i < p1.data.size(); i++) {
            if (i >= p2.data.size()) {
                return false;
            }
            Object d1 = p1.data.get(i);
            Object d2 = p2.data.get(i);
            if (d1 instanceof Integer d1i && d2 instanceof Integer d2i) {
                if (d1i < d2i) {
                    return true;
                } else if (d1i > d2i) {
                    return false;
                } else {
                    continue;
                }
            }

            Packet d1p = d1 instanceof Packet ? (Packet) d1 : new Packet(List.of(d1), null);
            Packet d2p = d2 instanceof Packet ? (Packet) d2 : new Packet(List.of(d2), null);
            Boolean isRight = isRightOrder(d1p, d2p);
            if (isRight != null) {
                return isRight;
            }
        }
        if (p1.data.size() != p2.data.size()) {
            return true;
        }
        return null;
    }

    private static List<Packet> readPackets(String inputFile) {
        List<Packet> packets = new ArrayList<>();
        FileUtil.read(inputFile, s -> {
            if (!StringUtils.hasText(s)) {
                return;
            }

            Packet p = new Packet(new ArrayList<>(), null);
            int number = -1;
            Packet current = p;
            for (int i = 1; i < s.length(); i++) {
                char ch = s.charAt(i);
                if (Character.isDigit(ch)) {
                    if (number == -1) {
                        number = 0;
                    }
                    number = number * 10 + (ch - '0');
                } else if (ch == ',') {
                    if (number != -1) {
                        current.data.add(number);
                        number = -1;
                    }
                } else if (ch == '[') {
                    Packet child = new Packet(new ArrayList<>(), current);
                    current.data.add(child);
                    current = child;
                } else if (ch == ']') {
                    if (number != -1) {
                        current.data.add(number);
                        number = -1;
                    }
                    current = current.parent;
                }
            }
            packets.add(p);
        });
//        for (int i = 0; i < packets.size(); i += 2) {
//            System.out.println(packets.get(i));
//            System.out.println(packets.get(i + 1));
//            System.out.println();
//        }
        return packets;
    }
    public record Packet(
            List<Object> data,
            Packet parent
    ) {
        @Override
        public String toString() {
            return "[" + data.stream().map(Object::toString).collect(Collectors.joining(",")) + "]";
        }
    }
}
