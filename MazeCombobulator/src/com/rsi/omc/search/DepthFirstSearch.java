package com.rsi.omc.search;

import com.rsi.omc.CombobulatorController;
import com.rsi.omc.maze.Coordinate;
import com.rsi.omc.maze.MazeRoom;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
/**
 * Implements an iterative DFS method - based on this post: 
 * @url http://www.csl.mtu.edu/cs2321/www/newLectures/26_Depth_First_Search.html
 * @author michael.peacock
 */
public class DepthFirstSearch extends MazeSearchStrategy {

    public DepthFirstSearch(CombobulatorController controller) {
        super(controller);
    }

    @Override
    /**
     * Method to find the current goal There are two goal types - Key and Exit
     *
     * @param currentLocation
     * @param goalName
     * @param goalFlag
     * @return
     */
    protected boolean findGoal(Coordinate currentLocation, String goalName, boolean goalFlag) {
        solution.setStrategy("Depth First");

        MazeRoom room = maze.getRoomMap().get(currentLocation);

        // Create a queue for BFS
        Stack<Coordinate> stack = new Stack<>();

        room.setVisited(true);
        Stack<Coordinate> myNeighbors = room.getNeighborStack();
        for (Coordinate myNeighbor : myNeighbors) {
            stack.push(myNeighbor);
        }

        while (!stack.isEmpty()) {
            solution.setStepCount(solution.getStepCount() + 1);
            
            // Dequeue a room 
            Coordinate nextLocation = stack.pop();
            MazeRoom nextRoom = maze.getRoomMap().get(nextLocation);
            
            // System.out.println("Checking Location: " + nextLocation);
            // Check the room
            if ("KEY".equals(goalName)) {
                solution.getKeySolution().add(nextRoom.getScreenLocation());
                if (!goalFlag && nextRoom.hasKey()) {
                    controller.getTextArea().appendText("Found Key on Step: " + solution.getStepCount() + "\n");
                    goalFlag = true;
                    break;
                }
            } else if ("EXIT".equals(goalName)) {
                solution.getExitSolution().add(nextRoom.getScreenLocation());
                if (!goalFlag && nextRoom.hasExit()) {
                    controller.getTextArea().appendText("Found Exit on Step: " + solution.getStepCount() + "\n");
                    goalFlag = true;
                    break;
                }
            }

            Stack<Coordinate> nextNeighbors = nextRoom.getNeighborStack();
            
            nextNeighbors.forEach((nextNeighbor) -> {
                MazeRoom neighborRoom = maze.getRoomMap().get(nextNeighbor);
                if (!neighborRoom.isVisited()) {
                    neighborRoom.setVisited(true);
                    stack.push(nextNeighbor);
                }
            });
        }
        return goalFlag;
    }
}
