package com.github.inkassso.aoc2023.wasteland.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class Node {
    private final String label;

    @Setter
    private Node left;
    @Setter
    private Node right;
}
