/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.Control;

import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.bo.ConfiguracionBO;
import com.fens.desktopMonitor.cron.MonitorCanceladoJob;
import com.fens.desktopMonitor.cron.MonitorEContabilidadJob;
import com.fens.desktopMonitor.cron.MonitorPaypointJob;
import com.fens.desktopMonitor.cron.MonitorTimbradoJob;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.jdbc.ConfiguracionDaoImpl;
import com.fens.desktopMonitor.util.GenericMethods;
import com.fens.desktopMonitor.util.GenericValidator;
import com.fens.desktopMonitor.views.Principal;
import java.io.File;
import java.sql.Connection;
import java.util.Date;
import javax.swing.JOptionPane;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerFactory;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author 578
 */
public class configuracionGeneral_action {
    
    GenericValidator gc = new GenericValidator(); 
    GenericMethods gm = new GenericMethods();
    Connection conn = Conexion.getConn();
    
    //Instancia Vista
    Principal wPrincipal;
    
    public configuracionGeneral_action(Principal vista) {       
       this.wPrincipal = vista;
    }
    
    
    public void guardaDatos(){
        
                       
        /* Validación Datos */
        String msgError = "";
        
        if(!gc.isValidString(wPrincipal.txtRutaProcesar.getText(), 1, 500))            
            msgError += "\"Origen de Archivos para Procesar\"\n"; 
        if(!gc.isValidString(wPrincipal.txtRutaEjecucion.getText(), 1, 500))
            msgError += "\"Destino de Comprobantes en Ejecución\"\n"; 
        if(!gc.isValidString(wPrincipal.txtRutaExitosas.getText(), 1, 500))
            msgError += "\"Destino de Comprobantes Exitosos\"\n"; 
        if(!gc.isValidString(wPrincipal.txtRutaErrores.getText(), 1, 500))
            msgError += "\"Destino de Comprobantes con Error\"\n"; 
        if(!gc.isValidString(wPrincipal.txtRutaXmlCancelar.getText(), 1, 500))
            msgError += "\"Origen de XML para Cancelación\"\n"; 
        if(!gc.isValidString(wPrincipal.txtRutaCancelados.getText(), 1, 500))
            msgError += "\"Destino de Archivos Cancelados\"\n";
        if(wPrincipal.chkImprimir.isSelected()){
            if(wPrincipal.cmbImpresora.getSelectedIndex()<=0)
                msgError += "\"Impresora\"\n";
        }
        if(!gc.isValidString(wPrincipal.txtRenombradoArchivosTimbrados.getText(), 1, 200))
            msgError += "\"Parámetros re-nombrado archivos Timbrados\"\n";
        if (StringManage.getValidString(wPrincipal.txtRutaEjecutableAdobe.getText()).length()>0){
            if(!gc.isValidString(wPrincipal.txtRutaEjecutableAdobe.getText(), 1, 200)){
                msgError += "\"Ruta Ejecutable Adobe Reader no es válida, es opcional, puede dejarla vacía\"\n";
            }else if (!wPrincipal.txtRutaEjecutableAdobe.getText().endsWith("AcroRd32.exe")){
                msgError += "\"Ruta Ejecutable Adobe Reader no es válida, no apunta a la aplicación AcroRd32.exe.\"";
            }else if (! new File(wPrincipal.txtRutaEjecutableAdobe.getText()).exists()) {
                msgError += "\"Ruta Ejecutable Adobe Reader no es válida, la aplicación AcroRd32.exe no existe.\"";
            }
            
        }
        
              
        /* Valida rutas duplicadas*/
        
        String pathArray[] = new String[6];
        pathArray[0] = wPrincipal.txtRutaProcesar.getText();
        pathArray[1] = wPrincipal.txtRutaEjecucion.getText();
        pathArray[2] = wPrincipal.txtRutaExitosas.getText();
        pathArray[3] = wPrincipal.txtRutaErrores.getText();
        pathArray[4] = wPrincipal.txtRutaXmlCancelar.getText();
        pathArray[5] = wPrincipal.txtRutaCancelados.getText();
               
        int conta = 0;
        for (int i = 0; i < pathArray.length ; i ++){
            for (int j = 0; j < pathArray.length ; j ++){
                if(pathArray[i].equals(pathArray[j])){                      
                    conta = conta + 1;
                    
                }
            }
        }
        
        if(conta > 6)
            msgError += "\"Rutas Duplicadas\"\n";  
        
                
        
        if(msgError.equals("")){
            
            /*Recupera Datos */
        
            String rutaProcesar = wPrincipal.txtRutaProcesar.getText().trim();
            String rutaEjecucion = wPrincipal.txtRutaEjecucion.getText().trim();
            String rutaExitosas = wPrincipal.txtRutaExitosas.getText().trim();
            String rutaErrores = wPrincipal.txtRutaErrores.getText().trim();
            String rutaXmlCancelar = wPrincipal.txtRutaXmlCancelar.getText().trim();
            String rutaXmlCancelados = wPrincipal.txtRutaCancelados.getText().trim();
            String impresora = (String) wPrincipal.cmbImpresora.getSelectedItem();
            String parametrosRenombradoTimbrados = wPrincipal.txtRenombradoArchivosTimbrados.getText().trim();
            String rutaEjecutableAdobeReader =  wPrincipal.txtRutaEjecutableAdobe.getText().trim();
            int demon = (int) wPrincipal.spnDemon.getValue();
            int activarImpresion;
            int lotePDFConcentrado;
            int ordenLecturaArchivos = wPrincipal.cmbOrdenLectura.getSelectedIndex() + 1;
            
            if(wPrincipal.chkImprimir.isSelected()){
                activarImpresion =  1;
            }else{
                activarImpresion = 0;
                impresora ="";
            }        
                    
            if (wPrincipal.chkLotePDFConcentrado.isSelected()){
                lotePDFConcentrado = 1;
            }else{
                lotePDFConcentrado = 0;
            }
                    
            /* Instanciamos objeto*/
            ConfiguracionBO configuracionBO = new ConfiguracionBO(conn,1); //Enviamos 1 por default ya que solo se guarda 1 registro de config del sistema.
            Configuracion configuracionDto = configuracionBO.getConfiguracion();            
           
            
            /* Seteamos  obj*/
            
            configuracionDto.setRutaorigenprocesar(rutaProcesar);
            configuracionDto.setRutacarpetaejecucion(rutaEjecucion);
            configuracionDto.setRutacarpetaexitosos(rutaExitosas);
            configuracionDto.setRutacarpetaerrores(rutaErrores);
            configuracionDto.setRutaorigencancelacionesxml(rutaXmlCancelar);
            configuracionDto.setRutadestinocancelacionesxml(rutaXmlCancelados);
            configuracionDto.setMinutosdemon(demon);
            configuracionDto.setActivarimpresion(activarImpresion);
            if(activarImpresion!=0)/* Si esta deshabilitada la casilla no actualice con "" la impresora - deja la que estaba*/
                configuracionDto.setImpresorapredeterminada(impresora);
            configuracionDto.setRenamearchivostimbrados(parametrosRenombradoTimbrados);
            configuracionDto.setRutaEjecutableAdobeReader(rutaEjecutableAdobeReader);
            configuracionDto.setLotePdfConcentrado(lotePDFConcentrado);
            configuracionDto.setOrdenLecturaArchivos(ordenLecturaArchivos);
            
            
            /* Guarda Datos */
            try{
                
                ConfiguracionDaoImpl configuracionDaoImpl = new ConfiguracionDaoImpl(this.conn);                
                configuracionDaoImpl.update(configuracionDto.createPk(), configuracionDto);
                
                JOptionPane.showMessageDialog(null,"Datos Guardados Satisfactoriamente.","Información",JOptionPane.INFORMATION_MESSAGE); 
                
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Ocurrio un error al guardar el registro:\n " + e.toString(),"Error",JOptionPane.ERROR_MESSAGE);                
            }
           
                 
            
        }else{
            
            /* Mensaje Error */ 
            JOptionPane.showMessageDialog(null,"Datos No Validos :\n" + msgError ,"Error",JOptionPane.ERROR_MESSAGE); 
        }
        
            
    }
    
