/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsi.omc.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class ResizableCanvas extends Canvas {
 
    public ResizableCanvas() {
      super();
      // Redraw canvas when size changes.
      widthProperty().addListener(evt -> draw());
      heightProperty().addListener(evt -> draw());
    }
    public ResizableCanvas(double width, double height) {
      super(width, height);
      widthProperty().addListener(evt -> draw());
      heightProperty().addListener(evt -> draw());
    }
 
    private void draw() {
      double width = getWidth();
      double height = getHeight();
 
      GraphicsContext gc = getGraphicsContext2D();
      gc.clearRect(0, 0, width, height);

    }
 
    @Override
    public boolean isResizable() {
      return true;
    }
 
    @Override
    public double prefWidth(double height) {
      return getWidth();
    }
 
    @Override
    public double prefHeight(double width) {
      return getHeight();
    }
  }