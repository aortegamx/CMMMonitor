/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fens.desktopMonitor.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ISCesarMartinez
 */
public class GenericValidator {
    
    //metodo para validar si la fecha es correcta con formato dd/MM/yyyy
    public boolean isDate(String fechax) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = formatoFecha.parse(fechax);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    //metodo para validar correo electronio
    public boolean isEmail(String correo) {
        if (correo==null)
            return false;
        
        Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("^[\\w\\-\\_]+(\\.[\\w\\-\\_]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            System.out.println("[" + mat.group() + "]");
            return true;
        }else{
            return false;
        }        
    }
    
    /**
     * Método para validar Contraseñas/Passwords seguros
     * 
     * De esta forma comprobaremos:
     *      Contraseñas que contengan al menos una letra mayúscula.
     *      Contraseñas que contengan al menos una letra minúscula.
     *      Contraseñas que contengan al menos un número o caracter especial.
     *      Contraseñas cuya longitud sea como mínimo 8 caracteres.
     *      Contraseñas cuya longitud máxima no debe ser arbitrariamente limitada.
     * 
     * @param password
     * @return true en caso de ser contraseña segura, false en caso contrario
     */
    public boolean isPasswordSeguro(String password) {
        Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
        mat = pat.matcher(password);
        if (mat.find()) {
            System.out.println("[" + mat.group() + "]");
            return true;
        }else{
            return false;
        }        
    }
    
    /*Método que tiene la función de validar el curp*/
     public boolean isCURP(String curp){
             curp=curp.toUpperCase().trim();
             return curp.matches("[A-Z][A,E,I,O,U,X][A-Z]{2}[0-9]{2}[0-1][0-9][0-3][0-9][M,H][A-Z]{2}[B,C,D,F,G,H,J,K,L,M,N,Ñ,P,Q,R,S,T,V,W,X,Y,Z]{3}[0-9,A-Z][0-9]");//("[A-Z]{4}[0-9]{6}[H,M][A-Z]{5}[0-9]{2}");
     }//Cierra método validarCurp

     /*Método que tiene la función de validar el rfc*/
     public boolean isRFC(String rfc){
          rfc=rfc.toUpperCase().trim();
          if (rfc.trim().length()<12 || rfc.trim().length()>13){
              return false;
          }else{
            //return rfc.toUpperCase().matches("[A-Z,Ñ,&]{3,4}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z,0-9]?[A-Z,0-9]?[0-9,A-Z]?");//("[A-Z]{4}[0-9]{6}[A-Z0-9]{3}");
            return rfc.toUpperCase().matches("[A-Z,Ñ,&]{3,4}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z,0-9]?[A-Z,0-9]?[0-9,A-Z]?");//("[A-Z]{4}[0-9]{6}[A-Z0-9]{3}");
          }
      }//Cierra método validarRFC
     
     public boolean isCodigoPostal(String CP){
         CP = CP.trim();
         try{
             int test = Integer.parseInt(CP);
         }catch(Exception ex){
             return false;
         }
         if(CP.length()!=5){
             return false;
         }
         return true;
     }
     
     public boolean isNumeric(String cadena, int minLenght, int maxLenght){
         cadena = cadena.trim();
         try{
             long test = Long.parseLong(cadena);
         }catch(Exception ex){
             return false;
         }
         if(cadena.length()<minLenght){
             return false;
         }
         if(cadena.length()>maxLenght){
             return false;
         }
         return true;
     }
     
     public boolean isNumeric(String cadena){
         cadena = cadena.trim();
         try{
             long test = Long.parseLong(cadena);
         }catch(Exception ex){
             return false;
         }
         return true;
     }
     
     /**
      * Verifica que sea una cadena valida según las especificaciones
      * @param cadena
      * @param minLenght longitud mínima
      * @param maxLenght longitud máxima
      * @return 
      */
     public boolean isValidString(String cadena, int minLenght, int maxLenght){
         cadena = cadena!=null?cadena.trim():"";
         if(cadena.length()<minLenght){
             return false;
         }
         if(cadena.length()>maxLenght){
             return false;
         }
         return true;
     }
     
     /*Método que tiene la función de validar un UUID de Timbre Fiscal Digital*/
     public boolean isUUID(String uuid){
        if (uuid==null)
            return false;
        
        if (uuid.length()!=36)
            return false;
         
        uuid=uuid.toUpperCase().trim();
        return uuid.matches("[a-f0-9A-F]{8}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{4}-[a-f0-9A-F]{12}");
     }
     
     /*Método que tiene la función de validar una cadena con una logitud especifica*/
      public boolean isValidStringExact(String cadena, int exactaLength){
         cadena = cadena!=null?cadena.trim():"";
         
         if(cadena.length() != exactaLength){
             return false;
         }
        
         return true;
     }
     
      
      
      /*Método que tiene la función de validar una cadena de destinatarios de correo*/
      public boolean isDestinatariosCorreo(String cadena){
          
          cadena = cadena!=null?cadena.trim():"";           
          
          if(cadena.equals("")){
             return false;
         }
          
          if (cadena != null) {
			
                StringTokenizer st = new StringTokenizer(cadena, ";");
                while (st.hasMoreTokens()) {
                    if(!isEmail(st.nextToken()))
                        return false;
                }			
          }        

          
          return true;
      }
      
      
      /*Método que valida extension de archivo*/
      public boolean isExtensionValida(String cadena, String extension){
          
          cadena = cadena!=null?cadena.trim():"";           
          
          if(cadena.equals("")){//si es vacia regresa true ya que el dato no es obligatorio
             return true;
          }
          
          if (cadena != null) {            
              String ext = cadena.substring(cadena.lastIndexOf('.') + 1);
            
              if(ext.equalsIgnoreCase(extension)){
                  return true;
              }else{
                  return false;
              }
          }        

          
          return true;
      }
      
      /**
       * Valida que un objeto File exista en el sistema de archivos
       * @param file File a validar
       * @return true en caso de existir, false en caso contrario
       */
      public boolean isValidFile(File file){
          if (file!=null){
              return file.exists();
          }else
              return false;
      }
      
      /**
       * Valida que un objeto File exista en el sistema de archivos
       * @param file File a validar
       * @return true en caso de existir, false en caso contrario
       */
      public boolean isValidDirectory(File file){
          if (isValidFile(file)){
              return file.isDirectory();
          }else
              return false;
      }
      
      
}
