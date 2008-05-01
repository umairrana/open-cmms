package com.matrix.focus.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PreventiveMaintenanceLogEntry {
    private Connection connection;
    public String preventive_maintenance_log_id;
    public String scheduled_id;
    public String pm_work_order_id;
    public String done_date;
    public String done_meter;
    public String time_taken;
    
    public String success;
    public String remarks;
    
    public PreventiveMaintenanceLogEntry(Connection con) {
        connection = con;
        done_date = "0000-00-00";
        time_taken = "0-0-0";
    }
    public void save() throws Exception{
        String sql = "INSERT INTO preventive_maintenance_log (" +
                                      "Scheduled_ID, " +
                                      "PM_Work_Order_ID) VALUES(?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,scheduled_id);
        stmt.setString(2,pm_work_order_id);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Scheduled maintenance was not saved to the Job.");        
        }
        else{
            sql = "SELECT Preventive_Maintenance_Log_ID FROM preventive_maintenance_log WHERE Scheduled_ID=? AND PM_Work_Order_ID=?";
            PreparedStatement stmt2 = connection.prepareStatement(sql);
            stmt2.setString(1,scheduled_id); 
            stmt2.setString(2,pm_work_order_id);
            
            ResultSet rec = stmt2.executeQuery();
            rec.first();
            preventive_maintenance_log_id = rec.getString("Preventive_Maintenance_Log_ID");
        }
    }
    
    public void log() throws Exception{
        String sql = "UPDATE " +
                    "preventive_maintenance_log SET " +
                        "Done_Date = ?," +
                        "Done_Meter = ?," +
                        "Time_Taken = ?," +
                        "Success = ?," +
                        "Remarks = ? " +
               "WHERE " +
                    "Preventive_Maintenance_Log_ID = ?";
                    
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,this.done_date);
        stmt.setString(2,this.done_meter);
        stmt.setString(3,this.time_taken);
        stmt.setString(4,this.success);
        stmt.setString(5,this.remarks);
        stmt.setString(6,this.preventive_maintenance_log_id);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Log details not updated.");        
        }
    }

    public void delete() throws Exception{
        String sql = "DELETE FROM preventive_maintenance_log WHERE Preventive_Maintenance_Log_ID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,preventive_maintenance_log_id);
      
        if(stmt.executeUpdate()!=1){
            throw new Exception("Job maintenance not deleted.");        
        }
    }
}
