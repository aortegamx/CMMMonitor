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

public class Serie implements Serializable
{
	/** 
	 * This attribute maps to the column ID_SERIE in the SERIE table.
	 */
	protected int idSerie;

	/** 
	 * This attribute represents whether the attribute idSerie has been modified since being read from the database.
	 */
	protected boolean idSerieModified = false;

	/** 
	 * This attribute maps to the column IDEMISOR in the SERIE table.
	 */
	protected int idemisor;

	/** 
	 * This attribute represents whether the attribute idemisor has been modified since being read from the database.
	 */
	protected boolean idemisorModified = false;

	/** 
	 * This attribute maps to the column IDTIPOCOMPROBANTE in the SERIE table.
	 */
	protected int idtipocomprobante;

	/** 
	 * This attribute represents whether the attribute idtipocomprobante has been modified since being read from the database.
	 */
	protected boolean idtipocomprobanteModified = false;

	/** 
	 * This attribute maps to the column NOMBRE_SERIE in the SERIE table.
	 */
	protected String nombreSerie;

	/** 
	 * This attribute represents whether the attribute nombreSerie has been modified since being read from the database.
	 */
	protected boolean nombreSerieModified = false;

	/** 
	 * This attribute maps to the column RANGO_INI in the SERIE table.
	 */
	protected int rangoIni;

	/** 
	 * This attribute represents whether the primitive attribute rangoIni is null.
	 */
	protected boolean rangoIniNull = true;

	/** 
	 * This attribute represents whether the attribute rangoIni has been modified since being read from the database.
	 */
	protected boolean rangoIniModified = false;

	/** 
	 * This attribute maps to the column RANGO_FIN in the SERIE table.
	 */
	protected int rangoFin;

	/** 
	 * This attribute represents whether the primitive attribute rangoFin is null.
	 */
	protected boolean rangoFinNull = true;

	/** 
	 * This attribute represents whether the attribute rangoFin has been modified since being read from the database.
	 */
	protected boolean rangoFinModified = false;

	/** 
	 * This attribute maps to the column ULTIMO_FOLIO in the SERIE table.
	 */
	protected int ultimoFolio;

	/** 
	 * This attribute represents whether the primitive attribute ultimoFolio is null.
	 */
	protected boolean ultimoFolioNull = true;

	/** 
	 * This attribute represents whether the attribute ultimoFolio has been modified since being read from the database.
	 */
	protected boolean ultimoFolioModified = false;

	/** 
	 * This attribute maps to the column ID_ESTATUS_SERIE in the SERIE table.
	 */
	protected int idEstatusSerie;

	/** 
	 * This attribute represents whether the primitive attribute idEstatusSerie is null.
	 */
	protected boolean idEstatusSerieNull = true;

	/** 
	 * This attribute represents whether the attribute idEstatusSerie has been modified since being read from the database.
	 */
	protected boolean idEstatusSerieModified = false;

	/**
	 * Method 'Serie'
	 * 
	 */
	public Serie()
	{
	}

	/**
	 * Method 'getIdSerie'
	 * 
	 * @return int
	 */
	public int getIdSerie()
	{
		return idSerie;
	}

	/**
	 * Method 'setIdSerie'
	 * 
	 * @param idSerie
	 */
	public void setIdSerie(int idSerie)
	{
		this.idSerie = idSerie;
		this.idSerieModified = true;
	}

	/** 
	 * Sets the value of idSerieModified
	 */
	public void setIdSerieModified(boolean idSerieModified)
	{
		this.idSerieModified = idSerieModified;
	}

