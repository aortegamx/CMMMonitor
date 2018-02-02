package com.fens.desktopMonitor.bo;

import com.fens.desktopMonitor.dto.Serie;
import com.fens.desktopMonitor.jdbc.SerieDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISC Cesar Martinez poseidon24@hotmail.com
 */
public class SerieBO {
    private Serie serie = null;
    private Connection conn = null;

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public SerieBO(Connection conn){ this.conn = conn;}
       
    
    public SerieBO(Connection conn, int idSerie){        
        this.conn = conn;
        try{
            SerieDaoImpl serieDaoImpl = new SerieDaoImpl(this.conn);
            this.serie = serieDaoImpl.findByPrimaryKey(idSerie);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Serie findSeriebyId(int idSerie) throws Exception{
        Serie serie = null;
        
        try{
            SerieDaoImpl serieDaoImpl = new SerieDaoImpl(this.conn);
            serie = serieDaoImpl.findByPrimaryKey(idSerie);
            if (serie==null){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
            if (serie.getIdSerie()<=0){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Serie. Error: " + e.getMessage());
        }
        
        return serie;
    }
    
    /**
     * Realiza una búsqueda por ID Serie en busca de
     * coincidencias
     * @param idSerie ID Del Serie para filtrar, -1 para mostrar todos los registros
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Serie[] findSeries(int idSerie, int minLimit,int maxLimit, String filtroBusqueda) {
        Serie[] serieDto = new Serie[0];
        SerieDaoImpl serieDao = new SerieDaoImpl(conn);
        try {
            String sqlFiltro="";
            if (idSerie>0){
                sqlFiltro ="ID_SERIE=" + idSerie + "  ";
            }else{
                sqlFiltro ="ID_SERIE>0 ";
            }
            
            if (!filtroBusqueda.trim().equals("")){
                sqlFiltro += filtroBusqueda;
            }
            
            if (minLimit<0)
                minLimit=0;
            
            String sqlLimit="";
            if ((minLimit>0 && maxLimit>0) || (minLimit==0 && maxLimit>0))
                sqlLimit = "OFFSET " + minLimit + "  ROWS FETCH NEXT " + maxLimit + "  ROWS ONLY";
            
            serieDto = serieDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY ID_SERIE ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return serieDto;
    }
    
    /**
     * Busca la Serie adecuada por Emisor y Tipo de Comprobante
     * @param idEmisor emisor al que pertenece la Serie
     * @param idTipoComprobante tipo de comprobante que sugiere la serie
     * @return Serie unica, null en caso de no encontrar ninguna adecuada
     */
    public Serie findUniqueSerieByEmisor(int idEmisor, int idTipoComprobante){
        Serie s = null;
        Serie[] series = findSeries(-1, 0, 0, 
                " AND IDEMISOR=" + idEmisor + " AND IDTIPOCOMPROBANTE="+idTipoComprobante
                + " AND ID_ESTATUS_SERIE=1"
                //+ " AND ULTIMO_FOLIO < RANGO_FIN"
                );
        if (series.length>0){
            s = series[0];
        }
        
        return s;
    }
    
}