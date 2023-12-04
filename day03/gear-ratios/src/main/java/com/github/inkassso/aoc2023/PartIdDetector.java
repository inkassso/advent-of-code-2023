package com.github.inkassso.aoc2023;

import com.github.inkassso.aoc2023.model.Field;
import com.github.inkassso.aoc2023.model.PartIdField;
import com.github.inkassso.aoc2023.model.SymbolField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PartIdDetector implements Detector {
    private final Field[][] fields;

    @Override
    public void detect() {
        List<Integer> parts = detectParts();

        long sum = parts.stream()
                .mapToLong(id -> id)
                .sum();
        log.info("Sum of Part IDs: {}", sum);
    }

    public List<Integer> detectParts() {
        log.debug("Detecting parts among fields");

        List<Integer> parts = new LinkedList<>();

        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j] instanceof PartIdField partIdField && !partIdField.confirmed()) {
                    checkHasAdjacentSymbol(partIdField, i, j).ifPresent(parts::add);
                }
            }
        }

        log.debug("Detected {} parts among fields: {}", parts.size(), parts);
        return parts;
    }

    private Optional<Integer> checkHasAdjacentSymbol(PartIdField field, int row, int col) {
        log.trace("Found part {} on coordinates {}x{}, checking for adjacent symbols", field.partId(), row, col);

        List<Field> fieldsToCheck = new LinkedList<>();

        if (row > 0) {
            for (int j = (col > 0 ? col - 1 : 0); j < fields[row - 1].length && j <= col + 1; j++) {
                fieldsToCheck.add(fields[row - 1][j]);
            }
        }
        if (col > 0) {
            fieldsToCheck.add(fields[row][col - 1]);
        }
        if (col < fields[row].length - 1) {
            fieldsToCheck.add(fields[row][col + 1]);
        }
        if (row < fields.length - 1) {
            for (int j = (col > 0 ? col - 1 : 0); j < fields[row + 1].length && j <= col + 1; j++) {
                fieldsToCheck.add(fields[row + 1][j]);
            }
        }


        for (Field fieldToCheck : fieldsToCheck) {
            if (fieldToCheck instanceof SymbolField symbolField) {
                field.confirmed(true);
                log.trace("Found adjacent symbol: {}", symbolField);
                return Optional.of(field.partId());
            }
        }
        return Optional.empty();
    }
}
