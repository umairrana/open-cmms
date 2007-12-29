package com.matrix.focus.util;

import com.matrix.focus.mdi.messageBar.MessageBar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import javax.swing.JDialog;

public class DBConnectionPool{
  /**When we were using the pool*/
  private String url,username,password;
  private JDialog parent;
  
  public DBConnectionPool(JDialog parent){
    this.parent = parent;
    try{
        /**Uncomment this and run if these properties have been channged.*/
        /*
        Utilities.setProperties("conf/dbconnection.inf",
                                new String[][]{
                                    {"DRIVER","com.mysql.jdbc.Driver"},
                                    {"URL","jdbc:mysql://192.168.0.17/focus"},
                                    {"USERNAME","focus"},
                                    {"PASSWORD","matrixit"}
                                }
                                ,"Database Connection Information");
        
        */
        
        Properties pro = Utilities.getProperties("conf/dbconnection.inf");
        String driver = pro.getProperty("DRIVER");
        url = pro.getProperty("URL");
        username = pro.getProperty("USERNAME");
        password = pro.getProperty("PASSWORD");
        
        /**Without the pool*/
        Class.forName (driver);
    } 
    catch(Exception e){ 
       e.printStackTrace();
       MessageBar.showErrorDialog(parent,"Invalid database driver.\nPlease select the correct driver and restart the program.","Focus");
       new DBInfoDialog().setLocationRelativeTo(parent);
    }
  }
  
  public Connection getConnection(){
      /**When we were using the pool*/
      //return pool.getConnection();
      
      /**Without the pool*/
      try{
        return DriverManager.getConnection(url,username,password);
      }
      catch(Exception e){
        MessageBar.showErrorDialog(parent,"Invalid database server\nPlease select the correct server.and restart the program.","Focus");
        new DBInfoDialog().setLocationRelativeTo(parent);
        return null;
      }
  }
}