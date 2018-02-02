/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.bo.comprobante;

import com.cmm.cvs2xml.util.DateManage;
import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.bo.ComprobanteFiscalBO;
import com.fens.desktopMonitor.bo.EmisorBO;
import com.fens.desktopMonitor.config.Configuration;
import com.fens.desktopMonitor.dto.Comprobantefiscal;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.dto.Emisor;
import com.fens.desktopMonitor.jdbc.ComprobantefiscalDaoImpl;
import com.fens.desktopMonitor.jdbc.ResourceManager;
import com.fens.desktopMonitor.util.FileManage;
import com.fens.desktopMonitor.util.GenericMethods;
import com.fens.desktopMonitor.util.GenericValidator;
import com.fens.desktopMonitor.views.Principal;
import com.tsp.interconecta.ws.WsGenericResp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mx.bigdata.sat.cfdiv.CFDv33;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import mx.bigdata.sat.common.nomina12.schema.Nomina;
import mx.bigdata.sat.retencion.CFDIRetencionv10;
import mx.bigdata.sat.retencion.v1.schema.Retenciones;

/**
 *
 * @author ISCesar
 */
public class ComprobanteProcesoCancelaBO {

    //Archivo a procesar
    private File fileCfdiXmlTimbrado = null;
    //Conexion a base de datos
    private Connection conn = null;
    //Configuracion del usuario
    private Configuracion configuracionDto = null;
    
    //Objeto respuesta DTO
    private Comprobantefiscal comprobanteFiscalDto = null;
    
    //Objeto auxiliar para validaciones de campos
    private final GenericValidator gc = new GenericValidator();
    
    //Flag para indicar error en el proceso por conexión a servicios PAC
    private boolean errorConexionPAC = false;
    
     //referencia a la vista para acceder al control Log - OPCIONAL
    private Principal wPrincipal = null;
    
    //Flag para indicar si se enviaran correos particulares, 1x1 en caso de errores
    // true en caso de requerirlo, false en caso de querer solo enviar el correo final unificado
    private boolean enviarCorreosUnicos = false;
    
    //Lista de errores generados en el proceso de este Archivo Maestro
    private List<String> listaMensajes = new ArrayList<String>();
    
    public ComprobanteProcesoCancelaBO(File fileCfdiXmlTimbrado,
            Configuracion configuracionDto, Connection conn){
        this.fileCfdiXmlTimbrado = fileCfdiXmlTimbrado;
        this.configuracionDto = configuracionDto;
        this.conn = conn;
    }
    
    public boolean tryProcesoCancela() throws Exception{
        
        try{
            //Validamos parametros de entrada
            this.validaParametros();
        }catch(Exception ex){
            throw new Exception("Error en parametros de ejecución: " + ex.getMessage());
        }
        
        comprobanteFiscalDto = new Comprobantefiscal();
        
        ComprobantefiscalDaoImpl comprobanteFiscalDao = new ComprobantefiscalDaoImpl(this.getConn());
        
        int tipoComprobante = 0;
        try{
            //Verificamos de que tipo de comprobante es (CFDI 3.2, Retenciones 1.0)
            tipoComprobante = verificaTipoComprobante();
            
            if (tipoComprobante == ComprobanteFiscalBO.TIPO_COMPROBANTE_FACTURA){
                return procesaCFDI32(comprobanteFiscalDao);
            }else if (tipoComprobante == ComprobanteFiscalBO.TIPO_COMPROBANTE_RETENCIONES) {
                return procesaRetenciones10(comprobanteFiscalDao);
            }
            
        }catch(Exception ex){
            //Error a nivel de elemento Comprobante Fiscal Cancelacion
            registraErrorArchivo(ex);
                        
            //Actualizamos registro Comprobantefiscal en BD (ERROR)
            if(comprobanteFiscalDto.getIdcomprobantefiscal()>0){
                comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_CANCELAR_ERROR);
                comprobanteFiscalDao.update(comprobanteFiscalDto.createPk(),comprobanteFiscalDto);
            }
            
        }
        
