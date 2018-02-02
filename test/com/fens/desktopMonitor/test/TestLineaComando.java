/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.test;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author ISCesar
 */
public class TestLineaComando {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        TestLineaComando test = new TestLineaComando();
        test.run();
    }
    
    private void run() throws IOException{
        File file = new File("C:\\Users\\ISCesar\\Desktop\\Accesos\\Monitor Fens\\PruebasFlorida\\out\\H_AQ07CU142386_VD14_B2C63940-32B9-4CE8-9B17-1E11A185752E.pdf");
//        Process p=Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd c:/ && dir\""); //"cmd /c dir"); 
        
        // C:\Program Files (x86)\Adobe\Reader 11.0\Reader>AcroRd32.exe /t C:\Users\ISCesar\Downloads\PasedeAbordar.pdf Microsoft XPS Document Writer
        //Process p=Runtime.getRuntime().exec("C:/Program Files (x86)/Adobe/Reader 11.0/Reader/AcroRd32.exe  /t \"C:\\Users\\ISCesar\\Downloads\\PasedeAbordar.pdf\" \"Microsoft XPS Document Writer\" "); 
        Process p=Runtime.getRuntime().exec("C:/Program Files (x86)/Adobe/Reader 11.0/Reader/AcroRd32.exe  /t \"" + file.getAbsolutePath() +  "\" \"Microsoft XPS Document Writer\" "); 
        
    }
    
}
