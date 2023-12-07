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
public class SingleRaceStatParser implements RaceStatParser {
    private static final Pattern TIME_PATTERN = Pattern.compile("^Time:(?<time>(?:\\s+\\d+)+)$");
    private static final Pattern DISTANCE_PATTERN = Pattern.compile("^Distance:(?<distance>(?:\\s+\\d+)+)$");

    private final InputStream inputStream;
    private int lineNr = 0;

    @Override
    public List<RaceStat> parse() throws IOException, ParseException {
        log.debug("Parsing race stat from input");

        try (InputStreamReader isr = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(isr)) {

            Long time = parseTime(reader);
            Long distance = parseDistance(reader);

            log.debug("Parsed stats about single race: time={}, distance={}", time, distance);

            return List.of(new RaceStat(time, distance));
        }
    }

    private Long parseTime(BufferedReader reader) throws IOException, ParseException {
        String line = readLine(reader);
        Matcher matcher = TIME_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new ParseException("Unexpected input, expected race time", lineNr);
        }
        return Long.parseLong(matcher.group("time").replaceAll("\\s+", ""));

    }

    private Long parseDistance(BufferedReader reader) throws IOException, ParseException {
        String line = readLine(reader);
        Matcher matcher = DISTANCE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new ParseException("Unexpected input, expected race distance", lineNr);
        }
        return Long.parseLong(matcher.group("distance").replaceAll("\\s+", ""));
    }

    private String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null) {
            lineNr++;
        }
        return line;
    }
}
