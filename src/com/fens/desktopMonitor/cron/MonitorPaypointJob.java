/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fens.desktopMonitor.cron;

import com.cmm.cvs2xml.paypoint.bean.PayPointDatos;
import com.cmm.cvs2xml.paypoint.utils.CmmCvsConvert;
import com.fens.desktopMonitor.config.Configuration;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.util.FileManage;
import com.fens.desktopMonitor.util.GenericValidator;
import com.fens.desktopMonitor.views.Principal;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;

/**
 *
 * @author DXN
 */
public class MonitorPaypointJob implements InterruptableJob{
    
    
    private boolean _interrupted = false;

    private JobKey _jobKey = null;
    
    private Connection conn = null;
    
    private Configuracion configuracionDto = null;
    private int maxErrorConnPAC=3;
    
    private List<File> listaArchivosProcesar = new ArrayList<File>();
    
    //referencia a la vista para acceder al control Log
    Principal wPrincipal = null;
    
    

    @Override
    public void interrupt() throws UnableToInterruptJobException {
               this._interrupted = true;
        JOptionPane.showMessageDialog(null, "Interrumpiendo Instancia de Monitor PayPoint.");
        if (listaArchivosProcesar!=null && listaArchivosProcesar.size()>0
                && configuracionDto!=null){
            regresarArchivosAEntrada(configuracionDto.getRutaCarpetaOrigenPaypoint(), listaArchivosProcesar.toArray(new File[listaArchivosProcesar.size()]));
        }
    }

    
    // metodo principal execute
    //la leida de las rutas de entrada y de salida
    // mandar al cvs el archivo para leer y carga la info del objeto y sus atributos
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        this._jobKey = context.getJobDetail().getKey();
        
        System.out.println(">>Instancia MonitorPayPointJob ("+_jobKey+"), en: " + new Date() + "");
        //Invocamos garbage collector
        System.gc();
        
