/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fens.desktopMonitor.bo;

import com.cmm.cvs2xml.util.DateManage;
import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.bo.comprobante.Cfd33BO;
import com.fens.desktopMonitor.bo.comprobante.ConexionPAC;
import com.fens.desktopMonitor.bo.comprobante.MailBO;
import com.fens.desktopMonitor.bo.comprobante.UtilNombraArchivosBO;
import com.fens.desktopMonitor.config.Configuration;
import com.fens.desktopMonitor.dto.Accioncomprobante;
import com.fens.desktopMonitor.dto.Archivomaestro;
import com.fens.desktopMonitor.dto.Comprobantefiscal;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.dto.Emisor;
import com.fens.desktopMonitor.dto.Ubicacionfiscal;
import com.fens.desktopMonitor.jdbc.ArchivomaestroDaoImpl;
import com.fens.desktopMonitor.jdbc.ComprobantefiscalDaoImpl;
import com.fens.desktopMonitor.jdbc.ResourceManager;
import com.fens.desktopMonitor.util.FileManage;
import com.fens.desktopMonitor.util.GenericMethods;
import com.fens.desktopMonitor.util.GenericValidator;
import com.fens.desktopMonitor.util.ImpresionJavaPDFBox;
import com.fens.desktopMonitor.views.Principal;
import com.tsp.interconecta.ws.WsGenericResp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Complemento;
import mx.bigdata.sat.cfdiv.CFDv33;
import mx.bigdata.sat.retencion.v1.schema.Retenciones;
import mx.bigdata.sat.security.KeyLoader;
import mx.bigdata.sat.security.KeyLoader2;

/**
 *
 * @author user
 */
public class ReaderXlsProcesoBO {
    
    private Connection conn=null;
    private Configuracion configuracionDto = null;
    private boolean errorConexionPAC = false;
    private Archivomaestro archivoMaestroDto = null;
    private Comprobantefiscal comprobanteFiscalDto = null;
    private ComprobantefiscalDaoImpl compFiscalDaoImpl = null;
    private ArchivomaestroDaoImpl archivomaestroDaoImpl = null;
    private boolean generarPDFConcentrado = false;
    private final GenericValidator gc = new GenericValidator();
    
    //columnas del xls
    public static final int RFC_EMISOR=0;
    public static final int RFC_RECEPTOR=1;
    public static final int NOMBRE=2;
    public static final int USO_CFDI=3;
    public static final int PAIS=4;
    public static final int NUMERO_REGISTRO_TRIBUTARIO=5;
    public static final int TIPO_COMPROBANTE=6;
    public static final int DESCUENTO=7;
    public static final int CONDICION_PAGO=8;
    public static final int MONEDA=9;
    public static final int TIPO_CAMBIO=10;
    public static final int FORMA_PAGO=11;
    public static final int METODO_DE_PAGO=12;
    public static final int SERIE=13;
    public static final int FOLIO=14;
    public static final int DESCRIPCION=15;
    public static final int MONTO=16;
    public static final int NUMERO_AUTORIZACION=17;
    public static final int FECHA_AUTORIZACION=18;
    public static final int CORREO=19;
    String mess=null;
    
    //nombre de las columnas
    public static final String RFC_EMISOR_NAME="RFC_EMISOR";
    public static final String RFC_RECEPTOR_NAME="RFC_RECEPTOR";
    public static final String NOMBRE_NAME="NOMBRE";
    public static final String USO_CFDI_NAME="USO_CFDI";
    public static final String PAIS_NAME="PAIS";
    public static final String NUMERO_REGISTRO_TRIBUTARIO_NAME="NUMERO_REGISTRO_TRIBUTARIO";
    public static final String TIPO_COMPROBANTE_NAME="TIPO_COMPROBANTE";
    public static final String DESCUENTO_NAME="DESCUENTO";
    public static final String CONDICION_PAGO_NAME="CONDICION_PAGO";
    public static final String MONEDA_NAME="MONEDA";
    public static final String TIPO_CAMBIO_NAME="TIPO_CAMBIO";
    public static final String FORMA_PAGO_NAME="FORMA_PAGO";
    public static final String METODO_DE_PAGO_NAME="METODO_DE_PAGO";
    public static final String SERIE_NAME="SERIE";
    public static final String FOLIO_NAME="FOLIO";
    public static final String DESCRIPCION_NAME="DESCRIPCION";
    public static final String MONTO_NAME="MONTO";
    public static final String NUMERO_AUTORIZACION_NAME="NUMERO_AUTORIZACION";
    public static final String FECHA_AUTORIZACION_NAME="FECHA_AUTORIZACION";
    public static final String CORREO_NAME="CORREO";
    
    private List<File> archivosProcesados=new ArrayList<File>();
    
    private Principal wPrincipal = null;
    
    public ReaderXlsProcesoBO(Configuracion configuracionDto, Connection conn){
        this.configuracionDto=configuracionDto;
        this.conn=conn;
        compFiscalDaoImpl = new ComprobantefiscalDaoImpl(this.getConn());
        archivomaestroDaoImpl = new ArchivomaestroDaoImpl(this.getConn());
    }
    