        return false;
        
    }
    
    protected int verificaTipoComprobante() throws Exception{
        int tipoComprobante = 0;
        
        String msgError = "";
        try{
            Comprobante comp = CFDv33.newComprobante(new FileInputStream(fileCfdiXmlTimbrado));
            if (comp!=null)
                tipoComprobante = ComprobanteFiscalBO.TIPO_COMPROBANTE_FACTURA;
        }catch(Exception ex){
            msgError += "No es un XML correspondiente al esquema CFDI 3.2 válido. ";
        }
        
        if (tipoComprobante==0){
            try{
                Retenciones comp = CFDIRetencionv10.newComprobante(new FileInputStream(fileCfdiXmlTimbrado));
                if (comp!=null)
                    tipoComprobante = ComprobanteFiscalBO.TIPO_COMPROBANTE_RETENCIONES;
            }catch(Exception ex){
                msgError += "No es un XML correspondiente al esquema Retenciones 1.0 válido. ";
            }
        }
        
        if (tipoComprobante==0){
            throw new Exception("Archivo inválido para cancelación: " + msgError);
        }
        
        return tipoComprobante;
    }
    
    protected boolean procesaCFDI32(ComprobantefiscalDaoImpl comprobanteFiscalDao) throws Exception{
        
        //Intentamos convertir archivo de entrada a Objetos de lenguaje
        Cfd33BO cfd33BO = null;
        try{                
            cfd33BO = new Cfd33BO(fileCfdiXmlTimbrado,Cfd33BO.CONTEXT_ARRAY_COMPLEMENTOS);
        }catch(Exception ex){
            throw new Exception("Error al intentar convertir archivo "
                    + "timbrado a objetos de lenguaje. Recuerde que los archivos a Cancelar no deben tener Addendas. " + ex.toString());
        }

        //Recuperamos RFC del emisor del comprobante
        String rfcEmisor;
        try{
            rfcEmisor = cfd33BO.getComprobanteFiscal().getEmisor().getRfc();
        }catch(Exception ex){
            throw new Exception("No se pudo extrar del comprobante fiscal a cancelar "
                    + "el RFC de emisor. " + ex.toString());
        }

        //Buscamos en la BD local el registro del Emisor asociado al RFC
        EmisorBO emisorBO = new EmisorBO(this.getConn());
        Emisor[] listaEmisorDto = emisorBO.findEmisores(-1, 0, 0, " AND RFC='" + rfcEmisor + "'");
        if (listaEmisorDto.length<=0){
            throw new Exception("El RFC de emisor expresado en el comprobante fiscal a cancelar "
                    + "'" +rfcEmisor+ "'"
                    + ", no esta registrado en la base de datos local.");
        }

        //Datos de empresa en bd local
        Emisor emisorDto = listaEmisorDto[0];

        //Id Emisor
        comprobanteFiscalDto.setIdemisor(emisorDto.getIdemisor());
        //Estatus
        comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_CANCELAR_EN_PROCESO);

        //Tipo comprobante
        if (tieneComplementoNomina(cfd33BO.getComprobanteFiscal())){
            comprobanteFiscalDto.setIdtipocomprobante(ComprobanteFiscalBO.TIPO_COMPROBANTE_NOMINA);
        }else{
            comprobanteFiscalDto.setIdtipocomprobante(ComprobanteFiscalBO.TIPO_COMPROBANTE_FACTURA);
        }

        //Nombre archivo
        comprobanteFiscalDto.setNombrearchivoxml(fileCfdiXmlTimbrado.getName());

        //Insertamos registro con datos minimos requeridos
        comprobanteFiscalDao.insert(comprobanteFiscalDto);

        try{
            //Serie
            comprobanteFiscalDto.setSerie(cfd33BO.getComprobanteFiscal().getSerie());
            //Folio
            comprobanteFiscalDto.setFolio(cfd33BO.getComprobanteFiscal().getFolio().substring(0, 10));
            //UUID
            comprobanteFiscalDto.setUuid(cfd33BO.getTimbreFiscalDigital().getUUID());
            //Sello Emisor
            comprobanteFiscalDto.setSelloemisor(cfd33BO.getComprobanteFiscal().getSello());
            //fecha hora sellado
            comprobanteFiscalDto.setFechahorasellado(cfd33BO.getComprobanteFiscal().getFecha().toGregorianCalendar().getTime());
            //fecha hora timbrado
            comprobanteFiscalDto.setFechahoratimbrado(cfd33BO.getTimbreFiscalDigital().getFechaTimbrado().toGregorianCalendar().getTime());
            //fecha hora proceso
            comprobanteFiscalDto.setFechahoraproceso(new Date());
            //rfc receptor
            comprobanteFiscalDto.setRfcreceptor(cfd33BO.getComprobanteFiscal().getReceptor().getRfc());
            //subtotal
            comprobanteFiscalDto.setSubtotal(cfd33BO.getComprobanteFiscal().getSubTotal().doubleValue());
            //total
            comprobanteFiscalDto.setTotal(cfd33BO.getComprobanteFiscal().getTotal().doubleValue());
            //cadena original
            comprobanteFiscalDto.setCadenaoriginal(cfd33BO.getCfd().getCadenaOriginal());
            //fecha cancelacion
            comprobanteFiscalDto.setFechacancelacion(new Date());
            //moneda
            comprobanteFiscalDto.setMoneda(String.valueOf(cfd33BO.getComprobanteFiscal().getMoneda()));

            //Actualizamos registro
            comprobanteFiscalDao.update(comprobanteFiscalDto.createPk(), comprobanteFiscalDto);
        }catch(Exception ex){
            throw new Exception("Error al intentar extraer información básica del"
                    + " Comprobante Fiscal a cancelar (p. ej: serie, folio, UUID, rfc receptor, sello...). "
                    + "Verifique que el archivo es un Comprobante Fiscal Digital válido con Timbre Fiscal." +ex.toString());
        }

        File fileCertificadoEmisor = new File(emisorDto.getRutacer());
        File fileLlavePrivadaEmisor = new File(emisorDto.getRutakey());
        byte[] certificadoEmisor=null;
        byte[] llavePrivadaEmisor=null;
        try{
            if (!gc.isValidFile(fileCertificadoEmisor))
                throw new Exception("Archivo de Certificado Publico de Emisor no existe o no ha sido definido correctamente.");
            if (!gc.isValidFile(fileLlavePrivadaEmisor))
                throw new Exception("Archivo de Llave Privada de Emisor no existe o no ha sido definido correctamente.");
            certificadoEmisor = FileManage.getBytesFromFile(fileCertificadoEmisor);
            llavePrivadaEmisor = FileManage.getBytesFromFile(fileLlavePrivadaEmisor);
        }catch(Exception ex){
            throw new Exception("No fue posible extraer el contenido del certificado "
                    + "y/o llave privada de emisor, verifique la configuración "
                    + "del Emisor con RFC '" + rfcEmisor + "' . " + ex.getMessage());
        }
        
        if(cfd33BO!=null&&cfd33BO.getComprobanteFiscal()!=null){
            cfd33BO.getComprobanteFiscal().setAddenda(null);
        }
        
        
        byte[] contenidoXML = null;
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            cfd33BO.getCfd().guardar(baos);
            contenidoXML = baos.toByteArray();
        }catch(Exception ex){
            throw new Exception("No fue posible extraer el contenido archivo a cancelar '"
                    + fileCfdiXmlTimbrado.getName() + "' . Verifique la validez del archivo. " 
                    + ex.toString());
        }

        //Procesamos
        WsGenericResp respWs = null;
        try{
            ConexionPAC conexionPAC = new ConexionPAC(this.configuracionDto);
            if(emisorDto.getSectorPrimario()==1){
                respWs = conexionPAC.cancelaComprobanteSectorPrimarioPAC(contenidoXML, certificadoEmisor, llavePrivadaEmisor, emisorDto.getEmisorpass());
            }else{
                respWs = conexionPAC.cancelaComprobantePAC(contenidoXML, certificadoEmisor, llavePrivadaEmisor, emisorDto.getEmisorpass());
            }
        }catch(Exception ex){
            registraErrorConnPAC(ex);

            //Forzamos final de metodo
            return false; 
        }

        //Procesamos respuesta de PAC
        if (respWs==null){
            throw new Exception("Respuesta de servicio PAC con valor nulo. ");
        }else{

            //Buscamos errores
            if (respWs.isIsError() && respWs.getNumError()!=202){

                //Revisamos si el codigo de respuesta es uno que corresponde
                // propiamente al PAC, su responsabilidad
                if (respWs.getNumError()==902 ||
                        respWs.getNumError()==903){
                    registraErrorConnPAC(new Exception("Existe conexión con el PAC, " 
                            + " sin embargo respondio con un error de su responsabilidad: "
                            + respWs.getErrorMessage()));
                    //Forzamos final de metodo
                    return false; 
                }

                if (respWs.getNumError()==205){
                    //Error al cancelar folio ante SAT, no existe
                    throw new Exception("Folio no cancelado ante SAT, "
                            + " puede ser debido a que el Comprobante aun no esta registrado, espere de 1 hasta 72 hrs después de su emisión. "
                            + " Numero de error, respuesta PAC: " + respWs.getNumError() + " : " + respWs.getErrorMessage());
                }

                //Con errores en general (responsabilidad del usuario local)
                throw new Exception("No se pudo cancelar comprobante, respuesta del PAC a continuación. " + 
                            respWs.getNumError() + " : " + respWs.getErrorMessage());

            }else{
                if (respWs.getFolioCodCancelacion().equals("201")
                        || respWs.getFolioCodCancelacion().equals("202")
                        || respWs.getNumError()==202){
                    //Exito en proceso de Cancelación

                    String rutaCarpetaCancelaExito = configuracionDto.getRutadestinocancelacionesxml()
                            + File.separator + Configuration.nombreDirExitoCancelacion
                            + File.separator;

                    String rutaCarpetaCancelaAdvertencia = configuracionDto.getRutadestinocancelacionesxml()
                            + File.separator + Configuration.nombreDirAdvertenciaCancelacion
                            + File.separator;

                    if (respWs.getFolioCodCancelacion().equals("201")){
                        //mover archivo a carpeta exito
                        String rutaArchivoExito = rutaCarpetaCancelaExito 
                                + FileManage.getFileNameWithoutExtension(fileCfdiXmlTimbrado.getName()) + ".cancelado";
                        FileManage.moveFile(fileCfdiXmlTimbrado.getAbsolutePath(), rutaArchivoExito);

                        //Escribir Acuse en carpeta exito
                        if(respWs.getAcuse()!=null){
                            try {
                                FileManage.createFileFromByteArray(respWs.getAcuse(), rutaCarpetaCancelaExito, 
                                        FileManage.getFileNameWithoutExtension(fileCfdiXmlTimbrado.getName()) + "_ACUSE.xml");
                            }catch(Exception e){
                                System.out.println("Error al escribir Acuse de Cancelacion: " + e.getMessage());
                            }
                        }
                    }else {
                        //mover archivo a carpeta Advertencias
                        String rutaArchivoExito = rutaCarpetaCancelaAdvertencia + fileCfdiXmlTimbrado.getName();
                        FileManage.moveFile(fileCfdiXmlTimbrado.getAbsolutePath(), rutaArchivoExito);

                        if (respWs.getFolioCodCancelacion().equals("202") || respWs.getNumError()==202)
                            listaMensajes.add("ADVERTENCIA Cancelación archivo<br/>"
                                    + "Archivo " + fileCfdiXmlTimbrado.getName() + " cancelado previamente. " + respWs.getErrorMessage() );

                        //Escribir Acuse en carpeta advertencia
                        if(respWs.getAcuse()!=null){
                            try {
                                FileManage.createFileFromByteArray(respWs.getAcuse(), rutaCarpetaCancelaAdvertencia, 
                                        FileManage.getFileNameWithoutExtension(fileCfdiXmlTimbrado.getName()) + "_ACUSE.xml");
                            }catch(Exception e){
                                System.out.println("Error al escribir Acuse de Cancelacion: " + e.getMessage());
                            }
                        }

                    }

                    //Almacenar información actualizada
                    comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_CANCELAR_EXITO);
                    comprobanteFiscalDao.update(comprobanteFiscalDto.createPk(), comprobanteFiscalDto);

                    return true;
                }else{
                    //Error al cancelar folio ante SAT, no existe o no corresponde a emisor
                    throw new Exception("Folio no cancelado ante SAT, "
                            + " puede ser debido a que el Comprobante aun no esta registrado, espere de 1 hasta 72 hrs después de su emisión. "
                            + " Numero de error, respuesta PAC: " + respWs.getFolioCodCancelacion());
                }

            }

        }
        
    }
    
    protected boolean procesaRetenciones10(ComprobantefiscalDaoImpl comprobanteFiscalDao) throws Exception{
        
        //Intentamos convertir archivo de entrada a Objetos de lenguaje
        CfdRetenciones10BO cfdRetenciones10BO = null;
        try{                
            cfdRetenciones10BO = new CfdRetenciones10BO(fileCfdiXmlTimbrado,CfdRetenciones10BO.CONTEXT_ARRAY_COMPLEMENTOS);
        }catch(Exception ex){
            throw new Exception("Error al intentar convertir archivo "
                    + "timbrado de Retenciones a objetos de lenguaje. Recuerde que los archivos a Cancelar no deben tener Addendas. " + ex.toString());
        }

        //Recuperamos RFC del emisor del comprobante
        String rfcEmisor;
        try{
            rfcEmisor = cfdRetenciones10BO.getComprobanteRetenciones().getEmisor().getRFCEmisor();
        }catch(Exception ex){
            throw new Exception("No se pudo extrar del comprobante de retenciones a cancelar "
                    + "el RFC de emisor. " + ex.toString());
        }

        //Buscamos en la BD local el registro del Emisor asociado al RFC
        EmisorBO emisorBO = new EmisorBO(this.getConn());
        Emisor[] listaEmisorDto = emisorBO.findEmisores(-1, 0, 0, " AND RFC='" + rfcEmisor + "'");
        if (listaEmisorDto.length<=0){
            throw new Exception("El RFC de emisor expresado en el comprobante de retenciones a cancelar "
                    + "'" +rfcEmisor+ "'"
                    + ", no esta registrado en la base de datos local.");
        }

        //Datos de empresa en bd local
        Emisor emisorDto = listaEmisorDto[0];

        //Id Emisor
        comprobanteFiscalDto.setIdemisor(emisorDto.getIdemisor());
        //Estatus
        comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_CANCELAR_EN_PROCESO);

        //Tipo comprobante
        /*
        if (tieneComplementoNomina(cfdRetenciones10BO.getComprobanteRetenciones())){
            comprobanteFiscalDto.setIdtipocomprobante(ComprobanteFiscalBO.TIPO_COMPROBANTE_NOMINA);
        }else{
            comprobanteFiscalDto.setIdtipocomprobante(ComprobanteFiscalBO.TIPO_COMPROBANTE_FACTURA);
        }
        */
        comprobanteFiscalDto.setIdtipocomprobante(ComprobanteFiscalBO.TIPO_COMPROBANTE_RETENCIONES);

        //Nombre archivo
        comprobanteFiscalDto.setNombrearchivoxml(fileCfdiXmlTimbrado.getName());

        //Insertamos registro con datos minimos requeridos
        comprobanteFiscalDao.insert(comprobanteFiscalDto);

        try{
            //Serie
            //comprobanteFiscalDto.setSerie(cfdRetenciones10BO.getComprobanteRetenciones());
            //Folio
            comprobanteFiscalDto.setFolio(cfdRetenciones10BO.getComprobanteRetenciones().getFolioInt());
            //UUID
            comprobanteFiscalDto.setUuid(cfdRetenciones10BO.getTimbreFiscalDigital().getUUID());
            //Sello Emisor
            comprobanteFiscalDto.setSelloemisor(cfdRetenciones10BO.getComprobanteRetenciones().getSello());
            //fecha hora sellado
            comprobanteFiscalDto.setFechahorasellado(cfdRetenciones10BO.getComprobanteRetenciones().getFechaExp());
            //fecha hora timbrado
            comprobanteFiscalDto.setFechahoratimbrado(cfdRetenciones10BO.getTimbreFiscalDigital().getFechaTimbrado());
            //fecha hora proceso
            comprobanteFiscalDto.setFechahoraproceso(new Date());
            //rfc receptor
            comprobanteFiscalDto.setRfcreceptor(StringManage.getValidString(cfdRetenciones10BO.getRfcReceptor()));
            //subtotal
            comprobanteFiscalDto.setSubtotal(0);
            //total
            comprobanteFiscalDto.setTotal(cfdRetenciones10BO.getComprobanteRetenciones().getTotales().getMontoTotOperacion().doubleValue());
            //cadena original
            comprobanteFiscalDto.setCadenaoriginal(cfdRetenciones10BO.getCfd().getCadenaOriginal());
            //fecha cancelacion
            comprobanteFiscalDto.setFechacancelacion(new Date());
            //moneda
            //comprobanteFiscalDto.setMoneda("");

            //Actualizamos registro
            comprobanteFiscalDao.update(comprobanteFiscalDto.createPk(), comprobanteFiscalDto);
        }catch(Exception ex){
            throw new Exception("Error al intentar extraer información básica del"
                    + " Comprobante de Retenciones a cancelar (p. ej: folio, UUID, rfc receptor, sello...). "
                    + "Verifique que el archivo es un Comprobante de Retenciones válido con Timbre Fiscal." +ex.toString());
        }

        File fileCertificadoEmisor = new File(emisorDto.getRutacer());
        File fileLlavePrivadaEmisor = new File(emisorDto.getRutakey());
        byte[] certificadoEmisor=null;
        byte[] llavePrivadaEmisor=null;
        try{
            if (!gc.isValidFile(fileCertificadoEmisor))
                throw new Exception("Archivo de Certificado Publico de Emisor no existe o no ha sido definido correctamente.");
            if (!gc.isValidFile(fileLlavePrivadaEmisor))
                throw new Exception("Archivo de Llave Privada de Emisor no existe o no ha sido definido correctamente.");
            certificadoEmisor = FileManage.getBytesFromFile(fileCertificadoEmisor);
            llavePrivadaEmisor = FileManage.getBytesFromFile(fileLlavePrivadaEmisor);
        }catch(Exception ex){
            throw new Exception("No fue posible extraer el contenido del certificado "
                    + "y/o llave privada de emisor, verifique la configuración "
                    + "del Emisor con RFC '" + rfcEmisor + "' . " + ex.getMessage());
        }

        byte[] contenidoXML = null;
        try{
            contenidoXML = FileManage.getBytesFromFile(fileCfdiXmlTimbrado);
        }catch(Exception ex){
            throw new Exception("No fue posible extraer el contenido archivo a cancelar '"
                    + fileCfdiXmlTimbrado.getName() + "' . Verifique la validez del archivo. " 
                    + ex.toString());
        }

        //Procesamos
        WsGenericResp respWs = null;
        try{
            ConexionPAC conexionPAC = new ConexionPAC(this.configuracionDto);
            respWs = conexionPAC.cancelaRetencionesPAC(contenidoXML, certificadoEmisor, llavePrivadaEmisor, emisorDto.getEmisorpass());
        }catch(Exception ex){
            registraErrorConnPAC(ex);

            //Forzamos final de metodo
            return false; 
        }

        //Procesamos respuesta de PAC
        if (respWs==null){
            throw new Exception("Respuesta de servicio PAC con valor nulo. ");
        }else{

            //Buscamos errores
            if (respWs.isIsError() && respWs.getNumError()!=1202){

                //Revisamos si el codigo de respuesta es uno que corresponde
                // propiamente al PAC, su responsabilidad
                if (respWs.getNumError()==902 ||
                        respWs.getNumError()==903){
                    registraErrorConnPAC(new Exception("Existe conexión con el PAC, " 
                            + " sin embargo respondio con un error de su responsabilidad: "
                            + respWs.getErrorMessage()));
                    //Forzamos final de metodo
                    return false; 
                }

                if (respWs.getNumError()==1205){
                    //Error al cancelar folio ante SAT, no existe
                    throw new Exception("Folio de Retenciones no cancelado ante SAT, "
                            + " puede ser debido a que el Comprobante aun no esta registrado, espere de 1 hasta 72 hrs después de su emisión. "
                            + " Numero de error, respuesta PAC: " + respWs.getNumError() + " : " + respWs.getErrorMessage());
                }

                //Con errores en general (responsabilidad del usuario local)
                throw new Exception("No se pudo cancelar comprobante, respuesta del PAC a continuación. " + 
                            respWs.getNumError() + " : " + respWs.getErrorMessage());

            }else{
                if (respWs.getFolioCodCancelacion().equals("1201")
                        || respWs.getFolioCodCancelacion().equals("1202")
                        || respWs.getNumError()==1202){
                    //Exito en proceso de Cancelación

                    String rutaCarpetaCancelaExito = configuracionDto.getRutadestinocancelacionesxml()
                            + File.separator + Configuration.nombreDirExitoCancelacion
                            + File.separator;

                    String rutaCarpetaCancelaAdvertencia = configuracionDto.getRutadestinocancelacionesxml()
                            + File.separator + Configuration.nombreDirAdvertenciaCancelacion
                            + File.separator;

                    if (respWs.getFolioCodCancelacion().equals("1201")){
                        //mover archivo a carpeta exito
                        String rutaArchivoExito = rutaCarpetaCancelaExito 
                                + FileManage.getFileNameWithoutExtension(fileCfdiXmlTimbrado.getName()) + ".cancelado";
                        FileManage.moveFile(fileCfdiXmlTimbrado.getAbsolutePath(), rutaArchivoExito);

                        //Escribir Acuse en carpeta exito
                        if(respWs.getAcuse()!=null){
                            try {
                                FileManage.createFileFromByteArray(respWs.getAcuse(), rutaCarpetaCancelaExito, 
                                        FileManage.getFileNameWithoutExtension(fileCfdiXmlTimbrado.getName()) + "_ACUSE.xml");
                            }catch(Exception e){
                                System.out.println("Error al escribir Acuse de Cancelacion: " + e.getMessage());
                            }
                        }
                    }else {
                        //mover archivo a carpeta Advertencias
                        String rutaArchivoExito = rutaCarpetaCancelaAdvertencia + fileCfdiXmlTimbrado.getName();
                        FileManage.moveFile(fileCfdiXmlTimbrado.getAbsolutePath(), rutaArchivoExito);

                        if (respWs.getFolioCodCancelacion().equals("1202") || respWs.getNumError()==1202)
                            listaMensajes.add("ADVERTENCIA Cancelación archivo Retenciones<br/>"
                                    + "Archivo " + fileCfdiXmlTimbrado.getName() + " cancelado previamente. " + respWs.getErrorMessage() );

                        //Escribir Acuse en carpeta advertencia
                        if(respWs.getAcuse()!=null){
                            try {
                                FileManage.createFileFromByteArray(respWs.getAcuse(), rutaCarpetaCancelaAdvertencia, 
                                        FileManage.getFileNameWithoutExtension(fileCfdiXmlTimbrado.getName()) + "_ACUSE.xml");
                            }catch(Exception e){
                                System.out.println("Error al escribir Acuse de Cancelacion: " + e.getMessage());
                            }
                        }

                    }

                    //Almacenar información actualizada
                    comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_CANCELAR_EXITO);
                    comprobanteFiscalDao.update(comprobanteFiscalDto.createPk(), comprobanteFiscalDto);

                    return true;
                }else{
                    //Error al cancelar folio ante SAT, no existe o no corresponde a emisor
                    throw new Exception("Folio de Retenciones no cancelado ante SAT, "
                            + " puede ser debido a que el Comprobante aun no esta registrado, espere de 1 hasta 72 hrs después de su emisión. "
                            + " Numero de error, respuesta PAC: " + respWs.getFolioCodCancelacion());
                }

            }

        }
        
    }
    
    /**
     * Detecta si el Comprobante dado contiene el complemento de nomina
     * @param comprobante
     * @return true en caso de contenerlo, false en caso contrario
     */
    protected boolean tieneComplementoNomina(Comprobante comprobante){
        boolean tieneComplementoNomina = false;
        
        List<Comprobante.Complemento> complementos = comprobante.getComplemento();
        if (complementos != null && complementos.size()>0) {
          for(Comprobante.Complemento item:complementos){
            java.util.List<Object> listComplementos = item.getAny();
            for (Object o : listComplementos) {

              //Nomina
              if (o instanceof Nomina) {
                  tieneComplementoNomina = true;
              }

            }
          }
        }
        
        return tieneComplementoNomina;
    }
    
    /**
     * Valida los parametros requeridos por el Objeto para poder
     * efectuar las acciones o procesos implementados.
     * @throws Exception 
     */
    protected void validaParametros() throws Exception{        
        if (!gc.isValidFile(fileCfdiXmlTimbrado))
            throw new Exception("El archivo Comprobante Fiscal a cancelar no existe en la carpeta local o no ha sido especificado.");
        
        if (configuracionDto==null)
            throw new Exception("No se ha especificado una configuracion de usuario a utilizar.");
          
        if (!gc.isValidDirectory(new File(configuracionDto.getRutaorigencancelacionesxml())))
            throw new Exception("El directorio de origen para cancelaciones no existe o no ha sido especificado.");
        
        if (!gc.isValidDirectory(new File(configuracionDto.getRutadestinocancelacionesxml())))
            throw new Exception("El directorio de salida para cancelaciones no existe o no ha sido especificado.");
        
    }
    
    
    protected void registraErrorConnPAC(Exception ex){
        this.errorConexionPAC = true;
        
        // p. ej: /cancela_out/error/pac/
        String rutaCarpetaCancelaErrorPAC = configuracionDto.getRutadestinocancelacionesxml()
                                + File.separator + Configuration.nombreDirErroresCancelacion
                                + File.separator + Configuration.nombreDirErroresPAC
                                + File.separator;
        
        
        //Creamos archivo de Log
        try{
            String tituloError = "\nError al intentar cancelar Comprobante con servicios PAC. "
                    + "\n(No es necesario que vuelva a depositar el archivo, en automático se volvió a "
                    + "depositar en el repositorio de Entrada de Cancelacion para la próxima ejecución del cron). \n\n"
                    + ex.toString();
            System.out.println(tituloError);
            FileManage.createFileFromString(tituloError,
                    rutaCarpetaCancelaErrorPAC, 
                    fileCfdiXmlTimbrado.getName()+"_LOG_CONN_PAC_ERROR.txt");
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //Enviamos info de archivo con error
        try{
            List<String> listaDestinatarios = GenericMethods.getListaCorreosValidos(configuracionDto.getDestinatariosfijos(), ";");
            if (listaDestinatarios.size()>0)
                enviarCorreoErrorConexionPAC(listaDestinatarios, ex.toString(), fileCfdiXmlTimbrado.getName());
        }catch(Exception e){
            e.printStackTrace();
        }
        //Movemos archivo a destino carpeta Errores PAC
        // y tambien de regreso a la carpeta de Cancelacion IN
        try{
            String rutaArchivoDestinoError = rutaCarpetaCancelaErrorPAC + fileCfdiXmlTimbrado.getName();
            String rutaArchivoIn = configuracionDto.getRutaorigencancelacionesxml()
                    + File.separator + fileCfdiXmlTimbrado.getName();
            
            //Copiamos archivo a carpeta Cancelaciones IN (para que se vuelva a procesar)
            FileManage.copyFile(fileCfdiXmlTimbrado.getAbsolutePath(), rutaArchivoIn);
            //Movemos archivo a carpeta de error para que quede registro
            FileManage.moveFile(fileCfdiXmlTimbrado.getAbsolutePath(), rutaArchivoDestinoError);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    protected void registraErrorArchivo(Exception ex){
        String rutaCarpetaCancelaError = configuracionDto.getRutadestinocancelacionesxml()
                                + File.separator + Configuration.nombreDirErroresCancelacion
                                + File.separator;
        
        if (fileCfdiXmlTimbrado==null
                || StringManage.getValidString(fileCfdiXmlTimbrado.getName()).equals("")){
            return;
        }
        
        //Creamos archivo de Log
        try{
            String tituloError = "\n---ERROR Procesando Cancelación. ";
            System.out.println(tituloError + "\n" + ex.getMessage());
            
            FileManage.createFileFromString(tituloError + "\n" + ex.getMessage(),
                    rutaCarpetaCancelaError, 
                    fileCfdiXmlTimbrado.getName()+"_LOG_ERROR.txt");
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //Enviamos info de archivo con error
        try{
            List<String> listaDestinatarios = GenericMethods.getListaCorreosValidos(configuracionDto.getDestinatariosfijos(), ";");
            if (listaDestinatarios.size()>0)
                enviarCorreoErrorArchivo(listaDestinatarios, ex.getMessage(), fileCfdiXmlTimbrado.getName());
        }catch(Exception e){
            e.printStackTrace();
        }
        //Movemos archivo a destino carpeta Errores
        try{
            FileManage.moveFile(fileCfdiXmlTimbrado.getAbsolutePath(), rutaCarpetaCancelaError + fileCfdiXmlTimbrado.getName());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    protected void enviarCorreoErrorConexionPAC(List<String> listaDestinatarios,
            String errorMsg, String nombreArchivoOriginal) throws Exception{
        
        if (listaDestinatarios.size()>0){
            MailBO mailBO  = inicializarMail(listaDestinatarios, null, null, null);

            String cuerpoCorreo = "Error en comunicación con servicios PAC.<br/>";
            cuerpoCorreo += "<br/><b>Proceso: </b>Cancelación.";
            cuerpoCorreo += "<br/><b>Fecha y Hora: </b>" + DateManage.dateTimeToStringEspanol(new Date());
            cuerpoCorreo += "<br/><b>Nombre de archivo original: </b>" + StringManage.getValidString(nombreArchivoOriginal);
            cuerpoCorreo += "<br/><b>Descripción del error: </b>";
            cuerpoCorreo += "<br/><i>"  + errorMsg + "</i>";

            cuerpoCorreo += "</br></br> (No es necesario que vuelva a depositar el archivo, en automático se volvió a "
                        + "depositar en el repositorio de Entrada de Cancelación para la próxima ejecución del cron). ";

            listaMensajes.add(cuerpoCorreo);
            mailBO.addMessagePlantilla(cuerpoCorreo);
            int destinatariosTotal = mailBO.getToQuantity() + mailBO.getCcQuantity();
            if (destinatariosTotal>0 && !enviarCorreosUnicos)
                mailBO.send(StringManage.getValidString(configuracionDto.getAsuntocorreo()) + " - Error comunicación con servicios PAC");
        }
    }
    
    protected void enviarCorreoErrorArchivo(List<String> listaDestinatarios,
            String errorMsg, String nombreArchivoOriginal) throws Exception{
        
        if (configuracionDto.getEnviodecorreo()==1 && listaDestinatarios.size()>0){
            MailBO mailBO  = inicializarMail(listaDestinatarios, null, null, null);

            String cuerpoCorreo = "Error al procesar Archivo.<br/>";
            cuerpoCorreo += "<br/><b>Tipo archivo: </b> Cancelación.";
            cuerpoCorreo += "<br/><b>Fecha y Hora: </b>" + DateManage.dateTimeToStringEspanol(new Date());
            cuerpoCorreo += "<br/><b>Nombre de archivo original: </b>" + StringManage.getValidString(nombreArchivoOriginal);
            cuerpoCorreo += "<br/><b>Descripción del error: </b>";
            cuerpoCorreo += "<br/><i>"  + errorMsg + "</i>";

            listaMensajes.add(cuerpoCorreo);
            mailBO.addMessagePlantilla(cuerpoCorreo);
            int destinatariosTotal = mailBO.getToQuantity() + mailBO.getCcQuantity();
            if (destinatariosTotal>0 && !enviarCorreosUnicos)
                mailBO.send(StringManage.getValidString(configuracionDto.getAsuntocorreo()) + " - Error procesamiento");
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

    public File getFileCfdiXmlTimbrado() {
        return fileCfdiXmlTimbrado;
    }

    public void setFileCfdiXmlTimbrado(File fileCfdiXmlTimbrado) {
        this.fileCfdiXmlTimbrado = fileCfdiXmlTimbrado;
    }

    public Configuracion getConfiguracionDto() {
        return configuracionDto;
    }

    public void setConfiguracionDto(Configuracion configuracionDto) {
        this.configuracionDto = configuracionDto;
    }

    public Comprobantefiscal getComprobanteFiscalDto() {
        return comprobanteFiscalDto;
    }

    public void setComprobanteFiscalDto(Comprobantefiscal comprobanteFiscalDto) {
        this.comprobanteFiscalDto = comprobanteFiscalDto;
    }

    public boolean isErrorConexionPAC() {
        return errorConexionPAC;
    }

    public void setErrorConexionPAC(boolean errorConexionPAC) {
        this.errorConexionPAC = errorConexionPAC;
    }

    public Principal getwPrincipal() {
        return wPrincipal;
    }

    public void setwPrincipal(Principal wPrincipal) {
        this.wPrincipal = wPrincipal;
    }

    public boolean isEnviarCorreosUnicos() {
        return enviarCorreosUnicos;
    }

    public void setEnviarCorreosUnicos(boolean enviarCorreosUnicos) {
        this.enviarCorreosUnicos = enviarCorreosUnicos;
    }

    public List<String> getListaMensajes() {
        return listaMensajes;
    }

    public void setListaMensajes(List<String> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }
    
}
