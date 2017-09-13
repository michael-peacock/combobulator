
package com.rsi.omc.graph;

import com.rsi.omc.ui.ZoomableScrollPane;
import com.rsi.omc.graph.Cell.CellType;
import com.rsi.omc.graph.cells.LabelCell;
import com.rsi.omc.graph.cells.RectangleCell;
import com.rsi.omc.maze.Coordinate;
import com.rsi.omc.maze.Maze;
import com.rsi.omc.maze.MazeRoom;
import java.util.Map;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;


public class Graph {

    private Model model;

    private Group canvas;

    private ZoomableScrollPane scrollPane;

    MouseGestures mouseGestures;

    /**
     * the pane wrapper is necessary or else the scrollpane would always align
     * the top-most and left-most child to the top and left eg when you drag the
     * top child down, the entire scrollpane would move down
     */
    CellLayer cellLayer;
    private Maze maze;

    public Graph() {

        this.model = new Model();

        canvas = new Group();
        cellLayer = new CellLayer();

        canvas.getChildren().add(cellLayer);

        mouseGestures = new MouseGestures(this);

        scrollPane = new ZoomableScrollPane(canvas);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

    }

    public Graph(Maze maze) {
        this();
        this.maze = maze;
    }

    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }

    public Pane getCellLayer() {
        return this.cellLayer;
    }

    public Model getModel() {
        return model;
    }

    public void beginUpdate() {
    }

    public void endUpdate() {

        // add components to graph pane
        getCellLayer().getChildren().addAll(model.getAddedEdges());
        getCellLayer().getChildren().addAll(model.getAddedCells());

        // remove components from graph pane
        getCellLayer().getChildren().removeAll(model.getRemovedCells());
        getCellLayer().getChildren().removeAll(model.getRemovedEdges());

        // enable dragging of cells
        for (Cell cell : model.getAddedCells()) {
            mouseGestures.makeDraggable(cell);
        }

        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        getModel().attachOrphansToGraphParent(model.getAddedCells());

        // remove reference to graphParent
        getModel().disconnectFromGraphParent(model.getRemovedCells());

        // merge added & removed cells with all cells
        getModel().merge();

    }

    public double getScale() {
        return this.scrollPane.getScaleValue();
    }
    
    public void addComponents() {

        Model model = getModel();

        beginUpdate();
        
        // Add all the cells, 
        
        for (Map.Entry<Coordinate, MazeRoom> roomEntry : maze.getRoomMap().entrySet()) {
            MazeRoom thisRoom = roomEntry.getValue();//maze.getRoomMap().get(maze.getEntrance());
            Cell cell = new RectangleCell(thisRoom.getId());
            cell.setLocation(thisRoom.getLocation());
            model.addCell(cell);
        }

        // now add all the edges
        for (Map.Entry<Coordinate, MazeRoom> roomEntry : maze.getRoomMap().entrySet()) {
            MazeRoom thisRoom = roomEntry.getValue();//maze.getRoomMap().get(maze.getEntrance());
        
            // get the neighbors of the current room
            Map<String, Coordinate> neighbors = thisRoom.getNeighbors();

                for (Map.Entry<String, Coordinate> entry : neighbors.entrySet()) {
                    String key = entry.getKey();

                    MazeRoom nextRoom = maze.getRoomMap().get(entry.getValue());
                    if(nextRoom != null) {
                        model.addEdge(thisRoom.getId(), nextRoom.getId());
                    }
                }
        }
        endUpdate();

 }

}
