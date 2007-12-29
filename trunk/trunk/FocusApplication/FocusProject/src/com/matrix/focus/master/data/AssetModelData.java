package com.matrix.focus.master.data;

import com.matrix.focus.master.entity.AssetModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssetModelData {
    public static void saveAssetModel(AssetModel model, Connection connection) throws Exception{
        String sql = "INSERT INTO asset_model" +
        "(Model_ID,Category_ID) VALUES " +
        "(?,?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,model.getID());
            stmt.setString(2,model.getCategory());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("New Machine model details not saved.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on saving new Machine model details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void updateAssetModel(AssetModel model, Connection connection) throws Exception{
        String sql = "UPDATE asset_model SET ";
              sql += "Category_ID=? WHERE Model_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,model.getCategory());
            stmt.setString(2,model.getID());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Machine model details not updated.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on updating Machine model details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void deleteAssetModel(String id, Connection connection) throws Exception{
        String sql = "DELETE FROM asset_model WHERE Model_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,id);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Machine model details not deleted.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on deleting Machine model details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static AssetModel getAssetModel(String id, Connection connection) throws Exception{
        String sql = "SELECT * FROM asset_model WHERE Model_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,id);
            
            ResultSet rec = stmt.executeQuery();
            if(rec.first()){
                return new AssetModel(
                    rec.getString("Model_ID"),
                    rec.getString("Category_ID")
                );
            }
            else{
                throw new Exception("Machine model details not found.");
            }
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on retrieving Machine model details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  
    }
    
}
