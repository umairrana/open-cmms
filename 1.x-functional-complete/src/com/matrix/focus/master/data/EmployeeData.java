package com.matrix.focus.master.data;

import com.matrix.focus.master.entity.Employee;
import com.matrix.focus.master.entity.EmployeeSkill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class EmployeeData{
    public static void saveEmployee(Employee emp, Connection connection)throws Exception{
        String sql = "INSERT INTO employee" +
        "(Employee_ID, Name, Title, Employee_Type, Skill_Level, Email, Mobile_No) VALUES " +
        "(?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,emp.getEmployeeID());
            stmt.setString(2,emp.getName());
            stmt.setString(3,emp.getJobTitle());
            stmt.setString(4,emp.getType());
            stmt.setString(5,emp.getSkillLevel());
            stmt.setString(6,emp.getEmail());
            stmt.setString(7,emp.getMobilephone());
            
            saveEmployeeSkill(emp.getEmployeeSkill(),connection);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("New employee details not saved.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on saving new employee details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void updateEmployee(Employee emp, Connection connection)throws Exception{
        String sql = "UPDATE employee SET ";
                sql += "Name=?, ";
                sql += "Title=?, ";
                sql += "Employee_Type=?, ";
                sql += "Skill_Level=?, ";
                sql += "Email=?, ";
                sql += "Mobile_No=? WHERE Employee_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,emp.getName());
            stmt.setString(2,emp.getJobTitle());
            stmt.setString(3,emp.getType());
            stmt.setString(4,emp.getSkillLevel());
            stmt.setString(5,emp.getEmail());
            stmt.setString(6,emp.getMobilephone());
            stmt.setString(7,emp.getEmployeeID());
            
            deleteEmployeeSkill(emp.getEmployeeID(),connection);
            saveEmployeeSkill(emp.getEmployeeSkill(),connection);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Employee details not updated.");
            }  
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on updating employee details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static Employee getEmployee(String emp_id,Connection connection)throws Exception{
        String sql = "SELECT * FROM employee WHERE Employee_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,emp_id);
            
            ResultSet rec = stmt.executeQuery();
            if(rec.first()){
                return new Employee(
                    rec.getString("Employee_ID"),
                    rec.getString("Name"),
                    rec.getString("Title"),
                    rec.getString("Employee_Type"),
                    rec.getString("Skill_Level"),
                    rec.getString("Email"),
                    rec.getString("Mobile_No"),
                    getEmployeeSkills(emp_id,connection)
                );
            }
            else{
                throw new Exception("Employee details not found.");
            }
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on retrieving Employee details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  
    }
    
    public static void deleteEmployee(String emp_id,Connection connection)throws Exception{
        String sql = "DELETE FROM employee WHERE Employee_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,emp_id);
                
            if(stmt.executeUpdate()==-1){
                throw new Exception("Employee details not deleted.");
            }
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on deleting employee details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    private static void saveEmployeeSkill(Vector<EmployeeSkill> skills, Connection connection)throws Exception{
        String sql = "INSERT INTO employee_skills" +
        "(Employee_ID, Maintenance_Category, Proficiency) VALUES " +
        "(?,?,?)";
        try{
            int size = skills.size();
            for(int i=0;i<size;i++){
                PreparedStatement stmt = connection.prepareStatement(sql);
                EmployeeSkill skill = skills.get(i);
                stmt.setString(1,skill.getEmployeeId());
                stmt.setString(2,skill.getSkill());
                stmt.setInt(3,skill.getProficiency());
                
                if(stmt.executeUpdate()!=1){
                    throw new Exception("Employee skill not saved.");
                }
            }
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on saving employee skill.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    private static Vector<EmployeeSkill> getEmployeeSkills(String emp_id, Connection connection)throws Exception{
        String sql = "SELECT * FROM employee_skills WHERE Employee_ID=? ORDER BY Proficiency";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,emp_id);
            
            ResultSet rec = stmt.executeQuery();
            Vector<EmployeeSkill> skills = new Vector();
            while(rec.next()){
                skills.add(
                    new EmployeeSkill(
                        rec.getString("Employee_ID"),
                        rec.getString("Maintenance_Category"),
                        rec.getInt("Proficiency")
                    )
                );
            }
            return skills;
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on retrieving Employee details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  
    }
    
    private static void deleteEmployeeSkill(String emp_id, Connection connection)throws Exception{
        String sql = "DELETE FROM employee_skills WHERE Employee_ID=?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,emp_id);
                
            if(stmt.executeUpdate()==-1){
                throw new Exception("Employee skill not deleted.");
            }
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on deleting employee skill.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
}
