package com.rsi.omc.maze;

import java.util.ArrayDeque;
import lombok.Data;

@Data
public class Maze {
	
	ArrayDeque<MazeRow> mazeRows = new ArrayDeque<>();

	
	public void addRow(MazeRow row) {
		
		this.mazeRows.add(row);
		
	}
}
