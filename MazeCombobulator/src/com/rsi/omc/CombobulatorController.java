/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsi.omc;

import com.rsi.omc.io.MazeLoader;
import com.rsi.omc.maze.Coordinate;
import com.rsi.omc.maze.MazeRoom;
import com.rsi.omc.maze.MazeRow;
import com.rsi.omc.maze.Maze;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author michael.peacock
 */
public class CombobulatorController implements Initializable {

    private final Desktop desktop = Desktop.getDesktop();
    private Stage stage;
    private Maze maze;
    private MazeLoader mazeLoader;

    private boolean foundExit, foundKey, completed, exiting;
    public static final int ROOM_HEIGHT = 45;
    public static final int ROOM_WIDTH = 45;
    public static final int LINE_WIDTH = 5;
    public static final int TRANSLATE_X = 10;
    public static final int TRANSLATE_Y = 10;

    public static final int RENDER = 1;
    public static final int SOLVE = 2;

    private static final double CIRCLE_RADIUS = 10;

    // list to hold solution 
    List<MazeRoom> solution = new ArrayList<>();

    private int stepCount;

    @FXML
    Parent root;
    @FXML
    private Label label;

    @FXML
    Button openButton;
    @FXML
    Button renderButton;
    @FXML
    Button solveButton;
    @FXML
    Button quitButton;

    @FXML
    TextArea textArea;
    @FXML
    Canvas mazePanel;

    @FXML
    private void handleOpenAction(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        stage = (Stage) root.getScene().getWindow();
        File mazeFile = chooser.showOpenDialog(stage);

        textArea.setText("Loading New Maze File ...\n");

        if (mazeFile != null) {
            textArea.appendText("Got File: " + mazeFile.toString());
            readFile(mazeFile);
        } else {
            textArea.appendText("No Maze File Selected\n");
        }

    }

    private void readFile(File mazeFile) {

        mazeLoader = new MazeLoader();
        mazeLoader.setMazeFile(mazeFile);
        textArea.appendText("Got Maze File: " + mazeFile + "\n");
        mazeLoader.parseMaze();
        textArea.appendText("Loaded Maze Definition.\n");
        textArea.appendText("Maze dimensions : " + mazeLoader.getMaze().getRowCount() + " Rows X " + mazeLoader.getMaze().getColCount() + " Columns\n");
        textArea.appendText("   Entrance Location : " + mazeLoader.getMaze().getEntrance() + "\n");
        textArea.appendText("   Exit Location : " + mazeLoader.getMaze().getExit() + "\n");
        textArea.appendText("   Key Location : " + mazeLoader.getMaze().getKey() + "\n");
        this.maze = mazeLoader.getMaze();
    }

    @FXML
    private void handleRenderAction(ActionEvent event) {
        textArea.appendText("Rendering Maze ...\n");
        render();
    }

    @FXML
    private void handleSolveAction(ActionEvent event) {
        textArea.appendText("Solving Maze ...\n");
        solve();
    }

