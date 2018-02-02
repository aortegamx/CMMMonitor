/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fens.desktopMonitor.test;

/**
 *
 * @author ISCesar
 */
public class TestGenerico {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       TestGenerico test = new TestGenerico();
       test.testA();
    }
    
    private void testA(){
        
        String d = "002";
        
        int claveMetodoPago = Integer.parseInt(d);
                
        System.out.println(claveMetodoPago);
        
    }
    
}
