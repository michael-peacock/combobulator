package com.rsi.omc.maze;

import lombok.Data;
//

@Data
public class MazeSolution {
    
    private int stepsToKey;
    private int stepsToExit;
    private String solutionMethod;


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
