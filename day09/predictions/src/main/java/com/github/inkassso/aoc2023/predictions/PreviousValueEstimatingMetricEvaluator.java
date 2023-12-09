package com.github.inkassso.aoc2023.predictions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class PreviousValueEstimatingMetricEvaluator implements MetricEvaluator {
    private final List<List<Long>> metrics;

    @Override
    public void evaluate() {
        log.debug("Evaluating the history of all metrics to estimate their previous values");

        long sumOfPredictions = metrics.stream()
                .mapToLong(this::predictPreviousValue)
                .sum();

        log.info("Sum of estimated previous values: {}", sumOfPredictions);
    }

    private long predictPreviousValue(List<Long> metricHistory) {
        if (log.isTraceEnabled()) {
            Stream<String> historyStr = metricHistory.size() <= 10 ? metricHistory.stream().map(Object::toString) :
                    Stream.concat(
                            Stream.concat(metricHistory.stream().limit(5).map(Objects::toString), Stream.of("...")),
                            metricHistory.stream().skip(metricHistory.size() - 5).map(Objects::toString)
                    );
            log.trace("Evaluating metric history ({}): [{}]", metricHistory.size(), historyStr);
        }

        if (metricHistory.size() == 1) {
            long value = metricHistory.get(0);
            if (value != 0L) {
                log.warn("Returning the only non-zero value in metric: {}", value);
            }
            return value;
        }
        if (metricHistory.stream().allMatch(Long.valueOf(0L)::equals)) {
            log.trace("Recursion end reached, sequence contains {} zeros", metricHistory.size());
            return 0L;
        }

        log.trace("Building difference sequence for a deeper recursion level");
        List<Long> differences = new ArrayList<>(metricHistory.size() - 1);
        metricHistory.stream()
                .reduce((previous, current) -> {
                    differences.add(current - previous);
                    return current;
                })
                .orElseThrow(() -> new IllegalStateException("Metric should have had at least 2 elements"));

        long firstValue = metricHistory.get(0);
        long estimatedPreviousValue = firstValue - predictPreviousValue(differences);
        log.trace("Value estimation finished: first={}, estimatedPrevious={}", firstValue, estimatedPreviousValue);
        return estimatedPreviousValue;
    }
}
