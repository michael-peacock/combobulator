/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsi.omc.graph;

import com.rsi.omc.maze.Coordinate;
import java.util.List;

/**
 *
 * @author Michael
 */
public class MazeLayout  extends Layout {

    public static final int FACTOR = 75;
    Graph graph;
    public MazeLayout(Graph graph) {

        this.graph = graph;

    }

    public void execute() {

        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {

            Coordinate location = cell.getLocation();
            
            double y = location.getRow() * FACTOR;
            double x = location.getColumn() * FACTOR;

            cell.relocate(x, y);

        }

    }
    
}
