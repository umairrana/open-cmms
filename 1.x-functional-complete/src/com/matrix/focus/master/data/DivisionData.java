package com.matrix.focus.master.data;

import com.matrix.focus.master.entity.Division;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DivisionData {

    public static void saveDivision(Division division, Connection connection) throws Exception{
        String sql = "INSERT INTO division" +
        "(Comp_ID, Dept_ID, Division_ID, Name, Telephone_No, Fax_No, Contact_Person, Email, Remarks,Address,Designation,Travel_Time) VALUES " +
        "(?,?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,division.getCompany());
            stmt.setString(2,division.getDepartment());
            stmt.setString(3,division.getID());
            stmt.setString(4,division.getName());
            stmt.setString(5,division.getTelephone());
            stmt.setString(6,division.getFax());
            stmt.setString(7,division.getContactPerson());
            stmt.setString(8,division.getEmail());
            stmt.setString(9,division.getRemarks());
            stmt.setString(10,division.getAddress());
            stmt.setString(11,division.getDesignation());
            stmt.setString(12,division.getTravelTime());
            if(stmt.executeUpdate()!=1){
                throw new Exception("New department details not saved.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on saving new department details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void updateDivision(Division division, Connection connection) throws Exception{
        String sql = "UPDATE division SET ";
              sql += "Name=?, ";
              sql += "Telephone_No=?, ";
              sql += "Fax_No=?, ";
              sql += "Contact_Person=?, ";
              sql += "Email=?, ";
              sql += "Remarks=? , Address=?, designation=?, travel_time=? " +
              "WHERE Comp_ID=? AND Dept_ID=? AND Division_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,division.getName());
            stmt.setString(2,division.getTelephone());
            stmt.setString(3,division.getFax());
            stmt.setString(4,division.getContactPerson());
            stmt.setString(5,division.getEmail());
            stmt.setString(6,division.getRemarks());
            stmt.setString(7,division.getAddress());
            stmt.setString(8,division.getDesignation());
            stmt.setString(9,division.getTravelTime());
            
            stmt.setString(10,division.getCompany());
            stmt.setString(11,division.getDepartment());
            stmt.setString(12,division.getID());
          
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Department details not updated.");
            }  
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on updating department details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void deleteDivision(String company_id, String department_id, String divition_id, Connection connection) throws Exception{
        String sql = "DELETE FROM division WHERE Comp_ID=? AND Dept_ID=? AND Division_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,company_id);
            stmt.setString(2,department_id);
            stmt.setString(3,divition_id);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Division details not deleted.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on deleting division details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static Division getDivision(String company_id, String department_id, String divition_id, Connection connection) throws Exception{
        String sql = "SELECT * FROM division WHERE Comp_ID=? AND Dept_ID=? AND Division_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,company_id);
            stmt.setString(2,department_id);
            stmt.setString(3,divition_id);
            
            ResultSet rec = stmt.executeQuery();
            if(rec.first()){
                return new Division(
                    rec.getString("Division_ID"),
                    rec.getString("Comp_ID"),
                    rec.getString("Dept_ID"),
                    rec.getString("Name"),
                    rec.getString("Telephone_No"),
                    rec.getString("Fax_No"),
                    rec.getString("Contact_Person"),
                    rec.getString("Email"),
                    rec.getString("Remarks"),
                    rec.getString("Address"),
                    rec.getString("Designation"),
                    rec.getString("Travel_Time")
                );
            }
            else{
                throw new Exception("Division details not found.");
            }
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on retrieving division details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  
    }
}
