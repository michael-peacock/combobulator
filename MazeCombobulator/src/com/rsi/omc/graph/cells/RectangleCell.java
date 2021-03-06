/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsi.omc.graph.cells;

import com.rsi.omc.graph.Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class RectangleCell extends Cell {

    public RectangleCell(String id) {
        super(id);

        Rectangle view = new Rectangle(50,50);

        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);
        
        

        Text text = new Text(id);
        text.setFill(Color.WHITESMOKE);
        text.setBoundsType(TextBoundsType.VISUAL); 
        
        setView(view);
        getChildren().add(text);

    }

}