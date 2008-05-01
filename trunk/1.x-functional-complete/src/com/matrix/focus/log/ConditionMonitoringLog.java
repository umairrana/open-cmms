/********************************************************
 *  Class   = ConditionMonitoringLog.java
 *  Details = The condition monitoring log entry
 *  Author  = Yasantha
 *  Date    = 2006.07.05
 * ******************************************************/

package com.matrix.focus.log;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ConditionMonitoringLog{
  private String cm_id;
  private String asset_id;
  private String done_date;
  private String reading_value;
  private Connection conn;
  
  public ConditionMonitoringLog(Connection dbConn){
    conn = dbConn;
  }
  
  public ConditionMonitoringLog(Connection dbConn,String cm_id, String asset_id, String done_date){
    conn = dbConn;
    this.cm_id = cm_id;      
    this.asset_id = asset_id;
    this.done_date = done_date;
    if(read()){}
    else throw new NullPointerException();
  }
  
  public void setCMID(String cm_id){
    this.cm_id = cm_id;
  }
  
  public String getCMID(){
    return this.cm_id;
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

  public void setReadingValue(String reading){
    this.reading_value = reading;
  }
  
  public String getReadingValue(){
    return this.reading_value;
  }
   
  public String save(){
    PreparedStatement prstmt;
    try{
      prstmt = conn.prepareStatement("INSERT INTO condition_monitoring_log( Condition_Monitoring_ID, Asset_ID, Done_Date, Reading_Value ) VALUES(?,?,?,?)"  );
      prstmt.setString(1,this.cm_id);
      prstmt.setString(2,this.asset_id);
      prstmt.setString(3,this.done_date);
      prstmt.setString(4,this.reading_value);
      
      if(prstmt.executeUpdate()>0){
        prstmt.close();
        return "OK";
      }
      else{
        prstmt.close();
        return "Condition Monitoring Log not saved.";
      }
    }
    catch(SQLException er){
      prstmt = null;
      String msg = "";
      int code = er.getErrorCode();
      if(code==1062){
        msg = "Duplicate entry for Condition Monitoring Log.";
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
    String sql = "UPDATE condition_monitoring_log SET Reading_Value=?, Done_Date=? WHERE Condition_Monitoring_ID=? AND Asset_ID=? AND Done_Date=? ";
    PreparedStatement prstmt;
    try{
      prstmt = conn.prepareStatement(sql);
      prstmt.setString(1,this.reading_value); 
      prstmt.setString(2,this.done_date);
      prstmt.setString(3,this.cm_id);
      prstmt.setString(4,this.asset_id);
      prstmt.setString(5,oldLogDateTimeVal);
      //System.out.println(prstmt.toString());
      if(prstmt.executeUpdate()>0){
        prstmt.close();
        return "OK";
      }
      else{
        prstmt.close();
        return "Condition Monitoring Log not found.";
      }
    }
    catch(Exception er){
      er.printStackTrace();
      prstmt = null;
      return "Unknown exception. Please correct it";
    }
  }  
  
  public String delete(){
    String sql = "DELETE FROM condition_monitoring_log WHERE Asset_ID =  '" + this.asset_id +"' "+
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
        return "Condition Monitoring Log not found.";
      }
    }
    catch(Exception er){
      er.printStackTrace();
      stmt = null;
      return "Unknown exception. Please correct it";
    }
  } 
  
  private boolean read(){
  /*
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
        Log.log(er);
      return false;
    }
   */
   return false;
  }

}



