package com.github.inkassso.aoc2023.pipemaze;

import com.github.inkassso.aoc2023.pipemaze.model.Direction;
import com.github.inkassso.aoc2023.pipemaze.model.Field;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FieldUtil {
    private static final Map<Field, Set<Direction>> DIRECTIONS_BY_FIELD = Map.of(
            Field.VERTICAL, Set.of(Direction.UP, Direction.DOWN),
            Field.HORIZONTAL, Set.of(Direction.LEFT, Direction.RIGHT),
            Field.NE_BEND, Set.of(Direction.UP, Direction.RIGHT),
            Field.NW_BEND, Set.of(Direction.UP, Direction.LEFT),
            Field.SE_BEND, Set.of(Direction.DOWN, Direction.RIGHT),
            Field.SW_BEND, Set.of(Direction.DOWN, Direction.LEFT)
    );
    private static final Map<Direction, Set<Field>> FIELDS_BY_DIRECTION = new EnumMap<>(Direction.class) {
        {
            for (Direction direction : Direction.values()) {
                put(direction, DIRECTIONS_BY_FIELD.entrySet().stream()
                        .filter(entry -> entry.getValue().contains(direction))
                        .map(Entry::getKey)
                        .collect(Collectors.toUnmodifiableSet()));
            }
        }
    };
    private static final Set<Field> PIPE_FIELDS = DIRECTIONS_BY_FIELD.keySet();

    private static Set<Direction> getDirections(Field field) {
        Set<Direction> directions = DIRECTIONS_BY_FIELD.get(field);
        if (directions == null) {
            throw new IllegalArgumentException("Not a pipe field: " + field);
        }
        return directions;
    }

    public static boolean isPipeField(Field field) {
        return PIPE_FIELDS.contains(field);
    }

    public static boolean connectsRight(Field field) {
        return isPipeField(field) && getDirections(field).contains(Direction.RIGHT);
    }

    public static boolean connectsLeft(Field field) {
        return isPipeField(field) && getDirections(field).contains(Direction.LEFT);
    }

    public static boolean connectsUp(Field field) {
        return isPipeField(field) && getDirections(field).contains(Direction.UP);
    }

    public static boolean connectsDown(Field field) {
        return isPipeField(field) && getDirections(field).contains(Direction.DOWN);
    }

    public static Direction getOtherDirection(Field field, Direction direction) {
        return getDirections(field).stream()
                .filter(Predicate.not(direction::equals))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("No other direction found: field=%s, direction=%s".formatted(field, direction)));
    }
}
