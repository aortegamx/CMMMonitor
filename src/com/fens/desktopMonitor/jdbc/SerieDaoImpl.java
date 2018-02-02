/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.fens.desktopMonitor.jdbc;

import com.fens.desktopMonitor.dao.*;
import com.fens.desktopMonitor.factory.*;
import com.fens.desktopMonitor.dto.*;
import com.fens.desktopMonitor.exceptions.*;
import java.sql.Connection;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public class SerieDaoImpl extends AbstractDAO implements SerieDao
{
	/** 
	 * The factory class for this DAO has two versions of the create() method - one that
takes no arguments and one that takes a Connection argument. If the Connection version
is chosen then the connection will be stored in this attribute and will be used by all
calls to this DAO, otherwise a new Connection will be allocated for each operation.
	 */
	protected java.sql.Connection userConn;

	/** 
	 * All finder methods in this class use this SELECT constant to build their queries
	 */
	protected final String SQL_SELECT = "SELECT ID_SERIE, IDEMISOR, IDTIPOCOMPROBANTE, NOMBRE_SERIE, RANGO_INI, RANGO_FIN, ULTIMO_FOLIO, ID_ESTATUS_SERIE FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( IDEMISOR, IDTIPOCOMPROBANTE, NOMBRE_SERIE, RANGO_INI, RANGO_FIN, ULTIMO_FOLIO, ID_ESTATUS_SERIE ) VALUES ( ?, ?, ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET IDEMISOR = ?, IDTIPOCOMPROBANTE = ?, NOMBRE_SERIE = ?, RANGO_INI = ?, RANGO_FIN = ?, ULTIMO_FOLIO = ?, ID_ESTATUS_SERIE = ? WHERE ID_SERIE = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE ID_SERIE = ?";

	/** 
	 * Index of column ID_SERIE
	 */
	protected static final int COLUMN_ID_SERIE = 1;

	/** 
	 * Index of column IDEMISOR
	 */
	protected static final int COLUMN_IDEMISOR = 2;

	/** 
	 * Index of column IDTIPOCOMPROBANTE
	 */
	protected static final int COLUMN_IDTIPOCOMPROBANTE = 3;

	/** 
	 * Index of column NOMBRE_SERIE
	 */
	protected static final int COLUMN_NOMBRE_SERIE = 4;

	/** 
	 * Index of column RANGO_INI
	 */
	protected static final int COLUMN_RANGO_INI = 5;

	/** 
	 * Index of column RANGO_FIN
	 */
	protected static final int COLUMN_RANGO_FIN = 6;

	/** 
	 * Index of column ULTIMO_FOLIO
	 */
	protected static final int COLUMN_ULTIMO_FOLIO = 7;

	/** 
	 * Index of column ID_ESTATUS_SERIE
	 */
	protected static final int COLUMN_ID_ESTATUS_SERIE = 8;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 8;

	/** 
	 * Index of primary-key column ID_SERIE
	 */
	protected static final int PK_COLUMN_ID_SERIE = 1;

	/** 
	 * Inserts a new row in the SERIE table.
	 */
	public SeriePk insert(Serie dto) throws SerieDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			StringBuffer sql = new StringBuffer();
			StringBuffer values = new StringBuffer();
			sql.append( "INSERT INTO " + getTableName() + " (" );
			int modifiedCount = 0;
			if (dto.isIdemisorModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "IDEMISOR" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdtipocomprobanteModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "IDTIPOCOMPROBANTE" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNombreSerieModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NOMBRE_SERIE" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isRangoIniModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "RANGO_INI" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isRangoFinModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "RANGO_FIN" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isUltimoFolioModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ULTIMO_FOLIO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdEstatusSerieModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ID_ESTATUS_SERIE" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (modifiedCount==0) {
				// nothing to insert
				throw new IllegalStateException( "Nothing to insert" );
			}
		
			sql.append( ") VALUES (" );
			sql.append( values );
			sql.append( ")" );
			stmt = conn.prepareStatement( sql.toString(), Statement.RETURN_GENERATED_KEYS );
			int index = 1;
			if (dto.isIdemisorModified()) {
				stmt.setInt( index++, dto.getIdemisor() );
			}
		
			if (dto.isIdtipocomprobanteModified()) {
				stmt.setInt( index++, dto.getIdtipocomprobante() );
			}
		
			if (dto.isNombreSerieModified()) {
				stmt.setString( index++, dto.getNombreSerie() );
			}
		
			if (dto.isRangoIniModified()) {
				if (dto.isRangoIniNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getRangoIni() );
				}
		
			}
		
			if (dto.isRangoFinModified()) {
				if (dto.isRangoFinNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getRangoFin() );
				}
		
			}
		
			if (dto.isUltimoFolioModified()) {
				if (dto.isUltimoFolioNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getUltimoFolio() );
				}
		
			}
		
			if (dto.isIdEstatusSerieModified()) {
				if (dto.isIdEstatusSerieNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdEstatusSerie() );
				}
		
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setIdSerie( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new SerieDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the SERIE table.
	 */
	public void update(SeriePk pk, Serie dto) throws SerieDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			StringBuffer sql = new StringBuffer();
			sql.append( "UPDATE " + getTableName() + " SET " );
			boolean modified = false;
			if (dto.isIdemisorModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "IDEMISOR=?" );
				modified=true;
			}
		
			if (dto.isIdtipocomprobanteModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "IDTIPOCOMPROBANTE=?" );
				modified=true;
			}
		
			if (dto.isNombreSerieModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NOMBRE_SERIE=?" );
				modified=true;
			}
		
			if (dto.isRangoIniModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "RANGO_INI=?" );
				modified=true;
			}
		
			if (dto.isRangoFinModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "RANGO_FIN=?" );
				modified=true;
			}
		
			if (dto.isUltimoFolioModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ULTIMO_FOLIO=?" );
				modified=true;
			}
		
			if (dto.isIdEstatusSerieModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ID_ESTATUS_SERIE=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE ID_SERIE=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdSerieModified()) {
				stmt.setInt( index++, dto.getIdSerie() );
			}
		
			if (dto.isIdemisorModified()) {
				stmt.setInt( index++, dto.getIdemisor() );
			}
		
			if (dto.isIdtipocomprobanteModified()) {
				stmt.setInt( index++, dto.getIdtipocomprobante() );
			}
		
			if (dto.isNombreSerieModified()) {
				stmt.setString( index++, dto.getNombreSerie() );
			}
		
			if (dto.isRangoIniModified()) {
				if (dto.isRangoIniNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getRangoIni() );
				}
		
			}
		
			if (dto.isRangoFinModified()) {
				if (dto.isRangoFinNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getRangoFin() );
				}
		
			}
		
			if (dto.isUltimoFolioModified()) {
				if (dto.isUltimoFolioNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getUltimoFolio() );
				}
		
			}
		
			if (dto.isIdEstatusSerieModified()) {
				if (dto.isIdEstatusSerieNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getIdEstatusSerie() );
				}
		
			}
		
			stmt.setInt( index++, pk.getIdSerie() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new SerieDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the SERIE table.
	 */
	public void delete(SeriePk pk) throws SerieDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			System.out.println( "Executing " + SQL_DELETE + " with PK: " + pk );
			stmt = conn.prepareStatement( SQL_DELETE );
			stmt.setInt( 1, pk.getIdSerie() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new SerieDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the SERIE table that matches the specified primary-key value.
	 */
	public Serie findByPrimaryKey(SeriePk pk) throws SerieDaoException
	{
		return findByPrimaryKey( pk.getIdSerie() );
	}

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'ID_SERIE = :idSerie'.
	 */
	public Serie findByPrimaryKey(int idSerie) throws SerieDaoException
	{
		Serie ret[] = findByDynamicSelect( SQL_SELECT + " WHERE ID_SERIE = ?", new Object[] {  new Integer(idSerie) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the SERIE table that match the criteria ''.
	 */
	public Serie[] findAll() throws SerieDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY ID_SERIE", null );
	}

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'ID_SERIE = :idSerie'.
	 */
	public Serie[] findWhereIdSerieEquals(int idSerie) throws SerieDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_SERIE = ? ORDER BY ID_SERIE", new Object[] {  new Integer(idSerie) } );
	}

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'IDEMISOR = :idemisor'.
	 */
	public Serie[] findWhereIdemisorEquals(int idemisor) throws SerieDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IDEMISOR = ? ORDER BY IDEMISOR", new Object[] {  new Integer(idemisor) } );
	}

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'IDTIPOCOMPROBANTE = :idtipocomprobante'.
	 */
	public Serie[] findWhereIdtipocomprobanteEquals(int idtipocomprobante) throws SerieDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IDTIPOCOMPROBANTE = ? ORDER BY IDTIPOCOMPROBANTE", new Object[] {  new Integer(idtipocomprobante) } );
	}

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'NOMBRE_SERIE = :nombreSerie'.
	 */
	public Serie[] findWhereNombreSerieEquals(String nombreSerie) throws SerieDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NOMBRE_SERIE = ? ORDER BY NOMBRE_SERIE", new Object[] { nombreSerie } );
	}

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'RANGO_INI = :rangoIni'.
	 */
	public Serie[] findWhereRangoIniEquals(int rangoIni) throws SerieDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE RANGO_INI = ? ORDER BY RANGO_INI", new Object[] {  new Integer(rangoIni) } );
	}

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'RANGO_FIN = :rangoFin'.
	 */
	public Serie[] findWhereRangoFinEquals(int rangoFin) throws SerieDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE RANGO_FIN = ? ORDER BY RANGO_FIN", new Object[] {  new Integer(rangoFin) } );
	}

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'ULTIMO_FOLIO = :ultimoFolio'.
	 */
	public Serie[] findWhereUltimoFolioEquals(int ultimoFolio) throws SerieDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ULTIMO_FOLIO = ? ORDER BY ULTIMO_FOLIO", new Object[] {  new Integer(ultimoFolio) } );
	}

	/** 
	 * Returns all rows from the SERIE table that match the criteria 'ID_ESTATUS_SERIE = :idEstatusSerie'.
	 */
	public Serie[] findWhereIdEstatusSerieEquals(int idEstatusSerie) throws SerieDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ID_ESTATUS_SERIE = ? ORDER BY ID_ESTATUS_SERIE", new Object[] {  new Integer(idEstatusSerie) } );
	}

	/**
	 * Method 'SerieDaoImpl'
	 * 
	 */
	public SerieDaoImpl()
	{
	}

	/**
	 * Method 'SerieDaoImpl'
	 * 
	 * @param userConn
	 */
	public SerieDaoImpl(final java.sql.Connection userConn)
	{
		this.userConn = userConn;
	}

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows)
	{
		this.maxRows = maxRows;
	}

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows()
	{
		return maxRows;
	}

	/**
	 * Method 'getTableName'
	 * 
	 * @return String
	 */
	public String getTableName()
	{
		return "SERIE";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected Serie fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			Serie dto = new Serie();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected Serie[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			Serie dto = new Serie();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		Serie ret[] = new Serie[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(Serie dto, ResultSet rs) throws SQLException
	{
		dto.setIdSerie( rs.getInt( COLUMN_ID_SERIE ) );
		dto.setIdemisor( rs.getInt( COLUMN_IDEMISOR ) );
		dto.setIdtipocomprobante( rs.getInt( COLUMN_IDTIPOCOMPROBANTE ) );
		dto.setNombreSerie( rs.getString( COLUMN_NOMBRE_SERIE ) );
		dto.setRangoIni( rs.getInt( COLUMN_RANGO_INI ) );
		if (rs.wasNull()) {
			dto.setRangoIniNull( true );
		}
		
		dto.setRangoFin( rs.getInt( COLUMN_RANGO_FIN ) );
		if (rs.wasNull()) {
			dto.setRangoFinNull( true );
		}
		
		dto.setUltimoFolio( rs.getInt( COLUMN_ULTIMO_FOLIO ) );
		if (rs.wasNull()) {
			dto.setUltimoFolioNull( true );
		}
		
		dto.setIdEstatusSerie( rs.getInt( COLUMN_ID_ESTATUS_SERIE ) );
		if (rs.wasNull()) {
			dto.setIdEstatusSerieNull( true );
		}
		
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(Serie dto)
	{
		dto.setIdSerieModified( false );
		dto.setIdemisorModified( false );
		dto.setIdtipocomprobanteModified( false );
		dto.setNombreSerieModified( false );
		dto.setRangoIniModified( false );
		dto.setRangoFinModified( false );
		dto.setUltimoFolioModified( false );
		dto.setIdEstatusSerieModified( false );
	}

	/** 
	 * Returns all rows from the SERIE table that match the specified arbitrary SQL statement
	 */
	public Serie[] findByDynamicSelect(String sql, Object[] sqlParams) throws SerieDaoException
	{
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			// construct the SQL statement
			final String SQL = sql;
		
		
			System.out.println( "Executing " + SQL );
			// prepare statement
			stmt = conn.prepareStatement( SQL );
			stmt.setMaxRows( maxRows );
		
			// bind parameters
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
		
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new SerieDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns all rows from the SERIE table that match the specified arbitrary SQL statement
	 */
	public Serie[] findByDynamicWhere(String sql, Object[] sqlParams) throws SerieDaoException
	{
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			// construct the SQL statement
			final String SQL = SQL_SELECT + " WHERE " + sql;
		
		
			System.out.println( "Executing " + SQL );
			// prepare statement
			stmt = conn.prepareStatement( SQL );
			stmt.setMaxRows( maxRows );
		
			// bind parameters
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
		
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new SerieDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

}
