package com.github.inkassso.aoc2023.seeds;

import com.github.inkassso.aoc2023.seeds.model.Almanac;
import com.github.inkassso.aoc2023.seeds.model.CategoryMap;
import com.github.inkassso.aoc2023.seeds.model.Range;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Slf4j
@RequiredArgsConstructor
public class AlmanacParser {
    private static final Pattern SEEDS_LINE_PATTERN = Pattern.compile("^seeds:(?<seedIds>(?:\\s+\\d+)+)$");
    private static final Pattern CATEGORY_MAP_START = Pattern.compile("^(?<source>\\w+)-to-(?<destination>\\w+)\\s+map:$");
    private static final Pattern CATEGORY_MAP_ENTRY = Pattern.compile("^\\d+\\s+\\d+\\s+\\d+$");

    private final InputStream inputStream;
    private int lineNr = 0;

    public Almanac parse() throws IOException, ParseException {
        log.debug("Parsing almanac from input");

        int lineNr = 1;
        try (InputStreamReader isr = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(isr)) {

            Set<Long> seeds = parseInitialSeeds(reader);
            List<CategoryMap> maps = parseCategoryMaps(reader);

            log.debug("Parsed {} seeds and {} category maps", seeds.size(), maps.size());
            return new Almanac(seeds, maps);
        }

    }

    private Set<Long> parseInitialSeeds(BufferedReader reader) throws IOException, ParseException {
        log.trace("Parsing initial seeds");

        String seedsLine = readLine(reader);
        Matcher matcher = SEEDS_LINE_PATTERN.matcher(seedsLine);
        if (!matcher.matches()) {
            throw new ParseException("Invalid input, expected initial seeds line, actual: " + seedsLine, lineNr);
        }

        Set<Long> initialSeeds = parseNumberList(matcher.group("seedIds"))
                .boxed()
                .collect(Collectors.toSet());

        log.trace("Parsed {} initial seeds: {}", initialSeeds.size(), initialSeeds);
        return initialSeeds;
    }

    private List<CategoryMap> parseCategoryMaps(BufferedReader reader) throws IOException, ParseException {
        log.trace("Parsing category maps");
        List<CategoryMap> maps = new LinkedList<>();

        String currentSource = null;
        String currentDestination = null;
        List<Range> currentRanges = null;

        while (true) {
            String line = readLine(reader);
            if (line == null) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }

            Matcher matcher = CATEGORY_MAP_START.matcher(line);
            if (matcher.matches()) {
                if (currentRanges != null) {
                    CategoryMap categoryMap = new CategoryMap(currentSource, currentDestination, currentRanges);
                    maps.add(categoryMap);
                }

                currentSource = matcher.group("source");
                currentDestination = matcher.group("destination");
                currentRanges = new LinkedList<>();
                log.trace("Parsed category map header: source={}, destination={}", currentSource, currentDestination);
                continue;
            }

            matcher = CATEGORY_MAP_ENTRY.matcher(line);
            if (matcher.matches()) {
                if (currentRanges == null) {
                    throw new ParseException("Category map range definitions without category map header", lineNr);
                }
                long[] rangeDef = parseNumberList(line).toArray();
                var range = new Range(rangeDef[0], rangeDef[1], rangeDef[2]);
                currentRanges.add(range);
                log.trace("Parsed category map range: {}", range);
                continue;
            }

            throw new ParseException("Unexpected input, expected category map header or range definition, actual: " + line, lineNr);
        }

        if (currentRanges != null) {
            CategoryMap categoryMap = new CategoryMap(currentSource, currentDestination, currentRanges);
            maps.add(categoryMap);
        }

        log.trace("Parsed {} category maps: {}", maps.size(), maps);
        return maps;
    }

    private static LongStream parseNumberList(String listString) {
        return Arrays.stream(listString.split("\\s+"))
                .filter(Predicate.not(String::isEmpty))
                .mapToLong(Long::parseLong);
    }

    private String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null) {
            lineNr++;
        }
        return line;
    }
}
