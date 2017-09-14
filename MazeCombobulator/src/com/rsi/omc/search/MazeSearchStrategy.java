
package com.rsi.omc.search;

import com.rsi.omc.CombobulatorController;
import com.rsi.omc.maze.Coordinate;
import com.rsi.omc.maze.Maze;
import com.rsi.omc.maze.MazeSolution;
import lombok.Data;

@Data
public abstract class MazeSearchStrategy {
    
    protected MazeSolution solution; 
    protected CombobulatorController controller;
    protected Maze maze;
    protected boolean foundKey, foundExit;
    
    public MazeSearchStrategy(CombobulatorController controller){

        solution = new MazeSolution();
        this.controller = controller;
        this.maze = controller.getMaze();

    }
    
    public MazeSolution solve() {
        
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
            controller.getTextArea().appendText("I got Discombobulated! Never found the key!\n");  
        }
        
        return this.getSolution();
    }
    
    protected abstract boolean findGoal(Coordinate currentLocation, String goalName, boolean goalFlag);
    
    
}
