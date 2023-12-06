package com.github.inkassso.aoc2023.seeds;

import com.github.inkassso.aoc2023.seeds.model.Almanac;
import com.github.inkassso.aoc2023.seeds.model.CategoryMap;
import com.github.inkassso.aoc2023.seeds.model.NumberRange;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class SeedRangesLowestLocationSearch implements LowestLocationSearch {
    private static final String SEED_CATEGORY = "seed";
    private static final String LOCATION_CATEGORY = "location";

    private final List<NumberRange> initialSeedRanges;
    private final Map<String, List<CategoryMap>> categoryMapsBySource;

    public SeedRangesLowestLocationSearch(Almanac almanac) {
        if (almanac.seeds().size() % 2 == 1) {
            throw new IllegalArgumentException("Invalid initial seed definition, expected even amount of numbers as range definitions");
        }
        this.initialSeedRanges = new ArrayList<>(almanac.seeds().size() / 2);
        for (int i = 0; i < almanac.seeds().size(); i+= 2) {
            Long rangeStart = almanac.seeds().get(i);
            Long rangeLength = almanac.seeds().get(i + 1);
            if (rangeLength > 0) {
                initialSeedRanges.add(new NumberRange(rangeStart, rangeLength));
            } else {
                log.warn("Ignoring empty initial seed range: start={}", rangeStart);
            }
        }

        this.categoryMapsBySource = almanac.maps().stream()
                .collect(Collectors.groupingBy(CategoryMap::sourceCategory));
    }

    @Override
    public void evaluate() {
        log.debug("Evaluating category maps to reach location category with initial seed ranges: {}", initialSeedRanges);
        List<NumberRange> locationNumbers = dfsLocations(SEED_CATEGORY, initialSeedRanges);
        if (locationNumbers == null) {
            log.error("Location category could not be reached using category maps");
            return;
        }
        log.info("Lowest location number: {}", locationNumbers.stream().min(Comparator.comparingLong(NumberRange::start)).map(Object::toString).orElse("<none>"));
    }

    private List<NumberRange> dfsLocations(String sourceCategory, List<NumberRange> numberRanges) {
        if (sourceCategory.equals(LOCATION_CATEGORY)) {
            log.trace("Reached location category with number ranges: {}", numberRanges);
            return numberRanges;
        }

        List<CategoryMap> categoryMaps = categoryMapsBySource.get(sourceCategory);
        if (categoryMaps == null) {
            throw new IllegalStateException("Mapping to unknown category: " + sourceCategory);
        }

        for (CategoryMap destinationCategoryMap : categoryMaps) {
            log.trace("Mapping numbers between categories: source={}, destination={}", sourceCategory, destinationCategoryMap.destinationCategory());

            List<NumberRange> mappedNumberRanges = destinationCategoryMap.mapRanges(numberRanges);

            List<NumberRange> locationNumbers = dfsLocations(destinationCategoryMap.destinationCategory(), mappedNumberRanges);
            if (locationNumbers != null) {
                return locationNumbers;
            }
        }

        log.trace("No path through category maps found to location category: source={}", sourceCategory);
        return null;
    }
}
