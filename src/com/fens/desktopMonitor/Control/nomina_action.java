/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fens.desktopMonitor.Control;

import com.fens.desktopMonitor.bo.ComprobanteFiscalBO;
import com.fens.desktopMonitor.bo.EmisorBO;
import com.fens.desktopMonitor.dto.Comprobantefiscal;
import com.fens.desktopMonitor.dto.Emisor;
import com.fens.desktopMonitor.dto.Nomina;
import com.fens.desktopMonitor.exceptions.ArchivomaestroDaoException;
import com.fens.desktopMonitor.exceptions.NominaDaoException;
import com.fens.desktopMonitor.export.ReportExportableXLS;
import com.fens.desktopMonitor.jdbc.ComprobantefiscalDaoImpl;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.jdbc.NominaDaoImpl;
import com.fens.desktopMonitor.views.Principal;
import java.awt.Color;
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
 * @author Hector
 */
public class nomina_action {
    
    
    //Instancia Vista
    Principal wPrincipal;
    
    private static Connection conn = Conexion.getConn();
    

    public nomina_action(Principal wPrincipal) {
        this.wPrincipal = wPrincipal;
    }

    public nomina_action() {
    }
    
    
    
    /**
     * Metodo cargar dats en tabla Archivo Maestro
     * @return Dato de tipo DefaultTableModel para cargar datos en tabla
     * @throws ArchivomaestroDaoException 
     */
    public TableModel fillNomina() throws NominaDaoException, Exception{
                
                 
        NominaDaoImpl nominaDaoImpl = new NominaDaoImpl(this.conn);   
        Nomina[] Nomina = nominaDaoImpl.findByDynamicWhere("IDNOMINA >0 AND IDNOMINA<40", null);//findAll(); 
        
        
        DefaultTableModel Model=new DefaultTableModel(); 
        
        Model.setColumnCount(0); 
        Model.addColumn("EMISOR");  /* agregando Columnas a la Jtable */
        Model.addColumn("UUID");
        Model.addColumn("NO. EMPLEADO");
        Model.addColumn("FECHA PAGO");
        Model.addColumn("FECHA INICIO PAGO"); 
        Model.addColumn("FECHA FINAL PAGO"); 
        Model.addColumn("DEPARTAMENTO"); 
        Model.addColumn("EMPLEADO"); 
        Model.addColumn("PUESTO"); 
        Model.addColumn("DIAS PAGADOS"); 
        Model.addColumn("PERCEPCIONES GRAVADAS");
        Model.addColumn("PERCEPCIONES EXENTAS");
        Model.addColumn("DEDUCCIONES GRAVADAS");
        Model.addColumn("DEDUCCIONES EXENTAS");
        Model.addColumn("INCAPACIDADES"); 
        Model.addColumn("HRS DOBLES"); 
        Model.addColumn("$ HRS DOBLES"); 
        Model.addColumn("HRS TRIPLES"); 
        Model.addColumn("$ HRS TRIPLES"); 
        
        
        
        Model.setNumRows(Nomina.length); 
        
        double totalDiasPagados = 0;
        double totalPercepcionesGravadas = 0;
        double totalPercepcionesExentas = 0;
        double totalDeduccionesGravadas = 0;
        double totalDeduccionesExentas = 0;
        double totalDescuentoIncapacidad = 0;
        double totalHorasDobles = 0;
        double totalHorasDoblesImporte = 0;
        double totalHorasTriples = 0;
        double totalHorasTriplesImporte = 0;
        
        
        int i=0;
        int x = 0;
        for(Nomina item:Nomina){        
            
            try{
                ComprobanteFiscalBO comprobanteBO = new ComprobanteFiscalBO(conn);
                Comprobantefiscal comprobanteDto = comprobanteBO.findComprobantefiscalbyId(item.getIdcomprobantefiscal());

                EmisorBO emisorBO = new EmisorBO();
                Emisor emisorDto = emisorBO.findEmisorbyId(conn,comprobanteDto.getIdemisor());

                Model.setValueAt(emisorDto.getRfc(), i, 0);
                Model.setValueAt(comprobanteDto.getUuid(), i, 1);
                Model.setValueAt(item.getNumEmpleado(), i, 2);
                Model.setValueAt(item.getFechaPago(), i, 3); 
                Model.setValueAt(item.getFechaInicialPago(), i, 4); 
                Model.setValueAt(item.getFechaFinPago(), i, 5); 
                Model.setValueAt(item.getDepartamento(), i,6); 
                Model.setValueAt(item.getNombreEmpleado(), i, 7); 
                Model.setValueAt(item.getPuesto(), i, 8); 
                Model.setValueAt(item.getNumDiasPagados(), i, 9); 
                Model.setValueAt(item.getTotalPercepcionGravadas(), i, 10); 
                Model.setValueAt(item.getTotalPercepcionExentas(), i, 11);
                Model.setValueAt(item.getTotalDeduccionGravadas(), i, 12); 
                Model.setValueAt(item.getTotalDeduccionExentas(), i, 13); 
                Model.setValueAt(item.getTotalIncapacidadDescuento(), i, 14); 
                Model.setValueAt(item.getTotalHrextraDobleHr(), i, 15); 
                Model.setValueAt(item.getTotalHrextraDobleImp(), i, 16); 
                Model.setValueAt(item.getTotalHrextraTripleHr(), i, 17); 
                Model.setValueAt(item.getTotalHrextraTripleImp(), i, 18); 
                
                 /* Suma totales*/
                totalDiasPagados += item.getNumDiasPagados();
                totalPercepcionesGravadas += item.getTotalPercepcionGravadas();
                totalPercepcionesExentas += item.getTotalPercepcionExentas();
                totalDeduccionesGravadas += item.getTotalDeduccionGravadas();
                totalDeduccionesExentas += item.getTotalDeduccionExentas();
                totalDescuentoIncapacidad += item.getTotalIncapacidadDescuento();
                totalHorasDobles += item.getTotalHrextraDobleHr();
                totalHorasDoblesImporte += item.getTotalHrextraDobleImp();
                totalHorasTriples += item.getTotalHrextraTripleHr();
                totalHorasTriplesImporte += item.getTotalHrextraTripleImp();
                
                
              }catch(Exception e){
                  e.printStackTrace();
              }
            
            i++;
         
        } 
        
        /* Agrego fila totales*/
         Object nuevaFila[]= {"","","","","","","","","",totalDiasPagados,totalPercepcionesGravadas,
         totalPercepcionesExentas,totalDeduccionesGravadas,totalDeduccionesExentas,totalDescuentoIncapacidad,
         totalHorasDobles,totalHorasDoblesImporte,totalHorasTriples,totalHorasTriplesImporte};
        
         Model.addRow(nuevaFila);
               
       
             
        return Model;        
        
    }

