package com.github.inkassso.aoc2023.scratchcards.model;

import java.util.Set;

public record ScratchCard(int id, Set<Integer> winningNumbers, Set<Integer> scratchedNumbers) {
}
