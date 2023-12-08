package com.github.inkassso.aoc2023.wasteland.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum Turn {
    LEFT('L', Node::left),
    RIGHT('R', Node::right);

    private final char symbol;
    private final Function<Node, Node> childSelector;

    public static Turn findBySymbol(int symbol) {
        for (Turn turn : Turn.values()) {
            if (turn.symbol == symbol) {
                return turn;
            }
        }
        throw new NoSuchElementException("Turn not found for symbol: " + (char) symbol);
    }
}
