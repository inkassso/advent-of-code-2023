package com.github.inkassso.aoc2023.boatraces;

import com.github.inkassso.aoc2023.boatraces.model.RaceStat;
import com.github.inkassso.aoc2023.boatraces.model.TimeRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class RecordBeatingRaceStatsEvaluator implements RaceStatsEvaluator {
    private final List<RaceStat> raceStats;

    @Override
    public void evaluate() {
        long product = raceStats.stream()
                .map(this::evaluateRaceStat)
                .mapToLong(range -> {
                    if (range == null) {
                        return 0;
                    }
                    return range.max() - range.min() + 1;   // range is inclusive
                })
                .reduce((acc, count) -> acc * count)
                .orElse(0L);
        log.info("Product of multiplying the numbers of ways to beat the records: {}", product);
    }

    private TimeRange evaluateRaceStat(RaceStat stat) {
        log.trace("Calculating time range to beat the record: {}", stat);
        long middle = stat.time() / 2;

        log.trace("Counting distance with button release time starting in middle time: {}", middle);
        long offset = 0;
        while ((middle - offset) * (stat.time() - middle + offset) > stat.distance()) {
            offset++;
        }
        offset--;

        if (offset < 0) {
            log.trace("No thresholds found, race is impossible to beat");
            return null;
        }

        log.trace("Found thresholds: min={}, max={}", middle - offset, ((long) (Math.ceil(stat.time() / 2.0))) + offset);
        return new TimeRange(middle - offset, ((long) (Math.ceil(stat.time() / 2.0))) + offset);
    }
}
