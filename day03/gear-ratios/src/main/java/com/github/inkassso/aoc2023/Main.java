package com.github.inkassso.aoc2023;

import com.github.inkassso.aoc2023.model.Field;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expected 1 argument, got " + args.length);
        }

        Field[][] fields;
        try (FileInputStream fileInputStream = new FileInputStream(args[0])) {
            var parser = new SchemaParser(fileInputStream);
            fields = parser.parse();
        } catch (ParseException e) {
            throw new IOException("Invalid input", e);
        }

        var detector = new PartIdDetector(fields);
        List<Integer> parts = detector.detectParts();
        log.debug("Detected parts: {}", parts);

        long sum = parts.stream()
                .mapToLong(id -> id)
                .sum();
        System.out.println("Sum of Part IDs: " + sum);
    }
}