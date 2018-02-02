package com.fens.desktopMonitor.util;

/**
 *
 * @author leonardo
 */

import java.awt.print.PrinterJob;
import java.io.File;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 *
 * @author leonardo
 */

public class ImpresionJavaPDFBox {
        
    private File file = null;    
    private String nombreImpresora = "";
    private int copias = 1;   

    public ImpresionJavaPDFBox(File file, String nombreImpresora) {
        this.file = file;
        if (nombreImpresora!=null)
            this.nombreImpresora = nombreImpresora;
    }
    
    
    public void printSilent() throws Exception{
        
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintService(printService);
        job.setCopies(this.copias);
        
        PDDocument pdf=PDDocument.load(this.file);
        pdf.silentPrint(job);//pdf.silentPrint();
        
    }
    
    public void printSilent2() throws Exception{        
        
        PrintService[] printServices = PrinterJob.lookupPrintServices();//OBTENEMOS TODOS LOS SERVICIOS DE IMPRESION        
        PrintService printService = null;
        for(PrintService servicio : printServices){
            //comparamos el nombre del servicio de impresion con el nombre que obtenemos de base de datos
            if(nombreImpresora.equals(servicio.getName())){//si es encontrado cargamos el servicio                
                printService = servicio;                
            }/*else if(servicio.getName().contains(nombreImpresora)){//si no es encontrado buscamos que contenga el nombre
                System.out.println("--------SERVICIO ENCONTRADO SIMILAR");
                printService = servicio;
            }*/
        }
        
        //Si se encuentra mandamos a imprimir, si no, mandamos en la impresora predeterminada:
        if(printService != null){//imprime en la seleccion            
            //System.out.println("///// IMPRIMIENDO EN SELECCION");            
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintService(printService);
            job.setCopies(this.copias);
            
            PDDocument pdf = PDDocument.load(this.file);
            pdf.silentPrint(job);
        }else{//imprime en la predeterminada  
            //System.out.println("///// IMPRIMIENDO EN PREDETERMINADA . . .");
            printSilent();
        }
    }

    public int getCopias() {
        return copias;
    }

    public void setCopias(int copias) {
        this.copias = copias;
    }
    
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public String getNombreImpresora() {
        return nombreImpresora;
    }
    public void setNombreImpresora(String nombreImpresora) {
        this.nombreImpresora = nombreImpresora;
    }
    
}
