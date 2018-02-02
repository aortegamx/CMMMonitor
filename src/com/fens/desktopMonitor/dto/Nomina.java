/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.fens.desktopMonitor.dto;

import java.io.Serializable;
import java.util.Date;

public class Nomina implements Serializable
{

    private static final long serialVersionUID = 1L;
	/** 
	 * This attribute maps to the column IDNOMINA in the NOMINA table.
	 */
	protected int idnomina;

	/** 
	 * This attribute represents whether the attribute idnomina has been modified since being read from the database.
	 */
	protected boolean idnominaModified = false;

	/** 
	 * This attribute maps to the column IDCOMPROBANTEFISCAL in the NOMINA table.
	 */
	protected int idcomprobantefiscal;

	/** 
	 * This attribute represents whether the attribute idcomprobantefiscal has been modified since being read from the database.
	 */
	protected boolean idcomprobantefiscalModified = false;

	/** 
	 * This attribute maps to the column NUM_EMPLEADO in the NOMINA table.
	 */
	protected String numEmpleado;

	/** 
	 * This attribute represents whether the attribute numEmpleado has been modified since being read from the database.
	 */
	protected boolean numEmpleadoModified = false;

	/** 
	 * This attribute maps to the column FECHA_PAGO in the NOMINA table.
	 */
	protected Date fechaPago;

	/** 
	 * This attribute represents whether the attribute fechaPago has been modified since being read from the database.
	 */
	protected boolean fechaPagoModified = false;

	/** 
	 * This attribute maps to the column FECHA_INICIAL_PAGO in the NOMINA table.
	 */
	protected Date fechaInicialPago;

	/** 
	 * This attribute represents whether the attribute fechaInicialPago has been modified since being read from the database.
	 */
	protected boolean fechaInicialPagoModified = false;

	/** 
	 * This attribute maps to the column FECHA_FIN_PAGO in the NOMINA table.
	 */
	protected Date fechaFinPago;

	/** 
	 * This attribute represents whether the attribute fechaFinPago has been modified since being read from the database.
	 */
	protected boolean fechaFinPagoModified = false;

	/** 
	 * This attribute maps to the column DEPARTAMENTO in the NOMINA table.
	 */
	protected String departamento;

	/** 
	 * This attribute represents whether the attribute departamento has been modified since being read from the database.
	 */
	protected boolean departamentoModified = false;

	/** 
	 * This attribute maps to the column NOMBRE_EMPLEADO in the NOMINA table.
	 */
	protected String nombreEmpleado;

	/** 
	 * This attribute represents whether the attribute nombreEmpleado has been modified since being read from the database.
	 */
	protected boolean nombreEmpleadoModified = false;

	/** 
	 * This attribute maps to the column PUESTO in the NOMINA table.
	 */
	protected String puesto;

	/** 
	 * This attribute represents whether the attribute puesto has been modified since being read from the database.
	 */
	protected boolean puestoModified = false;

	/** 
	 * This attribute maps to the column NUM_DIAS_PAGADOS in the NOMINA table.
	 */
	protected double numDiasPagados;

	/** 
	 * This attribute represents whether the primitive attribute numDiasPagados is null.
	 */
	protected boolean numDiasPagadosNull = true;

	/** 
	 * This attribute represents whether the attribute numDiasPagados has been modified since being read from the database.
	 */
	protected boolean numDiasPagadosModified = false;

	/** 
	 * This attribute maps to the column TOTAL_PERCEPCION_EXENTAS in the NOMINA table.
	 */
	protected double totalPercepcionExentas;

	/** 
	 * This attribute represents whether the primitive attribute totalPercepcionExentas is null.
	 */
	protected boolean totalPercepcionExentasNull = true;

	/** 
	 * This attribute represents whether the attribute totalPercepcionExentas has been modified since being read from the database.
	 */
	protected boolean totalPercepcionExentasModified = false;

	/** 
	 * This attribute maps to the column TOTAL_PERCEPCION_GRAVADAS in the NOMINA table.
	 */
	protected double totalPercepcionGravadas;

	/** 
	 * This attribute represents whether the primitive attribute totalPercepcionGravadas is null.
	 */
	protected boolean totalPercepcionGravadasNull = true;

	/** 
	 * This attribute represents whether the attribute totalPercepcionGravadas has been modified since being read from the database.
	 */
	protected boolean totalPercepcionGravadasModified = false;

	/** 
	 * This attribute maps to the column TOTAL_DEDUCCION_EXENTAS in the NOMINA table.
	 */
	protected double totalDeduccionExentas;

	/** 
	 * This attribute represents whether the primitive attribute totalDeduccionExentas is null.
	 */
	protected boolean totalDeduccionExentasNull = true;

	/** 
	 * This attribute represents whether the attribute totalDeduccionExentas has been modified since being read from the database.
	 */
	protected boolean totalDeduccionExentasModified = false;

	/** 
	 * This attribute maps to the column TOTAL_DEDUCCION_GRAVADAS in the NOMINA table.
	 */
	protected double totalDeduccionGravadas;

	/** 
	 * This attribute represents whether the primitive attribute totalDeduccionGravadas is null.
	 */
	protected boolean totalDeduccionGravadasNull = true;

	/** 
	 * This attribute represents whether the attribute totalDeduccionGravadas has been modified since being read from the database.
	 */
	protected boolean totalDeduccionGravadasModified = false;

