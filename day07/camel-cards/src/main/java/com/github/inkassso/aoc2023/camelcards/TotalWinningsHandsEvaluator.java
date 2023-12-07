package com.github.inkassso.aoc2023.camelcards;

import com.github.inkassso.aoc2023.camelcards.model.Turn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
public class TotalWinningsHandsEvaluator implements HandsEvaluator {
    private final List<Turn> turns;

    @Override
    public void evaluate() {
        List<Turn> turnsByRank = turns.stream()
                .sorted()
                .toList();
        long totalWinnings = IntStream.range(0, turnsByRank.size())
                .mapToLong(index -> {
                    Turn turn = turnsByRank.get(index);
                    long winnings = turn.bid() * (index + 1);
                    log.debug("Player winnings: hand={}, bid={}, rank={}, winnings={}", turn.getHandString(), turn.bid(), index + 1, winnings);
                    return winnings;
                })
                .sum();
        log.info("Total winnings for all turns: {}", totalWinnings);
    }
}
