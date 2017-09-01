package com.rsi.omc.ui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.rsi.omc.maze.Maze;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MazePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Maze maze;
	private int action;
	
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
        
        maze.render(g2d);
        
    }
    
    private void solveMaze(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(LINE_WIDTH));
        g2d.translate(TRANSLATE_X,TRANSLATE_Y);
        
        maze.solve(g2d);
        
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
				solveMaze(g);
				break;
				

			default:
				break;
			}
			
		}
	}

}
