package com.matrix.focus.master.data;

import com.matrix.focus.master.entity.ChecklistItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChecklistItemData {
    public static void saveChecklistItem(ChecklistItem item, Connection connection) throws Exception{
        String sql = "INSERT INTO checklist_item" +
        "(ID,Description,Checklist_Category) VALUES " +
        "(?,?,?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            item.setID(getNextChecklistItemID(connection));
            stmt.setString(1,item.getID());
            stmt.setString(2,item.getDescription());
            stmt.setString(3,item.getCategory());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("New checklist item details not saved.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on saving new checklist item details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void updateChecklistItem(ChecklistItem item, Connection connection) throws Exception{
        String sql = "UPDATE checklist_item SET ";
              sql += "Description=?, ";
              sql += "Checklist_Category=? WHERE ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,item.getDescription());
            stmt.setString(2,item.getCategory());
            stmt.setString(3,item.getID());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Checklist item details not updated.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on updating checklist item details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void deleteChecklistItem(String id, Connection connection) throws Exception{
        String sql = "DELETE FROM checklist_item WHERE ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,id);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Checklist item details not deleted.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on deleting checklist item details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static ChecklistItem getChecklistItem(String id, Connection connection) throws Exception{
        String sql = "SELECT * FROM checklist_item WHERE ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,id);
            
            ResultSet rec = stmt.executeQuery();
            if(rec.first()){
                return new ChecklistItem(
                    rec.getString("ID"),
                    rec.getString("Description"),
                    rec.getString("Checklist_Category")
                );
            }
            else{
                throw new Exception("Checklist item details not found.");
            }
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on retrieving checklist item details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  
    }
    
    public static String getNextChecklistItemID(Connection connection){
        /*[ CM-XXXX ]*/
        String sql = "SELECT SUBSTRING(MAX(ID),4)+1 FROM checklist_item";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            rec.next();
            return "CM-".concat(rec.getString(1));
            
        }
        catch(Exception er){
            //Very First Entry only.
            return "CM-1";
        }
        
    } 
}
