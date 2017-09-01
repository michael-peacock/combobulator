package com.rsi.omc.maze;

import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	
	private Map<Coordinate, MazeRoom> roomMap = new HashMap<>();
	
	private boolean foundExit, foundKey; 
	
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
				
				Coordinate currentLocation = new Coordinate(currentY,currentX);
				room.setLocation(currentLocation);
				roomMap.put(currentLocation, room);

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


	// recursive method to solve the maze
	public void solve(Graphics2D g2d) {
		
		Coordinate start = entrance;
		
		foundExit = traverse(start, g2d);
		
	}


	private boolean traverse(Coordinate currentLocation, Graphics2D g2d) {
		
		boolean completed = false; 
		
		MazeRoom room = roomMap.get(currentLocation);
		
		if (room.isVisited()) {
			return false;
		}
		
		room.setVisited(true);

		if (!foundKey && room.hasKey()) {
			foundKey = true;
		}
		
		if (!foundExit && room.hasExit()) {
			foundExit = true;
		}

		if (foundKey && foundExit) {
			completed = true;
		}
		else {
			completed =  traverse(getNextRoom(room), g2d);
		}

		return completed;
	}


	private Coordinate getNextRoom(MazeRoom room) {

		Coordinate nextLocation = new Coordinate(room.getLocation().getRow(),room.getLocation().getColumn());
		

		// north
		if (canGoNorth(room)) {
		   nextLocation.setRow(room.getLocation().getRow() - 1);
		}
		// south
		else if (canGoSouth(room)) {
		   nextLocation.setRow(room.getLocation().getRow() + 1);
		}
		// south
		else if (canGoWest(room)) {
		   nextLocation.setRow(room.getLocation().getColumn() - 1);
		}
		// south
		else if (canGoEast(room)) {
		   nextLocation.setRow(room.getLocation().getColumn() + 1);
		}

		
		return nextLocation;
		
	}


	/**
	 * 	In any room - I can moving a specific direction if
	 *  there is no wall
	 *  you're not moving out the entrance
	 *  you're not exiting before finding the key
		
	 * @param room
	 * @return
	 */
	private boolean canGoNorth(MazeRoom room) {
		return !room.hasNorthWall() 
			&& ( 
				(room.hasEntrance() 
			     &&  
				 !MazeRoom.NORTH_ENTRANCE.equals(room.getSpecialNotation()))
					||
				(room.hasExit() 
					  &&  
					!MazeRoom.NORTH_EXIT.equals(room.getSpecialNotation()) 
					  &&
			    	!foundKey
				)
				);
	}
	
	private boolean canGoSouth(MazeRoom room) {
		return !room.hasSouthWall() 
			&& ( 
				(room.hasEntrance() 
			     &&  
				 !MazeRoom.SOUTH_ENTRANCE.equals(room.getSpecialNotation()))
					||
				(room.hasExit() 
					  &&  
					!MazeRoom.SOUTH_EXIT.equals(room.getSpecialNotation()) 
					  &&
			    	!foundKey
				)
				);
	}

	private boolean canGoEast(MazeRoom room) {
		return !room.hasEastWall() 
			&& ( 
				(room.hasEntrance() 
			     &&  
				 !MazeRoom.EAST_ENTRANCE.equals(room.getSpecialNotation()))
					||
				(room.hasExit() 
					  &&  
					!MazeRoom.EAST_EXIT.equals(room.getSpecialNotation()) 
					  &&
			    	!foundKey
				)
				);
	}

	private boolean canGoWest(MazeRoom room) {
		return !room.hasWestWall() 
			&& ( 
				(room.hasEntrance() 
			     &&  
				 !MazeRoom.WEST_ENTRANCE.equals(room.getSpecialNotation()))
					||
				(room.hasExit() 
					  &&  
					!MazeRoom.WEST_EXIT.equals(room.getSpecialNotation()) 
					  &&
			    	!foundKey
				)
				);
	}

	
}
