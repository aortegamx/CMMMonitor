/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.bo.comprobante;

import com.cmm.cvs2xml.CmmCvsConvert;
import com.cmm.cvs2xml.bean.FacturaDatos;
import com.cmm.cvs2xml.bo.FacturaDatosBO;
import com.cmm.cvs2xml.bo.retenciones.RetencionesDatosBO;
import com.cmm.cvs2xml.complementos.nomina.bean.LineaDatosNomina;
import com.cmm.cvs2xml.retenciones.bean.RetencionesDatos;
import com.cmm.cvs2xml.util.DateManage;
import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.bo.AccionComprobanteBO;
import com.fens.desktopMonitor.bo.ArchivoMaestroBO;
import com.fens.desktopMonitor.bo.ComprobanteFiscalBO;
import com.fens.desktopMonitor.bo.EmisorBO;
import com.fens.desktopMonitor.bo.SerieBO;
import com.fens.desktopMonitor.bo.UbicacionFiscalBO;
import com.fens.desktopMonitor.config.Configuration;
import com.fens.desktopMonitor.dto.Accioncomprobante;
import com.fens.desktopMonitor.dto.Archivomaestro;
import com.fens.desktopMonitor.dto.Comprobantefiscal;
import com.fens.desktopMonitor.dto.ComprobantefiscalPk;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.dto.Emisor;
import com.fens.desktopMonitor.dto.Serie;
import com.fens.desktopMonitor.dto.Ubicacionfiscal;
import com.fens.desktopMonitor.jdbc.ArchivomaestroDaoImpl;
import com.fens.desktopMonitor.jdbc.ComprobantefiscalDaoImpl;
import com.fens.desktopMonitor.jdbc.NominaDaoImpl;
import com.fens.desktopMonitor.jdbc.ResourceManager;
import com.fens.desktopMonitor.jdbc.SerieDaoImpl;
import com.fens.desktopMonitor.util.DOMManage;
import com.fens.desktopMonitor.util.FileManage;
import com.fens.desktopMonitor.util.GenericMethods;
import com.fens.desktopMonitor.util.GenericValidator;
import com.fens.desktopMonitor.util.ImpresionJavaPDFBox;
import com.fens.desktopMonitor.views.Principal;
import com.google.common.collect.ImmutableMap;
import com.tsp.interconecta.ws.WsGenericResp;
import com.tsp.interconecta.ws.WsGenericRespExt;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import mx.bigdata.sat.cfdiv.CFDv33;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import mx.bigdata.sat.common.NamespacePrefixMapperImpl;
import mx.bigdata.sat.retencion.CFDIRetencionv10;
import mx.bigdata.sat.retencion.v1.schema.Retenciones;
import mx.bigdata.sat.security.KeyLoader2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author ISCesar
 */
public class ComprobanteProcesoExpideBO {

    //Archivo a procesar
    private File fileArchivoMaestro = null;
    //Conexion a base de datos
    private Connection conn = null;
    //Configuracion del usuario
    private Configuracion configuracionDto = null;
    
    //Objeto respuesta DTO
    private Archivomaestro archivoMaestroDto = null;
    //Objeto para conversion (CSV -> XML)
    protected CmmCvsConvert cmmCsvConvert;
    
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
    private List<String> listaMensajes = new ArrayList<>();
    
    //Flag para indicar si se ira almancenando una lista de Archivos
    // PDF de los timbrados exitosos para conformar un PDF unico al final
    private boolean generarPDFConcentrado = false;
    
    //Lista de archivos PDF, representaciones impresas de XML timbrados
    private List<File> listaArchivosPDF = new ArrayList<>();
    
    //Accion comprobante en caso de requerir la generacion de PDF Concentrado
    private Accioncomprobante accionComprobanteConcentrado = null;
    
    public ComprobanteProcesoExpideBO(File fileArchivoMaestro,
            Configuracion configuracionDto, Connection conn){
        this.fileArchivoMaestro = fileArchivoMaestro;
        this.configuracionDto = configuracionDto;
        this.conn = conn;
    }
    
    public boolean tryProcesoExpide() throws Exception{
        
        boolean incrementoFolio = false;//variable auxiliar para validar si se incremento el folio
        
        try{
            //Validamos parametros de entrada
            this.validaParametros();
        }catch(Exception ex){
            throw new Exception("Error en parametros de ejecución: " + ex.getMessage());
        }
        
        archivoMaestroDto = new Archivomaestro();
        
        ArchivomaestroDaoImpl archivomaestroDaoImpl = new ArchivomaestroDaoImpl(this.getConn());
        
        try{
            llenaInfoArchivoMaestro(archivoMaestroDto, fileArchivoMaestro);
            archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_EN_PROCESO);
            
            //Almacenamos registro de archivoMaestro
            archivomaestroDaoImpl.insert(archivoMaestroDto);
        }catch(Exception ex){
            throw new Exception("Error al intentar almacenar datos iniciales de Archivo Maestro en BD. "
                    + "Revise la conexion a BD y que el archivo fuente no este siendo usado por otro proceso." + ex.toString());
        }
        
