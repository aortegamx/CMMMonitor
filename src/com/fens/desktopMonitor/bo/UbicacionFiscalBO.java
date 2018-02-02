/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.bo;

import com.fens.desktopMonitor.dto.Emisor;
import com.fens.desktopMonitor.dto.Ubicacionfiscal;
import com.fens.desktopMonitor.jdbc.UbicacionfiscalDaoImpl;
import java.sql.Connection;

/**
 *
 * @author 578
 */
public class UbicacionFiscalBO {
    
    private Connection conn = null;
    private Ubicacionfiscal ubicacionFiscal  = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Ubicacionfiscal getUbicacionFiscal() {
        return ubicacionFiscal;
    }

    public void setUbicacionFiscal(Ubicacionfiscal ubicacionFiscal) {
        this.ubicacionFiscal = ubicacionFiscal;
    }

      
    public UbicacionFiscalBO(Connection conn) {
        this.conn = conn;
    }
    
    public UbicacionFiscalBO(Connection conn , int idUbicacionFiscal){     
        this.conn = conn;
        try{
            UbicacionfiscalDaoImpl ubicacionFiscalDaoImpl = new UbicacionfiscalDaoImpl(this.conn);
            this.ubicacionFiscal = ubicacionFiscalDaoImpl.findByPrimaryKey(idUbicacionFiscal);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
     public Ubicacionfiscal findByIdEmisor(Connection conn ,int idEmisor){   
         this.conn = conn;
        Ubicacionfiscal[] ubicaciones = null;

           
        try{
            UbicacionfiscalDaoImpl ubicacionFiscalDaoImpl = new UbicacionfiscalDaoImpl(this.conn);
            ubicaciones = ubicacionFiscalDaoImpl.findByEmisor(idEmisor);
            ubicacionFiscal = ubicaciones[0]; /*Relacion 1:1*/
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        
        return ubicacionFiscal;
    }
    
}
