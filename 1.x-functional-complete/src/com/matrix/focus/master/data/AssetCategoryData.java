package com.matrix.focus.master.data;

import com.matrix.focus.master.entity.AssetCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssetCategoryData{
    public static void saveAssetCategory(AssetCategory category, Connection connection) throws Exception{
        String sql = "INSERT INTO asset_category" +
        "(Category_ID,Officer_In_Charge,Remarks,Deleted) VALUES " +
        "(?,?,?,'false')";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,category.getID());
            stmt.setString(2,category.getOfficer());
            stmt.setString(3,category.getRemarks());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("New Machine category details not saved.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on saving new Machine category details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void updateAssetCategory(AssetCategory category, Connection connection) throws Exception{
        String sql = "UPDATE asset_category SET ";
              sql += "Officer_In_Charge=?, ";
              sql += "Remarks=? WHERE Category_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,category.getOfficer());
            stmt.setString(2,category.getRemarks());
            stmt.setString(3,category.getID());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Machine category details not updated.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on updating Machine category details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void deleteAssetCategory(String id, Connection connection) throws Exception{
        String sql = "DELETE FROM asset_category WHERE Category_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,id);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Machine category details not deleted.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on deleting Machine category details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static AssetCategory getAssetCategory(String id, Connection connection) throws Exception{
        String sql = "SELECT * FROM asset_category WHERE Category_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,id);
            
            ResultSet rec = stmt.executeQuery();
            if(rec.first()){
                return new AssetCategory(
                    rec.getString("Category_ID"),
                    rec.getString("Officer_In_Charge"),
                    rec.getString("Remarks")
                );
            }
            else{
                throw new Exception("Machine category details not found.");
            }
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on retrieving Machine category details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  
    }
}
