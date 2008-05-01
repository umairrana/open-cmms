package com.matrix.focus.register;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class PartRequirementEntry {
    private Connection connection;
    public String preventiveMaintenanceID;
    public String assetID;
    public String partID;
    public String amount;
    
    public PartRequirementEntry(Connection con) {
        connection = con;
    }
    
    public boolean save(){
      String sql = "INSERT INTO part_requirement (" +
                                      "Asset_ID, " +
                                      "Preventive_Maintenance_ID, " +
                                      "Part_ID, " +
                                      "Amount) VALUES(?,?,?,?)";
      
      
      try{
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,assetID);
        stmt.setString(2,preventiveMaintenanceID);
        stmt.setString(3,partID);
        stmt.setString(4,amount);
        
        if(stmt.executeUpdate()==1){
          return true;
        }
        else{
          return false;
        }
      }
      catch(Exception er){
        return false;
      }
    }
    
    public boolean update(){
        String sql = "UPDATE part_requirement SET Amount =? " +
                "WHERE Preventive_Maintenance_ID=? AND " +
                      "Asset_ID=? AND " +
                      "Part_ID=?";
        
        try{
          PreparedStatement stmt = connection.prepareStatement(sql);
          stmt.setString(1,amount);
          stmt.setString(2,preventiveMaintenanceID);
          stmt.setString(3,assetID);
          stmt.setString(4,partID);
          
          if(stmt.executeUpdate()==1){
            return true;
          }
          else{
            return false;
          }
        }
        catch(Exception er){
          return false;
        }
    }
    public boolean delete(){
        String sql = "DELETE FROM part_requirement WHERE Preventive_Maintenance_ID=? AND " +
                                           "Asset_ID=? AND " +
                                           "Part_ID=?";
        
        try{
          PreparedStatement stmt = connection.prepareStatement(sql);
          stmt.setString(1,preventiveMaintenanceID);
          stmt.setString(2,assetID);
          stmt.setString(3,partID);
          
          if(stmt.executeUpdate()==1){
            return true;
          }
          else{
            return false;
          }
        }
        catch(Exception er){
          return false;
        }
    }
}