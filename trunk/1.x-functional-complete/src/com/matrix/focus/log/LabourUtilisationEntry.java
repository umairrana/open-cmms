package com.matrix.focus.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LabourUtilisationEntry {
    private Connection connection;
    public String pm_work_order_id;
    public String employee_id;
    public String skill_category;
    public String worked_hours;
    public String team_id;
    public String planned_start_time;
    public String planned_end_time;
    public String setup_time;
    public String duration;
    public String starting_time;
    public String ending_time;
    public String time_slot_id;
    public String asset_id;
    public String pm_job;
    
    public LabourUtilisationEntry(Connection con) {
        this.connection = con;
    }
    
    public void save() throws Exception{
      String sql = "INSERT INTO labour_utilisation (" +
                                      "PM_Work_Order_ID, " +
                                      "Employee_ID, " +
                                      "Skill_Category," +
                                      "Team_ID," +
                                      "planned_start_time," +
                                      "planned_end_time," +
                                      "Setup_time," +
                                      "Duration," +
                                      "Time_Slot_ID," +
                                      "Asset_ID," +
                                      "PM_Job," +
                                      "Starting_Time," +
                                      "Ending_Time) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
      
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,pm_work_order_id);
        stmt.setString(2,employee_id);
        stmt.setString(3,skill_category);
        stmt.setString(4,team_id);
        stmt.setString(5,planned_start_time);
        stmt.setString(6,planned_end_time);
        stmt.setString(7,setup_time);
        stmt.setString(8,duration);
        stmt.setString(9,time_slot_id);
        stmt.setString(10,asset_id);
        stmt.setString(11,pm_job);
        stmt.setString(12,planned_start_time);
        stmt.setString(13,planned_end_time);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Labour allocation not saved");
        }
    }
    
    public void update() throws Exception{
        String sql = "UPDATE labour_utilisation " +
                     "SET " +
                        "Team_ID =?, " +
                        "planned_start_time =?, " +
                        "planned_end_time =?, " +
                        "Setup_time =?, " +
                        "Duration =?, " +
                        "Ending_Time=?," +
                        "Asset_ID =?, " +
                        "PM_Job =? " +
                     "WHERE PM_Work_Order_ID=? AND " +
                     "Employee_ID=? AND " +
                     "Skill_Category =? AND " +
                     "Time_Slot_ID =?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,team_id);
        stmt.setString(2,planned_start_time);
        stmt.setString(3,planned_end_time);
        stmt.setString(4,setup_time);
        stmt.setString(5,duration);
        stmt.setString(6,ending_time);
        stmt.setString(7,asset_id);
        stmt.setString(8,pm_job);
        stmt.setString(9,pm_work_order_id);
        stmt.setString(10,employee_id);
        stmt.setString(11,skill_category);
        stmt.setString(12,time_slot_id);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Labour allocation not updated");
        }
    }
    
    public void log() throws Exception{
        String sql = "UPDATE labour_utilisation " +
                     "SET Worked_Hours =?, " +
                         "Starting_Time = ?, " +
                         "Ending_Time = ? " +
                     "WHERE PM_Work_Order_ID=? AND " +
                     "Employee_ID=? AND " +
                     "Skill_Category =? AND " +
                     "Time_Slot_ID =?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,worked_hours);
        stmt.setString(2,starting_time);
        stmt.setString(3,ending_time);
        stmt.setString(4,pm_work_order_id);
        stmt.setString(5,employee_id);
        stmt.setString(6,skill_category);
        stmt.setString(7,time_slot_id);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Labour utilization not saved");
        }
    }
    
    public void delete() throws Exception{
        String sql = "DELETE FROM labour_utilisation " +
                     "WHERE PM_Work_Order_ID=? AND " +
                     "Employee_ID=? AND " +
                     "Skill_Category =? AND " +
                     "Time_Slot_ID =?";
        
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,pm_work_order_id);
        stmt.setString(2,employee_id);
        stmt.setString(3,skill_category);
        stmt.setString(4,time_slot_id);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Labour allocation not deleted");
        }
    }  
}