	/** 
	 * This attribute maps to the column TOTAL_INCAPACIDAD_DESCUENTO in the NOMINA table.
	 */
	protected double totalIncapacidadDescuento;

	/** 
	 * This attribute represents whether the primitive attribute totalIncapacidadDescuento is null.
	 */
	protected boolean totalIncapacidadDescuentoNull = true;

	/** 
	 * This attribute represents whether the attribute totalIncapacidadDescuento has been modified since being read from the database.
	 */
	protected boolean totalIncapacidadDescuentoModified = false;

	/** 
	 * This attribute maps to the column TOTAL_HREXTRA_DOBLE_HR in the NOMINA table.
	 */
	protected double totalHrextraDobleHr;

	/** 
	 * This attribute represents whether the primitive attribute totalHrextraDobleHr is null.
	 */
	protected boolean totalHrextraDobleHrNull = true;

	/** 
	 * This attribute represents whether the attribute totalHrextraDobleHr has been modified since being read from the database.
	 */
	protected boolean totalHrextraDobleHrModified = false;

	/** 
	 * This attribute maps to the column TOTAL_HREXTRA_DOBLE_IMP in the NOMINA table.
	 */
	protected double totalHrextraDobleImp;

	/** 
	 * This attribute represents whether the primitive attribute totalHrextraDobleImp is null.
	 */
	protected boolean totalHrextraDobleImpNull = true;

	/** 
	 * This attribute represents whether the attribute totalHrextraDobleImp has been modified since being read from the database.
	 */
	protected boolean totalHrextraDobleImpModified = false;

	/** 
	 * This attribute maps to the column TOTAL_HREXTRA_TRIPLE_HR in the NOMINA table.
	 */
	protected double totalHrextraTripleHr;

	/** 
	 * This attribute represents whether the primitive attribute totalHrextraTripleHr is null.
	 */
	protected boolean totalHrextraTripleHrNull = true;

	/** 
	 * This attribute represents whether the attribute totalHrextraTripleHr has been modified since being read from the database.
	 */
	protected boolean totalHrextraTripleHrModified = false;

	/** 
	 * This attribute maps to the column TOTAL_HREXTRA_TRIPLE_IMP in the NOMINA table.
	 */
	protected double totalHrextraTripleImp;

	/** 
	 * This attribute represents whether the primitive attribute totalHrextraTripleImp is null.
	 */
	protected boolean totalHrextraTripleImpNull = true;

	/** 
	 * This attribute represents whether the attribute totalHrextraTripleImp has been modified since being read from the database.
	 */
	protected boolean totalHrextraTripleImpModified = false;

	/**
	 * Method 'Nomina'
	 * 
	 */
	public Nomina()
	{
	}

	/**
	 * Method 'getIdnomina'
	 * 
	 * @return int
	 */
	public int getIdnomina()
	{
		return idnomina;
	}

	/**
	 * Method 'setIdnomina'
	 * 
	 * @param idnomina
	 */
	public void setIdnomina(int idnomina)
	{
		this.idnomina = idnomina;
		this.idnominaModified = true;
	}

	/** 
	 * Sets the value of idnominaModified
	 */
	public void setIdnominaModified(boolean idnominaModified)
	{
		this.idnominaModified = idnominaModified;
	}

	/** 
	 * Gets the value of idnominaModified
     * @return 
	 */
	public boolean isIdnominaModified()
	{
		return idnominaModified;
	}

	/**
	 * Method 'getIdcomprobantefiscal'
	 * 
	 * @return int
	 */
	public int getIdcomprobantefiscal()
	{
		return idcomprobantefiscal;
	}

	/**
	 * Method 'setIdcomprobantefiscal'
	 * 
	 * @param idcomprobantefiscal
	 */
	public void setIdcomprobantefiscal(int idcomprobantefiscal)
	{
		this.idcomprobantefiscal = idcomprobantefiscal;
		this.idcomprobantefiscalModified = true;
	}

	/** 
	 * Sets the value of idcomprobantefiscalModified
	 */
	public void setIdcomprobantefiscalModified(boolean idcomprobantefiscalModified)
	{
		this.idcomprobantefiscalModified = idcomprobantefiscalModified;
	}

	/** 
	 * Gets the value of idcomprobantefiscalModified
	 */
	public boolean isIdcomprobantefiscalModified()
	{
		return idcomprobantefiscalModified;
	}

	/**
	 * Method 'getNumEmpleado'
	 * 
	 * @return String
	 */
	public String getNumEmpleado()
	{
		return numEmpleado;
	}

	/**
	 * Method 'setNumEmpleado'
	 * 
	 * @param numEmpleado
	 */
	public void setNumEmpleado(String numEmpleado)
	{
		this.numEmpleado = numEmpleado;
		this.numEmpleadoModified = true;
	}

	/** 
	 * Sets the value of numEmpleadoModified
	 */
	public void setNumEmpleadoModified(boolean numEmpleadoModified)
	{
		this.numEmpleadoModified = numEmpleadoModified;
	}

	/** 
	 * Gets the value of numEmpleadoModified
	 */
	public boolean isNumEmpleadoModified()
	{
		return numEmpleadoModified;
	}

	/**
	 * Method 'getFechaPago'
	 * 
	 * @return Date
	 */
	public Date getFechaPago()
	{
		return fechaPago;
	}

	/**
	 * Method 'setFechaPago'
	 * 
	 * @param fechaPago
	 */
	public void setFechaPago(Date fechaPago)
	{
		this.fechaPago = fechaPago;
		this.fechaPagoModified = true;
	}

