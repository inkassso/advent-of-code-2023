package com.github.inkassso.aoc2023.predictions;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.function.Function;

public class Main {
    private static final String PREDICT_NEXT = "predict-next";
    private static final String ESTIMATE_PREVIOUS = "estimate-previous";

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 argument, got " + args.length);
        }

        Function<List<List<Long>>, MetricEvaluator> evaluatorFactory = switch (args[0]) {
            case PREDICT_NEXT -> NextValuePredictingMetricEvaluator::new;
            case ESTIMATE_PREVIOUS -> PreviousValueEstimatingMetricEvaluator::new;
            default ->
                    throw new IllegalArgumentException("Invalid command: expected=[%s, %s], actual=%s".formatted(PREDICT_NEXT, ESTIMATE_PREVIOUS, args[0]));
        };

        List<List<Long>> map = parseRaceStats(args[1]);

        var evaluator = evaluatorFactory.apply(map);
        evaluator.evaluate();
    }

    private static List<List<Long>> parseRaceStats(String inputFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(inputFile)) {
            var parser = new EnvReportParser(fileInputStream);
            return parser.parse();
        } catch (ParseException e) {
            throw new IOException("Invalid input", e);
        }
    }
}