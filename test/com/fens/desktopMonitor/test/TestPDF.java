/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.test;

import com.cmm.cvs2xml.CmmCvsConvert;
import com.cmm.cvs2xml.bean.FacturaDatos;
import com.cmm.cvs2xml.bo.FacturaDatosBO;
import com.cmm.cvs2xml.util.DateManage;
import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.bo.AccionComprobanteBO;
import com.fens.desktopMonitor.bo.ComprobanteFiscalBO;
import com.fens.desktopMonitor.bo.EmisorBO;
import com.fens.desktopMonitor.bo.UbicacionFiscalBO;
import com.fens.desktopMonitor.bo.comprobante.Cfd33BO;
import com.fens.desktopMonitor.bo.comprobante.UtilNombraArchivosBO;
import com.fens.desktopMonitor.dto.Accioncomprobante;
import com.fens.desktopMonitor.dto.Emisor;
import com.fens.desktopMonitor.dto.Ubicacionfiscal;
import com.fens.desktopMonitor.jdbc.ResourceManager;
import com.fens.desktopMonitor.util.FileManage;
import com.fens.desktopMonitor.util.GenericMethods;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import jxl.CellView;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import mx.bigdata.sat.cfdiv.CFDv33;
import mx.bigdata.sat.security.KeyLoader2;

/**
 *
 * @author ISCesar
 */
