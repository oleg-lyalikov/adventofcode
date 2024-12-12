package org.lialikov.adventofcode.adv2024;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;

public class Adv2024Day9 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 1928
        System.out.println(getPart1("2024/2024-12-09-sample.txt"));
        // 6337367222422
        System.out.println(getPart1("2024/2024-12-09.txt"));
        // 2858
        System.out.println(getPart2("2024/2024-12-09-sample.txt"));
        // 6361380647183
        System.out.println(getPart2("2024/2024-12-09.txt"));

        System.out.println(new Date());
    }

    private static List<Integer> readInput(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        if (lines.size() != 1) {
            throw new IllegalStateException("Unexpected lines count: " + lines.size());
        }
        String line = lines.get(0);
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            numbers.add(line.charAt(i) - '0');
        }
        return numbers;
    }

    private static int getFileId(int index) {
        return index / 2;
    }

    private static long getPart1(String inputFile) {
        List<Integer> input = readInput(inputFile);
        long res = 0;
        int leftI = 0;
        int leftN = input.get(leftI);
        int leftFileId = getFileId(leftI);
        int position = 0;
        int rightI = input.size() - 1;
        if (rightI % 2 != 0) {
            rightI--;
        }
        int rightN = input.get(rightI);
        int rightFileId = getFileId(rightI);
        //noinspection ConditionalBreakInInfiniteLoop
        while (true) {
            if (leftFileId == rightFileId && leftN == 0) {
                break;
            }
            while (rightN == 0) {
                rightI -= 2;
                if (rightI == leftI) {
                    if (leftN == 0) {
                        break;
                    }
                    rightN = leftN;
                } else {
                    rightN = input.get(rightI);
                }
                rightFileId = getFileId(rightI);
            }
            while (leftN == 0) {
                leftI++;
                if (leftI == rightI) {
                    if (rightN == 0) {
                        break;
                    }
                    leftN = rightN;
                } else {
                    leftN = input.get(leftI);
                }
                leftFileId = getFileId(leftI);
            }
            if (leftI % 2 == 0) {
                res += (long) leftFileId * position;
            } else {
                res += (long) rightFileId * position;
                rightN--;
            }
            leftN--;
            position++;
        }
        return res;
    }

    private static long getPart2(String inputFile) {
        List<Integer> input = readInput(inputFile);

        List<SpaceBlock> data = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            final SpaceType type = i % 2 == 0 ? SpaceType.FILE : SpaceType.SPACE;
            data.add(new SpaceBlock(type, input.get(i), i / 2));
        }
        int fileI = data.get(data.size() - 1).type == SpaceType.FILE ? data.size() - 1 : data.size() - 2;
        Set<Integer> visited = new HashSet<>();
        while (fileI > 0) {
            SpaceBlock fileBlock = data.get(fileI);
            if (fileBlock.type != SpaceType.FILE) {
                fileI--;
                continue;
            }
            if (visited.contains(fileBlock.n)) {
                fileI--;
                continue;
            }
            visited.add(fileBlock.n);
            for (int i = 0; i < fileI; i++) {
                SpaceBlock sb = data.get(i);
                if (sb.type != SpaceType.SPACE) {
                    continue;
                }
                if (fileBlock.size <= sb.size) {
                    data.set(fileI, new SpaceBlock(SpaceType.SPACE, fileBlock.size, 0));
                    data.add(i, fileBlock);
                    if (fileBlock.size == sb.size) {
                        data.remove(i + 1);
                    } else {
                        data.set(i + 1, new SpaceBlock(SpaceType.SPACE, sb.size - fileBlock.size, 0));
                        fileI++;
                    }
                    break;
                }
            }
            fileI--;
        }
        return getChecksum(data);
    }

    private static long getChecksum(List<SpaceBlock> data) {
        long position = 0;
        long res = 0;
        for (SpaceBlock block : data) {
            for (int j = 0; j < block.size; j++) {
                if (block.type == SpaceType.FILE) {
                    res += position * block.n;
                }
                position++;
            }
        }
        return res;
    }

    public enum SpaceType {
        FILE,
        SPACE
    }
    public record SpaceBlock(SpaceType type, int size, int n) { }
}
