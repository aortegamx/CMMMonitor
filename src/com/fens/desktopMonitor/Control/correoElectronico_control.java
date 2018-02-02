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
 * @author 578
 */
public class correoElectronico_control {
    
    GenericValidator gc = new GenericValidator(); 
    GenericMethods gm = new GenericMethods();
    Connection conn = Conexion.getConn();
    
    //Instancia Vista
    Principal wPrincipal;
    
    public correoElectronico_control(Principal vista) {       
       this.wPrincipal = vista;
    }
    
    
    public void guardaDatos(){
        
                       
        /* Validación Datos */
        String msgError = "";
        
        if(wPrincipal.chkActivaEmail.isSelected()){
        
            if(!gc.isValidString(wPrincipal.txtNomCorreo.getText(), 1, 100))            
                msgError += "\"Su Nombre\"\n"; 
            if(!gc.isEmail(wPrincipal.txtDirCorreo.getText()))
                msgError += "\"Dirección de Correo\"\n";         
            if(!gc.isValidString(String.valueOf(wPrincipal.pswCorreoPass.getPassword()), 1, 50))
                msgError += "\"Contraseña\"\n";         
            if(!gc.isValidString(wPrincipal.txtServidorSmtp.getText(), 1, 50))
                msgError += "\"Servidor de Correo Saliente\"\n"; 
            if(!gc.isNumeric(wPrincipal.txtPuertoSmtp.getText(), 1, 500))
                msgError += "\"Puerto SMTP\"\n";       
            if(!gc.isValidString(wPrincipal.txtDestinatariosFijos.getText(), 0, 300))
                msgError += "\"Destinatarios Fijos\"\n";
            if(!gc.isValidString(wPrincipal.txtAsuntoCorreo.getText(), 1, 200))
                msgError += "\"Asunto\"\n";
            if(!gc.isValidString(wPrincipal.txtPantillaCorreo.getText(), 1, 1000))
                msgError += "\"Plantilla\"\n";
        
            //si se escribio en destinatarios, se valida que al menos tenga uno valido
            if (wPrincipal.txtDestinatariosFijos.getText().trim().length()>0){
                String[] listaCorreos = GenericMethods.getArrayCorreos(wPrincipal.txtDestinatariosFijos.getText().trim(), ";");
                if (listaCorreos.length<=0)
                    msgError += "\"Destinatarios Fijos, ningun correo válido\"\n";
            }
                
                 
            if(msgError.equals("")){

                /*Recupera Datos */

                String usuarioRemitente = wPrincipal.txtNomCorreo.getText().trim();
                String correoRemitente = wPrincipal.txtDirCorreo.getText().trim();
                String correoPass = String.valueOf(wPrincipal.pswCorreoPass.getPassword()).trim();
                String servidorSmtp = wPrincipal.txtServidorSmtp.getText().trim();
                int puertoSmtp = Integer.parseInt(wPrincipal.txtPuertoSmtp.getText().trim());
                String destinatariosFijos = wPrincipal.txtDestinatariosFijos.getText().trim();
                String correoAsunto = wPrincipal.txtAsuntoCorreo.getText().trim();
                String plantillaCorreo = wPrincipal.txtPantillaCorreo.getText().trim();

                int chkActivo = 0;
                if(wPrincipal.chkActivaEmail.isSelected()){
                    chkActivo =  1; /* activo */
                }else{
                    chkActivo = 0;  /* inactivo */        
                }        

                int chkAutenticacion = 0;
                if(wPrincipal.chkServerAutentic.isSelected()){
                        chkAutenticacion =  1; /* activo */
                }else{
                    chkAutenticacion = 0;  /* inactivo */        
                }

                int chkLoteNotificacion;
                if (wPrincipal.chkLoteNotificaciones.isSelected()){
                    chkLoteNotificacion = 1;
                }else{
                    chkLoteNotificacion = 0;
                }


                /* Instanciamos objeto*/
                ConfiguracionBO configuracionBO = new ConfiguracionBO(conn,1); //Enviamos 1 por default ya que solo se guarda 1 registro de config del sistema.
                Configuracion configuracionDto = configuracionBO.getConfiguracion();            


                /* Seteamos  obj*/
                configuracionDto.setEnviodecorreo(chkActivo);
                configuracionDto.setUsuarioremitente(usuarioRemitente);
                configuracionDto.setCorreoremitente(correoRemitente);
                configuracionDto.setPasswordcorreo(correoPass);
                configuracionDto.setDominiosmtp(servidorSmtp);
                configuracionDto.setPuertosmtp(puertoSmtp);
                configuracionDto.setAutenticacionsmtp(chkAutenticacion);
                configuracionDto.setDestinatariosfijos(destinatariosFijos);
                configuracionDto.setAsuntocorreo(correoAsunto);
                configuracionDto.setPlantillacorreo(plantillaCorreo);
                configuracionDto.setLoteNotificaciones(chkLoteNotificacion);



                /* Guarda Datos */
                try{

                    ConfiguracionDaoImpl configuracionDaoImpl = new ConfiguracionDaoImpl(this.conn);                
                    configuracionDaoImpl.update(configuracionDto.createPk(), configuracionDto);

                    JOptionPane.showMessageDialog(null,"Datos Guradados Satisfactoriamente.","Información",JOptionPane.INFORMATION_MESSAGE); 

                }catch(Exception e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Ocurrio un error al guardar el registro:\n " + e.toString(),"Error",JOptionPane.ERROR_MESSAGE);                
                }



            }else{

                /* Mensaje Error */ 
                JOptionPane.showMessageDialog(null,"Datos No Validos :\n" + msgError ,"Error",JOptionPane.ERROR_MESSAGE); 
            }
        }//if
            
    }//guardaDatos
    
    
    
}