	/** 
	 * Sets the value of fechaPagoModified
	 */
	public void setFechaPagoModified(boolean fechaPagoModified)
	{
		this.fechaPagoModified = fechaPagoModified;
	}

	/** 
	 * Gets the value of fechaPagoModified
	 */
	public boolean isFechaPagoModified()
	{
		return fechaPagoModified;
	}

	/**
	 * Method 'getFechaInicialPago'
	 * 
	 * @return Date
	 */
	public Date getFechaInicialPago()
	{
		return fechaInicialPago;
	}

	/**
	 * Method 'setFechaInicialPago'
	 * 
	 * @param fechaInicialPago
	 */
	public void setFechaInicialPago(Date fechaInicialPago)
	{
		this.fechaInicialPago = fechaInicialPago;
		this.fechaInicialPagoModified = true;
	}

	/** 
	 * Sets the value of fechaInicialPagoModified
	 */
	public void setFechaInicialPagoModified(boolean fechaInicialPagoModified)
	{
		this.fechaInicialPagoModified = fechaInicialPagoModified;
	}

	/** 
	 * Gets the value of fechaInicialPagoModified
	 */
	public boolean isFechaInicialPagoModified()
	{
		return fechaInicialPagoModified;
	}

	/**
	 * Method 'getFechaFinPago'
	 * 
	 * @return Date
	 */
	public Date getFechaFinPago()
	{
		return fechaFinPago;
	}

	/**
	 * Method 'setFechaFinPago'
	 * 
	 * @param fechaFinPago
	 */
	public void setFechaFinPago(Date fechaFinPago)
	{
		this.fechaFinPago = fechaFinPago;
		this.fechaFinPagoModified = true;
	}

	/** 
	 * Sets the value of fechaFinPagoModified
	 */
	public void setFechaFinPagoModified(boolean fechaFinPagoModified)
	{
		this.fechaFinPagoModified = fechaFinPagoModified;
	}

	/** 
	 * Gets the value of fechaFinPagoModified
	 */
	public boolean isFechaFinPagoModified()
	{
		return fechaFinPagoModified;
	}

	/**
	 * Method 'getDepartamento'
	 * 
	 * @return String
	 */
	public String getDepartamento()
	{
		return departamento;
	}

	/**
	 * Method 'setDepartamento'
	 * 
	 * @param departamento
	 */
	public void setDepartamento(String departamento)
	{
		this.departamento = departamento;
		this.departamentoModified = true;
	}

	/** 
	 * Sets the value of departamentoModified
	 */
	public void setDepartamentoModified(boolean departamentoModified)
	{
		this.departamentoModified = departamentoModified;
	}

	/** 
	 * Gets the value of departamentoModified
	 */
	public boolean isDepartamentoModified()
	{
		return departamentoModified;
	}

	/**
	 * Method 'getNombreEmpleado'
	 * 
	 * @return String
	 */
	public String getNombreEmpleado()
	{
		return nombreEmpleado;
	}

	/**
	 * Method 'setNombreEmpleado'
	 * 
	 * @param nombreEmpleado
	 */
	public void setNombreEmpleado(String nombreEmpleado)
	{
		this.nombreEmpleado = nombreEmpleado;
		this.nombreEmpleadoModified = true;
	}

	/** 
	 * Sets the value of nombreEmpleadoModified
	 */
	public void setNombreEmpleadoModified(boolean nombreEmpleadoModified)
	{
		this.nombreEmpleadoModified = nombreEmpleadoModified;
	}

	/** 
	 * Gets the value of nombreEmpleadoModified
	 */
	public boolean isNombreEmpleadoModified()
	{
		return nombreEmpleadoModified;
	}

	/**
	 * Method 'getPuesto'
	 * 
	 * @return String
	 */
	public String getPuesto()
	{
		return puesto;
	}

	/**
	 * Method 'setPuesto'
	 * 
	 * @param puesto
	 */
	public void setPuesto(String puesto)
	{
		this.puesto = puesto;
		this.puestoModified = true;
	}

	/** 
	 * Sets the value of puestoModified
	 */
	public void setPuestoModified(boolean puestoModified)
	{
		this.puestoModified = puestoModified;
	}

	/** 
	 * Gets the value of puestoModified
	 */
	public boolean isPuestoModified()
	{
		return puestoModified;
	}

	/**
	 * Method 'getNumDiasPagados'
	 * 
	 * @return double
	 */
	public double getNumDiasPagados()
	{
		return numDiasPagados;
	}

	/**
	 * Method 'setNumDiasPagados'
	 * 
	 * @param numDiasPagados
	 */
	public void setNumDiasPagados(double numDiasPagados)
	{
		this.numDiasPagados = numDiasPagados;
		this.numDiasPagadosNull = false;
		this.numDiasPagadosModified = true;
	}

	/**
	 * Method 'setNumDiasPagadosNull'
	 * 
	 * @param value
	 */
	public void setNumDiasPagadosNull(boolean value)
	{
		this.numDiasPagadosNull = value;
		this.numDiasPagadosModified = true;
	}

	/**
	 * Method 'isNumDiasPagadosNull'
	 * 
	 * @return boolean
	 */
	public boolean isNumDiasPagadosNull()
	{
		return numDiasPagadosNull;
	}

