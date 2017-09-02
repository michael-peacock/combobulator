package com.rsi.omc.ui;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rsi.omc.io.MazeLoader;

import lombok.Data;

@Component
@Data
public class MainScreen extends JFrame {


	private static final long serialVersionUID = 1L;

	private File mazeFile;
	
    private MazePanel mazePanel;
    private JButton openButton;
    private JButton renderButton;
    private JButton solveButton;
    private JButton quitButton;
    private JLabel mainLabel;
    private JSeparator hSeparator;
    private JSeparator vSeparator;
    private TextArea textArea;
	
	MazeLoader mazeLoader;
	
	public MainScreen() throws HeadlessException {
		try {
		   for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Windows Classic".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable() {
	  	    public void run() {
	  	      mazeLoader = new MazeLoader();
	          openButton = new JButton();
	          renderButton = new JButton();
	          solveButton = new JButton();
	          quitButton = new JButton();
	          vSeparator = new JSeparator();
	          mainLabel = new JLabel();
	          hSeparator = new JSeparator();
	          textArea = new TextArea();
	          mazePanel = new MazePanel();	          
	  	      
	  	    }
	    });
		
		
	}

    public void initUI() {

    	System.out.println("initUI() isEventDispatchThread = " + javax.swing.SwingUtilities.isEventDispatchThread());
        initComponents();

        setTitle(this.getTitle());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setState(JFrame.NORMAL);
        setVisible(true);
        

    }
    private void initComponents() {

    	System.out.println("initComponents() isEventDispatchThread = " + javax.swing.SwingUtilities.isEventDispatchThread());

        mazePanel.setFocusable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        initButtons();

        vSeparator.setOrientation(javax.swing.SwingConstants.VERTICAL);

        mainLabel.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        mainLabel.setForeground(new java.awt.Color(51, 51, 255));
        mainLabel.setText("Overlook Maze Combobulator");

        textArea.setName("mainTextArea"); // NOI18N
        textArea.setPreferredSize(new Dimension(1024, 150));

        mazePanel.setBackground(new java.awt.Color(255, 255, 255));
        mazePanel.setName("mainCanvas"); // NOI18N
        mazePanel.setPreferredSize(new Dimension(1024, 650));
        mazePanel.setFocusTraversalPolicyProvider(false);
        mazePanel.setFocusable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(openButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(renderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(solveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(quitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                         )
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, true)
                            .addComponent(mazePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(textArea, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(hSeparator))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(mainLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(hSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(vSeparator)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(openButton)
                                .addGap(18, 18, 18)
                                .addComponent(renderButton)
                                .addGap(18, 18, 18)
                                .addComponent(solveButton)
                                .addGap(18, 18, 18)                                
                                .addComponent(quitButton)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mazePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(textArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))))
        );

       pack();
       
       
	   	KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		
	   	focusManager.addPropertyChangeListener( new PropertyChangeListener() {
	   		        public void propertyChange(PropertyChangeEvent e) {
	   		            String prop = e.getPropertyName();
	   		            if ( "focusOwner".equals(prop) && 
	   		            		( e.getNewValue() instanceof JButton 
	   		            		|| ( e.getNewValue() instanceof JPanel )
	   		            		)
	   		            	){
	   		                java.awt.Component comp = (java.awt.Component) e.getNewValue();
	   		                String name = comp.getName();
	   		                textArea.append("Component " + name + " now in focus\n" );
	   		            }
	   		        }
	   		    }
	   		);      
	   	
		addWindowFocusListener(new WindowAdapter() {
		    public void windowGainedFocus(WindowEvent e) {
		    	openButton.requestFocusInWindow();
		    }
		});

    }

    /**
     * Setup buttons on UI
     */
    private void initButtons() {
		
    	System.out.println("initButtons() isEventDispatchThread = " + javax.swing.SwingUtilities.isEventDispatchThread());
		
		openButton.setText("Open Maze File");
        openButton.setName("openButton");
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openAction(evt);
            }
        });
        
        renderButton.setText("Render Maze");
        renderButton.setName("renderButton");
        renderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	renderAction(evt);
            }
        });

        solveButton.setText("Solve Maze");
        solveButton.setName("solveButton");
        solveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	solveAction(evt);
            }
        });        
        
        quitButton.setText("Quit");
        quitButton.setName("quitButton");
        quitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitAction(evt);
            }
        });
	}
	
   
    // =========================================================
    // ActionEvent Handlers
    private void openAction(java.awt.event.ActionEvent evt) {    

    	System.out.println("openAction() isEventDispatchThread = " + javax.swing.SwingUtilities.isEventDispatchThread());

    	textArea.setText("Loading New Maze File ...\n");
    	
    	this.mazeFile = MazeFile.openFile(this);
    	
    	if(this.mazeFile != null) {
    		readFile();
    	}
    	else {
    		textArea.append("No Maze File Selected\n");
    	}
    	
    	renderButton.requestFocusInWindow(); 
    	
    }                                        

    private void renderAction(java.awt.event.ActionEvent evt) {                                         
    	
    	System.out.println("renderAction() isEventDispatchThread = " + javax.swing.SwingUtilities.isEventDispatchThread());

    	
    	if(this.mazeFile != null) {
    		renderMaze();
    	}
    	else {
    		textArea.append("No Maze File Selected\n");
    	}
    	
    	mazePanel.requestFocusInWindow(); 
    }      

    private void solveAction(java.awt.event.ActionEvent evt) {  
    	
       	System.out.println("solveAction() isEventDispatchThread = " + javax.swing.SwingUtilities.isEventDispatchThread());

    	
    	if(this.mazeFile != null) {
    		solveMaze();
    	}
    	else {
    		textArea.append("No Maze File Selected\n");
    	}
    	
    	openButton.requestFocusInWindow(); 
    }

    private void quitAction(java.awt.event.ActionEvent evt) {   
    	dispose();
        System.exit(0);
    }                                        

    
    // =========================================================    
    // call the maze file loader
    private void readFile() {

      	System.out.println("readFile() isEventDispatchThread = " + javax.swing.SwingUtilities.isEventDispatchThread());
    	
   		mazeLoader.setMazeFile(mazeFile);
   		textArea.append("Got Maze File: " + mazeFile + "\n");
   		mazeLoader.parseMaze();
   		textArea.append("Loaded Maze Definition.\n");
   		textArea.append("Maze dimensions : " + mazeLoader.getMaze().getRowCount() + " Rows X " + mazeLoader.getMaze().getColCount() + " Columns\n");
   		textArea.append("   Entrance Location : " + mazeLoader.getMaze().getEntrance() + "\n");
   		textArea.append("   Exit Location : " + mazeLoader.getMaze().getExit() + "\n");
   		textArea.append("   Key Location : " + mazeLoader.getMaze().getKey() + "\n");
	}
    
    // =========================================================    
    // call the maze renderer
    private void renderMaze(){

      	System.out.println("renderMaze() isEventDispatchThread = " + javax.swing.SwingUtilities.isEventDispatchThread());

    	
    	mazePanel.setMaze(mazeLoader.getMaze());
    	mazePanel.setAction(MazePanel.RENDER);
    	mazePanel.validate();
    	mazePanel.repaint();
    	
    }

    private void solveMaze(){

     	System.out.println("solveMaze() isEventDispatchThread = " + javax.swing.SwingUtilities.isEventDispatchThread());
    	
    	mazePanel.setMaze(mazeLoader.getMaze());
    	mazePanel.setAction(MazePanel.SOLVE);
    	mazePanel.validate();
       	mazePanel.repaint();
    	
    }
    
    
	
}
