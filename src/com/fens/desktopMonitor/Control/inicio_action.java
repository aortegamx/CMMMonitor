/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.Control;

import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.bo.ConfiguracionBO;
import com.fens.desktopMonitor.bo.comprobante.ConexionPAC;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.jdbc.ConfiguracionDaoImpl;
import com.fens.desktopMonitor.util.GenericValidator;
import com.fens.desktopMonitor.views.Inicio;
import com.fens.desktopMonitor.views.Principal;
import java.sql.Connection;
import javax.swing.JOptionPane;


/**
 *
 * @author 578
 */
public class inicio_action {
    
     GenericValidator gc = new GenericValidator(); 
     Connection conn = Conexion.getConn();
    
    //Instancia Vista
    Inicio wInicio;
    Principal wPrincipal;
   
    
    public inicio_action(Inicio vista) {       
       this.wInicio = vista;
    }
    

    public void login() {
        
        wPrincipal = new Principal();
        
        /* Validación Datos */
        String msgError = "";
        boolean flag = false;
        int acceso = -1;
        
        
        if(!gc.isValidString(wInicio.txtUsuario.getText(),1,100))
           flag = true;// msgError += "\"Usuario\"\n";   
        if(!gc.isValidString(String.valueOf(wInicio.txtContrasena.getPassword()), 1, 50))
           flag = true;// msgError += "\"Contraseña\"\n";    
        if(!gc.isValidString(wInicio.txtUrl.getText(), 1, 100))            
           flag = true;// msgError += "\"URL\"\n"; 
        



        if(!flag){

            /*Recupera Datos */

            String url = wInicio.txtUrl.getText().trim();
            String user = wInicio.txtUsuario.getText().trim();
            String pass = String.valueOf(wInicio.txtContrasena.getPassword()).trim();
            
                         

            /* Instanciamos objeto*/
            ConfiguracionBO configuracionBO = new ConfiguracionBO(conn,1); 
            Configuracion configuracionDto = configuracionBO.getConfiguracion();         


            /* Seteamos  obj*/
            configuracionDto.setLigapac(url);
            configuracionDto.setUsuariopac(user);
            configuracionDto.setPasspac(pass);
            
             /* ConexionPAC*/
            
            try{
                
            /* Instanciamos clase*/
            ConexionPAC conexionPAC = new ConexionPAC(configuracionDto); 
            
            
            acceso = conexionPAC.verificaLoginPAC();
            
                if(acceso > 0){

                    /* Entra y Guarda Datos */
                    try{

                        ConfiguracionDaoImpl configuracionDaoImpl = new ConfiguracionDaoImpl(this.conn);                
                        configuracionDaoImpl.update(configuracionDto.createPk(), configuracionDto);

                        JOptionPane.showMessageDialog(null,"Credenciales Validas.","Información",JOptionPane.INFORMATION_MESSAGE); 
                        wInicio.setVisible(false);                         
                        wPrincipal.setVisible(true); 

                    }catch(Exception e){
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null,"Ocurrio un error al guardar el registro:\n " + e.toString(),"Error",JOptionPane.ERROR_MESSAGE);                
                    }
            
                }else{

                    JOptionPane.showMessageDialog(null,"Credenciales Invalidas." + msgError ,"Información",JOptionPane.ERROR_MESSAGE); 
                }
            
                
            }catch(Exception e){
                e.printStackTrace();
               JOptionPane.showMessageDialog(null,"Problemas de Conexión. Verifique la URL","Error",JOptionPane.ERROR_MESSAGE);    
            }
            
                      
        }else{

            /* Mensaje Error */ 
            JOptionPane.showMessageDialog(null,"Credenciales Invalidas." + msgError ,"Información",JOptionPane.ERROR_MESSAGE); 
           
        }
        
        
    
    }
    
    public void inicializaDatosLogin(){
        ConfiguracionBO configuracionBO = new ConfiguracionBO(conn,1); 
        Configuracion configuracionDto = configuracionBO.getConfiguracion();
            
        if (configuracionDto!=null){
            if (!StringManage.getValidString(configuracionDto.getUsuariopac()).equals("")){
                wInicio.txtUsuario.setText(configuracionDto.getUsuariopac());
                wInicio.txtUrl.setText(configuracionDto.getLigapac());
            }
        }
        
    }
    
   
    
}
