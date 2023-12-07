package com.github.inkassso.aoc2023.camelcards.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum Card {
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    JACK('J'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');

    private static final Map<Character, Card> REVERSE_LOOKUP = new HashMap<>() {
        {
            for (Card card : Card.values()) {
                put(card.symbol, card);
            }
        }
    };

    public static Card findBySymbol(char symbol) {
        return Optional.ofNullable(REVERSE_LOOKUP.get(symbol))
                .orElseThrow(() -> new NoSuchElementException("Card not found for symbol: " + symbol));
    }

    private final char symbol;

    @Override
    public String toString() {
        return "[%s]".formatted(symbol);
    }
}
