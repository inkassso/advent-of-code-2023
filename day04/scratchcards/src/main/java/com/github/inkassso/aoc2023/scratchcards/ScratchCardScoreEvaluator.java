package com.github.inkassso.aoc2023.scratchcards;

import com.github.inkassso.aoc2023.scratchcards.model.ScratchCard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class ScratchCardScoreEvaluator {
    private final List<ScratchCard> scratchCards;

    public void evaluate() {
        long summedUpScores = evaluateScratchCardMatches().stream()
                .mapToLong(cardMatches -> {
                    if (cardMatches.isEmpty()) {
                        return 0L;
                    }
                    return 1L << (cardMatches.size() - 1);
                })
                .sum();

        log.info("Summed up scores of scratchcard matches: {}", summedUpScores);
    }

    private List<Set<Integer>> evaluateScratchCardMatches() {
        return scratchCards.stream()
                .map(scratchCard -> {
                    Set<Integer> intersection = new HashSet<>(scratchCard.winningNumbers());
                    intersection.retainAll(scratchCard.scratchedNumbers());
                    return intersection;
                })
                .toList();
    }
}