        boolean errorValidacionPAC = false;
        try{            
            cmmCsvConvert = new CmmCvsConvert();
            cmmCsvConvert.convertFile(fileArchivoMaestro);
            
            //Almacenamos numero de Comprobantes que contiene el archivo maestro
            archivoMaestroDto.setNumerofacturas(cmmCsvConvert.getListaFacturaDatos().size());
            archivomaestroDaoImpl.update(archivoMaestroDto.createPk(), archivoMaestroDto);
                    
            ComprobantefiscalDaoImpl compFiscalDaoImpl = new ComprobantefiscalDaoImpl(this.getConn());
            SerieDaoImpl serieDao = new SerieDaoImpl(conn);
            
            if (verificaArchivoRepetido(archivoMaestroDto)){
                throw new Exception("El archivo fue previamente procesado con éxito (o se esta procesando)."
                        + " Ya existe un archivo procesado con las mismas características .");
            }
            
            if (cmmCsvConvert.getListaFacturaDatos().size()>0){
                int comprobantesFiscalesProcesados = 0;
                for (FacturaDatos facturaDatos : cmmCsvConvert.getListaFacturaDatos()){
                    
                    Comprobantefiscal comprobanteFiscalDto = new Comprobantefiscal();
                    comprobanteFiscalDto.setIdarchivomaestro(archivoMaestroDto.getIdarchivomaestro());
                    comprobanteFiscalDto.setFechahoraproceso(new Date());
                    comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_TIMBRAR_EN_PROCESO);
                    int idTipoComprobante;
                    Serie serie = null;
                    
                    try {
                        //Validamos tipo de comprobante 
                        if (facturaDatos.getNominaDatos()!=null
                                && facturaDatos.getNominaDatos().getLineaDatosNomina()!=null){
                            idTipoComprobante = ComprobanteFiscalBO.TIPO_COMPROBANTE_NOMINA;
                        }else{
                            idTipoComprobante = ComprobanteFiscalBO.TIPO_COMPROBANTE_FACTURA;
                        }
                        comprobanteFiscalDto.setIdtipocomprobante(idTipoComprobante);
                        
                        //Compilamos cada ComprobanteFiscal
                        FacturaDatosBO facturaDatosBO = new FacturaDatosBO(facturaDatos);
                        Comprobante comp = facturaDatosBO.compilarComprobante();
                        

                        //Recuperamos información de Emisor del comprobante particular
                        String rfcEmisor = null;
                        try { 
                            rfcEmisor = facturaDatos.getLineaDatosCliente().getRfcEmisor();
                        }catch(Exception ex){
                            throw new Exception("No se expreso un RFC de Emisor valido de acuerdo al Layout. ");
                        }

                        EmisorBO emisorBO = new EmisorBO(this.getConn());
                        Emisor[] listaEmisorDto = emisorBO.findEmisores(-1, 0, 0, " AND RFC='" + rfcEmisor + "'");
                        if (listaEmisorDto.length<=0){
                            throw new Exception("El RFC de emisor expresado en el Layout "
                                    + "de entrada no esta registrado en la base de datos local.");
                        }
                        
                        //Datos de empresa en bd local
                        Emisor emisorDto = listaEmisorDto[0];

                        //Domicilio Matriz
                        Ubicacionfiscal ubicacionfiscal = new UbicacionFiscalBO(this.getConn()).findByIdEmisor(this.getConn(),emisorDto.getIdemisor());
                        if (facturaDatos.getNominaDatos()==null){
                            //Completamos datos de nodo Emisor
                            llenaInfoEmisor(comp, emisorDto, ubicacionfiscal);
                        }else{//si es de tipo nomina, no mandamos la direccion del emisor
                            //Completamos datos de nodo Emisor
                            llenaInfoEmisor(comp, emisorDto, null);
                        }
                        comprobanteFiscalDto.setIdemisor(emisorDto.getIdemisor());
                        comp.setFecha(DateManage.dateToXMLGregorianCalendar(new Date()));
                        
                        //--inicio, metemos datos adicionales, por Leonardo Montes de Oca, el 07/12/2017
                        try{comprobanteFiscalDto.setNombreReceptor(facturaDatos.getLineaDatosCliente().getDatosReceptor().getReceptor().getNombre());}catch(Exception e){}
                        try{comprobanteFiscalDto.setEmail(facturaDatos.getLineaDatosCliente().getDatosReceptor().getEmail());}catch(Exception e){}
                        try{comprobanteFiscalDto.setReferencia1(facturaDatos.getLineaDatosCliente().getDatosReceptor().getReferencia1());}catch(Exception e){}
                        try{comprobanteFiscalDto.setReferencia2(facturaDatos.getLineaDatosCliente().getDatosReceptor().getReferencia2());}catch(Exception e){}
                        try{comprobanteFiscalDto.setReferencia3(facturaDatos.getLineaDatosCliente().getDatosReceptor().getReferencia3());}catch(Exception e){}
                        //-- fin
                        
                        //Insertamos registro con datos minimos requeridos
                        ComprobantefiscalPk comprobantefiscalPk = compFiscalDaoImpl.insert(comprobanteFiscalDto);
                        comprobanteFiscalDto.setIdcomprobantefiscal(comprobantefiscalPk.getIdcomprobantefiscal());

                        //Recuperamos objetos de Certificado y Llave privada del emisor
                        java.security.cert.X509Certificate certX509 = null;
                        java.security.PrivateKey pkey = null;
                        try{
                             certX509 = KeyLoader2.loadX509Certificate(new FileInputStream(emisorDto.getRutacer()));
                             pkey = KeyLoader2.loadPKCS8PrivateKey(new FileInputStream(emisorDto.getRutakey()), emisorDto.getEmisorpass());
                        }catch(Exception ex){
                            throw new Exception("El emisor de RFC '" + rfcEmisor + "' configurado"
                                    + " no tiene Certificado y Llave privada validos." + ex.toString());
                        }                  
                        //Serie - Folio
                        //validamos si ya trae una serie y un folio:
                        if((comp.getSerie() == null || comp.getFolio() == null) || (comp.getSerie().trim().equals("") || comp.getFolio().equals(""))){
                            SerieBO serieBO = new SerieBO(conn);
                            serie = serieBO.findUniqueSerieByEmisor(emisorDto.getIdemisor(), idTipoComprobante);
                            int nuevoFolio;
                            if (serie!=null){
                                //Calculamos nuevo folio
                                nuevoFolio = serie.getUltimoFolio() + 1;
                                //Colocamos nuevo valor en Serie (dto) y actualizamos en bd
                                serie.setUltimoFolio(nuevoFolio);
                                serieDao.update(serie.createPk(), serie);
                                incrementoFolio = true;
                                //Asignamos a comprobante valor de serie y folio
                                comp.setSerie(serie.getNombreSerie());
                                comp.setFolio(""+nuevoFolio);
                            }
                        }
                        
                        //verificamos la opcion del emisor para el timbrado del sector primario
                        if(emisorDto.getSectorPrimario()==1){
                            crearComprobanteSectorPrimario(comp);
                        }
                        
                        if (certX509!=null && pkey!=null){
                            CFDv33 cFDv33 = creaCFD(comp);
                            //Sellamos comprobante con CSD emisor
                            try{
                                cFDv33.sellar(pkey, certX509);
                            }catch(Exception ex){
                                throw new Exception("Error en sellado de XML traducido: " + ex.getCause());
                            }

                            //Validamos estructura y verificamos XML
                            try{
                                cFDv33.validar();
                                
                                //verificamos sellado
                                cFDv33.verificar();
                            }catch(Exception ex){
                                ex.printStackTrace();
                                throw new Exception("Error en validación de estructura de XML traducido: " + ex.getCause());
                            }
                            //Escribimos archivo (temporal) sellado exitoso
                            File archivoComprobanteFiscalEnProceso = UtilNombraArchivosBO.calcFileComprobanteFiscal(configuracionDto, 
                                    ComprobanteFiscalBO.ESTATUS_TIMBRAR_EN_PROCESO, fileArchivoMaestro,
                                    facturaDatos, null, null, "xml");
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            try{
                                cFDv33.guardar(baos);
                                archivoComprobanteFiscalEnProceso = FileManage.createFileFromByteArrayOutputStream(baos, archivoComprobanteFiscalEnProceso);
                                if (!archivoComprobanteFiscalEnProceso.exists())
                                    throw new Exception("Archivo En Proceso no existente.");
                            }catch(Exception ex){
                                throw new Exception("No se pudo almacenar el archivo sellado localmente con CSD emisor en '"
                                        + archivoComprobanteFiscalEnProceso.getAbsolutePath() + "' . " + ex.toString());
                            }
                            System.out.println(new String(baos.toByteArray()));
                            //Procesamos timbre en PAC
                            WsGenericResp respWs;
                            try{
                                ConexionPAC conexionPAC = new ConexionPAC(this.configuracionDto);
                                if(emisorDto.getSectorPrimario()==1){
                                    conexionPAC.otorgarAccesoContribuyente(StringManage.getValidString(comp.getReceptor().getRfc()), StringManage.getValidString(comp.getReceptor().getNombre()));
                                    respWs = conexionPAC.timbraComprobanteSectorPrimarioPAC(emisorDto.getRutacer(),emisorDto.getRutakey(),emisorDto.getEmisorpass(),baos.toByteArray());
                                }else{
                                    respWs = conexionPAC.timbraComprobantePAC(baos.toByteArray());
                                }
                            }catch(UnsupportedEncodingException | MalformedURLException ex){
                                //throw new Exception("Error al intentar timbrar Comprobante Fiscal con servicios PAC. " + ex.toString());
                                registraErrorConnPAC(ex);

                                //Eliminamos archivo temporal en proceso
                                for (int k=0; k<100; k++){
                                    if (archivoComprobanteFiscalEnProceso.delete())
                                        break;
                                }
                                
                                //Actualizamos serie deshaciendo uso de ultimo folio
                                if(incrementoFolio){
                                    if (serie!=null){
                                        serie.setUltimoFolio(serie.getUltimoFolio()-1);
                                        serieDao.update(serie.createPk(), serie);
                                    }
                                }

                                //Forzamos final de metodo
                                return false; 
                            }
                            //Procesamos respuesta de PAC
                            if (respWs==null){
                                throw new Exception("Respuesta de servicio PAC con valor nulo. ");
                            }else{
                                //Buscamos errores
                                if (respWs.isIsError()){
                                    //Con errores
                                    
                                    //intentamos eliminar XML temporal sellado localmente
                                    try{
                                        for (int k=0; k<100; k++){
                                            if (archivoComprobanteFiscalEnProceso.delete())
                                                break;
                                        }
                                    }catch(Exception ex){
                                        ex.printStackTrace();
                                    }
                                    
                                    errorValidacionPAC = true;
                                    throw new Exception("No se timbro el comprobante, respuesta del PAC a continuación. " + 
                                                respWs.getNumError() + " : " + respWs.getErrorMessage());
                                }else{
                                    //Todo correcto en PAC
                                    //Guardamos archivo XML
                                    File archivoComprobanteFiscalExito = null;
                                    try{ 
                                        archivoComprobanteFiscalExito = UtilNombraArchivosBO.calcFileComprobanteFiscal(configuracionDto, 
                                                            ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO, fileArchivoMaestro,
                                                            facturaDatos, null, respWs.getFolioUUID(), "xml");

                                        archivoComprobanteFiscalExito = FileManage.createFileFromByteArray(respWs.getXML(), 
                                                FileManage.getParentPathString(archivoComprobanteFiscalExito) + File.separator , 
                                                archivoComprobanteFiscalExito.getName());
                                        //asignamos dato a objeto DTO
                                        if (archivoComprobanteFiscalExito!=null)
                                            comprobanteFiscalDto.setNombrearchivoxml(archivoComprobanteFiscalExito.getName());
                                    }catch(Exception ex){
                                        throw new Exception("Error inesperado al crear archivo XML de Comprobante Fiscal timbrado." + ex.toString());
                                    }

                                    //intentamos eliminar XML temporal sellado localmente
                                    try{
                                        for (int k=0; k<100; k++){
                                            if (archivoComprobanteFiscalEnProceso.delete())
                                                break;
                                        }
                                    }catch(Exception ex){
                                        ex.printStackTrace();
                                    }

                                    //Creamos PDF
                                    File archivoComprobanteFiscalExitoPDF = null;
                                    try{ 
                                        archivoComprobanteFiscalExitoPDF = UtilNombraArchivosBO.calcFileComprobanteFiscal(configuracionDto, 
                                                            ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO, fileArchivoMaestro,
                                                            facturaDatos, null, respWs.getFolioUUID(),  "pdf");
                                        
                                        Cfd33BO cfd33BO = new Cfd33BO(archivoComprobanteFiscalExito, Cfd33BO.CONTEXT_ARRAY_COMPLEMENTOS);
                                        ByteArrayOutputStream baosPDF;
                                        
                                        Accioncomprobante accionComprobanteDto = buscaAccionPersonalizada(facturaDatos);
                                        cfd33BO.setFacturaDatos(facturaDatos);
                                        cfd33BO.setAccionComprobante(accionComprobanteDto);
                                        baosPDF = cfd33BO.toPdf();

                                        archivoComprobanteFiscalExitoPDF = FileManage.createFileFromByteArray(baosPDF.toByteArray(),
                                                 FileManage.getParentPathString(archivoComprobanteFiscalExitoPDF) + File.separator , 
                                                 archivoComprobanteFiscalExitoPDF.getName());
                                        
                                        //asignamos dato a objeto DTO
                                        if (archivoComprobanteFiscalExitoPDF!=null){
                                            comprobanteFiscalDto.setNombrearchivopdf(archivoComprobanteFiscalExitoPDF.getName());
                                            if (generarPDFConcentrado)
                                                listaArchivosPDF.add(archivoComprobanteFiscalExitoPDF);
                                        }
                                    }catch(Exception ex){
                                        String errorPDF = "Error inesperado crear representacion impresa de Comprobante Fiscal. " + GenericMethods.exceptionStackTraceToString(ex);
                                        System.out.println(errorPDF);
                                        actualizarLogVista(errorPDF);
                                        //throw new Exception("Error inesperado crear representacion impresa de Comprobante Fiscal. " + ex.toString());
                                    }

                                    //Completamos datos de registro BD
                                    try{ 
                                        llenaInfoComprobanteFiscalDto(comprobanteFiscalDto, (Comprobante) cFDv33.getComprobante().getComprobante(), facturaDatos, respWs, archivoComprobanteFiscalExito);
                                    }catch(Exception ex){
                                        throw new Exception("Error inesperado al almacenar información de Comprobante Fiscal generado. " + ex.toString());
                                    }
                                    
                                    // Si se tienen addendas, se agregan en este punto
                                    if (facturaDatosBO.getFacturaDatos().isTieneAddendas()){
                                        archivoComprobanteFiscalExito = agregaAddendasDespuesDeTimbrado(archivoComprobanteFiscalExito, facturaDatosBO);
                                    }
                                    //Acciones en forma asíncrona
                                    try{
                                        final File archivoComprobanteFiscalExito_Aux = archivoComprobanteFiscalExito;
                                        final File archivoComprobanteFiscalExitoPDF_Aux = archivoComprobanteFiscalExitoPDF;
                                        final Comprobante comp_Aux = comp;
                                        final String uuid_Aux= respWs.getFolioUUID();
                                        final int idTipoComprobante_Aux = idTipoComprobante;
                                        final FacturaDatos facturaDatos_Aux = facturaDatos;

                                        Runnable r1 = new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    System.out.println("[Asíncrono] Ejecutando acciones para comprobante. " + new Date());
                                                    ejecutaAcciones(archivoComprobanteFiscalExito_Aux,archivoComprobanteFiscalExitoPDF_Aux, 
                                                            comp_Aux, uuid_Aux, idTipoComprobante_Aux, facturaDatos_Aux);
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
                            }                                
                        }else{
                            throw new Exception("Certificado y/o Llave privada de emisor no se pudieron recuperar, revise la configuración del emisor.");
                        }

                        //Si llego a este punto, indica que el proceso del Comprobante Fiscal (singular)
                        // fue exitoso, se almacena el objeto y se suma uno al contador de procesados
                        comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO);
                        comprobantesFiscalesProcesados++;
                        //Guardar en BD
                        //Actualizamos registro Comprobantefiscal en BD
                        compFiscalDaoImpl.update(comprobanteFiscalDto.createPk(),comprobanteFiscalDto);
                        //Creamos registro Nomina en BD
                        guardaInfoNomina(facturaDatos, comprobanteFiscalDto.getIdcomprobantefiscal());
                        
                    }catch(Exception ex){
                        //Error a nivel de elemento Comprobante Fiscal
                        registraErrorArchivo(TipoArchivo.COMPROBANTE_FISCAL, ex);
                        
                        //Actualizamos registro Comprobantefiscal en BD (ERROR)
                        if(comprobanteFiscalDto.getIdcomprobantefiscal()>0){
                            comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_TIMBRAR_ERROR);
                            compFiscalDaoImpl.update(comprobanteFiscalDto.createPk(),comprobanteFiscalDto);
                        }
                        
                        //Actualizamos estatus en BD de Archivo Maestro (ERROR)
                        if(archivoMaestroDto.getIdarchivomaestro()>0){
                            //Actualizamos registro Comprobantefiscal en BD
                            archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_ERROR);
                            archivomaestroDaoImpl.update(archivoMaestroDto.createPk(),archivoMaestroDto);
                        }
                        
                        //Actualizamos serie deshaciendo uso de ultimo folio
                        if(incrementoFolio){
                            if (serie!=null){
                                serie.setUltimoFolio(serie.getUltimoFolio()-1);
                                serieDao.update(serie.createPk(), serie);
                            }
                        }
                        
                        break;
                    }
                }
            
                //Se termino de procesar el Archivo Maestro
                
