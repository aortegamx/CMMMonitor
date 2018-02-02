/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.fens.desktopMonitor.dao;

import java.util.Date;
import com.fens.desktopMonitor.dto.*;
import com.fens.desktopMonitor.exceptions.*;

public interface ComprobantefiscalDao
{
	/** 
	 * Inserts a new row in the COMPROBANTEFISCAL table.
	 */
	public ComprobantefiscalPk insert(Comprobantefiscal dto) throws ComprobantefiscalDaoException;

	/** 
	 * Updates a single row in the COMPROBANTEFISCAL table.
	 */
	public void update(ComprobantefiscalPk pk, Comprobantefiscal dto) throws ComprobantefiscalDaoException;

	/** 
	 * Deletes a single row in the COMPROBANTEFISCAL table.
	 */
	public void delete(ComprobantefiscalPk pk) throws ComprobantefiscalDaoException;

	/** 
	 * Returns the rows from the COMPROBANTEFISCAL table that matches the specified primary-key value.
	 */
	public Comprobantefiscal findByPrimaryKey(ComprobantefiscalPk pk) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'IDCOMPROBANTEFISCAL = :idcomprobantefiscal'.
	 */
	public Comprobantefiscal findByPrimaryKey(int idcomprobantefiscal) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria ''.
	 */
	public Comprobantefiscal[] findAll() throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'IDCOMPROBANTEFISCAL = :idcomprobantefiscal'.
	 */
	public Comprobantefiscal[] findWhereIdcomprobantefiscalEquals(int idcomprobantefiscal) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'IDEMISOR = :idemisor'.
	 */
	public Comprobantefiscal[] findWhereIdemisorEquals(int idemisor) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'IDARCHIVOMAESTRO = :idarchivomaestro'.
	 */
	public Comprobantefiscal[] findWhereIdarchivomaestroEquals(int idarchivomaestro) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'IDESTATUS = :idestatus'.
	 */
	public Comprobantefiscal[] findWhereIdestatusEquals(int idestatus) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'IDTIPOCOMPROBANTE = :idtipocomprobante'.
	 */
	public Comprobantefiscal[] findWhereIdtipocomprobanteEquals(int idtipocomprobante) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'NOMBREARCHIVOXML = :nombrearchivoxml'.
	 */
	public Comprobantefiscal[] findWhereNombrearchivoxmlEquals(String nombrearchivoxml) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'NOMBREARCHIVOPDF = :nombrearchivopdf'.
	 */
	public Comprobantefiscal[] findWhereNombrearchivopdfEquals(String nombrearchivopdf) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'SERIE = :serie'.
	 */
	public Comprobantefiscal[] findWhereSerieEquals(String serie) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'FOLIO = :folio'.
	 */
	public Comprobantefiscal[] findWhereFolioEquals(String folio) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'UUID = :uuid'.
	 */
	public Comprobantefiscal[] findWhereUuidEquals(String uuid) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'SELLOEMISOR = :selloemisor'.
	 */
	public Comprobantefiscal[] findWhereSelloemisorEquals(String selloemisor) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'FECHAHORASELLADO = :fechahorasellado'.
	 */
	public Comprobantefiscal[] findWhereFechahoraselladoEquals(Date fechahorasellado) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'FECHAHORATIMBRADO = :fechahoratimbrado'.
	 */
	public Comprobantefiscal[] findWhereFechahoratimbradoEquals(Date fechahoratimbrado) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'FECHAHORAPROCESO = :fechahoraproceso'.
	 */
	public Comprobantefiscal[] findWhereFechahoraprocesoEquals(Date fechahoraproceso) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'RFCRECEPTOR = :rfcreceptor'.
	 */
	public Comprobantefiscal[] findWhereRfcreceptorEquals(String rfcreceptor) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'SUBTOTAL = :subtotal'.
	 */
	public Comprobantefiscal[] findWhereSubtotalEquals(double subtotal) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'TOTAL = :total'.
	 */
	public Comprobantefiscal[] findWhereTotalEquals(double total) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'CADENAORIGINAL = :cadenaoriginal'.
	 */
	public Comprobantefiscal[] findWhereCadenaoriginalEquals(String cadenaoriginal) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'FECHACANCELACION = :fechacancelacion'.
	 */
	public Comprobantefiscal[] findWhereFechacancelacionEquals(Date fechacancelacion) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'MONEDA = :moneda'.
	 */
	public Comprobantefiscal[] findWhereMonedaEquals(String moneda) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'OBSERVACIONES = :observaciones'.
	 */
	public Comprobantefiscal[] findWhereObservacionesEquals(String observaciones) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'NOMBRE_RECEPTOR = :nombreReceptor'.
	 */
	public Comprobantefiscal[] findWhereNombreReceptorEquals(String nombreReceptor) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'EMAIL = :email'.
	 */
	public Comprobantefiscal[] findWhereEmailEquals(String email) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'REFERENCIA1 = :referencia1'.
	 */
	public Comprobantefiscal[] findWhereReferencia1Equals(String referencia1) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'REFERENCIA2 = :referencia2'.
	 */
	public Comprobantefiscal[] findWhereReferencia2Equals(String referencia2) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the criteria 'REFERENCIA3 = :referencia3'.
	 */
	public Comprobantefiscal[] findWhereReferencia3Equals(String referencia3) throws ComprobantefiscalDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the specified arbitrary SQL statement
	 */
	public Comprobantefiscal[] findByDynamicSelect(String sql, Object[] sqlParams) throws ComprobantefiscalDaoException;

	/** 
	 * Returns all rows from the COMPROBANTEFISCAL table that match the specified arbitrary SQL statement
	 */
	public Comprobantefiscal[] findByDynamicWhere(String sql, Object[] sqlParams) throws ComprobantefiscalDaoException;

}
