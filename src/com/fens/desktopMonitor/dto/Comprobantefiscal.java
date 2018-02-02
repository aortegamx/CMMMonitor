/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.fens.desktopMonitor.dto;

import com.fens.desktopMonitor.dao.*;
import com.fens.desktopMonitor.factory.*;
import com.fens.desktopMonitor.exceptions.*;
import java.io.Serializable;
import java.util.*;
import java.util.Date;

public class Comprobantefiscal implements Serializable
{
	/** 
	 * This attribute maps to the column IDCOMPROBANTEFISCAL in the COMPROBANTEFISCAL table.
	 */
	protected int idcomprobantefiscal;

	/** 
	 * This attribute represents whether the attribute idcomprobantefiscal has been modified since being read from the database.
	 */
	protected boolean idcomprobantefiscalModified = false;

	/** 
	 * This attribute maps to the column IDEMISOR in the COMPROBANTEFISCAL table.
	 */
	protected int idemisor;

	/** 
	 * This attribute represents whether the attribute idemisor has been modified since being read from the database.
	 */
	protected boolean idemisorModified = false;

	/** 
	 * This attribute maps to the column IDARCHIVOMAESTRO in the COMPROBANTEFISCAL table.
	 */
	protected int idarchivomaestro;

	/** 
	 * This attribute represents whether the primitive attribute idarchivomaestro is null.
	 */
	protected boolean idarchivomaestroNull = true;

	/** 
	 * This attribute represents whether the attribute idarchivomaestro has been modified since being read from the database.
	 */
	protected boolean idarchivomaestroModified = false;

	/** 
	 * This attribute maps to the column IDESTATUS in the COMPROBANTEFISCAL table.
	 */
	protected int idestatus;

	/** 
	 * This attribute represents whether the attribute idestatus has been modified since being read from the database.
	 */
	protected boolean idestatusModified = false;

	/** 
	 * This attribute maps to the column IDTIPOCOMPROBANTE in the COMPROBANTEFISCAL table.
	 */
	protected int idtipocomprobante;

	/** 
	 * This attribute represents whether the attribute idtipocomprobante has been modified since being read from the database.
	 */
	protected boolean idtipocomprobanteModified = false;

	/** 
	 * This attribute maps to the column NOMBREARCHIVOXML in the COMPROBANTEFISCAL table.
	 */
	protected String nombrearchivoxml;

	/** 
	 * This attribute represents whether the attribute nombrearchivoxml has been modified since being read from the database.
	 */
	protected boolean nombrearchivoxmlModified = false;

	/** 
	 * This attribute maps to the column NOMBREARCHIVOPDF in the COMPROBANTEFISCAL table.
	 */
	protected String nombrearchivopdf;

	/** 
	 * This attribute represents whether the attribute nombrearchivopdf has been modified since being read from the database.
	 */
	protected boolean nombrearchivopdfModified = false;

	/** 
	 * This attribute maps to the column SERIE in the COMPROBANTEFISCAL table.
	 */
	protected String serie;

	/** 
	 * This attribute represents whether the attribute serie has been modified since being read from the database.
	 */
	protected boolean serieModified = false;

	/** 
	 * This attribute maps to the column FOLIO in the COMPROBANTEFISCAL table.
	 */
	protected String folio;

	/** 
	 * This attribute represents whether the attribute folio has been modified since being read from the database.
	 */
	protected boolean folioModified = false;

	/** 
	 * This attribute maps to the column UUID in the COMPROBANTEFISCAL table.
	 */
	protected String uuid;

	/** 
	 * This attribute represents whether the attribute uuid has been modified since being read from the database.
	 */
	protected boolean uuidModified = false;

	/** 
	 * This attribute maps to the column SELLOEMISOR in the COMPROBANTEFISCAL table.
	 */
	protected String selloemisor;

	/** 
	 * This attribute represents whether the attribute selloemisor has been modified since being read from the database.
	 */
	protected boolean selloemisorModified = false;

	/** 
	 * This attribute maps to the column FECHAHORASELLADO in the COMPROBANTEFISCAL table.
	 */
	protected Date fechahorasellado;

	/** 
	 * This attribute represents whether the attribute fechahorasellado has been modified since being read from the database.
	 */
	protected boolean fechahoraselladoModified = false;

	/** 
	 * This attribute maps to the column FECHAHORATIMBRADO in the COMPROBANTEFISCAL table.
	 */
	protected Date fechahoratimbrado;

	/** 
	 * This attribute represents whether the attribute fechahoratimbrado has been modified since being read from the database.
	 */
	protected boolean fechahoratimbradoModified = false;

	/** 
	 * This attribute maps to the column FECHAHORAPROCESO in the COMPROBANTEFISCAL table.
	 */
	protected Date fechahoraproceso;

	/** 
	 * This attribute represents whether the attribute fechahoraproceso has been modified since being read from the database.
	 */
	protected boolean fechahoraprocesoModified = false;

	/** 
	 * This attribute maps to the column RFCRECEPTOR in the COMPROBANTEFISCAL table.
	 */
	protected String rfcreceptor;

	/** 
	 * This attribute represents whether the attribute rfcreceptor has been modified since being read from the database.
	 */
	protected boolean rfcreceptorModified = false;

