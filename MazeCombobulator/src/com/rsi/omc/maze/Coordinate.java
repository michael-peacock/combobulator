package com.rsi.omc.maze;

import lombok.Data;

@Data
public class Coordinate {

	private int row;
	private int column;
        private boolean keyEvent;
        private boolean exitEvent;
	
	public Coordinate(int row, int col) {
		this.row = row;
		this.column = col;
	}

	
}
