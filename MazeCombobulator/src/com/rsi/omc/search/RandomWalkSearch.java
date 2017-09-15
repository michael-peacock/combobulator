/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsi.omc.search;

import com.rsi.omc.CombobulatorController;
import com.rsi.omc.maze.Coordinate;
import com.rsi.omc.maze.MazeRoom;
import java.util.Iterator;
import java.util.Map;

public class RandomWalkSearch extends MazeSearchStrategy {

    public RandomWalkSearch(CombobulatorController controller) {
        super(controller);
        solution.setStrategy("Peacock's Memory - probably a depth first variation");
        controller.getTextArea().appendText("Solving with Strategy : " + solution.getStrategy()+ "\n");
        
    }

    @Override
    /**
     * Method to find the current goal
     * There are two goal types - Key and Exit
     * @param currentLocation
     * @param goalName
     * @param goalFlag
     * @return 
     */
    protected boolean findGoal(Coordinate currentLocation, String goalName, boolean goalFlag) {

        solution.setStepCount(solution.getStepCount()+1);
        
        MazeRoom room = maze.getRoomMap().get(currentLocation);

        if (room.isVisited()) {
            return false;
        }

        room.setVisited(true);
        
        if ("KEY".equals(goalName)) {
            solution.getKeySolution().add(room.getScreenLocation());
            if (!goalFlag && room.hasKey()) {
                controller.getTextArea().appendText("Found Key on Step: " + solution.getStepCount() + "\n");
                
                return true;
            }
        }
        else if ("EXIT".equals(goalName)) {
            solution.getExitSolution().add(room.getScreenLocation());
            if (!goalFlag && room.hasExit()) {
                controller.getTextArea().appendText("Found Exit on Step: " + solution.getStepCount() + "\n");
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
    
}
