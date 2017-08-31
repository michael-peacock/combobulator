package com.rsi.omc.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
	
	public static final int ROOM_HEIGHT = 30;
	public static final int ROOM_WIDTH = 30;
	public static final int LINE_WIDTH = 5;
	public static final int TRANSLATE_X = 10;
	public static final int TRANSLATE_Y = 10;
	
    private void doDrawing(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(LINE_WIDTH));
        g2d.translate(TRANSLATE_X,TRANSLATE_Y);
        
        renderMaze(g2d);
        
        
    }
    
	private void renderMaze(Graphics2D g2d) {
		MainScreen mainScreen = (MainScreen) SwingUtilities.getWindowAncestor(this);
		TextArea textArea = mainScreen.getTextArea();

		int currentX = 0;
		int currentY = 0;
		
		for (Iterator<MazeRow> iterator = maze.getMazeRows().descendingIterator(); iterator.hasNext();) {
			MazeRow currentRow = (MazeRow) iterator.next();

			for (MazeRoom currentRoom : currentRow.getRooms()) {
				
				if (currentRoom.hasNorthWall()) {
					g2d.drawLine(currentX, currentY, currentX + ROOM_WIDTH, currentY);
				}
				if (currentRoom.hasSouthWall()) {
					g2d.drawLine(currentX, currentY + ROOM_HEIGHT, currentX + ROOM_WIDTH, currentY + ROOM_HEIGHT);
				}
				if (currentRoom.hasEastWall()) {
					g2d.drawLine(currentX + ROOM_WIDTH, currentY, currentX + ROOM_WIDTH, currentY + ROOM_HEIGHT);
				}
				if (currentRoom.hasWestWall()) {
					g2d.drawLine(currentX, currentY, currentX, currentY + ROOM_HEIGHT);
				}
				
				
				textArea.append("Cell: " + currentRoom + ", ");
				currentX += ROOM_WIDTH;
				
			}
			textArea.append("\n");
			currentX = 0;
			currentY += ROOM_HEIGHT;
			
			
			
		}
		
		//g2d.drawRect(x, y, width, height);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (this.maze != null) {
			doDrawing(g);
		}
	}

}