	/** 
	 * This attribute maps to the column SUBTOTAL in the COMPROBANTEFISCAL table.
	 */
	protected double subtotal;

	/** 
	 * This attribute represents whether the primitive attribute subtotal is null.
	 */
	protected boolean subtotalNull = true;

	/** 
	 * This attribute represents whether the attribute subtotal has been modified since being read from the database.
	 */
	protected boolean subtotalModified = false;

	/** 
	 * This attribute maps to the column TOTAL in the COMPROBANTEFISCAL table.
	 */
	protected double total;

	/** 
	 * This attribute represents whether the primitive attribute total is null.
	 */
	protected boolean totalNull = true;

	/** 
	 * This attribute represents whether the attribute total has been modified since being read from the database.
	 */
	protected boolean totalModified = false;

	/** 
	 * This attribute maps to the column CADENAORIGINAL in the COMPROBANTEFISCAL table.
	 */
	protected String cadenaoriginal;

	/** 
	 * This attribute represents whether the attribute cadenaoriginal has been modified since being read from the database.
	 */
	protected boolean cadenaoriginalModified = false;

	/** 
	 * This attribute maps to the column FECHACANCELACION in the COMPROBANTEFISCAL table.
	 */
	protected Date fechacancelacion;

	/** 
	 * This attribute represents whether the attribute fechacancelacion has been modified since being read from the database.
	 */
	protected boolean fechacancelacionModified = false;

	/** 
	 * This attribute maps to the column MONEDA in the COMPROBANTEFISCAL table.
	 */
	protected String moneda;

	/** 
	 * This attribute represents whether the attribute moneda has been modified since being read from the database.
	 */
	protected boolean monedaModified = false;

	/** 
	 * This attribute maps to the column OBSERVACIONES in the COMPROBANTEFISCAL table.
	 */
	protected String observaciones;

	/** 
	 * This attribute represents whether the attribute observaciones has been modified since being read from the database.
	 */
	protected boolean observacionesModified = false;

	/** 
	 * This attribute maps to the column NOMBRE_RECEPTOR in the COMPROBANTEFISCAL table.
	 */
	protected String nombreReceptor;

	/** 
	 * This attribute represents whether the attribute nombreReceptor has been modified since being read from the database.
	 */
	protected boolean nombreReceptorModified = false;

	/** 
	 * This attribute maps to the column EMAIL in the COMPROBANTEFISCAL table.
	 */
	protected String email;

	/** 
	 * This attribute represents whether the attribute email has been modified since being read from the database.
	 */
	protected boolean emailModified = false;

	/** 
	 * This attribute maps to the column REFERENCIA1 in the COMPROBANTEFISCAL table.
	 */
	protected String referencia1;

	/** 
	 * This attribute represents whether the attribute referencia1 has been modified since being read from the database.
	 */
	protected boolean referencia1Modified = false;

	/** 
	 * This attribute maps to the column REFERENCIA2 in the COMPROBANTEFISCAL table.
	 */
	protected String referencia2;

	/** 
	 * This attribute represents whether the attribute referencia2 has been modified since being read from the database.
	 */
	protected boolean referencia2Modified = false;

	/** 
	 * This attribute maps to the column REFERENCIA3 in the COMPROBANTEFISCAL table.
	 */
	protected String referencia3;

	/** 
	 * This attribute represents whether the attribute referencia3 has been modified since being read from the database.
	 */
	protected boolean referencia3Modified = false;

	/**
	 * Method 'Comprobantefiscal'
	 * 
	 */
	public Comprobantefiscal()
	{
	}

	/**
	 * Method 'getIdcomprobantefiscal'
	 * 
	 * @return int
	 */
	public int getIdcomprobantefiscal()
	{
		return idcomprobantefiscal;
	}

	/**
	 * Method 'setIdcomprobantefiscal'
	 * 
	 * @param idcomprobantefiscal
	 */
	public void setIdcomprobantefiscal(int idcomprobantefiscal)
	{
		this.idcomprobantefiscal = idcomprobantefiscal;
		this.idcomprobantefiscalModified = true;
	}

	/** 
	 * Sets the value of idcomprobantefiscalModified
	 */
	public void setIdcomprobantefiscalModified(boolean idcomprobantefiscalModified)
	{
		this.idcomprobantefiscalModified = idcomprobantefiscalModified;
	}

	/** 
	 * Gets the value of idcomprobantefiscalModified
	 */
	public boolean isIdcomprobantefiscalModified()
	{
		return idcomprobantefiscalModified;
	}

	/**
	 * Method 'getIdemisor'
	 * 
	 * @return int
	 */
	public int getIdemisor()
	{
		return idemisor;
	}

	/**
	 * Method 'setIdemisor'
	 * 
	 * @param idemisor
	 */
	public void setIdemisor(int idemisor)
	{
		this.idemisor = idemisor;
		this.idemisorModified = true;
	}

	/** 
	 * Sets the value of idemisorModified
	 */
	public void setIdemisorModified(boolean idemisorModified)
	{
		this.idemisorModified = idemisorModified;
	}

	/** 
	 * Gets the value of idemisorModified
	 */
	public boolean isIdemisorModified()
	{
		return idemisorModified;
	}

	/**
	 * Method 'getIdarchivomaestro'
	 * 
	 * @return int
	 */
	public int getIdarchivomaestro()
	{
		return idarchivomaestro;
	}

