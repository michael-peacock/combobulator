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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    
    private GraphicsContext gc;

    // list to hold solution screen coordinates
    List<Coordinate> solution = new ArrayList<>();

    private int stepCount;

    @FXML Parent root;
    @FXML Label label;

    @FXML Button openButton;
    @FXML Button renderButton;
    @FXML Button solveButton;
    @FXML Button quitButton;

    @FXML TextArea textArea;
    @FXML Canvas mazePanel;
    @FXML StackPane mazePane;

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
        gc = mazePanel.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);

    }

    // rendering the maze:
    public void render() {
        
        gc.clearRect(0, 0, 856.0, 579.0);
        gc.setFill(Color.BLACK);
        mazePane.getChildren().clear();
        
        gc = mazePanel.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        mazePane.getChildren().add(mazePanel);

        solution = new ArrayList<Coordinate>();
        
        int currentX = 0;
        int currentY = 0;

        for (Iterator<MazeRow> iterator = maze.getMazeRows().descendingIterator(); iterator.hasNext();) {
            MazeRow currentRow = (MazeRow) iterator.next();

            for (MazeRoom currentRoom : currentRow.getRooms()) {
                Coordinate screenLocation = new Coordinate(currentX + Maze.OFFSET, currentY + Maze.OFFSET);
                currentRoom.setScreenLocation(screenLocation);
                currentRoom.render(gc);
                currentX += Maze.ROOM_WIDTH;
            }

            currentX = 0;
            currentY += Maze.ROOM_HEIGHT;
        }
    }

    // Solve the maze
    // Here, set step count to 0
    // start at the start location and find the key
    // if the key is found, 
    //      then restart at the key location
    //      and find the exit
    public void solve() {
        
        stepCount = 0;
        
        // reset each room to be not visited
        maze.getRoomMap().forEach((k, v) -> v.setVisited(false));
        
        Coordinate startLocation = maze.getEntrance();
        Coordinate keyLocation = maze.getKey();
        //foundExit = traverse(start);

        boolean gotKey = findGoal(startLocation, "KEY", foundKey);
        if (gotKey) {

            // reset each room to be not visited - again
            maze.getRoomMap().forEach((k, v) -> v.setVisited(false));
            boolean exited = findGoal(keyLocation, "EXIT", foundExit);
        } 
        else {
            textArea.appendText("I got Discombobulated! Never found the key!\n");  
        }
        
        // try to animate the solution
        showSolution();
    }

    /**
     * Method to find the current goal
     * There are two goal types - Key and Exit
     * @param currentLocation
     * @param goalName
     * @param goalFlag
     * @return 
     */
    private boolean findGoal(Coordinate currentLocation, String goalName, boolean goalFlag) {

        stepCount++;
        
        MazeRoom room = maze.getRoomMap().get(currentLocation);
        if (room.hasKey()) {
            room.getScreenLocation().setKeyEvent(true);
            solution.add(room.getScreenLocation());
        }
        else if (room.hasExit()) {
            room.getScreenLocation().setExitEvent(true);
            solution.add(room.getScreenLocation());
        }
        else {     
            solution.add(room.getScreenLocation());
        }
        
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        
        textArea.appendText("Current Cell: (Row,Col): (" + currentLocation.getRow() + ","+  currentLocation.getColumn() +")\n");  
        
//        if ("KEY".equals(goalName)) {
//            gc.fillOval(
//                    room.getScreenLocation().getRow() + Maze.OFFSET + ROOM_WIDTH / 2,
//                    room.getScreenLocation().getColumn() + Maze.OFFSET + ROOM_WIDTH / 2,
//                    CIRCLE_RADIUS, CIRCLE_RADIUS
//            );
//        }
//        else {
//            gc.strokeOval(
//                room.getScreenLocation().getRow() + Maze.OFFSET + (ROOM_WIDTH /2) -1,
//                room.getScreenLocation().getColumn() + Maze.OFFSET + (ROOM_WIDTH /2)  -1,
//                CIRCLE_RADIUS+2, CIRCLE_RADIUS +2
//            );
//        }
        if (room.isVisited()) {
            return false;
        }

        room.setVisited(true);

        if ("KEY".equals(goalName)) {
            if (!goalFlag && room.hasKey()) {
                textArea.appendText("Found Key on Step: " + stepCount + "\n");
                return true;
            }
        }
        else if ("EXIT".equals(goalName)) {
            if (!goalFlag && room.hasExit()) {
                textArea.appendText("Found Exit on Step: " + stepCount + "\n");
                return true;
            }
        }
        
        // get the neighbors of the current room
        Map<String, Coordinate> neighbors = room.getNeighbors();

        // iterate through the neighbors, and check for the goal in each one
        // is this depth first or breadth first? 
        // I think it's depth first since I recurse into each neighbor
        for (Map.Entry<String, Coordinate> entry : neighbors.entrySet()) {
            String key = entry.getKey();
            
            MazeRoom nextRoom = maze.getRoomMap().get(entry.getValue());
            if (!nextRoom.isVisited()) {
                textArea.appendText("Checking " + MazeRoom.DIRECTIONS.get(key) + "\n");
                goalFlag = findGoal(entry.getValue(), goalName,goalFlag);
            }
            if (goalFlag) {
                return true;
            }
        }

        return false;
    }
    
    // try to animate a shape 
    // along the solution path 
    private void showSolution() {
        
        Path path = createPath();
        Animation animation = createPathAnimation(path, Duration.seconds(6));
        animation.play();
    
    }

    private Path createPath() {

        Path path = new Path();
        Coordinate start = solution.get(0);
        path.getElements().add(new MoveTo(start.getRow() + Maze.ROOM_WIDTH/2, start.getColumn()+  Maze.ROOM_HEIGHT/2));
        
        for (int i = 1; i < solution.size(); i++) {
            Coordinate location = solution.get(i);
            path.getElements().add(new LineTo(location.getRow() + Maze.ROOM_WIDTH/2, location.getColumn() + Maze.ROOM_HEIGHT/2));
        }
        
        return path;
    }    
    
private Animation createPathAnimation(Path path, Duration duration) {

        // move a node along a path. we want its position
        Circle pen = new Circle(0, 0, 4);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(4);
        gc.setFill(Color.BLUE);
        

        // create path transition
        PathTransition pathTransition = new PathTransition(duration, path, pen);
        pathTransition.currentTimeProperty().addListener( new ChangeListener<Duration>() {

        Coordinate oldLocation = null;

            /**
             * Draw a line from the old location to the new location
             */
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                // skip starting at 0/0
                if( oldValue == Duration.ZERO)
                    return;

                // get current location
                double x = pen.getTranslateX();
                double y = pen.getTranslateY();

                // initialize the location
                if( oldLocation == null) {
                    oldLocation = new Coordinate(Double.valueOf(x).intValue(),Double.valueOf(x).intValue());
                    return;
                }

                // draw Circle
                // gc.setStroke(Color.GREEN);
                    gc.fillOval(
                        x,
                        y,
                        CIRCLE_RADIUS, CIRCLE_RADIUS
                    );
                
                //gc.strokeLine(oldLocation.getRow(), oldLocation.getColumn(), x, y);

                // update old location with current one
                oldLocation.setRow(Double.valueOf(x).intValue());
                oldLocation.setColumn(Double.valueOf(y).intValue());
            }

        });

        return pathTransition;
    }    
  
    
}
