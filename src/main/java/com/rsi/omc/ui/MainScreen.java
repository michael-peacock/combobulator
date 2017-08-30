package com.rsi.omc.ui;

import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rsi.omc.io.MazeLoader;

@Component
public class MainScreen extends JFrame {


	private static final long serialVersionUID = 1L;
	private int height = 200;
	private int width = 500;
	private File mazeFile;
	
	@Autowired
	MazeLoader mazeLoader;

	public MainScreen() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

	public MainScreen(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	public MainScreen(String title) throws HeadlessException {
		super(title);
		
	}

	public MainScreen(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
	}

    public void initUI() {

        JButton quitButton = new JButton("Quit");

        quitButton.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });
        
        JButton openButton = new JButton("Open Maze");
        
        openButton.addActionListener((ActionEvent event) -> {

        	this.mazeFile = MazeFile.openFile(this);
        	readFile();
        	

        });
        

        createLayout(openButton, quitButton);

        setTitle(this.getTitle());
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    // call the file parser
    private void readFile() {
   		mazeLoader.setMazeFile(mazeFile);
   		mazeLoader.parseMaze();
	}

	private void createLayout(JComponent... arg) {

        Container pane = getContentPane();
        GroupLayout layout = new GroupLayout(pane);
        pane.setLayout(layout);

        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
        		layout.createSequentialGroup()
        		.addGroup(
        			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        				.addComponent(arg[0]) 
        		)
        		.addGroup(
            			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
              				.addComponent(arg[1]) 
        		
        		)
        );

        layout.setVerticalGroup(
        		layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(arg[0])
                        .addComponent(arg[1])
                 )
        );
    }    
	
}
