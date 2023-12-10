package org.lialikov.adventofcode.adv2022;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Adv2022Day5 {

    public static void main(String[] args) {
        System.out.println(getTopStack("2022/2022-12-05-sample.txt", false));
        System.out.println(getTopStack("2022/2022-12-05.txt", false));
        System.out.println(getTopStack("2022/2022-12-05-sample.txt", true));
        System.out.println(getTopStack("2022/2022-12-05.txt", true));
    }

    private static final Pattern MOVE_PATTERN = Pattern.compile("\\s*move\\s+(\\d+)\\s+from\\s+(\\d+)\\s+to\\s+(\\d+)\\s*");

    private static String getTopStack(String file, boolean moveAsStack) {
        List<Deque<Character>> stacks = new ArrayList<>();
        AtomicBoolean cratesRead = new AtomicBoolean(false);
        FileUtil.read(file, l -> {
            if (!StringUtils.hasText(l)) {
                return;
            }

            if (Character.isDigit(l.trim().charAt(0))) {
                cratesRead.set(true);
                return;
            }
            if (!cratesRead.get()) {
                for (int i = 0; i < l.length() - 1; i += 4) {
                    final int resIndex = i / 4;
                    String crate = l.substring(i, Math.min(l.length(), i + 4));
                    if (!StringUtils.hasText(crate)) {
                        continue;
                    }
                    if (crate.charAt(0) != '[' || crate.charAt(2) != ']') {
                        throw new IllegalStateException("Unsupported crate: " + crate);
                    }
                    char crateLetter = crate.charAt(1);
                    while (stacks.size() <= resIndex) {
                        stacks.add(new LinkedList<>());
                    }
                    if (stacks.get(resIndex) == null) {
                        stacks.set(resIndex, new LinkedList<>());
                    }
                    stacks.get(resIndex).addFirst(crateLetter);
                }
            } else {
                Matcher m = MOVE_PATTERN.matcher(l);
                if (!m.matches()) {
                    throw new IllegalStateException("Move pattern failure: " + l);
                }

                int count = Integer.parseInt(m.group(1));
                int from = Integer.parseInt(m.group(2)) - 1;
                int to = Integer.parseInt(m.group(3)) - 1;

                List<Character> temp = new ArrayList<>();
                while (count-- > 0) {
                    if (moveAsStack) {
                        temp.add(stacks.get(from).removeLast());
                    } else {
                        stacks.get(to)
                                .addLast(
                                        stacks.get(from).removeLast()
                                );
                    }
                }
                if (moveAsStack) {
                    for (int j = temp.size() - 1; j >= 0; j--) {
                        stacks.get(to).addLast(temp.get(j));
                    }
                }
            }
        });
        return stacks.stream().map(s -> s.getLast().toString()).collect(Collectors.joining());
    }
}
