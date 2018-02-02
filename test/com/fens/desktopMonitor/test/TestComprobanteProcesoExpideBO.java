/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.test;

import com.fens.desktopMonitor.bo.comprobante.ComprobanteProcesoExpideBO;
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
public class TestComprobanteProcesoExpideBO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        TestComprobanteProcesoExpideBO test = new TestComprobanteProcesoExpideBO();
        test.run();
    }
    
    public void run() throws Exception{
        
        Connection conn = ResourceManager.getConnection();
        
        Configuracion configuracionDto; //= new Configuracion();
        configuracionDto = new ConfiguracionDaoImpl(conn).findByPrimaryKey(1);
        /*configuracionDto.setUsuariopac("SERVISIM");
        configuracionDto.setPasspac("924256262544089137721036");
        configuracionDto.setLigapac("http://sctmixcoac.from-la.net:8083/CMM_Pruebas/InterconectaWs?wsdl");
        configuracionDto.setRutacarpetaejecucion("C:\\Users\\ISCesar\\Desktop\\PruebasMonitor\\exec");
        configuracionDto.setRutacarpetaexitosos("C:\\Users\\ISCesar\\Desktop\\PruebasMonitor\\exec");
        configuracionDto.setRutacarpetaerrores("C:\\Users\\ISCesar\\Desktop\\PruebasMonitor\\exec");*/
        
        //String csvFile = "C:\\Users\\ISCesar\\Dropbox\\TSP\\Equipo Fens\\Monitor CSV\\ARCHIVO_PRUEBA_NOMINA.CSV";
        /*
        String csvFile = "C:\\Users\\ISCesar\\Desktop\\AS17S2144757_R.CSV";
        File archivoMaestro = new File(csvFile);
        
        ComprobanteProcesoExpideBO comprobanteProcesoExpideBO 
                = new ComprobanteProcesoExpideBO(archivoMaestro, configuracionDto, conn);
        
        comprobanteProcesoExpideBO.tryProcesoExpide();
        */
        
        System.out.println(configuracionDto.getRutacarpetaejecucion());
        
        int maxErrorConnPAC = 2;
        
        int erroresPACActual = 0;
        boolean procesoAbortado = false;
        File dirIn = new File(configuracionDto.getRutacarpetaejecucion()); //getRutaorigenprocesar());
        if (dirIn.exists() && dirIn.isDirectory()){
        
            File[] files = dirIn.listFiles();
            for (File archivoMaestro : files){
                
                if (erroresPACActual>=maxErrorConnPAC){
                    procesoAbortado = true;
                    break;
                }
                
                if (archivoMaestro.isFile() 
                        && FileManage.getFileExtension(archivoMaestro.getName()).equalsIgnoreCase("CSV")){

                    ComprobanteProcesoExpideBO comprobanteProcesoExpideBO 
                            = new ComprobanteProcesoExpideBO(archivoMaestro, configuracionDto, conn);

                    comprobanteProcesoExpideBO.tryProcesoExpide();
                    if (comprobanteProcesoExpideBO.isErrorConexionPAC()){
                        //Hubo error de conexion a PAC
                        erroresPACActual++;
                    }

                }
                
            }
            
            if (procesoAbortado)
                regresarArchivosAEntrada(configuracionDto.getRutaorigenprocesar(), files);
            
            
        }
        
    }
    
    protected void regresarArchivosAEntrada(String rutaEntrada, File[] listadoArchivosEnProceso){
        
        for (File file : listadoArchivosEnProceso){
            
            if (file.isFile()
                    && FileManage.getFileExtension(file.getName()).equalsIgnoreCase("CSV")){
                
                if (file.exists()){
                    FileManage.moveFile(file.getAbsolutePath(), rutaEntrada + File.separator + file.getName());
                }
                
            }
            
        }
        
    }
    
}
