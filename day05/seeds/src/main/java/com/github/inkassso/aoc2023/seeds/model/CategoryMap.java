package com.github.inkassso.aoc2023.seeds.model;

import java.util.List;

public record CategoryMap(
        String sourceCategory,
        String destinationCategory,
        List<Range> ranges
) {
    public long mapItem(long sourceNumber) {
        for (Range range : ranges) {
            long offset = sourceNumber - range.sourceStart();
            if (offset >= 0 && offset < range.length()) {
                return range.destinationStart() + offset;
            }
        }
        return sourceNumber;
    }
}
