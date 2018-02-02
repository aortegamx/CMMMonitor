/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.Control;

import com.fens.desktopMonitor.bo.AccionComprobanteBO;
import com.fens.desktopMonitor.dto.Accioncomprobante;
import com.fens.desktopMonitor.jdbc.AccioncomprobanteDaoImpl;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.util.GenericMethods;
import com.fens.desktopMonitor.util.GenericValidator;
import com.fens.desktopMonitor.views.Principal;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ISCesar poseidon24@hotmail.com
 * @since 18/06/2014 09:48:25 AM
 */
public class configuracionAcciones_action {

    private final GenericValidator gc = new GenericValidator(); 
    private final GenericMethods gm = new GenericMethods();
    private Connection conn = null;
    
    //Vista
    protected Principal vPrincipal;

    public configuracionAcciones_action(Principal vPrincipal) {
        this.conn = Conexion.getConn();
        this.vPrincipal = vPrincipal;
    }
    
    public DefaultTableModel fillAcciones() throws Exception {             
        DefaultTableModel modelTable=new DefaultTableModel();
        
        AccionComprobanteBO accionComprobanteBO = new AccionComprobanteBO(conn);
        Accioncomprobante[] accionesComprobante = accionComprobanteBO.findAccioncomprobantes(-1, 0, 0, "");
        
        modelTable.setColumnCount(0); 
        modelTable.addColumn("ID");  /* agregando Columnas a la Jtable */
        modelTable.addColumn("Clave");
        modelTable.addColumn("C.P.");
        modelTable.addColumn("Copiar"); 
        modelTable.addColumn("Ruta Copia"); 
        modelTable.addColumn("Correo");
        modelTable.addColumn("Destinatarios");
        modelTable.addColumn("Imprimir");
        modelTable.addColumn("Impresora");
        modelTable.addColumn("Int. Adobe R.");
        modelTable.addColumn("Plantilla");
        modelTable.addColumn("Logo");
        
        modelTable.setNumRows(accionesComprobante.length); 
        
        int i=0;
        for(Accioncomprobante item : accionesComprobante){    
            boolean activarCopia = (item.getActivarCopiaArchivo()==1);
            boolean activarCorreo = (item.getActivarCorreo()==1);
            boolean activarImpresion = (item.getActivarImpresion()==1);
            boolean activarSoporteAdobe = (item.getActivarSoporteAdobe()==1);
            
            modelTable.setValueAt(item.getIdAccionComprobante(), i, 0);
            modelTable.setValueAt(item.getClaveAccion(), i, 1); 
            modelTable.setValueAt(item.getCodigoPostalAuto(), i, 2); 
            modelTable.setValueAt(activarCopia?"Si":"", i, 3); 
            modelTable.setValueAt(item.getRutaCopiaArchivo(), i, 4); 
            modelTable.setValueAt(activarCorreo?"Si":"", i, 5); 
            modelTable.setValueAt(item.getCorreoDestinatarios(), i, 6); 
            modelTable.setValueAt(activarImpresion?"Si":"", i,7); 
            modelTable.setValueAt(item.getNombreImpresora(), i,8); 
            modelTable.setValueAt(activarSoporteAdobe?"Si":"", i,9); 
            modelTable.setValueAt(item.getPlantillaFactura(), i,10); 
            modelTable.setValueAt(item.getRutaLogo(), i,11); 
            i++;
        } 
        
        return modelTable;
    }
    
    public void cargaAccioncomprobante(int idAccioncomprobante) throws Exception {
        
        AccionComprobanteBO accionComprobanteBO = new AccionComprobanteBO(conn, idAccioncomprobante);
        Accioncomprobante accionComprobanteDto = accionComprobanteBO.getAccioncomprobante();
        
        if (accionComprobanteDto!=null){
            /* Ponemos datos en jpanel*/
            vPrincipal.txtIdAccion.setText(""+accionComprobanteDto.getIdAccionComprobante());
            vPrincipal.txtAccionClave.setText(accionComprobanteDto.getClaveAccion());
            vPrincipal.txtAccionCodigoPostal.setText(accionComprobanteDto.getCodigoPostalAuto());
            
            vPrincipal.chkAccionCopiaArchivos.setSelected(accionComprobanteDto.getActivarCopiaArchivo()==1);
            vPrincipal.txtAccionRutaCopia.setText(accionComprobanteDto.getRutaCopiaArchivo());
            
            vPrincipal.chkAccionActivarCorreo.setSelected(accionComprobanteDto.getActivarCorreo()==1);
            vPrincipal.txtAccionDestinatarios.setText(accionComprobanteDto.getCorreoDestinatarios());
            
            vPrincipal.chkAccionActivarImpresion.setSelected(accionComprobanteDto.getActivarImpresion()==1);
            vPrincipal.cmbAccionImpresora.setSelectedItem(accionComprobanteDto.getNombreImpresora());
            
            vPrincipal.txtAccionPlantillaFactura.setText(accionComprobanteDto.getPlantillaFactura());
            vPrincipal.txtAccionRutaLogo.setText(accionComprobanteDto.getRutaLogo());
            
            vPrincipal.chkAccionActivarSoporteAdobe.setSelected(accionComprobanteDto.getActivarSoporteAdobe()==1);
            
        }else{
            JOptionPane.showMessageDialog(null, "El Emisor seleccionado no existe.");
        }
    }
    
