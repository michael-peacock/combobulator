package com.rsi.omc.maze;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Maze {

    private int rowCount;
    private int colCount;
    private int totalRooms;

    private Coordinate entrance;
    private Coordinate exit;
    private Coordinate key;

    private Map<Coordinate, MazeRoom> roomMap;
    private Map<Coordinate, MazeRoom> screenLocationMap;

    ArrayDeque<MazeRow> mazeRows;
    List<String> rawData;

    private boolean foundExit, foundKey, completed, exiting;

    public static final int OFFSET = 3;
    public static final int ROOM_HEIGHT = 25;
    public static final int ROOM_WIDTH = 25;
    public static final int LINE_WIDTH = 1;
    public static final int TRANSLATE_X = 5;
    public static final int TRANSLATE_Y = 5;

    public static final int RENDER = 1;
    public static final int SOLVE = 2;

    private static final double CIRCLE_RADIUS = 5;

    public Maze() {
        roomMap = new HashMap<>();
        screenLocationMap = new HashMap<>();
        mazeRows = new ArrayDeque<>();
        rawData = new ArrayList<>();
    }

    public void addRow(MazeRow row) {
        this.mazeRows.add(row);
    }

    public void addRawData(String line) {
        rawData.add(line);
    }

    public void parseRawData() {

        MazeRow row = null;
        for (String entry : rawData) {
            if ("ROW".equalsIgnoreCase(entry)) {
                row = new MazeRow();
                mazeRows.add(row);
                rowCount++;
                // reset column count on each new row
                colCount = 0;
            } else {
                MazeRoom room = new MazeRoom(entry);
                row.add(room);
                colCount++;
            }
        }

        // get important locations
        int currentX = 0;
        int currentY = 0;

        for (Iterator<MazeRow> iterator = getMazeRows().descendingIterator(); iterator.hasNext();) {
            MazeRow currentRow = (MazeRow) iterator.next();

            for (MazeRoom room : currentRow.getRooms()) {

                Coordinate currentLocation = new Coordinate(currentY, currentX);
                room.setLocation(currentLocation);
                roomMap.put(currentLocation, room);

                if (room.hasEntrance()) {
                    entrance = new Coordinate(currentY, currentX);
                }

                if (room.hasExit()) {
                    exit = new Coordinate(currentY, currentX);
                }
                if (room.hasKey()) {
                    key = new Coordinate(currentY, currentX);
                }

                currentX++;
            }

            currentX = 0;
            currentY++;
        }

        // populate neighbors;
        populateNeighbors();
    }

    public void populateNeighbors() {
        roomMap.forEach((k, v) -> {
            v.populateNeighbors();
        });
    }

}
