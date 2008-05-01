/********************************************************
 *  Class   = ExcelConnection.java
 *  Details = To get a excel connection
 *  Author  = Himesha
 *  Date    = 2006.07.15
 * ******************************************************/

package com.matrix.focus.connect;

import java.sql.DriverManager;
import java.sql.Connection;

public class ODBCConnection {

    private Connection con;
    
    public ODBCConnection(String odbcName, String username, String passsword) {
        // Load the JDBC-ODBC bridge driver 
        try {
            Class.forName ("sun.jdbc.odbc.JdbcOdbcDriver");
            // ODBC data source name 
            String dsn = "jdbc:odbc:" + odbcName;     
            // Connect to the database 
            con = DriverManager.getConnection(dsn, username, passsword); 
        } catch(Exception ee) {
            ee.printStackTrace();
        }
    }

    public Connection getConnection(){
      return con;
    }
}