	/** 
	 * Sets the value of numDiasPagadosModified
	 */
	public void setNumDiasPagadosModified(boolean numDiasPagadosModified)
	{
		this.numDiasPagadosModified = numDiasPagadosModified;
	}

	/** 
	 * Gets the value of numDiasPagadosModified
	 */
	public boolean isNumDiasPagadosModified()
	{
		return numDiasPagadosModified;
	}

	/**
	 * Method 'getTotalPercepcionExentas'
	 * 
	 * @return double
	 */
	public double getTotalPercepcionExentas()
	{
		return totalPercepcionExentas;
	}

	/**
	 * Method 'setTotalPercepcionExentas'
	 * 
	 * @param totalPercepcionExentas
	 */
	public void setTotalPercepcionExentas(double totalPercepcionExentas)
	{
		this.totalPercepcionExentas = totalPercepcionExentas;
		this.totalPercepcionExentasNull = false;
		this.totalPercepcionExentasModified = true;
	}

	/**
	 * Method 'setTotalPercepcionExentasNull'
	 * 
	 * @param value
	 */
	public void setTotalPercepcionExentasNull(boolean value)
	{
		this.totalPercepcionExentasNull = value;
		this.totalPercepcionExentasModified = true;
	}

	/**
	 * Method 'isTotalPercepcionExentasNull'
	 * 
	 * @return boolean
	 */
	public boolean isTotalPercepcionExentasNull()
	{
		return totalPercepcionExentasNull;
	}

	/** 
	 * Sets the value of totalPercepcionExentasModified
	 */
	public void setTotalPercepcionExentasModified(boolean totalPercepcionExentasModified)
	{
		this.totalPercepcionExentasModified = totalPercepcionExentasModified;
	}

	/** 
	 * Gets the value of totalPercepcionExentasModified
	 */
	public boolean isTotalPercepcionExentasModified()
	{
		return totalPercepcionExentasModified;
	}

	/**
	 * Method 'getTotalPercepcionGravadas'
	 * 
	 * @return double
	 */
	public double getTotalPercepcionGravadas()
	{
		return totalPercepcionGravadas;
	}

	/**
	 * Method 'setTotalPercepcionGravadas'
	 * 
	 * @param totalPercepcionGravadas
	 */
	public void setTotalPercepcionGravadas(double totalPercepcionGravadas)
	{
		this.totalPercepcionGravadas = totalPercepcionGravadas;
		this.totalPercepcionGravadasNull = false;
		this.totalPercepcionGravadasModified = true;
	}

	/**
	 * Method 'setTotalPercepcionGravadasNull'
	 * 
	 * @param value
	 */
	public void setTotalPercepcionGravadasNull(boolean value)
	{
		this.totalPercepcionGravadasNull = value;
		this.totalPercepcionGravadasModified = true;
	}

	/**
	 * Method 'isTotalPercepcionGravadasNull'
	 * 
	 * @return boolean
	 */
	public boolean isTotalPercepcionGravadasNull()
	{
		return totalPercepcionGravadasNull;
	}

	/** 
	 * Sets the value of totalPercepcionGravadasModified
	 */
	public void setTotalPercepcionGravadasModified(boolean totalPercepcionGravadasModified)
	{
		this.totalPercepcionGravadasModified = totalPercepcionGravadasModified;
	}

	/** 
	 * Gets the value of totalPercepcionGravadasModified
	 */
	public boolean isTotalPercepcionGravadasModified()
	{
		return totalPercepcionGravadasModified;
	}

	/**
	 * Method 'getTotalDeduccionExentas'
	 * 
	 * @return double
	 */
	public double getTotalDeduccionExentas()
	{
		return totalDeduccionExentas;
	}

	/**
	 * Method 'setTotalDeduccionExentas'
	 * 
	 * @param totalDeduccionExentas
	 */
	public void setTotalDeduccionExentas(double totalDeduccionExentas)
	{
		this.totalDeduccionExentas = totalDeduccionExentas;
		this.totalDeduccionExentasNull = false;
		this.totalDeduccionExentasModified = true;
	}

	/**
	 * Method 'setTotalDeduccionExentasNull'
	 * 
	 * @param value
	 */
	public void setTotalDeduccionExentasNull(boolean value)
	{
		this.totalDeduccionExentasNull = value;
		this.totalDeduccionExentasModified = true;
	}

	/**
	 * Method 'isTotalDeduccionExentasNull'
	 * 
	 * @return boolean
	 */
	public boolean isTotalDeduccionExentasNull()
	{
		return totalDeduccionExentasNull;
	}

	/** 
	 * Sets the value of totalDeduccionExentasModified
	 */
	public void setTotalDeduccionExentasModified(boolean totalDeduccionExentasModified)
	{
		this.totalDeduccionExentasModified = totalDeduccionExentasModified;
	}

	/** 
	 * Gets the value of totalDeduccionExentasModified
	 */
	public boolean isTotalDeduccionExentasModified()
	{
		return totalDeduccionExentasModified;
	}

	/**
	 * Method 'getTotalDeduccionGravadas'
	 * 
	 * @return double
	 */
	public double getTotalDeduccionGravadas()
	{
		return totalDeduccionGravadas;
	}

	/**
	 * Method 'setTotalDeduccionGravadas'
	 * 
	 * @param totalDeduccionGravadas
	 */
	public void setTotalDeduccionGravadas(double totalDeduccionGravadas)
	{
		this.totalDeduccionGravadas = totalDeduccionGravadas;
		this.totalDeduccionGravadasNull = false;
		this.totalDeduccionGravadasModified = true;
	}

