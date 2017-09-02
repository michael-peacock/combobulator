package com.rsi.omc.ui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;

import javax.swing.JPanel;

import com.rsi.omc.maze.Coordinate;
import com.rsi.omc.maze.Maze;
import com.rsi.omc.maze.MazeRoom;
import com.rsi.omc.maze.MazeRow;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MazePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Maze maze;
	private int action;
	
	private boolean foundExit, foundKey; 
	
	public static final int ROOM_HEIGHT = 75;
	public static final int ROOM_WIDTH = 75;
	public static final int LINE_WIDTH = 5;
	public static final int TRANSLATE_X = 10;
	public static final int TRANSLATE_Y = 10;

	public static final int RENDER = 1;
	public static final int SOLVE = 2;	

	
	
    private void drawMaze(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(LINE_WIDTH));
        g2d.translate(TRANSLATE_X,TRANSLATE_Y);
        
        render(g2d);
        
    }
    
    private void solveMaze(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(LINE_WIDTH));
        g2d.translate(TRANSLATE_X,TRANSLATE_Y);
        
        solve(g2d);
        
    } 
    
    public void update(Graphics g){
        paintComponent(g);        
    }    

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (this.maze != null) {
			
			switch (action) {
			case RENDER:
				drawMaze(g);
				break;
				
			case SOLVE:
				drawMaze(g);
				solveMaze(g);
				break;
				

			default:
				break;
			}
			
		}
	}
	
	
	
	// rendering the maze:
	public void render(Graphics2D g2d)  {
		
		int currentX = 0;
		int currentY = 0;
		
		for (Iterator<MazeRow> iterator = maze.getMazeRows().descendingIterator(); iterator.hasNext();) {
			MazeRow currentRow = (MazeRow) iterator.next();

			for (MazeRoom currentRoom : currentRow.getRooms()) {
				currentRoom.setScreenLocation(new Coordinate(currentX, currentY));
				currentRoom.render(g2d);
				currentX += MazePanel.ROOM_WIDTH;
			}
			
			currentX = 0;
			currentY += MazePanel.ROOM_HEIGHT;
		}
	}


	// recursive method to solve the maze
	public void solve(Graphics2D g2d) {
		
		Coordinate start = maze.getEntrance();
		reset();
		
		foundExit = traverse(start, g2d);
		
	}


	private void reset() {
		maze.getRoomMap().forEach((k,v)->v.setVisited(false));
		foundExit = false;
		foundKey = false;
		
	}


	private boolean traverse(Coordinate currentLocation, Graphics2D g2d) {
		validate();
		
		boolean completed = false; 
		
		MazeRoom room = maze.getRoomMap().get(currentLocation);
		
		room.renderString("x",g2d);
		validate();
		
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
		   nextLocation.setColumn(room.getLocation().getColumn() - 1);
		}
		// south
		else if (canGoEast(room)) {
		   nextLocation.setColumn(room.getLocation().getColumn() + 1);
		}

		
		return nextLocation;
		
	}


	/**
	 * 	In any room - I can moving a specific direction if
	 *  there is no wall in that direction
	 *  or you're not moving out of the entrance
	 *  or you're exiting after finding the key
	 *  the room has not been visited already
		
	 * @param room
	 * @return
	 */
	private boolean canGoNorth(MazeRoom room) {
		
		if (room.getLocation().getRow() == 0) {return false;}
		
		boolean visited = maze.getRoomMap().get(new Coordinate(room.getLocation().getRow()-1, room.getLocation().getColumn())).isVisited() == true;
		return !visited
			   && (!room.hasNorthWall() 
			   || (room.hasEntrance() && !MazeRoom.NORTH_ENTRANCE.equals(room.getSpecialNotation()))
			   || (room.hasExit() && MazeRoom.NORTH_EXIT.equals(room.getSpecialNotation()) && foundKey));
	}
	
	private boolean canGoSouth(MazeRoom room) {
		
		if (room.getLocation().getRow() == maze.getRowCount()-1) {return false;}
		
		boolean visited = maze.getRoomMap().get(new Coordinate(room.getLocation().getRow()+1, room.getLocation().getColumn())).isVisited() == true;
		return !visited
			   && (!room.hasSouthWall() 
			   || (room.hasEntrance() && !MazeRoom.SOUTH_ENTRANCE.equals(room.getSpecialNotation()))
			   || (room.hasExit() && MazeRoom.SOUTH_EXIT.equals(room.getSpecialNotation()) && foundKey));
	}

	private boolean canGoEast(MazeRoom room) {
		if (room.getLocation().getColumn() == maze.getColCount()-1) {return false;}
		boolean visited = maze.getRoomMap().get(new Coordinate(room.getLocation().getRow(), room.getLocation().getColumn()+1)).isVisited() == true;
		return !visited
			   && (!room.hasEastWall() 
			   || (room.hasEntrance() && !MazeRoom.EAST_ENTRANCE.equals(room.getSpecialNotation()))
			   || (room.hasExit() && MazeRoom.EAST_EXIT.equals(room.getSpecialNotation()) && foundKey));
	}

	private boolean canGoWest(MazeRoom room) {
		if (room.getLocation().getColumn() == 0) {return false;}
		boolean visited = maze.getRoomMap().get(new Coordinate(room.getLocation().getRow(), room.getLocation().getColumn()-1)).isVisited() == true;
		return !visited
			   && (!room.hasWestWall() 
			   || (room.hasEntrance() && !MazeRoom.WEST_ENTRANCE.equals(room.getSpecialNotation()))
			   || (room.hasExit() && MazeRoom.WEST_EXIT.equals(room.getSpecialNotation()) && foundKey));
	}

	

}
