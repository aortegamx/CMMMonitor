/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.test;

import com.fens.desktopMonitor.bo.comprobante.ComprobanteProcesoCancelaBO;
import com.fens.desktopMonitor.config.Configuration;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.jdbc.ConfiguracionDaoImpl;
import com.fens.desktopMonitor.jdbc.ResourceManager;
import com.fens.desktopMonitor.util.FileManage;
import java.io.File;
import java.sql.Connection;

/**
 *
 * @author ISCesar
 */
public class TestComprobanteProcesoCancelaBO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        TestComprobanteProcesoCancelaBO test = new TestComprobanteProcesoCancelaBO();
        test.run();
    }
    
    public void run() throws Exception{
        
        Connection conn = ResourceManager.getConnection();
        
        Configuracion configuracionDto;
        configuracionDto = new ConfiguracionDaoImpl(conn).findByPrimaryKey(1);
        
        int maxErrorConnPAC = 2;
        
        int erroresPACActual = 0;
        boolean procesoAbortado = false;
        
        // p. ej. .../cancela_in/exec
        File dirIn = new File(configuracionDto.getRutaorigencancelacionesxml()
                                + File.separator + Configuration.nombreDirProcesoCancelacion);
        
        if (dirIn.exists() && dirIn.isDirectory()){
        
            File[] files = dirIn.listFiles();
            for (File archivoTimbradoACancelar : files){
                
                if (erroresPACActual>=maxErrorConnPAC){
                    procesoAbortado = true;
                    break;
                }
                
                if (archivoTimbradoACancelar.isFile() 
                        && FileManage.getFileExtension(archivoTimbradoACancelar.getName()).equalsIgnoreCase("XML")){

                    ComprobanteProcesoCancelaBO comprobanteProcesoExpideBO 
                            = new ComprobanteProcesoCancelaBO(archivoTimbradoACancelar, configuracionDto, conn);

                    comprobanteProcesoExpideBO.tryProcesoCancela();
                    if (comprobanteProcesoExpideBO.isErrorConexionPAC()){
                        //Hubo error de conexion a PAC
                        erroresPACActual++;
                    }

                }
                
            }
            
            if (procesoAbortado)
                regresarArchivosAEntrada(configuracionDto.getRutaorigencancelacionesxml(), files);
            
            
        }
        
    }
    
    protected void regresarArchivosAEntrada(String rutaEntrada, File[] listadoArchivosEnProceso){
        
        for (File file : listadoArchivosEnProceso){
            
            if (file.isFile()
                    && FileManage.getFileExtension(file.getName()).equalsIgnoreCase("XML")){
                
                if (file.exists()){
                    FileManage.moveFile(file.getAbsolutePath(), rutaEntrada + File.separator + file.getName());
                }
                
            }
            
        }
        
    }
    
}
