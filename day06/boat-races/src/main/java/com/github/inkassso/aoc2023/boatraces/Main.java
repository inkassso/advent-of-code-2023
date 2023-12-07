package com.github.inkassso.aoc2023.boatraces;

import com.github.inkassso.aoc2023.boatraces.model.RaceStat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.function.Function;

public class Main {
    private static final String MULTIPLE = "multiple";
    private static final String SINGLE = "single";

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 argument, got " + args.length);
        }

        Function<InputStream, RaceStatParser> parserFactory = switch (args[0]) {
            case MULTIPLE -> MultipleRaceStatParser::new;
            case SINGLE -> SingleRaceStatParser::new;
            default -> throw new IllegalArgumentException("Invalid command: expected=[%s, %s], actual=%s".formatted(MULTIPLE, SINGLE, args[0]));
        };

        List<RaceStat> raceStats = parseRaceStats(args[1], parserFactory);

        var evaluator = new RecordBeatingRaceStatsEvaluator(raceStats);
        evaluator.evaluate();
    }

    private static List<RaceStat> parseRaceStats(String inputFile, Function<InputStream, RaceStatParser> parserFactory) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            var parser = parserFactory.apply(fileInputStream);
            return parser.parse();
        } catch (ParseException e) {
            throw new IOException("Invalid input", e);
        }
    }
}