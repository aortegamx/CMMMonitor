/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.bo.comprobante;

import com.cmm.cvs2xml.bean.FacturaDatos;
import com.cmm.cvs2xml.retenciones.bean.RetencionesDatos;
import com.cmm.cvs2xml.util.DateManage;
import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.bo.ArchivoMaestroBO;
import com.fens.desktopMonitor.bo.ComprobanteFiscalBO;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.util.FileManage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ISCesar
 */
public class UtilNombraArchivosBO {
    
    public static final String SEQ_GUION_BAJO = "_";
    public static final String SEQ_RFC_EMISOR = "%re%";
    public static final String SEQ_RFC_RECEPTOR = "%rr%";
    public static final String SEQ_NOMBRE_ARCHIVO_ORIGINAL = "%arc%";
    public static final String SEQ_FECHA_EMISION = "%fem%";
    public static final String SEQ_FOLIO = "%folio%";
    public static final String SEQ_UUID = "%uuid%";

    public static File calcFileArchivoMaestro(Configuracion configuracionDto, int estatus, File archivoMaestro) throws Exception{
        File file = null;
        
        try{
            String rutaArchivoResultado = "";
            if (estatus == ArchivoMaestroBO.ESTATUS_EN_PROCESO)
                rutaArchivoResultado = configuracionDto.getRutacarpetaejecucion();
            if (estatus == ArchivoMaestroBO.ESTATUS_EXITO)
                rutaArchivoResultado = configuracionDto.getRutacarpetaexitosos();
            if (estatus == ArchivoMaestroBO.ESTATUS_ERROR)
                rutaArchivoResultado = configuracionDto.getRutacarpetaerrores();
            
            if (StringManage.getValidString(rutaArchivoResultado).equals("")){
                throw new Exception("Ruta a carpeta de Archivo Maestro resultado no existente. "
                        + "Revise la configuración de carpetas del aplicativo.");
            }else{
                
                if (estatus==ArchivoMaestroBO.ESTATUS_EXITO){
                    rutaArchivoResultado += File.separator +  FileManage.getFileNameWithoutExtension(archivoMaestro.getName())
                                            + ".procesado";
                }else{
                    rutaArchivoResultado += File.separator + archivoMaestro.getName();
                }
                
                file = new File(rutaArchivoResultado);
            }
            
        }catch(Exception ex){
            throw new Exception("Error al intentar generar ruta y nombre de archivo resultado. " + ex.getMessage());
        }
        
        return file;
    }
    
    
    public static File calcFileComprobanteFiscal(
                        Configuracion configuracionDto, 
                        int estatus, 
                        File archivoMaestro,
                        FacturaDatos facturaDatos,
                        RetencionesDatos retencionesDatos,
                        String uuid,
                        String extension) throws Exception{
        
        File file = null;
        
        try{
            String rutaArchivoResultado = "";
            if (estatus == ComprobanteFiscalBO.ESTATUS_TIMBRAR_EN_PROCESO)
                rutaArchivoResultado = configuracionDto.getRutacarpetaejecucion();
            if (estatus == ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO)
                rutaArchivoResultado = configuracionDto.getRutacarpetaexitosos();
            if (estatus == ComprobanteFiscalBO.ESTATUS_TIMBRAR_ERROR)
                rutaArchivoResultado = configuracionDto.getRutacarpetaerrores();
            if (estatus == ComprobanteFiscalBO.ESTATUS_CANCELAR_EN_PROCESO
                    || estatus == ComprobanteFiscalBO.ESTATUS_CANCELAR_EXITO
                    || estatus == ComprobanteFiscalBO.ESTATUS_CANCELAR_ERROR)
                rutaArchivoResultado = configuracionDto.getRutadestinocancelacionesxml();
            
            if (StringManage.getValidString(rutaArchivoResultado).equals("")){
                throw new Exception("Ruta a carpeta de Comprobante Fiscal resultado no existente. "
                        + "Revise la configuración de carpetas del aplicativo.");
            }else{
                //Logica para renombrar archivo de acuerdo a especificaciones de configuración
                String especificaciones = configuracionDto.getRenamearchivostimbrados();
                
                String archivoFinal = rutaArchivoResultado;
                
                if (estatus==ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO){
                    //Calculo de nombre de archivo de salida segun configuracion
                    archivoFinal += File.separator
                                +convertirNombreArchivo(especificaciones, 
                                    FileManage.getFileNameWithoutExtension(archivoMaestro.getName()),
                                    facturaDatos, retencionesDatos, uuid)
                                + "." + extension;
                    file = new File(archivoFinal);
                    
                    //verificamos que no exista otro archivo nombrado igual
                    int i = 1;
                    while (file.exists()){
                        archivoFinal = rutaArchivoResultado;
                        //si ya existe el nombre del archivo, asignamos un numero secuencial
                        archivoFinal += File.separator
                                    + convertirNombreArchivo(especificaciones, 
                                        FileManage.getFileNameWithoutExtension(archivoMaestro.getName()),
                                        facturaDatos, retencionesDatos, uuid)
                                    + "_" + i
                                    + "." + extension;
                        file = new File(archivoFinal);
                        i++;
                    }
                    
                }else{
                    
                    archivoFinal += File.separator
                        + FileManage.getFileNameWithoutExtension(archivoMaestro.getName())
                        + "." + extension;
                    file = new File(archivoFinal);
                    
                    //verificamos que no exista otro archivo nombrado igual
                    int i = 1;
                    while (file.exists()){
                        archivoFinal = rutaArchivoResultado;
                        //si ya existe el nombre del archivo, asignamos un numero secuencial
                        archivoFinal += File.separator
                            + FileManage.getFileNameWithoutExtension(archivoMaestro.getName())
                            + "_" + i
                            + "." + extension;
                        file = new File(archivoFinal);
                        i++;
                    }
                    
                }
                
                /*rutaArchivoResultado += "/" 
                        + facturaDatos.getLineaDatosCliente().getDatosReceptor().getReceptor().getRfc()
                        + "_" + DateManage.getDateHourString()
                        + "_" + archivoMaestro.getName() 
                        + "." + extension;*/
            }
            
        }catch(Exception ex){
            throw new Exception("Error al intentar generar ruta y nombre de archivo resultado. " + ex.getMessage());
        }
        
        return file;
    }
    