	/**
	 * Method 'setIdarchivomaestro'
	 * 
	 * @param idarchivomaestro
	 */
	public void setIdarchivomaestro(int idarchivomaestro)
	{
		this.idarchivomaestro = idarchivomaestro;
		this.idarchivomaestroNull = false;
		this.idarchivomaestroModified = true;
	}

	/**
	 * Method 'setIdarchivomaestroNull'
	 * 
	 * @param value
	 */
	public void setIdarchivomaestroNull(boolean value)
	{
		this.idarchivomaestroNull = value;
		this.idarchivomaestroModified = true;
	}

	/**
	 * Method 'isIdarchivomaestroNull'
	 * 
	 * @return boolean
	 */
	public boolean isIdarchivomaestroNull()
	{
		return idarchivomaestroNull;
	}

	/** 
	 * Sets the value of idarchivomaestroModified
	 */
	public void setIdarchivomaestroModified(boolean idarchivomaestroModified)
	{
		this.idarchivomaestroModified = idarchivomaestroModified;
	}

	/** 
	 * Gets the value of idarchivomaestroModified
	 */
	public boolean isIdarchivomaestroModified()
	{
		return idarchivomaestroModified;
	}

	/**
	 * Method 'getIdestatus'
	 * 
	 * @return int
	 */
	public int getIdestatus()
	{
		return idestatus;
	}

	/**
	 * Method 'setIdestatus'
	 * 
	 * @param idestatus
	 */
	public void setIdestatus(int idestatus)
	{
		this.idestatus = idestatus;
		this.idestatusModified = true;
	}

	/** 
	 * Sets the value of idestatusModified
	 */
	public void setIdestatusModified(boolean idestatusModified)
	{
		this.idestatusModified = idestatusModified;
	}

	/** 
	 * Gets the value of idestatusModified
	 */
	public boolean isIdestatusModified()
	{
		return idestatusModified;
	}

	/**
	 * Method 'getIdtipocomprobante'
	 * 
	 * @return int
	 */
	public int getIdtipocomprobante()
	{
		return idtipocomprobante;
	}

	/**
	 * Method 'setIdtipocomprobante'
	 * 
	 * @param idtipocomprobante
	 */
	public void setIdtipocomprobante(int idtipocomprobante)
	{
		this.idtipocomprobante = idtipocomprobante;
		this.idtipocomprobanteModified = true;
	}

	/** 
	 * Sets the value of idtipocomprobanteModified
	 */
	public void setIdtipocomprobanteModified(boolean idtipocomprobanteModified)
	{
		this.idtipocomprobanteModified = idtipocomprobanteModified;
	}

	/** 
	 * Gets the value of idtipocomprobanteModified
	 */
	public boolean isIdtipocomprobanteModified()
	{
		return idtipocomprobanteModified;
	}

	/**
	 * Method 'getNombrearchivoxml'
	 * 
	 * @return String
	 */
	public String getNombrearchivoxml()
	{
		return nombrearchivoxml;
	}

	/**
	 * Method 'setNombrearchivoxml'
	 * 
	 * @param nombrearchivoxml
	 */
	public void setNombrearchivoxml(String nombrearchivoxml)
	{
		this.nombrearchivoxml = nombrearchivoxml;
		this.nombrearchivoxmlModified = true;
	}

	/** 
	 * Sets the value of nombrearchivoxmlModified
	 */
	public void setNombrearchivoxmlModified(boolean nombrearchivoxmlModified)
	{
		this.nombrearchivoxmlModified = nombrearchivoxmlModified;
	}

	/** 
	 * Gets the value of nombrearchivoxmlModified
	 */
	public boolean isNombrearchivoxmlModified()
	{
		return nombrearchivoxmlModified;
	}

	/**
	 * Method 'getNombrearchivopdf'
	 * 
	 * @return String
	 */
	public String getNombrearchivopdf()
	{
		return nombrearchivopdf;
	}

	/**
	 * Method 'setNombrearchivopdf'
	 * 
	 * @param nombrearchivopdf
	 */
	public void setNombrearchivopdf(String nombrearchivopdf)
	{
		this.nombrearchivopdf = nombrearchivopdf;
		this.nombrearchivopdfModified = true;
	}

	/** 
	 * Sets the value of nombrearchivopdfModified
	 */
	public void setNombrearchivopdfModified(boolean nombrearchivopdfModified)
	{
		this.nombrearchivopdfModified = nombrearchivopdfModified;
	}

	/** 
	 * Gets the value of nombrearchivopdfModified
	 */
	public boolean isNombrearchivopdfModified()
	{
		return nombrearchivopdfModified;
	}

	/**
	 * Method 'getSerie'
	 * 
	 * @return String
	 */
	public String getSerie()
	{
		return serie;
	}

	/**
	 * Method 'setSerie'
	 * 
	 * @param serie
	 */
	public void setSerie(String serie)
	{
		this.serie = serie;
		this.serieModified = true;
	}

	/** 
	 * Sets the value of serieModified
	 */
	public void setSerieModified(boolean serieModified)
	{
		this.serieModified = serieModified;
	}

	/** 
	 * Gets the value of serieModified
	 */
	public boolean isSerieModified()
	{
		return serieModified;
	}

