package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Adv2022Day8 {

    public static void main(String[] args) {
        System.out.println(getVisibleTrees("2022/2022-12-08-sample.txt"));
        System.out.println(getVisibleTrees("2022/2022-12-08.txt"));
        System.out.println(getTreeTopScore("2022/2022-12-08-sample.txt"));
        System.out.println(getTreeTopScore("2022/2022-12-08.txt"));
    }

    private static List<int[]> read(String file) {
        List<int[]> data = new ArrayList<>();
        FileUtil.read(file, l -> {
            int[] line = new int[l.length()];
            for (int i = 0 ; i < l.length(); i++) {
                int tree = Integer.parseInt(l.charAt(i) + "");
                line[i] = tree;
            }
            data.add(line);
        });

        int length = data.get(0).length;
        for (int[] line : data) {
            if (line.length != length) {
                throw new IllegalStateException("Unexpected length: " + Arrays.toString(line));
            }
        }

        return data;
    }

    private static long getVisibleTrees(String file) {
        List<int[]> data = read(file);

        int length = data.get(0).length;
        long res = 0;
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < length; j++) {
                if (isVisible(data, i, j)) {
                    res++;
                }
            }
        }
        return res;
    }

    private static boolean isVisible(List<int[]> data, int i, int j) {
        final int height = data.get(i)[j];

        boolean isVisible = true;
        for (int l = 0; l < j; l++) {
            if (data.get(i)[l] >= height) {
                isVisible = false;
                break;
            }
        }
        if (isVisible) {
            return true;
        }

        isVisible = true;
        for (int l = j + 1; l < data.get(i).length; l++) {
            if (data.get(i)[l] >= height) {
                isVisible = false;
                break;
            }
        }
        if (isVisible) {
            return true;
        }

        isVisible = true;
        for (int l = 0; l < i; l++) {
            if (data.get(l)[j] >= height) {
                isVisible = false;
                break;
            }
        }
        if (isVisible) {
            return true;
        }

        isVisible = true;
        for (int l = i + 1; l < data.size(); l++) {
            if (data.get(l)[j] >= height) {
                isVisible = false;
                break;
            }
        }
        return isVisible;
    }

    private static long getTreeTopScore(String file) {
        List<int[]> data = read(file);

        int length = data.get(0).length;
        long res = 0;
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < length; j++) {
                long treeScore = getTreeScore(data, i, j);
                if (treeScore > res) {
                    res = treeScore;
                }
            }
        }
        return res;
    }

    private static long getTreeScore(List<int[]> data, int i, int j) {
        final int height = data.get(i)[j];

        long left = 0;
        for (int l = j - 1; l >= 0; l--) {
            left++;
            if (data.get(i)[l] >= height) {
                break;
            }
        }

        long right = 0;
        for (int l = j + 1; l < data.get(i).length; l++) {
            right++;
            if (data.get(i)[l] >= height) {
                break;
            }
        }

        long top = 0;
        for (int l = i - 1; l >= 0; l--) {
            top++;
            if (data.get(l)[j] >= height) {
                break;
            }
        }

        long bottom = 0;
        for (int l = i + 1; l < data.size(); l++) {
            bottom++;
            if (data.get(l)[j] >= height) {
                break;
            }
        }

        return left * right * top * bottom;
    }
}
