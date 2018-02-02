/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fens.desktopMonitor.bo.comprobante;

import com.cmm.cvs2xml.bean.FacturaDatos;
import com.cmm.cvs2xml.bean.LineaDatosConcepto;
import com.cmm.cvs2xml.util.DateManage;
import com.cmm.cvs2xml.util.StringManage;
import com.fens.desktopMonitor.beans.ConceptoPersonalizadoMonitor;
import com.fens.desktopMonitor.beans.Impuesto;
import com.fens.desktopMonitor.bo.ConfiguracionBO;
import com.fens.desktopMonitor.bo.EmisorBO;
import com.fens.desktopMonitor.bo.NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO;
import com.fens.desktopMonitor.bo.UbicacionFiscalBO;
import com.fens.desktopMonitor.dto.Accioncomprobante;
import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.dto.Emisor;
import com.fens.desktopMonitor.dto.Ubicacionfiscal;
import com.fens.desktopMonitor.jdbc.Conexion;
import com.fens.desktopMonitor.util.FacturacionUtil;
import com.fens.desktopMonitor.util.FormatUtil;
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
import java.math.BigInteger;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import mx.bigdata.sat.cfdiv.CFDv33;
import mx.bigdata.sat.cfdiv.TFDv1c32;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.InformacionAduanera;
import mx.bigdata.sat.cfdi.v33.schema.TimbreFiscalDigital;
import mx.bigdata.sat.common.implocal.schema.ImpuestosLocales;
import mx.bigdata.sat.common.nomina12.schema.Nomina;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author user
 */
public class Cfd33BO {
    
    private Comprobante comprobanteFiscal = null;
    private mx.bigdata.sat.common.donat.schema.Donatarias donat=null;
    private mx.bigdata.sat.cfdi.v33.schema.Pagos pago10=null;
    private CFDv33 cfd = null;
    private TimbreFiscalDigital timbreFiscalDigital =null;
    private TFDv1c32 tFDv1 =null;
    private String comentarios=null;
    
    private FacturaDatos facturaDatos = null; //Opcional
    private Accioncomprobante accionComprobante = null; //Opcional
    
    private Connection conn = Conexion.getConn();
    
   ConfiguracionBO configuracionBO =  new ConfiguracionBO(conn,1); //Enviamos 1 por default ya que solo se guarda 1 registro de config del sistema.
   Configuracion configuracionDto = configuracionBO.getConfiguracion();   
    
    List<Impuesto> listImpuestos = null;
    
    public int tipoComprobanteLeido = 1; // 1 Factura, 2 Nomina
    String nombreTipoComp = "FACTURA";
    String representacionImpresa = "Factura_carta"; //variable para obtener la representacion impresa a mostrar
    
    int numeroConcento = 1; //TOTAL DE CONCEPTOS//variables para ver cuantos conceptos existen y se mapea al jasper de la representacion impresa para agrupar.
    int totalDeNumeroDeObjetos = 1;//TOTAL DE CONCEPTOS//variables para ver cuantos conceptos existen y se mapea al jasper de la representacion impresa para agrupar.
    
    //para los conceptos.
    List<Comprobante.Conceptos.Concepto> resultado = new ArrayList<Comprobante.Conceptos.Concepto>();
    
    List<ConceptoPersonalizadoMonitor> resultadoTipoPersonalizado = null;
    
    //Complementos
    //impuestos locales:
    ImpuestosLocales implocal = null;
    
    ///nomina:
    Nomina nomina = null;
    List<NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO> resultadoNomina = null;
    private NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO nominaCompDesc = null;
    private BigDecimal totalPercepciones = BigDecimal.ZERO;
    private BigDecimal totalDeducciones = BigDecimal.ZERO;
    ///
    
    //Colecciones de datos que iran en el apartado de detail del jasper
    JRBeanCollectionDataSource ds = null;
    
    public final static String CONTEXT_COMPLEMENTO_NOMINA = "mx.bigdata.sat.common.nomina12.schema";
    public final static String CONTEXT_COMPLEMENTO_IMPLOCAL = "mx.bigdata.sat.common.implocal.schema";
    public final static String CONTEXT_ADDENDA_SANOFI_V1 = "mx.bigdata.sat.addenda.sanofi.schema";
    public final static String CONTEXT_ADDENDA_VW_PMT_V1 = "mx.bigdata.sat.addenda.vwpmt.schema";
    public final static String CONTEXT_ADDENDA_CHRYSLER_PUA_V1 = "mx.bigdata.sat.addenda.chryslerpua.schema";
    public final static String CONTEXT_ADDENDA_GM_V13 = "mx.bigdata.sat.addenda.gm.v13.schema";
    public final static String CONTEXT_ADDENDA_FORD_FOM_V1 = "mx.bigdata.sat.addenda.fordfom.schema";
    public final static String CONTEXT_ADDENDA_CHRYSLER_PPY_V1 = "mx.bigdata.sat.addenda.chryslerppy.schema";
    public final static String CONTEXT_COMPLEMENTO_DONAT = "mx.bigdata.sat.common.donat.schema";
    
    public final static String[] CONTEXT_ARRAY_COMPLEMENTOS = {CONTEXT_COMPLEMENTO_NOMINA, 
                                                    CONTEXT_COMPLEMENTO_IMPLOCAL,
                                                    CONTEXT_COMPLEMENTO_DONAT};
    public final static String[] CONTEXT_ARRAY_ALL = {CONTEXT_COMPLEMENTO_NOMINA,
                                                    CONTEXT_COMPLEMENTO_IMPLOCAL,
                                                    CONTEXT_COMPLEMENTO_DONAT,
                                                    CONTEXT_ADDENDA_SANOFI_V1,
                                                    CONTEXT_ADDENDA_VW_PMT_V1,
                                                    CONTEXT_ADDENDA_CHRYSLER_PUA_V1,
                                                    CONTEXT_ADDENDA_GM_V13,
                                                    CONTEXT_ADDENDA_FORD_FOM_V1,
                                                    CONTEXT_ADDENDA_CHRYSLER_PPY_V1};
    
    public final static String[] NAMESPACE_AND_PREFIX_COMPLEMENTO_NOMINA = {"http://www.sat.gob.mx/nomina12", "nomina12"};
    public final static String[] NAMESPACE_AND_PREFIX_COMPLEMENTO_PAGO = {"http://www.sat.gob.mx/Pagos", "pago10"};
    public final static String[] NAMESPACE_AND_PREFIX_COMPLEMENTO_IMPLOCAL = {"http://www.sat.gob.mx/implocal", "implocal"};
    public final static String[] NAMESPACE_AND_PREFIX_ADDENDA_SANOFI_V1 = {"https://mexico.sanofi.com/schemas", "sanofi"};
    public final static String[] NAMESPACE_AND_PREFIX_ADDENDA_VW_PMT_V1 = {"http://www.vwnovedades.com/volkswagen/kanseilab/shcp/2009/Addenda/PMT", "PMT"};
    public final static String[] NAMESPACE_AND_PREFIX_ADDENDA_FORD_FOM_V1 = {"http://www.ford.com.mx/cfdi/addenda", "fomadd"};
    public final static String[] NAMESPACE_AND_PREFIX_COMPLEMENTO_DONAT = {"http://www.sat.gob.mx/donat", "donat"};
    public final static List<String[]> NAMESPACE_AND_PREFIXES_COMPLEMENTOS = new ArrayList<String[]>(Arrays.asList(
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_NOMINA,
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_IMPLOCAL,
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_PAGO,
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_DONAT));
    public final static List<String[]> NAMESPACE_AND_PREFIXES_ALL = new ArrayList<String[]>(Arrays.asList(
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_NOMINA,
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_IMPLOCAL,
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_PAGO,
                                NAMESPACE_AND_PREFIX_ADDENDA_SANOFI_V1,
                                NAMESPACE_AND_PREFIX_ADDENDA_VW_PMT_V1,
                                NAMESPACE_AND_PREFIX_ADDENDA_FORD_FOM_V1,
                                NAMESPACE_AND_PREFIX_COMPLEMENTO_DONAT));
    
     //public Cfd32BO(File file, List<String[]> namespaceAndPrefixes, String... contexts ) throws Exception{
    public Cfd33BO(File file, String... contexts ) throws Exception{
            Comprobante comp = CFDv33.newComprobante(new FileInputStream(file));
            /*CFDv32 cfdAux = new CFDv32(comp, 
                    "mx.bigdata.sat.common.nomina.schema", 
                    "mx.bigdata.sat.common.implocal.schema");*/
            CFDv33 cfdAux = new CFDv33(comp, contexts);
            cfdAux.validar(); // Valida el XML, que todos los elementos estén presentes
            cfdAux.verificar(); // Verifica un CFD ya firmado

            //Asginamos a los valores locales
            this.cfd = cfdAux;
            this.comprobanteFiscal = (Comprobante) cfd.getComprobante().getComprobante();//comp;
            //Extraemos el timbreFiscal Digital
            this.timbreFiscalDigital = extractTFD(comprobanteFiscal);
            
            try{
                this.tFDv1 = new TFDv1c32(cfd, timbreFiscalDigital);
            }catch(Exception ex){
                ex.printStackTrace();
            }
    }
     
