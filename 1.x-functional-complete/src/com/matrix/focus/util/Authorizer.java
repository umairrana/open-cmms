package com.matrix.focus.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Authorizer{
    public static final String CUSTOMERS                        = "CUSTOMERS";
    public static final String SALES                            = "SALES";
    public static final String MACHINE_CATEGORY                 = "MACHINE CATEGORY";
    public static final String MACHINE_MODEL                    = "MACHINE MODEL";
    public static final String PREVENTIVE_MAINTENANCES_MASTER   = "PREVENTIVE MAINTENANCES MASTER";
    public static final String READINGS_MASTER                  = "READINGS MASTER";
    public static final String INSPECTIONS_MASTER               = "INSPECTIONS MASTER";
    public static final String PARTS_INVENTORY                  = "PARTS INVENTORY";
    public static final String EMPLOYEE                         = "EMPLOYEE";
    public static final String EMPLOYEE_RATES                   = "EMPLOYEE RATES";
    public static final String EMPLOYEE_AVAILABLITY             = "EMPLOYEE AVAILABLITY";
    public static final String PREVENTIVE_MAINTENANCE_REGISTER  = "PREVENTIVE MAINTENANCE REGISTER";
    public static final String READINGS_REGISTER                = "READINGS REGISTER";
    public static final String INSPECTIONS_REGISTER             = "INSPECTIONS REGISTER";
    public static final String PLANNING                         = "PLANNING";
    public static final String JOB_CREATION                     = "JOB CREATION";
    public static final String JOB_COMPLETION                   = "JOB COMPLETION";
    public static final String JOB_CONFIRMATION                 = "JOB CONFIRMATION";
    public static final String EMPLOYEE_SCHEDULE                = "EMPLOYEE SCHEDULE";
    public static final String USERS_MANAGEMENT                 = "USERS MANAGEMENT";
    
    public static final String REGISTER_ALL                     = "REGISTER_ALL";
    
    public static boolean isCapable(String username,String role,Connection connection){
        try{
            String sql = "SELECT " +
                            "Active " +
                         "FROM " +
                            "user_role " +
                         "WHERE " +
                            "Username =? " +
                            "AND " +
                            "Role_Name =?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,username);
            stmt.setString(2,role);
            
            ResultSet rec = stmt.executeQuery();
            rec.first();
            return rec.getBoolean("Active");
        } 
        catch (Exception ex){
            //ex.printStackTrace();
            return false;
        } 
    }
}
