package com.matrix.focus.connect;

import com.matrix.focus.master.data.DivisionData;
import com.matrix.focus.master.entity.Division;

import com.matrix.focus.mdi.messageBar.MessageBar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SyncCustomer {

    private Division division;
    SyncCustomerContact syncCustContact;  
    private Connection connection;
    private MessageBar messageBar;
       
    public SyncCustomer(Connection connection,MessageBar msgBar) {
        this.connection = connection;
        this.messageBar=msgBar;        
    }
    
    public boolean syncCustomer()  throws Exception{
    
        int update=0;
        int insert=0;
        
        division = new Division();
        syncCustContact =new SyncCustomerContact(connection,messageBar); 
        ODBCConnection odbc = new ODBCConnection("Matrix","","matrixit");
        Connection tagConnection = odbc.getConnection();
        try{
            /**Read from TAG*/
            String sql = "SELECT customer_t.CUST_CODE, customer_t.NAME, (customer_t.ADDRESS1+customer_t.ADDRESS2+customer_t.ADDRESS3) AS ADDRESS, customer_t.PHONE_NO, customer_t.FAX  FROM customer_t ";

            PreparedStatement stmt = tagConnection.prepareStatement(sql);
            ResultSet rec = stmt.executeQuery();              

            while(rec.next()){
               division.setID(rec.getString("CUST_CODE"));
               division.setCompany("Matrix");
               division.setDepartment("Customers");
               division.setName(rec.getString("NAME"));
               division.setTelephone(rec.getString("PHONE_NO"));
               division.setFax(rec.getString("FAX"));
               division.setContactPerson("");
               division.setEmail("");
               division.setRemarks(""); 
               division.setAddress(rec.getString("ADDRESS"));
               division.setDesignation("");
               division.setTravelTime("");              

            /**Update MatrixFocus - Division from TAG*/             
               if(isSaved()){
                   DivisionData.updateDivision(division,connection);
                   update=update+1;
               }
               else{
                   DivisionData.saveDivision(division,connection);
                   insert=insert+1;
               }
               
            /**Update MatrixFocus - Division Contact from TAG*/  
               syncCustContact.syncCustomerContactSilent(division.getID());
                
            }
            
            tagConnection.close();
            messageBar.setMessage("Process Completed - "+insert+" no of record Inserted and "+update+" no of record Updated - ","OK");
        }
        catch(Exception e){
            e.printStackTrace();
            messageBar.setMessage("Failed to Complete the Process. - Failed @ record "+(insert+update)+" - "+e.getMessage(),"ERROR");
        }
        
        return true; 
    }
    
    private boolean isSaved(){
        boolean val= true;
        try {
            Division div = DivisionData.getDivision("Matrix","Customers",division.getID(),connection);
            val = true;
        } 
        catch(Exception e){
            if(e.getMessage().toString().equals("Division details not found.")){
                val = false;                
            }
        }
        return val;
    }
}
