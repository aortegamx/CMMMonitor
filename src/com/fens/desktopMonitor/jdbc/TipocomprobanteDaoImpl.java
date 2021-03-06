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

public class TipocomprobanteDaoImpl extends AbstractDAO implements TipocomprobanteDao
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
	protected final String SQL_SELECT = "SELECT IDTIPOCOMPROBANTE, NOMBRETIPOCOMPROBANTE, DESCRIPCION FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( NOMBRETIPOCOMPROBANTE, DESCRIPCION ) VALUES ( ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET NOMBRETIPOCOMPROBANTE = ?, DESCRIPCION = ? WHERE IDTIPOCOMPROBANTE = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE IDTIPOCOMPROBANTE = ?";

	/** 
	 * Index of column IDTIPOCOMPROBANTE
	 */
	protected static final int COLUMN_IDTIPOCOMPROBANTE = 1;

	/** 
	 * Index of column NOMBRETIPOCOMPROBANTE
	 */
	protected static final int COLUMN_NOMBRETIPOCOMPROBANTE = 2;

	/** 
	 * Index of column DESCRIPCION
	 */
	protected static final int COLUMN_DESCRIPCION = 3;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 3;

	/** 
	 * Index of primary-key column IDTIPOCOMPROBANTE
	 */
	protected static final int PK_COLUMN_IDTIPOCOMPROBANTE = 1;

	/** 
	 * Inserts a new row in the TIPOCOMPROBANTE table.
	 */
	public TipocomprobantePk insert(Tipocomprobante dto) throws TipocomprobanteDaoException
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
			if (dto.isNombretipocomprobanteModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NOMBRETIPOCOMPROBANTE" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isDescripcionModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "DESCRIPCION" );
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
			if (dto.isNombretipocomprobanteModified()) {
				stmt.setString( index++, dto.getNombretipocomprobante() );
			}
		
			if (dto.isDescripcionModified()) {
				stmt.setString( index++, dto.getDescripcion() );
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setIdtipocomprobante( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TipocomprobanteDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the TIPOCOMPROBANTE table.
	 */
	public void update(TipocomprobantePk pk, Tipocomprobante dto) throws TipocomprobanteDaoException
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
			if (dto.isNombretipocomprobanteModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NOMBRETIPOCOMPROBANTE=?" );
				modified=true;
			}
		
			if (dto.isDescripcionModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "DESCRIPCION=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE IDTIPOCOMPROBANTE=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdtipocomprobanteModified()) {
				stmt.setInt( index++, dto.getIdtipocomprobante() );
			}
		
			if (dto.isNombretipocomprobanteModified()) {
				stmt.setString( index++, dto.getNombretipocomprobante() );
			}
		
			if (dto.isDescripcionModified()) {
				stmt.setString( index++, dto.getDescripcion() );
			}
		
			stmt.setInt( index++, pk.getIdtipocomprobante() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TipocomprobanteDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the TIPOCOMPROBANTE table.
	 */
	public void delete(TipocomprobantePk pk) throws TipocomprobanteDaoException
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
			stmt.setInt( 1, pk.getIdtipocomprobante() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TipocomprobanteDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the TIPOCOMPROBANTE table that matches the specified primary-key value.
	 */
	public Tipocomprobante findByPrimaryKey(TipocomprobantePk pk) throws TipocomprobanteDaoException
	{
		return findByPrimaryKey( pk.getIdtipocomprobante() );
	}

	/** 
	 * Returns all rows from the TIPOCOMPROBANTE table that match the criteria 'IDTIPOCOMPROBANTE = :idtipocomprobante'.
	 */
	public Tipocomprobante findByPrimaryKey(int idtipocomprobante) throws TipocomprobanteDaoException
	{
		Tipocomprobante ret[] = findByDynamicSelect( SQL_SELECT + " WHERE IDTIPOCOMPROBANTE = ?", new Object[] {  new Integer(idtipocomprobante) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the TIPOCOMPROBANTE table that match the criteria ''.
	 */
	public Tipocomprobante[] findAll() throws TipocomprobanteDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY IDTIPOCOMPROBANTE", null );
	}

	/** 
	 * Returns all rows from the TIPOCOMPROBANTE table that match the criteria 'IDTIPOCOMPROBANTE = :idtipocomprobante'.
	 */
	public Tipocomprobante[] findWhereIdtipocomprobanteEquals(int idtipocomprobante) throws TipocomprobanteDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IDTIPOCOMPROBANTE = ? ORDER BY IDTIPOCOMPROBANTE", new Object[] {  new Integer(idtipocomprobante) } );
	}

	/** 
	 * Returns all rows from the TIPOCOMPROBANTE table that match the criteria 'NOMBRETIPOCOMPROBANTE = :nombretipocomprobante'.
	 */
	public Tipocomprobante[] findWhereNombretipocomprobanteEquals(String nombretipocomprobante) throws TipocomprobanteDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NOMBRETIPOCOMPROBANTE = ? ORDER BY NOMBRETIPOCOMPROBANTE", new Object[] { nombretipocomprobante } );
	}

	/** 
	 * Returns all rows from the TIPOCOMPROBANTE table that match the criteria 'DESCRIPCION = :descripcion'.
	 */
	public Tipocomprobante[] findWhereDescripcionEquals(String descripcion) throws TipocomprobanteDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE DESCRIPCION = ? ORDER BY DESCRIPCION", new Object[] { descripcion } );
	}

	/**
	 * Method 'TipocomprobanteDaoImpl'
	 * 
	 */
	public TipocomprobanteDaoImpl()
	{
	}

	/**
	 * Method 'TipocomprobanteDaoImpl'
	 * 
	 * @param userConn
	 */
	public TipocomprobanteDaoImpl(final java.sql.Connection userConn)
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
		return "TIPOCOMPROBANTE";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected Tipocomprobante fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			Tipocomprobante dto = new Tipocomprobante();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected Tipocomprobante[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			Tipocomprobante dto = new Tipocomprobante();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		Tipocomprobante ret[] = new Tipocomprobante[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(Tipocomprobante dto, ResultSet rs) throws SQLException
	{
		dto.setIdtipocomprobante( rs.getInt( COLUMN_IDTIPOCOMPROBANTE ) );
		dto.setNombretipocomprobante( rs.getString( COLUMN_NOMBRETIPOCOMPROBANTE ) );
		dto.setDescripcion( rs.getString( COLUMN_DESCRIPCION ) );
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(Tipocomprobante dto)
	{
		dto.setIdtipocomprobanteModified( false );
		dto.setNombretipocomprobanteModified( false );
		dto.setDescripcionModified( false );
	}

	/** 
	 * Returns all rows from the TIPOCOMPROBANTE table that match the specified arbitrary SQL statement
	 */
	public Tipocomprobante[] findByDynamicSelect(String sql, Object[] sqlParams) throws TipocomprobanteDaoException
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
			throw new TipocomprobanteDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the TIPOCOMPROBANTE table that match the specified arbitrary SQL statement
	 */
	public Tipocomprobante[] findByDynamicWhere(String sql, Object[] sqlParams) throws TipocomprobanteDaoException
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
			throw new TipocomprobanteDaoException( "Exception: " + _e.getMessage(), _e );
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