	/**
	 * Method 'getFolio'
	 * 
	 * @return String
	 */
	public String getFolio()
	{
		return folio;
	}

	/**
	 * Method 'setFolio'
	 * 
	 * @param folio
	 */
	public void setFolio(String folio)
	{
		this.folio = folio;
		this.folioModified = true;
	}

	/** 
	 * Sets the value of folioModified
	 */
	public void setFolioModified(boolean folioModified)
	{
		this.folioModified = folioModified;
	}

	/** 
	 * Gets the value of folioModified
	 */
	public boolean isFolioModified()
	{
		return folioModified;
	}

	/**
	 * Method 'getUuid'
	 * 
	 * @return String
	 */
	public String getUuid()
	{
		return uuid;
	}

	/**
	 * Method 'setUuid'
	 * 
	 * @param uuid
	 */
	public void setUuid(String uuid)
	{
		this.uuid = uuid;
		this.uuidModified = true;
	}

	/** 
	 * Sets the value of uuidModified
	 */
	public void setUuidModified(boolean uuidModified)
	{
		this.uuidModified = uuidModified;
	}

	/** 
	 * Gets the value of uuidModified
	 */
	public boolean isUuidModified()
	{
		return uuidModified;
	}

	/**
	 * Method 'getSelloemisor'
	 * 
	 * @return String
	 */
	public String getSelloemisor()
	{
		return selloemisor;
	}

	/**
	 * Method 'setSelloemisor'
	 * 
	 * @param selloemisor
	 */
	public void setSelloemisor(String selloemisor)
	{
		this.selloemisor = selloemisor;
		this.selloemisorModified = true;
	}

	/** 
	 * Sets the value of selloemisorModified
	 */
	public void setSelloemisorModified(boolean selloemisorModified)
	{
		this.selloemisorModified = selloemisorModified;
	}

	/** 
	 * Gets the value of selloemisorModified
	 */
	public boolean isSelloemisorModified()
	{
		return selloemisorModified;
	}

	/**
	 * Method 'getFechahorasellado'
	 * 
	 * @return Date
	 */
	public Date getFechahorasellado()
	{
		return fechahorasellado;
	}

	/**
	 * Method 'setFechahorasellado'
	 * 
	 * @param fechahorasellado
	 */
	public void setFechahorasellado(Date fechahorasellado)
	{
		this.fechahorasellado = fechahorasellado;
		this.fechahoraselladoModified = true;
	}

	/** 
	 * Sets the value of fechahoraselladoModified
	 */
	public void setFechahoraselladoModified(boolean fechahoraselladoModified)
	{
		this.fechahoraselladoModified = fechahoraselladoModified;
	}

	/** 
	 * Gets the value of fechahoraselladoModified
	 */
	public boolean isFechahoraselladoModified()
	{
		return fechahoraselladoModified;
	}

	/**
	 * Method 'getFechahoratimbrado'
	 * 
	 * @return Date
	 */
	public Date getFechahoratimbrado()
	{
		return fechahoratimbrado;
	}

	/**
	 * Method 'setFechahoratimbrado'
	 * 
	 * @param fechahoratimbrado
	 */
	public void setFechahoratimbrado(Date fechahoratimbrado)
	{
		this.fechahoratimbrado = fechahoratimbrado;
		this.fechahoratimbradoModified = true;
	}

	/** 
	 * Sets the value of fechahoratimbradoModified
	 */
	public void setFechahoratimbradoModified(boolean fechahoratimbradoModified)
	{
		this.fechahoratimbradoModified = fechahoratimbradoModified;
	}

	/** 
	 * Gets the value of fechahoratimbradoModified
	 */
	public boolean isFechahoratimbradoModified()
	{
		return fechahoratimbradoModified;
	}

	/**
	 * Method 'getFechahoraproceso'
	 * 
	 * @return Date
	 */
	public Date getFechahoraproceso()
	{
		return fechahoraproceso;
	}

	/**
	 * Method 'setFechahoraproceso'
	 * 
	 * @param fechahoraproceso
	 */
	public void setFechahoraproceso(Date fechahoraproceso)
	{
		this.fechahoraproceso = fechahoraproceso;
		this.fechahoraprocesoModified = true;
	}

	/** 
	 * Sets the value of fechahoraprocesoModified
	 */
	public void setFechahoraprocesoModified(boolean fechahoraprocesoModified)
	{
		this.fechahoraprocesoModified = fechahoraprocesoModified;
	}

	/** 
	 * Gets the value of fechahoraprocesoModified
	 */
	public boolean isFechahoraprocesoModified()
	{
		return fechahoraprocesoModified;
	}

	/**
	 * Method 'getRfcreceptor'
	 * 
	 * @return String
	 */
	public String getRfcreceptor()
	{
		return rfcreceptor;
	}

	/**
	 * Method 'setRfcreceptor'
	 * 
	 * @param rfcreceptor
	 */
	public void setRfcreceptor(String rfcreceptor)
	{
		this.rfcreceptor = rfcreceptor;
		this.rfcreceptorModified = true;
	}

	/** 
	 * Sets the value of rfcreceptorModified
	 */
	public void setRfcreceptorModified(boolean rfcreceptorModified)
	{
		this.rfcreceptorModified = rfcreceptorModified;
	}

