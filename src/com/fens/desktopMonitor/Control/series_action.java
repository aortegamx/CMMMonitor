/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.Control;

import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.bo.ComprobanteFiscalBO;
import com.fens.desktopMonitor.bo.SerieBO;
import com.fens.desktopMonitor.bo.TipoComprobanteBO;
import com.fens.desktopMonitor.dto.Serie;
import com.fens.desktopMonitor.dto.Tipocomprobante;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.jdbc.SerieDaoImpl;
import com.fens.desktopMonitor.util.GenericMethods;
import com.fens.desktopMonitor.util.GenericValidator;
import com.fens.desktopMonitor.views.Series;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 13/06/2014 05:51:31 PM
 */
public class series_action {

    private GenericValidator gc = new GenericValidator(); 
    private GenericMethods gm = new GenericMethods();
    private static Connection conn = Conexion.getConn();

    //Vista
    Series vSeries;
    
    public series_action(Series vista) {       
        this.vSeries = vista;
    }
    
    public static DefaultTableModel fillSeries(int idEmisor) throws Exception {             
        DefaultTableModel modelTable=new DefaultTableModel();
        
        SerieBO seriesBO = new SerieBO(conn);
        Serie[] series = seriesBO.findSeries(-1, 0, 0, " AND IDEMISOR = " + idEmisor);
        
        modelTable.setColumnCount(0); 
        modelTable.addColumn("ID");  /* agregando Columnas a la Jtable */
        modelTable.addColumn("ID Emisor");
        modelTable.addColumn("Tipo Comprobante");
        modelTable.addColumn("Nombre"); 
        modelTable.addColumn("Rango Inicio"); 
        modelTable.addColumn("Rango Fin");
        modelTable.addColumn("Ultimo Folio");
        modelTable.addColumn("Estatus");
        
        modelTable.setNumRows(series.length); 
        
        int i=0;
        for(Serie item : series){    
            String tipoComprobante = "DESCONOCIDO";
            switch (item.getIdtipocomprobante()){
                case ComprobanteFiscalBO.TIPO_COMPROBANTE_NOMINA:
                    tipoComprobante = "NOMINA";
                    break;
                case ComprobanteFiscalBO.TIPO_COMPROBANTE_FACTURA:
                    tipoComprobante = "FACTURA";
                    break;
                case ComprobanteFiscalBO.TIPO_COMPROBANTE_RETENCIONES:
                    tipoComprobante = "RETENCIONES";
                    break;
            }
            
            modelTable.setValueAt(item.getIdSerie(), i, 0);
            modelTable.setValueAt(item.getIdemisor(), i, 1); 
            modelTable.setValueAt(tipoComprobante, i, 2); 
            modelTable.setValueAt(item.getNombreSerie(), i, 3); 
            modelTable.setValueAt(item.getRangoIni(), i, 4); 
            modelTable.setValueAt(item.getRangoFin(), i, 5); 
            modelTable.setValueAt(item.getUltimoFolio(), i, 6); 
            modelTable.setValueAt(item.getIdEstatusSerie()==1?"ACTIVO":"INACTIVO", i,7); 
            i++;
        } 
        
        return modelTable;
    }
    
    public void cargaSerie(int idSerie) throws Exception {
        
        SerieBO serieBO = new SerieBO(conn, idSerie);
        Serie serieDto = serieBO.getSerie();
        
        if (serieDto!=null){
            /* Ponemos datos en jpanel*/
            vSeries.txtIdSerie.setText(""+serieDto.getIdSerie());
            vSeries.txtNombreSerie.setText(serieDto.getNombreSerie());
            vSeries.txtRangoInicio.setText(""+serieDto.getRangoIni());
            vSeries.txtRangoFin.setText(""+serieDto.getRangoFin());
            vSeries.txtUltimoFolio.setText("" + serieDto.getUltimoFolio());
            
            //int idEstatusIndex = -1;
            if (serieDto.getIdEstatusSerie()==1){
                //idEstatusIndex = 2;
                vSeries.comboEstatusSerie.setSelectedItem("ACTIVO");
            }else{
                //idEstatusIndex = 1;
                vSeries.comboEstatusSerie.setSelectedItem("INACTIVO");
            }
            
            TipoComprobanteBO tipoComprobanteBO = new TipoComprobanteBO(conn, serieDto.getIdtipocomprobante());
            if (tipoComprobanteBO.getTipocomprobante()!=null){
                vSeries.comboTipoComprobante.setSelectedItem(tipoComprobanteBO.getTipocomprobante());
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "El Emisor seleccionado no existe.");
        }
    }
    