     /**
     * Método para extraer el TimbreFiscalDigital de un Comprobante version CFDI 3.2
     * @param comp Comprobante
     * @return TimbreFiscalDigital obtenido del Comprobante
     */
    public TimbreFiscalDigital extractTFD(Comprobante comp){
        TimbreFiscalDigital tf = null;
        //Datos de TimbreFiscalDigital (Complemento CFDv3)
        if (comp.getComplemento()!=null){
            List<Comprobante.Complemento> tComp=comp.getComplemento();
            for(Comprobante.Complemento item:tComp){
                for (Object itemComplemento : item.getAny()){
                    try{
                        //Intentamos convertir el objeto que se itera a Timbre Fiscal
                        tf = (TimbreFiscalDigital) itemComplemento;
                        /* Una vez que obtuvimos todos los datos necesarios del
                           timbre fiscal terminamos el ciclo */
                        break;
                    }catch(Exception e){
                        //e.printStackTrace();
                    }
                }
            }
            for(Comprobante.Complemento item:tComp){
                for (Object itemComplemento : item.getAny()){
                    try{
                        //Intentamos convertir el objeto que se itera a Timbre Fiscal
                        donat = (mx.bigdata.sat.common.donat.schema.Donatarias) itemComplemento;
                        /* Una vez que obtuvimos todos los datos necesarios del
                           timbre fiscal terminamos el ciclo */
                        break;
                    }catch(Exception e){
                        //e.printStackTrace();
                    }
                }
            }
            for(Comprobante.Complemento item:tComp){
                for (Object itemComplemento : item.getAny()){
                    try{
                        //Intentamos convertir el objeto que se itera a Timbre Fiscal
                        pago10 = (mx.bigdata.sat.cfdi.v33.schema.Pagos) itemComplemento;
                        /* Una vez que obtuvimos todos los datos necesarios del
                           timbre fiscal terminamos el ciclo */
                        break;
                    }catch(Exception e){
                        //e.printStackTrace();
                    }
                }
            }
        }
        return tf;
    }
    
