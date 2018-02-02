/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.bo.comprobante;

import com.fens.desktopMonitor.dto.Configuracion;
import com.fens.desktopMonitor.util.FileManage;
import com.tsp.interconecta.ws.WsGenericResp;
import com.tsp.interconecta.ws.WsGenericRespExt;
import com.tsp.interconecta.ws.WsInsertaClienteResp;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author ISCesar
 */
public class ConexionPAC {

    private String userPAC = "";
    private String passPAC = "";
    private String urlWsPAC = "";
    
    public ConexionPAC(Configuracion configDto){
        
        userPAC = configDto.getUsuariopac();
        passPAC = configDto.getPasspac();
        urlWsPAC = configDto.getLigapac();
        
    }
    
    public WsGenericResp timbraComprobanteSectorPrimarioPAC(String certX509, String pkey, String pass,byte[] xmlCFDIbytes) throws UnsupportedEncodingException, MalformedURLException, Exception{
        
        
        URL urlPAC = new URL(urlWsPAC);
        
        File fileCertificadoEmisor = new File(certX509);
        File fileLlavePrivadaEmisor = new File(pkey);
        byte[] certificadoEmisor = FileManage.getBytesFromFile(fileCertificadoEmisor);;
        byte[] llavePrivadaEmisor = FileManage.getBytesFromFile(fileLlavePrivadaEmisor);
        
        /*return timbraEnviaCFDIReexpide(userPAC, passPAC, null, null, "", contenidoArchivo, "3.2",
                urlPAC);*/
        System.out.println("envio al pac sector primario--->");
        return enviarWSSPrimario(userPAC, passPAC, certificadoEmisor, llavePrivadaEmisor, pass, xmlCFDIbytes,
                urlPAC);
        
    }
    
    public void otorgarAccesoContribuyente(String contribuyenteRFC, String contribuyenteRazonSocial) throws UnsupportedEncodingException, MalformedURLException{
        
        
        URL urlPAC = new URL(urlWsPAC);
        
        /*return timbraEnviaCFDIReexpide(userPAC, passPAC, null, null, "", contenidoArchivo, "3.2",
                urlPAC);*/
        System.out.println("envio al pac  otorgarAccesoContribuyente sector primario--->");
        otorgarAccesoContribuyente(userPAC, userPAC, contribuyenteRFC, contribuyenteRazonSocial, urlPAC);
        
    }
    
    public WsGenericResp cancelaComprobanteSectorPrimarioPAC(byte[] xmlCFDIbytes, byte[] cerEmisor, byte[] keyEmisor, String passKeyEmisor) throws UnsupportedEncodingException, MalformedURLException{
        
        //String contenidoArchivo = new String(xmlCFDIbytes, "UTF8");
        
        URL urlPAC = new URL(urlWsPAC);
        
        return cancelarSectorPrimario(userPAC, passPAC, cerEmisor, keyEmisor, passKeyEmisor, xmlCFDIbytes, 
                urlPAC);
        
    }
    
    public WsGenericResp timbraComprobantePAC(byte[] xmlCFDIbytes) throws UnsupportedEncodingException, MalformedURLException{
        
        String contenidoArchivo = new String(xmlCFDIbytes, "UTF8");
        
        URL urlPAC = new URL(urlWsPAC);
        
        /*return timbraEnviaCFDIReexpide(userPAC, passPAC, null, null, "", contenidoArchivo, "3.2",
                urlPAC);*/
        System.out.println("envio al pac--->");
        return timbraEnviaCFDIxp(userPAC, passPAC, null, null, "", contenidoArchivo, "3.3",
                urlPAC);
        
    }
    
    public WsGenericResp timbraComprobanteEContabilidad(byte[] xmlCFDIbytes) throws UnsupportedEncodingException, MalformedURLException{
        
        String contenidoArchivo = new String(xmlCFDIbytes, "UTF8");
        
        URL urlPAC = new URL(urlWsPAC);
        
        /*return timbraEnviaCFDIReexpide(userPAC, passPAC, null, null, "", contenidoArchivo, "3.2",
                urlPAC);*/
        return timbraEnviaCFDIxp(userPAC, passPAC, null, null, "", contenidoArchivo, "cntElctronica",
                urlPAC);
        
    }
    
    public int verificaLoginPAC() throws MalformedURLException{
        URL urlPAC = new URL(urlWsPAC);
        
        return verificarLogin(userPAC, passPAC, urlPAC);
    }
    
    public WsGenericResp cancelaComprobantePAC(byte[] xmlCFDIbytes, byte[] cerEmisor, byte[] keyEmisor, String passKeyEmisor) throws UnsupportedEncodingException, MalformedURLException{
        
        String contenidoArchivo = new String(xmlCFDIbytes, "UTF8");
        
        URL urlPAC = new URL(urlWsPAC);
        
        return cancelaCFDI32(userPAC, passPAC, cerEmisor, keyEmisor, passKeyEmisor, contenidoArchivo, 
                urlPAC);
        
    }
    
    public WsGenericRespExt timbraRetencionesPAC(byte[] xmlCFDRetencionesBytes) throws UnsupportedEncodingException, MalformedURLException,Exception{
        
        //String contenidoArchivo = new String(xmlCFDRetencionesBytes, "UTF8");
        
        URL urlPAC = new URL(urlWsPAC);
        
        
        return timbraEnviaRetenciones(userPAC, passPAC, null, null, "", null, xmlCFDRetencionesBytes, false, false, "1.0",
                urlPAC);
        
    }
    
