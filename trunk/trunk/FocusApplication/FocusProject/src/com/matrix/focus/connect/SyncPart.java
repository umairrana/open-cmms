package com.matrix.focus.connect;

import com.matrix.focus.master.data.DivisionData;
import com.matrix.focus.master.data.PartData;
import com.matrix.focus.master.entity.Division;
import com.matrix.focus.master.entity.Part;
import com.matrix.focus.mdi.messageBar.MessageBar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SyncPart {

    private Part part;
    private Connection connection;
    private MessageBar messageBar;
       
    public SyncPart(Connection connection,MessageBar msgBar) {
        this.connection = connection;
        this.messageBar=msgBar;
        
    }
    
    public boolean sync() throws Exception{
    
        int update=0;
        int insert=0;
        part = new Part();
        ODBCConnection odbc = new ODBCConnection("Matrix","","matrixit");
        Connection tagConnection = odbc.getConnection();
        try{
            /**Read from TAG*/
            String sql = "SELECT items_t.PART_NO, items_t.DESCRIPTION, items_t.MESSURE, items_t.BRAND, suppliers_t.sup_name, stock_t.Cost, stock_t.B_Code, stock_t.QTY FROM (items_t INNER JOIN stock_t ON items_t.PART_NO = stock_t.PART_NO) INNER JOIN suppliers_t ON items_t.SUP_CODE = suppliers_t.sup_code";

            PreparedStatement stmt = tagConnection.prepareStatement(sql);
            ResultSet rec = stmt.executeQuery();              
            int i=0;
            while(rec.next()){
               part.setID(rec.getString("PART_NO"));
               part.setDescription(rec.getString("DESCRIPTION"));
               part.setUnit(rec.getString("MESSURE"));
               part.setBrand(rec.getString("BRAND"));
               part.setSupplier(rec.getString("sup_name"));
               part.setBatch(rec.getString("B_Code"));
               part.setUnitPrice(rec.getString("Cost"));
               part.setNeedApproval(false);
               part.setQtyAtHand(rec.getString("QTY")); 
               
             /**Update MatrixFocus from TAG  */  
              
               if(isSaved()){
                   PartData.updatePart(part,connection);
                   update=update+1;
               }
               else{
                   PartData.savePart(part,connection);
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
    
    private boolean isSaved(){
        boolean val= true;
        try {
            Part pt = PartData.getPart(part.getID(),part.getBrand(),part.getSupplier(),part.getBatch(),connection);
            val = true;
        } 
        catch(Exception e){
            if(e.getMessage().toString().equals("Part details not found.")){
                val = false;                
            }
        }
        return val;
    }
}

