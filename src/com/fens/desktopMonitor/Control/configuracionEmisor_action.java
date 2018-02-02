/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.Control;


import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.bo.EmisorBO;
import com.fens.desktopMonitor.bo.UbicacionFiscalBO;
import com.fens.desktopMonitor.dto.Emisor;
import com.fens.desktopMonitor.dto.Ubicacionfiscal;
import com.fens.desktopMonitor.exceptions.EmisorDaoException;
import com.fens.desktopMonitor.exceptions.UbicacionfiscalDaoException;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.jdbc.EmisorDaoImpl;
import com.fens.desktopMonitor.jdbc.UbicacionfiscalDaoImpl;
import com.fens.desktopMonitor.util.GenericMethods;
import com.fens.desktopMonitor.util.GenericValidator;
import com.fens.desktopMonitor.views.Principal;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 578
 */
public class configuracionEmisor_action {
    
    GenericValidator gc = new GenericValidator(); 
    GenericMethods gm = new GenericMethods();
    static Connection conn = Conexion.getConn();
    
    
    
    
    //Instancia Vista
    Principal wPrincipal;
    
    public configuracionEmisor_action(Principal vista) {       
       this.wPrincipal = vista;
    }

    public configuracionEmisor_action() {
        
    }
    
    
    
    
    public void guardaDatos() throws SQLException{
        
                       
        /* Validación Datos */
        String msgError = "";
        
                
        if(!gc.isRFC(wPrincipal.txtRfc.getText()))            
            msgError += "\"RFC\"\n";         
        if(!gc.isValidString(wPrincipal.txtRegimenFiscal.getText(), 1, 500))
            msgError += "\"Régimen Fiscal\"\n"; 
        if(!gc.isValidString(wPrincipal.txtCalle.getText(), 1, 100))
            msgError += "\"Calle\"\n";        
        if(!gc.isCodigoPostal(wPrincipal.txtCodigoPostal.getText()))
            msgError += "\"Código Postal\"\n"; 
        if(!gc.isValidString(wPrincipal.txtPais.getText(), 1, 100))
            msgError += "\"País\"\n"; 
        if(!gc.isValidString(wPrincipal.txtEstado.getText(), 1, 100))
            msgError += "\"Estado\"\n";
        if(!gc.isValidString(wPrincipal.txtMunicipio.getText(), 1, 100))
            msgError += "\"Municipio\"\n"; 
        if(!gc.isExtensionValida(wPrincipal.txtLogo.getText(),"jpg"))
            msgError += "\"Logo (.jpg)\"\n"; 
        if(!gc.isExtensionValida(wPrincipal.txtPlantilla.getText(),"jasper"))
            msgError += "\"Plantilla Factura (.jasper)\"\n"; 
        if (StringManage.getValidString(wPrincipal.txtPlantillaNomina.getText()).length()>0){
            if(!gc.isExtensionValida(wPrincipal.txtPlantillaNomina.getText(),"jasper"))
                msgError += "\"Plantilla Nomina (.jasper)\"\n"; 
        }
        if(!gc.isValidString(wPrincipal.txtCer.getText(), 1, 500) || !gc.isExtensionValida(wPrincipal.txtCer.getText(),"cer"))
            msgError += "\"Ruta Certificado (.cer)\"\n";
        if(!gc.isValidString(wPrincipal.txtKey.getText(), 1, 500) || !gc.isExtensionValida(wPrincipal.txtKey.getText(),"key"))
            msgError += "\"Ruta Llave Privada (.key)\"\n";
        if(!gc.isValidString(String.valueOf(wPrincipal.txtContrasena.getPassword()), 1, 50))
            msgError += "\"Contraseña\"\n";
        if (StringManage.getValidString(wPrincipal.txtPlantillaRetenciones.getText()).length()>0){
            if(!gc.isExtensionValida(wPrincipal.txtPlantillaRetenciones.getText(),"jasper"))
                msgError += "\"Plantilla Retenciones (.jasper)\"\n"; 
        }
        
        int chk = 0;
        if(wPrincipal.chkEstatusEmisor.isSelected()){
                chk =  1; /* activo */
            }else{
                chk = 0;  /* inactivo */        
            }      
       int chkS = 0;
       if(wPrincipal.chkSectorPrimario.isSelected()){
           chkS=1;
       }else{
           chkS=0;
       }
        
                
        
        if(msgError.equals("")){
            
            /*Recupera Datos */
        
            String rfc = wPrincipal.txtRfc.getText().trim();
            String razonSocial = wPrincipal.txtRazonSocial.getText().trim();
            String nombreComercial = wPrincipal.txtNombreComercial.getText().trim();
            String regimenFiscal = wPrincipal.txtRegimenFiscal.getText().trim();
            String calle = wPrincipal.txtCalle.getText().trim();
            String numExt = wPrincipal.txtNumExt.getText().trim();
            String numInt = wPrincipal.txtNumInt.getText().trim();
            String colonia = wPrincipal.txtColonia.getText().trim();
            String codigoPostal = wPrincipal.txtCodigoPostal.getText().trim();
            String pais = wPrincipal.txtPais.getText().trim();
            String estado = wPrincipal.txtEstado.getText().trim();
            String municipio = wPrincipal.txtMunicipio.getText().trim();
            String rutaLogo = wPrincipal.txtLogo.getText().trim();
            String rutaPlantilla = wPrincipal.txtPlantilla.getText().trim();
            String rutaPlantillaNomina = wPrincipal.txtPlantillaNomina.getText().trim();
            String rutaCer = wPrincipal.txtCer.getText().trim();
            String rutaKey = wPrincipal.txtKey.getText().trim();
            String passwordEmisor = String.valueOf(wPrincipal.txtContrasena.getPassword()).trim();
            int estatusEmisor = chk;
            String rutaPlantillaRetenciones = wPrincipal.txtPlantillaRetenciones.getText().trim();
                    
            /* Instanciamos objeto*/
            
            Emisor emisorDto = new Emisor();     
           
            
            /* Seteamos  obj*/
            
            emisorDto.setRfc(rfc);
            emisorDto.setRazonsocial(razonSocial);
            emisorDto.setNombrecomercial(nombreComercial);
            emisorDto.setRegimenfiscal(regimenFiscal);
            emisorDto.setEstatus(estatusEmisor);
            emisorDto.setPlantillacomprobante(rutaPlantilla);
            emisorDto.setPlantillanomina(rutaPlantillaNomina);
            emisorDto.setRutalogo(rutaLogo); 
            emisorDto.setRutacer(rutaCer);
            emisorDto.setRutakey(rutaKey);
            emisorDto.setEmisorpass(passwordEmisor);
            emisorDto.setEstatus(estatusEmisor);
            emisorDto.setPlantillaRetenciones(rutaPlantillaRetenciones);
            emisorDto.setSectorPrimario(chkS);
            
                        
            /* Guarda Datos */
            try{
                
                conn.setAutoCommit (false); /* -> conn.begin() -> Inicia transaccion -- Desactivar modo autocommit */
                                
                EmisorDaoImpl emisorDaoImpl = new EmisorDaoImpl(this.conn); 
                
                Emisor[] emisoresTmp = emisorDaoImpl.findWhereRfcEquals(emisorDto.getRfc());/* Buscamos emisores por rfc*/
                
                if(emisoresTmp.length == 0){ /* Si no existe el emisor insertamos  ----------------------------------*/
                    
                    /* Insertamos en emisor*/
                    emisorDaoImpl.insert(emisorDto);
                    Emisor[] emisoresTmp2 = emisorDaoImpl.findWhereRfcEquals(emisorDto.getRfc());
                    /* Ultimo emisor insertado para ubicacion */                
                    //Emisor emisorDtoUbicacion = emisorDaoImpl.findLast();//necesito ultimo reg insertado


                    /* Creamos obj ubicacicon*/
                    UbicacionFiscalBO ubicacionFiscalBO = new UbicacionFiscalBO(conn); 
                    Ubicacionfiscal ubicacionFiscalDto = new Ubicacionfiscal();
                    UbicacionfiscalDaoImpl ubicacionFiscalDaoImpl = new UbicacionfiscalDaoImpl(this.conn);

                    /* Seteamos  obj*/
                    ubicacionFiscalDto.setIdubicacionfiscal(ubicacionFiscalDaoImpl.findByDynamicWhere("IDUBICACIONFISCAL>0 ORDER BY IDUBICACIONFISCAL DESC", null)[0].getIdubicacionfiscal()+1);
                    ubicacionFiscalDto.setIdemisor(emisoresTmp2[0].getIdemisor()); //emisorDtoUbicacion.getIdemisor());
                    ubicacionFiscalDto.setCalle(calle);
                    ubicacionFiscalDto.setNumint(numInt);
                    ubicacionFiscalDto.setNumext(numExt);
                    ubicacionFiscalDto.setColonia(colonia);
                    ubicacionFiscalDto.setCodigopostal(codigoPostal);
                    ubicacionFiscalDto.setPais(pais);
                    ubicacionFiscalDto.setEstado(estado);
                    ubicacionFiscalDto.setMunicipio(municipio);

                    /* Insertamos en ubicacion*/                               
                    ubicacionFiscalDaoImpl.insert(ubicacionFiscalDto);

                    
                }else{ /* Si existe el emisor actualizamos    --------------------------------------*/  
                    
                    Emisor emisorActualizadoDto = emisoresTmp[0];                    
                    emisorDaoImpl.update(emisorActualizadoDto.createPk(), emisorDto);

                    /* Creamos obj ubicacion*/
                    UbicacionFiscalBO ubicacionFiscalBO = new UbicacionFiscalBO(conn); 
                    Ubicacionfiscal ubicacionFiscalDto = ubicacionFiscalBO.findByIdEmisor(conn,emisorActualizadoDto.getIdemisor());

                    /* Seteamos  obj*/

                    ubicacionFiscalDto.setIdemisor(ubicacionFiscalDto.getIdemisor());
                    ubicacionFiscalDto.setCalle(calle);
                    ubicacionFiscalDto.setNumint(numInt);
                    ubicacionFiscalDto.setNumext(numExt);
                    ubicacionFiscalDto.setColonia(colonia);
                    ubicacionFiscalDto.setCodigopostal(codigoPostal);
                    ubicacionFiscalDto.setPais(pais);
                    ubicacionFiscalDto.setEstado(estado);
                    ubicacionFiscalDto.setMunicipio(municipio);

                    /* Insertamos en ubicacion*/               
                    UbicacionfiscalDaoImpl ubicacionFiscalDaoImpl = new UbicacionfiscalDaoImpl(this.conn);                
                    ubicacionFiscalDaoImpl.update(ubicacionFiscalDto.createPk(), ubicacionFiscalDto);
                       
                }
                
                
                
                conn.commit(); /* Ejecuta transaccion */
                JOptionPane.showMessageDialog(null,"Datos Guardados Satisfactoriamente.","Información",JOptionPane.INFORMATION_MESSAGE); 
                wPrincipal.tblEmisor.setModel(fillEmisores()); /* Recargamos Grid*/
                wPrincipal.limpiaFormulario(wPrincipal.jPanel7);
                
                
                
            }catch(EmisorDaoException | UbicacionfiscalDaoException | HeadlessException | SQLException e){
                conn.rollback(); /* Deshace transaccion*/                
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"Ocurrio un error al guardar el registro:\n " + e.toString(),"Error",JOptionPane.ERROR_MESSAGE);                
            }finally{
                conn.setAutoCommit(true); /* Activa nuevamente modo Autocomit*/
                //conn.close(); /* Cerramos conexión */
                
            }
            
                    
        }else{
            
            /* Mensaje Error */ 
            JOptionPane.showMessageDialog(null,"Datos No Validos :\n" + msgError ,"Error",JOptionPane.ERROR_MESSAGE); 
        }
        
            
    }
    
    
    public static DefaultTableModel fillEmisores() throws EmisorDaoException {             
        
         
        EmisorDaoImpl emisorDaoImpl = new EmisorDaoImpl(conn);   
        Emisor [] Emisores = emisorDaoImpl.findByDynamicWhere(" ESTATUS != -1 ", null); 
        
        
        DefaultTableModel ModelEmisores=new DefaultTableModel(); 
        
        ModelEmisores.setColumnCount(0); 
        ModelEmisores.addColumn("ID");  /* agregando Columnas a la Jtable */
        ModelEmisores.addColumn("RFC");
        ModelEmisores.addColumn("RAZON SOCIAL"); 
        ModelEmisores.addColumn("REGIMEN FISCAL"); 
        ModelEmisores.addColumn("ESTATUS"); 
        
        
        
        ModelEmisores.setNumRows(Emisores.length); 
        
        int i=0;
        for(Emisor item:Emisores){        
            ModelEmisores.setValueAt(item.getIdemisor(), i, 0);
            ModelEmisores.setValueAt(item.getRfc(), i, 1); 
            ModelEmisores.setValueAt(item.getRazonsocial(), i, 2); 
            ModelEmisores.setValueAt(item.getRegimenfiscal(), i, 3); 
            ModelEmisores.setValueAt(item.getEstatus()==1?"ACTIVO":"INACTIVO", i,4); 
            i++;
        } 
       
        
        
        return ModelEmisores;
        
        
    }

    public void cargaEmisor(int idEmisor) throws Exception {
        
        EmisorBO emisorBO = new EmisorBO();
        Emisor emisorDto = new Emisor();             
        emisorDto = emisorBO.findEmisorbyId(Conexion.getConn(),idEmisor);
        
            
        
        UbicacionFiscalBO ubicacionFiscalBO = new UbicacionFiscalBO(Conexion.getConn());
        Ubicacionfiscal ubicacionDto = new Ubicacionfiscal();         
        ubicacionDto = ubicacionFiscalBO.findByIdEmisor(Conexion.getConn(),idEmisor);
        
        /* Ponemos datos en jpanel*/
        
        wPrincipal.txtRfc.setText(emisorDto.getRfc());
        wPrincipal.txtRazonSocial.setText(emisorDto.getRazonsocial());
        wPrincipal.txtNombreComercial.setText(emisorDto.getNombrecomercial());
        wPrincipal.txtRegimenFiscal.setText(emisorDto.getRegimenfiscal());
        wPrincipal.txtCalle.setText(ubicacionDto.getCalle());
        wPrincipal.txtNumExt.setText(ubicacionDto.getNumext());
        wPrincipal.txtNumInt.setText(ubicacionDto.getNumint());
        wPrincipal.txtColonia.setText(ubicacionDto.getColonia());
        wPrincipal.txtCodigoPostal.setText(ubicacionDto.getCodigopostal());
        wPrincipal.txtPais.setText(ubicacionDto.getPais());
        wPrincipal.txtEstado.setText(ubicacionDto.getEstado());
        wPrincipal.txtMunicipio.setText(ubicacionDto.getMunicipio());
        wPrincipal.txtLogo.setText(emisorDto.getRutalogo());
        wPrincipal.txtPlantilla.setText(emisorDto.getPlantillacomprobante());
        wPrincipal.txtPlantillaNomina.setText(emisorDto.getPlantillanomina());
        wPrincipal.txtCer.setText(emisorDto.getRutacer());
        wPrincipal.txtKey.setText(emisorDto.getRutakey());
        wPrincipal.txtContrasena.setText(emisorDto.getEmisorpass());
        wPrincipal.txtPlantillaRetenciones.setText(emisorDto.getPlantillaRetenciones());
        wPrincipal.chkSectorPrimario.setSelected(emisorDto.getSectorPrimario()==1?true:false);
    }

    public void eliminarEmisor(String rfc) throws EmisorDaoException { /* Actualiza estatus a -1  ---> Eliminado */
       
        if(rfc != null){
            /*Recupera rfc */                 
             String rfcEliminar = rfc;
             /*instanciamos  obj */

             EmisorDaoImpl emisorDaoImpl = new EmisorDaoImpl(this.conn);                 
             Emisor[] emisoresTmp = emisorDaoImpl.findWhereRfcEquals(rfcEliminar);/* Buscamos emisores por rfc*/

             if(emisoresTmp.length != 0){

                 Emisor emisorEliminadoDto = emisoresTmp[0];       
                 emisorEliminadoDto.setEstatus(-1);     /* Estatus -1 Eliminado*/        
                 emisorDaoImpl.update(emisorEliminadoDto.createPk(), emisorEliminadoDto);

                 JOptionPane.showMessageDialog(null,"Registro eliminado satisfactoriamente.","Información",JOptionPane.INFORMATION_MESSAGE);
                 wPrincipal.tblEmisor.setModel(fillEmisores()); /* Recargamos Grid*/
                 wPrincipal.limpiaFormulario(wPrincipal.jPanel7);

             }else{
                 JOptionPane.showMessageDialog(null, "El Emisor seleccionado no existe.");
             }
        }else{
             JOptionPane.showMessageDialog(null, "No ha seleccionado un Emisor.");
        }
             
        
    }
    
    
    
}
