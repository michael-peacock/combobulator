package com.rsi.omc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.rsi.omc.io.MazeLoader;
import com.rsi.omc.ui.MainScreen;

@SpringBootApplication
public class CombobulatorApplication {
	
	  public static class ExceptionHandler implements Thread.UncaughtExceptionHandler {

		public void handle(Throwable thrown) {
		// for EDT exceptions
		handleException(Thread.currentThread().getName(), thrown);
		}
		
		public void uncaughtException(Thread thread, Throwable thrown) {
		// for other uncaught exceptions
		handleException(thread.getName(), thrown);
		}
		
		protected void handleException(String tname, Throwable thrown) {
		System.err.println("Exception on " + tname);
		thrown.printStackTrace();
		}
	  }

	public static void main(String[] args) {
		
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
	    System.setProperty("sun.awt.exception.handler",
	                       ExceptionHandler.class.getName());
	    
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

