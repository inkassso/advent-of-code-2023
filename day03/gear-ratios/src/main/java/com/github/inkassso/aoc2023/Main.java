package com.github.inkassso.aoc2023;

import com.github.inkassso.aoc2023.model.Field;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.function.Function;

@Slf4j
public class Main {
    private static final String DETECT_PART_IDS = "detect-part-ids";
    private static final String DETECT_GEARS = "detect-gears";

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 argument, got " + args.length);
        }

        Function<Field[][], Detector> detectorFactory = switch (args[0]) {
            case DETECT_PART_IDS -> PartIdDetector::new;
            case DETECT_GEARS -> GearDetector::new;
            default -> throw new IllegalArgumentException("Invalid command: expected=[%s, %S], actual=%s".formatted(DETECT_PART_IDS, DETECT_GEARS, args[0]));
        };

        Field[][] fields = parseFields(args[1]);

        Detector detector = detectorFactory.apply(fields);
        detector.detect();
    }

    private static Field[][] parseFields(String inputFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            var parser = new SchemaParser(fileInputStream);
            return parser.parse();
        } catch (ParseException e) {
            throw new IOException("Invalid input", e);
        }
    }
}