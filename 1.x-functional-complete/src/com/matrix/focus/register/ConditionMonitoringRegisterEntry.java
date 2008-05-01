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
    public String order;
    
    public ConditionMonitoringRegisterEntry(Connection con){
        connection = con;
    }
    public boolean save(){
      String sql = "INSERT INTO machine_reading (" +
                                      "machine_no, " +
                                      "reading_id, " +
                                      "Interrupted, " +
                                      "Lower_Limit, " +
                                      "Upper_Limit, " +
                                      "Considering_Limit, " +
                                      "Unit_of_Measure, " +
                                      "order) VALUES(" +
                                                "'" + asset_id + "'," +
                                                "'" + condition_monitoring_id + "'," +
                                                "'" + interrupted + "'," +
                                                "'" + lower_limit + "'," +
                                                "'" + upper_limit + "'," +
                                                "'" + considering_limit + "'," +
                                                "'" + unit_of_messure + "'," +
                                                "'" + order +"')";
      
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
        String sql = "UPDATE machine_reading SET " +
            "Interrupted ='" + interrupted +"', " +
            "Lower_Limit ='" + lower_limit +"', " +
            "Upper_Limit ='" + upper_limit +"', " +
            "Considering_Limit ='" + considering_limit +"', " +
            "Unit_of_Measure ='" + unit_of_messure +"', " +
            "order ='" + order + "' WHERE machine_no='"+ asset_id +"' AND reading_id='" + condition_monitoring_id + "'";
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
