package com.rsi.omc.maze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class MazeRoom {
	
	private boolean northWall;
	private boolean southWall;
	private boolean eastWall;
	private boolean westWall;
	private String specialNotation;
		
	private static final String ONE = "1";
	public static final Map<String,String> DIRECTIONS = new HashMap<>(); 
	
	static {
		DIRECTIONS.put("N", "North");
		DIRECTIONS.put("S", "South");
		DIRECTIONS.put("E", "East");
		DIRECTIONS.put("W", "West");
	}
	
	public void setData(String[] newEntry) {
			this.northWall = ONE.equals(newEntry[0]);
			this.southWall = ONE.equals(newEntry[1]);
			this.eastWall = ONE.equals(newEntry[2]);
			this.westWall = ONE.equals(newEntry[3]);			
			this.specialNotation = newEntry[4];
	}

	public MazeRoom(String entry) {
		
		StringBuilder sb = new StringBuilder(entry);
		// delete {
		sb.deleteCharAt(0);
		// delete }
		sb.deleteCharAt(sb.length() - 1);
		String[] newEntry = sb.toString().split(",");
		setData(newEntry);
		
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

	

}
