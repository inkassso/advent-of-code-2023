package com.github.inkassso.aoc2023.pipemaze;

import com.github.inkassso.aoc2023.pipemaze.model.Direction;
import com.github.inkassso.aoc2023.pipemaze.model.Field;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class FarthestPointMazeEvaluator implements MazeEvaluator {
    private final Field[][] maze;

    @Override
    public void evaluate() {
        log.debug("Finding farthest point from start and path length from start");

        Point start = findStart();

        long pathLength = findAndMeasurePath(start);
        log.info("Path: length={}, farthestPoint={}", pathLength, (long) Math.ceil(pathLength * 0.5));
    }

    private Point findStart() {
        log.debug("Finding starting point");

        int y = 0;
        for (Field[] mazeLine : maze) {
            int x = 0;
            for (Field field : mazeLine) {
                if (field == Field.START) {
                    log.debug("Found start at position: x={}, y={}", x, y);
                    return new Point(x, y);
                }
                ++x;
            }
            ++y;
        }

        throw new IllegalArgumentException("Maze has no starting node");
    }

    private long findAndMeasurePath(Point start) {
        Set<Direction> possibleDirections = getPossibleDirections(start);
        Direction direction = possibleDirections.iterator().next();
        log.debug("Found possible directions from start: options={}, chosen={}", possibleDirections, direction);

        return followPathAndCalculate(start, direction);
    }

    private long followPathAndCalculate(Point start, Direction direction) {
        Point currentPosition = new Point(start);
        direction.apply(currentPosition);
        long pathLength = 1;

        while (!currentPosition.equals(start)) {
            if (currentPosition.y < 0 || currentPosition.y >= maze.length || currentPosition.x < 0 || currentPosition.x >= maze[currentPosition.y].length) {
                throw new IllegalArgumentException("Path ends at maze wall: currentPosition=(%dx%d), mazeSize=(%dx%d)"
                        .formatted(currentPosition.x, currentPosition.y, maze[0].length, maze.length));
            }

            Field currentField = maze[currentPosition.y][currentPosition.x];
            if (!FieldUtil.isPipeField(currentField)) {
                throw new IllegalArgumentException("Path ends with a non-pipe field: field=%s, currentPosition=(%dx%d)".formatted(currentField, currentPosition.x, currentPosition.y));
            }
            log.trace("Arrived at field: field={}, position=({}x{})", currentField, currentPosition.x, currentPosition.y);
            direction = FieldUtil.getOtherDirection(currentField, direction.getOpposite());

            direction.apply(currentPosition);
            pathLength++;
        }

        return pathLength;
    }

    private Set<Direction> getPossibleDirections(Point start) {
        Set<Direction> possibleDirections = EnumSet.noneOf(Direction.class);
        if (start.x > 0 && FieldUtil.connectsRight(maze[start.y][start.x-1])) {
            possibleDirections.add(Direction.LEFT);
        }
        if (start.x < maze[start.y].length - 1 && FieldUtil.connectsLeft(maze[start.y][start.x+1])) {
            possibleDirections.add(Direction.RIGHT);
        }
        if (start.y > 0 && FieldUtil.connectsDown(maze[start.y-1][start.x])) {
            possibleDirections.add(Direction.UP);
        }
        if (start.y < maze.length - 1 && FieldUtil.connectsUp(maze[start.y+1][start.x])) {
            possibleDirections.add(Direction.DOWN);
        }

        if (possibleDirections.size() != 2) {
            String directionList = possibleDirections.stream()
                    .map(Enum::toString)
                    .collect(Collectors.joining(", ", "[", "]"));
            throw new IllegalArgumentException("More than 2 pipes connected to start: " + directionList);
        }

        return possibleDirections;
    }

}
