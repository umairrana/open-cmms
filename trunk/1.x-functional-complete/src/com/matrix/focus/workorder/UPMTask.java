package com.matrix.focus.workorder;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UPMTask{
    public String upm_id;
    public String description;
    public String category;
    private Connection connection;
  
    public UPMTask(Connection con){
        connection = con;
    }
  
    public void save() throws Exception{
        upm_id = getNextUPM();
        String sql = "INSERT INTO upmaintenance_master (" +
                                    "UPM_ID, " +
                                    "Description, " +
                                    "UPM_Category) " +
                    "VALUES(?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,upm_id);
        stmt.setString(2,description);
        stmt.setString(3,category);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("New task not saved.");
        }
    }
    
    public void update() throws Exception{
        String sql = "UPDATE upmaintenance_master SET " +
                                    "Description = ?, " +
                                    "UPM_Category = ? WHERE UPM_ID = ? ";
                                    
        PreparedStatement stmt = connection.prepareStatement(sql);
     
        stmt.setString(1,description);
        stmt.setString(2,category);
        stmt.setString(3,upm_id);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("Task not updated.");
        }
    }

    public void delete() throws Exception{
        String sql = "DELETE FROM upmaintenance_master WHERE UPM_ID=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,upm_id);
        
        if(stmt.executeUpdate()!=1){
            throw new Exception("New task not saved.");
        }
    }
    
    private String getNextUPM(){
        String sql = "SELECT MAX(CAST(SUBSTRING(UPM_ID,5) AS SIGNED))+1 FROM upmaintenance_master";
        try{
            ResultSet rec = connection.createStatement().executeQuery(sql);
            rec.next();
            return "UPM-".concat(rec.getString(1));
            
        }
        catch(Exception er){
            return "UPM-1";
        }
        
    } 
}
