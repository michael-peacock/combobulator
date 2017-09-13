/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsi.omc;

import static com.rsi.omc.CombobulatorController.CIRCLE_RADIUS;
import static com.rsi.omc.CombobulatorController.KEY_COLOR;
import static com.rsi.omc.CombobulatorController.START_COLOR;
import com.rsi.omc.maze.Coordinate;
import com.rsi.omc.maze.Maze;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.StrokeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import lombok.Data;

@Data
public class SolutionView {
    
    private GraphicsContext gc;
    
    public SolutionView() {
    }
    
    public Animation addMarker(Coordinate location, Color beginColor, Color endColor) {
        int x = location.getRow() + Maze.ROOM_WIDTH/2;
        int y = location.getColumn()+  Maze.ROOM_HEIGHT/2;
        Circle marker = new Circle(x, y, CIRCLE_RADIUS);
        marker.setCenterX(x);
        marker.setCenterY(y);
        marker.setTranslateX(0);
        marker.setTranslateY(0);
        
        StrokeTransition st = new StrokeTransition(Duration.millis(20), marker, beginColor, Color.RED);
        st.setCycleCount(1);
        st.setAutoReverse(true);

        return st;
        
    }

    public Path createPath(List<Coordinate> pathLocations) {

        Path path = new Path();
        Coordinate start = pathLocations.get(0);
        path.getElements().add(new MoveTo(start.getRow() + Maze.ROOM_WIDTH/2, start.getColumn()+  Maze.ROOM_HEIGHT/2));
        
        for (int i = 1; i < pathLocations.size(); i++) {
            Coordinate location = pathLocations.get(i);
            path.getElements().add(new LineTo(location.getRow() + Maze.ROOM_WIDTH/2, location.getColumn() + Maze.ROOM_HEIGHT/2));
        }
        
        return path;
    }    
    
    public Animation createPathAnimation(Path path, Duration duration, String goal) {

        // move a node along a path. we want its position
        Circle pen = new Circle(0, 0, 8);

        // create path transition
        PathTransition pathTransition = new PathTransition(duration, path, pen);
        pathTransition.currentTimeProperty().addListener( new ChangeListener<Duration>() {

        Coordinate oldLocation = null;

            /**
             * Draw a line from the old location to the new location
             */
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                // skip starting at 0/0
                if( oldValue == Duration.ZERO)
                    return;

                // get current location
                double x = pen.getTranslateX();
                double y = pen.getTranslateY();

                // initialize the location
                if( oldLocation == null) {
                    oldLocation = new Coordinate(Double.valueOf(x).intValue(),Double.valueOf(x).intValue());
                    return;
                }

                // draw Circle
                if ("EXIT".equals(goal)) {
                    gc.setFill(KEY_COLOR);
                    gc.setStroke(KEY_COLOR);
                } 
                else {
                    gc.setFill(START_COLOR);
                    gc.setStroke(START_COLOR);
                }
               gc.strokeLine(Double.valueOf(oldLocation.getRow()), Double.valueOf(oldLocation.getColumn()), x, y);

//                gc.fillOval(
//                    x,
//                    y,
//                    CIRCLE_RADIUS, CIRCLE_RADIUS
//                );
                
                // update old location with current one
                oldLocation.setRow(Double.valueOf(x).intValue());
                oldLocation.setColumn(Double.valueOf(y).intValue());
            }

        });

        return pathTransition;
    }       
    
}
