/********************************************************
 *  Class   = LabourAvailabilityExcel.java
 *  Details = To get the labour availability basically from excel files
 *  Author  = Himesha
 *  Date    = 2006.07.15
 * ******************************************************/
package com.matrix.focus.util;

import java.util.Date;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class LabourAvailabilityExcel {

    //private ExcelConnection excelCon;
    private Connection conn;
    
    public LabourAvailabilityExcel() {
        //excelCon = new ExcelConnection("Labour_Force");
        //excelCon = new ExcelConnection("Focus_Connect_DS");
        //this.conn = excelCon.getConnection();
    }
    public String[] getAvailabilityOf(String empID, String targetDate){
        String info[] = new String[2]; //Returing array
        Statement stmt; // SQL statement object 
        String query;   // SQL select string
        ResultSet rs;   // SQL query results 
        boolean more;   // "more rows found" switch
         
        query = "SELECT From_Time, To_Time "
               + "FROM [Datewise$] "
               + "WHERE Employee_ID = '" + empID + "' AND Working_Date = '" + targetDate + "'";
         
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);            
            // Check to see if any rows were read
            more = rs.next(); 
            if (!more) {//No records in the Datewise table        
                query = "SELECT From_Time, To_Time "
                       + "FROM [Date_Periodwise$] "
                       + "WHERE Employee_ID = '" + empID + "' AND '" + targetDate + "' BETWEEN From_Date AND To_Date";     
                rs = stmt.executeQuery(query);
                more = rs.next(); 
                if (!more) {//No records in both tables
                    info[0] ="";
                    info[1] ="";
                }else{//Record found in the Date_Periodwise table
                    info[0] = rs.getString("From_Time");
                    info[1] = rs.getString("To_Time");  
                }
            }else{//Record found in the Datewise table
                info[0] = rs.getString("From_Time"); 
                info[0] = rs.getString("To_Time"); 
            }               
            rs.close(); 
            stmt.close();
            } catch (SQLException e) {
                System.out.println("<LabourAvailabilityExcel.java>" + e);
            }  
        return info;
    }
}