public class TestPDF {
    private Connection conn=null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        new TestPDF().check();
    }
    
    
    public void check(){
        try{
             WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("ISO-8859-1");

            Workbook archivoExcel = Workbook.getWorkbook(new File("C:\\Users\\user\\AppData\\Roaming\\Skype\\My Skype Received Files\\lay out completo.xls"),ws); 
        
            Sheet hoja = archivoExcel.getSheet(0);
            archivoExcel = Workbook.getWorkbook(new File("C:\\Users\\user\\Desktop\\Instalador V3 plus\\Aldeas\\Repositorio\\PDF_2\\Report.xls"),ws); 
            Sheet hoja2 = archivoExcel.getSheet(0);
            int count=0;
            
            ByteArrayOutputStream bos= new ByteArrayOutputStream();
            WritableWorkbook libro = Workbook.createWorkbook(bos);
            libro.createSheet("RESUMEN", 0);
            CellView view = new CellView();
            view.setAutosize(true);
            WritableSheet hojaCatalogo = libro.getSheet(0);
            for(int i=2;i<hoja.getRows();i++){
                boolean exist=false;
                for(int j=2;j<hoja2.getRows();j++){
                    //System.out.println("Row: "+hoja.getCell(1, i).getContents()+"--"+hoja2.getCell(2, j).getContents());
                    if(hoja.getCell(1, i).getContents().equals(hoja2.getCell(2, j).getContents())&&hoja.getCell(2, i).getContents().equals(hoja2.getCell(3, j).getContents())){
                        exist=true;
                        break;
                    }
                }
                if(!exist){
                    for(int p=0;p<19;p++){
                        Label contentTxt = null;
                        contentTxt = new Label(p, count, hoja.getCell(p, i).getContents());
                        hojaCatalogo.addCell(contentTxt);
                        hojaCatalogo.setColumnView(p, view);
                    }
                    count++;
                    System.out.println("Row: "+i);
                }
            }
            libro.write();
            libro.close();
            System.out.println(count);
            FileOutputStream fos = new FileOutputStream (new File("faltantes.xls"));
            bos.writeTo(fos);
            //29511796237015795596963
            /*File[] list=new File("C:\\Users\\user\\Desktop\\Instalador V3 plus\\Aldeas\\Repositorio\\out\\purebas_2").listFiles();
            for(File item:list){
                if(item.getName().endsWith(".xml")){
                    Cfd33BO cfd33BO = new Cfd33BO(item, Cfd33BO.CONTEXT_ARRAY_COMPLEMENTOS);
                    boolean exist=false;
                    int row=0;
                    for(int i=2;i<hoja.getRows();i++){
                        String rfc=hoja.getCell(1,i).getContents();
                        String folio=hoja.getCell(14,i).getContents();

                            System.out.println("Row: "+i+" RFC: "+rfc+" Folio: "+folio.trim());
                            if(cfd33BO.getComprobanteFiscal().getReceptor().getRfc().equals(rfc)&&cfd33BO.getComprobanteFiscal().getFolio().equals(folio.trim())){
                                exist=true;
                                row=i;
                                break;
                            }

                    }
                    if(!exist){
                        System.out.println("-- Row: "+row+" RFC: "+cfd33BO.getComprobanteFiscal().getReceptor().getRfc()+" Folio: "+cfd33BO.getComprobanteFiscal().getFolio());
                    }
                }
            }*/
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void runD(){
        try{
            File[] list=new File("C:\\Users\\user\\Desktop\\Instalador V3 plus\\Aldeas\\Repositorio\\out\\purebas_2").listFiles();
            for(File item:list){
                if(item.getName().endsWith(".xml")){
                    Cfd33BO cfd33BO = new Cfd33BO(item, Cfd33BO.CONTEXT_ARRAY_COMPLEMENTOS);
                    cfd33BO.setComentarios("DONATIVO BASE SOCIAL PENDIENTE POR FACTURAR EJERCICIO 2017.");
                    ByteArrayOutputStream baosPDF;
                    baosPDF = cfd33BO.toPdf();
                    File archivoComprobanteFiscalExitoPDF = null;
                    archivoComprobanteFiscalExitoPDF = FileManage.createFileFromByteArray(baosPDF.toByteArray(),
                                         "C:\\Users\\user\\Desktop\\Instalador V3 plus\\Aldeas\\Repositorio\\PDF_2\\", 
                                         item.getName().substring(0, item.getName().length()-4)+".pdf");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    /*public void run() throws IllegalArgumentException, Exception{
        CmmCvsConvert cmmCsvConvert = new CmmCvsConvert();
        cmmCsvConvert.convertFile(new File("C:\\Users\\user\\Desktop\\Instalador V3 plus\\repositorio\\in\\pruebas\\Nota00056kuku.CSV"));
        if (cmmCsvConvert.getListaFacturaDatos().size()>0){
            for (FacturaDatos facturaDatos : cmmCsvConvert.getListaFacturaDatos()){
                
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
                comp.setFecha(DateManage.dateToXMLGregorianCalendar(new Date()));

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
                
                if (certX509!=null && pkey!=null){
                    CFDv33 cFDv33 = creaCFD(comp);
                    //Sellamos comprobante con CSD emisor
                    try{
                        cFDv33.sellar(pkey, certX509);
                    }catch(Exception ex){
                        throw new Exception("Error en sellado de XML traducido: " + ex.getMessage());
                    }
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    cFDv33.guardar(baos);
                    System.out.println(new String(baos.toByteArray()));
                    //Validamos estructura y verificamos XML
                    try{
                        cFDv33.validar();

                        //verificamos sellado
                        cFDv33.verificar();
                        
                        System.out.println(cFDv33.getCadenaOriginal());
                    }catch(Exception ex){
                        ex.printStackTrace();
                        throw new Exception("Error en validación de estructura de XML traducido: " + ex.getCause());
                    }

                    //Guardamos archivo XML
                    File archivoComprobanteFiscalExito = new File("C:\\Users\\user\\Desktop\\Instalador V3 plus\\repositorio\\in\\pruebas33.xml");
                    try{
                        archivoComprobanteFiscalExito = FileManage.createFileFromByteArrayOutputStream(baos,archivoComprobanteFiscalExito);
                    }catch(Exception e){}
                    File archivoComprobanteFiscalExitoPDF = new File("C:\\Users\\user\\Desktop\\Instalador V3 plus\\repositorio\\in\\pruebas33.pdf");
                    try{ 

                        Cfd33BO cfd33BO = new Cfd33BO(archivoComprobanteFiscalExito, Cfd33BO.CONTEXT_ARRAY_COMPLEMENTOS);
                        ByteArrayOutputStream baosPDF;

                        Accioncomprobante accionComprobanteDto = buscaAccionPersonalizada(facturaDatos);
                        cfd33BO.setFacturaDatos(facturaDatos);
                        cfd33BO.setAccionComprobante(accionComprobanteDto);
                        baosPDF = cfd33BO.toPdf();
                        System.out.println("generado");
                        if(baosPDF==null){
                            System.out.println("null");
                        }
                        archivoComprobanteFiscalExitoPDF = FileManage.createFileFromByteArrayOutputStream(baosPDF,archivoComprobanteFiscalExitoPDF);
                    }catch(Exception ex){
                        ex.printStackTrace();
                        //throw new Exception("Error inesperado crear representacion impresa de Comprobante Fiscal. " + ex.toString());
                    }
                }else{
                    System.out.println("error");
                }
            }
        }else{
            System.out.println("error 1");
        }
    }
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
        }
        
        //Regimen Fiscal
            /*Comprobante.Emisor.RegimenFiscal regimen = new Comprobante.Emisor.RegimenFiscal();
            regimen.setRegimen(emisorDto.getRegimenfiscal());
        comp.getEmisor().setRegimenFiscal(emisorDto.getRegimenfiscal());
        
    }
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
            }
            
            //Si al menos contamos con un CodigoPostal de sucursal (ExpedidoEn)
            // o una Clave de Acción, buscamos el registro
            if (!StringManage.getValidString(claveAccion).equals("")
                    || !StringManage.getValidString(codigoPostalSucursal).equals("")){
                
                AccionComprobanteBO accionComprobanteBO = new AccionComprobanteBO(conn);
                accionComprobanteDto = accionComprobanteBO.findUniqueAccionByClaveOrCP(claveAccion, codigoPostalSucursal);
                
                if (accionComprobanteDto==null
                        && !StringManage.getValidString(claveAccion).equals("")){
                    //actualizarLogVista("Acción personalizada '" + claveAccion + "' no existe en Base de datos Local.");
                }
                
            }
        }catch(Exception ex){
            ex.printStackTrace();
                //actualizarLogVista("Error al recuperar Acción personalizada: " + ex.toString());
        }
        return accionComprobanteDto;
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
    }*/
}
