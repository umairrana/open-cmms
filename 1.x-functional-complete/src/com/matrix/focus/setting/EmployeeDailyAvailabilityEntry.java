package com.matrix.focus.setting;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EmployeeDailyAvailabilityEntry{
    
    public EmployeeDailyAvailabilityEntry(String emp, String date, Connection connection) throws Exception{
        String sql = "INSERT INTO employee_availability_daily (Employee_ID,Date) VALUES(?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,emp);
        stmt.setString(2,date);
        if(stmt.executeUpdate()!=1){
            throw new Exception("Could not save employee availability information");
        }
    }
    
    
    public static void setCheckInTime(String emp, String date, String checkInTime, Connection connection) throws Exception{
        String sql = "UPDATE employee_availability_daily SET Check_In_Time =? WHERE Employee_ID=? AND Date=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,checkInTime);
        stmt.setString(2,emp);
        stmt.setString(3,date);
        if(stmt.executeUpdate()!=1){
            throw new Exception("Could not save employee check in time information");
        }
    }
    
    public static void setCheckOutTime(String emp, String date, String checkOutTime, Connection connection) throws Exception{
        String sql = "UPDATE employee_availability_daily SET Check_Out_Time =? WHERE Employee_ID=? AND Date=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,checkOutTime);
        stmt.setString(2,emp);
        stmt.setString(3,date);
        if(stmt.executeUpdate()!=1){
            throw new Exception("Could not save employee check out time information");
        }
    }
    
    public static void delete(String emp, String date, Connection connection) throws Exception{
        String sql = "DELETE FROM employee_availability_daily WHERE Employee_ID=? AND Date=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,emp);
        stmt.setString(2,date);
        if(stmt.executeUpdate()!=1){
            throw new Exception("Could not delete employee availability information");
        }
    }
}
