package com.github.inkassso.aoc2023.pipemaze.model;

import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public enum Field {
    VERTICAL('|'),
    HORIZONTAL('-'),
    NE_BEND('L'),
    NW_BEND('J'),
    SW_BEND('7'),
    SE_BEND('F'),
    GROUND('.'),
    START('S');

    private static final Map<Character, Field> LOOKUP_MAP = new HashMap<>() {
        {
            for (Field field : Field.values()) {
                put(field.symbol, field);
            }
        }
    };

    public static Field findBySymbol(char symbol) {
        return Optional.ofNullable(LOOKUP_MAP.get(symbol))
                .orElseThrow(() -> new NoSuchElementException("Field not found for symbol: " + symbol));
    }

    private final char symbol;
}
