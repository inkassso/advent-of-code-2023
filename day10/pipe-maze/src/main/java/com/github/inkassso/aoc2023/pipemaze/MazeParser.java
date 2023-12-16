package com.github.inkassso.aoc2023.pipemaze;

import com.github.inkassso.aoc2023.pipemaze.model.Field;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

@Slf4j
@RequiredArgsConstructor
public class MazeParser {
    private static final String MAZE_LINE_PATTERN = "^[|\\-LJ7F.S]+$";

    private final InputStream inputStream;
    private int lineNr;

    public Field[][] parse() throws IOException, ParseException {
        log.debug("Parsing pipe maze from input");
        List<Field[]> maze = new LinkedList<>();
        int mazeLineLength = 0;

        try (InputStreamReader isr = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(isr)) {

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                lineNr++;
                Field[] mazeLine = parseMazeLine(line);

                if (mazeLineLength == 0) {
                    mazeLineLength = mazeLine.length;
                } else if (mazeLine.length != mazeLineLength) {
                    throw new ParseException("Unexpected maze line length: expected=%d, actual=%d".formatted(mazeLineLength, mazeLine.length), lineNr);
                }

                maze.add(mazeLine);
            }

            log.debug("Parsed {} maze lines, each {} fields long", maze.size(), mazeLineLength);

            return maze.toArray(Field[][]::new);
        }
    }

    private Field[] parseMazeLine(String line) throws IOException, ParseException {
        if (!line.matches(MAZE_LINE_PATTERN)) {
            throw new ParseException("Invalid maze line: " + line, lineNr);
        }
        return line.chars()
                .mapToObj(c -> (char) c)
                .map(Field::findBySymbol)
                .toArray(Field[]::new);
    }

}
