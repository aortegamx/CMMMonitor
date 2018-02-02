/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.bo.comprobante;

import com.cmm.cvs2xml.retenciones.bean.RetencionesDatos;
import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.bo.ConfiguracionBO;
import com.fens.desktopMonitor.bo.EmisorBO;
import com.fens.desktopMonitor.dto.Accioncomprobante;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.dto.Emisor;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.util.FacturacionUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.infosoft.fechas.FormateadorDeFechas;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import mx.bigdata.sat.retencion.CFDIRetencionv10;
import mx.bigdata.sat.retencion.TFDv1cRet10;
import mx.bigdata.sat.retencion.common.dividendos.schema.Dividendos;
import mx.bigdata.sat.retencion.common.pagosaextranjeros.schema.Pagosaextranjeros;
import mx.bigdata.sat.retencion.v1.schema.Retenciones;
import mx.bigdata.sat.retencion.v1.schema.TimbreFiscalDigital;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author 578
 */
public class CfdRetenciones10BO {
    
    private Retenciones comprobanteRetenciones = null;
    private CFDIRetencionv10 cfd = null;
    private TimbreFiscalDigital timbreFiscalDigital =null;
    private TFDv1cRet10 tFD =null;

    private RetencionesDatos retencionesDatos = null; //Opcional
    private Accioncomprobante accionComprobante = null; //Opcional
    
    private Connection conn = Conexion.getConn();
    
   ConfiguracionBO configuracionBO = new ConfiguracionBO(conn,1); //Enviamos 1 por default ya que solo se guarda 1 registro de config del sistema.
   Configuracion configuracionDto = configuracionBO.getConfiguracion();   
    
    public int tipoComprobanteLeido = 5; // 5 Retenciones
    String nombreTipoComp = "RETENCIONES";
    String representacionImpresa = "Retenciones_carta"; //variable para obtener la representacion impresa a mostrar
    
    //Colecciones de datos que iran en el apartado de detail del jasper
    JRBeanCollectionDataSource ds = null;
    
    public final static String CONTEXT_COMPLEMENTO_ARRENDAMIENTOENFIDEICOMISO = "mx.bigdata.sat.retencion.common.arrendamientoenfideicomiso.schema";
    public final static String CONTEXT_COMPLEMENTO_DIVIDENDOS = "mx.bigdata.sat.retencion.common.dividendos.schema";
    public final static String CONTEXT_COMPLEMENTO_PAGOSAEXTRANJEROS = "mx.bigdata.sat.retencion.common.pagosaextranjeros.schema";
    public final static String CONTEXT_ADDENDA_SANOFI_V1 = "mx.bigdata.sat.addenda.sanofi.schema";
    public final static String CONTEXT_ADDENDA_VW_PMT_V1 = "mx.bigdata.sat.addenda.vwpmt.schema";
    public final static String CONTEXT_ADDENDA_CHRYSLER_PUA_V1 = "mx.bigdata.sat.addenda.chryslerpua.schema";
    public final static String[] CONTEXT_ARRAY_COMPLEMENTOS = {CONTEXT_COMPLEMENTO_ARRENDAMIENTOENFIDEICOMISO, 
                                                    CONTEXT_COMPLEMENTO_DIVIDENDOS,
                                                    CONTEXT_COMPLEMENTO_PAGOSAEXTRANJEROS};
    public final static String[] CONTEXT_ARRAY_ALL = {CONTEXT_COMPLEMENTO_ARRENDAMIENTOENFIDEICOMISO, 
                                                    CONTEXT_COMPLEMENTO_DIVIDENDOS,
                                                    CONTEXT_COMPLEMENTO_PAGOSAEXTRANJEROS,
                                                    CONTEXT_ADDENDA_SANOFI_V1,
                                                    CONTEXT_ADDENDA_VW_PMT_V1,
                                                    CONTEXT_ADDENDA_CHRYSLER_PUA_V1};
    
