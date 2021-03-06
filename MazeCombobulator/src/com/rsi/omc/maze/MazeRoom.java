package com.rsi.omc.maze;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import lombok.Data;

@Data
public class MazeRoom {

    private boolean northWall;
    private boolean southWall;
    private boolean eastWall;
    private boolean westWall;
    private boolean visited;
    private String specialNotation;
    private Coordinate location;
    private Coordinate screenLocation;

    private Map<String, Coordinate> neighbors;
    private Stack<Coordinate> neighborStack;
    private Queue<Coordinate> neighborQueue;

    private static final String ONE = "1";
    public static final Map<String, String> DIRECTIONS = new HashMap<>();

    static {
        DIRECTIONS.put("N", "North");
        DIRECTIONS.put("S", "South");
        DIRECTIONS.put("E", "East");
        DIRECTIONS.put("W", "West");
    }

    public static final String NORTH_ENTRANCE = "B-N";
    public static final String SOUTH_ENTRANCE = "B-S";
    public static final String EAST_ENTRANCE = "B-E";
    public static final String WEST_ENTRANCE = "B-W";

    public static final String NORTH_EXIT = "X-N";
    public static final String SOUTH_EXIT = "X-S";
    public static final String EAST_EXIT = "X-E";
    public static final String WEST_EXIT = "X-W";

    public MazeRoom(String entry) {

        neighbors = new HashMap<>();
        neighborStack = new Stack<>();
        neighborQueue = new LinkedList<>();

        StringBuilder sb = new StringBuilder(entry);
        // delete {
        sb.deleteCharAt(0);
        // delete }
        sb.deleteCharAt(sb.length() - 1);
        String[] newEntry = sb.toString().split(",");
        setData(newEntry);

    }

    public String getId() {
        StringBuilder sb = new StringBuilder();
        if (isSpecialRoom()) {
            sb.append(getSpecialNotation()).append("\n");
        }
        sb.append("(");
        sb.append(getLocation().getRow());
        sb.append(",");
        sb.append(getLocation().getColumn());
        sb.append(")");

        return sb.toString();

    }

    public void setData(String[] newEntry) {
        this.northWall = ONE.equals(newEntry[0]);
        this.southWall = ONE.equals(newEntry[1]);
        this.eastWall = ONE.equals(newEntry[2]);
        this.westWall = ONE.equals(newEntry[3]);
        this.specialNotation = newEntry[4];
    }

    public boolean hasNorthWall() {
        return northWall;
    }

    public boolean hasSouthWall() {
        return southWall;
    }

    public boolean hasEastWall() {
        return eastWall;
    }

    public boolean hasWestWall() {
        return westWall;
    }

    public boolean hasKey() {
        return "K".equalsIgnoreCase(getSpecialNotation());
    }

    public boolean hasExit() {
        return getSpecialNotation().startsWith("X");
    }

    public boolean hasEntrance() {
        return getSpecialNotation().startsWith("B");
    }

    public String getEntranceDirection() {
        if (hasEntrance()) {
            return DIRECTIONS.get(getSpecialNotation().substring(2));
        }
        return "";
    }

    public String getExitDirection() {
        if (hasExit()) {
            return DIRECTIONS.get(getSpecialNotation().substring(2));
        }
        return "";
    }

    public boolean isSpecialRoom() {
        return hasKey() || hasEntrance() || hasExit();
    }

    public void render(GraphicsContext gc) {

        int xPos = screenLocation.getRow();
        int yPos = screenLocation.getColumn();

        if (hasNorthWall()) {
            gc.strokeLine(xPos, yPos, xPos + Maze.ROOM_WIDTH, yPos);
        }
        if (hasSouthWall()) {
            gc.strokeLine(xPos, yPos + Maze.ROOM_HEIGHT, xPos + Maze.ROOM_WIDTH, yPos + Maze.ROOM_HEIGHT);
        }
        if (hasEastWall()) {
            gc.strokeLine(xPos + Maze.ROOM_WIDTH, yPos, xPos + Maze.ROOM_WIDTH, yPos + Maze.ROOM_HEIGHT);
        }
        if (hasWestWall()) {
            gc.strokeLine(xPos, yPos, xPos, yPos + Maze.ROOM_HEIGHT);
        }

        if (hasEntrance() || hasExit() || hasKey()) {
            renderString(getSpecialNotation(), gc);
        }

    }

    public void renderString(String s, GraphicsContext gc) {

        int xPos = screenLocation.getRow();
        int yPos = screenLocation.getColumn();
        Paint currentStroke = gc.getStroke();
        Paint currentFill = gc.getStroke();
        double currentWidth = gc.getLineWidth();
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.BLACK);
        gc.setLineWidth(1.25);

        gc.fillOval(xPos, yPos, Maze.ROOM_WIDTH, Maze.ROOM_WIDTH);
        gc.strokeText(s, xPos + Maze.ROOM_WIDTH / 2 - 7, yPos + Maze.ROOM_HEIGHT / 2);

        gc.setStroke(currentStroke);
        gc.setFill(currentFill);
        gc.setLineWidth(currentWidth);
    }

    public void populateNeighbors() {

        if (!this.northWall && !getSpecialNotation().contains("N")) {
            Coordinate neighbor = new Coordinate(getLocation().getRow() - 1, getLocation().getColumn());
            neighbors.put("N", neighbor);
            neighborQueue.add(neighbor);
            neighborStack.push(neighbor);
        }
        if (!this.southWall && !getSpecialNotation().contains("S")) {
            Coordinate neighbor = new Coordinate(getLocation().getRow() + 1, getLocation().getColumn());
            neighbors.put("S", neighbor);
            neighborQueue.add(neighbor);
            neighborStack.push(neighbor);
        }

        if (!this.eastWall && !getSpecialNotation().contains("E")) {
            Coordinate neighbor = new Coordinate(getLocation().getRow(), getLocation().getColumn() + 1);
            neighbors.put("E", neighbor);
            neighborQueue.add(neighbor);
            neighborStack.push(neighbor);
        }

        if (!this.westWall && !getSpecialNotation().contains("W")) {
            Coordinate neighbor = new Coordinate(getLocation().getRow(), getLocation().getColumn() - 1);
            neighbors.put("W", neighbor);
            neighborQueue.add(neighbor);
            neighborStack.push(neighbor);
        }

    }

}
