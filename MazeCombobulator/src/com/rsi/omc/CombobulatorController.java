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
import java.util.Iterator;
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
    
    @FXML Parent root;
    @FXML private Label label;

    @FXML Button openButton;
    @FXML Button renderButton;
    @FXML Button solveButton;
    @FXML Button quitButton;
    
    @FXML TextArea textArea;
    @FXML Canvas mazePanel;
    
    
    @FXML
    private void handleOpenAction(ActionEvent event) {
     FileChooser chooser = new FileChooser();
     stage = (Stage) root.getScene().getWindow();
     File mazeFile = chooser.showOpenDialog(stage);
     
    textArea.setText("Loading New Maze File ...\n");

    if(mazeFile != null) {
         textArea.appendText("Got File: " + mazeFile.toString());
        readFile(mazeFile);
    }
    else {
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
        textArea.appendText("Rendering Maze ...");
        render();
    }
    
        @FXML
    private void handleSolveAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
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
    public void render()  {

        GraphicsContext gc = mazePanel.getGraphicsContext2D();
        gc.clearRect(0, 0, 856.0, 579.0);
        
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
    
}
