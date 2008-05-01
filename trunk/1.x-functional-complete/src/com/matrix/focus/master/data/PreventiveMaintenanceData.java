package com.matrix.focus.master.data;

import com.matrix.focus.master.entity.PreventiveMaintenance;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PreventiveMaintenanceData {

    public static void savePreventiveMaintenance(PreventiveMaintenance pm, Connection connection) throws Exception{
        String sql = "INSERT INTO preventive_maintenance" +
        "(ID,Description,Maintenance_Category,PM_Type,PM_Procedure) VALUES " +
        "(?,?,?,?,?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            pm.setID(getNextPreventiveMaintenanceID(connection));
            stmt.setString(1,pm.getID());
            stmt.setString(2,pm.getDescription());
            stmt.setString(3,pm.getCategory());
            stmt.setString(4,pm.getType());
            stmt.setString(5,pm.getProcedure());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("New preventive maintenance details not saved.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on saving new preventive maintenance details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void updatePreventiveMaintenance(PreventiveMaintenance pm, Connection connection) throws Exception{
        String sql = "UPDATE preventive_maintenance SET ";
              sql += "Description=?, ";
              sql += "Maintenance_Category=?, ";
              sql += "PM_Type=?, ";
              sql += "PM_Procedure=? WHERE ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,pm.getDescription());
            stmt.setString(2,pm.getCategory());
            stmt.setString(3,pm.getType());
            stmt.setString(4,pm.getProcedure());
            stmt.setString(5,pm.getID());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Preventive maintenance details not updated.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on updating preventive maintenance details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void deletePreventiveMaintenance(String pm_id, Connection connection) throws Exception{
        String sql = "DELETE FROM preventive_maintenance WHERE ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,pm_id);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Preventive maintenance details not deleted.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on deleting preventive maintenance details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static PreventiveMaintenance getPreventiveMaintenance(String pm_id, Connection connection) throws Exception{
        String sql = "SELECT * FROM preventive_maintenance WHERE ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,pm_id);
            
            ResultSet rec = stmt.executeQuery();
            if(rec.first()){
                return new PreventiveMaintenance(
                    rec.getString("ID"),
                    rec.getString("Description"),
                    rec.getString("Maintenance_Category"),
                    rec.getString("PM_Type"),
                    rec.getString("PM_Procedure")

                );
            }
            else{
                throw new Exception("Preventive maintenance details not found.");
            }
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on retrieving preventive maintenance details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  
    }
    
    public static String getNextPreventiveMaintenanceID(Connection connection){
      
        String sql = "SELECT MAX(CAST(SUBSTRING_INDEX(id, '-', -1) AS SIGNED))+1 FROM preventive_maintenance";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            rec.next();
            return "PM-".concat(rec.getString(1));
            
        }
        catch(Exception er){
            //Very First Entry only.
            return "PM-1";
        }
        
    } 

}
