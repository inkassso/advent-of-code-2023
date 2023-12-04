package com.github.inkassso.aoc2023.scratchcards;

import com.github.inkassso.aoc2023.scratchcards.model.ScratchCard;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.function.Function;

public class Main {
    private static final String EVALUATE_SCORES = "eval-scores";
    private static final String COUNT_CARDS = "count-cards";
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 argument, got " + args.length);
        }

        Function<List<ScratchCard>, Evaluator> evaluatorFactory = switch (args[0]) {
            case EVALUATE_SCORES -> ScoreEvaluator::new;
            case COUNT_CARDS -> CountEvaluator::new;
            default -> throw new IllegalArgumentException("Invalid command: expected=[%s, %S], actual=%s".formatted(EVALUATE_SCORES, COUNT_CARDS, args[0]));
        };

        List<ScratchCard> scratchCards = parseScratchCards(args[1]);

        var evaluator = evaluatorFactory.apply(scratchCards);
        evaluator.evaluate();
    }

    private static List<ScratchCard> parseScratchCards(String inputFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            var parser = new ScratchCardParser(fileInputStream);
            return parser.parse();
        } catch (ParseException e) {
            throw new IOException("Invalid input", e);
        }
    }
}