package com.matrix.focus.user;



import java.sql.Connection;
import java.sql.Statement;

public class UserRole{
    private Connection connection;
    
    public String username;
    public String role_name;
    public String active;
    public String date_assigned;
    public String creater;
    
    public UserRole(Connection con){
        this.connection = con;
    }
    
    public boolean save(){
        String sql = "INSERT INTO user_role " +
                        "(Username, Role_Name, Active, Date_Assigned, Creater) " +
                     "VALUES " +
                        "('"+username+"','"+role_name+"','"+active+"',NOW(),'"+creater+"')";
        try{
            Statement stmt = connection.createStatement();  
            if(stmt.executeUpdate(sql)>0){
                stmt.close();
                return true;
            }
            else{
                stmt.close();
                return false;
            }    
        } 
        catch(Exception ex){
            //ex.printStackTrace();
            return false;
        } 
    }
    
    public boolean update(){
        String sql = "UPDATE user_role SET " +
                        "Active ='"+active+ "'" +
                     "WHERE " +
                        "Username ='"+username+"'AND Role_Name ='"+role_name+"'";
        try{
            Statement stmt = connection.createStatement();  
            if(stmt.executeUpdate(sql)>0){
                stmt.close();
                return true;
            }
            else{
                stmt.close();
                return false;
            }    
        } 
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        } 
    }
    
    public boolean delete(){
        String sql = "DELETE FROM user_role " +
                            "WHERE " +
                                 "Username ='"+username+"'AND Role_Name ='"+role_name+"'";
        try{
            Statement stmt = connection.createStatement();  
            if(stmt.executeUpdate(sql)>0){
                stmt.close();
                return true;
            }
            else{
                stmt.close();
                return false;
            }    
        } 
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        } 
    }
    
}
