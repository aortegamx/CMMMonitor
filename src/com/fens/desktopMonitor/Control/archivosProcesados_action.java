/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.Control;

import com.fens.desktopMonitor.bo.EstatusBO;
import com.fens.desktopMonitor.dto.Archivomaestro;
import com.fens.desktopMonitor.dto.Estatus;
import com.fens.desktopMonitor.exceptions.ArchivomaestroDaoException;
import com.fens.desktopMonitor.exceptions.EstatusDaoException;
import com.fens.desktopMonitor.jdbc.ArchivomaestroDaoImpl;
import com.fens.desktopMonitor.jdbc.EstatusDaoImpl;
import com.fens.desktopMonitor.views.Principal;
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
import com.fens.desktopMonitor.export.ReportExportableXLS;
import com.fens.desktopMonitor.jdbc.Conexion;
import java.io.File;
import java.sql.Connection;

/**
 *
 * @author 578
 */
public class archivosProcesados_action {

    Connection conn = Conexion.getConn();
   //Instancia Vista
    Principal wPrincipal;
    
    public archivosProcesados_action(Principal vista) {       
       this.wPrincipal = vista;
    }

    public archivosProcesados_action() {
        
    }
    
    
    /**
     * Metodo cargar dats en tabla Archivo Maestro
     * @return Dato de tipo DefaultTableModel para cargar datos en tabla
     * @throws ArchivomaestroDaoException 
     */
    public TableModel fillArchivosProcesados() throws ArchivomaestroDaoException, EstatusDaoException {
                
                 
        ArchivomaestroDaoImpl archivoMaestroDaoImpl = new ArchivomaestroDaoImpl(this.conn);   
        Archivomaestro [] Archivosmaestro = archivoMaestroDaoImpl.findByDynamicWhere("IDARCHIVOMAESTRO >0 AND IDARCHIVOMAESTRO<40", null);//findAll(); 
        
        
        DefaultTableModel Model=new DefaultTableModel(); 
        
        Model.setColumnCount(0); 
        Model.addColumn("NOMBRE DEL ARCHIVO");  /* agregando Columnas a la Jtable */
        Model.addColumn("ESTATUS");
        Model.addColumn("FECHA CREACION"); 
        Model.addColumn("FECHA PROCESO"); 
        Model.addColumn("COMPROBANTES CONTENIDOS"); 
        Model.addColumn("TAMAÑO DE ARCHIVO"); 
        
        
        
        Model.setNumRows(Archivosmaestro.length); 
        
        int i=0;
        for(Archivomaestro item:Archivosmaestro){        
                            
            EstatusDaoImpl estatusDaoImpl = new EstatusDaoImpl(this.conn);
            Estatus estatusA = estatusDaoImpl.findByPrimaryKey(item.getIdestatus());            
            
            Model.setValueAt(item.getNombrearchivo(), i, 0);
            Model.setValueAt(estatusA.getNombreestatus(), i, 1); 
            Model.setValueAt(item.getFechacreacion(), i, 2); 
            Model.setValueAt(item.getFechaproceso(), i, 3); 
            Model.setValueAt(item.getNumerofacturas(), i,4); 
            Model.setValueAt(item.getTamanoarchivo(), i, 5); 
            i++;
        } 
       
        
        
        return Model;        
        
    }

    public TableModel fillArchivosProcesados(String estatus, Date fIni, Date fFin) throws ArchivomaestroDaoException, EstatusDaoException {
               
       String filtro ="";
       ArchivomaestroDaoImpl archivoMaestroDaoImpl = new ArchivomaestroDaoImpl(this.conn);
       Archivomaestro [] Archivosmaestro = null;
       
       
       if(!estatus.equals("Selecciona...")){
            String nombreEstatus = estatus;
            EstatusBO estatusBO = new EstatusBO();
            int idEstatus = -1;
            try{
                idEstatus = estatusBO.findEstatusbyNombre(nombreEstatus,1).getIdestatus();
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,e.toString(),"Error",JOptionPane.ERROR_MESSAGE);
            }
            
            filtro += " IDESTATUS =" + idEstatus ;
                         
              
       }
       
       
       if(fIni !=null && fFin != null){
           if(!filtro.equals(""))
                  filtro += " AND "  ;
           
           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
           
       
           filtro += " FECHAPROCESO BETWEEN '"+format.format(fIni)+ " 00:00:00' AND '"+format.format(fFin)+" 23:59:59' "  ;
       }
       
       /*query*/
       if(filtro.equals("")){
           System.out.println("-----------------A");
            Archivosmaestro = archivoMaestroDaoImpl.findAll();
       }else{
            System.out.println("-----------------B --- " + filtro);
            Archivosmaestro = archivoMaestroDaoImpl.findByDynamicWhere(filtro, null); 
       }
    
     
        
        DefaultTableModel Model=new DefaultTableModel(); 
        
        Model.setColumnCount(0); 
        Model.addColumn("NOMBRE DEL ARCHIVO");  /* agregando Columnas a la Jtable */
        Model.addColumn("ESTATUS");
        Model.addColumn("FECHA CREACION"); 
        Model.addColumn("FECHA PROCESO"); 
        Model.addColumn("COMPROBANTES CONTENIDOS"); 
        Model.addColumn("TAMAÑO DE ARCHIVO"); 
        
        
        
        Model.setNumRows(Archivosmaestro.length); 
        
        
        
        int i=0;
        for(Archivomaestro item:Archivosmaestro){        
                            
            EstatusDaoImpl estatusDaoImpl = new EstatusDaoImpl(this.conn);
            Estatus estatusA = estatusDaoImpl.findByPrimaryKey(item.getIdestatus());            
            
            Model.setValueAt(item.getNombrearchivo(), i, 0);
            Model.setValueAt(estatusA.getNombreestatus(), i, 1); 
            Model.setValueAt(item.getFechacreacion(), i, 2); 
            Model.setValueAt(item.getFechaproceso(), i, 3); 
            Model.setValueAt(item.getNumerofacturas(), i,4); 
            Model.setValueAt(item.getTamanoarchivo(), i, 5); 
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
                        
            tb.add(wPrincipal.tblArchivosProcesados);
            nom.add("ArchivosProcesados");
            
            int num_cols = wPrincipal.tblArchivosProcesados.getColumnCount();
            /* Lleno lista con ec}ncabezados tabla*/
            for(int i =0 ; i < num_cols ; i++){                
                head_table.add(wPrincipal.tblArchivosProcesados.getColumnName(i));
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
