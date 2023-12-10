package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Adv2023Day4 {

    public static void main(String[] args) {
        System.out.println(getCardsPoints("2023/2023-12-04-sample.txt"));
        System.out.println(getCardsPoints("2023/2023-12-04.txt"));
        System.out.println(getTotalScratchCards("2023/2023-12-04-sample.txt"));
        System.out.println(getTotalScratchCards("2023/2023-12-04.txt"));
    }

    private static Set<Integer> parse(String string) {
        return Arrays.stream(string.split("\\s+"))
                .filter(StringUtils::hasText)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    public static long getCardsPoints(String inputFile) {
        AtomicLong res = new AtomicLong(0);
        FileUtil.read(inputFile, s -> {
            String[] parts = s.substring(s.indexOf(":") + 1).split("\\|");
            if (parts.length != 2) {
                throw new IllegalStateException("Parts length: " + parts.length);
            }
            Set<Integer> winning = parse(parts[0]);
            Set<Integer> card = parse(parts[1]);
            long cardPoint = 0;
            for (Integer point: card) {
                if (winning.contains(point)) {
                    if (cardPoint == 0) {
                        cardPoint = 1;
                    } else {
                        cardPoint = cardPoint * 2;
                    }
                }
            }
            res.addAndGet(cardPoint);
        });

        return res.get();
    }

    public static long getTotalScratchCards(String inputFile) {
        Pattern p = Pattern.compile("Card\\s+(\\d+):\\s*(.*)\\|(.*)");
        Map<Integer, Integer> cardTimes = new HashMap<>();
        FileUtil.read(inputFile, s -> {
            Matcher m = p.matcher(s);
            if (!m.matches()) {
                throw new IllegalStateException(s);
            }
            int cardNumber = Integer.parseInt(m.group(1));
            cardTimes.putIfAbsent(cardNumber, 0);
            cardTimes.put(cardNumber, cardTimes.get(cardNumber) + 1);

            Set<Integer> winning = parse(m.group(2));
            Set<Integer> card = parse(m.group(3));
            long cardMatches = 0;
            for (Integer point: card) {
                if (winning.contains(point)) {
                    cardMatches++;
                }
            }

            int next = cardNumber + 1;
            int currentTimes = cardTimes.get(cardNumber);
            for (int i = next; i < next + cardMatches; i++) {
                cardTimes.putIfAbsent(i, 0);
                cardTimes.put(i, cardTimes.get(i) + currentTimes);
            }
        });

        return cardTimes.values().stream().mapToLong(v -> (long) v).sum();
    }
}
