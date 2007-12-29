package com.matrix.focus.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class PartUtilisationEntry {
    private Connection connection;
    public String preventive_maintenance_log_id;
    public String part_id;
    public String required_amount;
    public String used_amount;
    public String unit_price;
    public String authorization_required;
    public String authorized;
    public String authorized_by;
    public String authorized_datetime;
    public String brand;
    public String supplier;
    public String batch;
    
    public PartUtilisationEntry(Connection con) {
        connection = con;
        authorized = "false";
        authorized_by = " ";
        used_amount = " ";
    }
    
    public void save() throws Exception{
        String sql = "INSERT INTO part_utilisation " +
                        "(Preventive_Maintenance_Log_ID, " +
                        "Part_ID, " +
                        "Required_Amount, " +
                        "Brand, " +
                        "Supplier," +
                        "Batch," +
                        "Unit_Price," +
                        "Authorization_Required," +
                        "Authorized) " +
           "VALUES(?,?,?,?,?,?,?,?,?)";
                 
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,preventive_maintenance_log_id);
        stmt.setString(2,part_id);
        stmt.setString(3,required_amount);
        stmt.setString(4,brand);
        stmt.setString(5,supplier);
        stmt.setString(6,batch);
        stmt.setString(7,unit_price);
        stmt.setString(8,authorization_required);
        stmt.setString(9,authorized);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Part allocation not saved.");
        }
    }
    
    public void update() throws Exception{
        String sql = "UPDATE part_utilisation " +
                        "SET " +
                            "Required_Amount = ? " + 
                     "WHERE " +
                            "Preventive_Maintenance_Log_ID = ? AND " +
                            "Part_ID=? AND " +
                            "Brand =? AND " +
                            "Supplier =? AND " +
                            "Batch =?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,required_amount);
        stmt.setString(2,preventive_maintenance_log_id);
        stmt.setString(3,part_id);
        stmt.setString(4,brand);
        stmt.setString(5,supplier);
        stmt.setString(6,batch);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Part allocation not updated.");
        }
    }
    
    public void delete() throws Exception{
        String sql = "DELETE FROM part_utilisation " + 
                        "WHERE " +
                           "Preventive_Maintenance_Log_ID = ? AND " +
                           "Part_ID=? AND " +
                           "Brand =? AND " +
                           "Supplier =? AND " +
                           "Batch =?";       
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,preventive_maintenance_log_id);
        stmt.setString(2,part_id);
        stmt.setString(3,brand);
        stmt.setString(4,supplier);
        stmt.setString(5,batch);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Part allocation was not deleted.");
        }
    }
    
    public void log() throws Exception{
        String sql = "UPDATE part_utilisation " +
                        "SET " +
                            "Used_Amount = ? " + 
                     "WHERE " +
                            "Preventive_Maintenance_Log_ID = ? AND " +
                            "Part_ID=? AND " +
                            "Brand =? AND " +
                            "Supplier =? AND " +
                            "Batch =?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,used_amount);
        stmt.setString(2,preventive_maintenance_log_id);
        stmt.setString(3,part_id);
        stmt.setString(4,brand);
        stmt.setString(5,supplier);
        stmt.setString(6,batch);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Part allocation not updated.");
        }
    }
    
    public void authorize() throws Exception{
        String sql = "UPDATE part_utilisation " +
                        "SET " +
                            "Authorized = ?, " +
                            "Authorized_by = ?, " +
                            "Authorized_datetime = CURRENT_TIMESTAMP() " + 
                     "WHERE " +
                            "Preventive_Maintenance_Log_ID = ? AND " +
                            "Part_ID=? AND " +
                            "Brand =? AND " +
                            "Supplier =? AND " +
                            "Batch =?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,authorized);
        stmt.setString(2,authorized_by);
        stmt.setString(3,preventive_maintenance_log_id);
        stmt.setString(4,part_id);
        stmt.setString(5,brand);
        stmt.setString(6,supplier);
        stmt.setString(7,batch);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Part allocation not updated.");
        }
    }
}
