package com.github.inkassso.aoc2023.predictions;

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
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
public class EnvReportParser {
    private static final String METRIC_HISTORY = "^-?\\d+(?:\\s+-?\\d+)+$";

    private final InputStream inputStream;
    private int lineNr = 0;

    public List<List<Long>> parse() throws IOException, ParseException {
        log.debug("Parsing turns from input");
        List<List<Long>> metrics = new LinkedList<>();

        try (InputStreamReader isr = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(isr)) {

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                lineNr++;
                if (!line.matches(METRIC_HISTORY)) {
                    throw new ParseException("Invalid metric history: " + line, lineNr);
                }

                List<Long> history = parseNumberList(line);
                metrics.add(history);
            }

            log.debug("Parsed {} metrics", metrics.size());

            return metrics;
        }
    }

    private static List<Long> parseNumberList(String listString) {
        return Arrays.stream(listString.split("\\s+"))
                .filter(Predicate.not(String::isEmpty))
                .map(Long::parseLong)
                .toList();
    }
}
