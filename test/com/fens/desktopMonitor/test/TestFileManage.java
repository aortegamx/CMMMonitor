/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.test;

import com.fens.desktopMonitor.util.FileManage;
import java.io.File;

/**
 *
 * @author ISCesar
 */
public class TestFileManage {

    String rutaCarpeta = "C:\\Users\\ISCesar\\Desktop\\TRA9801201Y2_T1876_CA9D6B34-DDC5-4747-A248-0D950488F3C4";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestFileManage test = new TestFileManage();
        //test.ordenaBasico(new File(test.rutaCarpeta));
        //test.ordenaAlfabeticoAscendente(new File(test.rutaCarpeta));
        test.ordenaFechaAscendente(new File(test.rutaCarpeta));
    }
    
    protected void ordenaBasico(File dir){
        File[] files = dir.listFiles();
        for (File file : files){
            System.out.println(file.getName());
        }
    }
    
    protected void ordenaAlfabeticoAscendente(File dir){
        File[] files = FileManage.dirListByAscendingName(dir, null);
        for (File file : files){
            System.out.println(file.getName());
        }
    }
    
    protected void ordenaFechaAscendente(File dir){
        File[] files = FileManage.dirListByAscendingDate(dir, null);
        for (File file : files){
            System.out.println(file.getName());
        }
    }
    
}