    public void eliminarSerie(int idSerie, int idEmisor) throws Exception { 
       
        if(idSerie >0 ){
             SerieBO serieBO = new SerieBO(conn, idSerie);
             SerieDaoImpl serieDao = new SerieDaoImpl(conn);

             if(serieBO.getSerie()!=null){

                 Serie serieDto = serieBO.getSerie();
                 //serieDto.setIdEstatusSerie(0);     /* Estatus -1 Eliminado*/        
                 //serieDao.update(serieDto.createPk(), serieDto);
                 serieDao.delete(serieDto.createPk());

                 JOptionPane.showMessageDialog(null,"Registro eliminado satisfactoriamente.","Información",JOptionPane.INFORMATION_MESSAGE);
                 vSeries.getjTableSeries().setModel(fillSeries(idEmisor)); /* Recargamos Grid*/
                 vSeries.limpiaFormulario(vSeries.getjPanelDatosSerie());

             }else{
                 JOptionPane.showMessageDialog(null, "El Emisor seleccionado no existe.");
             }
        }else{
             JOptionPane.showMessageDialog(null, "No ha seleccionado una Serie a eliminar.");
        }
             
        
    }

    public void guardaSerie(int idEmisor) throws SQLException{
        
                       
        /* Validación Datos */
        String msgError = "";
        
        int idSerie = -1;
        int rangoInicio = -1;
        int rangoFin = -1;
        int ultimoFolio = -1;
        int idTipoComprobante = -1;
        int idEstatus = -1;
        
                   
        try{
            idSerie = Integer.parseInt(vSeries.txtIdSerie.getText());
        }catch(Exception ex){}
        
        if(!gc.isValidString(vSeries.txtNombreSerie.getText(), 1, 20))
            msgError += "\"Nombre de la Serie\"\n"; 
        if(!gc.isNumeric(vSeries.txtRangoInicio.getText(), 1, 9))
            msgError += "\"Rango Inicio\"\n";
        if(!gc.isNumeric(vSeries.txtRangoFin.getText(), 1, 9))
            msgError += "\"Rango Fin\"\n";
        if(!gc.isNumeric(vSeries.txtUltimoFolio.getText(), 1, 9))
            msgError += "\"Ultimo Folio\"\n";
        
        try{
            rangoInicio = Integer.parseInt(vSeries.txtRangoInicio.getText());
        }catch(Exception ex){
            msgError +="\"Rango Inicio\" debe ser un valor entero.\n";
        }
        try{
            rangoFin = Integer.parseInt(vSeries.txtRangoFin.getText());
        }catch(Exception ex){
            msgError +="\"Rango Fin\" debe ser un valor entero.\n";
        }
        try{
            ultimoFolio = Integer.parseInt(vSeries.txtUltimoFolio.getText());
        }catch(Exception ex){
            msgError +="\"Ultimo Folio\" debe ser un valor entero.\n";
        }
        
        
        if(!msgError.equals("")){
            /* Mensaje Error */ 
            JOptionPane.showMessageDialog(null,"Datos No Validos :\n" + msgError ,"Error",JOptionPane.ERROR_MESSAGE); 
        }
        
        if (rangoInicio<=0){
            msgError +="\"Rango Inicio\" debe ser mayor a Cero.\n";
        }
        
        if (rangoFin<=0 || rangoFin<rangoInicio
                || rangoFin<ultimoFolio){
            msgError +="\"Rango Fin\" debe ser mayor a Cero, mayor a Rango Inicio, y Mayor a ultimo Folio.\n";
        }
        
        if (ultimoFolio<(rangoInicio-1) || ultimoFolio>rangoFin){
            msgError +="\"Ultimo Folio usado\" debe ser mayor o igual a Rango Inicio (-1), y Menor a Rango Fin.\n";
        }
        
        if (vSeries.comboEstatusSerie.getSelectedIndex()>0){
            if (vSeries.comboEstatusSerie.getSelectedItem().toString().equals("ACTIVO")){
                idEstatus=1;
            }else if (vSeries.comboEstatusSerie.getSelectedItem().toString().equals("INACTIVO")){
                idEstatus=0;
            }
        }
        
        if (vSeries.comboTipoComprobante.getSelectedIndex()>0){
            try{
                Tipocomprobante tipoComprobante = (Tipocomprobante)vSeries.comboTipoComprobante.getSelectedItem();
                idTipoComprobante = tipoComprobante.getIdtipocomprobante();
            }catch(Exception ex){}
        }
        
        if (idEstatus<0){
            msgError +="\"Estatus de Serie\" no se ha seleccionado.\n";
        }
        if (idTipoComprobante<0){
            msgError +="\"Tipo de Comprobante\" no se ha seleccionado.\n";
        }
                
        
        if(msgError.equals("")){           
                        
            /* Guarda Datos */
            try{
                                
                SerieDaoImpl serieDaoImpl = new SerieDaoImpl(this.conn); 
                Serie serieDto = serieDaoImpl.findByPrimaryKey(idSerie);
                
                if(serieDto==null){ /* Si no existe la serie insertamos  ----------------------------------*/
                    
                    serieDto = new Serie();     
                    /* Seteamos  obj*/
                    serieDto.setIdemisor(idEmisor);
                    serieDto.setNombreSerie(StringManage.getValidString(vSeries.txtNombreSerie.getText()));
                    serieDto.setRangoIni(rangoInicio);
                    serieDto.setRangoFin(rangoFin);
                    serieDto.setUltimoFolio(ultimoFolio);
                    serieDto.setIdEstatusSerie(idEstatus);
                    serieDto.setIdtipocomprobante(idTipoComprobante);

                    serieDaoImpl.insert(serieDto);
                    
                }else{ /* Si existe la serie actualizamos    --------------------------------------*/  
                    
                    serieDto.setIdemisor(idEmisor);
                    serieDto.setNombreSerie(StringManage.getValidString(vSeries.txtNombreSerie.getText()));
                    serieDto.setRangoIni(rangoInicio);
                    serieDto.setRangoFin(rangoFin);
                    serieDto.setUltimoFolio(ultimoFolio);
                    serieDto.setIdEstatusSerie(idEstatus);
                    serieDto.setIdtipocomprobante(idTipoComprobante);
                    
                    serieDaoImpl.update(serieDto.createPk(), serieDto);
                       
                }
                
                JOptionPane.showMessageDialog(null,"Datos Guardados Satisfactoriamente.","Información",JOptionPane.INFORMATION_MESSAGE); 
                vSeries.getjTableSeries().setModel(fillSeries(idEmisor)); /* Recargamos Grid*/
                vSeries.limpiaFormulario(vSeries.getjPanelDatosSerie());
                
                
            }catch(Exception e){               
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Ocurrio un error al guardar el registro:\n " + e.toString(),"Error",JOptionPane.ERROR_MESSAGE);                
            }            
                    
        }else{
            
            /* Mensaje Error */ 
            JOptionPane.showMessageDialog(null,"Datos No Validos :\n" + msgError ,"Error",JOptionPane.ERROR_MESSAGE); 
        }
        
            
    }
    
    public GenericValidator getGc() {
        return gc;
    }

    public void setGc(GenericValidator gc) {
        this.gc = gc;
    }

    public GenericMethods getGm() {
        return gm;
    }

    public void setGm(GenericMethods gm) {
        this.gm = gm;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    
    
    
}
