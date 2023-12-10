package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Adv2023Day8 {

    public static void main(String[] args) {
        // 2
        //System.out.println(getStepsToZZZ("2023/2023-12-08-sample1.txt"));
        // 6
        //System.out.println(getStepsToZZZ("2023/2023-12-08-sample2.txt"));
        // 12643
        //System.out.println(getStepsToZZZ("2023/2023-12-08.txt"));
        // 6
        //System.out.println(getStepsToAllZ("2023/2023-12-08-sample3.txt"));
        System.out.println(new Date());
        // 13133452426987
        System.out.println(getStepsToAllZ("2023/2023-12-08.txt"));
        System.out.println(new Date());
    }

    public static long getStepsToZZZ(String inputFile) {
        Map<String, Node> map = new HashMap<>();
        AtomicReference<Node> first = new AtomicReference<>();
        AtomicReference<String> route = new AtomicReference<>();
        FileUtil.read(inputFile, s -> {
            if (!StringUtils.hasText(s)) {
                return;
            }
            if (route.get() == null) {
                route.set(s);
                return;
            }

            String name = s.substring(0, 3);
            String[] lr = s.substring(s.indexOf("(") + 1, s.indexOf(")")).split(", ");
            Node node = new Node();
            node.name = name;
            node.left = lr[0];
            node.right = lr[1];
            map.put(name, node);
            if (first.get() == null) {
                first.set(node);
            }
        });

        int i = 0;
        long res = 0;
        Node node = map.get("AAA");
        long globalI = 0;
        String routeValue = route.get();
        while (true) {
            res++;
            char ch = routeValue.charAt(i);
            if (ch == 'L') {
                node = map.get(node.left);
            } else if (ch == 'R') {
                node = map.get(node.right);
            }
            if (node.name.equals("ZZZ")) {
                break;
            }

            if (i + 1 == routeValue.length()) {
                i = 0;
            } else {
                i++;
            }
            globalI++;
            if (globalI % 100000000 == 0) {
                System.out.println("Global i: " + globalI);
            }
        }
        return res;
    }

    public static long getStepsToAllZ(String inputFile) {
        Map<String, Node> map = new HashMap<>();
        AtomicReference<Node> first = new AtomicReference<>();
        AtomicReference<String> route = new AtomicReference<>();
        FileUtil.read(inputFile, s -> {
            if (!StringUtils.hasText(s)) {
                return;
            }
            if (route.get() == null) {
                route.set(s);
                return;
            }

            String name = s.substring(0, 3);
            String[] lr = s.substring(s.indexOf("(") + 1, s.indexOf(")")).split(", ");
            Node node = new Node();
            node.name = name;
            node.left = lr[0];
            node.right = lr[1];
            map.put(name, node);
            if (first.get() == null) {
                first.set(node);
            }
        });

        int i = 0;
        long res = 0;
        String routeValue = route.get();
        int length = routeValue.length();
        System.out.println("Route length: " + length);
        List<Node> nodes = map.values().stream().filter(n -> n.name.endsWith("A")).toList();
        Node[] array = nodes.toArray(new Node[0]);
        while (true) {
            res++;
            char ch = routeValue.charAt(i);
            if (ch == 'L') {
                //nodes = nodes.stream().map(n -> map.get(n.left)).toList();
                for (int j = 0; j < array.length; j++) {
                    array[j] = map.get(array[j].left);
                }
            } else if (ch == 'R') {
                //nodes = nodes.stream().map(n -> map.get(n.right)).toList();
                for (int j = 0; j < array.length; j++) {
                    array[j] = map.get(array[j].right);
                }
            }
            int check = 5;
            if (array[check].name.endsWith("Z")) {
                System.out.println("Z iteration: " + res);
            }
            boolean allMatch = true;
            for (Node value : array) {
                if (value.name.charAt(value.name.length() - 1) != 'Z') {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                break;
            }
//            if (nodes.stream().allMatch(n -> n.name.endsWith("Z"))) {
//                break;
//            }

            if (i + 1 == routeValue.length()) {
                i = 0;
            } else {
                i++;
            }

            if (res % 100000000 == 0) {
                System.out.println("res: " + res);
            }
        }
        // 19099 = 71 * 269
        // 12643 = 47 * 269
        // 11567 = 43 * 269
        // 21251 = 79 * 269
        // 15871 = 59 * 269
        // 19637 = 73 * 269
        return res;
    }

     private static class Node {
        String name;
        String left;
        String right;
     }
}
