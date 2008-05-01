package com.matrix.focus.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RequestedJobsPartAllocationEntry {
    
    public String pm_work_order_id;
    public String asset_id;
    public String task_id;
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
    
    private Connection connection;
    
    public RequestedJobsPartAllocationEntry(Connection con) {
        connection = con;
    }
    
    public void save() throws Exception{
        String sql = "INSERT INTO part_utilization_for_requested_maintenance (" +
                                      "PM_Work_Order_ID, " +
                                      "Asset_ID, " +
                                      "Task_ID, " +
                                      "Part_ID, " +
                                      "Required_Amount, " +
                                      "Authorization_Required, " +
                                      "Brand, " +
                                      "Supplier, " +
                                      "Batch, " +
                                      "Unit_Price," +
                                      "Authorized) " +
                              "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                                      
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,pm_work_order_id);
        stmt.setString(2,asset_id);
        stmt.setString(3,task_id);
        stmt.setString(4,part_id);
        stmt.setString(5,required_amount);
        stmt.setString(6,authorization_required);
        stmt.setString(7,brand);
        stmt.setString(8,supplier);
        stmt.setString(9,batch);
        stmt.setString(10,unit_price);
        stmt.setString(11,authorized);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Part allocation not saved.");
        }
    }
    
    public void update() throws Exception{
        String sql = "UPDATE part_utilization_for_requested_maintenance " +
                     "SET " +
                            "Required_Amount =? " +
                     "WHERE " +
                            "PM_Work_Order_ID =? AND " +
                            "Asset_ID =? AND " +
                            "Task_ID =? AND " +
                            "Part_ID =? AND " +
                            "Brand =? AND " +
                            "Supplier =? AND " +
                            "Batch =?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,required_amount);
        stmt.setString(2,pm_work_order_id);
        stmt.setString(3,asset_id);
        stmt.setString(4,task_id);
        stmt.setString(5,part_id);
        stmt.setString(6,brand);
        stmt.setString(7,supplier);
        stmt.setString(8,batch);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Part allocation not updated.");
        }
    }
    
    public void delete() throws Exception{
        String sql = "DELETE FROM part_utilization_for_requested_maintenance " +
                     "WHERE " +
                        "PM_Work_Order_ID =? AND " +
                        "Asset_ID =? AND " +
                        "Task_ID =? AND " +
                        "Part_ID =? AND " +
                        "Brand =? AND " +
                        "Supplier =? AND " +
                        "Batch =?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,pm_work_order_id);
        stmt.setString(2,asset_id);
        stmt.setString(3,task_id);
        stmt.setString(4,part_id);
        stmt.setString(5,brand);
        stmt.setString(6,supplier);
        stmt.setString(7,batch);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Part allocation was not deleted.");
        }
    }
    
    public void authorize() throws Exception{
        String sql = "UPDATE part_utilization_for_requested_maintenance " +
                     "SET " +
                        "Authorized = ?, " +
                        "Authorized_by = ?, " +
                        "Authorized_datetime = CURRENT_TIMESTAMP() " +
                     "WHERE " +
                            "PM_Work_Order_ID =? AND " +
                            "Asset_ID =? AND " +
                            "Task_ID =? AND " +
                            "Part_ID =? AND " +
                            "Brand =? AND " +
                            "Supplier =? AND " +
                            "Batch =?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,authorized);
        stmt.setString(2,authorized_by);
        stmt.setString(3,pm_work_order_id);
        stmt.setString(4,asset_id);
        stmt.setString(5,task_id);
        stmt.setString(6,part_id);
        stmt.setString(7,brand);
        stmt.setString(8,supplier);
        stmt.setString(9,batch);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Part authorization not updated.");
        }
    }
    
    public void log() throws Exception{
        String sql = "UPDATE part_utilization_for_requested_maintenance " +
                     "SET " +
                            "Used_Amount =? " +
                     "WHERE " +
                            "PM_Work_Order_ID =? AND " +
                            "Asset_ID =? AND " +
                            "Task_ID =? AND " +
                            "Part_ID =? AND " +
                            "Brand =? AND " +
                            "Supplier =? AND " +
                            "Batch =?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,used_amount);
        stmt.setString(2,pm_work_order_id);
        stmt.setString(3,asset_id);
        stmt.setString(4,task_id);
        stmt.setString(5,part_id);
        stmt.setString(6,brand);
        stmt.setString(7,supplier);
        stmt.setString(8,batch);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Part allocation not updated.");
        }
    }
    
}
