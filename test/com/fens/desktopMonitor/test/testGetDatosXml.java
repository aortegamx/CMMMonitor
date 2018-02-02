/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.test;

import com.fens.desktopMonitor.bo.comprobante.Cfd32BO;
import com.fens.desktopMonitor.util.FileManage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 578
 */
public class testGetDatosXml {
    
    public static void main (String[] args){
        
        //File xmlFile = new File("C:\\Users\\leonardo\\Documents\\Leonardo\\Proyectos 2014\\CMMDesktopMonitor\\CFDI_FACTURA.xml");
        File xmlFile = new File("C:\\Users\\leonardo\\Documents\\Leonardo\\Proyectos 2014\\CMMDesktopMonitor\\CFDI_NOMINA2.xml");
        //File xmlFile = new File("C:\\Users\\leonardo\\Documents\\Leonardo\\Proyectos 2014\\CMMDesktopMonitor\\CFDI_NOMINA.xml");
        //File xmlFile = new File("C:\\Users\\leonardo\\Documents\\Leonardo\\Proyectos 2014\\CMMDesktopMonitor\\CFDI_FACTURA2.xml");
        try {
            
           
            Cfd32BO cfd = new Cfd32BO(xmlFile, Cfd32BO.CONTEXT_ARRAY_COMPLEMENTOS);            
            ByteArrayOutputStream baosPDF = cfd.toPdf();
            System.out.println(" . . . PROCESO TERMINADO");
            //FileManage fileManage = new FileManage();
            
            
            //fileManage.createFileFromByteArrayOutputStream(baosPDF, "C:/pruebas/comp/" , "prueba3.pdf");  
            
        } catch (Exception e) {
           e.printStackTrace();
        }
        
        
        
    }
    
}
