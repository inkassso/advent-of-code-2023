package com.github.inkassso.aoc2023.camelcards;

import com.github.inkassso.aoc2023.camelcards.model.Card;
import com.github.inkassso.aoc2023.camelcards.model.Turn;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.function.Function;

public class Main {
    private static final String WITH_JACK = "with-jack";
    private static final String WITH_JOKER = "with-joker";

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 argument, got " + args.length);
        }

        Function<Character, Card> cardFactory = switch (args[0]) {
            case WITH_JACK -> CardLookup::findBySymbolWithJack;
            case WITH_JOKER -> CardLookup::findBySymbolWithJoker;
            default ->
                    throw new IllegalArgumentException("Invalid command: expected=[%s, %s], actual=%s".formatted(WITH_JACK, WITH_JOKER, args[0]));
        };

        List<Turn> turns = parseRaceStats(args[1], cardFactory);

        var evaluator = new TotalWinningsHandsEvaluator(turns);
        evaluator.evaluate();
    }

    private static List<Turn> parseRaceStats(String inputFile, Function<Character, Card> cardFactory) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            var parser = new RoundParser(fileInputStream, cardFactory);
            return parser.parse();
        } catch (ParseException e) {
            throw new IOException("Invalid input", e);
        }
    }
}