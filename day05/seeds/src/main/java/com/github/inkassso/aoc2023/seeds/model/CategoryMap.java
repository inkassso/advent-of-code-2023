package com.github.inkassso.aoc2023.seeds.model;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public record CategoryMap(
        String sourceCategory,
        String destinationCategory,
        List<MappingRange> mappingRanges
) {
    public long mapItem(long sourceNumber) {
        for (MappingRange mappingRange : mappingRanges) {
            long offset = sourceNumber - mappingRange.sourceStart();
            if (offset >= 0 && offset < mappingRange.length()) {
                return mappingRange.destinationStart() + offset;
            }
        }
        return sourceNumber;
    }

    public List<NumberRange> mapRanges(List<NumberRange> numberRanges) {
        Queue<NumberRange> remainingNumberRanges = new LinkedList<>(numberRanges);
        List<NumberRange> destinationRanges = new LinkedList<>();

        // implemented with a queue of ranges to evaluate
        while (!remainingNumberRanges.isEmpty()) {
            var numberRange = remainingNumberRanges.poll();

            long numberRangeStart = numberRange.start();
            long numberRangeEnd = numberRange.start() + numberRange.length();
            boolean numberRangeOverlapped = false;
            log.trace("Mapping number range: {} -> {}", numberRangeStart, numberRangeEnd);

            for (MappingRange mappingRange : mappingRanges) {
                long mappingRangeStart = mappingRange.sourceStart();
                long mappingRangeEnd = mappingRange.sourceStart() + mappingRange.length();
                log.trace("Checking number range overlap with mapping range: {} -> {}", mappingRangeStart, mappingRangeEnd);

                if (numberRangeStart <= mappingRangeStart && numberRangeEnd > mappingRangeStart) {
                    long numbersBeforeMappingRangeStartCount = mappingRangeStart - numberRangeStart;
                    if (numbersBeforeMappingRangeStartCount > 0) {
                        log.trace("{} numbers from number range remaining before map start, returning to queue for further processing", numbersBeforeMappingRangeStartCount);
                        remainingNumberRanges.add(new NumberRange(numberRangeStart, numbersBeforeMappingRangeStartCount));
                        numberRangeOverlapped = true;
                    }
                }
                if (numberRangeStart < mappingRangeEnd && numberRangeEnd > mappingRangeStart) {
                    long rangeStartOffset = 0;
                    long rangeLength = mappingRange.length();

                    long numbersAfterMappingRangeEndCount = numberRangeEnd - mappingRangeEnd;
                    if (numbersAfterMappingRangeEndCount > 0) {
                        log.trace("{} numbers from number range remaining after map end, returning to queue for further processing", numbersAfterMappingRangeEndCount);
                        remainingNumberRanges.add(new NumberRange(mappingRangeEnd, numbersAfterMappingRangeEndCount));
                    } else {
                        rangeLength += numbersAfterMappingRangeEndCount;
                    }

                    if (numberRangeStart > mappingRangeStart) {
                        rangeStartOffset = numberRangeStart - mappingRangeStart;
                        rangeLength -= rangeStartOffset;
                    }
                    log.trace("Mapped numbers from source range [{} -> {}] to destination range [{} -> {}]",
                            mappingRange.sourceStart() + rangeStartOffset, mappingRange.sourceStart() + rangeStartOffset + rangeLength,
                            mappingRange.destinationStart() + rangeStartOffset, mappingRange.destinationStart() + rangeStartOffset + rangeLength);
                    destinationRanges.add(new NumberRange(mappingRange.destinationStart() + rangeStartOffset, rangeLength));
                    numberRangeOverlapped = true;
                }
            }

            if (!numberRangeOverlapped) {
                destinationRanges.add(numberRange);
            }
        }

        return consolidateRanges(destinationRanges);
    }

    private List<NumberRange> consolidateRanges(List<NumberRange> ranges) {
        if (ranges.size() < 2) {
            return ranges;
        }
        log.trace("Consolidating ranges to merge overlaps");
        LinkedList<NumberRange> sortedRanges = ranges.stream()
                .sorted(Comparator.comparingLong(NumberRange::start))
                .collect(Collectors.toCollection(LinkedList::new));

        var rangeIterator = sortedRanges.listIterator();
        NumberRange previous = rangeIterator.next();
        while (rangeIterator.hasNext()) {
            NumberRange current = rangeIterator.next();
            if (previous.start() + previous.length() >= current.start()) {
                log.trace("Merging ranges: previous={}, current={}", previous, current);
                rangeIterator.remove();
                rangeIterator.previous();
                if (previous.start() + previous.length() < current.start() + current.length()) {
                    previous = new NumberRange(previous.start(), current.start() + current.length() - previous.start());
                    rangeIterator.set(previous);
                }
                rangeIterator.next();
            } else {
                previous = current;
            }
        }

        return sortedRanges;
    }
}
