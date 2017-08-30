package com.rsi.omc.maze;

import lombok.Data;

@Data
public class MazeRoom {
	
	private final int northWall;
	private final int southWall;
	private final int eastWall;
	private final int westWall;
	private String specialNotation;
		
	public MazeRoom(int north, int south, int east, int west, String notation) {
			this.northWall = north;
			this.southWall = south;
			this.eastWall = east;
			this.westWall = west;			
			this.specialNotation = notation;
		}

}
