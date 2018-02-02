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

public class Archivomaestro implements Serializable
{
	/** 
	 * This attribute maps to the column IDARCHIVOMAESTRO in the ARCHIVOMAESTRO table.
	 */
	protected int idarchivomaestro;

	/** 
	 * This attribute represents whether the attribute idarchivomaestro has been modified since being read from the database.
	 */
	protected boolean idarchivomaestroModified = false;

	/** 
	 * This attribute maps to the column NOMBREARCHIVO in the ARCHIVOMAESTRO table.
	 */
	protected String nombrearchivo;

	/** 
	 * This attribute represents whether the attribute nombrearchivo has been modified since being read from the database.
	 */
	protected boolean nombrearchivoModified = false;

	/** 
	 * This attribute maps to the column IDESTATUS in the ARCHIVOMAESTRO table.
	 */
	protected int idestatus;

	/** 
	 * This attribute represents whether the attribute idestatus has been modified since being read from the database.
	 */
	protected boolean idestatusModified = false;

	/** 
	 * This attribute maps to the column FECHACREACION in the ARCHIVOMAESTRO table.
	 */
	protected Date fechacreacion;

	/** 
	 * This attribute represents whether the attribute fechacreacion has been modified since being read from the database.
	 */
	protected boolean fechacreacionModified = false;

	/** 
	 * This attribute maps to the column FECHAPROCESO in the ARCHIVOMAESTRO table.
	 */
	protected Date fechaproceso;

	/** 
	 * This attribute represents whether the attribute fechaproceso has been modified since being read from the database.
	 */
	protected boolean fechaprocesoModified = false;

	/** 
	 * This attribute maps to the column NUMEROFACTURAS in the ARCHIVOMAESTRO table.
	 */
	protected int numerofacturas;

	/** 
	 * This attribute represents whether the primitive attribute numerofacturas is null.
	 */
	protected boolean numerofacturasNull = true;

	/** 
	 * This attribute represents whether the attribute numerofacturas has been modified since being read from the database.
	 */
	protected boolean numerofacturasModified = false;

	/** 
	 * This attribute maps to the column TAMANOARCHIVO in the ARCHIVOMAESTRO table.
	 */
	protected long tamanoarchivo;

	/** 
	 * This attribute represents whether the primitive attribute tamanoarchivo is null.
	 */
	protected boolean tamanoarchivoNull = true;

	/** 
	 * This attribute represents whether the attribute tamanoarchivo has been modified since being read from the database.
	 */
	protected boolean tamanoarchivoModified = false;

	/** 
	 * This attribute maps to the column MD5_CHECKSUM in the ARCHIVOMAESTRO table.
	 */
	protected String md5Checksum;

	/** 
	 * This attribute represents whether the attribute md5Checksum has been modified since being read from the database.
	 */
	protected boolean md5ChecksumModified = false;

	/** 
	 * This attribute maps to the column OBSERVACIONES in the ARCHIVOMAESTRO table.
	 */
	protected String observaciones;

	/** 
	 * This attribute represents whether the attribute observaciones has been modified since being read from the database.
	 */
	protected boolean observacionesModified = false;

