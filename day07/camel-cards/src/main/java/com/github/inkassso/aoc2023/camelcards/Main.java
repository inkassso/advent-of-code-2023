package com.github.inkassso.aoc2023.camelcards;

import com.github.inkassso.aoc2023.camelcards.model.Turn;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.function.Function;

public class Main {
    private static final String TOTAL_WINNINGS = "total-winnings";

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 argument, got " + args.length);
        }

        Function<List<Turn>, HandsEvaluator> evaluatorFactory = switch (args[0]) {
            case TOTAL_WINNINGS -> TotalWinningsHandsEvaluator::new;
            default -> throw new IllegalArgumentException("Invalid command: expected=[%s], actual=%s".formatted(TOTAL_WINNINGS, args[0]));
        };

        List<Turn> turns = parseRaceStats(args[1]);

        var evaluator = evaluatorFactory.apply(turns);
        evaluator.evaluate();
    }

    private static List<Turn> parseRaceStats(String inputFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            var parser = new RoundParser(fileInputStream);
            return parser.parse();
        } catch (ParseException e) {
            throw new IOException("Invalid input", e);
        }
    }
}