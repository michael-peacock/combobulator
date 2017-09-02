package com.rsi.omc.maze;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.rsi.omc.ui.MazePanel;

import lombok.Data;

@Data
public class MazeRoom {
	
	private boolean northWall;
	private boolean southWall;
	private boolean eastWall;
	private boolean westWall;
	private boolean visited;
	private String specialNotation;
	private Coordinate location;
	private Coordinate screenLocation;
	
		
	private static final String ONE = "1";
	public static final Map<String,String> DIRECTIONS = new HashMap<>(); 
	
	static {
		DIRECTIONS.put("N", "North");
		DIRECTIONS.put("S", "South");
		DIRECTIONS.put("E", "East");
		DIRECTIONS.put("W", "West");
	}
	
	public static final String NORTH_ENTRANCE = "B-N";
	public static final String SOUTH_ENTRANCE = "B-S";
	public static final String EAST_ENTRANCE = "B-E";
	public static final String WEST_ENTRANCE = "B-W";
	
	public static final String NORTH_EXIT = "X-N";
	public static final String SOUTH_EXIT = "X-S";
	public static final String EAST_EXIT = "X-E";
	public static final String WEST_EXIT = "X-W";
	
	public MazeRoom(String entry) {
		
		StringBuilder sb = new StringBuilder(entry);
		// delete {
		sb.deleteCharAt(0);
		// delete }
		sb.deleteCharAt(sb.length() - 1);
		String[] newEntry = sb.toString().split(",");
		setData(newEntry);
		
	}

	public void setData(String[] newEntry) {
		this.northWall = ONE.equals(newEntry[0]);
		this.southWall = ONE.equals(newEntry[1]);
		this.eastWall = ONE.equals(newEntry[2]);
		this.westWall = ONE.equals(newEntry[3]);			
		this.specialNotation = newEntry[4];
	}	
	
	public boolean hasNorthWall() {
		return northWall;
	}
	
	public boolean hasSouthWall() {
		return southWall;
	}
	
	public boolean hasEastWall(){
		return eastWall;
	}
	
	public boolean hasWestWall() {
		return westWall;
	}
	
	public boolean hasKey() {
		return "K".equalsIgnoreCase(getSpecialNotation());
	}
	
	public boolean hasExit() {
		return getSpecialNotation().startsWith("X");
	}

	public boolean hasEntrance() {
		return getSpecialNotation().startsWith("B");
	}
	
	public String getEntranceDirection() {
		if (hasEntrance()) {
			return DIRECTIONS.get( getSpecialNotation().substring(2) );
		}
		return "";
	}
	
	public String getExitDirection() {
		if (hasExit()) {
			return DIRECTIONS.get( getSpecialNotation().substring(2) );
		}
		return "";
	}

	public void render(Graphics2D g2d) {
		
		int xPos = screenLocation.getRow();
		int yPos = screenLocation.getColumn();

		if (hasNorthWall()) {
			g2d.drawLine(xPos, yPos, xPos + MazePanel.ROOM_WIDTH, yPos);
		}
		if (hasSouthWall()) {
			g2d.drawLine(xPos, yPos + MazePanel.ROOM_HEIGHT, xPos + MazePanel.ROOM_WIDTH, yPos + MazePanel.ROOM_HEIGHT);
		}
		if (hasEastWall()) {
			g2d.drawLine(xPos + MazePanel.ROOM_WIDTH, yPos, xPos + MazePanel.ROOM_WIDTH, yPos + MazePanel.ROOM_HEIGHT);
		}
		if (hasWestWall()) {
			g2d.drawLine(xPos, yPos, xPos, yPos + MazePanel.ROOM_HEIGHT);
		}
		
		if (hasEntrance() || hasExit() || hasKey()) {
			renderString(getSpecialNotation(), g2d );
		}		
				
		
	}

	public void renderString(String s, Graphics2D g2d){
		
		int xPos = screenLocation.getRow();
		int yPos = screenLocation.getColumn();
		
        int stringLen = (int) g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();
        int start = MazePanel.ROOM_WIDTH/2 - stringLen/2;
        g2d.drawString(s, start + xPos, yPos + MazePanel.ROOM_HEIGHT/2);
	}

	

}
