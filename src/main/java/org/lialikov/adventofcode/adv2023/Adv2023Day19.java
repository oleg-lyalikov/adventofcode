package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.springframework.data.util.Pair;
import org.springframework.util.StringUtils;

import java.util.*;

public class Adv2023Day19 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 19114
        System.out.println(getPart1("2023/2023-12-19-sample.txt"));
        // 376008
        System.out.println(getPart1("2023/2023-12-19.txt"));
        // 167409079868000
        System.out.println(getPart2("2023/2023-12-19-sample.txt"));
        // 124078207789312
        System.out.println(getPart2("2023/2023-12-19.txt"));

        System.out.println(new Date());
    }

    private static final Workflow REJ = new Workflow("R", Collections.emptyList());
    private static final Workflow ACC = new Workflow("A", Collections.emptyList());

    private static long getPart2(String inputFile) {
        Input input = readInput(inputFile);

        int min = 1;
        int max = 4000;

        Workflow workflow = input.workflows.get("in");
        PartRanges partRanges = new PartRanges(new HashMap<>());
        for (Part part : Part.values()) {
            partRanges.ranges.put(part, Pair.of(min, max));
        }
        return getAccepted(input, workflow, partRanges);
    }

    private static long getAccepted(Input input, Workflow workflow, PartRanges partRanges) {
        if (workflow == REJ) {
            return 0;
        } else if (workflow == ACC) {
            long res = 1;
            for (Pair<Integer, Integer> range : partRanges.ranges.values()) {
                res *= (range.getSecond() - range.getFirst() + 1);
            }
            return res;
        }
        long res = 0;
        for (Rule rule : workflow.rules) {
            if (rule.isUnconditional()) {
                return res + getAccepted(input, input.workflows.get(rule.workflow), partRanges);
            }
            Pair<Integer, Integer> range = partRanges.ranges.get(rule.part);
            final Pair<Integer, Integer> newRange;
            if (rule.condition == Condition.LESS) {
                newRange = Pair.of(range.getFirst(), rule.value - 1);
            } else {
                newRange = Pair.of(rule.value + 1, range.getSecond());
            }
            if (newRange.getFirst() <= newRange.getSecond()) {
                PartRanges newPartRanges = new PartRanges(new HashMap<>(partRanges.ranges));
                newPartRanges.ranges.put(rule.part, newRange);
                res += getAccepted(input, input.workflows.get(rule.workflow), newPartRanges);
            }

            final Pair<Integer, Integer> nextNewRange;
            if (rule.condition == Condition.LESS) {
                nextNewRange = Pair.of(rule.value, range.getSecond());
            } else {
                nextNewRange = Pair.of(range.getFirst(), rule.value);
            }
            if (nextNewRange.getFirst() > nextNewRange.getSecond()) {
                throw new IllegalStateException("!!!");
            }
            partRanges.ranges.put(rule.part, nextNewRange);
        }
        throw new IllegalStateException("!!!");
    }

    private static long getPart1(String inputFile) {
        Input input = readInput(inputFile);

        long res = 0;
        for (Packet packet : input.packets) {
            Workflow workflow = input.workflows.get("in");
            while (workflow != REJ && workflow != ACC) {
                boolean workflowFound = false;
                for (Rule rule : workflow.rules) {
                    if (rule.isUnconditional()) {
                        workflow = input.workflows.get(rule.workflow);
                        workflowFound = true;
                        break;
                    }
                    int packetValue = packet.data.get(rule.part);
                    if ((rule.condition == Condition.MORE && packetValue > rule.value) ||
                            (rule.condition == Condition.LESS && packetValue < rule.value)) {
                        workflow = input.workflows.get(rule.workflow);
                        workflowFound = true;
                        break;
                    }
                }
                if (!workflowFound) {
                    throw new IllegalStateException("No more rules and next workflow is not found");
                }
            }
            if (workflow == ACC) {
                res += packet.data.values().stream().mapToLong(i -> i).sum();
            }
        }

        return res;
    }

    private static Input readInput(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);
        Map<String, Workflow> workflows = new HashMap<>();
        workflows.put("A", ACC);
        workflows.put("R", REJ);
        boolean readWorkflows = true;
        List<Packet> packets = new ArrayList<>();
        for (String line : lines) {
            if (!StringUtils.hasText(line)) {
                readWorkflows = false;
                continue;
            }
            if (readWorkflows) {
                String name = line.substring(0, line.indexOf('{'));
                String rulesStr = line.substring(line.indexOf('{') + 1, line.indexOf('}'));
                String[] rulesStrList = rulesStr.split(",");
                List<Rule> rules = new ArrayList<>();
                for (String ruleStr : rulesStrList) {
                    int index = ruleStr.indexOf(":");
                    if (index < 0) {
                        rules.add(new Rule(ruleStr));
                    } else {
                        Part part = Part.valueOf("" + ruleStr.charAt(0));
                        char sign = ruleStr.charAt(1);
                        final Condition condition;
                        if (sign == '>') {
                            condition = Condition.MORE;
                        } else if (sign == '<') {
                            condition = Condition.LESS;
                        } else {
                            throw new IllegalStateException("!!!");
                        }
                        int value = Integer.parseInt(ruleStr.substring(2, index));
                        String workflow = ruleStr.substring(index + 1);
                        rules.add(new Rule(part, condition, value, workflow));
                    }
                }
                workflows.put(name, new Workflow(name, rules));
            } else {
                line = line.substring(1, line.length() - 1);
                String[] parts = line.split(",");
                Map<Part, Integer> values = new HashMap<>();
                for (String part : parts) {
                    Part p = Part.valueOf("" + part.charAt(0));
                    if (part.charAt(1) != '=') {
                        throw new IllegalStateException("!!!");
                    }
                    Integer value = Integer.parseInt(part.substring(2));
                    values.put(p, value);
                }
                packets.add(new Packet(values));
            }
        }

        return new Input(workflows, packets);
    }

    private record PartRanges(
            Map<Part, Pair<Integer, Integer>> ranges
    ) {
    }

    private record Input(
        Map<String, Workflow> workflows,
        List<Packet> packets
    ) {
    }
    private record Packet(
        Map<Part, Integer> data
    ) {
    }
    private record Workflow(
            String name,
            List<Rule> rules
    ) {
    }

    private record Rule(
        Part part,
        Condition condition,
        int value,
        String workflow
    ) {
        Rule(String workflow) {
            this(null, null, 0, workflow);
        }

        public boolean isUnconditional() {
            return part == null;
        }
    }

    private enum Condition {
        MORE,
        LESS
    }

    private enum Part {
        x,
        m,
        a,
        s
    }
}
