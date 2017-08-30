package com.rsi.omc.ui;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;



public class MazeFile {

	public static File openFile(Component component) {
		
		File selectedFile = null;
		
    	JFileChooser fileChooser = new JFileChooser();
    	fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    	int result = fileChooser.showOpenDialog(component);
    	if (result == JFileChooser.APPROVE_OPTION) {
    	    selectedFile = fileChooser.getSelectedFile();
    	    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
    	}
    	return selectedFile;
	}
	
	
}
