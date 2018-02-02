/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fens.desktopMonitor.test;

import com.fens.desktopMonitor.config.Configuration;
import com.fens.desktopMonitor.jdbc.ResourceManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author ISCesar
 */
public class TestLeerColumnasDeTabla {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        TestLeerColumnasDeTabla test = new TestLeerColumnasDeTabla();
        test.run();
    }

    private void run() throws Exception {
        Connection con = ResourceManager.getConnection();
        
        PreparedStatement pstmt = con.prepareStatement("SELECT * FROM ACCIONCOMPROBANTE");
        ResultSetMetaData rsmd = pstmt.getMetaData();
        int numColumns = rsmd.getColumnCount();
        int i=1;
        while (i <= numColumns) {
            String str = rsmd.getColumnName(i);
            System.out.println(str);
            i++;
        }
        
    }
    
}