    public final static String[] NAMESPACE_AND_PREFIX_COMPLEMENTO_ARRENDAMIENTOENFIDEICOMISO = {"http://www.sat.gob.mx/esquemas/retencionpago/1/arrendamientoenfideicomiso", "arrendamientoenfideicomiso"};
    public final static String[] NAMESPACE_AND_PREFIX_COMPLEMENTO_DIVIDENDOS = {"http://www.sat.gob.mx/esquemas/retencionpago/1/dividendos", "dividendos"};
    public final static String[] NAMESPACE_AND_PREFIX_COMPLEMENTO_PAGOSAEXTRANJEROS = {"http://www.sat.gob.mx/esquemas/retencionpago/1/pagosaextranjeros", "pagosaextranjeros"};
    public final static String[] NAMESPACE_AND_PREFIX_ADDENDA_SANOFI_V1 = {"https://mexico.sanofi.com/schemas", "sanofi"};
    public final static String[] NAMESPACE_AND_PREFIX_ADDENDA_VW_PMT_V1 = {"http://www.vwnovedades.com/volkswagen/kanseilab/shcp/2009/Addenda/PMT", "PMT"};
    public final static List<String[]> NAMESPACE_AND_PREFIXES_COMPLEMENTOS = new ArrayList<String[]>(Arrays.asList(
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_ARRENDAMIENTOENFIDEICOMISO,
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_DIVIDENDOS,
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_PAGOSAEXTRANJEROS));
    public final static List<String[]> NAMESPACE_AND_PREFIXES_ALL = new ArrayList<String[]>(Arrays.asList(
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_ARRENDAMIENTOENFIDEICOMISO,
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_DIVIDENDOS,
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_PAGOSAEXTRANJEROS,
                                NAMESPACE_AND_PREFIX_ADDENDA_SANOFI_V1,
                                NAMESPACE_AND_PREFIX_ADDENDA_VW_PMT_V1));
    
    
    private Pagosaextranjeros pagosaextranjeros = null;
    private List<Retenciones.Totales.ImpRetenidos> resultado = new ArrayList<Retenciones.Totales.ImpRetenidos>();
    
     //public Cfd32BO(File file, List<String[]> namespaceAndPrefixes, String... contexts ) throws Exception{
    public CfdRetenciones10BO(File file, String... contexts ) throws Exception{
            Retenciones comp = CFDIRetencionv10.newComprobante(new FileInputStream(file));
            CFDIRetencionv10 cfdAux = new CFDIRetencionv10(comp, contexts);
            cfdAux.validar(); // Valida el XML, que todos los elementos estén presentes
            cfdAux.verificar(); // Verifica un CFD ya firmado

            //Asginamos a los valores locales
            this.cfd = cfdAux;
            this.comprobanteRetenciones = (Retenciones) cfd.getComprobante().getComprobante();//comp;
            //Extraemos el timbreFiscal Digital
            this.timbreFiscalDigital = extractTFD(comprobanteRetenciones);
            
            try{
                this.tFD = new TFDv1cRet10(cfd, timbreFiscalDigital);
            }catch(Exception ex){
                ex.printStackTrace();
            }
    }
     
     /**
     * Método para extraer el TimbreFiscalDigital de un Comprobante de Retenciones v1.0
     * @param comp Comprobante
     * @return TimbreFiscalDigital obtenido del Comprobante
     */
    public TimbreFiscalDigital extractTFD(Retenciones comp){
        TimbreFiscalDigital tf = null;
        //Datos de TimbreFiscalDigital (Complemento CFDv3)
        if (comp.getComplemento()!=null){
            for (Object itemComplemento : comp.getComplemento().getAny()){
                try{
                    //Intentamos convertir el objeto que se itera a Timbre Fiscal
                    tf = (TimbreFiscalDigital) itemComplemento;
                    /* Una vez que obtuvimos todos los datos necesarios del
                       timbre fiscal terminamos el ciclo */
                    break;
                }catch(Exception e){}
            }
        }
        return tf;
    }
    
    
    public String getRfcReceptor(){
        String rfcReceptor = null;
        if (this.comprobanteRetenciones!=null){
            if (this.comprobanteRetenciones.getReceptor().getNacional()!=null){
                rfcReceptor = this.comprobanteRetenciones.getReceptor().getNacional().getRFCRecep();
            }else if (this.comprobanteRetenciones.getReceptor().getExtranjero()!=null){
                rfcReceptor = "XEXX010101000";
            }
        }
        return rfcReceptor;
    }
    
