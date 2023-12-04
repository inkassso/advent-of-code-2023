package com.github.inkassso.aoc2023.scratchcards;

import com.github.inkassso.aoc2023.scratchcards.model.ScratchCard;
import com.github.inkassso.aoc2023.scratchcards.model.ScratchCardScore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CountEvaluator implements Evaluator {
    private final List<ScratchCard> scratchCards;

    @Override
    public void evaluate() {
        long allCardsCount = evaluateCardCounts().values().stream()
                .mapToLong(count -> count)
                .sum();

        log.info("Total amount of cards: {}", allCardsCount);
    }

    private Map<Integer, Long> evaluateCardCounts() {
        var scoreEvaluator = new ScoreEvaluator(scratchCards);

        Map<Integer, Long> cardCounts = new HashMap<>();
        for (ScratchCard card : scratchCards) {
            log.trace("Initializing card {} with original count: 1", card.id());
            cardCounts.put(card.id(), 1L);
        }

        for (ScratchCardScore score : scoreEvaluator.evaluateScratchCardMatches()) {
            Long currentCardCount = cardCounts.get(score.cardId());
            log.trace("Processing score of card {}: currentCopies={}, matches={}", score.cardId(), currentCardCount, score.matchingNumbers().size());
            if (currentCardCount == null) {
                throw new IllegalStateException("Processing card that wasn't initialized with any count in the pool: id=" + score.cardId());
            }
            for (int i = score.cardId() + 1; i <= score.cardId() + score.matchingNumbers().size(); i++) {
                cardCounts.compute(i, (cardCopyId, cardCopyCount) -> {
                    if (cardCopyCount == null) {
                        throw new IllegalStateException("Updating card count that wasn't initialized with any count in the pool: id=" + cardCopyId);
                    }
                    long newCopyCount = cardCopyCount + currentCardCount;
                    log.trace("Updating copy count for subsequent card {}: before={}, after={}", cardCopyId, cardCopyCount, newCopyCount);
                    return newCopyCount;
                });
            }
        }

        return cardCounts;
    }
}
