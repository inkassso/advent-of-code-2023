package com.github.inkassso.aoc2023.seeds;

import com.github.inkassso.aoc2023.seeds.model.Almanac;
import com.github.inkassso.aoc2023.seeds.model.CategoryMap;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class SeedListLowestLocationSearch implements LowestLocationSearch {
    private static final String SEED_CATEGORY = "seed";
    private static final String LOCATION_CATEGORY = "location";

    private final Set<Long> initialSeedNumbers;
    private final Map<String, List<CategoryMap>> categoryMapsBySource;

    public SeedListLowestLocationSearch(Almanac almanac) {
        this.initialSeedNumbers = almanac.seeds();
        this.categoryMapsBySource = almanac.maps().stream()
                .collect(Collectors.groupingBy(CategoryMap::sourceCategory));
    }

    @Override
    public void evaluate() {
        log.debug("Evaluating category maps to reach location category with initial seed numbers: {}", initialSeedNumbers);
        Set<Long> locationNumbers = dfsLocations(SEED_CATEGORY, initialSeedNumbers);
        if (locationNumbers == null) {
            log.error("Location category could not be reached using category maps");
            return;
        }
        log.info("Lowest location number: {}", locationNumbers.stream().sorted().findFirst().map(Object::toString).orElse("<none>"));
    }

    private Set<Long> dfsLocations(String sourceCategory, Set<Long> numbers) {
        if (sourceCategory.equals(LOCATION_CATEGORY)) {
            log.debug("Reached location category with numbers: {}", numbers);
            return numbers;
        }

        List<CategoryMap> categoryMaps = categoryMapsBySource.get(sourceCategory);
        if (categoryMaps == null) {
            throw new IllegalStateException("Mapping to unknown category: " + sourceCategory);
        }

        for (CategoryMap destinationCategoryMap : categoryMaps) {
            log.trace("Mapping numbers between categories: source={}, destination={}", sourceCategory, destinationCategoryMap.destinationCategory());

            Set<Long> mappedNumbers = numbers.stream().map(destinationCategoryMap::mapItem).collect(Collectors.toSet());

            Set<Long> locationNumbers = dfsLocations(destinationCategoryMap.destinationCategory(), mappedNumbers);
            if (locationNumbers != null) {
                return locationNumbers;
            }
        }

        log.trace("No path through category maps found to location category: source={}", sourceCategory);
        return null;
    }
}
