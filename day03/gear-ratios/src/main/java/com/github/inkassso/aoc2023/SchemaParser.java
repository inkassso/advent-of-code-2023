package com.github.inkassso.aoc2023;

import com.github.inkassso.aoc2023.model.EmptyField;
import com.github.inkassso.aoc2023.model.Field;
import com.github.inkassso.aoc2023.model.PartIdField;
import com.github.inkassso.aoc2023.model.SymbolField;
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
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class SchemaParser {
    private final InputStream inputStream;

    public Field[][] parse() throws IOException, ParseException {
        log.debug("Parsing fields from input");

        List<Field[]> fields = new LinkedList<>();

        int lineNr = 1;
        int lineLength = -1;

        try (InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr)) {

            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (line.length() != lineLength) {
                    if (lineLength != -1) {
                        throw new ParseException("Unexpected line length: expected=%d, actual=%d".formatted(lineLength, line.length()), (lineNr - 1) * lineLength);
                    }
                    lineLength = line.length();
                }

                Field[] fieldLine = parseFieldLine(line, lineNr);
                fields.add(fieldLine);

                lineNr++;
            }
        }

        log.debug("Parsed {} lines, each {} chars long", fields.size(), lineLength);
        return fields.toArray(new Field[fields.size()][]);
    }

    private Field[] parseFieldLine(String line, int lineNr) {
        log.trace("Parsing input line #{}", lineNr);
        Pattern expressionPattern = Pattern.compile("(?<partId>\\d+)|(?<symbol>[^.])");

        Field[] fields = new Field[line.length()];
        int previousMatchEnd = 0;

        Matcher matcher = expressionPattern.matcher(line);
        while (matcher.find()) {
            log.trace("Found expression on position {}-{}: {}", matcher.start(), matcher.end(), matcher.group());

            // fill void from previous end to current match start with empty fields
            for (int i = previousMatchEnd; i < matcher.start(); i++) {
                fields[i] = EmptyField.INSTANCE;
            }

            String partId = matcher.group("partId");
            String symbol = matcher.group("symbol");

            Field field;
            if (partId != null) {
                field = new PartIdField(Integer.parseInt(partId));
            } else if (symbol != null) {
                if (symbol.length() != 1) {
                    throw new IllegalStateException("Invalid symbol, expected single character: " + symbol);
                }
                field = new SymbolField(symbol.charAt(0));
            } else {
                throw new IllegalStateException("Neither part ID nor symbol captured");
            }

            for (int i = matcher.start(); i < matcher.end(); i++) {
                fields[i] = field;
            }
            previousMatchEnd = matcher.end();
        }

        for (int i = previousMatchEnd; i < fields.length; i++) {
            fields[i] = EmptyField.INSTANCE;
        }

        return fields;
    }
}
