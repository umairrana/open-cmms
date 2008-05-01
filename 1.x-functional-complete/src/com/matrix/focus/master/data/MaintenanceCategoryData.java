package com.matrix.focus.master.data;

import com.matrix.focus.master.entity.MaintenanceCategory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MaintenanceCategoryData{

    public static void saveMaintenanceCategory(MaintenanceCategory category, Connection connection) throws Exception{
        String sql = "INSERT INTO preventive_maintenance_category" +
        "(Maintenance_Category,Responsible_Person,Remarks) VALUES " +
        "(?,?,?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,category.getCategory());
            stmt.setString(2,category.getResponsiblePerson());
            stmt.setString(3,category.getRemarks());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("New maintenance category details not saved.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on saving new maintenance category details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void updateMaintenanceCategory(MaintenanceCategory category, Connection connection) throws Exception{
        String sql = "UPDATE preventive_maintenance_category SET ";
              sql += "Responsible_Person=?, ";
              sql += "Remarks=? WHERE Maintenance_Category=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,category.getResponsiblePerson());
            stmt.setString(2,category.getRemarks());
            stmt.setString(3,category.getCategory());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Maintenance category details not updated.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on updating maintenance category details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void deleteMaintenanceCategory(String category, Connection connection) throws Exception{
        String sql = "DELETE FROM preventive_maintenance_category WHERE Maintenance_Category=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,category);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Maintenance category details not deleted.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on deleting mintenance category details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static MaintenanceCategory getMaintenanceCategory(String category, Connection connection) throws Exception{
        String sql = "SELECT * FROM preventive_maintenance_category WHERE Maintenance_Category=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,category);
            
            ResultSet rec = stmt.executeQuery();
            if(rec.first()){
                return new MaintenanceCategory(
                    rec.getString("Maintenance_Category"),
                    rec.getString("Responsible_Person"),
                    rec.getString("Remarks")
                );
            }
            else{
                throw new Exception("Maintenance category details not found.");
            }
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on retrieving maintenance category details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  
    }
}
