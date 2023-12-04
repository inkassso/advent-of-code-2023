package com.github.inkassso.aoc2023.scratchcards;

import com.github.inkassso.aoc2023.scratchcards.model.ScratchCard;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected 1 argument, got " + args.length);
        }

        List<ScratchCard> scratchCards = parseScratchCards(args[0]);
        new ScratchCardScoreEvaluator(scratchCards).evaluate();
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