package com.github.inkassso.aoc2023.scratchcards;

import com.github.inkassso.aoc2023.scratchcards.model.ScratchCard;
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

@Slf4j
@RequiredArgsConstructor
public class ScratchCardParser {
    private static final Pattern CARD_LINE_PATTERN = Pattern.compile("^Card\\s+(?<cardId>\\d+):(?<winning>(?:\\s+\\d+)+)\\s+\\|(?<scratched>(?:\\s+\\d+)+)$");

    private final InputStream inputStream;

    public List<ScratchCard> parse() throws IOException, ParseException {
        log.debug("Parsing scratchcards from input");

        List<ScratchCard> cards = new LinkedList<>();

        int lineNr = 1;
        try (InputStreamReader isr = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(isr)) {

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                ScratchCard scratchCard = parseScratchCard(line, lineNr);
                cards.add(scratchCard);

                lineNr++;
            }
        }

        log.debug("Parsed {} cards", cards.size());
        return cards;
    }

    private ScratchCard parseScratchCard(String line, int lineNr) throws ParseException {
        log.trace("Parsing input line #{}", lineNr);

        Matcher matcher = CARD_LINE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new ParseException("Invalid card definition: " + line, lineNr);
        }

        int cardId = Integer.parseInt(matcher.group("cardId"));
        Set<Integer> winningNumbers = parseNumberList(matcher.group("winning"));
        Set<Integer> scratchedNumbers = parseNumberList(matcher.group("scratched"));

        log.trace("Parsed scratchcard: id={}, winning={}, scratched={}", cardId, winningNumbers, scratchedNumbers);
        return new ScratchCard(cardId, winningNumbers, scratchedNumbers);
    }

    private static Set<Integer> parseNumberList(String listString) {
        return Arrays.stream(listString.split("\\s+"))
                .filter(Predicate.not(String::isEmpty))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }
}
