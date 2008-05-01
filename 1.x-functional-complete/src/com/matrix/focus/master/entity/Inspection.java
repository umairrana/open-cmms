package com.matrix.focus.master.entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Inspection{
  public String id;
  public String description;
  public String category;
  public String deleted;
  private Connection conn;
  
  public Inspection(Connection dbConn){
  conn = dbConn;
}

  public Inspection(Connection dbConn, String id){
  conn = dbConn;
  this.id = id;
  if(read()){}
  else throw new NullPointerException();
}

  private boolean read(){
    String sql = "CALL get_inspection_all('"+id+"')";
    Statement stmt;
    ResultSet rec;
    try{
      stmt = conn.createStatement();
      rec = stmt.executeQuery(sql);
      rec.next();
        this.id = rec.getString("Inspection_ID");
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
    String sql = "DELETE FROM inspectionmaster WHERE Inspection_ID='"+id+"'";
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
      String sql = "INSERT INTO inspectionmaster (Inspection_ID, Category, Description) VALUES('" + id + "','" +  category + "','" +description + "')";
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
      String sql = "UPDATE inspectionmaster SET " +
                            "Description='" + description + "', " +
                            "Category='"+ category + "' " +
                   "WHERE Inspection_ID ='" + id + "'";
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
         String sql = "SELECT MAX(Inspection_ID)+1 FROM inspectionmaster";
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

