package com.matrix.focus.user;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {
    private Connection connection;
    
    public String employee_id;
    public String username;
    public String name;
    public String title;
    public String password;
    public String confirm_password;
    public String active;
    public String date_created;
    public String creater;
    private String old_username;
    
    public User(Connection dbcon,String username) throws Exception{
        connection = dbcon;
        this.username = username;
        this.old_username = username;
        read();
    }
    
    public User(Connection dbcon){
        connection = dbcon;
    }
    
    public boolean save() throws Exception{
        if(employee_id.equals("")){
            throw new Exception("Employee ID is empty.");
        }
        else if(username.equals("")){
            throw new Exception("Username is empty.");
        }
        else if(username.length()<3){
            throw new Exception("Username must contain atleast 3 characters.");
        }
        else if(password.length()<3){
            throw new Exception("Password must contain atleast 3 characters.");
        }
        else if(!confirm_password.equals(password)){
            throw new Exception("Passwords do not match.");
        }
        else{
            try{
               String sql = "INSERT INTO user " +
                                        "(Employee_ID,Employee_Name,Employee_Title,Username, Password, Active, Date_Created, Creater, Deleted) " +
                                   "VALUES " +
                                        "('"+ employee_id +"','"+name+"','"+title+"','"+username+"','"+password+"','"+active+"',NOW(),'"+creater+"','false')"; 
                
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
    }
    
    public boolean update()  throws Exception{
        if(employee_id.equals("")){
            throw new Exception("Employee ID is empty.");
        }
        else if(username.equals("")){
            throw new Exception("Username is empty.");
        }
        else if(username.length()<3){
            throw new Exception("Username must contain atleast 3 characters.");
        }
        else if(password.length()<3){
            throw new Exception("Password must contain atleast 3 characters.");
        }
        else if(!confirm_password.equals(password)){
            throw new Exception("Passwords do not match.");
        }
        else{
            try{
                String sql = "UPDATE user SET " +
                                "Username ='"+username+"'," +
                                "Employee_Name ='"+name+"'," +
                                "Employee_Title ='"+title+"'," +
                                "Password ='"+password+"'," +
                                "Active ='"+active+"' " +
                             "WHERE " +
                                "Username ='"+ old_username +"'";
                                
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
            catch(Exception er){
                //ex.printStackTrace();
                return false;
            }
        }
    }
    
    public boolean changePassword()  throws Exception{
        if(password.length()<6){
            throw new Exception("Password must contain atleast 6 characters.");
        }
        else if(!confirm_password.equals(password)){
            throw new Exception("Passwords do not match.");
        }
        else{
            try{
                String sql = "UPDATE user SET " +
                                "Password ='"+password+"' " +
                             "WHERE " +
                                "Username ='"+ username +"'";
                                
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
            catch(Exception er){
                er.printStackTrace();
                return false;
            }
        }
    }
    
    public boolean delete(){
        try{
            String sql = "UPDATE user SET deleted ='true' WHERE Username ='"+ username +"'";
            
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
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        } 
        
    }
    
    private void read() throws Exception{
        //String sql = "SELECT u.Employee_ID, u.Username, e.Name, e.Title, u.Password, u.Active, u.Date_Created, u.Creater FROM user u, employee e WHERE u.Employee_ID = e.Employee_ID AND u.Username ='"+username+"' AND u.deleted = 'false'";
        String sql = "SELECT u.Employee_ID, u.Username, u.Employee_Name, u.Employee_Title, u.Password, u.Active, u.Date_Created, u.Creater FROM user u WHERE u.Username ='"+username+"' AND u.deleted = 'false'";
        ResultSet rec = connection.createStatement().executeQuery(sql);
        rec.first();
        employee_id = rec.getString("Employee_ID");
        username = rec.getString("Username");
        name = rec.getString("Employee_Name");
        title = rec.getString("Employee_Title");
        password = rec.getString("Password");
        active = rec.getString("Active");
        date_created = rec.getString("Date_Created").substring(0,19);
        creater = rec.getString("Creater");
    }
}
