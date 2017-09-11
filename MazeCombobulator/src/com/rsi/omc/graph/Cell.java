/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsi.omc.graph;

import com.rsi.omc.maze.Coordinate;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class Cell extends StackPane {
    
    
    public enum CellType {

        RECTANGLE,
        TRIANGLE,
        LABEL,
        IMAGE,
        BUTTON,
        TITLEDPANE
        ;

    }

    String cellId;
    Coordinate location;
    
    List<Cell> cellChildren = new ArrayList<>();
    List<Cell> cellParents = new ArrayList<>();

    Node view;

    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    
    public Cell(String cellId) {
        this.cellId = cellId;
    }

    public void addCellChild(Cell cell) {
        cellChildren.add(cell);
    }

    public List<Cell> getCellChildren() {
        return cellChildren;
    }

    public void addCellParent(Cell cell) {
        cellParents.add(cell);
    }

    public List<Cell> getCellParents() {
        return cellParents;
    }

    public void removeCellChild(Cell cell) {
        cellChildren.remove(cell);
    }

    public void setView(Node view) {

        this.view = view;
        getChildren().add(view);

    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }
}