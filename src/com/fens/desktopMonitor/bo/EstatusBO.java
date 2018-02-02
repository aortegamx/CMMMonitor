/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.bo;

import com.fens.desktopMonitor.dto.Estatus;
import com.fens.desktopMonitor.jdbc.EstatusDaoImpl;
import java.sql.Connection;

/**
 *
 * @author 578
 */
public class EstatusBO {
    
    private Connection conn = null;
    private Estatus estatus  = null;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Estatus getEstatus() {
        return estatus;
    }

    public void setEstatus(Estatus estatus) {
        this.estatus = estatus;
    }
    
    
    public Estatus findEstatusbyNombre(String nombreEstatus, int tipoEstatus) throws Exception{
        Estatus[] estatus = null;
        Estatus estatusDto = null;
        try{
            EstatusDaoImpl estatusDaoImpl = new EstatusDaoImpl(this.conn);
            estatus = estatusDaoImpl.findByDynamicWhere(" TIPOESTATUS ="+ tipoEstatus +" AND NOMBREESTATUS ='"+ nombreEstatus +"' FETCH FIRST 1 ROWS ONLY ", null);
            
            estatusDto = estatus[0];
            
            if (estatusDto==null){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
            if (estatusDto.getIdestatus()<=0){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Archivomaestro. Error: " + e.getMessage());
        }
        
        return estatusDto;
    }
    
    
    
}