        //Recuperamos datos de Contexto (parametros)
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        try{
            if (conn==null)
                conn = (Connection) dataMap.get("conexion_bd");
            configuracionDto = (Configuracion) dataMap.get("configuracion_dto");
            maxErrorConnPAC = dataMap.getInt("max_error_conn_pac");
            wPrincipal =  (Principal) dataMap.get("vista_principal");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "No se asigno al Hilo del servicio una configuración.");
        }
        
        if (this._interrupted) {
            //Proceso interrumpido, no hacemos nada
            JOptionPane.showMessageDialog(null, "Instancia de Monitor PayPoint interrumpido exitosamente.");
        }else{
            
             GenericValidator gc = new GenericValidator();
            
            if (conn!=null && configuracionDto!=null){
                //Recuperamos información elemental para ejecución
                String carpetaPayPointInStr = configuracionDto.getRutaCarpetaOrigenPaypoint();
                String carpetaPayPointOut = configuracionDto.getRutaCarpetaDestinoPaypoint();
                String carpetaPayPointExec = configuracionDto.getRutaCarpetaDestinoPaypoint() + File.separator + Configuration.nombreDirErroresCancelacion ;
                File dirIn = new File(carpetaPayPointInStr);
                File dirError = new File(carpetaPayPointExec);
                
                if (gc.isValidDirectory(dirIn)){
                    dirError.mkdirs();
                    
                    //Antes de continuar nos aseguramos que no exista
                    // errores en conexion a PAC
                    /*if (configuracionDto.getErrorPacUltimaConn()>=maxErrorConnPAC){
                        //De acuerdo a la base de datos en la ultima ejecución (Job-Hilo)
                        //se registro que existe error con la conexión al PAC, lo comprobamos
                        if (!pruebaConexionPAC()){
                            //Se continua sin conexion al PAC, por lo tanto se suspende este Hilo
                            return;
                        }
                    }*/
                    
                    final String extensionArchivosMonitorear = "CSV";

                    //Leer archivos carpeta Cancelaciones In
                    File[] filesIn = dirIn.listFiles(new FilenameFilter() {
                         @Override
                         public boolean accept(File file, String name) {
                             return name.toUpperCase().endsWith("."+extensionArchivosMonitorear);
                         }
                     });
                    
                    //>>Comenzamos procesamiento
                    
                    //Movemos todos los archivos leídos de la carpeta IN a EXEC
                    for (File fileIn : filesIn) {
                        try {
                            /*File fileExec = new File(carpetaPayPointExec + File.separator + fileIn.getName());
                            if (FileManage.moveFile(fileIn.getAbsolutePath(), fileExec.getAbsolutePath())) {
                                listaArchivosProcesar.add(fileExec);
                            }*/
                            listaArchivosProcesar.add(fileIn);
                        } catch (Exception ex) {
                        }
                    }

                    //Procesamos archivos recuperados en esta instancia de Cron (Job-Hilo)
                    //y que apartamos en la carpeta EXEC
                    int erroresPACActual = configuracionDto.getErrorPacUltimaConn();
                    int erroresProcesoInesperadosActual = 0;
                    boolean procesoAbortado = false;
                    for (File archivoEntradaFormatoEConta : listaArchivosProcesar) {
                        
                        if (this._interrupted) {
                            //Si se ha dado la orden desde la vista para detener este Job
                            procesoAbortado = true;
                            break;
                        }
                        
                        try{
                            //Verificamos si se excede el maximo de errores en el Hilo
                            // actual, por concepto de conexión a PAC
                            /*if (erroresPACActual >= maxErrorConnPAC) {
                                //Si excede registramos en BD el suceso y abortamos el procesamiento
                                // de archivos del hilo actual
                                registraErrorConnPAC();
                                procesoAbortado = true;
                                break;
                            }*/

                            //Corroboramos una vez mas que el archivo a procesar no sea un Directorio
                            if (archivoEntradaFormatoEConta.isFile()) {
                                //Ejecutamos proceso de transformación y timbrado
                                //llamo a convertir a XML
                                
                                //rutaDestino
                                String rutaArchivoResultado =  configuracionDto.getRutaCarpetaDestinoPaypoint()+ File.separatorChar;
                                //nombre archivo
                                String nombreArchivoResultado = FileManage.getFileNameWithoutExtension(archivoEntradaFormatoEConta.getName()) + ".xml";
                                PayPointDatos payPointDatos = new PayPointDatos();
                                String archivoDestino = rutaArchivoResultado+nombreArchivoResultado;
                                CmmCvsConvert cmmCvsConvert = new CmmCvsConvert();
                                cmmCvsConvert.convertFile(archivoEntradaFormatoEConta,archivoDestino);
                                File fileOut= new File(carpetaPayPointOut + File.separator + archivoEntradaFormatoEConta.getName());
                                if (FileManage.moveFile(archivoEntradaFormatoEConta.getAbsolutePath(), fileOut.getAbsolutePath())) {
                                    System.out.println("Archivo movido");
                                }
                                /*EContabilidadProcesoBO eContabilidadProcesoBO
                                        = new EContabilidadProcesoBO(archivoEntradaFormatoEConta, configuracionDto, getConn());

                                eContabilidadProcesoBO.setwPrincipal(wPrincipal);
                                
                                eContabilidadProcesoBO.tryProcesoExpide();*/
                                /*if (eContabilidadProcesoBO.isErrorConexionPAC()) {
                                    //Hubo error de conexion a PAC
                                    registraErrorConnPAC();
                                    erroresPACActual++;
                                }*/

                            }
                        } catch (Exception ex) {
                            erroresProcesoInesperadosActual++;
                            String err = "Error inesperado en transformación de formatos PayPoint de 1 archivo.\n\n"+ex.toString();
                            JOptionPane.showMessageDialog(null, err);
                            //generarmos el archivo de error.
                            File fileOutError= new File(carpetaPayPointExec + File.separator + archivoEntradaFormatoEConta.getName());
                            if (FileManage.moveFile(archivoEntradaFormatoEConta.getAbsolutePath(), fileOutError.getAbsolutePath())) {
                                    System.out.println("Archivo movido a carpeta ERROR");
                            }
                            String nombreArchivoResultado = FileManage.getFileNameWithoutExtension(archivoEntradaFormatoEConta.getName()) + "_LOG.txt";
                            String errUrl = carpetaPayPointExec + File.separator + nombreArchivoResultado;
                            
                            try {
                                createFileError(err,errUrl);
                            } catch (IOException ex1) {
                                ex1.printStackTrace();
                            }
                            
                            //Cómo máximo habrá 3 errores inesperados en el procesamiento por Hilo
                            //Estos ocurren p. ej. si una configuración es incorrecta
                            if (erroresProcesoInesperadosActual>=3){
                                procesoAbortado = true;
                                break;
                            }
                            
                        }

                    }

                   /* if (procesoAbortado) {
                        if (listaArchivosProcesar!=null
                                && listaArchivosProcesar.size()>0)
                            regresarArchivosAEntrada(configuracionDto.getRutaorigencancelacionesxml(), listaArchivosProcesar.toArray(new File[listaArchivosProcesar.size()]));
                    }*/
                    
                    
                }else{
                    JOptionPane.showMessageDialog(null, "La carpeta de Origen de archivos configurada para Formatos PayPoint no existe, "
                            + "corrija la configuración y reinicie el servicio.");
                }
                
            }else{
                JOptionPane.showMessageDialog(null, "No existe una configuración válida.");
            }
            
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
    
    private Connection getConn() {
        /*if (this.conn==null){
            try {
                this.conn = ResourceManager.getConnection();
            } catch (SQLException ex) {}
        }else{
            try {
                if (this.conn.isClosed()){
                    this.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }
        return conn;*/
        conn = Conexion.getConn();
        return conn;
    }
    private void createFileError(String error, String urlError) throws IOException{
        System.out.println("Error: "+error);
        System.out.println("URL Error: "+urlError);
        File file = new File(urlError);
  
        //Create the file
        if(!file.exists()){
            if (file.createNewFile()){
                System.out.println("File is created!");
            }else{
                System.out.println("File already exists.");
            }
        }
 
        //Write Content
        FileWriter writer = new FileWriter(file);
        writer.write(error);
        writer.close();
    }
    
}

