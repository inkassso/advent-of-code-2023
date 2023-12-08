package com.github.inkassso.aoc2023.camelcards;

import com.github.inkassso.aoc2023.camelcards.model.Card;
import com.github.inkassso.aoc2023.camelcards.model.Turn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class RoundParser {
    private static final Pattern TURN_PATTERN = Pattern.compile("^(?<hand>[2-9TJQKA]{5})\\s+(?<bid>\\d+)$");

    private final InputStream inputStream;
    private final Function<Character, Card> cardFactory;
    private int lineNr = 0;

    public List<Turn> parse() throws IOException, ParseException {
        log.debug("Parsing turns from input");

        try (InputStreamReader isr = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(isr)) {

            List<Turn> turns = parseTurns(reader);

            log.debug("Parsed {} turns", turns.size());

            return turns;
        }
    }

    private List<Turn> parseTurns(BufferedReader reader) throws IOException, ParseException {
        List<Turn> turns = new LinkedList<>();
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            lineNr++;

            Turn turn = parseTurn(line);
            turns.add(turn);
        }
        return turns;
    }

    private Turn parseTurn(String line) throws ParseException {
        Matcher matcher = TURN_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new ParseException("Unexpected input, expected hand and bid", lineNr);
        }
        List<Card> hand = matcher.group("hand").chars()
                .mapToObj(i -> (char) i)
                .map(cardFactory)
                .toList();
        long bid = Long.parseLong(matcher.group("bid"));
        return new Turn(hand, bid);
    }
}