    public WsGenericResp cancelaRetencionesPAC(byte[] xmlCFDRetencionesBytes, byte[] cerEmisor, byte[] keyEmisor, String passKeyEmisor) throws UnsupportedEncodingException, MalformedURLException, Exception{
        
        String contenidoArchivo = new String(xmlCFDRetencionesBytes, "UTF8");
        
        URL urlPAC = new URL(urlWsPAC);
        
        return cancelaCFDIRetenciones(userPAC, passPAC, cerEmisor, keyEmisor, passKeyEmisor, contenidoArchivo, "1.0", null, 
                urlPAC);
        
    }
   
    
    private static WsGenericRespExt timbraEnviaCFDIReexpide(java.lang.String user, java.lang.String userPassword, byte[] certificadoEmisor, byte[] llavePrivadaEmisor, java.lang.String llavePrivadaEmisorPassword, java.lang.String xmlCFDI, String versionCFDI,
                URL urlWsPAC) {
        //com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService(urlWsPAC);
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        return port.timbraEnviaCFDIReexpide(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, xmlCFDI,versionCFDI);
    }
    
    private static WsGenericResp timbraEnviaCFDIxp(java.lang.String user, java.lang.String userPassword, byte[] certificadoEmisor, byte[] llavePrivadaEmisor, java.lang.String llavePrivadaEmisorPassword, java.lang.String xmlCFDI, String versionCFDI,
        URL urlWsPAC) {
        //com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService(urlWsPAC);
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        return port.timbraEnviaCFDIxp(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, xmlCFDI,versionCFDI);
    }
    
    private static int verificarLogin(java.lang.String user, java.lang.String userPassword,
            URL urlWsPAC) {
        //com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService(urlWsPAC);
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        return port.verificarLogin(user, userPassword);
    }
    
    private static WsGenericResp cancelaCFDI32(java.lang.String user, java.lang.String userPassword, byte[] certificadoEmisor, byte[] llavePrivadaEmisor, java.lang.String llavePrivadaEmisorPassword, java.lang.String xmlCFDI,
            URL urlWsPAC) {
        //com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService(urlWsPAC);
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        return port.cancelaCFDI32(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, xmlCFDI);
    }
    
    private static WsGenericRespExt timbraEnviaRetenciones(java.lang.String user, java.lang.String userPassword, byte[] certificadoEmisor, byte[] llavePrivadaEmisor, java.lang.String llavePrivadaEmisorPassword, java.lang.String cadenaXmlCFDI, byte[] bytesXmlCFDI, boolean controlRetimbrado, boolean requiereSelladoEmisor, java.lang.String versionCFDI,
            URL urlWsPAC) throws Exception {
        //com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService(urlWsPAC);
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        //throw new Exception("Método de Web Service no implementado");
        return port.timbraEnviaRetenciones(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, cadenaXmlCFDI, bytesXmlCFDI, controlRetimbrado, requiereSelladoEmisor, versionCFDI);
    }
    
    private static WsGenericResp cancelaCFDIRetenciones(java.lang.String user, java.lang.String userPassword, byte[] certificadoEmisor, byte[] llavePrivadaEmisor, java.lang.String llavePrivadaEmisorPassword, java.lang.String xmlCFDI, java.lang.String versionCFDI, java.lang.String xmlPeticionCancelacionSellada,
            URL urlWsPAC) throws Exception {
        //com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService();
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService(urlWsPAC);
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        //throw new Exception("Método de Web Service no implementado");
        return port.cancelaCFDIRetenciones(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, xmlCFDI, versionCFDI, xmlPeticionCancelacionSellada);
    }
    
    public WsGenericResp enviarWSSPrimario(java.lang.String user, java.lang.String userPassword, byte[] certificadoSPrimario, byte[] llavePrivadaSPrimario, java.lang.String llavePrivadaPassword, byte[] bytesXmlCFDI, URL urlWsPAC) {
        
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService(urlWsPAC);
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();       
        return port.timbraEnviaCFDIBytesSPrimario(user, userPassword, certificadoSPrimario, llavePrivadaSPrimario, llavePrivadaPassword, bytesXmlCFDI,"3.3");
        
        /*
        WsGenericResp resp = new WsGenericResp();
        resp.setIsError(true);
        resp.setNumError(902);
        resp.setErrorMessage("Método no implementado.");        
        return resp;*/
    }
    
    public WsInsertaClienteResp otorgarAccesoContribuyente(java.lang.String user, java.lang.String userPassword, java.lang.String contribuyenteRFC, java.lang.String contribuyenteRazonSocial, URL urlWsPAC) {
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService(urlWsPAC);
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        return port.otorgarAccesoContribuyente(user, userPassword, contribuyenteRFC, contribuyenteRazonSocial);
    }
    
    public WsGenericResp cancelarSectorPrimario(java.lang.String user, java.lang.String userPassword, byte[] certificadoEmisor, byte[] llavePrivadaEmisor, java.lang.String llavePrivadaEmisorPassword, byte[] xmlCFDI, URL urlWsPAC) {
        com.tsp.interconecta.ws.InterconectaWsService service = new com.tsp.interconecta.ws.InterconectaWsService(urlWsPAC);
        com.tsp.interconecta.ws.InterconectaWs port = service.getInterconectaWsPort();
        return port.cancelaCFDISectorPrimario(user, userPassword, certificadoEmisor, llavePrivadaEmisor, llavePrivadaEmisorPassword, xmlCFDI);
    }
    
}
