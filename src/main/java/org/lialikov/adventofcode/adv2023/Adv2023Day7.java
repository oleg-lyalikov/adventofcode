package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.lialikov.adventofcode.util.ParseUtil;

import java.util.*;

public class Adv2023Day7 {

    public static void main(String[] args) {
        // 6440
        //System.out.println(getTotalWinnings("2023/2023-12-07-sample.txt"));
        // 253866470
        //System.out.println(getTotalWinnings("2023/2023-12-07.txt"));
        // 5905
        //System.out.println(getTotalWinnings2("2023/2023-12-07-sample.txt"));
        // 254796637 too high
        System.out.println(getTotalWinnings2("2023/2023-12-07.txt"));
    }

    private static final Map<Character, Integer> cardWeigh = new HashMap<>();
    static {
        cardWeigh.put('A', 14);
        cardWeigh.put('K', 13);
        cardWeigh.put('Q', 12);
        cardWeigh.put('J', 11);
        cardWeigh.put('T', 10);
        cardWeigh.put('9', 9);
        cardWeigh.put('8', 8);
        cardWeigh.put('7', 7);
        cardWeigh.put('6', 6);
        cardWeigh.put('5', 5);
        cardWeigh.put('4', 4);
        cardWeigh.put('3', 3);
        cardWeigh.put('2', 2);
    }
    private static final Map<Character, Integer> cardWeigh2 = new HashMap<>();
    static {
        cardWeigh2.put('A', 14);
        cardWeigh2.put('K', 13);
        cardWeigh2.put('Q', 12);
        cardWeigh2.put('J', 1);
        cardWeigh2.put('T', 10);
        cardWeigh2.put('9', 9);
        cardWeigh2.put('8', 8);
        cardWeigh2.put('7', 7);
        cardWeigh2.put('6', 6);
        cardWeigh2.put('5', 5);
        cardWeigh2.put('4', 4);
        cardWeigh2.put('3', 3);
        cardWeigh2.put('2', 2);
    }

    public static long getTotalWinnings(String inputFile) {
        List<HandBid> handBids = new ArrayList<>();
        FileUtil.read(inputFile, s -> handBids.add(new HandBid(
                s.substring(0, 5),
                ParseUtil.parse(s.substring(6)).get(0)
        )));

        handBids.sort(new HandComparator(cardWeigh));
        long res = 0;
        for (int i = 0; i < handBids.size(); i++) {
            res += handBids.get(i).bid * (i + 1);
        }
        return res;
    }

    public static long getTotalWinnings2(String inputFile) {
        List<HandBid> handBids = new ArrayList<>();
        FileUtil.read(inputFile, s -> {
            String hand = s.substring(0, 5);
            handBids.add(new HandBid(
                    hand,
                    ParseUtil.parse(s.substring(6)).get(0),
                    HandBid.getRank2(hand)
            ));
        });

        handBids.sort(new HandComparator(cardWeigh2));
        long res = 0;
        for (int i = 0; i < handBids.size(); i++) {
            res += handBids.get(i).bid * (i + 1);
        }
        return res;
    }

    private static final class HandBid {
        private final String hand;
        private final long bid;
        private final int rank;

        public HandBid(String hand, long bid) {
            this.hand = hand;
            this.bid = bid;
            this.rank = getRank(hand);
        }

        public HandBid(String hand, long bid, int rank) {
            this.hand = hand;
            this.bid = bid;
            this.rank = rank;
        }

        private int getRank(String hand) {
            Map<Character, Integer> counts = new HashMap<>();
            for (char ch : hand.toCharArray()) {
                counts.putIfAbsent(ch, 0);
                counts.put(ch, counts.get(ch) + 1);
            }
            boolean hasThree = false;
            int hasTwo = 0;
            for (Map.Entry<Character, Integer> e : counts.entrySet()) {
                if (e.getValue() == 5) {
                    return 21;
                } else if (e.getValue() == 4) {
                    return 20;
                } else if (e.getValue() == 3) {
                    hasThree = true;
                } else if (e.getValue() == 2) {
                    hasTwo++;
                }
            }
            if (hasThree && hasTwo > 0) {
                return 19;
            } else if (hasThree) {
                return 18;
            } else if (hasTwo == 2) {
                return 17;
            } else if (hasTwo == 1) {
                return 16;
            } else {
                return 15;
            }
        }

        static int getRank2(String hand) {
            Map<Character, Integer> counts = new HashMap<>();
            for (char ch : hand.toCharArray()) {
                counts.putIfAbsent(ch, 0);
                counts.put(ch, counts.get(ch) + 1);
            }
            Integer jokers = counts.get('J');
            if (jokers == null) {
                jokers = 0;
            }
            boolean hasThree = false;
            int hasTwo = 0;
            final int max = 21;
            for (Map.Entry<Character, Integer> e : counts.entrySet()) {
                if (e.getValue() == 5) {
                    return max;
                } else if (e.getValue() == 4) {
                    if (jokers > 0) {
                        return max;
                    }
                    return max - 1;
                } else if (e.getValue() == 3) {
                    hasThree = true;
                } else if (e.getValue() == 2) {
                    hasTwo++;
                }
            }
            if (hasThree && hasTwo > 0) {
                if (jokers > 0) {
                    return max;
                }
                return max - 2;
            } else if (hasThree) {
                if (jokers > 0) {
                    return max - 1;
                }
                return max - 3;
            } else if (hasTwo == 2) {
                if (jokers == 2) {
                    return max - 1;
                } else if (jokers == 1) {
                    return max - 2;
                }
                return max - 4;
            } else if (hasTwo == 1) {
                if (jokers > 0) {
                    return max - 3;
                }
                return max - 5;
            } else {
                if (jokers > 0) {
                    return max - 5;
                }
                return max - 6;
            }
        }

        @Override
        public String toString() {
            return "HandBid{" +
                    "hand='" + hand + '\'' +
                    ", bid=" + bid +
                    ", rank=" + rank +
                    '}';
        }
    }

    private static final class HandComparator implements Comparator<HandBid> {
        private final Map<Character, Integer> cardWeigh;
        public HandComparator(Map<Character, Integer> cardWeigh) {
            this.cardWeigh = cardWeigh;
        }
        @Override
        public int compare(HandBid o1, HandBid o2) {
            int compare = Integer.compare(o1.rank, o2.rank);
            if (compare != 0) {
                return compare;
            }
            for (int i = 0; i < 5; i++) {
                char ch1 = o1.hand.charAt(i);
                char ch2 = o2.hand.charAt(i);
                compare = Integer.compare(cardWeigh.get(ch1), cardWeigh.get(ch2));
                if (compare != 0) {
                    return compare;
                }
            }
            return 0;
        }
    }
}