    public String getNombreRazonSocialReceptor(){
        String nombreRazonSocial = "";
        if (this.comprobanteRetenciones!=null){
            if (this.comprobanteRetenciones.getReceptor().getNacional()!=null){
                nombreRazonSocial = StringManage.getValidString(this.comprobanteRetenciones.getReceptor().getNacional().getNomDenRazSocR());
            }else if (this.comprobanteRetenciones.getReceptor().getExtranjero()!=null){
                nombreRazonSocial = this.comprobanteRetenciones.getReceptor().getExtranjero().getNomDenRazSocR();
            }
        }
        return nombreRazonSocial;
    }
    
    /**
     * Crea representación impresa de Comprobante Fiscal de Retenciones (XML)
     * @return ByteArrayOutputStream con contenido de PDF creado
     * @throws Exception 
     */
    /*
    public ByteArrayOutputStream toPdf() throws Exception{
        ByteArrayOutputStream baos = null;
        
        return null;
    }*/
    public ByteArrayOutputStream toPdf() throws Exception {
        ByteArrayOutputStream baos = null;

        //Recuperamos datos de Complementos
        //Declaracion de objetos Complementos
        //...otras declaraciones de complementos aquí
        //Recuperamos complementos
        Retenciones.Complemento comp = this.comprobanteRetenciones.getComplemento();
        if (comp != null) {
            java.util.List<Object> listComplementos = comp.getAny(); //this.cfd.getComprobante().getComplementoGetAny();
            for (Object o : listComplementos) {

                //Impuestos Locales
                if (o instanceof Pagosaextranjeros) {
                    pagosaextranjeros = (Pagosaextranjeros) o;
                    System.out.println("_______ SE ENCONTRO PAGOS A EXTRANJEROS . . .");
                }

            //...otros complementos aquí
            }
        }

        //Mapeamos los impuestos retenidos.     
        if (this.comprobanteRetenciones.getTotales()!=null){
            for (Retenciones.Totales.ImpRetenidos item : this.comprobanteRetenciones.getTotales().getImpRetenidos()) {
                resultado.add(item);
            }
        }
        
        //enviamos que la coleccion sera de tipo concepto
        if(resultado.size() > 0){
            ds = new JRBeanCollectionDataSource(resultado);
        }else{
            Retenciones.Totales.ImpRetenidos enBlancoParaPintarEnPdf = new Retenciones.Totales.ImpRetenidos();
            enBlancoParaPintarEnPdf.setBaseRet(BigDecimal.ZERO);
            enBlancoParaPintarEnPdf.setImpuesto("---");
            enBlancoParaPintarEnPdf.setMontoRet(BigDecimal.ZERO);
            enBlancoParaPintarEnPdf.setTipoPagoRet("---");
            resultado.add(enBlancoParaPintarEnPdf);
            ds = new JRBeanCollectionDataSource(resultado);
        }
        

        File fileTempQRCode = null;
        try {
            Map<String, Object> parametros = new HashMap<String, Object>();

            parametros.put("RETENCION_CLAVE", this.comprobanteRetenciones.getCveRetenc());
            parametros.put("RETENCION_DESCRIPCION", this.comprobanteRetenciones.getDescRetenc());
            
            if(this.comprobanteRetenciones.getComplemento() != null){
                java.util.List<Object> listComplementos = this.comprobanteRetenciones.getComplemento().getAny();
                Pagosaextranjeros pagosaextranjeros = null;
                Dividendos dividendos = null;
                for (Object o : listComplementos) {
                    //Pago a extranjeros
                    if (o instanceof Pagosaextranjeros) {
                      pagosaextranjeros = (Pagosaextranjeros) o;
                      System.out.println("_______ SE ENCONTRO PAGOS A EXTRANJEROS . . .");
                    }
                    //Dividendos
                    if (o instanceof Dividendos) {
                        System.out.println("_______ SE ENCONTRO DIVIDENDOS . . .");
                      dividendos = (Dividendos) o;
                    }
                }
                ///*** para retencion de tipo pago extranjero:
                if(pagosaextranjeros != null){
                    try{parametros.put("RETENCION_PAGOEXTRANJERO_BENEFICIARIO", pagosaextranjeros.getBeneficiario().getNomDenRazSocB());}catch(Exception e){}
                    try{parametros.put("RETENCION_PAGOEXTRANJERO_PAISRESIDEFECFISCAL", pagosaextranjeros.getNoBeneficiario().getPaisDeResidParaEfecFisc().name());}catch(Exception e){}
                    try{parametros.put("RETENCION_PAGOEXTRANJERO_BENEDESCRIPCONCEPTO", pagosaextranjeros.getNoBeneficiario().getDescripcionConcepto());}catch(Exception e){}
                    try{parametros.put("RETENCION_PAGOEXTRANJERO_BENECONCEPTOPAGO", pagosaextranjeros.getNoBeneficiario().getConceptoPago());}catch(Exception e){}
                    try{parametros.put("RETENCION_PAGOEXTRANJERO_ESBENEFEFECTDELCOBRO", pagosaextranjeros.getEsBenefEfectDelCobro());}catch(Exception e){}
                }
                if(dividendos != null){
                    try{parametros.put("RETENCION_DIVIDENDO_TIPOSOCIEDAD", dividendos.getDividOUtil().getTipoSocDistrDiv());}catch(Exception e){}
                    try{parametros.put("RETENCION_DIVIDENDO_MONTOISRACREDITABLE", dividendos.getDividOUtil().getMontISRAcredNal() + "");}catch(Exception e){}
                    try{parametros.put("RETENCION_DIVIDENDO_MONTODIVACUMNACIONAL", dividendos.getDividOUtil().getMontDivAcumNal() + "");}catch(Exception e){}
                    try{parametros.put("RETENCION_DIVIDENDO_TIPODIVIDENDO", dividendos.getDividOUtil().getCveTipDivOUtil());}catch(Exception e){}
                    try{parametros.put("RETENCION_DIVIDENDO_MONTRETEXTDIVEXT", dividendos.getDividOUtil().getMontRetExtDivExt() + "");}catch(Exception e){}
                    try{parametros.put("RETENCION_DIVIDENDO_MONTISRACREDRETMEXICO", dividendos.getDividOUtil().getMontISRAcredRetMexico() + "");}catch(Exception e){}
                    try{parametros.put("RETENCION_DIVIDENDO_MONTISRACREDRETEXTRANJERO", dividendos.getDividOUtil().getMontISRAcredRetExtranjero() + "");}catch(Exception e){}
                    try{parametros.put("RETENCION_DIVIDENDO_MONTDIVACUMEXT", dividendos.getDividOUtil().getMontDivAcumExt() + "");}catch(Exception e){}      
                }

                ///***
            }

            parametros.put("FECHA_EXPEDICION", FormateadorDeFechas.getFecha(this.comprobanteRetenciones.getFechaExp().getTime(), FormateadorDeFechas.FORMATO_DDMMYYYYHHMMSS));

            if (this.comprobanteRetenciones.getFolioInt() != null) {
                parametros.put("FOLIO_GENERADO", this.comprobanteRetenciones.getFolioInt());
            }

            parametros.put("CLIENTE_NOMBRE", getNombreRazonSocialReceptor());
            parametros.put("CLIENTE_RFC", getRfcReceptor());
            parametros.put("CLIENTE_NACIONALIDAD", this.comprobanteRetenciones.getReceptor().getNacionalidad());
            if (this.comprobanteRetenciones.getReceptor().getNacional()!=null
                    && this.comprobanteRetenciones.getReceptor().getNacional().getCURPR()!=null){
                parametros.put("CLIENTE_CURP", this.comprobanteRetenciones.getReceptor().getNacional().getCURPR());
            }
            if (this.comprobanteRetenciones.getReceptor().getExtranjero()!=null
                    && this.comprobanteRetenciones.getReceptor().getExtranjero().getNumRegIdTrib()!=null){
                parametros.put("CLIENTE_EXT_NRID", this.comprobanteRetenciones.getReceptor().getExtranjero().getNumRegIdTrib());
            }

            //DATOS DE PERIODO
            parametros.put("PERIODO_MES_INI", this.comprobanteRetenciones.getPeriodo().getMesIni());
            parametros.put("PERIODO_MES_FIN", this.comprobanteRetenciones.getPeriodo().getMesFin());
            parametros.put("PERIODO_EJERC", this.comprobanteRetenciones.getPeriodo().getEjerc());
            
            // DATOS DE EMPRESA
            parametros.put("EMPRESA_RAZON", StringManage.getValidString(this.comprobanteRetenciones.getEmisor().getNomDenRazSocE()));
            parametros.put("EMPRESA_RFC", this.comprobanteRetenciones.getEmisor().getRFCEmisor());
            if (this.comprobanteRetenciones.getEmisor().getCURPE()!=null)
                parametros.put("EMPRESA_CURP", this.comprobanteRetenciones.getEmisor().getCURPE());

            parametros.put("NOM_TIPO_COMP", nombreTipoComp != null ? nombreTipoComp : "");
            parametros.put("TOTAL_OPERACION", this.comprobanteRetenciones.getTotales().getMontoTotOperacion().toString());
            parametros.put("TOTAL_GRAVADO", this.comprobanteRetenciones.getTotales().getMontoTotGrav().toString());
            parametros.put("TOTAL_EXENTO", this.comprobanteRetenciones.getTotales().getMontoTotExent().toString());
            parametros.put("TOTAL_RETENIDO", this.comprobanteRetenciones.getTotales().getMontoTotRet().toString());
            //parametros.put("SUBTOTAL", this.comprobanteRetenciones.getSubTotal().toString());
            parametros.put("IMPORTE_LETRA", FacturacionUtil.importeLetra(this.comprobanteRetenciones.getTotales().getMontoTotOperacion().doubleValue(), "mxn"));

            EmisorBO emisorBO = new EmisorBO();
            Emisor emisorDto = emisorBO.findEmisorbyRfc(conn, this.comprobanteRetenciones.getEmisor().getRFCEmisor());

                        //LOGO//
            //Sí hay Logo especifico para este archivo (acciones personalizadas), esta prevalece
            if (accionComprobante != null) {
                if (StringManage.getValidString(accionComprobante.getRutaLogo()).length() > 0) {
                    parametros.put("LOGO", accionComprobante.getRutaLogo());
                }
            }
            //De lo contrario, buscamos por logo de Emisor o dejamos vacío
            if (!parametros.containsKey("LOGO")) {
                if (emisorDto.getRutalogo() != null) {
                    parametros.put("LOGO", emisorDto.getRutalogo());
                } else {// SI LA EMPRESA NO TIENE UN LOGO PROPIO no se carga ninguno                        
                    parametros.put("LOGO", "");
                }
            }

            if (this.timbreFiscalDigital.getFechaTimbrado() != null) {
                parametros.put("FECHA_IMPRESION", FormateadorDeFechas.getFecha(this.timbreFiscalDigital.getFechaTimbrado().getTime(), FormateadorDeFechas.FORMATO_DDMMYYYYHHMMSS));
            } else {
                parametros.put("FECHA_IMPRESION", "");
            }

            // DETALLE DE IMPUESTOS
            //mapeoImpuestos();
            //System.setProperty("jasper.reports.compile.class.path", "lib/jasperreports-3.7.4.jar");
            //JasperReport reporteImpuestos = (JasperReport) JRLoader.loadObject("jasper/FacturaImpuestos.jasper");
            //parametros.put("REPORTE_IMPUESTOS", reporteImpuestos);
            //parametros.put("DATOS_IMPUESTOS", new JRBeanCollectionDataSource(listImpuestos));

            QRCodeWriter qrWriter = new QRCodeWriter();
            try {
                Hashtable params = new Hashtable();
                params.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                fileTempQRCode = new File("C:/temp/" + new Date().getTime() + ".gif");
                fileTempQRCode.getParentFile().mkdirs(); //creamos carpeta en caso de que no exista en sistema
                String dato = "";
                if (this.comprobanteRetenciones.getReceptor().getNacional()!=null){
                    //Nacionalidad mexicana
                    dato = "?re=" + this.comprobanteRetenciones.getEmisor().getRFCEmisor() 
                         + "&rr=" + getRfcReceptor()
                         + "&tt=" + new DecimalFormat("0000000000.000000").format(this.comprobanteRetenciones.getTotales().getMontoTotOperacion().doubleValue())
                         + "&id=" + this.timbreFiscalDigital.getUUID();
                }else if (this.comprobanteRetenciones.getReceptor().getExtranjero()!=null){
                    //nacionalidad Extranjera
                    dato = "?re=" + this.comprobanteRetenciones.getEmisor().getRFCEmisor() 
                         + "&nr=" + StringManage.getValidString(this.comprobanteRetenciones.getReceptor().getExtranjero().getNumRegIdTrib())
                         + "&tt=" + new DecimalFormat("0000000000.000000").format(this.comprobanteRetenciones.getTotales().getMontoTotOperacion().doubleValue())
                         + "&id=" + this.timbreFiscalDigital.getUUID();
                }
                MatrixToImageWriter.writeToFile(qrWriter.encode(dato, BarcodeFormat.QR_CODE, 625, 625), "gif", fileTempQRCode);
                parametros.put("UUID", this.timbreFiscalDigital.getUUID());
                parametros.put("QRCODE", fileTempQRCode.getAbsolutePath());
            } catch (WriterException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Información adicional 
            {
                String concatDatosAdicionales = "";
                if (this.retencionesDatos != null) {
                    if (this.retencionesDatos.getLineaDatosAdicionales() != null
                            && this.retencionesDatos.getLineaDatosAdicionales().getCamposAdicionales() != null) {
                        for (String campoAdicional : this.retencionesDatos.getLineaDatosAdicionales().getCamposAdicionales()) {
                            concatDatosAdicionales += campoAdicional + "\n";
                        }
                    }
                }
                parametros.put("DATOS_ADICIONALES", concatDatosAdicionales);
            }

            //Comentarios
            //try {
            //    if (StringManage.getValidString(this.retencionesDatos.getLineaDatosComprobante().get().getObservaciones()).length() > 0) {
            //        parametros.put("COMENTARIOS", this.retencionesDatos.getLineaDatosComprobante().getDatosComprobante().getObservaciones());
            //    }
            //} catch (Exception ex) {
            //}

            parametros.put("NO_CERTIFICADO", this.comprobanteRetenciones.getNumCert());
            parametros.put("NO_CERTIFICADO_SAT", this.timbreFiscalDigital.getNoCertificadoSAT());
            parametros.put("SELLO_DIGITAL", this.timbreFiscalDigital.getSelloCFD());
            parametros.put("SELLO_SAT", this.timbreFiscalDigital.getSelloSAT());

            parametros.put("CADENA_ORIGINAL", "||" + this.timbreFiscalDigital.getVersion() + "|" + this.timbreFiscalDigital.getUUID() + "|"
                    + FormateadorDeFechas.getFecha(this.timbreFiscalDigital.getFechaTimbrado().getTime(), FormateadorDeFechas.FORMATO_AAAAMMDDTHHMMSSS)
                    + "|" + this.timbreFiscalDigital.getSelloCFD() + "|" + this.timbreFiscalDigital.getNoCertificadoSAT() + "||");

            System.setProperty("jasper.reports.compile.class.path", "/lib/jasperreports-3.7.4.jar");

            //VALIDAMOS SI TIENE REPRESENTACION IMPRESA PERSONALIZADA:
            String representacionAuxiliar = representacionImpresa; //para ayuda por si no esta el personalizado recuperar nombre del pdf generico

            try {//pdf personalizado

                String pathPlantillaComprobante = null;
                if (representacionImpresa.equals("Retenciones_carta")) {
                    pathPlantillaComprobante = emisorDto.getPlantillaRetenciones();
                } else {
                    System.out.println("Plantilla Desconocida");
                }

                //Sí hay plantilla especifica para este archivo (acciones personalizadas), esta prevalece
                if (accionComprobante != null) {
                    if (StringManage.getValidString(accionComprobante.getPlantillaFactura()).length() > 0) {
                        pathPlantillaComprobante = accionComprobante.getPlantillaFactura();
                    }
                }

                System.out.println("__________PDF PERSONALIZADO");
                //representacionImpresa += "_"+this.comprobanteRetenciones.getEmisor().getRfc(); 
                System.out.println("________________PDF PERSONALIZADO. . . " + pathPlantillaComprobante);

                parametros.putAll(parametros);

                parametros.put(JRParameter.REPORT_LOCALE, Locale.US);
                byte[] bytes = JasperRunManager.runReportToPdf(pathPlantillaComprobante, parametros, ds);

                baos = new ByteArrayOutputStream(bytes.length);
                baos.write(bytes, 0, bytes.length);

                //Eliminamos archivo temp para codigo bidimensional
                if (fileTempQRCode != null) {
                    try {
                        fileTempQRCode.delete();
                    } catch (Exception ex) {
                    }
                }

                return baos;
            } catch (Exception e) {
                representacionImpresa = representacionAuxiliar;
                System.out.println("________________PDF generico. . . " + representacionImpresa + ".jasper");
                byte[] bytes = JasperRunManager.runReportToPdf("jasper/" + representacionImpresa + ".jasper", parametros, ds);

                baos = new ByteArrayOutputStream(bytes.length);
                baos.write(bytes, 0, bytes.length);
                            //e.printStackTrace();

                //Eliminamos archivo temp para codigo bidimensional
                if (fileTempQRCode != null) {
                    try {
                        fileTempQRCode.delete();
                    } catch (Exception ex) {
                    }
                }

                return baos;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileTempQRCode != null) {
                try {
                    fileTempQRCode.delete();
                } catch (Exception ex) {
                }
            }
        }
        //***************************************************

        return null;
    }

    public Retenciones getComprobanteRetenciones() {
        return comprobanteRetenciones;
    }

    public void setComprobanteRetenciones(Retenciones comprobanteRetenciones) {
        this.comprobanteRetenciones = comprobanteRetenciones;
    }

    public CFDIRetencionv10 getCfd() {
        return cfd;
    }

    public void setCfd(CFDIRetencionv10 cfd) {
        this.cfd = cfd;
    }

    public TimbreFiscalDigital getTimbreFiscalDigital() {
        return timbreFiscalDigital;
    }

    public void setTimbreFiscalDigital(TimbreFiscalDigital timbreFiscalDigital) {
        this.timbreFiscalDigital = timbreFiscalDigital;
    }

    public TFDv1cRet10 gettFD() {
        return tFD;
    }

    public void settFD(TFDv1cRet10 tFD) {
        this.tFD = tFD;
    }

    public RetencionesDatos getRetencionesDatos() {
        return retencionesDatos;
    }

    public void setRetencionesDatos(RetencionesDatos retencionesDatos) {
        this.retencionesDatos = retencionesDatos;
    }

    public Accioncomprobante getAccionComprobante() {
        return accionComprobante;
    }

    public void setAccionComprobante(Accioncomprobante accionComprobante) {
        this.accionComprobante = accionComprobante;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public ConfiguracionBO getConfiguracionBO() {
        return configuracionBO;
    }

    public void setConfiguracionBO(ConfiguracionBO configuracionBO) {
        this.configuracionBO = configuracionBO;
    }

    public Configuracion getConfiguracionDto() {
        return configuracionDto;
    }

    public void setConfiguracionDto(Configuracion configuracionDto) {
        this.configuracionDto = configuracionDto;
    }

    public String getNombreTipoComp() {
        return nombreTipoComp;
    }

    public void setNombreTipoComp(String nombreTipoComp) {
        this.nombreTipoComp = nombreTipoComp;
    }

    public String getRepresentacionImpresa() {
        return representacionImpresa;
    }

    public void setRepresentacionImpresa(String representacionImpresa) {
        this.representacionImpresa = representacionImpresa;
    }

    public JRBeanCollectionDataSource getDs() {
        return ds;
    }

    public void setDs(JRBeanCollectionDataSource ds) {
        this.ds = ds;
    }

}
