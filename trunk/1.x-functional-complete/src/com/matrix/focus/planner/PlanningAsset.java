package com.matrix.focus.planner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class PlanningAsset{
    
    public String   ASSET_ID;
    public String   COMMISSIONED_DATE;
    public int      AVERAGE_METER_PER_DAY;
    public boolean  IS_PLANNING;
    private Connection connection;
    
    public PlanningAsset(String asset_id, Connection connection) throws Exception{
        this.ASSET_ID = asset_id;
        this.connection = connection;
        
        String sql = "SELECT AverageMeterPerDay, Planned FROM asset WHERE Asset_ID=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,ASSET_ID);
        
        ResultSet rec = stmt.executeQuery();
        
        if(rec.first()){
            AVERAGE_METER_PER_DAY = rec.getInt("AverageMeterPerDay");
            IS_PLANNING = rec.getBoolean("Planned");
        }
        else{
            throw new Exception("No Asset found for '"+asset_id+"'");
        }
    }
    
    public boolean hasRegisteredPMs() throws Exception{
        PreparedStatement pS=connection.prepareStatement("SELECT * FROM Preventive_Maintenance_Register WHERE asset_id=?");
        pS.setString(1,ASSET_ID);
        ResultSet rS = pS.executeQuery();
        if(rS.first()){
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean hasRegisteredPlanningPMs() throws Exception{
        PreparedStatement pS=connection.prepareStatement("SELECT * FROM Preventive_Maintenance_Register WHERE asset_id=? AND Plan='true'");
        pS.setString(1,ASSET_ID);
        ResultSet rS = pS.executeQuery();
        if(rS.first()){
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean hasRegistedMeterBasedPlanningPMs() throws Exception{
        PreparedStatement pS=connection.prepareStatement("SELECT * FROM Preventive_Maintenance_Register WHERE asset_id=? AND Plan='true' AND Basis='Meter Reading'");
        pS.setString(1,ASSET_ID);
        ResultSet rS = pS.executeQuery();
        if(rS.first()){
            return true;
        }
        else{
            return false;
        }
    }
    
    public void setAverageMeterPerDay(int avg)throws Exception{
        PreparedStatement p = connection.prepareStatement("Update asset SET AverageMeterPerDay=? WHERE asset_id=?");
        p.setInt(1,avg);
        p.setString(2,ASSET_ID);
        if(p.executeUpdate()!=1){
            throw new Exception("Average meter per day was not saved.");
        }
        else{
            this.AVERAGE_METER_PER_DAY = avg;
        }
    }
    
    public void setPlanEndDate(String end_date) throws Exception{
        PreparedStatement p = connection.prepareStatement("Update asset SET PlanEndDate=? WHERE asset_id=?");
        p.setString(1,end_date);
        p.setString(2,ASSET_ID);
        if(p.executeUpdate()!=1){
            throw new Exception("Plan end date was not saved.");
        }
    }
    
    public void setServicing(boolean servicing) throws Exception{
        PreparedStatement p = connection.prepareStatement("Update asset SET Planned=? WHERE asset_id=?");
        p.setString(1,servicing+"");
        p.setString(2,ASSET_ID);
        if(p.executeUpdate()!=1){
            throw new Exception("Servicing was not saved.");
        }
    }
    
    public void setPlanEndMeter(int end_meter) throws Exception{
        PreparedStatement p = connection.prepareStatement("Update asset SET PlanEndMeter=? WHERE asset_id=?");
        p.setInt(1,end_meter);
        p.setString(2,ASSET_ID);
        if(p.executeUpdate()!=1){
            throw new Exception("Average meter per day was not saved.");
        }
    }
    
    public void setPlanningBasis(String basis) throws Exception{
        PreparedStatement p = connection.prepareStatement("Update asset SET Planning_Basis=? WHERE asset_id=?");
        p.setString(1,basis);
        p.setString(2,ASSET_ID);
        if(p.executeUpdate()!=1){
            throw new Exception("Planning Basis was not saved.");
        }
    }
    
    public boolean hasPlannedPMs() throws Exception{
        PreparedStatement pS=connection.prepareStatement("SELECT * FROM scheduled_preventive_maintenance WHERE asset_id=?");
        pS.setString(1,ASSET_ID);
        ResultSet rS = pS.executeQuery();
        if(rS.first()){
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean hasPendingWorkOrders(){
        try{
            PreparedStatement pS = connection.prepareStatement(
            
            "SELECT         Scheduled_ID " +
                                 "FROM      scheduled_preventive_maintenance " +
                                 "WHERE     " +
                                
                                           "Asset_ID = ? " +
                                           "AND " +
                                           "Scheduled_ID IN (SELECT Scheduled_ID " +
                                                            "FROM   preventive_maintenance_log " +
                                                            "WHERE  Done_Date = '0000-00-00 00:00:00')"           
            
            );
            pS.setString(1,ASSET_ID);
            ResultSet r = pS.executeQuery();
            if(r.first()){
                return true;           
            }
            else{
                return false;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean hasDoneWorkOrders(){
    
        try{
            PreparedStatement pS = connection.prepareStatement(
            "SELECT    Scheduled_ID " +
                                 "FROM      scheduled_preventive_maintenance " +
                                 "WHERE     " +
                                           "Asset_ID = ? " +
                                           "AND " +
                                           "Scheduled_ID IN (SELECT Scheduled_ID " +
                                                            "FROM   preventive_maintenance_log " +
                                                            "WHERE  Done_Date != '0000-00-00 00:00:00')"
            );
            pS.setString(1,ASSET_ID);
            ResultSet r = pS.executeQuery();
            if(r.first()){
                return true;           
            }
            else{
                return false;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    
    public Vector<AssetPM> getAllPlannedAssetPMs() throws Exception{
        
        PreparedStatement stmt = connection.prepareStatement("SELECT preventive_maintenance_id FROM preventive_maintenance_register WHERE Plan ='true' AND asset_id=?");
        
        stmt.setString(1,ASSET_ID);
        ResultSet rec = stmt.executeQuery();
        
        Vector<AssetPM> plannedPMs = new Vector<AssetPM>();
        
        while(rec.next()){
            AssetPM mpm = new AssetPM(ASSET_ID,rec.getString("preventive_maintenance_id"),connection);
            if(mpm.isPlanned()){
                plannedPMs.add(mpm);
            }
        }
        return plannedPMs;
    }
    
    public Vector<AssetPM> getAllPlanningAssetPMs() throws Exception{
    
        PreparedStatement stmt = connection.prepareStatement("SELECT preventive_maintenance_id FROM preventive_maintenance_register WHERE Plan ='true' AND asset_id=?");
        stmt.setString(1,ASSET_ID);
        ResultSet rec = stmt.executeQuery();
        
        Vector<AssetPM> planningPMs = new Vector<AssetPM>();
        
        while(rec.next()){
            AssetPM mpm = new AssetPM(ASSET_ID,rec.getString("preventive_maintenance_id"),connection);
            planningPMs.add(mpm);
        }
        return planningPMs;
    }
    
    //Kantha on 24.02.2008
    public void deletePlan() throws Exception{
        Vector<AssetPM> plannedPMs = getAllPlannedAssetPMs();
        int size = plannedPMs.size();
        for(int i=0;i<size;i++){
            AssetPM mpm = plannedPMs.get(i);
            if(mpm.HAS_DONE_WORK_ORDERS){
                mpm.deletePlanFrom(mpm.LAST_DONE_DATE);
            }
            else{
                mpm.deleteCompletePlan();
            }
        }
    }
    
    //Kantha on 24.02.2008
    public void doFreshPlan() throws Exception{
        Vector<AssetPM> plannedPMs = getAllPlanningAssetPMs();
        int size = plannedPMs.size();
        for(int i=0;i<size;i++){
            AssetPM mpm = plannedPMs.get(i);
            mpm.planFrom(mpm.PLAN_START_DATE,mpm.PLAN_END_DATE,this.AVERAGE_METER_PER_DAY);
        }
        
    }
    
    //Kantha on 24.02.2008
    public void replan() throws Exception{
        Vector<AssetPM> plannedPMs = getAllPlanningAssetPMs();
        int size = plannedPMs.size();
        for(int i=0;i<size;i++){
            AssetPM mpm = plannedPMs.get(i);
            if(mpm.HAS_DONE_WORK_ORDERS){
                mpm.planFrom(mpm.LAST_DONE_DATE,mpm.PLAN_END_DATE,this.AVERAGE_METER_PER_DAY);
            }
            else{
                mpm.planFrom(mpm.PLAN_START_DATE,mpm.PLAN_END_DATE,this.AVERAGE_METER_PER_DAY);
            }
        }  
    }
    
    public void planOrExtend() throws Exception{
        Vector<AssetPM> plannedPMs = getAllPlanningAssetPMs();
        int size = plannedPMs.size();
        for(int i=0;i<size;i++){
            AssetPM mpm = plannedPMs.get(i);
            if(mpm.HAS_DONE_WORK_ORDERS){
                mpm.planFrom(mpm.LAST_DONE_DATE,mpm.PLAN_END_DATE,this.AVERAGE_METER_PER_DAY);
            }
            else{
                mpm.planFrom(mpm.PLAN_START_DATE,mpm.PLAN_END_DATE,this.AVERAGE_METER_PER_DAY);
            }
        }
    }
    
    public void reschedule() throws Exception{
        Vector<AssetPM> plannedPMs = getAllPlannedAssetPMs();
        int size = plannedPMs.size();
        for(int i=0;i<size;i++){
            AssetPM mpm = plannedPMs.get(i);
            if(mpm.HAS_DONE_WORK_ORDERS){
                mpm.deletePlanFrom(mpm.LAST_DONE_DATE);
                mpm.planFrom(mpm.LAST_DONE_DATE,getEndingDate(),AVERAGE_METER_PER_DAY);
            }
            else{
                mpm.deleteCompletePlan();
                mpm.planFrom(mpm.PLAN_START_DATE,getEndingDate(),AVERAGE_METER_PER_DAY);
            }
        }
    }
    
    public void extend() throws Exception{
        Vector<AssetPM> plannedPMs = getAllPlannedAssetPMs();
        int size = plannedPMs.size();
        for(int i=0;i<size;i++){
            AssetPM mpm = plannedPMs.get(i);
            mpm.planFrom(mpm.getLastScheduledDate(),mpm.PLAN_END_DATE,this.AVERAGE_METER_PER_DAY);
        }    
    }
    
    public void curtail() throws Exception{
        Vector<AssetPM> plannedPMs = getAllPlannedAssetPMs();
        int size = plannedPMs.size();
        for(int i=0;i<size;i++){
            AssetPM mpm = plannedPMs.get(i);
            mpm.deletePlanFrom(mpm.PLAN_END_DATE);
        }  
    }
    
    public int getCurrentMeter(String []currentDate) throws Exception{
        
            PreparedStatement pS=connection.prepareStatement("SELECT " +
                                                                "PML.Done_Date," +
                                                                "PML.Done_Meter " +
                                                            "FROM " +
                                                                "preventive_maintenance_log PML," +
                                                                "scheduled_preventive_maintenance SPM " +
                                                            "WHERE " +
                                                                "SPM.asset_id=? " +
                                                                "AND " +
                                                                "SPM.scheduled_ID = PML.scheduled_id " +
                                                                "AND "+
                                                                "PML.Done_Date != '0000-00-00 00:00:00' "+
                                                                "ORDER BY " +
                                                                "PML.Done_Meter DESC"
                                                            );
            pS.setString(1,ASSET_ID);
            ResultSet rS = pS.executeQuery();
            if(rS.first()){
            
                try {
                    currentDate[0] = rS.getString(1);
                }
                catch (Exception e) {
                    e.toString();
                }
                
                return rS.getInt(2);
            }
            else{
                String sql ="SELECT preventive_maintenance_register.StartingDate,preventive_maintenance_register.StartingMeter FROM preventive_maintenance_register WHERE preventive_maintenance_register.Asset_ID =  ? ORDER BY preventive_maintenance_register.StartingMeter ASC LIMIT 1";
                PreparedStatement pSt=connection.prepareStatement(sql);
                pSt.setString(1,ASSET_ID);
                ResultSet rSt = pSt.executeQuery();
                if(rSt.first()){
                    try {
                        currentDate[0] = rSt.getString(1);
                    }
                    catch (Exception e) {
                        e.toString();
                    }
                   
                    return rSt.getInt(2);
                }
                else{
                  throw new Exception("No Done Meter Reading");
                }
            }
        
    }
    
    public int getNewMeterAverage(int newMeter,String newDate)throws Exception{
    
        String currentDate[] = new String[1];
        int currentMeter = getCurrentMeter(currentDate);
        int dateDiff;
        int meterDiff;
        
        PreparedStatement preStatement=connection.prepareStatement("SELECT DATEDIFF('"+ newDate +"','" + currentDate[0] + "')");
        ResultSet resultSet = preStatement.executeQuery();
        resultSet.first();
        
        dateDiff = resultSet.getInt(1);
        meterDiff = newMeter - currentMeter;
        
        return meterDiff/dateDiff;
    }
    
    public String getEndingDate()  throws Exception{
    
        PreparedStatement pSt = connection.prepareStatement("SELECT PlanEndMeter FROM asset WHERE asset_id=? AND PlanEndMeter>0");
        pSt.setString(1,ASSET_ID);
        ResultSet rSt = pSt.executeQuery();
        
        if(rSt.first()){
            int eL = rSt.getInt(1);
            return getEndingDate(eL);
        }
        else{
            PreparedStatement pS=connection.prepareStatement("SELECT PlanEndDate FROM asset WHERE asset_Id=?");
            pS.setString(1,ASSET_ID);
            ResultSet rS = pS.executeQuery();
            rS.first();
            return rS.getString(1);
        }      
    }
    
    public String getEndingDate(int endingLimit)throws Exception{
    
        PreparedStatement p = connection.prepareStatement("SELECT AverageMeterPerDay FROM asset WHERE asset_id=?");
        p.setString(1,ASSET_ID);
        ResultSet r = p.executeQuery();
        r.first();
        int aV = r.getInt(1);
        int nDays = endingLimit/aV;
        
        PreparedStatement pStatement=connection.prepareStatement("SELECT Date_of_commission FROM aSSET WHERE ASSET_ID=?");
        pStatement.setString(1,ASSET_ID);
        ResultSet rSet = pStatement.executeQuery();
        rSet.first();
        
        PreparedStatement preStatement=connection.prepareStatement("SELECT ADDDATE('"+ rSet.getString(1) +"'," + nDays + ")");
        ResultSet resultSet = preStatement.executeQuery();
        resultSet.first();
        return resultSet.getString(1);
    }

    public void stopPlanningAllAssetPMs() throws Exception{
    
        String sql = "UPDATE preventive_maintenance_register SET plan = 'false' WHERE  asset_id =?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,ASSET_ID);
        if(0==stmt.executeUpdate()){
            throw new Exception("Could not set planning to stop.");
        }
    }
    
    public int getCalculatedAverage(int newMeter,String newDate) throws SQLException {

        PreparedStatement pS = connection.prepareStatement("SELECT getCalculatedAverage(?,?,?)");
        pS.setString(1,ASSET_ID);
        pS.setString(2,newDate);
        pS.setInt(3,newMeter);
        
        ResultSet rS = pS.executeQuery();
        rS.first();
        return rS.getInt(1);
    }
    
}
