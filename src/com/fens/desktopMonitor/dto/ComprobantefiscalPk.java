/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.fens.desktopMonitor.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** 
 * This class represents the primary key of the COMPROBANTEFISCAL table.
 */
public class ComprobantefiscalPk implements Serializable
{
	protected int idcomprobantefiscal;

	/** 
	 * This attribute represents whether the primitive attribute idcomprobantefiscal is null.
	 */
	protected boolean idcomprobantefiscalNull;

	/** 
	 * Sets the value of idcomprobantefiscal
	 */
	public void setIdcomprobantefiscal(int idcomprobantefiscal)
	{
		this.idcomprobantefiscal = idcomprobantefiscal;
	}

	/** 
	 * Gets the value of idcomprobantefiscal
	 */
	public int getIdcomprobantefiscal()
	{
		return idcomprobantefiscal;
	}

	/**
	 * Method 'ComprobantefiscalPk'
	 * 
	 */
	public ComprobantefiscalPk()
	{
	}

	/**
	 * Method 'ComprobantefiscalPk'
	 * 
	 * @param idcomprobantefiscal
	 */
	public ComprobantefiscalPk(final int idcomprobantefiscal)
	{
		this.idcomprobantefiscal = idcomprobantefiscal;
	}

	/** 
	 * Sets the value of idcomprobantefiscalNull
	 */
	public void setIdcomprobantefiscalNull(boolean idcomprobantefiscalNull)
	{
		this.idcomprobantefiscalNull = idcomprobantefiscalNull;
	}

	/** 
	 * Gets the value of idcomprobantefiscalNull
	 */
	public boolean isIdcomprobantefiscalNull()
	{
		return idcomprobantefiscalNull;
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
		
		if (!(_other instanceof ComprobantefiscalPk)) {
			return false;
		}
		
		final ComprobantefiscalPk _cast = (ComprobantefiscalPk) _other;
		if (idcomprobantefiscal != _cast.idcomprobantefiscal) {
			return false;
		}
		
		if (idcomprobantefiscalNull != _cast.idcomprobantefiscalNull) {
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
		_hashCode = 29 * _hashCode + (idcomprobantefiscalNull ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.fens.desktopMonitor.dto.ComprobantefiscalPk: " );
		ret.append( "idcomprobantefiscal=" + idcomprobantefiscal );
		return ret.toString();
	}

}