    public void eliminarAccioncomprobante(int idAccioncomprobante) throws Exception { 
       
        if(idAccioncomprobante >0 ){
             AccionComprobanteBO accionComprobanteBO = new AccionComprobanteBO(conn, idAccioncomprobante);
             AccioncomprobanteDaoImpl accionComprobanteDao = new AccioncomprobanteDaoImpl(conn);

             if(accionComprobanteBO.getAccioncomprobante()!=null){

                 Accioncomprobante accionComprobanteDto = accionComprobanteBO.getAccioncomprobante();
                 //accionComprobanteDto.setIdEstatusAccioncomprobante(0);     /* Estatus -1 Eliminado*/        
                 //accionComprobanteDao.update(accionComprobanteDto.createPk(), accionComprobanteDto);
                 accionComprobanteDao.delete(accionComprobanteDto.createPk());

                 JOptionPane.showMessageDialog(null,"Registro eliminado satisfactoriamente.","Información",JOptionPane.INFORMATION_MESSAGE);
                 vPrincipal.jTableAcciones.setModel(fillAcciones()); /* Recargamos Grid*/
                 vPrincipal.limpiaFormulario(vPrincipal.jPanelConfigAcciones, true);

             }else{
                 JOptionPane.showMessageDialog(null, "La configuración de Acción seleccionada no existe.");
             }
        }else{
             JOptionPane.showMessageDialog(null, "No ha seleccionado una Configuración de Acción a eliminar.");
        }
             
        
    }
    
