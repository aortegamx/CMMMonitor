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

public class EmisorDaoImpl extends AbstractDAO implements EmisorDao
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
	protected final String SQL_SELECT = "SELECT IDEMISOR, RFC, RAZONSOCIAL, NOMBRECOMERCIAL, REGIMENFISCAL, ESTATUS, REGISTROPATRONAL, PLANTILLACOMPROBANTE, RUTALOGO, RUTACER, RUTAKEY, EMISORPASS, PLANTILLANOMINA, PLANTILLA_RETENCIONES, SECTOR_PRIMARIO FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( IDEMISOR, RFC, RAZONSOCIAL, NOMBRECOMERCIAL, REGIMENFISCAL, ESTATUS, REGISTROPATRONAL, PLANTILLACOMPROBANTE, RUTALOGO, RUTACER, RUTAKEY, EMISORPASS, PLANTILLANOMINA, PLANTILLA_RETENCIONES, SECTOR_PRIMARIO ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET IDEMISOR = ?, RFC = ?, RAZONSOCIAL = ?, NOMBRECOMERCIAL = ?, REGIMENFISCAL = ?, ESTATUS = ?, REGISTROPATRONAL = ?, PLANTILLACOMPROBANTE = ?, RUTALOGO = ?, RUTACER = ?, RUTAKEY = ?, EMISORPASS = ?, PLANTILLANOMINA = ?, PLANTILLA_RETENCIONES = ?, SECTOR_PRIMARIO = ? WHERE IDEMISOR = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE IDEMISOR = ?";

	/** 
	 * Index of column IDEMISOR
	 */
	protected static final int COLUMN_IDEMISOR = 1;

	/** 
	 * Index of column RFC
	 */
	protected static final int COLUMN_RFC = 2;

	/** 
	 * Index of column RAZONSOCIAL
	 */
	protected static final int COLUMN_RAZONSOCIAL = 3;

	/** 
	 * Index of column NOMBRECOMERCIAL
	 */
	protected static final int COLUMN_NOMBRECOMERCIAL = 4;

	/** 
	 * Index of column REGIMENFISCAL
	 */
	protected static final int COLUMN_REGIMENFISCAL = 5;

	/** 
	 * Index of column ESTATUS
	 */
	protected static final int COLUMN_ESTATUS = 6;

	/** 
	 * Index of column REGISTROPATRONAL
	 */
	protected static final int COLUMN_REGISTROPATRONAL = 7;

	/** 
	 * Index of column PLANTILLACOMPROBANTE
	 */
	protected static final int COLUMN_PLANTILLACOMPROBANTE = 8;

	/** 
	 * Index of column RUTALOGO
	 */
	protected static final int COLUMN_RUTALOGO = 9;

	/** 
	 * Index of column RUTACER
	 */
	protected static final int COLUMN_RUTACER = 10;

	/** 
	 * Index of column RUTAKEY
	 */
	protected static final int COLUMN_RUTAKEY = 11;

	/** 
	 * Index of column EMISORPASS
	 */
	protected static final int COLUMN_EMISORPASS = 12;

	/** 
	 * Index of column PLANTILLANOMINA
	 */
	protected static final int COLUMN_PLANTILLANOMINA = 13;

	/** 
	 * Index of column PLANTILLA_RETENCIONES
	 */
	protected static final int COLUMN_PLANTILLA_RETENCIONES = 14;

	/** 
	 * Index of column SECTOR_PRIMARIO
	 */
	protected static final int COLUMN_SECTOR_PRIMARIO = 15;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 15;

	/** 
	 * Index of primary-key column IDEMISOR
	 */
	protected static final int PK_COLUMN_IDEMISOR = 1;

	/** 
	 * Inserts a new row in the EMISOR table.
	 */
	public EmisorPk insert(Emisor dto) throws EmisorDaoException
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
		
			if (dto.isRfcModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "RFC" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isRazonsocialModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "RAZONSOCIAL" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNombrecomercialModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NOMBRECOMERCIAL" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isRegimenfiscalModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "REGIMENFISCAL" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isEstatusModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "ESTATUS" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isRegistropatronalModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "REGISTROPATRONAL" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isPlantillacomprobanteModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "PLANTILLACOMPROBANTE" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isRutalogoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "RUTALOGO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isRutacerModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "RUTACER" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isRutakeyModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "RUTAKEY" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isEmisorpassModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "EMISORPASS" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isPlantillanominaModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "PLANTILLANOMINA" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isPlantillaRetencionesModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "PLANTILLA_RETENCIONES" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isSectorPrimarioModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "SECTOR_PRIMARIO" );
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
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdemisorModified()) {
				stmt.setInt( index++, dto.getIdemisor() );
			}
		
			if (dto.isRfcModified()) {
				stmt.setString( index++, dto.getRfc() );
			}
		
			if (dto.isRazonsocialModified()) {
				stmt.setString( index++, dto.getRazonsocial() );
			}
		
			if (dto.isNombrecomercialModified()) {
				stmt.setString( index++, dto.getNombrecomercial() );
			}
		
			if (dto.isRegimenfiscalModified()) {
				stmt.setString( index++, dto.getRegimenfiscal() );
			}
		
			if (dto.isEstatusModified()) {
				if (dto.isEstatusNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getEstatus() );
				}
		
			}
		
			if (dto.isRegistropatronalModified()) {
				stmt.setString( index++, dto.getRegistropatronal() );
			}
		
			if (dto.isPlantillacomprobanteModified()) {
				stmt.setString( index++, dto.getPlantillacomprobante() );
			}
		
			if (dto.isRutalogoModified()) {
				stmt.setString( index++, dto.getRutalogo() );
			}
		
			if (dto.isRutacerModified()) {
				stmt.setString( index++, dto.getRutacer() );
			}
		
			if (dto.isRutakeyModified()) {
				stmt.setString( index++, dto.getRutakey() );
			}
		
			if (dto.isEmisorpassModified()) {
				stmt.setString( index++, dto.getEmisorpass() );
			}
		
			if (dto.isPlantillanominaModified()) {
				stmt.setString( index++, dto.getPlantillanomina() );
			}
		
			if (dto.isPlantillaRetencionesModified()) {
				stmt.setString( index++, dto.getPlantillaRetenciones() );
			}
		
			if (dto.isSectorPrimarioModified()) {
				stmt.setInt( index++, dto.getSectorPrimario() );
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new EmisorDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the EMISOR table.
	 */
	public void update(EmisorPk pk, Emisor dto) throws EmisorDaoException
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
		
			if (dto.isRfcModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "RFC=?" );
				modified=true;
			}
		
			if (dto.isRazonsocialModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "RAZONSOCIAL=?" );
				modified=true;
			}
		
			if (dto.isNombrecomercialModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NOMBRECOMERCIAL=?" );
				modified=true;
			}
		
			if (dto.isRegimenfiscalModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "REGIMENFISCAL=?" );
				modified=true;
			}
		
			if (dto.isEstatusModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "ESTATUS=?" );
				modified=true;
			}
		
			if (dto.isRegistropatronalModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "REGISTROPATRONAL=?" );
				modified=true;
			}
		
			if (dto.isPlantillacomprobanteModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "PLANTILLACOMPROBANTE=?" );
				modified=true;
			}
		
			if (dto.isRutalogoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "RUTALOGO=?" );
				modified=true;
			}
		
			if (dto.isRutacerModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "RUTACER=?" );
				modified=true;
			}
		
			if (dto.isRutakeyModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "RUTAKEY=?" );
				modified=true;
			}
		
			if (dto.isEmisorpassModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "EMISORPASS=?" );
				modified=true;
			}
		
			if (dto.isPlantillanominaModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "PLANTILLANOMINA=?" );
				modified=true;
			}
		
			if (dto.isPlantillaRetencionesModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "PLANTILLA_RETENCIONES=?" );
				modified=true;
			}
		
			if (dto.isSectorPrimarioModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "SECTOR_PRIMARIO=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE IDEMISOR=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdemisorModified()) {
				stmt.setInt( index++, dto.getIdemisor() );
			}
		
			if (dto.isRfcModified()) {
				stmt.setString( index++, dto.getRfc() );
			}
		
			if (dto.isRazonsocialModified()) {
				stmt.setString( index++, dto.getRazonsocial() );
			}
		
			if (dto.isNombrecomercialModified()) {
				stmt.setString( index++, dto.getNombrecomercial() );
			}
		
			if (dto.isRegimenfiscalModified()) {
				stmt.setString( index++, dto.getRegimenfiscal() );
			}
		
			if (dto.isEstatusModified()) {
				if (dto.isEstatusNull()) {
					stmt.setNull( index++, java.sql.Types.INTEGER );
				} else {
					stmt.setInt( index++, dto.getEstatus() );
				}
		
			}
		
			if (dto.isRegistropatronalModified()) {
				stmt.setString( index++, dto.getRegistropatronal() );
			}
		
			if (dto.isPlantillacomprobanteModified()) {
				stmt.setString( index++, dto.getPlantillacomprobante() );
			}
		
			if (dto.isRutalogoModified()) {
				stmt.setString( index++, dto.getRutalogo() );
			}
		
			if (dto.isRutacerModified()) {
				stmt.setString( index++, dto.getRutacer() );
			}
		
			if (dto.isRutakeyModified()) {
				stmt.setString( index++, dto.getRutakey() );
			}
		
			if (dto.isEmisorpassModified()) {
				stmt.setString( index++, dto.getEmisorpass() );
			}
		
			if (dto.isPlantillanominaModified()) {
				stmt.setString( index++, dto.getPlantillanomina() );
			}
		
			if (dto.isPlantillaRetencionesModified()) {
				stmt.setString( index++, dto.getPlantillaRetenciones() );
			}
		
			if (dto.isSectorPrimarioModified()) {
				stmt.setInt( index++, dto.getSectorPrimario() );
			}
		
			stmt.setInt( index++, pk.getIdemisor() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new EmisorDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the EMISOR table.
	 */
	public void delete(EmisorPk pk) throws EmisorDaoException
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
			stmt.setInt( 1, pk.getIdemisor() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new EmisorDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the EMISOR table that matches the specified primary-key value.
	 */
	public Emisor findByPrimaryKey(EmisorPk pk) throws EmisorDaoException
	{
		return findByPrimaryKey( pk.getIdemisor() );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'IDEMISOR = :idemisor'.
	 */
	public Emisor findByPrimaryKey(int idemisor) throws EmisorDaoException
	{
		Emisor ret[] = findByDynamicSelect( SQL_SELECT + " WHERE IDEMISOR = ?", new Object[] {  new Integer(idemisor) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria ''.
	 */
	public Emisor[] findAll() throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY IDEMISOR", null );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'IDEMISOR = :idemisor'.
	 */
	public Emisor[] findWhereIdemisorEquals(int idemisor) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IDEMISOR = ? ORDER BY IDEMISOR", new Object[] {  new Integer(idemisor) } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'RFC = :rfc'.
	 */
	public Emisor[] findWhereRfcEquals(String rfc) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE RFC = ? ORDER BY RFC", new Object[] { rfc } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'RAZONSOCIAL = :razonsocial'.
	 */
	public Emisor[] findWhereRazonsocialEquals(String razonsocial) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE RAZONSOCIAL = ? ORDER BY RAZONSOCIAL", new Object[] { razonsocial } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'NOMBRECOMERCIAL = :nombrecomercial'.
	 */
	public Emisor[] findWhereNombrecomercialEquals(String nombrecomercial) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NOMBRECOMERCIAL = ? ORDER BY NOMBRECOMERCIAL", new Object[] { nombrecomercial } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'REGIMENFISCAL = :regimenfiscal'.
	 */
	public Emisor[] findWhereRegimenfiscalEquals(String regimenfiscal) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE REGIMENFISCAL = ? ORDER BY REGIMENFISCAL", new Object[] { regimenfiscal } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'ESTATUS = :estatus'.
	 */
	public Emisor[] findWhereEstatusEquals(int estatus) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE ESTATUS = ? ORDER BY ESTATUS", new Object[] {  new Integer(estatus) } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'REGISTROPATRONAL = :registropatronal'.
	 */
	public Emisor[] findWhereRegistropatronalEquals(String registropatronal) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE REGISTROPATRONAL = ? ORDER BY REGISTROPATRONAL", new Object[] { registropatronal } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'PLANTILLACOMPROBANTE = :plantillacomprobante'.
	 */
	public Emisor[] findWherePlantillacomprobanteEquals(String plantillacomprobante) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE PLANTILLACOMPROBANTE = ? ORDER BY PLANTILLACOMPROBANTE", new Object[] { plantillacomprobante } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'RUTALOGO = :rutalogo'.
	 */
	public Emisor[] findWhereRutalogoEquals(String rutalogo) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE RUTALOGO = ? ORDER BY RUTALOGO", new Object[] { rutalogo } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'RUTACER = :rutacer'.
	 */
	public Emisor[] findWhereRutacerEquals(String rutacer) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE RUTACER = ? ORDER BY RUTACER", new Object[] { rutacer } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'RUTAKEY = :rutakey'.
	 */
	public Emisor[] findWhereRutakeyEquals(String rutakey) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE RUTAKEY = ? ORDER BY RUTAKEY", new Object[] { rutakey } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'EMISORPASS = :emisorpass'.
	 */
	public Emisor[] findWhereEmisorpassEquals(String emisorpass) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE EMISORPASS = ? ORDER BY EMISORPASS", new Object[] { emisorpass } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'PLANTILLANOMINA = :plantillanomina'.
	 */
	public Emisor[] findWherePlantillanominaEquals(String plantillanomina) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE PLANTILLANOMINA = ? ORDER BY PLANTILLANOMINA", new Object[] { plantillanomina } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'PLANTILLA_RETENCIONES = :plantillaRetenciones'.
	 */
	public Emisor[] findWherePlantillaRetencionesEquals(String plantillaRetenciones) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE PLANTILLA_RETENCIONES = ? ORDER BY PLANTILLA_RETENCIONES", new Object[] { plantillaRetenciones } );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the criteria 'SECTOR_PRIMARIO = :sectorPrimario'.
	 */
	public Emisor[] findWhereSectorPrimarioEquals(int sectorPrimario) throws EmisorDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE SECTOR_PRIMARIO = ? ORDER BY SECTOR_PRIMARIO", new Object[] {  new Integer(sectorPrimario) } );
	}

	/**
	 * Method 'EmisorDaoImpl'
	 * 
	 */
	public EmisorDaoImpl()
	{
	}

	/**
	 * Method 'EmisorDaoImpl'
	 * 
	 * @param userConn
	 */
	public EmisorDaoImpl(final java.sql.Connection userConn)
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
		return "EMISOR";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected Emisor fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			Emisor dto = new Emisor();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected Emisor[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			Emisor dto = new Emisor();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		Emisor ret[] = new Emisor[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(Emisor dto, ResultSet rs) throws SQLException
	{
		dto.setIdemisor( rs.getInt( COLUMN_IDEMISOR ) );
		dto.setRfc( rs.getString( COLUMN_RFC ) );
		dto.setRazonsocial( rs.getString( COLUMN_RAZONSOCIAL ) );
		dto.setNombrecomercial( rs.getString( COLUMN_NOMBRECOMERCIAL ) );
		dto.setRegimenfiscal( rs.getString( COLUMN_REGIMENFISCAL ) );
		dto.setEstatus( rs.getInt( COLUMN_ESTATUS ) );
		if (rs.wasNull()) {
			dto.setEstatusNull( true );
		}
		
		dto.setRegistropatronal( rs.getString( COLUMN_REGISTROPATRONAL ) );
		dto.setPlantillacomprobante( rs.getString( COLUMN_PLANTILLACOMPROBANTE ) );
		dto.setRutalogo( rs.getString( COLUMN_RUTALOGO ) );
		dto.setRutacer( rs.getString( COLUMN_RUTACER ) );
		dto.setRutakey( rs.getString( COLUMN_RUTAKEY ) );
		dto.setEmisorpass( rs.getString( COLUMN_EMISORPASS ) );
		dto.setPlantillanomina( rs.getString( COLUMN_PLANTILLANOMINA ) );
		dto.setPlantillaRetenciones( rs.getString( COLUMN_PLANTILLA_RETENCIONES ) );
		dto.setSectorPrimario( rs.getInt( COLUMN_SECTOR_PRIMARIO ) );
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(Emisor dto)
	{
		dto.setIdemisorModified( false );
		dto.setRfcModified( false );
		dto.setRazonsocialModified( false );
		dto.setNombrecomercialModified( false );
		dto.setRegimenfiscalModified( false );
		dto.setEstatusModified( false );
		dto.setRegistropatronalModified( false );
		dto.setPlantillacomprobanteModified( false );
		dto.setRutalogoModified( false );
		dto.setRutacerModified( false );
		dto.setRutakeyModified( false );
		dto.setEmisorpassModified( false );
		dto.setPlantillanominaModified( false );
		dto.setPlantillaRetencionesModified( false );
		dto.setSectorPrimarioModified( false );
	}

	/** 
	 * Returns all rows from the EMISOR table that match the specified arbitrary SQL statement
	 */
	public Emisor[] findByDynamicSelect(String sql, Object[] sqlParams) throws EmisorDaoException
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
			throw new EmisorDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the EMISOR table that match the specified arbitrary SQL statement
	 */
	public Emisor[] findByDynamicWhere(String sql, Object[] sqlParams) throws EmisorDaoException
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
			throw new EmisorDaoException( "Exception: " + _e.getMessage(), _e );
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