    public void procesarXls(File archivoXls) throws IOException, BiffException{
        WorkbookSettings ws = new WorkbookSettings();
        ws.setEncoding("ISO-8859-1");

        Workbook archivoExcel = Workbook.getWorkbook(archivoXls,ws); 
        
        
        Sheet hoja = archivoExcel.getSheet(0); 
        int numColumnas = hoja.getColumns(); 
        int numFilas = hoja.getRows(); 
        String data; 
        GenericValidator gc=new GenericValidator();
        List<ExceptionXls> listError=new ArrayList<ExceptionXls>();

        System.out.println("listado-->"+numColumnas+","+numFilas);
        
        String errorStr="";
        String exitoStr="";
        boolean valido=false;
        
        int tipoXls=0;
        try{
            tipoXls=Integer.parseInt(hoja.getCell(1,0 ).getContents());
        }catch(Exception e){
            e.printStackTrace();
        }
        
        if(hoja.getCell(0,0).getContents().equals("FACTURA")&&tipoXls==1){
            valido=true;
            for (int fila = 2; fila < numFilas; fila++) {
                String message="";
                DatosXls procesoData=new DatosXls();
                for (int columna = 0; columna < numColumnas; columna++) {
                    data = hoja.getCell(columna,fila ).getContents();
                    switch(columna){
                        case RFC_EMISOR:
                            if(!data.trim().equals("")){
                                if(gc.isRFC(data)){
                                    procesoData.setRFC_EMISOR(data);
                                }else{
                                    message+="Error en la columna "+RFC_EMISOR_NAME+": RFC no valido";
                                }
                            }else{
                                message+="Error en la columna "+RFC_EMISOR_NAME+": El dato se encuentra vacio";
                            }
                        break;
                        case RFC_RECEPTOR:
                            if(!data.trim().equals("")){
                                if(gc.isRFC(data)){
                                    procesoData.setRFC_RECEPTOR(data);
                                }else{
                                    message+="Error en la columna "+RFC_RECEPTOR_NAME+": RFC no valido";
                                }
                            }else{
                                message+="Error en la columna "+RFC_RECEPTOR_NAME+": El dato se encuentra vacio";
                            }
                        break;
                        case NOMBRE:
                            if(!data.trim().equals("")){
                                procesoData.setNOMBRE(data);
                            }else{
                                message+="Error en la columna "+NOMBRE_NAME+": El dato se encuentra vacio";
                            }
                        break;
                        case USO_CFDI:
                            if(!data.trim().equals("")){
                                procesoData.setUSO_CFDI(data);
                            }else{
                                message+="Error en la columna "+USO_CFDI_NAME+": El dato se encuentra vacio";
                            }
                        break;
                        case PAIS:
                            if(!data.trim().equals("")){
                                procesoData.setPAIS_NAME(data);
                            }
                        break;
                        case NUMERO_REGISTRO_TRIBUTARIO:
                            if(!data.trim().equals("")){
                                procesoData.setNUMERO_REGISTRO_TRIBUTARIO_NAME(data);
                            }
                        break;
                        case TIPO_COMPROBANTE:
                            if(!data.trim().equals("")){
                                procesoData.setTIPO_COMPROBANTE(data);
                            }else{
                                message+="Error en la columna "+TIPO_COMPROBANTE_NAME+": El dato se encuentra vacio";
                            }
                        break;
                        case DESCUENTO:
                            if(!data.trim().equals("")){
                                if(gc.isNumeric(data)){
                                    procesoData.setDESCUENTO(data);
                                }else{
                                    message+="Error en la columna "+DESCUENTO_NAME+": No es un dato numerico";
                                }
                            }
                        break;
                        case CONDICION_PAGO:
                            if(!data.trim().equals("")){
                                procesoData.setCONDICION(data);
                            }
                        break;
                        case MONEDA:
                            if(!data.trim().equals("")){
                                procesoData.setMONEDA(data);
                            }else{
                                message+="Error en la columna "+MONEDA_NAME+": El dato se encuentra vacio";
                            }
                        break;
                        case TIPO_CAMBIO:
                            if(!data.trim().equals("")){
                                if(gc.isNumeric(data)){
                                    procesoData.setTIPO_CAMBIO(data);
                                }else{
                                    message+="Error en la columna "+TIPO_CAMBIO_NAME+": No es un dato numerico";
                                }
                            }
                        break;
                        case FORMA_PAGO:
                            if(!data.trim().equals("")){
                                if(gc.isNumeric(data)){
                                    procesoData.setFORMA_PAGO(data);
                                }else{
                                    message+="Error en la columna "+FORMA_PAGO_NAME+": No es un dato numerico";
                                }
                            }else{
                                message+="Error en la columna "+FORMA_PAGO_NAME+": El dato se encuentra vacio";
                            }
                        break;
                        case METODO_DE_PAGO:
                            if(!data.trim().equals("")){
                                procesoData.setMETODO_DE_PAGO(data);
                            }else{
                                message+="Error en la columna "+METODO_DE_PAGO_NAME+": El dato se encuentra vacio";
                            }
                        break;
                        case SERIE:
                            if(!data.trim().equals("")){
                                procesoData.setSERIE(data);
                            }
                        break;
                        case FOLIO:
                            if(!data.trim().equals("")){
                                procesoData.setFOLIO(data);
                            }
                        break;
                        case DESCRIPCION:
                            if(!data.trim().equals("")){
                                procesoData.setDESCRIPCION(data);
                            }else{
                                message+="Error en la columna "+DESCRIPCION_NAME+": El dato se encuentra vacio";
                            }
                        break;
                        case MONTO:
                            if(!data.trim().equals("")){
                                if(gc.isNumeric(data)){
                                    procesoData.setMONTO(data);
                                }else{
                                    message+="Error en la columna "+MONTO_NAME+": No es un dato numerico";
                                }
                            }else{
                                message+="Error en la columna "+MONTO_NAME+": El dato se encuentra vacio";
                            }
                        break;
                        case NUMERO_AUTORIZACION:
                            if(!data.trim().equals("")){
                                procesoData.setNUMERO_AUTORIZACION(data);
                            }
                        break;
                        case FECHA_AUTORIZACION:
                            if(!data.trim().equals("")){
                                if(gc.isDate(data)){
                                    String temp[]=data.split("/");
                                    if(temp[2].length()==2){
                                        data=temp[0]+"/"+temp[1]+"/20"+temp[2];
                                    }
                                    procesoData.setFECHA_AUTORIZACION(data);
                                }else{
                                    message+="Error en la columna "+NUMERO_AUTORIZACION_NAME+": No es un dato de fecha 'dd/MM/yyyy'";
                                }
                            }else{
                                message+="Error en la columna "+FECHA_AUTORIZACION_NAME+": El dato se encuentra vacio";
                            }
                        break;
                        case CORREO:
                            if(!data.trim().equals("")){
                                if(gc.isEmail(data.trim())){
                                    procesoData.setCORREO(data.trim());
                                }
                            }
                            break;
                    }
                }
                //verificamos si existen errores
                if(message.equals("")){
                    //generamos el comprobante
                    if(fila==2){
                        try{
                            archivoMaestroDto = new Archivomaestro();

                            try{
                                llenaInfoArchivoMaestro(archivoMaestroDto, archivoXls);
                                archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_EN_PROCESO);

                                //Almacenamos registro de archivoMaestro
                                archivomaestroDaoImpl.insert(archivoMaestroDto);
                            }catch(Exception ex){
                                message+="Error al intentar almacenar datos iniciales de Archivo Maestro en BD. "
                                        + "Revise la conexion a BD y que el archivo fuente no este siendo usado por otro proceso." + ex.toString();
                            }

                            //Almacenamos numero de Comprobantes que contiene el archivo maestro
                            try{
                                archivoMaestroDto.setNumerofacturas(numFilas);
                                archivomaestroDaoImpl.update(archivoMaestroDto.createPk(), archivoMaestroDto);
                            }catch(Exception e){}

                            if (verificaArchivoRepetido(archivoMaestroDto)){
                                message+="El archivo fue previamente procesado con éxito (o se esta procesando)."
                                        + " Ya existe un archivo procesado con las mismas características .";
                            }

                        }catch(Exception e){
                            e.printStackTrace();
                            message+=e.getLocalizedMessage();
                        }
                    }
                    try{
                        if(message.equals("")){
                            System.out.println("Procesar el archivo");
                            /*final List<ExceptionXls> listError1=listError;
                            final File archivoXls1=archivoXls;
                            final int numFilas1=numFilas;
                            final DatosXls procesoData1=procesoData;
                            final int fila1=fila;
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        createComprobante(procesoData1, archivoXls1, listError1, numFilas1, fila1);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        mess=ex.getLocalizedMessage();
                                    }
                                }
                            });
                            thread.start();*/
                        }
                        try {
                            createComprobante(procesoData, archivoXls, listError, numFilas, fila);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            mess=ex.getLocalizedMessage();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        message+=e.getLocalizedMessage();
                    }
                    if(mess!=null&&!mess.equals("")){
                        message+=mess;
                    }
                    if(!message.equals("")){
                        //Actualizamos registro Comprobantefiscal en BD (ERROR)

                        if(comprobanteFiscalDto==null){
                            comprobanteFiscalDto = new Comprobantefiscal();
                            comprobanteFiscalDto.setIdarchivomaestro(archivoMaestroDto.getIdarchivomaestro());
                            comprobanteFiscalDto.setFechahoraproceso(new Date());
                            comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_TIMBRAR_ERROR);
                            try{
                                compFiscalDaoImpl.update(comprobanteFiscalDto.createPk(),comprobanteFiscalDto);
                            }catch(Exception e){}
                        }

                        if(comprobanteFiscalDto.getIdcomprobantefiscal()>0){
                            comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_TIMBRAR_ERROR);
                            try{
                                compFiscalDaoImpl.update(comprobanteFiscalDto.createPk(),comprobanteFiscalDto);
                            }catch(Exception e){}
                        }

                        //Actualizamos estatus en BD de Archivo Maestro (ERROR)
                        if(archivoMaestroDto.getIdarchivomaestro()>0){
                            //Actualizamos registro Comprobantefiscal en BD
                            archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_ERROR);
                            try{
                                archivomaestroDaoImpl.update(archivoMaestroDto.createPk(),archivoMaestroDto);
                            }catch(Exception e){}
                        }
                    }
                    //mandamos el error
                    ExceptionXls error=new ExceptionXls();
                    error.setFila(fila);
                    error.setMessage(message);
                    listError.add(error);
                }
            }
        }else{
            errorStr+="-\tError el tipo de XLS a leer es invalido";
            //Movemos archivo a destino carpeta Exito
            try{

                if(archivoMaestroDto==null){
                    archivoMaestroDto = new Archivomaestro();

                    try{
                        llenaInfoArchivoMaestro(archivoMaestroDto, archivoXls);
                        archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_EN_PROCESO);

                        //Almacenamos registro de archivoMaestro
                        archivomaestroDaoImpl.insert(archivoMaestroDto);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }

                //Actualizamos estatus en BD de Archivo Maestro (EXITO)
                if(archivoMaestroDto.getIdarchivomaestro()>0 ){
                    //Actualizamos registro Comprobantefiscal en BD
                    archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_ERROR);
                    archivomaestroDaoImpl.update(archivoMaestroDto.createPk(),archivoMaestroDto);
                }
                String rutaArchivoDestinoExito = UtilNombraArchivosBO.calcFileArchivoMaestro(configuracionDto, ArchivoMaestroBO.ESTATUS_ERROR, archivoXls).getAbsolutePath();

                //Movemos archivo a carpeta 
                FileManage.moveFile(archivoXls.getAbsolutePath(), rutaArchivoDestinoExito);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        //generamos los archivos de errores
        File archivoError=new File(FileManage.getFileNameWithoutExtension(configuracionDto.getRutacarpetaerrores()+File.separator+archivoXls.getName())+"_Error.txt");
        File archivoExito=new File(FileManage.getFileNameWithoutExtension(configuracionDto.getRutacarpetaexitosos()+File.separator+archivoXls.getName())+"_Exito.txt");
        for (int fila = 1; fila < numFilas; fila++) {
            boolean exist=false;
            for(ExceptionXls error:listError){
                if(error.getFila()==fila){
                    exist=true;
                    errorStr+="-\tError en la fila: "+fila+", RFC Receptor: "+hoja.getCell(1,fila ).getContents()+(hoja.getCell(14,fila ).getContents()!=null&&!hoja.getCell(14,fila ).getContents().trim().equals("")?" Folio: "+hoja.getCell(14,fila).getContents()+", ":"")+(hoja.getCell(13,fila ).getContents()!=null&&!hoja.getCell(13,fila ).getContents().trim().equals("")?" Serie: "+hoja.getCell(13,fila).getContents()+", ":"")+" Error: "+error.getMessage()+"\n";
                }
            }
            if(!exist){
                exitoStr+="-\tExito en la fila: "+fila+", RFC Receptor: "+hoja.getCell(1,fila ).getContents()+(hoja.getCell(14,fila ).getContents()!=null&&!hoja.getCell(14,fila ).getContents().trim().equals("")?" Folio: "+hoja.getCell(14,fila).getContents()+", ":"")+(hoja.getCell(13,fila ).getContents()!=null&&!hoja.getCell(13,fila ).getContents().trim().equals("")?" Serie: "+hoja.getCell(13,fila).getContents()+", ":"")+"\n";
            }
        }
        
        if(!errorStr.equals("")){
            //guardamos el archivo con los errores
            FileManage.createFileString(archivoError, errorStr);
        }
        if(!exitoStr.equals("")){
            //guardamos el archico con exito
            FileManage.createFileString(archivoExito, exitoStr);
        }
        
        if(valido){
            //Movemos archivo a destino carpeta Exito
            try{
                String rutaArchivoDestinoExito = "";
                if(!errorStr.equals("")){
                    rutaArchivoDestinoExito = UtilNombraArchivosBO.calcFileArchivoMaestro(configuracionDto, ArchivoMaestroBO.ESTATUS_ERROR, archivoXls).getAbsolutePath();
                }else{
                    rutaArchivoDestinoExito = UtilNombraArchivosBO.calcFileArchivoMaestro(configuracionDto, ArchivoMaestroBO.ESTATUS_EXITO, archivoXls).getAbsolutePath();
                }
                System.out.println(rutaArchivoDestinoExito);
                //Movemos archivo a carpeta 
                FileManage.moveFile(archivoXls.getAbsolutePath(), rutaArchivoDestinoExito);

                if(archivoMaestroDto==null){
                    archivoMaestroDto = new Archivomaestro();

                    try{
                        //llenaInfoArchivoMaestro(archivoMaestroDto, archivoXls);
                        archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_EN_PROCESO);

                        //Almacenamos registro de archivoMaestro
                        archivomaestroDaoImpl.insert(archivoMaestroDto);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }

                //Actualizamos estatus en BD de Archivo Maestro (EXITO)
                if(archivoMaestroDto.getIdarchivomaestro()>0 ){
                    //Actualizamos registro Comprobantefiscal en BD
                    if(!errorStr.equals("")){
                        archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_EXITO);
                    }else{
                        archivoMaestroDto.setIdestatus(ArchivoMaestroBO.ESTATUS_EXITO);
                    }
                    archivomaestroDaoImpl.update(archivoMaestroDto.createPk(),archivoMaestroDto);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        //eliminar XML temporales
        try{
            for (File item:archivosProcesados){
                item.delete();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void createComprobante(DatosXls procesoData, File archivoXls, List<ExceptionXls> listError, int numFacturas, int fila) throws Exception{
        mx.bigdata.sat.cfdi.v33.schema.ObjectFactory factory=new mx.bigdata.sat.cfdi.v33.schema.ObjectFactory();
        mx.bigdata.sat.cfdi.v33.schema.Comprobante comprobanteFiscal=factory.createComprobante();
        
        comprobanteFiscal.setVersion("3.3");
        comprobanteFiscal.setFecha(DateManage.dateToXMLGregorianCalendar(new Date()));
        if(mx.bigdata.sat.cfdi.v33.schema.CTipoDeComprobante.fromValue(procesoData.getTIPO_COMPROBANTE())!=null){
            comprobanteFiscal.setTipoDeComprobante(mx.bigdata.sat.cfdi.v33.schema.CTipoDeComprobante.fromValue(procesoData.getTIPO_COMPROBANTE()));
        }else{
            throw new Exception(" - El tipo de comprobante es invalido");
        }
        
        if(mx.bigdata.sat.cfdi.v33.schema.CMoneda.fromValue(procesoData.getMONEDA())!=null){
            comprobanteFiscal.setMoneda(mx.bigdata.sat.cfdi.v33.schema.CMoneda.fromValue(procesoData.getMONEDA()));
            if(!procesoData.getMONEDA().equals("MXN")&&!procesoData.getMONEDA().equals("XXX")){
                if(procesoData.getTIPO_CAMBIO()!=null&&!procesoData.getTIPO_CAMBIO().equals("")){
                    double tipoCambio=0;
                    try{
                        tipoCambio=Double.parseDouble(procesoData.getTIPO_CAMBIO());
                    }catch(Exception e){}
                    if(tipoCambio<=0){
                        throw new Exception(" - El tipo de cambio es requerido por el tipo de moneda "+procesoData.getMONEDA());
                    }else{
                        comprobanteFiscal.setTipoCambio(new BigDecimal(tipoCambio).setScale(2, RoundingMode.HALF_UP));
                    }
                }else{
                    throw new Exception(" - El tipo de cambio es requerido por el tipo de moneda "+procesoData.getMONEDA());
                }
            }
        }else{
            throw new Exception(" - La moneda es invalida");
        }
        comprobanteFiscal.setFolio(procesoData.getFOLIO());
        comprobanteFiscal.setSerie(procesoData.getSERIE());
        double descuento=0;
        double monto=0;
        if(procesoData.getMONTO()!=null&&!procesoData.getMONTO().equals("")){
             try{
                monto=Double.parseDouble(procesoData.getMONTO());
            }catch(Exception e){}
            if(monto<=0){
                throw new Exception(" - El monto debe ser mayor a 0");
            }else{
                try{
                    descuento=Double.parseDouble(procesoData.getDESCUENTO());
                }catch(Exception e){}
                comprobanteFiscal.setSubTotal(new BigDecimal(monto).setScale(2, RoundingMode.HALF_UP));
                if(descuento>0){
                    BigDecimal montoDescuento=new BigDecimal(descuento).setScale(NOMBRE, RoundingMode.HALF_UP);
                    BigDecimal montoTotal=new BigDecimal(monto).setScale(NOMBRE, RoundingMode.HALF_UP);
                    monto=montoTotal.doubleValue()-montoDescuento.doubleValue();
                    comprobanteFiscal.setTotal(new BigDecimal(monto).setScale(2, RoundingMode.HALF_UP));
                    comprobanteFiscal.setDescuento(montoDescuento);
                }else{
                    comprobanteFiscal.setTotal(new BigDecimal(monto).setScale(2, RoundingMode.HALF_UP));
                }
            }
        }else{
            throw new Exception(" - El monto es requerido");
        }
        
        if(mx.bigdata.sat.cfdi.v33.schema.CMetodoPago.fromValue(procesoData.getMETODO_DE_PAGO())!=null){
            comprobanteFiscal.setMetodoPago(mx.bigdata.sat.cfdi.v33.schema.CMetodoPago.fromValue(procesoData.getMETODO_DE_PAGO()));
        }else{
            throw new Exception(" - El metodo de pago es requerido");
        }
        comprobanteFiscal.setFormaPago(procesoData.getFORMA_PAGO());
        
        EmisorBO emisorBO = new EmisorBO(this.getConn());
        Emisor[] listaEmisorDto = emisorBO.findEmisores(-1, 0, 0, " AND RFC='" + procesoData.getRFC_EMISOR() + "'");
        if (listaEmisorDto.length<=0){
            throw new Exception(" - El RFC de emisor expresado en el Layout "
                    + "de entrada no esta registrado en la base de datos local.");
        }
        //Datos de empresa en bd local
        Emisor emisorDto = listaEmisorDto[0];
        Ubicacionfiscal ubicacionfiscal = new UbicacionFiscalBO(this.getConn()).findByIdEmisor(this.getConn(),emisorDto.getIdemisor());
        
        comprobanteFiscal.setLugarExpedicion(ubicacionfiscal.getCodigopostal());
        
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Emisor emisor=new mx.bigdata.sat.cfdi.v33.schema.Comprobante.Emisor();
        emisor.setNombre(emisorDto.getRazonsocial());
        emisor.setRegimenFiscal(emisorDto.getRegimenfiscal());
        emisor.setRfc(procesoData.getRFC_EMISOR());
        comprobanteFiscal.setEmisor(emisor);
        
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Receptor receptor=new mx.bigdata.sat.cfdi.v33.schema.Comprobante.Receptor();
        receptor.setNombre(procesoData.getNOMBRE());
        receptor.setRfc(procesoData.getRFC_RECEPTOR());
        if(mx.bigdata.sat.cfdi.v33.schema.CUsoCFDI.fromValue(procesoData.getUSO_CFDI())!=null){
            receptor.setUsoCFDI(mx.bigdata.sat.cfdi.v33.schema.CUsoCFDI.fromValue(procesoData.getUSO_CFDI()));
        }else{
            throw new Exception("El uso del cfdi es requerido");
        }
        if(procesoData.getPAIS_NAME()!=null&&!procesoData.getPAIS_NAME().trim().equals("")){
            if(mx.bigdata.sat.cfdi.v33.schema.CPais.fromValue(procesoData.getPAIS_NAME())!=null){
                receptor.setResidenciaFiscal(mx.bigdata.sat.cfdi.v33.schema.CPais.fromValue(procesoData.getPAIS_NAME()));
            }else{
                throw new Exception("El pais es invalido");
            }
        }
        if(procesoData.getNUMERO_REGISTRO_TRIBUTARIO_NAME()!=null&&!procesoData.getNUMERO_REGISTRO_TRIBUTARIO_NAME().trim().equals("")){
            receptor.setNumRegIdTrib(procesoData.getNUMERO_REGISTRO_TRIBUTARIO_NAME());
        }
        comprobanteFiscal.setReceptor(receptor);
        
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos conceptos=new mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos();
        mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto concepto=new mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto();
        concepto.setCantidad(BigDecimal.ONE);
        concepto.setDescripcion("Financiación de ayudas");
        concepto.setImporte(new BigDecimal(monto).setScale(2, RoundingMode.HALF_UP));
        concepto.setValorUnitario(new BigDecimal(monto).setScale(2, RoundingMode.HALF_UP));
        if(descuento>0){
            concepto.setDescuento(new BigDecimal(descuento).setScale(2, RoundingMode.HALF_UP));
        }
        concepto.setClaveProdServ("84101600");
        concepto.setClaveUnidad("C62");
        conceptos.getConcepto().add(concepto);
        comprobanteFiscal.setConceptos(conceptos);
        
        mx.bigdata.sat.common.donat.schema.ObjectFactory factoryDonat=new mx.bigdata.sat.common.donat.schema.ObjectFactory();
        mx.bigdata.sat.common.donat.schema.Donatarias donat=factoryDonat.createDonatarias();
        
        donat.setVersion("1.1");
        donat.setFechaAutorizacion(DateManage.dateToXMLGregorianCalendar(DateManage.stringToDate(procesoData.getFECHA_AUTORIZACION(), "dd/MM/yyyy")));
        donat.setNoAutorizacion(procesoData.getNUMERO_AUTORIZACION());
        donat.setLeyenda(procesoData.getDESCRIPCION());
        
        try{
            if (comprobanteFiscal.getComplemento() == null || comprobanteFiscal.getComplemento().size() <=0){
                //comp.setComplemento(new Complemento());
                comprobanteFiscal.getComplemento().add(new Complemento());
            }
            //comp.getComplemento().getAny().add((mx.bigdata.sat.complementos.schema.iedu.InstEducativas)complementoObjeto);
            comprobanteFiscal.getComplemento().get(0).getAny().add(donat);
        }catch(Exception e){}
        
        //verificamos si no se produjo un error
            int idComprobanteFiscal=0;
            try{
                idComprobanteFiscal=compFiscalDaoImpl.findByDynamicWhere("IDCOMPROBANTEFISCAL>0 ORDER BY IDCOMPROBANTEFISCAL DESC", null)[0].getIdcomprobantefiscal()+1;
            }catch(Exception e){}
            comprobanteFiscalDto = new Comprobantefiscal();
            comprobanteFiscalDto.setIdcomprobantefiscal(idComprobanteFiscal);
            comprobanteFiscalDto.setIdemisor(emisorDto.getIdemisor());
            comprobanteFiscalDto.setIdarchivomaestro(archivoMaestroDto.getIdarchivomaestro());
            comprobanteFiscalDto.setFechahoraproceso(new Date());
            comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_TIMBRAR_EN_PROCESO);
            int idTipoComprobante=ComprobanteFiscalBO.TIPO_COMPROBANTE_FACTURA;
            comprobanteFiscalDto.setIdtipocomprobante(idTipoComprobante);
            comprobanteFiscalDto.setNombreReceptor(procesoData.getNOMBRE());
            //sellamos el comprobante
            //Recuperamos objetos de Certificado y Llave privada del emisor
            java.security.cert.X509Certificate certX509 = null;
            java.security.PrivateKey pkey = null;
            try{
                 certX509 = KeyLoader2.loadX509Certificate(new FileInputStream(emisorDto.getRutacer()));
                 pkey = KeyLoader2.loadPKCS8PrivateKey(new FileInputStream(emisorDto.getRutakey()), emisorDto.getEmisorpass());
            }catch(Exception ex){
                throw new Exception("El emisor de RFC '" + procesoData.getRFC_EMISOR() + "' configurado"
                        + " no tiene Certificado y Llave privada validos." + ex.toString());
            }
            
            if (certX509!=null && pkey!=null){
                CFDv33 cFDv33 = null;
                //Sellamos comprobante con CSD emisor
                try{
                    cFDv33 = creaCFD(comprobanteFiscal);
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
                File fileArchivoMaestro=new File("archivotemporal_"+fila+".xml");
                File archivoComprobanteFiscalEnProceso = null;

                try{
                    archivoComprobanteFiscalEnProceso = UtilNombraArchivosBO.calcFileComprobanteFiscal(configuracionDto, 
                        ComprobanteFiscalBO.ESTATUS_TIMBRAR_EN_PROCESO, fileArchivoMaestro,
                        procesoData.getRFC_EMISOR(), procesoData.getRFC_RECEPTOR(), StringManage.getValidString(procesoData.getFOLIO()), null, null, "xml");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try{
                        System.out.println(new String(baos.toByteArray()));
                        cFDv33.guardar(baos);
                        archivoComprobanteFiscalEnProceso = FileManage.createFileFromByteArrayOutputStream(baos, archivoComprobanteFiscalEnProceso);
                        if (!archivoComprobanteFiscalEnProceso.exists()){
                            throw new Exception("Archivo En Proceso no existente.");
                        }
                    }catch(Exception ex){
                        throw new Exception("No se pudo almacenar el archivo sellado localmente con CSD emisor en '"
                                + archivoComprobanteFiscalEnProceso.getAbsolutePath() + "' . " + ex.toString());
                    }
                    
                    //Procesamos timbre en PAC
                    WsGenericResp respWs = null;
                    try{
                        ConexionPAC conexionPAC = new ConexionPAC(this.configuracionDto);
                        respWs = conexionPAC.timbraComprobantePAC(baos.toByteArray());
                    }catch(UnsupportedEncodingException | MalformedURLException ex){
                        //Eliminamos archivo temporal en proceso

                        //Forzamos final de metodo
                        ex.getLocalizedMessage();
                    }
                    if (respWs==null){
                        throw new Exception("Respuesta de servicio PAC con valor nulo.");
                    }else{
                        //Buscamos errores
                        if (respWs.isIsError()){
                            //Con errores

                            //intentamos eliminar XML temporal sellado localmente
                            archivosProcesados.add(archivoComprobanteFiscalEnProceso);

                            throw new Exception("No se timbro el comprobante, respuesta del PAC a continuación. " + 
                                        respWs.getNumError() + " : " + respWs.getErrorMessage());
                        }else{
                            //Guardamos archivo XML
                            File archivoComprobanteFiscalExito = null;
                            try{ 
                                archivoComprobanteFiscalExito = UtilNombraArchivosBO.calcFileComprobanteFiscal(configuracionDto, 
                                                    ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO, fileArchivoMaestro,
                                                    procesoData.getRFC_EMISOR(), procesoData.getRFC_RECEPTOR(), StringManage.getValidString(procesoData.getFOLIO()), null, respWs.getFolioUUID(), "xml");

                                System.out.println(new String(respWs.getXML()));
                                
                                archivoComprobanteFiscalExito = FileManage.createFileFromByteArray(respWs.getXML(), 
                                        FileManage.getParentPathString(archivoComprobanteFiscalExito) + File.separator , 
                                        archivoComprobanteFiscalExito.getName());

                                //asignamos dato a objeto DTO
                                if (archivoComprobanteFiscalExito!=null)
                                    comprobanteFiscalDto.setNombrearchivoxml(archivoComprobanteFiscalExito.getName());
                            }catch(Exception ex){
                                ex.printStackTrace();
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
                                                    procesoData.getRFC_EMISOR(), procesoData.getRFC_RECEPTOR(), StringManage.getValidString(procesoData.getFOLIO()), null, respWs.getFolioUUID(),  "pdf");

                                Cfd33BO cfd33BO = new Cfd33BO(archivoComprobanteFiscalExito, Cfd33BO.CONTEXT_ARRAY_COMPLEMENTOS);
                                if(procesoData.getCONDICION()!=null&&!procesoData.getCONDICION().equals("")){
                                    cfd33BO.setComentarios(procesoData.getCONDICION());
                                }
                                ByteArrayOutputStream baosPDF;
                                
                                System.out.println("PDF "+respWs.getFolioUUID());
                                
                                /*Accioncomprobante accionComprobanteDto = buscaAccionPersonalizada(facturaDatos);
                                cfd33BO.setFacturaDatos(facturaDatos);
                                cfd33BO.setAccionComprobante(accionComprobanteDto);*/
                                baosPDF = cfd33BO.toPdf();

                                archivoComprobanteFiscalExitoPDF = FileManage.createFileFromByteArray(baosPDF.toByteArray(),
                                         FileManage.getParentPathString(archivoComprobanteFiscalExitoPDF) + File.separator , 
                                         archivoComprobanteFiscalExitoPDF.getName());

                                //asignamos dato a objeto DTO
                                if (archivoComprobanteFiscalExitoPDF!=null){
                                    comprobanteFiscalDto.setNombrearchivopdf(archivoComprobanteFiscalExitoPDF.getName());
                                    /*if (generarPDFConcentrado)
                                        listaArchivosPDF.add(archivoComprobanteFiscalExitoPDF);*/
                                }
                            }catch(Exception ex){
                                ex.printStackTrace();
                                throw new Exception("Error inesperado crear representacion impresa de Comprobante Fiscal. " + GenericMethods.exceptionStackTraceToString(ex));
                                //throw new Exception("Error inesperado crear representacion impresa de Comprobante Fiscal. " + ex.toString());
                            }
                            
                            //Completamos datos de registro BD
                            try{ 
                                llenaInfoComprobanteFiscalDto(comprobanteFiscalDto, (Comprobante) cFDv33.getComprobante().getComprobante(), respWs, archivoComprobanteFiscalExito);
                                comprobanteFiscalDto.setFolio(comprobanteFiscal.getFolio().substring(0, 10));
                                compFiscalDaoImpl.insert(comprobanteFiscalDto);
                            }catch(Exception ex){
                                throw new Exception("Error inesperado al almacenar información de Comprobante Fiscal generado. " + ex.toString());
                            }
                            
                            
                            //Acciones en forma asíncrona
                            try{
                                final File archivoComprobanteFiscalExito_Aux = archivoComprobanteFiscalExito;
                                final File archivoComprobanteFiscalExitoPDF_Aux = archivoComprobanteFiscalExitoPDF;
                                final String uuid_Aux= respWs.getFolioUUID();
                                final Comprobante comp_Aux = comprobanteFiscal;
                                final int idTipoComprobante_Aux = idTipoComprobante;
                                final String correo=procesoData.getCORREO();
                                if(correo!=null&&!correo.trim().equals("")){
                                    Runnable r1 = new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                System.out.println("[Asíncrono] Ejecutando acciones para comprobante. " + new Date());
                                                ejecutaAcciones(archivoComprobanteFiscalExito_Aux,archivoComprobanteFiscalExitoPDF_Aux, 
                                                        comp_Aux, uuid_Aux, idTipoComprobante_Aux, correo);
                                            } catch (Exception ex) {
                                                System.out.println("[Asíncrono] Error al intentar ejecutar acciones para comprobante. " + ex.toString());
                                                ex.printStackTrace();
                                            }
                                        }
                                    };

                                    Thread thr1 = new Thread(r1);
                                    thr1.start();
                                }
                            }catch(Exception ex){
                                System.out.println("Error inesperado al ejecutar acciones para comprobante de retenciones timbrado de forma asíncrona. " + ex.toString());
                                ex.printStackTrace();
                            }
                            
                        }
                    }
                    
                    comprobanteFiscalDto.setIdestatus(ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO);

                    //Guardar en BD
                    //Actualizamos registro Comprobantefiscal en BD
                    compFiscalDaoImpl.update(comprobanteFiscalDto.createPk(),comprobanteFiscalDto);
                    
                    
                }catch(Exception e){
                    throw new Exception("No se pudo almacenar el archivo '"
                                            + archivoComprobanteFiscalEnProceso.getAbsolutePath() + "' . " + e.toString());
                }

            }else{
                throw new Exception("Certificado y/o Llave privada de emisor no se pudieron recuperar, revise la configuración del emisor.");
            }
            
        
        
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
    
    public void ejecutaAcciones(File fileXML, File filePDF, Comprobante comp, 
            String UUID, int idTipoComprobante, String correo) throws Exception{
        
        boolean notificarCorreo = true;
        
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
                    if (gc.isEmail(correo))
                        listaDestinatario.add(correo);

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
    
     protected void enviarCorreoComprobanteFiscalReceptor(List<String> listaDestinatarios, List<String> listaCC,
            File fileXML, File filePDF, Comprobante comp, String UUID, int idTipoComprobante) throws Exception{
        
        if ((listaDestinatarios.size()>0 || listaCC.size()>0) ){
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
            
        }
        
    }
    
    protected void actualizarLogVista(String mensaje){
        //Si tenemos conexion a la vista, actualizamos el log
        if (getwPrincipal()!=null){
            int maxlines = 10000;
            if (getwPrincipal().txtLogTemporal.getText().length()>maxlines){
                getwPrincipal().txtLogTemporal.replaceRange(null, 0, maxlines);
            }
            getwPrincipal().txtLogTemporal.append("\n" + mensaje);
        }
    }
    
    protected void llenaInfoComprobanteFiscalDto(
            Comprobantefiscal comprobanteFiscalDto, 
            Comprobante comp,
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
        
    }
    
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

    /**
     * @return the generarPDFConcentrado
     */
    public boolean isGenerarPDFConcentrado() {
        return generarPDFConcentrado;
    }

    /**
     * @param generarPDFConcentrado the generarPDFConcentrado to set
     */
    public void setGenerarPDFConcentrado(boolean generarPDFConcentrado) {
        this.generarPDFConcentrado = generarPDFConcentrado;
    }

    /**
     * @return the wPrincipal
     */
    public Principal getwPrincipal() {
        return wPrincipal;
    }

    /**
     * @param wPrincipal the wPrincipal to set
     */
    public void setwPrincipal(Principal wPrincipal) {
        this.wPrincipal = wPrincipal;
    }
    
    public class DatosXls{
        private String RFC_EMISOR;
        private String RFC_RECEPTOR;
        private String NOMBRE;
        private String USO_CFDI;
        private String PAIS_NAME;
        private String NUMERO_REGISTRO_TRIBUTARIO_NAME;
        private String TIPO_COMPROBANTE;
        private String DESCUENTO;
        private String CONDICION;
        private String MONEDA;
        private String TIPO_CAMBIO;
        private String FORMA_PAGO;
        private String METODO_DE_PAGO;
        private String SERIE;
        private String FOLIO;
        private String DESCRIPCION;
        private String MONTO;
        private String NUMERO_AUTORIZACION;
        private String FECHA_AUTORIZACION;
        private String CORREO;

        /**
         * @return the RFC_RECEPTOR
         */
        public String getRFC_RECEPTOR() {
            return RFC_RECEPTOR;
        }

        /**
         * @param RFC_RECEPTOR the RFC_RECEPTOR to set
         */
        public void setRFC_RECEPTOR(String RFC_RECEPTOR) {
            this.RFC_RECEPTOR = RFC_RECEPTOR;
        }

        /**
         * @return the NOMBRE
         */
        public String getNOMBRE() {
            return NOMBRE;
        }

        /**
         * @param NOMBRE the NOMBRE to set
         */
        public void setNOMBRE(String NOMBRE) {
            this.NOMBRE = NOMBRE;
        }

        /**
         * @return the USO_CFDI
         */
        public String getUSO_CFDI() {
            return USO_CFDI;
        }

        /**
         * @param USO_CFDI the USO_CFDI to set
         */
        public void setUSO_CFDI(String USO_CFDI) {
            this.USO_CFDI = USO_CFDI;
        }

        /**
         * @return the TIPO_COMPROBANTE
         */
        public String getTIPO_COMPROBANTE() {
            return TIPO_COMPROBANTE;
        }

        /**
         * @param TIPO_COMPROBANTE the TIPO_COMPROBANTE to set
         */
        public void setTIPO_COMPROBANTE(String TIPO_COMPROBANTE) {
            this.TIPO_COMPROBANTE = TIPO_COMPROBANTE;
        }

        /**
         * @return the DESCUENTO
         */
        public String getDESCUENTO() {
            return DESCUENTO;
        }

        /**
         * @param DESCUENTO the DESCUENTO to set
         */
        public void setDESCUENTO(String DESCUENTO) {
            this.DESCUENTO = DESCUENTO;
        }

        /**
         * @return the CONDICION
         */
        public String getCONDICION() {
            return CONDICION;
        }

        /**
         * @param CONDICION the CONDICION to set
         */
        public void setCONDICION(String CONDICION) {
            this.CONDICION = CONDICION;
        }

        /**
         * @return the MONEDA
         */
        public String getMONEDA() {
            return MONEDA;
        }

        /**
         * @param MONEDA the MONEDA to set
         */
        public void setMONEDA(String MONEDA) {
            this.MONEDA = MONEDA;
        }

        /**
         * @return the TIPO_CAMBIO
         */
        public String getTIPO_CAMBIO() {
            return TIPO_CAMBIO;
        }

        /**
         * @param TIPO_CAMBIO the TIPO_CAMBIO to set
         */
        public void setTIPO_CAMBIO(String TIPO_CAMBIO) {
            this.TIPO_CAMBIO = TIPO_CAMBIO;
        }

        /**
         * @return the FORMA_PAGO
         */
        public String getFORMA_PAGO() {
            return FORMA_PAGO;
        }

        /**
         * @param FORMA_PAGO the FORMA_PAGO to set
         */
        public void setFORMA_PAGO(String FORMA_PAGO) {
            this.FORMA_PAGO = FORMA_PAGO;
        }

        /**
         * @return the METODO_DE_PAGO
         */
        public String getMETODO_DE_PAGO() {
            return METODO_DE_PAGO;
        }

        /**
         * @param METODO_DE_PAGO the METODO_DE_PAGO to set
         */
        public void setMETODO_DE_PAGO(String METODO_DE_PAGO) {
            this.METODO_DE_PAGO = METODO_DE_PAGO;
        }

        /**
         * @return the SERIE
         */
        public String getSERIE() {
            return SERIE;
        }

        /**
         * @param SERIE the SERIE to set
         */
        public void setSERIE(String SERIE) {
            this.SERIE = SERIE;
        }

        /**
         * @return the FOLIO
         */
        public String getFOLIO() {
            return FOLIO;
        }

        /**
         * @param FOLIO the FOLIO to set
         */
        public void setFOLIO(String FOLIO) {
            this.FOLIO = FOLIO;
        }

        /**
         * @return the DESCRIPCION
         */
        public String getDESCRIPCION() {
            return DESCRIPCION;
        }

        /**
         * @param DESCRIPCION the DESCRIPCION to set
         */
        public void setDESCRIPCION(String DESCRIPCION) {
            this.DESCRIPCION = DESCRIPCION;
        }

        /**
         * @return the MONTO
         */
        public String getMONTO() {
            return MONTO;
        }

        /**
         * @param MONTO the MONTO to set
         */
        public void setMONTO(String MONTO) {
            this.MONTO = MONTO;
        }

        /**
         * @return the NUMERO_AUTORIZACION
         */
        public String getNUMERO_AUTORIZACION() {
            return NUMERO_AUTORIZACION;
        }

        /**
         * @param NUMERO_AUTORIZACION the NUMERO_AUTORIZACION to set
         */
        public void setNUMERO_AUTORIZACION(String NUMERO_AUTORIZACION) {
            this.NUMERO_AUTORIZACION = NUMERO_AUTORIZACION;
        }

        /**
         * @return the FECHA_AUTORIZACION
         */
        public String getFECHA_AUTORIZACION() {
            return FECHA_AUTORIZACION;
        }

        /**
         * @param FECHA_AUTORIZACION the FECHA_AUTORIZACION to set
         */
        public void setFECHA_AUTORIZACION(String FECHA_AUTORIZACION) {
            this.FECHA_AUTORIZACION = FECHA_AUTORIZACION;
        }

        /**
         * @return the RFC_EMISOR
         */
        public String getRFC_EMISOR() {
            return RFC_EMISOR;
        }

        /**
         * @param RFC_EMISOR the RFC_EMISOR to set
         */
        public void setRFC_EMISOR(String RFC_EMISOR) {
            this.RFC_EMISOR = RFC_EMISOR;
        }

        /**
         * @return the PAIS_NAME
         */
        public String getPAIS_NAME() {
            return PAIS_NAME;
        }

        /**
         * @param PAIS_NAME the PAIS_NAME to set
         */
        public void setPAIS_NAME(String PAIS_NAME) {
            this.PAIS_NAME = PAIS_NAME;
        }

        /**
         * @return the NUMERO_REGISTRO_TRIBUTARIO_NAME
         */
        public String getNUMERO_REGISTRO_TRIBUTARIO_NAME() {
            return NUMERO_REGISTRO_TRIBUTARIO_NAME;
        }

        /**
         * @param NUMERO_REGISTRO_TRIBUTARIO_NAME the NUMERO_REGISTRO_TRIBUTARIO_NAME to set
         */
        public void setNUMERO_REGISTRO_TRIBUTARIO_NAME(String NUMERO_REGISTRO_TRIBUTARIO_NAME) {
            this.NUMERO_REGISTRO_TRIBUTARIO_NAME = NUMERO_REGISTRO_TRIBUTARIO_NAME;
        }

        /**
         * @return the CORREO
         */
        public String getCORREO() {
            return CORREO;
        }

        /**
         * @param CORREO the CORREO to set
         */
        public void setCORREO(String CORREO) {
            this.CORREO = CORREO;
        }
        
        
    }
    
    public class ExceptionXls{
        private int fila=0;
        private String message="";

        /**
         * @return the fila
         */
        public int getFila() {
            return fila;
        }

        /**
         * @param fila the fila to set
         */
        public void setFila(int fila) {
            this.fila = fila;
        }

        /**
         * @return the message
         */
        public String getMessage() {
            return message;
        }

        /**
         * @param message the message to set
         */
        public void setMessage(String message) {
            this.message = message;
        }
    }
    
    public static void main(String args[]) throws DatatypeConfigurationException, IOException, BiffException{
        String fecha="26/01/2018";
        WorkbookSettings ws = new WorkbookSettings();
        ws.setEncoding("ISO-8859-1");

        Workbook archivoExcel = Workbook.getWorkbook(new File("C:\\Users\\user\\Desktop\\Instalador V3 plus\\Aldeas\\Repositorio\\out\\PR_2.xls"),ws); 
        
        Sheet hoja = archivoExcel.getSheet(0);
        fecha=hoja.getCell(18,2).getContents();
        String temp[]=fecha.split("/");
        if(temp[2].length()==2){
            fecha=temp[0]+"/"+temp[1]+"/20"+temp[2];
        }
        System.out.println(fecha);
        System.out.println(DateManage.dateToXMLGregorianCalendar(DateManage.stringToDate(fecha, "dd/MM/yyyy")));
    }
}
