/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsi.omc.graph.cells;

import com.rsi.omc.graph.Cell;
import javafx.scene.control.Button;


public class ButtonCell extends Cell {

    public ButtonCell(String id) {
        super(id);

        Button view = new Button(id);

        setView(view);

    }

}