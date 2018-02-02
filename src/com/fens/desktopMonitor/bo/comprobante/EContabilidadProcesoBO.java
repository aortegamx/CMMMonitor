/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.bo.comprobante;

import com.cmm.cvs2xml.CmmCvsConvert;
import com.cmm.cvs2xml.bean.FacturaDatos;
import com.cmm.cvs2xml.bo.AuxiliarCtasDatosBO;
import com.cmm.cvs2xml.bo.AuxiliarFoliosDatosBO;
import com.cmm.cvs2xml.bo.BalanzaDatosBO;
import com.cmm.cvs2xml.bo.CatalogoDatosBO;
import com.cmm.cvs2xml.bo.PolizasDatosBO;
import com.cmm.cvs2xml.util.DateManage;
import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.bo.ComprobanteFiscalBO;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.jdbc.ResourceManager;
import com.fens.desktopMonitor.util.FileManage;
import com.fens.desktopMonitor.util.GenericMethods;
import com.fens.desktopMonitor.util.GenericValidator;
import com.fens.desktopMonitor.views.Principal;
import com.tsp.interconecta.ws.WsGenericResp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante;
import mx.bigdata.sat.econtabilidad.v13.BCEv13;
import mx.bigdata.sat.econtabilidad.v13.CTLv13;
import mx.bigdata.sat.econtabilidad.v13.Ctasv13;
import mx.bigdata.sat.econtabilidad.v13.PLZv13;
import mx.bigdata.sat.econtabilidad.v13.RespAuxv13;
import mx.bigdata.sat.econtabilidad.v13.schema.balanza.Balanza;
import mx.bigdata.sat.econtabilidad.v13.schema.ctas.AuxiliarCtas;
import mx.bigdata.sat.econtabilidad.v13.schema.cuentas.Catalogo;
import mx.bigdata.sat.econtabilidad.v13.schema.folios.RepAuxFol;
import mx.bigdata.sat.econtabilidad.v13.schema.polizas.Polizas;

/**
 *
 * @author ISCesar
 */
public class EContabilidadProcesoBO {

    //Archivo a procesar
    private File fileArchivoMaestro = null;
    //Conexion a base de datos
    private Connection conn = null;
    //Configuracion del usuario
    private Configuracion configuracionDto = null;
    
    //Objeto para conversion (CSV -> XML)
    protected CmmCvsConvert cmmCsvConvert;
    
    //Objeto auxiliar para validaciones de campos
    private final GenericValidator gc = new GenericValidator();
    
    //Flag para indicar error en el proceso por conexión a servicios PAC
    private boolean errorConexionPAC = false;
    
    //referencia a la vista para acceder al control Log - OPCIONAL
    private Principal wPrincipal = null;
    
    public EContabilidadProcesoBO(File fileArchivoMaestro,
            Configuracion configuracionDto, Connection conn){
        this.fileArchivoMaestro = fileArchivoMaestro;
        this.configuracionDto = configuracionDto;
        this.conn = conn;
    }
    
