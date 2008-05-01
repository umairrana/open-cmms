package com.matrix.focus.register;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class LabourRequirementEntry {
    private Connection connection;
    public String preventiveMaintenanceID;
    public String assetID;
    public String maintenanceCategory;
    public String amount;
    
    public LabourRequirementEntry(Connection con) {
        connection = con;
    }
    
    public boolean save(){
      String sql = "INSERT INTO labour_requirement (" +
                                      "Asset_ID, " +
                                      "Preventive_Maintenance_ID, " +
                                      "Maintenance_Category, " +
                                      "Amount) VALUES(?,?,?,?)";
      try{
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,assetID);
        stmt.setString(2,preventiveMaintenanceID);
        stmt.setString(3,maintenanceCategory);
        stmt.setString(4,amount);
        
        if(stmt.executeUpdate()==0){
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
        String sql = "UPDATE labour_requirement SET Amount =? WHERE " +
                    "Preventive_Maintenance_ID=? AND " +
                    "Asset_ID=? AND " +
                    "Maintenance_Category=?";
        
        try{
          PreparedStatement stmt = connection.prepareStatement(sql);
          stmt.setString(1,amount);
          stmt.setString(2,preventiveMaintenanceID);
          stmt.setString(3,assetID);
          stmt.setString(4,maintenanceCategory);
          
          if(stmt.executeUpdate()==0){
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
        String sql = "DELETE FROM labour_requirement WHERE Preventive_Maintenance_ID=? AND " +
                                           "Asset_ID=? AND " +
                                           "Maintenance_Category=?";
        
        try{
          PreparedStatement stmt = connection.prepareStatement(sql);
          stmt.setString(1,preventiveMaintenanceID);
          stmt.setString(2,assetID);
          stmt.setString(3,maintenanceCategory);
          
          if(stmt.executeUpdate()==0){
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