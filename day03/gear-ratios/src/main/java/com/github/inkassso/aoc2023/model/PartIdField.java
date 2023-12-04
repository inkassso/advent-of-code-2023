package com.github.inkassso.aoc2023.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class PartIdField implements Field {
    private final int partId;

    @Setter
    private boolean confirmed;
}
