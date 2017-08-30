package com.rsi.omc;

import javax.swing.JFrame;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.rsi.omc.io.MazeLoader;
import com.rsi.omc.ui.MainScreen;

@SpringBootApplication
public class CombobulatorApplication extends JFrame {
	

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		
	     ConfigurableApplicationContext context = new SpringApplicationBuilder(CombobulatorApplication.class)
	                .headless(false).run(args);
		MainScreen screen = context.getBean(MainScreen.class);
		screen.setTitle("Overlook Maze Combobulator");
        screen.initUI();
        screen.setVisible(true);
		
	}
	
	@Bean
	public MazeLoader mazeLoader() {
		
		return new MazeLoader();
		
	}
}
