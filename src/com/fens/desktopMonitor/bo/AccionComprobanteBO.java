package com.fens.desktopMonitor.bo;

import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.dto.Accioncomprobante;
import com.fens.desktopMonitor.jdbc.AccioncomprobanteDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISC Cesar Martinez poseidon24@hotmail.com
 */
public class AccionComprobanteBO {
    private Accioncomprobante accionComprobante = null;
    private Connection conn = null;

    public Accioncomprobante getAccioncomprobante() {
        return accionComprobante;
    }

    public void setAccioncomprobante(Accioncomprobante accionComprobante) {
        this.accionComprobante = accionComprobante;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public AccionComprobanteBO(Connection conn){ this.conn = conn;}
       
    
    public AccionComprobanteBO(Connection conn, int idAccioncomprobante){        
        this.conn = conn;
        try{
            AccioncomprobanteDaoImpl accionComprobanteDaoImpl = new AccioncomprobanteDaoImpl(this.conn);
            this.accionComprobante = accionComprobanteDaoImpl.findByPrimaryKey(idAccioncomprobante);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Accioncomprobante findAccioncomprobantebyId(int idAccioncomprobante) throws Exception{
        Accioncomprobante accionComprobante = null;
        
        try{
            AccioncomprobanteDaoImpl accionComprobanteDaoImpl = new AccioncomprobanteDaoImpl(this.conn);
            accionComprobante = accionComprobanteDaoImpl.findByPrimaryKey(idAccioncomprobante);
            if (accionComprobante==null){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
            if (accionComprobante.getIdAccionComprobante()<=0){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Accioncomprobante. Error: " + e.getMessage());
        }
        
        return accionComprobante;
    }
    
    /**
     * Realiza una búsqueda por ID Accioncomprobante en busca de
     * coincidencias
     * @param idAccioncomprobante ID Del Accioncomprobante para filtrar, -1 para mostrar todos los registros
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Accioncomprobante[] findAccioncomprobantes(int idAccioncomprobante, int minLimit,int maxLimit, String filtroBusqueda) {
        Accioncomprobante[] accionComprobanteDto = new Accioncomprobante[0];
        AccioncomprobanteDaoImpl accionComprobanteDao = new AccioncomprobanteDaoImpl(conn);
        try {
            String sqlFiltro="";
            if (idAccioncomprobante>0){
                sqlFiltro ="ID_ACCION_COMPROBANTE=" + idAccioncomprobante + "  ";
            }else{
                sqlFiltro ="ID_ACCION_COMPROBANTE>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = "OFFSET " + minLimit + "  ROWS FETCH NEXT " + maxLimit + "  ROWS ONLY";
            
            accionComprobanteDto = accionComprobanteDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_ACCION_COMPROBANTE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return accionComprobanteDto;
    }
    
    /**
     * Busca la Accioncomprobante adecuada por Emisor y Tipo de Comprobante
     * @param idEmisor emisor al que pertenece la Accioncomprobante
     * @param idTipoComprobante tipo de comprobante que sugiere la accionComprobante
     * @return Accioncomprobante unica, null en caso de no encontrar ninguna adecuada
     */
    public Accioncomprobante findUniqueAccionByClaveOrCP(String claveAccion, String codigoPostal){
        Accioncomprobante acc = null;
        String filtroBusqueda = "";
        if (!StringManage.getValidString(claveAccion).equals(""))
            filtroBusqueda  += " AND CLAVE_ACCION='" + claveAccion + "' ";
        if (!StringManage.getValidString(codigoPostal).equals("")){
            
            if (StringManage.getValidString(filtroBusqueda).length()>0){
                filtroBusqueda += " OR CODIGO_POSTAL_AUTO='"+codigoPostal + "' ";
            }else{
                filtroBusqueda += " AND CODIGO_POSTAL_AUTO='"+codigoPostal + "' ";
            }
        }
        Accioncomprobante[] accionComprobantes = findAccioncomprobantes(-1, 0, 0, filtroBusqueda);
        if (accionComprobantes.length>0){
            acc = accionComprobantes[0];
        }
        
        return acc;
    }
    
}