    @FXML
    private void handleQuitAction(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    // rendering the maze:
    public void render() {

        GraphicsContext gc = mazePanel.getGraphicsContext2D();
        gc.clearRect(0, 0, 856.0, 579.0);
        gc.setFill(Color.BLACK);

        int currentX = 0;
        int currentY = 0;

        for (Iterator<MazeRow> iterator = maze.getMazeRows().descendingIterator(); iterator.hasNext();) {
            MazeRow currentRow = (MazeRow) iterator.next();

            for (MazeRoom currentRoom : currentRow.getRooms()) {
                currentRoom.setScreenLocation(new Coordinate(currentX, currentY));
                currentRoom.render(gc);
                currentX += Maze.ROOM_WIDTH;
            }

            currentX = 0;
            currentY += Maze.ROOM_HEIGHT;
        }
    }

    // Solve the maze
    public void solve() {

        stepCount = 0;
        
        Coordinate startLocation = maze.getEntrance();
        Coordinate keyLocation = maze.getKey();
        //foundExit = traverse(start);

        boolean gotKey = findGoal(startLocation, "KEY", foundKey);
        if (gotKey) {

            // reset each room to be not visited
            maze.getRoomMap().forEach((k, v) -> v.setVisited(false));
            boolean exited = findGoal(keyLocation, "EXIT", foundExit);
        }

    }

    private boolean findGoal(Coordinate currentLocation, String goalName, boolean goalFlag) {

        stepCount++;
        GraphicsContext gc = mazePanel.getGraphicsContext2D();
        MazeRoom room = maze.getRoomMap().get(currentLocation);

        gc.setFill(Color.BLACK);
        gc.setStroke(Color.RED);

        if ("KEY".equals(goalName)) {
            gc.fillOval(
                    room.getScreenLocation().getRow() + Maze.OFFSET + ROOM_WIDTH / 2,
                    room.getScreenLocation().getColumn() + Maze.OFFSET + ROOM_WIDTH / 2,
                    CIRCLE_RADIUS, CIRCLE_RADIUS
            );
        }
        else {
        gc.strokeOval(
                room.getScreenLocation().getRow() + Maze.OFFSET + (ROOM_WIDTH /2) -1,
                room.getScreenLocation().getColumn() + Maze.OFFSET + (ROOM_WIDTH /2)  -1,
                CIRCLE_RADIUS+2, CIRCLE_RADIUS +2
            );
        }
        if (room.isVisited()) {
            return false;
        }

        room.setVisited(true);

        if ("KEY".equals(goalName)) {
            if (!goalFlag && room.hasKey()) {
                textArea.appendText("Found Key on step: " + stepCount + "\n");
                return true;
            }
        }
        else if ("EXIT".equals(goalName)) {
            if (!goalFlag && room.hasExit()) {
                textArea.appendText("Found Exit on step: " + stepCount + "\n");
                return true;
            }
        }

        Map<String, Coordinate> neighbors = room.getNeighbors();

        for (Map.Entry<String, Coordinate> entry : neighbors.entrySet()) {
            String key = entry.getKey();
            textArea.appendText("Checking " + MazeRoom.DIRECTIONS.get(key) + "\n");
            MazeRoom nextRoom = maze.getRoomMap().get(entry.getValue());
            if (!nextRoom.isVisited()) {
                goalFlag = findGoal(entry.getValue(), goalName,goalFlag);
            }
            if (goalFlag) {
                return true;
            }
        }

        return false;
    }

    private boolean traverse(Coordinate currentLocation) {

        stepCount++;
        GraphicsContext gc = mazePanel.getGraphicsContext2D();
        MazeRoom room = maze.getRoomMap().get(currentLocation);

        solution.add(room);

        gc.setFill(Color.GREEN);

        gc.fillOval(
                room.getScreenLocation().getRow() + Maze.OFFSET + ROOM_WIDTH / 2,
                room.getScreenLocation().getColumn() + Maze.OFFSET + ROOM_WIDTH / 2,
                CIRCLE_RADIUS, CIRCLE_RADIUS
        );

        if (room.isVisited()) {
            return false;
        }

        room.setVisited(true);

        if (!foundKey && room.hasKey()) {
            textArea.appendText("Found Key on step: " + stepCount + "\n");
            foundKey = true;
        }

        if (!foundExit && room.hasExit()) {
            textArea.appendText("Found Exit on step: " + stepCount + "\n");
            foundExit = true;
        }
        if (foundKey && foundExit) {
            completed = true;
        } else {
            completed = traverse(getNextRoom(room));
        }

        return completed;
    }

    private Coordinate getNextRoom(MazeRoom room) {

        Coordinate nextLocation = new Coordinate(room.getLocation().getRow(), room.getLocation().getColumn());

        // north
        if (canGoNorth(room)) {
            nextLocation.setRow(room.getLocation().getRow() - 1);
            if (MazeRoom.NORTH_EXIT.equals(room.getSpecialNotation()) && foundKey) {
                completed = true;
            }
        } // south
        else if (canGoSouth(room)) {
            nextLocation.setRow(room.getLocation().getRow() + 1);
            if (MazeRoom.SOUTH_EXIT.equals(room.getSpecialNotation()) && foundKey) {
                completed = true;
            }
        } // south
        else if (canGoWest(room)) {
            nextLocation.setColumn(room.getLocation().getColumn() - 1);
            if (MazeRoom.WEST_EXIT.equals(room.getSpecialNotation()) && foundKey) {
                completed = true;
            }
        } // south
        else if (canGoEast(room)) {
            nextLocation.setColumn(room.getLocation().getColumn() + 1);
            if (MazeRoom.EAST_EXIT.equals(room.getSpecialNotation()) && foundKey) {
                completed = true;
            }
        }

        return nextLocation;

    }

    /**
     * In any room - I can moving a specific direction if there is no wall in
     * that direction or you're not moving out of an entrance or you're exiting
     * after finding the key the room has not been visited already
     *
     * @param room
     * @return
     */
    private boolean canGoNorth(MazeRoom room) {

        boolean visited = false;

//            if (room.getLocation().getRow() == 0) {
//                    if (exitingNorth(room));
//            }
        if (room.hasNorthWall()) {
            return false;
        }
        if (MazeRoom.NORTH_ENTRANCE.equals(room.getSpecialNotation())) {
            return false;
        }
        if (!foundKey && MazeRoom.NORTH_EXIT.equals(room.getSpecialNotation())) {
            return false;
        }

        Coordinate nextLocation = new Coordinate(room.getLocation().getRow() - 1, room.getLocation().getColumn());

        MazeRoom nextRoom = maze.getRoomMap().get(nextLocation);
        if (nextRoom != null) {
            return (nextRoom.isVisited()) ? false : true;
        }
        return false;
    }

    private boolean canGoSouth(MazeRoom room) {

        boolean visited = false;

//            if (room.getLocation().getRow() == maze.getRowCount()-1) {
//                return exitingSouth(room);
//            }
        if (room.hasSouthWall()) {
            return false;
        }
        if (room.hasEntrance() && MazeRoom.SOUTH_ENTRANCE.equals(room.getSpecialNotation())) {
            return false;
        }
        Coordinate nextLocation = new Coordinate(room.getLocation().getRow() + 1, room.getLocation().getColumn());

        MazeRoom nextRoom = maze.getRoomMap().get(nextLocation);
        if (nextRoom != null) {
            return (nextRoom.isVisited()) ? false : true;
        }
        return false;

    }

    private boolean canGoEast(MazeRoom room) {

        boolean visited = false;

//            if (room.getLocation().getColumn() == maze.getColCount()-1) {
//                    return exitingEast(room);
//            }
        if (room.hasEastWall()) {
            return false;
        }
        if (room.hasEntrance() && MazeRoom.EAST_ENTRANCE.equals(room.getSpecialNotation())) {
            return false;
        }
        Coordinate nextLocation = new Coordinate(room.getLocation().getRow(), room.getLocation().getColumn() + 1);

        MazeRoom nextRoom = maze.getRoomMap().get(nextLocation);
        if (nextRoom != null) {
            return (nextRoom.isVisited()) ? false : true;
        }
        return false;

    }

    private boolean canGoWest(MazeRoom room) {

        boolean visited = false;

//            if (room.getLocation().getColumn() == 0) {
//                return exitingWest(room);
//            }
        if (room.hasWestWall()) {
            return false;
        }
        if (room.hasEntrance() && MazeRoom.WEST_ENTRANCE.equals(room.getSpecialNotation())) {
            return false;
        }
        Coordinate nextLocation = new Coordinate(room.getLocation().getRow(), room.getLocation().getColumn() - 1);

        MazeRoom nextRoom = maze.getRoomMap().get(nextLocation);
        if (nextRoom != null) {
            return (nextRoom.isVisited()) ? false : true;
        }
        return false;
    }

    private boolean exitingNorth(MazeRoom room) {
        return room.hasExit() && MazeRoom.NORTH_EXIT.equals(room.getSpecialNotation()) && foundKey;
    }

    private boolean exitingSouth(MazeRoom room) {
        return room.hasExit() && MazeRoom.SOUTH_EXIT.equals(room.getSpecialNotation()) && foundKey;
    }

    private boolean exitingEast(MazeRoom room) {
        return room.hasExit() && MazeRoom.EAST_EXIT.equals(room.getSpecialNotation()) && foundKey;
    }

    private boolean exitingWest(MazeRoom room) {
        return room.hasExit() && MazeRoom.WEST_EXIT.equals(room.getSpecialNotation()) && foundKey;
    }

}
