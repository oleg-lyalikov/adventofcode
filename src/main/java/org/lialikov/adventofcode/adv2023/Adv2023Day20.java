package org.lialikov.adventofcode.adv2023;

import org.lialikov.adventofcode.util.FileUtil;
import org.lialikov.adventofcode.util.MathUtil;

import java.util.*;

public class Adv2023Day20 {

    public static void main(String[] args) {
        System.out.println(new Date());

        // 11687500
        System.out.println(getPart1("2023/2023-12-20-sample.txt"));
        // 812721756
        System.out.println(getPart1("2023/2023-12-20.txt"));
        // 233338595643977
        System.out.println(getPart2("2023/2023-12-20.txt"));

        System.out.println(new Date());
    }

    private static final String BROADCASTER_NAME = "broadcaster";
    private static final Module OUTPUT = new Module(ModuleType.OUTPUT, null, Collections.emptyList());

    private static long getPart2(String inputFile) {
        Input input = readInput(inputFile);
        ModulesState modulesState = new ModulesState(input);
        modulesState.iterateUntilRxFound();
        long res = 1;
        for (long i : modulesState.highPulseIterations.values()) {
            res = MathUtil.lcm(res, i);
        }
        return res;
    }

    private static long getPart1(String inputFile) {
        Input input = readInput(inputFile);
        ModulesState modulesState = new ModulesState(input);
        modulesState.iterate(1000);
        return modulesState.lowCount * modulesState.highCount;
    }

    private static Module readModule(String line) {
        String[] parts = line.split("\\s+->\\s+");
        if (parts.length != 2) {
            throw new IllegalStateException("!!!");
        }
        List<String> dest = Arrays.stream(parts[1].split("\\s*,\\s*")).toList();
        if (BROADCASTER_NAME.equals(parts[0])) {
            return new Module(ModuleType.BROADCASTER, parts[0], dest);
        }

        String name = parts[0].substring(1);
        char type = parts[0].charAt(0);
        final ModuleType moduleType;
        if (type == '%') {
            moduleType = ModuleType.FLIP_FLOP;
        } else if (type == '&') {
            moduleType = ModuleType.CONJUNCTION;
        } else {
            throw new IllegalStateException("!!!");
        }
        return new Module(moduleType, name, dest);
    }

    private static Input readInput(String inputFile) {
        List<String> lines = FileUtil.readLines(inputFile);

        List<Module> modules = new ArrayList<>();
        Map<String, Module> byName = new HashMap<>();
        for (String line : lines) {
            Module module = readModule(line);
            modules.add(module);
            byName.put(module.name, module);
        }
        return new Input(modules, byName);
    }

    private static class ModulesState {
        Input input;
        Map<Module, List<Module>> dests = new HashMap<>();
        long iteration = 0;
        Map<Module, State> flipFlopStates = new HashMap<>();
        Map<Module, Map<Module, Pulse>> conjLastPulse = new HashMap<>();
        long lowCount = 0;
        long highCount = 0;
        Long rxMinIteration = null;
        Module rxConjInputModule;
        Map<Module, Long> highPulseIterations = new HashMap<>();

        ModulesState(Input input) {
            this.input = input;
            input.modules.forEach(m -> {
                if (m.type == ModuleType.FLIP_FLOP) {
                    flipFlopStates.put(m, State.OFF);
                } else if (m.type == ModuleType.CONJUNCTION) {
                    Map<Module, Pulse> conjPulse = new HashMap<>();
                    input.modules.forEach(mod -> {
                        if (mod.dest.contains(m.name)) {
                            conjPulse.put(mod, Pulse.LOW);
                        }
                    });
                    conjLastPulse.put(m, conjPulse);
                }
                dests.put(m, m.dest.stream().map(destName -> {
                    Module module = input.byName.get(destName);
                    if (module == null) {
                        System.out.println("Found OUTPUT module: " + destName);
                        return OUTPUT;
                    }
                    return module;
                }).toList());
            });

            for (Module m : input.modules) {
                if (m.dest.contains("rx")) {
                    if (rxConjInputModule != null) {
                        throw new IllegalStateException("!!!");
                    }
                    rxConjInputModule = m;
                    if (rxConjInputModule.type != ModuleType.CONJUNCTION) {
                        throw new IllegalStateException("!!!");
                    }
                    System.out.println("rx conj input module destinations: " + conjLastPulse.get(rxConjInputModule).keySet());
                    for (Module mmm : conjLastPulse.get(rxConjInputModule).keySet()) {
                        if (mmm.type == ModuleType.FLIP_FLOP) {
                            System.out.println("FLIP_FLOP: " + mmm);
                        } else {
                            System.out.println("rx conj input module destinations: " + conjLastPulse.get(mmm).keySet());
                        }
                    }
                }
            }
        }