    /**
     * Crea representación impresa de Comprobante Fiscal (XML)
     *
     * @return ByteArrayOutputStream con contenido de PDF creado
     * @throws Exception 
     */
    public ByteArrayOutputStream toPdf() throws Exception{
        ByteArrayOutputStream baos = null;
        
        //Recuperamos datos de Complementos
        //Declaracion de objetos Complementos
                
        //...otras declaraciones de complementos aquí

        //Recuperamos complementos
        List<Comprobante.Complemento> comp = this.comprobanteFiscal.getComplemento();
        if (comp != null && comp.size()>0) {
          for(Comprobante.Complemento item:comp){
            java.util.List<Object> listComplementos = item.getAny(); //this.cfd.getComprobante().getComplementoGetAny();
            for (Object o : listComplementos) {

              //Impuestos Locales
              if (o instanceof ImpuestosLocales) {
                implocal = (ImpuestosLocales) o;
                System.out.println("_______ SE ENCONTRO IMPUESTOS LOCALES . . .");
              }

              //Nomina
              if (o instanceof Nomina) {
                  System.out.println("_______ SE ENCONTRO NOMINA EN EL COMPROBANTE . . .");
                nomina = (Nomina) o;
              }

              //...otros complementos aquí

            }
          }
        }
        
        //Mapeamos los conceptos.                                
        for(Comprobante.Conceptos.Concepto item : this.comprobanteFiscal.getConceptos().getConcepto()){
            //Agregamos información aduanera a la propia Descripción de Concepto para Representacion impresa
            if (item.getInformacionAduanera()!=null){
                String strAduana = "";
                for (InformacionAduanera infoAduanera :  item.getInformacionAduanera()){
                    strAduana += "\nNumero Pedimento: " + StringManage.getValidString(infoAduanera.getNumeroPedimento());
                    
                }
                item.setDescripcion(item.getDescripcion() + "\n" + strAduana);
            }
            //verificamos si contiene impuestos
            String imp_x_concepto="";
            if(item.getImpuestos()!=null){
                if(item.getImpuestos().getRetenciones()!=null){
                    int numRet=item.getImpuestos().getRetenciones().getRetencion().size();
                    int var=0;
                    for(Comprobante.Conceptos.Concepto.Impuestos.Retenciones.Retencion ret:item.getImpuestos().getRetenciones().getRetencion()){
                        if(var==numRet-1){
                            imp_x_concepto+=ret.getImpuesto()+" - "+(ret.getImpuesto().equals("002")?"IVA":(ret.getImpuesto().equals("003")?"IEPS":(ret.getImpuesto().equals("002")?"ISR":"")))+" "+ret.getImporte();
                        }else{
                            imp_x_concepto+=ret.getImpuesto()+" - "+(ret.getImpuesto().equals("002")?"IVA":(ret.getImpuesto().equals("003")?"IEPS":(ret.getImpuesto().equals("002")?"ISR":"")))+" "+ret.getImporte()+"\n";
                        }
                        var++;
                    }
                }
                if(item.getImpuestos().getTraslados()!=null){
                    int numTras=item.getImpuestos().getTraslados().getTraslado().size();
                    int var=0;
                    for(Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado tras:item.getImpuestos().getTraslados().getTraslado()){
                        if(var==numTras-1){
                            imp_x_concepto+=tras.getImpuesto()+" - "+(tras.getImpuesto().equals("002")?"IVA":(tras.getImpuesto().equals("003")?"IEPS":(tras.getImpuesto().equals("002")?"ISR":"")))+" "+tras.getImporte();
                        }else{
                            imp_x_concepto+=tras.getImpuesto()+" - "+(tras.getImpuesto().equals("002")?"IVA":(tras.getImpuesto().equals("003")?"IEPS":(tras.getImpuesto().equals("002")?"ISR":"")))+" "+tras.getImporte()+"\n";
                        }
                        var++;
                    }
                }
            }
            if(imp_x_concepto.equals("")){
                item.setNoIdentificacion("Sin impuestos");
            }else{
                item.setNoIdentificacion(imp_x_concepto);
            }
            if(facturaDatos!=null){
                if(facturaDatos.getListaLineaDatosConceptos()!=null){
                    for(LineaDatosConcepto concep:facturaDatos.getListaLineaDatosConceptos()){
                        if(item.getClaveProdServ().equals(concep.getDatosConcepto().getConcepto().getClaveProdServ())){
                            if(concep.getDatosConcepto().getDescripcionServProd()!=null&&!concep.getDatosConcepto().getDescripcionServProd().equals("")){
                                item.setClaveProdServ(item.getClaveProdServ()+"-"+concep.getDatosConcepto().getDescripcionServProd());
                            }
                        }
                    }
                }
            }
            item.setUnidad(item.getClaveUnidad()+" "+(item.getUnidad()!=null?item.getUnidad():""));
            resultado.add(item);
        }
        //enviamos que la coleccion sera de tipo concepto
        ds = new JRBeanCollectionDataSource(resultado);
        
        if(facturaDatos!=null){
            //Si existe otro tipo de configuracion para enviar cierta colección de Objetos de concepto
            if (this.facturaDatos.getLineaDatosTipoObjetoConcepto()!=null){
                if (this.facturaDatos.getLineaDatosTipoObjetoConcepto().getTipoObjetosConcepto() == 2){
                    resultadoTipoPersonalizado = new ArrayList<>();
                    mapeoConceptosTipoPersonalizadoMonitor();

                    ds = new JRBeanCollectionDataSource(resultadoTipoPersonalizado);
                }
            }
        }
        if(donat!=null){
            representacionImpresa="Recibo_donativo";
        }
        if(pago10!=null){
            representacionImpresa="Factura_carta_pago";
        }
        
        //validamos si trae el complemento de nomina, de ser asi cargamos toda la info necesaria para mapear los parametros.
        if (nomina!=null){
            //Este es un comprobante fiscal de Nómina
            tipoComprobanteLeido = 2; //2 nomina
            nombreTipoComp = "RECIBO DE NOMINA";
            representacionImpresa = "Nomina_carta";            
            resultadoNomina = new ArrayList<>();
            mapeoConceptosNomina();
            ////*volvemos a mapear los datos de los conceptos para que salga una nueva hoja:
            mapeoConceptosNominaSegundaHoja();
            ////*
            //enviamos que sera de tipo resultadoNomina la coleccion.
            ds = new JRBeanCollectionDataSource(resultadoNomina);         
        }
        if(donat!=null){
            nombreTipoComp = "DONATIVO";
        }
        
/*        //Para acceder a los datos del comprobante en general
        System.out.println(this.comprobanteFiscal.getFolio());
       // System.out.println(this.comprobanteFiscal.getEmisor);        
        System.out.println(this.comprobanteFiscal.getSerie());
        System.out.println(this.comprobanteFiscal.getEmisor().getNombre());
        if (this.comprobanteFiscal.getEmisor().getDomicilioFiscal()!=null)
            System.out.println(this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getCalle());
        
        //Para acceder a datos de timbre Fiscal Digital
        if (this.timbreFiscalDigital!=null){
            System.out.println(this.timbreFiscalDigital.getUUID());
            System.out.println(this.timbreFiscalDigital.getNoCertificadoSAT()); //No. Certificado de PAC
            //...
        }
        
        //Para acceder a datos de un complemento
        if (nomina!=null){
            System.out.println(nomina.getCURP());
        }
*/        
        
        /////////////////////**************
        File fileTemp =null;
        try {             
                Map<String, Object> parametros = new HashMap<>();

                //parametros.put("CONTADOR_CONCEPTOS",numeroConcento); //mapeo del numero de conceptos para mapearlo al jasper y agruparlos
                parametros.put("CONTADOR_CONCEPTOS",totalDeNumeroDeObjetos); //mapeo del numero de conceptos para mapearlo al jasper y agruparlos


                parametros.put("TIPO_RECIBO",this.comprobanteFiscal.getTipoDeComprobante());                       
                String metodoPago = this.comprobanteFiscal.getMetodoPago().value().equals("PUE")?"PUE-Pago en una sola exihibición":"PPD-Pago en parcialidades o diferido";
                
                parametros.put("TIPO_PAGO", metodoPago);

                /*if(this.comprobanteFiscal.getNumCtaPago() != null)
                    parametros.put("NUM_CUENTA", this.comprobanteFiscal.getNumCtaPago());*/

                parametros.put("LUGAR_FECHA_EXPEDICION", this.comprobanteFiscal.getLugarExpedicion() +", " + FormateadorDeFechas.getFecha(this.comprobanteFiscal.getFecha().toGregorianCalendar().getTime().getTime(),FormateadorDeFechas.FORMATO_AAAAMMDDTHHMMSSS));
                parametros.put("FECHA_EXPEDICION", FormateadorDeFechas.getFecha(this.comprobanteFiscal.getFecha().toGregorianCalendar().getTime().getTime(),FormateadorDeFechas.FORMATO_DDMMYYYY));                                

                
                parametros.put("REGIMEN_FISCAL", getRegimenFiscal(this.comprobanteFiscal.getEmisor().getRegimenFiscal()));

                if(this.comprobanteFiscal.getFolio()!= null){
                    parametros.put("FOLIO_GENERADO", this.comprobanteFiscal.getFolio());
                    parametros.put("SERIE_FOLIO",this.comprobanteFiscal.getSerie());
                }
                
                
                parametros.put("USO_CFDI",getUsoCfdi(this.comprobanteFiscal.getReceptor().getUsoCFDI().value()));

                //donativos por xls
                if(donat!=null){
                    parametros.put("DONATIVO_LEYENDA", donat.getLeyenda());
                    parametros.put("DONATIVO_NUMERO_AUTORIZACION", donat.getNoAutorizacion());
                    parametros.put("DONATIVO_FECHA_AUTORIZACION", FormateadorDeFechas.getFecha(donat.getFechaAutorizacion().toGregorianCalendar().getTime().getTime(),FormateadorDeFechas.FORMATO_DDMMYYYY));
                    if(comentarios!=null){
                        parametros.put("COMENTARIOS", comentarios);
                    }
                }
                //complemento de pago
                if(pago10!=null){
                    
                }
                
                //facturas relacionadas
                if(facturaDatos!=null){
                    if(facturaDatos.getListaDatosCFDIRelacionado()!=null&&facturaDatos.getListaDatosCFDIRelacionado().getDatosCFDIRelacionado().size()>0){
                        parametros.put("TIPO_RELACION", getTipoRelacion(facturaDatos.getListaDatosCFDIRelacionado().getTipoRelacion()));
                        String uuidRelacionados="";
                        for(Comprobante.CfdiRelacionados.CfdiRelacionado uuid:facturaDatos.getListaDatosCFDIRelacionado().getDatosCFDIRelacionado()){
                            uuidRelacionados+=uuid.getUUID()+", ";
                        }
                        if(uuidRelacionados.endsWith(", ")){
                            uuidRelacionados=uuidRelacionados.substring(0, uuidRelacionados.length()-2);
                        }
                        parametros.put("UUID_RELACIONADO", uuidRelacionados);
                    }
                }
                
                if(tipoComprobanteLeido==2){//si es de tipo nomina  
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try{
                        parametros.put("NOM_DEPARTAMENTO", nomina.getReceptor().getDepartamento());
                    }catch(Exception e){}
                    try{
                        parametros.put("NOM_NO_EMPLEADO", nomina.getReceptor().getNumEmpleado());
                    }catch(Exception e){}
                    //parametros.put("NOM_NOM_PUESTO", nomPuesto.getNombre());
                    try{
                        parametros.put("NOM_NO_AFILIACION", nomina.getReceptor().getNumSeguridadSocial());
                    }catch(Exception e){}
                    try{
                        String dateFechaInicial = sdf.format(nomina.getFechaInicialPago().toGregorianCalendar().getTime());
                        String dateFechaFinal = sdf.format(nomina.getFechaFinalPago().toGregorianCalendar().getTime());
                        parametros.put("NOM_PERIODO_PAGO", dateFechaInicial + " AL " + dateFechaFinal);
                    }catch(Exception e){}
                    try{
                        parametros.put("NOM_DIAS_PAGADOS", nomina.getNumDiasPagados().doubleValue());
                    }catch(Exception e){}
                    try{
                    parametros.put("NOM_SALARIO_DIARIO", nomina.getReceptor().getSalarioDiarioIntegrado().doubleValue());
                    }catch(Exception e){}
                    try{
                        parametros.put("NOM_PAGO_NETO", FormatUtil.doubleToStringPuntoComas(this.comprobanteFiscal.getTotal().doubleValue()));
                    }catch(Exception e){}
                    try{
                        parametros.put("NOM_RFC", this.comprobanteFiscal.getReceptor().getRfc());
                    }catch(Exception e){}
                    try{
                        parametros.put("NOM_NOM_PUESTO", (this.comprobanteFiscal.getReceptor().getNombre()  + ", "+ nomina.getReceptor().getPuesto()));
                    }catch(Exception e){
                        try{
                            parametros.put("NOM_NOM_PUESTO", (this.comprobanteFiscal.getReceptor().getNombre()));
                        }catch(Exception e2){}
                    }
                    try{
                        parametros.put("NOM_NOMBRE", (this.comprobanteFiscal.getReceptor().getNombre()));
                    }catch(Exception e){}
                     try{
                        parametros.put("NOM_PUESTO", (nomina.getReceptor().getPuesto()));
                    }catch(Exception e){}                            
                    parametros.put("NOM_CURP", nomina.getReceptor().getCurp());
                    //parametros.put("NOM_TIPO_CONTRATO", nomina.getReceptor().getTipoContrato());
                    //para obtener el tipo de contrato segun el catalogo
                    if(nomina.getReceptor().getTipoContrato() != null && nomina.getReceptor().getTipoContrato().trim().equals("01")){
                        parametros.put("NOM_TIPO_CONTRATO", "Tiempo indeterminado");
                    }else if(nomina.getReceptor().getTipoContrato() != null && nomina.getReceptor().getTipoContrato().trim().equals("02")){
                        parametros.put("NOM_TIPO_CONTRATO", "Obra determinada");
                    }else if(nomina.getReceptor().getTipoContrato() != null && nomina.getReceptor().getTipoContrato().trim().equals("03")){
                        parametros.put("NOM_TIPO_CONTRATO", "Tiempo determinado");
                    }else if(nomina.getReceptor().getTipoContrato() != null && nomina.getReceptor().getTipoContrato().trim().equals("04")){
                        parametros.put("NOM_TIPO_CONTRATO", "Por temporada");
                    }else if(nomina.getReceptor().getTipoContrato() != null && nomina.getReceptor().getTipoContrato().trim().equals("05")){
                        parametros.put("NOM_TIPO_CONTRATO", "Sujeto a prueba");
                    }else if(nomina.getReceptor().getTipoContrato() != null && nomina.getReceptor().getTipoContrato().trim().equals("06")){
                        parametros.put("NOM_TIPO_CONTRATO", "Con capacitación inicial");
                    }else if(nomina.getReceptor().getTipoContrato() != null && nomina.getReceptor().getTipoContrato().trim().equals("07")){
                        parametros.put("NOM_TIPO_CONTRATO", "Pago de hora laborada");
                    }else if(nomina.getReceptor().getTipoContrato() != null && nomina.getReceptor().getTipoContrato().trim().equals("08")){
                        parametros.put("NOM_TIPO_CONTRATO", "Comisión laboral");
                    }else if(nomina.getReceptor().getTipoContrato() != null && nomina.getReceptor().getTipoContrato().trim().equals("09")){
                        parametros.put("NOM_TIPO_CONTRATO", "No existe relación de trabajo");
                    }else if(nomina.getReceptor().getTipoContrato() != null && nomina.getReceptor().getTipoContrato().trim().equals("10")){
                        parametros.put("NOM_TIPO_CONTRATO", "Jubilación, pensión, retiro");
                    }else if(nomina.getReceptor().getTipoContrato() != null && nomina.getReceptor().getTipoContrato().trim().equals("99")){
                        parametros.put("NOM_TIPO_CONTRATO", "Otro contrato");
                    }else{
                        parametros.put("NOM_TIPO_CONTRATO", "Otra contrato");
                    }
                    
                    //parametros.put("NOM_TIPO_JORNADA", nomina.getReceptor().getTipoJornada());
                    //para obtener el tipo de jornada segun el catalogo del sat
                    if(nomina.getReceptor().getTipoJornada()!=null && nomina.getReceptor().getTipoJornada().trim().equals("01")){
                        parametros.put("NOM_TIPO_JORNADA", "Diurna");
                    }else if(nomina.getReceptor().getTipoJornada()!=null && nomina.getReceptor().getTipoJornada().trim().equals("02")){
                        parametros.put("NOM_TIPO_JORNADA", "Nocturna");
                    }else if(nomina.getReceptor().getTipoJornada()!=null && nomina.getReceptor().getTipoJornada().trim().equals("03")){
                        parametros.put("NOM_TIPO_JORNADA", "Mixta");
                    }else if(nomina.getReceptor().getTipoJornada()!=null && nomina.getReceptor().getTipoJornada().trim().equals("04")){
                        parametros.put("NOM_TIPO_JORNADA", "Por hora");
                    }else if(nomina.getReceptor().getTipoJornada()!=null && nomina.getReceptor().getTipoJornada().trim().equals("05")){
                        parametros.put("NOM_TIPO_JORNADA", "Reducida");
                    }else if(nomina.getReceptor().getTipoJornada()!=null && nomina.getReceptor().getTipoJornada().trim().equals("06")){
                        parametros.put("NOM_TIPO_JORNADA", "Continuada");
                    }else if(nomina.getReceptor().getTipoJornada()!=null && nomina.getReceptor().getTipoJornada().trim().equals("07")){
                        parametros.put("NOM_TIPO_JORNADA", "Partida");
                    }else if(nomina.getReceptor().getTipoJornada()!=null && nomina.getReceptor().getTipoJornada().trim().equals("08")){
                        parametros.put("NOM_TIPO_JORNADA", "Por turnos");
                    }else if(nomina.getReceptor().getTipoJornada()!=null && nomina.getReceptor().getTipoJornada().trim().equals("99")){
                        parametros.put("NOM_TIPO_JORNADA", "Otra Jornada");
                    }else{
                        parametros.put("NOM_TIPO_JORNADA", "Otra Jornada");
                    }

                    //parametros.put("NOM_PERIODICIDAD", nomina.getReceptor().getPeriodicidadPago());                            
                    //para obtener la periodicidad segun el catalogo del sat:
                    if(nomina.getReceptor().getPeriodicidadPago() != null && nomina.getReceptor().getPeriodicidadPago().trim().equals("01")){
                        parametros.put("NOM_PERIODICIDAD", "Diario");
                    }else if(nomina.getReceptor().getPeriodicidadPago() != null && nomina.getReceptor().getPeriodicidadPago().trim().equals("02")){
                        parametros.put("NOM_PERIODICIDAD", "Semanal");
                    }else if(nomina.getReceptor().getPeriodicidadPago() != null && nomina.getReceptor().getPeriodicidadPago().trim().equals("03")){
                        parametros.put("NOM_PERIODICIDAD", "Catorcenal");
                    }else if(nomina.getReceptor().getPeriodicidadPago() != null && nomina.getReceptor().getPeriodicidadPago().trim().equals("04")){
                        parametros.put("NOM_PERIODICIDAD", "Quincenal");
                    }else if(nomina.getReceptor().getPeriodicidadPago() != null && nomina.getReceptor().getPeriodicidadPago().trim().equals("05")){
                        parametros.put("NOM_PERIODICIDAD", "Mensual");
                    }else if(nomina.getReceptor().getPeriodicidadPago() != null && nomina.getReceptor().getPeriodicidadPago().trim().equals("06")){
                        parametros.put("NOM_PERIODICIDAD", "Bimestral");
                    }else if(nomina.getReceptor().getPeriodicidadPago() != null && nomina.getReceptor().getPeriodicidadPago().trim().equals("07")){
                        parametros.put("NOM_PERIODICIDAD", "Unidad obra");
                    }else if(nomina.getReceptor().getPeriodicidadPago() != null && nomina.getReceptor().getPeriodicidadPago().trim().equals("08")){
                        parametros.put("NOM_PERIODICIDAD", "Comisión");
                    }else if(nomina.getReceptor().getPeriodicidadPago() != null && nomina.getReceptor().getPeriodicidadPago().trim().equals("09")){
                        parametros.put("NOM_PERIODICIDAD", "Precio alzado");
                    }else if(nomina.getReceptor().getPeriodicidadPago() != null && nomina.getReceptor().getPeriodicidadPago().trim().equals("99")){
                        parametros.put("NOM_PERIODICIDAD", "Otra Periodicidad");
                    }else{
                        parametros.put("NOM_PERIODICIDAD", "Otra Periodicidad");
                    }
                    
                    
                    try{
                        String dateFechaIngreso = sdf.format(nomina.getReceptor().getFechaInicioRelLaboral().toGregorianCalendar().getTime());
                        parametros.put("NOM_FECHA_INGRESO", dateFechaIngreso);
                    }catch(Exception e){}
                    try{
                        String ant = nomina.getReceptor().getAntigüedad();
                        ant = ant.replace("P", "");
                        ant = ant.replace("W", "");
                        parametros.put("NOM_ANTIGUEDAD", Integer.valueOf(ant));
                    }catch(Exception e){}
                    try{
                        parametros.put("NOM_SALARIO_BASE", FormatUtil.doubleToStringPuntoComas(nomina.getReceptor().getSalarioBaseCotApor().doubleValue()));
                    }catch(Exception e){}
                    try{
                        parametros.put("NOM_SALARIO_INTEGRADO", FormatUtil.doubleToStringPuntoComas(nomina.getReceptor().getSalarioDiarioIntegrado().doubleValue()));
                    }catch(Exception e){}
                    try{
                        String dateFechaPago = sdf.format(nomina.getFechaPago().toGregorianCalendar().getTime());
                        parametros.put("NOM_FECHA_PAGO", dateFechaPago);
                    }catch(Exception e){}
                    try{
                        if(nomina.getEmisor().getRegistroPatronal()!=null)
                            parametros.put("NOM_REGISTRO_PATRONAL", ( (!nomina.getEmisor().getRegistroPatronal().equals(""))?nomina.getEmisor().getRegistroPatronal():null ));
                    }catch(Exception e){}
                     parametros.put("NOM_TOTAL_DEDUCCIONES", FormatUtil.doubleToStringPuntoComas(totalDeducciones.doubleValue()));
                     parametros.put("NOM_TOTAL_PERCEPCIONES", FormatUtil.doubleToStringPuntoComas(totalPercepciones.doubleValue()));

                }
                //}else{//si es de tipo factura

                    parametros.put("CLIENTE_NOMBRE",this.comprobanteFiscal.getReceptor().getNombre() != null ? this.comprobanteFiscal.getReceptor().getNombre() : "");
                    parametros.put("CLIENTE_RFC",this.comprobanteFiscal.getReceptor().getRfc() != null ? this.comprobanteFiscal.getReceptor().getRfc() : "");                           
                    
                    if (nomina==null){
                        /*try{parametros.put("CLIENTE_DIRECCION",(this.comprobanteFiscal.getReceptor().getDomicilio().getCalle() != null ? this.comprobanteFiscal.getReceptor().getDomicilio().getCalle(): "")
                                                                    + (this.comprobanteFiscal.getReceptor().getDomicilio().getNoExterior() != null ? " No. " + this.comprobanteFiscal.getReceptor().getDomicilio().getNoExterior(): "")
                                                                    // +
                                                                    // (factura.getCliente().getNumeroInterior()!=null?
                                                                    // " INTERIOR " +
                                                                    // factura.getCliente().getNumeroInterior():
                                                                    // "")
                                                                    + (this.comprobanteFiscal.getReceptor().getDomicilio().getNoInterior() != null ? " "
                                                                                    + this.comprobanteFiscal.getReceptor().getDomicilio().getNoInterior()
                                                                                    : "")
                                                                    + (this.comprobanteFiscal.getReceptor().getDomicilio().getCodigoPostal() != null ? " C.P. "
                                                                                    + this.comprobanteFiscal.getReceptor().getDomicilio().getCodigoPostal()
                                                                                    : "")
                                                                    + (this.comprobanteFiscal.getReceptor().getDomicilio().getColonia() != null ? " COL. "
                                                                                    + this.comprobanteFiscal.getReceptor().getDomicilio().getColonia()
                                                                                    : "")
                                                                    + " "
                                                                    + (this.comprobanteFiscal.getReceptor().getDomicilio().getPais() != null ? this.comprobanteFiscal.getReceptor().getDomicilio().getPais() + ","
                                                                                    : "")
                                                                    + " "
                                                                    + (this.comprobanteFiscal.getReceptor().getDomicilio().getEstado() != null ? this.comprobanteFiscal.getReceptor().getDomicilio().getEstado() + ","
                                                                                    : "")
                                                                    + " "
                                                                    + (this.comprobanteFiscal.getReceptor().getDomicilio().getMunicipio() != null ? this.comprobanteFiscal.getReceptor().getDomicilio().getMunicipio() : ""));
                        }catch(Exception e){}                    
                        //Direccion de receptor (cliente) por partes
                        try{parametros.put("CLIENTE_DIR_CALLE",StringManage.getValidString(this.comprobanteFiscal.getReceptor().getDomicilio().getCalle()));}catch(Exception e){}
                        try{parametros.put("CLIENTE_DIR_NUM_EXT",StringManage.getValidString(this.comprobanteFiscal.getReceptor().getDomicilio().getNoExterior()));}catch(Exception e){}
                        try{parametros.put("CLIENTE_DIR_NUM_INT",StringManage.getValidString(this.comprobanteFiscal.getReceptor().getDomicilio().getNoInterior()));}catch(Exception e){}
                        try{parametros.put("CLIENTE_DIR_COLONIA",StringManage.getValidString(this.comprobanteFiscal.getReceptor().getDomicilio().getColonia()));}catch(Exception e){}
                        try{parametros.put("CLIENTE_DIR_MUNICIPIO",StringManage.getValidString(this.comprobanteFiscal.getReceptor().getDomicilio().getMunicipio()));}catch(Exception e){}
                        try{parametros.put("CLIENTE_DIR_ESTADO",StringManage.getValidString(this.comprobanteFiscal.getReceptor().getDomicilio().getEstado()));}catch(Exception e){}
                        try{parametros.put("CLIENTE_DIR_PAIS",StringManage.getValidString(this.comprobanteFiscal.getReceptor().getDomicilio().getPais()));}catch(Exception e){}
                        try{parametros.put("CLIENTE_DIR_CP", StringManage.getValidString(this.comprobanteFiscal.getReceptor().getDomicilio().getCodigoPostal()));}catch(Exception e){}
                    */
                    }
                 //}



                // DATOS DE EMPRESA
                parametros.put("EMPRESA_RAZON", this.comprobanteFiscal.getEmisor().getNombre());
                parametros.put("EMPRESA_RFC", this.comprobanteFiscal.getEmisor().getRfc());

                if(nomina != null){
                    EmisorBO emisorBO2 = new EmisorBO(conn);
                        Emisor[] listaEmisorDto = emisorBO2.findEmisores(-1, 0, 0, " AND RFC='" + this.comprobanteFiscal.getEmisor().getRfc() + "'");
                        if (listaEmisorDto.length<=0){
                            throw new Exception("El RFC de emisor expresado en el Layout "
                                    + "de entrada no esta registrado en la base de datos local.");
                        }                        
                        //Datos de empresa en bd local
                        Emisor emisorDto = listaEmisorDto[0];
                        //Domicilio Matriz
                        Ubicacionfiscal ubicacionfiscal = new UbicacionFiscalBO(conn).findByIdEmisor(conn,emisorDto.getIdemisor());
                        try{
                    if (ubicacionfiscal.getNumint() != null) {
                            parametros.put("EMPRESA_DIRECCION", ubicacionfiscal.getCalle()
                                            + " "
                                            + ubicacionfiscal.getNumext()
                                            + " "
                                            + ubicacionfiscal.getNumint()
                                            + ", COL. "
                                            + ubicacionfiscal.getColonia());
                    } else
                            parametros.put("EMPRESA_DIRECCION", ubicacionfiscal.getCalle()
                                            + " "
                                            + ubicacionfiscal.getNumext()
                                            + " COL. "
                                            + ubicacionfiscal.getColonia());
                    }catch(Exception e){}

                    try{parametros.put("EMPRESA_UBICACION", ubicacionfiscal.getPais()
                                                    + " "
                                                    + ubicacionfiscal.getEstado()
                                                    + " DEL. "
                                                    + ubicacionfiscal.getMunicipio()
                                                    + " C.P. "
                                                    + ubicacionfiscal.getCodigopostal());
                    }catch(Exception e){}
                }else{
                        
                    /*try{
                    if (this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getNoInterior() != null) {
                            parametros.put("EMPRESA_DIRECCION", this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getCalle()
                                            + " "
                                            + this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getNoExterior()
                                            + " "
                                            + this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getNoInterior()
                                            + ", COL. "
                                            + this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getColonia());
                    } else
                            parametros.put("EMPRESA_DIRECCION", this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getCalle()
                                            + " "
                                            + this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getNoExterior()
                                            + " COL. "
                                            + this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getColonia());
                    }catch(Exception e){}

                    try{parametros.put("EMPRESA_UBICACION", this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getPais()
                                                    + " "
                                                    + this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getEstado()
                                                    + " DEL. "
                                                    + this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getMunicipio()
                                                    + " C.P. "
                                                    + this.comprobanteFiscal.getEmisor().getDomicilioFiscal().getCodigoPostal());
                    }catch(Exception e){}*/
                
                }

                parametros.put("TIPO_MONEDA", String.valueOf(this.comprobanteFiscal.getMoneda()));
                parametros.put("NOM_TIPO_COMP", nombreTipoComp != null ? nombreTipoComp : "");
                parametros.put("TOTAL", this.comprobanteFiscal.getTotal().toString());			
                parametros.put("SUBTOTAL", this.comprobanteFiscal.getSubTotal().toString());
                //BigDecimal subtotalDec = new BigDecimal(cf.getImporteSubtotal());
                //parametros.put("SUBTOTAL_DEC", subtotalDec);
                parametros.put("IMPORTE_LETRA", FacturacionUtil.importeLetra(this.comprobanteFiscal.getTotal().doubleValue(), String.valueOf(this.comprobanteFiscal.getMoneda())));
                //parametros.put("COMENTARIOS", cf.getComentarios());
                parametros.put("TIPO_CAMBIO", StringManage.getValidString(this.comprobanteFiscal.getTipoCambio()+""));
                if (this.comprobanteFiscal.getDescuento()!=null){
                    parametros.put("DESCUENTO", this.comprobanteFiscal.getDescuento().toString());
                    //try{parametros.put("DESCUENTO_PORCENTAJE", StringManage.getValidString(this.comprobanteFiscal.getMotivoDescuento()));}catch(Exception e){}
                }

                EmisorBO emisorBO =  new EmisorBO();
                Emisor emisorDto = emisorBO.findEmisorbyRfc(conn,this.comprobanteFiscal.getEmisor().getRfc());

                //LOGO//
                //Sí hay Logo especifico para este archivo (acciones personalizadas), esta prevalece
                if (accionComprobante!=null){
                    if (StringManage.getValidString(accionComprobante.getRutaLogo()).length()>0)
                        parametros.put("LOGO",accionComprobante.getRutaLogo());
                }
                //De lo contrario, buscamos por logo de Emisor o dejamos vacío
                if (!parametros.containsKey("LOGO")){
                    if(emisorDto.getRutalogo()!= null ){
                            parametros.put("LOGO",emisorDto.getRutalogo());
                    } else {// SI LA EMPRESA NO TIENE UN LOGO PROPIO no se carga ninguno                        
                            parametros.put("LOGO","");
                    }
                }

                if (this.timbreFiscalDigital!=null&&this.timbreFiscalDigital.getFechaTimbrado() != null) {
                   parametros.put("FECHA_IMPRESION", FormateadorDeFechas.getFecha(this.timbreFiscalDigital.getFechaTimbrado().toGregorianCalendar().getTime().getTime(),FormateadorDeFechas.FORMATO_DDMMYYYYHHMMSS));                        
                } else {
                    parametros.put("FECHA_IMPRESION", "");
                } 

                // DETALLE DE IMPUESTOS
                //parametros.put("IMPUESTO_TOTAL",String.valueOf(this.comprobanteFiscal.getImpuestos()));
                mapeoImpuestos();
                System.setProperty("jasper.reports.compile.class.path","lib/jasperreports-3.7.4.jar");
                JasperReport reporteImpuestos = (JasperReport) JRLoader.loadObject("jasper/FacturaImpuestos.jasper");
                parametros.put("REPORTE_IMPUESTOS", reporteImpuestos);
                parametros.put("DATOS_IMPUESTOS",new JRBeanCollectionDataSource(listImpuestos));

                QRCodeWriter qrWriter = new QRCodeWriter();
                try {
                        Hashtable params = new Hashtable();
                        params.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.H);
                        fileTemp = new File("C:/temp/"+ new Date().getTime() + ".gif");
                        fileTemp.getParentFile().mkdirs(); //creamos carpeta en caso de que no exista en sistema
                        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
                        otherSymbols.setDecimalSeparator('.');
                        otherSymbols.setGroupingSeparator(','); 
                        DecimalFormat df = new DecimalFormat("0000000000.000000", otherSymbols);
                        String dato = "?re=" + this.comprobanteFiscal.getEmisor().getRfc() + "&rr="+ this.comprobanteFiscal.getReceptor().getRfc() + "&tt="+df.format(this.comprobanteFiscal.getTotal().doubleValue())+ "&id=" + (this.timbreFiscalDigital!=null?this.timbreFiscalDigital.getUUID():"");
                        MatrixToImageWriter.writeToFile(qrWriter.encode(dato, BarcodeFormat.QR_CODE, 625, 625),"gif", fileTemp);
                        parametros.put("UUID", this.timbreFiscalDigital!=null?this.timbreFiscalDigital.getUUID():"");
                        parametros.put("QRCODE", fileTemp.getAbsolutePath());
                } catch (WriterException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }


                //Información adicional 
                {
                    String concatDatosAdicionales = "";
                    if (this.facturaDatos!=null){
                        if (this.facturaDatos.getLineaDatosAdicionales()!=null
                                && this.facturaDatos.getLineaDatosAdicionales().getCamposAdicionales()!=null){
                            int i = 1;
                            for (String campoAdicional : this.facturaDatos.getLineaDatosAdicionales().getCamposAdicionales()){
                                concatDatosAdicionales += campoAdicional + "\n";
                                parametros.put("DATO_ADICIONAL_"+i, campoAdicional); 
                                i++;
                            }
                        }
                    }
                    //Este parametro contiene todos los datos adicionales en un solo String, separados con un salto de linea entre cada uno
                    parametros.put("DATOS_ADICIONALES", concatDatosAdicionales); 
                }

                //Comentarios
                try{
                    if (StringManage.getValidString(this.facturaDatos.getLineaDatosFactura().getDatosComprobante().getObservaciones()).length()>0)
                        parametros.put("COMENTARIOS", this.facturaDatos.getLineaDatosFactura().getDatosComprobante().getObservaciones());
                }catch(Exception ex){}

               parametros.put("NO_CERTIFICADO", this.comprobanteFiscal.getNoCertificado());
               parametros.put("CONDICIONES_PAGO",getFormaPago(this.comprobanteFiscal.getFormaPago())); 
               parametros.put("NO_CERTIFICADO_SAT", this.timbreFiscalDigital!=null?this.timbreFiscalDigital.getNoCertificadoSAT():"");
               parametros.put("SELLO_DIGITAL", this.timbreFiscalDigital!=null?this.timbreFiscalDigital.getSelloCFD():"");
               parametros.put("SELLO_SAT", this.timbreFiscalDigital!=null?this.timbreFiscalDigital.getSelloSAT():"");  
               
               if(this.timbreFiscalDigital!=null){
                if(this.timbreFiscalDigital.getNoCertificadoSAT() != null){
                    if(!this.timbreFiscalDigital.getNoCertificadoSAT().trim().equals(""))
                    parametros.put("NO_CERTIFICADO_SAT", this.timbreFiscalDigital.getNoCertificadoSAT());
                }
                if(this.timbreFiscalDigital.getRfcProvCertif() != null && !this.timbreFiscalDigital.getRfcProvCertif().trim().equals("")){
                    parametros.put("PAC_RFC", this.timbreFiscalDigital.getRfcProvCertif());
                }
                if(this.timbreFiscalDigital.getLeyenda() != null && !this.timbreFiscalDigital.getLeyenda().trim().equals("")){
                    parametros.put("PAC_LEYENDA", this.timbreFiscalDigital.getLeyenda());
                }

                parametros.put("CADENA_ORIGINAL","||"+ this.timbreFiscalDigital.getVersion()+ "|"+ this.timbreFiscalDigital.getUUID()+ "|"
                        + FormateadorDeFechas.getFecha(this.timbreFiscalDigital.getFechaTimbrado().toGregorianCalendar().getTime().getTime(),FormateadorDeFechas.FORMATO_AAAAMMDDTHHMMSSS)
                        + "|" + this.timbreFiscalDigital.getSelloCFD() + "|" + this.timbreFiscalDigital.getNoCertificadoSAT() + "||");
               }
                System.setProperty("jasper.reports.compile.class.path","/lib/jasperreports-3.7.4.jar");                       

                //VALIDAMOS SI TIENE REPRESENTACION IMPRESA PERSONALIZADA:
                String representacionAuxiliar = representacionImpresa; //para ayuda por si no esta el personalizado recuperar nombre del pdf generico

                try{//pdf personalizado

                    String pathPlantillaComprobante = null;
                    if(representacionImpresa.equals("Factura_carta")){
                            pathPlantillaComprobante = emisorDto.getPlantillacomprobante();
                    }else if(representacionImpresa.equals("Nomina_carta")){
                            pathPlantillaComprobante = emisorDto.getPlantillanomina();
                    }else{
                         System.out.println("Plantilla Desconocida");
                    }

                    //Sí hay plantilla especifica para este archivo (acciones personalizadas), esta prevalece
                    if (accionComprobante!=null){
                        if (StringManage.getValidString(accionComprobante.getPlantillaFactura()).length()>0)
                            pathPlantillaComprobante = accionComprobante.getPlantillaFactura();
                    }
                    System.out.println("__________PDF PERSONALIZADO");
                    //representacionImpresa += "_"+this.comprobanteFiscal.getEmisor().getRfc(); 
                    System.out.println("________________PDF PERSONALIZADO. . . "+ pathPlantillaComprobante);  

                    parametros.putAll(parametros);

                    parametros.put(JRParameter.REPORT_LOCALE, Locale.US); 
                    byte[] bytes = JasperRunManager.runReportToPdf(pathPlantillaComprobante,parametros, ds);                            

                    baos = new ByteArrayOutputStream(bytes.length);
                    baos.write(bytes, 0, bytes.length);

                    //Eliminamos archivo temp para codigo bidimensional
                    if (fileTemp!=null){
                        try{
                            fileTemp.delete();
                        }catch(Exception ex){}
                    }

                    return baos;
                }catch(Exception e){                            
                    representacionImpresa = representacionAuxiliar; 
                    System.out.println("________________PDF generico. . . "+representacionImpresa+".jasper");                           
                    byte[] bytes = JasperRunManager.runReportToPdf("jasper/"+representacionImpresa+".jasper" ,parametros, ds);                           

                    baos = new ByteArrayOutputStream(bytes.length);
                    baos.write(bytes, 0, bytes.length);
                    e.printStackTrace();

                    //Eliminamos archivo temp para codigo bidimensional
                    if (fileTemp!=null){
                        try{
                            fileTemp.delete();
                        }catch(Exception ex){}
                    }

                    return baos;
                }
          } catch (Exception e) {
                e.printStackTrace();
          }finally{
            if (fileTemp!=null){
                try{
                    fileTemp.delete();
                }catch(Exception ex){}
            }
          }        
        //***************************************************
        
