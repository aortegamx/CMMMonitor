/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.init;

import com.fens.desktopMonitor.dto.Tipocomprobante;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.jdbc.TipocomprobanteDaoImpl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 2/07/2014 09:29:47 AM
 */
public class ActualizaBD {

    private Connection conn = null;
    
    public void verificaActualiza() throws SQLException, Exception{
        if (conn==null){
            //conn = ResourceManager.getConnection();
            conn = Conexion.getConn();
        }
        
        //Invocamos distintos metodos para cada tabla a actualizar
        try{
            boolean actualizacionBD;
            
            actualizacionBD = tablaAccionComprobante() || tablaConfiguracion() 
                    || tablaTipoComprobante() || tablaEmisor() || tablaComprobanteFiscal();
            
            //Otras tablas o actualizaciones requeridas a BD...
            
            
            //si hubo al menos una actualizacion de bd mostramos mensaje
            if (actualizacionBD){
                JOptionPane.showMessageDialog(null, "Estructura de base de datos Actualizada");
            }
        }catch(Exception ex){
            throw new Exception("Imposible concluir actualización de base de datos, "
                    + " probablemente la actualización se tendra que hacer manualmente"
                    + " o sustituir completamente la base de datos actual. \n"
                    + "Informe este problema a su proveedor: " + ex.toString());
        }
    }
    
