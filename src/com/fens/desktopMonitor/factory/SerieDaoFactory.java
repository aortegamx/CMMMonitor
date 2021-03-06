/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package com.fens.desktopMonitor.factory;

import java.sql.Connection;
import com.fens.desktopMonitor.dao.*;
import com.fens.desktopMonitor.jdbc.*;

public class SerieDaoFactory
{
	/**
	 * Method 'create'
	 * 
	 * @return SerieDao
	 */
	public static SerieDao create()
	{
		return new SerieDaoImpl();
	}

	/**
	 * Method 'create'
	 * 
	 * @param conn
	 * @return SerieDao
	 */
	public static SerieDao create(Connection conn)
	{
		return new SerieDaoImpl( conn );
	}

}