                //Movemos archivo a destino carpeta Exito
                try{
                    String rutaArchivoDestinoExito = UtilNombraArchivosBO.calcFileArchivoMaestro(configuracionDto, ArchivoMaestroBO.ESTATUS_EXITO, fileArchivoMaestro).getAbsolutePath();
                    
                    //Movemos archivo a carpeta 
                    FileManage.moveFile(fileArchivoMaestro.getAbsolutePath(), rutaArchivoDestinoExito);
                    
                    //Actualizamos estatus en BD de Archivo Maestro (EXITO)
                    if(archivoMaestroDto.getIdarchivomaestro()>0 
                            && comprobantesFiscalesProcesados==cmmCsvConvert.getListaFacturaDatos().size()){
                        //Actualizamos registro Comprobantefiscal en BD
                        archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_EXITO);
                        archivomaestroDaoImpl.update(archivoMaestroDto.createPk(),archivoMaestroDto);
                    }
                    return !errorValidacionPAC;
                    
                }catch(Exception e){
                    e.printStackTrace();
                }
                
            } else if (cmmCsvConvert.getRetencionesDatos()!=null){
                
                //Comprobante de Retenciones
                return procesaRetenciones(cmmCsvConvert.getRetencionesDatos(), compFiscalDaoImpl, serieDao, archivomaestroDaoImpl);
                
            }else{
                //Error a nivel Archivo Maestro de entrada
                throw new Exception("El archivo de entrada contiene 0 registros de Comprobantes a procesar.");
            }
            
        }catch(Exception ex){
            //Error a nivel Archivo Maestro de entrada
            registraErrorArchivo(TipoArchivo.ARCHIVO_MAESTRO, ex);
            
            if(archivoMaestroDto.getIdarchivomaestro()>0){
                //Actualizamos registro Comprobantefiscal en BD
                archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_ERROR);
                archivomaestroDaoImpl.update(archivoMaestroDto.createPk(),archivoMaestroDto);
            }
            
        }
        return false;
    }
    
    private boolean procesaRetenciones(RetencionesDatos retencionesDatos, 
        ComprobantefiscalDaoImpl compFiscalDaoImpl, SerieDaoImpl serieDao,
        ArchivomaestroDaoImpl archivomaestroDaoImpl) throws Exception {
        
        boolean errorValidacionPAC = false;
        Comprobantefiscal comprobanteFiscalDto = new Comprobantefiscal();
        comprobanteFiscalDto.setIdarchivomaestro(archivoMaestroDto.getIdarchivomaestro());
        comprobanteFiscalDto.setFechahoraproceso(new Date());
        comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_TIMBRAR_EN_PROCESO);
        int idTipoComprobante = ComprobanteFiscalBO.TIPO_COMPROBANTE_RETENCIONES;
        //String rfcReceptorExtranjero = "";
        Serie serie = null;

        try {
            //Validamos tipo de comprobante 
            comprobanteFiscalDto.setIdtipocomprobante(idTipoComprobante);

            //Compilamos Comprobante
            RetencionesDatosBO retencionesDatosBO = new RetencionesDatosBO(retencionesDatos);
            Retenciones comp = retencionesDatosBO.compilarComprobante();

            //Recuperamos información de Emisor del comprobante particular
            String rfcEmisor = null;
            try { 
                rfcEmisor = retencionesDatos.getLineaDatosComprobante().getRetenciones().getEmisor().getRFCEmisor();
            }catch(Exception ex){
                throw new Exception("No se expreso un RFC de Emisor valido de acuerdo al Layout de RETENCIONES. ");
            }

            EmisorBO emisorBO = new EmisorBO(this.getConn());
            Emisor[] listaEmisorDto = emisorBO.findEmisores(-1, 0, 0, " AND RFC='" + rfcEmisor + "'");
            if (listaEmisorDto.length<=0){
                throw new Exception("El RFC de emisor expresado en el Layout de RETENCIONES "
                        + "de entrada no esta registrado en la base de datos local.");
            }

            //Datos de empresa en bd local
            Emisor emisorDto = listaEmisorDto[0];

            //Domicilio Matriz
            //Ubicacionfiscal ubicacionfiscal = new UbicacionFiscalBO(this.getConn()).findByIdEmisor(this.getConn(),emisorDto.getIdemisor());

            //Completamos datos de nodo Emisor
                //Nombre emisor
                comp.getEmisor().setNomDenRazSocE(emisorDto.getRazonsocial());
            comprobanteFiscalDto.setIdemisor(emisorDto.getIdemisor());
            comp.setFechaExp(new Date());

            //Insertamos registro con datos minimos requeridos
            compFiscalDaoImpl.insert(comprobanteFiscalDto);

            //Recuperamos objetos de Certificado y Llave privada del emisor
            java.security.cert.X509Certificate certX509 = null;
            java.security.PrivateKey pkey = null;
            try{
                 certX509 = KeyLoader2.loadX509Certificate(new FileInputStream(emisorDto.getRutacer()));
                 pkey = KeyLoader2.loadPKCS8PrivateKey(new FileInputStream(emisorDto.getRutakey()), emisorDto.getEmisorpass());
            }catch(Exception ex){
                throw new Exception("El emisor de RFC '" + rfcEmisor + "' configurado"
                        + " no tiene Certificado y Llave privada validos." + ex.toString());
            }

            //Serie - Folio
            SerieBO serieBO = new SerieBO(conn);
            serie = serieBO.findUniqueSerieByEmisor(emisorDto.getIdemisor(), idTipoComprobante);
            int nuevoFolio;
            if (serie!=null){
                //Calculamos nuevo folio
                nuevoFolio = serie.getUltimoFolio() + 1;
                //Colocamos nuevo valor en Serie (dto) y actualizamos en bd
                serie.setUltimoFolio(nuevoFolio);
                serieDao.update(serie.createPk(), serie);

                //Asignamos a comprobante valor de serie y folio
                //comp.setSerie(serie.getNombreSerie());
                comp.setFolioInt(""+nuevoFolio);
            }/*else{
                throw new Exception("No hay ninguna serie configurada para documentos de Retenciones, es indispensable, ya que el folio interno es requerido.");
            }*/
            
            if (certX509!=null && pkey!=null){
                CFDIRetencionv10 cFDIRetencionv10 = creaComprobanteRetenciones(comp);

                //Sellamos comprobante con CSD emisor
                try{
                    cFDIRetencionv10.sellar(pkey, certX509);
                }catch(Exception ex){
                    throw new Exception("Error en sellado de XML Retenciones traducido: " + ex.getMessage());
                }

                //Validamos estructura y verificamos XML
                try{
                    cFDIRetencionv10.validar();
                    //verificamos sellado
                    cFDIRetencionv10.verificar();
                }catch(Exception ex){
                    throw new Exception("Error en validación de estructura de XML Retenciones traducido: " + ex.getMessage());
                }

                //Escribimos archivo (temporal) sellado exitoso
                File archivoComprobanteFiscalEnProceso = UtilNombraArchivosBO.calcFileComprobanteFiscal(configuracionDto, 
                        ComprobanteFiscalBO.ESTATUS_TIMBRAR_EN_PROCESO, fileArchivoMaestro,
                        null, retencionesDatos, null, "xml");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                try{
                    cFDIRetencionv10.guardar(baos);
                    archivoComprobanteFiscalEnProceso = FileManage.createFileFromByteArrayOutputStream(baos, archivoComprobanteFiscalEnProceso);
                    if (!archivoComprobanteFiscalEnProceso.exists())
                        throw new Exception("Archivo En Proceso no existente.");
                }catch(Exception ex){
                    throw new Exception("No se pudo almacenar el archivo sellado localmente con CSD emisor en '"
                            + archivoComprobanteFiscalEnProceso.getAbsolutePath() + "' . " + ex.toString());
                }

                //Procesamos timbre en PAC
                WsGenericRespExt respWs = null;
                try{
                    ConexionPAC conexionPAC = new ConexionPAC(this.configuracionDto);
                    respWs = conexionPAC.timbraRetencionesPAC(baos.toByteArray());
                }catch(Exception ex){
                    //throw new Exception("Error al intentar timbrar Comprobante Fiscal con servicios PAC. " + ex.toString());
                    registraErrorConnPAC(ex);

                    //Eliminamos archivo temporal en proceso
                    for (int k=0; k<100; k++){
                        if (archivoComprobanteFiscalEnProceso.delete())
                            break;
                    }

                    //Actualizamos serie deshaciendo uso de ultimo folio
                    if (serie!=null){
                        serie.setUltimoFolio(serie.getUltimoFolio()-1);
                        serieDao.update(serie.createPk(), serie);
                    }

                    //Forzamos final de metodo
                    return false; 
                }

                //Procesamos respuesta de PAC
                if (respWs==null){
                    throw new Exception("Respuesta de servicio PAC con valor nulo. ");
                }else{
                    //Buscamos errores
                    if (respWs.isIsError()){
                        //Con errores

                        //intentamos eliminar XML temporal sellado localmente
                        try{
                            for (int k=0; k<100; k++){
                                if (archivoComprobanteFiscalEnProceso.delete())
                                    break;
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }

                        errorValidacionPAC = true;
                        throw new Exception("No se timbro el comprobante, respuesta del PAC a continuación. " + 
                                    respWs.getNumError() + " : " + respWs.getErrorMessage());
                    }else{
                        //Todo correcto en PAC

                        //Guardamos archivo XML
                        File archivoComprobanteFiscalExito = null;
                        try{ 
                            archivoComprobanteFiscalExito = UtilNombraArchivosBO.calcFileComprobanteFiscal(configuracionDto, 
                                                ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO, fileArchivoMaestro,
                                                null, retencionesDatos , respWs.getFolioUUID(), "xml");

                            archivoComprobanteFiscalExito = FileManage.createFileFromByteArray(respWs.getXML(), 
                                    FileManage.getParentPathString(archivoComprobanteFiscalExito) + File.separator , 
                                    archivoComprobanteFiscalExito.getName());

                            //asignamos dato a objeto DTO
                            if (archivoComprobanteFiscalExito!=null)
                                comprobanteFiscalDto.setNombrearchivoxml(archivoComprobanteFiscalExito.getName());
                        }catch(Exception ex){
                            throw new Exception("Error inesperado al crear archivo XML de Comprobante Fiscal timbrado." + ex.toString());
                        }

                        //intentamos eliminar XML temporal sellado localmente
                        try{
                            for (int k=0; k<100; k++){
                                if (archivoComprobanteFiscalEnProceso.delete())
                                    break;
                            }
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }

                        //Creamos PDF
                        File archivoComprobanteFiscalExitoPDF = null;
                        try{ 
                            archivoComprobanteFiscalExitoPDF = UtilNombraArchivosBO.calcFileComprobanteFiscal(configuracionDto, 
                                                ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO, fileArchivoMaestro,
                                                null, retencionesDatos, respWs.getFolioUUID(),  "pdf");

                            CfdRetenciones10BO cfdRetenciones10BO = new CfdRetenciones10BO(archivoComprobanteFiscalExito, CfdRetenciones10BO.CONTEXT_ARRAY_COMPLEMENTOS);
                            ByteArrayOutputStream baosPDF;

                            Accioncomprobante accionComprobanteDto = buscaAccionPersonalizada(retencionesDatos);
                            cfdRetenciones10BO.setRetencionesDatos(retencionesDatos);
                            cfdRetenciones10BO.setAccionComprobante(accionComprobanteDto);
                            baosPDF = cfdRetenciones10BO.toPdf();

                            archivoComprobanteFiscalExitoPDF = FileManage.createFileFromByteArray(baosPDF.toByteArray(),
                                     FileManage.getParentPathString(archivoComprobanteFiscalExitoPDF) + File.separator , 
                                     archivoComprobanteFiscalExitoPDF.getName());

                            //asignamos dato a objeto DTO
                            if (archivoComprobanteFiscalExitoPDF!=null){
                                comprobanteFiscalDto.setNombrearchivopdf(archivoComprobanteFiscalExitoPDF.getName());
                                if (generarPDFConcentrado)
                                    listaArchivosPDF.add(archivoComprobanteFiscalExitoPDF);
                            }
                        }catch(Exception ex){
                            String errorPDF = "Error inesperado al crear representacion impresa de Comprobante de Retenciones. " + GenericMethods.exceptionStackTraceToString(ex);
                            System.out.println(errorPDF);
                            String cuerpoCorreo = "Error al procesar PDF de comprobante Retenciones.<br/>";
                            cuerpoCorreo += "<br/><b>Fecha y Hora: </b>" + DateManage.dateTimeToStringEspanol(new Date());
                            cuerpoCorreo += "<br/><b>Nombre de archivo original: </b>" + StringManage.getValidString(fileArchivoMaestro.getName());
                            cuerpoCorreo += "<br/><b>Descripción del error: </b>";
                            cuerpoCorreo += "<br/><i>"  + errorPDF + "</i>";
                            listaMensajes.add(cuerpoCorreo);
                            actualizarLogVista(errorPDF);
                        }

                        //Completamos datos de registro BD
                        try{ 
                            llenaInfoComprobanteFiscalDto(comprobanteFiscalDto, (Retenciones) cFDIRetencionv10.getComprobante().getComprobante(), retencionesDatos, respWs, archivoComprobanteFiscalExito);
                        }catch(Exception ex){
                            throw new Exception("Error inesperado al almacenar información de Comprobante Fiscal generado. " + ex.toString());
                        }

                        // Si se tienen addendas, se agregan en este punto
                        /*
                        if (retencionesDatosBO.getRetencionesDatos().isTieneAddendas()){
                            archivoComprobanteFiscalExito = agregaAddendasDespuesDeTimbrado(archivoComprobanteFiscalExito, retencionesDatosBO);
                        }
                        */

                        //Acciones en forma asíncrona
                        try{
                            final File archivoComprobanteFiscalExito_Aux = archivoComprobanteFiscalExito;
                            final File archivoComprobanteFiscalExitoPDF_Aux = archivoComprobanteFiscalExitoPDF;
                            final Retenciones comp_Aux = comp;
                            final String uuid_Aux= respWs.getFolioUUID();
                            final int idTipoComprobante_Aux = idTipoComprobante;
                            final RetencionesDatos retencionesDatos_Aux = retencionesDatos;

                            Runnable r1 = new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        System.out.println("[Asíncrono] Ejecutando acciones para comprobante de retenciones. " + new Date());
                                        ejecutaAcciones(archivoComprobanteFiscalExito_Aux,archivoComprobanteFiscalExitoPDF_Aux, 
                                                comp_Aux, uuid_Aux, idTipoComprobante_Aux, retencionesDatos_Aux);
                                    } catch (Exception ex) {
                                        System.out.println("[Asíncrono] Error al intentar ejecutar acciones para comprobante de retenciones. " + ex.toString());
                                        ex.printStackTrace();
                                    }
                                }
                            };

                            Thread thr1 = new Thread(r1);
                            thr1.start();

                        }catch(Exception ex){
                            System.out.println("Error inesperado al ejecutar acciones para comprobante de retenciones timbrado de forma asíncrona. " + ex.toString());
                            ex.printStackTrace();
                        }


                    }
                }                                

            }else{
                throw new Exception("Certificado y/o Llave privada de emisor no se pudieron recuperar, revise la configuración del emisor.");
            }

            //Si llego a este punto, indica que el proceso del Comprobante de Retenciones
            // fue exitoso, se almacena el objeto y se suma uno al contador de procesados
            comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO);

            //Guardar en BD
            //Actualizamos registro Comprobantefiscal en BD
            compFiscalDaoImpl.update(comprobanteFiscalDto.createPk(),comprobanteFiscalDto);
            //Creamos registro Nomina en BD
            //guardaInfoNomina(facturaDatos, comprobanteFiscalDto.getIdcomprobantefiscal());


        }catch(Exception ex){
            //Error a nivel de elemento Comprobante Fiscal
            registraErrorArchivo(TipoArchivo.COMPROBANTE_FISCAL, ex);

            //Actualizamos registro Comprobantefiscal en BD (ERROR)
            if(comprobanteFiscalDto.getIdcomprobantefiscal()>0){
                comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_TIMBRAR_ERROR);
                compFiscalDaoImpl.update(comprobanteFiscalDto.createPk(),comprobanteFiscalDto);
            }

            //Actualizamos estatus en BD de Archivo Maestro (ERROR)
            if(archivoMaestroDto.getIdarchivomaestro()>0){
                //Actualizamos registro Comprobantefiscal en BD
                archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_ERROR);
                archivomaestroDaoImpl.update(archivoMaestroDto.createPk(),archivoMaestroDto);
            }

            //Actualizamos serie deshaciendo uso de ultimo folio
            if (serie!=null){
                serie.setUltimoFolio(serie.getUltimoFolio()-1);
                serieDao.update(serie.createPk(), serie);
            }
        }
        
        //Se termino de procesar el Archivo Maestro
                
        //Movemos archivo a destino carpeta Exito
        try{
            String rutaArchivoDestinoExito = UtilNombraArchivosBO.calcFileArchivoMaestro(configuracionDto, ArchivoMaestroBO.ESTATUS_EXITO, fileArchivoMaestro).getAbsolutePath();

            //Movemos archivo a carpeta 
            FileManage.moveFile(fileArchivoMaestro.getAbsolutePath(), rutaArchivoDestinoExito);

            //Actualizamos estatus en BD de Archivo Maestro (EXITO)
            if(archivoMaestroDto.getIdarchivomaestro()>0 ){
                //Actualizamos registro Comprobantefiscal en BD
                archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_EXITO);
                archivomaestroDaoImpl.update(archivoMaestroDto.createPk(),archivoMaestroDto);
            }
            return !errorValidacionPAC;

        }catch(Exception e){
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Método auxiliar para la generación correcta del objeto CFD
     * con los esquemas necesarios
     * @return 
     */
    private CFDIRetencionv10 creaComprobanteRetenciones(Retenciones comp) throws Exception{
        CFDIRetencionv10 cFDIRetencionv10 = new CFDIRetencionv10(comp, 
                        CfdRetenciones10BO.CONTEXT_ARRAY_COMPLEMENTOS);
        for (String[] namespacePrefix : CfdRetenciones10BO.NAMESPACE_AND_PREFIXES_COMPLEMENTOS){
            cFDIRetencionv10.addNamespace(namespacePrefix[0], namespacePrefix[1]);
        }
        
        return cFDIRetencionv10;
    }
    
    /**
     * Método auxiliar para la generación correcta del objeto CFD
     * con los esquemas necesarios
     * @return 
     */
    private CFDv33 creaCFD(Comprobante comp){
        CFDv33 cFDv33=null;
        try{
            cFDv33 = new CFDv33(comp, 
                            Cfd33BO.CONTEXT_ARRAY_COMPLEMENTOS);
            //cFDv32.addNamespace("http://www.sat.gob.mx/nomina", "nomina");
            //cFDv32.addNamespace("http://www.sat.gob.mx/implocal", "implocal");
            for (String[] namespacePrefix : Cfd33BO.NAMESPACE_AND_PREFIXES_COMPLEMENTOS){
                cFDv33.addNamespace(namespacePrefix[0], namespacePrefix[1]);
            }
        }catch(Exception e){
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return cFDv33;
    }
    
    private File agregaAddendasDespuesDeTimbrado(File archivoTimbrado, FacturaDatosBO facturaDatosBO){
        File archivoTimbradoConAddendas = archivoTimbrado;
        try{
            facturaDatosBO.setCompilarAddendas(true);
            Comprobante compAddendas = facturaDatosBO.compilarComprobante();

            if (compAddendas!=null && compAddendas.getAddenda()!=null){
                mx.bigdata.sat.addenda.sanofi.schema.Sanofi addendaSanofiV1 = null;
                mx.bigdata.sat.addenda.vwpmt.schema.Factura addendaVwPmtV1 = null;
                mx.bigdata.sat.addenda.chryslerpua.schema.Factura addendaChryslerPuaV1 = null;
                mx.bigdata.sat.addenda.gm.v13.schema.ADDENDAGM addendaGmV13  = null;
                mx.bigdata.sat.addenda.fordfom.schema.Addenda addendaFordV1  = null;
                mx.bigdata.sat.addenda.chryslerppy.schema.Factura addendaChryslerPpyV1 = null;

                java.util.List<Object> listAddendas = compAddendas.getAddenda().getAny();
                for (Object o : listAddendas) {

                    //Addenda Sanofi v1
                    if (o instanceof mx.bigdata.sat.addenda.sanofi.schema.Sanofi) {
                      addendaSanofiV1 = (mx.bigdata.sat.addenda.sanofi.schema.Sanofi) o;
                      System.out.println(" - SE ENCONTRO ADDENDA SANOFI V1");
                    }
                    
                    //Addenda VW PMT v1
                    if (o instanceof mx.bigdata.sat.addenda.vwpmt.schema.Factura) {
                      addendaVwPmtV1 = (mx.bigdata.sat.addenda.vwpmt.schema.Factura) o;
                      System.out.println(" - SE ENCONTRO ADDENDA VW PMT V1");
                    }
                    
                    //Addenda Chrysler PUA v1
                    if (o instanceof mx.bigdata.sat.addenda.chryslerpua.schema.Factura) {
                      addendaChryslerPuaV1 = (mx.bigdata.sat.addenda.chryslerpua.schema.Factura) o;
                      System.out.println(" - SE ENCONTRO ADDENDA CHRYSLER PUA V1");
                    }
                    
                    //Addenda GM v1.3
                    if (o instanceof mx.bigdata.sat.addenda.gm.v13.schema.ADDENDAGM) {
                      addendaGmV13 = (mx.bigdata.sat.addenda.gm.v13.schema.ADDENDAGM) o;
                      System.out.println(" - SE ENCONTRO ADDENDA GM v1.3");
                    }
                    
                    //Addenda Ford Fom v1
                    if (o instanceof mx.bigdata.sat.addenda.fordfom.schema.Addenda) {
                      addendaFordV1 = (mx.bigdata.sat.addenda.fordfom.schema.Addenda) o;
                      System.out.println(" - SE ENCONTRO ADDENDA FORD FOM V1");
                    }
                    
                    //Addenda Chrysler PPY v1
                    if (o instanceof mx.bigdata.sat.addenda.chryslerppy.schema.Factura) {
                      addendaChryslerPpyV1 = (mx.bigdata.sat.addenda.chryslerppy.schema.Factura) o;
                      System.out.println(" - SE ENCONTRO ADDENDA CHRYSLER PPY V1");
                    }

                    //Otras addendas
                    //...
                }

                //Hacemos parse/traduccion del Comprobante timbrado a un objeto DOM en java
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                Document documentFinal = dbf.newDocumentBuilder().parse(archivoTimbrado);
                XPathFactory xpf = XPathFactory.newInstance();
                XPath xpath = xpf.newXPath();

                //Nos aseguramos de primero conocer si nodo Addenda existente en cfdi timbrado aunque venga vacío
                XPathExpression expression = xpath.compile("/Comprobante/Addenda");
                Node nodeAddenda = (Node) expression.evaluate(documentFinal, XPathConstants.NODE);

                //si existe eliminamos el nodo de Addenda
                if (nodeAddenda!=null)
                    nodeAddenda.getParentNode().removeChild(nodeAddenda);
                //Lo agregamos en limpio
                nodeAddenda = DOMManage.createNode(documentFinal, "cfdi:Addenda", null);

                //Agregamos contenidos de addendas a nodo Addenda
                //Addenda sanofi v1
                Element domElementAddendaSanofiV1 = parseNodeAddendaSanofiV1(addendaSanofiV1);
                if (domElementAddendaSanofiV1!=null){
                    Node nodeAddendaSanofiV1Imported = documentFinal.importNode(domElementAddendaSanofiV1, true);
                    nodeAddenda.appendChild(nodeAddendaSanofiV1Imported);
                }
                //Addenda vw pmt v1
                Element domElementAddendaVwPmtV1 = parseNodeAddendaGeneric(addendaVwPmtV1,"http://www.vwnovedades.com/volkswagen/kanseilab/shcp/2009/Addenda/PMT","PMT",null);
                if (domElementAddendaVwPmtV1!=null){
                    Node nodeAddendaImported = documentFinal.importNode(domElementAddendaVwPmtV1, true);
                    nodeAddenda.appendChild(nodeAddendaImported);
                }
                //Addenda chrysler pua v1
                Element domElementAddendaChryslerPuaV1 = parseNodeAddendaGeneric(addendaChryslerPuaV1,null,null,null);
                if (domElementAddendaChryslerPuaV1!=null){
                    Node nodeAddendaImported = documentFinal.importNode(domElementAddendaChryslerPuaV1, true);
                    nodeAddenda.appendChild(nodeAddendaImported);
                }
                //Addenda GM v1.3
                Element domElementAddendaGMv13 = parseNodeAddendaGeneric(addendaGmV13,null,null,null);
                if (domElementAddendaGMv13!=null){
                    Node nodeAddendaImported = documentFinal.importNode(domElementAddendaGMv13, true);
                    nodeAddenda.appendChild(nodeAddendaImported);
                }
                //Addenda Ford Fom v1
                Element domElementAddendaFordFomV1 = parseNodeAddendaGeneric(addendaFordV1,"http://www.ford.com.mx/cfdi/addenda","fomadd","http://www.ford.com.mx/cfdi/addenda http://www.ford.com.mx/cfdi/addenda/cfdiAddendaFord_1_0.xsd");
                if (domElementAddendaFordFomV1!=null){
                    Node nodeAddendaFordFomV1Imported = documentFinal.importNode(domElementAddendaFordFomV1, true);
                    nodeAddenda.appendChild(nodeAddendaFordFomV1Imported);
                }
                
                //Addenda Chrysler PPY v1
                Element domElementAddendaChryslerPpyV1 = parseNodeAddendaGeneric(addendaChryslerPpyV1,null,null,null);
                if (domElementAddendaChryslerPpyV1!=null){
                    Node nodeAddendaImported = documentFinal.importNode(domElementAddendaChryslerPpyV1, true);
                    nodeAddenda.appendChild(nodeAddendaImported);
                }
                
                //Otras addendas
                //...

                //Agregamos nodo Addenda a Comprobante
                documentFinal.getFirstChild().appendChild(nodeAddenda);

                //Re-Escribimos archivo final
                archivoTimbradoConAddendas = FileManage.createFileFromString(DOMManage.documentToStringIndented(documentFinal, false), archivoTimbrado.getParentFile().getAbsolutePath()+File.separator, archivoTimbrado.getName());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return archivoTimbradoConAddendas;
    }
    
    private Element parseNodeAddendaSanofiV1(mx.bigdata.sat.addenda.sanofi.schema.Sanofi addendaSanofiV1){
        Element domElementAddendaSanofiV1 = null;
        try{
            if (addendaSanofiV1!=null){
                JAXBContext jc = JAXBContext.newInstance(mx.bigdata.sat.addenda.sanofi.schema.Sanofi.class);

                // Create the Document
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.newDocument();

                // Marshal the Object to a Document
                ImmutableMap<String, String> prefix_sanofi = 
                        ImmutableMap.of("http://www.w3.org/2001/XMLSchema-instance","xsi", 
                                        "https://mexico.sanofi.com/schemas", "sanofi");
                Marshaller m = jc.createMarshaller();
                m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                            new NamespacePrefixMapperImpl(prefix_sanofi));
                m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, 
                   "https://mexico.sanofi.com/schemas https://mexico.sanofi.com/schemas/sanofi.xsd");
                m.marshal(addendaSanofiV1, document);

                domElementAddendaSanofiV1 = document.getDocumentElement();

            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return domElementAddendaSanofiV1;
    }
    
    private Element parseNodeAddendaGeneric(Object addenda, String prefixURL, String prefixName, String schemaLocation){
        Element domElementAddenda = null;
        try{
            if (addenda!=null){
                JAXBContext jc = JAXBContext.newInstance(addenda.getClass());

                // Create the Document
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.newDocument();

                // Marshal the Object to a Document
                Marshaller m = jc.createMarshaller();
                
                if (prefixURL!=null && prefixName!=null){
                    ImmutableMap<String, String> prefix_addenda = 
                            ImmutableMap.of("http://www.w3.org/2001/XMLSchema-instance","xsi", 
                                            prefixURL, prefixName);
                    m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                                new NamespacePrefixMapperImpl(prefix_addenda));
                }
                m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                if (schemaLocation!=null)
                    m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);
                m.marshal(addenda, document);

                domElementAddenda = document.getDocumentElement();

            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return domElementAddenda;
    }
    
    public boolean verificaArchivoRepetido(Archivomaestro archivomaestroDto){
        boolean isRepetido = false;
        
        try{
            String filtroBusqueda = " AND IDESTATUS="+ArchivoMaestroBO.ESTATUS_EXITO;
            if (!StringManage.getValidString(archivomaestroDto.getMd5Checksum()).equals(""))
                filtroBusqueda += " AND MD5_CHECKSUM = '" + archivomaestroDto.getMd5Checksum() +"'";
            if (archivomaestroDto.getFechacreacion()!=null
                    && !StringManage.getValidString(archivomaestroDto.getNombrearchivo()).equals("")
                    && archivomaestroDto.getTamanoarchivo()>0){
                
                //p. ej: '2014-06-12 17:00:26.571'
                SimpleDateFormat formatFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                
                filtroBusqueda += " AND ( "
                        + " TAMANOARCHIVO=" + archivomaestroDto.getTamanoarchivo()
                        + " OR NOMBREARCHIVO='" + archivomaestroDto.getNombrearchivo() + "'"
                        + " OR FECHACREACION = '" + formatFecha.format(archivomaestroDto.getFechacreacion()) +"'"
                        + ")";
            }
            ArchivoMaestroBO archivoMaestroBO = new ArchivoMaestroBO(conn);
            Archivomaestro[] archivosEncontrados = archivoMaestroBO.findArchivomaestros(-1, 0, 0, 
                    filtroBusqueda );
            if (archivosEncontrados.length>0){
                isRepetido = true;
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return isRepetido;
    }
    
    protected void guardaInfoNomina(FacturaDatos facturaDatos, int idComprobantefiscal) throws Exception{
        if (facturaDatos!=null){
            if (facturaDatos.getNominaDatos()!=null){
                //Inicializamos objeto DTO
                com.fens.desktopMonitor.dto.Nomina nominaDto = new com.fens.desktopMonitor.dto.Nomina();
                
                //Comenzamos llenado de datos
                LineaDatosNomina lineaDatosNomina = facturaDatos.getNominaDatos().getLineaDatosNomina();
                mx.bigdata.sat.common.nomina12.schema.Nomina nominaComplemento = lineaDatosNomina.getDatosNomina().getNomina();
                
                List<mx.bigdata.sat.common.nomina12.schema.Nomina.Incapacidades.Incapacidad> listaIncapacidad = facturaDatos.getNominaDatos().getListaIncapacidades();
                List<mx.bigdata.sat.common.nomina12.schema.Nomina.Percepciones.Percepcion.HorasExtra> listaHrsExtra = facturaDatos.getNominaDatos().getListaHorasExtras();
                
                nominaDto.setIdcomprobantefiscal(idComprobantefiscal);
                nominaDto.setDepartamento(StringManage.getValidString(nominaComplemento.getReceptor().getDepartamento()));
                nominaDto.setFechaFinPago(nominaComplemento.getFechaFinalPago().toGregorianCalendar().getTime());
                nominaDto.setFechaInicialPago(nominaComplemento.getFechaInicialPago().toGregorianCalendar().getTime());
                nominaDto.setFechaPago(nominaComplemento.getFechaPago().toGregorianCalendar().getTime());
                nominaDto.setNombreEmpleado(StringManage.getValidString(facturaDatos.getLineaDatosCliente().getDatosReceptor().getReceptor().getNombre()));
                nominaDto.setNumDiasPagados((nominaComplemento.getNumDiasPagados()!=null ? nominaComplemento.getNumDiasPagados().doubleValue(): 0));
                nominaDto.setNumEmpleado(nominaComplemento.getReceptor().getNumEmpleado());
                nominaDto.setPuesto(StringManage.getValidString(nominaComplemento.getReceptor().getPuesto()));
                
                nominaDto.setTotalDeduccionExentas(lineaDatosNomina.getTotalDeduccionesExentas()!=null?lineaDatosNomina.getTotalDeduccionesExentas().doubleValue():0);
                nominaDto.setTotalDeduccionGravadas(lineaDatosNomina.getTotalDeduccionesGravadas()!=null?lineaDatosNomina.getTotalDeduccionesGravadas().doubleValue():0);
                nominaDto.setTotalPercepcionExentas(lineaDatosNomina.getTotalPercepcionesExentas()!=null?lineaDatosNomina.getTotalPercepcionesExentas().doubleValue():0);
                nominaDto.setTotalPercepcionGravadas(lineaDatosNomina.getTotalPercepcionesGravadas()!=null?lineaDatosNomina.getTotalPercepcionesGravadas().doubleValue():0);
                
                double totalDescuentoIncapacidad = 0;
                if (listaIncapacidad!=null && listaIncapacidad.size()>0){
                    for (mx.bigdata.sat.common.nomina12.schema.Nomina.Incapacidades.Incapacidad incapacidad : listaIncapacidad)
                        totalDescuentoIncapacidad += incapacidad.getImporteMonetario().setScale(4, RoundingMode.UP).doubleValue();
                }
                nominaDto.setTotalIncapacidadDescuento(totalDescuentoIncapacidad);
                
                double totalExtrasDoblesHoras = 0;
                double totalExtrasDoblesImporte = 0;
                double totalExtrasTriplesHoras = 0;
                double totalExtrasTriplesImporte = 0;
                if (listaHrsExtra!=null && listaHrsExtra.size()>0){
                    for (mx.bigdata.sat.common.nomina12.schema.Nomina.Percepciones.Percepcion.HorasExtra hrsExtra : listaHrsExtra){
                        if (hrsExtra.getTipoHoras().equals("Dobles")){
                            totalExtrasDoblesHoras += hrsExtra.getHorasExtra() ;
                            totalExtrasDoblesImporte += hrsExtra.getImportePagado().setScale(4, RoundingMode.UP).doubleValue();
                        }else if (hrsExtra.getTipoHoras().equals("Triples")){
                            totalExtrasTriplesHoras += hrsExtra.getHorasExtra() ;
                            totalExtrasTriplesImporte += hrsExtra.getImportePagado().setScale(4, RoundingMode.UP).doubleValue();
                        }
                    }
                }
                nominaDto.setTotalHrextraDobleHr(totalExtrasDoblesHoras);
                nominaDto.setTotalHrextraDobleImp(totalExtrasDoblesImporte);
                nominaDto.setTotalHrextraTripleHr(totalExtrasTriplesHoras);
                nominaDto.setTotalHrextraTripleImp(totalExtrasTriplesImporte);
                
                //Almacenamos en base de datos
                NominaDaoImpl nominaDaoImpl = new NominaDaoImpl(this.getConn());
                nominaDaoImpl.insert(nominaDto);
                
            }
        }
    }
    
    /**
     * Completar información del nodo emisor en el objeto correspondiente al XML
     * @param comp Comprobante a completar
     * @param emisorDto Datos del emisor
     * @param ubicacionFiscalDto Datos de la ubicacion fiscal, direccion de la matriz
     */
    protected void llenaInfoEmisor(Comprobante comp, Emisor emisorDto, Ubicacionfiscal ubicacionFiscalDto ){
        
        //Nombre emisor
        comp.getEmisor().setNombre(emisorDto.getRazonsocial());
        
        //Domicilio Matriz Emisor
        /*if (ubicacionFiscalDto!=null){
            TUbicacionFiscal tUbicacionFiscal = new TUbicacionFiscal();
            
            if (!StringManage.getValidString(ubicacionFiscalDto.getCalle()).equals(""))
                tUbicacionFiscal.setCalle(StringManage.getValidString(ubicacionFiscalDto.getCalle()));
            if (!StringManage.getValidString(ubicacionFiscalDto.getCodigopostal()).equals(""))
                tUbicacionFiscal.setCodigoPostal(StringManage.getValidString(ubicacionFiscalDto.getCodigopostal()));
            if (!StringManage.getValidString(ubicacionFiscalDto.getColonia()).equals(""))
                tUbicacionFiscal.setColonia(StringManage.getValidString(ubicacionFiscalDto.getColonia()));
            if (!StringManage.getValidString(ubicacionFiscalDto.getEstado()).equals(""))
                tUbicacionFiscal.setEstado(StringManage.getValidString(ubicacionFiscalDto.getEstado()));
            if (!StringManage.getValidString(ubicacionFiscalDto.getMunicipio()).equals(""))
                tUbicacionFiscal.setMunicipio(StringManage.getValidString(ubicacionFiscalDto.getMunicipio()));
            if (!StringManage.getValidString(ubicacionFiscalDto.getNumext()).equals(""))
                tUbicacionFiscal.setNoExterior(StringManage.getValidString(ubicacionFiscalDto.getNumext()));
            if (!StringManage.getValidString(ubicacionFiscalDto.getNumint()).equals(""))
                tUbicacionFiscal.setNoInterior(StringManage.getValidString(ubicacionFiscalDto.getNumint()));
            if (!StringManage.getValidString(ubicacionFiscalDto.getPais()).equals(""))
                tUbicacionFiscal.setPais(StringManage.getValidString(ubicacionFiscalDto.getPais()));
            
            comp.getEmisor().setDomicilioFiscal(tUbicacionFiscal);
        }*/
        
        //Regimen Fiscal
            /*Comprobante.Emisor.RegimenFiscal regimen = new Comprobante.Emisor.RegimenFiscal();
            regimen.setRegimen(emisorDto.getRegimenfiscal());*/
        comp.getEmisor().setRegimenFiscal(emisorDto.getRegimenfiscal());
        
    }
    
    /**
     * Completa la informacion de un objeto DTO Archivo Maestro
     * con la informacion que se puede obtener de un objeto File
     * @param archivoMaestroDto
     * @param archivoFuente
     * @throws IOException 
     */
    protected void llenaInfoArchivoMaestro(Archivomaestro archivoMaestroDto, File archivoFuente) throws Exception{
        Path path = Paths.get(archivoFuente.getAbsolutePath());        
        BasicFileAttributes bfa = Files.readAttributes(path, BasicFileAttributes.class);
        Date fechaCreacion = new Date(bfa.creationTime().toMillis());
        String md5Checksum = FileManage.getMD5ChecksumFromFile(archivoFuente);
        
        archivoMaestroDto.setFechacreacion(fechaCreacion);
        archivoMaestroDto.setFechaproceso(new Date());
        archivoMaestroDto.setNombrearchivo(archivoFuente.getName());
        archivoMaestroDto.setTamanoarchivo(bfa.size());
        archivoMaestroDto.setMd5Checksum(md5Checksum);
    }
    
    protected void llenaInfoComprobanteFiscalDto(
            Comprobantefiscal comprobanteFiscalDto, 
            Comprobante comp,
            FacturaDatos facturaDatos,
            WsGenericResp respuestaWsPAC, 
            File archivoComprobante){
        
        comprobanteFiscalDto.setCadenaoriginal(respuestaWsPAC.getCadenaOriginal());
        comprobanteFiscalDto.setFechahorasellado(comp.getFecha().toGregorianCalendar().getTime());
        comprobanteFiscalDto.setFechahoratimbrado(respuestaWsPAC.getFechaHoraTimbrado().toGregorianCalendar().getTime());
        comprobanteFiscalDto.setFolio(comp.getFolio());
        comprobanteFiscalDto.setMoneda(String.valueOf(comp.getMoneda()));
        comprobanteFiscalDto.setRfcreceptor(comp.getReceptor().getRfc());
        comprobanteFiscalDto.setSelloemisor(comp.getSello());
        comprobanteFiscalDto.setSerie(comp.getSerie());
        comprobanteFiscalDto.setSubtotal(comp.getSubTotal().doubleValue());
        comprobanteFiscalDto.setTotal(comp.getTotal().doubleValue());
        comprobanteFiscalDto.setUuid(respuestaWsPAC.getFolioUUID());
        
        if (facturaDatos.getLineaDatosAdicionales()!=null){
            if (facturaDatos.getLineaDatosAdicionales().getCamposAdicionales()!=null){
                if (facturaDatos.getLineaDatosAdicionales().getCamposAdicionales().size()>0){
                    String datosAdicionales = "";
                    for (String dato : facturaDatos.getLineaDatosAdicionales().getCamposAdicionales()){
                        datosAdicionales += dato + "|";
                    }
                    if (datosAdicionales.length()>=300){
                        datosAdicionales = datosAdicionales.substring(0, 299);
                    }
                    comprobanteFiscalDto.setObservaciones(datosAdicionales);
                }
            }
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
            
        if (!gc.isValidDirectory(new File(configuracionDto.getRutacarpetaejecucion())))
            throw new Exception("El directorio de Proceso Temporal no existe o no ha sido especificado.");
        
        if (!gc.isValidDirectory(new File(configuracionDto.getRutacarpetaexitosos())))
            throw new Exception("El directorio de Exito no existe o no ha sido especificado.");
        
        if (!gc.isValidDirectory(new File(configuracionDto.getRutacarpetaerrores())))
            throw new Exception("El directorio de Error no existe o no ha sido especificado.");
        
    }
    
    protected void registraErrorArchivo(TipoArchivo tipoArchivo, Exception ex){
        //Creamos archivo de Log
        try{
            String tituloError = "\n---ERROR Procesando " + (tipoArchivo == TipoArchivo.ARCHIVO_MAESTRO?"Archivo Maestro":"Comprobante Fiscal") + ". ";
            System.out.println(tituloError + "\n" + ex.getMessage());
            FileManage.createFileFromString(tituloError + "\n" + ex.getMessage()
                    + "\n" +  GenericMethods.exceptionStackTraceToString(ex) ,
                    configuracionDto.getRutacarpetaerrores()+File.separator, 
                    archivoMaestroDto.getNombrearchivo()+"_LOG_ERROR.txt");
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //Enviamos info de archivo con error
        try{
            List<String> listaDestinatarios = GenericMethods.getListaCorreosValidos(configuracionDto.getDestinatariosfijos(), ";");
            if (listaDestinatarios.size()>0)
                enviarCorreoErrorArchivo(listaDestinatarios, ex.getMessage(), tipoArchivo, archivoMaestroDto.getNombrearchivo());
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
    
    protected void registraErrorConnPAC(Exception ex){
        this.errorConexionPAC = true;
        
        //Creamos archivo de Log
        try{
            String tituloError = "\nError al intentar timbrar Comprobante Fiscal con servicios PAC. "
                    + "\n(No es necesario que vuelva a depositar el archivo, en automático se volvió a "
                    + "depositar en el repositorio de Entrada para la próxima ejecución del cron). \n\n"
                    + ex.toString();
            System.out.println(tituloError);
            FileManage.createFileFromString(tituloError,
                    configuracionDto.getRutacarpetaerrores()+File.separator + Configuration.nombreDirErroresPAC  +File.separator, 
                    archivoMaestroDto.getNombrearchivo()+"_LOG_CONN_PAC_ERROR.txt");
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //Enviamos info de archivo con error
        try{
            List<String> listaDestinatarios = GenericMethods.getListaCorreosValidos(configuracionDto.getDestinatariosfijos(), ";");
            if (listaDestinatarios.size()>0)
                enviarCorreoErrorConexionPAC(listaDestinatarios, ex.toString(), archivoMaestroDto.getNombrearchivo());
        }catch(Exception e){
            e.printStackTrace();
        }
        //Movemos archivo a destino carpeta Errores PAC
        // y tambien de regreso a la carpeta IN
        try{
            String rutaArchivoDestinoError = configuracionDto.getRutacarpetaerrores() 
                    + File.separator + Configuration.nombreDirErroresPAC
                    + File.separator + fileArchivoMaestro.getName();
            String rutaArchivoIn = configuracionDto.getRutaorigenprocesar()
                    + File.separator + fileArchivoMaestro.getName();
            
            //Copiamos archivo a carpeta IN (para que se vuelva a procesar)
            FileManage.copyFile(fileArchivoMaestro.getAbsolutePath(), rutaArchivoIn);
            //Movemos archivo a carpeta de error para que quede registro
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
            cuerpoCorreo += "<br/><b>Tipo archivo: </b>" + (tipoArchivo==TipoArchivo.ARCHIVO_MAESTRO ? "Archivo Maestro": "Comprobante Fiscal" );
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
    
    protected void enviarCorreoErrorConexionPAC(List<String> listaDestinatarios,
            String errorMsg, String nombreArchivoOriginal) throws Exception{
        
        if (listaDestinatarios.size()>0){
            MailBO mailBO  = inicializarMail(listaDestinatarios, null, null, null);

            String cuerpoCorreo = "Error en comunicación con servicios PAC.<br/>";
            cuerpoCorreo += "<br/><b>Proceso: </b>Timbrado.";
            cuerpoCorreo += "<br/><b>Fecha y Hora: </b>" + DateManage.dateTimeToStringEspanol(new Date());
            cuerpoCorreo += "<br/><b>Nombre de archivo original: </b>" + StringManage.getValidString(nombreArchivoOriginal);
            cuerpoCorreo += "<br/><b>Descripción del error: </b>";
            cuerpoCorreo += "<br/><i>"  + errorMsg + "</i>";

            cuerpoCorreo += "</br></br> (No es necesario que vuelva a depositar el archivo, en automático se volvió a "
                        + "depositar en el repositorio de Entrada para la próxima ejecución del cron). ";

            listaMensajes.add(cuerpoCorreo);
            mailBO.addMessagePlantilla(cuerpoCorreo);
            int destinatariosTotal = mailBO.getToQuantity() + mailBO.getCcQuantity();
            if (destinatariosTotal>0 && !enviarCorreosUnicos)
                mailBO.send(StringManage.getValidString(configuracionDto.getAsuntocorreo()) + " - Error comunicación con servicios PAC");
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
            cuerpoCorreo += "<br/><b><i>Fecha sellado</i></b> : " + DateManage.dateTimeToStringEspanol(comp.getFecha().toGregorianCalendar().getTime()) ;
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
    
    public void imprimirPdf(File filePdf, int idTipoComprobante,
            String nombreImpresora) throws Exception{
        //Revisamos si en la configuración se decidio activar impresion automática
        //ademas que el archivo no se un valor nulo, y la impresora tenga un nombre
        if (configuracionDto.getActivarimpresion()==1
                && gc.isValidFile(filePdf)
                && !StringManage.getValidString(nombreImpresora).equals("") ){
            
            ImpresionJavaPDFBox impresionJavaPDFBox = new ImpresionJavaPDFBox(filePdf, nombreImpresora);
            
            int numeroCopias = 1;
            
            /*
            if (idTipoComprobante==ComprobanteFiscalBO.TIPO_COMPROBANTE_NOMINA){
                numeroCopias = 2;
            }*/
            
            //Numero de copias
            impresionJavaPDFBox.setCopias(numeroCopias);
            
            //Ejecutamos impresión silenciosa
            impresionJavaPDFBox.printSilent2();
            
            actualizarLogVista("\t\t * Impresion de PDF " + FileManage.getFileNameWithoutExtension(filePdf.getName())
                                                    + " - " + new Date());
            
        }
        
    }
    
    public void imprimirPdfViaAdobeReader(File filePdf, int idTipoComprobante,
            String nombreImpresora) throws Exception{
        //Revisamos si en la configuración se decidio activar impresion automática
        //ademas que el archivo no se un valor nulo, y la impresora tenga un nombre
        if (configuracionDto.getActivarimpresion()==1
                && gc.isValidFile(filePdf)
                && !StringManage.getValidString(nombreImpresora).equals("") ){
            
            //C:/Program Files (x86)/Adobe/Reader 11.0/Reader/AcroRd32.exe  /t "C:\\Users\\ISCesar\\Downloads\\PasedeAbordar.pdf" "Microsoft XPS Document Writer"
            String comando = configuracionDto.getRutaEjecutableAdobeReader() + " /t \"" + filePdf.getAbsolutePath() + "\" \"" + nombreImpresora + "\"";
            Process p=Runtime.getRuntime().exec(comando); 
            
            actualizarLogVista("\t\t * Impresion de PDF (vía Adobe Reader) " + FileManage.getFileNameWithoutExtension(filePdf.getName())
                                    + " - " + new Date()
                                    + "\n\t\t Comando ejecutado: " + comando);
            
        }
        
    }

    public void ejecutaAcciones(File fileXML, File filePDF, Comprobante comp, 
            String UUID, int idTipoComprobante,FacturaDatos facturaDatos) throws Exception{
        
        boolean notificarCorreo = false;
        try{
            notificarCorreo = facturaDatos.getLineaDatosCliente().getDatosReceptor().isNotificar();
        }catch(Exception ex){}
            
        //---------------------------------------------
        //Acciones personalizadas
        {                
                Accioncomprobante accionComprobanteDto = buscaAccionPersonalizada(facturaDatos);
                
                //Accion existente
                if (accionComprobanteDto!=null){
                    accionComprobanteConcentrado = accionComprobanteDto;
                    
                    actualizarLogVista("\t ++ Accion personalizada '" + accionComprobanteDto.getClaveAccion() + "' para comprobante " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                        + " - " + new Date());
                    
                    //Envio de Correo (Accion personalizada)
                    try {
                        if (accionComprobanteDto.getActivarCorreo()== 1
                                && notificarCorreo) {
                            List<String> listaDestinatario = new ArrayList<String>();
                            if (gc.isEmail(facturaDatos.getLineaDatosCliente().getDatosReceptor().getEmail()))
                                listaDestinatario.add(facturaDatos.getLineaDatosCliente().getDatosReceptor().getEmail());
                            
                            List<String> listaCC = GenericMethods.getListaCorreosValidos(accionComprobanteDto.getCorreoDestinatarios(), ";");
                            enviarCorreoComprobanteFiscalReceptor(listaDestinatario, listaCC, fileXML, 
                                    filePDF, comp, UUID, idTipoComprobante);
                        }
                    } catch (Exception ex) {
                        System.out.println("Error Acción personalizada '" + accionComprobanteDto.getClaveAccion() + "' al enviar Correo. " + ex.toString());
                        actualizarLogVista("\t ! Error Accion personalizada '" + accionComprobanteDto.getClaveAccion() + "' al enviar Correo para comprobante " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                       + "\n\t" + ex.toString() + " - " + new Date());
                    }
                    
                    //Impresion (Accion personalizada)
                    try {
                        if (accionComprobanteDto.getActivarImpresion()== 1) {
                            //Enviamos a imprimir documento
                            if (accionComprobanteDto.getActivarSoporteAdobe()==1
                                    && StringManage.getValidString(configuracionDto.getRutaEjecutableAdobeReader()).length()>0){
                                
                                //Impresión asistida por Adobe Reader (aplicación --> adobe reader --> impresora física)
                                if (!generarPDFConcentrado)
                                    imprimirPdfViaAdobeReader(filePDF, idTipoComprobante, accionComprobanteDto.getNombreImpresora());
                            }else{
                                //Impresion directa (aplicación --> impresora física)
                                if (!generarPDFConcentrado)
                                    imprimirPdf(filePDF, idTipoComprobante, accionComprobanteDto.getNombreImpresora());
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("Error Acción personalizada '" + accionComprobanteDto.getClaveAccion() + "' al imprimir Archivo PDF. " + ex.toString());
                        actualizarLogVista("\t ! Error Accion personalizada '" + accionComprobanteDto.getClaveAccion() + "' al imprimir Archivo PDF para comprobante " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                       + "\n\t" + ex.toString() + " - " + new Date());
                    }
                    
                    //Copia de archivos (Accion personalizada)
                    try{
                        if (accionComprobanteDto.getActivarCopiaArchivo()==1){
                            File dirCopia = new File(accionComprobanteDto.getRutaCopiaArchivo());
                            if (gc.isValidDirectory(dirCopia)){
                                /*
                                FileManage.copyFile(filePDF.getAbsolutePath(), 
                                        dirCopia.getAbsolutePath() + File.separator + filePDF.getName());
                                FileManage.copyFile(fileXML.getAbsolutePath(), 
                                        dirCopia.getAbsolutePath() + File.separator + fileXML.getName());*/
                                FileManage.copyFile2(fileXML, 
                                        new File(dirCopia.getAbsolutePath() + File.separator + fileXML.getName()));
                                FileManage.copyFile2(filePDF, 
                                        new File(dirCopia.getAbsolutePath() + File.separator + filePDF.getName()));
                                
                                actualizarLogVista("\t\t * Copia de archivos " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                                    + " - " + new Date());
                                
                            }else{
                                throw new Exception("El directorio para la copia de archivos no existe.");
                            }
                        }
                    }catch(Exception ex){
                        System.out.println("Error Acción personalizada '" + accionComprobanteDto.getClaveAccion() + "' al copiar Archivos. " + ex.toString());
                        actualizarLogVista("\t ! Error Accion personalizada '" + accionComprobanteDto.getClaveAccion() + "' al copiar Archivos para comprobante " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                       + "\n\t" + ex.toString() + " - " + new Date());
                    }
                    
                    //Terminamos método, por que este documento tenia Acciones personalizadas
                    // y no se ejecutaran las básicas
                    return;
                    
                }
                
            
        }
        
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

            //Enviamos a imprimir documento
            try{
                if (!generarPDFConcentrado)
                    imprimirPdf(filePDF, idTipoComprobante, configuracionDto.getImpresorapredeterminada());
            }catch(Exception ex){
                throw new Exception("Error inesperado al imprimir Comprobante Fiscal generado. " + ex.toString());
            }
        }
        
    }
    
    /**
     * Efectua la busqueda en base de datos para un objeto FacturaDatos
     * en espera de encontrar una accionPersonalizada que le corresponda
     * @param facturaDatos
     * @return 
     */
    protected Accioncomprobante buscaAccionPersonalizada(FacturaDatos facturaDatos){
        Accioncomprobante accionComprobanteDto = null;
    
        try{
            String claveAccion = "";
            String codigoPostalSucursal = "";
            if (facturaDatos.getLineaDatosAccionPersonalizada()!=null){
                claveAccion = facturaDatos.getLineaDatosAccionPersonalizada().getClaveAccion();
            }
            
            /*if (facturaDatos.getExpedidoEn()!=null){
                codigoPostalSucursal = facturaDatos.getExpedidoEn().getCodigoPostal();
            }*/
            
            //Si al menos contamos con un CodigoPostal de sucursal (ExpedidoEn)
            // o una Clave de Acción, buscamos el registro
            if (!StringManage.getValidString(claveAccion).equals("")
                    || !StringManage.getValidString(codigoPostalSucursal).equals("")){
                
                AccionComprobanteBO accionComprobanteBO = new AccionComprobanteBO(conn);
                accionComprobanteDto = accionComprobanteBO.findUniqueAccionByClaveOrCP(claveAccion, codigoPostalSucursal);
                
                if (accionComprobanteDto==null
                        && !StringManage.getValidString(claveAccion).equals("")){
                    actualizarLogVista("Acción personalizada '" + claveAccion + "' no existe en Base de datos Local.");
                }
                
            }
        }catch(Exception ex){
                actualizarLogVista("Error al recuperar Acción personalizada: " + ex.toString());
        }
        return accionComprobanteDto;
    }
    
    public void ejecutaAcciones(File fileXML, File filePDF, Retenciones comp, 
            String UUID, int idTipoComprobante,RetencionesDatos retencionesDatos) throws Exception{
        
        boolean notificarCorreo = false;
        try{
            notificarCorreo = retencionesDatos.getLineaDatosComprobante().isNotificar();
        }catch(Exception ex){}
            
        //---------------------------------------------
        //Acciones personalizadas
        {                
                Accioncomprobante accionComprobanteDto = buscaAccionPersonalizada(retencionesDatos);
                
                //Accion existente
                if (accionComprobanteDto!=null){
                    accionComprobanteConcentrado = accionComprobanteDto;
                    
                    actualizarLogVista("\t ++ Accion personalizada '" + accionComprobanteDto.getClaveAccion() + "' para comprobante " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                        + " - " + new Date());
                    
                    //Envio de Correo (Accion personalizada)
                    try {
                        if (accionComprobanteDto.getActivarCorreo()== 1
                                && notificarCorreo) {
                            List<String> listaDestinatario = new ArrayList<String>();
                            if (gc.isEmail(retencionesDatos.getLineaDatosReceptor().getEmail()))
                                listaDestinatario.add(retencionesDatos.getLineaDatosReceptor().getEmail());
                            
                            List<String> listaCC = GenericMethods.getListaCorreosValidos(accionComprobanteDto.getCorreoDestinatarios(), ";");
                            enviarCorreoComprobanteRetencionesReceptor(listaDestinatario, listaCC, fileXML, 
                                    filePDF, comp, UUID, idTipoComprobante);
                        }
                    } catch (Exception ex) {
                        System.out.println("Error Acción personalizada '" + accionComprobanteDto.getClaveAccion() + "' al enviar Correo. " + ex.toString());
                        actualizarLogVista("\t ! Error Accion personalizada '" + accionComprobanteDto.getClaveAccion() + "' al enviar Correo para comprobante " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                       + "\n\t" + ex.toString() + " - " + new Date());
                    }
                    
                    //Impresion (Accion personalizada)
                    try {
                        if (accionComprobanteDto.getActivarImpresion()== 1) {
                            //Enviamos a imprimir documento
                            if (accionComprobanteDto.getActivarSoporteAdobe()==1
                                    && StringManage.getValidString(configuracionDto.getRutaEjecutableAdobeReader()).length()>0){
                                
                                //Impresión asistida por Adobe Reader (aplicación --> adobe reader --> impresora física)
                                if (!generarPDFConcentrado)
                                    imprimirPdfViaAdobeReader(filePDF, idTipoComprobante, accionComprobanteDto.getNombreImpresora());
                            }else{
                                //Impresion directa (aplicación --> impresora física)
                                if (!generarPDFConcentrado)
                                    imprimirPdf(filePDF, idTipoComprobante, accionComprobanteDto.getNombreImpresora());
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("Error Acción personalizada '" + accionComprobanteDto.getClaveAccion() + "' al imprimir Archivo PDF. " + ex.toString());
                        actualizarLogVista("\t ! Error Accion personalizada '" + accionComprobanteDto.getClaveAccion() + "' al imprimir Archivo PDF para comprobante " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                       + "\n\t" + ex.toString() + " - " + new Date());
                    }
                    
                    //Copia de archivos (Accion personalizada)
                    try{
                        if (accionComprobanteDto.getActivarCopiaArchivo()==1){
                            File dirCopia = new File(accionComprobanteDto.getRutaCopiaArchivo());
                            if (gc.isValidDirectory(dirCopia)){
                                FileManage.copyFile2(fileXML, 
                                        new File(dirCopia.getAbsolutePath() + File.separator + fileXML.getName()));
                                FileManage.copyFile2(filePDF, 
                                        new File(dirCopia.getAbsolutePath() + File.separator + filePDF.getName()));
                                
                                actualizarLogVista("\t\t * Copia de archivos " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                                    + " - " + new Date());
                                
                            }else{
                                throw new Exception("El directorio para la copia de archivos no existe.");
                            }
                        }
                    }catch(Exception ex){
                        System.out.println("Error Acción personalizada '" + accionComprobanteDto.getClaveAccion() + "' al copiar Archivos. " + ex.toString());
                        actualizarLogVista("\t ! Error Accion personalizada '" + accionComprobanteDto.getClaveAccion() + "' al copiar Archivos para comprobante " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                       + "\n\t" + ex.toString() + " - " + new Date());
                    }
                    
                    //Terminamos método, por que este documento tenia Acciones personalizadas
                    // y no se ejecutaran las básicas
                    return;
                    
                }
                
            
        }
        
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
                    if (gc.isEmail(retencionesDatos.getLineaDatosReceptor().getEmail()))
                        listaDestinatario.add(retencionesDatos.getLineaDatosReceptor().getEmail());

                    List<String> listaCC = GenericMethods.getListaCorreosValidos(configuracionDto.getDestinatariosfijos(), ";");
                    enviarCorreoComprobanteRetencionesReceptor(listaDestinatario, listaCC, fileXML, 
                            filePDF, comp, UUID, idTipoComprobante);
                }
            }catch(Exception ex){
                throw new Exception("Error inesperado al enviar correo con Comprobante Fiscal generado a receptor. " + ex.toString());
            }

            //Enviamos a imprimir documento
            try{
                if (!generarPDFConcentrado)
                    imprimirPdf(filePDF, idTipoComprobante, configuracionDto.getImpresorapredeterminada());
            }catch(Exception ex){
                throw new Exception("Error inesperado al imprimir Comprobante Fiscal generado. " + ex.toString());
            }
        }
        
    }
    
    /**
     * Efectua la busqueda en base de datos para un objeto FacturaDatos
     * en espera de encontrar una accionPersonalizada que le corresponda
     * @param facturaDatos
     * @return 
     */
    protected Accioncomprobante buscaAccionPersonalizada(RetencionesDatos retencionesDatos){
        Accioncomprobante accionComprobanteDto = null;
    
        try{
            String claveAccion = "";
            if (retencionesDatos.getLineaDatosAccionPersonalizada()!=null){
                claveAccion = retencionesDatos.getLineaDatosAccionPersonalizada().getClaveAccion();
            }
            
            //Si al menos contamos con un CodigoPostal de sucursal (ExpedidoEn)
            // o una Clave de Acción, buscamos el registro
            if (!StringManage.getValidString(claveAccion).equals("")){
                
                AccionComprobanteBO accionComprobanteBO = new AccionComprobanteBO(conn);
                accionComprobanteDto = accionComprobanteBO.findUniqueAccionByClaveOrCP(claveAccion, "");
                
                if (accionComprobanteDto==null
                        && !StringManage.getValidString(claveAccion).equals("")){
                    actualizarLogVista("Acción personalizada '" + claveAccion + "' no existe en Base de datos Local.");
                }
                
            }
        }catch(Exception ex){
                actualizarLogVista("Error al recuperar Acción personalizada: " + ex.toString());
        }
        return accionComprobanteDto;
    }
    
    protected void enviarCorreoComprobanteRetencionesReceptor(List<String> listaDestinatarios, List<String> listaCC,
            File fileXML, File filePDF, Retenciones comp, String UUID, int idTipoComprobante) throws Exception{
        
        if (configuracionDto.getEnviodecorreo()==1 && 
                (listaDestinatarios.size()>0 || listaCC.size()>0) ){
            //List<String> listaCC = GenericMethods.getListaCorreosValidos(configuracionDto.getDestinatariosfijos(), ";");

            List<File> archivosAdjuntos = new ArrayList<File>(Arrays.asList(fileXML, filePDF));
            MailBO mailBO = inicializarMail(listaDestinatarios, listaCC, null, archivosAdjuntos);
            
            String rfcReceptor = "";
            String nombreReceptor = "";
            if (comp.getReceptor().getNacional()!=null){
                rfcReceptor = comp.getReceptor().getNacional().getRFCRecep();
                nombreReceptor = comp.getReceptor().getNacional().getNomDenRazSocR();
            }else if (comp.getReceptor().getExtranjero()!=null){
                rfcReceptor = "XEXX010101000";
                nombreReceptor = comp.getReceptor().getExtranjero().getNomDenRazSocR();
            }

            String cuerpoCorreo = "Comprobante de Retenciones generado.<br/>";
            cuerpoCorreo += "<br/><b><i>Tipo Comprobante</i></b> : " + (idTipoComprobante==ComprobanteFiscalBO.TIPO_COMPROBANTE_RETENCIONES ? "Retenciones": "" );
            cuerpoCorreo += "<br/><b><i>Emisor</i></b> : " + StringManage.getValidString(comp.getEmisor().getRFCEmisor()) + " - "  + StringManage.getValidString(comp.getEmisor().getNomDenRazSocE());
            cuerpoCorreo += "<br/><b><i>Receptor</i></b> : " + StringManage.getValidString(rfcReceptor) + " - "  + StringManage.getValidString(nombreReceptor);
            cuerpoCorreo += "<br/><b><i>Folio Fiscal</i></b> : " + StringManage.getValidString(UUID);
            cuerpoCorreo += "<br/><b><i>Folio interno</i></b> : " + StringManage.getValidString(comp.getFolioInt()) ;
            cuerpoCorreo += "<br/><b><i>Fecha sellado</i></b> : " + DateManage.dateTimeToStringEspanol(comp.getFechaExp()) ;
            mailBO.addMessagePlantilla(cuerpoCorreo);

            int destinatariosTotal = mailBO.getToQuantity() + mailBO.getCcQuantity();
            if (destinatariosTotal>0){
                mailBO.send(StringManage.getValidString(configuracionDto.getAsuntocorreo()) + " - Comprobante Retenciones " + StringManage.getValidString(UUID));
                
                actualizarLogVista("\t\t * Envio por correo " + FileManage.getFileNameWithoutExtension(fileXML.getName())
                                                    + " - " + new Date());
            }
            
        }
    }
    
    protected void llenaInfoComprobanteFiscalDto(
            Comprobantefiscal comprobanteFiscalDto, 
            Retenciones comp,
            RetencionesDatos retencionesDatos,
            WsGenericRespExt respuestaWsPAC, 
            File archivoComprobante){
        
        String rfcReceptor = "";
        if (comp.getReceptor().getNacional()!=null){
            rfcReceptor = comp.getReceptor().getNacional().getRFCRecep();
        }else if (comp.getReceptor().getExtranjero()!=null){
            rfcReceptor = "XEXX010101000";
        }
            
        comprobanteFiscalDto.setCadenaoriginal(respuestaWsPAC.getCadenaOriginal());
        comprobanteFiscalDto.setFechahorasellado(comp.getFechaExp());
        comprobanteFiscalDto.setFechahoratimbrado(respuestaWsPAC.getFechaHoraTimbrado().toGregorianCalendar().getTime());
        comprobanteFiscalDto.setFolio(comp.getFolioInt());
        comprobanteFiscalDto.setMoneda("DESCONOCIDO");//comp.getMoneda());
        comprobanteFiscalDto.setRfcreceptor(rfcReceptor);
        comprobanteFiscalDto.setSelloemisor(comp.getSello());
        //comprobanteFiscalDto.setSerie();
        //comprobanteFiscalDto.setSubtotal(comp.getTotales().getMontoTotRet());
        comprobanteFiscalDto.setTotal(comp.getTotales().getMontoTotRet().doubleValue());
        comprobanteFiscalDto.setUuid(respuestaWsPAC.getFolioUUID());
        
        if (retencionesDatos.getLineaDatosAdicionales()!=null){
            if (retencionesDatos.getLineaDatosAdicionales().getCamposAdicionales()!=null){
                if (retencionesDatos.getLineaDatosAdicionales().getCamposAdicionales().size()>0){
                    String datosAdicionales = "";
                    for (String dato : retencionesDatos.getLineaDatosAdicionales().getCamposAdicionales()){
                        datosAdicionales += dato + "|";
                    }
                    if (datosAdicionales.length()>=300){
                        datosAdicionales = datosAdicionales.substring(0, 299);
                    }
                    comprobanteFiscalDto.setObservaciones(datosAdicionales);
                }
            }
        }
    }
    
    protected void crearComprobanteSectorPrimario(Comprobante comp){
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Emisor emisor=comp.getEmisor();
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Receptor receptor=comp.getReceptor();

        mx.bigdata.sat.cfdi.v33.schema.ObjectFactory of=new mx.bigdata.sat.cfdi.v33.schema.ObjectFactory();
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Emisor emisorTemp=of.createComprobanteEmisor();
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Receptor receptorTemp=of.createComprobanteReceptor();

        emisorTemp.setNombre(receptor.getNombre());
        emisorTemp.setRegimenFiscal("622");
        emisorTemp.setRfc(receptor.getRfc());

        receptorTemp.setNombre(emisor.getNombre());
        receptorTemp.setUsoCFDI(receptor.getUsoCFDI());
        receptorTemp.setNumRegIdTrib(receptor.getNumRegIdTrib());
        receptorTemp.setRfc(emisor.getRfc());
        receptorTemp.setResidenciaFiscal(receptor.getResidenciaFiscal());

        comp.setEmisor(emisorTemp);
        comp.setReceptor(receptorTemp);

    }
    
    protected void actualizarLogVista(String mensaje){
        //Si tenemos conexion a la vista, actualizamos el log
        if (wPrincipal!=null){
            int maxlines = 10000;
            if (wPrincipal.txtLogTemporal.getText().length()>maxlines){
                wPrincipal.txtLogTemporal.replaceRange(null, 0, maxlines);
            }
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
        ARCHIVO_MAESTRO,
        COMPROBANTE_FISCAL
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

    public Archivomaestro getArchivoMaestroDto() {
        return archivoMaestroDto;
    }

    public void setArchivoMaestroDto(Archivomaestro archivoMaestroDto) {
        this.archivoMaestroDto = archivoMaestroDto;
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

    public List<File> getListaArchivosPDF() {
        return listaArchivosPDF;
    }

    public void setListaArchivosPDF(List<File> listaArchivosPDF) {
        this.listaArchivosPDF = listaArchivosPDF;
    }

    public boolean isGenerarPDFConcentrado() {
        return generarPDFConcentrado;
    }

    public void setGenerarPDFConcentrado(boolean generarPDFConcentrado) {
        this.generarPDFConcentrado = generarPDFConcentrado;
    }    

    public Accioncomprobante getAccionComprobanteConcentrado() {
        return accionComprobanteConcentrado;
    }

    public void setAccionComprobanteConcentrado(Accioncomprobante accionComprobanteConcentrado) {
        this.accionComprobanteConcentrado = accionComprobanteConcentrado;
    }
    
}
