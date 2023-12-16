package com.github.inkassso.aoc2023.pipemaze;

import com.github.inkassso.aoc2023.pipemaze.model.Field;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.function.Function;

public class Main {
    private static final String FARTHEST_POINT = "farthest-point";

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 argument, got " + args.length);
        }

        Function<Field[][], MazeEvaluator> pathFinderFactory = switch (args[0]) {
            case FARTHEST_POINT -> FarthestPointMazeEvaluator::new;
            default ->
                    throw new IllegalArgumentException("Invalid command: expected=[%s], actual=%s".formatted(FARTHEST_POINT, args[0]));
        };

        Field[][] maze = parseMaze(args[1]);

        var evaluator = pathFinderFactory.apply(maze);
        evaluator.evaluate();
    }

    private static Field[][] parseMaze(String inputFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            var parser = new MazeParser(fileInputStream);
            return parser.parse();
        } catch (ParseException e) {
            throw new IOException("Invalid input", e);
        }
    }
}