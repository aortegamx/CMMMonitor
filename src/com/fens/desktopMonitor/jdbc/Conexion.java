/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author 578
 */
public class Conexion {
    
    public static Connection conn = null ;

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public static Connection getConn() {                
        if (conn==null){
            try {
                conn = ResourceManager.getConnection();
            } catch (SQLException ex) {}
        }else{
            try {
                if (conn.isClosed()){
                    conn = ResourceManager.getConnection();
                }
            } catch (SQLException ex) {}
        }    
        return conn;
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
    
    
    
    
}
