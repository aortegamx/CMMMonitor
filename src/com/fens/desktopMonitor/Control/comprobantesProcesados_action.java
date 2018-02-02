/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.Control;

import com.fens.desktopMonitor.bo.ComprobanteFiscalBO;
import com.fens.desktopMonitor.bo.EmisorBO;
import com.fens.desktopMonitor.bo.EstatusBO;
import com.fens.desktopMonitor.bo.TipoComprobanteBO;
import com.fens.desktopMonitor.dto.Comprobantefiscal;
import com.fens.desktopMonitor.dto.Emisor;
import com.fens.desktopMonitor.dto.Estatus;
import com.fens.desktopMonitor.dto.Tipocomprobante;
import com.fens.desktopMonitor.exceptions.ArchivomaestroDaoException;
import com.fens.desktopMonitor.exceptions.ComprobantefiscalDaoException;
import com.fens.desktopMonitor.exceptions.EmisorDaoException;
import com.fens.desktopMonitor.exceptions.EstatusDaoException;
import com.fens.desktopMonitor.exceptions.TipocomprobanteDaoException;
import com.fens.desktopMonitor.export.ReportExportableXLS;
import com.fens.desktopMonitor.jdbc.ComprobantefiscalDaoImpl;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.jdbc.EmisorDaoImpl;
import com.fens.desktopMonitor.jdbc.EstatusDaoImpl;
import com.fens.desktopMonitor.jdbc.TipocomprobanteDaoImpl;
import com.fens.desktopMonitor.util.FormatUtil;
import com.fens.desktopMonitor.views.Principal;
import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author 578
 */
public class comprobantesProcesados_action {
    
    Connection conn = Conexion.getConn();
    //Instancia Vista
    Principal wPrincipal;
    
    public comprobantesProcesados_action(Principal vista) {       
       this.wPrincipal = vista;
    }

    public comprobantesProcesados_action() {
        
    }
    
    
    /**
     * Metodo cargar dats en tabla Archivo Maestro
     * @return Dato de tipo DefaultTableModel para cargar datos en tabla
     * @throws ArchivomaestroDaoException 
     */
    public TableModel fillArchivosProcesados() throws ComprobantefiscalDaoException, EstatusDaoException, TipocomprobanteDaoException, EmisorDaoException{
                
                 
        ComprobantefiscalDaoImpl comprobanteFiscaDaoImpl = new ComprobantefiscalDaoImpl(this.conn);   
        Comprobantefiscal[] comprobantesFiscales = comprobanteFiscaDaoImpl.findByDynamicWhere("IDCOMPROBANTEFISCAL >0 AND IDCOMPROBANTEFISCAL<40", null);//findAll(); 
        
        
        DefaultTableModel Model=new DefaultTableModel(); 
        
        Model.setColumnCount(0); /* agregando Columnas a la Jtable */
        Model.addColumn("ID COMPROBANTE");
        Model.addColumn("EMISOR");
        Model.addColumn("RECEPTOR");  
            Model.addColumn("NOMBRE_RECEPTOR");
        Model.addColumn("TIPO COMPROBANTE");
        Model.addColumn("SERIE"); 
        Model.addColumn("FOLIO"); 
        Model.addColumn("UUID"); 
        Model.addColumn("FECHA SELLADO"); 
        Model.addColumn("FECHA TIMBRADO"); 
        Model.addColumn("FECHA PROCESO"); 
        Model.addColumn("ESTATUS");         
            Model.addColumn("TOTAL");      
            Model.addColumn("EMAIL");
            Model.addColumn("REFERENCIA1");
            Model.addColumn("REFERENCIA2");
            Model.addColumn("REFERENCIA3");
        
        Model.setNumRows(comprobantesFiscales.length); 
        
        
        int i=0;
        for(Comprobantefiscal item:comprobantesFiscales){               
            
            /*Obtenemos estatus */
            EstatusDaoImpl estatusDaoImpl = new EstatusDaoImpl(this.conn);
            Estatus estatusA = estatusDaoImpl.findByPrimaryKey(item.getIdestatus());
            
            /*Obtenemos tipocomprobante */
            
            TipocomprobanteDaoImpl tipoComprobanteDaoImpl = new TipocomprobanteDaoImpl(this.conn);
            Tipocomprobante comprobante = tipoComprobanteDaoImpl.findByPrimaryKey(item.getIdtipocomprobante());
            
             /*Obtenemos emisor */
            
            EmisorDaoImpl emisorDaoImpl = new EmisorDaoImpl(this.conn);
            Emisor emisor = emisorDaoImpl.findByPrimaryKey(item.getIdemisor());
            
                        
            Model.setValueAt(item.getIdcomprobantefiscal(), i, 0);
            Model.setValueAt(emisor.getRfc() , i, 1); 
            Model.setValueAt(item.getRfcreceptor(), i, 2); 
                Model.setValueAt(item.getNombreReceptor() !=null?item.getNombreReceptor():"", i, 3);
            Model.setValueAt(comprobante.getNombretipocomprobante(), i, 4); 
            Model.setValueAt(item.getSerie(), i,5); 
            Model.setValueAt(item.getFolio(), i, 6); 
            Model.setValueAt(item.getUuid(), i, 7); 
            Model.setValueAt(item.getFechahorasellado(), i, 8); 
            Model.setValueAt(item.getFechahoratimbrado(), i, 9); 
            Model.setValueAt(item.getFechahoraproceso(), i, 10); 
            Model.setValueAt(estatusA.getNombreestatus(), i, 11); 
            //Model.setValueAt(item.getTotal(), i, 11); 
                Model.setValueAt(FormatUtil.doubleToString(item.getTotal()), i, 12);
                Model.setValueAt(item.getEmail()!=null?item.getEmail():"", i, 13); 
                Model.setValueAt(item.getReferencia1()!=null?item.getReferencia1():"", i, 14); 
                Model.setValueAt(item.getReferencia2()!=null?item.getReferencia2():"", i, 15); 
                Model.setValueAt(item.getReferencia3()!=null?item.getReferencia3():"", i, 16); 
            i++;
        } 
       
        
        
        return Model;        
        
    }

