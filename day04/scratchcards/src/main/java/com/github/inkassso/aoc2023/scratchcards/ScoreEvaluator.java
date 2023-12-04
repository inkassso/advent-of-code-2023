package com.github.inkassso.aoc2023.scratchcards;

import com.github.inkassso.aoc2023.scratchcards.model.ScratchCard;
import com.github.inkassso.aoc2023.scratchcards.model.ScratchCardScore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class ScoreEvaluator implements Evaluator {
    private final List<ScratchCard> scratchCards;

    public void evaluate() {
        long summedUpScores = evaluateScratchCardMatches().stream()
                .map(ScratchCardScore::matchingNumbers)
                .mapToLong(matchingNumbers -> {
                    if (matchingNumbers.isEmpty()) {
                        return 0L;
                    }
                    return 1L << (matchingNumbers.size() - 1);
                })
                .sum();

        log.info("Summed up scores of scratchcard matches: {}", summedUpScores);
    }

    public List<ScratchCardScore> evaluateScratchCardMatches() {
        return scratchCards.stream()
                .map(scratchCard -> {
                    Set<Integer> intersection = new HashSet<>(scratchCard.winningNumbers());
                    intersection.retainAll(scratchCard.scratchedNumbers());
                    return new ScratchCardScore(scratchCard.id(), intersection);
                })
                .toList();
    }
}
