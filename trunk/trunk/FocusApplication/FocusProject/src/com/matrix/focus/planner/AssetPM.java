package com.matrix.focus.planner;

import com.matrix.focus.util.Utilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AssetPM{

    public String   ASSET_ID;
    public String   PM_ID;
    public boolean  TIME_BASED;
    public int      CYCLE_METER;
    public int      CYCLE_TIME;
    public String   PLAN_START_DATE;
    public int      PLAN_START_METER;

    private Connection connection;
    
    public AssetPM(String asset_id, String pm_id, Connection connection) throws Exception{
        this.ASSET_ID = asset_id;
        this.PM_ID = pm_id;
        this.connection = connection;
        
        String sql = "SELECT " +
                        "IF(r.Basis='Time Period','true','false') AS TimeBased, " +
                        "r.CycleMeter, " +
                        "r.CycleTime, " +
                        "r.StartingDate," +
                        "r.StartingMeter " +
                     "FROM " +
                        "preventive_maintenance_register r, " +
                        "asset a " +
                     "WHERE " +
                        "r.Preventive_Maintenance_ID =? AND " +
                        "r.Asset_ID =? ";
                        
        PreparedStatement stmt = this.connection.prepareStatement(sql);
        stmt.setString(1,PM_ID);
        stmt.setString(2,ASSET_ID);

        ResultSet rec = stmt.executeQuery();
        rec.first();
        
        this.TIME_BASED = rec.getBoolean("TimeBased");
        this.CYCLE_METER = rec.getInt("CycleMeter");
        this.CYCLE_TIME = Utilities.getDays(rec.getString("CycleTime"));
        this.PLAN_START_DATE = rec.getString("StartingDate");
        this.PLAN_START_METER = rec.getInt("StartingMeter");
    }
    
    public boolean isTimeBased(){
        return TIME_BASED;
    }
    
    public boolean isPlanned() throws Exception{
        String sql = "SELECT Scheduled_ID " +
                     "FROM scheduled_preventive_maintenance " +
                     "WHERE Preventive_Maintenance_ID =? AND Asset_ID =?";
        
        PreparedStatement stmt = connection.prepareStatement(sql); 
        stmt.setString(1,PM_ID);
        stmt.setString(2,ASSET_ID);
        
        ResultSet rec = stmt.executeQuery();
        return rec.first();
    }
    
    public boolean hasPendingWorkOrders() throws Exception{
        String sql = "SELECT 	Scheduled_ID " +
                     "FROM 	scheduled_preventive_maintenance " +
                     "WHERE 	Preventive_Maintenance_ID =? " +
                               "AND " +
                               "Asset_ID =? " +
                               "AND " +
                               "Scheduled_ID IN (SELECT Scheduled_ID " +
                                                "FROM 	preventive_maintenance_log " +
                                                "WHERE 	Done_Date = '0000-00-00 00:00:00')"; 
        
        PreparedStatement stmt = connection.prepareStatement(sql); 
        stmt.setString(1,PM_ID);
        stmt.setString(2,ASSET_ID);
        
        ResultSet rec = stmt.executeQuery();
        return rec.first();
    }
    
    public boolean hasDoneWorkOrders() throws Exception{
        String sql = "SELECT    Scheduled_ID " +
                     "FROM      scheduled_preventive_maintenance " +
                     "WHERE     Preventive_Maintenance_ID = ? " +
                               "AND " +
                               "Asset_ID = ? " +
                               "AND " +
                               "Scheduled_ID IN (SELECT Scheduled_ID " +
                                                "FROM   preventive_maintenance_log " +
                                                "WHERE  Done_Date != '0000-00-00 00:00:00')"; 
        
        PreparedStatement stmt = connection.prepareStatement(sql); 
        stmt.setString(1,PM_ID);
        stmt.setString(2,ASSET_ID);
        
        ResultSet rec = stmt.executeQuery();
        return rec.first();
    }
    
    public String getLastDoneDate() throws Exception{
    
        String sql = "SELECT  MAX(Done_Date) AS LastDoneDate " +
                     "FROM    preventive_maintenance_log " +
                     "WHERE   Done_Date != '0000-00-00 00:00:00' " +
                             "AND " +
                             "Scheduled_ID IN (SELECT  Scheduled_ID " +
                                              "FROM    scheduled_preventive_maintenance " +
                                              "WHERE   Preventive_Maintenance_ID = ? " +
                                                       "AND " +
                                                       "Asset_ID = ?)";
        
        PreparedStatement stmt = connection.prepareStatement(sql); 
        stmt.setString(1,PM_ID);
        stmt.setString(2,ASSET_ID);
        
        ResultSet rec = stmt.executeQuery();
        if(rec.first()){
            return rec.getString("LastDoneDate");
        }
        else{
            throw new Exception("NO Last Done Date found.");
        }
    }
    
    public int getLastDoneMeter() throws Exception{
        String sql = "SELECT  IFNULL(MAX(Done_Meter),0) AS LastDoneMeter " +
                     "FROM    preventive_maintenance_log " +
                     "WHERE   Done_Date != '0000-00-00 00:00:00' " +
                             "AND " +
                             "Scheduled_ID IN (SELECT  Scheduled_ID " +
                                              "FROM    scheduled_preventive_maintenance " +
                                              "WHERE   Preventive_Maintenance_ID = ? " +
                                                       "AND " +
                                                       "Asset_ID = ?)";
        
        PreparedStatement stmt = connection.prepareStatement(sql); 
        stmt.setString(1,PM_ID);
        stmt.setString(2,ASSET_ID);
        
        ResultSet rec = stmt.executeQuery();
        if(rec.first()){
            return Integer.parseInt(rec.getString("LastDoneMeter"));
        }
        else{
            throw new Exception("NO Last Done Meter found.");
        }
    }
    
    public String getLastScheduledDate() throws Exception{
        String sql = "SELECT MAX(modified_date) FROM scheduled_preventive_maintenance WHERE Preventive_Maintenance_ID =? AND Asset_ID =?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,PM_ID);
        stmt.setString(2,ASSET_ID);
        
        ResultSet rec = stmt.executeQuery();
        rec.first();
        
        return rec.getString(1);
    }
    
    public void planFrom(String starting_date, String ending_date, int average_meter_per_day) throws Exception{
        if(isTimeBased()){
            planTimeBasedFrom(starting_date,ending_date);
        }
        else{
            if(Utilities.isPositive(average_meter_per_day+"")){
                planMeterBasedFrom(starting_date,ending_date,average_meter_per_day);
            }
            else{
                throw new Exception("PM "+ PM_ID + " can not be planned without a valid Average Meter for the Machine.");
            }
        }
    }
    
    private void planTimeBasedFrom(String starting_date, String ending_date) throws Exception{
        PreparedStatement stmt = this.connection.prepareStatement("CALL planTimeBased(?,?,?,?)");
        stmt.setString(1,ASSET_ID);
        stmt.setString(2,PM_ID);
        stmt.setString(3,starting_date);
        stmt.setString(4,ending_date);
        
        stmt.executeUpdate();
    }
    
    private void planMeterBasedFrom(String starting_date, String ending_date, int average_meter_per_day) throws Exception{           
        PreparedStatement stmt = this.connection.prepareStatement("CALL planMeterBased(?,?,?,?,?)");
        stmt.setString(1,ASSET_ID);
        stmt.setString(2,PM_ID);
        stmt.setString(3,starting_date);
        stmt.setString(4,ending_date);
        stmt.setInt(5,average_meter_per_day);
        
        stmt.executeUpdate();
    }
    
    public void rescheduleFrom(String from_date,int days_difference,int meter_difference) throws Exception{
         if(isTimeBased()){
             rescheduleTimeBased(from_date,days_difference);
         }
         else{
            if(days_difference!=0){
                rescheduleMeterBasedDateOnly(from_date,days_difference);        
            }
            
            if(meter_difference!=0){
                rescheduleMeterBasedMeterOnly(from_date,meter_difference);
            }
         }
    }
    
    private void rescheduleTimeBased(String from_date, int days_diff) throws Exception{
        PreparedStatement stmt = this.connection.prepareStatement("CALL rescheduleTimeBased(?,?,?,?)");
        stmt.setString(1,ASSET_ID);
        stmt.setString(2,PM_ID);
        stmt.setString(3,from_date);
        stmt.setInt(4,days_diff);
                
        int rows = stmt.executeUpdate();
        System.out.println(rows);
        
    }
    
    private void rescheduleMeterBasedDateOnly(String from_date, int days_diff) throws Exception{
        PreparedStatement stmt = this.connection.prepareStatement("CALL rescheduleMeterBasedDateOnly(?,?,?,?)");
        stmt.setString(1,ASSET_ID);
        stmt.setString(2,PM_ID);
        stmt.setString(3,from_date);
        stmt.setInt(4,days_diff);

        stmt.executeUpdate();
    }
    
    private void rescheduleMeterBasedMeterOnly(String from_date, int meter_diff) throws Exception{
        PreparedStatement stmt = this.connection.prepareStatement("CALL rescheduleMeterBasedMeterOnly(?,?,?,?)");
        stmt.setString(1,ASSET_ID);
        stmt.setString(2,PM_ID);
        stmt.setString(3,from_date);
        stmt.setInt(4,meter_diff);
                
       stmt.executeUpdate();
    }
    
    public void deleteCompletePlan() throws Exception{
        String sql = "DELETE FROM scheduled_preventive_maintenance WHERE Preventive_Maintenance_ID =? AND Asset_ID =?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,PM_ID);
        stmt.setString(2,ASSET_ID);
        
        if(0==stmt.executeUpdate()){
            throw new Exception("No planing entries were deleted.");
        }
    }
    
    public void deletePlanFrom(String date) throws Exception{
        String sql = "DELETE FROM scheduled_preventive_maintenance WHERE DATE(Modified_Date)> ? AND Preventive_Maintenance_ID =? AND Asset_ID =?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,date);
        stmt.setString(2,PM_ID);
        stmt.setString(3,ASSET_ID);
        
        if(0==stmt.executeUpdate()){
        //    throw new Exception("No planing entries were deleted.");
            System.out.println("No planing entries were deleted.");
        }
    }
    
    public void stopPlanning() throws Exception{
        String sql = "UPDATE preventive_maintenance_register SET Plan = 'false' WHERE Preventive_Maintenance_ID =? AND Asset_ID =?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,PM_ID);
        stmt.setString(2,ASSET_ID);
        
        if(0==stmt.executeUpdate()){
            throw new Exception("Could not set planning to stop.");
        }
    }
    
    public void removeFromRegister() throws Exception{
        String sql = "DELETE FROM preventive_maintenance_register WHERE Preventive_Maintenance_ID =? AND Asset_ID =?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,PM_ID);
        stmt.setString(2,ASSET_ID);
        
        if(0==stmt.executeUpdate()){
            throw new Exception("Could not remove the PM.");
        }
    }
    
    public int getNextHighestMeterTo(int meter) throws Exception{
        String sql = "SELECT Modified_Meter FROM scheduled_preventive_maintenance WHERE Modified_Meter > ? AND Preventive_Maintenance_ID =? AND Asset_ID =? ORDER BY Modified_Meter ASC";
        PreparedStatement stmt = connection.prepareStatement(sql);
        
        stmt.setInt(1,meter);
        stmt.setString(2,PM_ID);
        stmt.setString(3,ASSET_ID);
        
        ResultSet rec = stmt.executeQuery();
        if(rec.first()){
            return rec.getInt("Modified_Meter");
        }
        else{
            throw new Exception("NO next highest meter found.");
        } 
    }
    
    public String getDateForMeter(int meter) throws Exception{
        String sql = "SELECT Modified_Date FROM scheduled_preventive_maintenance WHERE Modified_Meter = ? AND Preventive_Maintenance_ID =? AND Asset_ID =?";
        PreparedStatement stmt = connection.prepareStatement(sql); 
        stmt.setInt(1,meter);
        stmt.setString(2,PM_ID);
        stmt.setString(3,ASSET_ID);
        
        ResultSet rec = stmt.executeQuery();
        if(rec.first()){
            return rec.getString("Modified_Date");
        }
        else{
            throw new Exception("NO date found.");
        } 
    }
    
    public String getPlanEndDate()  throws Exception{
    
        PreparedStatement stmt_1 = connection.prepareStatement("SELECT PlanEndMeter,AverageMeterPerDay FROM asset WHERE asset_id=? AND PlanEndMeter>0");
        stmt_1.setString(1,ASSET_ID);
        ResultSet rec_1 = stmt_1.executeQuery();
        
        if(rec_1.first()){
            int endMeter = rec_1.getInt(1);
            int average = rec_1.getInt(2);
        
            PreparedStatement stmt_2 = connection.prepareStatement("SELECT StartingDate, StartingMeter FROM preventive_maintenance_register WHERE asset_id = ? AND Preventive_Maintenance_ID = ?");
            stmt_2.setString(1,ASSET_ID);
            stmt_2.setString(2,PM_ID);
            ResultSet rec_2 = stmt_2.executeQuery();
           
            rec_2.first();
            String startDate = rec_2.getString(1);
            
            int startMeter = rec_2.getInt(2);
            
            int planning_days = (endMeter - startMeter) / average;
            
            PreparedStatement preStatement=connection.prepareStatement("SELECT ADDDATE('"+ startDate +"'," + planning_days + ")");
            ResultSet resultSet = preStatement.executeQuery();
            resultSet.first();
            String ed = resultSet.getString(1);
            
            return ed;
            
        }
        else{
            PreparedStatement pS=connection.prepareStatement("SELECT PlanEndDate FROM asset WHERE asset_id=?");
            pS.setString(1,ASSET_ID);
            ResultSet rS = pS.executeQuery();
            rS.first();
            return rS.getString(1);
        }      
    }
}

