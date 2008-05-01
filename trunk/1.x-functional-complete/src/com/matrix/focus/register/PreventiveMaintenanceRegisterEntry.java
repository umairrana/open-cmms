package com.matrix.focus.register;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class PreventiveMaintenanceRegisterEntry {
    private Connection connection;
    
    public String preventive_maintenance_id;
    public String asset_id;
    public String basis;
    public String cycle_time;
    public int cycle_meter;
    public String criticalness;
    public String standard_time;
    public int tolerance;
    public int starting_meter;
    public String starting_date;
    public boolean plan;
    
    public PreventiveMaintenanceRegisterEntry(Connection con) {
        connection = con;
    }
    
    public boolean save(){
        String sql = "INSERT INTO preventive_maintenance_register (" +
                                      "Preventive_Maintenance_ID, " +
                                      "Asset_ID, " +
                                      "Standard_Time, " +
                                      "Tolerance, " +
                                      "Basis, " +
                                      "CycleTime, " +
                                      "CycleMeter, " +
                                      "Criticalness, " +
                                      "StartingMeter, " +
                                      "StartingDate, " +
                                      "Plan) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,preventive_maintenance_id);
            stmt.setString(2,asset_id);
            stmt.setString(3,standard_time);
            stmt.setInt(4,tolerance);
            stmt.setString(5,basis);
            stmt.setString(6,cycle_time);
            stmt.setInt(7,cycle_meter);
            stmt.setString(8,criticalness);
            stmt.setInt(9,starting_meter);
            stmt.setString(10,starting_date);
            stmt.setString(11,plan+"");
            
            if(stmt.executeUpdate()==1){
                return true;
            }
            else{
                return false;
            }
        } 
        catch(Exception ex){
            //ex.printStackTrace();
            return false;
        } 
    }
    
    public boolean update(){
        String sql = "UPDATE preventive_maintenance_register SET " +
                                      "Standard_Time=?, " +
                                      "Tolerance=?, " +
                                      "Basis=?, " +
                                      "CycleTime=?, " +
                                      "CycleMeter=?, " +
                                      "Criticalness=?, " +
                                      "StartingMeter=?, " +
                                      "StartingDate=?, " +
                                      "Plan=?" +
                    "WHERE Preventive_Maintenance_ID =? AND Asset_ID =? ";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,standard_time);
            stmt.setInt(2,tolerance);
            stmt.setString(3,basis);
            stmt.setString(4,cycle_time);
            stmt.setInt(5,cycle_meter);
            stmt.setString(6,criticalness);
            stmt.setInt(7,starting_meter);
            stmt.setString(8,starting_date);
            stmt.setString(9,plan+"");
            stmt.setString(10,preventive_maintenance_id);
            stmt.setString(11,asset_id);
            
            if(stmt.executeUpdate()==1){
                return true;
            }
            else{
                return false;
            }
        } 
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        } 
    }
    
    public boolean delete(){
        String sql = "DELETE FROM preventive_maintenance_register  WHERE Preventive_Maintenance_ID=? AND Asset_ID=?";
        
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,preventive_maintenance_id);
            stmt.setString(2,asset_id);
            
            if(stmt.executeUpdate()==1){
                return true;
            }
            else{
                return false;
            }
        } 
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        } 
    }
}
