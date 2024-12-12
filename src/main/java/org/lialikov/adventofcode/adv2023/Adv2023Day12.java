package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Adv2023Day12 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 21
        //System.out.println(getArrangementsSum("2023/2023-12-12-sample.txt"));
        //System.out.println(getArrangements("?###???????? 3,2,1"));

        // 7734. That's not the right answer; your answer is too high.
        // 7670
        //System.out.println(getArrangementsSum("2023/2023-12-12.txt"));

        // 525152
        //System.out.println(getArrangementsSum2("2023/2023-12-12-sample.txt"));
        // 13416031002222  .That's not the right answer; your answer is too low.
        //System.out.println(getArrangementsSum2("2023/2023-12-12.txt"));
        getArrangements2("???#.?????#??? 1,1,1,5");
        getArrangements2("???#.?????#????? 1,1,1,5");
        getArrangements2("???#.?????#?????? 1,1,1,5");
        // 16384
        //System.out.println(getArrangements2("?###???????? 3,2,1"));

        // 1
        //System.out.println(getArrangements(".?..??#?##????#...? 4,1"));
        // 3
        //System.out.println(getArrangements("...????..??????.# 2,2,3,1"));
        // 24
        //System.out.println(getArrangements("???????#???.?## 1,1,1,1,3"));
        // 3
        //System.out.println(getArrangements("?.#???#????#???#. 2,1,4,1"));
        // 1
        //System.out.println(getArrangements("???????##????????. 1,2,7,1,2"));
        // 13
        //System.out.println(getArrangements("?.???????.????. 5,1,1"));
        // 5
        //System.out.println(getArrangements("??.??#?#?. 1,4"));
        // 15
        //System.out.println(getArrangements("??.???????????.? 4,2"));
        // 7
        //System.out.println(getArrangements("??#.?????#??? 1,1,1,5"));
        // 1
        //System.out.println(getArrangements("??#..#??#? 2,4"));
        // 3
        //System.out.println(getArrangements("??.##?#.##???#??? 4,2,2,1"));
        // 3
        //System.out.println(getArrangements("???.?##?#.?.? 1,5,1,1"));
        // 2
        //System.out.println(getArrangements("?#???#????.#??.#?#. 1,6,3,1,1"));
        ////////////////////////////////
        // 252
        //System.out.println(getArrangements("????????????????.# 1,3,1,1,1,1"));
        // 252
        //System.out.println(getArrangements("??????????????? 1,1,1,2,1"));

        // 3
        //System.out.println(getArrangements("????.#??..????#??. 3,3"));

        // 125?
        //System.out.println(getArrangements("???????.?????????? 1,3,1,1,1,2"));

        System.out.println(new Date());
    }

    private static long getArrangementsSum2(String fileName) {
        return getArrangementsSum(fileName, false);
    }

    private static long getArrangementsSum(String fileName) {
        return getArrangementsSum(fileName, true);
    }

    private static long getArrangementsSum(String fileName, boolean part1) {
        TreeMap<Long, List<String>> inputs = new TreeMap<>();
        //AtomicLong res = new AtomicLong(244);
        //AtomicInteger toSkip = new AtomicInteger(2);
        AtomicLong res = new AtomicLong(0);
        AtomicInteger toSkip = new AtomicInteger(8);
        AtomicInteger counter = new AtomicInteger(1);
        FileUtil.read(fileName, s -> {
            if (toSkip.get() > 0) {
                toSkip.decrementAndGet();
                counter.incrementAndGet();
                return;
            }
            long arrangements = getArrangements(s, part1, counter.get());
            res.addAndGet(arrangements);
            System.out.println("Line # " + counter.get() + ", value: " + arrangements + ", total: " + res.get());
            inputs.computeIfAbsent(arrangements, a -> new ArrayList<>());
            inputs.get(arrangements).add(s);
            counter.incrementAndGet();
        });
        return res.get();
    }

    private static long getArrangements2(String s) {
        return getArrangements(s, false, 1);
    }

    private static long getArrangements(String s, boolean part1, int counter) {
        String[] parts = s.split("\\s+");
        String status = parts[0];
        List<Integer> groups = Arrays.stream(parts[1].split(","))
                .map(Integer::parseInt)
                .toList();
        if (part1) {
            return bruteForce(groups, status, counter).count();
        } else {
            // TODO: temp, remove
            //status = "?" + status;

            System.out.println("Old:");
            Stats p1 = bruteForce(groups, status, counter);
            System.out.println(p1);

            System.out.println("With appended single '?':");
            Stats withQuestionP = bruteForce(groups, "?" + status, counter);
//            System.out.println(withQuestionP);

            System.out.println("With appended single '?' AT THE END:");
            Stats withQuestionAtTheEndP = bruteForce(groups, status + "?", counter);
//            System.out.println(withQuestionAtTheEndP);

            long lastRes = 0;
            for (int total = 2; total <= 2; total++) {
                System.out.println("Total: " + total);
                long estimate = p1.count();
                if (status.charAt(status.length() - 1) == '#') {
                    for (int i = 0; i < total - 1; i++) {
                        estimate *= estimate;
                    }
                    //estimate = estimate * estimate * estimate * estimate * estimate;
                } else if (status.charAt(0) == '.' || status.charAt(status.length() - 1) == '.') {
                    long withQuestion = withQuestionP.count();
                    for (int i = 0; i < total - 1; i++) {
                        estimate *= withQuestion;
                    }
                    //estimate = estimate * withQuestion * withQuestion * withQuestion * withQuestion;
                } else {
                    long withQuestionAtTheEnd = withQuestionAtTheEndP.count();
                    for (int i = 0; i < total - 1; i++) {
                        estimate *= withQuestionAtTheEnd;
                    }
                    //estimate = estimate * withQuestionAtTheEnd * withQuestionAtTheEnd * withQuestionAtTheEnd * withQuestionAtTheEnd;
                }

                // 159618 correct and 268912 incorrect
                System.out.println("New: (estimate: "  + estimate + ")");
//            if (estimate > 500000) {
//                return 0;
//            }

//            if (p1.getFirst() != withQuestion && p1.getFirst() != withQuestionAtTheEnd && withQuestion != withQuestionAtTheEnd) {
//                long res = bruteForce2(groups, status, counter);
//                System.out.println("Estimate: " + estimate + ", match: " + (estimate == res));
//                return res;
//            } else {
//                return estimate;
//            }

                Stats stats = bruteForce2(groups, status, total, counter);
                System.out.println("Estimate: " + estimate + ", match: " + (estimate == stats.count()) + ", stats: " + stats);
                if (estimate != stats.count()) {
                    //throw new IllegalStateException("!!!");
                }
                lastRes = stats.count();
                //return res;
                //return estimate;
            }
            return lastRes;
        }
    }
    private static Stats bruteForce2(List<Integer> groups, String status, int total, int counter) {
        List<String> newSt = new ArrayList<>();
        List<Integer> newGroups = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            newSt.add(status);
            newGroups.addAll(groups);
        }
        //String newStatus = String.join("?", newSt);
        // TODO: temp
        String newStatus = String.join("", newSt);
        return bruteForce(newGroups, newStatus, counter);
    }

    private static String replaceStartDots(String line) {
        while (line.charAt(0) == '.') {
            line = line.substring(1);
        }
        return line;
    }

    private static String replaceEndDots(String line) {
        while (line.charAt(line.length() - 1) == '.') {
            line = line.substring(0, line.length() - 1);
        }
        return line;
    }

    private static String simplify(String line, List<Integer> groups) {
        String original = line;
        System.out.println(line);
        line = line.replaceAll("\\.+", ".");
        line = replaceStartDots(line);
        line = replaceEndDots(line);

//        for (int i = 0; i < line.length(); i++) {
//            if (i == 0 && line.charAt(0) == '.') {
//                line = line.substring(1);
//                i = -1;
//                continue;
//            }
//            if (line.charAt(i) == '?') {
//                String trySharp = StringUtil.replaceAt(line, i, '#');
//                if (!hasValid(trySharp, groups)) {
//                    if (i == 0) {
//                        line = line.substring(1);
//                        i = -1;
//                    } else {
//                        line = StringUtil.replaceAt(line, i, '.');
//                    }
//                } else if (!hasValid(StringUtil.replaceAt(line, i, '.'), groups)) {
//                    line = StringUtil.replaceAt(line, i, '#');
//                }
//            }
//        }

        int maxLength = groups.stream().mapToInt(i -> i).max().orElseThrow();
        String toFind = "#".repeat(maxLength);
        int index = line.indexOf(toFind);
        while (index >= 0) {
            String start = index > 0 ? line.substring(0, index - 1) + '.' : "";
            int endBeginIndex = index + maxLength + 1;
            String end = endBeginIndex < line.length() ? ('.' + line.substring(endBeginIndex)) : "";
            line = start + toFind + end;
            index = line.indexOf(toFind, index + 1);
        }

        int firstLength = groups.get(0);
        int count = 0;
        int i = 0;
        while (true) {
            if (line.charAt(i) == '?' || line.charAt(i) == '#') {
                count++;
            } else if (count != firstLength) {
                line = replaceStartDots(line.substring(i));
                count = 0;
                i = 0;
                continue;
            }
            if (count >= firstLength) {
                break;
            }
            i++;
        }

        int first = groups.get(0);
        i = 0;
        while (true) {
            if (line.charAt(i + first) == '#') {
                i++;
            } else {
                break;
            }
        }
        if (i > 0) {
            line = line.substring(i);
        }
        if (line.charAt(0) == '#') {
            line = "#".repeat(first) + "." + line.substring(first + 1);
        }


        int lastLength = groups.get(groups.size() - 1);
        count = 0;
        i = line.length() - 1;
        while (true) {
            if (line.charAt(i) == '?' || line.charAt(i) == '#') {
                count++;
            } else if (count != lastLength) {
                line = replaceEndDots(line.substring(0, i + 1));
                count = 0;
                i = line.length() - 1;
                continue;
            }
            if (count >= lastLength) {
                break;
            }
            i--;
        }

        i = line.length() - 1;
        while (true) {
            if (line.charAt(i - lastLength) == '#') {
                i--;
            } else {
                break;
            }
        }
        if (i < line.length() - 1) {
            line = line.substring(0, i + 1);
        }
        if (line.charAt(line.length() - 1) == '#') {
            line = line.substring(0, line.length() - lastLength - 1) + '.' + "#".repeat(lastLength);
        }

        return original;
        //return line;
    }

    private static boolean hasValid(String line, List<Integer> groups) {
        int[] min = getMin(groups, line);
        return min != null;
    }

    private static Stats bruteForce(List<Integer> groups, String status, int counter) {
        status = simplify(status, groups);
        System.out.println(status + " --- " + groups);

//        List<Integer> questions = new ArrayList<>();
//        for (int i = 0; i < status.length(); i++) {
//            if (status.charAt(i) == '?') {
//                questions.add(i);
//            }
//        }

        int[] startMin = getMin(groups, status);
        if (startMin == null || !isValid(status, startMin, groups)) {
            throw new IllegalStateException(status);
        }

        //int[] start = getAnyStart(groups);
        int[] start = startMin;
        long res = 1;
        long endSharps = endsWithSharp(start, groups, status) ? 1 : 0;
        long startSharps = startsWithSharp(start) ? 1 : 0;
        long startAndEndSharps = (endSharps == 1 && startSharps == 1) ? 1 : 0;
        //System.out.println(toString(status, start, groups) + "        - " + res);
//        if (isValid(status, start, groups)) {
//            res++;
//        }

        int[] toTry = Arrays.copyOf(start, start.length);
        Stats p = iterate(start, toTry, groups, status, null);
        res += p.count();
        endSharps += p.sharpAtEnd();
        startSharps += p.sharpAtStart();
        startAndEndSharps += p.sharpAtEndAndStart();
        System.out.println(res + " -------- line # " + counter);
        return new Stats(res, endSharps, startSharps, startAndEndSharps);
    }

    private static boolean endsWithSharp(int[] toTry, List<Integer> groups, String status) {
        return toTry[toTry.length - 1] + groups.get(groups.size() - 1) == status.length();
    }

    private static boolean startsWithSharp(int[] toTry) {
        return toTry[0] == 0;
    }

    private static Stats iterate(int[] start, int[] toTry, List<Integer> groups, String status, Integer maxIterations) {
        long res = 0;
        long endSharp = 0;
        long startSharp = 0;
        long startAndEndSharp = 0;
        while (true) {
            int i = toTry.length - 1;
            toTry[i]++;
            boolean end = false;
            while ((toTry[toTry.length - 1] + groups.get(groups.size() - 1)) > status.length()) {
                i--;
                if (i < 0) {
                    end = true;
                    break;
                }
                toTry[i]++;
                if (toTry[i] >= status.length()) {
                    if (res == 0) {
                        return new Stats(0L, 0L, 0L, 0L);
                    } else {
                        throw new IllegalStateException("!!!!");
                    }
                }
                boolean failed = false;
                while (status.length() >= toTry[i] && status.charAt(toTry[i]) == '.') {
                    toTry[i]++;
                    if (toTry[i] >= status.length()) {
                        failed = true;
                        break;
                    }
                }
                if (failed) {
                    // TODO: buhoj pisal
                    continue;
                }
                for (int j = i + 1; j < groups.size(); j++) {
                    toTry[j] = Math.max(start[j], toTry[j - 1] + groups.get(j - 1) + 1);
                }
            }
            if (end) {
                return new Stats(res, endSharp, startSharp, startAndEndSharp);
            }
            if (isValid(status, toTry, groups)) {
                res++;
                System.out.println(toString(status, toTry, groups));
                boolean endsWithSharp = endsWithSharp(toTry, groups, status);
                if (endsWithSharp) {
                    endSharp++;
                }
                boolean startsWithSharp = startsWithSharp(toTry);
                if (startsWithSharp) {
                    startSharp++;
                }
                if (startsWithSharp && endsWithSharp) {
                    startAndEndSharp++;
                }
                if (maxIterations != null && res == maxIterations) {
                    return new Stats(res, endSharp, startSharp, startAndEndSharp);
                }
                //System.out.println(toString(status, toTry, groups) + "        - " + res);
            }
        }
    }
    private static String toString(String line, int[] toTry, List<Integer> groups) {
        char[] res = new char[line.length()];
        for (int i = 0; i < toTry[0]; i++) {
            res[i] = line.charAt(i);
        }
        for (int i = 0; i < toTry.length; i++) {
            for (int lineI = toTry[i]; lineI < toTry[i] + groups.get(i); lineI++) {
                res[lineI] = '#';
            }
            int maxJ = (i == toTry.length - 1) ? line.length() : toTry[i + 1];
            for (int j = toTry[i] + groups.get(i); j < maxJ; j++) {
                res[j] = line.charAt(j);
            }
        }
        return new String(res);
    }

    private static boolean isValid(String line, int[] toTry, List<Integer> groups) {
        for (int i = 0; i < toTry[0]; i++) {
            if (line.charAt(i) == '#') {
                return false;
            }
        }
        for (int i = 0; i < toTry.length; i++) {
            int lineI = toTry[i];
            for (int j = lineI; j < lineI + groups.get(i); j++) {
                if (j >= line.length() || line.charAt(j) == '.') {
                    return false;
                }
            }
            int maxJ = (i == toTry.length - 1) ? line.length() : toTry[i + 1];
            for (int j = lineI + groups.get(i); j < maxJ; j++) {
                if (line.charAt(j) == '#') {
                    return false;
                }
            }
        }
        return true;
    }

    private static int sumLength(int[] groups) {
        int res = 0;
        for (int i = 0; i < groups.length; i++) {
            res += (groups.length + 1);
        }
        return res;
    }

    private static int[] getAnyStart(List<Integer> groups, int first) {
        int[] start = new int[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            if (i == 0) {
                start[i] = first;
            } else {
                start[i] = start[i - 1] + groups.get(i - 1) + 1;
            }
        }
        return start;
    }

    private static int[] getMin(List<Integer> groups, String s) {
        int[] res = getAnyStart(groups, 0);
        if (isValid(s, res, groups)) {
            return res;
        }
        long matched = iterate(Arrays.copyOf(res, res.length), res, groups, s, 1).count();
        return matched > 0 ? res : null;
//        int i = 0;
//        int length = groups.get(i);
//        int currentLength = 0;
//        boolean needBreak = false;
//        for (int j = 0; j < s.length(); j++) {
//            if (currentLength == length) {
//                if (s.charAt(j) == '#' || (i == groups.size() - 1 && s.indexOf('#', j) != - 1)) {
//                    j = res[i] + 1;
//                    currentLength = 0;
//                    needBreak = false;
//                } else {
//                    i++;
//                    if (i == groups.size()) {
//                        break;
//                    }
//                    length = groups.get(i);
//                    needBreak = true;
//                    currentLength = 0;
//                }
//            }
//            if (s.charAt(j) == '.') {
//                if (currentLength > 0 && currentLength != length) {
//                    return null;
//                }
//                if (currentLength > 0) {
//                    j = res[i] + 1;
//                    currentLength = 0;
//                }
//                needBreak = false;
//            } else if (s.charAt(j) == '#') {
//                if (needBreak) {
//                    j = res[i] + 1;
//                    currentLength = 0;
//                    needBreak = false;
//                    continue;
//                }
//                if (currentLength == 0) {
//                    res[i] = j;
//                }
//                currentLength++;
//            } else if (s.charAt(j) == '?') {
//                if (needBreak) {
//                    needBreak = false;
//                    continue;
//                }
//                if (currentLength == 0) {
//                    res[i] = j;
//                }
//                currentLength++;
//            }
//        }
//        return res;
    }

    private static int[] getMax(List<Integer> groups, String s) {
        int[] res = new int[groups.size()];
        int i = groups.size() - 1;
        int length = groups.get(i);
        int currentLength = 0;
        boolean needBreak = false;
        for (int j = s.length() - 1; j >= 0; j--) {
            if (currentLength == length) {
                if (s.charAt(j) == '#' || (i == 0 && s.indexOf('#') < j)) {
                    j = res[i] + length - 2;
                    currentLength = 0;
                    needBreak = false;
                } else {
                    i--;
                    if (i == -1) {
                        break;
                    }
                    length = groups.get(i);
                    needBreak = true;
                    currentLength = 0;
                }
            }
            if (s.charAt(j) == '.') {
                if (currentLength > 0) {
                    j = res[i] + length - 2;
                    currentLength = 0;
                }
                needBreak = false;
            } else if (s.charAt(j) == '#') {
                if (needBreak) {
                    j = res[i] + length - 2;
                    currentLength = 0;
                    needBreak = false;
                    continue;
                }
                if (currentLength == 0) {
                    res[i] = j - length + 1;
                }
                currentLength++;
            } else if (s.charAt(j) == '?') {
                if (needBreak) {
                    needBreak = false;
                    continue;
                }
                if (currentLength == 0) {
                    res[i] = j - length + 1;
                }
                currentLength++;
            }
        }
        return res;
    }

    private record Stats(
            long count,
            long sharpAtEnd,
            long sharpAtStart,
            long sharpAtEndAndStart
    ) {
    }
}
