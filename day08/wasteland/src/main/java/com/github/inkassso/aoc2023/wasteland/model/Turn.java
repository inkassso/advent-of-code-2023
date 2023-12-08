package com.github.inkassso.aoc2023.wasteland.model;

import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public enum Turn {
    LEFT('L'),
    RIGHT('R');

    private final char symbol;

    public static Turn findBySymbol(int symbol) {
        for (Turn turn : Turn.values()) {
            if (turn.symbol == symbol) {
                return turn;
            }
        }
        throw new NoSuchElementException("Turn not found for symbol: " + (char) symbol);
    }
}
