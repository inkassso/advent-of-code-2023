package com.github.inkassso.aoc2023.model;

public class EmptyField implements Field {
    public static EmptyField INSTANCE = new EmptyField();

    private EmptyField() {
    }
}
