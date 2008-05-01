package com.matrix.focus.setting;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EmployeePeriodAvailabilityEntry{
    
    public EmployeePeriodAvailabilityEntry(String emp, String check_in_date, Connection connection) throws Exception{
        String sql = "INSERT INTO employee_availability_period (Employee_ID,Check_In_Date) VALUES(?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,emp);
        stmt.setString(2,check_in_date);
        if(stmt.executeUpdate()!=1){
            throw new Exception("Could not save employee availability information");
        }
    }
    
    public static void setCheckOutDate(String emp, String check_in_date, String checkOutDate, Connection connection) throws Exception{
        String sql = "UPDATE employee_availability_period SET Check_Out_Date =? WHERE Employee_ID=? AND Check_In_Date=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,checkOutDate);
        stmt.setString(2,emp);
        stmt.setString(3,check_in_date);
        if(stmt.executeUpdate()!=1){
            throw new Exception("Could not save employee check out date information");
        }
    }
    
    public static void setCheckInTime(String emp, String check_in_date, String checkInTime, Connection connection) throws Exception{
        String sql = "UPDATE employee_availability_period SET Check_In_Time =? WHERE Employee_ID=? AND Check_In_Date=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,checkInTime);
        stmt.setString(2,emp);
        stmt.setString(3,check_in_date);
        if(stmt.executeUpdate()!=1){
            throw new Exception("Could not save employee check in time information");
        }
    }
    
    public static void setCheckOutTime(String emp, String check_in_date, String checkOutTime, Connection connection) throws Exception{
        String sql = "UPDATE employee_availability_period SET Check_Out_Time =? WHERE Employee_ID=? AND Check_In_Date=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,checkOutTime);
        stmt.setString(2,emp);
        stmt.setString(3,check_in_date);
        if(stmt.executeUpdate()!=1){
            throw new Exception("Could not save employee check out time information");
        }
    }
    
    public static void delete(String emp, String check_in_date, Connection connection) throws Exception{
        String sql = "DELETE FROM employee_availability_period WHERE Employee_ID=? AND Check_In_Date=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,emp);
        stmt.setString(2,check_in_date);
        if(stmt.executeUpdate()!=1){
            throw new Exception("Could not delete employee availability information");
        }
    }
}

