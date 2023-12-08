package com.github.inkassso.aoc2023.wasteland;

import com.github.inkassso.aoc2023.wasteland.model.Node;
import com.github.inkassso.aoc2023.wasteland.model.Turn;
import com.github.inkassso.aoc2023.wasteland.model.WastelandMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SinglePathPathFinder implements PathFinder {
    private static final String START = "AAA";
    private static final String END = "ZZZ";

    private final WastelandMap map;

    @Override
    public void findPath() {
        Node current = map.nodes().get(START);
        if (current == null) {
            throw new IllegalArgumentException("Map does not contain a starting node");
        }

        long instructions = 0L;
        var iterator = map.instructions().iterator();

        while (!current.label().equals(END)) {
            if (!iterator.hasNext()) {
                iterator = map.instructions().iterator();
            }
            current = iterator.next().childSelector().apply(current);
            instructions++;
        }

        log.info("Total instructions followed: {}", instructions);
    }

}