    public static Connection getConn() {
        return conn;
    }

    public static void setConn(Connection conn) {
        nomina_action.conn = conn;
    }
    

    public TableModel fillNomina(String rfc , String fechaFiltrada , Date fIni, Date fFin , String comodin) throws NominaDaoException {
       
        String filtro ="";  
        NominaDaoImpl nominaDaoImpl = new NominaDaoImpl(this.conn);   
        Nomina[] Nomina = null;
        
                        
        
        String campoFecha = null;
        if(!fechaFiltrada.equals("Selecciona...")){            
                             
            if(fechaFiltrada.equals("Fecha Inicio Pago"))
                campoFecha = "fecha_inicial_pago";
            if(fechaFiltrada.equals("Fecha Fin Pago"))
                campoFecha = "fecha_fin_pago";
            if(fechaFiltrada.equals("Fecha de Pago"))
                campoFecha = "fecha_pago";
                               
                 
        
            if(fIni !=null && fFin != null){
               if(!filtro.equals(""))
                      filtro += " AND "  ;

               SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


               filtro += " " + campoFecha + " BETWEEN '"+format.format(fIni)+ " 00:00:00' AND '"+format.format(fFin)+" 23:59:59' "  ;
           } 
            
        }
        
        
        if(rfc !=null && !rfc.equals("")){
           if(!filtro.equals(""))
                  filtro += " AND "  ;
           
                              
               filtro += " IDCOMPROBANTEFISCAL IN (SELECT IDCOMPROBANTEFISCAL FROM COMPROBANTEFISCAL WHERE IDEMISOR IN( SELECT IDEMISOR FROM EMISOR WHERE RFC = '"+rfc+"' )) ";
                        
        } 
        
        
        
        if(comodin !=null && !comodin.equals("") ){
           if(!filtro.equals(""))
                  filtro += " AND "  ;
           
           try{
               ComprobanteFiscalBO comprobanteFiscalBO = new ComprobanteFiscalBO(conn);
               Comprobantefiscal comprobanteFiscalDto = comprobanteFiscalBO.findComprobantefiscalbyUuid(comodin);
               
               
               filtro += "  IDCOMPROBANTEFISCAL ="+ comprobanteFiscalDto.getIdcomprobantefiscal() +" ";
               
           }catch(Exception e){
               
               filtro += " (UPPER(DEPARTAMENTO) LIKE UPPER('%"+comodin+"%') OR UPPER(PUESTO) LIKE UPPER('%"+comodin+"%') OR UPPER(NUM_EMPLEADO) LIKE UPPER('"+comodin+"'))";         
           }
                   
         
               
           
       }
        
        
        
        /*query*/
       if(filtro.equals("")){
           System.out.println("-----------------A");
           Nomina = nominaDaoImpl.findAll();
       }else{
            System.out.println("-----------------B --- " + filtro);
            Nomina = nominaDaoImpl.findByDynamicWhere(filtro, null); 
       }
        
        
        DefaultTableModel Model=new DefaultTableModel(); 
        
        Model.setColumnCount(0); 
        Model.addColumn("EMISOR");  /* agregando Columnas a la Jtable */
        Model.addColumn("UUID");
        Model.addColumn("NO. EMPLEADO");
        Model.addColumn("FECHA PAGO");
        Model.addColumn("FECHA INICIO PAGO"); 
        Model.addColumn("FECHA FINAL PAGO"); 
        Model.addColumn("DEPARTAMENTO"); 
        Model.addColumn("EMPLEADO"); 
        Model.addColumn("PUESTO"); 
        Model.addColumn("DIAS PAGADOS"); 
        Model.addColumn("PERCEPCIONES GRAVADAS");
        Model.addColumn("PERCEPCIONES EXENTAS");
        Model.addColumn("DEDUCCIONES GRAVADAS");
        Model.addColumn("DEDUCCIONES EXENTAS");
        Model.addColumn("INCAPACIDADES"); 
        Model.addColumn("HRS DOBLES"); 
        Model.addColumn("$ HRS DOBLES"); 
        Model.addColumn("HRS TRIPLES"); 
        Model.addColumn("$ HRS TRIPLES"); 
        
        
        
        Model.setNumRows(Nomina.length); 
        
        double totalDiasPagados = 0;
        double totalPercepcionesGravadas = 0;
        double totalPercepcionesExentas = 0;
        double totalDeduccionesGravadas = 0;
        double totalDeduccionesExentas = 0;
        double totalDescuentoIncapacidad = 0;
        double totalHorasDobles = 0;
        double totalHorasDoblesImporte = 0;
        double totalHorasTriples = 0;
        double totalHorasTriplesImporte = 0;
        
        
        int i=0;
        for(Nomina item:Nomina){        
            
            try{
                ComprobanteFiscalBO comprobanteBO = new ComprobanteFiscalBO(conn);
                Comprobantefiscal comprobanteDto = comprobanteBO.findComprobantefiscalbyId(item.getIdcomprobantefiscal());

                EmisorBO emisorBO = new EmisorBO();
                Emisor emisorDto = emisorBO.findEmisorbyId(conn,comprobanteDto.getIdemisor());

                Model.setValueAt(emisorDto.getRfc(), i, 0);
                Model.setValueAt(comprobanteDto.getUuid(), i, 1);
                Model.setValueAt(item.getNumEmpleado(), i, 2);
                Model.setValueAt(item.getFechaPago(), i, 3); 
                Model.setValueAt(item.getFechaInicialPago(), i, 4); 
                Model.setValueAt(item.getFechaFinPago(), i, 5); 
                Model.setValueAt(item.getDepartamento(), i,6); 
                Model.setValueAt(item.getNombreEmpleado(), i, 7); 
                Model.setValueAt(item.getPuesto(), i, 8); 
                Model.setValueAt(item.getNumDiasPagados(), i, 9); 
                Model.setValueAt(item.getTotalPercepcionGravadas(), i, 10); 
                Model.setValueAt(item.getTotalPercepcionExentas(), i, 11);
                Model.setValueAt(item.getTotalDeduccionGravadas(), i, 12); 
                Model.setValueAt(item.getTotalDeduccionExentas(), i, 13); 
                Model.setValueAt(item.getTotalIncapacidadDescuento(), i, 14); 
                Model.setValueAt(item.getTotalHrextraDobleHr(), i, 15); 
                Model.setValueAt(item.getTotalHrextraDobleImp(), i, 16); 
                Model.setValueAt(item.getTotalHrextraTripleHr(), i, 17); 
                Model.setValueAt(item.getTotalHrextraTripleImp(), i, 18); 
                
                /* Suma totales*/
                totalDiasPagados += item.getNumDiasPagados();
                totalPercepcionesGravadas += item.getTotalPercepcionGravadas();
                totalPercepcionesExentas += item.getTotalPercepcionExentas();
                totalDeduccionesGravadas += item.getTotalDeduccionGravadas();
                totalDeduccionesExentas += item.getTotalDeduccionExentas();
                totalDescuentoIncapacidad += item.getTotalIncapacidadDescuento();
                totalHorasDobles += item.getTotalHrextraDobleHr();
                totalHorasDoblesImporte += item.getTotalHrextraDobleImp();
                totalHorasTriples += item.getTotalHrextraTripleHr();
                totalHorasTriplesImporte += item.getTotalHrextraTripleImp();
                
              }catch(Exception e){
                  e.printStackTrace();
              }
            
            i++;
        } 
        
        /* Agrego fila totales*/
         Object nuevaFila[]= {"","","","","","","","","",totalDiasPagados,totalPercepcionesGravadas,
         totalPercepcionesExentas,totalDeduccionesGravadas,totalDeduccionesExentas,totalDescuentoIncapacidad,
         totalHorasDobles,totalHorasDoblesImporte,totalHorasTriples,totalHorasTriplesImporte};
        
         Model.addRow(nuevaFila);
       
        
            
        
        
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
                        
            tb.add(wPrincipal.tblNomina);
            nom.add("ArchivosProcesados");
            
            int num_cols = wPrincipal.tblNomina.getColumnCount();
            /* Lleno lista con ecabezados tabla*/
            for(int i =0 ; i < num_cols ; i++){                
                head_table.add(wPrincipal.tblNomina.getColumnName(i));
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