	/**
	 * Method 'Archivomaestro'
	 * 
	 */
	public Archivomaestro()
	{
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
		this.idarchivomaestroModified = true;
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
	 * Method 'getNombrearchivo'
	 * 
	 * @return String
	 */
	public String getNombrearchivo()
	{
		return nombrearchivo;
	}

	/**
	 * Method 'setNombrearchivo'
	 * 
	 * @param nombrearchivo
	 */
	public void setNombrearchivo(String nombrearchivo)
	{
		this.nombrearchivo = nombrearchivo;
		this.nombrearchivoModified = true;
	}

	/** 
	 * Sets the value of nombrearchivoModified
	 */
	public void setNombrearchivoModified(boolean nombrearchivoModified)
	{
		this.nombrearchivoModified = nombrearchivoModified;
	}

	/** 
	 * Gets the value of nombrearchivoModified
	 */
	public boolean isNombrearchivoModified()
	{
		return nombrearchivoModified;
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
	 * Method 'getFechacreacion'
	 * 
	 * @return Date
	 */
	public Date getFechacreacion()
	{
		return fechacreacion;
	}

	/**
	 * Method 'setFechacreacion'
	 * 
	 * @param fechacreacion
	 */
	public void setFechacreacion(Date fechacreacion)
	{
		this.fechacreacion = fechacreacion;
		this.fechacreacionModified = true;
	}

	/** 
	 * Sets the value of fechacreacionModified
	 */
	public void setFechacreacionModified(boolean fechacreacionModified)
	{
		this.fechacreacionModified = fechacreacionModified;
	}

	/** 
	 * Gets the value of fechacreacionModified
	 */
	public boolean isFechacreacionModified()
	{
		return fechacreacionModified;
	}

	/**
	 * Method 'getFechaproceso'
	 * 
	 * @return Date
	 */
	public Date getFechaproceso()
	{
		return fechaproceso;
	}

	/**
	 * Method 'setFechaproceso'
	 * 
	 * @param fechaproceso
	 */
	public void setFechaproceso(Date fechaproceso)
	{
		this.fechaproceso = fechaproceso;
		this.fechaprocesoModified = true;
	}

	/** 
	 * Sets the value of fechaprocesoModified
	 */
	public void setFechaprocesoModified(boolean fechaprocesoModified)
	{
		this.fechaprocesoModified = fechaprocesoModified;
	}

	/** 
	 * Gets the value of fechaprocesoModified
	 */
	public boolean isFechaprocesoModified()
	{
		return fechaprocesoModified;
	}

	/**
	 * Method 'getNumerofacturas'
	 * 
	 * @return int
	 */
	public int getNumerofacturas()
	{
		return numerofacturas;
	}

	/**
	 * Method 'setNumerofacturas'
	 * 
	 * @param numerofacturas
	 */
	public void setNumerofacturas(int numerofacturas)
	{
		this.numerofacturas = numerofacturas;
		this.numerofacturasNull = false;
		this.numerofacturasModified = true;
	}

	/**
	 * Method 'setNumerofacturasNull'
	 * 
	 * @param value
	 */
	public void setNumerofacturasNull(boolean value)
	{
		this.numerofacturasNull = value;
		this.numerofacturasModified = true;
	}

	/**
	 * Method 'isNumerofacturasNull'
	 * 
	 * @return boolean
	 */
	public boolean isNumerofacturasNull()
	{
		return numerofacturasNull;
	}

	/** 
	 * Sets the value of numerofacturasModified
	 */
	public void setNumerofacturasModified(boolean numerofacturasModified)
	{
		this.numerofacturasModified = numerofacturasModified;
	}

	/** 
	 * Gets the value of numerofacturasModified
	 */
	public boolean isNumerofacturasModified()
	{
		return numerofacturasModified;
	}

	/**
	 * Method 'getTamanoarchivo'
	 * 
	 * @return long
	 */
	public long getTamanoarchivo()
	{
		return tamanoarchivo;
	}

	/**
	 * Method 'setTamanoarchivo'
	 * 
	 * @param tamanoarchivo
	 */
	public void setTamanoarchivo(long tamanoarchivo)
	{
		this.tamanoarchivo = tamanoarchivo;
		this.tamanoarchivoNull = false;
		this.tamanoarchivoModified = true;
	}

	/**
	 * Method 'setTamanoarchivoNull'
	 * 
	 * @param value
	 */
	public void setTamanoarchivoNull(boolean value)
	{
		this.tamanoarchivoNull = value;
		this.tamanoarchivoModified = true;
	}

	/**
	 * Method 'isTamanoarchivoNull'
	 * 
	 * @return boolean
	 */
	public boolean isTamanoarchivoNull()
	{
		return tamanoarchivoNull;
	}

	/** 
	 * Sets the value of tamanoarchivoModified
	 */
	public void setTamanoarchivoModified(boolean tamanoarchivoModified)
	{
		this.tamanoarchivoModified = tamanoarchivoModified;
	}

	/** 
	 * Gets the value of tamanoarchivoModified
	 */
	public boolean isTamanoarchivoModified()
	{
		return tamanoarchivoModified;
	}

	/**
	 * Method 'getMd5Checksum'
	 * 
	 * @return String
	 */
	public String getMd5Checksum()
	{
		return md5Checksum;
	}

	/**
	 * Method 'setMd5Checksum'
	 * 
	 * @param md5Checksum
	 */
	public void setMd5Checksum(String md5Checksum)
	{
		this.md5Checksum = md5Checksum;
		this.md5ChecksumModified = true;
	}

	/** 
	 * Sets the value of md5ChecksumModified
	 */
	public void setMd5ChecksumModified(boolean md5ChecksumModified)
	{
		this.md5ChecksumModified = md5ChecksumModified;
	}

	/** 
	 * Gets the value of md5ChecksumModified
	 */
	public boolean isMd5ChecksumModified()
	{
		return md5ChecksumModified;
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
		
		if (!(_other instanceof Archivomaestro)) {
			return false;
		}
		
		final Archivomaestro _cast = (Archivomaestro) _other;
		if (idarchivomaestro != _cast.idarchivomaestro) {
			return false;
		}
		
		if (idarchivomaestroModified != _cast.idarchivomaestroModified) {
			return false;
		}
		
		if (nombrearchivo == null ? _cast.nombrearchivo != nombrearchivo : !nombrearchivo.equals( _cast.nombrearchivo )) {
			return false;
		}
		
		if (nombrearchivoModified != _cast.nombrearchivoModified) {
			return false;
		}
		
		if (idestatus != _cast.idestatus) {
			return false;
		}
		
		if (idestatusModified != _cast.idestatusModified) {
			return false;
		}
		
		if (fechacreacion == null ? _cast.fechacreacion != fechacreacion : !fechacreacion.equals( _cast.fechacreacion )) {
			return false;
		}
		
		if (fechacreacionModified != _cast.fechacreacionModified) {
			return false;
		}
		
		if (fechaproceso == null ? _cast.fechaproceso != fechaproceso : !fechaproceso.equals( _cast.fechaproceso )) {
			return false;
		}
		
		if (fechaprocesoModified != _cast.fechaprocesoModified) {
			return false;
		}
		
		if (numerofacturas != _cast.numerofacturas) {
			return false;
		}
		
		if (numerofacturasNull != _cast.numerofacturasNull) {
			return false;
		}
		
		if (numerofacturasModified != _cast.numerofacturasModified) {
			return false;
		}
		
		if (tamanoarchivo != _cast.tamanoarchivo) {
			return false;
		}
		
		if (tamanoarchivoNull != _cast.tamanoarchivoNull) {
			return false;
		}
		
		if (tamanoarchivoModified != _cast.tamanoarchivoModified) {
			return false;
		}
		
		if (md5Checksum == null ? _cast.md5Checksum != md5Checksum : !md5Checksum.equals( _cast.md5Checksum )) {
			return false;
		}
		
		if (md5ChecksumModified != _cast.md5ChecksumModified) {
			return false;
		}
		
		if (observaciones == null ? _cast.observaciones != observaciones : !observaciones.equals( _cast.observaciones )) {
			return false;
		}
		
		if (observacionesModified != _cast.observacionesModified) {
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
		_hashCode = 29 * _hashCode + idarchivomaestro;
		_hashCode = 29 * _hashCode + (idarchivomaestroModified ? 1 : 0);
		if (nombrearchivo != null) {
			_hashCode = 29 * _hashCode + nombrearchivo.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (nombrearchivoModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idestatus;
		_hashCode = 29 * _hashCode + (idestatusModified ? 1 : 0);
		if (fechacreacion != null) {
			_hashCode = 29 * _hashCode + fechacreacion.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechacreacionModified ? 1 : 0);
		if (fechaproceso != null) {
			_hashCode = 29 * _hashCode + fechaproceso.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechaprocesoModified ? 1 : 0);
		_hashCode = 29 * _hashCode + numerofacturas;
		_hashCode = 29 * _hashCode + (numerofacturasNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (numerofacturasModified ? 1 : 0);
		_hashCode = 29 * _hashCode + (int) (tamanoarchivo ^ (tamanoarchivo >>> 32));
		_hashCode = 29 * _hashCode + (tamanoarchivoNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (tamanoarchivoModified ? 1 : 0);
		if (md5Checksum != null) {
			_hashCode = 29 * _hashCode + md5Checksum.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (md5ChecksumModified ? 1 : 0);
		if (observaciones != null) {
			_hashCode = 29 * _hashCode + observaciones.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (observacionesModified ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return ArchivomaestroPk
	 */
	public ArchivomaestroPk createPk()
	{
		return new ArchivomaestroPk(idarchivomaestro);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.fens.desktopMonitor.dto.Archivomaestro: " );
		ret.append( "idarchivomaestro=" + idarchivomaestro );
		ret.append( ", nombrearchivo=" + nombrearchivo );
		ret.append( ", idestatus=" + idestatus );
		ret.append( ", fechacreacion=" + fechacreacion );
		ret.append( ", fechaproceso=" + fechaproceso );
		ret.append( ", numerofacturas=" + numerofacturas );
		ret.append( ", tamanoarchivo=" + tamanoarchivo );
		ret.append( ", md5Checksum=" + md5Checksum );
		ret.append( ", observaciones=" + observaciones );
		return ret.toString();
	}

}