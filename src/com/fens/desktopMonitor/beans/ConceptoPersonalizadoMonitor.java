/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fens.desktopMonitor.beans;

import java.math.BigDecimal;

/**
 *
 * @author ISCesar
 */
public class ConceptoPersonalizadoMonitor {

    //Datos generales de Concepto de acuerdo a XSD SAT CFDI 3.2
    private BigDecimal cantidad;
    private String unidad;
    private String noIdentificacion;
    private String descripcion;
    private BigDecimal valorUnitario;
    private BigDecimal importe;
    private String claveUnidad;
    private String claveProdServ;
    
    //Datos personalizados de monitor CSV por cada concepto
    private String nombre;
    private BigDecimal porcentajeDescuento;
    private BigDecimal montoDescuento;
    private BigDecimal IVAporcentaje;
    private BigDecimal IVAmonto;
    private BigDecimal IEPSporcentaje;
    private BigDecimal IEPSmonto;
    private BigDecimal IVARetenidoPorcentaje;
    private BigDecimal IVARetenidoMonto;
    private BigDecimal ISRPorcentaje;
    private BigDecimal ISRMonto;

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getNoIdentificacion() {
        return noIdentificacion;
    }

    public void setNoIdentificacion(String noIdentificacion) {
        this.noIdentificacion = noIdentificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public BigDecimal getMontoDescuento() {
        return montoDescuento;
    }

    public void setMontoDescuento(BigDecimal montoDescuento) {
        this.montoDescuento = montoDescuento;
    }

    public BigDecimal getIVAporcentaje() {
        return IVAporcentaje;
    }

    public void setIVAporcentaje(BigDecimal IVAporcentaje) {
        this.IVAporcentaje = IVAporcentaje;
    }

    public BigDecimal getIVAmonto() {
        return IVAmonto;
    }

    public void setIVAmonto(BigDecimal IVAmonto) {
        this.IVAmonto = IVAmonto;
    }

    public BigDecimal getIEPSporcentaje() {
        return IEPSporcentaje;
    }

    public void setIEPSporcentaje(BigDecimal IEPSporcentaje) {
        this.IEPSporcentaje = IEPSporcentaje;
    }

    public BigDecimal getIEPSmonto() {
        return IEPSmonto;
    }

    public void setIEPSmonto(BigDecimal IEPSmonto) {
        this.IEPSmonto = IEPSmonto;
    }

    public BigDecimal getIVARetenidoPorcentaje() {
        return IVARetenidoPorcentaje;
    }

    public void setIVARetenidoPorcentaje(BigDecimal IVARetenidoPorcentaje) {
        this.IVARetenidoPorcentaje = IVARetenidoPorcentaje;
    }

    public BigDecimal getIVARetenidoMonto() {
        return IVARetenidoMonto;
    }

    public void setIVARetenidoMonto(BigDecimal IVARetenidoMonto) {
        this.IVARetenidoMonto = IVARetenidoMonto;
    }

    public BigDecimal getISRPorcentaje() {
        return ISRPorcentaje;
    }

    public void setISRPorcentaje(BigDecimal ISRPorcentaje) {
        this.ISRPorcentaje = ISRPorcentaje;
    }

    public BigDecimal getISRMonto() {
        return ISRMonto;
    }

    public void setISRMonto(BigDecimal ISRMonto) {
        this.ISRMonto = ISRMonto;
    }

    public String getClaveProdServ() {
        return claveProdServ;
    }

    public void setClaveProdServ(String claveProdServ) {
        this.claveProdServ = claveProdServ;
    }

    public String getClaveUnidad() {
        return claveUnidad;
    }

    public void setClaveUnidad(String claveUnidad) {
        this.claveUnidad = claveUnidad;
    }
    
    

}
