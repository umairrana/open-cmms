package com.matrix.focus.connect;

import com.matrix.focus.master.data.DivisionContactData;
import com.matrix.focus.master.data.DivisionData;
import com.matrix.focus.master.entity.DivisionContact;
import com.matrix.focus.mdi.messageBar.MessageBar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SyncCustomerContact {
    private DivisionContact divisioncontact;
    private Connection connection;
    private MessageBar messageBar;
       
    public SyncCustomerContact(Connection connection,MessageBar msgBar) {
        this.connection = connection;
        this.messageBar=msgBar;
        
    }
    
    public boolean syncCustomerContact(String division)  throws Exception{
    
        int update=0;
        int insert=0;
        
        divisioncontact = new DivisionContact();
        ODBCConnection odbc = new ODBCConnection("Matrix","","matrixit");
        Connection tagConnection = odbc.getConnection();
        String sql ="";
        System.out.println(division);
        try{
            /**Read from TAG*/
            if(division.trim().equals("ALL"))
                sql = "SELECT customer_contacts_t.CUST_CODE, customer_contacts_t.contact_person, customer_contacts_t.DesignationP, customer_contacts_t.Email, customer_contacts_t.DirectNo FROM customer_contacts_t ";
            else
                sql = "SELECT customer_contacts_t.CUST_CODE, customer_contacts_t.contact_person, customer_contacts_t.DesignationP, customer_contacts_t.Email, customer_contacts_t.DirectNo FROM customer_contacts_t  WHERE (((customer_contacts_t.CUST_CODE)='"+ division +"')) ";

            PreparedStatement stmt = tagConnection.prepareStatement(sql);
            ResultSet rec = stmt.executeQuery();              

            while(rec.next()){
               divisioncontact.setID(rec.getString("CUST_CODE"));
               divisioncontact.setCompany("Matrix");
               divisioncontact.setDepartment("Customers");
               divisioncontact.setContactPerson(rec.getString("contact_person"));
               divisioncontact.setTelephone(rec.getString("DirectNo"));
               divisioncontact.setEmail(rec.getString("Email"));
               divisioncontact.setDesignation(rec.getString("DesignationP"));

             /**Update MatrixFocus from TAG */            
               if(isSaved()){
                    DivisionContactData.updateDivisionContact(divisioncontact,connection);
                    update=update+1;
               }
               else{
                    DivisionContactData.saveDivisionContact(divisioncontact,connection);
                    insert=insert+1;
               }
              
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
    
    public boolean syncCustomerContactSilent(String division)  throws Exception{
        
        int update=0;
        int insert=0;
            
        divisioncontact = new DivisionContact();
        ODBCConnection odbc = new ODBCConnection("Matrix","","matrixit");
        Connection tagConnection = odbc.getConnection();
        System.out.println(division);
        try{        
            /**Read from TAG*/
            String sql = "SELECT customer_contacts_t.CUST_CODE, customer_contacts_t.contact_person, customer_contacts_t.DesignationP, customer_contacts_t.Email, customer_contacts_t.DirectNo FROM customer_contacts_t  WHERE (((customer_contacts_t.CUST_CODE)='"+ division +"')) ";
            PreparedStatement stmt = tagConnection.prepareStatement(sql);
            ResultSet rec = stmt.executeQuery();              

            while(rec.next()){
               divisioncontact.setID(rec.getString("CUST_CODE"));
               divisioncontact.setCompany("Matrix");
               divisioncontact.setDepartment("Customers");
               divisioncontact.setContactPerson(rec.getString("contact_person"));
               divisioncontact.setTelephone(rec.getString("DirectNo"));
               divisioncontact.setEmail(rec.getString("Email"));
               divisioncontact.setDesignation(rec.getString("DesignationP"));
                              

            /**Update MatrixFocus from TAG*/             
              if(isSaved()){
                   DivisionContactData.updateDivisionContact(divisioncontact,connection);
                   update=update+1;
              }
              else{
                   DivisionContactData.saveDivisionContact(divisioncontact,connection);
                   insert=insert+1;
              }
                 
            }
            
            tagConnection.close();
            System.out.println("Process Completed - "+insert+" no of record Inserted and "+update+" no of record Updated - ");
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Failed to Complete the Process. - Failed @ record "+(insert+update)+" - "+e.getMessage());
        }
        
        return true; 
    }
    
    private boolean isSaved(){
        boolean val= true;
        try {
            DivisionContact divCon = DivisionContactData.getDivisionContact("Matrix","Customers",divisioncontact.getID(),divisioncontact.getContactPerson(),connection);
            val = true;
        } 
        catch(Exception e){
            if(e.getMessage().toString().equals("Contact details not found.")){
                val = false;                
            }
        }
        return val;
    }
    
    /**
        if(isSaved())
            System.out.println("Update  - "+divisioncontact.getID()+" - "+divisioncontact.getContactPerson()+" - "+divisioncontact.getTelephone());
        else
           System.out.println("Insert  - "+divisioncontact.getID()+" - "+divisioncontact.getContactPerson()+" - "+divisioncontact.getTelephone()); 
    */
    
}
