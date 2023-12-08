package com.github.inkassso.aoc2023.wasteland;

import com.github.inkassso.aoc2023.wasteland.model.Node;
import com.github.inkassso.aoc2023.wasteland.model.Turn;
import com.github.inkassso.aoc2023.wasteland.model.WastelandMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MapParser {
    private static final Pattern INSTRUCTIONS_PATTERN = Pattern.compile("^[RL]+$");
    private static final Pattern NODE_PATTERN = Pattern.compile("^(?<label>\\w+)\\s+=\\s+\\((?<left>\\w+),\\s+(?<right>\\w+)\\)$");

    private final InputStream inputStream;
    private int lineNr = 0;

    public WastelandMap parse() throws IOException, ParseException {
        log.debug("Parsing turns from input");

        try (InputStreamReader isr = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(isr)) {

            List<Turn> turns = parseTurns(reader);
            Map<String, Node> nodes = parseNodes(reader);

            log.debug("Parsed {} turns and {} nodes", turns.size(), nodes.size());

            return new WastelandMap(turns, nodes);
        }
    }

    private List<Turn> parseTurns(BufferedReader reader) throws IOException, ParseException {
        String line = readLine(reader);

        Matcher matcher = INSTRUCTIONS_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new ParseException("Invalid instructions line, expected left-right instructions: " + line, lineNr);
        }
        return line.chars()
                .mapToObj(c -> (char) c)
                .map(Turn::findBySymbol)
                .toList();
    }

    private Map<String, Node> parseNodes(BufferedReader reader) throws ParseException, IOException {
        Map<String, Node> nodes = new HashMap<>();

        while (true) {
            String line = readLine(reader);
            if (line == null) {
                break;
            }
            if (line.isEmpty()) {
                continue;
            }

            Matcher matcher = NODE_PATTERN.matcher(line);
            if (!matcher.matches()) {
                throw new ParseException("Invalid node definition, expected left-right traversals", lineNr);
            }

            String label = matcher.group("label");
            String left = matcher.group("left");
            String right = matcher.group("right");

            Node node = nodes.computeIfAbsent(label, Node::new);
            Node leftChild = nodes.computeIfAbsent(left, Node::new);
            Node rightChild = nodes.computeIfAbsent(right, Node::new);
            node.setChildren(leftChild, rightChild);

            log.trace("Parsed node traversal: {}", node);
        }

        List<Node> onlyDeclared = nodes.values().stream()
                .filter(node -> node.left() == null || node.right() == null)
                .toList();
        if (!onlyDeclared.isEmpty()) {
            throw new ParseException("%s nodes were referenced but not defined: [%s]".formatted(
                    onlyDeclared.size(), onlyDeclared.stream().map(Node::label).collect(Collectors.joining(", "))), lineNr);
        }

        return nodes;
    }

    private String readLine(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        lineNr++;
        return line;
    }
}
