package com.github.inkassso.aoc2023.boatraces;

import com.github.inkassso.aoc2023.boatraces.model.RaceStat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
@RequiredArgsConstructor
public class RaceStatParser {
    private static final Pattern TIME_PATTERN = Pattern.compile("^Time:(?<times>(?:\\s+\\d+)+)$");
    private static final Pattern DISTANCE_PATTERN = Pattern.compile("^Distance:(?<distances>(?:\\s+\\d+)+)$");

    private final InputStream inputStream;
    private int lineNr = 0;

    public List<RaceStat> parse() throws IOException, ParseException {
        log.debug("Parsing race stats from input");

        try (InputStreamReader isr = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(isr)) {

            List<Long> times = parseTimes(reader);
            List<Long> distances = parseDistances(reader);
            if (times.size() != distances.size()) {
                throw new ParseException("Invalid input, unequal number of times and distances: nrOfTimes=%d, nrOfDistances=%d".formatted(times.size(), distances.size()), lineNr);
            }

            log.debug("Parsed stats about {} races", times.size());

            return IntStream.range(0, times.size())
                    .mapToObj(i -> new RaceStat(times.get(i), distances.get(i)))
                    .toList();
        }
    }

    private List<Long> parseTimes(BufferedReader reader) throws IOException, ParseException {
        String line = readLine(reader);
        Matcher matcher = TIME_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new ParseException("Unexpected input, expected race times", lineNr);
        }
        return parseNumberList(matcher.group("times")).boxed().toList();
    }

    private List<Long> parseDistances(BufferedReader reader) throws IOException, ParseException {
        String line = readLine(reader);
        Matcher matcher = DISTANCE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new ParseException("Unexpected input, expected race distances", lineNr);
        }
        return parseNumberList(matcher.group("distances")).boxed().toList();
    }

    private static LongStream parseNumberList(String listString) {
        return Arrays.stream(listString.split("\\s+"))
                .filter(Predicate.not(String::isEmpty))
                .mapToLong(Long::parseLong);
    }

    private String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null) {
            lineNr++;
        }
        return line;
    }
}
