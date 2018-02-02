/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fens.desktopMonitor.bo;

import com.fens.desktopMonitor.dto.Comprobantefiscal;
import com.fens.desktopMonitor.jdbc.ComprobantefiscalDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISC Cesar Martinez poseidon24@hotmail.com
 */
public class ComprobanteFiscalBO {
    private Comprobantefiscal comprobanteFiscal = null;
    private Connection conn = null;
    
    public static final int ESTATUS_TIMBRAR_EN_PROCESO = 4;
    public static final int ESTATUS_TIMBRAR_EXITO = 5;
    public static final int ESTATUS_TIMBRAR_ERROR = 6;
    
    public static final int ESTATUS_CANCELAR_EN_PROCESO = 7;
    public static final int ESTATUS_CANCELAR_EXITO = 8;
    public static final int ESTATUS_CANCELAR_ERROR = 9;
    
    public static final int TIPO_COMPROBANTE_FACTURA = 1;
    public static final int TIPO_COMPROBANTE_NOMINA = 2;
    public static final int TIPO_COMPROBANTE_RETENCIONES = 3;

    public Comprobantefiscal getComprobantefiscal() {
        return comprobanteFiscal;
    }

    public void setComprobantefiscal(Comprobantefiscal comprobanteFiscal) {
        this.comprobanteFiscal = comprobanteFiscal;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ComprobanteFiscalBO(Connection conn){ this.conn = conn;}
       
    
    public ComprobanteFiscalBO(Connection conn, int idComprobantefiscal){        
        this.conn = conn;
        try{
            ComprobantefiscalDaoImpl comprobanteFiscalDaoImpl = new ComprobantefiscalDaoImpl(this.conn);
            this.comprobanteFiscal = comprobanteFiscalDaoImpl.findByPrimaryKey(idComprobantefiscal);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Comprobantefiscal findComprobantefiscalbyId(int idComprobantefiscal) throws Exception{
        Comprobantefiscal comprobanteFiscal = null;
        
        try{
            ComprobantefiscalDaoImpl comprobanteFiscalDaoImpl = new ComprobantefiscalDaoImpl(this.conn);
            comprobanteFiscal = comprobanteFiscalDaoImpl.findByPrimaryKey(idComprobantefiscal);
            if (comprobanteFiscal==null){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
            if (comprobanteFiscal.getIdcomprobantefiscal()<=0){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Comprobantefiscal. Error: " + e.getMessage());
        }
        
        return comprobanteFiscal;
    }
    
    /**
     * Realiza una búsqueda por ID Comprobantefiscal en busca de
     * coincidencias
     * @param idComprobanteFiscal ID Del ComprobanteFiscal para filtrar, -1 para mostrar todos los registros
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Comprobantefiscal[] findComprobantefiscales(int idComprobanteFiscal,int minLimit,int maxLimit, String filtroBusqueda) {
        Comprobantefiscal[] comprobanteFiscalDto = new Comprobantefiscal[0];
        ComprobantefiscalDaoImpl comprobanteFiscalDao = new ComprobantefiscalDaoImpl(conn);
        try {
            String sqlFiltro="";
            if (idComprobanteFiscal>0){
                sqlFiltro ="IDCOMPROBANTEFISCAL=" + idComprobanteFiscal + "  ";
            }else{
                sqlFiltro ="IDCOMPROBANTEFISCAL>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = "OFFSET " + minLimit + "  ROWS FETCH NEXT " + maxLimit + "  ROWS ONLY";
                //sqlLimit = " LIMIT " + minLimit + "," + maxLimit;
            
            comprobanteFiscalDto = comprobanteFiscalDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY IDCOMPROBANTEFISCAL ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return comprobanteFiscalDto;
    }
    
    
    public Comprobantefiscal findComprobantefiscalbyUuid(String uuid) throws Exception{
        Comprobantefiscal comprobanteFiscal = null;
        Comprobantefiscal[] comprobantesFiscales = null;
        
        try{
            ComprobantefiscalDaoImpl comprobanteFiscalDaoImpl = new ComprobantefiscalDaoImpl(this.conn);
            comprobantesFiscales = comprobanteFiscalDaoImpl.findByDynamicWhere(" UUID ='"+ uuid +"' ", null);
            
            comprobanteFiscal = comprobantesFiscales[0];
            
            if (comprobanteFiscal==null){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
            if (comprobanteFiscal.getIdcomprobantefiscal()<=0){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Comprobantefiscal. Error: " + e.getMessage());
        }
        
        return comprobanteFiscal;
    }
    
}