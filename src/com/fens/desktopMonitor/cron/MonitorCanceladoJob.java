/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fens.desktopMonitor.cron;

import com.cmm.cvs2xml.util.DateManage;
import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.bo.comprobante.ComprobanteProcesoCancelaBO;
import com.fens.desktopMonitor.bo.comprobante.ConexionPAC;
import com.fens.desktopMonitor.bo.comprobante.MailBO;
import com.fens.desktopMonitor.config.Configuration;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.exceptions.ConfiguracionDaoException;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.jdbc.ConfiguracionDaoImpl;
import com.fens.desktopMonitor.util.FileManage;
import com.fens.desktopMonitor.util.GenericMethods;
import com.fens.desktopMonitor.util.GenericValidator;
import com.fens.desktopMonitor.views.Principal;
import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.mail.MessagingException;
import javax.swing.JOptionPane;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 14/06/2014 01:56:03 PM
 */
public class MonitorCanceladoJob implements InterruptableJob {

    private boolean _interrupted = false;

    private JobKey _jobKey = null;
    
    private Connection conn = null;
    
    private Configuracion configuracionDto = null;
    private int maxErrorConnPAC=3;
    
    private final List<File> listaArchivosProcesar = new ArrayList<File>();
    
    //referencia a la vista para acceder al control Log
    Principal wPrincipal = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        this._jobKey = context.getJobDetail().getKey();
        Date fechaInicioProceso = new Date();
        System.out.println(">>Instancia MonitorCanceladoJob ("+_jobKey+"), en: " + new Date() + "");
        //Invocamos garbage collector
        System.gc();
        
