package com.github.inkassso.aoc2023.wasteland;

import com.github.inkassso.aoc2023.wasteland.model.WastelandMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.function.Function;

public class Main {
    private static final String SINGLE_PATH = "single-path";
    private static final String CONCURRENT_PATHS = "concurrent-paths";

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 argument, got " + args.length);
        }

        Function<WastelandMap, PathFinder> pathFinderFactory = switch (args[0]) {
            case SINGLE_PATH -> SinglePathPathFinder::new;
            case CONCURRENT_PATHS -> ConcurrentPathsPathFinder::new;
            default ->
                    throw new IllegalArgumentException("Invalid command: expected=[%s, %s], actual=%s".formatted(SINGLE_PATH, CONCURRENT_PATHS, args[0]));
        };

        WastelandMap map = parseRaceStats(args[1]);

        var evaluator = pathFinderFactory.apply(map);
        evaluator.findPath();
    }

    private static WastelandMap parseRaceStats(String inputFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            var parser = new MapParser(fileInputStream);
            return parser.parse();
        } catch (ParseException e) {
            throw new IOException("Invalid input", e);
        }
    }
}