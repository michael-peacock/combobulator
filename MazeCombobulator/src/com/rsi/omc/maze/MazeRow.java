package com.rsi.omc.maze;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MazeRow {

	List<MazeRoom> rooms = new ArrayList<>();
	
	public void add(MazeRoom room) {
		this.rooms.add(room);
	}
	
	
	
}
