package com.rsi.omc.maze;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Maze {

	private int rowCount;
	private int colCount;
	private int totalRooms;
	
	ArrayDeque<MazeRow> mazeRows = new ArrayDeque<>();
	List<String> rawData = new ArrayList<>();

	
	public void addRow(MazeRow row) {
		this.mazeRows.add(row);
	}


	public void addRawData(String line) {
		rawData.add(line);
	}


	public void parseRawData() {
		
		MazeRow row = null;
		for (String entry : rawData) {
			if ("ROW".equalsIgnoreCase(entry)) {
				row = new MazeRow();
				mazeRows.add(row);
				rowCount ++;
				// reset column count on each new row
				colCount = 0;
			}
			else{
				row.add(new MazeRoom(entry));
				colCount++;
			}
		}
	}
	
}
