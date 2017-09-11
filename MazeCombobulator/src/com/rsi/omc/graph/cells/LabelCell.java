package com.rsi.omc.graph.cells;

import com.rsi.omc.graph.Cell;
import javafx.scene.control.Label;

public class LabelCell extends Cell {

    public LabelCell(String id) {
        super(id);

        Label view = new Label(id);

        setView(view);

    }

}