    public void guardaAccion() throws SQLException{
        
                       
        /* Validación Datos */
        String msgError = "";
        
        int idAccion = -1;
        String claveAccion;
        String codigoPostal;
        String rutaCopia;
        String correoDestinatarios;
        String nombreImpresora = "";
        boolean activarCopia;
        boolean activarCorreo;
        boolean activarImpresion;
        String plantillaFactura = "";
        String rutaLogo = "";
        boolean activarSoporteAdobe;
        
                   
        try{
            idAccion = Integer.parseInt(vPrincipal.txtIdAccion.getText());
        }catch(Exception ex){}
        claveAccion =  vPrincipal.txtAccionClave.getText();
        codigoPostal = vPrincipal.txtAccionCodigoPostal.getText();
        rutaCopia = vPrincipal.txtAccionRutaCopia.getText();
        correoDestinatarios = vPrincipal.txtAccionDestinatarios.getText();
        if (vPrincipal.cmbAccionImpresora.getSelectedIndex()>0)
            nombreImpresora = vPrincipal.cmbAccionImpresora.getSelectedItem().toString();
        activarCopia = vPrincipal.chkAccionCopiaArchivos.isSelected();
        activarCorreo = vPrincipal.chkAccionActivarCorreo.isSelected();
        activarImpresion = vPrincipal.chkAccionActivarImpresion.isSelected();
        plantillaFactura = vPrincipal.txtAccionPlantillaFactura.getText();
        rutaLogo = vPrincipal.txtAccionRutaLogo.getText();
        activarSoporteAdobe = vPrincipal.chkAccionActivarSoporteAdobe.isSelected();
        
        if(!gc.isValidString(claveAccion, 1, 100))
            msgError += "\"Clave de la Acción\"\n"; 
        if(!gc.isValidString(codigoPostal, 0, 10))
            msgError += "\"Código Postal\"\n";
        if (activarCopia){
            if(!gc.isValidString(rutaCopia, 1, 500))
                msgError += "\"Ruta Copia Archivo\"\n";
            if (!gc.isValidDirectory(new File(rutaCopia)))
                msgError += "\"Ruta Copia Archivo, no es una carpeta accesible o existente.\"\n";
        }
        if (activarCorreo){
            if(!gc.isValidString(correoDestinatarios, 1, 300))
                msgError += "\"Destinatarios\"\n";
        }
        if (activarImpresion){
            if(!gc.isValidString(nombreImpresora, 1, 300))
                msgError += "\"Impresora\"\n";
        }
        if (plantillaFactura.length()>0){
            if (!gc.isValidString(plantillaFactura, 1, 300))
                msgError += "\"Plantilla Factura, longitud inválida.\"\n";
            if (!gc.isValidFile(new File(plantillaFactura)))
                msgError += "\"Plantilla Factura, no existe el archivo.\"\n";
            if (!gc.isExtensionValida(plantillaFactura, "jasper"))
                msgError += "\"Plantilla Factura, archivo inválido.\"\n";
        }
        if (rutaLogo.length()>0){
            if (!gc.isValidString(rutaLogo, 1, 300))
                msgError += "\"Logo, longitud inválida\"\n";
            if (!gc.isValidFile(new File(rutaLogo)))
                msgError += "\"Logo, no existe el archivo.\"\n";
            if (!gc.isExtensionValida(rutaLogo, "jpg"))
                msgError += "\"Logo, archivo inválido.\"\n";
        }
        
        if(msgError.equals("")){           
                        
            /* Guarda Datos */
            try{
                                
                AccioncomprobanteDaoImpl accionComprobanteDaoImpl = new AccioncomprobanteDaoImpl(this.conn); 
                Accioncomprobante accionComprobanteDto = accionComprobanteDaoImpl.findByPrimaryKey(idAccion);
                
                if(accionComprobanteDto==null){ /* Si no existe la accionComprobante insertamos  ----------------------------------*/
                    
                    accionComprobanteDto = new Accioncomprobante();     
                    /* Seteamos  obj*/
                    accionComprobanteDto.setClaveAccion(claveAccion);
                    accionComprobanteDto.setCodigoPostalAuto(codigoPostal);
                    accionComprobanteDto.setActivarCopiaArchivo(activarCopia?1:0);
                    accionComprobanteDto.setActivarCorreo(activarCorreo?1:0);
                    accionComprobanteDto.setActivarImpresion(activarImpresion?1:0);
                    accionComprobanteDto.setRutaCopiaArchivo(rutaCopia);
                    accionComprobanteDto.setCorreoDestinatarios(correoDestinatarios);
                    accionComprobanteDto.setNombreImpresora(nombreImpresora);
                    accionComprobanteDto.setPlantillaFactura(plantillaFactura);
                    accionComprobanteDto.setRutaLogo(rutaLogo);
                    accionComprobanteDto.setActivarSoporteAdobe(activarSoporteAdobe?1:0);

                    accionComprobanteDaoImpl.insert(accionComprobanteDto);
                    
                }else{ /* Si existe la accionComprobante actualizamos    --------------------------------------*/  
                    
                    accionComprobanteDto.setClaveAccion(claveAccion);
                    accionComprobanteDto.setCodigoPostalAuto(codigoPostal);
                    accionComprobanteDto.setActivarCopiaArchivo(activarCopia?1:0);
                    accionComprobanteDto.setActivarCorreo(activarCorreo?1:0);
                    accionComprobanteDto.setActivarImpresion(activarImpresion?1:0);
                    accionComprobanteDto.setRutaCopiaArchivo(rutaCopia);
                    accionComprobanteDto.setCorreoDestinatarios(correoDestinatarios);
                    accionComprobanteDto.setNombreImpresora(nombreImpresora);
                    accionComprobanteDto.setPlantillaFactura(plantillaFactura);
                    accionComprobanteDto.setRutaLogo(rutaLogo);
                    accionComprobanteDto.setActivarSoporteAdobe(activarSoporteAdobe?1:0);
                    
                    accionComprobanteDaoImpl.update(accionComprobanteDto.createPk(), accionComprobanteDto);
                       
                }
                
                JOptionPane.showMessageDialog(null,"Datos Guardados Satisfactoriamente.","Información",JOptionPane.INFORMATION_MESSAGE); 
                vPrincipal.jTableAcciones.setModel(fillAcciones()); /* Recargamos Grid*/
                vPrincipal.limpiaFormulario(vPrincipal.jPanelAccionesForm, true);
                
                
            }catch(Exception e){               
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Ocurrio un error al guardar el registro:\n " + e.toString(),"Error",JOptionPane.ERROR_MESSAGE);                
            }            
                    
        }else{
            
            /* Mensaje Error */ 
            JOptionPane.showMessageDialog(null,"Datos No Validos :\n" + msgError ,"Error",JOptionPane.ERROR_MESSAGE); 
        }
        
            
    }
    
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection aConn) {
        conn = aConn;
    }
    
}
