package com.matrix.focus.master.data;

import com.matrix.focus.master.entity.Division;
import com.matrix.focus.master.entity.Part;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class PartData {

    public static Vector<Part> getParts(Connection connection) throws Exception{
        //String sql = "SELECT *,get_part_allocated_amount(Part_ID,Brand,Supplier,Batch) AS Allocated_Qty FROM part";
        String sql = "SELECT Part_ID, Description, Unit, Brand, Supplier, Authorization, Remarks, Deleted, Unit_Price, Batch, IFNULL(Qty_At_Hand,0)  as Qty_At_Hand,'000' AS Allocated_Qty FROM part";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            
            ResultSet rec = stmt.executeQuery();
            Vector<Part> parts = new Vector();
            Part part = null;
            while(rec.next()){
                part = new Part(
                                rec.getString("Part_ID"),
                                rec.getString("Description"),
                                rec.getString("Unit"),
                                rec.getString("Brand"),
                                rec.getString("Supplier"),
                                rec.getString("Batch"),
                                rec.getString("Unit_Price"),
                                (rec.getString("Authorization").equals("YES")?true:false),
                                rec.getString("Qty_At_Hand"),
                                rec.getString("Allocated_Qty"),
                                "0",
                                rec.getString("Remarks")
                                );
                part.available_qty = getAvailableQty(part.qty_at_hand,part.allocated_qty);
                parts.add(part);
            }
            return parts;
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on retrieving parts details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }  
    }
    
    public static void updatePartRemarks(Part part, Connection connection) throws Exception{
        String sql = "UPDATE part SET Remarks=?" +
                     "WHERE " +
                        "Part_ID =? AND " +
                        "Brand =? AND " +
                        "Supplier =? AND " +
                        "Batch =?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,part.remarks);
            stmt.setString(2,part.id);
            stmt.setString(3,part.brand);
            stmt.setString(4,part.supplier);
            stmt.setString(5,part.batch);
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Part remarks not updated.");
            }
            
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on updating part remarks details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    private static String getAvailableQty(String qty_at_hand, String qty_allocated){
        double at_h = Double.parseDouble(qty_at_hand);
        double used = Double.parseDouble(qty_allocated);
        if(at_h>=used){
            return (at_h - used) + "";
        }
        else{
            return "0";
        }
    }
    
    public static void savePart(Part part, Connection connection) throws Exception{
        String sql = "INSERT INTO part " +
        "(Part_ID, Description, Unit, Brand, Supplier, Batch, Unit_Price, Authorization, Qty_At_Hand, Remarks, Deleted) VALUES " +
        "(?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,part.getID());
            stmt.setString(2,part.getDescription());
            stmt.setString(3,part.getUnit());
            stmt.setString(4,part.getBrand());
            stmt.setString(5,part.getSupplier());
            stmt.setString(6,part.getBatch());
            stmt.setString(7,part.getUnitPrice());
            stmt.setString(8,part.getNeedApproval());
            stmt.setString(9,part.getQtyAtHand());
            stmt.setString(10,"");
            stmt.setString(11,"false");
            if(stmt.executeUpdate()!=1){
                throw new Exception("New Part details not saved.");
            }
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on saving new Part details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }
    
    public static void updatePart(Part part, Connection connection) throws Exception{
        String sql = "UPDATE part SET ";
              sql += "Description=?, ";
              sql += "Unit=?, ";
              sql += "Unit_Price=?, Qty_At_Hand=?, Remarks=? " + 
                     "WHERE " +
                       "Part_ID =? AND " +
                       "Brand =? AND " +
                       "Supplier =? AND " +
                       "Batch =?";

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1,part.getDescription());
            stmt.setString(2,part.getUnit());
            stmt.setString(3,part.getUnitPrice());
            stmt.setString(4,part.getQtyAtHand());
            stmt.setString(5,"");   
            stmt.setString(6,part.getID());
            stmt.setString(7,part.getBrand());
            stmt.setString(8,part.getSupplier());
            stmt.setString(9,part.getBatch());
            
            if(stmt.executeUpdate()!=1){
                throw new Exception("Part details not updated.");
            }  
        } 
        catch(SQLException s){
            s.printStackTrace();
            throw new Exception("Error on updating part details.");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public static Part getPart(String part_id,String brand,String supplier,String batch,Connection connection) throws Exception{
        String sql = "SELECT Part_ID FROM part  WHERE Part_ID=? AND  Brand=?  AND  Supplier=?  AND  Batch=?";

        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,part_id.trim());
            stmt.setString(2,brand.trim());
            stmt.setString(3,supplier.trim());
            stmt.setString(4,batch.trim());
            
            //System.out.println(part_id+ " - "+brand+ " - "+supplier+ " - "+batch);
            
            ResultSet rec = stmt.executeQuery();
            if(rec.first()){
                return new Part(rec.getString("Part_ID"),"","","","","","",false,"","","","");
            /*  String id,
                String description,
                String unit,
                String brand,
                String supplier,
                String batch, 
                String unit_price,
                boolean needApproval,
                String qty_at_hand,
                String allocated_qty,
                String available_qty,
                String remarks      */
            }
            else{
                throw new Exception("Part details not found.");
            }
        } 
        catch(SQLException s){
            //s.printStackTrace();
            throw new Exception("Error on retrieving part details.");
        }
        catch(Exception e){
            //e.printStackTrace();
            throw new Exception(e.getMessage());
        }  
    }
}
