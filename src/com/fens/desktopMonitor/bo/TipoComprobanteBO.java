package com.fens.desktopMonitor.bo;

import com.fens.desktopMonitor.dto.Tipocomprobante;
import com.fens.desktopMonitor.jdbc.TipocomprobanteDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISC Cesar Martinez poseidon24@hotmail.com
 */
public class TipoComprobanteBO {
    private Tipocomprobante tipoComprobante = null;
    private Connection conn = null;

    public Tipocomprobante getTipocomprobante() {
        return tipoComprobante;
    }

    public void setTipocomprobante(Tipocomprobante tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public TipoComprobanteBO(Connection conn){ this.conn = conn;}
       
    
    public TipoComprobanteBO(Connection conn, int idTipocomprobante){        
        this.conn = conn;
        try{
            TipocomprobanteDaoImpl tipoComprobanteDaoImpl = new TipocomprobanteDaoImpl(this.conn);
            this.tipoComprobante = tipoComprobanteDaoImpl.findByPrimaryKey(idTipocomprobante);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Tipocomprobante findTipocomprobantebyId(int idTipocomprobante) throws Exception{
        Tipocomprobante tipoComprobante = null;
        
        try{
            TipocomprobanteDaoImpl tipoComprobanteDaoImpl = new TipocomprobanteDaoImpl(this.conn);
            tipoComprobante = tipoComprobanteDaoImpl.findByPrimaryKey(idTipocomprobante);
            if (tipoComprobante==null){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
            if (tipoComprobante.getIdtipocomprobante()<=0){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Tipocomprobante. Error: " + e.getMessage());
        }
        
        return tipoComprobante;
    }
    
    /**
     * Realiza una búsqueda por ID Tipocomprobante en busca de
     * coincidencias
     * @param idTipocomprobante ID Del Tipocomprobante para filtrar, -1 para mostrar todos los registros
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Tipocomprobante[] findTipocomprobantes(int idTipocomprobante, int minLimit,int maxLimit, String filtroBusqueda) {
        Tipocomprobante[] tipoComprobanteDto = new Tipocomprobante[0];
        TipocomprobanteDaoImpl tipoComprobanteDao = new TipocomprobanteDaoImpl(conn);
        try {
            String sqlFiltro="";
            if (idTipocomprobante>0){
                sqlFiltro ="IDTIPOCOMPROBANTE=" + idTipocomprobante + "  ";
            }else{
                sqlFiltro ="IDTIPOCOMPROBANTE>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = "OFFSET " + minLimit + "  ROWS FETCH NEXT " + maxLimit + "  ROWS ONLY";
            
            tipoComprobanteDto = tipoComprobanteDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY IDTIPOCOMPROBANTE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return tipoComprobanteDto;
    }
    
     public Tipocomprobante findTipoComprobantebyNombre(String nombreTipoComprobante) throws Exception{
        Tipocomprobante[] tipocomprobante = null;
        Tipocomprobante tipoComprobanteDto = null;
        try{
            TipocomprobanteDaoImpl estatusDaoImpl = new TipocomprobanteDaoImpl(this.conn);
            tipocomprobante = estatusDaoImpl.findByDynamicWhere(" NOMBRETIPOCOMPROBANTE ='"+ nombreTipoComprobante +"' FETCH FIRST 1 ROWS ONLY ", null);
            
            tipoComprobanteDto = tipocomprobante[0];
            
            if (tipoComprobanteDto==null){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
            
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Archivomaestro. Error: " + e.getMessage());
        }
        
        return tipoComprobanteDto;
    }
    
    
}