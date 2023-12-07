package com.github.inkassso.aoc2023.boatraces;

import com.github.inkassso.aoc2023.boatraces.model.RaceStat;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface RaceStatParser {
    List<RaceStat> parse() throws IOException, ParseException;
}