    public void iniciarServiciosMonitor(boolean isReinicio){
        try{
            if (wPrincipal.getSchedulerCron()==null){
                SchedulerFactory sf = new StdSchedulerFactory();
                wPrincipal.setSchedulerCron(sf.getScheduler());
                
                //Connection conn = ResourceManager.getConnection();
                ConfiguracionBO configuracionBO = new ConfiguracionBO(conn, 1);
                Configuracion configuracionDto = configuracionBO.getConfiguracion();

                JobDataMap jobDataMap = new JobDataMap();
                jobDataMap.put("configuracion_dto", configuracionDto);
                jobDataMap.put("conexion_bd", conn);
                jobDataMap.put("max_error_conn_pac", 3); //3 errores maximo de conexion a PAC
                jobDataMap.put("vista_principal", wPrincipal); //referencia a vista, para poderla actualizar

                //Job Timbrado
                JobDetail jobTimbrado = JobBuilder.newJob(MonitorTimbradoJob.class)
                        .withIdentity("jobTimbrado", "group1")
                        .usingJobData(jobDataMap)
                        .build();
                
                //Job Cancelado
                JobDetail jobCancelado = JobBuilder.newJob(MonitorCanceladoJob.class)
                        .withIdentity("jobCancelado", "group1")
                        .usingJobData(jobDataMap)
                        .build();
                
                //Job E-Contabilidad
                JobDetail jobEContabilidad = JobBuilder.newJob(MonitorEContabilidadJob.class)
                        .withIdentity("jobEContabilidad", "group1")
                        .usingJobData(jobDataMap)
                        .build();
                
                //Job E-Contabilidad
                JobDetail jobPayPoint = JobBuilder.newJob(MonitorPaypointJob.class)
                        .withIdentity("jobPayPoint", "group1")
                        .usingJobData(jobDataMap)
                        .build();
                
                Trigger trigger = TriggerBuilder.newTrigger()
                                                .withIdentity("trigger", "group1")
                                                .startNow()
                                                .withSchedule(simpleSchedule()
                                                        .withIntervalInMinutes(configuracionDto.getMinutosdemon())
                                                        .repeatForever())
                                                .build();
                
                Trigger trigger2 = TriggerBuilder.newTrigger()
                                                .withIdentity("trigger2", "group1")
                                                .startNow()
                                                .withSchedule(simpleSchedule()
                                                        .withIntervalInMinutes(configuracionDto.getMinutosdemon())
                                                        .repeatForever())
                                                .build();
                
                Trigger trigger3 = TriggerBuilder.newTrigger()
                                                .withIdentity("trigger3", "group1")
                                                .startNow()
                                                .withSchedule(simpleSchedule()
                                                        .withIntervalInMinutes(configuracionDto.getMinutosdemon())
                                                        .repeatForever())
                                                .build();
                
                Trigger trigger4 = TriggerBuilder.newTrigger()
                                                .withIdentity("trigger4", "group1")
                                                .startNow()
                                                .withSchedule(simpleSchedule()
                                                        .withIntervalInMinutes(configuracionDto.getMinutosdemon())
                                                        .repeatForever())
                                                .build();
                wPrincipal.getSchedulerCron().scheduleJob(jobTimbrado, trigger);
                wPrincipal.getSchedulerCron().scheduleJob(jobCancelado, trigger2);
                wPrincipal.getSchedulerCron().scheduleJob(jobEContabilidad, trigger3);
                wPrincipal.getSchedulerCron().scheduleJob(jobPayPoint, trigger4);
                
                wPrincipal.setJobMonitorTimbrado(jobTimbrado);
                wPrincipal.setJobMonitorCancelado(jobCancelado);
                wPrincipal.setJobMonitorContabilidad(jobEContabilidad);
                wPrincipal.setJobMonitorPayPoint(jobPayPoint);

                wPrincipal.getSchedulerCron().start();
                wPrincipal.setServicioIniciado(true);
                wPrincipal.switchEstatusServicio();
                
                actualizarLogVista(" >>> Servicio iniciado - " + new Date());
                actualizarLogVista(getInfoConfiguracionServicio(configuracionDto));
                
                JOptionPane.showMessageDialog(wPrincipal, "Servicio iniciado exitosamente.");
            }
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(wPrincipal, "Error inesperado al iniciar servicio: " + ex.toString());
        }
    }
    
