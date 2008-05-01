package com.matrix.focus.master.data;

import com.matrix.focus.master.entity.ConditionMonitoringItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConditionMonitoringItemData {
    public static void saveConditionMonitoringItem(ConditionMonitoringItem item, Connection connection) throws Exception{
        String sql = "INSERT INTO condition_monitoring" +
        "(ID,Description,Category,Unit) VALUES " +
        "(?,?,?,?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            item.setID(getNextConditionMonitoringItemID(connection));
            stmt.setString(1,item.getID());
            stmt.setString(2,item.getDescription());
            stmt.setString(3,item.getCategory());
            stmt.setString(4,item.getUnit());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("New condition monitoring item details not saved.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on saving new condition monitoring item details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void updateConditionMonitoringItem(ConditionMonitoringItem item, Connection connection) throws Exception{
        String sql = "UPDATE condition_monitoring SET ";
              sql += "Description=?, ";
              sql += "Category=?, ";
              sql += "Unit=? WHERE ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,item.getDescription());
            stmt.setString(2,item.getCategory());
            stmt.setString(3,item.getUnit());
            stmt.setString(4,item.getID());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Condition monitoring item details not updated.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on updating condition monitoring item details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void deleteConditionMonitoringItem(String id, Connection connection) throws Exception{
        String sql = "DELETE FROM condition_monitoring WHERE ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,id);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Condition monitoring item details not deleted.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on deleting condition monitoring item details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static ConditionMonitoringItem getConditionMonitoringItem(String id, Connection connection) throws Exception{
        String sql = "SELECT * FROM condition_monitoring WHERE ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,id);
            
            ResultSet rec = stmt.executeQuery();
            if(rec.first()){
                return new ConditionMonitoringItem(
                    rec.getString("ID"),
                    rec.getString("Description"),
                    rec.getString("Category"),
                    rec.getString("Unit")
                );
            }
            else{
                throw new Exception("Condition monitoring item details not found.");
            }
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on retrieving condition monitoring item details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  
    }
    
    public static String getNextConditionMonitoringItemID(Connection connection){
        /*[ CK-XXXX ]*/
        String sql = "SELECT SUBSTRING(MAX(ID),4)+1 FROM condition_monitoring";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            rec.next();
            return "CK-".concat(rec.getString(1));
            
        }
        catch(Exception er){
            //Very First Entry only.
            return "CK-1";
        }
        
    } 
}