	/** 
	 * Gets the value of rfcreceptorModified
	 */
	public boolean isRfcreceptorModified()
	{
		return rfcreceptorModified;
	}

	/**
	 * Method 'getSubtotal'
	 * 
	 * @return double
	 */
	public double getSubtotal()
	{
		return subtotal;
	}

	/**
	 * Method 'setSubtotal'
	 * 
	 * @param subtotal
	 */
	public void setSubtotal(double subtotal)
	{
		this.subtotal = subtotal;
		this.subtotalNull = false;
		this.subtotalModified = true;
	}

	/**
	 * Method 'setSubtotalNull'
	 * 
	 * @param value
	 */
	public void setSubtotalNull(boolean value)
	{
		this.subtotalNull = value;
		this.subtotalModified = true;
	}

	/**
	 * Method 'isSubtotalNull'
	 * 
	 * @return boolean
	 */
	public boolean isSubtotalNull()
	{
		return subtotalNull;
	}

	/** 
	 * Sets the value of subtotalModified
	 */
	public void setSubtotalModified(boolean subtotalModified)
	{
		this.subtotalModified = subtotalModified;
	}

	/** 
	 * Gets the value of subtotalModified
	 */
	public boolean isSubtotalModified()
	{
		return subtotalModified;
	}

	/**
	 * Method 'getTotal'
	 * 
	 * @return double
	 */
	public double getTotal()
	{
		return total;
	}

	/**
	 * Method 'setTotal'
	 * 
	 * @param total
	 */
	public void setTotal(double total)
	{
		this.total = total;
		this.totalNull = false;
		this.totalModified = true;
	}

	/**
	 * Method 'setTotalNull'
	 * 
	 * @param value
	 */
	public void setTotalNull(boolean value)
	{
		this.totalNull = value;
		this.totalModified = true;
	}

	/**
	 * Method 'isTotalNull'
	 * 
	 * @return boolean
	 */
	public boolean isTotalNull()
	{
		return totalNull;
	}

	/** 
	 * Sets the value of totalModified
	 */
	public void setTotalModified(boolean totalModified)
	{
		this.totalModified = totalModified;
	}

	/** 
	 * Gets the value of totalModified
	 */
	public boolean isTotalModified()
	{
		return totalModified;
	}

	/**
	 * Method 'getCadenaoriginal'
	 * 
	 * @return String
	 */
	public String getCadenaoriginal()
	{
		return cadenaoriginal;
	}

	/**
	 * Method 'setCadenaoriginal'
	 * 
	 * @param cadenaoriginal
	 */
	public void setCadenaoriginal(String cadenaoriginal)
	{
		this.cadenaoriginal = cadenaoriginal;
		this.cadenaoriginalModified = true;
	}

	/** 
	 * Sets the value of cadenaoriginalModified
	 */
	public void setCadenaoriginalModified(boolean cadenaoriginalModified)
	{
		this.cadenaoriginalModified = cadenaoriginalModified;
	}

	/** 
	 * Gets the value of cadenaoriginalModified
	 */
	public boolean isCadenaoriginalModified()
	{
		return cadenaoriginalModified;
	}

	/**
	 * Method 'getFechacancelacion'
	 * 
	 * @return Date
	 */
	public Date getFechacancelacion()
	{
		return fechacancelacion;
	}

	/**
	 * Method 'setFechacancelacion'
	 * 
	 * @param fechacancelacion
	 */
	public void setFechacancelacion(Date fechacancelacion)
	{
		this.fechacancelacion = fechacancelacion;
		this.fechacancelacionModified = true;
	}

	/** 
	 * Sets the value of fechacancelacionModified
	 */
	public void setFechacancelacionModified(boolean fechacancelacionModified)
	{
		this.fechacancelacionModified = fechacancelacionModified;
	}

	/** 
	 * Gets the value of fechacancelacionModified
	 */
	public boolean isFechacancelacionModified()
	{
		return fechacancelacionModified;
	}

	/**
	 * Method 'getMoneda'
	 * 
	 * @return String
	 */
	public String getMoneda()
	{
		return moneda;
	}

	/**
	 * Method 'setMoneda'
	 * 
	 * @param moneda
	 */
	public void setMoneda(String moneda)
	{
		this.moneda = moneda;
		this.monedaModified = true;
	}

	/** 
	 * Sets the value of monedaModified
	 */
	public void setMonedaModified(boolean monedaModified)
	{
		this.monedaModified = monedaModified;
	}

	/** 
	 * Gets the value of monedaModified
	 */
	public boolean isMonedaModified()
	{
		return monedaModified;
	}

	/**
	 * Method 'getObservaciones'
	 * 
	 * @return String
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Method 'setObservaciones'
	 * 
	 * @param observaciones
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
		this.observacionesModified = true;
	}

	/** 
	 * Sets the value of observacionesModified
	 */
	public void setObservacionesModified(boolean observacionesModified)
	{
		this.observacionesModified = observacionesModified;
	}

	/** 
	 * Gets the value of observacionesModified
	 */
	public boolean isObservacionesModified()
	{
		return observacionesModified;
	}

	/**
	 * Method 'getNombreReceptor'
	 * 
	 * @return String
	 */
	public String getNombreReceptor()
	{
		return nombreReceptor;
	}

