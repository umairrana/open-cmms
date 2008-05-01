/********************************************************
 *  Class   = ChecklistLog.java
 *  Details = The checklist log entry
 *  Author  = Yasantha
 *  Date    = 2006.07.05
 * ******************************************************/

package com.matrix.focus.log;


import com.matrix.focus.util.Validator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ChecklistLog{
  private String checklist_id;
  private String asset_id;
  private String done_date;
  private String status;
  private String remarks;

  private Connection conn;
  
  public ChecklistLog(Connection dbConn){
    conn = dbConn;
  }
  
  public ChecklistLog(Connection dbConn,String checklist_id, String asset_id, String done_date){
    conn = dbConn;
    this.checklist_id = checklist_id;      
    this.asset_id = asset_id;
    this.done_date = done_date;
    if(read()){}
    else throw new NullPointerException();
  }
  
  public void setChecklistID(String checklist_id){
    this.checklist_id = checklist_id;
  }
  public String getChecklistID(){
    return this.checklist_id;
  }
  
  public void setAssetID(String asset_id){
    this.asset_id = asset_id;
  }
  public String getAssetID(){
    return this.asset_id;
  }
  
  public void setDoneDate(String done_date){
    this.done_date = done_date;
  }
  public String getDoneDate(){
    return this.done_date;
  }

  public void setStatus(String status){
    this.status = status;
  }
  public String getStatus(){
    return this.status;
  }
   
  public void setRemarks(String remarks){
    this.remarks = remarks;
  }
  public String getRemarks(){
    return this.remarks;
  }
  
  public String save(){
    PreparedStatement prstmt;
    try{
      prstmt = conn.prepareStatement("INSERT INTO checklist_log(checklist_id,asset_id,done_date,status,remarks) VALUES(?,?,?,?,?)"  );
      prstmt.setString(1,this.checklist_id);
      prstmt.setString(2,this.asset_id);
      prstmt.setString(3,this.done_date);
      prstmt.setString(4,this.status);
      prstmt.setString(5,this.remarks);
      
      if(prstmt.executeUpdate()>0){
        prstmt.close();
        return "OK";
      }
      else{
        prstmt.close();
        return "Checklist Log not saved.";
      }
    }
    catch(SQLException er){
      prstmt = null;
      String msg = "";
      int code = er.getErrorCode();
      if(code==1062){
        msg = "Duplicate entry for Checklist Log.";
      }
      else{
        msg = "SQL Error: "+er.getMessage();
      }
      return msg;
    }
    catch(Exception er){
      er.printStackTrace();
      return "Unknown exception. Please correct it";
    }
  }

  public String update(String oldLogDateTimeVal){
    String sql = "UPDATE checklist_log SET Status=?, Remarks=?, Done_Date=? WHERE Checklist_ID=? AND Asset_ID=? AND Done_Date=? AND Deleted='false' ";
    PreparedStatement prstmt;
    try{
      prstmt = conn.prepareStatement(sql);
      prstmt.setString(1,this.status);
      prstmt.setString(2,this.remarks); 
      prstmt.setString(3,this.done_date);
      prstmt.setString(4,this.checklist_id);
      prstmt.setString(5,this.asset_id);
      prstmt.setString(6,oldLogDateTimeVal);
      
      //System.out.println(prstmt.toString());
      
      if(prstmt.executeUpdate()>0){
        prstmt.close();
        return "OK";
      }
      else{
        prstmt.close();
        return "Checklist Log not found.";
      }
    }
    catch(Exception er){
      er.printStackTrace();
      prstmt = null;
      return "Unknown exception. Please correct it";
    }
  }  
  
  public String delete(){
    /*
    String sql = "UPDATE checklist_log SET Deleted = 'true' WHERE "+
    " Asset_ID =  '" + this.asset_id +"' "+
    " AND Done_Date = '"+ this.done_date + "' ";
    */
    String sql = "DELETE FROM checklist_log WHERE Asset_ID =  '" + this.asset_id +"' "+
                " AND Done_Date = '"+ this.done_date + "' ";
    Statement stmt;
    try{
      stmt = conn.createStatement();
      if(stmt.executeUpdate(sql) >0){
        stmt.close();
        return "OK";
      }
      else{
        stmt.close();
        return "Checklist_ID not found.";
      }
    }
    catch(Exception er){
      er.printStackTrace();
      stmt = null;
      return "Unknown exception. Please correct it";
    }
  } 
  
  private boolean read(){
    String sql = "CALL get_checklist_log('"+this.checklist_id+"','"+this.asset_id+"','"+this.done_date+"')";
    Statement stmt;
    ResultSet rec;
    try{
      stmt = conn.createStatement();
      rec = stmt.executeQuery(sql);
      rec.next();
        this.status = rec.getString("Status");
        this.remarks = rec.getString("Remarks");
        
        rec.close();
        stmt.close();
        return true;
    }
    catch(Exception er){
      rec = null;
      stmt = null;
      er.printStackTrace();
      return false;
    }
  }
}


