/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fens.desktopMonitor.bo;

import com.fens.desktopMonitor.dto.Archivomaestro;
import com.fens.desktopMonitor.jdbc.ArchivomaestroDaoImpl;
import java.sql.Connection;

/**
 *
 * @author ISC Cesar Martinez poseidon24@hotmail.com
 */
public class ArchivoMaestroBO {
    private Archivomaestro archivoMaestro = null;
    private Connection conn = null;
    
    public static final int ESTATUS_EN_PROCESO = 1;
    public static final int ESTATUS_EXITO = 2;
    public static final int ESTATUS_ERROR = 3;

    public Archivomaestro getArchivomaestro() {
        return archivoMaestro;
    }

    public void setArchivomaestro(Archivomaestro archivoMaestro) {
        this.archivoMaestro = archivoMaestro;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    public ArchivoMaestroBO(Connection conn){ this.conn = conn;}
       
    
    public ArchivoMaestroBO(Connection conn, int idArchivomaestro){        
        this.conn = conn;
        try{
            ArchivomaestroDaoImpl archivoMaestroDaoImpl = new ArchivomaestroDaoImpl(this.conn);
            this.archivoMaestro = archivoMaestroDaoImpl.findByPrimaryKey(idArchivomaestro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Archivomaestro findArchivomaestrobyId(int idArchivomaestro) throws Exception{
        Archivomaestro archivoMaestro = null;
        
        try{
            ArchivomaestroDaoImpl archivoMaestroDaoImpl = new ArchivomaestroDaoImpl(this.conn);
            archivoMaestro = archivoMaestroDaoImpl.findByPrimaryKey(idArchivomaestro);
            if (archivoMaestro==null){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
            if (archivoMaestro.getIdarchivomaestro()<=0){
                throw new Exception("No se encontro ningun registro que corresponda con los parámetros específicados.");
            }
        }catch(Exception e){
            throw new Exception("Ocurrió un error inesperado mientras se intentaba recuperar la información del Archivomaestro. Error: " + e.getMessage());
        }
        
        return archivoMaestro;
    }
    
    /**
     * Realiza una búsqueda por ID Archivomaestro en busca de
     * coincidencias
     * @param idArchivoMaestro ID Del ArchivoMaestro para filtrar, -1 para mostrar todos los registros
     *  @param minLimit Limite inferior de la paginación (Desde) 0 en caso de no existir limite inferior
     * @param maxLimit Limite superior de la paginación (Hasta) 0 en caso de no existir limite superior
     * @param filtroBusqueda Cadena con un filtro de búsqueda personalizado
     * @return DTO Marca
     */
    public Archivomaestro[] findArchivomaestros(int idArchivoMaestro, int minLimit,int maxLimit, String filtroBusqueda) {
        Archivomaestro[] archivoMaestroDto = new Archivomaestro[0];
        ArchivomaestroDaoImpl archivoMaestroDao = new ArchivomaestroDaoImpl(conn);
        try {
            String sqlFiltro="";
            if (idArchivoMaestro>0){
                sqlFiltro ="IDARCHIVOMAESTRO=" + idArchivoMaestro + "  ";
            }else{
                sqlFiltro ="IDARCHIVOMAESTRO>0 ";
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
            
            archivoMaestroDto = archivoMaestroDao.findByDynamicWhere( 
                    sqlFiltro
                    + " ORDER BY IDARCHIVOMAESTRO ASC"
                    + sqlLimit
                    , new Object[0]);
            
        } catch (Exception ex) {
            System.out.println("Error de consulta a Base de Datos: " + ex.toString());
            ex.printStackTrace();
        }
        
        return archivoMaestroDto;
    }
    
}