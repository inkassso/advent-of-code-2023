package com.github.inkassso.aoc2023.pipemaze.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public enum Direction {
    LEFT(p -> p.translate(-1, 0)),
    RIGHT(p -> p.translate(1, 0)),
    UP(p -> p.translate(0, -1)),
    DOWN(p -> p.translate(0, 1));

    static {
        LEFT.opposite = RIGHT;
        RIGHT.opposite = LEFT;
        UP.opposite = DOWN;
        DOWN.opposite = UP;
    }

    private final Consumer<Point> translator;

    @Getter
    private Direction opposite;

    public void apply(Point point) {
        log.trace("Moving: fromPosition=({}x{}), direction={}", point.x, point.y, this);
        translator.accept(point);
    }
}