    public TableModel fillComprobantesProcesados(String tipoComp,String estatus, Date fIni, Date fFin , String rfc , String comodin) throws ComprobantefiscalDaoException, EstatusDaoException, TipocomprobanteDaoException, EmisorDaoException {
        
        String filtro ="";         
        ComprobantefiscalDaoImpl comprobanteFiscaDaoImpl = new ComprobantefiscalDaoImpl(this.conn);   
        Comprobantefiscal[] comprobantesFiscales = null; 
        /* Filtros*/
        
        if(!tipoComp.equals("Selecciona...")){
            String nombreComprobante = estatus;
            TipoComprobanteBO tipoComprobanteBO = new TipoComprobanteBO(null);
            int idTipoComp = -1;
            try{
                idTipoComp = tipoComprobanteBO.findTipoComprobantebyNombre(tipoComp).getIdtipocomprobante();
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,e.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            }
            
            filtro += " IDTIPOCOMPROBANTE =" + idTipoComp ;
                   
       }
        
        if(!estatus.equals("Selecciona...")){
            String nombreEstatus = estatus;
            EstatusBO estatusBO = new EstatusBO();
            int idEstatus = -1;
            try{
                idEstatus = estatusBO.findEstatusbyNombre(nombreEstatus,2).getIdestatus();
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,e.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            }
            
            if(!filtro.equals(""))
                  filtro += " AND "  ;
            
            
            filtro += " IDESTATUS =" + idEstatus ;
                   
       }        
        
       if(fIni !=null && fFin != null){
           if(!filtro.equals(""))
                  filtro += " AND "  ;
           
           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
           
       
           filtro += " FECHAHORAPROCESO BETWEEN '"+format.format(fIni)+ " 00:00:00' AND '"+format.format(fFin)+" 23:59:59' "  ;
       } 
       
       
       if(rfc !=null && !rfc.equals("")){
           if(!filtro.equals(""))
                  filtro += " AND "  ;
           try{
               EmisorBO emisorBO = new EmisorBO();
               Emisor emisorDto = emisorBO.findEmisorbyRfc(conn,rfc);
               
               
               filtro += " (IDEMISOR ="+ emisorDto.getIdemisor() + " OR UPPER(RFCRECEPTOR) LIKE UPPER('"+ rfc +"')) ";
               
           }catch(Exception e){
               filtro += " RFCRECEPTOR LIKE UPPER('"+ rfc +"') ";
                        
           }
       } 
       
       
       if(comodin !=null){
           if(!filtro.equals(""))
                  filtro += " AND "  ;
                   
           filtro += " (UPPER(SERIE) LIKE UPPER('%"+comodin+"%') OR UPPER(FOLIO) LIKE UPPER('%"+comodin+"%') OR UPPER(UUID) LIKE UPPER('%"+comodin+"%')) ";
               
           
       }
       
       
        
        /*query*/
       if(filtro.equals("")){
           System.out.println("-----------------A");
           comprobantesFiscales = comprobanteFiscaDaoImpl.findAll();
       }else{
            System.out.println("-----------------B --- " + filtro);
            comprobantesFiscales = comprobanteFiscaDaoImpl.findByDynamicWhere(filtro, null); 
       }
        
        
        
        
        
        
        DefaultTableModel Model=new DefaultTableModel(); 
        
        Model.setColumnCount(0); /* agregando Columnas a la Jtable */
        Model.addColumn("ID COMPROBANTE");
        Model.addColumn("EMISOR");
        Model.addColumn("RECEPTOR");  
            Model.addColumn("NOMBRE_RECEPTOR");
        Model.addColumn("TIPO COMPROBANTE");
        Model.addColumn("SERIE"); 
        Model.addColumn("FOLIO"); 
        Model.addColumn("UUID"); 
        Model.addColumn("FECHA SELLADO"); 
        Model.addColumn("FECHA TIMBRADO"); 
        Model.addColumn("FECHA PROCESO"); 
        Model.addColumn("ESTATUS");      
            Model.addColumn("TOTAL");
            Model.addColumn("EMAIL");
            Model.addColumn("REFERENCIA1");
            Model.addColumn("REFERENCIA2");
            Model.addColumn("REFERENCIA3");
        
        
        Model.setNumRows(comprobantesFiscales.length); 
        
        
        int i=0;
        for(Comprobantefiscal item:comprobantesFiscales){               
            
            /*Obtenemos estatus */
            EstatusDaoImpl estatusDaoImpl = new EstatusDaoImpl(this.conn);
            Estatus estatusA = estatusDaoImpl.findByPrimaryKey(item.getIdestatus());
            
            /*Obtenemos tipocomprobante */
            
            TipocomprobanteDaoImpl tipoComprobanteDaoImpl = new TipocomprobanteDaoImpl(this.conn);
            Tipocomprobante comprobante = tipoComprobanteDaoImpl.findByPrimaryKey(item.getIdtipocomprobante());
            
             /*Obtenemos emisor */
            
            EmisorDaoImpl emisorDaoImpl = new EmisorDaoImpl(this.conn);
            Emisor emisor = emisorDaoImpl.findByPrimaryKey(item.getIdemisor());
            
                        
            Model.setValueAt(item.getIdcomprobantefiscal(), i, 0);
            Model.setValueAt(emisor.getRfc() , i, 1); 
            Model.setValueAt(item.getRfcreceptor(), i, 2); 
                Model.setValueAt(item.getNombreReceptor() !=null?item.getNombreReceptor():"", i, 3);
            Model.setValueAt(comprobante.getNombretipocomprobante(), i, 4); 
            Model.setValueAt(item.getSerie(), i,5); 
            Model.setValueAt(item.getFolio(), i, 6); 
            Model.setValueAt(item.getUuid(), i, 7); 
            Model.setValueAt(item.getFechahorasellado(), i, 8); 
            Model.setValueAt(item.getFechahoratimbrado(), i, 9); 
            Model.setValueAt(item.getFechahoraproceso(), i, 10); 
            Model.setValueAt(estatusA.getNombreestatus(), i, 11); 
                Model.setValueAt(FormatUtil.doubleToString(item.getTotal()), i, 12);
                Model.setValueAt(item.getEmail()!=null?item.getEmail():"", i, 13); 
                Model.setValueAt(item.getReferencia1()!=null?item.getReferencia1():"", i, 14); 
                Model.setValueAt(item.getReferencia2()!=null?item.getReferencia2():"", i, 15); 
                Model.setValueAt(item.getReferencia3()!=null?item.getReferencia3():"", i, 16);  
            i++;
        } 
       
        
        
        return Model;
        
    }
    
    
    public void exportaToExcel() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de excel","xls");
        chooser.setFileFilter(filter);
        chooser.setDialogTitle("Guardar Archivo");
        chooser.setMultiSelectionEnabled(false);
        chooser.setAcceptAllFileFilterUsed(false);
        if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
            List<JTable> tb = new ArrayList<>();
            List<String> nom = new ArrayList<>();
            List<String> head_table = new ArrayList<>();  
                        
            tb.add(wPrincipal.tblCompProcesados);
            nom.add("ComprobantesProcesados");
            
            int num_cols = wPrincipal.tblCompProcesados.getColumnCount();
            /* Lleno lista con ec}ncabezados tabla*/
            for(int i =0 ; i < num_cols ; i++){                
                head_table.add(wPrincipal.tblCompProcesados.getColumnName(i));
            }
            
            String file = chooser.getSelectedFile().toString().concat(".xls");
            try{
                ReportExportableXLS e = new ReportExportableXLS(new File(file),tb,nom,head_table);
                if(e.export()){
                    JOptionPane.showMessageDialog(null, "Datos Exportados Correctamente.");
                }
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al exportar datos.");
            }
        }
          
    }

    
    
    
}


