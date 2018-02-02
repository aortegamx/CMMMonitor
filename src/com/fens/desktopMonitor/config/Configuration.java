/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author 578
 */
public class Configuration {
    
    Properties prop = null;
    String pathPropertiesFile ="/appConfig.properties";

    String jdbc_url;
    String jdbc_user;
    String jdbc_password;
    
    public static final String nombreDirErroresPAC = "PAC";
    public static final String nombreDirErroresCancelacion = "error";
    public static final String nombreDirExitoCancelacion = "exito";
    public static final String nombreDirProcesoCancelacion = "exec";
    public static final String nombreDirAdvertenciaCancelacion = "advertencia";
    
    /**
     * Inicializa los valores de Configuración de la aplicación segun el archivo de propiedades
     */
    public Configuration(){
        prop = new Properties();
        InputStream is = null;

        try {
            //is=new FileInputStream(pathPropertiesFile);
            is = this.getClass().getResourceAsStream(pathPropertiesFile);
            prop.load(is);

            for (Enumeration e = prop.keys(); e.hasMoreElements() ; ) {
                 // Obtenemos el objeto
                 Object obj = e.nextElement();
                 String propertyToken =obj.toString();
                 if (propertyToken.equals("jdbc.url")) {
                    this.jdbc_url = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("jdbc.user")) {
                    this.jdbc_user = prop.getProperty(propertyToken);
                }else if (propertyToken.equals("jdbc.password")) {
                    this.jdbc_password = prop.getProperty(propertyToken);
                }
                 
            }
        } catch(IOException ioe) {
            System.out.println("No se puedo inicializar." + ioe.getMessage());
        }
    }
    

    public String getJdbc_url() {
        return jdbc_url;
    }

    public void setJdbc_url(String jdbc_url) {
        this.jdbc_url = jdbc_url;
    }

    public String getJdbc_user() {
        return jdbc_user;
    }

    public void setJdbc_user(String jdbc_user) {
        this.jdbc_user = jdbc_user;
    }

    public String getJdbc_password() {
        return jdbc_password;
    }

    public void setJdbc_password(String jdbc_password) {
        this.jdbc_password = jdbc_password;
    }
    
        
    
    
}
