package com.github.inkassso.aoc2023.wasteland;

import com.github.inkassso.aoc2023.wasteland.model.Node;
import com.github.inkassso.aoc2023.wasteland.model.Turn;
import com.github.inkassso.aoc2023.wasteland.model.WastelandMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ConcurrentPathsPathFinder implements PathFinder {
    private static final String START_PATTERN = "^.*A$";
    private static final String END_PATTERN = "^.*Z$";

    private final WastelandMap map;

    @Override
    public void findPath() {
        Map<Node, Long> nodeHops = new HashMap<>();
        map.nodes().values().stream()
                .filter(node -> node.label().matches(START_PATTERN))
                .forEach(node -> nodeHops.put(node, 0L));
        if (nodeHops.isEmpty()) {
            throw new IllegalArgumentException("Map contains no starting nodes");
        }

        Set<Node> endNodes = map.nodes().values().stream()
                .filter(node -> node.label().matches(END_PATTERN))
                .collect(Collectors.toSet());

        log.debug("Finding concurrent paths: startNodes={}, endNodes={}", nodeHops.keySet().stream().map(Node::label).toList(), endNodes.stream().map(Node::label).toList());
        Map<Node, Long> actualHopsToEndNodes = countStepsToEndNodes(endNodes, nodeHops);

        long actualNumberOfSteps = lcm(actualHopsToEndNodes.values());
        log.info("Total steps required: {}", actualNumberOfSteps);
    }

    private Map<Node, Long> countStepsToEndNodes(Set<Node> endNodes, Map<Node, Long> currentNodeHops) {
        Map<Node, Long> actualHopsToEndNodes = new HashMap<>();
        Map<Node, Long> temp = new HashMap<>();

        var iterator = map.instructions().iterator();
        while (!endNodes.stream().allMatch(actualHopsToEndNodes::containsKey)) {
            if (!iterator.hasNext()) {
                iterator = map.instructions().iterator();
            }
            Turn turn = iterator.next();
            log.trace("Next turn: {}", turn);

            for (Map.Entry<Node, Long> current : currentNodeHops.entrySet()) {
                Node next = turn.childSelector().apply(current.getKey());

                if (endNodes.contains(next)) {
                    actualHopsToEndNodes.put(next, current.getValue() + 1);
                    endNodes.remove(next);
                    log.debug("Found distance to ending node: node={}, distance={}, remaining={}", next.label(), current.getValue() + 1, endNodes.size());
                }

                log.trace("Traversing between nodes: from={}, to={}", current.getKey().label(), next.label());
                temp.put(next, current.getValue() + 1);
            }
            currentNodeHops.clear();
            currentNodeHops.putAll(temp);
            temp.clear();
        }
        return actualHopsToEndNodes;
    }

    private static long lcm(Collection<Long> input)
    {
        return input.stream()
                .mapToLong(nr -> nr)
                .reduce(1, ConcurrentPathsPathFinder::lcm);
    }

    private static long lcm(long a, long b)
    {
        return a * (b / gcd(a, b));
    }

    private static long gcd(long a, long b)
    {
        while (b > 0)
        {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
