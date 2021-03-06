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

public class UbicacionfiscalDaoImpl extends AbstractDAO implements UbicacionfiscalDao
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
	protected final String SQL_SELECT = "SELECT IDUBICACIONFISCAL, IDEMISOR, CALLE, NUMINT, NUMEXT, COLONIA, CODIGOPOSTAL, PAIS, ESTADO, MUNICIPIO FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( IDEMISOR, CALLE, NUMINT, NUMEXT, COLONIA, CODIGOPOSTAL, PAIS, ESTADO, MUNICIPIO ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET IDEMISOR = ?, CALLE = ?, NUMINT = ?, NUMEXT = ?, COLONIA = ?, CODIGOPOSTAL = ?, PAIS = ?, ESTADO = ?, MUNICIPIO = ? WHERE IDUBICACIONFISCAL = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE IDUBICACIONFISCAL = ?";

	/** 
	 * Index of column IDUBICACIONFISCAL
	 */
	protected static final int COLUMN_IDUBICACIONFISCAL = 1;

	/** 
	 * Index of column IDEMISOR
	 */
	protected static final int COLUMN_IDEMISOR = 2;

	/** 
	 * Index of column CALLE
	 */
	protected static final int COLUMN_CALLE = 3;

	/** 
	 * Index of column NUMINT
	 */
	protected static final int COLUMN_NUMINT = 4;

	/** 
	 * Index of column NUMEXT
	 */
	protected static final int COLUMN_NUMEXT = 5;

	/** 
	 * Index of column COLONIA
	 */
	protected static final int COLUMN_COLONIA = 6;

	/** 
	 * Index of column CODIGOPOSTAL
	 */
	protected static final int COLUMN_CODIGOPOSTAL = 7;

	/** 
	 * Index of column PAIS
	 */
	protected static final int COLUMN_PAIS = 8;

	/** 
	 * Index of column ESTADO
	 */
	protected static final int COLUMN_ESTADO = 9;

	/** 
	 * Index of column MUNICIPIO
	 */
	protected static final int COLUMN_MUNICIPIO = 10;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 10;

	/** 
	 * Index of primary-key column IDUBICACIONFISCAL
	 */
	protected static final int PK_COLUMN_IDUBICACIONFISCAL = 1;

	/** 
	 * Inserts a new row in the UBICACIONFISCAL table.
	 */
	public UbicacionfiscalPk insert(Ubicacionfiscal dto) throws UbicacionfiscalDaoException
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
		
			if (dto.isCalleModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "CALLE" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNumintModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NUMINT" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNumextModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NUMEXT" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isColoniaModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "COLONIA" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isCodigopostalModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "CODIGOPOSTAL" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isPaisModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "PAIS" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isEstadoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ESTADO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isMunicipioModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "MUNICIPIO" );
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
		
			if (dto.isCalleModified()) {
				stmt.setString( index++, dto.getCalle() );
			}
		
			if (dto.isNumintModified()) {
				stmt.setString( index++, dto.getNumint() );
			}
		
			if (dto.isNumextModified()) {
				stmt.setString( index++, dto.getNumext() );
			}
		
			if (dto.isColoniaModified()) {
				stmt.setString( index++, dto.getColonia() );
			}
		
			if (dto.isCodigopostalModified()) {
				stmt.setString( index++, dto.getCodigopostal() );
			}
		
			if (dto.isPaisModified()) {
				stmt.setString( index++, dto.getPais() );
			}
		
			if (dto.isEstadoModified()) {
				stmt.setString( index++, dto.getEstado() );
			}
		
			if (dto.isMunicipioModified()) {
				stmt.setString( index++, dto.getMunicipio() );
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setIdubicacionfiscal( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new UbicacionfiscalDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the UBICACIONFISCAL table.
	 */
	public void update(UbicacionfiscalPk pk, Ubicacionfiscal dto) throws UbicacionfiscalDaoException
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
		
			if (dto.isCalleModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "CALLE=?" );
				modified=true;
			}
		
			if (dto.isNumintModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NUMINT=?" );
				modified=true;
			}
		
			if (dto.isNumextModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NUMEXT=?" );
				modified=true;
			}
		
			if (dto.isColoniaModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "COLONIA=?" );
				modified=true;
			}
		
			if (dto.isCodigopostalModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "CODIGOPOSTAL=?" );
				modified=true;
			}
		
			if (dto.isPaisModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "PAIS=?" );
				modified=true;
			}
		
			if (dto.isEstadoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ESTADO=?" );
				modified=true;
			}
		
			if (dto.isMunicipioModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "MUNICIPIO=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE IDUBICACIONFISCAL=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdubicacionfiscalModified()) {
				stmt.setInt( index++, dto.getIdubicacionfiscal() );
			}
		
			if (dto.isIdemisorModified()) {
				stmt.setInt( index++, dto.getIdemisor() );
			}
		
			if (dto.isCalleModified()) {
				stmt.setString( index++, dto.getCalle() );
			}
		
			if (dto.isNumintModified()) {
				stmt.setString( index++, dto.getNumint() );
			}
		
			if (dto.isNumextModified()) {
				stmt.setString( index++, dto.getNumext() );
			}
		
			if (dto.isColoniaModified()) {
				stmt.setString( index++, dto.getColonia() );
			}
		
			if (dto.isCodigopostalModified()) {
				stmt.setString( index++, dto.getCodigopostal() );
			}
		
			if (dto.isPaisModified()) {
				stmt.setString( index++, dto.getPais() );
			}
		
			if (dto.isEstadoModified()) {
				stmt.setString( index++, dto.getEstado() );
			}
		
			if (dto.isMunicipioModified()) {
				stmt.setString( index++, dto.getMunicipio() );
			}
		
			stmt.setInt( index++, pk.getIdubicacionfiscal() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new UbicacionfiscalDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the UBICACIONFISCAL table.
	 */
	public void delete(UbicacionfiscalPk pk) throws UbicacionfiscalDaoException
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
			stmt.setInt( 1, pk.getIdubicacionfiscal() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new UbicacionfiscalDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the UBICACIONFISCAL table that matches the specified primary-key value.
	 */
	public Ubicacionfiscal findByPrimaryKey(UbicacionfiscalPk pk) throws UbicacionfiscalDaoException
	{
		return findByPrimaryKey( pk.getIdubicacionfiscal() );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria 'IDUBICACIONFISCAL = :idubicacionfiscal'.
	 */
	public Ubicacionfiscal findByPrimaryKey(int idubicacionfiscal) throws UbicacionfiscalDaoException
	{
		Ubicacionfiscal ret[] = findByDynamicSelect( SQL_SELECT + " WHERE IDUBICACIONFISCAL = ?", new Object[] {  new Integer(idubicacionfiscal) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria ''.
	 */
	public Ubicacionfiscal[] findAll() throws UbicacionfiscalDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY IDUBICACIONFISCAL", null );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria 'IDEMISOR = :idemisor'.
	 */
	public Ubicacionfiscal[] findByEmisor(int idemisor) throws UbicacionfiscalDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IDEMISOR = ?", new Object[] {  new Integer(idemisor) } );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria 'IDUBICACIONFISCAL = :idubicacionfiscal'.
	 */
	public Ubicacionfiscal[] findWhereIdubicacionfiscalEquals(int idubicacionfiscal) throws UbicacionfiscalDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IDUBICACIONFISCAL = ? ORDER BY IDUBICACIONFISCAL", new Object[] {  new Integer(idubicacionfiscal) } );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria 'IDEMISOR = :idemisor'.
	 */
	public Ubicacionfiscal[] findWhereIdemisorEquals(int idemisor) throws UbicacionfiscalDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IDEMISOR = ? ORDER BY IDEMISOR", new Object[] {  new Integer(idemisor) } );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria 'CALLE = :calle'.
	 */
	public Ubicacionfiscal[] findWhereCalleEquals(String calle) throws UbicacionfiscalDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CALLE = ? ORDER BY CALLE", new Object[] { calle } );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria 'NUMINT = :numint'.
	 */
	public Ubicacionfiscal[] findWhereNumintEquals(String numint) throws UbicacionfiscalDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NUMINT = ? ORDER BY NUMINT", new Object[] { numint } );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria 'NUMEXT = :numext'.
	 */
	public Ubicacionfiscal[] findWhereNumextEquals(String numext) throws UbicacionfiscalDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NUMEXT = ? ORDER BY NUMEXT", new Object[] { numext } );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria 'COLONIA = :colonia'.
	 */
	public Ubicacionfiscal[] findWhereColoniaEquals(String colonia) throws UbicacionfiscalDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE COLONIA = ? ORDER BY COLONIA", new Object[] { colonia } );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria 'CODIGOPOSTAL = :codigopostal'.
	 */
	public Ubicacionfiscal[] findWhereCodigopostalEquals(String codigopostal) throws UbicacionfiscalDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE CODIGOPOSTAL = ? ORDER BY CODIGOPOSTAL", new Object[] { codigopostal } );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria 'PAIS = :pais'.
	 */
	public Ubicacionfiscal[] findWherePaisEquals(String pais) throws UbicacionfiscalDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE PAIS = ? ORDER BY PAIS", new Object[] { pais } );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria 'ESTADO = :estado'.
	 */
	public Ubicacionfiscal[] findWhereEstadoEquals(String estado) throws UbicacionfiscalDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ESTADO = ? ORDER BY ESTADO", new Object[] { estado } );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the criteria 'MUNICIPIO = :municipio'.
	 */
	public Ubicacionfiscal[] findWhereMunicipioEquals(String municipio) throws UbicacionfiscalDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE MUNICIPIO = ? ORDER BY MUNICIPIO", new Object[] { municipio } );
	}

	/**
	 * Method 'UbicacionfiscalDaoImpl'
	 * 
	 */
	public UbicacionfiscalDaoImpl()
	{
	}

	/**
	 * Method 'UbicacionfiscalDaoImpl'
	 * 
	 * @param userConn
	 */
	public UbicacionfiscalDaoImpl(final java.sql.Connection userConn)
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
		return "UBICACIONFISCAL";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected Ubicacionfiscal fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			Ubicacionfiscal dto = new Ubicacionfiscal();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected Ubicacionfiscal[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			Ubicacionfiscal dto = new Ubicacionfiscal();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		Ubicacionfiscal ret[] = new Ubicacionfiscal[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(Ubicacionfiscal dto, ResultSet rs) throws SQLException
	{
		dto.setIdubicacionfiscal( rs.getInt( COLUMN_IDUBICACIONFISCAL ) );
		dto.setIdemisor( rs.getInt( COLUMN_IDEMISOR ) );
		dto.setCalle( rs.getString( COLUMN_CALLE ) );
		dto.setNumint( rs.getString( COLUMN_NUMINT ) );
		dto.setNumext( rs.getString( COLUMN_NUMEXT ) );
		dto.setColonia( rs.getString( COLUMN_COLONIA ) );
		dto.setCodigopostal( rs.getString( COLUMN_CODIGOPOSTAL ) );
		dto.setPais( rs.getString( COLUMN_PAIS ) );
		dto.setEstado( rs.getString( COLUMN_ESTADO ) );
		dto.setMunicipio( rs.getString( COLUMN_MUNICIPIO ) );
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(Ubicacionfiscal dto)
	{
		dto.setIdubicacionfiscalModified( false );
		dto.setIdemisorModified( false );
		dto.setCalleModified( false );
		dto.setNumintModified( false );
		dto.setNumextModified( false );
		dto.setColoniaModified( false );
		dto.setCodigopostalModified( false );
		dto.setPaisModified( false );
		dto.setEstadoModified( false );
		dto.setMunicipioModified( false );
	}

	/** 
	 * Returns all rows from the UBICACIONFISCAL table that match the specified arbitrary SQL statement
	 */
	public Ubicacionfiscal[] findByDynamicSelect(String sql, Object[] sqlParams) throws UbicacionfiscalDaoException
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
			throw new UbicacionfiscalDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the UBICACIONFISCAL table that match the specified arbitrary SQL statement
	 */
	public Ubicacionfiscal[] findByDynamicWhere(String sql, Object[] sqlParams) throws UbicacionfiscalDaoException
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
			throw new UbicacionfiscalDaoException( "Exception: " + _e.getMessage(), _e );
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
