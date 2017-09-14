package com.rsi.omc.maze;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
//

@Data
public class MazeSolution {
    
    private int stepCount;
    private String strategy;
    protected List<Coordinate> keySolution;
    protected List<Coordinate> exitSolution;
    
    public MazeSolution() {
        keySolution = new ArrayList<Coordinate>();
        exitSolution = new ArrayList<Coordinate>();    
    }
    public MazeSolution(String strategy) {
        this();
        this.strategy = strategy;
    }


//    @Override
//    public int compareTo(Object o) {
//        MazeSolution other = (MazeSolution) o;
//        
//        return ComparisonChain.start()
//                .compare(stepsToExit, other.stepsToExit)
//                .compare(stepsToKey, other.stepsToKey)
//                .result();
//    }
//    
}