        return null;
    }
    
    public void mapeoImpuestos(){
        listImpuestos = new ArrayList<Impuesto>();
        int contadorImmpuestos = 1;
        
        //impuestos Federales
        if(this.comprobanteFiscal.getImpuestos() != null){
            Impuesto impPDF;
            if(this.comprobanteFiscal.getImpuestos().getTraslados() != null){
            //trasladados
                for(Comprobante.Impuestos.Traslados.Traslado impuesto : this.comprobanteFiscal.getImpuestos().getTraslados().getTraslado()){
                    impPDF = new Impuesto();  
                    impPDF.setIdImpuesto(contadorImmpuestos);
                    impPDF.setIdEmpresa(0);
                    impPDF.setNombre(impuesto.getImpuesto());
                    impPDF.setDescripcion(impuesto.getImpuesto());
                    try{
                        impPDF.setPorcentaje(impuesto.getTasaOCuota().doubleValue());
                    }catch(Exception e){}
                    impPDF.setTrasladado(true);                    
                    //impPDF.setTotal((tras.getTasa().multiply(new BigDecimal(0.01)).multiply(this.comprobanteFiscal.getSubTotal().subtract(this.comprobanteFiscal.getDescuento()))).doubleValue());
                    impPDF.setTotal(impuesto.getImporte().doubleValue());
                    impPDF.setIdEstatus(1);//1 Activo
                    impPDF.setImpuestoLocal(false);
                    listImpuestos.add(impPDF);
                    contadorImmpuestos++;
                }
            }
            if(this.comprobanteFiscal.getImpuestos().getRetenciones() != null){
                //retenidos                
                for(Comprobante.Impuestos.Retenciones.Retencion impuesto : this.comprobanteFiscal.getImpuestos().getRetenciones().getRetencion()){
                    impPDF = new Impuesto();  
                    impPDF.setIdImpuesto(contadorImmpuestos);
                    impPDF.setIdEmpresa(0);
                    impPDF.setNombre(impuesto.getImpuesto());
                    impPDF.setDescripcion(impuesto.getImpuesto());
                    impPDF.setPorcentaje(FormatUtil.obtenerTasaRetencion(impuesto.getImporte(), this.comprobanteFiscal.getSubTotal()).doubleValue());
                    impPDF.setTrasladado(false);
                    impPDF.setTotal(impuesto.getImporte().doubleValue());
                    impPDF.setIdEstatus(1);//1 Activo
                    impPDF.setImpuestoLocal(false);
                    listImpuestos.add(impPDF);
                    contadorImmpuestos++;
                 }
            }                     
        }else{
            System.out.println(".....NO HAY IMPUESTOS FEDERALES EN EL XML");
        }
        
        //impuestos locales
        if(implocal != null){
            if(implocal.getRetencionesLocalesAndTrasladosLocales() != null){
                if (implocal.getRetencionesLocalesAndTrasladosLocales().size()>0){
                    Impuesto impPDF = null; 
                    for(Object o : implocal.getRetencionesLocalesAndTrasladosLocales()){
                        
                        //Traslado Local
                        if (o instanceof ImpuestosLocales.TrasladosLocales) {
                            ImpuestosLocales.TrasladosLocales impuesto = (ImpuestosLocales.TrasladosLocales) o;
                            impPDF = new Impuesto();  
                            impPDF.setIdImpuesto(contadorImmpuestos);
                            impPDF.setIdEmpresa(0);
                            impPDF.setNombre(impuesto.getImpLocTrasladado());
                            impPDF.setDescripcion(impuesto.getImpLocTrasladado());
                            impPDF.setPorcentaje(impuesto.getTasadeTraslado().doubleValue());
                            impPDF.setTrasladado(true);
                            impPDF.setTotal(impuesto.getImporte().doubleValue());
                            impPDF.setIdEstatus(1);//1 Activo
                            impPDF.setImpuestoLocal(true);
                            listImpuestos.add(impPDF);
                            contadorImmpuestos++;
                        }
                        
                        //Retencion Local
                        if (o instanceof ImpuestosLocales.RetencionesLocales) {
                            ImpuestosLocales.RetencionesLocales impuesto = (ImpuestosLocales.RetencionesLocales) o;
                            impPDF = new Impuesto();  
                            impPDF.setIdImpuesto(contadorImmpuestos);
                            impPDF.setIdEmpresa(0);
                            impPDF.setNombre(impuesto.getImpLocRetenido());
                            impPDF.setDescripcion(impuesto.getImpLocRetenido());
                            impPDF.setPorcentaje(impuesto.getTasadeRetencion().doubleValue());
                            impPDF.setTrasladado(false);
                            impPDF.setTotal(impuesto.getImporte().doubleValue());
                            impPDF.setIdEstatus(1);//1 Activo
                            impPDF.setImpuestoLocal(true);
                            listImpuestos.add(impPDF);
                            contadorImmpuestos++;
                        }
                        
                    }
                }
            }
        }else{
            
        }
                    
    }
    
    public void mapeoConceptosNomina(){
        
        //recuperamos los conceptos del comprobante              
        /*for(Concepto item : resultado){
            nominaCompDesc = new NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO();            
            nominaCompDesc.setIdTipoConcepto(1);//mandamos que es concepto
            nominaCompDesc.setIdComprobanteDescripcion(numeroConcento);
            nominaCompDesc.setNombre("");
            nominaCompDesc.setDescripcion(item.getDescripcion());
            nominaCompDesc.setPrecio(item.getValorUnitario().doubleValue());
            nominaCompDesc.setIdentificacion(item.getNoIdentificacion());                           
            resultadoNomina.add(nominaCompDesc);
            numeroConcento++;
        }*/
        
        //obtenemos los datos de Percepciones
        /*if(nomina.getPercepciones() != null
                && nomina.getPercepciones().getPercepcion() != null){
            BigDecimal total = new BigDecimal(BigInteger.ZERO);
            for(Nomina.Percepciones.Percepcion percepcion : nomina.getPercepciones().getPercepcion()){
                total = BigDecimal.ZERO;
                nominaCompDesc = new NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO(); 
                nominaCompDesc.setIdTipoConcepto(2);//mandamos percepcion
                nominaCompDesc.setIdComprobanteDdescripcionPercepcion(numeroConcento);
                nominaCompDesc.setNombreDescripcionPercepcion(percepcion.getConcepto());
                nominaCompDesc.setClavePercepcion(percepcion.getClave());
                nominaCompDesc.setImporteGravadoPercepcion(percepcion.getImporteGravado().doubleValue());
                nominaCompDesc.setImporteExentoPercepcion(percepcion.getImporteExento().doubleValue());
                total = percepcion.getImporteGravado().add(percepcion.getImporteExento());
                nominaCompDesc.setSumaGravadoExentoPercepcion(total.doubleValue());
                totalPercepciones = totalPercepciones.add(total);
                resultadoNomina.add(nominaCompDesc);  
                numeroConcento++;
            }
        }*/
        
        //obtenemos los datos de Deducciones
        /*if(nomina.getDeducciones() != null && nomina.getDeducciones().getDeduccion() != null){
            BigDecimal total = new BigDecimal(BigInteger.ZERO);
            for(Nomina.Deducciones.Deduccion deduccion : nomina.getDeducciones().getDeduccion()){
                total = BigDecimal.ZERO;
                nominaCompDesc = new NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO();
                nominaCompDesc.setIdTipoConcepto(3);//mandamos deduccion
                nominaCompDesc.setIdComprobanteDdescripcionDeduccion(numeroConcento);
                nominaCompDesc.setNombreDescripcionDeduccion(deduccion.getConcepto());
                nominaCompDesc.setClaveDeduccion(deduccion.getClave());
                nominaCompDesc.setImporteGravadoDeduccion(deduccion.getImporteGravado().doubleValue());
                nominaCompDesc.setImporteExentoDeduccion(deduccion.getImporteExento().doubleValue());
                total = deduccion.getImporteGravado().add(deduccion.getImporteExento());
                nominaCompDesc.setSumaGravadoExentoDeduccion(total.doubleValue());
                totalDeducciones = totalDeducciones.add(total);
                resultadoNomina.add(nominaCompDesc);
                numeroConcento++;            
            }
        }*/
        
        ////*
        //obtenemos los datos de Percepciones y Deducciones
        //vemos el tamaño de la longitud para ver hasta donde va a recorrer lis arreglos:
        int numeroPercepciones = 0;
        int numeroDeducciones = 0;
        if(nomina.getPercepciones() != null && nomina.getPercepciones().getPercepcion() != null){
            numeroPercepciones = nomina.getPercepciones().getPercepcion().size();
        }
        if(nomina.getDeducciones() != null && nomina.getDeducciones().getDeduccion() != null){
            numeroDeducciones = nomina.getDeducciones().getDeduccion().size();
        }
        //validamos hasta donde recorrera el arreglo:
        int contadorMaximoTamano = 0;
        if(numeroDeducciones >= numeroPercepciones)
            contadorMaximoTamano = numeroDeducciones;
        else if(numeroDeducciones < numeroPercepciones)
                contadorMaximoTamano = numeroPercepciones;
        
        //variables auxiliares para el conteo
        int recorrido = 0;
        BigDecimal total = new BigDecimal(BigInteger.ZERO);
        while( recorrido < contadorMaximoTamano){            
            Nomina.Percepciones.Percepcion percepcion = null;
            Nomina.Deducciones.Deduccion deduccion = null;
            try{
                percepcion = nomina.getPercepciones().getPercepcion().get(recorrido);
            }catch(Exception e){}
            try{
                deduccion = nomina.getDeducciones().getDeduccion().get(recorrido);
            }catch(Exception e){}
            
            nominaCompDesc = new NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO();
            //las percepciones
            if(percepcion != null){            
            total = BigDecimal.ZERO;                
                nominaCompDesc.setIdTipoConcepto(2);//mandamos percepcion
                nominaCompDesc.setIdComprobanteDdescripcionPercepcion(numeroConcento);
                nominaCompDesc.setNombreDescripcionPercepcion(percepcion.getConcepto());
                nominaCompDesc.setClavePercepcion(percepcion.getClave());
                nominaCompDesc.setImporteGravadoPercepcion(percepcion.getImporteGravado().doubleValue());
                nominaCompDesc.setImporteExentoPercepcion(percepcion.getImporteExento().doubleValue());
                total = percepcion.getImporteGravado().add(percepcion.getImporteExento());
                nominaCompDesc.setSumaGravadoExentoPercepcion(total.doubleValue());
                totalPercepciones = totalPercepciones.add(total);                
                numeroConcento++;
            }
            //las deducciones
            if(deduccion != null){                
            total = BigDecimal.ZERO;                
                nominaCompDesc.setIdTipoConcepto(3);//mandamos deduccion
                nominaCompDesc.setIdComprobanteDdescripcionDeduccion(numeroConcento);
                nominaCompDesc.setNombreDescripcionDeduccion(deduccion.getConcepto());
                nominaCompDesc.setClaveDeduccion(deduccion.getClave());
                nominaCompDesc.setImporteGravadoDeduccion(deduccion.getImporte().doubleValue());
              //  nominaCompDesc.setImporteExentoDeduccion(deduccion..doubleValue());
                total = deduccion.getImporte(); //.add(deduccion.getImporteExento());
                nominaCompDesc.setSumaGravadoExentoDeduccion(total.doubleValue());
                totalDeducciones = totalDeducciones.add(total);                
                numeroConcento++;    
            }
            resultadoNomina.add(nominaCompDesc);
            totalDeNumeroDeObjetos++;
            recorrido++;
        }
        ////*

        //obtenemos los datos de las hora extra
        if(nomina.getPercepciones() != null && nomina.getPercepciones().getPercepcion() != null){
            for(Nomina.Percepciones.Percepcion percepcion : nomina.getPercepciones().getPercepcion()){
                if (percepcion.getHorasExtra() != null){
                    for (Nomina.Percepciones.Percepcion.HorasExtra horaExtra : percepcion.getHorasExtra()){
                        nominaCompDesc = new NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO();
                        nominaCompDesc.setIdTipoConcepto(4);//mandamos hora extra
                        nominaCompDesc.setIdComprobanteDescripcionHorasExtra(numeroConcento);
                        nominaCompDesc.setDiasHorasExtra(horaExtra.getDias());
                        nominaCompDesc.setTipoHoras(horaExtra.getTipoHoras());
                        nominaCompDesc.setImportePagado(horaExtra.getImportePagado().doubleValue());            
                        resultadoNomina.add(nominaCompDesc);
                        numeroConcento++; 
                        totalDeNumeroDeObjetos++;
                    }
                }
            }
        }
       
      
        //obtenemos los datos de las incapacidades
        if(nomina.getIncapacidades() != null && nomina.getIncapacidades().getIncapacidad() != null){
            for(Nomina.Incapacidades.Incapacidad incapacidad : nomina.getIncapacidades().getIncapacidad()){
                nominaCompDesc = new NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO();
                nominaCompDesc.setIdTipoConcepto(5);
                nominaCompDesc.setIdComprobanteDescripcionIncapacidad(numeroConcento);
                nominaCompDesc.setDiasIncapacidad(incapacidad.getDiasIncapacidad());
                nominaCompDesc.setNombreDescripcionIncapacidad("Tipo: "+incapacidad.getTipoIncapacidad());
                nominaCompDesc.setDescuentoIncapacidad(incapacidad.getImporteMonetario().doubleValue());            
                resultadoNomina.add(nominaCompDesc);
                numeroConcento++;
                totalDeNumeroDeObjetos++;
            }
        }
        
        //obtenemos los datos de otros pagos
        if(nomina.getOtrosPagos() != null && nomina.getOtrosPagos().getOtroPago() != null){
            for(Nomina.OtrosPagos.OtroPago otroPago : nomina.getOtrosPagos().getOtroPago()){
                nominaCompDesc = new NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO();
                
                total = BigDecimal.ZERO; 
                nominaCompDesc.setIdTipoConcepto(2);//mandamos percepcion
                nominaCompDesc.setIdComprobanteDdescripcionPercepcion(numeroConcento);
                nominaCompDesc.setNombreDescripcionPercepcion(otroPago.getConcepto());
                nominaCompDesc.setClavePercepcion(otroPago.getClave());
                nominaCompDesc.setImporteGravadoPercepcion(otroPago.getImporte().doubleValue());
                nominaCompDesc.setImporteExentoPercepcion(0);
                total = otroPago.getImporte();
                nominaCompDesc.setSumaGravadoExentoPercepcion(total.doubleValue());
                totalPercepciones = totalPercepciones.add(total);                
                numeroConcento++;                
                resultadoNomina.add(nominaCompDesc);
                totalDeNumeroDeObjetos++;
            }
        }
        
    }
    
    ////*
    public void mapeoConceptosNominaSegundaHoja(){
        List<NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO> resultadoNomina2 = new ArrayList<NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO>();               
        
        for(NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO dato : resultadoNomina){
            resultadoNomina2.add(dato);
        }
                
        for(NominaComprobanteDescripcionConcepDeducPerceHorasIncapaBO dato : resultadoNomina2){
            resultadoNomina.add(dato);
        }        
    }
    ////*
    
    public void mapeoConceptosTipoPersonalizadoMonitor(){
        for (LineaDatosConcepto lineaDatosConcepto :  this.facturaDatos.getListaLineaDatosConceptos() ){
            if (lineaDatosConcepto.getDatosConcepto()!=null){
                ConceptoPersonalizadoMonitor cpm = new ConceptoPersonalizadoMonitor();

                if (lineaDatosConcepto.getDatosConcepto().getConcepto()!=null){
                    cpm.setCantidad(lineaDatosConcepto.getDatosConcepto().getConcepto().getCantidad());
                    cpm.setUnidad(lineaDatosConcepto.getDatosConcepto().getConcepto().getUnidad());
                    cpm.setNoIdentificacion(lineaDatosConcepto.getDatosConcepto().getConcepto().getNoIdentificacion());
                    cpm.setDescripcion(lineaDatosConcepto.getDatosConcepto().getConcepto().getDescripcion());
                    cpm.setValorUnitario(lineaDatosConcepto.getDatosConcepto().getConcepto().getValorUnitario());
                    cpm.setImporte(lineaDatosConcepto.getDatosConcepto().getConcepto().getImporte());
                    cpm.setClaveProdServ(lineaDatosConcepto.getDatosConcepto().getConcepto().getClaveProdServ());
                    cpm.setClaveUnidad(lineaDatosConcepto.getDatosConcepto().getConcepto().getClaveUnidad());
                    
                    //Agregamos información aduanera a la propia Descripción de Concepto para Representacion impresa
                    if (lineaDatosConcepto.getDatosConcepto().getConcepto().getInformacionAduanera()!=null){
                        String strAduana = "";
                        for (InformacionAduanera infoAduanera :  lineaDatosConcepto.getDatosConcepto().getConcepto().getInformacionAduanera()){
                            strAduana += "\nNumero Pedimento: " + StringManage.getValidString(infoAduanera.getNumeroPedimento());

                        }
                        cpm.setDescripcion(cpm.getDescripcion() + "\n" + strAduana);
                    }
                }

                cpm.setNombre(lineaDatosConcepto.getDatosConcepto().getNombre());
                cpm.setPorcentajeDescuento(lineaDatosConcepto.getDatosConcepto().getPorcentajeDescuento());
                cpm.setMontoDescuento(lineaDatosConcepto.getDatosConcepto().getMontoDescuento());
                cpm.setIVAporcentaje(lineaDatosConcepto.getDatosConcepto().getIVAporcentaje());
                cpm.setIVAmonto(lineaDatosConcepto.getDatosConcepto().getIVAmonto());
                cpm.setIEPSporcentaje(lineaDatosConcepto.getDatosConcepto().getIEPSporcentaje());
                cpm.setIEPSmonto(lineaDatosConcepto.getDatosConcepto().getIEPSmonto());
                cpm.setIVARetenidoPorcentaje(lineaDatosConcepto.getDatosConcepto().getIVARetenidoPorcentaje());
                cpm.setIVARetenidoMonto(lineaDatosConcepto.getDatosConcepto().getIVARetenidoMonto());
                cpm.setISRPorcentaje(lineaDatosConcepto.getDatosConcepto().getISRPorcentaje());
                cpm.setISRMonto(lineaDatosConcepto.getDatosConcepto().getISRMonto());

                resultadoTipoPersonalizado.add(cpm);
            }
        }
    }
    
    public void mapeoConceptosTipoPersonalizadoMonitorDonat(){
        for (mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto lineaDatosConcepto :  this.comprobanteFiscal.getConceptos().getConcepto() ){
            ConceptoPersonalizadoMonitor cpm = new ConceptoPersonalizadoMonitor();
            
            cpm.setCantidad(lineaDatosConcepto.getCantidad());
            cpm.setUnidad(lineaDatosConcepto.getUnidad());
            cpm.setNoIdentificacion(lineaDatosConcepto.getNoIdentificacion());
            cpm.setDescripcion(lineaDatosConcepto.getDescripcion());
            cpm.setValorUnitario(lineaDatosConcepto.getValorUnitario());
            cpm.setImporte(lineaDatosConcepto.getImporte());
            cpm.setClaveProdServ(lineaDatosConcepto.getClaveProdServ());
            cpm.setClaveUnidad(lineaDatosConcepto.getClaveUnidad());

            resultadoTipoPersonalizado.add(cpm);
            
        }
    }

    private String getRegimenFiscal(String regimen){
        String regimenFiscal[]={"601-General de Ley Personas Morales",
"603-Personas Morales con Fines no Lucrativos",
"605-Sueldos y Salarios e Ingresos Asimilados a Salarios",
"606-Arrendamiento",
"608-Demás ingresos",
"609-Consolidación",
"610-Residentes en el Extranjero sin Establecimiento Permanente en México",
"611-Ingresos por Dividendos (socios y accionistas)",
"612-Personas Físicas con Actividades Empresariales y Profesionales",
"614-Ingresos por intereses",
"616-Sin obligaciones fiscales",
"620-Sociedades Cooperativas de Producción que optan por diferir sus ingresos",
"621-Incorporación Fiscal",
"622-Actividades Agrícolas, Ganaderas, Silvícolas y Pesqueras",
"623-Opcional para Grupos de Sociedades",
"624-Coordinados",
"628-Hidrocarburos",
"607-Régimen de Enajenación o Adquisición de Bienes",
"629-De los Regímenes Fiscales Preferentes y de las Empresas Multinacionales",
"630-Enajenación de acciones en bolsa de valores",
"615-Régimen de los ingresos por obtención de premios"};
        for(String item:regimenFiscal){
            String temp[]=item.split("-");
            if(temp[0].equals(regimen)){
                return item;
            }
        }
        return regimen;
    }
    
    private String getUsoCfdi(String usoCfdi){
        String cfdi[]={"G01-Adquisición de mercancias",
"G02-Devoluciones, descuentos o bonificaciones" ,
"G03-Gastos en general" ,
"I01-Construcciones" ,
"I02-Mobilario y equipo de oficina por inversiones" ,
"I03-Equipo de transporte" ,
"I04-Equipo de computo y accesorios" ,
"I05-Dados, troqueles, moldes, matrices y herramental" ,
"I06-Comunicaciones telefónicas" ,
"I07-Comunicaciones satelitales" ,
"I08-Otra maquinaria y equipo" ,
"D01-Honorarios médicos, dentales y gastos hospitalarios." ,
"D02-Gastos médicos por incapacidad o discapacidad" ,
"D03-Gastos funerales." ,
"D04-Donativos." ,
"D05-Intereses reales efectivamente pagados por créditos hipotecarios (casa habitación)." ,
"D06-Aportaciones voluntarias al SAR." ,
"D07-Primas por seguros de gastos médicos." ,
"D08-Gastos de transportación escolar obligatoria." ,
"D09-Depósitos en cuentas para el ahorro, primas que tengan como base planes de pensiones." ,
"D10-Pagos por servicios educativos (colegiaturas)" ,
"P01-Por definir"};
        for(String item:cfdi){
            String temp[]=item.split("-");
            if(temp[0].equals(usoCfdi)){
                return item;
            }
        }
        return usoCfdi;
    }
    
    private String getFormaPago(String pago){
        String formaPago[]={"01-Efectivo" ,
"02-Cheque nominativo" ,
"03-Transferencia electrónica de fondos" ,
"04-Tarjeta de crédito" ,
"05-Monedero electrónico" ,
"06-Dinero electrónico" ,
"08-Vales de despensa" ,
"12-Dación en pago" ,
"13-Pago por subrogación" ,
"14-Pago por consignación" ,
"15-Condonación" ,
"17-Compensación" ,
"23-Novación" ,
"24-Confusión" ,
"25-Remisión de deuda" ,
"26-Prescripción o caducidad" ,
"27-A satisfacción del acreedor" ,
"28-Tarjeta de débito" ,
"29-Tarjeta de servicios" ,
"30-Aplicación de anticipos" ,
"31-Intermediario pagos" ,
"99-Por definir"};
        for(String item:formaPago){
            String temp[]=item.split("-");
            if(temp[0].equals(pago)){
                return item;
            }
        }
        return pago;
    }
    
    private String getTipoRelacion(String relacion){
        String relaciones[]={"01-Nota de crédito de los documentos relacionados",
"02-Nota de débito de los documentos relacionados" ,
"03-Devolución de mercancía sobre facturas o traslados previos" ,
"04-Sustitución de los CFDI previos" ,
"05-Traslados de mercancias facturados previamente" ,
"06-Factura generada por los traslados previos" ,
"07-CFDI por aplicación de anticipo" ,
"08-Factura generada por pagos en parcialidades" ,
"09-Factura generada por pagos diferidos"};
        for(String item:relaciones){
            String temp[]=item.split("-");
            if(temp[0].equals(relacion)){
                return item;
            }
        }
        return relacion;
    }
    
    public Comprobante getComprobanteFiscal() {
        return comprobanteFiscal;
    }

    public void setComprobanteFiscal(Comprobante comprobanteFiscal) {
        this.comprobanteFiscal = comprobanteFiscal;
    }

    public CFDv33 getCfd() {
        return cfd;
    }

    public void setCfd(CFDv33 cfd) {
        this.cfd = cfd;
    }

    public TimbreFiscalDigital getTimbreFiscalDigital() {
        return timbreFiscalDigital;
    }

    public void setTimbreFiscalDigital(TimbreFiscalDigital timbreFiscalDigital) {
        this.timbreFiscalDigital = timbreFiscalDigital;
    }

    public TFDv1c32 gettFDv1() {
        return tFDv1;
    }

    public void settFDv1(TFDv1c32 tFDv1) {
        this.tFDv1 = tFDv1;
    }

    public FacturaDatos getFacturaDatos() {
        return facturaDatos;
    }

    public void setFacturaDatos(FacturaDatos facturaDatos) {
        this.facturaDatos = facturaDatos;
    }

    public Accioncomprobante getAccionComprobante() {
        return accionComprobante;
    }

    public void setAccionComprobante(Accioncomprobante accionComprobante) {
        this.accionComprobante = accionComprobante;
    }

    /**
     * @return the comentarios
     */
    public String getComentarios() {
        return comentarios;
    }

    /**
     * @param comentarios the comentarios to set
     */
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}
