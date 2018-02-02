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
import java.util.Date;
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

public class ArchivomaestroDaoImpl extends AbstractDAO implements ArchivomaestroDao
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
	protected final String SQL_SELECT = "SELECT IDARCHIVOMAESTRO, NOMBREARCHIVO, IDESTATUS, FECHACREACION, FECHAPROCESO, NUMEROFACTURAS, TAMANOARCHIVO, MD5_CHECKSUM, OBSERVACIONES FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( NOMBREARCHIVO, IDESTATUS, FECHACREACION, FECHAPROCESO, NUMEROFACTURAS, TAMANOARCHIVO, MD5_CHECKSUM, OBSERVACIONES ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET NOMBREARCHIVO = ?, IDESTATUS = ?, FECHACREACION = ?, FECHAPROCESO = ?, NUMEROFACTURAS = ?, TAMANOARCHIVO = ?, MD5_CHECKSUM = ?, OBSERVACIONES = ? WHERE IDARCHIVOMAESTRO = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE IDARCHIVOMAESTRO = ?";

	/** 
	 * Index of column IDARCHIVOMAESTRO
	 */
	protected static final int COLUMN_IDARCHIVOMAESTRO = 1;

	/** 
	 * Index of column NOMBREARCHIVO
	 */
	protected static final int COLUMN_NOMBREARCHIVO = 2;

	/** 
	 * Index of column IDESTATUS
	 */
	protected static final int COLUMN_IDESTATUS = 3;

	/** 
	 * Index of column FECHACREACION
	 */
	protected static final int COLUMN_FECHACREACION = 4;

	/** 
	 * Index of column FECHAPROCESO
	 */
	protected static final int COLUMN_FECHAPROCESO = 5;

	/** 
	 * Index of column NUMEROFACTURAS
	 */
	protected static final int COLUMN_NUMEROFACTURAS = 6;

	/** 
	 * Index of column TAMANOARCHIVO
	 */
	protected static final int COLUMN_TAMANOARCHIVO = 7;

	/** 
	 * Index of column MD5_CHECKSUM
	 */
	protected static final int COLUMN_MD5_CHECKSUM = 8;

	/** 
	 * Index of column OBSERVACIONES
	 */
	protected static final int COLUMN_OBSERVACIONES = 9;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 9;

	/** 
	 * Index of primary-key column IDARCHIVOMAESTRO
	 */
	protected static final int PK_COLUMN_IDARCHIVOMAESTRO = 1;

	/** 
	 * Inserts a new row in the ARCHIVOMAESTRO table.
	 */
	public ArchivomaestroPk insert(Archivomaestro dto) throws ArchivomaestroDaoException
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
			if (dto.isNombrearchivoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NOMBREARCHIVO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isIdestatusModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "IDESTATUS" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isFechacreacionModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "FECHACREACION" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isFechaprocesoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "FECHAPROCESO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNumerofacturasModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NUMEROFACTURAS" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isTamanoarchivoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "TAMANOARCHIVO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isMd5ChecksumModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "MD5_CHECKSUM" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isObservacionesModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "OBSERVACIONES" );
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
			if (dto.isNombrearchivoModified()) {
				stmt.setString( index++, dto.getNombrearchivo() );
			}
		
			if (dto.isIdestatusModified()) {
				stmt.setInt( index++, dto.getIdestatus() );
			}
		
			if (dto.isFechacreacionModified()) {
				stmt.setTimestamp(index++, dto.getFechacreacion()==null ? null : new java.sql.Timestamp( dto.getFechacreacion().getTime() ) );
			}
		
			if (dto.isFechaprocesoModified()) {
				stmt.setTimestamp(index++, dto.getFechaproceso()==null ? null : new java.sql.Timestamp( dto.getFechaproceso().getTime() ) );
			}
		
			if (dto.isNumerofacturasModified()) {
				if (dto.isNumerofacturasNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getNumerofacturas() );
				}
		
			}
		
			if (dto.isTamanoarchivoModified()) {
				if (dto.isTamanoarchivoNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setLong( index++, dto.getTamanoarchivo() );
				}
		
			}
		
			if (dto.isMd5ChecksumModified()) {
				stmt.setString( index++, dto.getMd5Checksum() );
			}
		
			if (dto.isObservacionesModified()) {
				stmt.setString( index++, dto.getObservaciones() );
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setIdarchivomaestro( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new ArchivomaestroDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the ARCHIVOMAESTRO table.
	 */
	public void update(ArchivomaestroPk pk, Archivomaestro dto) throws ArchivomaestroDaoException
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
			if (dto.isNombrearchivoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NOMBREARCHIVO=?" );
				modified=true;
			}
		
			if (dto.isIdestatusModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "IDESTATUS=?" );
				modified=true;
			}
		
			if (dto.isFechacreacionModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "FECHACREACION=?" );
				modified=true;
			}
		
			if (dto.isFechaprocesoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "FECHAPROCESO=?" );
				modified=true;
			}
		
			if (dto.isNumerofacturasModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NUMEROFACTURAS=?" );
				modified=true;
			}
		
			if (dto.isTamanoarchivoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "TAMANOARCHIVO=?" );
				modified=true;
			}
		
			if (dto.isMd5ChecksumModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "MD5_CHECKSUM=?" );
				modified=true;
			}
		
			if (dto.isObservacionesModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "OBSERVACIONES=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE IDARCHIVOMAESTRO=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdarchivomaestroModified()) {
				stmt.setInt( index++, dto.getIdarchivomaestro() );
			}
		
			if (dto.isNombrearchivoModified()) {
				stmt.setString( index++, dto.getNombrearchivo() );
			}
		
			if (dto.isIdestatusModified()) {
				stmt.setInt( index++, dto.getIdestatus() );
			}
		
			if (dto.isFechacreacionModified()) {
				stmt.setTimestamp(index++, dto.getFechacreacion()==null ? null : new java.sql.Timestamp( dto.getFechacreacion().getTime() ) );
			}
		
			if (dto.isFechaprocesoModified()) {
				stmt.setTimestamp(index++, dto.getFechaproceso()==null ? null : new java.sql.Timestamp( dto.getFechaproceso().getTime() ) );
			}
		
			if (dto.isNumerofacturasModified()) {
				if (dto.isNumerofacturasNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getNumerofacturas() );
				}
		
			}
		
			if (dto.isTamanoarchivoModified()) {
				if (dto.isTamanoarchivoNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setLong( index++, dto.getTamanoarchivo() );
				}
		
			}
		
			if (dto.isMd5ChecksumModified()) {
				stmt.setString( index++, dto.getMd5Checksum() );
			}
		
			if (dto.isObservacionesModified()) {
				stmt.setString( index++, dto.getObservaciones() );
			}
		
			stmt.setInt( index++, pk.getIdarchivomaestro() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new ArchivomaestroDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the ARCHIVOMAESTRO table.
	 */
	public void delete(ArchivomaestroPk pk) throws ArchivomaestroDaoException
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
			stmt.setInt( 1, pk.getIdarchivomaestro() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new ArchivomaestroDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the ARCHIVOMAESTRO table that matches the specified primary-key value.
	 */
	public Archivomaestro findByPrimaryKey(ArchivomaestroPk pk) throws ArchivomaestroDaoException
	{
		return findByPrimaryKey( pk.getIdarchivomaestro() );
	}

	/** 
	 * Returns all rows from the ARCHIVOMAESTRO table that match the criteria 'IDARCHIVOMAESTRO = :idarchivomaestro'.
	 */
	public Archivomaestro findByPrimaryKey(int idarchivomaestro) throws ArchivomaestroDaoException
	{
		Archivomaestro ret[] = findByDynamicSelect( SQL_SELECT + " WHERE IDARCHIVOMAESTRO = ?", new Object[] {  new Integer(idarchivomaestro) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the ARCHIVOMAESTRO table that match the criteria ''.
	 */
	public Archivomaestro[] findAll() throws ArchivomaestroDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY IDARCHIVOMAESTRO", null );
	}

	/** 
	 * Returns all rows from the ARCHIVOMAESTRO table that match the criteria 'IDARCHIVOMAESTRO = :idarchivomaestro'.
	 */
	public Archivomaestro[] findWhereIdarchivomaestroEquals(int idarchivomaestro) throws ArchivomaestroDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IDARCHIVOMAESTRO = ? ORDER BY IDARCHIVOMAESTRO", new Object[] {  new Integer(idarchivomaestro) } );
	}

	/** 
	 * Returns all rows from the ARCHIVOMAESTRO table that match the criteria 'NOMBREARCHIVO = :nombrearchivo'.
	 */
	public Archivomaestro[] findWhereNombrearchivoEquals(String nombrearchivo) throws ArchivomaestroDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NOMBREARCHIVO = ? ORDER BY NOMBREARCHIVO", new Object[] { nombrearchivo } );
	}

	/** 
	 * Returns all rows from the ARCHIVOMAESTRO table that match the criteria 'IDESTATUS = :idestatus'.
	 */
	public Archivomaestro[] findWhereIdestatusEquals(int idestatus) throws ArchivomaestroDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IDESTATUS = ? ORDER BY IDESTATUS", new Object[] {  new Integer(idestatus) } );
	}

	/** 
	 * Returns all rows from the ARCHIVOMAESTRO table that match the criteria 'FECHACREACION = :fechacreacion'.
	 */
	public Archivomaestro[] findWhereFechacreacionEquals(Date fechacreacion) throws ArchivomaestroDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE FECHACREACION = ? ORDER BY FECHACREACION", new Object[] { fechacreacion==null ? null : new java.sql.Timestamp( fechacreacion.getTime() ) } );
	}

	/** 
	 * Returns all rows from the ARCHIVOMAESTRO table that match the criteria 'FECHAPROCESO = :fechaproceso'.
	 */
	public Archivomaestro[] findWhereFechaprocesoEquals(Date fechaproceso) throws ArchivomaestroDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE FECHAPROCESO = ? ORDER BY FECHAPROCESO", new Object[] { fechaproceso==null ? null : new java.sql.Timestamp( fechaproceso.getTime() ) } );
	}

	/** 
	 * Returns all rows from the ARCHIVOMAESTRO table that match the criteria 'NUMEROFACTURAS = :numerofacturas'.
	 */
	public Archivomaestro[] findWhereNumerofacturasEquals(int numerofacturas) throws ArchivomaestroDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NUMEROFACTURAS = ? ORDER BY NUMEROFACTURAS", new Object[] {  new Integer(numerofacturas) } );
	}

	/** 
	 * Returns all rows from the ARCHIVOMAESTRO table that match the criteria 'TAMANOARCHIVO = :tamanoarchivo'.
	 */
	public Archivomaestro[] findWhereTamanoarchivoEquals(int tamanoarchivo) throws ArchivomaestroDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE TAMANOARCHIVO = ? ORDER BY TAMANOARCHIVO", new Object[] {  new Integer(tamanoarchivo) } );
	}

	/** 
	 * Returns all rows from the ARCHIVOMAESTRO table that match the criteria 'MD5_CHECKSUM = :md5Checksum'.
	 */
	public Archivomaestro[] findWhereMd5ChecksumEquals(String md5Checksum) throws ArchivomaestroDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE MD5_CHECKSUM = ? ORDER BY MD5_CHECKSUM", new Object[] { md5Checksum } );
	}

	/** 
	 * Returns all rows from the ARCHIVOMAESTRO table that match the criteria 'OBSERVACIONES = :observaciones'.
	 */
	public Archivomaestro[] findWhereObservacionesEquals(String observaciones) throws ArchivomaestroDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE OBSERVACIONES = ? ORDER BY OBSERVACIONES", new Object[] { observaciones } );
	}

	/**
	 * Method 'ArchivomaestroDaoImpl'
	 * 
	 */
	public ArchivomaestroDaoImpl()
	{
	}

	/**
	 * Method 'ArchivomaestroDaoImpl'
	 * 
	 * @param userConn
	 */
	public ArchivomaestroDaoImpl(final java.sql.Connection userConn)
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
		return "ARCHIVOMAESTRO";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected Archivomaestro fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			Archivomaestro dto = new Archivomaestro();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected Archivomaestro[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			Archivomaestro dto = new Archivomaestro();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		Archivomaestro ret[] = new Archivomaestro[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(Archivomaestro dto, ResultSet rs) throws SQLException
	{
		dto.setIdarchivomaestro( rs.getInt( COLUMN_IDARCHIVOMAESTRO ) );
		dto.setNombrearchivo( rs.getString( COLUMN_NOMBREARCHIVO ) );
		dto.setIdestatus( rs.getInt( COLUMN_IDESTATUS ) );
		dto.setFechacreacion( rs.getTimestamp(COLUMN_FECHACREACION ) );
		dto.setFechaproceso( rs.getTimestamp(COLUMN_FECHAPROCESO ) );
		dto.setNumerofacturas( rs.getInt( COLUMN_NUMEROFACTURAS ) );
		if (rs.wasNull()) {
			dto.setNumerofacturasNull( true );
		}
		
		dto.setTamanoarchivo( rs.getLong( COLUMN_TAMANOARCHIVO ) );
		if (rs.wasNull()) {
			dto.setTamanoarchivoNull( true );
		}
		
		dto.setMd5Checksum( rs.getString( COLUMN_MD5_CHECKSUM ) );
		dto.setObservaciones( rs.getString( COLUMN_OBSERVACIONES ) );
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(Archivomaestro dto)
	{
		dto.setIdarchivomaestroModified( false );
		dto.setNombrearchivoModified( false );
		dto.setIdestatusModified( false );
		dto.setFechacreacionModified( false );
		dto.setFechaprocesoModified( false );
		dto.setNumerofacturasModified( false );
		dto.setTamanoarchivoModified( false );
		dto.setMd5ChecksumModified( false );
		dto.setObservacionesModified( false );
	}

	/** 
	 * Returns all rows from the ARCHIVOMAESTRO table that match the specified arbitrary SQL statement
	 */
	public Archivomaestro[] findByDynamicSelect(String sql, Object[] sqlParams) throws ArchivomaestroDaoException
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
			throw new ArchivomaestroDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the ARCHIVOMAESTRO table that match the specified arbitrary SQL statement
	 */
	public Archivomaestro[] findByDynamicWhere(String sql, Object[] sqlParams) throws ArchivomaestroDaoException
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
			throw new ArchivomaestroDaoException( "Exception: " + _e.getMessage(), _e );
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