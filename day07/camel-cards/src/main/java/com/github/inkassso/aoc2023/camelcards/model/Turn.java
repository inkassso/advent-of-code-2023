package com.github.inkassso.aoc2023.camelcards.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Getter
@RequiredArgsConstructor
public class Turn implements Comparable<Turn> {
    private static final int HAND_SIZE = 5;

    private final List<Card> hand;
    private final long bid;

    private HandType handType;

    @Override
    public int compareTo(Turn other) {
        String thisHandStr = this.getHandString();
        String otherHandStr = other.getHandString();
        log.trace("Comparing hands: this={}, other={}", thisHandStr, otherHandStr);

        HandType thisType = evaluateHandType();
        HandType otherType = other.evaluateHandType();

        int typeComparison = thisType.compareTo(otherType);
        if (typeComparison != 0) {
            log.trace("Hand types are unequal: winner={}, type={}",
                    typeComparison > 0 ? "this (%s)".formatted(thisHandStr) : "other (%s)".formatted(otherHandStr),
                    typeComparison > 0 ? thisType : otherType);
            return typeComparison;
        }

        int handComparison = IntStream.range(0, HAND_SIZE)
                .reduce(0, (acc, cardIndex) -> {
                    if (acc != 0) {
                        return acc;
                    }
                    int cardComparison = this.hand.get(cardIndex).compareTo(other.hand.get(cardIndex));
                    if (cardComparison != 0) {
                        log.trace("Hand types are equal cards are different on position {}: winner={}",
                                cardIndex + 1,
                                cardComparison > 0 ? "this (%s)".formatted(thisHandStr) : "other (%s)".formatted(otherHandStr));
                    }
                    return cardComparison;
                });
        if (handComparison == 0) {
            log.warn("Hands are of equal type and card strengths: this={}, other={}, winner=none", thisHandStr, otherHandStr);
        }
        return handComparison;
    }

    private HandType evaluateHandType() {
        if (handType != null) {
            return handType;
        }
        String handStr = getHandString();
        log.trace("Evaluating hand type: hand={}", handStr);

        Map<Card, List<Card>> groups = hand.stream().collect(Collectors.groupingBy(Function.identity()));
        handType = switch (groups.size()) {
            case 5 -> HandType.HIGH_CARD;
            case 4 -> HandType.ONE_PAIR;
            case 3 -> {
                boolean areThreeSame = groups.values().stream().anyMatch(group -> group.size() == 3);
                if (areThreeSame) {
                    yield HandType.THREE_OF_A_KIND;
                }
                yield HandType.TWO_PAIR;
            }
            case 2 -> {
                boolean areFourSame = groups.values().stream().anyMatch(group -> group.size() == 4);
                if (areFourSame) {
                    yield HandType.FOUR_OF_A_KIND;
                }
                yield HandType.FULL_HOUSE;
            }
            case 1 -> HandType.FIVE_OF_A_KIND;
            default -> throw new IllegalStateException("Unexpected amount of card groups by strength: " + groups.size());
        };

        log.trace("Evaluated hand type: hand={}, type={}", handStr, handType);
        return handType;
    }

    public String getHandString() {
        return hand.stream().map(Card::symbol).map(Object::toString).collect(Collectors.joining());
    }
}
