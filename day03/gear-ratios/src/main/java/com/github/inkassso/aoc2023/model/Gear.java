package com.github.inkassso.aoc2023.model;

public record Gear(int partId1, int partId2) {
    public int ratio() {
        return partId1 * partId2;
    }
}