    /**
     * Actualiza la estructura de la tabla configuracion
     * @return true en caso de al menos una actualizacion efectuada, false en caso contrario
     * @throws SQLException 
     */
    private boolean tablaConfiguracion() throws SQLException{
        boolean actualizacionRequerida = false;
        
        //Recuperamos las columnas actuales
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM CONFIGURACION");
        ResultSetMetaData rsmd = pstmt.getMetaData();
        int numColumns = rsmd.getColumnCount();
        
        //Columnas nuevas a verificar y crear
        
            //Nuevas columnas - 18/08/2014
            boolean existeRutaEjecutableAdobeReader = false;
            
            //Nuevas columnas - 18/09/2014
            boolean existeRutaOrigenEContabilidad = false;
            boolean existeRutaDestinoEContabilidad = false;
            
            //Nuevas columnas - 15/12/2014
            boolean existeOrdenLecturaArchivos = false;
            boolean existeLotePdfConcentrado = false;
            boolean existeLoteNotificaciones = false;
        
            //Nuevas columnas - 01/02/2017
            boolean existeRutaOrigenPayPoint = false;
            boolean existeRutaDestinoPayPoint = false;
            
        //Recorremos columnas
        int i=1;
        while (i <= numColumns) {
            //Recuperamos nombres de columnas
            String str = rsmd.getColumnName(i);
            System.out.println(str);
            
            //verificamos existencia de columnas nuevas
            if (str.equalsIgnoreCase("ruta_ejecutable_adobe_reader"))
                existeRutaEjecutableAdobeReader = true;
            
            if (str.equalsIgnoreCase("ruta_carpeta_origen_econtabilidad"))
                existeRutaOrigenEContabilidad = true;
            
            if (str.equalsIgnoreCase("ruta_carpeta_destino_econtabilidad"))
                existeRutaDestinoEContabilidad = true;
            
            if (str.equalsIgnoreCase("ruta_carpeta_origen_paypoint"))
                existeRutaOrigenPayPoint = true;
            
            if (str.equalsIgnoreCase("ruta_carpeta_destino_paypoint"))
                existeRutaDestinoPayPoint = true;
            
            if (str.equalsIgnoreCase("orden_lectura_archivos"))
                existeOrdenLecturaArchivos = true;
            
            if (str.equalsIgnoreCase("lote_pdf_concentrado"))
                existeLotePdfConcentrado = true;
            
            if (str.equalsIgnoreCase("lote_notificaciones"))
                existeLoteNotificaciones = true;
                
            i++;
        }
        
        //Agregamos a tabla columnas faltantes
        Statement st = conn.createStatement();
        if (!existeRutaEjecutableAdobeReader){
            String sql = "ALTER TABLE configuracion ADD ruta_ejecutable_adobe_reader VARCHAR(200)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        
        if (!existeRutaOrigenEContabilidad){
            String sql = "ALTER TABLE configuracion ADD ruta_carpeta_origen_econtabilidad VARCHAR(200)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        
        if (!existeRutaDestinoEContabilidad){
            String sql = "ALTER TABLE configuracion ADD ruta_carpeta_destino_econtabilidad VARCHAR(200)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        
        if (!existeRutaOrigenPayPoint){
            String sql = "ALTER TABLE configuracion ADD ruta_carpeta_origen_paypoint VARCHAR(200)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        
        if (!existeRutaDestinoPayPoint){
            String sql = "ALTER TABLE configuracion ADD ruta_carpeta_destino_paypoint VARCHAR(200)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        
        if (!existeOrdenLecturaArchivos){
            String sql = "ALTER TABLE configuracion ADD orden_lectura_archivos INT NOT NULL DEFAULT 1";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        
        if (!existeLotePdfConcentrado){
            String sql = "ALTER TABLE configuracion ADD lote_pdf_concentrado INT NOT NULL DEFAULT 0";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        
        if (!existeLoteNotificaciones){
            String sql = "ALTER TABLE configuracion ADD lote_notificaciones INT NOT NULL DEFAULT 1";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        
        return actualizacionRequerida;
    }
    
    /**
     * Actualiza la estructura de la tabla accionComprobante
     * @return true en caso de al menos una actualizacion efectuada, false en caso contrario
     * @throws SQLException 
     */
    private boolean tablaAccionComprobante() throws SQLException{
        boolean actualizacionRequerida = false;
        
        //Recuperamos las columnas actuales
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ACCIONCOMPROBANTE");
        ResultSetMetaData rsmd = pstmt.getMetaData();
        int numColumns = rsmd.getColumnCount();
        
        //Columnas nuevas a verificar y crear
        
            //Nuevas columnas - 02/07/2014
            boolean existePlantillaFactura = false;
            boolean existePlantillaNomina = false;
            boolean existeRutaLogo = false;
            //Nuevas columnas - 18/08/2014
            boolean existeActivarSoporteAdobe = false;
        
        //Recorremos columnas
        int i=1;
        while (i <= numColumns) {
            //Recuperamos nombres de columnas
            String str = rsmd.getColumnName(i);
            System.out.println(str);
            
            //verificamos existencia de columnas nuevas
            if (str.equalsIgnoreCase("plantilla_factura"))
                existePlantillaFactura = true;
            if (str.equalsIgnoreCase("plantilla_nomina"))
                existePlantillaNomina = true;
            if (str.equalsIgnoreCase("ruta_logo"))
                existeRutaLogo = true;
            if (str.equalsIgnoreCase("activar_soporte_adobe"))
                existeActivarSoporteAdobe = true;
            
            i++;
        }
        
        //Agregamos a tabla columnas faltantes
        Statement st = conn.createStatement();
        if (!existePlantillaFactura){
            String sql = "ALTER TABLE ACCIONCOMPROBANTE ADD plantilla_factura varchar(300)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        if (!existePlantillaNomina){
            String sql = "ALTER TABLE ACCIONCOMPROBANTE ADD plantilla_nomina varchar(300)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        if (!existeRutaLogo){
            String sql = "ALTER TABLE ACCIONCOMPROBANTE ADD ruta_logo varchar(300)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        if (!existeActivarSoporteAdobe){
            String sql = "ALTER TABLE ACCIONCOMPROBANTE ADD activar_soporte_adobe INT NOT NULL DEFAULT 0";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        
        return actualizacionRequerida;
    }
    
    /**
     * Actualiza la estructura de la tabla configuracion
     * @return true en caso de al menos una actualizacion efectuada, false en caso contrario
     * @throws SQLException 
     */
    private boolean tablaTipoComprobante() throws Exception{
        boolean actualizacionRequerida = false;
        
        TipocomprobanteDaoImpl tipocomprobanteDao = new TipocomprobanteDaoImpl(conn);
        
        Tipocomprobante tipoComprobanteDto = null;
        
        try {
            tipoComprobanteDto = tipocomprobanteDao.findByPrimaryKey(3);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        if (tipoComprobanteDto==null){
            tipoComprobanteDto = new Tipocomprobante();
            tipoComprobanteDto.setNombretipocomprobante("RETENCIONES");
            tipoComprobanteDto.setDescripcion("RETENCIONES");
         
            tipocomprobanteDao.insert(tipoComprobanteDto);
            
            actualizacionRequerida = true;
        }
            
            
        return actualizacionRequerida;
    }
    
    /**
     * Actualiza la estructura de la tabla Emisor
     * @return true en caso de al menos una actualizacion efectuada, false en caso contrario
     * @throws SQLException 
     */
    private boolean tablaEmisor() throws SQLException{
        boolean actualizacionRequerida = false;
        
        //Recuperamos las columnas actuales
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM EMISOR");
        ResultSetMetaData rsmd = pstmt.getMetaData();
        int numColumns = rsmd.getColumnCount();
        
        //Columnas nuevas a verificar y crear
        
            //Nuevas columnas - 31/03/2015
            boolean existePlantillaRetenciones = false;
            boolean existeSectorPrimario = false;
        
        //Recorremos columnas
        int i=1;
        while (i <= numColumns) {
            //Recuperamos nombres de columnas
            String str = rsmd.getColumnName(i);
            System.out.println(str);
            
            //verificamos existencia de columnas nuevas
            if (str.equalsIgnoreCase("plantilla_retenciones"))
                existePlantillaRetenciones = true;
            if (str.equalsIgnoreCase("sector_primario"))
                existeSectorPrimario = true;
            
            i++;
        }
        
        //Agregamos a tabla columnas faltantes
        Statement st = conn.createStatement();
        if (!existePlantillaRetenciones){
            String sql = "ALTER TABLE EMISOR ADD plantilla_retenciones varchar(300)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        
        if (!existeSectorPrimario){
            String sql = "ALTER TABLE EMISOR ADD sector_primario INT NOT NULL DEFAULT 0";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        
        return actualizacionRequerida;
    }
    
    /**
     * Actualiza la estructura de la tabla accionComprobante
     * @return true en caso de al menos una actualizacion efectuada, false en caso contrario
     * @throws SQLException 
     */
    private boolean tablaComprobanteFiscal() throws SQLException{
        boolean actualizacionRequerida = false;
        
        //Recuperamos las columnas actuales
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM COMPROBANTEFISCAL");
        ResultSetMetaData rsmd = pstmt.getMetaData();
        int numColumns = rsmd.getColumnCount();
        
        //Columnas nuevas a verificar y crear
        
            //Nuevas columnas - 02/07/2014
            boolean existeNombreReceptor = false;
            boolean existeEmail = false;
            boolean existeReferencia1 = false;
            boolean existeReferencia2 = false;
            boolean existeReferencia3 = false;
                    
            //Nuevas columnas - 18/08/2014
            boolean existeActivarSoporteAdobe = false;
        
        //Recorremos columnas
        int i=1;
        while (i <= numColumns) {
            //Recuperamos nombres de columnas
            String str = rsmd.getColumnName(i);
            System.out.println(str);
            
            //verificamos existencia de columnas nuevas
            if (str.equalsIgnoreCase("NOMBRE_RECEPTOR"))
                existeNombreReceptor = true;
            if (str.equalsIgnoreCase("EMAIL"))
                existeEmail = true;
            if (str.equalsIgnoreCase("REFERENCIA1"))
                existeReferencia1 = true;
            if (str.equalsIgnoreCase("REFERENCIA2"))
                existeReferencia2 = true;
            if (str.equalsIgnoreCase("REFERENCIA3"))
                existeReferencia3 = true;
            
            i++;
        }
        
        //Agregamos a tabla columnas faltantes
        Statement st = conn.createStatement();
        if (!existeNombreReceptor){
            String sql = "ALTER TABLE COMPROBANTEFISCAL ADD NOMBRE_RECEPTOR varchar(300)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        if (!existeEmail){
            String sql = "ALTER TABLE COMPROBANTEFISCAL ADD EMAIL varchar(60)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        if (!existeReferencia1){
            String sql = "ALTER TABLE COMPROBANTEFISCAL ADD REFERENCIA1 varchar(60)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        if (!existeReferencia2){
            String sql = "ALTER TABLE COMPROBANTEFISCAL ADD REFERENCIA2 varchar(60)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        if (!existeReferencia3){
            String sql = "ALTER TABLE COMPROBANTEFISCAL ADD REFERENCIA3 varchar(60)";
            st.executeUpdate(sql);
            
            actualizacionRequerida = true;
        }
        
        return actualizacionRequerida;
    }
    
}
