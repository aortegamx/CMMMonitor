/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.fens.desktopMonitor.dao;

import com.fens.desktopMonitor.dto.*;
import com.fens.desktopMonitor.exceptions.*;

public interface SerieDao
{
	/** 
	 * Inserts a new row in the SERIE table.
	 */
	public SeriePk insert(Serie dto) throws SerieDaoException;

	/** 
	 * Updates a single row in the SERIE table.
	 */
	public void update(SeriePk pk, Serie dto) throws SerieDaoException;

	/** 
	 * Deletes a single row in the SERIE table.
	 */
	public void delete(SeriePk pk) throws SerieDaoException;

	/** 
	 * Returns the rows from the SERIE table that matches the specified primary-key value.
	 */
	public Serie findByPrimaryKey(SeriePk pk) throws SerieDaoException;

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'ID_SERIE = :idSerie'.
	 */
	public Serie findByPrimaryKey(int idSerie) throws SerieDaoException;

	/** 
	 * Returns all rows from the SERIE table that match the criteria ''.
	 */
	public Serie[] findAll() throws SerieDaoException;

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'ID_SERIE = :idSerie'.
	 */
	public Serie[] findWhereIdSerieEquals(int idSerie) throws SerieDaoException;

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'IDEMISOR = :idemisor'.
	 */
	public Serie[] findWhereIdemisorEquals(int idemisor) throws SerieDaoException;

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'IDTIPOCOMPROBANTE = :idtipocomprobante'.
	 */
	public Serie[] findWhereIdtipocomprobanteEquals(int idtipocomprobante) throws SerieDaoException;

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'NOMBRE_SERIE = :nombreSerie'.
	 */
	public Serie[] findWhereNombreSerieEquals(String nombreSerie) throws SerieDaoException;

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'RANGO_INI = :rangoIni'.
	 */
	public Serie[] findWhereRangoIniEquals(int rangoIni) throws SerieDaoException;

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'RANGO_FIN = :rangoFin'.
	 */
	public Serie[] findWhereRangoFinEquals(int rangoFin) throws SerieDaoException;

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'ULTIMO_FOLIO = :ultimoFolio'.
	 */
	public Serie[] findWhereUltimoFolioEquals(int ultimoFolio) throws SerieDaoException;

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'ID_ESTATUS_SERIE = :idEstatusSerie'.
	 */
	public Serie[] findWhereIdEstatusSerieEquals(int idEstatusSerie) throws SerieDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the SERIE table that match the specified arbitrary SQL statement
	 */
	public Serie[] findByDynamicSelect(String sql, Object[] sqlParams) throws SerieDaoException;

	/** 
	 * Returns all rows from the SERIE table that match the specified arbitrary SQL statement
	 */
	public Serie[] findByDynamicWhere(String sql, Object[] sqlParams) throws SerieDaoException;

}
