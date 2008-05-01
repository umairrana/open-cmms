package com.matrix.focus.master.entity;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Reading{

    public String id;
    public String description;
    public String category;
    private Connection conn;
    
    public Reading(Connection dbConn){
    conn = dbConn;
    }
    
    public Reading(Connection dbConn, String id){
    conn = dbConn;
    this.id = id;
    if(read()){}
    else throw new NullPointerException();
    }
    
    private boolean read(){
    String sql = "CALL get_readings_all('"+id+"')";
    Statement stmt;
    ResultSet rec;
    try{
      stmt = conn.createStatement();
      rec = stmt.executeQuery(sql);
      rec.next();
        this.id = rec.getString("Reading_ID");
        description = rec.getString("Description");
        category = rec.getString("Category");
        //procedure = rec.getString("PM_Procedure");
        //pm_type = rec.getString("PM_Type");
       
      rec.close();
      stmt.close();
      return true;
    }
    catch(Exception er){
      rec = null;
      stmt = null;
      er.printStackTrace();
      return false;
    }
    }
      
    public boolean delete(){
    String sql = "DELETE FROM readingsmaster WHERE Reading_ID='"+id+"'";
    Statement stmt;
    try{
      stmt = conn.createStatement();
      if(stmt.executeUpdate(sql) >0){
        stmt.close();
        return true;
      }
      else{
        stmt.close();
        return false;
      }
    }
    catch(Exception er){
      stmt = null;
      er.printStackTrace();
      return false;
    }
    }
    
    public boolean save(){
      String sql = "INSERT INTO readingsmaster (Reading_ID, Category, Description) VALUES('" + id + "','" +  category + "','" +description + "')";
      Statement stmt;
      try{
        stmt = conn.createStatement();         
        if(stmt.executeUpdate(sql)>0){
          stmt.close();
          return true;
        }
        else{
          stmt.close();
          return false;
        }
      }
      catch(Exception er){
        stmt = null;
        //er.printStackTrace();
        return false;
      }
    } 
    
    public boolean update(){
      String sql = "UPDATE readingsmaster SET " +
                            "Description='" + description + "', " +
                            "Category='"+ category + "' " +
                   "WHERE Reading_ID ='" + id + "'";
      Statement stmt;
      try{
        stmt = conn.createStatement();           
        if(stmt.executeUpdate(sql)>0){
          stmt.close();
          return true;
        }
        else{
          stmt.close();
          return false;
        }
      }
      catch(Exception er){
        stmt = null;
        er.printStackTrace();
        return false;
      }
    }
    
    public static String getNextPreventiveMaintenanceID(Connection connection){
         /*[ PM-XXXX ]*/
         String sql = "SELECT MAX(Reading_ID)+1 FROM readingsmaster";
         try{
             ResultSet rec = connection.createStatement().executeQuery(sql);
             rec.next();
             return rec.getString(1);
             
         }
         catch(Exception er){
             //Very First Entry only.
             return "1";
         }
         
     }
}