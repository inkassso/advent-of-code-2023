package com.github.inkassso.aoc2023.boatraces;

import com.github.inkassso.aoc2023.boatraces.model.RaceStat;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.function.Function;

public class Main {
    private static final String BEAT_RECORDS = "beat-records";

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 argument, got " + args.length);
        }

        Function<List<RaceStat>, RaceStatsEvaluator> evaluatorFactory = switch (args[0]) {
            case BEAT_RECORDS -> RecordBeatingRaceStatsEvaluator::new;
            default -> throw new IllegalArgumentException("Invalid command: expected=[%s], actual=%s".formatted(BEAT_RECORDS, args[0]));
        };

        List<RaceStat> raceStats = parseRaceStats(args[1]);

        var evaluator = evaluatorFactory.apply(raceStats);
        evaluator.evaluate();
    }

    private static List<RaceStat> parseRaceStats(String inputFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            var parser = new RaceStatParser(fileInputStream);
            return parser.parse();
        } catch (ParseException e) {
            throw new IOException("Invalid input", e);
        }
    }
}