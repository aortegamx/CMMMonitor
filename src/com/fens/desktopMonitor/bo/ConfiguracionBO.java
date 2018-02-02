/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.bo;

import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.jdbc.ConfiguracionDaoImpl;
import java.sql.Connection;

/**
 *
 * @author 578
 */
public class ConfiguracionBO {
    
    private Connection conn = null;
    private Configuracion configuracion  = null;
    

    public ConfiguracionBO(Connection conn) {
        this.conn = conn;
        
    }
    
       
    public ConfiguracionBO(Connection conn,int idConfiguracion){  
        this.conn = conn;
        try{
            ConfiguracionDaoImpl ConfiguracionDaoImpl = new ConfiguracionDaoImpl(this.conn);
            this.configuracion = ConfiguracionDaoImpl.findByPrimaryKey(idConfiguracion);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Configuracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Configuracion configuracion) {
        this.configuracion = configuracion;
    }
    
    
    
    
}