    public void detenerServiciosMonitor(boolean mostrarMensajes){
        try{
            if (wPrincipal.getSchedulerCron()!=null){
                //Interrumpimos Job de Timbrado
                if (wPrincipal.getJobMonitorTimbrado()!=null)
                    wPrincipal.getSchedulerCron().interrupt(wPrincipal.getJobMonitorTimbrado().getKey());
                //Interrumpimos Job de Cancelado
                if (wPrincipal.getJobMonitorCancelado()!=null)
                    wPrincipal.getSchedulerCron().interrupt(wPrincipal.getJobMonitorCancelado().getKey());
                //Interrumpimos Job de Contabilidad
                if (wPrincipal.getJobMonitorContabilidad()!=null)
                    wPrincipal.getSchedulerCron().interrupt(wPrincipal.getJobMonitorContabilidad().getKey());
                //Apagamos Servicio Scheduler de cron
                wPrincipal.getSchedulerCron().shutdown(false);
                //Asignamos a null Scheduler
                wPrincipal.setSchedulerCron(null);
                
                wPrincipal.setServicioIniciado(false);
                wPrincipal.switchEstatusServicio();
                
               actualizarLogVista(" >>> Servicio detenido - " + new Date());
                
                if (mostrarMensajes)
                    JOptionPane.showMessageDialog(wPrincipal, "Servicio detenido exitosamente.");
            }else{
                if (mostrarMensajes)
                    JOptionPane.showMessageDialog(wPrincipal, "No se ha iniciado el servicio, por lo tanto no se puede detener.");
            }
        }catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(wPrincipal, "Error inesperado al detener servicio: " + ex.toString());
        }
        
    }
    
    public void reiniciarServiciosMonitor(){        
        actualizarLogVista(" >>> Petición de reinicio de servicio - " + new Date());
        
        detenerServiciosMonitor(false);
        iniciarServiciosMonitor(true);
    }
	
    
    protected String getInfoConfiguracionServicio(Configuracion configuracion){
        String configServicio  = "";
        
        configServicio+= "--- CONFIGURACION GENERAL DE SERVICIO ---";
        configServicio+= "\n\t Liga PAC: " + configuracion.getLigapac();
        configServicio+= "\n\t Impresiones activas: " + (configuracion.getActivarimpresion()==1?"SI":"NO");
        configServicio+= "\n\t Impresora predeterminada: " + (configuracion.getImpresorapredeterminada());
        configServicio+= "\n\t Ruta ejecutable Adobe Reader: " + (configuracion.getRutaEjecutableAdobeReader());
        configServicio+= "\n\t PDF único por lote timbrado: " + (configuracion.getLotePdfConcentrado()==1?"SI":"NO");
        configServicio+= "\n\t Envío de correos activos: " + (configuracion.getEnviodecorreo()==1?"SI":"NO");
        configServicio+= "\n\t Notificación única por Lote: " + (configuracion.getLoteNotificaciones()==1?"SI":"NO");
        configServicio+= "\n\t Revision de carpetas entrada cada " + configuracion.getMinutosdemon() + " minuto(s)";
        configServicio+= "\n\t --Configuración de carpetas";
        configServicio+= "\n\t\tTimbrado entrada: " + configuracion.getRutaorigenprocesar();
        configServicio+= "\n\t\tTimbrado ejecucion temporal: " + configuracion.getRutacarpetaejecucion();
        configServicio+= "\n\t\tTimbrado salida exito: " + configuracion.getRutacarpetaexitosos();
        configServicio+= "\n\t\tTimbrado error: " + configuracion.getRutacarpetaerrores();
        configServicio+= "\n\t\tCancelado entrada: " + configuracion.getRutaorigencancelacionesxml();
        configServicio+= "\n\t\tCancelado salida: " + configuracion.getRutadestinocancelacionesxml();
        configServicio+= "\n----------------------------------------\n";
        
        return configServicio;
    }
    
    protected void actualizarLogVista(String mensaje){
        //Si tenemos conexion a la vista, actualizamos el log
        if (wPrincipal!=null){
            wPrincipal.txtLogTemporal.append("\n" + mensaje);
        }
    }
        
}
