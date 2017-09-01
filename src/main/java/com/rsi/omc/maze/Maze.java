package com.rsi.omc.maze;

import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.rsi.omc.ui.MazePanel;

import lombok.Data;

@Data
public class Maze {

	private int rowCount;
	private int colCount;
	private int totalRooms;
	
	private Coordinate entrance;
	private Coordinate exit;
	private Coordinate key;
	
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
				MazeRoom room = new MazeRoom(entry);
				row.add(room);
				colCount++;
			}
		}
		
		// get important locations
		int currentX = 0;
		int currentY = 0;
		
		for (Iterator<MazeRow> iterator = getMazeRows().descendingIterator(); iterator.hasNext();) {
			MazeRow currentRow = (MazeRow) iterator.next();

			for (MazeRoom room : currentRow.getRooms()) {

				if(room.hasEntrance()) {
					entrance = new Coordinate(currentY,currentX);
				}

				if(room.hasExit()) {
					exit = new Coordinate(currentY,currentX);
				}
				if(room.hasKey()) {
					key = new Coordinate(currentY,currentX);
				}
				
				currentX ++;
			}
			
			currentX = 0;
			currentY ++;
		}
		
		
		
		
		
	}
	
	// rendering the maze:
	public void render(Graphics2D g2d)  {
		
		int currentX = 0;
		int currentY = 0;
		
		for (Iterator<MazeRow> iterator = getMazeRows().descendingIterator(); iterator.hasNext();) {
			MazeRow currentRow = (MazeRow) iterator.next();

			for (MazeRoom currentRoom : currentRow.getRooms()) {
				currentRoom.render(currentX, currentY, g2d);
				currentX += MazePanel.ROOM_WIDTH;
			}
			
			currentX = 0;
			currentY += MazePanel.ROOM_HEIGHT;
		}
		
	}
	
}