	/**
	 * Method 'setTotalDeduccionGravadasNull'
	 * 
	 * @param value
	 */
	public void setTotalDeduccionGravadasNull(boolean value)
	{
		this.totalDeduccionGravadasNull = value;
		this.totalDeduccionGravadasModified = true;
	}

	/**
	 * Method 'isTotalDeduccionGravadasNull'
	 * 
	 * @return boolean
	 */
	public boolean isTotalDeduccionGravadasNull()
	{
		return totalDeduccionGravadasNull;
	}

	/** 
	 * Sets the value of totalDeduccionGravadasModified
	 */
	public void setTotalDeduccionGravadasModified(boolean totalDeduccionGravadasModified)
	{
		this.totalDeduccionGravadasModified = totalDeduccionGravadasModified;
	}

	/** 
	 * Gets the value of totalDeduccionGravadasModified
	 */
	public boolean isTotalDeduccionGravadasModified()
	{
		return totalDeduccionGravadasModified;
	}

	/**
	 * Method 'getTotalIncapacidadDescuento'
	 * 
	 * @return double
	 */
	public double getTotalIncapacidadDescuento()
	{
		return totalIncapacidadDescuento;
	}

	/**
	 * Method 'setTotalIncapacidadDescuento'
	 * 
	 * @param totalIncapacidadDescuento
	 */
	public void setTotalIncapacidadDescuento(double totalIncapacidadDescuento)
	{
		this.totalIncapacidadDescuento = totalIncapacidadDescuento;
		this.totalIncapacidadDescuentoNull = false;
		this.totalIncapacidadDescuentoModified = true;
	}

	/**
	 * Method 'setTotalIncapacidadDescuentoNull'
	 * 
	 * @param value
	 */
	public void setTotalIncapacidadDescuentoNull(boolean value)
	{
		this.totalIncapacidadDescuentoNull = value;
		this.totalIncapacidadDescuentoModified = true;
	}

	/**
	 * Method 'isTotalIncapacidadDescuentoNull'
	 * 
	 * @return boolean
	 */
	public boolean isTotalIncapacidadDescuentoNull()
	{
		return totalIncapacidadDescuentoNull;
	}

	/** 
	 * Sets the value of totalIncapacidadDescuentoModified
	 */
	public void setTotalIncapacidadDescuentoModified(boolean totalIncapacidadDescuentoModified)
	{
		this.totalIncapacidadDescuentoModified = totalIncapacidadDescuentoModified;
	}

	/** 
	 * Gets the value of totalIncapacidadDescuentoModified
	 */
	public boolean isTotalIncapacidadDescuentoModified()
	{
		return totalIncapacidadDescuentoModified;
	}

	/**
	 * Method 'getTotalHrextraDobleHr'
	 * 
	 * @return double
	 */
	public double getTotalHrextraDobleHr()
	{
		return totalHrextraDobleHr;
	}

	/**
	 * Method 'setTotalHrextraDobleHr'
	 * 
	 * @param totalHrextraDobleHr
	 */
	public void setTotalHrextraDobleHr(double totalHrextraDobleHr)
	{
		this.totalHrextraDobleHr = totalHrextraDobleHr;
		this.totalHrextraDobleHrNull = false;
		this.totalHrextraDobleHrModified = true;
	}

	/**
	 * Method 'setTotalHrextraDobleHrNull'
	 * 
	 * @param value
	 */
	public void setTotalHrextraDobleHrNull(boolean value)
	{
		this.totalHrextraDobleHrNull = value;
		this.totalHrextraDobleHrModified = true;
	}

	/**
	 * Method 'isTotalHrextraDobleHrNull'
	 * 
	 * @return boolean
	 */
	public boolean isTotalHrextraDobleHrNull()
	{
		return totalHrextraDobleHrNull;
	}

	/** 
	 * Sets the value of totalHrextraDobleHrModified
	 */
	public void setTotalHrextraDobleHrModified(boolean totalHrextraDobleHrModified)
	{
		this.totalHrextraDobleHrModified = totalHrextraDobleHrModified;
	}

	/** 
	 * Gets the value of totalHrextraDobleHrModified
	 */
	public boolean isTotalHrextraDobleHrModified()
	{
		return totalHrextraDobleHrModified;
	}

	/**
	 * Method 'getTotalHrextraDobleImp'
	 * 
	 * @return double
	 */
	public double getTotalHrextraDobleImp()
	{
		return totalHrextraDobleImp;
	}

	/**
	 * Method 'setTotalHrextraDobleImp'
	 * 
	 * @param totalHrextraDobleImp
	 */
	public void setTotalHrextraDobleImp(double totalHrextraDobleImp)
	{
		this.totalHrextraDobleImp = totalHrextraDobleImp;
		this.totalHrextraDobleImpNull = false;
		this.totalHrextraDobleImpModified = true;
	}

	/**
	 * Method 'setTotalHrextraDobleImpNull'
	 * 
	 * @param value
	 */
	public void setTotalHrextraDobleImpNull(boolean value)
	{
		this.totalHrextraDobleImpNull = value;
		this.totalHrextraDobleImpModified = true;
	}

	/**
	 * Method 'isTotalHrextraDobleImpNull'
	 * 
	 * @return boolean
	 */
	public boolean isTotalHrextraDobleImpNull()
	{
		return totalHrextraDobleImpNull;
	}

