package com.github.inkassso.aoc2023.wasteland.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
public class Node {
    private final String label;

    @EqualsAndHashCode.Exclude
    private Node left;
    @EqualsAndHashCode.Exclude
    private Node right;

    public void setChildren(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        if (left == null && right == null) {
            return "Node{%s}".formatted(label);
        }
        return "Node{%s -> (%s, %s)}".formatted(label, left.label, right.label);
    }
}
