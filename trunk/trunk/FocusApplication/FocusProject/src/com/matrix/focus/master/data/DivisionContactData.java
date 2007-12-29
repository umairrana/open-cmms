package com.matrix.focus.master.data;

import com.matrix.focus.master.entity.DivisionContact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionContactData {

    public static void saveDivisionContact(DivisionContact divisionContact, Connection connection) throws Exception{
        String sql = "INSERT INTO division_contacts" +
        "(Comp_ID, Dept_ID, Division_ID,Contact_Person,Designation,Email, Direct_Phone_No) VALUES " +
        "(?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,divisionContact.getCompany());
            stmt.setString(2,divisionContact.getDepartment());
            stmt.setString(3,divisionContact.getID());
            stmt.setString(4,divisionContact.getContactPerson());
            stmt.setString(5,divisionContact.getDesignation());
            stmt.setString(6,divisionContact.getEmail());
            stmt.setString(7,divisionContact.getTelephone());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("New Contact details not saved.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on saving new contact details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void updateDivisionContact(DivisionContact divisionContact, Connection connection) throws Exception{
        String sql = "UPDATE division_contacts SET ";
                sql += "designation=?, ";
                sql += "Email=?, ";
                sql += "Direct_Phone_No=?  "+
              "WHERE Comp_ID=? AND Dept_ID=? AND Division_ID=? AND Contact_Person=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,divisionContact.getDesignation());
            stmt.setString(2,divisionContact.getEmail());
            stmt.setString(3,divisionContact.getTelephone());
            
            stmt.setString(4,divisionContact.getCompany());
            stmt.setString(5,divisionContact.getDepartment());
            stmt.setString(6,divisionContact.getID());
            stmt.setString(7,divisionContact.getContactPerson());
          
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Contact details not updated.");
            }  
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on updating contact details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void deleteDivisionContact(String company_id, String department_id, String divition_id,String contact_Person, Connection connection) throws Exception{
        String sql = "DELETE FROM division_contacts WHERE Comp_ID=? AND Dept_ID=? AND Division_ID=?  AND Contact_Person=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,company_id);
            stmt.setString(2,department_id);
            stmt.setString(3,divition_id);
            stmt.setString(4,contact_Person);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Contact details not deleted.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on deleting contact details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static DivisionContact getDivisionContact(String company_id, String department_id, String divition_id,String contact_Person, Connection connection) throws Exception{
        String sql = "SELECT * FROM division_contacts WHERE Comp_ID=? AND Dept_ID=? AND Division_ID=? AND Contact_Person=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,company_id);
            stmt.setString(2,department_id);
            stmt.setString(3,divition_id);
            stmt.setString(4,contact_Person);
            
            ResultSet rec = stmt.executeQuery();
            if(rec.first()){
                return new DivisionContact(
                    rec.getString("Division_ID"),
                    rec.getString("Comp_ID"),
                    rec.getString("Dept_ID"),
                    rec.getString("Contact_Person"),
                    rec.getString("Designation"),
                    rec.getString("Email"),
                    rec.getString("Direct_Phone_No") );
            }
            else{
                throw new Exception("Contact details not found.");
            }
        } 
        catch(SQLException s){
            //s.printStackTrace();
            throw new Exception("Error on retrieving contact details.");
        }
        catch(Exception e){
            //e.printStackTrace();
            throw new Exception(e.getMessage());
        }  
    }
}
