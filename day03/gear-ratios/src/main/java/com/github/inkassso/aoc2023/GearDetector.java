package com.github.inkassso.aoc2023;

import com.github.inkassso.aoc2023.model.Field;
import com.github.inkassso.aoc2023.model.Gear;
import com.github.inkassso.aoc2023.model.PartIdField;
import com.github.inkassso.aoc2023.model.SymbolField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class GearDetector implements Detector {
    public static final char ASTERISK = '*';
    private final Field[][] fields;

    @Override
    public void detect() {
        List<Gear> parts = detectGears();

        long sum = parts.stream()
                .mapToLong(Gear::ratio)
                .sum();
        log.info("Sum of Gear ratios: {}", sum);
    }

    public List<Gear> detectGears() {
        log.debug("Detecting gears among fields");

        List<Gear> parts = new LinkedList<>();

        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j] instanceof SymbolField symbolField && symbolField.symbol() == ASTERISK) {
                    checkHasExactlyTwoAdjacentPartIds(symbolField, i, j).ifPresent(parts::add);
                }
            }
        }

        log.debug("Detected {} gears among fields: {}", parts.size(), parts);
        return parts;
    }

    private Optional<Gear> checkHasExactlyTwoAdjacentPartIds(SymbolField field, int row, int col) {
        log.trace("Found gear on coordinates {}x{}, checking for adjacent part IDs", row, col);

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

        List<PartIdField> adjacentPartIds = new ArrayList<>(8); // 8 is the max amount of neighboring fields
        for (Field fieldToCheck : fieldsToCheck) {
            if (fieldToCheck instanceof PartIdField partIdField && !partIdField.confirmed()) {
                partIdField.confirmed(true);
                log.trace("Found adjacent part ID: {}", partIdField.partId());
                adjacentPartIds.add(partIdField);
            }
        }
        if (adjacentPartIds.size() != 2) {
            return Optional.empty();
        }
        return Optional.of(new Gear(adjacentPartIds.get(0).partId(), adjacentPartIds.get(1).partId()));
    }
}