        public void iterateUntilRxFound() {
            iterate(null, true);
        }

        public void iterate(long iterations) {
            iterate(iterations, false);
        }

        public void iterate(Long iterations, boolean stopWhenRxFound) {
            for (iteration = 0; ; iteration++) {
                if (iterations != null && iteration == iterations) {
                    break;
                }
                if (iteration % 1000000 == 0) {
                    System.out.println("Iterations: " + iteration + ", date: " + new Date());
                }
                lowCount++;
                Module broadcaster = input.byName.get(BROADCASTER_NAME);

                List<Signal> next = new ArrayList<>();
                for (Module dest : dests.get(broadcaster)) {
                    next.add(new Signal(broadcaster, dest, Pulse.LOW));
                }

                while (!next.isEmpty()) {
                    List<Signal> accumulating = new ArrayList<>();
                    for (Signal signal : next) {
                        if (signal.pulse == Pulse.HIGH) {
                            highCount++;
                        } else {
                            lowCount++;
                        }
                        if ("rx".equals(signal.to.name) && signal.pulse == Pulse.LOW) {
                            if (rxMinIteration == null) {
                                rxMinIteration = iteration + 1;
                                System.out.println("rx min iteration: " + rxMinIteration);
                                if (stopWhenRxFound) {
                                    return;
                                }
                            }
                        }
                        if (signal.to.type == ModuleType.FLIP_FLOP) {
                            if (signal.pulse != Pulse.HIGH) {
                                State state = flipFlopStates.get(signal.to);
                                Pulse nextP = state == State.OFF ? Pulse.HIGH : Pulse.LOW;
                                State nextS = state == State.ON ? State.OFF : State.ON;
                                flipFlopStates.put(signal.to, nextS);

                                List<Module> destinations = dests.get(signal.to);
                                destinations.forEach(d -> accumulating.add(new Signal(signal.to, d, nextP)));
                            }
                        } else if (signal.to.type == ModuleType.CONJUNCTION) {
                            Map<Module, Pulse> conjState = conjLastPulse.get(signal.to);
                            conjState.put(signal.from, signal.pulse);
                            boolean allHigh = conjState.values().stream().allMatch(p -> p == Pulse.HIGH);
                            Pulse nextP = allHigh ? Pulse.LOW : Pulse.HIGH;

                            List<Module> destinations = dests.get(signal.to);
                            destinations.forEach(d -> accumulating.add(new Signal(signal.to, d, nextP)));

                            if (stopWhenRxFound && conjLastPulse.get(rxConjInputModule).containsKey(signal.to)) {
                                if (nextP == Pulse.HIGH) {
                                    highPulseIterations.computeIfAbsent(signal.to, k -> iteration + 1);
                                    //System.out.println("Iteration: " + (iteration + 1) + ", signal.to = " + signal.to + ", pulse: " + nextP);
                                    if (highPulseIterations.keySet().size() == conjLastPulse.get(rxConjInputModule).size()) {
                                        return;
                                    }
                                }
                            }
                        } else if (signal.to.type != ModuleType.OUTPUT) {
                            throw new IllegalStateException("!!!");
                        }
                    }
                    next = accumulating;
                }
            }
        }
    }

    private record Signal(
            Module from,
            Module to,
            Pulse pulse
    ) {}
    private enum State {
        ON,
        OFF
    }
    private record Input(
         List<Module> modules,
         Map<String, Module> byName
    ) {
    }
    private enum Pulse {
        LOW,
        HIGH
    }
    private record Module(
        ModuleType type,
        String name,
        List<String> dest
    ) {
    }
    private enum ModuleType {
        FLIP_FLOP,
        CONJUNCTION,
        BROADCASTER,
        OUTPUT
    }
}
