/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fens.desktopMonitor.Control;

import com.fens.desktopMonitor.bo.ConfiguracionBO;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.jdbc.ConfiguracionDaoImpl;
import com.fens.desktopMonitor.util.GenericMethods;
import com.fens.desktopMonitor.util.GenericValidator;
import com.fens.desktopMonitor.views.Principal;
import java.sql.Connection;
import javax.swing.JOptionPane;

/**
 *
 * @author DXN
 */
public class configuracionPayPoint_action {
    GenericValidator gc = new GenericValidator(); 
    GenericMethods gm = new GenericMethods();
    Connection conn = Conexion.getConn();
    
    //Instancia Vista
    Principal wPrincipal;

    public configuracionPayPoint_action() {
    }

    public configuracionPayPoint_action(Principal wPrincipal) {
        this.wPrincipal = wPrincipal;
    }
    public void guardaDatos(){
        
                       
        /* Validación Datos */
        String msgError = "";
        
        if(!gc.isValidString(wPrincipal.txtRutaEntradaPayPoint.getText(), 1, 200))            
            msgError += "\"Origen de Archivos para Procesar\"\n"; 
        if(!gc.isValidString(wPrincipal.txtRutaDestinoPayPoint.getText(), 1, 200))
            msgError += "\"Destino de Comprobantes en Ejecución\"\n"; 
        
              
        /* Valida rutas duplicadas*/
        
        String pathArray[] = new String[2];
        pathArray[0] = wPrincipal.txtRutaEntradaPayPoint.getText();
        pathArray[1] = wPrincipal.txtRutaDestinoPayPoint.getText();
               
        int conta = 0;
        for (int i = 0; i < pathArray.length ; i ++){
            for (int j = 0; j < pathArray.length ; j ++){
                if(pathArray[i].equals(pathArray[j])){                      
                    conta = conta + 1;
                    
                }
            }
        }
        
        if(conta > 2)
            msgError += "\"Rutas Duplicadas\"\n";  
        
                
        
        if(msgError.equals("")){
            
            /*Recupera Datos */
        
            String rutaInPayPoint = wPrincipal.txtRutaEntradaPayPoint.getText().trim();
            String rutaOutPayPoint = wPrincipal.txtRutaDestinoPayPoint.getText().trim();
                    
                    
            /* Instanciamos objeto*/
            ConfiguracionBO configuracionBO = new ConfiguracionBO(conn,1); //Enviamos 1 por default ya que solo se guarda 1 registro de config del sistema.
            Configuracion configuracionDto = configuracionBO.getConfiguracion();            
           
            
            /* Seteamos  obj*/
            
            configuracionDto.setRutaCarpetaOrigenPaypoint(rutaInPayPoint);
            configuracionDto.setRutaCarpetaDestinoPaypoint(rutaOutPayPoint);
            
            
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
}