    public void tryProcesoExpide() throws Exception{
        
        try{
            //Validamos parametros de entrada
            this.validaParametros();
        }catch(Exception ex){
            throw new Exception("Error en parametros de ejecución: " + ex.getMessage());
        }
        
        TipoArchivo tipoFormato = TipoArchivo.FORMATO_CONTABILIDAD_DESCONOCIDO;
        try{            
            cmmCsvConvert = new CmmCvsConvert();
            cmmCsvConvert.convertFile(fileArchivoMaestro);
            
            boolean exitoEscritura = true;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String nombreArchivoResultado = FileManage.getFileNameWithoutExtension(fileArchivoMaestro.getName()) + ".xml";
            String rutaArchivoResultado =  configuracionDto.getRutaCarpetaDestinoEcontabilidad() + File.separatorChar;
            File archivoFormatoExito = null;
            
            //Proceso del ws para generar comprobante
            WsGenericResp respWs;
            ConexionPAC conexionPAC = new ConexionPAC(this.configuracionDto);
            
            if (cmmCsvConvert.getCatalogoDatos()!=null){
                //Formato de Catalogo
                tipoFormato = TipoArchivo.FORMATO_CONTABILIDAD_CATALOGO;
                
                CatalogoDatosBO catalogoDatosBO = new CatalogoDatosBO(cmmCsvConvert.getCatalogoDatos());
                Catalogo catalogo = catalogoDatosBO.compilarFormato();
                CTLv13 cTLv1 = new CTLv13(catalogo);
                cTLv1.validar();
                
                try{
                    respWs=conexionPAC.timbraComprobanteEContabilidad(baos.toByteArray());
                }catch(UnsupportedEncodingException | MalformedURLException ex){
                    throw new Exception("Error al intentar generar la contabilidad electronica con servicios PAC. " + ex.toString());
                }
                if(!respWs.isIsError()){
                    cTLv1.guardar(baos);
                
                    archivoFormatoExito = new File(rutaArchivoResultado + "catalogoCtas_" + nombreArchivoResultado);
                    FileManage.createFileFromByteArrayOutputStream(baos, archivoFormatoExito);
                }else{
                    throw new Exception("Error al intentar generar la contabilidad electronica con servicios PAC. " + respWs.getErrorMessage());
                }
            }else if (cmmCsvConvert.getBalanzaDatos()!=null){
                //Formato de Balanza
                tipoFormato = TipoArchivo.FORMATO_CONTABILIDAD_BALANZA;
                
                BalanzaDatosBO balanzaDatosBO = new BalanzaDatosBO(cmmCsvConvert.getBalanzaDatos());
                Balanza balanza = balanzaDatosBO.compilarFormato();
                BCEv13 bCEv1 = new BCEv13(balanza);
                bCEv1.validar();
                
                try{
                    respWs=conexionPAC.timbraComprobanteEContabilidad(baos.toByteArray());
                }catch(UnsupportedEncodingException | MalformedURLException ex){
                    throw new Exception("Error al intentar generar la contabilidad electronica con servicios PAC. " + ex.toString());
                }
                if(!respWs.isIsError()){
                    bCEv1.guardar(baos);
                
                    archivoFormatoExito = new File(rutaArchivoResultado + "balanza_" + nombreArchivoResultado);
                    FileManage.createFileFromByteArrayOutputStream(baos, archivoFormatoExito);
                }else{
                    throw new Exception("Error al intentar generar la contabilidad electronica con servicios PAC. " + respWs.getErrorMessage());
                }
            }else if (cmmCsvConvert.getPolizasDatos()!=null){
                //Formato de Polizas
                tipoFormato = TipoArchivo.FORMATO_CONTABILIDAD_POLIZAS;
                
                PolizasDatosBO polizasDatosBO = new PolizasDatosBO(cmmCsvConvert.getPolizasDatos());
                Polizas polizas = polizasDatosBO.compilarFormato();
                PLZv13 pLZv1 = new PLZv13(polizas);
                pLZv1.validar();
                
                try{
                    respWs=conexionPAC.timbraComprobanteEContabilidad(baos.toByteArray());
                }catch(UnsupportedEncodingException | MalformedURLException ex){
                    throw new Exception("Error al intentar generar la contabilidad electronica con servicios PAC. " + ex.toString());
                }
                if(!respWs.isIsError()){
                    pLZv1.guardar(baos);
                
                    archivoFormatoExito = new File(rutaArchivoResultado + "poliza_" + nombreArchivoResultado);
                    FileManage.createFileFromByteArrayOutputStream(baos, archivoFormatoExito);
                }else{
                    throw new Exception("Error al intentar generar la contabilidad electronica con servicios PAC. " + respWs.getErrorMessage());
                }
            }else if (cmmCsvConvert.getAuxCtasDatos()!=null){
                //Auxiliar cuentas
                tipoFormato=TipoArchivo.FORMATO_CONTABILIDAD_AUXILIAR_CUENTAS;
                
                AuxiliarCtasDatosBO auxCtasBO=new AuxiliarCtasDatosBO(cmmCsvConvert.getAuxCtasDatos());
                AuxiliarCtas auxCtas=auxCtasBO.compilarFormato();
                Ctasv13 ctas=new Ctasv13(auxCtas);
                ctas.validar();
                
                try{
                    respWs=conexionPAC.timbraComprobanteEContabilidad(baos.toByteArray());
                }catch(UnsupportedEncodingException | MalformedURLException ex){
                    throw new Exception("Error al intentar generar la contabilidad electronica con servicios PAC. " + ex.toString());
                }
                if(!respWs.isIsError()){
                    ctas.guardar(baos);
                
                    archivoFormatoExito = new File(rutaArchivoResultado + "auxiliar_cuentas_" + nombreArchivoResultado);
                    FileManage.createFileFromByteArrayOutputStream(baos, archivoFormatoExito);
                }else{
                    throw new Exception("Error al intentar generar la contabilidad electronica con servicios PAC. " + respWs.getErrorMessage());
                }
            }else if(cmmCsvConvert.getAuxFoliosDatos()!=null){
                //Auxiliar folios
                tipoFormato=TipoArchivo.FORMATO_CONTABILIDAD_AUXILIAR_FOLIO;
                
                AuxiliarFoliosDatosBO auxFolioBO=new AuxiliarFoliosDatosBO(cmmCsvConvert.getAuxFoliosDatos());
                RepAuxFol auxFolio=auxFolioBO.compilarFormato();
                RespAuxv13 folio=new RespAuxv13(auxFolio);
                folio.validar();
                
                try{
                    respWs=conexionPAC.timbraComprobanteEContabilidad(baos.toByteArray());
                }catch(UnsupportedEncodingException | MalformedURLException ex){
                    throw new Exception("Error al intentar generar la contabilidad electronica con servicios PAC. " + ex.toString());
                }
                if(!respWs.isIsError()){
                    folio.guardar(baos);
                
                    archivoFormatoExito = new File(rutaArchivoResultado + "auxiliar_folio_" + nombreArchivoResultado);
                    FileManage.createFileFromByteArrayOutputStream(baos, archivoFormatoExito);
                }else{
                    throw new Exception("Error al intentar generar la contabilidad electronica con servicios PAC. " + respWs.getErrorMessage());
                }
            }else{
                //Error a nivel Archivo Maestro de entrada
                throw new Exception("El archivo de entrada contiene 0 registros de Formatos para Contabilidad Electrónica.");
            }
            
            if (exitoEscritura){
                //Movemos archivo a destino carpeta Exito
                try{
                    String rutaArchivoDestinoExito = configuracionDto.getRutaCarpetaDestinoEcontabilidad(); //UtilNombraArchivosBO.calcFileArchivoMaestro(configuracionDto, ArchivoMaestroBO.ESTATUS_EXITO, fileArchivoMaestro).getAbsolutePath();
                    
                    rutaArchivoDestinoExito += File.separator +  FileManage.getFileNameWithoutExtension(fileArchivoMaestro.getName())
                                            + ".procesado";
                    
                    //Movemos archivo a carpeta 
                    FileManage.moveFile(fileArchivoMaestro.getAbsolutePath(), rutaArchivoDestinoExito);
                    
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                //Acciones en forma asíncrona
                try{
                    final File archivoComprobanteFiscalExito_Aux = archivoFormatoExito;
                    final TipoArchivo tipoFormato_Aux = tipoFormato;

                    Runnable r1 = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println("[Asíncrono] Ejecutando acciones para comprobante. " + new Date());

                            } catch (Exception ex) {
                                System.out.println("[Asíncrono] Error al intentar ejecutar acciones para comprobante. " + ex.toString());
                                ex.printStackTrace();
                            }
                        }
                    };

                    Thread thr1 = new Thread(r1);
                    thr1.start();

                }catch(Exception ex){
                    System.out.println("Error inesperado al ejecutar acciones para comprobante timbrado de forma asíncrona. " + ex.toString());
                    ex.printStackTrace();
                }
            }
            
        }catch(Exception ex){
            //Error a nivel Archivo Maestro de entrada
            registraErrorArchivo(TipoArchivo.FORMATO_CONTABILIDAD_DESCONOCIDO, ex);            
        }
    }
    
    /**
     * Valida los parametros requeridos por el Objeto para poder
     * efectuar las acciones o procesos implementados.
     * @throws Exception 
     */
    protected void validaParametros() throws Exception{        
        if (!gc.isValidFile(fileArchivoMaestro))
            throw new Exception("El archivo maestro a procesar no existe o no ha sido especificado.");
        
        if (configuracionDto==null)
            throw new Exception("No se ha especificado una configuracion de usuario a utilizar.");
            
        if (!gc.isValidDirectory(new File(configuracionDto.getRutaCarpetaOrigenEcontabilidad())))
            throw new Exception("El directorio de Origen para la Contabilidad electrónica no existe o no ha sido especificado.");
        
        if (!gc.isValidDirectory(new File(configuracionDto.getRutaCarpetaDestinoEcontabilidad())))
            throw new Exception("El directorio de Destino para la Contabilidad electrónica no existe o no ha sido especificado.");
        
    }
    
    protected void registraErrorArchivo(TipoArchivo tipoArchivo, Exception ex){
        //Creamos archivo de Log
        try{
            String tituloError = "\n---ERROR Procesando " + (tipoArchivo==TipoArchivo.FORMATO_CONTABILIDAD_DESCONOCIDO ? "Formato para Contabilidad Electronica": "" ) + ". ";
            System.out.println(tituloError + "\n" + ex.getMessage());
            FileManage.createFileFromString(tituloError + "\n" + ex.getMessage()
                    + "\n" + GenericMethods.exceptionStackTraceToString(ex) ,
                    configuracionDto.getRutacarpetaerrores()+File.separator, 
                    fileArchivoMaestro.getName()+"_LOG_ERROR.txt");
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //Enviamos info de archivo con error
        try{
            List<String> listaDestinatarios = GenericMethods.getListaCorreosValidos(configuracionDto.getDestinatariosfijos(), ";");
            if (listaDestinatarios.size()>0)
                enviarCorreoErrorArchivo(listaDestinatarios, ex.getMessage(), tipoArchivo, fileArchivoMaestro.getName());
        }catch(Exception e){
            e.printStackTrace();
        }
        //Movemos archivo a destino carpeta Errores
        try{
            String rutaArchivoDestinoError = configuracionDto.getRutacarpetaerrores() + File.separator + fileArchivoMaestro.getName();
            FileManage.moveFile(fileArchivoMaestro.getAbsolutePath(), rutaArchivoDestinoError);
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    protected void enviarCorreoErrorArchivo(List<String> listaDestinatarios,
            String errorMsg, TipoArchivo tipoArchivo, String nombreArchivoOriginal) throws Exception{
        
        if (configuracionDto.getEnviodecorreo()==1 && listaDestinatarios.size()>0){
            MailBO mailBO  = inicializarMail(listaDestinatarios, null, null, null);

            String cuerpoCorreo = "Error al procesar Archivo.<br/>";
            cuerpoCorreo += "<br/><b>Tipo archivo: </b>" + (tipoArchivo==TipoArchivo.FORMATO_CONTABILIDAD_DESCONOCIDO ? "Formato para Contabilidad Electronica": "" );
            cuerpoCorreo += "<br/><b>Fecha y Hora: </b>" + DateManage.dateTimeToStringEspanol(new Date());
            cuerpoCorreo += "<br/><b>Nombre de archivo original: </b>" + StringManage.getValidString(nombreArchivoOriginal);
            cuerpoCorreo += "<br/><b>Descripción del error: </b>";
            cuerpoCorreo += "<br/><i>"  + errorMsg + "</i>";

            mailBO.addMessagePlantilla(cuerpoCorreo);
            int destinatariosTotal = mailBO.getToQuantity() + mailBO.getCcQuantity();
            if (destinatariosTotal>0)
                mailBO.send(StringManage.getValidString(configuracionDto.getAsuntocorreo()) + " - Error procesamiento");
        }
    }
    
    protected void enviarCorreoComprobanteFiscalReceptor(List<String> listaDestinatarios, List<String> listaCC,
            File fileXML, File filePDF, Comprobante comp, String UUID, int idTipoComprobante) throws Exception{
        
        if (configuracionDto.getEnviodecorreo()==1 && 
                (listaDestinatarios.size()>0 || listaCC.size()>0) ){
            //List<String> listaCC = GenericMethods.getListaCorreosValidos(configuracionDto.getDestinatariosfijos(), ";");

            List<File> archivosAdjuntos = new ArrayList<File>(Arrays.asList(fileXML, filePDF));
            MailBO mailBO = inicializarMail(listaDestinatarios, listaCC, null, archivosAdjuntos);

            String cuerpoCorreo = "Comprobante Fiscal generado.<br/>";
            cuerpoCorreo += "<br/><b><i>Tipo Comprobante</i></b> : " + (idTipoComprobante==ComprobanteFiscalBO.TIPO_COMPROBANTE_NOMINA ? "N&oacute;mina": "Factura" );
            cuerpoCorreo += "<br/><b><i>Emisor</i></b> : " + StringManage.getValidString(comp.getEmisor().getRfc()) + " - "  + StringManage.getValidString(comp.getEmisor().getNombre());
            cuerpoCorreo += "<br/><b><i>Receptor</i></b> : " + StringManage.getValidString(comp.getReceptor().getRfc()) + " - "  + StringManage.getValidString(comp.getReceptor().getNombre());
            cuerpoCorreo += "<br/><b><i>Folio Fiscal</i></b> : " + StringManage.getValidString(UUID);
            cuerpoCorreo += "<br/><b><i>Serie - Folio</i></b> : " + StringManage.getValidString(comp.getSerie()) + " - " + StringManage.getValidString(comp.getFolio()) ;
            cuerpoCorreo += "<br/><b><i>Fecha sellado</i></b> : " + DateManage.dateTimeToStringEspanol(comp.getFecha()) ;
            mailBO.addMessagePlantilla(cuerpoCorreo);

            int destinatariosTotal = mailBO.getToQuantity() + mailBO.getCcQuantity();
            if (destinatariosTotal>0){
                mailBO.send(StringManage.getValidString(configuracionDto.getAsuntocorreo()) + " - Comprobante Fiscal " + StringManage.getValidString(UUID));
                
                actualizarLogVista("\t\t * Envio por correo " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                                    + " - " + new Date());
            }
            
        }
    }
    
    protected MailBO inicializarMail(List<String> listaDestinatarios, List<String> listaCC, 
            List<String> listaCCO, List<File> listaArchivosAdjuntos) throws Exception{
        
        MailBO mailBO = new MailBO(this.getConn(), false);
        mailBO.setConfiguration(configuracionDto);
        mailBO.setEnvioAsincrono(true);
        
        if (listaDestinatarios!=null)
            for (String dest : listaDestinatarios) {
                try {
                    mailBO.addTo(dest, dest);
                } catch (Exception e) {
                    System.out.println("No se pudo agregar el destinatario: " + dest + e.getMessage());
                }
            }
        
        if (listaCC!=null)
            for (String dest : listaCC) {
                try {
                    mailBO.addCC(dest, dest);
                } catch (Exception e) {
                    System.out.println("No se pudo agregar el destinatario CC: " + dest + e.getMessage());
                }
            }
        if (listaCCO!=null)
            for (String dest : listaCCO) {
                try {
                    mailBO.addBCC(dest, dest);
                } catch (Exception e) {
                    System.out.println("No se pudo agregar el destinatario CCO: " + dest + e.getMessage());
                }
            }
        
        if (listaArchivosAdjuntos!=null)
            for (File adjunto : listaArchivosAdjuntos) {
                if (adjunto!=null){
                    try {
                        mailBO.addFile(adjunto.getAbsolutePath(), adjunto.getName());
                    } catch (Exception e) {
                        System.out.println("No se pudo agregar el archivo Adjunto: " + adjunto.getName() + e.getMessage());
                    }
                }
            }
        
        return mailBO;
    }

    public void ejecutaAcciones(File fileXML, File filePDF, Comprobante comp, 
            String UUID, int idTipoComprobante,FacturaDatos facturaDatos) throws Exception{
        
        boolean notificarCorreo = false;
        try{
            notificarCorreo = facturaDatos.getLineaDatosCliente().getDatosReceptor().isNotificar();
        }catch(Exception ex){}
        
        //---------------------------------------------
        //Acciones básicas
        {
            actualizarLogVista("\t ++ Accion basica para comprobante " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                        + " - " + new Date());
            //Enviamos notificacion correo
            try{
                //Correo a Receptor
                if (notificarCorreo){
                    //List<String> listaDestinatario = new ArrayList<String>(Arrays.asList(facturaDatos.getLineaDatosCliente().getDatosReceptor().getEmail()));
                    List<String> listaDestinatario = new ArrayList<String>();
                    if (gc.isEmail(facturaDatos.getLineaDatosCliente().getDatosReceptor().getEmail()))
                        listaDestinatario.add(facturaDatos.getLineaDatosCliente().getDatosReceptor().getEmail());

                    List<String> listaCC = GenericMethods.getListaCorreosValidos(configuracionDto.getDestinatariosfijos(), ";");
                    enviarCorreoComprobanteFiscalReceptor(listaDestinatario, listaCC, fileXML, 
                            filePDF, comp, UUID, idTipoComprobante);
                }
            }catch(Exception ex){
                throw new Exception("Error inesperado al enviar correo con Comprobante Fiscal generado a receptor. " + ex.toString());
            }
        }
        
    }
    
    protected void actualizarLogVista(String mensaje){
        //Si tenemos conexion a la vista, actualizamos el log
        if (wPrincipal!=null){
            wPrincipal.txtLogTemporal.append("\n" + mensaje);
        }
    }

    public Principal getwPrincipal() {
        return wPrincipal;
    }

    public void setwPrincipal(Principal wPrincipal) {
        this.wPrincipal = wPrincipal;
    }
    
    public enum TipoArchivo{
        FORMATO_CONTABILIDAD_DESCONOCIDO,
        FORMATO_CONTABILIDAD_CATALOGO,
        FORMATO_CONTABILIDAD_BALANZA,
        FORMATO_CONTABILIDAD_POLIZAS,
        FORMATO_CONTABILIDAD_AUXILIAR_CUENTAS,
        FORMATO_CONTABILIDAD_AUXILIAR_FOLIO
    }
    
    public boolean isErrorConexionPAC() {
        return errorConexionPAC;
    }

    public void setErrorConexionPAC(boolean errorConexionPAC) {
        this.errorConexionPAC = errorConexionPAC;
    }
    
    public Connection getConn() {
        if (this.conn==null){
            try {
                this.conn = ResourceManager.getConnection();
            } catch (SQLException ex) {}
        }else{
            try {
                if (this.conn.isClosed()){
                    this.conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public File getFileArchivoMaestro() {
        return fileArchivoMaestro;
    }

    public void setFileArchivoMaestro(File fileArchivoMaestro) {
        this.fileArchivoMaestro = fileArchivoMaestro;
    }

    public Configuracion getConfiguracionDto() {
        return configuracionDto;
    }

    public void setConfiguracionDto(Configuracion configuracionDto) {
        this.configuracionDto = configuracionDto;
    }
    
}
