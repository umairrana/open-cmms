package com.matrix.focus.workorder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class RequestedTask{
    public String pm_work_order_id;
    public String asset_id;
    public String task_id;
    public String done_date;
    public String time_taken;
    public String success;
    public String remarks;
    
    private Connection connection;
    
    public RequestedTask(Connection con){
        this.connection = con;
    }
    
    public void save() throws Exception{
        String sql = "INSERT INTO requested_maintenance_log (" +
                              "pm_work_order_id, " +
                              "asset_id, " +
                              "task_id) " +
                              "VALUES(?,?,?)";
    
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,pm_work_order_id);
        stmt.setString(2,asset_id);
        stmt.setString(3,task_id);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Task was not saved to the Job");
        }
    }
    
    public void delete() throws Exception{
        String sql = "DELETE FROM requested_maintenance_log " +
                                        "WHERE " +
                                            "pm_work_order_id =? AND " +
                                            "asset_id =? AND " +
                                            "task_id =?";
                                                 
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,pm_work_order_id);
        stmt.setString(2,asset_id);
        stmt.setString(3,task_id);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Task was not removed from the Job");
        }
    }
    
    public void log() throws Exception{
        String sql = "UPDATE requested_maintenance_log SET " +
                                        "Done_Date =?, " +
                                        "Time_Taken =?, " +
                                        "Success =?," +
                                        "Remarks =? " +  
                                        "WHERE " +
                                            "PM_Work_Order_ID =? AND " +
                                            "Asset_ID =? AND " +
                                            "Task_ID =?";
                                                 
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,done_date);
        stmt.setString(2,time_taken);
        stmt.setString(3,success);
        stmt.setString(4,remarks);
        stmt.setString(5,pm_work_order_id);
        stmt.setString(6,asset_id);
        stmt.setString(7,task_id);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Task log was not saved.");
        }
    }
}