	/** 
	 * Sets the value of totalHrextraDobleImpModified
	 */
	public void setTotalHrextraDobleImpModified(boolean totalHrextraDobleImpModified)
	{
		this.totalHrextraDobleImpModified = totalHrextraDobleImpModified;
	}

	/** 
	 * Gets the value of totalHrextraDobleImpModified
	 */
	public boolean isTotalHrextraDobleImpModified()
	{
		return totalHrextraDobleImpModified;
	}

	/**
	 * Method 'getTotalHrextraTripleHr'
	 * 
	 * @return double
	 */
	public double getTotalHrextraTripleHr()
	{
		return totalHrextraTripleHr;
	}

	/**
	 * Method 'setTotalHrextraTripleHr'
	 * 
	 * @param totalHrextraTripleHr
	 */
	public void setTotalHrextraTripleHr(double totalHrextraTripleHr)
	{
		this.totalHrextraTripleHr = totalHrextraTripleHr;
		this.totalHrextraTripleHrNull = false;
		this.totalHrextraTripleHrModified = true;
	}

	/**
	 * Method 'setTotalHrextraTripleHrNull'
	 * 
	 * @param value
	 */
	public void setTotalHrextraTripleHrNull(boolean value)
	{
		this.totalHrextraTripleHrNull = value;
		this.totalHrextraTripleHrModified = true;
	}

	/**
	 * Method 'isTotalHrextraTripleHrNull'
	 * 
	 * @return boolean
	 */
	public boolean isTotalHrextraTripleHrNull()
	{
		return totalHrextraTripleHrNull;
	}

	/** 
	 * Sets the value of totalHrextraTripleHrModified
	 */
	public void setTotalHrextraTripleHrModified(boolean totalHrextraTripleHrModified)
	{
		this.totalHrextraTripleHrModified = totalHrextraTripleHrModified;
	}

	/** 
	 * Gets the value of totalHrextraTripleHrModified
	 */
	public boolean isTotalHrextraTripleHrModified()
	{
		return totalHrextraTripleHrModified;
	}

	/**
	 * Method 'getTotalHrextraTripleImp'
	 * 
	 * @return double
	 */
	public double getTotalHrextraTripleImp()
	{
		return totalHrextraTripleImp;
	}

	/**
	 * Method 'setTotalHrextraTripleImp'
	 * 
	 * @param totalHrextraTripleImp
	 */
	public void setTotalHrextraTripleImp(double totalHrextraTripleImp)
	{
		this.totalHrextraTripleImp = totalHrextraTripleImp;
		this.totalHrextraTripleImpNull = false;
		this.totalHrextraTripleImpModified = true;
	}

	/**
	 * Method 'setTotalHrextraTripleImpNull'
	 * 
	 * @param value
	 */
	public void setTotalHrextraTripleImpNull(boolean value)
	{
		this.totalHrextraTripleImpNull = value;
		this.totalHrextraTripleImpModified = true;
	}

	/**
	 * Method 'isTotalHrextraTripleImpNull'
	 * 
	 * @return boolean
	 */
	public boolean isTotalHrextraTripleImpNull()
	{
		return totalHrextraTripleImpNull;
	}

	/** 
	 * Sets the value of totalHrextraTripleImpModified
	 */
	public void setTotalHrextraTripleImpModified(boolean totalHrextraTripleImpModified)
	{
		this.totalHrextraTripleImpModified = totalHrextraTripleImpModified;
	}

