package com.matrix.focus.register;



import java.sql.Connection;
import java.sql.Statement;

public class ConditionMonitoringRegisterEntry {
    private Connection connection;
    private String id;
    public String asset_id;
    public String condition_monitoring_id;
    public String interrupted;
    public String lower_limit; 
    public String upper_limit;
    public String considering_limit;
    public String unit_of_messure;
    public String registered_level;
    
    public ConditionMonitoringRegisterEntry(Connection con){
        connection = con;
    }
    public boolean save(){
      String sql = "INSERT INTO condition_monitoring_register (" +
                                      "Asset_ID, " +
                                      "Condition_Monitoring_ID, " +
                                      "Interrupted, " +
                                      "Lower_Limit, " +
                                      "Upper_Limit, " +
                                      "Considering_Limit, " +
                                      "Unit_of_Messure, " +
                                      "Registered_Level) VALUES(" +
                                                "'" + asset_id + "'," +
                                                "'" + condition_monitoring_id + "'," +
                                                "'" + interrupted + "'," +
                                                "'" + lower_limit + "'," +
                                                "'" + upper_limit + "'," +
                                                "'" + considering_limit + "'," +
                                                "'" + unit_of_messure + "'," +
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
        String sql = "UPDATE condition_monitoring_register SET " +
            "Interrupted ='" + interrupted +"', " +
            "Lower_Limit ='" + lower_limit +"', " +
            "Upper_Limit ='" + upper_limit +"', " +
            "Considering_Limit ='" + considering_limit +"', " +
            "Unit_of_Messure ='" + unit_of_messure +"', " +
            "Registered_Level ='" + registered_level + "' WHERE Asset_ID='"+ asset_id +"' AND Condition_Monitoring_ID='" + condition_monitoring_id + "'";
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
        String sql = "DELETE FROM condition_monitoring_register  WHERE Asset_ID='"+ asset_id +"' AND Condition_Monitoring_ID='" + condition_monitoring_id + "'";
        
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