	/**
	 * Method 'setNombreReceptor'
	 * 
	 * @param nombreReceptor
	 */
	public void setNombreReceptor(String nombreReceptor)
	{
		this.nombreReceptor = nombreReceptor;
		this.nombreReceptorModified = true;
	}

	/** 
	 * Sets the value of nombreReceptorModified
	 */
	public void setNombreReceptorModified(boolean nombreReceptorModified)
	{
		this.nombreReceptorModified = nombreReceptorModified;
	}

	/** 
	 * Gets the value of nombreReceptorModified
	 */
	public boolean isNombreReceptorModified()
	{
		return nombreReceptorModified;
	}

	/**
	 * Method 'getEmail'
	 * 
	 * @return String
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * Method 'setEmail'
	 * 
	 * @param email
	 */
	public void setEmail(String email)
	{
		this.email = email;
		this.emailModified = true;
	}

	/** 
	 * Sets the value of emailModified
	 */
	public void setEmailModified(boolean emailModified)
	{
		this.emailModified = emailModified;
	}

	/** 
	 * Gets the value of emailModified
	 */
	public boolean isEmailModified()
	{
		return emailModified;
	}

	/**
	 * Method 'getReferencia1'
	 * 
	 * @return String
	 */
	public String getReferencia1()
	{
		return referencia1;
	}

	/**
	 * Method 'setReferencia1'
	 * 
	 * @param referencia1
	 */
	public void setReferencia1(String referencia1)
	{
		this.referencia1 = referencia1;
		this.referencia1Modified = true;
	}

	/** 
	 * Sets the value of referencia1Modified
	 */
	public void setReferencia1Modified(boolean referencia1Modified)
	{
		this.referencia1Modified = referencia1Modified;
	}

	/** 
	 * Gets the value of referencia1Modified
	 */
	public boolean isReferencia1Modified()
	{
		return referencia1Modified;
	}

	/**
	 * Method 'getReferencia2'
	 * 
	 * @return String
	 */
	public String getReferencia2()
	{
		return referencia2;
	}

	/**
	 * Method 'setReferencia2'
	 * 
	 * @param referencia2
	 */
	public void setReferencia2(String referencia2)
	{
		this.referencia2 = referencia2;
		this.referencia2Modified = true;
	}

	/** 
	 * Sets the value of referencia2Modified
	 */
	public void setReferencia2Modified(boolean referencia2Modified)
	{
		this.referencia2Modified = referencia2Modified;
	}

	/** 
	 * Gets the value of referencia2Modified
	 */
	public boolean isReferencia2Modified()
	{
		return referencia2Modified;
	}

	/**
	 * Method 'getReferencia3'
	 * 
	 * @return String
	 */
	public String getReferencia3()
	{
		return referencia3;
	}

	/**
	 * Method 'setReferencia3'
	 * 
	 * @param referencia3
	 */
	public void setReferencia3(String referencia3)
	{
		this.referencia3 = referencia3;
		this.referencia3Modified = true;
	}

	/** 
	 * Sets the value of referencia3Modified
	 */
	public void setReferencia3Modified(boolean referencia3Modified)
	{
		this.referencia3Modified = referencia3Modified;
	}

	/** 
	 * Gets the value of referencia3Modified
	 */
	public boolean isReferencia3Modified()
	{
		return referencia3Modified;
	}