	/** 
	 * Gets the value of totalHrextraTripleImpModified
	 */
	public boolean isTotalHrextraTripleImpModified()
	{
		return totalHrextraTripleImpModified;
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
		
		if (!(_other instanceof Nomina)) {
			return false;
		}
		
		final Nomina _cast = (Nomina) _other;
		if (idnomina != _cast.idnomina) {
			return false;
		}
		
		if (idnominaModified != _cast.idnominaModified) {
			return false;
		}
		
		if (idcomprobantefiscal != _cast.idcomprobantefiscal) {
			return false;
		}
		
		if (idcomprobantefiscalModified != _cast.idcomprobantefiscalModified) {
			return false;
		}
		
		if (numEmpleado == null ? _cast.numEmpleado != numEmpleado : !numEmpleado.equals( _cast.numEmpleado )) {
			return false;
		}
		
		if (numEmpleadoModified != _cast.numEmpleadoModified) {
			return false;
		}
		
		if (fechaPago == null ? _cast.fechaPago != fechaPago : !fechaPago.equals( _cast.fechaPago )) {
			return false;
		}
		
		if (fechaPagoModified != _cast.fechaPagoModified) {
			return false;
		}
		
		if (fechaInicialPago == null ? _cast.fechaInicialPago != fechaInicialPago : !fechaInicialPago.equals( _cast.fechaInicialPago )) {
			return false;
		}
		
		if (fechaInicialPagoModified != _cast.fechaInicialPagoModified) {
			return false;
		}
		
		if (fechaFinPago == null ? _cast.fechaFinPago != fechaFinPago : !fechaFinPago.equals( _cast.fechaFinPago )) {
			return false;
		}
		
		if (fechaFinPagoModified != _cast.fechaFinPagoModified) {
			return false;
		}
		
		if (departamento == null ? _cast.departamento != departamento : !departamento.equals( _cast.departamento )) {
			return false;
		}
		
		if (departamentoModified != _cast.departamentoModified) {
			return false;
		}
		
		if (nombreEmpleado == null ? _cast.nombreEmpleado != nombreEmpleado : !nombreEmpleado.equals( _cast.nombreEmpleado )) {
			return false;
		}
		
		if (nombreEmpleadoModified != _cast.nombreEmpleadoModified) {
			return false;
		}
		
		if (puesto == null ? _cast.puesto != puesto : !puesto.equals( _cast.puesto )) {
			return false;
		}
		
		if (puestoModified != _cast.puestoModified) {
			return false;
		}
		
		if (numDiasPagados != _cast.numDiasPagados) {
			return false;
		}
		
		if (numDiasPagadosNull != _cast.numDiasPagadosNull) {
			return false;
		}
		
		if (numDiasPagadosModified != _cast.numDiasPagadosModified) {
			return false;
		}
		
		if (totalPercepcionExentas != _cast.totalPercepcionExentas) {
			return false;
		}
		
		if (totalPercepcionExentasNull != _cast.totalPercepcionExentasNull) {
			return false;
		}
		
		if (totalPercepcionExentasModified != _cast.totalPercepcionExentasModified) {
			return false;
		}
		
		if (totalPercepcionGravadas != _cast.totalPercepcionGravadas) {
			return false;
		}
		
		if (totalPercepcionGravadasNull != _cast.totalPercepcionGravadasNull) {
			return false;
		}
		
		if (totalPercepcionGravadasModified != _cast.totalPercepcionGravadasModified) {
			return false;
		}
		
		if (totalDeduccionExentas != _cast.totalDeduccionExentas) {
			return false;
		}
		
		if (totalDeduccionExentasNull != _cast.totalDeduccionExentasNull) {
			return false;
		}
		
		if (totalDeduccionExentasModified != _cast.totalDeduccionExentasModified) {
			return false;
		}
		
		if (totalDeduccionGravadas != _cast.totalDeduccionGravadas) {
			return false;
		}
		
		if (totalDeduccionGravadasNull != _cast.totalDeduccionGravadasNull) {
			return false;
		}
		
		if (totalDeduccionGravadasModified != _cast.totalDeduccionGravadasModified) {
			return false;
		}
		
		if (totalIncapacidadDescuento != _cast.totalIncapacidadDescuento) {
			return false;
		}
		
		if (totalIncapacidadDescuentoNull != _cast.totalIncapacidadDescuentoNull) {
			return false;
		}
		
		if (totalIncapacidadDescuentoModified != _cast.totalIncapacidadDescuentoModified) {
			return false;
		}
		
		if (totalHrextraDobleHr != _cast.totalHrextraDobleHr) {
			return false;
		}
		
		if (totalHrextraDobleHrNull != _cast.totalHrextraDobleHrNull) {
			return false;
		}
		
		if (totalHrextraDobleHrModified != _cast.totalHrextraDobleHrModified) {
			return false;
		}
		
		if (totalHrextraDobleImp != _cast.totalHrextraDobleImp) {
			return false;
		}
		
		if (totalHrextraDobleImpNull != _cast.totalHrextraDobleImpNull) {
			return false;
		}
		
		if (totalHrextraDobleImpModified != _cast.totalHrextraDobleImpModified) {
			return false;
		}
		
		if (totalHrextraTripleHr != _cast.totalHrextraTripleHr) {
			return false;
		}
		
		if (totalHrextraTripleHrNull != _cast.totalHrextraTripleHrNull) {
			return false;
		}
		
		if (totalHrextraTripleHrModified != _cast.totalHrextraTripleHrModified) {
			return false;
		}
		
		if (totalHrextraTripleImp != _cast.totalHrextraTripleImp) {
			return false;
		}
		
		if (totalHrextraTripleImpNull != _cast.totalHrextraTripleImpNull) {
			return false;
		}
		
		if (totalHrextraTripleImpModified != _cast.totalHrextraTripleImpModified) {
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
		_hashCode = 29 * _hashCode + idnomina;
		_hashCode = 29 * _hashCode + (idnominaModified ? 1 : 0);
		_hashCode = 29 * _hashCode + idcomprobantefiscal;
		_hashCode = 29 * _hashCode + (idcomprobantefiscalModified ? 1 : 0);
		if (numEmpleado != null) {
			_hashCode = 29 * _hashCode + numEmpleado.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (numEmpleadoModified ? 1 : 0);
		if (fechaPago != null) {
			_hashCode = 29 * _hashCode + fechaPago.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechaPagoModified ? 1 : 0);
		if (fechaInicialPago != null) {
			_hashCode = 29 * _hashCode + fechaInicialPago.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechaInicialPagoModified ? 1 : 0);
		if (fechaFinPago != null) {
			_hashCode = 29 * _hashCode + fechaFinPago.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (fechaFinPagoModified ? 1 : 0);
		if (departamento != null) {
			_hashCode = 29 * _hashCode + departamento.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (departamentoModified ? 1 : 0);
		if (nombreEmpleado != null) {
			_hashCode = 29 * _hashCode + nombreEmpleado.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (nombreEmpleadoModified ? 1 : 0);
		if (puesto != null) {
			_hashCode = 29 * _hashCode + puesto.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (puestoModified ? 1 : 0);
		long temp_numDiasPagados = Double.doubleToLongBits(numDiasPagados);
		_hashCode = 29 * _hashCode + (int) (temp_numDiasPagados ^ (temp_numDiasPagados >>> 32));
		_hashCode = 29 * _hashCode + (numDiasPagadosNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (numDiasPagadosModified ? 1 : 0);
		long temp_totalPercepcionExentas = Double.doubleToLongBits(totalPercepcionExentas);
		_hashCode = 29 * _hashCode + (int) (temp_totalPercepcionExentas ^ (temp_totalPercepcionExentas >>> 32));
		_hashCode = 29 * _hashCode + (totalPercepcionExentasNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (totalPercepcionExentasModified ? 1 : 0);
		long temp_totalPercepcionGravadas = Double.doubleToLongBits(totalPercepcionGravadas);
		_hashCode = 29 * _hashCode + (int) (temp_totalPercepcionGravadas ^ (temp_totalPercepcionGravadas >>> 32));
		_hashCode = 29 * _hashCode + (totalPercepcionGravadasNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (totalPercepcionGravadasModified ? 1 : 0);
		long temp_totalDeduccionExentas = Double.doubleToLongBits(totalDeduccionExentas);
		_hashCode = 29 * _hashCode + (int) (temp_totalDeduccionExentas ^ (temp_totalDeduccionExentas >>> 32));
		_hashCode = 29 * _hashCode + (totalDeduccionExentasNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (totalDeduccionExentasModified ? 1 : 0);
		long temp_totalDeduccionGravadas = Double.doubleToLongBits(totalDeduccionGravadas);
		_hashCode = 29 * _hashCode + (int) (temp_totalDeduccionGravadas ^ (temp_totalDeduccionGravadas >>> 32));
		_hashCode = 29 * _hashCode + (totalDeduccionGravadasNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (totalDeduccionGravadasModified ? 1 : 0);
		long temp_totalIncapacidadDescuento = Double.doubleToLongBits(totalIncapacidadDescuento);
		_hashCode = 29 * _hashCode + (int) (temp_totalIncapacidadDescuento ^ (temp_totalIncapacidadDescuento >>> 32));
		_hashCode = 29 * _hashCode + (totalIncapacidadDescuentoNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (totalIncapacidadDescuentoModified ? 1 : 0);
		long temp_totalHrextraDobleHr = Double.doubleToLongBits(totalHrextraDobleHr);
		_hashCode = 29 * _hashCode + (int) (temp_totalHrextraDobleHr ^ (temp_totalHrextraDobleHr >>> 32));
		_hashCode = 29 * _hashCode + (totalHrextraDobleHrNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (totalHrextraDobleHrModified ? 1 : 0);
		long temp_totalHrextraDobleImp = Double.doubleToLongBits(totalHrextraDobleImp);
		_hashCode = 29 * _hashCode + (int) (temp_totalHrextraDobleImp ^ (temp_totalHrextraDobleImp >>> 32));
		_hashCode = 29 * _hashCode + (totalHrextraDobleImpNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (totalHrextraDobleImpModified ? 1 : 0);
		long temp_totalHrextraTripleHr = Double.doubleToLongBits(totalHrextraTripleHr);
		_hashCode = 29 * _hashCode + (int) (temp_totalHrextraTripleHr ^ (temp_totalHrextraTripleHr >>> 32));
		_hashCode = 29 * _hashCode + (totalHrextraTripleHrNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (totalHrextraTripleHrModified ? 1 : 0);
		long temp_totalHrextraTripleImp = Double.doubleToLongBits(totalHrextraTripleImp);
		_hashCode = 29 * _hashCode + (int) (temp_totalHrextraTripleImp ^ (temp_totalHrextraTripleImp >>> 32));
		_hashCode = 29 * _hashCode + (totalHrextraTripleImpNull ? 1 : 0);
		_hashCode = 29 * _hashCode + (totalHrextraTripleImpModified ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return NominaPk
	 */
	public NominaPk createPk()
	{
		return new NominaPk(idnomina);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
    @Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		ret.append( "com.fens.desktopMonitor.dto.Nomina: " );
		ret.append("idnomina=").append(idnomina);
		ret.append(", idcomprobantefiscal=").append(idcomprobantefiscal);
		ret.append(", numEmpleado=").append(numEmpleado);
		ret.append(", fechaPago=").append(fechaPago);
		ret.append(", fechaInicialPago=").append(fechaInicialPago);
		ret.append(", fechaFinPago=").append(fechaFinPago);
		ret.append(", departamento=").append(departamento);
		ret.append(", nombreEmpleado=").append(nombreEmpleado);
		ret.append(", puesto=").append(puesto);
		ret.append(", numDiasPagados=").append(numDiasPagados);
		ret.append(", totalPercepcionExentas=").append(totalPercepcionExentas);
		ret.append(", totalPercepcionGravadas=").append(totalPercepcionGravadas);
		ret.append(", totalDeduccionExentas=").append(totalDeduccionExentas);
		ret.append(", totalDeduccionGravadas=").append(totalDeduccionGravadas);
		ret.append(", totalIncapacidadDescuento=").append(totalIncapacidadDescuento);
		ret.append(", totalHrextraDobleHr=").append(totalHrextraDobleHr);
		ret.append(", totalHrextraDobleImp=").append(totalHrextraDobleImp);
		ret.append(", totalHrextraTripleHr=").append(totalHrextraTripleHr);
		ret.append(", totalHrextraTripleImp=").append(totalHrextraTripleImp);
		return ret.toString();
	}

}