package com.github.inkassso.aoc2023.wasteland.model;

import java.util.List;
import java.util.Map;

public record WastelandMap(List<Turn> instructions, Map<String, Node> nodes) {
}