	/** 
	 * Gets the value of idSerieModified
	 */
	public boolean isIdSerieModified()
	{
		return idSerieModified;
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
	 * Method 'getNombreSerie'
	 * 
	 * @return String
	 */
	public String getNombreSerie()
	{
		return nombreSerie;
	}

	/**
	 * Method 'setNombreSerie'
	 * 
	 * @param nombreSerie
	 */
	public void setNombreSerie(String nombreSerie)
	{
		this.nombreSerie = nombreSerie;
		this.nombreSerieModified = true;
	}

	/** 
	 * Sets the value of nombreSerieModified
	 */
	public void setNombreSerieModified(boolean nombreSerieModified)
	{
		this.nombreSerieModified = nombreSerieModified;
	}

	/** 
	 * Gets the value of nombreSerieModified
	 */
	public boolean isNombreSerieModified()
	{
		return nombreSerieModified;
	}

	/**
	 * Method 'getRangoIni'
	 * 
	 * @return int
	 */
	public int getRangoIni()
	{
		return rangoIni;
	}

	/**
	 * Method 'setRangoIni'
	 * 
	 * @param rangoIni
	 */
	public void setRangoIni(int rangoIni)
	{
		this.rangoIni = rangoIni;
		this.rangoIniNull = false;
		this.rangoIniModified = true;
	}

	/**
	 * Method 'setRangoIniNull'
	 * 
	 * @param value
	 */
	public void setRangoIniNull(boolean value)
	{
		this.rangoIniNull = value;
		this.rangoIniModified = true;
	}

	/**
	 * Method 'isRangoIniNull'
	 * 
	 * @return boolean
	 */
	public boolean isRangoIniNull()
	{
		return rangoIniNull;
	}

	/** 
	 * Sets the value of rangoIniModified
	 */
	public void setRangoIniModified(boolean rangoIniModified)
	{
		this.rangoIniModified = rangoIniModified;
	}

	/** 
	 * Gets the value of rangoIniModified
	 */
	public boolean isRangoIniModified()
	{
		return rangoIniModified;
	}

	/**
	 * Method 'getRangoFin'
	 * 
	 * @return int
	 */
	public int getRangoFin()
	{
		return rangoFin;
	}

	/**
	 * Method 'setRangoFin'
	 * 
	 * @param rangoFin
	 */
	public void setRangoFin(int rangoFin)
	{
		this.rangoFin = rangoFin;
		this.rangoFinNull = false;
		this.rangoFinModified = true;
	}

	/**
	 * Method 'setRangoFinNull'
	 * 
	 * @param value
	 */
	public void setRangoFinNull(boolean value)
	{
		this.rangoFinNull = value;
		this.rangoFinModified = true;
	}

	/**
	 * Method 'isRangoFinNull'
	 * 
	 * @return boolean
	 */
	public boolean isRangoFinNull()
	{
		return rangoFinNull;
	}

	/** 
	 * Sets the value of rangoFinModified
	 */
	public void setRangoFinModified(boolean rangoFinModified)
	{
		this.rangoFinModified = rangoFinModified;
	}

	/** 
	 * Gets the value of rangoFinModified
	 */
	public boolean isRangoFinModified()
	{
		return rangoFinModified;
	}

	/**
	 * Method 'getUltimoFolio'
	 * 
	 * @return int
	 */
	public int getUltimoFolio()
	{
		return ultimoFolio;
	}

	/**
	 * Method 'setUltimoFolio'
	 * 
	 * @param ultimoFolio
	 */
	public void setUltimoFolio(int ultimoFolio)
	{
		this.ultimoFolio = ultimoFolio;
		this.ultimoFolioNull = false;
		this.ultimoFolioModified = true;
	}

	/**
	 * Method 'setUltimoFolioNull'
	 * 
	 * @param value
	 */
	public void setUltimoFolioNull(boolean value)
	{
		this.ultimoFolioNull = value;
		this.ultimoFolioModified = true;
	}

	/**
	 * Method 'isUltimoFolioNull'
	 * 
	 * @return boolean
	 */
	public boolean isUltimoFolioNull()
	{
		return ultimoFolioNull;
	}

	/** 
	 * Sets the value of ultimoFolioModified
	 */
	public void setUltimoFolioModified(boolean ultimoFolioModified)
	{
		this.ultimoFolioModified = ultimoFolioModified;
	}

	/** 
	 * Gets the value of ultimoFolioModified
	 */
	public boolean isUltimoFolioModified()
	{
		return ultimoFolioModified;
	}

	/**
	 * Method 'getIdEstatusSerie'
	 * 
	 * @return int
	 */
	public int getIdEstatusSerie()
	{
		return idEstatusSerie;
	}

	/**
	 * Method 'setIdEstatusSerie'
	 * 
	 * @param idEstatusSerie
	 */
	public void setIdEstatusSerie(int idEstatusSerie)
	{
		this.idEstatusSerie = idEstatusSerie;
		this.idEstatusSerieNull = false;
		this.idEstatusSerieModified = true;
	}

	/**
	 * Method 'setIdEstatusSerieNull'
	 * 
	 * @param value
	 */
	public void setIdEstatusSerieNull(boolean value)
	{
		this.idEstatusSerieNull = value;
		this.idEstatusSerieModified = true;
	}

	/**
	 * Method 'isIdEstatusSerieNull'
	 * 
	 * @return boolean
	 */
	public boolean isIdEstatusSerieNull()
	{
		return idEstatusSerieNull;
	}

	/** 
	 * Sets the value of idEstatusSerieModified
	 */
	public void setIdEstatusSerieModified(boolean idEstatusSerieModified)
	{
		this.idEstatusSerieModified = idEstatusSerieModified;
	}

	/** 
	 * Gets the value of idEstatusSerieModified
	 */
	public boolean isIdEstatusSerieModified()
	{
		return idEstatusSerieModified;
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
		
		if (!(_other instanceof Serie)) {
			return false;
		}
		
		final Serie _cast = (Serie) _other;
		if (idSerie != _cast.idSerie) {
			return false;
		}
		
		if (idSerieModified != _cast.idSerieModified) {
			return false;
		}
		
		if (idemisor != _cast.idemisor) {
			return false;
		}
		
		if (idemisorModified != _cast.idemisorModified) {
			return false;
		}
		
		if (idtipocomprobante != _cast.idtipocomprobante) {
			return false;
		}
		
		if (idtipocomprobanteModified != _cast.idtipocomprobanteModified) {
			return false;
		}
		
		if (nombreSerie == null ? _cast.nombreSerie != nombreSerie : !nombreSerie.equals( _cast.nombreSerie )) {
			return false;
		}
		
		if (nombreSerieModified != _cast.nombreSerieModified) {
			return false;
		}
		
		if (rangoIni != _cast.rangoIni) {
			return false;
		}
		
		if (rangoIniNull != _cast.rangoIniNull) {
			return false;
		}
		
		if (rangoIniModified != _cast.rangoIniModified) {
			return false;
		}
		
		if (rangoFin != _cast.rangoFin) {
			return false;
		}
		
		if (rangoFinNull != _cast.rangoFinNull) {
			return false;
		}
		
		if (rangoFinModified != _cast.rangoFinModified) {
			return false;
		}
		
		if (ultimoFolio != _cast.ultimoFolio) {
			return false;
		}
		
		if (ultimoFolioNull != _cast.ultimoFolioNull) {
			return false;
		}
		
		if (ultimoFolioModified != _cast.ultimoFolioModified) {
			return false;
		}
		
		if (idEstatusSerie != _cast.idEstatusSerie) {
			return false;
		}
		
		if (idEstatusSerieNull != _cast.idEstatusSerieNull) {
			return false;
		}
		
		if (idEstatusSerieModified != _cast.idEstatusSerieModified) {
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
		_hashCode = 29 * _hashCode + idSerie;
		_hashCode = 29 * _hashCode + (idSerieModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idemisor;
		_hashCode = 29 * _hashCode + (idemisorModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idtipocomprobante;
		_hashCode = 29 * _hashCode + (idtipocomprobanteModified ? 1 : 0);
		if (nombreSerie != null) {
			_hashCode = 29 * _hashCode + nombreSerie.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (nombreSerieModified ? 1 : 0);
		_hashCode = 29 * _hashCode + rangoIni;
		_hashCode = 29 * _hashCode + (rangoIniNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (rangoIniModified ? 1 : 0);
		_hashCode = 29 * _hashCode + rangoFin;
		_hashCode = 29 * _hashCode + (rangoFinNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (rangoFinModified ? 1 : 0);
		_hashCode = 29 * _hashCode + ultimoFolio;
		_hashCode = 29 * _hashCode + (ultimoFolioNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (ultimoFolioModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idEstatusSerie;
		_hashCode = 29 * _hashCode + (idEstatusSerieNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (idEstatusSerieModified ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return SeriePk
	 */
	public SeriePk createPk()
	{
		return new SeriePk(idSerie);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.fens.desktopMonitor.dto.Serie: " );
		ret.append( "idSerie=" + idSerie );
		ret.append( ", idemisor=" + idemisor );
		ret.append( ", idtipocomprobante=" + idtipocomprobante );
		ret.append( ", nombreSerie=" + nombreSerie );
		ret.append( ", rangoIni=" + rangoIni );
		ret.append( ", rangoFin=" + rangoFin );
		ret.append( ", ultimoFolio=" + ultimoFolio );
		ret.append( ", idEstatusSerie=" + idEstatusSerie );
		return ret.toString();
	}

}