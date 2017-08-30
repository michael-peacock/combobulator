package com.rsi.omc.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.rsi.omc.maze.Maze;
import lombok.Data;

@Data
public class MazeLoader {

	private File mazeFile;

	private Maze maze;

	public void parseMaze() {
		if (this.mazeFile != null) {
			
		try (BufferedReader br = new BufferedReader(new FileReader(this.mazeFile))) {
	    
			String line = null;
			
			while ((line = br.readLine()) != null ) {
				
				System.out.println(line);
				
			}

	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
		}
		
	}
	
	

}
