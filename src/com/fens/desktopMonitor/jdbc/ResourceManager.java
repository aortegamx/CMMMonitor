package com.fens.desktopMonitor.jdbc;

import com.fens.desktopMonitor.config.Configuration;
import java.sql.*;

public class ResourceManager
{
    private static String JDBC_DRIVER   = "org.apache.derby.jdbc.EmbeddedDriver";
    
    /*
    private static String JDBC_URL      = "jdbc:derby:C:/Users/578/Documents/NetBeansProjects/CMMDesktopMonitor/DB/monitorTexto";

    private static String JDBC_USER     = "APP";
    private static String JDBC_PASSWORD = "root"; */

    private static Driver driver = null;

    public static synchronized Connection getConnection()
	throws SQLException
    {
        if (driver == null)
        {
            try
            {
                Class jdbcDriverClass = Class.forName( JDBC_DRIVER );
                driver = (Driver) jdbcDriverClass.newInstance();
                DriverManager.registerDriver( driver );
            }
            catch (Exception e)
            {
                System.out.println( "Failed to initialise JDBC driver" );
                e.printStackTrace();
            }
        }

        /*
        return DriverManager.getConnection(
                JDBC_URL,
                JDBC_USER,
                JDBC_PASSWORD
        );*/
        
        Configuration appConfig = new Configuration();
        return DriverManager.getConnection(
                appConfig.getJdbc_url(),
                appConfig.getJdbc_user(),
                appConfig.getJdbc_password()
        );
        
    }


	public static void close(Connection conn)
	{
		try {
			if (conn != null) conn.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}

	public static void close(PreparedStatement stmt)
	{
		try {
			if (stmt != null) stmt.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}

	public static void close(ResultSet rs)
	{
		try {
			if (rs != null) rs.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}

	}

}