	/**
	 * Method 'equals'
	 * 
	 * @param _other
	 * @return boolean
	 */
	public boolean equals(Object _other)
	{
		if (_other == null) {
			return false;
		}
		
		if (_other == this) {
			return true;
		}
		
		if (!(_other instanceof Comprobantefiscal)) {
			return false;
		}
		
		final Comprobantefiscal _cast = (Comprobantefiscal) _other;
		if (idcomprobantefiscal != _cast.idcomprobantefiscal) {
			return false;
		}
		
		if (idcomprobantefiscalModified != _cast.idcomprobantefiscalModified) {
			return false;
		}
		
		if (idemisor != _cast.idemisor) {
			return false;
		}
		
		if (idemisorModified != _cast.idemisorModified) {
			return false;
		}
		
		if (idarchivomaestro != _cast.idarchivomaestro) {
			return false;
		}
		
		if (idarchivomaestroNull != _cast.idarchivomaestroNull) {
			return false;
		}
		
		if (idarchivomaestroModified != _cast.idarchivomaestroModified) {
			return false;
		}
		
		if (idestatus != _cast.idestatus) {
			return false;
		}
		
		if (idestatusModified != _cast.idestatusModified) {
			return false;
		}
		
		if (idtipocomprobante != _cast.idtipocomprobante) {
			return false;
		}
		
		if (idtipocomprobanteModified != _cast.idtipocomprobanteModified) {
			return false;
		}
		
		if (nombrearchivoxml == null ? _cast.nombrearchivoxml != nombrearchivoxml : !nombrearchivoxml.equals( _cast.nombrearchivoxml )) {
			return false;
		}
		
		if (nombrearchivoxmlModified != _cast.nombrearchivoxmlModified) {
			return false;
		}
		
		if (nombrearchivopdf == null ? _cast.nombrearchivopdf != nombrearchivopdf : !nombrearchivopdf.equals( _cast.nombrearchivopdf )) {
			return false;
		}
		
		if (nombrearchivopdfModified != _cast.nombrearchivopdfModified) {
			return false;
		}
		
		if (serie == null ? _cast.serie != serie : !serie.equals( _cast.serie )) {
			return false;
		}
		
		if (serieModified != _cast.serieModified) {
			return false;
		}
		
		if (folio == null ? _cast.folio != folio : !folio.equals( _cast.folio )) {
			return false;
		}
		
		if (folioModified != _cast.folioModified) {
			return false;
		}
		
		if (uuid == null ? _cast.uuid != uuid : !uuid.equals( _cast.uuid )) {
			return false;
		}
		
		if (uuidModified != _cast.uuidModified) {
			return false;
		}
		
		if (selloemisor == null ? _cast.selloemisor != selloemisor : !selloemisor.equals( _cast.selloemisor )) {
			return false;
		}
		
		if (selloemisorModified != _cast.selloemisorModified) {
			return false;
		}
		
		if (fechahorasellado == null ? _cast.fechahorasellado != fechahorasellado : !fechahorasellado.equals( _cast.fechahorasellado )) {
			return false;
		}
		
		if (fechahoraselladoModified != _cast.fechahoraselladoModified) {
			return false;
		}
		
		if (fechahoratimbrado == null ? _cast.fechahoratimbrado != fechahoratimbrado : !fechahoratimbrado.equals( _cast.fechahoratimbrado )) {
			return false;
		}
		
		if (fechahoratimbradoModified != _cast.fechahoratimbradoModified) {
			return false;
		}
		
		if (fechahoraproceso == null ? _cast.fechahoraproceso != fechahoraproceso : !fechahoraproceso.equals( _cast.fechahoraproceso )) {
			return false;
		}
		
		if (fechahoraprocesoModified != _cast.fechahoraprocesoModified) {
			return false;
		}
		
		if (rfcreceptor == null ? _cast.rfcreceptor != rfcreceptor : !rfcreceptor.equals( _cast.rfcreceptor )) {
			return false;
		}
		
		if (rfcreceptorModified != _cast.rfcreceptorModified) {
			return false;
		}
		
		if (subtotal != _cast.subtotal) {
			return false;
		}
		
		if (subtotalNull != _cast.subtotalNull) {
			return false;
		}
		
		if (subtotalModified != _cast.subtotalModified) {
			return false;
		}
		
		if (total != _cast.total) {
			return false;
		}
		
		if (totalNull != _cast.totalNull) {
			return false;
		}
		
		if (totalModified != _cast.totalModified) {
			return false;
		}
		
		if (cadenaoriginal == null ? _cast.cadenaoriginal != cadenaoriginal : !cadenaoriginal.equals( _cast.cadenaoriginal )) {
			return false;
		}
		
		if (cadenaoriginalModified != _cast.cadenaoriginalModified) {
			return false;
		}
		
		if (fechacancelacion == null ? _cast.fechacancelacion != fechacancelacion : !fechacancelacion.equals( _cast.fechacancelacion )) {
			return false;
		}
		
		if (fechacancelacionModified != _cast.fechacancelacionModified) {
			return false;
		}
		
		if (moneda == null ? _cast.moneda != moneda : !moneda.equals( _cast.moneda )) {
			return false;
		}
		
		if (monedaModified != _cast.monedaModified) {
			return false;
		}
		
		if (observaciones == null ? _cast.observaciones != observaciones : !observaciones.equals( _cast.observaciones )) {
			return false;
		}
		
		if (observacionesModified != _cast.observacionesModified) {
			return false;
		}
		
		if (nombreReceptor == null ? _cast.nombreReceptor != nombreReceptor : !nombreReceptor.equals( _cast.nombreReceptor )) {
			return false;
		}
		
		if (nombreReceptorModified != _cast.nombreReceptorModified) {
			return false;
		}
		
		if (email == null ? _cast.email != email : !email.equals( _cast.email )) {
			return false;
		}
		
		if (emailModified != _cast.emailModified) {
			return false;
		}
		
		if (referencia1 == null ? _cast.referencia1 != referencia1 : !referencia1.equals( _cast.referencia1 )) {
			return false;
		}
		
		if (referencia1Modified != _cast.referencia1Modified) {
			return false;
		}
		
		if (referencia2 == null ? _cast.referencia2 != referencia2 : !referencia2.equals( _cast.referencia2 )) {
			return false;
		}
		
		if (referencia2Modified != _cast.referencia2Modified) {
			return false;
		}
		
		if (referencia3 == null ? _cast.referencia3 != referencia3 : !referencia3.equals( _cast.referencia3 )) {
			return false;
		}
		
		if (referencia3Modified != _cast.referencia3Modified) {
			return false;
		}
		
		return true;
	}

