/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsi.omc;

import com.rsi.omc.graph.Graph;
import com.rsi.omc.graph.Layout;
import com.rsi.omc.graph.MazeLayout;
import com.rsi.omc.ui.ZoomableScrollPane;
import com.rsi.omc.io.MazeLoader;
import com.rsi.omc.maze.Coordinate;
import com.rsi.omc.maze.MazeRoom;
import com.rsi.omc.maze.MazeRow;
import com.rsi.omc.maze.Maze;
import com.rsi.omc.ui.ResizableCanvas;

import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
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

    public static final double CANVAS_HEIGHT = 16000.0;
    public static final double CANVAS_WIDTH = 16000.0;
    public static final int LINE_WIDTH = 1;
    public static final int TRANSLATE_X = 2;
    public static final int TRANSLATE_Y = 2;

    public static final int RENDER = 1;
    public static final int SOLVE = 2;

    public static final double CIRCLE_RADIUS = 5;
    
    private GraphicsContext gc;

    // list to hold keySolution screen coordinates
    List<Coordinate> keySolution = new ArrayList<>();
    List<Coordinate> exitSolution = new ArrayList<>();

    private int stepCount;

    @FXML Parent root;
    @FXML Label label;

    @FXML Button openButton;
    @FXML Button renderButton;
    @FXML Button solveButton;
    @FXML Button quitButton;
    @FXML Button graphButton;
    @FXML TextField animationDuration;

    @FXML TextArea textArea;
    ResizableCanvas mazePanel;
    ZoomableScrollPane zoomPane;
    @FXML StackPane mazePane;
    public static final Paint START_COLOR = Color.CORNFLOWERBLUE;
    public static final Paint KEY_COLOR = Color.DARKRED;
    private double animDuration = 10.0;

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
    
    @FXML
    private void handleMazeGraphAction(ActionEvent event) {
        textArea.appendText("Rendering Maze Graph ...\n");
        renderMazeGraph();
    }
    @FXML
    private void handleMazeDurationTextChange(ActionEvent event) {
        this.animDuration = Double.parseDouble(animationDuration.getText());
        textArea.appendText("Animation Duration Changed to " + animDuration + " seconds\n");
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mazePanel = getNewCanvas(CANVAS_HEIGHT, CANVAS_WIDTH);
        zoomPane = new ZoomableScrollPane(mazePanel);
        mazePane.getChildren().add(zoomPane);
        
        gc = mazePanel.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        
        animationDuration.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    animDuration = Double.parseDouble(animationDuration.getText());
                    textArea.appendText("Animation Duration Changed to " + animDuration + " seconds\n");
                }
            }
        });
        
    }

    private ResizableCanvas getNewCanvas(double height, double width) {
       ResizableCanvas myPanel = new ResizableCanvas(width, height);

        myPanel.widthProperty().bind(
                       mazePane.widthProperty());
        myPanel.heightProperty().bind(
                       mazePane.heightProperty());
        return myPanel;
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
        
        mazePanel = getNewCanvas(CANVAS_HEIGHT, CANVAS_WIDTH);
 
        
        zoomPane = new ZoomableScrollPane(mazePanel);
        
        mazePane.getChildren().add(zoomPane);
        
    }


    public void clearCanvas() {
        gc.clearRect(0, 0, mazePanel.getWidth(), mazePanel.getHeight());
        gc.setFill(Color.BLACK);
        mazePane.getChildren().clear();
        
        mazePanel = getNewCanvas(CANVAS_HEIGHT, CANVAS_WIDTH);
        zoomPane = new ZoomableScrollPane(mazePanel);
        mazePane.getChildren().add(zoomPane);

        gc = mazePanel.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        
    }
    
    // rendering the maze:
    public void render() {
        
        clearCanvas();

        keySolution = new ArrayList<Coordinate>();
        exitSolution = new ArrayList<Coordinate>();
        
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

        boolean gotKey = findGoal(startLocation, "KEY", foundKey);
        if (gotKey) {
            // reset each room to be not visited - again
            maze.getRoomMap().forEach((k, v) -> v.setVisited(false));
            boolean exited = findGoal(keyLocation, "EXIT", foundExit);
        } 
        else {
            textArea.appendText("I got Discombobulated! Never found the key!\n");  
        }
        
        // try to animate the keySolution
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
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        
        //textArea.appendText("Current Cell: (Row,Col): (" + currentLocation.getRow() + ","+  currentLocation.getColumn() +")\n");  

        if (room.isVisited()) {
            return false;
        }

        room.setVisited(true);

        if ("KEY".equals(goalName)) {
            keySolution.add(room.getScreenLocation());
            if (!goalFlag && room.hasKey()) {
                textArea.appendText("Found Key on Step: " + stepCount + "\n");
                return true;
            }
        }
        else if ("EXIT".equals(goalName)) {
            exitSolution.add(room.getScreenLocation());
            if (!goalFlag && room.hasExit()) {
                textArea.appendText("Found Exit on Step: " + stepCount + "\n");
                return true;
            }
        }
        
        // get the neighbors of the current room
        Map<String, Coordinate> neighbors = room.getNeighbors();
        

        // iterate through the neighbors, and check for the goal in each one
        for (Iterator<String> iterator = neighbors.keySet().iterator(); iterator.hasNext();) {
            String key = iterator.next();
            Coordinate nextLocation = neighbors.get(key);
            MazeRoom nextRoom = maze.getRoomMap().get(nextLocation);
     
            if (!nextRoom.isVisited()) {
                //textArea.appendText("Checking " + MazeRoom.DIRECTIONS.get(key) + "\n");
                goalFlag = findGoal(nextLocation,goalName,goalFlag);
            }
            if (goalFlag) {
                break;
            }
        }
        return goalFlag;
    }
        
    
    // try to animate a shape 
    // along the keySolution path 
    private void showSolution() {

        SolutionView view = new SolutionView();
        view.setGc(gc);
        
        Path keyPath = view.createPath(keySolution);
        Path exitPath = view.createPath(exitSolution);
        
        
        Animation keyAnimation = view.createPathAnimation(keyPath, Duration.seconds(animDuration),"KEY");
        Animation exitAnimation = view.createPathAnimation(exitPath, Duration.seconds(animDuration),"EXIT");
        
        Animation startMarker = view.addMarker(maze.getEntrance(), Color.BLACK, Color.CORNFLOWERBLUE);
        Animation keyMarker = view.addMarker(maze.getKey(), Color.CORNFLOWERBLUE, Color.LIGHTGREEN);
        Animation exitMarker = view.addMarker(maze.getExit(), Color.LIGHTGREEN, Color.DARKBLUE);
        
        gc.setLineWidth(4);

        SequentialTransition animationSeq = new SequentialTransition(
                    startMarker, 
                    keyAnimation,keyMarker,
                    exitAnimation,exitMarker);
        animationSeq.setAutoReverse(true);
        animationSeq.play();
    }
    
 

    private void renderMazeGraph() {
        // clear the graphics area 
        gc.clearRect(0, 0, mazePanel.getHeight(), mazePanel.getWidth());
        gc.setFill(Color.BLACK);
        mazePane.getChildren().clear();
 
        Graph graph = new Graph(mazeLoader.getMaze());
        
        graph.addComponents();
        
        Layout layout = new MazeLayout(graph);
        layout.execute();

        mazePane.getChildren().add(graph.getScrollPane());
        
    }
  
    
    
}
