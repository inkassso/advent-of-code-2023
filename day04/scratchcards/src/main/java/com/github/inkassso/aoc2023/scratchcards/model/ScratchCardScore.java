package com.github.inkassso.aoc2023.scratchcards.model;

import java.util.Set;

public record ScratchCardScore(int cardId, Set<Integer> matchingNumbers) {
}
