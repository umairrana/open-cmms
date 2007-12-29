package com.matrix.focus.register;



import java.sql.Connection;
import java.sql.Statement;

public class CheckListRegisterEntry {
    private Connection connection;
    
    private String id;
    public String asset_id;
    public String checklist_id;
    public String interrupted;
    public String registered_level;
    
    public CheckListRegisterEntry(Connection con){
        connection = con;
    }
    public boolean save(){        
      String sql = "INSERT INTO checklist_register (" +
                                      "Asset_ID, " +
                                      "Checklist_ID, " +
                                      "Interrupted, " +
                                      "Registered_Level) VALUES(" +
                                                "'" + asset_id + "'," +
                                                "'" + checklist_id + "'," +
                                                "'" + interrupted + "'," +
                                                "'" + registered_level +"')";
      
      Statement stmt;
      try{
        stmt = connection.createStatement();  
        
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
        return false;
      }
    }
    
    public boolean update(){
        String sql = "UPDATE checklist_register SET " +
            "Interrupted ='" + interrupted +"', " +
            "Registered_Level ='" + registered_level + "' WHERE Asset_ID='"+ asset_id +"' AND Checklist_ID='" + checklist_id + "'";
        Statement stmt;
        try{
          stmt = connection.createStatement();  
          
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
          return false;
        }
    }
    public int delete(){
        String sql = "DELETE FROM checklist_register  WHERE Asset_ID='"+ asset_id +"' AND Checklist_ID='" + checklist_id + "'";
        
        Statement stmt;
        try{
          stmt = connection.createStatement();  
          
          if(stmt.executeUpdate(sql)>0){
            stmt.close();
            return stmt.getUpdateCount();
          }
          else{
            stmt.close();
            return 0;
          }
        }
        catch(Exception er){
          stmt = null;
          er.printStackTrace();
          return -1;
        }
    }
}
