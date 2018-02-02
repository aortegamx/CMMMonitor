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

public class EstatusDaoImpl extends AbstractDAO implements EstatusDao
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
	protected final String SQL_SELECT = "SELECT IDESTATUS, NOMBREESTATUS, DESCRIPCION, TIPOESTATUS FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( NOMBREESTATUS, DESCRIPCION, TIPOESTATUS ) VALUES ( ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET NOMBREESTATUS = ?, DESCRIPCION = ?, TIPOESTATUS = ? WHERE IDESTATUS = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE IDESTATUS = ?";

	/** 
	 * Index of column IDESTATUS
	 */
	protected static final int COLUMN_IDESTATUS = 1;

	/** 
	 * Index of column NOMBREESTATUS
	 */
	protected static final int COLUMN_NOMBREESTATUS = 2;

	/** 
	 * Index of column DESCRIPCION
	 */
	protected static final int COLUMN_DESCRIPCION = 3;

	/** 
	 * Index of column TIPOESTATUS
	 */
	protected static final int COLUMN_TIPOESTATUS = 4;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 4;

	/** 
	 * Index of primary-key column IDESTATUS
	 */
	protected static final int PK_COLUMN_IDESTATUS = 1;

	/** 
	 * Inserts a new row in the ESTATUS table.
	 */
	public EstatusPk insert(Estatus dto) throws EstatusDaoException
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
			if (dto.isNombreestatusModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NOMBREESTATUS" );
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
		
			if (dto.isTipoestatusModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "TIPOESTATUS" );
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
			if (dto.isNombreestatusModified()) {
				stmt.setString( index++, dto.getNombreestatus() );
			}
		
			if (dto.isDescripcionModified()) {
				stmt.setString( index++, dto.getDescripcion() );
			}
		
			if (dto.isTipoestatusModified()) {
				stmt.setInt( index++, dto.getTipoestatus() );
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setIdestatus( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new EstatusDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the ESTATUS table.
	 */
	public void update(EstatusPk pk, Estatus dto) throws EstatusDaoException
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
			if (dto.isNombreestatusModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NOMBREESTATUS=?" );
				modified=true;
			}
		
			if (dto.isDescripcionModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "DESCRIPCION=?" );
				modified=true;
			}
		
			if (dto.isTipoestatusModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "TIPOESTATUS=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE IDESTATUS=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdestatusModified()) {
				stmt.setInt( index++, dto.getIdestatus() );
			}
		
			if (dto.isNombreestatusModified()) {
				stmt.setString( index++, dto.getNombreestatus() );
			}
		
			if (dto.isDescripcionModified()) {
				stmt.setString( index++, dto.getDescripcion() );
			}
		
			if (dto.isTipoestatusModified()) {
				stmt.setInt( index++, dto.getTipoestatus() );
			}
		
			stmt.setInt( index++, pk.getIdestatus() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new EstatusDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the ESTATUS table.
	 */
	public void delete(EstatusPk pk) throws EstatusDaoException
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
			stmt.setInt( 1, pk.getIdestatus() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new EstatusDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the ESTATUS table that matches the specified primary-key value.
	 */
	public Estatus findByPrimaryKey(EstatusPk pk) throws EstatusDaoException
	{
		return findByPrimaryKey( pk.getIdestatus() );
	}

	/** 
	 * Returns all rows from the ESTATUS table that match the criteria 'IDESTATUS = :idestatus'.
	 */
	public Estatus findByPrimaryKey(int idestatus) throws EstatusDaoException
	{
		Estatus ret[] = findByDynamicSelect( SQL_SELECT + " WHERE IDESTATUS = ?", new Object[] {  new Integer(idestatus) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the ESTATUS table that match the criteria ''.
	 */
	public Estatus[] findAll() throws EstatusDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY IDESTATUS", null );
	}

	/** 
	 * Returns all rows from the ESTATUS table that match the criteria 'IDESTATUS = :idestatus'.
	 */
	public Estatus[] findWhereIdestatusEquals(int idestatus) throws EstatusDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IDESTATUS = ? ORDER BY IDESTATUS", new Object[] {  new Integer(idestatus) } );
	}

	/** 
	 * Returns all rows from the ESTATUS table that match the criteria 'NOMBREESTATUS = :nombreestatus'.
	 */
	public Estatus[] findWhereNombreestatusEquals(String nombreestatus) throws EstatusDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NOMBREESTATUS = ? ORDER BY NOMBREESTATUS", new Object[] { nombreestatus } );
	}

	/** 
	 * Returns all rows from the ESTATUS table that match the criteria 'DESCRIPCION = :descripcion'.
	 */
	public Estatus[] findWhereDescripcionEquals(String descripcion) throws EstatusDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE DESCRIPCION = ? ORDER BY DESCRIPCION", new Object[] { descripcion } );
	}

	/** 
	 * Returns all rows from the ESTATUS table that match the criteria 'TIPOESTATUS = :tipoestatus'.
	 */
	public Estatus[] findWhereTipoestatusEquals(int tipoestatus) throws EstatusDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE TIPOESTATUS = ? ORDER BY TIPOESTATUS", new Object[] {  new Integer(tipoestatus) } );
	}

	/**
	 * Method 'EstatusDaoImpl'
	 * 
	 */
	public EstatusDaoImpl()
	{
	}

	/**
	 * Method 'EstatusDaoImpl'
	 * 
	 * @param userConn
	 */
	public EstatusDaoImpl(final java.sql.Connection userConn)
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
		return "ESTATUS";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected Estatus fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			Estatus dto = new Estatus();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected Estatus[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			Estatus dto = new Estatus();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		Estatus ret[] = new Estatus[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(Estatus dto, ResultSet rs) throws SQLException
	{
		dto.setIdestatus( rs.getInt( COLUMN_IDESTATUS ) );
		dto.setNombreestatus( rs.getString( COLUMN_NOMBREESTATUS ) );
		dto.setDescripcion( rs.getString( COLUMN_DESCRIPCION ) );
		dto.setTipoestatus( rs.getInt( COLUMN_TIPOESTATUS ) );
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(Estatus dto)
	{
		dto.setIdestatusModified( false );
		dto.setNombreestatusModified( false );
		dto.setDescripcionModified( false );
		dto.setTipoestatusModified( false );
	}

	/** 
	 * Returns all rows from the ESTATUS table that match the specified arbitrary SQL statement
	 */
	public Estatus[] findByDynamicSelect(String sql, Object[] sqlParams) throws EstatusDaoException
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
			throw new EstatusDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the ESTATUS table that match the specified arbitrary SQL statement
	 */
	public Estatus[] findByDynamicWhere(String sql, Object[] sqlParams) throws EstatusDaoException
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
			throw new EstatusDaoException( "Exception: " + _e.getMessage(), _e );
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