	/**
	 * Method 'hashCode'
	 * 
	 * @return int
	 */
	public int hashCode()
	{
		int _hashCode = 0;
		_hashCode = 29 * _hashCode + idcomprobantefiscal;
		_hashCode = 29 * _hashCode + (idcomprobantefiscalModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idemisor;
		_hashCode = 29 * _hashCode + (idemisorModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idarchivomaestro;
		_hashCode = 29 * _hashCode + (idarchivomaestroNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (idarchivomaestroModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idestatus;
		_hashCode = 29 * _hashCode + (idestatusModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idtipocomprobante;
		_hashCode = 29 * _hashCode + (idtipocomprobanteModified ? 1 : 0);
		if (nombrearchivoxml != null) {
			_hashCode = 29 * _hashCode + nombrearchivoxml.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (nombrearchivoxmlModified ? 1 : 0);
		if (nombrearchivopdf != null) {
			_hashCode = 29 * _hashCode + nombrearchivopdf.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (nombrearchivopdfModified ? 1 : 0);
		if (serie != null) {
			_hashCode = 29 * _hashCode + serie.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (serieModified ? 1 : 0);
		if (folio != null) {
			_hashCode = 29 * _hashCode + folio.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (folioModified ? 1 : 0);
		if (uuid != null) {
			_hashCode = 29 * _hashCode + uuid.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (uuidModified ? 1 : 0);
		if (selloemisor != null) {
			_hashCode = 29 * _hashCode + selloemisor.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (selloemisorModified ? 1 : 0);
		if (fechahorasellado != null) {
			_hashCode = 29 * _hashCode + fechahorasellado.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechahoraselladoModified ? 1 : 0);
		if (fechahoratimbrado != null) {
			_hashCode = 29 * _hashCode + fechahoratimbrado.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechahoratimbradoModified ? 1 : 0);
		if (fechahoraproceso != null) {
			_hashCode = 29 * _hashCode + fechahoraproceso.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechahoraprocesoModified ? 1 : 0);
		if (rfcreceptor != null) {
			_hashCode = 29 * _hashCode + rfcreceptor.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (rfcreceptorModified ? 1 : 0);
		long temp_subtotal = Double.doubleToLongBits(subtotal);
		_hashCode = 29 * _hashCode + (int) (temp_subtotal ^ (temp_subtotal >>> 32));
		_hashCode = 29 * _hashCode + (subtotalNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (subtotalModified ? 1 : 0);
		long temp_total = Double.doubleToLongBits(total);
		_hashCode = 29 * _hashCode + (int) (temp_total ^ (temp_total >>> 32));
		_hashCode = 29 * _hashCode + (totalNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (totalModified ? 1 : 0);
		if (cadenaoriginal != null) {
			_hashCode = 29 * _hashCode + cadenaoriginal.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (cadenaoriginalModified ? 1 : 0);
		if (fechacancelacion != null) {
			_hashCode = 29 * _hashCode + fechacancelacion.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechacancelacionModified ? 1 : 0);
		if (moneda != null) {
			_hashCode = 29 * _hashCode + moneda.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (monedaModified ? 1 : 0);
		if (observaciones != null) {
			_hashCode = 29 * _hashCode + observaciones.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (observacionesModified ? 1 : 0);
		if (nombreReceptor != null) {
			_hashCode = 29 * _hashCode + nombreReceptor.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (nombreReceptorModified ? 1 : 0);
		if (email != null) {
			_hashCode = 29 * _hashCode + email.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (emailModified ? 1 : 0);
		if (referencia1 != null) {
			_hashCode = 29 * _hashCode + referencia1.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (referencia1Modified ? 1 : 0);
		if (referencia2 != null) {
			_hashCode = 29 * _hashCode + referencia2.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (referencia2Modified ? 1 : 0);
		if (referencia3 != null) {
			_hashCode = 29 * _hashCode + referencia3.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (referencia3Modified ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return ComprobantefiscalPk
	 */
	public ComprobantefiscalPk createPk()
	{
		return new ComprobantefiscalPk(idcomprobantefiscal);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.fens.desktopMonitor.dto.Comprobantefiscal: " );
		ret.append( "idcomprobantefiscal=" + idcomprobantefiscal );
		ret.append( ", idemisor=" + idemisor );
		ret.append( ", idarchivomaestro=" + idarchivomaestro );
		ret.append( ", idestatus=" + idestatus );
		ret.append( ", idtipocomprobante=" + idtipocomprobante );
		ret.append( ", nombrearchivoxml=" + nombrearchivoxml );
		ret.append( ", nombrearchivopdf=" + nombrearchivopdf );
		ret.append( ", serie=" + serie );
		ret.append( ", folio=" + folio );
		ret.append( ", uuid=" + uuid );
		ret.append( ", selloemisor=" + selloemisor );
		ret.append( ", fechahorasellado=" + fechahorasellado );
		ret.append( ", fechahoratimbrado=" + fechahoratimbrado );
		ret.append( ", fechahoraproceso=" + fechahoraproceso );
		ret.append( ", rfcreceptor=" + rfcreceptor );
		ret.append( ", subtotal=" + subtotal );
		ret.append( ", total=" + total );
		ret.append( ", cadenaoriginal=" + cadenaoriginal );
		ret.append( ", fechacancelacion=" + fechacancelacion );
		ret.append( ", moneda=" + moneda );
		ret.append( ", observaciones=" + observaciones );
		ret.append( ", nombreReceptor=" + nombreReceptor );
		ret.append( ", email=" + email );
		ret.append( ", referencia1=" + referencia1 );
		ret.append( ", referencia2=" + referencia2 );
		ret.append( ", referencia3=" + referencia3 );
		return ret.toString();
	}

}