/********************************************************
 *  Class   = LabourAvailability.java
 *  Details = To get the labour availability basically from excel files
 *  Author  = Himesha
 *  Date    = 2007.01.09
 * ******************************************************/
 
package com.matrix.focus.util;

 import java.sql.Connection;
 import java.sql.Statement;
 import java.sql.ResultSet;
 import java.sql.SQLException;

public class LabourAvailability {

    private Connection conn;
    
    public LabourAvailability(Connection dbConn) {
        conn = dbConn;
    }
    
    public String[] getAvailabilityOf(String empID, String targetDate){
        String info[] = new String[2]; //Returing array
        Statement stmt; // SQL statement object 
        String query;   // SQL select string
        ResultSet rs;   // SQL query results 
        boolean more;   // "more rows found" switch
         
        query = "SELECT Check_In_Time, Check_Out_Time "
               + "FROM employee_availability_daily "
               + "WHERE Employee_ID = '" + empID + "' AND `Date` = '" + targetDate + "'";
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);            
            // Check to see if any rows were read
            more = rs.first(); 
            if (!more) {//No records in the employee_availability_daily table        
                query = "SELECT Check_In_Time, Check_Out_Time "
                       + "FROM employee_availability_period "
                       + "WHERE Employee_ID = '" + empID + "' AND '" + targetDate + "' BETWEEN Check_In_Date AND Check_Out_Date";     
                
                rs = stmt.executeQuery(query);
                more = rs.first(); 
                if (!more) {//No records in both tables
                    info[0] ="00:00";
                    info[1] ="24:00";
                }else{//Record found in the employee_availability_period table
                    info[0] = rs.getString("Check_In_Time");
                    info[1] = rs.getString("Check_Out_Time"); 
                }
            }else{//Record found in the employee_availability_daily table
                info[0] = rs.getString("Check_In_Time"); 
                info[1] = rs.getString("Check_Out_Time"); 
            }               
            rs.close(); 
            stmt.close();
            } catch (SQLException e) {
                System.out.println("<LabourAvailability.java>" + e);
            }  
        return info;
    }
}
