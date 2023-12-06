package com.github.inkassso.aoc2023.seeds;

import com.github.inkassso.aoc2023.seeds.model.Almanac;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.function.Function;

public class Main {
    private static final String SEED_LIST = "seed-list";
    private static final String SEED_RANGES = "seed-ranges";

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 argument, got " + args.length);
        }

        Function<Almanac, LowestLocationSearch> evaluatorFactory = switch (args[0]) {
            case SEED_LIST -> SeedListLowestLocationSearch::new;
            case SEED_RANGES -> SeedRangesLowestLocationSearch::new;
            default -> throw new IllegalArgumentException("Invalid command: expected=[%s, %s], actual=%s".formatted(SEED_LIST, SEED_RANGES, args[0]));
        };

        Almanac almanac = parseAlmanac(args[1]);

        var evaluator = evaluatorFactory.apply(almanac);
        evaluator.evaluate();
    }

    private static Almanac parseAlmanac(String inputFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            var parser = new AlmanacParser(fileInputStream);
            return parser.parse();
        } catch (ParseException e) {
            throw new IOException("Invalid input", e);
        }
    }
}