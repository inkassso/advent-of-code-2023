package com.github.inkassso.aoc2023.camelcards;

import com.github.inkassso.aoc2023.camelcards.model.Card;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CardLookup {

    private static final Map<Character, Card> REVERSE_LOOKUP_WITH_JACK;
    private static final Map<Character, Card> REVERSE_LOOKUP_WITH_JOKER;

    static {
        var allCards = new HashMap<Character, Card>() {
            {
                for (Card card : Card.values()) {
                    put(card.symbol(), card);
                }
            }
        };

        REVERSE_LOOKUP_WITH_JACK = new HashMap<>(allCards);
        REVERSE_LOOKUP_WITH_JACK.put(Card.JACK.symbol(), Card.JACK);

        REVERSE_LOOKUP_WITH_JOKER = new HashMap<>(allCards);
        REVERSE_LOOKUP_WITH_JOKER.put(Card.JOKER.symbol(), Card.JOKER);
    }

    public static Card findBySymbolWithJack(char symbol) {
        return Optional.ofNullable(REVERSE_LOOKUP_WITH_JACK.get(symbol))
                .orElseThrow(() -> new NoSuchElementException("Card not found for symbol: " + symbol));
    }

    public static Card findBySymbolWithJoker(char symbol) {
        return Optional.ofNullable(REVERSE_LOOKUP_WITH_JOKER.get(symbol))
                .orElseThrow(() -> new NoSuchElementException("Card not found for symbol: " + symbol));
    }

}
