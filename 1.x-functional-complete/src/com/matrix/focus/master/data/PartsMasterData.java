package com.matrix.focus.master.data;

import com.matrix.focus.master.entity.Part;
import com.matrix.focus.master.entity.PartsMaster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class PartsMasterData {

    public static Vector<PartsMaster> getParts(Connection connection) throws Exception{
	String sql = "SELECT p.Part_ID, p.Description, p.Authorization, p.Remarks, p.Creator, p.Created_Date, p.Updater, p.Updated_Date FROM parts_master p";
	try{
	    PreparedStatement stmt = connection.prepareStatement(sql);
            
	    ResultSet rec = stmt.executeQuery();
	    Vector<PartsMaster> parts = new Vector();
	    PartsMaster part = null;
	    while(rec.next()){
		part = new PartsMaster(
				rec.getString("Part_ID"),
				rec.getString("Description"),
				rec.getString("Authorization"),
				rec.getString("Remarks"),
				rec.getString("Creator"),
				rec.getString("Created_Date").substring(0,19),
				rec.getString("Updater"),
				rec.getString("Updated_Date").substring(0,19)
				);
		parts.add(part);
	    }
	    return parts;
	} 
	catch(SQLException s){
	    s.printStackTrace();
	    throw new Exception("Error on retrieving parts master details.");
	}
	catch(Exception e){
	    e.printStackTrace();
	    throw new Exception(e.getMessage());
	}  
    }
    
    public static void updatePartRemarks(PartsMaster part, Connection connection) throws Exception{
	String sql = "UPDATE parts_master SET Remarks=?" +
		     "WHERE " +
			"Part_ID =?";
	try{
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setString(1,part.remarks);
	    stmt.setString(2,part.partId);
            
	    if(stmt.executeUpdate()!=1){
		throw new Exception("Part remarks not updated.");
	    }
            
	} 
	catch(SQLException s){
	    s.printStackTrace();
	    throw new Exception("Error on updating part remarks details in parts master.");
	}
	catch(Exception e){
	    e.printStackTrace();
	    throw new Exception(e.getMessage());
	}
    }

    public static void updatePartRemarksAndAuthorization(PartsMaster part, Connection connection) throws Exception{
        String sql = "UPDATE parts_master SET Remarks=?, Authorization =?, Updater=?, Updated_Date= CURRENT_TIMESTAMP()  " +
                     "WHERE " +
                        "Part_ID =?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,part.remarks);
            stmt.setString(2,part.authorization);         
            stmt.setString(3,part.lastUpdater);
            stmt.setString(4,part.partId);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Part remarks not updated.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on updating part remarks details in parts master.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
       
    public static void insertPart(PartsMaster part, Connection connection) throws Exception{
	String sql = "INSERT INTO parts_master " +
	"(Part_ID, Description, Remarks, Creator, Created_Date) VALUES " +
	"(?,?,?,?, CURRENT_TIMESTAMP())";
	try{
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setString(1,part.getPartId());
	    stmt.setString(2,part.getDescription());
	    stmt.setString(3,part.getRemarks());
	    stmt.setString(4,part.getCreator());
	    if(stmt.executeUpdate()!=1){
		throw new Exception("Part not inserted to Parts Master.");
	    }
	} 
	catch(SQLException s){
	    s.printStackTrace();
	    throw new Exception("Error in inserting part to Part Master.");
	}
	catch(Exception e){
	    e.printStackTrace();
	    throw new Exception(e.getMessage());
	}
    }
    
    public static PartsMaster getPart(String partId, Connection connection) throws Exception{
	String sql = "SELECT Part_ID FROM part  WHERE Part_ID=? ";

	try{
	    PreparedStatement stmt = connection.prepareStatement(sql);
	    stmt.setString(1,partId.trim());
            
	    ResultSet rec = stmt.executeQuery();
	    if(rec.first()){
		return new PartsMaster(
				rec.getString("Part_ID"),
				rec.getString("Description"),
				rec.getString("Authorization"),
				rec.getString("Remarks"),
				rec.getString("Creator"),
				rec.getString("Created_Date").substring(0,19),
				rec.getString("Updater"),
				rec.getString("Updated_Date").substring(0,19)
				);
	    }
	    else{
		throw new Exception("Part master details not found.");
	    }
	} 
	catch(SQLException s){
	    //s.printStackTrace();
	    throw new Exception("Error on retrieving part master details.");
	}
	catch(Exception e){
	    //e.printStackTrace();
	    throw new Exception(e.getMessage());
	}  
    }
    
    public static void deletePart(String partId, Connection connection) throws Exception{
        String sql = "DELETE from parts_master " +
                     "WHERE " +
                        "Part_ID =?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,partId);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Part not deleted.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on deleting part from parts master.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
}
