/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.bo;

import com.fens.desktopMonitor.dto.Emisor;
import com.fens.desktopMonitor.jdbc.EmisorDaoImpl;
import java.sql.Connection;

/**
 *
 * @author 578
 */
public class EmisorBO {
    
    private Connection conn = null;
    private Emisor emisor  = null;

    public EmisorBO() {
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Emisor getEmisor() {
        return emisor;
    }

    public void setEmisor(Emisor emisor) {
        this.emisor = emisor;
    }
    
    
    public EmisorBO(Connection conn){ this.conn = conn;}
    
    public EmisorBO(Connection conn,int idEmisor){  
        this.conn = conn;
        try{
            EmisorDaoImpl EmisorDaoImpl = new EmisorDaoImpl(this.conn);
            this.emisor = EmisorDaoImpl.findByPrimaryKey(idEmisor);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public Emisor findEmisorbyId(Connection conn, int idEmisor) throws Exception {
        this.conn = conn;
        Emisor Emisor = null;

        try {
            EmisorDaoImpl emisorDaoImpl = new EmisorDaoImpl(this.conn);
            Emisor = emisorDaoImpl.findByPrimaryKey(idEmisor);
            if (Emisor == null) {
                throw new Exception("No se encontro ningun Emisor que corresponda con los parámetros específicados.");
            }
            if (Emisor.getIdemisor() <= 0) {
                throw new Exception("No se encontro ningun Emisor que corresponda con los parámetros específicados.");
            }
        } catch (Exception e) {
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Emisor. Error: " + e.getMessage());
        }

        return Emisor;
    }
    
    /**
     * Realiza una búsqueda por ID Emisor en busca de
     * coincidencias
     * @param idEmisor ID Del ArchivoMaestro para filtrar, -1 para mostrar todos los registros
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Emisor[] findEmisores(int idEmisor, int minLimit,int maxLimit, String filtroBusqueda) {
        Emisor[] emisorDto = new Emisor[0];
        EmisorDaoImpl emisorDao = new EmisorDaoImpl(conn);
        try {
            String sqlFiltro="";
            if (idEmisor>0){
                sqlFiltro ="IDEMISOR=" + idEmisor + "  ";
            }else{
                sqlFiltro ="IDEMISOR>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = " OFFSET " + minLimit + "  ROWS FETCH NEXT " + maxLimit + "  ROWS ONLY";
                //sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            emisorDto = emisorDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY IDEMISOR ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return emisorDto;
    }
    
    /**
     * 
     * @param rfc
     * @return Emisor     
     */
    
    public Emisor findEmisorbyRfc(Connection conn , String rfc) throws Exception {
        Emisor[] emisores = null;
        Emisor Emisor = null;
        
        try {
            
            EmisorDaoImpl emisorDaoImpl = new EmisorDaoImpl(this.conn);
            emisores = emisorDaoImpl.findWhereRfcEquals(rfc);
            
            
            if (emisores.length > 0) {
                Emisor = emisores[0];
            }else{                
                throw new Exception("No se encontro ningun Emisor que corresponda con los parámetros específicados.");
            }
                
            
        } catch (Exception e) {
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Emisor. Error: " + e.getMessage());
        }

        return Emisor;
    }
    
}