    public static File calcFileComprobanteFiscal(
                        Configuracion configuracionDto, 
                        int estatus, 
                        File archivoMaestro,
                        String rfcEmisor,
                        String rfcReceptor,
                        String folio,
                        RetencionesDatos retencionesDatos,
                        String uuid,
                        String extension) throws Exception{
        
        File file = null;
        
        try{
            String rutaArchivoResultado = "";
            if (estatus == ComprobanteFiscalBO.ESTATUS_TIMBRAR_EN_PROCESO)
                rutaArchivoResultado = configuracionDto.getRutacarpetaejecucion();
            if (estatus == ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO)
                rutaArchivoResultado = configuracionDto.getRutacarpetaexitosos();
            if (estatus == ComprobanteFiscalBO.ESTATUS_TIMBRAR_ERROR)
                rutaArchivoResultado = configuracionDto.getRutacarpetaerrores();
            if (estatus == ComprobanteFiscalBO.ESTATUS_CANCELAR_EN_PROCESO
                    || estatus == ComprobanteFiscalBO.ESTATUS_CANCELAR_EXITO
                    || estatus == ComprobanteFiscalBO.ESTATUS_CANCELAR_ERROR)
                rutaArchivoResultado = configuracionDto.getRutadestinocancelacionesxml();
            
            if (StringManage.getValidString(rutaArchivoResultado).equals("")){
                throw new Exception("Ruta a carpeta de Comprobante Fiscal resultado no existente. "
                        + "Revise la configuración de carpetas del aplicativo.");
            }else{
                //Logica para renombrar archivo de acuerdo a especificaciones de configuración
                String especificaciones = configuracionDto.getRenamearchivostimbrados();
                
                String archivoFinal = rutaArchivoResultado;
                
                if (estatus==ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO){
                    //Calculo de nombre de archivo de salida segun configuracion
                    archivoFinal += File.separator
                                +convertirNombreArchivo(especificaciones, 
                                    FileManage.getFileNameWithoutExtension(archivoMaestro.getName()),
                                    rfcEmisor, rfcReceptor, folio, retencionesDatos, uuid)
                                + "." + extension;
                    file = new File(archivoFinal);
                    
                    //verificamos que no exista otro archivo nombrado igual
                    int i = 1;
                    while (file.exists()){
                        archivoFinal = rutaArchivoResultado;
                        //si ya existe el nombre del archivo, asignamos un numero secuencial
                        archivoFinal += File.separator
                                    + convertirNombreArchivo(especificaciones, 
                                        FileManage.getFileNameWithoutExtension(archivoMaestro.getName()),
                                        rfcEmisor, rfcReceptor, folio, retencionesDatos, uuid)
                                    + "_" + i
                                    + "." + extension;
                        file = new File(archivoFinal);
                        i++;
                    }
                    
                }else{
                    
                    archivoFinal += File.separator
                        + FileManage.getFileNameWithoutExtension(archivoMaestro.getName())
                        + "." + extension;
                    file = new File(archivoFinal);
                    
                    //verificamos que no exista otro archivo nombrado igual
                    int i = 1;
                    while (file.exists()){
                        archivoFinal = rutaArchivoResultado;
                        //si ya existe el nombre del archivo, asignamos un numero secuencial
                        archivoFinal += File.separator
                            + FileManage.getFileNameWithoutExtension(archivoMaestro.getName())
                            + "_" + i
                            + "." + extension;
                        file = new File(archivoFinal);
                        i++;
                    }
                    
                }
                
                /*rutaArchivoResultado += "/" 
                        + facturaDatos.getLineaDatosCliente().getDatosReceptor().getReceptor().getRfc()
                        + "_" + DateManage.getDateHourString()
                        + "_" + archivoMaestro.getName() 
                        + "." + extension;*/
            }
            
        }catch(Exception ex){
            throw new Exception("Error al intentar generar ruta y nombre de archivo resultado. " + ex.getMessage());
        }
        
        return file;
    }
    
    
    public static String convertirNombreArchivo(
            String secuenciaRenombrar, 
            String nombreArchivoOriginal, 
            FacturaDatos facturaDatos,
            RetencionesDatos retencionesDatos,
            String uuid){
        
        String nombreArchivoConvertido  = nombreArchivoOriginal;
        
        try{
            String rfcEmisor =  null; 
            String rfcReceptor = null;
            String fechaEmision = null;
            String folio = null;
            
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            fechaEmision = dateFormat.format(date);
            
            if (facturaDatos!=null){
                rfcEmisor = facturaDatos.getLineaDatosCliente().getRfcEmisor();
                rfcReceptor = facturaDatos.getLineaDatosCliente().getDatosReceptor().getReceptor().getRfc();
            
                folio = StringManage.getValidString(facturaDatos.getLineaDatosFactura().getDatosComprobante().getComprobante().getFolio());
            }
            
            if (retencionesDatos!=null){
                rfcEmisor = retencionesDatos.getLineaDatosComprobante().getRetenciones().getEmisor().getRFCEmisor();
                if (retencionesDatos.getLineaDatosReceptor().getReceptor().getNacional()!=null){
                    rfcReceptor = retencionesDatos.getLineaDatosReceptor().getReceptor().getNacional().getRFCRecep();
                }else{
                    rfcReceptor = "XEXX010101000"; 
                }
            
                folio = StringManage.getValidString(retencionesDatos.getLineaDatosComprobante().getRetenciones().getFolioInt());
            }
            
            if (StringManage.getValidString(secuenciaRenombrar).equals("")){
                //Si no hay secuencia establecida por el usuario, se usa por defecto
                nombreArchivoConvertido = nombreArchivoOriginal
                        + "_" + DateManage.getDateHourString();
            }else{
                String auxNombre = secuenciaRenombrar;
                
                auxNombre = auxNombre.replace(SEQ_RFC_EMISOR, (rfcEmisor!=null?rfcEmisor:""));
                auxNombre = auxNombre.replace(SEQ_RFC_RECEPTOR, (rfcReceptor!=null?rfcReceptor:""));
                auxNombre = auxNombre.replace(SEQ_NOMBRE_ARCHIVO_ORIGINAL, (nombreArchivoOriginal!=null?nombreArchivoOriginal:""));
                auxNombre = auxNombre.replace(SEQ_FECHA_EMISION, (fechaEmision!=null?fechaEmision:""));
                auxNombre = auxNombre.replace(SEQ_FOLIO, (folio!=null?folio:""));
                auxNombre = auxNombre.replace(SEQ_UUID, (uuid!=null?uuid:""));
                
                nombreArchivoConvertido = auxNombre;
            }
            
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return nombreArchivoConvertido;
        
    }
    
    public static String convertirNombreArchivo(
            String secuenciaRenombrar, 
            String nombreArchivoOriginal,
            String rfcEmisor,
            String rfcReceptor,
            String folio,
            RetencionesDatos retencionesDatos,
            String uuid){
        
        String nombreArchivoConvertido  = nombreArchivoOriginal;
        
        try{
            
            String fechaEmision = null;
            
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            fechaEmision = dateFormat.format(date);
            
            if (retencionesDatos!=null){
                rfcEmisor = retencionesDatos.getLineaDatosComprobante().getRetenciones().getEmisor().getRFCEmisor();
                if (retencionesDatos.getLineaDatosReceptor().getReceptor().getNacional()!=null){
                    rfcReceptor = retencionesDatos.getLineaDatosReceptor().getReceptor().getNacional().getRFCRecep();
                }else{
                    rfcReceptor = "XEXX010101000"; 
                }
            
                folio = StringManage.getValidString(retencionesDatos.getLineaDatosComprobante().getRetenciones().getFolioInt());
            }
            
            if (StringManage.getValidString(secuenciaRenombrar).equals("")){
                //Si no hay secuencia establecida por el usuario, se usa por defecto
                nombreArchivoConvertido = nombreArchivoOriginal
                        + "_" + DateManage.getDateHourString();
            }else{
                String auxNombre = secuenciaRenombrar;
                
                auxNombre = auxNombre.replace(SEQ_RFC_EMISOR, (rfcEmisor!=null?rfcEmisor:""));
                auxNombre = auxNombre.replace(SEQ_RFC_RECEPTOR, (rfcReceptor!=null?rfcReceptor:""));
                auxNombre = auxNombre.replace(SEQ_NOMBRE_ARCHIVO_ORIGINAL, (nombreArchivoOriginal!=null?nombreArchivoOriginal:""));
                auxNombre = auxNombre.replace(SEQ_FECHA_EMISION, (fechaEmision!=null?fechaEmision:""));
                auxNombre = auxNombre.replace(SEQ_FOLIO, (folio!=null?folio:""));
                auxNombre = auxNombre.replace(SEQ_UUID, (uuid!=null?uuid:""));
                
                nombreArchivoConvertido = auxNombre;
            }
            
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return nombreArchivoConvertido;
        
    }
    
    public static File calcFileComprobanteFiscal(
                        Configuracion configuracionDto, 
                        int estatus, 
                        File archivoMaestro,
                        String extension) throws Exception{
        
        File file = null;
        
        try{
            String rutaArchivoResultado = "";
            if (estatus == ComprobanteFiscalBO.ESTATUS_TIMBRAR_EN_PROCESO)
                rutaArchivoResultado = configuracionDto.getRutacarpetaejecucion();
            if (estatus == ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO)
                rutaArchivoResultado = configuracionDto.getRutacarpetaexitosos();
            if (estatus == ComprobanteFiscalBO.ESTATUS_TIMBRAR_ERROR)
                rutaArchivoResultado = configuracionDto.getRutacarpetaerrores();
            if (estatus == ComprobanteFiscalBO.ESTATUS_CANCELAR_EN_PROCESO
                    || estatus == ComprobanteFiscalBO.ESTATUS_CANCELAR_EXITO
                    || estatus == ComprobanteFiscalBO.ESTATUS_CANCELAR_ERROR)
                rutaArchivoResultado = configuracionDto.getRutadestinocancelacionesxml();
            
            if (StringManage.getValidString(rutaArchivoResultado).equals("")){
                throw new Exception("Ruta a carpeta de Comprobante Fiscal resultado no existente. "
                        + "Revise la configuración de carpetas del aplicativo.");
            }else{
                //Logica para renombrar archivo de acuerdo a especificaciones de configuración
                String especificaciones = configuracionDto.getRenamearchivostimbrados();
                
                String archivoFinal = rutaArchivoResultado;
                
                if (estatus==ComprobanteFiscalBO.ESTATUS_TIMBRAR_EXITO){
                    //Calculo de nombre de archivo de salida segun configuracion
                    archivoFinal += File.separator
                                +convertirNombreArchivo(especificaciones, 
                                    FileManage.getFileNameWithoutExtension(archivoMaestro.getName()))
                                + "." + extension;
                    file = new File(archivoFinal);
                    
                    //verificamos que no exista otro archivo nombrado igual
                    int i = 1;
                    while (file.exists()){
                        archivoFinal = rutaArchivoResultado;
                        //si ya existe el nombre del archivo, asignamos un numero secuencial
                        archivoFinal += File.separator
                                    + convertirNombreArchivo(especificaciones, 
                                        FileManage.getFileNameWithoutExtension(archivoMaestro.getName()))
                                    + "_" + i
                                    + "." + extension;
                        file = new File(archivoFinal);
                        i++;
                    }
                    
                }else{
                    
                    archivoFinal += File.separator
                        + FileManage.getFileNameWithoutExtension(archivoMaestro.getName())
                        + "." + extension;
                    file = new File(archivoFinal);
                    
                    //verificamos que no exista otro archivo nombrado igual
                    int i = 1;
                    while (file.exists()){
                        archivoFinal = rutaArchivoResultado;
                        //si ya existe el nombre del archivo, asignamos un numero secuencial
                        archivoFinal += File.separator
                            + FileManage.getFileNameWithoutExtension(archivoMaestro.getName())
                            + "_" + i
                            + "." + extension;
                        file = new File(archivoFinal);
                        i++;
                    }
                    
                }
                
                /*rutaArchivoResultado += "/" 
                        + facturaDatos.getLineaDatosCliente().getDatosReceptor().getReceptor().getRfc()
                        + "_" + DateManage.getDateHourString()
                        + "_" + archivoMaestro.getName() 
                        + "." + extension;*/
            }
            
        }catch(Exception ex){
            throw new Exception("Error al intentar generar ruta y nombre de archivo resultado. " + ex.getMessage());
        }
        
        return file;
    }
	
	
	public static String convertirNombreArchivo(
            String secuenciaRenombrar, 
            String nombreArchivoOriginal){
        
        String nombreArchivoConvertido  = nombreArchivoOriginal;
        
        try{
            //Si no hay secuencia establecida por el usuario, se usa por defecto
            nombreArchivoConvertido = nombreArchivoOriginal
                    + "_" + DateManage.getDateHourString();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return nombreArchivoConvertido;
        
    }
    
}
