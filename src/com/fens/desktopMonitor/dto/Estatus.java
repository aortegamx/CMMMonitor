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

public class Estatus implements Serializable
{
	/** 
	 * This attribute maps to the column IDESTATUS in the ESTATUS table.
	 */
	protected int idestatus;

	/** 
	 * This attribute represents whether the attribute idestatus has been modified since being read from the database.
	 */
	protected boolean idestatusModified = false;

	/** 
	 * This attribute maps to the column NOMBREESTATUS in the ESTATUS table.
	 */
	protected String nombreestatus;

	/** 
	 * This attribute represents whether the attribute nombreestatus has been modified since being read from the database.
	 */
	protected boolean nombreestatusModified = false;

	/** 
	 * This attribute maps to the column DESCRIPCION in the ESTATUS table.
	 */
	protected String descripcion;

	/** 
	 * This attribute represents whether the attribute descripcion has been modified since being read from the database.
	 */
	protected boolean descripcionModified = false;

	/** 
	 * This attribute maps to the column TIPOESTATUS in the ESTATUS table.
	 */
	protected int tipoestatus;

	/** 
	 * This attribute represents whether the attribute tipoestatus has been modified since being read from the database.
	 */
	protected boolean tipoestatusModified = false;

	/**
	 * Method 'Estatus'
	 * 
	 */
	public Estatus()
	{
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
	 * Method 'getNombreestatus'
	 * 
	 * @return String
	 */
	public String getNombreestatus()
	{
		return nombreestatus;
	}

	/**
	 * Method 'setNombreestatus'
	 * 
	 * @param nombreestatus
	 */
	public void setNombreestatus(String nombreestatus)
	{
		this.nombreestatus = nombreestatus;
		this.nombreestatusModified = true;
	}

	/** 
	 * Sets the value of nombreestatusModified
	 */
	public void setNombreestatusModified(boolean nombreestatusModified)
	{
		this.nombreestatusModified = nombreestatusModified;
	}

	/** 
	 * Gets the value of nombreestatusModified
	 */
	public boolean isNombreestatusModified()
	{
		return nombreestatusModified;
	}

	/**
	 * Method 'getDescripcion'
	 * 
	 * @return String
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * Method 'setDescripcion'
	 * 
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
		this.descripcionModified = true;
	}

	/** 
	 * Sets the value of descripcionModified
	 */
	public void setDescripcionModified(boolean descripcionModified)
	{
		this.descripcionModified = descripcionModified;
	}

	/** 
	 * Gets the value of descripcionModified
	 */
	public boolean isDescripcionModified()
	{
		return descripcionModified;
	}

	/**
	 * Method 'getTipoestatus'
	 * 
	 * @return int
	 */
	public int getTipoestatus()
	{
		return tipoestatus;
	}

	/**
	 * Method 'setTipoestatus'
	 * 
	 * @param tipoestatus
	 */
	public void setTipoestatus(int tipoestatus)
	{
		this.tipoestatus = tipoestatus;
		this.tipoestatusModified = true;
	}

	/** 
	 * Sets the value of tipoestatusModified
	 */
	public void setTipoestatusModified(boolean tipoestatusModified)
	{
		this.tipoestatusModified = tipoestatusModified;
	}

	/** 
	 * Gets the value of tipoestatusModified
	 */
	public boolean isTipoestatusModified()
	{
		return tipoestatusModified;
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
		
		if (!(_other instanceof Estatus)) {
			return false;
		}
		
		final Estatus _cast = (Estatus) _other;
		if (idestatus != _cast.idestatus) {
			return false;
		}
		
		if (idestatusModified != _cast.idestatusModified) {
			return false;
		}
		
		if (nombreestatus == null ? _cast.nombreestatus != nombreestatus : !nombreestatus.equals( _cast.nombreestatus )) {
			return false;
		}
		
		if (nombreestatusModified != _cast.nombreestatusModified) {
			return false;
		}
		
		if (descripcion == null ? _cast.descripcion != descripcion : !descripcion.equals( _cast.descripcion )) {
			return false;
		}
		
		if (descripcionModified != _cast.descripcionModified) {
			return false;
		}
		
		if (tipoestatus != _cast.tipoestatus) {
			return false;
		}
		
		if (tipoestatusModified != _cast.tipoestatusModified) {
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
		_hashCode = 29 * _hashCode + idestatus;
		_hashCode = 29 * _hashCode + (idestatusModified ? 1 : 0);
		if (nombreestatus != null) {
			_hashCode = 29 * _hashCode + nombreestatus.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (nombreestatusModified ? 1 : 0);
		if (descripcion != null) {
			_hashCode = 29 * _hashCode + descripcion.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (descripcionModified ? 1 : 0);
		_hashCode = 29 * _hashCode + tipoestatus;
		_hashCode = 29 * _hashCode + (tipoestatusModified ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return EstatusPk
	 */
	public EstatusPk createPk()
	{
		return new EstatusPk(idestatus);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.fens.desktopMonitor.dto.Estatus: " );
		ret.append( "idestatus=" + idestatus );
		ret.append( ", nombreestatus=" + nombreestatus );
		ret.append( ", descripcion=" + descripcion );
		ret.append( ", tipoestatus=" + tipoestatus );
		return ret.toString();
	}

}
