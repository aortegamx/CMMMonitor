/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.test;

import com.fens.desktopMonitor.util.ImpresionJavaPDFBox;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leonardo
 */
public class testImpresion {
    
    public static void main(String []args){
        File file = new File("C:\\Users\\leonardo\\Documents\\Leonardo\\Proyectos 2014\\CMMDesktopMonitor\\Nomina_carta1402620176558PDF.pdf");
        ImpresionJavaPDFBox im = new ImpresionJavaPDFBox(file, "Brother HL-3140CW series");
        try {
            im.printSilent2();
        } catch (Exception ex) {
            Logger.getLogger(testImpresion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
