package com.rsi.omc.search;

import com.rsi.omc.CombobulatorController;
import com.rsi.omc.maze.Coordinate;
import com.rsi.omc.maze.MazeRoom;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
/**
 * Implements a BFS method - based on geeksforgeeks.org post here: 
 * @url http://www.geeksforgeeks.org/breadth-first-traversal-for-a-graph/
 * @author michael.peacock
 */
public class BreadthFirstSearch extends MazeSearchStrategy {

    public BreadthFirstSearch(CombobulatorController controller) {
        super(controller);
        solution.setStrategy("Breadth First");
        controller.getTextArea().appendText("Solving with Strategy : " + solution.getStrategy()+ "\n");
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
        
        MazeRoom room = maze.getRoomMap().get(currentLocation);

        // Create a queue for BFS
        LinkedList<Coordinate> queue = new LinkedList<>();

        room.setVisited(true);
        queue.add(currentLocation);

        while (!queue.isEmpty()) {
            solution.setStepCount(solution.getStepCount() + 1);
            
            // Dequeue a room 
            Coordinate nextLocation = queue.poll();
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

            // haven't returned, so add any neighbors to the queue and retry
            // Get all adjacent rooms of the dequeued room
            // If a neighbor has not been visited, then mark it
            // visited and enqueue it
            Map<String, Coordinate> neighbors = nextRoom.getNeighbors();
            Iterator<Coordinate> it = neighbors.values().iterator();
            while (it.hasNext()) {
                Coordinate neighbor = it.next();
                MazeRoom neighborRoom = maze.getRoomMap().get(neighbor);
                if (!neighborRoom.isVisited()) {
                    neighborRoom.setVisited(true);
                    queue.add(neighbor);
                }
            }
        }
        return goalFlag;
    }
}
