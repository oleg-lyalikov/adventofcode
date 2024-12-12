package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Adv2023Day13 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 405
        System.out.println(getPart1("2023/2023-12-13-sample.txt"));
        // 30705
        System.out.println(getPart1("2023/2023-12-13.txt"));
        // 400
        System.out.println(getPart2("2023/2023-12-13-sample.txt"));
        // 44615
        System.out.println(getPart2("2023/2023-12-13.txt"));

        System.out.println(new Date());
    }

    public static long getPart2(String inputFile) {
        List<String> all = FileUtil.readLines(inputFile);
        all.add("");
        List<String> current = new ArrayList<>();
        long res = 0;
        int index = 0;
        for (String line: all) {
            if (StringUtils.hasText(line)) {
                current.add(line);
            } else {
                index++;
                int xSize = current.get(0).length();
                int xFound = -1;
                for (int x = 0; x < xSize - 1; x++) {
                    if (isVerticalMirror2(current, x)) {
                        xFound = x;
                        break;
                    }
                }

                if (xFound != -1) {
                    res += xFound + 1;
                } else {
                    int yFound = -1;
                    for (int y = 0; y < current.size() - 1; y++) {
                        if (isHorizontalMirror2(current, y)) {
                            yFound = y;
                            break;
                        }
                    }
                    if (yFound == -1) {
                        throw new IllegalStateException("!!!\n" + index);
                    }
                    res += ((yFound + 1) * 100L);
                }

                current = new ArrayList<>();
            }
        }
        return res;
    }

    public static boolean isVerticalMirror2(List<String> lines, int x) {
        int maxSize = Math.min(x + 1, lines.get(0).length() - x - 1);
        int differs = 0;
        for (int i = 0; i < maxSize; i++) {
            int x1 = x - i;
            int x2 = x + i + 1;
            for (String s : lines) {
                if (s.charAt(x1) != s.charAt(x2)) {
                    differs++;
                    if (differs > 1) {
                        break;
                    }
                }
            }
            if (differs > 1) {
                return false;
            }
        }
        return differs == 1;
    }

    public static boolean isHorizontalMirror2(List<String> lines, int y) {
        int maxSize = Math.min(y + 1, lines.size() - y - 1);
        int differs = 0;
        for (int i = 0; i < maxSize; i++) {
            int y1 = y - i;
            int y2 = y + i + 1;
            for (int x = 0; x < lines.get(0).length(); x++) {
                if (lines.get(y1).charAt(x) != lines.get(y2).charAt(x)) {
                    differs++;
                    if (differs > 1) {
                        break;
                    }
                }
            }
            if (differs > 1) {
                break;
            }
        }
        return differs == 1;
    }

    public static long getPart1(String inputFile) {
        List<String> all = FileUtil.readLines(inputFile);
        all.add("");
        List<String> current = new ArrayList<>();
        long res = 0;
        int index = 0;
        for (String line: all) {
            if (StringUtils.hasText(line)) {
                current.add(line);
            } else {
                index++;
                int xSize = current.get(0).length();
                int xFound = -1;
                for (int x = 0; x < xSize - 1; x++) {
                    if (isVerticalMirror(current, x)) {
                        xFound = x;
                        break;
                    }
                }

                if (xFound != -1) {
                    res += xFound + 1;
                } else {
                    int yFound = -1;
                    for (int y = 0; y < current.size() - 1; y++) {
                        if (isHorizontalMirror(current, y)) {
                            yFound = y;
                            break;
                        }
                    }
                    if (yFound == -1) {
                        throw new IllegalStateException("!!!\n" + index);
                    }
                    res += ((yFound + 1) * 100L);
                }

                current = new ArrayList<>();
            }
        }
        return res;
    }

    public static boolean isVerticalMirror(List<String> lines, int x) {
        int maxSize = Math.min(x + 1, lines.get(0).length() - x - 1);
        boolean differ = false;
        for (int i = 0; i < maxSize; i++) {
            int x1 = x - i;
            int x2 = x + i + 1;
            for (String s : lines) {
                if (s.charAt(x1) != s.charAt(x2)) {
                    differ = true;
                    break;
                }
            }
            if (differ) {
                return false;
            }
        }
        return true;
    }

    public static boolean isHorizontalMirror(List<String> lines, int y) {
        int maxSize = Math.min(y + 1, lines.size() - y - 1);
        boolean differ = false;
        for (int i = 0; i < maxSize; i++) {
            int y1 = y - i;
            int y2 = y + i + 1;
            for (int x = 0; x < lines.get(0).length(); x++) {
                if (lines.get(y1).charAt(x) != lines.get(y2).charAt(x)) {
                    differ = true;
                    break;
                }
            }
            if (differ) {
                return false;
            }
        }
        return true;
    }
}
