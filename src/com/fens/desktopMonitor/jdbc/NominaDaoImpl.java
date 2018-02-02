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

public class NominaDaoImpl extends AbstractDAO implements NominaDao
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
	protected final String SQL_SELECT = "SELECT IDNOMINA, IDCOMPROBANTEFISCAL, NUM_EMPLEADO, FECHA_PAGO, FECHA_INICIAL_PAGO, FECHA_FIN_PAGO, DEPARTAMENTO, NOMBRE_EMPLEADO, PUESTO, NUM_DIAS_PAGADOS, TOTAL_PERCEPCION_EXENTAS, TOTAL_PERCEPCION_GRAVADAS, TOTAL_DEDUCCION_EXENTAS, TOTAL_DEDUCCION_GRAVADAS, TOTAL_INCAPACIDAD_DESCUENTO, TOTAL_HREXTRA_DOBLE_HR, TOTAL_HREXTRA_DOBLE_IMP, TOTAL_HREXTRA_TRIPLE_HR, TOTAL_HREXTRA_TRIPLE_IMP FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( IDCOMPROBANTEFISCAL, NUM_EMPLEADO, FECHA_PAGO, FECHA_INICIAL_PAGO, FECHA_FIN_PAGO, DEPARTAMENTO, NOMBRE_EMPLEADO, PUESTO, NUM_DIAS_PAGADOS, TOTAL_PERCEPCION_EXENTAS, TOTAL_PERCEPCION_GRAVADAS, TOTAL_DEDUCCION_EXENTAS, TOTAL_DEDUCCION_GRAVADAS, TOTAL_INCAPACIDAD_DESCUENTO, TOTAL_HREXTRA_DOBLE_HR, TOTAL_HREXTRA_DOBLE_IMP, TOTAL_HREXTRA_TRIPLE_HR, TOTAL_HREXTRA_TRIPLE_IMP ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET IDCOMPROBANTEFISCAL = ?, NUM_EMPLEADO = ?, FECHA_PAGO = ?, FECHA_INICIAL_PAGO = ?, FECHA_FIN_PAGO = ?, DEPARTAMENTO = ?, NOMBRE_EMPLEADO = ?, PUESTO = ?, NUM_DIAS_PAGADOS = ?, TOTAL_PERCEPCION_EXENTAS = ?, TOTAL_PERCEPCION_GRAVADAS = ?, TOTAL_DEDUCCION_EXENTAS = ?, TOTAL_DEDUCCION_GRAVADAS = ?, TOTAL_INCAPACIDAD_DESCUENTO = ?, TOTAL_HREXTRA_DOBLE_HR = ?, TOTAL_HREXTRA_DOBLE_IMP = ?, TOTAL_HREXTRA_TRIPLE_HR = ?, TOTAL_HREXTRA_TRIPLE_IMP = ? WHERE IDNOMINA = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE IDNOMINA = ?";

	/** 
	 * Index of column IDNOMINA
	 */
	protected static final int COLUMN_IDNOMINA = 1;

	/** 
	 * Index of column IDCOMPROBANTEFISCAL
	 */
	protected static final int COLUMN_IDCOMPROBANTEFISCAL = 2;

	/** 
	 * Index of column NUM_EMPLEADO
	 */
	protected static final int COLUMN_NUM_EMPLEADO = 3;

	/** 
	 * Index of column FECHA_PAGO
	 */
	protected static final int COLUMN_FECHA_PAGO = 4;

	/** 
	 * Index of column FECHA_INICIAL_PAGO
	 */
	protected static final int COLUMN_FECHA_INICIAL_PAGO = 5;

	/** 
	 * Index of column FECHA_FIN_PAGO
	 */
	protected static final int COLUMN_FECHA_FIN_PAGO = 6;

	/** 
	 * Index of column DEPARTAMENTO
	 */
	protected static final int COLUMN_DEPARTAMENTO = 7;

	/** 
	 * Index of column NOMBRE_EMPLEADO
	 */
	protected static final int COLUMN_NOMBRE_EMPLEADO = 8;

	/** 
	 * Index of column PUESTO
	 */
	protected static final int COLUMN_PUESTO = 9;

	/** 
	 * Index of column NUM_DIAS_PAGADOS
	 */
	protected static final int COLUMN_NUM_DIAS_PAGADOS = 10;

	/** 
	 * Index of column TOTAL_PERCEPCION_EXENTAS
	 */
	protected static final int COLUMN_TOTAL_PERCEPCION_EXENTAS = 11;

	/** 
	 * Index of column TOTAL_PERCEPCION_GRAVADAS
	 */
	protected static final int COLUMN_TOTAL_PERCEPCION_GRAVADAS = 12;

	/** 
	 * Index of column TOTAL_DEDUCCION_EXENTAS
	 */
	protected static final int COLUMN_TOTAL_DEDUCCION_EXENTAS = 13;

	/** 
	 * Index of column TOTAL_DEDUCCION_GRAVADAS
	 */
	protected static final int COLUMN_TOTAL_DEDUCCION_GRAVADAS = 14;

	/** 
	 * Index of column TOTAL_INCAPACIDAD_DESCUENTO
	 */
	protected static final int COLUMN_TOTAL_INCAPACIDAD_DESCUENTO = 15;

	/** 
	 * Index of column TOTAL_HREXTRA_DOBLE_HR
	 */
	protected static final int COLUMN_TOTAL_HREXTRA_DOBLE_HR = 16;

	/** 
	 * Index of column TOTAL_HREXTRA_DOBLE_IMP
	 */
	protected static final int COLUMN_TOTAL_HREXTRA_DOBLE_IMP = 17;

	/** 
	 * Index of column TOTAL_HREXTRA_TRIPLE_HR
	 */
	protected static final int COLUMN_TOTAL_HREXTRA_TRIPLE_HR = 18;

	/** 
	 * Index of column TOTAL_HREXTRA_TRIPLE_IMP
	 */
	protected static final int COLUMN_TOTAL_HREXTRA_TRIPLE_IMP = 19;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 19;

	/** 
	 * Index of primary-key column IDNOMINA
	 */
	protected static final int PK_COLUMN_IDNOMINA = 1;

	/** 
	 * Inserts a new row in the NOMINA table.
	 */
	public NominaPk insert(Nomina dto) throws NominaDaoException
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
			if (dto.isIdcomprobantefiscalModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "IDCOMPROBANTEFISCAL" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNumEmpleadoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NUM_EMPLEADO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isFechaPagoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "FECHA_PAGO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isFechaInicialPagoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "FECHA_INICIAL_PAGO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isFechaFinPagoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "FECHA_FIN_PAGO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isDepartamentoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "DEPARTAMENTO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNombreEmpleadoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NOMBRE_EMPLEADO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isPuestoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "PUESTO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isNumDiasPagadosModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "NUM_DIAS_PAGADOS" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isTotalPercepcionExentasModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "TOTAL_PERCEPCION_EXENTAS" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isTotalPercepcionGravadasModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "TOTAL_PERCEPCION_GRAVADAS" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isTotalDeduccionExentasModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "TOTAL_DEDUCCION_EXENTAS" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isTotalDeduccionGravadasModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "TOTAL_DEDUCCION_GRAVADAS" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isTotalIncapacidadDescuentoModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "TOTAL_INCAPACIDAD_DESCUENTO" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isTotalHrextraDobleHrModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "TOTAL_HREXTRA_DOBLE_HR" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isTotalHrextraDobleImpModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "TOTAL_HREXTRA_DOBLE_IMP" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isTotalHrextraTripleHrModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "TOTAL_HREXTRA_TRIPLE_HR" );
				values.append( "?" );
				modifiedCount++;
			}
		
			if (dto.isTotalHrextraTripleImpModified()) {
				if (modifiedCount>0) {
					sql.append( ", " );
					values.append( ", " );
				}
		
				sql.append( "TOTAL_HREXTRA_TRIPLE_IMP" );
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
			if (dto.isIdcomprobantefiscalModified()) {
				stmt.setInt( index++, dto.getIdcomprobantefiscal() );
			}
		
			if (dto.isNumEmpleadoModified()) {
				stmt.setString( index++, dto.getNumEmpleado() );
			}
		
			if (dto.isFechaPagoModified()) {
				stmt.setTimestamp(index++, dto.getFechaPago()==null ? null : new java.sql.Timestamp( dto.getFechaPago().getTime() ) );
			}
		
			if (dto.isFechaInicialPagoModified()) {
				stmt.setTimestamp(index++, dto.getFechaInicialPago()==null ? null : new java.sql.Timestamp( dto.getFechaInicialPago().getTime() ) );
			}
		
			if (dto.isFechaFinPagoModified()) {
				stmt.setTimestamp(index++, dto.getFechaFinPago()==null ? null : new java.sql.Timestamp( dto.getFechaFinPago().getTime() ) );
			}
		
			if (dto.isDepartamentoModified()) {
				stmt.setString( index++, dto.getDepartamento() );
			}
		
			if (dto.isNombreEmpleadoModified()) {
				stmt.setString( index++, dto.getNombreEmpleado() );
			}
		
			if (dto.isPuestoModified()) {
				stmt.setString( index++, dto.getPuesto() );
			}
		
			if (dto.isNumDiasPagadosModified()) {
				if (dto.isNumDiasPagadosNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getNumDiasPagados() );
				}
		
			}
		
			if (dto.isTotalPercepcionExentasModified()) {
				if (dto.isTotalPercepcionExentasNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalPercepcionExentas() );
				}
		
			}
		
			if (dto.isTotalPercepcionGravadasModified()) {
				if (dto.isTotalPercepcionGravadasNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalPercepcionGravadas() );
				}
		
			}
		
			if (dto.isTotalDeduccionExentasModified()) {
				if (dto.isTotalDeduccionExentasNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalDeduccionExentas() );
				}
		
			}
		
			if (dto.isTotalDeduccionGravadasModified()) {
				if (dto.isTotalDeduccionGravadasNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalDeduccionGravadas() );
				}
		
			}
		
			if (dto.isTotalIncapacidadDescuentoModified()) {
				if (dto.isTotalIncapacidadDescuentoNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalIncapacidadDescuento() );
				}
		
			}
		
			if (dto.isTotalHrextraDobleHrModified()) {
				if (dto.isTotalHrextraDobleHrNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalHrextraDobleHr() );
				}
		
			}
		
			if (dto.isTotalHrextraDobleImpModified()) {
				if (dto.isTotalHrextraDobleImpNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalHrextraDobleImp() );
				}
		
			}
		
			if (dto.isTotalHrextraTripleHrModified()) {
				if (dto.isTotalHrextraTripleHrNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalHrextraTripleHr() );
				}
		
			}
		
			if (dto.isTotalHrextraTripleImpModified()) {
				if (dto.isTotalHrextraTripleImpNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalHrextraTripleImp() );
				}
		
			}
		
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setIdnomina( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new NominaDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the NOMINA table.
	 */
	public void update(NominaPk pk, Nomina dto) throws NominaDaoException
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
			if (dto.isIdcomprobantefiscalModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "IDCOMPROBANTEFISCAL=?" );
				modified=true;
			}
		
			if (dto.isNumEmpleadoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NUM_EMPLEADO=?" );
				modified=true;
			}
		
			if (dto.isFechaPagoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "FECHA_PAGO=?" );
				modified=true;
			}
		
			if (dto.isFechaInicialPagoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "FECHA_INICIAL_PAGO=?" );
				modified=true;
			}
		
			if (dto.isFechaFinPagoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "FECHA_FIN_PAGO=?" );
				modified=true;
			}
		
			if (dto.isDepartamentoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "DEPARTAMENTO=?" );
				modified=true;
			}
		
			if (dto.isNombreEmpleadoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NOMBRE_EMPLEADO=?" );
				modified=true;
			}
		
			if (dto.isPuestoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "PUESTO=?" );
				modified=true;
			}
		
			if (dto.isNumDiasPagadosModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "NUM_DIAS_PAGADOS=?" );
				modified=true;
			}
		
			if (dto.isTotalPercepcionExentasModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "TOTAL_PERCEPCION_EXENTAS=?" );
				modified=true;
			}
		
			if (dto.isTotalPercepcionGravadasModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "TOTAL_PERCEPCION_GRAVADAS=?" );
				modified=true;
			}
		
			if (dto.isTotalDeduccionExentasModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "TOTAL_DEDUCCION_EXENTAS=?" );
				modified=true;
			}
		
			if (dto.isTotalDeduccionGravadasModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "TOTAL_DEDUCCION_GRAVADAS=?" );
				modified=true;
			}
		
			if (dto.isTotalIncapacidadDescuentoModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "TOTAL_INCAPACIDAD_DESCUENTO=?" );
				modified=true;
			}
		
			if (dto.isTotalHrextraDobleHrModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "TOTAL_HREXTRA_DOBLE_HR=?" );
				modified=true;
			}
		
			if (dto.isTotalHrextraDobleImpModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "TOTAL_HREXTRA_DOBLE_IMP=?" );
				modified=true;
			}
		
			if (dto.isTotalHrextraTripleHrModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "TOTAL_HREXTRA_TRIPLE_HR=?" );
				modified=true;
			}
		
			if (dto.isTotalHrextraTripleImpModified()) {
				if (modified) {
					sql.append( ", " );
				}
		
				sql.append( "TOTAL_HREXTRA_TRIPLE_IMP=?" );
				modified=true;
			}
		
			if (!modified) {
				// nothing to update
				return;
			}
		
			sql.append( " WHERE IDNOMINA=?" );
			System.out.println( "Executing " + sql.toString() + " with values: " + dto );
			stmt = conn.prepareStatement( sql.toString() );
			int index = 1;
			if (dto.isIdnominaModified()) {
				stmt.setInt( index++, dto.getIdnomina() );
			}
		
			if (dto.isIdcomprobantefiscalModified()) {
				stmt.setInt( index++, dto.getIdcomprobantefiscal() );
			}
		
			if (dto.isNumEmpleadoModified()) {
				stmt.setString( index++, dto.getNumEmpleado() );
			}
		
			if (dto.isFechaPagoModified()) {
				stmt.setTimestamp(index++, dto.getFechaPago()==null ? null : new java.sql.Timestamp( dto.getFechaPago().getTime() ) );
			}
		
			if (dto.isFechaInicialPagoModified()) {
				stmt.setTimestamp(index++, dto.getFechaInicialPago()==null ? null : new java.sql.Timestamp( dto.getFechaInicialPago().getTime() ) );
			}
		
			if (dto.isFechaFinPagoModified()) {
				stmt.setTimestamp(index++, dto.getFechaFinPago()==null ? null : new java.sql.Timestamp( dto.getFechaFinPago().getTime() ) );
			}
		
			if (dto.isDepartamentoModified()) {
				stmt.setString( index++, dto.getDepartamento() );
			}
		
			if (dto.isNombreEmpleadoModified()) {
				stmt.setString( index++, dto.getNombreEmpleado() );
			}
		
			if (dto.isPuestoModified()) {
				stmt.setString( index++, dto.getPuesto() );
			}
		
			if (dto.isNumDiasPagadosModified()) {
				if (dto.isNumDiasPagadosNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getNumDiasPagados() );
				}
		
			}
		
			if (dto.isTotalPercepcionExentasModified()) {
				if (dto.isTotalPercepcionExentasNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalPercepcionExentas() );
				}
		
			}
		
			if (dto.isTotalPercepcionGravadasModified()) {
				if (dto.isTotalPercepcionGravadasNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalPercepcionGravadas() );
				}
		
			}
		
			if (dto.isTotalDeduccionExentasModified()) {
				if (dto.isTotalDeduccionExentasNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalDeduccionExentas() );
				}
		
			}
		
			if (dto.isTotalDeduccionGravadasModified()) {
				if (dto.isTotalDeduccionGravadasNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalDeduccionGravadas() );
				}
		
			}
		
			if (dto.isTotalIncapacidadDescuentoModified()) {
				if (dto.isTotalIncapacidadDescuentoNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalIncapacidadDescuento() );
				}
		
			}
		
			if (dto.isTotalHrextraDobleHrModified()) {
				if (dto.isTotalHrextraDobleHrNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalHrextraDobleHr() );
				}
		
			}
		
			if (dto.isTotalHrextraDobleImpModified()) {
				if (dto.isTotalHrextraDobleImpNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalHrextraDobleImp() );
				}
		
			}
		
			if (dto.isTotalHrextraTripleHrModified()) {
				if (dto.isTotalHrextraTripleHrNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalHrextraTripleHr() );
				}
		
			}
		
			if (dto.isTotalHrextraTripleImpModified()) {
				if (dto.isTotalHrextraTripleImpNull()) {
					stmt.setNull( index++, java.sql.Types.DOUBLE );
				} else {
					stmt.setDouble( index++, dto.getTotalHrextraTripleImp() );
				}
		
			}
		
			stmt.setInt( index++, pk.getIdnomina() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new NominaDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the NOMINA table.
	 */
	public void delete(NominaPk pk) throws NominaDaoException
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
			stmt.setInt( 1, pk.getIdnomina() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new NominaDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the NOMINA table that matches the specified primary-key value.
	 */
	public Nomina findByPrimaryKey(NominaPk pk) throws NominaDaoException
	{
		return findByPrimaryKey( pk.getIdnomina() );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'IDNOMINA = :idnomina'.
	 */
	public Nomina findByPrimaryKey(int idnomina) throws NominaDaoException
	{
		Nomina ret[] = findByDynamicSelect( SQL_SELECT + " WHERE IDNOMINA = ?", new Object[] {  new Integer(idnomina) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria ''.
	 */
	public Nomina[] findAll() throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY IDNOMINA", null );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'IDNOMINA = :idnomina'.
	 */
	public Nomina[] findWhereIdnominaEquals(int idnomina) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IDNOMINA = ? ORDER BY IDNOMINA", new Object[] {  new Integer(idnomina) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'IDCOMPROBANTEFISCAL = :idcomprobantefiscal'.
	 */
	public Nomina[] findWhereIdcomprobantefiscalEquals(int idcomprobantefiscal) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE IDCOMPROBANTEFISCAL = ? ORDER BY IDCOMPROBANTEFISCAL", new Object[] {  new Integer(idcomprobantefiscal) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'NUM_EMPLEADO = :numEmpleado'.
	 */
	public Nomina[] findWhereNumEmpleadoEquals(String numEmpleado) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NUM_EMPLEADO = ? ORDER BY NUM_EMPLEADO", new Object[] { numEmpleado } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'FECHA_PAGO = :fechaPago'.
	 */
	public Nomina[] findWhereFechaPagoEquals(Date fechaPago) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE FECHA_PAGO = ? ORDER BY FECHA_PAGO", new Object[] { fechaPago==null ? null : new java.sql.Timestamp( fechaPago.getTime() ) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'FECHA_INICIAL_PAGO = :fechaInicialPago'.
	 */
	public Nomina[] findWhereFechaInicialPagoEquals(Date fechaInicialPago) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE FECHA_INICIAL_PAGO = ? ORDER BY FECHA_INICIAL_PAGO", new Object[] { fechaInicialPago==null ? null : new java.sql.Timestamp( fechaInicialPago.getTime() ) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'FECHA_FIN_PAGO = :fechaFinPago'.
	 */
	public Nomina[] findWhereFechaFinPagoEquals(Date fechaFinPago) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE FECHA_FIN_PAGO = ? ORDER BY FECHA_FIN_PAGO", new Object[] { fechaFinPago==null ? null : new java.sql.Timestamp( fechaFinPago.getTime() ) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'DEPARTAMENTO = :departamento'.
	 */
	public Nomina[] findWhereDepartamentoEquals(String departamento) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE DEPARTAMENTO = ? ORDER BY DEPARTAMENTO", new Object[] { departamento } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'NOMBRE_EMPLEADO = :nombreEmpleado'.
	 */
	public Nomina[] findWhereNombreEmpleadoEquals(String nombreEmpleado) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NOMBRE_EMPLEADO = ? ORDER BY NOMBRE_EMPLEADO", new Object[] { nombreEmpleado } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'PUESTO = :puesto'.
	 */
	public Nomina[] findWherePuestoEquals(String puesto) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE PUESTO = ? ORDER BY PUESTO", new Object[] { puesto } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'NUM_DIAS_PAGADOS = :numDiasPagados'.
	 */
	public Nomina[] findWhereNumDiasPagadosEquals(double numDiasPagados) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE NUM_DIAS_PAGADOS = ? ORDER BY NUM_DIAS_PAGADOS", new Object[] {  new Double(numDiasPagados) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'TOTAL_PERCEPCION_EXENTAS = :totalPercepcionExentas'.
	 */
	public Nomina[] findWhereTotalPercepcionExentasEquals(double totalPercepcionExentas) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE TOTAL_PERCEPCION_EXENTAS = ? ORDER BY TOTAL_PERCEPCION_EXENTAS", new Object[] {  new Double(totalPercepcionExentas) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'TOTAL_PERCEPCION_GRAVADAS = :totalPercepcionGravadas'.
	 */
	public Nomina[] findWhereTotalPercepcionGravadasEquals(double totalPercepcionGravadas) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE TOTAL_PERCEPCION_GRAVADAS = ? ORDER BY TOTAL_PERCEPCION_GRAVADAS", new Object[] {  new Double(totalPercepcionGravadas) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'TOTAL_DEDUCCION_EXENTAS = :totalDeduccionExentas'.
	 */
	public Nomina[] findWhereTotalDeduccionExentasEquals(double totalDeduccionExentas) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE TOTAL_DEDUCCION_EXENTAS = ? ORDER BY TOTAL_DEDUCCION_EXENTAS", new Object[] {  new Double(totalDeduccionExentas) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'TOTAL_DEDUCCION_GRAVADAS = :totalDeduccionGravadas'.
	 */
	public Nomina[] findWhereTotalDeduccionGravadasEquals(double totalDeduccionGravadas) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE TOTAL_DEDUCCION_GRAVADAS = ? ORDER BY TOTAL_DEDUCCION_GRAVADAS", new Object[] {  new Double(totalDeduccionGravadas) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'TOTAL_INCAPACIDAD_DESCUENTO = :totalIncapacidadDescuento'.
	 */
	public Nomina[] findWhereTotalIncapacidadDescuentoEquals(double totalIncapacidadDescuento) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE TOTAL_INCAPACIDAD_DESCUENTO = ? ORDER BY TOTAL_INCAPACIDAD_DESCUENTO", new Object[] {  new Double(totalIncapacidadDescuento) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'TOTAL_HREXTRA_DOBLE_HR = :totalHrextraDobleHr'.
	 */
	public Nomina[] findWhereTotalHrextraDobleHrEquals(double totalHrextraDobleHr) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE TOTAL_HREXTRA_DOBLE_HR = ? ORDER BY TOTAL_HREXTRA_DOBLE_HR", new Object[] {  new Double(totalHrextraDobleHr) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'TOTAL_HREXTRA_DOBLE_IMP = :totalHrextraDobleImp'.
	 */
	public Nomina[] findWhereTotalHrextraDobleImpEquals(double totalHrextraDobleImp) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE TOTAL_HREXTRA_DOBLE_IMP = ? ORDER BY TOTAL_HREXTRA_DOBLE_IMP", new Object[] {  new Double(totalHrextraDobleImp) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'TOTAL_HREXTRA_TRIPLE_HR = :totalHrextraTripleHr'.
	 */
	public Nomina[] findWhereTotalHrextraTripleHrEquals(double totalHrextraTripleHr) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE TOTAL_HREXTRA_TRIPLE_HR = ? ORDER BY TOTAL_HREXTRA_TRIPLE_HR", new Object[] {  new Double(totalHrextraTripleHr) } );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the criteria 'TOTAL_HREXTRA_TRIPLE_IMP = :totalHrextraTripleImp'.
	 */
	public Nomina[] findWhereTotalHrextraTripleImpEquals(double totalHrextraTripleImp) throws NominaDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE TOTAL_HREXTRA_TRIPLE_IMP = ? ORDER BY TOTAL_HREXTRA_TRIPLE_IMP", new Object[] {  new Double(totalHrextraTripleImp) } );
	}

	/**
	 * Method 'NominaDaoImpl'
	 * 
	 */
	public NominaDaoImpl()
	{
	}

	/**
	 * Method 'NominaDaoImpl'
	 * 
	 * @param userConn
	 */
	public NominaDaoImpl(final java.sql.Connection userConn)
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
		return "NOMINA";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected Nomina fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			Nomina dto = new Nomina();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected Nomina[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			Nomina dto = new Nomina();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		Nomina ret[] = new Nomina[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(Nomina dto, ResultSet rs) throws SQLException
	{
		dto.setIdnomina( rs.getInt( COLUMN_IDNOMINA ) );
		dto.setIdcomprobantefiscal( rs.getInt( COLUMN_IDCOMPROBANTEFISCAL ) );
		dto.setNumEmpleado( rs.getString( COLUMN_NUM_EMPLEADO ) );
		dto.setFechaPago( rs.getTimestamp(COLUMN_FECHA_PAGO ) );
		dto.setFechaInicialPago( rs.getTimestamp(COLUMN_FECHA_INICIAL_PAGO ) );
		dto.setFechaFinPago( rs.getTimestamp(COLUMN_FECHA_FIN_PAGO ) );
		dto.setDepartamento( rs.getString( COLUMN_DEPARTAMENTO ) );
		dto.setNombreEmpleado( rs.getString( COLUMN_NOMBRE_EMPLEADO ) );
		dto.setPuesto( rs.getString( COLUMN_PUESTO ) );
		dto.setNumDiasPagados( rs.getDouble( COLUMN_NUM_DIAS_PAGADOS ) );
		if (rs.wasNull()) {
			dto.setNumDiasPagadosNull( true );
		}
		
		dto.setTotalPercepcionExentas( rs.getDouble( COLUMN_TOTAL_PERCEPCION_EXENTAS ) );
		if (rs.wasNull()) {
			dto.setTotalPercepcionExentasNull( true );
		}
		
		dto.setTotalPercepcionGravadas( rs.getDouble( COLUMN_TOTAL_PERCEPCION_GRAVADAS ) );
		if (rs.wasNull()) {
			dto.setTotalPercepcionGravadasNull( true );
		}
		
		dto.setTotalDeduccionExentas( rs.getDouble( COLUMN_TOTAL_DEDUCCION_EXENTAS ) );
		if (rs.wasNull()) {
			dto.setTotalDeduccionExentasNull( true );
		}
		
		dto.setTotalDeduccionGravadas( rs.getDouble( COLUMN_TOTAL_DEDUCCION_GRAVADAS ) );
		if (rs.wasNull()) {
			dto.setTotalDeduccionGravadasNull( true );
		}
		
		dto.setTotalIncapacidadDescuento( rs.getDouble( COLUMN_TOTAL_INCAPACIDAD_DESCUENTO ) );
		if (rs.wasNull()) {
			dto.setTotalIncapacidadDescuentoNull( true );
		}
		
		dto.setTotalHrextraDobleHr( rs.getDouble( COLUMN_TOTAL_HREXTRA_DOBLE_HR ) );
		if (rs.wasNull()) {
			dto.setTotalHrextraDobleHrNull( true );
		}
		
		dto.setTotalHrextraDobleImp( rs.getDouble( COLUMN_TOTAL_HREXTRA_DOBLE_IMP ) );
		if (rs.wasNull()) {
			dto.setTotalHrextraDobleImpNull( true );
		}
		
		dto.setTotalHrextraTripleHr( rs.getDouble( COLUMN_TOTAL_HREXTRA_TRIPLE_HR ) );
		if (rs.wasNull()) {
			dto.setTotalHrextraTripleHrNull( true );
		}
		
		dto.setTotalHrextraTripleImp( rs.getDouble( COLUMN_TOTAL_HREXTRA_TRIPLE_IMP ) );
		if (rs.wasNull()) {
			dto.setTotalHrextraTripleImpNull( true );
		}
		
		reset(dto);
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(Nomina dto)
	{
		dto.setIdnominaModified( false );
		dto.setIdcomprobantefiscalModified( false );
		dto.setNumEmpleadoModified( false );
		dto.setFechaPagoModified( false );
		dto.setFechaInicialPagoModified( false );
		dto.setFechaFinPagoModified( false );
		dto.setDepartamentoModified( false );
		dto.setNombreEmpleadoModified( false );
		dto.setPuestoModified( false );
		dto.setNumDiasPagadosModified( false );
		dto.setTotalPercepcionExentasModified( false );
		dto.setTotalPercepcionGravadasModified( false );
		dto.setTotalDeduccionExentasModified( false );
		dto.setTotalDeduccionGravadasModified( false );
		dto.setTotalIncapacidadDescuentoModified( false );
		dto.setTotalHrextraDobleHrModified( false );
		dto.setTotalHrextraDobleImpModified( false );
		dto.setTotalHrextraTripleHrModified( false );
		dto.setTotalHrextraTripleImpModified( false );
	}

	/** 
	 * Returns all rows from the NOMINA table that match the specified arbitrary SQL statement
	 */
	public Nomina[] findByDynamicSelect(String sql, Object[] sqlParams) throws NominaDaoException
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
			throw new NominaDaoException( "Exception: " + _e.getMessage(), _e );
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
	 * Returns all rows from the NOMINA table that match the specified arbitrary SQL statement
	 */
	public Nomina[] findByDynamicWhere(String sql, Object[] sqlParams) throws NominaDaoException
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
			throw new NominaDaoException( "Exception: " + _e.getMessage(), _e );
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