        int archivosEntrada = 0;
        int archivosProcesados = 0;
        int archivosPorProcesar = 0;
        int archivosExito = 0;
        int archivosError = 0;
        int archivosErrorConexionPAC = 0;
        
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
            JOptionPane.showMessageDialog(null, "Instancia de Monitor Cancelaciones interrumpido exitosamente.");
        }else{
            
             GenericValidator gc = new GenericValidator();
            
            if (conn!=null && configuracionDto!=null){
                //Recuperamos información elemental para ejecución
                boolean enviarCorreoUnico = configuracionDto.getLoteNotificaciones()==1;
                String carpetaCancelaInStr = configuracionDto.getRutaorigencancelacionesxml();
                String carpetaCancelaExec = configuracionDto.getRutaorigencancelacionesxml() + File.separator + Configuration.nombreDirProcesoCancelacion ;
                File dirIn = new File(carpetaCancelaInStr);
                File dirExec = new File(carpetaCancelaExec);
                
                if (gc.isValidDirectory(dirIn)){
                    dirExec.mkdirs();
                    
                    //Antes de continuar nos aseguramos que no exista
                    // errores en conexion a PAC
                    if (configuracionDto.getErrorPacUltimaConn()>=maxErrorConnPAC){
                        //De acuerdo a la base de datos en la ultima ejecución (Job-Hilo)
                        //se registro que existe error con la conexión al PAC, lo comprobamos
                        if (!pruebaConexionPAC()){
                            //Se continua sin conexion al PAC, por lo tanto se suspende este Hilo
                            return;
                        }
                    }
                    
                    final String extensionArchivosMonitorear = "XML";

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
                            File fileExec = new File(carpetaCancelaExec + File.separator + fileIn.getName());
                            if (FileManage.moveFile(fileIn.getAbsolutePath(), fileExec.getAbsolutePath())) {
                                listaArchivosProcesar.add(fileExec);
                            }
                        } catch (Exception ex) {
                        }
                    }

                    //Procesamos archivos recuperados en esta instancia de Cron (Job-Hilo)
                    //y que apartamos en la carpeta EXEC
                    int erroresPACActual = configuracionDto.getErrorPacUltimaConn();
                    int erroresProcesoInesperadosActual = 0;
                    boolean procesoAbortado = false;
                    List<String> listaMensajes = new ArrayList<>();
                    archivosEntrada = listaArchivosProcesar.size();
                    for (File archivoCFDICancelar : listaArchivosProcesar) {
                        
                        if (this._interrupted) {
                            //Si se ha dado la orden desde la vista para detener este Job
                            procesoAbortado = true;
                            listaMensajes.add("<b>Se detecto la Detención del Proceso de forma Manual desde la consola principal."
                                    + "<br/>Verifique la integridad de los archivos no procesados en la carpeta entrada para cancelacion.</b>");
                            break;
                        }
                        
                        try{
                            //Verificamos si se excede el maximo de errores en el Hilo
                            // actual, por concepto de conexión a PAC
                            if (erroresPACActual >= maxErrorConnPAC) {
                                //Si excede registramos en BD el suceso y abortamos el procesamiento
                                // de archivos del hilo actual
                                registraErrorConnPAC();
                                procesoAbortado = true;
                                listaMensajes.add("Suspensión del Proceso por errores recurrentes acumulados con conexión a PAC. Se supero el máximo de " 
                                        + maxErrorConnPAC + " errores de conexión o provocados por el PAC."
                                        + "<br/><b>No es necesario que vuelva a colocar los archivos restantes Por Procesar, "
                                        + "en automático serán retornados a la carpeta de cancelación de entrada, para el próximo proceso.</b>");
                                break;
                            }

                            //Corroboramos una vez mas que el archivo a procesar no sea un Directorio
                            if (archivoCFDICancelar.isFile()) {
                                //Ejecutamos proceso de transformación y timbrado
                                
                                ComprobanteProcesoCancelaBO comprobanteProcesoCancelaBO 
                                        = new ComprobanteProcesoCancelaBO(archivoCFDICancelar, configuracionDto, getConn());

                                comprobanteProcesoCancelaBO.setwPrincipal(wPrincipal);
                                //Configuramos para no enviar un correo por cada error
                                comprobanteProcesoCancelaBO.setEnviarCorreosUnicos(enviarCorreoUnico);
                                
                                if (comprobanteProcesoCancelaBO.tryProcesoCancela()){
                                    archivosExito++;
                                }else{
                                    if (!comprobanteProcesoCancelaBO.isErrorConexionPAC())
                                        archivosError++;
                                }
                                if (comprobanteProcesoCancelaBO.isErrorConexionPAC()) {
                                    //Hubo error de conexion a PAC
                                    registraErrorConnPAC();
                                    archivosErrorConexionPAC++;
                                    erroresPACActual++;
                                }
                                //Agregamos mensajes de error a lista general
                                listaMensajes.addAll(comprobanteProcesoCancelaBO.getListaMensajes());
                                
                            }
                            archivosProcesados++;
                        } catch (Exception ex) {
                            erroresProcesoInesperadosActual++;
                            listaMensajes.add("Error inesperado en proceso de 1 archivo: " + GenericMethods.exceptionStackTraceToString(ex));
                            JOptionPane.showMessageDialog(null, "Error inesperado en cancelación de 1 archivo.\n\n"+ex.toString());
                            
                            //Cómo máximo habrá 3 errores inesperados en el procesamiento por Hilo
                            //Estos ocurren p. ej. si una configuración es incorrecta
                            if (erroresProcesoInesperadosActual>=3){
                                procesoAbortado = true;
                                break;
                            }
                            
                        }

                    }

                    if (procesoAbortado) {
                        if (listaArchivosProcesar!=null
                                && listaArchivosProcesar.size()>0)
                            archivosPorProcesar = regresarArchivosAEntrada(configuracionDto.getRutaorigencancelacionesxml(), listaArchivosProcesar.toArray(new File[listaArchivosProcesar.size()]));
                    }
                    
                    Date fechaFinProceso = new Date();
                    
                    if (archivosEntrada>0){
                        if (enviarCorreoUnico)
                            enviaNotificacionUnificada(listaMensajes, fechaInicioProceso, fechaFinProceso,
                                    archivosEntrada, archivosProcesados, archivosPorProcesar, 
                                    archivosExito, archivosError, archivosErrorConexionPAC);
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null, "La carpeta de Origen de archivos configurada para Cancelado no existe, "
                            + "corrija la configuración y reinicie el servicio.");
                }
                
            }else{
                JOptionPane.showMessageDialog(null, "No existe una configuración válida.");
            }
            
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        this._interrupted = true;
        JOptionPane.showMessageDialog(null, "Interrumpiendo Instancia de Monitor Cancelaciones.");
        if (listaArchivosProcesar!=null && listaArchivosProcesar.size()>0
                && configuracionDto!=null){
            regresarArchivosAEntrada(configuracionDto.getRutaorigencancelacionesxml(), listaArchivosProcesar.toArray(new File[listaArchivosProcesar.size()]));
        }
    }
    
    private void registraErrorConnPAC(){
        if (this.configuracionDto!=null){
            try{
                //Actualizamos en BD estatus de conexion a PAC
                configuracionDto.setErrorPacUltimaConn(configuracionDto.getErrorPacUltimaConn() + 1);
                new ConfiguracionDaoImpl(getConn()).update(configuracionDto.createPk(), configuracionDto);
            }catch(ConfiguracionDaoException ex){
                System.out.println("Error al intentar actualizar estatus de conexion a PAC en configuración: " + ex.toString());
            }
        }
    }
    
    private boolean pruebaConexionPAC(){
        boolean exito = false;
        
        try{
            ConexionPAC conexionPAC = new ConexionPAC(configuracionDto);
            if (conexionPAC.verificaLoginPAC()>0){
                exito = true;
                try{
                    //Actualizamos en BD estatus de conexion a PAC
                    configuracionDto.setErrorPacUltimaConn(0);
                    new ConfiguracionDaoImpl(getConn()).update(configuracionDto.createPk(), configuracionDto);
                }catch(ConfiguracionDaoException ex){
                    System.out.println("Error al intentar actualizar estatus de conexion a PAC en configuración: " + ex.toString());
                }
            }
        }catch(Exception ex){
            System.out.println("El intento de login silencioso para probar conexion a PAC no tuvo éxito: " + ex.toString());
        }
        
        return exito;
    }
    
    protected int regresarArchivosAEntrada(String rutaEntrada, File[] listadoArchivosEnProceso){
        int archivosRetornadosAEntrada = 0;
        for (File file : listadoArchivosEnProceso){
            
            if (file.isFile()
                    && FileManage.getFileExtension(file.getName()).equalsIgnoreCase("XML")){
                
                if (file.exists()){
                    if (FileManage.moveFile(file.getAbsolutePath(), rutaEntrada + File.separator + file.getName()))
                        archivosRetornadosAEntrada++;
                }
                
            }
            
        }
        return archivosRetornadosAEntrada;
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
    
     /**
     * Realiza el envío de la notificación final por lote
     * indicando contadores y al final una lista con los mensajes 
     * de Errores presentados.
     * @param listaMensajes Lista con mensajes de Error o Alertas
     * @param fechaInicio Fecha de inicio del proceso
     * @param fechaFin Fecha de fin del proceso
     * @param nEntrada Cantidad de Archivos en Entrada
     * @param nProcesados Cantidad de Archivos Procesados
     * @param nPorProcesar Cantidad de Archivos por Procesar
     * @param nExito Cantidad de Archivos con Exito
     * @param nError Cantidad de Archivos con Error
     */
    private void enviaNotificacionUnificada(List<String> listaMensajes, 
            Date fechaInicio, Date fechaFin,
            long nEntrada, long nProcesados, long nPorProcesar, long nExito, long nError, long nErrorPAC) {
        
        List<String> listaDestinatarios = GenericMethods.getListaCorreosValidos(configuracionDto.getDestinatariosfijos(), ";");
        
        try{
            MailBO mailBO  = inicializarMail(listaDestinatarios, null, null, null);
            StringBuilder strB = new StringBuilder("******* DESCRIPCION DE INFORME <b>CANCELACIÓN</b> ******* <br/>" +
                        "<br/>" +
                        "Fecha y hora de Inicio: " + DateManage.dateToSQLDateTime(fechaInicio) +"<br/>" +
                        "Fecha y hora de Fin: " + DateManage.dateToSQLDateTime(fechaFin) +"<br/>" +
                        "<br/>" +
                        "Archivos en Lote inicial: " + nEntrada + "<br/>" +
                        "Archivos Procesados: " + nProcesados + "<br/>" +
                        ">> Archivos Procesados-Exitosos: " + nExito + "<br/>" +
                        ">> Archivos Procesados-Errores: " + nError + "<br/>" +
                        "Archivos con Problemas conexión PAC: " + nErrorPAC + "<br/>" +
                        "Archivos por Procesar: " + nPorProcesar + "<br/>" +
                        "<br/>");
            
            for (String msg : listaMensajes){
                strB.append("<hr/>");
                strB.append(msg);
                //strB.append("<hr/>");
            }
            strB.append("<br/>******* FIN DESCRIPCION DE INFORME *******");
            
            mailBO.addMessagePlantilla(strB.toString());
            int destinatariosTotal = mailBO.getToQuantity() + mailBO.getCcQuantity();
            actualizarLogVista("Enviando correo de notificación final de lote (cancelación) - " + new Date());
            if (destinatariosTotal>0)
                mailBO.send(StringManage.getValidString(configuracionDto.getAsuntocorreo()) + "- CANCELACIÓN - Fin de proceso Lote");
        }catch(Exception ex){
            actualizarLogVista("Error al enviar correo de notificación final de lote (cancelación): " + GenericMethods.exceptionStackTraceToString(ex));
        }
    }
    
    protected MailBO inicializarMail(List<String> listaDestinatarios, List<String> listaCC, 
            List<String> listaCCO, List<File> listaArchivosAdjuntos) throws Exception{
        
        MailBO mailBO = new MailBO(this.getConn(), false);
        mailBO.setConfiguration(configuracionDto);
        mailBO.setEnvioAsincrono(true);
        
        if (listaDestinatarios!=null)
            for (String dest : listaDestinatarios) {
                try {
                    mailBO.addTo(dest, dest);
                } catch (UnsupportedEncodingException | MessagingException e) {
                    System.out.println("No se pudo agregar el destinatario: " + dest + e.getMessage());
                }
            }
        
        if (listaCC!=null)
            for (String dest : listaCC) {
                try {
                    mailBO.addCC(dest, dest);
                } catch (UnsupportedEncodingException | MessagingException e) {
                    System.out.println("No se pudo agregar el destinatario CC: " + dest + e.getMessage());
                }
            }
        if (listaCCO!=null)
            for (String dest : listaCCO) {
                try {
                    mailBO.addBCC(dest, dest);
                } catch (UnsupportedEncodingException | MessagingException e) {
                    System.out.println("No se pudo agregar el destinatario CCO: " + dest + e.getMessage());
                }
            }
        
        if (listaArchivosAdjuntos!=null)
            for (File adjunto : listaArchivosAdjuntos) {
                if (adjunto!=null){
                    try {
                        mailBO.addFile(adjunto.getAbsolutePath(), adjunto.getName());
                    } catch (MessagingException e) {
                        System.out.println("No se pudo agregar el archivo Adjunto: " + adjunto.getName() + e.getMessage());
                    }
                }
            }
        
        return mailBO;
    }
    
    protected void actualizarLogVista(String mensaje){
        //Si tenemos conexion a la vista, actualizamos el log
        if (wPrincipal!=null){
            //Si el texto de la vista del Log excede de 10 mil caracteres
            // borramos esos primeros 10 mil , para dar espacio a mas
            int maxlines = 10000;
            if (wPrincipal.txtLogTemporal.getText().length()>maxlines){
                wPrincipal.txtLogTemporal.replaceRange(null, 0, maxlines);
            }
            wPrincipal.txtLogTemporal.append("\n" + mensaje);
        }
    }

}
