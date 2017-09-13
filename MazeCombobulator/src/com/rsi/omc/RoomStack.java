/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsi.omc;

import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

/**
 *
 * @author michael.peacock
 */
public class RoomStack<Coordinate> implements Iterable<Coordinate>{

    Stack myStack;
    Queue myQueue;
    public RoomStack() {
    }
    
    
    @Override
    public Iterator<Coordinate> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void forEach(Consumer<? super Coordinate> cnsmr) {
        Iterable.super.forEach(cnsmr); //To change body of generated methods, choose Tools | Templates.
    }
    
}
