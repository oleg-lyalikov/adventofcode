package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Adv2023Day2 {

    public static void main(String[] args) {
        System.out.println(getIDsSum("2023/2023-12-02-sample.txt"));
        System.out.println(getIDsSum("2023/2023-12-02.txt"));
        System.out.println(getPowerSum("2023/2023-12-02-sample.txt"));
        System.out.println(getPowerSum("2023/2023-12-02.txt"));
    }

    private static final int RED_MAX = 12;
    private static final int GREEN_MAX = 13;
    private static final int BLUE_MAX = 14;

    public static long getIDsSum(String inputFile) {
        AtomicLong res = new AtomicLong(0);
        Pattern cubePattern = Pattern.compile("\\s*(\\d+)\\s+(green|blue|red)");
        FileUtil.read(inputFile, s -> {
            int startIndex = s.indexOf(":");
            int id = Integer.parseInt(s.substring(5, startIndex));
            String data = s.substring(startIndex + 1);
            String[] plays = data.split(";");
            boolean possible = true;
            for (String play : plays) {
                String[] cubes = play.split(",");
                for (String cube : cubes) {
                    Matcher m = cubePattern.matcher(cube);
                    if (!m.matches()) {
                        throw new IllegalStateException(cube);
                    }
                    int number = Integer.parseInt(m.group(1));
                    String name = m.group(2);
                    if ("blue".equalsIgnoreCase(name) && number > BLUE_MAX) {
                        possible = false;
                    } else if ("red".equalsIgnoreCase(name) && number > RED_MAX) {
                        possible = false;
                    } else if ("green".equalsIgnoreCase(name) && number > GREEN_MAX) {
                        possible = false;
                    }
                }
            }
            if (possible) {
                res.addAndGet(id);
            }
        });
        return res.get();
    }

    public static long getPowerSum(String inputFile) {
        AtomicLong res = new AtomicLong(0);
        Pattern cubePattern = Pattern.compile("\\s*(\\d+)\\s+(green|blue|red)");
        FileUtil.read(inputFile, s -> {
            int startIndex = s.indexOf(":");
            String data = s.substring(startIndex + 1);
            String[] plays = data.split(";");
            int blue = 0;
            int red = 0;
            int green = 0;
            for (String play : plays) {
                String[] cubes = play.split(",");
                for (String cube : cubes) {
                    Matcher m = cubePattern.matcher(cube);
                    if (!m.matches()) {
                        throw new IllegalStateException(cube);
                    }
                    int number = Integer.parseInt(m.group(1));
                    String name = m.group(2);
                    if ("blue".equalsIgnoreCase(name) && number > blue) {
                        blue = number;
                    } else if ("red".equalsIgnoreCase(name) && number > red) {
                        red = number;
                    } else if ("green".equalsIgnoreCase(name) && number > green) {
                        green = number;
                    }
                }
            }
            long power = (long) blue * red * green;
            //System.out.println(power);
            res.addAndGet(power);
        });
        return res.get();
    }
}
