package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Adv2023Day5 {

    public static void main(String[] args) {
        System.out.println(getLowestLocation("2023/2023-12-05-sample.txt"));
        System.out.println(getLowestLocation("2023/2023-12-05.txt"));
        System.out.println(getLowestLocation2("2023/2023-12-05-sample.txt"));
        System.out.println(new Date());
        //System.out.println(getLowestLocation2("2023/2023-12-05.txt"));
        // 31161857
        // 34036367
        System.out.println(getLowestLocation2("2023/2023-12-05-mel.txt"));
        System.out.println(new Date());
    }

    private static List<Long> parse(String string) {
        return Arrays.stream(string.split("\\s+"))
                .filter(StringUtils::hasText)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public static long getLowestLocation(String inputFile) {
        List<List<MapRange>> maps = new ArrayList<>();
        AtomicReference<List<Long>> seeds = new AtomicReference<>();
        AtomicInteger mapIndex = new AtomicInteger(-1);
        FileUtil.read(inputFile, s -> {
            if (!StringUtils.hasText(s)) {
                return;
            }
            if (s.startsWith("seeds")) {
                seeds.set(parse(s.substring(s.indexOf(":") + 1)));
                return;
            } else if (s.contains("seed-to-soil map")) {
                mapIndex.set(0);
                return;
            } else if (s.contains("soil-to-fertilizer")) {
                mapIndex.set(1);
                return;
            } else if (s.contains("fertilizer-to-water")) {
                mapIndex.set(2);
                return;
            } else if (s.contains("water-to-light")) {
                mapIndex.set(3);
                return;
            } else if (s.contains("light-to-temperature")) {
                mapIndex.set(4);
                return;
            } else if (s.contains("temperature-to-humidity")) {
                mapIndex.set(5);
                return;
            } else if (s.contains("humidity-to-location")) {
                mapIndex.set(6);
                return;
            }

            if (mapIndex.get() >= maps.size()) {
                maps.add(new ArrayList<>());
            }

            List<Long> numbers = parse(s);
            maps.get(mapIndex.get()).add(new MapRange(numbers.get(0), numbers.get(1), numbers.get(2)));
        });

        long location = Long.MAX_VALUE;
        for (long seed: seeds.get()) {
            long destination = seed;
            for (List<MapRange> ranges : maps) {
                destination = getDestination(destination, ranges);
            }
            if (destination < location) {
                location = destination;
            }
        }
        return location;
    }


    public static long getLowestLocation2(String inputFile) {
        List<List<MapRange>> maps = new ArrayList<>();
        AtomicReference<List<Pair<Long, Long>>> seeds = new AtomicReference<>();
        AtomicInteger mapIndex = new AtomicInteger(-1);
        FileUtil.read(inputFile, s -> {
            if (!StringUtils.hasText(s)) {
                return;
            }
            if (s.startsWith("seeds")) {
                List<Long> values = parse(s.substring(s.indexOf(":") + 1));
                List<Pair<Long, Long>> pairs = new ArrayList<>();
                for (int i = 0; i < values.size(); i += 2) {
                    pairs.add(Pair.of(values.get(i), values.get(i + 1)));
                }
                seeds.set(pairs);
                return;
            } else if (s.contains("seed-to-soil map")) {
                mapIndex.set(0);
                return;
            } else if (s.contains("soil-to-fertilizer")) {
                mapIndex.set(1);
                return;
            } else if (s.contains("fertilizer-to-water")) {
                mapIndex.set(2);
                return;
            } else if (s.contains("water-to-light")) {
                mapIndex.set(3);
                return;
            } else if (s.contains("light-to-temperature")) {
                mapIndex.set(4);
                return;
            } else if (s.contains("temperature-to-humidity")) {
                mapIndex.set(5);
                return;
            } else if (s.contains("humidity-to-location")) {
                mapIndex.set(6);
                return;
            }

            if (mapIndex.get() >= maps.size()) {
                maps.add(new ArrayList<>());
            }

            List<Long> numbers = parse(s);
            maps.get(mapIndex.get()).add(new MapRange(numbers.get(0), numbers.get(1), numbers.get(2)));
        });

        long location = Long.MAX_VALUE;
        for (Pair<Long, Long> pair : seeds.get()) {
            for (long seed = pair.getFirst(); seed < pair.getFirst() + pair.getSecond(); seed++) {
                long destination = seed;
                for (List<MapRange> ranges : maps) {
                    destination = getDestination(destination, ranges);
                }
                if (destination < location) {
                    location = destination;
                }
            }
        }
        return location;
    }

    private static long getDestination(long source, List<MapRange> ranges) {
        for (MapRange range : ranges) {
            Long destination = range.getDestination(source);
            if (destination != null) {
                return destination;
            }
        }
        return source;
    }

    private record MapRange(
            long destinationStart,
            long sourceStart,
            long length
    ) {
        public Long getDestination(long source) {
            if (source >= sourceStart && source < sourceStart + length) {
                return destinationStart + (source - sourceStart);
            }
            return null;
        }
    }
}
