package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Adv2023Day14 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 136
        //System.out.println(getPart1("2023/2023-12-14-sample.txt"));
        // 110565
        //System.out.println(getPart1("2023/2023-12-14.txt"));
        // 64
        //System.out.println(getPart2("2023/2023-12-14-sample.txt"));
        // 89845, cycle every 42, first number at 118 cycle
        System.out.println(getPart2("2023/2023-12-14.txt"));

        System.out.println(new Date());
    }

    public static long getPart2(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        int[][] data = new int[lines.size()][lines.get(0).length()];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                char ch = lines.get(i).charAt(j);
                data[i][j] = ch == '.' ?
                        0 :
                        (ch == 'O' ? 1 : 2);
            }
        }
        print(data);
        for (int i = 0; i < 1000; i++) {
            fullRoll(data);
//            if (i % 1000 == 0) {
//                System.out.println(count(data));
//            }
            System.out.println((i + 1) + ": " + count(data));
        }

        return count(data);
    }

    private static long count(int[][] data) {
        long res = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                if (data[i][j] == 1) {
                    res += (data.length - i);
                }
            }
        }
        return res;
    }

    private static void fullRoll(int[][] data) {
        roll(data, Direction.N);
        roll(data, Direction.W);
        roll(data, Direction.S);
        roll(data, Direction.E);
    }

    private static void roll(int[][] data, Direction direction) {
        if (direction == Direction.N) {
            for (int j = 0; j < data[0].length; j++) {
                int next = 0;
                for (int i = 0; i < data.length; i++) {
                    int d = data[i][j];
                    if (d == 2) {
                        next = i + 1;
                    } else if (d == 1) {
                        if (i != next) {
                            int temp = data[next][j];
                            data[next][j] = 1;
                            data[i][j] = temp;
                        }
                        next++;
                    }
                }
            }
        } else if (direction == Direction.W) {
            for (int i = 0; i < data.length; i++) {
                int next = 0;
                for (int j = 0; j < data[0].length; j++) {
                    int d = data[i][j];
                    if (d == 2) {
                        next = j + 1;
                    } else if (d == 1) {
                        if (j != next) {
                            int temp = data[i][next];
                            data[i][next] = 1;
                            data[i][j] = temp;
                        }
                        next++;
                    }
                }
            }
        } else if (direction == Direction.S) {
            for (int j = 0; j < data[0].length; j++) {
                int next = data.length - 1;
                for (int i = data.length - 1; i >= 0; i--) {
                    int d = data[i][j];
                    if (d == 2) {
                        next = i - 1;
                    } else if (d == 1) {
                        if (i != next) {
                            int temp = data[next][j];
                            data[next][j] = 1;
                            data[i][j] = temp;
                        }
                        next--;
                    }
                }
            }
        } else if (direction == Direction.E) {
            for (int i = 0; i < data.length; i++) {
                int next = data[0].length - 1;
                for (int j = data[0].length - 1; j >= 0; j--) {
                    int d = data[i][j];
                    if (d == 2) {
                        next = j - 1;
                    } else if (d == 1) {
                        if (j != next) {
                            int temp = data[i][next];
                            data[i][next] = 1;
                            data[i][j] = temp;
                        }
                        next--;
                    }
                }
            }
        }
    }

    private static void print(int[][] data) {
        for (int[] datum : data) {
            for (int j = 0; j < data[0].length; j++) {
                System.out.print(
                        datum[j] == 0 ?
                                '.' :
                                (datum[j] == 1 ? 'O' : '#')
                );
            }
            System.out.println();
        }
        System.out.println();
    }

    public static long getPart1(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        long res = 0;
        for (int x = 0; x < lines.get(0).length(); x++) {
            long next = 0;
            for (int y = 0; y < lines.size(); y++) {
                char ch = lines.get(y).charAt(x);
                if (ch == '#') {
                    next = y + 1;
                } else if (ch == 'O') {
                    res += (lines.size() - next);
                    next++;
                }
            }
        }
        return res;
    }

    enum Direction {
        N,
        W,
        S,
        E
    }
}
